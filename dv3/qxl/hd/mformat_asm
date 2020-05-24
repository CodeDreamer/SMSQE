; DV3 QXL HD Disk Format		  1993     Tony Tebby
;
; 2020-04-07  1.01  Changed for new ddf_mname definition (length word) (MK)

	section dv3

	xdef	hd_mformat

	xref	hd_hold
	xref	hd_release
	xref	hd_fchk

	xref	dv3_slen
	xref	qxl_mess_add

	xref	gu_achp0
	xref	gu_rchp
	xref	cv_decil

	include 'dev8_keys_err'
	include 'dev8_smsq_qxl_keys'
	include 'dev8_smsq_qxl_comm_keys'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'
	include 'dev8_mac_assert'

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

	move.b	#ddf.dd,ddf_density(a4)  ; set density
	move.b	#2,ddf_slflag(a4)	 ; set sector length flag
	jsr	dv3_slen

	clr.l	ddf_psoff(a4)		 ; set base of partition

	lea	ddf_mname+2(a4),a0
	jsr	cv_decil		 ; size of disk required
	bne.l	hmf_inam
	moveq	#11,d0			 ; convert megabytes to sectors
	asl.l	d0,d1
	bvs.l	hmf_inam
	ble.l	hmf_inam

	jsr	hd_fchk 		 ; check ok to format
	bne.s	hmf_exit

	lea	ddf_mname(a4),a0
	move.w	#4,(a0)+
	move.l	#'WIN0',(a0)		 ; blat name
	add.b	d7,3(a0)

	move.l	qxl_message,a1
	lea	qxl_ms_phys(a1),a1	 ; message key

	move.b	#qxm.fdriv,(a1)+	 ; ... message key
	move.b	d7,(a1) 		 ; win drive
	add.b	#$82,(a1)+		 ; is a file
	move.w	#1,(a1)+		 ; only one message at a time
	move.l	d1,(a1)+		 ; number of sectors
	clr.l	(a1)			 ; no ID
	subq.l	#8,a1
	move.w	#$c,-(a1)		 ; message length
	jsr	qxl_mess_add		 ; add the message
hmf_wait_done
; blat #$aa
	tst.w	(a1)			 ; wait for reply (flagged by -ve len)
	bpl.s	hmf_wait_done
; blat #$55

	tst.b	qxm_err-qxl_ms_len(a1)	 ; error?
	bne.s	hmf_fmtf

	move.l	qxm_ngood-qxl_ms_len(a1),d3 ; nr sectors

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
