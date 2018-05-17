; DV3 QL5B Sector Allocate, Locate and Truncate  V3.00		 1992 Tony Tebby

	section dv3

	xdef	qlf_sa5b
	xdef	qlf_sl5b
	xdef	qlf_st5b
	xdef	qlf_idfr
	xdef	qlf_idtr

	xref	dv3_sbid

	include 'dev8_keys_ql5b'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_qlf_keys'
	include 'dev8_keys_err'

;+++
; DV3 QL5A move file from root
; NB the file buffers  must be flushed before calling this routine!
;
;	d0  r	error code
;	d6 cr p old file ID / new file ID
;	d7 c  p drive ID / number
;	a3 c  p pointer to linkage
;	a4 c  p pointer to drive definition
;
;	status return positive (OK) or standard error
;
;---
qlf_idfr
q5si.reg reg	d1/d2/d3/d4/d5/a1/a2
	movem.l q5si.reg,-(sp)
	moveq	#0,d0
	moveq	#0,d2
	bsr.l	q5sl_find		 ; find first group
	blt.s	q5si_exit
	move.l	d0,d1			 ; new file ID
	bset	#11,d1
	addq.w	#1,d1			 ; is group adjusted a bit
	bra.s	q5si_do

;+++
; DV3 QL5A move file to root
; NB the file buffers  must be flushed before calling this routine!
;
;	d0  r	error code
;	d1 c  p new file ID
;	d6 cr p old file ID / new file ID
;	d7 c  p drive ID / number
;	a3 c  p pointer to linkage
;	a4 c  p pointer to drive definition
;
;	status return positive (OK) or standard error
;
;---
qlf_idtr
	movem.l q5si.reg,-(sp)
	moveq	#0,d2
	bsr.l	q5s_split		 ; split old ID

q5si_do
	jsr	dv3_sbid		 ; change IDs in slave blocks

	exg	d1,d6			 ; d6 is now file ID
	subq.w	#1,d1			 ; old internal
	move.w	d6,d0
	subq.w	#1,d0			 ; ... new internal
	eor.w	d0,d1			 ; and d1 is difference
	move.w	d1,d2
	lsr.w	#4,d1			 ; top two nibbles difference in d1
	lsl.w	#4,d2			 ; bottom nibble in topn of d2

	lea	q5a_gmap+qdf_map(a4),a1  ; bottom of range
	move.l	qdf_mtop(a4),a2 	 ; top of range

q5si_loop
	cmp.b	(a1),d3 		 ; msb file?
	bne.s	q5si_lend		 ; ... mismatch
	moveq	#$fffffff0,d0
	and.b	1(a1),d0
	cmp.b	d0,d4			 ; bottom of file?
	bne.s	q5si_lend		 ; ... mismatch
	eor.b	d1,(a1) 		 ; ... match, change ID
	eor.b	d2,1(a1)
q5si_lend
	addq.l	#3,a1
	cmp.l	a2,a1
	blt.s	q5si_loop		 ; carry on

	move.w	qdf_msect(a4),d0
	lea	qdf_mupd(a4),a1
q5si_mloop
	st	(a1)+			 ; mark all map sectors updated
	subq.w	#1,d0
	bgt.s	q5si_mloop

	st	ddf_mupd(a4)

	moveq	#0,d0
q5si_exit
	movem.l (sp)+,q5si.reg
	rts

;+++
; DV3 QL5B allocate new group of sectors
;
;	d0 cr	known logical drive group / logical drive group
;	d1 c  p file group of known drive group
;	d2 c  p next file group
;	d6 c  u file ID
;	d7 c  p drive ID / number
;	a3 c  p pointer to linkage
;	a4 c  p pointer to drive definition
;
;	status return positive (OK) or standard error
;
;---
qlf_sa5b
q5sa.reg reg	d2/d3/d4/d5/a1/a2
	movem.l q5sa.reg,-(sp)
	subq.w	#1,d2
	blt.s	q5sa_new		 ; new file

	bsr.l	q5sl_find		 ; find previous group
	blt.s	q5sa_exit
	addq.b	#1,d5			 ; now allocate this group
	bcc.s	q5sa_v
	addq.b	#1,d4
q5sa_v
	bsr.s	q5sa_vg 		 ; looking from d0
	ble.s	q5sa_drfl		 ; ... not found
q5sa_ok
	bsr.s	q5sa_set
	tst.l	d0
q5sa_exit
	movem.l (sp)+,q5sa.reg
	rts

q5sa_drfl
q5sn_drfl
	moveq	#err.drfl,d0
	bra.s	q5sa_exit		 ; drive full

;+++
; DV3 QL5B allocate first group of new file
;
;	d0  r	logical drive group
;	d6 c  u file ID if in root directory (if not in root directory = 0)
;	d7 c  p drive ID / number
;	a3 c  p pointer to linkage
;	a4 c  p pointer to drive definition
;
;	status return positive (OK) or standard error
;---
;;;qlf_sn5b
;;;	   movem.l q5sa.reg,-(sp)
q5sa_new
	moveq	#0,d0
	move.w	q5a_scyl+qdf_map(a4),d0  ; keep clear of track 0
	divu	q5a_allc+qdf_map(a4),d0
	bsr.s	q5sa_vg 		 ; look for vacant sector
	ble.s	q5sn_drfl		 ; ... not found
q5sn_ok
	tst.w	d6			 ; file ID given?
	bne.s	q5sn_sid		 ; ... yes
	move.w	d0,d6			 ; use first group number as file ID
	bset	#11,d6
	addq.w	#1,d6			 ; adjusted a bit
q5sn_sid
	moveq	#0,d2			 ; block zero of file
	bsr.l	q5s_split		 ; split form of group ID
	bsr.s	q5sa_set		 ; set it

	tst.l	d0
	movem.l (sp)+,q5sa.reg
	rts


; look for vacant group

q5sa_vg
	bsr.s	q5sa_vgx		 ; look for vacant group from d0
	bgt.s	q5sa_rts		 ; .. ok

q5sa_vg0
	moveq	#0,d0			 ; try from start
q5sa_vgx
	lea	q5a_gmap+qdf_map(a4),a2  ; base of map
	add.w	d0,a2			 ; + group offset
	add.w	d0,a2
	add.w	d0,a2

q5sa_vg2
	move.l	qdf_mtop(a4),a1 	 ; top of map
	moveq	#$fffffffd,d0
q5sa_vloop
	cmp.b	(a2),d0 		 ; free?
	beq.s	q5sa_group		 ; ... yes, set group in d0
	addq.l	#3,a2
	cmp.l	a1,a2
	blt.s	q5sa_vloop
	clr.w	d0			 ; no empty groups
q5sa_rts
	rts

; set a group in the map

q5sa_set
	move.b	d3,(a2)+		 ; set file/block in group map
	move.b	d4,(a2)+
	move.b	d5,(a2)+

	move.w	q5a_allc+qdf_map(a4),d2
	sub.w	d2,q5a_free+qdf_map(a4)  ; one fewer free allocation blocks
	subq.w	#1,ddf_afree+2(a4)

; Set map updated

q5s_mupdt
	st	qdf_mupd(a4)		 ; sector zero updated
	move.l	a2,d2
	sub.l	a4,d2
	sub.w	#qdf_map+1,d2
	divu	ddf_slen(a4),d2
	add.w	#qdf_mupd,d2
	st	(a4,d2.w)		 ; and this one
	swap	d2
	subq.w	#2,d2			 ; previous one as well?
	bge.s	q5sm_mup		 ; ... no
	swap	d2
	st	-1(a4,d2.w)		 ; ... yes
q5sm_mup
	st	ddf_mupd(a4)		 ; map updated
	rts

q5sa_group
q5sl_group
	lea	q5a_gmap+qdf_map(a4),a1
	move.l	a2,d0
	sub.l	a1,d0
	divu	#3,d0			 ; group number (msw must be zero)
	rts


;+++
; DV3 QL5B locate group of sectors
;
;	d0 cr	known logical drive group / logical drive group
;	d1 c  p file group of known drive group
;	d2 c  p file group required
;	d6 c  p file ID
;	d7 c  p drive ID / number
;	a3 c  p pointer to linkage
;	a4 c  p pointer to drive definition
;
;	status return positive (OK) or standard error
;---
qlf_sl5b
q5sl.reg reg	d2/d3/d4/d5/a1/a2
	movem.l q5sl.reg,-(sp)
	bsr.s	q5sl_find
	movem.l (sp)+,q5sl.reg
	rts

; routine to find a group in the map

q5sl_find
	bsr.s	q5s_split		 ; split group ID
	tst.w	d0			 ; any known group?
	beq.s	q5sl_start
	cmp.w	d1,d2			 ; req sector before or after known?
	beq.s	q5sl_sd0		 ; its right here!
q5sl_start
	lea	q5a_gmap+qdf_map(a4),a2
	add.w	d0,a2
	add.w	d0,a2
	add.w	d0,a2			 ; start search here
	blt.s	q5sl_bef		 ; before

	addq.l	#3,a2			 ; skip this one
	move.l	a2,d0
	move.l	qdf_mtop(a4),a1 	 ; top of range

q5sl_lu
	cmp.b	1(a2),d4		 ; top of group / bottom of file
	bne.s	q5sl_lue		 ; ... mismatch
	cmp.b	2(a2),d5		 ; lsb group matches?
	bne.s	q5sl_lue		 ; ... mismatch
	cmp.b	(a2),d3 		 ; and msb file?
	beq.s	q5sl_group
q5sl_lue
	addq.l	#3,a2
	cmp.l	a1,a2
	blt.s	q5sl_lu 		 ; carry on
	cmp.l	d0,a2			 ; back at the start?
	beq.s	q5sl_mchk		 ; ... yes
	lea	q5a_gmap+qdf_map(a4),a2  ; ... yes, start again at the bottom
	move.l	d0,a1
	bra.s	q5sl_lu

q5sl_bef
	move.l	a2,d0
	lea	q5a_gmap+qdf_map(a4),a1  ; bottom of range

q5sl_ld
	subq.l	#3,a2
	cmp.b	1(a2),d4		 ; top of group / bottom of file
	bne.s	q5sl_lde		 ; ... mismatch
	cmp.b	2(a2),d5		 ; lsb group matches?
	bne.s	q5sl_lde		 ; ... mismatch
	cmp.b	(a2),d3 		 ; and msb file?
	beq.s	q5sl_group
q5sl_lde
	cmp.l	a1,a2
	bgt.s	q5sl_ld 		 ; carry on
	cmp.l	d0,a2			 ; back at the start?
	beq.s	q5sl_mchk		 ; ... no
	move.l	qdf_mtop(a4),a2 	 ; ... yes, start again at the top
	move.l	d0,a1
	bra.s	q5sl_ld

q5sl_mchk
	moveq	#err.mchk,d0		 ; oops, not found
q5sl_sd0
	tst.l	d0
	rts

; routine to set split form of file ID (d6) / group (d2) in d3/d4/d5

q5s_split
	move.w	d6,d3			 ; file ID
	subq.w	#1,d3			 ; -1
	move.w	d3,d4
	lsr.w	#4,d3			 ; top two nibbles of ID in d3

	swap	d4
	move.w	d2,d4
	lsl.w	#4,d4
	lsl.l	#4,d4
	swap	d4			 ; lower nibble ID, top nibble group

	move.w	d2,d5			 ; bottom two nibbles of group
	rts

;+++
; DV3 QL5B truncate sector allocation
;
;	d0 cr	known logical drive group / error status
;	d1 c  p file group of known drive group
;	d2 c  p first file group to free
;	d6 c  p file ID
;	d7 c  p drive ID / number
;	a3 c  p pointer to linkage
;	a4 c  p pointer to drive definition
;
;	status return standard
;---
qlf_st5b
q5st.reg reg	d1/d2/d6/a1/a2
	movem.l q5st.reg,-(sp)
	subq.w	#1,d6			 ; file ID in map
	move.w	d2,d1			 ; truncate to here

	lea	q5a_gmap+qdf_map(a4),a2  ; bottom of sector map
	move.l	qdf_mtop(a4),a1 	 ; and top of range

q5st_loop
	moveq	#0,d0
	move.b	(a2),d0 		 ; file msb
	lsl.w	#8,d0
	move.b	1(a2),d0		 ; file lsn / block msn
	ror.l	#4,d0
	cmp.w	d0,d6			 ; is this the right file number?
	bne.s	q5st_next		 ; ... no
	swap	d0
	lsr.w	#4,d0
	move.b	2(a2),d0
	cmp.w	d1,d0			 ; is the block off the end of file?
	blt.s	q5st_next		 ; ... no
	move.b	#$fd,(a2)		 ; ... yes, free the sector
	move.w	q5a_allc+qdf_map(a4),d0
	add.w	d0,q5a_free+qdf_map(a4)  ; ... one more free
	add.w	#1,ddf_afree+2(a4)
	bsr	q5s_mupdt		 ; map updated
q5st_next
	addq.l	#3,a2			 ; next sector
	cmp.l	a1,a2
	blt.s	q5st_loop

	moveq	#0,d0
	movem.l (sp)+,q5st.reg
	rts

	end
