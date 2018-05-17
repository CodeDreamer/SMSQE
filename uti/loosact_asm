; Loose menu item action utility routines	1993 Jochen Merz

	section utility

	xdef	ut_rdwci	; redraw current item, return OK & no event
	xdef	ut_rdwie	; redraw current item, preserve error & event

	include dev8_keys_wman
	include dev8_keys_wstatus
	include dev8_keys_wwork
;+++
; Set current item to available and redraw it, preserving error and event.
;		Entry				Exit
;	d0					preserved
;	d2-d3					smashed
;	d4					preserved
;	a0	channel ID			preserved
;	a1	window status area		preserved
;	a2	window manager vector		preserved
;	a3	loose menu item 		preserved
;	a4	working defn			preserved
;
;	Condition codes set
;---
ut_rdwie
	movem.l d0/d4,-(sp)		; preserve error & event
	bsr.s	ut_rdwci		; redraw
	movem.l (sp)+,d0/d4		; fetch error & event
	tst.l	d0			; set condition codes
	rts

;+++
; Set current item to available and redraw it.
;
;		Entry				Exit
;	d2-d3					smashed
;	d4					0 (no event)
;	a0	channel ID			preserved
;	a1	window status area		preserved
;	a2	window manager vector		preserved
;	a3	loose menu item 		preserved
;	a4	working defn			preserved
;
;	Condition codes set to ok
;---
ut_rdwci
	move.w	wwl_item(a3),d2 		; get current item number
	move.b	#wsi.mkav,ws_litem(a1,d2.w)	; set it to make available
	moveq	#-1,d3				; redraw selective
	moveq	#0,d4				; no event
	jmp	wm.ldraw(a2)

	end
