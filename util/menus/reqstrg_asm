; request a string			 1991 Jochen Merz   V0.02

	section utility

	include dev8_keys_menu
	include dev8_keys_thg

	xdef	mu_reqst
	xref	ut_usmen,ut_frmen
;+++
; This routine requests a single string.
;
;		Entry			Exit
;	D0.l				error, 0 or +1 for ESC
;	D1.l	origin or -1		preserved
;	D2.b	colourways		preserved
;	D3.l	window width (chars)	preserved
;	A0	window title		preserved
;	A2	window message		preserved
;	A3	string buffer		preserved
;---
mu_reqst
stk.frm equ	$30
req_reg reg	d4/a0-a5
	movem.l req_reg,-(sp)
	sub.l	#stk.frm,sp
	move.l	sp,a5			; prepare workspace

	movem.l d1-d3,-(sp)
	move.l	#'RSTR',d2		; we need READ STRING
	bsr	ut_usmen		; try to use Menu Thing
	movem.l (sp)+,d1-d3
	bne.s	req_err 		; failed, do simple request

	move.l	a1,a4			; address of Thing

	move.l	#(thp.call+thp.str)<<16,d4
	move.l	d4,rs_mennm-4(a5)
	move.l	a0,rs_mennm(a5) 	 ; window title

	move.l	d4,rs_defnm-4(a5)
	move.l	a3,rs_defnm(a5) 	 ; string buffer

	move.l	d4,rs_prmpt-4(a5)
	move.l	a2,rs_prmpt(a5) 	; prompt

	move.l	d3,rs_chars(a5) 	; width

	swap	d1
	move.l	d1,rs_xpos(a5)		; x-origin

	swap	d1
	move.l	d1,rs_ypos(a5)		; y-origin

	and.l	#$ff,d2 		; only low byte
	move.l	d2,rs_mainc(a5) 	; main colourway

	move.l	#(thp.ret+thp.str)<<16,rs_fname-4(a5)
	move.l	a3,rs_fname(a5) 	; return file name

	move.l	a5,a1			; the parameter table
	jsr	thh_code(a4)		; get filename via menu

	bsr	ut_frmen		; and free Menu Extension
req_err
	add.l	#stk.frm,sp		; adjust stack
	movem.l (sp)+,req_reg
	tst.l	d0
	rts

	end
