; Re-size window (long pointer) 		V1.00	 1988	Tony Tebby

	section wut

	xdef	ut_rsize

	xref	wu_gwrpp,ut_wm_setup

	include 'dev8_keys_wman'
	include 'dev8_keys_wwork'
	include 'dev8_mac_assert'
;+++
; Re-sizing a window isn't a managed operation, so this routine is provided
; to do one version of the resize operation.  This also finds the new absolute
; pointer position is that required to keep the bottom left hand corner of the
; window in the same position.	WM.SETUP is called to reset the working
; definition.
;
;	Registers:
;		Entry				Exit
;	D0					error code
;	D1	max window size 		new size
;	D2					new absolute pointer position
;	A2	window manager			preserved
;	A4	current working defn.		preserved
;---
ut_rsize
wrs.reg reg	d4/d5/d6/d7/a0/a1/a3
	movem.l wrs.reg,-(sp)
	move.l	d0,d7			; keep increments
	bne.s	wrs_smax
	move.l	#$00020001,d7		; ... none, use default increments
wrs_smax
	move.l	d1,d5			; keep max size
	move.l	ww_wstat(a4),a1 	; and get window's status area
	jsr	wu_gwrpp		; get window relative pointer position
	move.l	d1,d6

	jsr	wm.chwin(a2)		; find size change
	bne.s	wrs_exit

	move.l	ww_xsize(a4),d4 	; current window size
	bsr.s	wrs_disc		; try x
	bsr.s	wrs_disc		; and y

	move.l	ww_xorg(a4),d5		; keep origin

	assert	ww_wstat,ww_wdef-4,ww_chid-8,0
	move.l	a4,a0
	move.l	(a0)+,a1
	move.l	(a0)+,a3
	move.l	(a0),a0
	jsr	ut_wm_setup		; reset definition
	bne.s	wrs_exit

	swap	d6
	cmp.w	ww_xsize(a4),d6 	; is X ptr in window
	blo.s	wrs_chky		; yes
	move.w	ww_xsize(a4),d6
	subq.w	#2,d6
wrs_chky
	swap	d6
	cmp.w	ww_ysize(a4),d6 	; is Y in window?
	blo.s	wrs_sorg		; yes
	move.w	ww_ysize(a4),d6
	subq.w	#1,d6

wrs_sorg
	move.l	d6,ww_xorg(a4)		; set pointer origin same as old posn
	move.l	d5,d2			; old window origin
	add.l	d4,d2			; old bottom = new bottom
	sub.l	d1,d2			; new top
	add.l	d6,d2			; new absolute pointer position
wrs_exit
	tst.l	d0
	movem.l (sp)+,wrs.reg
	rts

wrs_disc
	swap	d1			 ; do the other half
	swap	d4
	swap	d5
	swap	d7

	move.w	d7,d0
	lsr.w	#1,d0			 ; half of increment in d0
	sub.w	d1,d0			 ; plus (negative) change
	bge.s	wrs_disd		 ; positive - OK
	sub.w	d7,d0			 ; negative, fix 68000 divs bug
wrs_disd
	ext.l	d0
	divs	d7,d0			 ; divided
	muls	d7,d0			 ; to round
	move.w	d0,d1

	add.w	d4,d1			 ; new window size
	bgt.s	wrs_cmax		 ; ... large enough
wrs_cmin
	add.w	d7,d1			 ; ... too small, bump it up
	ble.s	wrs_cmin
wrs_cmax
	cmp.w	d5,d1			 ; too big now?
	ble.s	wrs_rts 		 ; ... no
	sub.w	d7,d1			 ; ... yes, squeeze it now
	bra.s	wrs_cmax
wrs_rts
	rts

	end
