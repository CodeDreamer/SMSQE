; QL Arithmetic ADD/SUB      V2.01   1990  Tony Tebby	QJUMP

	section qa

	xdef	qa_add
	xdef	qa_addd
	xdef	qa_add1
	xdef	qa_sub
	xdef	qa_subd
	xdef	qa_subr
	xdef	qa_halve
	xdef	qa_double

	xref	qa_neg

	include 'dev8_keys_err'

;+++
; QL Arithmetic: ADD 1
;
;	This routine ADDs 1.00000000 to TOS
;
;	d0  r	error return 0 or ERR.OVFL
;	d1 c  p mantissa
;	d2 c  p exponent
;	a1 c  p pointer to arithmetic stack
;	status return standard
;---
qa_add1
qad.reg reg	d1/d2/d3
	movem.l qad.reg,-(sp)
	move.w	#$801,d2		 ; FP 1
	moveq	#1,d1
	ror.l	#2,d1
	bra.s	qad_do

;+++
; QL Arithmetic: ADD D1/D2
;
;	This routine ADDs the FP D1/D2 to TOS
;
;	d0  r	error return 0 or ERR.OVFL
;	d1 c  p mantissa
;	d2 c  p exponent
;	a1 c  p pointer to arithmetic stack
;	status return standard
;---
qa_addd
	movem.l qad.reg,-(sp)
	bra.s	qad_do

;+++
; QL Arithmetic: SUB reversed
;
;	This routine POPS the FP at TOS and adds it to NOS negated.
;
;	d0  r	error return 0 or ERR.OVFL
;	a1 c  u pointer to arithmetic stack
;	status return standard
;---
qa_subr
	addq.l	#6,a1
	jsr	qa_neg			 ; negate NOS
	subq.l	#6,a1
	bra.s	qa_add

;+++
; QL Arithmetic: SUB D1/D2
;
;	This routine SUBTRACTS the FP D1/D2 from TOS
;
;	d0  r	error return 0 or ERR.OVFL
;	d1 c  p mantissa
;	d2 c  p exponent
;	a1 c  p pointer to arithmetic stack
;	status return standard
;---
qa_subd
	movem.l qad.reg,-(sp)
	neg.l	d1			 ; negate mantissa
	bvs.s	qsd_ovfl
	bpl.s	qad_do			 ; OK
	move.l	d1,d0
	add.l	d0,d0
	bvs.s	qad_do			 ; already normalised
	move.l	d0,d1
	subq.w	#1,d2			 ; renormalise
	bge.s	qad_do
	asr.l	#1,d1
	addq.w	#1,d2			 ; underflow
	bra.s	qad_do

qsd_ovfl
	lsr.l	#1,d1			 ; do not bother about overflows etc.
	addq.w	#1,d2
	bra.s	qad_do

;+++
; QL Arithmetic: SUB
;
;	This routine POPS the FP at TOS and subtracts it from NOS
;
;	d0  r	error return 0 or ERR.OVFL
;	a1 c  u pointer to arithmetic stack
;	status return standard
;---
qa_sub
	jsr	qa_neg			 ; negate TOS
;+++
; QL Arithmetic: ADD
;
;	This routine POPS the FP at TOS and adds it to NOS
;
;	d0  r	error return 0 or ERR.OVFL
;	a1 c  u pointer to arithmetic stack
;	status return standard
;---
qa_add
	movem.l qad.reg,-(sp)
	move.w	(a1)+,d2		 ; get top of stack
	move.l	(a1)+,d1

qad_do
	moveq	#0,d0
	move.w	(a1)+,d0		 ; one exponent
	move.l	(a1)+,d3		 ; and it's mantissa

	sub.w	d2,d0			 ; difference
	beq.s	qad_add 		 ; ... none
	blt.s	qad_und3		 ; ... unnormalise d3
	cmp.w	#32,d0			 ; very small?
	bhi.s	qad_ex6 		 ; ... yes, ignore d1/d2
	add.w	d0,d2			 ; new exponent
	asr.l	d0,d1			 ; ... unnormalise d1
	bra.s	qad_ufl
qad_und3
	neg.w	d0
	cmp.w	#32,d0			 ; very small?
	bhi.s	qad_put 		 ; yes, just use d1/d2
	asr.l	d0,d3			 ; unnormalise d3
qad_ufl
	moveq	#0,d0
	addx.w	d0,d0			 ; keep underflow bit
qad_add
	add.l	d3,d1			 ; add numbers
	bvs.s	qad_ovfl		 ; ... overflow on add

	move.l	d1,d3			 ; is re-normalise needed?
	add.l	d3,d3
	bvs.s	qad_rndup		 ; ... no
	add.l	d0,d3			 ; ... restore missing bit
	bra.s	qad_renorm

qad_rnloop
	add.l	d3,d3			 ; double up
	bvs.s	qad_put 		 ; ... oops, done
qad_renorm
	subq.w	#1,d2			 ; decrease exponent
	blt.s	qad_uflow		 ; no more
	move.l	d3,d1
	bra.s	qad_rnloop

qad_uflow
	moveq	#0,d2			 ; underflowed
	bra.s	qad_put

qad_rndup
	add.l	d0,d1			 ; ... no, round up
	bvs.s	qad_ovfl		 ; overflowed
qad_put
	move.l	d1,-(a1)		 ; put result
	move.w	d2,-(a1)
qad_ok
	moveq	#0,d0
qad_exit
	movem.l (sp)+,qad.reg
	rts
qad_ex6
	subq.l	#6,a1			 ; keep NOS
	bra.s	qad_ok

qad_ovfl
	asr.l	#1,d1
	bchg	#31,d1			 ; restore number
	moveq	#0,d0
	addx.l	d0,d1			 ; and round up
	addq.w	#1,d2			 ; mantissa greater
	cmp.w	#$0fff,d2
	bls.s	qad_put 		 ; ok
	moveq	#err.ovfl,d0
	bra.s	qad_exit

;+++
; QL Arithmetic: Double
;
;	This routine Doubles the FP at TOS
;
;	d0  r	error return 0 or ERR.OVFL
;	a1 c  p pointer to arithmetic stack
;	status return standard
;---
qa_double
	move.w	(a1),d0 		 ; old exponent
	addq.w	#1,d0
	cmp.w	#$0fff,d0		 ; overflowed?
	bgt.s	qad_dov 		 ; ... yes
	move.w	d0,(a1) 		 ; ... no, set it
qad_dok
	moveq	#0,d0
	rts
qad_dov
	moveq	#err.ovfl,d0
	rts

;+++
; QL Arithmetic: Halve
;
;	This routine halves the FP at TOS
;
;	d0  r	error return 0 or ERR.OVFL
;	a1 c  p pointer to arithmetic stack
;	status return standard
;---
qa_halve
	subq.w	#1,(a1) 		 ; halve
	bpl.s	qad_dok 		 ; ok
	clr.w	(a1)			 ; underflowed
	asr	2(a1)			 ; halve the mantissa
	roxr	4(a1)
	moveq	#0,d0
	rts

	end
