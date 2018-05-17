* Convert BINARY strings to integer  V2.00     1986  Tony Tebby  QJUMP
*
	section cv
*
	xdef	cr_binib	binary string to byte
	xdef	cr_biniw			 word
	xdef	cr_binil			 long
	xdef	cr_bini

	include dev8_keys_err

*
*	d0  r	0 or err.expr
*	d0 cr	number of characters to convert / error (cr_bini)
*	d1  r	(long) result
*	d7 c  p 0 or end of buffer
*	a0 c  u pointer to buffer containing string
*	a1 c  u pointer to stack to take result (cr_bini.)
*	a6	base address
*
* Binary conversions
*
*	These routines convert the binary string at (a6,a0) to an integer.
*	They stop when enough digits have been read or the end of buffer
*	is reached or a non binary charater is found.
*
cr_binib
	moveq	#8,d0			maximum of eight digits
	bsr.s	cr_bini
	bne.s	crb_rts
	subq.l	#1,a1			put result on stack
	move.b	d1,(a6,a1.l)
	moveq	#0,d0
crb_rts
	rts
*
cr_biniw
	moveq	#16,d0			maximum of 16 digits
	bsr.s	cr_bini
	bne.s	crb_rts
	subq.l	#2,a1			put result on stack
	move.w	d1,(a6,a1.l)
	moveq	#0,d0
	rts
*
cr_binil
	moveq	#32,d0			maximum of 32 digits
	bsr.s	cr_bini
	bne.s	crb_rts
	subq.l	#4,a1
	move.l	d1,(a6,a1.l)		put result on stack
	moveq	#0,d0
	rts
*
* binary conversion
*
cr_bini
	move.l	a0,-(sp)
	move.l	d2,-(sp)		save d2
	moveq	#0,d1			initialise result
	moveq	#0,d2
	bra.s	cbi_eloop
cbi_loop
	cmp.l	a0,d7			at end?
	beq.s	cbi_done		... yes
	move.b	(a6,a0.l),d2		next digit
	sub.b	#'1',d2 		0 or 1
	bhi.s	cbi_done		... no
	addq.b	#1,d2
	blt.s	cbi_done
	add.l	d1,d1			shift up
	add.l	d2,d1
	addq.l	#1,a0			and next character from buffer
cbi_eloop
	dbra	d0,cbi_loop
*
cbi_done
	move.l	(sp)+,d2		restore d2
	cmp.l	(sp)+,a0
	beq.s	cbi_xp
	moveq	#0,d0			no error
	rts

cbi_xp
	moveq	#err.iexp,d0
	rts
	end
