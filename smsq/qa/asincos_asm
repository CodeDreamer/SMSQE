; QL Arithmetic ASIN ACOS ATAN ACOT  V2.01   1990  Tony Tebby	QJUMP

	section qa

	xdef	qa_asin
	xdef	qa_acos
	xdef	qa_atan
	xdef	qa_atan2
	xdef	qa_acot

	xref	qa_poly
	xref	qa_sqrt
	xref	qa_neg
	xref	qa_negcz
	xref	qa_add
	xref	qa_add1
	xref	qa_addd
	xref	qa_subr
	xref	qa_subd
	xref	qa_mul
	xref	qa_muld
	xref	qa_square
	xref	qa_div
	xref	qa_recip
	xref	qa_dup
	xref	qa_swap
	xref	qa_pushd
	xref	qa_push1
	xref	qa_pi

	xref.l	qa.pim
	xref.s	qa.pimsw
	xref.l	qa.pi3m

;+++
; QL Arithmetic: ACOS
;
;	This routine finds the arccosine of TOS. ACOS(x)=ATAN(SQRT(1-x^2)/x).
;
;	d0  r	error return 0
;	a1 c  p pointer to arithmetic stack
;	status return standard
;---
qa_acos
	tst.b	2(a1)			 ; negative?
	bge.s	qas_acos		 ; ... no
	jsr	qa_neg			 ; not now
	pea	qas_pi			 ; but take from PI

qas_acos
	st	-(sp)			 ; flag acos required
	bra.s	qas_stan

;+++
; QL Arithmetic: ASIN
;
;	This routine finds the arcsine of TOS. Uses ASIN(x)=ATAN(x/SQRT(1-x^2))
;
;	d0  r	error return 0
;	a1 c  p pointer to arithmetic stack
;	status return standard
;---
qa_asin
	tst.b	2(a1)			 ; negative?
	bge.s	qas_asin		 ; ... no
	jsr	qa_neg			 ; not now
	pea	qa_negcz		 ; but restore

qas_asin
	sf	-(sp)
qas_stan
	jsr	qa_dup			 ; x, x
	jsr	qa_dup			 ; x, x, x
	addq.l	#6,a1
	jsr	qa_neg			 ; x, -x, x
	jsr	qa_add1 		 ; x, 1-x, x
	subq.l	#6,a1
	jsr	qa_add1 		 ; 1+x, 1-x, x
	jsr	qa_mul			 ; 1-x^2, x
	jsr	qa_sqrt 		 ; sqrt (1-x^2), x
	bne.s	qas_rts2
	cmp.l	#$08005a82,(a1) 	 ; >sqrt(.5)?
	bgt.s	qas_div 		 ; ... yes, ok
	jsr	qa_swap 		 ; ... no, cot
	not.b	(sp)			 ; and change range
qas_div
	jsr	qa_div
	bra.s	qas_atsp2		 ; do atan for range 0 to pi/2

qas_rts2
	addq.l	#2,sp			 : asin/acos |x|>1
qas_rts
	rts

;+++
; QL Arithmetic: ATAN2
;
;	This routine finds the arctangent of NOS/TOS. Cody and Waite. TT.
;
;	d0  r	error return 0
;	a1 c  p pointer to arithmetic stack
;	status return standard
;---
qa_atan2
	tst.w	8(a1)			 ; y negative?
	bge.s	qas_a2x
	pea	qa_negcz		 ; negate result
	addq.l	#6,a1
	jsr	qa_neg			 ; y positive
	subq.l	#6,a1
qas_a2x
	tst.w	2(a1)			 ; x negative?
	bge.s	qas_a2d 		 ; ... no
	jsr	qa_neg			 ; make pos
	pea	qas_pi			 ; and subtract from PI
qas_a2d
	pea	qas_att 		 ; do tan after divide
	jmp	qa_div

;+++
; QL Arithmetic: ACOT
;
;	This routine finds the arccotangent of TOS. Cody and Waite, TT
;
;	d0  r	error return 0
;	a1 c  p pointer to arithmetic stack
;	status return standard
;---
qa_acot
	tst.b	2(a1)			 ; negative?
	bge.s	qas_atc 		 ; ... no
	jsr	qa_neg			 ; not now
	pea	qa_negcz
qas_atc
	st	-(sp)			 ; flag cotangent
	bra.s	qas_atr

;+++
; QL Arithmetic: ATAN
;
;	This routine finds the arctangent of TOS. Cody and Waite. TT.
;
;	d0  r	error return 0
;	a1 c  p pointer to arithmetic stack
;	status return standard
;---
qa_atan
	tst.b	2(a1)			 ; negative?
	bge.s	qas_att 		 ; ... no
	jsr	qa_neg			 ; not now
	pea	qa_negcz

qas_att
	sf	-(sp)			 ; flag tangent
qas_atr
	cmp.w	#$0801,(a1)		 ; <=1
	blt.s	qas_atsp2		 ; ... no
	jsr	qa_recip
	not.b	(sp)			 ; reverse flag

qas_atsp2
	tst.b	(sp)+			 ; subtract from PI/2?
	beq.s	qas_atan		 ; ... no
	pea	qas_pi2

qas_atan
qas.reg reg	d1/d2/a2
	movem.l qas.reg,-(sp)
	moveq	#0,d2			 ; primary range
	cmp.l	#$07FF4498,(a1) 	 ; > 2-sqrt(3)?
	blt.s	qas_bipoly		 ; ... no
	jsr	qa_dup			 ; duplicate
	addq.l	#6,a1			 ; (x), x
	move.w	#$0800,d2
	move.l	#$49E69D16,d1		 ; 1/sqrt(3)
	jsr	qa_subd 		 ; (x), x-1/sqrt(3)
	subq.l	#6,a1
	jsr	qa_muld 		 ; x/sqrt(3), x-1/sqrt(3)
	jsr	qa_add1 		 ; 1+x/sqrt(3), x-1/sqrt(3)
	jsr	qa_div			 ; (x-1/sqrt(3))/(1+x/sqrt(3))
	moveq	#-1,d2			 ; flag reduction required

qas_bipoly
	lea	qas_tab,a2
	jsr	qa_dup			 ; duplicate #f
	jsr	qa_dup
	jsr	qa_square		 ; #g, #f, #f
	move.w	(a1),d2
	move.l	2(a1),d1
	jsr	qa_poly 		 ; #P, #f, #f
	jsr	qa_muld 		 ; #g*P, #f ,#f
	jsr	qa_pushd		 ; #g, #g*P, #f, #f
	jsr	qa_poly 		 ; #Q, #g*P, #f, #f
	jsr	qa_div			 ; #g*P/Q, #f, #f
	jsr	qa_mul
	jsr	qa_add

	tst.l	d2			 ; reduction required?
	bpl.s	qas_exit		 ; ... no

	move.w	#$0800,d2
	move.l	#qa.pi3m,d1		 ; pi/6
	jsr	qa_addd 		 ; bump up the range

qas_exit
	tst.l	d0
	movem.l (sp)+,qas.reg
qas_rts1
	rts

qas_pi2
	bne.s	qas_rts1		 ; bad CC
	move.l	#qa.pim,-(a1)
	move.w	#$0801,-(a1)		 ; push pi/2
	jmp	qa_subr 		 ; and reverse subtract

qas_pi
	bne.s	qas_rts1		 ; bad CC
	move.l	#qa.pim,-(a1)
	move.w	#$0802,-(a1)		 ; push pi
	jmp	qa_subr 		 ; and reverse subtract

	dc.w	$0803,$451F,$BEDF	 ; q0
	dc.w	$0803,$4C09,$1DF8	 ; q1
	dc.w	$0801,$4000,$0000	 ; q2
	dc.w	2			 ; poly Q

	dc.w	$0801,$A3D5,$AC3C	 ; p0
	dc.w	$0800,$A3D6,$2904	 ; p1
	dc.w	1			 ; poly P
qas_tab

	end
