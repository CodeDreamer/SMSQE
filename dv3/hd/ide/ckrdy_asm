; DV3 IDE Check Ready Drive   V3.00     1998	  Tony Tebby

	section dv3

	xdef	id_ckrdy

	xref	id_cmdw

	include 'dev8_keys_err'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'
	include 'dev8_dv3_hd_ide_keys'

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
id_ckrdy
  ; save a5!!
	moveq	#0,d0
	rts

	end
