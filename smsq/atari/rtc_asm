; Read, set and adjust the real time clock  V2.00   1986  Tony Tebby  QJUMP

	section sms

	xdef	sms_rrtc	read clock
	xdef	sms_srtc	set clock
	xdef	sms_artc	adjust clock

	xref	rtc_set

	include 'dev8_keys_sys'
	include 'dev8_smsq_smsq_base_keys'

;+++
; Read real time clock
;
;	d1  r	time read
;---
sms_rrtc
	move.l	sys_rtc(a6),d1		 ; time
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
	add.l	sys_rtc(a6),d1

;+++
; Set real time clock
;
;	d1 cr	time to set (seconds) / time read
;---
sms_srtc
	move.l	d1,sys_rtc(a6)
	tst.b	sys_prtc(a6)		 ; RTC protected?
	bne	srtc_rtok		 ; ... yes
	move.l	sms.rte,-(sp)		 ; return
	jmp	rtc_set

	end
