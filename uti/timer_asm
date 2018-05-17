*	uti_timer_asm

	include dev8_keys_qdos_sms
	include dev8_keys_err
	include dev8_keys_atari
	include dev8_mac_xref

	section utility

	xdef	ut_etime
	xdef	ut_dtime


;+++
;	timer b
;
;		Entry			Exit
;	D0.l				error code
;	D1.b	timer mode		preserved
;	D2.b	timer b data		preserved
;	A0.l	address of int. routine preserved
;
;	Error returns: err.ipar   address=0 / mode =0  / data =0
;---
timereg reg	d3/d7/a0-a1
ut_etime
	movem.l timereg,-(sp)
	movem.l d1-d2,-(sp)
	lea	time_thg,a1
	xjsr	ut_thuse
	movem.l (sp)+,d1-d2

	tst.l	d0
	bne.s	timer_rts		; error from timer thing

	tst.b	d1
	beq.s	timer_err		; mode 0

	tst.b	d2
	beq.s	timer_err		; data 0

	cmp.l	#0,a0
	beq.s	timer_err		; address 0

;	 lea	 timer,a1
	move.w	sr,d7
	trap	#0
	move.l	a0,$1a0 		; interrupt routine
	move.b	#0,mfp_ctlb		; timer B control (stop timer)
	move.b	d2,mfp_datb		; timer B data
	move.b	d1,mfp_ctlb		; timer B control (insert mode)
	moveq	#0,d0			; no error
	bset	#mfp..tbi,mfp_imra	; set timer b interrupt mask
	bset	#mfp..tbi,mfp_tbe	; interrupt enable timer b
	move.w	d7,sr
	bra.s	timer_rts

timer_err
	moveq	#err.ipar,d0

timer_rts
	movem.l (sp)+,timereg
	tst.l	d0			; error ?
	rts
;+++
;	timer b disable
;---
ut_dtime
	move.l	a1,-(sp)
	bclr	#mfp..tbi,mfp_imra	; clear timer b interrupt mask
	bclr	#mfp..tbi,mfp_tbe	; clear interrupt enable timer b
	bclr	#mfp..tbi,mfp_tbpi	; clear mfp pending interrupt b
	bclr	#mfp..tbi,mfp_isra	; clear in service bit
	lea	time_thg,a1		; thing name
	xjsr	ut_thfre		; free it
	move.l	(sp)+,a1
	rts

;+++
;	an example of a timer b interrupt routine
;---

timer
	move.w	d1,-(sp)
	move.b	mfp_ctlb,d1		; timer B control (read mode)
	move.b	#0,mfp_ctlb		; timer B control (stop timer)

	addq.l	#1,$2817c		; sys_top -4

	move.b	d1,mfp_ctlb		; timer B control (insert mode)

	bclr	#mfp..tbi,mfp_tbpi	; clear mfp pending interrupt b
	bclr	#mfp..tbi,mfp_isra	; clear in service bit
	move.w	(sp)+,d1
	rte

time_thg dc.w	5,'Timer '


	end
