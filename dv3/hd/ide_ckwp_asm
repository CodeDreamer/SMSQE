; DV3 IDE Check Write Protect  V3.00   1998	 Tony Tebby

	section dv3

	xdef	id_ckwp

	include 'dev8_keys_err'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'
	include 'dev8_dv3_hd_ide_keys'

;+++
; This routine reads the sense information and checks the WP flag
;
;	d7 c  p drive ID / number
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return undefined
;
;---
id_ckwp
idwp.reg reg	a1/a2
	movem.l idwp.reg,-(sp)

	sf	ddf_wprot(a4)		 ; set write protect ... dragon

	movem.l  (sp)+,idwp.reg
	rts

	end
