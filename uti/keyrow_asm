; Do a KEYROW request

	include dev8_keys_qdos_sms
	include dev8_keys_sys

	section utility

	xdef	ut_keyrow	; standard keyrow
	xdef	ut_key_acs	; just check ALT CTLR SHIFT

;---
;		Entry			Exit
;	d0.b				bits 2,1,0 for A, C, S
;---
ut_key_acs
	move.l	d1,-(sp)
	moveq	#7,d1		; row 7
	bsr.s	ut_keyrow
	moveq	#%111,d0
	and.b	d1,d0		; result into d0
	move.l	(sp)+,d1
	tst.b	d0		; check if anything pressed
	rts

;+++
;		Entry			Exit
;	d1.b	row			result
;---
ut_keyrow
	movem.l d5/d7/a3,-(sp)
	sub.w	#8,sp
	move.l	sp,a3
	move.w	#$0901,(a3)
	clr.l	2(a3)
	move.w	#$0102,6(a3)
	move.b	d1,6(a3)
	moveq	#sms.hdop,d0
	trap	#do.smsq
	add.w	#8,sp
	movem.l (sp)+,d5/d7/a3
	tst.l	d0
	rts

	end
