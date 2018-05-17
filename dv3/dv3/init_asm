; DV3 Initialisation	V3.00		      1992 Tony Tebby


	section dv3

	xdef	dv3_init
	xdef	dv3_name

	xdef	msd.hdrs
msd.hdrs equ	'_'	; host directory separator for MSDOS format

	xref.l	dv3_vers

	xref	gu_thini

	include 'dev8_dv3_keys'
	include 'dev8_keys_thg'
	include 'dev8_keys_68000'

dv3_dthg dc.l	 dv3_top
	 dc.l	 0
	 dc.l	 dv3_vers
dv3_name dc.w	 3,'DV3'

;+++
; The DV3 initialisation routine.
;
;	d0  r	error code
;
;---
dv3_init
d3i.reg reg	a1
	move.l	a1,-(sp)
	lea	dv3_dthg,a1	    ; Thing definition
	jsr	gu_thini
	bne.s	d3i_exit
	move.l	a1,th_thing(a1)     ; thing is linkage
	moveq	#0,d0
d3i_exit
	move.l	(sp)+,a1
	rts
	end
