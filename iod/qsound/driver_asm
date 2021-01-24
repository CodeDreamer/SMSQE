; AY chip sound driver.  2020 by Marcel Kilgus
; v. 1.00

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
	move.l	d2,(a4)+		; ddb_open/ddb_chips

	moveq	#0,d1
	bsr	ay_silence
	moveq	#1,d1
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

	move.w	p.chip(sp),d4		; chip number
	beq	ayo_ipar
	cmp.b	ddb_chips(a3),d4	; how many chips there are
	bhi.s	ayo_ipar
	subq.w	#1,d4			; we're 0 based

	moveq	#err.fdiu,d0
	tas	ddb_open(a3,d4.w)
	bne.s	ayo_rts

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
	moveq	#14,d5			; length must be multiple of 14
	mulu	p.buff(sp),d5		; frame buffer size
	add.l	d5,d1
	move.w	mem.achp,a2
	jsr	(a2)
	bne.s	ayo_err
	move.b	d4,chn_chip(a0)
	move.l	d5,chn_bsze(a0)

	move.l	d5,d1
	lea	chn.len(a0),a2		; setup output queue
	move.l	a2,chn_outq(a0)
	move.w	ioq.setq,a1
	jsr	(a1)

	moveq	#0,d1
	move.b	chn_chip(a0),d1
	bsr.s	ay_silence

	lsl.w	#2,d4
	move.l	a0,ddb_cdb0(a3,d4.w)	; must be last as poll checks this

	moveq	#0,d0
ayo_rts
	lea	p.end(sp),sp
	rts
ayo_err
	clr.b	ddb_open(a4,d4.w)
	bra.s	ayo_rts
ayo_ipar
	moveq	#err.ipar,d0
	bra.s	ayo_rts

ay_ipar
	moveq	#err.ipar,d0
	rts

; Silence AY chip on init/close
;
; d1 = chip
ay_silence
	clr.l	-(sp)
	clr.l	-(sp)
	clr.l	-(sp)
	clr.l	-(sp)
	move.l	sp,a1
	bsr	ay_set_regs
	lea	16(sp),sp
	rts

; Close
ay_close
	moveq	#0,d7
	move.b	chn_chip(a0),d7
	move.l	d7,d2
	bsr.s	ay_silence

	sf	ddb_open(a3,d7.w)
	lsl.l	#2,d7
	clr.l	ddb_cdb0(a3,d7.w)
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
	move.l	d0,a0
	move.l	chn_outq(a0),a2

	move.l	qu_nexti(a2),d0 	; next in
	move.l	qu_nexto(a2),a1 	; next out
	sub.l	a1,d0			; number in queue
	beq.s	ayp_ok			; ... none
	bgt.s	ayp_check		; ... there are some
	add.l	chn_bsze(a0),d0 	; wrapped around, add total
ayp_check
	cmp.w	#14,d0			; one complete register set?
	bcs.s	ayp_rts 		; ... no
	move.l	qu_nexto(a2),a1 	; next out
	move.b	chn_chip(a0),d2 	; chip number
	bsr.s	ay_set_regs
	addq.l	#1,chn_poll(a0)
	add.w	#14,a4
	cmp.l	qu_endq(a2),a1		; off end?
	bne.s	ayp_ok			; ... no
	lea	qu_strtq(a2),a1 	; ... yes, reset queue pointer
ayp_ok
	move.l	a1,qu_nexto(a2) 	; set next out
ayp_rts
	rts

; a1 = registers
ay_set_regs
	move.l	a5,-(sp)
	moveq	#ay.wrall,d0
	move.l	ddb_qsnd(a3),a5
	jsr	(a5)
	move.l	(sp)+,a5
	rts

	end
