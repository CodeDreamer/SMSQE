; SBSEXT_TK2_BASE - TK2 loadable version		    2017 Marcel Kilgus

	section base

	xdef	base

	xref	banner
	xref	init

	include 'dev8_keys_qlv'

base
	suba.l	a0,a0
	lea	banner(pc),a1
	move.w	ut.wtext,a2
	jsr	(a2)
	bra.s	init

	end
