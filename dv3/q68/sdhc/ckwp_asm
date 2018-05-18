; DV3 for Q68 : SDHC Check Write Protect  V1.00 @ W. Lenerz 2016

; based on
; DV3 QXL Hard Disk Check Write Protect  V3.00	 1993	 Tony Tebby

	section dv3

	xdef	hd_ckwp

	include 'dev8_keys_java'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'


;+++
; Check write protect (logical protection only : card is always writable
;
;
;	a3 c  p pointer to linkage block
;	a4 c  p pointer to drive definition
;
;	all registers preserved
;
;	status arbitrary
;
;---
hd_ckwp
	move.l	a1,-(a7)
	lea	-1(a3,d7.w),a1
	tst.b	hdl_wprt(a1)
	smi	ddf_wprot(a4)		 ; set write protect
hdwp_exit
	move.l	(a7)+,a1
	rts

	end
