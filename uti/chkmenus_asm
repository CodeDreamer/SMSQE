; Check for Menu's presence and version number   1990 Jochen Merz  V0.01

	section utility

	include dev8_keys_wman
	include dev8_keys_wstatus
	include dev8_keys_qdos_sms
	include dev8_keys_err
	include dev8_keys_thg
	include dev8_mac_xref

	xdef	ut_menvs		; check for MENUS and version

;+++
; Check for menus and return version number.
;
;		Entry			Exit
;	d0	minimum version 	OK or error text
;---
ut_menvs
	movem.l d1-d4/a0-a4,-(sp)
	move.l	d0,d4		; keep version
	xlea	met_nmen,a4	; error message Menu Missing
	moveq	#0,d2		; no extension
	xjsr	ut_usmen	; try to use Menus
	bne.s	mver_err	; failed!
	xjsr	ut_frmen	; free it
	xlea	met_mver,a4	; Menu could be too old
	move.l	#$ff00ffff,d1	; knock out any version letters
	and.l	d1,d3		; actual Menu
	and.l	d1,d4		; desired Menu
	cmp.l	d3,d4		; version OK?
	bgt.s	mver_err	; no
	tst.l	d0
mver_ret
	movem.l (sp)+,d1-d4/a0-a4
	rts

mver_err
	move.l	a4,d0		; return string
	or.l	#$80000000,d0	; with error bit set
	bra.s	mver_ret

	end
