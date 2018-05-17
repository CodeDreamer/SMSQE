; base area SMSQ Q40 Serial Drivers

	section header


	xdef	q40_insta0

	xref	smsq_end

header_base
	dc.l	q40_ser-header_base	 ; length of header
	dc.l	0			 ; module length unknown
	dc.l	smsq_end-q40_ser	 ; loaded length
	dc.l	0			 ; checksum
	dc.l	0			 ; always select
	dc.b	1			 ; 1 level down
	dc.b	0
	dc.w	smsq_name-*

smsq_name
	dc.w	24,'SMSQ Q40 Serial Drivers '
	dc.l	'    '
	dc.w	$200a


	section base

	xref	iob_smsq		 ; SMSQ IOB bits

	xref	q40_int2
	xref	q40_spp_init
	xref	dev_init
	xref	nul_init
	xref	pipe_init
	xref	rd_init
	xref	iob_init
	xref	history_init
	xref	ssss_init
	xref	kbd_inithwt
	xref	hwt_create
	xref	hwt_ilist

	xref	hw_poll

	include 'dev8_keys_q40'
	include 'dev8_keys_68000'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_sys'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_mac_assert'

q40_ser
	bra.l	start

	xdef	iou_dirshr
iou_dirshr dc.w 0	; shared directories supported	(RAM disk)


	section init
start
	moveq	#16,d0
	jsr	hwt_create		 ; create hardware table
	jsr	q40_spp_init
	jsr	dev_init
	jsr	nul_init
	jsr	pipe_init
	jsr	rd_init
	jsr	iob_init
	jsr	history_init
	jsr	ssss_init
	jsr	kbd_inithwt

	moveq	#sms.xtop,d0
	trap	#do.sms2

	clr.b	q40_kack		 ; acknowledge keyboard junk

	lea	hw_poll,a0		 ; poll tidy up
	lea	sms.hpoll,a5
	bsr.s	q40_insta0

	moveq	#2,d1			 ; set up interrupt level 2
	lea	q40_int2,a1
	jmp	hwt_ilist

; install in base area

Q40_insta0
	move.l	a0,d0
	swap	d0
	bsr.s	q40_instw
	swap	d0
q40_instw
	jmp	sms.wbase

; Spurious interrupt handler

q40_spint
	or.w	#$0200,sr
	move.l	exv_i2,-(sp)
	rts

	end
