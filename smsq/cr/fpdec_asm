; Convert floating point to dec	string	V2.01   1990  Tony Tebby  QJUMP

	section	cv

	xdef	cr_fpdec
	xdef	ca_fpdec

	xref	ca_idec

	xref	qa_muld
	xref	qa_mul
	xref	qa_divd
	xref	qa_div
	xref	qa_dup
	xref	qa_nlint
	xref	qa_neg
	xref	qa_power

	include	'dev8_keys_qlv'
;+++
; Decimal conversion: FP to  characters
;
;	d0  r	error return 0
;	d1  r	nr of chars returned
;	a0 c  u	pointer	to buffer to take characters
;	a1 c  u	pointer	to stack holding FP
;	a6	base address
;	status return standard
;---
cr_fpdec
	move.l	a0,-(sp)
	add.l	a6,a0
	add.l	a6,a1
	bsr.s	ca_fpdec
	sub.l	a6,a0
	sub.l	a6,a1
	move.l	a0,d1
	sub.l	(sp)+,d1	 ; length returned
	rts

;+++
; Decimal conversion: FP to  characters
;
;	d0  r	error return 0
;	a0 c  u	pointer	to buffer to take characters
;	a1 c  u	pointer	to stack holding FP
;	status return standard
;---
ca_fpdec
cfd.reg	 reg	d1/d2/d4/a2
	movem.l	cfd.reg,-(sp)

	tst.l	2(a1)			 ; negative or zero?
	beq.l	cfd_zero
	bgt.s	cfd_do
	move.b	#'-',(a0)+
	jsr	qa_neg			 ; positive now

cfd_do
	move.w	(a1),d4			 ; get exponent
	cmp.w	#$10,d4			 ; ....	too small altogether?
	bgt.s	cfd_scale
	moveq	#$10,d4			 ; limit it here
cfd_scale
	mulu	#3,d4			 ; 2^10	appx 10^3
	divu	#10,d4
	ext.l	d4
	sub.w	#$800*3/10,d4

	moveq	#6,d1			 ; five	dps
	sub.w	d4,d1			 ; scaling
	beq.s	cfd_int
	clr.w	-(a1)
	move.l	#$08045000,-(a1)
	move.w	d1,-(a1)
	bgt.s	cfd_up
	neg.w	(a1)
	jsr	qa_power		 ; raise to power
	jsr	qa_div			 ; and divide
	bra.s	cfd_int
cfd_up
	jsr	qa_power
	jsr	qa_mul
	bra.s	cfd_int

; mantissa too large, retry

cfd_up1
	bset	#31,d4			 ; flag	up
	lea	qa_divd,a2
	addq.w	#1,d4			 ; one more
	bra.s	cfn_exp1

; mantissa too small, retry

cfd_dn1
	lea	qa_muld,a2
	subq.w	#1,d4			 ; one fewer
cfn_exp1
	move.l	#$50000000,d1
	move.w	#$0804,d2
	jsr	(a2)

cfd_int
	cmp.w	#$81f,(a1)		 ; is exponent too large?
	bge.s	cfd_up1

	jsr	qa_dup
	jsr	qa_nlint		 ; dup and convert to long int

	move.l	(a1)+,d1		 ; get number

	cmp.l	#9999999,d1		 ; is it too large
	bgt.s	cfd_up1			 ; ... yes, increase exponent by one


	cmp.l	#1000000,d1		 ; is it too small?
	bge.s	cfd_pop			 ; ... no
	tst.l	d4			 ; already adjusted up?
	bpl.s	cfd_dn1			 ; ... no

	move.l	#1000000,d1		 ; ... yes

cfd_pop
	addq.l	#6,a1			 ; pop number

	move.l	a0,a2

	jsr	ca_idec			 ; and convert to decimal

	moveq	#1,d1
	addq.w	#1,d4
	cmp.w	#6,d4			 ; use exp format?
	shi	d2
	bhi.s	cfd_dp			 ; ... yes
	move.w	d4,d1			 ; ... no

cfd_dp
	subq.w	#1,d4
	move.l	a0,d0
	sub.l	a2,d0
	sub.w	d1,d0			 ; number of chars to move

	move.l	a0,a2			 ; keep	end
	bra.s	cfd_dpe
cfd_dpl
	move.b	-(a2),1(a2)
cfd_dpe
	dbra	d0,cfd_dpl

	move.b	#'.',(a2)

	addq.l	#1,a0
cfd_strip
	cmp.b	#'0',-(a0)		 ; trailing zeros?
	beq.s	cfd_strip

	cmp.b	#'.',(a0)+		 ; trailing '.'?
	bne.s	cfd_exp
	subq.l	#1,a0			 ; ... yes, exclude it

cfd_exp
	tst.b	d2			 ; any exponent?
	beq.s	cfd_ok			 ; ... no
	move.b	#'E',(a0)+		 ; ... set it
	move.w	d4,d1
	ext.l	d1
	jsr	ca_idec

cfd_ok
	moveq	#0,d0
	movem.l	(sp)+,cfd.reg
	rts

cfd_zero
	move.b	#'0',(a0)+
	addq.l	#6,a1
	bra.s	cfd_ok

	end
