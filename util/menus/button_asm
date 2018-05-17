; Button routine			 1990 Jochen Merz  V0.02

	section utility

	include dev8_mac_xref
	include dev8_keys_thg
	include dev8_keys_menu

	xdef	mu_buttn

;+++
; Make a Button for a job and wait.
;
;		Entry				Exit
;	D1.l	origin or -1 (button frame)
;	D2.b	colourways
;	A3	pointer to button name
;
;	Error returns:	ERR.NI	Thing or THING not implemented
;---

stk_frm equ	$18			; room for parameter table
btnreg	reg	a1/a4

mu_buttn
	movem.l btnreg,-(sp)

	movem.l d1-d3,-(sp)
	move.l	#'BUTN',d2		; we need a button
	xbsr	ut_usmen		; try to use Menu Thing
	movem.l (sp)+,d1-d3
	bne.s	no_buttn		; failed

	move.l	a1,a4			; address of Thing

	sub.l	#stk_frm,sp
	move.l	sp,a1			; prepare workspace

	move.l	#(thp.call+thp.str)<<16,bt_type(a1)
	move.l	a3,bt_name(a1)		; button name
	swap	d1
	move.l	d1,bt_xpos(a1)
	swap	d1
	move.l	d1,bt_ypos(a1)

	and.l	#$ff,d2 		; only low byte
	move.l	d2,bt_mainc(a1)

	jsr	thh_code(a4)		; make it a button

	add.l	#stk_frm,sp

	xbsr	ut_frmen		; and free Menu Extension
no_buttn
	movem.l (sp)+,btnreg
	rts


	end
