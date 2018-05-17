; SBAS_CMFPTK -	SBASIC compiler	process	floating point tokens

	section	sbas

	xdef	sb_cmneg

;+++
; This routine negates the floating point parser token at -2(a2). A2 is	updated.
;
;	standard return		zero
;
;	d1  r	mantissa or integer
;	d2  r	exponent
;	d6 c  p	first 2	bytes of token
;	a2 cr	pointer	to last	four bytes / end of token
;	status return arbitrary
;---
sb_cmneg
	move.w	#$0fff,d2
	and.w	d6,d2			 ; exponent
	move.l	(a2)+,d1		 ; the mantissa
	beq.s	sbcn_rts		 ; ... none

	blt.s	sbcn_neg		 ; neg to pos can overflow!!
	neg.l	d1			 ; negate
	btst	#30,d1			 ; unnormalised?
	beq.s	sbcn_rts		 ; ... no
	sub.w	#1,d2			 ; ... yes, smaller exponent please
	bge.s	sbcn_dble		 ; ... and double mantissa
	clr.w	d2			 ; exponent has	underflowed as well
	rts
sbcn_dble
	add.l	d1,d1
	rts

sbcn_neg
	neg.l	d1			 ; negate mantissa
	bvc.s	sbcn_rts		 ; ... ok
	lsr.l	#1,d1			 ; neg to pos overflows
	addq.w	#1,d2			 ; correct it
	cmp.w	#$1000,d2		 ; exponent overflowed?
	blo.s	sbcn_rts		 ; ... no

	add.l	d1,d1
	subq.l	#1,d1			 ; cure	the overflow
	subq.w	#1,d2			 ; restore exponent
sbcn_rts
	rts

	end
