; Nasty Initialisation for Gold Card for SMSQ

	section header

	xref	smsq_end
	xref.l	rtc.card
	xref.s	rtc.ptype

	include 'dev8_keys_stella_bl'

header_base
	dc.l	nasty_base-header_base	 ; length of header
	dc.l	0			 ; module length unknown
	dc.l	smsq_end-nasty_base	 ; loaded length
	dc.l	0			 ; checksum
	dc.l	select-header_base	 ; select on processor
	dc.b	1			 ; 1 level down
	dc.b	0
	dc.w	smsq_name-*

smsq_name
	dc.w	25,'SMSQ Initialisation - '
	dc.l	rtc.card
	dc.l	'    '
	dc.w	$200a

select
	cmp.b	#rtc.ptype,sbl_ptype(a5)  ; the right processor?
	bne.s	sel_noload
	moveq	#sbl.load,d0
	rts
sel_noload
	moveq	#sbl.noload,d0
	rts

	section init

	xref	rtc_init

	xref	sms_rrtc
	xref	sms_srtc
	xref	sms_artc

	include 'dev8_keys_sys'
	include 'dev8_keys_qdos_sms'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_mac_assert'

nasty_base

; The nasty initialisation requires a return to system state
; The interrupts remain masked

	moveq	#sms.xtop,d0
	trap	#do.sms2		 ; do code until rts as extop

	assert	sms.rrtc,sms.srtc-1,sms.artc-2
	lea	sms.t1tab+sms.rrtc*4,a5  ; set rtc
	lea	sms_rrtc,a0
	bsr.s	nasty_wbase
	lea	sms_srtc,a0
	bsr.s	nasty_wbase
	lea	sms_artc,a0
	bsr.s	nasty_wbase

	jsr	sms.cenab		 ; enable 'both' caches
	clr.w	sys_castt(a6)		 ; and say caches enabled

	jsr	rtc_init
	move.l	d1,sys_rtc(a6)		 ; set RTC
	rts

nasty_wbase
	move.l	a0,d0			 ; write a0 to vector area
	swap	d0
	bsr.s	wb_do
	swap	d0
wb_do
	jmp	sms.wbase

	end
