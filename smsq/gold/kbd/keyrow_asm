; GOLD Card Ketrow (called from pointer routines)

	section kbd

	xdef	kbd_keyrow

	include 'dev8_keys_qdos_sms'

;+++
; Internal keyrow call for pointer interface
; Called with d1 = row number
;---
kbd_keyrow
	move.l	a3,-(sp)
	lsl.w	#8,d1
	add.w	#%10,d1
	move.w	d1,-(sp)
	clr.l	-(sp)
	move.w	#$0901,-(sp)
	move.l	sp,a3
	moveq	#sms.hdop,d0
	trap	#do.sms2
	addq.l	#8,sp
	move.l	(sp)+,a3
	rts

	end
