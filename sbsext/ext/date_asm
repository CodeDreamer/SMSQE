; DATE / DATE$ / DAY$ / SDATE / ADATE	  V2.01      1990  Tony Tebby	 QJUMP
;
; 2006-06-28  2.01  Added YEAR%, MONTH%, DAY% and WEEKDAY% (MK)
;
;	DATE (date | y,m,d,h,m,s)
;	DATE$ (date | y,m,d,h,m,s)
;	DAY$ (date | y,m,d,h,m,s)
;	SDATE y,m,d,h,m,s
;	ADATE s
;	YEAR% (date)
;	MONTH% (date)
;	DAY% (date)
;	WEEKDAY% (date)

	section exten

	xdef	date
	xdef	date$
	xdef	day$
	xdef	sdate
	xdef	adate
	xdef	year
	xdef	month
	xdef	day
	xdef	weekday

	xref	ut_gxli1		 ; get one long integer
	xref	ut_gtint		 ; get integers
	xref	ut_chkri
	xref	ut_retst
	xref	ut_rtfd1
	xref	ut_rtint
	xref	cv_calst
	xref	cv_dnymd

	include 'dev8_keys_qlv'
	include 'dev8_keys_sbasic'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_err'

ql.zdate equ	$7fffb7e9

date
	bsr	get_date
	jmp	ut_rtfd1

date$
	move.l	cv.ildat*3+qlv.off+2,a4  ; convert to date
	bra.s	date_get
day$
	move.l	cv.ilday*3+qlv.off+2,a4  ; convert to day of week
date_get
	bsr.s	get_date
	move.l	d1,-(sp)
	moveq	#30,d1
	jsr	ut_chkri		 ; get space on stack
	move.l	(sp)+,d1

	jsr	(a4)			 ; convert it
	jmp	ut_retst		 ; return string

sdate
	bsr.s	get_date
	moveq	#sms.srtc,d0
	bra.s	date_trap

adate
	jsr	ut_gxli1		 ; get adjustment
	bne.s	date_rts
	move.l	(a6,a1.l),d1
	moveq	#sms.artc,d0

date_trap
	trap	#do.sms2
date_rts
	rts

weekday
	bsr.s	get_date
	lsr.l	#1,d1
	divu	#24*60*60/2,d1		 ; date and time
	andi.l	#$ffff,d1
	divu	#7,d1
	swap	d1
	jmp	ut_rtint

year
	moveq	#4,d7
	bra.s	date_part
month
	moveq	#2,d7
	bra.s	date_part
day
	moveq	#0,d7
date_part
	bsr.s	get_date
	lsr.l	#1,d1
	divu	#24*60*60/2,d1		 ; date and time
	andi.l	#$ffff,d1
	add.l	#ql.zdate,d1		 ; in external format

	moveq	#0,d0
	jsr	cv_calst
	jsr	cv_dnymd		 ; set year month and day

	move.w	d2,-(sp)
	move.l	d1,-(sp)
	move.w	0(sp,d7.w),d1
	addq.l	#6,sp
	jmp	ut_rtint

get_date
	move.l	a5,d0
	sub.l	a3,d0			 ; any param?
	beq.s	getd_rrtc
	subq.w	#8,d0			 ; one param?
	beq.s	getd_long
	sub.w	#5*8,d0 		 ; 6 params?
	beq.s	getd_ints		 ; ... yes
	addq.w	#8,d0			 ; 5 params
	bne.s	getd_ipar		 ; ... no
	move.l	sb_arthp(a6),a1
	subq.l	#2,a1
	clr.w	(a6,a1.l)
	move.l	a1,sb_arthp(a6) 	 ; fake 0 seconds

getd_ints
	jsr	ut_gtint		 ; get integers
	bne.s	getd_rt4
	add.l	a6,a1
	jsr	cv.datil*3+qlv.off	 ; convert y,m,d, h,m,s to long word
	sub.l	a6,a1
	bra.s	getd_arthp

getd_long
	jsr	ut_gxli1		 ; one long int
	bne.s	getd_rt4
	move.l	(a6,a1.l),d1
	addq.l	#4,a1			 ; pop it
getd_arthp
	move.l	a1,sb_arthp(a6)
	rts

getd_rrtc
	moveq	#sms.rrtc,d0		 ; read date from real time clock
	trap	#do.sms2
	rts

getd_ipar
	moveq	#err.ipar,d0
getd_rt4
	addq.l	#4,sp
	rts

	end
