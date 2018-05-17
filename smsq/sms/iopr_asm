; IO Priority Setting	  V2.00    1986  Tony Tebby

	section sms

	xdef	sms_iopr

	xref	sms_rtok

	include 'dev8_keys_sys'

;+++
; IO Priority Setting OS entry
;---
sms_iopr
	move.w	d2,sys_iopr(a6)  ; set priority
	jmp	sms_rtok	 ; always return OK
	end
