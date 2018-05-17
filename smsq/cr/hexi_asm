* Convert HEX strings to integer  V2.00    1986  Tony Tebby  QJUMP
*
	section cv
*
	xdef	cr_hexib	hex string to	 byte
	xdef	cr_hexiw			 word
	xdef	cr_hexil			 long
	xdef	cr_hexi

	include dev8_keys_err

*
*	d0  r	0 OK ERR.EXPR no digits
*	d0 cr	number of characters to convert / error (cr_hexi)
*	d1  r	(long) result
*	d7 c  p 0 or end of buffer
*	a0 c  u pointer to buffer containing string
*	a1 c  u pointer to stack to take result (cr_hexi.)
*	a6	base address
*
* Hexadecimal conversions
*
*	These routines convert the string at (a6,a0) to an integer.
*	They stop when a non-hexadecimal digit is encountered, enough
*	digits have been read or the end of buffer is reached.
*
cr_hexib
	moveq	#2,d0			maximum of two digits
	bsr.s	cr_hexi
	bne.s	crh_rts
	subq.l	#1,a1			put result on stack
	move.b	d1,(a6,a1.l)
	moveq	#0,d0
crh_rts
	rts
*
cr_hexiw
	moveq	#4,d0			maximum of four digits
	bsr.s	cr_hexi
	bne.s	crh_rts
	subq.l	#2,a1			put result on stack
	move.w	d1,(a6,a1.l)
	moveq	#0,d0
	rts
*
cr_hexil
	moveq	#8,d0			maximum of eight digits
	bsr.s	cr_hexi
	bne.s	crh_rts
	subq.l	#4,a1
	move.l	d1,(a6,a1.l)		put result on stack
	moveq	#0,d0
	rts
*
* hexadecimal conversion
*
cr_hexi
	move.l	a0,-(sp)		save a0
	move.l	d2,-(sp)		save d2
	moveq	#0,d1			initialise result
	bra.s	chi_eloop
chi_loop
	cmp.l	a0,d7			at end?
	beq.s	chi_done		... yes
	moveq	#-$30,d2		digits start at '0'
	add.b	(a6,a0.l),d2		next digit
	blt.s	chi_done		...<0
	cmp.b	#9,d2			>9?
	bls.s	chi_add 		... no, add this digit
	bclr	#5,d2			'a'='A'=$11
	cmp.b	#$10,d2 		<A?
	ble.s	chi_done		... yes, done
	subq.b	#7,d2			'a'='A'=$a
	cmp.b	#$10,d2 		>$10?
	bhs.s	chi_done		... yes
chi_add
	lsl.l	#4,d1			shift up
	or.b	d2,d1			put in new digit
	addq.l	#1,a0			and next character from buffer
chi_eloop
	dbra	d0,chi_loop
*
chi_done
	move.l	(sp)+,d2		restore d2
	cmp.l	(sp)+,a0		any digits?
	beq.s	chi_xp
	moveq	#0,d0			no error
	rts

chi_xp
	moveq	#err.iexp,d0
	rts
	end
