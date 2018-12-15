; QXL SMSQ BEEP emulation    1988  Tony Tebby	 QJUMP

	section sms

	xdef	hdop_beep
	xdef	hdop_bpof

	xref	qxl_mess_add

	include 'dev8_keys_sys'
	include 'dev8_keys_err'
	include 'dev8_smsq_qxl_keys'
	include 'dev8_smsq_qxl_comm_keys'

ho.bpoff equ	  1098			; beep 0
ho.bpscl equ	  14585-ho.bpoff	; beep 128 - beep 0
ho.bpssf equ	  7			; /128

ho.bpfa  equ	  ho.bpscl/128*325*2	 ; duration = (140 + a/(b+2*pitch)) * QL
ho.bpfb  equ	  ho.bpscl/128*5*2-ho.bpoff*2
ho.bpfo  equ	  125

hdop_beep
hob.rege reg	 d1-d3/a0/a1
hob.regx reg	 d1-d3/a0/a1/a3
frame	equ	$10
	movem.l hob.rege,-(sp)
	sub.w	#frame,sp		 ; some work space
	move.l	sp,a0

	move.l	qxl_message,a5
	lea	qxl_ms_beep+qxl_ms_spare(a5),a5
	clr.w	(a5)+

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
	move.l	a5,a1
	move.l	#12<<16+qxm.beep<<8+$ff,(a5)+ ; message header

	moveq	#0,d0			 ; get pitch 1
	move.b	(a0)+,d0
	subq.b	#1,d0
	mulu	#ho.bpscl,d0
	lsr.l	#ho.bpssf,d0
	add.w	#ho.bpoff,d0		 ; scaled and offset
	move.w	d0,d2
	move.w	d0,(a5)+
	move.w	d2,d0

	moveq	#0,d1			 ; get pitch 2
	move.b	(a0)+,d1
	tst.w	(a0)			 ; any interval
	beq.s	hob_setl		 ; ... none
	move.w	d1,d0
	subq.w	#1,d0
	mulu	#ho.bpscl,d0
	lsr.l	#ho.bpssf,d0
	add.w	#ho.bpoff,d0		 ; scaled and offset
hob_setl
	add.w	d0,d2			 ; total of pitches
	move.w	d0,(a5)+

	add.w	#ho.bpfb,d2
	move.l	#ho.bpfa,d0
	divu	d2,d0
	add.w	#ho.bpfo,d0		 ; pitch fudge factor for duration
	move.w	(a0)+,d3
	beq.s	hob_slen		 ; ... no interval
	ror.w	#8,d3
	mulu	d0,d3			 ; interval
	lsl.l	#4,d3			 ; reduce rounding problems
	add.l	#$8000,d3		 ; rounded
	clr.w	d3
	swap	d3
	mulu	#qxl.8tik>>4,d3 	 ; PC eighth tick to QXL poll correction
	add.l	#$8000,d3
	swap	d3
	tst.w	d3
	bne.s	hob_slen
	moveq	#1,d3			 ; cannot have zero
hob_slen
	move.w	(a0)+,d2		 ; length
	beq.s	hob_step
	ror.w	#8,d2
	mulu	d0,d2
	swap	d2
	addq.w	#1,d2
hob_step
	move.b	(a0)+,d0		 ; step / wrap
	move.b	d0,d1
	ext.w	d0
	asr.w	#4,d0
	muls	#ho.bpscl,d0
	asr.l	#ho.bpssf,d0
	move.w	d0,(a5)+		 ; step
	move.w	d3,(a5)+		 ; interval

	addq.b	#1,d1
	and.w	#$000f,d1		 ; 0-15
	move.w	d1,(a5)

; message is complete

	jsr	qxl_mess_add
	st	sys_qlbp(a6)		 ; ... beeping now
	move.w	d2,-(a1)		 ; set it going
hob_exframe
	add.w	#frame,sp
hob_ok
	moveq	#0,d0
	movem.l (sp)+,hob.regx
	rts

hdop_bpof
	movem.l hob.rege,-(sp)
	move.l	qxl_message,a5
	move.w	#1,qxl_ms_beep+qxl_ms_spare(a5)
	sf	sys_qlbp(a6)		 ; ... not beeping now
	bra.s	hob_ok
	end
