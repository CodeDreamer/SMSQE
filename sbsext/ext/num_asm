; Odd numerical functions   V2.00    	1990  Tony Tebby   QJUMP
;
;	PRTY_SET nbytes, parity area	   fill area with parity bytes
;
;	PURE (number, nr bits zero)	   set trailing bits to zero
;	FPHEX$ (number) 		   hex representation of FP
;	HEXFP (string)			   FP in hex representation
;	BDIFF (number, number)		   difference in bits
;	FDIFF (number, number)		   floating point difference
;
;	RND_MSB% (old%, mask%, parity area) next rnd number (new MSB)
;	RND_SEQL (seed%, mask%, parity area, 65536 words) length of sequence
;	RND_SEQE (seed%, mask1$, mask2%, parity area) equality of sequences
;
	section exten

	xdef	pure
	xdef	fphex$
	xdef	hexfp
	xdef	bdiff
	xdef	fdiff

	xdef	prty_set
	xdef	rnd_msb
	xdef	rnd_seql
	xdef	rnd_seqe
	xref	ut_gxin1		 ; get integer
	xref	ut_gxin2
	xref	ut_gtint
	xref	ut_gtfp1
	xref	ut_gxfp1
	xref	ut_gxst1
	xref	ut_gxli1
	xref	ut_gxli2
	xref	ut_chkri
	xref	ut_remst
	xref	ut_retin
	xref	ut_retfp
	xref	ut_retst
	xref	ut_rtfd1
	xref	ut_rnorm

	include 'dev8_keys_err'
	include 'dev8_keys_sbasic'
	include 'dev8_keys_qlv'

pure
	jsr	ut_gtfp1		 ; get floating point on stack
	bne.s	num_rts
	addq.l	#8,a3
	jsr	ut_gxin1		 ; get purify bits
	bne.s	num_rts
	moveq	#-1,d1
	move.w	(a6,a1.l),d2
	addq.l	#2,a1
	lsl.l	d2,d1
	and.l	d1,2(a6,a1.l)		 ; purify
	jmp	ut_retfp

fdiff
	jsr	ut_gtfp1		 ; get first fp
	bne.s	num_rts
	addq.l	#8,a3
	jsr	ut_gxfp1		 ; and second
	bne.s	num_rts
	move.l	8(a6,a1.l),d1
	move.l	2(a6,a1.l),d3		 ; mantissas
	move.w	6(a6,a1.l),d2
	move.w	d2,d0
	sub.w	(a6,a1.l),d0		 ; same exponent?
	beq.s	numf_diff		 ; ... yes
	bgt.s	numf_sd3
	neg.w	d0
	asr.l	d0,d1
	add.w	d0,d2
	bra.s	numf_diff
numf_sd3
	asr.l	d0,d3
numf_diff
	addq.l	#6,a1
	clr.w	(a6,a1.l)		 ; zero difference
	clr.l	2(a6,a1.l)
	sub.l	d3,d1
	beq.s	numf_ret
	jsr	ut_rnorm
numf_ret
	jmp	ut_retfp		 ; return difference

num_rts
	rts

bdiff
	jsr	ut_gtfp1		 ; get first fp
	bne.s	num_rts
	addq.l	#8,a3
	jsr	ut_gxfp1		 ; and second
	bne.s	num_rts
	move.l	8(a6,a1.l),d1
	move.l	2(a6,a1.l),d3		 ; mantissas
	move.w	6(a6,a1.l),d0
	sub.w	(a6,a1.l),d0		 ; same exponent?
	beq.s	numb_diff		 ; ... yes
	bgt.s	numb_sd3
	neg.w	d0
	asr.l	d0,d1
	bra.s	numb_diff
numb_sd3
	asr.l	d0,d3
numb_diff
	sub.l	d3,d1
	add.w	#12,a1
	jmp	ut_rtfd1		 ; return difference in bits


fphex$
	moveq	#14,d1			 ; enough space
	jsr	ut_chkri
	jsr	ut_gxfp1		 ; get floating point
	bne.s	num_rts

	move.l	sb_buffb(a6),a0 	 ; buffer
	jsr	cv.iwhex*3+qlv.off     
	jsr	cv.ilhex*3+qlv.off     

	move.l	-4(a6,a0.l),-4(a6,a1.l)
	move.l	-8(a6,a0.l),-8(a6,a1.l)
	move.l	-12(a6,a0.l),-12(a6,a1.l)
	move.w	#12,-14(a6,a1.l)	 ; string length

	sub.w	#14,a1
	jmp	ut_retst


hexfp
	bsr.l	ut_gxst1	get exactly one string
	bne.s	num_rts
	jsr	ut_remst	... remove it
	move.w	(a6,a1.l),d7	get length of string
	addq.l	#2,a1		skip it
	move.l	sb_buffb(a6),a0
	moveq	#5,d0		6 double digits

numh_loop
	bsr.s	numh_digit
	move.b	d2,d1
	bsr.s	numh_digit
	lsl.b	#4,d1
	or.b	d2,d1
	move.b	d1,(a6,a0.l)
	addq.l	#1,a0
	dbra	d0,numh_loop

	move.l	sb_arthp(a6),a1
	move.l	-4(a6,a0.l),-4(a6,a1.l)
	move.w	-6(a6,a0.l),-6(a6,a1.l)
	subq.l	#6,a1
	jmp	ut_retfp

numh_digit
	subq.w	#1,d7
	blt.s	numh_null	no more digits
	moveq	#-$30,d2	digits start at '0'=$30
	add.b	(a6,a1.l),d2	get digit
	addq.l	#1,a1
	blt.s	numh_xp 	less than 0, bum
	cmp.b	#9,d2		>9?
	ble.s	numh_rts	... no, ok
	bclr	#5,d2		'a'='a'=$11
	cmp.b	#$10,d2 	<a?
	ble.s	numh_xp 	... yes, bum
	sub.b	#7,d2		'a'='a'=$a
	cmp.b	#$10,d2 	<16?
	bhi.s	numh_xp 	... no, bum
	rts
numh_null
	moveq	#0,d2
numh_rts
	rts

numh_xp
	moveq	#err.iexp,d0
	addq.l	#4,sp
	rts



prty_set
	jsr	ut_gxli2		 ; get long integers
	bne.s	rnd_rts
	move.l	(a6,a1.l),d4		 ; number to generate
	move.l	4(a6,a1.l),a0
	add.l	d4,a0			 ; fill from end
	move.w	d4,d2
	moveq	#0,d3

prty_byloop
	subq.w	#1,d2
	move.w	d2,d1
	moveq	#0,d0

prty_prloop
	lsr.w	#1,d1
	addx.b	d3,d0
	tst.w	d1
	bne.s	prty_prloop

	and.b	#1,d0
	move.b	d0,-(a0)

	tst.w	d2
	bne.s	prty_byloop

	moveq	#0,d0
rnd_rts
	rts


rnd_msb
	subq.l	#8,a5		 ; ignore last parameter
	jsr	ut_gxin2	 ; our numbers
	bne.s	rnd_rts
	add.w	#$10,a3
	addq.l	#8,a5
	jsr	ut_gxli1	 ; parity table
	bne.s	rnd_rts
	move.l	(a6,a1.l),a0
	move.w	4(a6,a1.l),d1
	move.w	6(a6,a1.l),d2
	and.w	d1,d2
	move.b	(a0,d2.w),d2
	lsr.b	#1,d2
	roxr.w	#1,d1		 ; new msb
	addq.l	#6,a1
	move.w	d1,(a6,a1.l)
	jmp	ut_retin

rnd_seql
	sub.w	#$10,a5 	 ; ignore last parameters
	jsr	ut_gxin2	 ; our numbers
	bne.s	rnd_rts
	add.w	#$10,a3
	add.w	#$10,a5
	jsr	ut_gxli2	 ; parity table / workspace
	bne.s	rnd_rts
	move.l	(a6,a1.l),a0
	move.l	4(a6,a1.l),a2
	move.w	8(a6,a1.l),d1
	move.w	$a(a6,a1.l),d2

	moveq	#-1,d0

rnd_loop
	move.w	d1,(a2)+	 ; next rn
	move.w	d2,d3
	and.w	d1,d3
	move.b	(a0,d3.w),d3
	lsr.b	#1,d3
	roxr.w	#1,d1		 ; new msb
	dbra	d0,rnd_loop

	move.w	d1,d2
	moveq	#0,d1		 ; sequence length >=1

rnd_slen
	addq.l	#1,d1
	cmp.w	-(a2),d2
	bne.s	rnd_slen

	add.w	#12,a1
	jmp	ut_rtfd1	 ; return float length

rnd_seqe
	subq.l	#8,a5		 ; ignore last parameter
	jsr	ut_gtint	 ; our numbers
	bne.l	rnd_rts
	add.w	#$18,a3
	addq.l	#8,a5
	jsr	ut_gxli1	 ; parity table
	bne.l	rnd_rts
	move.l	(a6,a1.l),a0
	move.w	4(a6,a1.l),d1
	move.w	6(a6,a1.l),d2
	move.w	8(a6,a1.l),d3
	move.w	d1,d5		 ; keep seed

	moveq	#-1,d0

rnd_seel
	move.w	d2,d4
	and.w	d5,d4
	move.b	(a0,d4.w),d4
	lsr.b	#1,d4
	roxr.w	#1,d5		 ; new msb
	dbra	d0,rnd_seel	 ; 65536 times

	moveq	#-1,d0

rnd_sest
	cmp.w	d1,d5		 ; start of sequence?
	beq.s	rnd_secm	 ; ... yes
	move.w	d2,d4
	and.w	d1,d4
	move.b	(a0,d4.w),d4
	lsr.b	#1,d4
	roxr.w	#1,d1		 ; new msb
	dbra	d0,rnd_sest	 ; 65536 times
	bra.s	rnd_sene

rnd_secm
	move.w	d2,d4
	and.w	d1,d4
	move.b	(a0,d4.w),d4	 ; next from seq 1
	move.w	d3,d0
	and.w	d1,d0
	cmp.b	(a0,d0.w),d4	 ; same sequence?
	bne.s	rnd_sene

	lsr.b	#1,d4
	roxr.w	#1,d1		 ; new msb
	cmp.w	d1,d5		 ; start of sequence?
	bne.s	rnd_secm	 ; ... no

	moveq	#1,d1		 ; ... yes, equal
	bra.s	rnd_seex

rnd_sene
	moveq	#0,d1
rnd_seex
	add.w	#10,a1
	jmp	ut_rtfd1	 ; return float length


	end
