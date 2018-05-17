* Put string onto RI stack and return  V0.0    1985  Tony Tebby   QJUMP
*
	section utils
*
	xdef	ut_rtstr		 ; return string
	xdef	ut_rtsst		 ; return sub-string
*
	xref	ut_chkri		check for room
	xref	ut_retst		return string
*
*	d1 c	sub-string length
*	a4 c	pointer to string or substring
*
ut_rtstr
	move.w	(a4)+,d1		actual length
ut_rtsst
	move.w	d1,-(sp)
	addq.w	#3,d1
	and.l	#$7ffe,d1
*
	bsr.s	ut_chkri		check the RI stack for room
*
	add.l	d1,a4			starting from the top
	subq.l	#2,a4
	subq.w	#2,d1
	beq.s	cpy_slen
cpy_str
	subq.l	#1,a1 
	move.b	-(a4),(a6,a1.l) 	copy the string
	subq.l	#1,d1
	bgt.s	cpy_str
cpy_slen
	subq.w	#2,a1
	move.w	(sp)+,(a6,a1.l)
* 
*	ut_retst is here		!!!
*
	end
