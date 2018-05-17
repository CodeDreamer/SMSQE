; Mouse control    1988	 Tony Tebby

;	MOUSE_SPEED  #ch, acceleration, wake_up
;	MOUSE_STUFF  #ch, hot$
;
; version 1.00	- wl
; replaced reference to ee_qptr_keys by keys_qlv and thus changed
; ca.gtlin to sb.gtlin and ca.gtstr to sb.gtstr

	section proc

	xdef	mouse_speed
	xdef	mouse_stuff

	include 'dev8_keys_err'
	include 'dev8_keys_qdos_io'
	include 'dev8_mac_assert'
	include 'dev8_keys_con'
	include 'dev8_keys_qlv'

	xref	ut_chan1

ptm_chk9
	cmp.w	#9,d1			; is d1 in 0..9 ?
	bhi.s	ptm_exor		; no
	moveq	#0,d0
	rts
ptm_exor
	moveq	#err.orng,d0
	rts
*
*	Set the mouse speed: the acceleration may be in the range 0-9,
*	as may the wake-up speed.
*
mouse_speed
	jsr	ut_chan1		; get us a channel ID
	bne.s	ptm_exit		; whoops
	move.l	a0,a4
	move.w	sb.gtlin,a2
	jsr	(a2)			; get some long integers
	bne.s	ptm_exit		; whoops again
	subq.w	#1,d3			; just one?
	bmi.s	ptm_exbp		; no, none: wally
	beq.s	ptm_sacc		; yes, set acceleration
*
	addq.l	#7,a1			; wake-up speed is just a byte
	moveq	#0,d1
	move.b	0(a6,a1.l),d1		; get it
	bsr.s	ptm_chk9		; check it's in range
	bne.s	ptm_exit		; it isn't
	moveq	#1,d2			; one byte
	moveq	#pt_wake,d1		; set wake-up speed
	bsr.s	ptm_slnk		; do it
	bne.s	ptm_exit		; eek
	subq.l	#7,a1			; skip back to acceleration
*
ptm_sacc
	move.w	2(a6,a1.l),d1		; get required acceleration
	bsr.s	ptm_chk9		; be sure it's in range
	bne.s	ptm_exit		; wrong
	moveq	#10,d2			; 0..9 becomes
	sub.w	d1,d2			; 10..1
	move.w	d2,2(a6,a1.l)		; in the case of Y
	move.w	d2,0(a6,a1.l)		; in the case of X
	assert	pt_xaccl,pt_yaccl-2
	moveq	#pt_xaccl,d1		; we want to change the x and y accels
ms.accl equ	4
	moveq	#ms.accl,d2		; that's four bytes
	bsr.s	ptm_slnk		; set that

ptm_exit
	rts
ptm_exbp
	moveq	#err.ipar,d0
	bra.s	ptm_exit
*
ptm_exni
	moveq	#err.nimp,d0
	bra.s	ptm_exit
*
ptm_slnk
	move.l	a1,-(sp)
	move.l	a4,a0			; get channel ID
	moveq	#-1,d3			; infinite timeout
	moveq	#iop.slnk,d0		; set bytes in linkage block
	trap	#4			; this is SuperBASIC
	trap	#3
	move.l	(sp)+,a1
	tst.l	d0
	rts
*
*	Set key sequence resulting from pressing both mouse buttons.
*	This is zero (to unset it), one or two characters long, 
*	but may be set to ALT/something to get longer sequences if needed.
*
mouse_stuff
	jsr	ut_chan1		; get a channel
	bne	ptm_exit		; whoops
	move.l	a0,a4
*
	move.w	sb.gtstr,a2		; get string to set
	jsr	(a2)
	bne	ptm_exit		; oops
	subq.w	#1,d3			; just one
	bne	ptm_exbp
*
	move.w	0(a6,a1.l),d2		; number of bytes to set
	beq.s	ptm_clrc		; none, clear characters
	cmp.w	#2,d2			; more than two?
	bgt	ptm_exbp		; yes, not reasonable
	beq.s	ptm_skln		; exactly two, just skip length
	clr.b	3(a6,a1.l)		; one, clear second
ptm_skln
	addq.l	#2,a1			; skip length
ptm_clrc
	moveq	#2,d2			; always set both characters
	move.w	#pt_stuf1,d1		; first to set is here
	bsr	ptm_slnk
	bra	ptm_exit

	end
