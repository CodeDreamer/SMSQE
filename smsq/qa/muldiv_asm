; QL Arithmetic Multiply/Divide    V2.02   1990  Tony Tebby  QJUMP
;
; 2006-12-18  2.02  Fixed denormalised value on divide (MK)

	section qa

	xdef	qa_mul
	xdef	qa_muld
	xdef	qa_square
	xdef	qa_div
	xdef	qa_divd
	xdef	qa_recip

	xref	qa_push1
	xref	qa_swap

	include 'dev8_keys_err'

;+++
; QL Arithmetic: DIV by D1/D2
;
;	This routine Divides the TOS by the FP D1/D2
;
;	d0  r	error return 0 or ERR.OVFL
;	d1 c  p mantissa
;	d2 c  p exponent
;	a1 c  p pointer to arithmetic stack
;	status return standard
;---
qa_divd
qmd.reg reg	d1/d2/d3/d4/d7
	movem.l qmd.reg,-(sp)
	tst.l	d1			 ; d1 positive
	bra.s	qdv_do

;+++
; QL Arithmetic: Reciprocal
;
;	This routine divides the FP at TOS into 1.
;
;	d0  r	error return 0 or ERR.OVFL
;	a1 c  u pointer to arithmetic stack
;	status return standard
;---
qa_recip
	jsr	qa_push1
	jsr	qa_swap

;+++
; QL Arithmetic: Divide
;
;	This routine POPS the FP at TOS and Divides the NOS by it
;
;	d0  r	error return 0 or ERR.OVFL
;	a1 c  u pointer to arithmetic stack
;	status return standard
;---
qa_div
	movem.l qmd.reg,-(sp)
	move.w	(a1)+,d2		; get top of stack
	move.l	(a1)+,d1

qdv_do
	slt	d7			 ; set negative
	bgt.s	qdv_abs2
	neg.l	d1
	bvc.s	qdv_abs2
	lsr.l	#1,d1			 ; overflow and exponent
	addq.w	#1,d2			 ; ... goes up
qdv_abs2
	move.w	(a1)+,d0
	move.l	(a1)+,d3		 ; other mantissa
	beq.s	qdv_0			 ; it was zero
	bgt.s	qdv_sexp
	not.b	d7
	neg.l	d3
	bvc.s	qdv_sexp
	lsr.l	#1,d3			 ; overflow and exponent
	subq.w	#1,d2			 ; ... goes down

qdv_sexp
	sub.w	d0,d2			 ; difference of exponents
	neg.w	d2
	add.w	#$801,d2		 ; re-center

qdv_nd1
	add.l	d1,d1			 ; normalise to msb
	bvs.s	qdv_nd3 		 ; ... ok
	beq.l	qmd_ovfl		 ; divide by nothing
	add.w	#1,d2
	bra.s	qdv_nd1

qdv_nd3
	add.l	d3,d3			 ; normalise to msb
	bvs.s	qdv_div
	sub.w	#1,d2
	bra.s	qdv_nd3

qdv_div
	lsr.l	#1,d1			 ; spare bit please
	lsr.l	#1,d3

	moveq	#31,d0			 ; 31 subtractions
	moveq	#0,d4

	cmp.l	d1,d3			 ; ensure that msb is set
	bge.s	qdv_loop
	add.l	d3,d3
	sub.w	#1,d2

qdv_loop
	cmp.l	d1,d3			 ; can we subtract?
	blo.s	qdv_eloop
	bset	d0,d4			 ; ... yes - set it
	sub.l	d1,d3
qdv_eloop
	add.l	d3,d3			 ; increase bit to divide into
	dbra	d0,qdv_loop

	addq.l	#1,d4			 ; round up
	lsr.l	#1,d4
	move.l	d4,d1
	bra.l	qmd_ckexp

qdv_0
	tst.l	d1			 ; zero by zero?
	bne.l	qmd_ok6
	subq.l	#6,a1			 ; return zero
	bra.l	qmd_exov

;+++
; QL Arithmetic: MUL by D1/D2
;
;	This routine Multiplies the TOS by the FP D1/D2
;
;	d0  r	error return 0 or ERR.OVFL
;	d1 c  p mantissa
;	d2 c  p exponent
;	a1 c  p pointer to arithmetic stack
;	status return standard
;---
qa_muld
	movem.l qmd.reg,-(sp)
	tst.l	d1			 ; d1 positive?
	bra.s	qml_do

;+++
; QL Arithmetic: SQUARE TOS
;
;	This routine Multiplies the TOS by itself
;
;	d0  r	error return 0 or ERR.OVFL
;	a1 c  p pointer to arithmetic stack
;	status return standard
;---
qa_square
	movem.l qmd.reg,-(sp)
	move.w	(a1),d2 		 ; get top stack
	move.l	2(a1),d1
	bra.s	qml_do

;+++
; QL Arithmetic: Multiply
;
;	This routine POPS the FP at TOS and multiplies the NOS by it
;
;	d0  r	error return 0 or ERR.OVFL
;	a1 c  u pointer to arithmetic stack
;	status return standard
;---
qa_mul
	movem.l qmd.reg,-(sp)
	move.w	(a1)+,d2		 ; get top stack
	move.l	(a1)+,d1
qml_do
	beq.l	qmd_z6			 ; d1 zero
	slt	d7			 ; set negative
	bgt.s	qml_abs2
	neg.l	d1
	bvc.s	qml_abs2
	lsr.l	#1,d1			 ; $80000000 -> $40000000
	addq.w	#1,d2			 ; ... exp up
qml_abs2
	move.w	(a1)+,d0
	move.l	(a1)+,d3		 ; other mantissa
	beq.l	qmd_ok6 		 ; ... it was zero
	bgt.s	qml_sexp
	not.b	d7
	neg.l	d3
	bvc.s	qml_sexp
	lsr.l	#1,d3			 ; move up and exponent
	addq.w	#1,d2			 ; ... goes down

qml_sexp
	add.w	d0,d2			 ; add both exponents
	sub.w	#$800,d2		 ; they were centered

	add.l	d1,d1
	add.l	d3,d3			 ; make the most of them

	move.w	d3,d0
	mulu	d1,d0			 ; least sig long word
	clr.w	d0
	swap	d0			 ; msw of it

	move.l	d3,d4
	swap	d4
	mulu	d1,d4			 ; first middle long word
	add.l	d4,d0

	move.w	d3,d4
	swap	d1
	mulu	d1,d4			 ; second middle long word
	add.l	d0,d4
	move.w	d4,d0			 ; keep round up bit
	clr.w	d4
	roxl.w	#1,d4			 ; overflow bit here
	swap	d4			 ; 17 bit add-in

	swap	d3
	mulu	d3,d1			 ; MS long word
	add.l	d4,d1			 ; corrected
	bpl.s	qml_round		 ; round up by 1/2

	addq.l	#1,d1			 ; ... rounded
	lsr.l	#1,d1			 ; and divide by two
	bra.s	qmd_ckexp

qml_round
	subq.w	#1,d2			 ; exponent has got smaller
	lsl.w	#1,d0			 ; next bit down
	moveq	#0,d0
	addx.l	d0,d1			 ; add bit in
	bpl.s	qmd_ckexp		 ; ... ok

	lsr.l	#1,d1
	addq.w	#1,d2			 ; re-normalise

qmd_ckexp
	tst.w	d2			 ; underflowed exponent
	bgt.s	qmd_ckov
	beq.s	qmd_put
	neg.w	d2
	cmp.w	#$20,d2 		 ; two great to shift?
	bge.s	qmd_zero
	asr.l	d2,d1			 ; underflowed number
	moveq	#0,d2
	bra.s	qmd_put
qmd_zero
	moveq	#0,d1
	moveq	#0,d2
	bra.s	qmd_put

qmd_ckov
	cmp.w	#$0fff,d2		 ; exponent too great?
	bhi.s	qmd_ovfl

qmd_put
	tst.b	d7			 ; negate?
	beq.s	qmd_putd		 ; ... no
	neg.l	d1
	cmp.l	#$c0000000,d1		 ; funny value?
	bne.s	qmd_putd
	tst.w	d2			 ; underflowable?
	beq.s	qmd_putd		 ; ... yes
	add.l	d1,d1			 ; renorm
	subq.w	#1,d2

qmd_putd
	move.l	d1,-(a1)
	move.w	d2,-(a1)		 ; put result
qmd_ok
	moveq	#0,d0
qmd_exit
	movem.l (sp)+,qmd.reg
	rts

qmd_z6
	move.l	d1,2(a1)
	move.w	d1,(a1)
	bra.s	qmd_ok


qmd_ok6
	subq.l	#6,a1
	bra.s	qmd_ok

qmd_ovfl
	move.l	#$7fffffff,d1		 ; maximum mantissa
	tst.b	d7			 ; negative result?
	beq.s	qmd_sov
	neg.l	d1			 ; allow it to be negated again
qmd_sov
	move.l	d1,-(a1)
	move.w	#$0fff,-(a1)
qmd_exov
	moveq	#err.ovfl,d0
	bra.s	qmd_exit

	end
