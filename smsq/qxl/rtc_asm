; Read, set and adjust the real time clock  V2.00   1986  Tony Tebby  QJUMP

	section sms

	xdef	sms_rrtc	read clock
	xdef	sms_srtc	set clock
	xdef	sms_artc	adjust clock

	xref	cv_rtdtm
	xref	qxl_mess_add

	include 'dev8_keys_sys'
	include 'dev8_smsq_qxl_keys'
	include 'dev8_smsq_qxl_comm_keys'
	include 'dev8_smsq_smsq_base_keys'

;+++
; Read real time clock
;
;	d1  r	time read
;---
sms_rrtc
	move.l	sys_rtc(a6),d1		; time
	bra.s	sms_rtok

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
	move.l	a1,-(sp)
	move.l	qxl_message,a1
	lea	qxl_ms_date(a1),a1	 ; use fixed buffer
	jsr	cv_rtdtm		 ; convert to ymd hms
	sub.w	#1980,(a1)		 ; years from 1980
	move.b	#qxm.srtc,(a1)		 ; set message
	move.w	#8,-(a1)		 ; set length
	jsr	qxl_mess_add		 ; add the message
	move.l	(sp)+,a1

sms_rtok
	moveq	#0,d0
	move.l	sms.rte,a5
	jmp	(a5)
	end
