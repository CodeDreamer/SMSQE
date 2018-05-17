; QL Arithmetic PUSH onto Stack     V2.01   1990  Tony Tebby  QJUMP

	section qa

	xdef	qa_pushd
	xdef	qa_pushz
	xdef	qa_push1
	xdef	qa_put1
	xdef	qa_pushn
	xdef	qa_pushk
	xdef	qa_dup
	xdef	qa_over
	xdef	qa_swap
	xdef	qa_roll

;+++
; QL Arithmetic: PUSH (FP) onto stack
;
;	This routine pushes the FP D1,D2
;
;	d0  r	error return 0
;	d1 c  p mantissa
;	d2 c  p exponent
;	a1 c  u pointer to arithmetic stack
;	status return standard
;---
qa_pushd
	move.l	d1,-(a1)
	move.w	d2,-(a1)
	moveq	#0,d0
	rts
;+++
; QL Arithmetic: PUSH zero onto stack
;
;	This routine pushes 0.00 onto the stack
;
;	d0  r	error return 0
;	a1 c  u pointer to arithmetic stack
;	status return standard
;---
qa_pushz
	clr.l	-(a1)
	clr.w	-(a1)
	moveq	#0,d0
	rts
;+++
; QL Arithmetic: PUT 1 onto stack
;
;	This routine puts 1.00 on top of TOS
;
;	d0  r	error return 0
;	a1 c  u pointer to arithmetic stack
;	a6	base address
;	status return standard
;---
qa_put1
	addq.l	#6,a1
;+++
; QL Arithmetic: PUSH 1 onto stack
;
;	This routine pushes 1.00 onto the stack
;
;	d0  r	error return 0
;	a1 c  u pointer to arithmetic stack
;	a6	base address
;	status return standard
;---
qa_push1
	clr.w	-(a1)
	move.l	#$08014000,-(a1)
	moveq	#0,d0
	rts
;+++
; QL Arithmetic: PUSH byte (a3)+ onto stack and float
;
;	This routine pushes n onto the stack
;
;	d0  r	error return 0
;	a1 c  u pointer to arithmetic stack
;	a3 c  u pointer to operation list
;	status return standard
;---
qa_pushn
	moveq	#0,d0
	move.b	(a3)+,d0
	beq.s	qa_pushz		 ; zero
	move.w	d1,-(sp)
	ror.l	#8,d0			 ; unnormalised number
	move.w	#$0808,d1		 ; has this exponent
qpn_loop
	subq.w	#1,d1
	asl.l	#1,d0
	bvc.s	qpn_loop		 ; not done

	roxr.l	#1,d0
	move.l	d0,-(a1)
	move.w	d1,-(a1)		 ; push normalised number
	move.w	(sp)+,d1
	moveq	#0,d0
	rts
;+++
; QL Arithmetic: PUSH Konstant onto stack
;
;	This routine pushes a constant onto the stack ---- Miverva special
;
;	d0  r	error return 0
;	a1 c  u pointer to arithmetic stack
;	a3 c  u pointer to operation list
;	status return standard
;---
qa_pushk

* These constants are accessed in a strange, but clever fashion. Only the
* mantissa is held here, and the exponent is constructed from part of the
* selecting byte. We limit ourselves to 16 mantissas, each of which will have
* an exponent constructed for it. E.g. We have the mantissa for PI here,
* but we can supply 2*PI, PI/2, PI/4, etc as well. There is a further twist,
* in that the sixteen constants are arranged in order of their typically
* required magnitude. The exponent we build for them is the sum of a base
* exponent and BOTH nibbles of the selector, giving us a fairly wide range of
* possible values, and variations on the same. At present, we have only found
* duplication in the system code of the constants PI and PI/180, the other
* entries are not heavily used at the moment...

	subq.l	#6,A1
	moveq	#0,d0
	move.b	(a3)+,d0	      ;  exp	 D0
	move.w	#$07F0,(A1)	      ;  07F0	 00hl
	or.w	D0,(A1) 	      ;  07Fl
	lsr.b	#4,D0		      ; 	 000h
	ext.w	D0		      ; 	 000h
	add.w	D0,(A1) 	      ;  done
	lsl.b	#2,D0
	move.l	KTAB-8*4(PC,D0.W),D0 mantissa
	move.l	D0,2(A1)
	moveq	#0,D0
	rts

;		Mantissa  Exp Err  What     Value	  Reference
;	dc.l	$???????? ??? +.?? ???????? 0.??????????? $00-$4F
	dc.l	$477D1A89 7FB +.29 PI/180   0.01745329252 $56 RI.PI180
	dc.l	$6F2DEC55 7FF -.41 LOG10(e) 0.4342944819  $69 RI.LOGE
	dc.l	$430548E1 800 -.29 PI/6     0.5235987756  $79 RI.PI6
ktab
	dc.l	$58B90BFC 800 -.09 LN(2)    0.6931471806  $88 RI.LN2
	dc.l	$6ED9EBA1 801 +.38 SQRT(3)  1.732050808   $98 RI.SQRT3
	dc.l	$6487ED51 802 +.07 PI	    3.141592654   $A8 RI.PI $A7=RI.PI2
;	dc.l	$???????? ??? +.?? ???????? ?.?????????   $B0-$FF

;+++
; QL Arithmetic: DUPlicate (FP) on stack
;
;	This routine duplicates the top of stack
;
;	d0  r	error return 0
;	a1 c  u pointer to arithmetic stack
;	a6	base address
;	status return standard
;---
qa_dup
	move.l	2(a1),-(a1)
	move.w	4(a1),-(a1)
	moveq	#0,d0
	rts
;+++
; QL Arithmetic: duplicate NOS (FP) OVER TOS (FP)
;
;	This routine duplicates the NOS
;
;	d0  r	error return 0
;	a1 c  u pointer to arithmetic stack
;	status return standard
;---
qa_over
	move.l	8(a1),-(a1)
	move.w	10(a1),-(a1)
	moveq	#0,d0
	rts
;+++
; QL Arithmetic: SWAP (FP) TOS with NOS
;
;	d0  r	0
;	a1 c  p pointer to arithmetic stack
;	status return standard
;---
qa_swap
	move.w	6(a1),d0
	move.w	(a1),6(a1)
	move.w	d0,(a1)+
	move.l	6(a1),d0
	move.l	(a1),6(a1)
	move.l	d0,(a1)
	subq.l	#2,a1
	moveq	#0,d0
	rts
;+++
; QL Arithmetic: ROLL (FP) NNOS to TOS
;
;	d0  r	0
;	a1 c  p pointer to arithmetic stack
;	status return standard
;---
qa_roll
	move.w	12(a1),d0
	move.w	6(a1),12(a1)
	move.w	(a1),6(a1)
	move.w	d0,(a1)+
	move.l	12(a1),d0
	move.l	6(a1),12(a1)
	move.l	(a1),6(a1)
	move.l	d0,(a1)
	subq.l	#2,a1
	moveq	#0,d0
	rts
	end
