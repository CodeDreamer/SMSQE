* Hex/binary conversions  V0.6	  1984/1985  Tony Tebby  QJUMP
*
*	HEX$ (value,number of bits)	value to hex string
*	BIN$ (value,number of bits)	value to binary string
*	HEX (string)			hex string to value
*	BIN (string)			binary string to value
*
	section exten
*
	xdef	hex$
	xdef	bin$
	xdef	hex
	xdef	bin
*
	xref	ut_gxli2		get two long integers
	xref	ut_gxst1		get one string
	xref	ut_remst		remove string
	xref	ut_chkri		check for room on RI stack
	xref	ut_rtfd1		return floating point of d1
	xref	ut_retst		return a string
	xref	err_bp
*
	include dev8_sbsext_ext_keys
*
hex$
	moveq	#2,d5		four bits per digit
	move.w	cn..itohl,a4
	bra.s	hex_bin_
bin$
	moveq	#0,d5		one bit per digit
	move.w	cn..itobl,a4
hex_bin_
	moveq	#$22,d1 	reserve enough stack space for binary string
	bsr.l	ut_chkri
*
	bsr.l	ut_gxli2	get exactly 2 long integers
	bne.s	conv_rts
	move.l	4(a6,a1.l),d4	get number of bits required
	ble.s	conv_bp 	... what none!!
	moveq	#$20,d1
	cmp.w	d1,d4		we can't do more than 32
	bhi.s	conv_bp 	(unsigned greater than)
	move.l	(a6,a1.l),4(a6,a1.l) put number at top of stack
*
	addq.l	#4,a1		now there is only one long word there
	lea	4(a1),a0	set pointer to top of stack
	lsr.w	d5,d1		and allow room for 32 bit conversion
	sub.w	d1,a0
	jsr	(a4)
*
	subq.w	#1,d4		round up the number of digits required
	lsr.w	d5,d4
	addq.w	#1,d4
	btst	#0,d4		now the problems: is it an odd length string?
	beq.s	conv_set_len	... no
	move.w	d4,d1		... yes, so we have to move it
	suba.w	d1,a1		move the pointer
conv_up_loop
	move.b	(a6,a1.l),-1(a6,a1.l) and move a digit
	addq.l	#1,a1
	dbra	d1,conv_up_loop this actually moves d1+1 characters
	subq.l	#2,a1		so add 1 to a1 for this, and 1 'cos it's moved
conv_set_len
	suba.w	d4,a1		move a1 to start of string
	subq.l	#2,a1		... and a word further on
	move.w	d4,(a6,a1.l)	then put the string length in
	bra.l	ut_retst	and return it
conv_bp
	moveq	#err.bp,d0
conv_rts
	rts
*
hex
	lea	hex_long(pc),a4 convert from hex
	bra.s	hex_bin
bin
	lea	bin_long(pc),a4 convert from binary
hex_bin
	bsr.l	ut_gxst1	get exactly one string
	bne.s	conv_rts
	jsr	ut_remst	... remove it
	move.w	(a6,a1.l),d0	get length of string
	move.w	d0,d7		save it
	addq.l	#2,a1		skip it
	moveq	#0,d1		initialise value
	jsr	(a4)		convert it
	bne.s	conv_rts	... oops
conv_fp
	jmp	ut_rtfd1	float d1 onto stack and return
*
hex_loop
	moveq	#-$30,d2	digits start at '0'=$30
	add.b	(a6,a1.l),d2	get digit
	addq.l	#1,a1
	blt.s	conv_xp 	less than 0, bum
	cmp.b	#9,d2		>9?
	ble.s	hex_set 	... no, ok
	bclr	#5,d2		'a'='a'=$11
	cmp.b	#$10,d2 	<a?
	ble.s	conv_xp 	... yes, bum
	sub.b	#7,d2		'a'='a'=$a
	cmp.b	#$10,d2 	<16?
	bhs.s	conv_xp 	... no, bum
hex_set
	lsl.l	#4,d1		put digit in
	or.b	d2,d1
hex_long
	dbra	d0,hex_loop
conv_ok
	moveq	#0,d0
	rts
conv_xp
	moveq	#err.xp,d0
	rts
*
bin_loop
	move.b	(a6,a1.l),d2	get digit
	addq.l	#1,a1
	roxr.b	#1,d2		get lsbit
	roxl.l	#1,d1		put it in
bin_long
	dbra	d0,bin_loop
	bra.s	conv_ok
	end
