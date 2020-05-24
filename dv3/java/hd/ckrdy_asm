; DV3 Check Ready Drive   1.00 W. Lenerz 2020
;
;

	section dv3

	xdef	hd_ckrdy

	include 'dev8_keys_err'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'
	include 'dev8_dv3_hd_ide_keys'
	include 'dev8_keys_java'

;+++
; This routine checks if the drive is ready and if the medium has changed
;
;	d7 c  p drive ID / number
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0, ERR.NC or ERR.MCHK
;
;---
hdk.reg reg	d1

hd_ckrdy
	movem.l hdk.reg,-(sp)

	moveq	#jta.ckry,d0
	dc.w	jva.trpA
	moveq	#jta.gtrm,d0
	dc.w	jva.trpA

	lea	hdl_remd(a3),a2
	move.b	d1,-1(a2,d7.w)
	beq	hdrm_ok
	sf	ddf_remv(a4)

hdrm_rm
	move.b	#ddf.mchg,ddf_mstat(a4) ; Always assume changed
hdrm_ok
	movem.l (sp)+,hdk.reg
	moveq	#0,d0
	rts

	end
