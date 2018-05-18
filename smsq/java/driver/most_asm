; Base area SMSQmulator Drivers  V.1.05 (c) W. Lenerz 2012-2017

; v. 1.01  possibly set up scheduler loop to check if idle  & cleanup
; v. 1.02 2015 May 24	possibly use ieee routines
; v. 1.03 2015 Jul 29	integrated the rtc routines
; v. 1.04 2016 Oct 13	call to init copy QL screen routine
; v. 1.05 2017 Apr 07	ctrl_nit replaces timer,scrap;patchfpu also in ctrl thing

	section header

	xref	smsq_end

header_base
	dc.l	gl_most-header_base	 ; length of header
	dc.l	0			 ; module length unknown
	dc.l	smsq_end-gl_most	 ; loaded length
	dc.l	0			 ; checksum
	dc.l	0			 ; always select
	dc.b	1			 ; 1 level down
	dc.b	0
	dc.w	smsq_name-*

smsq_name
	dc.w	18,'SMSQ JAVA Drivers '
	dc.w	$200a


	section base

	xref	iob_smsq		 ; SMSQ IOB bits

	xref	qd_int2
	xref	qpc_patchfpu
	xref	hdop_init
	xref	nfa_init
	xref	mem_init
	xref	sfa_init
	xref	win_init
	xref	sms_hdop
	xref	dev_init
	xref	nul_init
	xref	pipe_init
	xref	rd_init
	xref	iob_init
	xref	history_init
	xref	ssss_init
	xref	hdop_keyr
	xref	snd_init
	xref	cpyscr_init
	xref	ctrl_init

	include 'dev8_keys_qlv'
	include 'dev8_keys_sys'
	include 'dev8_keys_psf'
	include 'dev8_keys_68000'
	include 'dev8_keys_qlhw'
	include 'dev8_keys_qdos_sms'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_smsq_smsq_config_keys'
	include 'dev8_smsq_gold_keys'
	include 'dev8_keys_qdos_ioa'
	include 'dev8_keys_java'


gl_most
	bra.l	start

	xdef	iou_dirshr
iou_dirshr dc.w 0	; shared directories supported	(RAM disk)

	section init

start	jsr	hdop_init
	jsr	dev_init
	jsr	nul_init
	jsr	pipe_init
	jsr	rd_init
	jsr	iob_init
	jsr	history_init
	jsr	sfa_init
	jsr	nfa_init
	jsr	mem_init
	jsr	win_init		; keep as last device driver
	jsr	ssss_init
	jsr	snd_init
	jsr	cpyscr_init
	jsr	ctrl_init

sys_init

; When we have linked in the interrupt servers, we can enable the interrupts

	moveq	#sms.xtop,d0
	trap	#do.sms2

	clr.w	psf_sr(a5)		; we can clear interrupts on return

	lea	qd_int2,a0		; set interrupt routine address
	lea	exv_i2,a5
	move.l	a0,(a5)

	move.b	 #0,sys_qlir(a6)	; always clear interface int

	lea	hw_poll,a0
	lea	sms.hpoll,a5		; setup harware poll, which is just anrts
	move.l	a0,(a5)

	lea	sms_hdop,a0
	lea	sms.t1tab+sms.hdop*4,a1 ; set hdop
	move.l	a0,(a1)+
	tst.b	sms.conf+sms_ieee	; use patched FP routines?
	beq.s	cpu_rtc 		; no ->
	bsr	qpc_patchfpu
cpu_rtc
	lea	sms.t1tab+sms.rrtc*4,a1  ; set rtc
	lea	sms_rrtc,a0
	move.l	a0,(a1)+
	lea	sms_srtc,a0		; srtc and artc are not implemented
	move.l	a0,(a1)+
	move.l	a0,(a1)

cpu_idl tst.b	sms.conf+sms_nqimi	; use less cpu if idle?
	beq.s	exit			; no ->...
	lea	sched,a0		; yes, link in my scheduler routine
	clr.l	(a0)+			; just in case
	lea	jva_sched,a1
	move.l	a1,(a0)
	subq.l	#4,a0
	moveq	#sms.lshd,d0
	trap	#$1
exit	clr.l	d0
hw_poll 				; this is the "hardware poll" !!!!!!
	rts

	dc.l	0			; space for possible scheduler routine link
sched	dc.l	0,0


**********
*
* Java scheduler routine - tries to see whether machine is busy
*
**********
jva_sched
	dc.w	jva.trp7
	rts

;+++
; Read date taking the Year Month Day Hour Minute and Second from the RTC
;
;	d1  r  date
;	status return 0 or err.nc
;---
sms_rrtc
	move.l	sms.rte,-(sp)
	moveq	#jt5.time,d0
	dc.w	jva.trp5			; get time from JVM
	rts

;+++
; Adjust the RTC date by D1 seconds
;
;	d1 cr  adjustment / date
;	status return 0
;---
;+++
; Set date taking putting Year Month Day Hour Minute and Second into the RTC
;
;	d1 cp  date
;	status return 0 or err.nc
;---

; these are not implemented

sms_srtc
sms_artc
	moveq	#jt5.qry,d0
	dc.w	jva.trp5
	move.l	sms.rte,-(sp)
	moveq	#0,d0
	rts

	end
