* Integer to decimal string conversion	 V2.00	  1986  Tony Tebby   QJUMP
*
	section cv
*
	xdef	cr_iwdec	Integer word to decimal
	xdef	cr_ildec		long
	xdef	ca_iwdec	ditto, but a1 and a0 are absolute
	xdef	ca_ildec
	xdef	ca_idec
*
*	d0  r	0 no error possible
*	d1  r	number of digits
*	a0 c  u pointer to buffer to put characters
*	a1 c  u pointer to stack to get value from (cr_i.dec)
*	a6	base address
*
*	all other registers preserved
*
cr_iwdec
	move.w	(a6,a1.l),d1
	ext.l	d1			 ; signed!!!
	addq.l	#2,a1
	bra.s	cr_idec
*
cr_ildec
	move.l	(a6,a1.l),d1		 ; set value to convert
	addq.l	#4,a1
*
cr_idec
	add.l	a6,a0
	bsr.s	ca_idec
	sub.l	a6,a0
	rts

ca_ildec
	move.l	(a1)+,d1		 ; long off stack
	bra.s	ca_idec
ca_iwdec
	move.w	(a1)+,d1		 ; int off stack
	ext.l	d1

ca_idec
	movem.l d2/d3,-(sp)		 ; save d2,d3
	moveq	#0,d2
	moveq	#1,d3			 ; count of digits
	tst.l	d1			 ; zero result?
	bgt.s	cid_start		 ; ... no
	beq.s	cid_dummy		 ; ... yes
	neg.l	d1			 ; negative
	move.b	#'-',(a0)+		 ; add sign
	bset	#16,d3			 ; and a character
	bra.s	cid_start

cid_dummy
	clr.b	-(sp)			... yes, put on dummy
	bra.s	cid_copy
*
cid_loop
	addq.w	#1,d3			one more digit
cid_start
	move.l	d1,d0			divide top end
	clr.w	d0
	swap	d0
	beq.s	cid_lsw 		none, do least sig word
	divu	#10,d0
	move.w	d0,d2			save result
cid_lsw
	move.w	d1,d0			now divide top remainder + bottom
	divu	#10,d0
	swap	d0			remainder is our digit
	move.b	d0,-(sp)		save it
*
	move.w	d2,d0			new top end (new bottom in msw)
	move.l	d0,d1			back to d1
	swap	d1			the right way round
	bne.s	cid_loop		some more
*
cid_copy
	moveq	#'0',d0 		copy ascii character
	add.b	(sp)+,d0
	move.b	d0,(a0)+
	addq.w	#1,d1			increment count
	cmp.w	d1,d3			... done?
	bne.s	cid_copy		... no
*
	swap	d3
	add.w	d3,d1			... add extra digit for sign
	moveq	#0,d0			no error
	movem.l (sp)+,d2/d3		restore regs
	rts
	end
