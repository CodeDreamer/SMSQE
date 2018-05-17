; Automatic pulldown window handling	 1993 Jochen Merz     V2.00

	section utility

	include dev8_keys_wman
	include dev8_keys_wstatus
	include dev8_keys_k
	include dev8_keys_qdos_pt
	include dev8_mac_xref

	xdef	ut_pulld		; pulldown window here
	xdef	ut_pullat		; pulldown window at given position

;+++
; This routine pulls down a window, wait until an error or event occurs
; and removes the pulldown window. If the window is not scaleable, d2 should
; be set to 0.
;
;		Entry				   Exit
;	d1.l	memsize required for this layout   ???
;	d2.l	size for layout (if scaleable)	   ???
;	d3.b	colourway			   ???
;	d4.l					   DO or WAKE event returned
;	a0					   ???
;	a1	window status area		   preserved
;	a2	window manager vector		   preserved
;	a3	menu to set up			   ???
;	a4	modify routine after setup (or 0)  ???
;	a5	modify routine after draw (or 0)   ???
;---
ut_pulld
	moveq	#-1,d4		; position at pointer
				; .. and fall into ..
;+++
; This routine pulls down a window, wait until an error or event occurs
; and removes the pulldown window. If the window is not scaleable, d2 should
; be set to 0.
;
;		Entry				   Exit
;	d1.l	memsize required for this layout   ???
;	d2.l	size for layout (if scaleable)	   ???
;	d3.b	colourway			   ???
;	d4.l	origin				   DO or WAKE event returned
;	a0					   ???
;	a1	window status area		   preserved
;	a2	window manager vector		   preserved
;	a3	menu to set up			   ???
;	a4	modify routine after setup (or 0)  ???
;	a5	modify routine after draw (or 0)   ???
;---
ut_pullat
	move.l	a4,-(sp)	; keep routine
	xjsr	ut_setup
	move.l	(sp)+,a3
	xbne	gu_die
	move.l	a3,d0		; routine supplied?
	beq.s	pull_nsr
	move.l	a1,-(sp)
	jsr	(a3)		; call set item sub-routine
	move.l	(sp)+,a1
pull_nsr
	move.b	d3,d2		; colourway given?
	bmi.s	pull_nocol
	xjsr	wu_scmwn	; change colour
pull_nocol
	move.l	d4,d1		; this position
	jsr	wm.pulld(a2)	; position the window
	bne	gu_die
	jsr	wm.wdraw(a2)	; and draw its contents
	bne	gu_die
	move.l	a5,d0		; routine supplied?
	beq.s	pull_ndr
	jsr	(a5)		; call pre-draw routine
pull_ndr

rptr_lp
	jsr	wm.rptr(a2)
	bne.s	rptr_ok 	; error!
	move.l	wsp_evnt(a1),d1 ; get events
	moveq	#0,d4
	btst	#pt..can,d1	; cancelled?
	bne.s	rptr_evnt	; yes, return event
	moveq	#pt..wake,d4
	btst	d4,d1		; woken?
	bne.s	rptr_evnt	; yes, return event
	moveq	#pt..do,d4
	btst	d4,d1		; DOne?
	beq.s	rptr_lp 	; no, loop again
rptr_evnt
rptr_ok
	xjsr	ut_unset	; unset window
	xjsr	gu_fclos	; and close it
	tst.l	d0
	rts

	end
