; Date Conversions, QL compatible	        1990  Tony Tebby

	section uq

	xdef	uq_ldate
	xdef	uq_ldowk
	xdef	uq_datel

	xref	cv_calst
	xref	cv_dnymd
	xref	cv_ymddn

	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_msgc'


ql.zdate equ	$7fffb7e9
;+++
; Push QL format date (20 character string) onto stack (a1,a6)
;
;	d1 c  p date
;	a1 c  p pointer to stack
;
;	status return zero
;---
uq_ldate
uld.reg reg	d1/d2/d3/a0
	movem.l uld.reg,-(sp)
	add.l	a6,a1			 ; OK for us
	moveq	#0,d3
	lsr.l	#1,d1
	roxr.l	#1,d3			 ; keep lsb

	divu	#24*60*60/2,d1		 ; date and time
	swap	d1
	move.w	d1,d3			 ; time
	rol.l	#1,d3			 ; ... corrected
	clr.w	d1
	swap	d1			 ; date
	add.l	#ql.zdate,d1		 ; in external format

	bsr.s	uld_p60 		 ; set seconds
	move.b	#':',-(a1)

	bsr.s	uld_p60 		 ; set minutes
	move.b	#':',-(a1)

	bsr.s	uld_p10 		 ; set hours
	move.b	#' ',-(a1)

	moveq	#0,d0
	jsr	cv_calst
	jsr	cv_dnymd		 ; set year month and day

	move.l	d1,d3
	clr.w	d3
	swap	d3
	bsr.s	uld_p10 		 ; set days
	move.b	#' ',-(a1)

	move.l	a1,-(sp)
	move.w	#msgc.mth3,a1		 ; three letter months
	moveq	#sms.mptr,d0
	trap	#do.smsq
	lea	2(a1),a0		 ; set month
	add.w	d1,a0
	add.w	d1,a0
	add.w	d1,a0
	move.l	(sp)+,a1
	move.b	-(a0),-(a1)
	move.b	-(a0),-(a1)
	move.b	-(a0),-(a1)
	move.b	#' ',-(a1)

	move.w	d2,d3
	bsr.s	uld_p1k 		 ; set year

	move.w	#20,-(a1)		 ; string length
	sub.l	a6,a1			 ; and restore pointer

	moveq	#0,d0
	movem.l (sp)+,uld.reg
	rts

uld_p1k
	moveq	#10,d0
	bsr.s	uld_pdig		 ; fourth digit
	bsr.s	uld_pdig		 ; third digit
uld_p10
	moveq	#10,d0
	bsr.s	uld_pdig		 ; second digit
	bra.s	uld_prem		 ; ... and remainder
uld_p60
	moveq	#10,d0
	bsr.s	uld_pdig		 ; second of mod 60
	moveq	#6,d0			 ; first of mod 60
uld_pdig
	divu	d0,d3			 ; divide
	swap	d3			 ; ... to get remainder
uld_prem
	add.b	#'0',d3
	move.b	d3,-(a1)		 ; put digit on stack
	clr.w	d3
	swap	d3
	rts

;+++
; Push QL format doy of week (3 character string) onto stack (a1,a6)
;
;	d1 c  p date
;	a1 c  p pointer to stack
;
;	status return zero
;---
uq_ldowk
udw.reg reg	d1/a0
	movem.l udw.reg,-(sp)
	add.l	a6,a1			 ; we can do this!!
	lsr.l	#1,d1
	divu	#24*60*60/2,d1
	moveq	#0,d0
	move.w	d1,d0			 ; day
	divu	#7,d0
	move.l	d0,d1
	swap	d1			 ; ... of week

	move.l	a1,-(sp)
	move.w	#msgc.dow3,a1		 ; three letter day of week
	moveq	#sms.mptr,d0
	trap	#do.smsq
	lea	5(a1),a0		 ; set day of week
	add.w	d1,a0
	add.w	d1,a0
	add.w	d1,a0
	move.l	(sp)+,a1

	clr.b	-(a1)
	move.b	-(a0),-(a1)		 ; ... and copied
	move.b	-(a0),-(a1)
	move.b	-(a0),-(a1)
	move.w	#3,-(a1)		 ; length

	sub.l	a6,a1
	moveq	#0,d0
	movem.l (sp)+,udw.reg
	rts

;+++
; convert year, month, day, hour, minute and second to long word
;
;	d1  r	date
;	a1 c  u pointer to 6 integers
;
;	status return zero
;---
uq_datel
udl.reg reg	d2
	move.l	d2,-(sp)
	moveq	#0,d2
	move.w	(a1)+,d2		 ; year
	move.l	(a1)+,d1
	swap	d1			 ; day and month

	moveq	#0,d0
	jsr	cv_calst
	jsr	cv_ymddn		 ; convert it

	sub.l	#ql.zdate,d1		 ; ql base

	moveq	#24,d0
	bsr.s	udl_atime		 ; add hours
	moveq	#60,d0
	bsr.s	udl_atime		 ; add minutes
	moveq	#60,d0
	bsr.s	udl_atime		 ; add seconds

	move.l	(sp)+,d2
	moveq	#0,d0
	rts

udl_atime
	move.l	d1,d2
	clr.w	d2
	swap	d2			 ; msword date
	mulu	d0,d1			 ; multiply both words
	mulu	d0,d2
	swap	d2			 ; and add them
	add.l	d2,d1
	move.w	(a1)+,d0		 ; ... then add time
	add.l	d0,d1
	rts
	end
