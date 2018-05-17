; QL Arithmetic Power Floating Point	 V2.01	 1990	Tony Tebby  QJUMP

	section qa

	xdef	qa_powfp
	xdef	qa_powfd

	xref	qa_powin
	xref	qa_int
	xref	qa_flotd
	xref	qa_divd
	xref	qa_muld
	xref	qa_add
	xref	qa_addd
	xref	qa_dup
	xref	qa_swap
	xref	qa_polyo
	xref	qa_poly

	include 'dev8_keys_err'

;+++
; QL Arithmetic: TOS^D1.D2
;
;	Raises TOS to the power of D1 exp D2
;
;	Uses Cody and Waite POWER floating point binary.
;
;	d0  r	error return 0 or ERR.OVFL
;	a1 c  u pointer to arithmetic stack
;	status return standard
;---
qa_powfd
	move.l	d1,-(a1)
	move.w	d2,-(a1)		 ; not very efficient this
;+++
; QL Arithmetic: NOS^TOS
;
;	POPS the FP at TOS and raises NOS to the power of it.
;
;	Uses Cody and Waite POWER floating point binary.
;
;	d0  r	error return 0 or ERR.OVFL
;	a1 c  u pointer to arithmetic stack
;	status return standard
;---
qa_powfp
qpf.reg reg	d1/d2/d3/d4/d5/d6/d7/a2
	move.w	(a1),d0 		 ; exponent
	beq.s	qpf_zpwr
	sub.w	#$0800,d0		 ; could be integer?
	ble.s	qpf_float		 ; ... no
	cmp.w	#8,d0			 ; above this float will be faster
	bgt.s	qpf_float
	move.l	d1,-(sp)
	move.l	2(a1),d1
	add.l	d1,d1
	lsl.l	d0,d1			 ; any remainder bits?
	movem.l (sp)+,d1
	bne.s	qpf_float		 ; ... yes
qpf_int
	jsr	qa_int
	jmp	qa_powin		 ; raise to integer power

qpf_zpwr
	tst.l	2(a1)			 ; true zero?
	beq.s	qpf_int 		 ; ... yes

qpf_float
	movem.l qpf.reg,-(sp)
	move.w	(a1)+,d7
	move.l	(a1)+,d6		 ; save power

	move.w	(a1)+,d4		 ; exponent
	move.l	(a1)+,d3		 ; mantissa
	bgt.s	qpf_n.3 		 ; ... ok
	blt.l	qpf_erov		 ; ... oops
	tst.l	d6			 ; zero to negative exponent
	bgt.l	qpf_ex6 		 ; ... no
	bra.l	qpf_erov		 ; ... yes

qpf_n.3
	sub.w	#$0800,d4		 ; #m true (non-offset exponent)
qpf_n.5 				 ; #r=0 #g=d3
	add.l	d3,d3			 ; #g*2
qpf_n.6
	lea	qpf_tab,a2
	moveq	#4,d5			 ; #p
	cmp.l	8*4-4(a2,d5.w),d3
	bhi.s	qpf_p4
	moveq	#9*4,d5
qpf_p4
	cmp.l	4*4-4(a2,d5.w),d3
	bhi.s	qpf_p2
	add.w	#4*4,d5
qpf_p2
	cmp.l	2*4-4(a2,d5.w),d3
	bhi.s	qpf_n.7
	addq.w	#2*4,d5

qpf_n.7 				 ; #p (*4) now set
	move.l	(a2,d5.w),d0
	move.l	d3,d1
	moveq	#0,d2			 ; assume #g=a
	sub.l	d0,d1			 ; #g-a
	beq.s	qpf_dvga
	move.w	#$801,d2		 ; exponent of #g-a
qpf_nrga
	subq.w	#1,d2
	add.l	d1,d1
	bvc.s	qpf_nrga

qpf_rmga
	roxr.l	#1,d1			 ; restore mantissa
qpf_dvga
	move.l	d1,-(a1)
	move.w	d2,-(a1)

	add.l	d0,d3			 ; #g+a
	roxr.l	#1,d3			 ; remove overflow
	lsr.l	#1,d3			 ; normalised form
	move.l	d3,d1
	move.w	#$801,d2		 ; just greater than one
	jsr	qa_divd 		 ; #z on stack

qpf_n.8
	jsr	qa_dup
	lea	qpf_pcoef,a2		 ; p coefficients
	jsr	qa_polyo		 ; odd terms only #R(z)+z*K on stack
	jsr	qa_add			 ; #U2

qpf_n.9
	move.l	d6,d1
	move.w	d7,d2			 ; #Y
	jsr	qa_muld 		 ; #U2*Y

	move.w	d4,d1
	lsl.w	#4,d1			 ; #m*16
	lsr.w	#2,d5			 ; #p
	sub.w	d5,d1			 ; #m*16-p
	jsr	qa_flotd
	subq.w	#4,(a1) 		 ; #U1, #U2*Y on stack
	jsr	qa_dup			 ; #U1, #U1, #U2*Y
	move.l	d6,d1
	move.w	d7,d2			 ; reduce #Y
	bsr.l	qpf_redu
	exg	d4,d2
	exg	d3,d1
	jsr	qa_muld 		 ; #U1*Y2, #U1, #U2*Y
	jsr	qa_swap 		 ; #U1, #U1*Y2, #U2*Y
	addq.l	#6,a1
	jsr	qa_add			 ; #W=U2*Y+U1*Y2
	sub.w	#6*2,a1 		 ; #U1, (...), #W
	move.w	d4,d2
	move.l	d3,d1
	jsr	qa_muld 		 ; #U1*Y1, (...), #W
	move.w	6*2(a1),d2
	move.l	6*2+2(a1),d1
	bsr.l	qpf_redu		 ; reduce #W
	move.w	d4,6(a1)
	move.l	d3,6+2(a1)		 ; #U1*Y1, #W2, #W
	jsr	qa_addd 		 ; #W'=W1+U1*Y1, #W2, #W
	bsr.l	qpf_redp		 ; reduce #W'
	move.w	d2,6(a1)
	move.l	d1,6+2(a1)		 ; #W2, #W'1
	exg	d2,d4
	exg	d1,d3
	jsr	qa_addd 		 ; #W'2=W2+W'-W'1, #W'1
	bsr.s	qpf_redp		 ; reduce #W'2
	jsr	qa_addd 		 ; #W'1+W"
	addq.w	#4,(a1) 		 ; *16
	jsr	qa_int
qpf_n.10.11
	bne.s	qpf_exit		 ; ... oops
	move.w	(a1)+,d7		 ; #IW1
					 ; #W''2 is in d3,d4
qpf_n.13
	move.l	d3,-(a1)		 ; #W''2 onto stack
	move.w	d4,-(a1)
	tst.l	d3			 ; <=0?
	ble.s	qpf_setm		 ; ... yes
	addq.w	#1,d7
	bvs.s	qpf_erov
	move.w	#$7fc,d2
	move.l	#$80000000,d1		 ; subtract 1/16
	jsr	qa_addd
qpf_setm
	move.w	d7,d0			 ; keep #IW1
	asr.w	#4,d7			 ; #m'
	addq.w	#1,d7			 ; ... adjusted to make p>0
qpf_setp
	move.w	d7,d6
	asl.w	#4,d6
	sub.w	d0,d6			 ; #p'

qpf_n.14
	lea	qpf_qcoef,a2
	jsr	qa_poly 		 ; #Z

qpf_n.15
	lsl.w	#2,d6			 ; #p*4
	beq.s	qpf_add1		 ; #A1(p+1) is 1
	move.l	qpf_tab(pc,d6.w),d1	 ; #A1(p+1)
	move.w	#$800,d2
	addq.l	#1,d1			 ; round up
	lsr.l	#1,d1			 ; normalised
	jsr	qa_muld 		 ; #A1(p+1)*Z
	bra.s	qpf_ada1
qpf_add1
	move.l	#$40000000,d1		 ; add 1
	move.w	#$801,d2

qpf_ada1
	jsr	qa_addd 		 ; Z=#A1(p+1)+A1(p+1)*Z

	add.w	d7,(a1) 		 ; correct exponenet

qpf_exok
	moveq	#0,d0
qpf_exit
	movem.l (sp)+,qpf.reg
	rts

qpf_ex6
	subq.l	#6,a1			 ; keep zero
	bra.s	qpf_exok

qpf_ovx4
	addq.l	#4,sp			 ; remove return
qpf_erov
	moveq	#err.ovfl,d0
	bra.s	qpf_exit


qpf_redp
	move.w	(a1)+,d2		; pop number to reduce
	move.l	(a1)+,d1
; d1,d2 call fp
; d3,d4 return < 1/16
; d1,d2 return reduced by d3,d4
qpf_redu
	move.w	d2,d4			 ; a copy
	move.l	d1,d3
	beq.s	qpf_rts 		 ; 0,0

	move.w	#$81b,d0		 ; scrub all but part >1/16
	sub.w	d2,d0
	blt.s	qpf_ovx4		 ; ... oops, none
	cmp.w	#31,d0			 ; all <1/16?
	bge.s	qpf_zero
	asr.l	d0,d1
	asl.l	d0,d1			 ; ms bit
	sub.l	d1,d3			 ; all that is left
	beq.s	qpf_uflow		 ; ... nothing

	move.w	#$07fc,d4
	move.w	d2,d0
	sub.w	d4,d0
	asl.l	d0,d3			 ; pre-normalise

	move.l	d3,d0			 ; re-normalise
qpf_rnlp
	add.l	d0,d0			 ; double up
	bvs.s	qpf_rts 		 ; ... oops, done
	subq.w	#1,d4			 ; decrease exponent
	blt.s	qpf_uflow		 ; no more
	move.l	d0,d3
	bra.s	qpf_rnlp

qpf_uflow
	moveq	#0,d4			 ; underflowed
qpf_rts
	rts
qpf_zero
	moveq	#0,d2			 ; none >1/16
	moveq	#0,d1
	rts

qpf_tab
	dc.l	$ffffffff
	dc.l	$F5257D15
	dc.l	$EAC0C6E8
	dc.l	$E0CCDEEC
	dc.l	$D744FCCB
	dc.l	$CE248C15
	dc.l	$C5672A11
	dc.l	$BD08A39F
	dc.l	$B504F334
	dc.l	$AD583EEA
	dc.l	$A5FED6AA
	dc.l	$9EF53261
	dc.l	$9837F052
	dc.l	$91C3D374
	dc.l	$8B95C1E4
	dc.l	$85AAC368
	dc.l	$80000000

	dc.w	$0000,$0000,$0000		 ; q0
	dc.w	$0800,$58B9,$0BFB		 ; q1
	dc.w	$07FE,$7AFE,$F7F7		 ; q2
	dc.w	$07FC,$71AC,$1B53		 ; q3
	dc.w	$07FA,$4EC6,$A59B		 ; q4
	dc.w	$07F7,$558A,$8C38		 ; q5
	dc.w	5				 ; order 5
qpf_qcoef

	dc.w	$07FF,$7154,$7652
	dc.w	$07FD,$7B1C,$26FD
	dc.w	$07FB,$49E7,$80B7
	dc.w	5				 ; order 5
qpf_pcoef

	end
