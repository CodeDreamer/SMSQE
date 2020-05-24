; DV3 JAVA HDD Check Write Protect  V1.00 @ W. Lenerz 2014
; based on
; DV3 QXL Hard Disk Check Write Protect  V3.00	 1993	 Tony Tebby

	section dv3

	xdef	hd_ckwp

	include 'dev8_keys_java'
	include 'dev8_dv3_keys'


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
	moveq	#jta.ckwp,d0			; is disk write protected?
	dc.w	jva.trpa			; return NZ if write proted
	sne	ddf_wprot(a4)
hdwp_exit
	rts

	end
