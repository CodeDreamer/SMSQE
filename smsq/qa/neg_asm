; QL Arithmetic NEG/ABS      V2.01   1990  Tony Tebby	QJUMP

	section qa

	xdef	qa_neg
	xdef	qa_negcz
	xdef	qa_abs

	include 'dev8_keys_err'

;+++
; QL Arithmetic: ABS
;
;	This routine makes TOS positive
;	If it overflows, the result is still "correct"
;
;	d0  r	error return 0 or ERR.OVFL
;	a1 c  p pointer to arithmetic stack
;	status return standard
;---
qa_abs
	tst.b	2(a1)			 ; negative?
	bmi.s	qa_neg			 ; ... yes
	moveq	#0,d0
	rts

;+++
; QL Arithmetic: NEG if CC zero
;
;	This routine negates TOS
;	If it overflows, the result is still "correct"
;	d0  r	error return 0 or ERR.OVFL
;	a1 c  u pointer to arithmetic stack
;	status return standard
;---
qa_negcz
	bne.s	qan_rts
;+++
; QL Arithmetic: NEG
;
;	This routine negates TOS
;	If it overflows, the result is still "correct"
;	d0  r	error return 0 or ERR.OVFL
;	a1 c  u pointer to arithmetic stack
;	status return standard
;---
qa_neg
	move.l	2(a1),d0		 ; different actions different signs
	beq.s	qan_rts
	blt.s	qan_neg 		 ; neg to pos can overflow!!
	neg.l	d0			 ; negate
	move.l	d0,2(a1)
	add.l	d0,d0			 ; does this overflow?
	bvs.s	qan_ok			 ; ... yes
	sub.w	#1,(a1) 		 ; ... no, smaller exponent please
	bge.s	qan_set 		 ; ... and put in doubled mantissa
	clr.w	(a1)			 ; exponent has underflowed as well
	bra.s	qan_ok

qan_neg
	neg.l	d0			 ; negate mantissa
	bvc.s	qan_set 		 ; ... ok
	lsr.l	#1,d0			 ; neg to pos overflows
	addq.w	#1,(a1) 		 ; correct it
	cmp.w	#$1000,(a1)		 ; exponent overflowed?
	bhs.s	qan_ovfl		 ; ... yes

qan_set
	move.l	d0,2(a1)		 ; set mantissa

qan_ok
	moveq	#0,d0			 ; ... no
qan_rts
	rts
qan_ovfl
	moveq	#err.ovfl,d0
	rts
	end
