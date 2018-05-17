; Get various defaults from Menu INFO call	1997 Jochen Merz

	section utility

	include dev8_mac_xref
	include dev8_keys_menu
	include dev8_keys_err
	include dev8_keys_thg
	include dev8_keys_qlv

	xdef	mu_getdef	; get various defaults

;+++
; Get various defaults from Menu INFO call (requires Menu 7.02 or higher)
;
;		Entry			Exit
;	d1.b				main colourway
;	d2.b				sub colourway
;	d3.b				button colourway
;	d4.l				icon explain delay | update frequency
;	d5.b				explanation colourway
;
;	All possible error returns from Menu.
;---
get_reg reg	d7/a0-a2/a4/a6
stk_frm equ	$20

mu_getdef
	movem.l get_reg,-(sp)

	move.l	#'INFO',d2
	xbsr	ut_usmen	; try to use Menu Thing
	bne.s	err_nomenu
	move.l	a1,a4		; address of Thing

	sub.l	#stk_frm,sp
	move.l	sp,a1		; here is our parameter table

	move.l	#inf.cole,d1	; everse orer!
	bsr.s	get_it
	bne.s	err_menuerr	; error from menu call
	move.b	d1,d5

	move.l	#inf.iexp,d1
	bsr.s	get_it
	bne.s	err_menuerr
	move.b	d1,d4
	swap	d4

	move.l	#inf.ffui,d1	; update frequency
	bsr.s	get_it
	bne.s	err_menuerr	; error from menu call
	move.b	d1,d4

	move.l	#inf.colb,d1	; next button colourway
	bsr.s	get_it
	bne.s	err_menuerr
	move.b	d1,d3

	move.l	#inf.cols,d1	; next comes sub-colourway
	bsr.s	get_it
	bne.s	err_menuerr
	move.b	d1,d2

	move.l	#inf.colm,d1	; finally, main colourway
	bsr.s	get_it		; result comes back in d1

err_menuerr
	move.l	d1,-(sp)
	xbsr	ut_frmen	; finally free Menu Extension
	move.l	(sp)+,d1

	add.l	#stk_frm,sp	; adjust stack
err_nomenu
	movem.l (sp)+,get_reg
	tst.l	d0
	rts

get_it
	move.l	#(thp.ret+thp.str)<<16,4(a1) ; return parameter is string
	move.l	d1,(a1) 	; inquiry key
	lea	$c(a1),a2	; point to return string
	move.l	a2,inf_ret(a1)
	jsr	thh_code(a4)	; call routine to get string

	movem.l d2-d3/a0-a3,-(sp) ; conversion routine smashes a lot!
	sub.l	a6,a6		; relative A6
	lea	stk_frm(a1),a1	; return stack
	moveq	#0,d7
	move.w	(a2)+,d7
	add.l	a2,d7		; end of string
	move.l	a2,a0		; start of string
	move.w	cv.deciw,a2
	jsr	(a2)		; convert
	move.w	(a1),d1 	; return value
	movem.l (sp)+,d2-d3/a0-a3
	tst.l	d0
	rts

	end
