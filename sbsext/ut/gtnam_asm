* Get a string on the stack   V0.6    1985  Tony Tebby   QJUMP
*
* 2006-08-29  0.6  Fixed return value for sliced strings (MK)
*
	section utils
*
	xdef	ut_gtnm1	get one name/string (however many params)
	xdef	ut_gxnm1	get exactly one name/string
	xdef	ut_gtnam	get name/string (no check)
	xdef	ut_gnmpt	get name using a3 as indirect pointer
	xdef	ut_ntnam	get name from name table
*
	xref	ut_par1
	xref	ut_ckri6
	xref	ut_gnst1
	xref	ut_remst
	xref	ut_nmtos
*
	include dev8_sbsext_ext_keys
*
*	d0  r	error return
*	d1   s
*	d2   s
*	d3   s
*	d4-d7 p
*	a0    p
*	a1  r	pointer to first param on stack
*	a2   s
*	a3 c  p pointer to first param in name table
*	a4    p
*	a5 c  p pointer beyond last param in name table
*
ut_gxnm1
	bsr.l	ut_par1 		; check for exactly one parameter
ut_gtnm1
	cmp.l	a3,a5			; is there at least one param?
	bgt.s	ut_gtnam		; yes, there is
ut_gtnul
	bsr.l	ut_ckri6		; make room for string
	sub.l	#2,a1			; total length 2
	clr.w	(a6,a1.l)		; zero length
	bra.s	utgn_ok
*
ut_gtnam
	moveq	#$f,d0			; extract type of name
;;;	   and.b   1(a6,a3.l),d0
	move.w	(a6,a3.l),d1						    0.6
	cmp.w	#$0300,d1		; substring?			    0.6
	beq.s	ut_gtnst		; ... yes, get it		    0.6
	and.b	d1,d0							    0.6
	beq.s	ut_gtnul		; a null!!
*
	tst.w	2(a6,a3.l)		; is there a name?
	bmi.s	ut_gtnst		; ... no, always get string

	subq.b	#1,d0			; is it a string?
	bne.s	ut_gnmpt		; ... no, get the name instead

ut_gtnst
	bsr.s	ut_gnst1		; get one string (no check)
	bne.s	utgn_rts
	bsr.l	ut_remst		; remove string
	bra.s	utgn_ok
*
ut_gnmpt
	moveq	#0,d1
	move.w	2(a6,a3.l),d1		; get the pointer to the real entry
	blt.s	ut_gtnul		; ... expression is no good
	lsl.l	#3,d1			; in multiples of 8 bytes
	add.l	bv_ntbas(a6),d1
ut_ntnam
	moveq	#0,d3
	move.w	2(a6,d1.l),d3		; thus the pointer to the name
	add.l	bv_nlbas(a6),d3
	bsr.l	ut_nmtos		; put name on stack
utgn_ok
	moveq	#0,d0
utgn_rts
	rts
	end
