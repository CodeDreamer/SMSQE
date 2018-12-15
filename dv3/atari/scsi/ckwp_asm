; DV3 SCSI Hard Disk Check Write Protect   1993     Tony Tebby

	section dv3

	xdef	sc_ckwp

	xref	sc_inqry
	xref	sc_rsns

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
sc_ckwp
	move.l	a2,-(sp)
	lea	hdl_remd(a3),a2

	tst.b	hdl_wprt-hdl_remd-1(a2,d7.w)
	smi	ddf_wprot(a4)		 ; set write protect
	bne.s	scwp_exit		 ; logical protection, do not check phys

	moveq	#0,d0
	move.b	hdl_targ-hdl_remd-1(a2,d7.w),d0 ; target number
	add.w	d0,a2
	tst.b	(a2)			 ; removable?
	beq.s	scwp_exit		 ; ... no
	bmi.s	scwp_chk		 ; ... yes

	jsr	sc_inqry		 ; inquire
	beq.s	scwp_remv		 ; ... ok
	jsr	sc_inqry		 ; ... inquire again
	bne.s	scwp_exit
scwp_remv
	tst.b	hdl_buff+scci_remv(a3)
	smi	(a2)			 ; set removeable
	smi	ddf_remv(a4)		 ; set flag
	bpl.s	scwp_exit

scwp_chk
	jsr	sc_rsns 		 ; read the sense data
	bne.s	scwp_exit
	tst.b	hdl_buff+sccm_wprt(a3)
	smi	ddf_wprot(a4)
scwp_exit
	move.l	(sp)+,a2
	rts

	end
