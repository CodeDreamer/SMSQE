* Check for room in buffer  V0.4     1985  Tony Tebby	QJUMP
*
	section utils
*
	xdef	ut_chkbf
*
	include dev8_sbsext_ext_keys
*
*	d0   s
*	d1 c  p space required
*	d2-d7 p
*	a0    p
*	a1  r	buffer pointer
*	a2-a5 p
*
ut_chkbf
	movem.l d1/d2/d3,-(sp)
	move.w	bv..chrix,a1
	jsr	bvo_chbf(a1)		check buffer!!
	movem.l (sp)+,d1/d2/d3
	move.l	bv_bfp(a6),a1
	rts
	end
