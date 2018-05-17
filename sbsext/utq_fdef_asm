* Set address of file system defaults	V0.1    Tony Tebby   QJUMP
*
	section utils
*
	xdef	ut_fdef
*
	include dev8_sbsext_ext_keys
*
*	d2 c p	pointer to default pointer
*	a4   r	pointer to default string
*
ut_fdef
	movem.l d1/d2/a0,-(sp)
	moveq	#mt.inf,d0
	trap	#1
	movem.l (sp)+,d1/d2
	move.l	sv_fdefo(a0,d2.w),a4
	move.l	(sp)+,a0
	rts
	end
