* Get channel id or string or name  V0.3     1985  Tony Tebby	QJUMP
*
	section utils
*
	xdef	ut_fname
*
	xref	ut_gtnam
	xref	ut_chget
*
*	d0  r	<0 error, =0 filename, >0 #n
*
ut_fname
	tst.b	1(a6,a3.l)	is it a hash
	bge.l	ut_gtnam	... no, get string
	bsr.l	ut_chget	get channel
	subq.l	#8,a3		backspace parm pointer
	bne.s	utf_rts 	... oops
	sub.l	a1,a1		set no name pointer
	moveq	#1,d0		set return positive
utf_rts
	rts
	end
