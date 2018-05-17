	section gw
* miscellaneous arithmetic
	xdef	RI_ABEX,RI_ABEXB,RI_K_B

	include 'dev8_keys_qlv'

ri_abex
	movem.l A5/A6,-(SP)	   absolute A1 and A4 entry for EXEC
	move.w	qa.op,a5
	bra.s	ri_op

ri_abexb
	movem.l A5/A6,-(SP)	   absolute A1 and A4 entry for EXECB
	move.w	qa.mop,a5
ri_op
	sub.l	a6,a6
	jsr	(a5)
	movem.l (SP)+,A5/A6
	rts

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

ri_k_b				;	exp	D0
	move.w	#$07F0,-6(A6,A1.L)	07F0	xxhl
	or.b	D0,-5(A6,A1.L)		07Fl
	lsr.b	#4,D0				xx0h
	ext.w	D0				000h
	add.w	D0,-6(A6,A1.L)		done
	lsl.b	#2,D0
	move.l	KTAB-8*4(PC,D0.W),D0 mantissa
push
	subq.l	#6,A1
put
	move.l	D0,2(A6,A1.L)
ok
	moveq	#0,D0
	rts


*		Mantissa  Exp Err  What     Value	  Reference
;	dc.l	$???????? ??? +.?? ???????? 0.??????????? $00-$4F
	dc.l	$477D1A89 7FB +.29 PI/180   0.01745329252 $56 RI.PI180
	dc.l	$6F2DEC55 7FF -.41 LOG10(e) 0.4342944819  $69 RI.LOGE
	dc.l	$430548E1 800 -.29 PI/6     0.5235987756  $79 RI.PI6
ktab
	dc.l	$58B90BFC 800 -.09 LN(2)    0.6931471806  $88 RI.LN2
	dc.l	$6ED9EBA1 801 +.38 SQRT(3)  1.732050808   $98 RI.SQRT3
	dc.l	$6487ED51 802 +.07 PI	    3.141592654   $A8 RI.PI $A7=RI.PI2
;	dc.l	$???????? ??? +.?? ???????? ?.?????????   $B0-$FF

	end
