* 32-bit integer arithmetic		v0.01   Mar 1988  J.R.Oakley  QJUMP
*
	section cv
*
	include 'dev8_keys_68000'
*
	xdef	cv_uldiv
	xdef	cv_sldiv
	xdef	cv_ulmul
	xdef	cv_slmul
*+++
* Divide a 32-bit signed dividend by a 32-bit signed divisor, giving
* similar quotient and remainder.  The rules are that the remainder has
* the same sign as the divisor.
*
*	Registers:
*		Entry				Exit
*	D0	divisor 			quotient
*	D1	dividend			remainder
*---
cv_sldiv
divsreg reg	d2/d3/d6/d7
	movem.l divsreg,-(sp)
	moveq	#0,d6			; assume both numbers are positive
	move.l	d0,d7			; keep and check divisor
	bpl.s	sdv_chkd		; it is positive, no fixes
	neg.l	d0			; make it positive
	addq.w	#2,d6			; and flag it negative
sdv_chkd
	tst.l	d1
	bpl.s	sdv_usdv		; dividend is positive
	neg.l	d1			; it isn't
	addq.w	#4,d6			; so flag it
sdv_usdv
	bsr.s	usdiv			; do the division
	beq.s	sdv_sign		; OK, just set the signs
	bvs.s	sdv_exit		; ...oops
	move.w	sdv_fixt(pc,d6.w),d6	; how to fix result
	jmp	sdv_fixt(pc,d6.w)	; do it

sdv_sign
	move.w	sdv_sigt(pc,d6.w),d6	; how to fix result
	jmp	sdv_sigt(pc,d6.w)	; do it
sdv_sigt
	dc.w	sdv_exit-sdv_sigt
	dc.w	sdv_sqnr-sdv_sigt
	dc.w	sdv_sgnq-sdv_sigt
	dc.w	sdv_negr-sdv_sigt

sdv_sgnq
	neg.l	d1			; remainder same sign as divisor
sdv_sqnr
	neg.l	d0			; result is negative
sdv_exit
	movem.l (sp)+,divsreg
	rts

sdv_fixt
	dc.w	sdv_exit-sdv_fixt
	dc.w	sdv_fqnr-sdv_fixt
	dc.w	sdv_fixq-sdv_fixt
	dc.w	sdv_negr-sdv_fixt
sdv_fqnr
	neg.l	d1			; remainder same sign as divisor
sdv_fixq
	addq.l	#1,d0
	neg.l	d0			; different signs, result is negative
	sub.l	d7,d1			; and the remainder is different
sdv_negr
	neg.l	d1
	movem.l (sp)+,divsreg
	rts
*+++
* Divide a 32-bit dividend by a 32-bit divisor, yielding a
* 32-bit quotient and remainder - this cannot overflow, but may
* attempt to divide by zero.
*
*	Registers:
*		Entry				Exit
*	D0	divisor 			quotient
*	D1	dividend			remainder
*---
cv_uldiv
divureg reg	d2/d3
	movem.l divureg,-(sp)
	bsr.s	usdiv			; do the divide
	movem.l (sp)+,divureg
	rts
*
usdiv
	tst.l	d0			; dividing by zero?
	beq.s	exov			; some people!
	moveq	#1,d3			; amount to add...
	moveq	#0,d2			; ...to answer
	tst.l	d0
lsloop
	bmi.s	chksub			; as big as we can get now
	cmp.l	d0,d1			; divisor bigger?
	blo.s	do_div			; yes, start dividing
	add.l	d3,d3			; no, increase quotient increment
	add.l	d0,d0
	bra.s	lsloop
do_div
	lsr.l	#1,d0			; divisor/2
	lsr.l	#1,d3			; and quotient increment
	beq.s	exok			; no increment, done
chksub
	cmp.l	d0,d1			; can we subtract?
	blo.s	do_div			; not yet
	sub.l	d0,d1			; yes
	add.l	d3,d2			; so we can increment quotient
	bra.s	do_div
exok
	move.l	d2,d0			; return quotient
	tst.l	d1			; clear V, set Z if no remainder
exit
	rts
exov
	move	#sr.v,ccr		; set V, clear Z
	bra.s	exit
*+++
* Multiply two 32-bit signed numbers, giving a 64-bit signed result
*
*	Registers:
*		Entry				Exit
*	D0	multiplier			result, LSLW
*	D1	multiplicand			result, MSLW
*---
cv_slmul
smulreg reg	d3-d6
	movem.l smulreg,-(sp)
	move.l	d0,d6			; is multiplier negative?
	bpl.s	usm_chka		; no
	neg.l	d0
usm_chka
	eor.l	d1,d6			; get sign of result
	tst.l	d1			; is multiplicand negative?
	bpl.s	usm_mult		; no
	neg.l	d1			; not any more
usm_mult
	bsr.s	usmul
	tst.l	d6			; different signs?
	bpl.s	usm_exit		; no, finished
	neg.l	d0			; negate...
	negx.l	d1			; ...the 64-bit result
usm_exit
	movem.l (sp)+,smulreg
	rts
*+++
* Multiply two 32-bit unsigned numbers, giving a 64-bit result
*
*	Registers:
*		Entry				Exit
*	D0	multiplier			result, LSLW
*	D1	multiplicand			result, MSLW
*---
cv_ulmul
usmreg	reg	d3-d5
	movem.l usmreg,-(sp)
	bsr.s	usmul
	movem.l (sp)+,usmreg
	rts
*
usmul
	move.l	d0,d3			; copy multiplier
	move.l	d1,d4			; and multiplicand
	moveq	#0,d5			; upper LW starts as zero
	moveq	#0,d0			; result is
	moveq	#0,d1			; also zero
mullp
	lsr.l	#1,d3			; get a bit
	bcc.s	noadd			; there wasn't one
	add.l	d4,d0			; add to LSLW
	addx.l	d5,d1			; and with carry to MSLW
noadd
	tst.l	d3			; end of multiplier?
	beq.s	mulexit 		; yes
	add.l	d4,d4			; no, double...
	addx.l	d5,d5			; ...the multiplicand
	bra.s	mullp			; next bit
mulexit
	rts
*
	end
