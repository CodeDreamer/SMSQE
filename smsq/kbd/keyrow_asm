; SMSQ KEYROW emulation    1988  Tony Tebby

	section kbd

	xdef	kbd_keyrow
	xdef	hdop_keyr

	include 'dev8_keys_sys'
	include 'dev8_keys_err'
	include 'dev8_smsq_kbd_keys'

;+++
; Internal keyrow call for pointer interface
; Called with d1 = row number
;---
kbd_keyrow
	move.l	a3,-(sp)
	bra.s	kbkr_do
;+++
; Internal keyrow call for SMS.HDOP routine
; Called with A3 pointing to number of parameters (byte)
; and a3 already on stack
;---
hdop_keyr
	cmp.b	#1,(a3)+		 ; just one parameter byte?
	bne.s	kbkr_bp 		 ; ... no
	moveq	#%11,d0 		 ; first byte description only
	and.l	(a3)+,d0
	bne.s	kbkr_bp 		 ; not a nibble
	moveq	#0,d1
	move.b	(a3)+,d1		 ; row number

kbkr_do
	move.l	sys_klnk(a6),a3 	 ; get kbd linkage
	moveq	#0,d0
	cmp.b	#1,d1			 ; is it cursor key?
	bne.s	kbkr_set
	move.b	kb_joyst(a3),d0 	 ; ... yes, set joystick status
kbkr_set
	or.b	kb_krwem(a3,d1.w),d0	 ; get keyrow
	move.l	d0,d1
	bra.s	kbkr_ok

kbkr_bp
	moveq	#err.ipar,d0
	bra.s	kbkr_exit
kbkr_ok
	moveq	#0,d0
kbkr_exit
	move.l	(sp)+,a3
	rts
	end
