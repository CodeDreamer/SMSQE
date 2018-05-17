; Read all extensions into table	1994 Jochen Merz

	section utility

	include dev8_mac_xref
	include dev8_keys_thg

	xdef	mu_getexts	; get pre-defined extensions

;+++
; Reads all eight pre-defined extensions into a table. The table has to be
; 64 bytes long, every entry containing a word length and up to 6 chars.
;
;		Entry				Exit
;	a5	ptr to table			preserved
;
;	All possible error returns from Menu.
;---
mu_getexts
stk_frm equ	16
get_reg reg	d4/a1/a4-a5

	movem.l get_reg,-(sp)
	move.l	#'INFO',d2
	xbsr	ut_usmen	; try to use Menu Thing
	bne.s	no_menu
	move.l	a1,a4		; address of Thing

	sub.l	#stk_frm,sp
	move.l	sp,a1		; here is our parameter table

	move.l	#(thp.ret+thp.str)<<16,4(a1) ; return parameter is string

	moveq	#0,d4		; start with first extension

get_loop
	move.l	d4,(a1) 	; inquiry key
	move.l	a5,8(a1)	; result to here
	jsr	thh_code(a4)	; call routine to get string
	bne.s	get_error

	addq.l	#8,a5		; point to next result location
	addq.b	#1,d4
	cmp.b	#8,d4		; all done?
	bne.s	get_loop

get_error
	xbsr	ut_frmen	; finally free Menu Extension

	add.l	#stk_frm,sp
no_menu
	movem.l (sp)+,get_reg
	tst.l	d0
	rts

	end
