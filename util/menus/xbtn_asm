; Extended Button routine

	section utility

	include dev8_mac_xref
	include dev8_keys_thg
	include dev8_keys_menu

	xdef	mu_xbtn

;+++
; Make a Button for a job and wait.
;
;		Entry				Exit
;	D1.l	origin or -1 (button frame)	exit code
;	D2.b	colourways
;	D3.b	set if "edited" sprite is required
;	D4.b	set if ESC is valid keystroke
;	A3	pointer to button name
;
;	Error returns:	ERR.NI	Thing or THING not implemented
;---
stk_frm equ	$30			; room for parameter table
btnreg	reg	a1/a3-a4

mu_xbtn
	movem.l btnreg,-(sp)

	movem.l d1-d3,-(sp)
	move.l	#'XBTN',d2		; we need a extended button
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

;;;	   and.l   #$ff,d2		   ; only low byte
;;;	   move.l  d2,bt_mainc(a1)

	clr.l	bt_spr2(a1)		; default no edit sprite
	move.l	#$10000,d0		; and return with HIT

	tst.b	d3			; edit sprite
	beq.s	no_edit 		; no

	move.l	#2,bt_spr2(a1)		; get it
	bset	#bto..flsh,d0		; set flashing

no_edit
	tst.b	d4			; return on ESC
	beq.s	no_esc			; no

	bset	#bto..resc,d0		; set ESC

no_esc
	move.l	d0,bt_opts(a1)		; insert options

	move.l	#(thp.ret+thp.ulng)<<16,bt_retp-4(a1)
	lea	$2c(a1),a3
	move.l	a3,bt_retp(a1)		; return parameter

	jsr	thh_code(a4)		; make it a button

	move.l	$2c(a1),d3		; get selection keystroke

	add.l	#stk_frm,sp

	xbsr	ut_frmen		; and free Menu Extension
	move.l	d3,d1			; do it in the right position

no_buttn
	movem.l (sp)+,btnreg
	tst.l	d0
	rts


	end
