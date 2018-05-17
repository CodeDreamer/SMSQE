; Convert date to ASCII into buffer	1999 Jochen Merz
; Year-2000 safe!

	section utility

	xdef	ut_timdat	; date and time
	xdef	ut_date 	; date only
	xdef	ut_datex	; date with more parameters

dat.reg reg	d1-d5/a2
;+++
; Convert date to ASCII date into a buffer.
;
;		Entry			Exit
;	d1.l	date			preserved
;	d6.b	separator (e.g. ./-)	preserved
;	d7.b	format: 		preserved
;		 0 dd.mm.yy
;		 1 dd.mm.yyyy
;		 2 mm.dd.yy
;		 3 mm.dd.yyyy
;	a1	pointer to buffer	updated pointer to buffer
;---
ut_datex
	movem.l dat.reg,-(sp)
	bsr	dat_parts	; get parts of date
	btst	#1,d7		; month and day swapped?
	beq.s	daymonth_ok	; no
	exg	d1,d4
daymonth_ok
	move.w	d1,d0
	bsr.s	dat_puti
	move.b	d6,(a1)+
	move.w	d4,d0
	bsr.s	dat_puti
	move.b	d6,(a1)+
	moveq	#0,d0
	move.w	d2,d0
	btst	#0,d7		; full year?
	beq.s	year_twodig
	divu	#100,d0
	bsr.s	dat_puti
	move.w	d2,d0
year_fourlp
	cmp.w	#99,d0
	bls.s	year_fourok
	sub.w	#100,d0
	bra.s	year_fourlp
year_fourok
	bsr.s	dat_puti
	bra.s	dat_ret
year_twodig
	divu	#100,d0
	swap	d0
	bsr.s	dat_puti
	bra.s	dat_ret

;+++
; Convert date to ASCII date into a buffer. The format is:
; dd.mm.yy
;
;		Entry			Exit
;	d1.l	date			preserved
;	a1	pointer to buffer	updated pointer to buffer
;---
ut_date
	movem.l dat.reg,-(sp)
	bsr.s	dat_parts	; get parts of date
	bra.s	dat_all

;+++
; Convert date to full date and time ASCII into a buffer. The format is:
; hh:mm dd.mm.yy
;
;		Entry			Exit
;	d1.l	date			preserved
;	a1	pointer to buffer	updated pointer to buffer
;---
ut_timdat
	movem.l dat.reg,-(sp)
	bsr.s	dat_parts	; get parts of date
	move.l	d2,d0
	swap	d0		; this is the hour
	bsr.s	dat_puti
	move.b	#':',(a1)+
	move.w	d3,d0		; minutes
	bsr.s	dat_puti
;	 move.b  #':',(a1)+
;	 move.l  d3,d0
;	 swap	 d0		 ; seconds
;	 bsr.s	 dat_puti
	move.b	#' ',(a1)+	; separate date
dat_all
	bsr.s	dat_putdt	; and put in date
dat_ret
	movem.l (sp)+,dat.reg
	moveq	#0,d0
	rts

dat_puti
	and.l	#$0000ffff,d0
	divu	#10,d0
	add.l	#$00300030,d0
	move.b	d0,(a1)+
	swap	d0
	move.b	d0,(a1)+
	rts

dat_putdt
	move.w	d1,d0
	bsr.s	dat_puti
	move.b	#'.',(a1)+
	move.w	d4,d0
	bsr.s	dat_puti
	move.b	#'.',(a1)+
	moveq	#0,d0
	move.w	d2,d0
	divu	#100,d0
	swap	d0
	bra.s	dat_puti

dat_divl
	moveq	#0,d0
	swap	d1
	move.w	d1,d0
	divu	d2,d0
	swap	d0
	move.w	d0,d1
	swap	d1
	divu	d2,d1
	move.w	d1,d0
	swap	d1
	exg	d0,d1
	rts

dat_parts
	move.w	#60,d2		; sixty seconds ...
	bsr.s	dat_divl
	move.w	d0,d3		; number of seconds
	swap	d3
	bsr.s	dat_divl
	move.w	d0,d3		; number of minutes
	divu	#24,d1
	move.l	d1,d2		; number of hours
	and.l	#$ffff,d1
	move.l	d1,d0
	divu	#7,d0
	swap	d0
	divu	#1461,d1
	move.w	d1,d2
	asl.w	#2,d2
	add.w	#1961,d2	; base year
	clr.w	d1
	swap	d1
	divu	#365,d1
	moveq	#0,d4
	cmp.w	#4,d1		; leap year?
	bne.s	datp_noleap
	subq.w	#1,d1
	move.w	#365,d4
datp_noleap
	add.w	d1,d2
	swap	d1
	add.w	d4,d1
	moveq	#0,d5
	move.w	d2,d4
	and.w	#3,d4
	bne.s	datp_leap
	moveq	#1,d5
datp_leap
	move.w	d5,d4
	add.w	#58,d4
	cmp.w	d4,d1
	ble.s	datp_feb
	addq.w	#2,d1
	sub.w	d5,d1
datp_feb
	move.w	d1,d5
	add.w	#92,d5
	mulu	#100,d5
	divu	#3055,d5
	move.w	d5,d4
	add.w	#92,d1
	mulu	#3055,d5
	divu	#100,d5
	sub.w	d5,d1
	subq.w	#2,d4
	rts

	end
