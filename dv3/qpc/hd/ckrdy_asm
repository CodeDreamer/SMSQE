; DV3 QPC Check Ready Drive   V3.02     1998	  Tony Tebby
;
; 2014-08-19  3.01  Check whether drive definition is actually valid (MK)
; 2020-02-23  3.02  Update ddf_remv in case of auto_detect (MK)

	section dv3

	xdef	hd_ckrdy

	include 'dev8_keys_err'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'
	include 'dev8_dv3_hd_ide_keys'
	include 'dev8_smsq_qpc_keys'

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
hdk.reg reg	d2/a3

hd_ckrdy
	movem.l hdk.reg,-(sp)
	tst.b	ddf_mstat(a4)		; drive undefined?
	beq.s	hdrm_ok 		; then we do nothing
	lea	hdl_remd(a3),a2
	move.b	-1(a2,d7.w),d2
	tst.b	d2
	smi	ddf_remv(a4)		; set removeable flag
	beq.s	hdrm_ok 		; OK, not removeable
	subq.b	#1,d2
	bne.s	hdrm_rm 		; Removeable

	dc.w	qpc.hdchk		; Auto detect
	move.b	d2,-1(a2,d7.w)		; New removeable status
	tst.b	d2
	smi	ddf_remv(a4)		; set removeable flag
	beq.s	hdrm_ok 		; No removeable
hdrm_rm
	move.b	#ddf.mchg,ddf_mstat(a4) ; Always assume changed
hdrm_ok
	movem.l (sp)+,hdk.reg
	moveq	#0,d0
	rts

	end
