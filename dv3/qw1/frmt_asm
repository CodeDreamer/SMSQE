; DV3 MSDOS Format	       V3.01	       2017 W. Lenerz
; based on
; DV3 MSDOS Format	       V3.00	       1993 Tony Tebby

	section dv3

	xdef	qw1_frmt

	include 'dev8_dv3_keys'
	include 'dev8_keys_err'

;+++
; DV3 QLW1 Format  - not implemented
;
;	d0 cr	format type / error code
;	d7 c  p drive ID / number
;	a1 c  p pointer to name
;	a3 c  p pointer to linkage
;	a4 c  p pointer to drive definition
;
;	status return standard
;
;---
qw1_frmt
	moveq	#err.nimp,d0
	rts

	end
