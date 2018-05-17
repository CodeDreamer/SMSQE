; File error handler			 1991 Jochen Merz   V001

	section utility

	include dev8_keys_menu
	include dev8_keys_thg
	include dev8_mac_xref

	xdef	mu_filer

;+++
; Handles all kinds of file errors. The user may just confirm the error, or
; optionally retry the operation.
;
;		Entry			Exit
;	D0.l	file error		undefined
;	D1.l	origin or -1		preserved
;	D2.b	colourways		preserved
;	D3.l	if set, allow ed/ov	0=ESC or Abort 1=retry
;					2=overwrite 3=edit
;---
mu_filer
stk.frm equ	$20
fer_reg reg	d4/a0-a5
	movem.l fer_reg,-(sp)
	sub.l	#stk.frm,sp

	move.l	sp,a5			; prepare workspace
	move.l	sp,a1			; setup parameter table

	move.l	d0,fe_error(a1) 	; error code

	move.l	d3,fe_optns(a1) 	; options

	swap	d1
	move.l	d1,fe_xpos(a1)		; x-origin

	swap	d1
	move.l	d1,fe_ypos(a1)		; y-origin

	and.l	#$ff,d2 		; only low byte
	move.l	d2,fe_mainc(a1) 	; colourways

	move.l	#(thp.ret+thp.slng)<<16,fe_actn-4(a1)  ; return parameter type
	lea	$1c(a1),a0		; pointer to return parameter
	move.l	a0,fe_actn(a1)

	move.l	#'FERR',d2
	xbsr	ut_usmen
	bne.s	mn_error		; cannot use menu
	exg	a5,a1			; exchange Thing and parameter table

	jsr	thh_code(a5)		; call Thing

	move.l	$1c(a1),d3		; return parameter

no_error
	xbsr	ut_frmen		; free menu
mn_error
	add.l	#stk.frm,sp		; adjust stack
	movem.l (sp)+,fer_reg
	tst.l	d0
	rts

	end
