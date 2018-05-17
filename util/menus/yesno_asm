; yes/no selection			 1991 Jochen Merz   V001

	section utility

	include dev8_keys_thg
	include dev8_mac_xref
	include dev8_keys_menu

	xdef	mu_yesno

;+++
; This routine pops up a menu, displays a message and waits for a yes or
; no user selection.
;
;		Entry			Exit
;	D0.l				error, 0 or +1 for ESC
;	D1.l	origin or -1		preserved
;	D2.b	colourways		preserved
;	D3.l	set if default yes	0 no, 1 yes
;	A0	window title		preserved
;	A2	window message		preserved
;---
mu_yesno
stk.frm equ	$44
yno_reg reg	d4/a0-a2/a4-a5
	movem.l yno_reg,-(sp)
	sub.l	#stk.frm,sp
	move.l	sp,a5			; prepare workspace

	movem.l d1-d3,-(sp)
	move.l	#'ITSL',d2
	xbsr	ut_usmen
	movem.l (sp)+,d1-d3
	bne.s	yno_error		; cannot use menu

	move.l	a1,a4			; that's the Thing

	move.l	#(thp.call+thp.str)<<16,d4
	move.l	d4,is_mentp(a5)
	move.l	a0,is_mennm(a5) 	; window title

	move.l	d4,is_prmtp(a5)
	move.l	a2,is_prmpt(a5) 	; request string

	xlea	met_no,a0
	xlea	met_yes,a2
	tst.b	d3
	beq.s	def_no

	exg	a0,a2
def_no
	move.l	d4,is_item1-4(a5)
	move.l	a0,is_item1(a5)        ; first item

	move.l	d4,is_item2-4(a5)
	move.l	a2,is_item2(a5)        ; second item

	clr.l	is_item3(a5)	       ; no third item

	swap	d1
	move.l	d1,is_xpos(a5)	       ; x-origin

	swap	d1
	move.l	d1,is_ypos(a5)	       ; y-origin

	and.l	#$ff,d2 		; only low byte
	move.l	d2,is_mainc(a5)        ; main colourway
	move.l	#(thp.ret+thp.ulng)<<16,is_itnum-4(a5)
	lea	$40(a5),a0
	move.l	a0,is_itnum(a5)

	move.l	a5,a1			; the parameter table
	jsr	thh_code(a4)		; get filename via menu

	move.b	d3,d2			; check default
	move.l	$40(a5),d3		; return parameter

	subq.b	#1,d3			; this gives 0 and 1
	tst.b	d2			; right order?
	beq.s	no_error

	bchg	#0,d3			; toggle state

no_error
	xbsr	ut_frmen		; free menu

yno_error
	add.l	#stk.frm,sp		; adjust stack
	movem.l (sp)+,yno_reg
	tst.l	d0
	rts

	end
