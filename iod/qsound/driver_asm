; AY chip sound driver	 v1.01	  2021 by Marcel Kilgus
;
; 2021-08-01  1.01  Channel can now access several chips at the same time (MK)

	section qsound_drv

	xdef	qsound_drv_init

	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_qdos_io'
	include 'dev8_keys_iod'
	include 'dev8_keys_sys'
	include 'dev8_keys_err'
	include 'dev8_keys_qlv'
	include 'dev8_keys_qu'
	include 'dev8_keys_qsound'
	include 'dev8_iod_qsound_driver_keys'

qsound_drv_init
	move.l	#ddb_end,d1		; Allocate ddb
	moveq	#0,d2
	moveq	#sms.achp,d0
	trap	#1
	tst.l	d0
	bne.s	ay_rts

	move.l	a0,a3
	moveq	#sms.info,d0
	trap	#1
	move.l	sys_qsound(a0),ddb_qsnd(a3)	; Get QSound entry

	lea	iod_ioad(a3),a4
	lea	ay_io,a2
	move.l	a2,(a4)+		; I/O
	lea	ay_open,a2
	move.l	a2,(a4)+		; open - iod_open $20
	lea	ay_close,a2
	move.l	a2,(a4)+		; close - iod_clos $24

	lea	ay_ipar,a2
	move.l	a2,(a4)+		; ddb_ssio - byte test not implemented
	move.l	a2,(a4)+		; ddb_ssi - byte fetch not implemented
	lea	ay_pbyt,a2
	move.l	a2,(a4)+		; ddb_sso - byte send
	move.w	ay_rts,(a4)+		; ddb_rts

	move.l	ddb_qsnd(a3),a2 	; Get QSound entry
	moveq	#ay.info,d0
	jsr	(a2)
	move.b	d2,(a4) 		; ddb_chips

	moveq	#0,d2
	bsr	ay_silence
	moveq	#1,d2
	bsr	ay_silence

	lea	ay_poll,a2
	move.l	a2,iod_plad(a3)
	lea	iod_pllk(a3),a0
	moveq	#sms.lpol,d0
	trap	#1

	lea	iod_iolk(a3),a0
	moveq	#sms.liod,d0
	trap	#1
	moveq	#0,d0
ay_rts
	rts

p.chip	equ	0
p.type	equ	2
p.freq	equ	4
p.pan	equ	6
p.vol	equ	8
p.buff	equ	10
p.end	equ	12

ayo_rts2
	bra.l	ayo_rts
ay_open
	move.l	a3,a4
	lea	-p.end(sp),sp
	move.l	sp,a3
	move.w	iou.dnam,a2
	jsr	(a2)
	bra.s	ayo_rts2
	bra.s	ayo_rts2
	bra.s	ayo_do

	dc.w	6,'QSOUND'
	dc.w	6
	dc.w	-1,1			; chip number
	dc.w	' T',0			; chip type
	dc.w	' F',17744		; chip frequency
	dc.w	' P',1			; pan mode
	dc.w	' V',255		; volume
	dc.w	' _',10 		; buffer size
ayo_do
	move.l	a4,a3			; recover DDB

	move.w	p.chip(sp),d4		; chip bit mask
	beq	ayo_ipar
	move.b	ddb_chips(a3),d0	; how many chips there are
	moveq	#1,d1
	lsl.b	d0,d1			; maximum chip mask
	cmp.b	d1,d4			; bit mask within range?
	bcc	ayo_ipar		; ... no

	moveq	#err.fdiu,d0
	move.b	d4,d1			; chip mask
	and.b	ddb_chipm(a3),d1	; one of the chips already in use?
	bne.s	ayo_rts2		; ... yes

	lea	ddb_cdb0(a3),a4 	; check for empty slot
	tst.l	(a4)
	beq.s	ayo_slot_ok
	addq.l	#4,a4
	tst.l	(a4)
	bne.s	ayo_rts2		; ... nope, none
ayo_slot_ok
	move.l	ddb_qsnd(a3),a2 	; Get QSound entry
	move.w	p.type(sp),d1
	moveq	#ay.chip_type,d0
	jsr	(a2)

	move.w	p.freq(sp),d1
	mulu	#100,d1
	moveq	#ay.chip_freq,d0
	jsr	(a2)

	move.w	p.pan(sp),d1
	moveq	#ay.stereo,d0
	jsr	(a2)

	move.w	p.vol(sp),d1
	moveq	#ay.volume,d0
	jsr	(a2)

	moveq	#chn.len+qu.hdlen,d1	; allocate CDB + queue header
	moveq	#0,d6
	move.b	mask_to_cnt(pc,d4.w),d6 ; mask to chip count
	mulu	#14,d6			; buffer must be multiple of this
	move.w	d6,d5
	mulu	p.buff(sp),d5		; frame buffer size
	add.l	d5,d1
	move.w	mem.achp,a2
	jsr	(a2)
	bne.s	ayo_rts
	move.b	d4,chn_chipm(a0)	; remember chip mask
	move.l	d5,chn_bsze(a0) 	; buffer size

	move.l	d5,d1
	lea	chn.len(a0),a2		; setup output queue
	move.l	a2,chn_outq(a0)
	move.w	ioq.setq,a1
	jsr	(a1)

	move.w	d6,chn_frame(a0)	; frame size

	lea	chn_chip0(a0),a2
	move.b	d4,d0			; chip mask
	moveq	#0,d2			; current chip number
	bra.s	ayo_chip_loop

mask_to_cnt
	dc.b	0,1,1,2,2,2,2,3

ayo_chip_set
	move.b	d2,(a2)+		; insert chip
ayo_chip_loop
	addq.l	#1,d2			; try next
	lsr.b	#1,d0
	bcs.s	ayo_chip_set
	bne.s	ayo_chip_loop

	bsr.s	ay_silence_chips	; silence all chips in channel

	or.b	d4,ddb_chipm(a3)	; mark chips as occupied
	move.l	a4,chn_slot(a0) 	; remember the slot
	move.l	a0,(a4) 		; save CDB. Must be last as poll checks it

	moveq	#0,d0
ayo_rts
	lea	p.end(sp),sp
	rts
ayo_ipar
	moveq	#err.ipar,d0
	bra.s	ayo_rts
ayo_iuse
	moveq	#err.fdiu,d0
	bra.s	ayo_rts

ay_ipar
	moveq	#err.ipar,d0
	rts


; Silence all chips of a channel
ay_silence_chips
	lea	chn_chip0(a0),a2
ay_silence_loop
	move.b	(a2)+,d2
	beq.s	ay_silence_rts

	subq.b	#1,d2
	bsr	ay_silence
	bra.s	ay_silence_loop
ay_silence_rts
	rts

; Close
ay_close
	bsr.s	ay_silence_chips	; silence all chips in channel

	move.b	chn_chipm(a0),d0
	not.b	d0
	and.b	d0,ddb_chipm(a3)	; chips not in use anymore

	move.l	chn_slot(a0),a2
	clr.l	(a2)			; free ddb_cbd slot
	move.w	mem.rchp,a2
	jmp	(a2)

; I/O
ay_io
	cmp.l	#iof.posr,d0
	beq.s	ay_posr

	pea	ddb_ssio(a3)
	move.w	iou.ssio,a2
	jmp	(a2)

; Return song position
ay_posr
	tst.l	d1
	bne.s	ay_ipar
	move.l	chn_poll(a0),d1
	moveq	#0,d0
	rts

; Send register value
ay_pbyt
	move.l	chn_outq(a0),a2
	move.w	ioq.pbyt,a3
	jmp	(a3)

; AY polled task
ayp.reg reg	a0-a3
ay_poll
;	 movem.l ayp.reg,-(sp)
	move.l	ddb_cdb0(a3),d0
	beq.s	ayp_next1
	bsr.s	ay_poll_chip
ayp_next1
	move.l	ddb_cdb1(a3),d0
	beq.s	ayp_next2
	bsr.s	ay_poll_chip
ayp_next2
;	 movem.l (sp)+,ayp.reg
	rts

ay_poll_chip
	move.l	d0,a0			; cdb
	move.l	chn_outq(a0),a2

	move.l	qu_nexti(a2),d0 	; next in
	move.l	qu_nexto(a2),a1 	; next out
	sub.l	a1,d0			; number in queue
	beq.s	ayp_ok			; ... none
	bgt.s	ayp_check		; ... there are some
	add.l	chn_bsze(a0),d0 	; wrapped around, add total
ayp_check
	cmp.w	chn_frame(a0),d0	; complete frame available?
	bcs.s	ayp_rts 		; ... no
	move.l	qu_nexto(a2),a1 	; next out

	lea	chn_chip0(a0),a4	; chip numbers
ayp_loop
	move.b	(a4)+,d2
	beq.s	ayp_done
	subq.b	#1,d2			; API is 0 based
	bsr.s	ay_set_regs		; this will update a1
	bra.s	ayp_loop
ayp_done
	addq.l	#1,chn_poll(a0)
	cmp.l	qu_endq(a2),a1		; off end?
	bne.s	ayp_ok			; ... no
	lea	qu_strtq(a2),a1 	; ... yes, reset queue pointer
ayp_ok
	move.l	a1,qu_nexto(a2) 	; set next out
ayp_rts
	rts

; Silence AY chip on init/close
;
; d2 = chip
ay_silence
	lea	silence(pc),a1

; d2 = chip
; a1 = registers
ay_set_regs
	move.l	a5,-(sp)
	moveq	#ay.wrall,d0
	move.l	ddb_qsnd(a3),a5
	jsr	(a5)
	move.l	(sp)+,a5
	rts

silence
	dc.w	0,0,0,0,0,0,0

	end
