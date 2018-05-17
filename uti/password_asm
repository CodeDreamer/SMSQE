; Convert password string into long			2000 Jochen Merz

	section utility

	xdef	ut_password

	include dev8_keys_err

pw.buff equ    16

;---
; Converts standard string password of up to 16 characters into a longword ID.
;
;		Entry			Exit
;	D1				password ID or 0 for no password
;	A0	ptr to password
;
;	Error returns: orng		string too long
;		       +1		"opensesame" ... your last hope
;---
ut_password
pw.reg	reg	d2/a1
	movem.l pw.reg,-(sp)
	moveq	#0,d0		; all is OK so far
	moveq	#0,d1		; assume no password
	move.w	(a0)+,d2	; password length
	beq.s	pass_ret
	cmp.w	#pw.buff,d2	; will it fit?
	bge.s	pass_orng
	cmp.w	#10,d2		; special password?
	bne.s	no_sesame
	cmp.l	#'open',(a0)	; "opensesame"?
	bne.s	no_sesame
	cmp.w	#'se',4(a0)
	bne.s	no_sesame
	cmp.l	#'same',6(a0)
	bne.s	no_sesame
	moveq	#1,d0
	bra.s	pass_ret
no_sesame
	sub.l	#pw.buff,sp
	move.l	sp,a1
	move.l	#$ac65eb37,(a1)+ ; fill some odd values
	move.l	#$56ca73be,(a1)+
	move.l	#$c65ab37e,(a1)+
	move.l	#$6ca53be7,(a1)
	move.l	sp,a1
	move.w	d2,d0
	subq.w	#1,d2		; prepare for DBRA
pass_loop
	move.b	(a0)+,d1
	eor.b	d1,(a1)+
	dbra	d2,pass_loop
	move.l	(sp),d1
	add.l	4(sp),d1
	add.l	8(sp),d1
	add.l	12(sp),d1
	add.w	d0,d1

	add.l	#pw.buff,sp
	moveq	#0,d0
	bra.s	pass_ret
pass_orng
	moveq	#err.orng,d0
pass_ret
	movem.l (sp)+,pw.reg
	tst.l	d0
	rts

	end
