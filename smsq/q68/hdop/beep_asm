; Q68 SMSQ BEEP emulation  1.01   2016 W. Lenerz
; 2018-06-28	1.01 save A2! (wl)
; based on
; Q40 SMSQ BEEP emulation    1988  Tony Tebby

	section sms

	xdef	hdop_beep
	xdef	hdop_bpof

	xref	hdop_poll
	xref	hdop_ssss

	include 'dev8_keys_sys'
	include 'dev8_keys_q68'
	include 'dev8_keys_iod'
	include 'dev8_keys_err'
	include 'dev8_keys_sss'
	include 'dev8_smsq_q40_hdop_data'

hob.rege reg	 d1-d3/a0-a2
hdop_beep
hob.regx reg	 d1-d3/a0-a3
	movem.l hob.rege,-(sp)
	move.b	q68_slug,-(sp)
	sf	q68_slug
	move.l	a3,a1			; save parameter block
	bsr.l	hob_lloc		; locate linkage and stop beeping
	bne.l	hob_exit
	lea	ho_work(a3),a0
	move.b	(a1)+,d0		; number of parameters
	moveq	#0,d1			; no nibbles
	move.b	(a1)+,d2
	lsl.l	#8,d2
	move.b	(a1)+,d2
	lsl.l	#8,d2
	move.b	(a1)+,d2		; use this instead of move.l  (a1)+,d2
	lsl.l	#8,d2
	move.b	(a1)+,d2		; mask of bits
	bra.s	hob_dlend
hob_dloop
	moveq	#0,d3
	move.b	(a1)+,d3		; next byte
	ror.l	#1,d2			; any to send?
	bcs.s	hob_skip		; ... no
	ror.l	#1,d2			; how much?
	bcs.s	hob_byte		; a byte
	and.b	#$0f,d3 		; ... this much
	not.b	d1			; any sent already?
	bne.s	hob_msnib		; ... no
	or.b	d3,(a0)+		; put it in
	bra.s	hob_dlend
hob_msnib
	lsl.b	#4,d3
	move.b	d3,(a0) 		; set ms nibble
	bra.s	hob_dlend
hob_byte
	tst.b	d1			; a nibble in already?
	beq.s	hob_whole		; ... no
	ror.w	#4,d3			; ms nibble
	or.b	d3,(a0)+
	rol.w	#8,d3			; ls nibble
	move.b	d3,(a0)
	bra.s	hob_dlend
hob_whole
	move.b	d3,(a0)+		; whole byte
	bra.s	hob_dlend
hob_skip
	ror.l	#1,d2			; skip bit
hob_dlend
	dbra	d0,hob_dloop		; next bit.

	lea	ho_work(a3),a0
	moveq	#0,d0			; get pitch 1
	move.b	(a0)+,d0
	subq.b	#1,d0
	mulu	#ho.bpscl,d0
	lsr.l	#ho.bpssf,d0
	add.w	#ho.bpoff,d0		; scaled and offset
	move.w	d0,ho_bphgh(a3)
	move.w	d0,d2

	moveq	#0,d1			; get pitch 2
	move.b	(a0)+,d1
	subq.b	#1,d1
	tst.w	(a0)			; any interval?
	beq.s	hob_setl		; ... none
	move.b	d1,d0
	mulu	#ho.bpscl,d0
	lsr.l	#ho.bpssf,d0
	add.w	#ho.bpoff,d0		; scaled and offset
hob_setl
	move.w	d0,ho_bplow(a3)
	add.w	d0,d2			; total of pitches

	add.w	#ho.bpfb,d2
	move.l	#ho.bpfa,d0
	divu	d2,d0
	add.w	#ho.bpfo,d0		; pitch fudge factor for duration
	move.w	(a0)+,d3
	ror.w	#8,d3
	mulu	d0,d3			; interval
	add.l	#$8000,d3		; rounded
	swap	d3
	move.w	d3,ho_bpint(a3) 	; keep it
	move.w	(a0)+,d3		; length
	beq.s	hob_snln
	ror.w	#8,d3

	mulu	d0,d3
	swap	d3
	addq.w	#1,d3

;	 tst.w	 d3
;	 bne.s	 hob_snln
;	 moveq	 #1,d3

hob_snln
	not.w	d3			; length (-ve)

	move.b	(a0)+,d0		; step / wrap
	move.b	d0,d1
	ext.w	d0
	asr.w	#4,d0
	muls	#ho.bpscl,d0
	asr.l	#ho.bpssf,d0
	move.w	d0,ho_bpstp(a3) 	; step

	tst.w	ho_bpint(a3)		; cannot have zero interval
	bne.s	hob_swrap
	addq.w	#1,ho_bpint(a3)
	lsl.w	ho_bpstp(a3)		; increase step
	bne.s	hob_swrap
	subq.w	#2,ho_bpint(a3) 	; ... none, almost infinite interval

hob_swrap
	addq.b	#1,d1
	and.w	#$000f,d1		; 0-15
	move.w	d1,ho_bpwrp(a3)

	st	sys_qlbp(a6)		; ... beeping now
	move.w	d3,ho_bptim(a3) 	; set it going

hob_ok
	moveq	#0,d0
hob_exit
	move.b	(sp)+,q68_slug
hob_exit2
	movem.l (sp)+,hob.regx
	rts

hdop_bpof
	movem.l hob.rege,-(sp)
	clr.b	sys_qlbp(a6)		 ; ... not beeping now
	pea	hob_exit2

;   locate linkage and kill old beep

hob_lloc
	lea	sys_poll(a6),a3 	  ; find kbd poll linkage
	lea	hdop_poll,a0
holl_look
	move.l	(a3),d0 		  ; next link
	beq.s	holl_bp 		  ; ... all done
	move.l	d0,a3
	cmp.l	iod_plad-iod_pllk(a3),a0  ; ours?
	bne.s	holl_look		  ; ... no
	subq.l	#iod_pllk,a3
	clr.w	ho_bptim(a3)		  ; ... stop generating beeps
	moveq	#sss_kill,d0
	move.l	a4,-(sp)
	jsr	hdop_ssss		  ; kill the old beep
	move.l	(sp)+,a4

	moveq	#0,d0
	rts

holl_bp
	moveq	#err.ipar,d1
	rts

	end
