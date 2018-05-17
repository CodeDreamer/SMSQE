; DV3 QPC Hard Disk Check Write Protect  V3.00	 1993	 Tony Tebby

	section dv3

	xdef	hd_ckwp

	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'
	include 'dev8_smsq_qpc_keys'

;+++
; Check write protect, density: does dothing
;
;
;	a3 c  p pointer to linkage block
;	a4 c  p pointer to drive definition
;
;	all registers except d0 preserved
;
;	status arbitrary
;
;---
hd_ckwp
	move.l	a1,-(a7)
	lea	-1(a3,d7.w),a1
	tst.b	hdl_wprt(a1)
	smi	ddf_wprot(a4)		 ; set write protect
	bne.s	hdwp_exit		 ; logical protection, do not check phys

	dc.w	qpc.chkwp
	sne	ddf_wprot(a4)
hdwp_exit
	move.l	(a7)+,a1
	rts
	end
