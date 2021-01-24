; QPC SMSQ BEEP emulation    1988  Tony Tebby	 QJUMP
;
; 2018-01-03  1.01  Moved beep killing to QPC2 to support shorter beeps

	section sms

	xdef	hdop_beep
	xdef	hdop_bpof

	include 'dev8_keys_sys'
	include 'dev8_keys_err'
	include 'dev8_smsq_qpc_keys'

hdop_beep

hob.rege reg	 d1-d3/a0
hob.regx reg	 d1-d3/a0/a3
frame	equ	$10
	movem.l hob.rege,-(sp)
	sub.w	#frame,sp		 ; some work space
	move.l	sp,a0

	clr.w	qpc_bcnt

	move.b	(a3)+,d0		 ; number of parameters
	moveq	#0,d1			 ; no nibbles
	move.l	(a3)+,d2		 ; mask of bits
	bra.s	hob_dlend
hob_dloop
	moveq	#0,d3
	move.b	(a3)+,d3		 ; next byte
	ror.l	#1,d2			 ; any to send?
	bcs.s	hob_skip		 ; ... no
	ror.l	#1,d2			 ; how much?
	bcs.s	hob_byte		 ; a byte
	and.b	#$0f,d3 		 ; ... this much
	not.b	d1			 ; any sent already?
	bne.s	hob_msnib		 ; ... no
	or.b	d3,(a0)+		 ; put it in
	bra.s	hob_dlend
hob_msnib
	lsl.b	#4,d3
	move.b	d3,(a0) 		 ; set ms nibble
	bra.s	hob_dlend
hob_byte
	tst.b	d1			 ; a nibble in already?
	beq.s	hob_whole		 ; ... no
	ror.w	#4,d3			 ; ms nibble
	or.b	d3,(a0)+
	rol.w	#8,d3			 ; ls nibble
	move.b	d3,(a0)
	bra.s	hob_dlend
hob_whole
	move.b	d3,(a0)+		 ; whole byte
	bra.s	hob_dlend
hob_skip
	ror.l	#1,d2			 ; skip bit
hob_dlend
	dbra	d0,hob_dloop		 ; next bit.

	move.l	sp,a0
	dc.w	qpc.sbeep		 ; start beep sound
	st	sys_qlbp(a6)		 ; ... beeping now

	add.w	#frame,sp
hob_ok
	moveq	#0,d0
	movem.l (sp)+,hob.regx
	rts

hdop_bpof
	movem.l hob.rege,-(sp)
	dc.w	qpc.kbeep
	sf	sys_qlbp(a6)		 ; ... not beeping now
	bra.s	hob_ok

	end
