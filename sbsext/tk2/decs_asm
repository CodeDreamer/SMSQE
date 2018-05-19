* Decimal conversions  V1.0     1984/1985  Tony Tebby	 QJUMP
*
*	FDEC$ (value,field,ndp) 	fixed point decimal conversion
*	IDEC$ (value,field,ndp) 	scaled integer decimal conversion
*	CDEC$ (value,field,ndp) 	ditto, with commas
*
*	FEXP$ (value,field,ndp) 	exponent conversion
*
	section exten
*
	xdef	fdec$
	xdef	cdec$
	xdef	idec$
	xdef	fexp$
	xdef	idec_do
	xdef	fdec_do
	xdef	fexp_do
*
	xref	dec_stuff
	xref	dec_cfill
	xref	dec_ov
	xref	ut_chkri
	xref	ut_gtfp1
	xref	ut_gtint
	xref	ut_retst
	xref	dec_point		decimal point character
*
	include dev8_sbsext_ext_keys
*
* Setup decimal function args
*
dec_set
	moveq	#6*6,d1 	reserve space for 6 floating point nrs
	bsr.l	ut_chkri
*
	moveq	#$18,d0 	are there just three parameters?
	add.l	a3,d0
	sub.l	a5,d0
	bne.s	dec_bp_4	... no
	bsr.l	ut_gtfp1	get one floating point
	bne.s	dec_rt_4	... oops
	addq.l	#8,a3		now get
	bsr.l	ut_gtint	integers
	bne.s	dec_rt_4	... oops
	move.w	(a6,a1.l),d6	number of digits
	cmp.w	#30,d6		field of 30
	bhi.s	dec_bp_4
	moveq	#0,d5
	move.w	2(a6,a1.l),d5	number of dp
	blt.s	dec_bp_4
*
	addq.l	#4,a1
*
	move.l	a1,a4
	moveq	#-5,d0		find the arithmetic stack pointer to
	add.w	d6,d0
	bclr	#0,d0		take a string (rounded up in length)
	sub.w	d0,a4		less 6 bytes
*
	sub.l	a5,a5		no floating currency
*
	moveq	#' ',d3 	fill with spaces
	moveq	#0,d4		set sign and dp flags
	move.b	dec_point(pc),d4
	moveq	#0,d0
	rts
*
dec_bp_4
	moveq	#err.bp,d0
dec_rt_4
	addq.l	#4,sp
	rts
	page
*
cdec$
	moveq	#4,d7		comma every 3 digits
	bra.s	idec_1
idec$
	moveq	#$7f,d7 	no commas
idec_1
	bsr.s	dec_set 	set up call values
	bsr.l	dec_iconv	convert to nearest long integer (and to ASCII)
	bra.s	dec_setr	and set return
*
fdec$
	moveq	#$7f,d7 	set no comma
	bsr.s	dec_set 	set up call values
	bsr.l	fdec_do 	do fdec
*
dec_setr
	subq.l	#2,a1		set string pointer
	move.w	d6,(a6,a1.l)	set string length
	bra.l	ut_retst	and return string
fexp$
	bsr.s	dec_set 	set up call values
	move.w	d6,d0		and check field at least 7 greater than NDP
	sub.w	d5,d0
	subq.w	#7,d0
	blt.l	dec_err1	... oops
	bsr.s	fexp_do 	convert
	bra.s	dec_setr	and return
*
dec_10s
	lea	dec_scal(pc),a3 	scaling
	bsr.s	dec_10			put ten on stack
	move.w	d0,-2(a6,a1.l)		put the DP scaling
	subq.l	#2,a1
	bge.s	dec_exri		negative
	neg.w	(a6,a1.l)		... yes, make positive
	addq.l	#dec_scld-dec_scal,a3
dec_exri
	move.w	ri..execb,a2
	jmp	(a2)			... and execute it
dec_10 
	move.l	#$50000000,-4(a6,a1.l)	put 10.000 on the ri stack
	move.w	#$0804,-6(a6,a1.l) 
	subq.l	#6,a1
	rts
*
fexp_do
	move.w	d5,d0			create max size integer
	moveq	#1,d3			... in d3
fexp_smax
	add.l	d3,d3			*2
	move.l	d3,d1
	asl.l	#2,d1			*8
	bvs.l	dec_err1		... oops
	add.l	d1,d3			*10
	dbra	d0,fexp_smax
*
	move.w	(a6,a1.l),d7		get exponent
	sub.w	#$802-330,d7		offset to 10^-99
	blt.s	fexp_0
	mulu	#3,d7			2^10 appx 10^3
	divu	#10,d7
	sub.w	#99,d7
	bra.s	fexp_prescale		... no
fexp_0
	moveq	#0,d7			kill exponent
fexp_prescale
	move.w	d5,d0			and set prescaling factor
	sub.w	d7,d0
	bsr.s	dec_10s 		scale by power 10
	bra.s	fexp_int
*
* mantissa too large, retry
*
fexp_1
	addq.w	#1,d7			increase decimal exp
	lea	dec_div(pc),a3
	bsr.s	dec_10			put 10 on and do
	bsr.s	dec_exri
*
fexp_int
	cmp.w	#$81f,(a6,a1.l) 	is exponent too large?
	bge.s	fexp_1
	lea	dec_dlint(pc),a3	dup and convert to long int
	bsr.s	dec_exri  
*
	move.l	(a6,a1.l),d1		get mantissa
	addq.l	#4,a1
	bge.s	fexp_csize
	neg.l	d1			negate
fexp_csize
	cmp.l	d3,d1			is it > 10^(ndp+1)?
	bge.s	fexp_1			... no, increase exponent by one
*
	moveq	#' ',d3 		fill with spaces
	move.w	d6,-(sp)		save field
	subq.w	#4,d6			(now smaller)
	move.w	d7,-(sp)		save exponent
	moveq	#$7f,d7 		(no commas!!)
	exg	a1,a4			set regs
	subq.l	#4,a4
	bsr.s	dec_value		and convert value
	movem.w (sp)+,d1/d6
	lea	(a1,d6.w),a2		find position for exponent
	move.b	#'E',-4(a6,a2.l)	set it
	moveq	#'+',d0
	tst.l	d1			check sign
	bge.s	fexp_exp
	neg.l	d1			make positive
	moveq	#'-',d0
fexp_exp
	move.b	d0,-3(a6,a2.l)		set sign
	bsr.s	fexp_dig		two digits
	bsr.s	fexp_dig 
	tst.w	d1			possibly a third
	beq.s	dec_rts 		... no
*
fexp_dig
	divu	#10,d1			divide
	moveq	#'0',d0
	swap	d1			add remainder to ascii 0
	add.b	d1,d0
	clr.w	d1			clear remainder
	swap	d1			to do again
	bra.l	dec_stuff
*
fdec_do
	move.w	d5,d0			scale using ndp
	bsr.l	dec_10s 		scale by power of 10
	bne.s	dec_err1		... oops, too big

idec_do
dec_iconv
	lea	dec_nlint(pc),a3	get nearest long integer
	bsr.l	dec_exri
dec_err1
	exg	a1,a4
	bne.s	dec_ov
*
dec_value
	swap	d4			get the sign type
	move.l	(a6,a4.l),d1		get the value
	bge.s	dec_tset		... it was positive
	neg.l	d1			now it's positive
	bvs.s	dec_ov			... oops, was too big!
	addq.w	#5,d4			move character pointer on
dec_tset
	move.w	d6,-(sp)		save field width
	move.b	dec_tsign(pc,d4.w),d0	get trailing sign
	beq.s	dec_conv		(nothing)
	subq.w	#1,d6			field is smaller
	add.w	d6,a1
	move.b	d0,(a6,a1.l)		set character
	sub.w	d6,a1
dec_conv
	swap	d4			restore DP
	bsr.s	dec_cfill		convert the number
	move.w	(sp)+,d6		restore field width
*
	tst.l	d5			sign after currency?
	bpl.s	dec_flcur		... no
	swap	d4			get leading sign
	move.b	dec_lsign(pc,d4.w),d0
	beq.s	dec_flcur		... none
	bsr.s	dec_stuff		stuff a character (must do bsr!!!)

dec_flcur
	move.b	(a6,a5.l),d0		get floating currency character
	subq.l	#1,a5
	beq.s	dec_sign		... done, set sign
	bsr.s	dec_stuff		... stuff character
	bra.s	dec_flcur
*
dec_sign
	tst.l	d5			sign before currency?
	bmi.s	dec_rts 		... no
	swap	d4			get leading sign
	move.b	dec_lsign(pc,d4.w),d0
	beq.s	dec_rts 		... none
	bsr.s	dec_stuff		stuff a character (must do bsr!!!)
dec_rts
	rts
*
*
* Table of RI ops
*
dec_scal
	dc.b	ri.float		scale by factor of 10
	dc.b	ri.powfp
	dc.b	ri.mult
	dc.b	0
dec_scld
	dc.b	ri.float
	dc.b	ri.powfp
dec_div
	dc.b	ri.div			divide by 10
	dc.b	0
dec_dlint
	dc.b	ri.dup			duplicate
dec_nlint
	dc.b	ri.nlint
	dc.b	0
	ds.w	0
*
* Table of signs (leading +,-, trailing +,-)
*
dec_lsign
	dc.b	000,'+',000,000,' '
	dc.b	'-','-',000,000,'('
dec_tsign
	dc.b	000,000,' ','+',' '
	dc.b	000,000,'-','-',')'
	end
