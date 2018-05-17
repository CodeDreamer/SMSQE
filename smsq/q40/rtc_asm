; Q40 RTC read / set

	xdef	rtc_init

	xdef	sms_rrtc	read clock
	xdef	sms_srtc	set clock
	xdef	sms_artc	adjust clock

	xref	cv_rtdtm
	xref	cv_dtmrt

	include 'dev8_keys_sys'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_keys_q40'
	include 'dev8_keys_err'
	include 'dev8_mac_assert'

	section sms

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

	section rtc

;+++
; Initialise date taking the Year Month Day Hour Minute and Second from the RTC
;
;	d1  r  date
;	status return 0 or err.nc
;---
rtc_init
hw_rrtc
rtci.reg reg	 d2/a0/a1
rtci.frame equ	 $08
	movem.l rtci.reg,-(sp)
	subq.w	#rtci.frame,sp

	lea	Q40_rtc,a0

	bset	#q40r..read,q40r_ctl(a0) ; set read data mode

	lea	(sp),a1

	moveq	#0,d1
	moveq	#q40r_year,d0
	bsr.s	rtc_rbyte
	cmp.w	#70,d1			 ; 1970+?
	bhs.s	rtci_year
	add.w	#100,d1
rtci_year
	add.w	#1900,d1
	move.w	d1,(a1)+

	moveq	#q40r_month,d0
	bsr.s	rtc_rbyte
	move.b	d1,(a1)+

	moveq	#q40r_day,d0
	bsr.s	rtc_rbyte
	move.b	d1,(a1)+

	st	(a1)+			 ; do not bother about dow

	moveq	#q40r_hour,d0
	bsr.s	rtc_rbyte
	move.b	d1,(a1)+

	moveq	#q40r_min,d0
	bsr.s	rtc_rbyte
	move.b	d1,(a1)+

	moveq	#q40r_sec,d0
	bsr.s	rtc_rbyte
	move.b	d1,(a1)+

	lea	(sp),a1

	bclr	#q40r..read,q40r_ctl(a0) ; unset read data mode

	jsr	cv_dtmrt		 ; convert to real time

rtci_exit
	addq.l	#rtci.frame,sp
	movem.l (sp)+,rtci.reg
	rts

rtc_rbyte
	move.b	(a0,d0.w),d1
	moveq	#$f,d2
	and.b	d1,d2	       ; units
	lsr.b	#4,d1	       ; tens
	mulu	#10,d1
	add.b	d2,d1
	rts

rtc_wbyte
	divu	#10,d1
	move.l	d1,d2
	lsl.b	#4,d1		 ; int(x/10) * 16
	swap	d2		 ; +remainder
	add.b	d2,d1
	move.b	d1,(a0,d0.w)
	moveq	#0,d1
	rts

;+++
; Set date putting Year Month Day DoW Hour Minute and Second into the RTC
;
;	d1 cp  date
;	status return 0 or err.nc
;---
rtc_set
rtcs.reg  reg	 d1/d2/a0/a1
rtcs.frame equ	 $08
	movem.l rtcs.reg,-(sp)
	subq.l	#rtcs.frame,sp
	move.l	sp,a1
	jsr	cv_rtdtm		 ; convert date to date and time

	lea	q40_rtc,a0

	moveq	#$3f,d0
	or.b	q40r_ctl(a0),d0 	  ; keep calibration data
	bset	#q40r..write,d0
	move.b	d0,q40r_ctl(a0) 	  ; set write data mode
	move.b	d0,q40r_ctl(a0) 	  ; and confirm

	lea	(sp),a1

	moveq	#0,d1
	move.w	(a1)+,d1
	sub.w	#2000,d1
	bge.s	rtcs_year
	add.w	#100,d1
rtcs_year
	moveq	#q40r_year,d0
	bsr.s	rtc_wbyte

	move.b	(a1)+,d1
	moveq	#q40r_month,d0
	bsr.s	rtc_wbyte

	move.b	(a1)+,d1
	moveq	#q40r_day,d0
	bsr.s	rtc_wbyte

	move.b	(a1)+,d1
	bpl.s	rtcs_sdow
	moveq	#1,d1
rtcs_sdow
	moveq	#q40r_dow,d0
	bsr.s	rtc_wbyte

rtcs_hour
	move.b	(a1)+,d1
	moveq	#q40r_hour,d0
	bsr.s	rtc_wbyte

	move.b	(a1)+,d1
	moveq	#q40r_min,d0
	bsr.s	rtc_wbyte

	move.b	(a1)+,d1
	moveq	#q40r_sec,d0
	bsr.s	rtc_wbyte

	bclr	#q40r..write,q40r_ctl(a0) ; unset write data mode

	moveq	#0,d0
rtcs_exit
	addq.l	#rtcs.frame,sp
	movem.l (sp)+,rtcs.reg
	rts


	end
