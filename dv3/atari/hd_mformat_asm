; DV3 Atari Winchester Format	V2.00	 1990	 Tony Tebby QJUMP

	section dv3

	xdef	ahd_mformat

	xref	hd_fchk
	xref	hd_fpart
	xref	dv3_slen

	xref	gu_achpp
	xref	gu_rchp

	include 'dev8_keys_err'
	include 'dev8_keys_atari'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'
	include 'dev8_mac_assert'

;+++
; This formats a partition on an Atari Winchester disk
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
ahd_mformat
hmf.reg reg	d3/d4/d5/a0/a1/a2/a5
	movem.l hmf.reg,-(sp)
	jsr	hdl_ckrdy(a3)		 ; check ready
	blt.s	hmf_exit

	jsr	hdl_ckwp(a3)
	tst.b	ddf_wprot(a4)		 ; write protected?
	bne.s	hmf_ro

	move.b	#ddf.dd,ddf_density(a4)  ; set density
	move.b	#2,ddf_slflag(a4)	 ; set sector length flag
	jsr	dv3_slen

	jsr	hd_fpart		 ; find partition
	bne.s	hmf_exit

	move.l	d3,ddf_psoff(a4)	 ; set base of partition

	cmp.l	#art.qflg,d4		 ; suitable format?
	bne.s	hmf_fmtf		 ; ... no

	move.l	d2,d4			 ; partition size
	blt.s	hmf_fmtf		 ; no partition!!

	jsr	hd_fchk 		 ; check OK to format
	bne.s	hmf_exit

	clr.l	-(sp)			 ; no 4096 byte
	clr.l	-(sp)			 ; no 2048 byte
	clr.l	-(sp)			 ; no 1024 byte
	move.l	d4,-(sp)		 ; drive size in 512 byte
	clr.l	-(sp)			 ; no 256 byte
	clr.l	-(sp)			 ; no 128 byte
	clr.l	-(sp)			 ; no tracks or heads

	move.l	sp,a0
	jsr	ddf_fselect(a4) 	 ; select format
	add.w	#7*4,sp
	bne.s	hmf_exit		 ; ... oops

	moveq	#ddf.full,d0		 ; ... the only type of format we can do
	sub.l	a0,a0
	jsr	ddf_format(a4)		 ; so do it!
	st	ddf_slbl(a4)		 ; set slave block range

hmf_exit
	movem.l (sp)+,hmf.reg
	tst.l	d0
	rts

hmf_ro
	moveq	#err.rdo,d0
	bra.s	hmf_exit

hmf_fmtf
	moveq	#err.fmtf,d0
	bra.s	hmf_exit

	end
