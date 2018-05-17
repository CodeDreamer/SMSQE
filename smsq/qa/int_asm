; QL Arithmetic INT	 V2.01	 1990	Tony Tebby  QJUMP

	section qa

	xdef	qa_int
	xdef	qa_nint
	xdef	qa_nlint

	xdef	qa_nintd



	include 'dev8_keys_err'

;+++
; QL Arithmetic: NINTD
;
;	This routine converts float D2.w,D1.l to INT D1.w  (internal)
;
;	d0  r	error return 0 or ERR.OVFL
;	status return standard
;---
qa_nintd
	swap	d1			 ; only msw is interesting
	bpl.s	qnd_shift		 ; unless we are rounding
	addq.w	#1,d1			 ; round up
	bvc.s	qnd_shift		 ; no overflow
	roxr.w	#1,d1			 ; recover overflow bit
	addq.w	#1,d2

qnd_shift
	sub.w	#$80f,d2		 ; integer in range?
	beq.s	qnd_ok			 ; ... no shift
	bgt.s	qnd_ovfl
	neg.w	d2
	cmp.w	#$10,d2 		 ; too big a shift?
	ble.s	qnd_sd2
	moveq	#$10,d2 		 ; sign only
qnd_sd2
	asr.w	d2,d1
qnd_ok
	moveq	#0,d0
	rts
qnd_ovfl
	moveq	#err.ovfl,d0
	rts

;+++
; QL Arithmetic: INT
;
;	This routine pops the FLOAT at TOS, truncates to INT and pushes it.
;
;	d0  r	error return 0 or ERR.OVFL
;	a1 c  u pointer to arithmetic stack  (points to rubbish int on error)
;	status return standard
;---
qa_int
	moveq	#-1,d0			 ; flag INT
	bra.s	qai_word
;+++
; QL Arithmetic: NINT
;
;	This routine pops the FLOAT at TOS, rounds to NINT and pushes it.
;
;	d0  r	error return 0 or ERR.OVFL
;	a1 c  u pointer to arithmetic stack  (points to rubbish int on error)
;	status return standard
;---
qa_nint
qai.reg reg	d1/d2
	moveq	#0,d0			 ; flag NINT
qai_word
	movem.l qai.reg,-(sp)
	move.w	(a1)+,d2
	move.w	(a1)+,d1		 ; the msw FP

	sub.w	#$80f,d2		 ; integer in range?
	beq.s	qai_nsft		 ; ... no shift
	bgt.s	qai_ovfl
	neg.w	d2
	cmp.w	#$10,d2 		 ; too big a shift?
	ble.s	qai_sd2
	moveq	#$11,d2 		 ; just keep bit
qai_sd2
	asr.w	d2,d1
	tst.b	d0			 ; rounding required?
	bne.s	qai_pshw		 ; ... no
	roxl.w	#1,d0
	add.w	d0,d1			 ; ... yes, round it
	bvc.s	qai_pshw
	bra.s	qai_ovfl
qai_nsft
	tst.b	d0			 ; rounding required?
	bne.s	qai_pshw
	tst.w	(a1)			 ; ... yes, check next sig bit down
	bpl.s	qai_pshw		 ; ... not set
	addq.w	#1,d1
	bvs.s	qai_ovfl

qai_pshw
	move.w	d1,(a1) 		 ; set integer
	moveq	#0,d0
qai_exit
	movem.l (sp)+,qai.reg
	rts
qai_ovfl
	moveq	#err.ovfl,d0
	bra.s	qai_exit

;+++
; QL Arithmetic: NLINT
;
;	This routine pops the FLOAT at TOS, rounds to LONG INT and pushes it.
;
;	d0  r	error return 0 or ERR.OVFL
;	a1 c  u pointer to arithmetic stack
;	status return standard
;---
qa_nlint
	movem.l qai.reg,-(sp)
	move.w	(a1)+,d2
	move.l	(a1),d1 		 ; the FP

	sub.w	#$81f,d2		 ; integer in range?
	beq.s	qai_pshl		 ; ... no shift
	bgt.s	qai_ovfl
	neg.w	d2
	cmp.w	#$20,d2 		 ; too big a shift?
	bge.s	qai_zerl
	asr.l	d2,d1
	moveq	#0,d0
	roxl.w	#1,d0			 ; get underflowed bit
	add.l	d0,d1			 ; ... and round it
	bra.s	qai_pshl

qai_zerl
	moveq	#0,d1
qai_pshl
	move.l	d1,(a1) 		 ; set long integer
	moveq	#0,d0
	bra.s	qai_exit
	end
