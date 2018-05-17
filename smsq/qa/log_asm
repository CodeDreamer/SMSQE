; QL Arithmetic LN, LOG10     V2.01   1990  Tony Tebby  QJUMP

	section qa

	xdef	qa_ln
	xdef	qa_log10

	xref	qa_add
	xref	qa_addd
	xref	qa_mul
	xref	qa_muld
	xref	qa_div
	xref	qa_float
	xref	qa_pushd
	xref	qa_dup
	xref	qa_swap

	include 'dev8_keys_err'

; long divide: assumes msb of divisor set

qln_uldiv
	move.l	d1,d2
	moveq	#0,d1		 ; initialise result of division
	moveq	#31,d4		 ; set counter
qud_loop
	cmp.l	d0,d2		 ; divide this?
	blo.s	qud_eloop	 ; ... no
qud_sub
	bset	d4,d1
	sub.l	d0,d2
qud_eloop
	add.l	d2,d2
	dbcc	d4,qud_sub	 ; overflow?
	tst.w	d4
	dbmi	d4,qud_loop	 ; done?
	moveq	#0,d0
	roxl.b	#1,d0
	add.l	d0,d1		 ; round up
qud_rts
	rts

;+++
; QL Arithmetic: LOG10
;
;	This routine finds the log base 10 of TOS. Algorithm Cody and Waite
;
;	d0  r	error return 0 or ERR.IEXP
;	a1 c  p pointer to arithmetic stack
;	status return standard
;---
qa_log10
	moveq	#-1,d0
	bra.s	qln_do
;+++
; QL Arithmetic: LN
;
;	This routine finds the natural log of TOS. Algorithm Cody and Waite / TT
;
;	d0  r	error return 0 or ERR.IEXP
;	a1 c  p pointer to arithmetic stack
;	status return standard
;---
qa_ln
	moveq	#0,d0
qln_do
qln.reg reg	d1/d2/d3/d4/a2
	movem.l qln.reg,-(sp)
	move.l	d0,d3			 ; keep log10 flag
	move.w	(a1)+,d3
	move.l	(a1)+,d1		 ; mantissa
	ble.l	qln_ovfl		 ; oops

	sub.w	#$0800,d3		 ; log of number in range 0.5 ... 1

	cmp.l	#$5a82799a,d1		 ; >sqrt(1/2)?
	bgt.s	qln_uprng		 ; ... yes, upper range
	subq.w	#1,d3			 ; ... no, lower range, adjust exp
	bclr	#$1e,d1 		 ; and set our polynomial value
	move.l	d1,d0
	beq.s	qln_0			 ; ln=0
	bset	#$1f,d0
	bsr	qln_uldiv		 ; #z
	bra.s	qln_nrmz
qln_uprng
	bset	#$1f,d1
	move.l	d1,d0
	neg.l	d1
	bsr	qln_uldiv		 ; -#z
	neg.l	d1			 ; #z

qln_nrmz
	move.w	#$0802,d2		 ; unnormalised #z
qln_nrz1
	subq.w	#1,d2			 ; ... normalize
	add.l	d1,d1
	bvc.s	qln_nrz1
	roxr.l	#1,d1			 ; restore #z

	jsr	qa_pushd
	jsr	qa_pushd
	jsr	qa_pushd
	jsr	qa_muld 		 ; #w, #z, #z

	move.w	(a1)+,d2
	move.l	(a1)+,d1		 ; #w in d1.d2
	lea	qln_tab,a2
	bsr.s	qln_coef		 ; #a1
	jsr	qa_muld 		 ; #a1*w
	bsr.s	qln_coef
	jsr	qa_add			 ; #a0+a1*w
	jsr	qa_muld 		 ; #w*(a0+a1*w)
	bsr.s	qln_coef		 ; #b0
	jsr	qa_addd 		 ; #b0+b1*w   (b1=1)
	jsr	qa_div			 ; #w*A/B
	jsr	qa_mul			 ; #z*w*A/B
	jsr	qa_add			 ; #z+z*w*A/B
	bra.s	qln_e10

qln_0
	moveq	#0,d2
	jsr	qa_pushd		 ; push zero log
	lea	qln_etab,a2

qln_e10
	add.w	d3,d3			 ; use double exponent for ln
	tst.l	d3			 ; l10?
	bpl.s	qln_addexp		 ; ... no
	asr.w	#1,d3			 ; ... yes, normal exponent for l10
	subq.l	#6,a2			 ; use l10 corrections
	bsr.s	qln_coef		 ; correct the ln
	jsr	qa_mul

qln_addexp
	tst.w	d3			 ; any exponent to float
	beq.s	qln_exit		 ; ... no need
	move.w	d3,-(a1)		 ; float exponent
	jsr	qa_float
	jsr	qa_dup
	addq.l	#6,a1
	sub.w	#2,(a1) 		 ; final bit is 0.25*exponent
	jsr	qa_swap
	subq.l	#6,a1
	bsr.s	qln_coef
	jsr	qa_mul			 ; exponent*(log(2)-0.25)
	jsr	qa_add			 ; ... + log (mantissa)
	jsr	qa_add			 ; ... + exponent*0.25
qln_exit
	movem.l (sp)+,qln.reg
	rts

qln_coef
	move.l	-(a2),-(a1)		 ; push coefficient
	move.w	-(a2),-(a1)
	rts

qln_ovfl
	moveq	#err.ovfl,d0
	bra.s	qln_exit

	dc.w	$07FC,$6882,$6A13    ; l10(2)-.25
	dc.w	$07FF,$6F2D,$EC55    ; l10(e)
	dc.w	$07FD,$62E4,$2FF0    ; ln(2)/2-.25
qln_etab
	dc.w	$0803,$A6BC,$EEE1    ; b1
	dc.w	$07FF,$88FB,$E7C1    ; a0
	dc.w	$07FA,$6F6B,$44F2    ; a1
qln_tab
	end
