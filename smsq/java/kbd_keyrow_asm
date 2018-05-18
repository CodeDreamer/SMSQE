; SMSQmulator keyrow emulation	V1.01  (c) W. Lenerz 2012-2015
						 
; 1.01 calling with d1>7 no longer calls keyrow 0

; based on
; SMSQ KEYROW emulation    1988  Tony Tebby

	section kbd

	xdef	kbd_keyrow
	xdef	hdop_keyr

	include 'dev8_keys_sys'
	include 'dev8_keys_err'
	include 'dev8_keys_java'
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
	addq.l	#1,a3
	cmp.b	#1,(a3)+		 ; just one parameter byte?
	bne.s	kbkr_bp 		 ; ... no
	moveq	#%11,d0 		 ; first byte description only
	and.l	(a3)+,d0

       ; bne.s	 kbkr_bp		  ; not a nibble
	moveq	#0,d1
	move.b	(a3)+,d1		 ; row number
	
kbkr_do
	cmp.b	#7,d1
	bgt.s	no_read 		; I can't read more than that!
	move.l	sys_klnk(a6),d0
	beq.s	kbkr_bp 		; ooops
	move.l	d0,a3
	move.b	kb_krwem(a3,d1.w),d1	; get keyrow state
kbkr_ok
	moveq	#0,d0
kbkr_exit
	move.l	(sp)+,a3
	rts


kbkr_bp
	moveq	#err.ipar,d0
	bra.s	kbkr_exit
no_read
	moveq	#0,d1
	bra.s	kbkr_ok
	end
