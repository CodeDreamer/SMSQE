; Trig etc  functions	V2.00	   1990  Tony Tebby   QJUMP
;
	section exten

	xdef	int
	xdef	abs
	xdef	cos
	xdef	sin
	xdef	tan
	xdef	cot
	xdef	asin
	xdef	acos
	xdef	atan
	xdef	acot
	xdef	sqrt
	xdef	ln
	xdef	log10
	xdef	exp
	xdef	pi
	xdef	rad
	xdef	deg

	xdef	pi_d180

	xref	ut_gtfp
	xref	ut_gxfp1
	xref	ut_par0
	xref	ut_ckri6
	xref	ut_chkri
	xref	ut_retin
	xref	ut_retfp

	include 'dev8_keys_err'
	include 'dev8_keys_sbasic'
	include 'dev8_keys_qlv'

trig	macro	name
[name]	moveq	#qa.[name],d7
	bra.s	trig
	endm

	trig	abs
	trig	cos
	trig	sin
	trig	tan
	trig	cot
	trig	asin
	trig	acos
	trig	acot
	trig	sqrt
	trig	ln
	trig	log10
	trig	exp

atan
	jsr	ut_gtfp
	bne.s	trig_rts
	moveq	#qa.atan,d7		 ; assume atan
	subq.w	#1,d3
	beq.s	trig_chkri
	moveq	#qa.atan2,d7		 ; try atan2
	subq.w	#1,d3
	beq.s	trig_chkri

trig_ipar
	moveq	#err.ipar,d0
trig_rts
	rts


trig
	jsr	ut_gxfp1		 ; one param only
	bne.s	trig_rts
trig_chkri
	moveq	#8*6,d1
	jsr	ut_chkri

	move.b	d7,d0
trig_do
	jsr	qa.op*3+qlv.off 	 ; do operation
	beq.l	ut_retfp		 ; and return
	rts

int
	jsr	ut_gxfp1		 ; one param only
	bne.s	trig_rts
	move.w	#$080f,d1
	sub.w	(a6,a1.l),d1		 ; shift right for int
	blt.s	int_long		 ; ... too far for int
	move.w	2(a6,a1.l),d0		 ; mantissa
	addq.l	#4,a1
	cmp.w	#16,d1
	ble.s	int_do
	moveq	#16,d1
int_do
	asr.w	d1,d0
	move.w	d0,(a6,a1.l)
	jmp	ut_retin		 ; and return

int_long
	add.w	#$10,d1 		 ; shift for mask
	ble.s	int_rtfp		 ; ... number much too large
	moveq	#-1,d0
	lsl.w	d1,d0
	and.w	d0,4(a6,a1.l)		 ; mask out rubbish bits
int_rtfp
	jmp	ut_retfp

pi
	jsr	ut_par0 		 ; no parameter
	move.l	sb_arthp(a6),a1
	moveq	#qa.pi,d0
	bra.s	trig_do

rad
	lea	pi_d180,a0		 ; smaller
	bra.s	rad_deg

deg
	lea	d180_pi,a0
rad_deg
	jsr	ut_gxfp1		 ; one param only
	jsr	ut_ckri6		 ; and some room
	subq.l	#6,a1			 ; space for multiple / divide
	move.w	(a0)+,(a6,a1.l)
	move.l	(a0)+,2(a6,a1.l)
	moveq	#qa.mul,d0
	bra.s	trig_do

pi_d180 dc.w	$07FB,$477D,$1A89
d180_pi  dc.w	 $0806,$7297,$706A
	end
