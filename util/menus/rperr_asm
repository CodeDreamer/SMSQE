; Report an error			 1990 Jochen Merz V0.03

	section utility

	include dev8_keys_menu
	include dev8_keys_thg
	include dev8_mac_xref

	xdef	mu_rperr

;+++
; Report and error and return after confirmation.
;
;		Entry				Exit
;	D0.l	error code
;	D1.l	origin
;	D2.b	colourways
;	D4					preserved
;	A1					preserved
;	A4					preserved
;
;	Error returns:	ERR.NI	Thing or THING not implemented
;---
mu_rperr
stk_frm equ	24			; room for parameter table
repreg	reg	d4/a1/a4

	movem.l repreg,-(sp)
	move.l	d0,d4			; keep error code

	movem.l d1-d2,-(sp)
	move.l	#'RPER',d2		; we need a button
	xbsr	ut_usmen		; try to use Menu Thing
	movem.l (sp)+,d1-d2
	bne.s	no_menu 		; failed

	move.l	a1,a4			; address of Thing

	sub.l	#stk_frm,sp
	move.l	sp,a1

	move.l	d4,er_error(a1) 	; the error

	swap	d1
	move.l	d1,er_xpos(a1)		; x-origin

	swap	d1
	move.l	d1,er_ypos(a1)		; y-origin

	and.l	#$ff,d2 		; only low byte
	move.l	d2,er_mainc(a1) 	; colourways

	jsr	thh_code(a4)		; report it

	add.l	#stk_frm,sp

	xbsr	ut_frmen		; and free Menu Extension
no_menu
	movem.l (sp)+,repreg
	rts



	end
