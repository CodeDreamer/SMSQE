* Check for room on RI stack  V0.4     1985  Tony Tebby  QJUMP
*
	section utils
*
	xdef	ut_ckri6
	xdef	ut_chkri
	xdef	ut_setri
*
	include dev8_sbsext_ext_keys
*
*	d0   s
*	d1 c  p space required
*	d2-d7 p
*	a0    p
*	a1  r	RI stack pointer
*	a2-a5 p
*
ut_ckri6
	movem.l d1/d2/d3,-(sp)
	moveq	#6,d1
	bra.s	utc_do
ut_chkri
	movem.l d1/d2/d3,-(sp)
utc_do
	move.w	bv..chrix,a1
	jsr	(a1)
	movem.l (sp)+,d1/d2/d3		and restore them
ut_setri
	move.l	bv_rip(a6),a1
	rts
	end
