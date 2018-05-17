; Convert date and time to real time	   V0.00   1993 Tony Tebby

	section cv

	xdef	cv_dtmrt

	xref	cv_mnths

;+++
; Convert date and time to real time
;
;	d1  r	real time
;	a1 c  p pointer to date and time
;
;	status return 0
;---
cv_dtmrt
	move.w	(a1)+,d1
	sub.w	#1961,d1		 ; year since 1961
	moveq	#0,d0
	move.w	d1,d0			 ; keep it
	mulu	#365,d1 		 ; day

	ror.l	#2,d0			 ; leap year counter and year in cycle
	add.w	d0,d1			 ; + corrector for leap year
	clr.w	d0
	move.b	(a1)+,d0		 ; month
	add.w	d0,d0
	add.w	cv_mnths-2(pc,d0.w),d1
	cmp.l	#$c0000004,d0		 ; January / February in third year?
	bhi.s	drt_nleep		 ; ... no
	subq.w	#1,d1			 ; yes, we've gone too far
drt_nleep

	moveq	#0,d0
	move.b	(a1)+,d0
	add.w	d0,d1			 ; + day
	move.w	d1,-(sp)
	addq.l	#1,a1			 ; skip day of week

; the day is done, now the time

	move.b	(a1)+,d0
	moveq	#60,d1
	mulu	d0,d1			 ; hours * 60
	move.b	(a1)+,d0
	add.w	d0,d1			 ; hours * 60 + minutes
	mulu	#60,d1			 ; hours * 3600 + minutes * 60
	move.b	(a1)+,d0
	add.l	d0,d1			 ; ........................ + seconds

	move.w	(sp)+,d0		 ; recover day number
	mulu	#24*60*60/2,d0		 ; day in 2 second units
	add.l	d0,d0			 ; day in seconds

	add.l	d0,d1

	subq.l	#8,a1			 ; restore a1
	moveq	#0,d0
drt_rts
	rts
	end
