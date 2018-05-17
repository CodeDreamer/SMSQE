; QL Arithmetic (Rel A6) Operations    V2.02   1990  Tony Tebby  QJUMP
;
; 2006-10-04  2.02  Fixed load/store for addresses below $80 (MK)

	section qa

	xdef	qr_op
	xdef	qr_mop
	xdef	qa_op
	xdef	qa_mop

	include 'dev8_keys_qlv'
	include 'dev8_keys_err'

;+++
; QL Arithmetic: Process Multiple operation list
;
;	d0  r	error return
;	a1 c  u pointer to arithmetic stack
;	a3 c  p pointer to operation list (absolute)
;	a4 c  p pointer to (top of) variables area
;	a6	base address
;	status return standard
;---
qr_mop
	add.l	a6,a1
	add.l	a6,a4
	bsr.s	qa_mop

	sub.l	a6,a4
	sub.l	a6,a1
	rts

;+++
; QL Arithmetic: Process Multiple operation list
;
;	d0  r	error return
;	a1 c  u pointer to arithmetic stack (absolute)
;	a3 c  p pointer to operation list (absolute)
;	a4 c  p pointer to (top of) variables area (absolute)
;	status return standard
;---
qa_mop
	move.l	a3,-(sp)
	moveq	#0,d0
	bra.s	qam_eloop
qam_loop
	bsr.s	qa_op
	bne.s	qam_exit
qam_eloop
	move.b	(a3)+,d0
	bne.s	qam_loop

qam_exit
	move.l	(sp)+,a3
	rts

;+++
; QL Arithmetic: do operation
;
;	d0 cr	operation code error return
;	a1 c  u pointer to arithmetic stack
;	a4 c  p pointer to (top of) variables area
;	a6	base address
;	status return standard
;---
qr_op
	add.l	a6,a1
	add.l	a6,a4
	bsr.s	qa_op

	sub.l	a6,a4
	sub.l	a6,a1
	rts

;+++
; QL Arithmetic: do operation
;
;	d0 cr	operation code error return
;	a1 c  u pointer to arithmetic stack
;	a4 c  p pointer to (top of) variables area
;	a6	base address
;	status return standard
;---
qa_op
	cmpi.b	#qa.maxop,d0		 ; operation?
	bhi.s	qao_ldst		 ; ... no
	ext.w	d0
	add.w	d0,d0
	add.w	qao_tab(pc,d0.w),d0
	jmp	qao_tab(pc,d0.w)	 ; ... do it

qao_ldst
	ori.w	#$ff00,d0		 ; always negative
	bclr	#0,d0			 ; load or store
	beq.s	qao_load
	move.w	(a1)+,(a4,d0.w)
	move.l	(a1)+,2(a4,d0.w)	 ; store
	bra.s	qao_ok
qao_load
	move.l	2(a4,d0.w),-(a1)	 ; load
	move.w	(a4,d0.w),-(a1)
qao_ok
	moveq	#0,d0
	rts

rel	macro	routine
	xref	[routine]
	dc.w	[routine]-*
	endm
nimp	macro
	dc.w	qao_nimp-*
	endm

qao_tab
	dc.w	qao_ok-*	   ; 00
	rel	qa_push1
	rel	qa_nint
	rel	qa_pushz
	rel	qa_int		   ; 04
	rel	qa_pushn
	rel	qa_nlint
	rel	qa_pushk
	rel	qa_float	   ; 08
	rel	qa_lfloat
	rel	qa_add
	nimp
	rel	qa_sub		   ; 0C
	rel	qa_halve
	rel	qa_mul
	rel	qa_double
	rel	qa_div		   ; 10
	rel	qa_recip
	rel	qa_abs
	rel	qa_roll
	rel	qa_neg		   ; 14
	rel	qa_over
	rel	qa_dup
	rel	qa_swap
	rel	qa_cos		   ; 18
	nimp
	rel	qa_sin
	nimp
	rel	qa_tan		   ; 1C
	nimp
	rel	qa_cot
	nimp
	rel	qa_asin 	   ; 20
	nimp
	rel	qa_acos
	rel	qa_atan2
	rel	qa_atan 	   ; 24
	nimp
	rel	qa_acot
	nimp
	rel	qa_sqrt 	   ; 28
	rel	qa_square
	rel	qa_ln
	nimp
	rel	qa_log10	   ; 2C
	nimp
	rel	qa_exp
	nimp
	rel	qa_powfp	   ; 30
	nimp
	rel	qa_pi

qao_nimp
	moveq	#err.nimp,d0
	rts

	end
