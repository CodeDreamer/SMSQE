; Gold / Supergold card determine display hardware type  1997	Tony Tebby

	section init

	xdef	gl_disptype

	include 'dev8_keys_aurora'
	include 'dev8_keys_sys'

;+++
; Determines the display hardware type
;
;	d6 c  u $x0000x00+machine  +  $00   QL
;				      $C0   LCD
;				      $A0   Aurora
;	status returned 0
;---
gl_disptype
	moveq	#0,d0
	rts

	end
