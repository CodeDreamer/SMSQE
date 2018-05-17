; Setup and unset a window definition		1993-94 Jochen Merz V0.01

	include dev8_keys_wman
	include dev8_mac_xref

	section utility

	xdef	ut_setups	; Setup a (scaled) window definition
	xdef	ut_setup	; Setup a fixed size window definition
	xdef	ut_unset	; Unset a window def. and free memory

;+++
; Setup fixed size window definition.
;
;		Entry				Exit
;	d1	required memory size		preserved
;	d2					smashed
;	a0	channel ID			preserved
;	a2	window manager vector		preserved
;	a4					window working definition
;
;	Error returns: out of memory
;	Condition codes set on return
;---
ut_setup
	moveq	#0,d2		; use fixed size
				; and fall into scaled setup
;+++
; Setup scaled window definition.
;
;		Entry				Exit
;	d1	required memory size		preserved
;	d2	x,y size
;	a0	channel ID			preserved
;	a2	window manager vector		preserved
;	a4					window working definition
;
;	Error returns: out of memory
;	Condition codes set on return
;---
ut_setups
setupreg reg	 d1/a0
	movem.l setupreg,-(sp)	; keep required size and channel ID
	xjsr	ut_alchp
	move.l	a0,a4		; it's the working definition
	movem.l (sp)+,setupreg
	bne.s	set_exit	; oops, it does not exist
	move.l	d2,d1
	jsr	wm.setup(a2)	; set up the working definition
set_exit
	rts

;+++
; Unset a window definition and release its space
;
;		Entry				Exit
;	d0.l					preserved
;	a4	window working definition	???
;---
ut_unset
	movem.l d0/a0,-(sp)	; keep channel ID and error
	jsr	wm.unset(a2)	; unset current definition
	move.l	a4,a0
	xjsr	ut_rechp	; release memory
	movem.l (sp)+,d0/a0
	rts

	end
