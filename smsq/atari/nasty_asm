; Nasty Initialisation for Atari ST for SMSQ

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
	dc.w	26,'SMSQ Atari Initialisation '
	dc.l	'    '
	dc.w	$200a


	section init

	xdef	sms_wbase

	xref	rtc_init
	xref	at_poll
	xref	hw_poll
	xref	init_hdop

	xref	gu_exvt
	xref	sms_hdop

	xref	sms_rrtc
	xref	sms_srtc
	xref	sms_artc

	include 'dev8_keys_sys'
	include 'dev8_keys_psf'
	include 'dev8_keys_atari'
	include 'dev8_keys_68000'
	include 'dev8_keys_qdos_sms'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_mac_assert'

nasty_base
	jsr	init_hdop		 ; clean initialisation of hdop server

; The nasty initialisation requires a return to system state
; On exit the interrupts are cleared in the status register

	moveq	#sms.xtop,d0
	trap	#do.sms2		 ; do code until rts as extop

	clr.w	psf_sr(a5)		 ; we can clear interrupts on return

	move.b	#3,sys_pmem(a6) 	 ; set default protection level

	lea	at_int2,a0		 ; set null interrupt level 2
	move.l	a0,exv_i2
	lea	at_int4,a0		 ; set null interrupt level 4
	move.l	a0,exv_i4

	lea	at_poll,a0
	move.l	a0,vio_200h+mfp_vbas	 ; link in polling interrupt

	lea	hw_poll,a0
	move.l	a0,sms.hpoll		 ; and tidy up

	lea	sms_hdop,a0
	move.l	a0,sms.t1tab+sms.hdop*4  ; set hdop

	assert	sms.rrtc,sms.srtc-1,sms.artc-2
	lea	sms.t1tab+sms.rrtc*4,a1  ; set rtc
	lea	sms_rrtc,a0
	move.l	a0,(a1)+
	lea	sms_srtc,a0
	move.l	a0,(a1)+
	lea	sms_artc,a0
	move.l	a0,(a1)+

	bset	#mfp..tci,mfp_tcm	 ; unmask timer interrupt

	move.b	sys_ptyp(a6),d1 	 ; processor

	cmp.b	#$20,d1 		 ; 020 or more?
	blt.s	nasty_done		 ; ... no

	moveq	#sys.mtyp,d3
	and.b	sys_mtyp(a6),d3 	 ; machine type
	cmp.b	#sys.mmste,d3		 ; mega ste of less?
	bhi.s	nasty_cache		 ; ... no

	lea	nasty_hc30e,a0		 ; enable hypercache
	moveq	#exv_accf,d0		 ; with access fault protection
	jsr	gu_exvt

nasty_cache
	jsr	sms.cenab		 ; enable both caches
	clr.w	sys_castt(a6)		 ; and say enabled

nasty_done
	jsr	rtc_init
	move.l	d1,sys_rtc(a6)		 ; set RTC
	rts

nasty_hc30e
	or.b	#at.hc30e,at_hc30e	 ; enable hypercache 030 caches
	rts

;+++
; Atari SMS2 interrupt 2,4 server sets interrupt mask to level 4
;---
at_int2
at_int4
	or.w	#$0400,(sp)	 ; level 2 (or 3)
	and.w	#$fcff,(sp)	 ; level 2
	rte

sms_wbase
	jmp	sms.wbase

	end
