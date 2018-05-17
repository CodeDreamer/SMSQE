; Atari RTC read / set

	section init

	xdef	rtc_init  ; smsq
	xdef	rtc_set   ; smsq

	xref	cv_rtdtm
	xref	cv_dtmrt

	include 'dev8_keys_atari_rtc'
	include 'dev8_keys_err'
	include 'dev8_keys_sys'
	include 'dev8_mac_assert'

rtt_rego dc.b	rtt.secs,rtt.mins,rtt.hours,rtt.dow,rtt.day,rtt.month,rtt.year
	ds.w	0
;+++
; Initialise date taking the Year Month Day Hour Minute and Second from the RTC
;
;	d1  r  date
;	status return 0 or err.nc
;---
rtc_init
;+++
; Read date taking the Year Month Day Hour Minute and Second from the RTC
;
;	d1  r  date
;	status return 0 or err.nc
;---
hw_rrtc
ard.reg reg	a0/a1
ard.frame equ	$08
	movem.l ard.reg,-(sp)
	move.l	sp,a1			 ; get date on stack
	subq.w	#ard.frame,sp
	moveq	#sys.mtyp,d0		 ; machine ID bits
	and.b	sys_mtyp(a6),d0
	beq.l	ard_nc			 ; no RTC

	cmp.w	#sys.mfal,d0		 ; Falcon or TT?
	bhs.s	ard_tt			 ; ... yes

	bclr	#rtc..stp,rtc_mode	 ; stop the clock

	lea	at_mrtc,a0		 ; from ST RTC

	assert	rtc_seco-at_mrtc,1
	bsr.s	ard_dec 		 ; get seconds
	assert	rtc_mino-at_mrtc,5
	bsr.s	ard_dec 		 ; get minutes
	assert	rtc_hro-at_mrtc,9
	bsr.s	ard_dec 		 ; get hours
	addq.w	#2,a0			 ; skip day of week
	st	-(a1)
	assert	rtc_dayo-at_mrtc,$f
	bsr.s	ard_dec 		 ; get days
	assert	rtc_mono-at_mrtc,$13
	bsr.s	ard_dec 		 ; get months
	assert	rtc_yro-at_mrtc,$17
	bsr.s	ard_dec 		 ; get years
	clr.b	-(a1)			 ; clear msb of years
	add.w	#1980,(a1)		 ; set true date

	bset	#rtc..stp,rtc_mode	 ; start the clock
	bra.s	ard_set

ard_dec
	moveq	#$f,d0			 ; ls nibble only
	and.w	(a0)+,d0		 ; get units
	moveq	#$f,d1
	and.w	(a0)+,d1		 ; and tens
	add.b	d1,d1			 ; *2
	add.w	d1,d0
	lsl.w	#2,d1			 ; *8
	add.w	d1,d0
	move.b	d0,-(a1)		 ; put digit (0-99) into buffer
	rts

ard_tt
	move.b	#rtt.ctld,rtc_sreg	 ; select register d
	btst	#rtt..vrt,rtc_data	 ; valid time?
	beq.s	ard_nc			 ; no!?
ard_wait
	move.b	#rtt.ctla,rtc_sreg	 ; register a
	btst	#rtt..upd,rtc_data	 ; update in progress?
	bne.s	ard_wait		 ; yes, so wait

	moveq	#6,d0			 ; seven registers to do
	lea	rtt_rego,a0		 ; register order
ard_copy
	move.b	(a0)+,rtc_sreg		 ; select register
	move.b	rtc_data,-(a1)		 ; and data into buffer
	dbra	d0,ard_copy
	clr.b	-(a1)
	add.w	#1968,(a1)		 ; year was base 1968

ard_set
	jsr	cv_dtmrt		 ; convert to real time
ard_exit
	addq.l	#ard.frame,sp
	movem.l (sp)+,ard.reg
	rts

ard_nc
	moveq	#err.nc,d0
	bra.s	ard_exit


;+++
; Set date putting Year Month Day DoW Hour Minute and Second into the RTC
;
;	d1 cp  date
;	status return 0 or err.nc
;---
rtc_set
;+++
; Set date putting Year Month Day DoW Hour Minute and Second into the RTC
;
;	d1 cp  date
;	status return 0 or err.nc
;---
hw_srtc
asd.reg  reg	a0/a1
asd.frame equ	$08
	movem.l asd.reg,-(sp)
	subq.l	#asd.frame,sp
	move.l	sp,a1
	jsr	cv_rtdtm		 ; convert date to date and time

	moveq	#sys.mtyp,d0		 ; machine ID bits
	and.b	sys_mtyp(a6),d0
	beq.s	asd_ok			 ; no RTC, give up

	cmp.w	#sys.mfal,d0		 ; Falcon or TT?
	bhs.s	asd_tt			 ; ... yes

	sub.w	#1980,(a1)		 ; set ST date

	bclr	#rtc..stp,rtc_mode	 ; stop the clock

	lea	at_mrtc,a0		 ; to ST RTC
	addq.l	#8,a1			 ; starting at seconds

	assert	rtc_seco-at_mrtc,1
	bsr.s	asd_dec 		 ; set seconds
	assert	rtc_mino-at_mrtc,5
	bsr.s	asd_dec 		 ; set minutes
	assert	rtc_hro-at_mrtc,9
	bsr.s	asd_dec 		 ; set hours
	addq.w	#2,a0			 ; skip day of week
	subq.l	#1,a1
	assert	rtc_dayo-at_mrtc,$f
	bsr.s	asd_dec 		 ; set days
	assert	rtc_mono-at_mrtc,$13
	bsr.s	asd_dec 		 ; set months
	assert	rtc_yro-at_mrtc,$17
	bsr.s	asd_dec 		 ; set years

	bset	#rtc..stp,rtc_mode	 ; start the clock
	bra.s	asd_ok

asd_dec
	moveq	#0,d0			 ; next byte
	move.b	-(a1),d0
	divu	#10,d0			 ; units in msw, tens in lsw
	move.l	d0,(a0)+		 ; set both
	rts

asd_tt
	sub.w	#1968,(a1)		 ; TT year is base 1968

	move.b	#rtt.ctlb,rtc_sreg	 ; select register b
	move.b	#rtt.set+rtt.sqwe+rtt.bin+rtt.24h,rtc_data ; to init

	move.b	#rtt.ctla,rtc_sreg	 ; register a
	move.b	#rtt.32768+rtt.rt10,rtc_data ; init

	moveq	#6,d0			 ; seven registers to do
	lea	rtt_rego,a0		 ; register order
	addq.l	#8,a1			 ; starting from here

asd_copy
	move.b	(a0)+,rtc_sreg		 ; select register
	move.b	-(a1),rtc_data		 ; and data into buffer
	dbra	d0,asd_copy

	move.b	#rtt.ctlb,rtc_sreg	 ; select register b
	move.b	#rtt.sqwe+rtt.bin+rtt.24h,rtc_data ; to init (not setting)
 
asd_ok
	moveq	#0,d0
asd_exit
	addq.l	#asd.frame,sp
	movem.l (sp)+,asd.reg
	rts


	end
