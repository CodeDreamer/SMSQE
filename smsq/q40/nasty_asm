; Nasty Initialisation for Q40 for SMSQ

	section header

	xref	smsq_end

header_base
	dc.l	nasty_base-header_base	 ; length of header
	dc.l	0			 ; module length unknown
	dc.l	smsq_end-nasty_base	 ; loaded length
	dc.l	0			 ; checksum
	dc.l	0			 ; always select
	dc.b	1			 ; 1 level down
	dc.b	0
	dc.w	smsq_name-*

smsq_name
	dc.w	24,'SMSQ Q40 Initialisation '
	dc.l	'    '
	dc.w	$200a


	section init

	xref	rtc_init
	xref	hdop_init

	xref	sms_hdop

	xref	sms_rrtc
	xref	sms_srtc
	xref	sms_artc

	include 'dev8_keys_sys'
	include 'dev8_keys_psf'
	include 'dev8_keys_q40'
	include 'dev8_keys_68000'
	include 'dev8_keys_qdos_sms'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_mac_assert'

nasty_base
	jsr	hdop_init		 ; clean initialisation of hdop server

; The nasty initialisation requires a return to system state
; On exit the interrupts are cleared in the status register

	moveq	#sms.xtop,d0
	trap	#do.sms2		 ; do code until rts as extop

	clr.w	psf_sr(a5)		 ; we can clear interrupts on return

	move.b	#3,sys_pmem(a6) 	 ; set default protection level

	lea	sms_hdop,a0
	lea	sms.t1tab+sms.hdop*4,a5  ; set hdop
	bsr.s	q40_insta0

	assert	sms.rrtc,sms.srtc-1,sms.artc-2
	lea	sms.t1tab+sms.rrtc*4,a5  ; set rtc
	lea	sms_rrtc,a0
	bsr.s	q40_insta0		 ; install vector (a0)
	lea	sms_srtc,a0
	bsr.s	q40_insta0		 ; install vector (a0)
	lea	sms_artc,a0
	bsr.s	q40_insta0		 ; install vector (a0)

nasty_cache
	jsr	sms.cenab		 ; enable both caches
	clr.w	sys_castt(a6)		 ; and say enabled

nasty_done
	jsr	rtc_init
	move.l	d1,sys_rtc(a6)		 ; set RTC
	rts

; install in base area

Q40_insta0
	move.l	a0,d0
	swap	d0
	bsr.s	q40_instw
	swap	d0
q40_instw
	jmp	sms.wbase

	end
