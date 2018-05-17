; Move area (generalised PAN/SCROLL)  Aurora version V1.00    Tony Tebby
;							       Marcel Kilgus
; v. 1.01 just calls java trap

	section con

	xdef	cn_mblock
	xdef	pt_mblock
	include 'dev8_keys_java'
;
;	d1 c  s size of section to move
;	d2 c  s old origin in source area
;	d3 c  s new origin in destination area
;	d4    s scratch
;	d5    s scratch
;	d6/d7	preserved
;	a0/a1	preserved
;	a2 c	row increment of source area
;	a3 c	row increment of destination area
;	a4 c	base address of source area
;	a5 c	base address of destination area
;	a6/a7	preserved
;
pt_mblock
cn_mblock

	moveq	#jt5.move,d0
	dc.w	jva.trp5
;	rts				; the RTS is handled directly in java

	end
