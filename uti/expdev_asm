; Expand string if substitution device is at start of string
; 1992-93 Jochen Merz			V1.01

	section utility

	include dev8_keys_thg
	include dev8_keys_qdos_sms
	include dev8_mac_xref

	xdef	fu_expdev

;+++
; This routine checks the string if DEVx_ is being present at the
; start of the string and replaces it by the current setting of the
; device. The rest of the filename or subdirectory is copied behind,
; and if the string becomes too long, an empty string is returned.
; If no DEV Thing is found, the string is returned unmodified.
;
;		Entry				Exit
;	A3	pointer to string		preserved
;---
fu_expdev
exp.reg reg	d0-d5/a0-a2/a4
expt.frm equ	50
	movem.l exp.reg,-(sp)
	move.l	#$dfdfdf00,d0		; DEVx mask
	moveq	#5,d5			; length of driver name
	and.l	2(a3),d0
	cmp.l	#$44455600,d0		; that's DEV?
	bne	exp_ret
	move.b	(a3,d5.w),d4		; drive number
	cmp.b	#'0',d4 		; check number for lower bound
	bls	exp_ret
	cmp.b	#'8',d4 		; check upper bound
	bhi	exp_ret
; here we are with a DEV, so it must be converted now

	lea	dev_thg,a0
	move.l	#'USE$',d2		; we want to know the setup
	moveq	#-1,d1
	moveq	#100,d3
	moveq	#sms.uthg,d0
	xjsr	gu_thjmp

	bne.s	exp_ret 		; does not exist, so ignore
	move.l	a1,a4			; keep THING address

	sub.l	#expt.frm,sp		; allow for parameters on stack
	move.l	sp,a1
	move.b	d4,d0
	sub.b	#'0',d0 		; the drive we're looking for
	move.l	d0,(a1)
	move.l	#thp.ret+thp.str,4(a1)	; second
	lea	12(a1),a0
	move.l	a0,8(a1)		; address of return parameter

	jsr	thh_code(a4)		; get usage

	move.w	(a3),d0 		; get length
	move.w	d0,d3			; keep it
	clr.w	(a3)
	sub.w	d5,d0			; minus previous string
	move.w	d0,d4			; that many bytes to move
	move.w	12(a1),d2		; length to copy
	beq.s	exp_retf		; empty dev - no destination
	add.w	d2,d0			; add new string
	cmp.w	#41,d0
	bhi.s	exp_retf		; string too long
	lea	1(a3,d3.w),a0
	move.w	d2,d1
	sub.w	d5,d1
exp_cp1l
	move.b	(a0),(a0,d1.w)		; copy up
	subq.l	#1,a0
	subq.w	#1,d3
	bne.s	exp_cp1l
	lea	14(a1),a0
	lea	2(a3),a2
exp_cp2l
	move.b	(a0)+,(a2)+
	subq.w	#1,d2
	bne.s	exp_cp2l
	move.w	d0,(a3) 		; return new length
exp_retf
	add.l	#expt.frm,sp

	lea	dev_thg,a0
	moveq	#-1,d1
	moveq	#sms.fthg,d0
	xjsr	gu_thjmp

exp_ret
	movem.l (sp)+,exp.reg
	rts

dev_thg dc.w	3,'DEV '

	end
