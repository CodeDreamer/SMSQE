; DV3 QPC Hard Disk Format		      1993	Tony Tebby
;
; 2020-04-07  1.01  Changed for new ddf_mname definition (length word) (MK)
;		    Allow name to be set at format time: win1_10_name (MK)

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
	include 'dev8_smsq_qpc_keys'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'
	include 'dev8_mac_assert'

;+++
; This routine formats a medium
;
;	d0 cr	format type / error code
;	d1 cr	format dependent flag or zero / good sectors
;	d7 c  p drive ID / number
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return standard
;---
hd_mformat
hmf.reg reg	d3/d4/d5/d7/a0/a1/a2/a5
hmf.d7	equ	12
	movem.l hmf.reg,-(sp)

hmf_wstart
	jsr	hd_hold
	bne.s	hmf_wstart

	move.b	#ddf.dd,ddf_density(a4)  ; set density
	move.b	#2,ddf_slflag(a4)	 ; set sector length flag
	jsr	dv3_slen

	clr.l	ddf_psoff(a4)		 ; set base of partition

	lea	ddf_mname(a4),a0
	move.w	(a0)+,d1
	lea	(a0,d1.w),a1		 ; end of buffer
	move.l	a1,d7			 ; for cv_decil
	jsr	cv_decil		 ; size of disk required
	bne.l	hmf_inam
	moveq	#11,d0			 ; convert megabytes to sectors
	asl.l	d0,d1
	bvs.l	hmf_inam
	ble.l	hmf_inam

	move.l	hmf.d7(sp),d7		 ; restore drive number
	cmp.l	a0,a1			 ; at end of string?
	beq.s	hmf_def_name		 ; use default

	cmp.b	#'_',(a0)		 ; length followed by underscore?
	bne.s	hmf_inam		 ; no, illegal name

	addq.w	#1,a0			 ; skip underscore
	moveq	#0,d0			 ; char counter
	lea	ddf_mname+2(a4),a2

hmf_copy_rest
	cmp.l	a0,a1
	beq.s	hmf_name_copied
	move.b	(a0)+,(a2)+
	addq.w	#1,d0
	bra.s	hmf_copy_rest

hmf_name_copied
	lea	ddf_mname(a4),a0
	move.w	d0,(a0) 		 ; set size
	bra.s	hmf_name_set

hmf_def_name
	lea	ddf_mname(a4),a0
	move.w	#4,(a0)+		 ; no name given, insert default
	move.l	#'WIN0',(a0)
	add.b	d7,3(a0)

hmf_name_set
	jsr	hd_fchk 		 ; check ok to format
	bne.s	hmf_exit
 
	dc.w	qpc.hdfmt		 ; d1 = sectors, d7 = drive
	dc.w	$4AFB			 ; for security

	move.l	d1,d3			 ; number of sectors available
	beq.s	hmf_fmtf		 ; ... none

	clr.l	-(sp)			 ; no 4096 byte
	clr.l	-(sp)			 ; no 2048 byte
	clr.l	-(sp)			 ; no 1024 byte
	move.l	d3,-(sp)		 ; sectors per track !!
	clr.l	-(sp)			 ; no 256 byte
	clr.l	-(sp)			 ; no 128 byte
	clr.l	-(sp)			 ; no cylinders / heads

	move.l	sp,a0
	jsr	ddf_fselect(a4) 	 ; select format
	add.w	#7*4,sp
	bne.s	hmf_exit		 ; ... oops

	moveq	#ddf.full,d0		 ; ... the only type of format we can do
	sub.l	a0,a0
	jsr	ddf_format(a4)		 ; so do it!
	st	ddf_slbl(a4)		 ; set slave block range

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
