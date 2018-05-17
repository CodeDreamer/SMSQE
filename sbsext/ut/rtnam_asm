* Move name to RI stack (and return it)  V0.0	 1985	 Tony Tebby  QJUMP  
*
	section utils
*
	xdef	ut_rtnam		put name on stack and return it
	xdef	ut_nmtos		put name on stack
*
	xref	ut_chkri		check for room
	xref	ut_retst		return string
*
*	d3 c s	pointer to name wrt a6 (ut_nmtos)
*	a1  r	pointer to RI stack
*	a4 c  p pointer to name wrt a6 (ut_rtnam)
*
ut_rtnam
	move.l	a4,d3			set pointer
	bsr.s	ut_nmtos		get name
	bra.s	ut_retst		and return string
*
* put name on RI stack
*
ut_nmtos
	moveq	#3,d1			get the length of the name as a long word
	add.b	(a6,d3.l),d1
	bclr	#0,d1			rounded up (+2)
	bsr.s	ut_chkri
*
	add.l	d1,d3			move to end of string (ish)
ut_nam_loop
	subq.l	#1,d3			and copy it down
	subq.l	#1,a1
	move.b	-1(a6,d3.l),(a6,a1.l)
	subq.w	#1,d1
	bgt.s	ut_nam_loop
	clr.b	(a6,a1.l)
	rts
	end
