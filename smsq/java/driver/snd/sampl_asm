; SOUNDSAMPLE keyword  v. 1.00	 (c) W. Lenerz 2004 - 2012


;*********************************************************
;*
;* c = SOUNDSAMPLE
;* get a soundsample, if 0 - nothing there
;* = 2 words, high word one channel, low word the other
;*
;* v. 1.00	 2004 Nov 12 23:33:09
;*
;* copyright (c) W. Lenerz 2004 - 2012 - see the licence in the documentation
;*
;*  NB these all presume that the sueues lie at $14 and $18 (a3).
;*  This will NOT be the case for Q40/Q60, but there is no legal way to
;*  get at the outqueue that I know of.
;*
;*********************************************************

	section msound


	xdef	soundsample
	xdef	soundsample2
	xdef	soundsample3
	xdef	sf_nosss
;	 xdef	 bp_err

	include 'dev8_keys_qdos_io'
	include 'dev8_keys_qdos_ioa'
	include 'dev8_smsq_q40_hdop_data'
	include 'dev8_keys_68000'
	include 'dev8_keys_err'
	include 'dev8_keys_sss'
	include 'dev8_keys_sbasic'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_qlv'


ssss_qin    equ $14
ssss_qout   equ $18

soundsample  moveq   #0,d2
	move.l	exv_i4,a3		; point to Interrupt level 4
	move.l	-(a3),a2		; this could be SSSS vector
	cmp.l	#sss.flag,-(a3) 	; is it really?
	bne.s	sf_nosss		; no!
	move.l	ssss_qout(a3),a2	: next out
	cmp.l	ssss_qin(a3),a2 	; any sound?
	beq.s	rt_fp			; no
	move.b	(a2),d2 		; get sound
	swap	d2
	move.b	1(a2),d2		; only return 0,0 if no sound
	tst.l	d2
	bne.s	rt_fp
	moveq  #1,d2
; RT_FP: converts a long word (in D2) into a floating point
; & returns it to BASIC
; v 1.00	20.3.1990	 W. Lenerz 1990
;
rt_fp	move.l	d2,d5
	moveq	#6,d1
	move.l	$58(a6),a1
	move.w	$11a,a2
	jsr	(a2)		; get space on maths stack for return value
	move.l	d5,d2
	move.l	$58(a6),a1
	subq.l	#6,a1
	move.l	a1,$58(a6)
rt_fp2	bsr.s	float		; convert long into float
	move.w	d0,(a6,a1.l)
	move.l	d2,2(a6,a1.l)
	moveq	#2,d4
	clr.l	d0
	rts


; float converts long int in d2 into floating point
; uses d2,d0
; v. 2.00 5.6.1990	 w. lenerz 1990

float	moveq	#0,d0
	tst.l	d2		; is it 0?
	beq.s	float2		; if 0, no need to convert
	move.w	#$0820,d0
floatlp1
	subq.w	#1,d0		; conversion long word ½ float
	asl.l	#1,d2
	bvc.s	floatlp1
	roxr.l	#1,d2
float2	RTS

sf_nosss
	moveq	#err.nimp,d0
	rts

bp_err	moveq	#-15,d0
err_out rts

*****************************************************
*
* SOUNDSAMPLE2 div%,end%,channel1%,channel2%
* SOUNDSAMPLE3 div%,end%,TURBO_V(channel1%),TURBO_V(channel2%)
* gets a soundsample, returns it in channel1%,channel2%
* Soundsample2 : returns params through param list, can't be used with Turbo
* soundsample3 : can be used with Turbo
* div% is a calling paramter, divisor, values will be DIVed by that
* end% is a param that wil be used if no sound is being sent
* (use 1, not 0, if no divisor needed
* = 2 words, high word one channel, low word the other
*
* v. 1.01	2012.07.29 merged Sample2 & 3, both are the same
* v. 1.00	2004 Nov 13 10:08:20
*
* copyright (c) W. Lenerz 2004	- see the licence in the documentation
*
*****************************************************

soundsample2
soundsample3
	move.l	a3,d0
	add.l	#32,d0			; 3 params only?
	cmp.l	d0,a5
	bne	bp_err			; no!

	move.l	exv_i4,a4		; point to Interrupt level 4
	subq.l	#8,a4			; shoud point to ssss marker
	cmp.l	#sss.flag,(a4)		; does is it really?
	bne.s	sf_nosss		; no!

trbotest				; as of here, A4 = ssss marker
	moveq	#sms.injb,d0
	moveq	#-1,d1
	moveq	#0,d2
	trap	#do.sms2		; get info on job
	moveq	#0,d7
	cmpi.l	#'Turb',34(a0)		; is this a turbo'd prog?
	beq.s	tboout
	moveq	#1,d7			; D7 = flag for turbo
tboout	move.l	$58(a6),a1
	lea	16(a3),a5
	move.w	$112,a2
	jsr	(a2)			; get divisor & end
	tst.l	 d0
	bne.s	err_out
	subq.w	#2,d3
	bne.s	bp_err
	move.l	(a6,a1.l),d6		; div | end
	addq.l	#4,$58(a6)		; reset maths stack
	move.l	a5,a3
	lea	16(a3),a5
	tst.b	d7			; turbo'd prog?
	bne.s	do_trb1 		; ... yes

	move.w	(a6,a3.l),d0
	andi.b	#$f,d0
	cmp.b	#3,d0			; is param (channel 1) integer?
	bne	bp_err			; no->
	move.w	8(a6,a3.l),d0
	andi.b	#$f,d0
	cmp.b	#3,d0			; same check for channel 2
	bne	bp_err
	moveq	#8,d1
	move.w	$11a,a2
	jsr	(a2)			; get space on maths stack for 8 bytes
	tst.l	d0
	bne	err_out
	bsr.s	getdata 		; get data into d0 & d7
nosnd	move.l	$58(a6),a1		; re-adjust stack
	subq.l	#2,a1
	move.w	d0,(a6,a1.l)
	move.l	a1,$58(a6)
	move.w	$120,a2
	jsr	(a2)			; return first param
	addq.l	#8,a3
	move.l	$58(a6),a1		; re-adjust stack
	subq.l	#2,a1
	move.w	d7,(a6,a1.l)
	move.l	a1,$58(a6)
	move.w	$120,a2
	jsr	(a2)			; return second param
	clr.l	d0
snd_out rts

do_trb1 				; handle turbo'd progs
	move.w	$118,a2
	jsr	(a2)			; get 2 long ints (turbo channels)
	tst.l	d0
	bne.s	snd_out 		; oops
	subq.w	#2,d3			; got 2 ?
	bne	bp_err			; no
	addq.l	#8,$58(a6)		; adjust maths stack
	bsr.s	getdata 		; get sound data into d7/d0
	move.l	(a1,a6.l),a0		; first channel passed as param....
	move.l	(a0),a0 		; ... now
	move.w	d0,(a0) 		; put in sound data
	move.l	(a1,a6.l),a0		; second channel pased as param....
	move.l	(a0),a0 		; ... now
	move.w	d7,(a0) 		; put in sound data
	clr.l	d0			; no error
	rts

; A4 = ssss marker
getdata move.w	d6,d7
	move.w	d6,d0			; preset no sound !
	move.l	ssss_qout(a4),a2	: next out
	cmp.l	ssss_qin(a4),a2 	; any sound?
	beq.s	gotsnd			; no
	move.b	(a2),d0
	move.b	1(a2),d7
gotsnd	swap	d6
	tst.w	d6
	beq.s	get_out
	divu.w	d6,d0
	divu.w	d6,d7
get_out rts


	end
