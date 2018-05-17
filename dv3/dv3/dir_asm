; DV3 Directory IO	V3.00		      1992 Tony Tebby

	section dv3

	xdef	dv3_dir

	xref	dv3_rhdr
	xref	dv3_minf
	xref	dv3_xinf

	xref	dv3_iqdt

	xref	cv_dosrt

	include 'dev8_dv3_keys'
	include 'dev8_keys_hdr'
	include 'dev8_keys_dos'
	include 'dev8_keys_err'
	include 'dev8_keys_qdos_io'
	include 'dev8_keys_k'
;+++
; Directory IO routines
;
;	d0 c	operation
;	d1 cr	amount transferred / byte / position etc.
;	d2 c	buffer size
;	d5   s	file pointer
;	d6 c  p drive number / sector length
;	a0 c	channel base address
;	a1 cr	buffer address
;	a2   s
;	a3 c  p linkage block address
;	a4 c  p pointer to physical definition
;	a5   s
;---
dv3_dir
	subq.b	#iob.flin,d0		 ; is is fetch line?
	ble.l	ddr_fetch		 ; ... yes or odd
	subq.b	#iob.fmul-iob.flin,d0	 ; is it fetch multiple?
	beq.l	ddr_read
	sub.b	#iof.posa-iob.fmul,d0	 ; is it position?
	beq.s	ddr_posab
	subq.b	#iof.posr-iof.posa,d0	 ; relative?
	beq.s	ddr_posre
	subq.b	#iof.minf-iof.posr,d0	 ; medium info?
	beq.l	dv3_minf
	subq.b	#iof.rhdr-iof.minf,d0	 ; read header?
	beq.s	ddr_rhdr
	subq.b	#iof.xinf-iof.rhdr,d0	 ; extended medium info?
	beq.l	dv3_xinf
dv3_ipar
	moveq	#err.ipar,d0		 ; ... no
	rts

; directory positioning

ddr_posre
	add.l	d3c_fpos(a0),d1 	 ; absolute position
	move.l	d1,d3c_fpos(a0)
	bvc.s	ddp_ckef
	bra.s	ddp_eof

ddr_posab
	move.l	d1,d3c_fpos(a0) 	 ; absolute position

ddp_ckef
	blt.s	ddp_begf		 ; beginning of file
	cmp.l	d3c_feof(a0),d1 	 ; end of file?
	bge.s	ddp_eof
	moveq	#0,d0
	rts

ddp_eof
	move.l	d3c_feof(a0),d1 	 ; end of file
	bra.s	ddp_seof

ddp_begf
	moveq	#0,d1			 ; beginning of file
ddp_seof
	move.l	d1,d3c_fpos(a0) 	 ; set end of file
	moveq	#err.eof,d0
	rts

; directory read header

ddr_rhdr
	tst.w	d3c_name(a0)		 ; any name?
	beq.s	ddr_rhroot		 ; ... no, it is root
	tst.l	d3c_denum(a0)		 ; which directory entry is in here?
	bmi.s	ddr_rhnroot		 ; my header
	moveq	#-1,d0			 ; always scrumple
	jsr	ddf_drefile(a4) 	 ; fetch directory entry
	blt.s	ddr_rts
ddr_rhnroot
	subq.w	#1,d3c_name(a0) 	 ; shorten name
	bsr.s	ddr_rhdo		 ; and read header (in any format)
	addq.w	#1,d3c_name(a0) 	 ; and restore it
	tst.l	d0
ddr_rts
	rts

ddr_rhroot
	lea	d3c_dren(a0),a5 	 ; clear out entry
	moveq	#d3c.dren/4-1,d0
ddr_rhrc
	clr.l	(a5)+
	dbra	d0,ddr_rhrc

	move.l	ddf_rdlen(a4),d3c_feof(a0); length of root
	st	d3c_type(a0)		 ; and type
	move.b	#dos.subd,d3c_dsattr(a0)

ddr_rhdo
	move.l	d3c_feof(a0),d0 	 ; genuine length
	jmp	dv3_rhdr


ddr_fetch
	beq.s	ddr_line		 ; fetch line
	move.l	d3c_fsect(a0),-(sp)	 ; current sector updated automatically
	move.w	d0,-(sp)		 ; save key
	moveq	#1,d2
	moveq	#0,d1
	move.l	sp,a1			 ; put byte here
	bsr.s	ddr_read		 ; read one byte
	move.b	(sp),d1 		 ; return byte
	move.w	(sp)+,d2		 ; key
	move.l	(sp)+,a2
	tst.l	d0
	bne.s	ddr_frts		 ; oops
	addq.b	#iob.flin-iob.fbyt,d2	 ; fetch byte
	beq.s	ddr_frts
	subq.l	#1,d3c_fpos(a0) 	 ; backspace pointer
	move.l	a2,d3c_fsect(a0)	 ; current sector restored
	moveq	#0,d0
ddr_frts
	rts

; read line - $40 bytes at a time

ddr_line
	moveq	#hdr.len-1,d0
	and.l	d3c_fpos(a0),d0 	 ; position in directory entry
	moveq	#hdr.len,d3
	sub.w	d0,d3			 ; amount to fetch
	cmp.w	d3,d2			 ; buffer is big enough
	ble.s	ddrl_bf 		 ; ... no

	move.l	d3,d2			 ; ... yes, fetch rest of entry
	bsr.s	ddr_read
	bne.s	ddrl_rts		 ; ... oops
	move.b	#k.nl,(a1)+		 ; add newline to end
	addq.w	#1,d1
	rts

ddrl_bf
	bsr.s	ddr_read		 ; read a complete buffer
	bne.s	ddrl_rts
	moveq	#err.bffl,d0		 ; if no other error, it is buffer full
ddrl_rts
	rts

; read directory entries

ddr_read
	move.l	d3c_fpos(a0),d5 	 ; file position

	move.w	d2,d4
	beq.l	ddr_exok
	sub.w	d1,d4			 ; amount left to fetch

ddr_bloop
	moveq	#hdr.len-1,d3
	and.w	d5,d3			 ; position in directory entry
	move.l	d5,d2
	lsr.l	#6,d2			 ; entry number
	cmp.l	d3c_fenum(a0),d2	 ; ... entry already formatted?
	beq.l	ddr_unpack
	cmp.l	d3c_denum(a0),d2	 ; ... entry already loaded?
	beq.s	ddr_pack		 ; ... yes, pack it up

	tst.w	d3			 ; start at beginning of entry?
	bne.s	ddr_rodds		 ; ... no, odd bits
ddr_eloop
	cmp.l	d3c_feof(a0),d5 	 ; end of file
	bhs.s	ddr_eof
	cmp.w	#hdr.len,d4		 ; a whole entry?
	bne.s	ddr_rodds		 ; ... no, odd bits


	moveq	#ddf.qdos,d0		 ; QDOS format
	jsr	ddf_drent(a4)		 ; directory entry
	blt.l	ddr_exit		 ; ... oops
	bgt.s	ddr_pack		 ; pack up standard entry

	moveq	#hdr.len,d2
	add.w	d2,a1			 ; move pointer on
	add.l	d2,d5			 ; file position
	add.w	d2,d1
	sub.w	d2,d4			 ; amount left to fetch
	ble.l	ddr_exit

	move.l	d5,d2
	lsr.l	#6,d2
	bra.s	ddr_eloop		 ; get another one

ddr_eof
	moveq	#err.eof,d0
	bra.l	ddr_exit

ddr_rodds
	move.l	a1,a2
	moveq	#ddf.qdos,d0		 ; read QDOS entry
	lea	d3c_fentry(a0),a1	 ; into formatted entry
	jsr	ddf_drent(a4)
	exg	a1,a2
	blt.s	ddr_eof
	bgt.s	ddr_pack
	move.l	d2,d3c_fenum(a0)	 ; packed up
	bra.l	ddr_unpa2		 ; ... so unpack it

ddr_pack
	move.l	d2,d3c_fenum(a0)	 ; will be packed up
	lea	d3c_fentry(a0),a2
	moveq	#hdr.len/4-1,d2
	moveq	#0,d0
ddr_pzero
	move.l	d0,(a2)+		 ; empty directory entry
	dbra	d2,ddr_pzero

	tst.w	d3c_fnam(a0)		 ; any name?
	beq.l	ddr_unpack

	movem.l d1/a1,-(sp)		 ; save what and where to unpack

	lea	d3c_fentry(a0),a2
	moveq	#0,d1
	moveq	#hdr.len,d0		 ; QDOS programs expect $40 bytes extra
	sub.l	ddf_fhlen(a4),d0	 ; but it is already longer
	add.l	d3c_flen(a0),d0
	move.l	d0,(a2)+

	move.b	d3c_dsattr(a0),(a2)+
	move.b	d3c_type(a0),(a2)+
	move.l	d3c_data(a0),(a2)+
	move.l	d3c_xtra(a0),(a2)+

	move.w	d3c_fnam(a0),d0
	add.w	d3c_name(a0),d0 	 ; full name length
	cmp.w	#d3c.qnml,d0
	ble.s	ddr_nlset
	moveq	#d3c.qnml,d0		 ; limited to QL length
ddr_nlset
	move.w	d0,(a2)+

	lea	dv3_iqdt,a5		 ; internal to QDOS translate
	lea	d3c_name(a0),a1 	 ; directory name
	move.w	(a1)+,d2		 ; length
	beq.s	ddr_fncopy
	sub.w	d2,d0			 ; amount left to fetch after
	bge.s	ddr_dncle		 ; ... some
	add.w	d0,d2			 ; cannot copy that much
	moveq	#0,d0
	bra.s	ddr_dncle
ddr_dncloop
	move.b	(a1)+,d1
	move.b	(a5,d1.w),(a2)+ 	 ; translated name characters
ddr_dncle
	dbra	d2,ddr_dncloop

ddr_fncopy
	lea	d3c_fnam+2(a0),a1	 ; file name
	bra.s	ddr_fncle
ddr_fncloop
	move.b	(a1)+,d1
	move.b	(a5,d1.w),(a2)+ 	 ; translated name characters
ddr_fncle
	dbra	d0,ddr_fncloop

	lea	d3c_fentry+hdr_date(a0),a2 ; the final odd bits
	move.l	d3c_updd(a0),d1
	bsr.s	ddr_date

	move.w	d3c_vers(a0),(a2)+
	move.w	d3c_flid(a0),(a2)+

	move.l	d3c_arcd(a0),d1
	bsr.s	ddr_date
	movem.l (sp)+,d1/a1

; directory entry is packed up

ddr_unpack
	lea	d3c_fentry(a0),a2
ddr_unpa2
	add.w	d3,a2			 ; position in entry
	moveq	#hdr.len,d2		 ; length of formatted entry
	sub.w	d3,d2			 ; amount left
	cmp.w	d2,d4			 ; all?
	bge.s	ddr_upset		 ; ... yes
	move.w	d4,d2			 ; no, just to end
ddr_upset
	move.w	d2,d0
	bra.s	ddr_uple
ddr_uploop
	move.b	(a2)+,(a1)+		 ; unpack
ddr_uple
	dbra	d2,ddr_uploop

	add.w	d0,d1			 ; update bits
	add.l	d0,d5			 ; update pointer
	sub.w	d0,d4			 ; another bit
	bgt	ddr_bloop		 ; ... yes
ddr_exok
	moveq	#0,d0
ddr_exit
	move.l	d5,d3c_fpos(a0) 	 ; set file position
	rts

ddr_date
	tst.b	ddf_dosd(a4)		 ; DOS date?
	beq.s	ddr_dd1 		 ; ... no, simple
	jsr	cv_dosrt		 ; convert
ddr_dd1
	move.l	d1,(a2)+
	rts

	end
