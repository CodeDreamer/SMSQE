; DV3 ACSI Hard Disk Check Write Protect   1993     Tony Tebby

	section dv3

	xdef	ac_ckwp

	xref	ac_rsns
	xref	ac_inqry

	include 'dev8_keys_err'
	include 'dev8_keys_scsi'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'

;+++
; This routine reads the sense information and checks the WP flag
;
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return undefined
;
;---
ac_ckwp
acwp.reg reg	a1/a2
	movem.l acwp.reg,-(sp)
	lea	hdl_remd(a3),a2

	tst.b	hdl_wprt-hdl_remd-1(a2,d7.w)
	smi	ddf_wprot(a4)		 ; set write protect
	bne.s	acwp_exit		 ; logical protection, do not check phys

	moveq	#0,d0
	move.b	hdl_targ-hdl_remd-1(a2,d7.w),d0 ; target number
	add.w	d0,a2
	tst.b	(a2)			 ; removable?
	beq.s	acwp_exit		 ; ... no
	bmi.s	acwp_chk		 ; ... yes

	jsr	ac_inqry		 ; inquire
	beq.s	acwp_remv		 ; ... ok
	jsr	ac_inqry		 ; ... inquire again
	bne.s	acwp_exit
acwp_remv
	tst.b	scci_remv(a1)
	smi	(a2)			 ; set removeable
	smi	ddf_remv(a4)		 ; set flag
	bpl.s	acwp_exit

acwp_chk
	jsr	ac_rsns 		 ; read the sense data
	bne.s	acwp_exit
	tst.b	sccm_wprt(a1)
	smi	ddf_wprot(a4)
acwp_exit
	movem.l  (sp)+,acwp.reg
	rts

	end
