; Q68 RTC read / set	1.00 (c) W. Lenerz 2017
; 2020-05-08  1.01  always use PC clock, not  sys_rtc (wl)
	xdef	rtc_init

	xdef	sms_rrtc	read clock
	xdef	sms_srtc	set clock
	xdef	sms_artc	adjust clock

	xref	cv_rtdtm
	xref	cv_dtmrt

	include 'dev8_keys_sys'
	include 'dev8_smsq_smsq_base_keys'
	include 'DEV8_keys_qlhw'
	include 'dev8_keys_err'
	include 'dev8_mac_assert'

	section sms

;+++
; Read real time clock
;
;	d1  r	time read
;---
sms_rrtc
	move.l	pc_clock,d1
srtc_rtok
	moveq	#0,d0
	move.l	sms.rte,a5
	jmp	(a5)

;+++
; Adjust real time clock
;
;	d1 cr	adjustment (seconds) / time read
;---
sms_artc
	add.l	pc_clock,d1

;+++
; Set real time clock
;
;	d1 cr	time to set (seconds) / time read
;---
sms_srtc
;	 tst.b	 sys_prtc(a6)			 ; hardware clock protected?
;	 bne.s	 srtc_rtok			 ; yes ->
	move.l	d1,pc_clock			; also set hardware clock
	bra.s	srtc_rtok

;+++
; Intialize SMSQE clock from hardware clock
;
;---
rtc_init
	move.l	pc_clock,sys_rtc(a6)
	rts


	end
