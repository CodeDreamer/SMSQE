; DV3 QL5B Format Load File    V3.00    1994	Tony Tebby QJUMP

	section dv3

	xdef	qlf_ld5b

	xref	qlf_ls5b

	xref	dv3_sload
	xref	dv3_sbloc

	include 'dev8_keys_ql5b'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_qlf_keys'
	include 'dev8_keys_hdr'
	include 'dev8_keys_err'

;+++
; DV3 QL5B format load file.
; It checks whether the first sector of a file is in slave blocks, and if
; it is, it does ordinary IO.
;
;	d1 cr	amount read so far
;	d2 c  p amount to load
;	d3   s
;	d4   s
;	d5   s
;	d6 c  p file id
;	d7 c  p drive ID / number
;	a0 c  p channel block (end address of load)
;	a1 c  u pointer to memory to load into
;	a2   s	start address of load
;	a3 c  p pointer to linkage block
;	a4 c  p pointer to physical definition
;	a5    p
;---
qlf_ld5b
	tst.l	d1			 ; any loaded already?
	bne.l	dv3_sload		 ; ... yes, standard load

;*** d1 must be zero for code below

	move.l	d3c_feof(a0),d3 	 ; get length
	moveq	#hdr.len,d0
	sub.l	d0,d3			 ; real length!!
	cmp.l	#1024,d3		 ; at least 1024 bytes?
	blt.l	dv3_sload		 ; ... no

	tst.b	ddf_slld(a4)		 ; slaved loading preferred?
	beq.s	ql5_load		 ; ... no, direct load

	moveq	#0,d5			 ; first sector
	lea	d3c_csb(a0),a2
	jsr	dv3_sbloc		 ; locate slave block
	seq	ddf_slld(a4)		 ; slaved loading preferred
	beq.l	dv3_sload		 ; do standard load

ql5_load
ql5.rege reg	d3/d6/a0/a5	 ; length on entry
ql5.regx reg	d1/d6/a0/a5	 ; is d1 on exit
	movem.l ql5.rege,-(sp)

ql5_flush
	moveq	#-1,d0
	jsr	ddl_fflush(a3)
	addq.l	#-err.nc,d0		 ; not complete?
	beq.s	ql5_flush		 ; ... yes, wait
	subq.l	#-err.nc,d0
	bne.l	ql5_exit		 ; ... oops

	subq.w	#1,d6			 ; file ID is one different in map
	move.l	a1,a2			 ; start address of load
	lea	(a1,d3.l),a0		 ; end address of load
	lea	qdf_map(a4),a5		 ; map
	moveq	#0,d5
	move.w	ddf_slen(a4),d5 	 ; sector length

	moveq	#0,d3			 ; start looking at track 0

ql5_tr_loop
	moveq	#0,d4			 ; side 0

ql5_si_loop
	clr.w	d4			 ; start at physical sector 0 (offset)

ql5_se_loop
	move.l	d4,d0
	bpl.s	ql5_ps_set		 ; side 0
	tas	d0			 ; side 1
ql5_ps_set
	moveq	#0,d1			 ; find logical sector
	move.w	q5a_scyl(a5),d1
	subq.w	#1,d1
ql5_ps_look
	cmp.b	q5a_lgph(a5,d1.w),d0	 ; the right sector?
	dbeq	d1,ql5_ps_look

	move.w	d3,d0			 ; track
	mulu	q5a_scyl(a5),d0 	 ; * nr of sectors (msw end d0=0)
	add.w	d1,d0			 ; logical sector on drive

	move.l	d0,d2
	divu	q5a_allc(a5),d2 	 ; posn in map (msw is posn in group)
	move.w	d2,d1
	add.w	d2,d2
	add.w	d1,d2			 ; address in map

	lea	q5a_gmap(a5,d2.w),a1
	move.b	(a1)+,d1		 ; get 12 bits of map
	lsl.w	#8,d1
	move.b	(a1)+,d1
	ror.l	#4,d1
	cmp.w	d6,d1			 ; is the file the same?
	bne.s	ql5_se_next		 ; ... no
	swap	d1			 ; ... yes
	lsr.w	#4,d1
	move.b	(a1)+,d1		 ; get group number
	mulu	q5a_allc(a5),d1 	 ; as sector number
	swap	d2
	add.w	d2,d1			 ; + sector within group

	mulu	d5,d1			 ; gives address from base of load
	bne.s	ql5_sa1 		 ; not the first sector

	lea	(a2),a1 		 ; load into start
	moveq	#hdr.len,d1		 ; offset of start in sector
	bra.s	ql5_ckend

ql5_sa1
	lea	-hdr.len(a2,d1.l),a1	 ; set start address (less header)
	moveq	#0,d1			 ; and no offset
ql5_ckend
	cmp.l	a0,a1			 ; is start of sector off end of file?
	bge.s	ql5_se_next		 ; ... yes
	move.l	a0,d2			 ; end of file
	sub.l	a1,d2			 ; amount to read
	add.l	d1,d2			 ; corrected
	cmp.l	d5,d2			 ; at least a sector?
	blt.s	ql5_buff		 ; ... no, buffer it

	move.l	d5,d2
	tst.w	d1			 ; part sector?
	beq.s	ql5_rdo 		 ; ... no

ql5_buff
	move.l	a1,a5
	move.l	ddl_bfbas(a3),a1	 ; ... yes, use buffer
	bsr.s	ql5_rsect		 ; read sector
	bne.s	ql5_exit		 ; ... oops

	add.w	d1,a1			 ; start copying from here
	sub.w	d1,d2
	bra.s	ql5_plend
ql5_ploop
	move.b	(a1)+,(a5)+

ql5_plend
	dbra	d2,ql5_ploop

	lea	qdf_map(a4),a5		 ; restore map
	bra.s	ql5_se_next

ql5_rsect
	jsr	qlf_ls5b		 ; get physical sector number
	move.l	d2,-(sp)
	moveq	#1,d2
	jsr	ddl_rsect(a3)		 ; read sector
	move.l	(sp)+,d2
	tst.l	d0
	rts

ql5_rdo
	bsr.s	ql5_rsect		 ; read sector
	bne.s	ql5_exit		 ; ... oops

ql5_se_next
	addq.w	#1,d4			 ; next physical sector
	cmp.w	q5a_strk(a5),d4 	 ; off end?
	blt.l	ql5_se_loop		 ; ... no

	bset	#31,d4			 ; side 1
	bne.s	ql5_tr_next		 ; ... already
	cmp.w	q5a_scyl(a5),d4 	 ; is it single sided?
	bne.l	ql5_si_loop		 ; ... no

ql5_tr_next
	addq.l	#1,d3			 ; next track (cylinder)
	cmp.w	q5a_trak(a5),d3 	 ; off end?
	blt.l	ql5_tr_loop		 ; ... no

	move.l	a0,a1			 ; end pointer
	moveq	#0,d0
ql5_exit
	movem.l (sp)+,ql5.regx
	rts
	end
