; DV3 IDE Format   V300    1998  Tony Tebby

	section dv3

	xdef	id_mformat

	xref	hd_fpart
	xref	hd_fchk
	xref	id_ident
	xref	dv3_slen

	xref	gu_achpp
	xref	gu_rchp

	include 'dev8_keys_err'
	include 'dev8_keys_qlwa'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'
	include 'dev8_dv3_hd_ide_keys'
	include 'dev8_mac_assert'

;+++
; This formats an IDE disk
;
;	d0 cr	format type / error code
;	d1 cr	format dependent flag or zero / good sectors
;	d2  r	total sectors
;	d7 c  p drive ID / number
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return standard
;---
id_mformat
imf.reg reg	d3/d4/d5/a0/a1/a2/a5
	movem.l imf.reg,-(sp)
	jsr	hdl_ckrdy(a3)		 ; check ready
	blt.l	imf_exit

	jsr	hdl_ckwp(a3)
	tst.b	ddf_wprot(a4)		 ; write protected?
	bne.l	imf_ro

	jsr	hd_fchk 		; check OK to format
	bne.l	imf_exit

	move.b	#ddf.dd,ddf_density(a4)  ; set density
	move.b	#2,ddf_slflag(a4)	 ; set sector length flag
	jsr	dv3_slen

	jsr	hd_fpart		 ; find partition
	bne.s	imf_exit

	move.l	d3,ddf_psoff(a4)	 ; set base of partition

	cmp.l	#qwa.pflg,d4		 ; suitable format?
	bne.s	imf_fmtf		 ; ... no

	jsr	id_ident		 ; get the identity information
	bne.s	imf_fmtf

	move.l	d2,d4			 ; partition size
	blt.s	imf_set

	divu	ddf_scyl(a4),d4 	 ; number of cylinders
	move.w	d4,d0			 ; in a long word
	move.l	d0,ddf_atotal(a4)
	beq.s	imf_fmtf		 ; ... none

imf_set
	move.w	ddf_heads(a4),d2	 ; number of heads
	move.l	ddf_atotal(a4),d1	 ; number of cylinders (word in long word)
	move.w	ddf_strk(a4),d3 	 ; number of sectors
	clr.l	-(sp)			 ; no 4096 byte
	clr.l	-(sp)			 ; no 2048 byte
	clr.l	-(sp)			 ; no 1024 byte
	move.l	d3,-(sp)		 ; sectors per track
	clr.l	-(sp)			 ; no 256 byte
	clr.l	-(sp)			 ; no 128 byte
	move.w	d1,-(sp)		 ; cylinders
	move.w	d2,-(sp)		 ; heads

	move.l	sp,a0
	jsr	ddf_fselect(a4) 	 ; select format
	add.w	#7*4,sp
	bne.s	imf_exit		 ; ... oops

	moveq	#ddf.full,d0		 ; ... the only type of format we can do
	sub.l	a0,a0
	jsr	ddf_format(a4)		 ; so do it!
	st	ddf_slbl(a4)		 ; set slave block range

imf_exit
	movem.l (sp)+,imf.reg
	tst.l	d0
	rts

imf_ro
	moveq	#err.rdo,d0
	bra.s	imf_exit

imf_fmtf
	moveq	#err.fmtf,d0
	bra.s	imf_exit

	end
