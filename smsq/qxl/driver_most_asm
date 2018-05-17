; SMS2 QXL card built in IO drivers

	section header

	xref	smsq_end

header_base
	dc.l	drv_base-header_base	 ; length of header
	dc.l	0			 ; module length unknown
	dc.l	smsq_end-drv_base	 ; loaded length
	dc.l	0			 ; checksum
	dc.l	0			 ; always select
	dc.b	1			 ; 1 level down
	dc.b	0
	dc.w	smsq_name-*

smsq_name
	dc.w	16,'SMSQ QXL Drivers'
	dc.l	'    '
	dc.w	$200a


	section base

	xref	iob_smsq		 ; SMSQ IOB bits

	xref	kbd_initi
	xref	qxl_spp_init
	xref	qxl_spp_inits
	xref	mouse_init
	xref	nul_init
	xref	pipe_init
	xref	rd_init
	xref	iob_init
	xref	history_init

	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_qlv'
	include 'dev8_smsq_smsq_base_keys'

drv_base
	bra.l	init

	xdef	iou_dirshr
iou_dirshr dc.w 0	; shared directories supported	(RAM disk)


	section init
	include 'dev8_smsq_qxl_keys'

init
	jsr	kbd_initi
	move.l	a3,-(sp)
	jsr	qxl_spp_init
	move.l	a3,-(sp)
	jsr	mouse_init
	jsr	nul_init
	jsr	pipe_init
	jsr	rd_init
	jsr	iob_init
	jsr	history_init

	movem.l (sp)+,a2/a3
	moveq	#sms.xtop,d0
	trap	#do.sms2

	move.l	a3,qxl_kbd_link
	move.l	a2,a3
	jmp	qxl_spp_inits

	end
