; QL Arithmetic Raise to Power	V2.01   1990  Tony Tebby  QJUMP

	section qa

	xdef	qa_power
	xdef	qa_powin

	xref	qa_square
	xref	qa_dup
	xref	qa_mul
	xref	qa_div
	xref	qa_recip
	xref	qa_swap
	xref	qa_put1
;+++
; QL Arithmetic: Raise to Power (positive or negative)
;
;	This routine pops the integer at TOS and raises NOS to that power
;
;	d0  r	error return 0 or ERR.OVFL
;	a1 c  u pointer to arithmetic stack
;	status return standard
;---
qa_powin
	tst.w	(a1)			 ; negative power?
	bge.s	qa_power		 ; ... no
	neg.w	(a1)			 ; positive now
	bsr.s	qa_power
	beq.l	qa_recip
	rts

;+++
; QL Arithmetic: Raise to Power (positive only)
;
;	This routine pops the integer at TOS and raises NOS to that power
;
;	d0  r	error return 0 or ERR.OVFL
;	a1 c  u pointer to arithmetic stack
;	status return standard
;---
qa_power
	move.w	(a1)+,d0		 ; get power to raise
	beq.l	qa_put1

	tst.w	(a1)			 ; zero exponent?
	bne.s	qpi_do			 ; ... no
	tst.l	2(a1)			 ; zero mantissa?
	bne.s	qpi_do			 ; ... yes
	moveq	#0,d0			 ; ... done
	rts

qpi_do
	move.l	d1,-(sp)
	moveq	#0,d1
	move.w	d0,d1			 ; nothing yet

	jsr	qa_dup			 ; duplicate
	bra.s	qpi_next

qpi_loop
	jsr	qa_square		 ; square multiplier
	bne.s	qpi_exit		 ; ... oops
qpi_next
	lsr.w	#1,d1
	bcc.s	qpi_eloop
	bset	#31,d1			 ; any value set yet?
	bne.s	qpi_muln
	move.w	(a1),6(a1)
	move.l	2(a1),8(a1)		 ; start off with this value
	bra.s	qpi_eloop
qpi_muln
	jsr	qa_mul			 ; multiply
	bne.s	qpi_exit		 ; ... oops
	subq.l	#6,a1			 ; but keep value
qpi_eloop
	tst.w	d1
	bne.s	qpi_loop

	addq.l	#6,a1			 ; pop multiplier

qpi_exit
	move.l	(sp)+,d1
	tst.l	d0
qpi_rts
	rts

	end
