* List extensions      V1.00    1985  Tony Tebby   QJUMP
*
*	EXTRAS [#n]			list extra procedures/functions
*
* 2002-07-04  1.00  Removed TK2 limitation of not showing commands in ROM space.
*
	section exten
*
	xdef	extras
*
	xref	ut_ntnam
	xref	ut_winset
	xref	ut_winchk
	xref	ut_wrta1
	xref	ut_wrtnl
	xref	err_bp
*
	include dev8_sbsext_ext_keys
*
*
extras
	bsr.l	ut_winset	get channel id and set window size
	bne.s	extra_rts	... oops
	cmp.l	a5,a3		there should be no more parameters
	bne.l	err_bp
	move.l	bv_ntbas(a6),a3 set base of name table
extra_loop
	moveq	#$e,d1		is entry 8 or 9?
	and.b	(a6,a3.l),d1
	subq.b	#8,d1
	bne.s	extra_next	... no, try next
;	 cmp.l	 #$c000,4(a6,a3.l) ... yes, is address in base rom?
;	 blt.s	 extra_next	 ... yes, try next
	move.l	a3,d1
	bsr.l	ut_ntnam	get name from name table (onto ri stack)
	bsr.l	ut_winchk	check if window full
	bsr.l	ut_wrta1	and write it out
	bne.s	extra_rts	... oops
	bsr.l	ut_wrtnl	with newline
	bne.s	extra_rts	... oops
extra_next
	addq.l	#8,a3		take next entry
	cmp.l	bv_ntp(a6),a3	off end?
	blt.s	extra_loop	... no
extra_rts
	rts
	end
