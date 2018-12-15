; base area SMSQ GOLD Drivers

	section header

	xref	smsq_end

header_base
	dc.l	gl_most-header_base	 ; length of header
	dc.l	0			 ; module length unknown
	dc.l	smsq_end-gl_most	 ; loaded length
	dc.l	0			 ; checksum
	dc.l	0			 ; always select
	dc.b	1			 ; 1 level down
	dc.b	0
	dc.w	smsq_name-*

smsq_name
	dc.w	17,'SMSQ GOLD Drivers '
	dc.l	'    '
	dc.w	$200a


	section base

	xref	iob_smsq		 ; SMSQ IOB bits

	xref	qd_int2

	xref	hdop_init
	xref	sms_hdop
	xref	dev_init
	xref	nul_init
	xref	pipe_init
	xref	rd_init
	xref	iob_init
	xref	history_init
	xref	par_init
	xref	ser_baud

	include 'dev8_keys_qlv'
	include 'dev8_keys_sys'
	include 'dev8_keys_psf'
	include 'dev8_keys_68000'
	include 'dev8_keys_qlhw'
	include 'dev8_keys_qdos_sms'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_smsq_smsq_config_keys'
	include 'dev8_smsq_gold_keys'
	include 'dev8_mac_assert'

gl_most
	bra.l	start

	xdef	iou_dirshr
iou_dirshr dc.w 0	; shared directories supported	(RAM disk)


	section init
start
	jsr	hdop_init
	jsr	dev_init
	jsr	nul_init
	jsr	pipe_init
	jsr	rd_init
	jsr	iob_init
	jsr	history_init
	jsr	par_init

sys_init

; When we have linked in the interrupt servers, we can enable the interrupts

	moveq	#sms.xtop,d0
	trap	#do.sms2

	clr.w	psf_sr(a5)		 ; we can clear interrupts on return

	lea	qd_int2,a0		 ; set interrupt routine address
	lea	exv_i2,a5
	bsr.s	irm_wbase

	moveq	#$ffffff00+pc.maskt+pc.intri,d0 ; enable transmit interrupt
	move.b	d0,sys_qlir(a6) 	 ; .... and always clear interface int
	move.b	d0,pc_intr

	lea	hw_poll,a0
	lea	sms.hpoll,a5		 ; and tidy up
	bsr.s	irm_wbase

	lea	sms_hdop,a0
	lea	sms.t1tab+sms.hdop*4,a5  ; set hdop
	bsr.s	irm_wbase

	assert	sms.hdop,sms.comm-1
	lea	gl_comm,a0
	bsr.s	irm_wbase		 ; comms baud routine

	move.w	#9600,d1		 ; set harware to 9600
	jmp	ser_baud

gl_comm
	move.l	sms.rte,-(sp)		 ; return
	jmp	ser_baud		 ; do GOLD card Baud


irm_wbase
	move.l	a0,d0			 ; write a0 to vector area
	swap	d0
	bsr.s	wb_do
	swap	d0
wb_do
	jmp	sms.wbase

;+++
; Gold SMSQ polling interrupt server operating off the frame interrupt.
; Clears the frame interupt (and, implicitly, the interface interrupt).
; See SHD_POLL.
;---
hw_poll
;  subq.w  #1,$20004
;  bgt.s   xx
;  move.w  #50,$20004
;  not.w   $20006
;xx
	moveq	#pc.intrf,d7
	or.b	sys_qlir(a6),d7
	move.b	d7,pc_intr		 ; clear offending interrupt
	rts

	end
