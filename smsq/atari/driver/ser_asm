; base area SMSQ ATARI ST Serial Drivers

	section header

	xref	smsq_end

header_base
	dc.l	at_ser-header_base	 ; length of header
	dc.l	0			 ; module length unknown
	dc.l	smsq_end-at_ser 	 ; loaded length
	dc.l	0			 ; checksum
	dc.l	0			 ; always select
	dc.b	1			 ; 1 level down
	dc.b	0
	dc.w	smsq_name-*

smsq_name
	dc.w	26,'SMSQ Atari Serial Drivers '
	dc.l	'    '
	dc.w	$200a


	section base

	xref	iob_smsq		 ; SMSQ IOB bits

	xref	dev_init
	xref	par_init
	xref	nul_init
	xref	pipe_init
	xref	rd_init
	xref	iob_init
	xref	kbd_init
	xref	history_init

	xref	at_baud
	xref	ut_procdef

	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_sys'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_mac_proc'


at_ser
	bra.l	start

	xdef	iou_dirshr
iou_dirshr dc.w 0	; shared directories supported	(RAM disk)


	section init
start
	jsr	dev_init
	jsr	par_init
	jsr	nul_init
	jsr	pipe_init
	jsr	rd_init
	jsr	iob_init
	jsr	kbd_init
	jsr	history_init

	lea	at_procs,a1
	jsr	ut_procdef

	moveq	#sms.xtop,d0
	trap	#do.sms2

	lea	at_comm,a0
	move.l	a0,sms.t1tab+sms.comm*4  ; comms baud routine
	rts

at_comm
	move.l	sms.rte,-(sp)		 ; return
	jmp	at_baud 		 ; do atari baud


at_procs
	proc_stt
	proc_end
	proc_stt
	proc_def A_MACHINE,machine
	proc_def A_PROCESSOR,processor
	proc_end

	end
