; Standard menu action routines 	 1991 Jochen Merz

	section utility

	xdef	mea_slep	; Standard Sleep action routine
	xdef	mea_move	; Standard Move action routine
	xdef	mea_quit	; Standard Quit action routine
	xdef	mea_wake	; Standard Wake action routine
	xdef	mea_help	; Standard Help action routine
	xdef	mea_size	; Standard Resize action routine
	xdef	mea_do		; Standard Do action routine

	include dev8_keys_qdos_pt
	include dev8_keys_wstatus
	include dev8_keys_wwork
	include dev8_keys_k

;+++
; Standard menu action routine for OK or any other item which should result in
; a DO event. The item is made available and the event returned.
;
;		Entry				Exit
;	d2					smashed
;	a1	window status area		preserved
;	a3	pointer to loose menu item	preserved
;---
mea_do
	moveq	#pt..do,d4			; DO event
	bra.s	mea_alle

;+++
; Standard menu help action routine.
; Makes help item available and returns help event.
;
;		Entry				Exit
;	d2					smashed
;	a1	window status area		preserved
;	a3	pointer to loose menu item	preserved
;---
mea_help
	moveq	#pt..help,d4
	bra.s	mea_alle

;+++
; Standard menu sleep action routine.
; Makes sleep item available and returns sleep event.
;
;		Entry				Exit
;	d2					smashed
;	a1	window status area		preserved
;	a3	pointer to loose menu item	preserved
;---
mea_slep
	moveq	#pt..zzzz,d4			; sleep event
mea_alle
	move.w	wwl_item(a3),d2 		; get item number
	move.b	#wsi.mkav,ws_litem(a1,d2.w)	; set it to make available
	moveq	#0,d0				; no error
	rts

;+++
; Standard menu move action routine.
; Makes item available and returns move event.
;
;		Entry				Exit
;	d2					smashed
;	a1	window status area		preserved
;	a3	pointer to loose menu item	preserved
;---
mea_move
	moveq	#pt..wmov,d4
	bra.s	mea_alle

;+++
; Standard menu cancel action routine.
; Makes cancel item available and returns cancel event.
;
;		Entry				Exit
;	d2					smashed
;	a1	window status area		preserved
;	a3	pointer to loose menu item	preserved
;---
mea_quit
	moveq	#pt..can,d4			; cancel event
	bra.s	mea_alle

;+++
; Standard menu resize routine, which handles DO.
; Makes resize item available and returns event.
;
;		Entry				Exit
;	d2					smashed
;	a1	window status area		preserved
;	a3	pointer to loose menu item	preserved
;---
mea_size
	cmp.w	#k.do,d2			; was it a DO?
	bne.s	mea_sizn
	bset	#pt..do,wsp_weve(a1)		; yes, return this event
mea_sizn
	bset	#pt..wsiz,wsp_weve(a1)
	moveq	#pt..wsiz,d4			; return resize anyway
	bra	mea_alle

;+++
; Standard menu wake action routine.
; Makes sleep item available and returns wake event.
;
;		Entry				Exit
;	d2					smashed
;	a1	window status area		preserved
;	a3	pointer to loose menu item	preserved
;---
mea_wake
	moveq	#pt..wake,d4			; wake event
	bra.s	mea_alle

	end
