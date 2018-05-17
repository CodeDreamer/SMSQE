; RND / RANDOMISE	 V2.00	    1990  Tony Tebby	QJUMP
;
;	RND (range)
;
	section exten

	xdef	rnd
	xdef	randomise

	xref	ut_gtint		 ; get integers
	xref	ut_gxli1
	xref	ut_retin
	xref	ut_rtfd1

	include 'dev8_keys_err'
	include 'dev8_keys_sbasic'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_sys'

rnd
	move.l	sb_rand(a6),d0
	move.w	d0,d4
	swap	d0
	mulu	#$c12d,d0		   ; multiply up
	mulu	#$712d,d4
	swap	d0
	clr.w	d0
	add.l	d0,d4
	addq.l	#1,d4
	move.l	d4,sb_rand(a6)		 ; and save it

	cmp.l	a3,a5			 ; any parameters?
	beq.s	rnd_fp			 ; ... no, fp

	jsr	ut_gtint		 ; integers
	bne.s	rnd_rts
	moveq	#0,d1			 ; bottom of range
	subq.b	#2,d3
	blt.s	rnd_int
	bgt.s	rnd_ipar

	move.w	(a6,a1.l),d1		 ; bottom of range given
	addq.l	#2,a1
rnd_int
	move.w	(a6,a1.l),d2
	addq.w	#1,d2
	sub.w	d1,d2			 ; size of range

	swap	d4			 ; use msword
	mulu	d2,d4			 ;
	swap	d4			 ; and overflow
	add.w	d4,d1			 ; add base

	move.w	d1,(a6,a1.l)
	jmp	ut_retin		 ; return it

rnd_fp
	moveq	#2,d1
	or.l	d4,d1
	lsr.l	#1,d1			 ; positive >= 1
	jsr	ut_rtfd1
	sub.w	#$1f,(a6,a1.l)		 ; put into range (0-1)
	rts

rnd_ipar
	moveq	#err.ipar,d0
rnd_rts
	rts

randomise
	cmp.l	a3,a5			 ; any param?
	beq.s	rnd_rnd
	jsr	ut_gxli1		 ; one long int
	move.l	(a6,a1.l),d1
	move.l	d1,d2
	swap	d1			 ; in both halves of word
	add.l	d2,d1
	move.l	d1,sb_rand(a6)
	rts

rnd_rnd
	move.l	a6,a0
	moveq	#sms.xtop,d0
	trap	#do.sms2
	move.w	sys_rand(a6),d0
	add.w	d0,sb_rand(a0)
	sub.w	d0,sb_rand+2(a0)
	moveq	#0,d0
	rts

	end
