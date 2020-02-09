; FREE_FMFM AND ALFM keywords 1.00 (c) W. Lenerz 2017
; 2018-12-10  1.01  make sure mem request is even (wl) - thanks to Martyn Hill.

	section procs

	xdef	alfm
	xdef	free_fmem

	xref	ut_rtfd1
	xref	ut_gxin1

	include dev8_keys_q68
	include dev8_keys_err
	include dev8_keys_sbasic

free_fmem
	lea	ut_rtfd1,a2		; float & return d1
	move.l	a2,-(a7)
ffmem2
	move.l	#q68_sramb,a2		; pointer to first free mem in fast ram
	move.l	#q68_sramt,d1		; top of ram
	move.l	(a2),d3
	sub.l	d3,d1
	subq.l	#4,d1			; remaining free mem
	bge.s	ok
	clr.l	d1			; nothing remains
ok	rts


alfm	jsr	ut_gxin1
	bne.s	ok
	move.w	(a6,a1.l),d0
	addq.l	#1,d0
	bclr	#0,d0			; make sure it's even
	addq.l	#2,sb_arthp(a6)
	bsr.s	ffmem2			; check how much mem is free
	ble.s	oom
	sub.l	d0,d1			; we need this much mem
	blt.s	oom			; but there's not enough remaining
	move.l	d3,d1			; pointer to free mem
	add.l	d0,d3			; new Sram top
	move.l	d3,(a2)
	clr.l	d0
	jmp	ut_rtfd1

oom	moveq	#err.imem,d0
	rts

	end
