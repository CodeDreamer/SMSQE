; DV3 java HD format   1.01 @ W. Lenerz 2020
;
; 2020-04-08  1.01  Changed for new ddf_mname definition (length word) (wl)
; based on
; DV3 QXL Floppy Disk Format (actually HD format)   1993     Tony Tebby

	section dv3

	xdef	hd_mformat

	xref	hd_hold
	xref	hd_release
	xref	hd_fchk

	xref	dv3_slen

	xref	gu_achp0
	xref	gu_rchp
	xref	cv_decil

	include 'dev8_keys_err'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'
	include 'dev8_mac_assert'
	include 'dev8_keys_java'

;+++
; This routine formats a medium
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
hd_mformat
hmf.reg reg	d3/d4/d5/a0/a1/a2/a5
	movem.l hmf.reg,-(sp)

hmf_wstart
	jsr	hd_hold
	bne.s	hmf_wstart
	move.b	#ddf.dd,ddf_density(a4) ; set density
	moveq	#jta.qslen,d0
	dc.w	jva.trpa		; query sector length, return in D5
	move.b	d5,ddf_slflag(a4)	; set sector length flag
	jsr	dv3_slen

	clr.l	ddf_psoff(a4)		; set base of partition

	lea	ddf_mname(a4),a0

;	 jsr	 hd_fchk		 ; check format permission (asks for user input in channel 0)
;	bne.s	hmf_exit

	lea	ddf_mname(a4),a0
	move.w	#4,(a0)
	move.l	#'WIN0',2(a0)		  ; blat name
	add.b	d7,5(a0)


	moveq	#jta.frmt,d0		; format drive
	dc.w	jva.trpa		; do it (create container of D1 megabytes)
					; nbr of sectors returned in d2, also sets Z flag
	bne.s	hmf_fmtf		; oops


	clr.l	-(sp)			; no 4096 byte
	clr.l	-(sp)			; no 2048 byte
	clr.l	-(sp)			; no 1024 byte
	move.l	d2,-(sp)		; sectors per track !!
	clr.l	-(sp)			; no 256 byte
	clr.l	-(sp)			; no 128 byte
	clr.l	-(sp)			; no cylinders / heads


	move.l	sp,a0
	jsr	ddf_fselect(a4) 	; select format (gets to dv3_qlf_fsel)
	add.w	#7*4,sp
	bne.s	hmf_exit		; ... oops

	moveq	#ddf.full,d0		; ... the only type of format we can do
	sub.l	a0,a0
	jsr	ddf_format(a4)		; so do it!  gets to qlf_ftwa_asm
	st	ddf_slbl(a4)		; set slave block range

hmf_exit
	jsr	hd_release
	movem.l (sp)+,hmf.reg
	tst.l	d0
	rts

hmf_fmtf
	moveq	#err.fmtf,d0
	bra.s	hmf_exit

hmf_inam
	moveq	#err.inam,d0
	bra.s	hmf_exit

	end
