; Super GOLD Card RTC operations

	section sms

	xdef	sms_artc
	xdef	sms_rrtc
	xdef	sms_srtc

	xdef	rtc_init

	xdef	rtc.ptype
	xdef	rtc.card
rtc.ptype equ	$20
rtc.card  equ	'SGC '

	include 'dev8_smsq_gold_keys'
	include 'dev8_keys_err'
	include 'dev8_keys_sys'
	include 'dev8_mac_assert'
	include 'dev8_smsq_smsq_base_keys'

rtc_init
	rts

;+++
; Read date taking the Year Month Day Hour Minute and Second from the RTC
;
;	d1  r  date
;	status return 0 or err.nc
;---
sms_rrtc
	move.l	sms.rte,-(sp)

gl_rdate
	tst.b	sys_prtc(a6)		; protected date?
	beq.s	grd_do			; ... no
grd_ql
	move.l	ql_clock,d0		; date
	move.l	ql_clock,d1		; and again
	sub.l	d1,d0			; ... the same?
	bne.s	grd_ql
	rts

;--------

grd_60
	mulu	#60,d3
grd_10
	moveq	#5,d5
grd_10l
	subq.w	#1,d5
	blt.s	grd_10p

	move.w	#$f0,d0
	move.w	d0,d4
	and.w	(a1),d4 		 ; get 10s
	and.w	(a1),d0
	cmp.w	d0,d4			 ; stable data?
	bne.s	grd_10l 		 ; ... no
	lsr.b	#4,d0
	cmp.b	#9,d0
	bge.s	grd_10l
grd_10p
	subq.l	#glk.rtca,a1
	add.w	d0,d0
	move.w	d0,d2
	lsl.w	#2,d2
	add.w	d0,d2			 ; *10
grd_unit
	moveq	#0,d0			 ; ... patch
grd_ul
	subq.w	#1,d5
	blt.s	grd_up

	move.w	#$f0,d0
	move.w	d0,d4
	and.w	(a1),d4 		 ; get units
	and.w	(a1),d0
	cmp.w	d0,d4			 ; stable data?
	bne.s	grd_ul			 ; ... no
	lsr.b	#4,d0
	cmp.b	#9,d0
	bge.s	grd_ul
grd_up
	subq.l	#glk.rtca,a1
	add.w	d2,d0			 ; add 10s
	rts

grd_mtab
	dc.w	0
	dc.w	31
	dc.w	31+28
	dc.w	31+28+31
	dc.w	31+28+31+30
	dc.w	31+28+31+30+31
	dc.w	31+28+31+30+31+30
	dc.w	31+28+31+30+31+30+31
	dc.w	31+28+31+30+31+30+31+31
	dc.w	31+28+31+30+31+30+31+31+30
	dc.w	31+28+31+30+31+30+31+31+30+31
	dc.w	31+28+31+30+31+30+31+31+30+31+30
grd_mtbe


; Read RTC

grd_do
grd.reg  reg	d2/d3/d4/d5/d6/d7/a1
	movem.l grd.reg,-(sp)
	moveq	#10,d6			; try lots of times
grd_loop
	lea	glc_base,a1
grd_rflags
	move.w	#$f0,d0
	move.w	d0,d7
	and.w	glo_rtcs(a1),d0
	and.w	glo_rtcs(a1),d7
	cmp.b	d0,d7			 ; stable data
	bne.s	grd_rflags

	swap	d7			 ; am / pm

	lea	glo_s10(a1),a1
	bsr.l	grd_10
	move.w	d0,d7			 ; keep seconds
	lea	glo_y10-glo_s10+glk.rtca*2(a1),a1
	bsr.l	grd_10			 ; get year
	move.w	d0,d1
	mulu	#365,d1 		 ; year to days
	divu	#4,d0
	add.w	d0,d1			 ; + leap years
	swap	d0
	move.w	d0,d3			 ; keep remainder
	bsr.l	grd_10			 ; month (1-12??)
	subq.w	#3,d3			 ; is it a leap year?
	bne.s	grd_smonth		 ; ... no
	cmp.w	#2,d0			 ; beyond February?
	ble.s	grd_smonth		 ; ... no
	addq.w	#1,d1			 ; yes, add a day
grd_smonth
	add.w	d0,d0
	add.w	grd_mtab-2(pc,d0.w),d1	 ; add first day of month table

	bsr.l	grd_10
	subq.w	#1,d0			 ; day is 1 to 31
	add.w	d0,d1			 ; ... this is now the complete day
	mulu	#12*60*60,d1		 ; 2 seconds
	add.l	d1,d1			 ; seconds

	bsr.l	grd_10			 ; hours
	btst	#glk..24h+16,d7 	 ; 24 hour?
	bne.s	grd_min 		 ; ... yes
	divu	#12,d0			 ; time is mod 12
	swap	d0
	btst	#glk..pm+16,d7		 ; but is is PM?
	beq.s	grd_min 		 ; ... no
	add.w	#12,d0			 ; ... yes
grd_min
	move.w	d0,d3
	bsr.l	grd_60			 ; hours+minutes
	add.w	d0,d3
	bsr.l	grd_60			 ; +seconds
	add.l	d0,d3
	add.l	d3,d1			 ; complete date

	sub.w	d7,d0			 ; time changed?
	dbeq	d6,grd_loop
	moveq	#0,d0			 ; ... too many times
grd_exit
	movem.l (sp)+,grd.reg
	rts

;+++
; Adjust the RTC date by D1 seconds
;
;	d1 cr  adjustment / date
;	status return 0
;---
sms_artc
gl_adate
	move.l	d1,-(sp)		 ; save adjustment
	bsr.l	gl_rdate
	add.l	(sp)+,d1		 ; adjusted date

;+++
; Set date taking putting Year Month Day Hour Minute and Second into the RTC
;
;	d1 cp  date
;	status return 0 or err.nc
;---
sms_srtc
	move.l	sms.rte,-(sp)

gl_sdate
gsd.reg  reg	d1/d2/d3/d4/d5/d6/d7/a0/a1/a2
stk_d1	equ	$00
	movem.l gsd.reg,-(sp)

	lea	ql_clock,a1
	clr.b	(a1)+			 ; clear the clock
	moveq	#$ffffffdf,d3		 ; set one byte at a time
	moveq	#3,d2			 ; four altogether
gsd_sqlb
	asr.w	#1,d3			 ; next byte
	moveq	#0,d0
	rol.l	#8,d1
	move.b	d1,d0			 ; msbyte
	bra.s	gsd_sqel
gsd_sqlp
	move.b	d3,(a1) 		 ; clock register
gsd_sqel
	dbra	d0,gsd_sqlp
	dbra	d2,gsd_sqlb

	moveq	#0,d0
	tst.b	sys_prtc(a6)		 ; protect the clock?
	bne.l	gsd_exit

	moveq	#10,d6
gsd_rtry
	lea	glc_base,a2
	move.w	sr,-(sp)
	or.w	#$0700,sr
	sf	glo_rtcc(a2)
	sf	glo_dow(a2)
	sf	glo_d01(a2)
	move.b	#glk.rtcs,glo_rtcc(a2)	 ; clock stop
	move.w	(sp)+,sr
	lea	glo_s01(a2),a1
	move.l	stk_d1(sp),d1

	moveq	#0,d0			 ; seconds
	lsr.l	#1,d1			 ; prevent overflow on divide
	roxr.w	#1,d0			 ; seconds bit in msb d0.w

	divu	#24*60*30,d1		 ; split into days / 2 seconds

	moveq	#0,d3
	move.w	d1,d3
	move.w	d0,d1
	rol.l	#1,d1			 ; seconds in upper d1 (ovf in lsb)
	moveq	#6,d2
	bsr.l	gsd_divp		 ; divide upper d1 by 10 and d2 and put
	bsr.l	gsd_divp		 ; twice
	clr.w	d1
	swap	d1			 ; hours are the remainder
	divu	#12,d1
	move.w	d1,d7			 ; 0/1 in d7 is am/pm
	clr.w	d1
	swap	d1			 ; if remainder is 0
	bne.s	gsd_hours
	moveq	#12,d1			 ; it's 12 o'clock
gsd_hours
	bsr.l	gsd_putd

	divu	#365*4+1,d3		 ; four year cycle
	move.l	d3,d1			 ;
	swap	d1			 ; four year day
	lsl.w	#2,d3			 ; years

	cmp.w	#365*3+31+28,d1 	 ; is it Feb 29?
	blt.s	gsd_day
	bgt.s	gsd_day1

	addq.w	#3,d3			 ; leap year
	moveq	#2,d2			 ; leap month
	moveq	#29,d1			 ; leap day
	bra.s	gsd_pday

gsd_day1
	subq.w	#1,d1			 ; after Feb 29 adjust by one day
gsd_day
	ext.l	d1
	divu	#365,d1

	add.w	d1,d3			 ; real years
	clr.w	d1
	swap	d1			 ; day of year

	moveq	#13,d2			 ; month
	lea	grd_mtbe,a0
gsd_mloop
	subq.w	#1,d2
	cmp.w	-(a0),d1
	blt.s	gsd_mloop

	sub.w	(a0),d1 		 ; day of month
	addq.w	#1,d1			 ; 1..31
gsd_pday
	bsr.s	gsd_putd

	move.w	d2,d1			 ; month
	bsr.s	gsd_putd

	move.w	d3,d1			 ; year
	bsr.s	gsd_putd

	moveq	#0,d1
	lea	$3c(a2),a1
	bsr.s	gsd_put

	addq.b	#1,d3			 ; leap year counter (1961 base)
	assert	glk..pm+1,glk.lpsf
	add.b	d3,d3
	add.b	d7,d3
	lsl.b	#glk..pm,d3
	move.b	d3,d1
	lea	glo_rtcs(a2),a1 	 ; leap year / am/pm
	bsr.s	gsd_pnib

	move.w	sr,-(sp)
	or.w	#$0700,sr
	sf	glo_rtcc(a2)
	sf	glo_dow(a2)
	sf	glo_d01(a2)
	move.b	#glk.rtcg,glo_rtcc(a2)	 ; clock go
	move.w	(sp)+,sr

	jsr	grd_do			 ; date correct?
	move.l	d1,d0
	move.l	stk_d1(sp),d1
	sub.l	d1,d0
	dbeq	d6,gsd_rtry
gsd_exit
	moveq	#0,d0			 ; ... no
	movem.l (sp)+,gsd.reg
	rts

; put two decimal digits (d1)

gsd_putd
	divu	#10,d1
	bsr.s	gsd_putm
	bra.s	gsd_putm

; divide d1 by d2 and put two decimal digits

gsd_divp
	swap	d1
	divu	#10,d1
	bsr.s	gsd_putm		 ; next nibble is 0-9
	swap	d1
	divu	d2,d1			 ; get 10s

gsd_putm
	swap	d1
gsd_put
	lsl.b	#4,d1
gsd_pnib
	moveq	#8,d5
gsd_putl
	move.w	sr,-(sp)
	or.w	#$0700,sr
	sf	glo_rtcc(a2)
	sf	glo_dow(a2)
	sf	glo_d01(a2)
	move.b	d1,(a1)
	not.b	d1
	sf	glo_rtcc(a2)
	sf	glo_dow(a2)
	sf	glo_d01(a2)
	move.b	d1,glo_ten(a2)
	move.w	(sp)+,sr
	not.b	d1
	moveq	#$fffffff0,d4
	and.w	(a1),d4
	cmp.b	d4,d1
	beq.s	gsd_putx
	dbra	d5,gsd_putl
	move.b	#1,sys_prtc(a6)
gsd_putx
	addq.l	#glk.rtca,a1
	clr.w	d1			 ; we've used this bit
	rts

	end
