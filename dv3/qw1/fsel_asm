; DV3 MSDOS Format Select	     V3.01	     1993 Tony Tebby

; adapted for qubide  (wl) 2017 3.01

	section dv3

	xdef	qw1_fsel

	include 'dev8_keys_err'

;+++
; DV3  Format Select
;
;	d0 cr	format type / error code
;	d7 c  p drive ID / number
;	a0 c  p pointer to physical format table
;	a3 c  p pointer to linkage
;	a4 c  p pointer to drive definition
;
;	status return standard
;
;---
qw1_fsel
	moveq	#err.nimp,d0
	rts

	end
