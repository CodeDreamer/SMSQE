; QL-SD RESPR version initialisation				 2018 M. Kilgus

	section header

	xdef	nd_init

	xref	banner
	xref	init

	include 'dev8_keys_qlv'

base
	suba.l	a0,a0
	lea	banner(pc),a1		; Output version string
	move.w	ut.wtext,a2
	jsr	(a2)
	moveq	#1,d7			; start by RESPR
	bra.s	init

; Doesn't make sense for RESPR version to have network code, provide stub
nd_init
	rts

	end
