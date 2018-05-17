; Read, set and adjust the real time clock  V2.00   1986  Tony Tebby  QJUMP

	section sms

	xdef	sms_rrtc	read clock
	xdef	sms_srtc	set clock
	xdef	sms_artc	adjust clock

	xref	cv_rtdos

	include 'dev8_keys_sys'
	include 'dev8_smsq_qpc_keys'
	include 'dev8_smsq_smsq_base_keys'

;+++
; Read real time clock
;
;	d1  r	time read
;---
sms_rrtc
	move.l	qpc_rtc,d1	     ; time
	move.l	sms.rte,a5
	jmp	(a5)

;+++
; Adjust real time clock
;
;	d1 cr	adjustment (seconds) / time read
;---
sms_artc
	add.l	qpc_rtc,d1

;+++
; Set real time clock
;
;	d1 cr	time to set (seconds) / time read
;---
sms_srtc
	move.l	d1,qpc_rtc
	tst.b	sys_prtc(a6)		 ; protected date?
	bne.s	sms_rtok
	move.l	a0,-(sp)
	move.l	d1,-(sp)
	jsr	cv_rtdos		 ; convert to dos
	dc.w	qpc.sclck+1		 ; set clock of PC
	move.l	(sp)+,d1
	move.l	(sp)+,a0
sms_rtok
	moveq	#0,d0
	move.l	sms.rte,a5
	jmp	(a5)
	end
