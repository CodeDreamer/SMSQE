; DV3 QPC Start / Stop Drive   V3.00	 1998	   Tony Tebby

	section dv3

	xdef	hd_ststp		; start / stop drive

	include 'dev8_keys_err'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'
	include 'dev8_dv3_hd_ide_keys'

;+++
; This routine starts or stops a drive
;
;	d0 cr start (-1), stop ($ffff) rundown (0+ minutes)/ error
;	d7 c  p drive ID / number
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0, ERR.NC or ERR.MCHK
;
;---
hd_ststp
	moveq	#0,d0
	rts

	end
