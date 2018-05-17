; Trap#2 error interaction

	section utility


	include dev8_keys_qdos_ioa
	include dev8_mac_xref

	xdef	do_ioafer	; Do trap #2





;+++
; Do trap#2, and ask in error case for Edit, Retry or Abort.
;
;	 ENTRY			EXIT
; D0.l	 required action	-1 Error or ESC  / 3 Edit name
;---
do_ioafer
	movem.l d1-d3/a1,-(sp)	; keep registers for another go
	move.l	d0,-(sp)	; and keep required action
	move.l	a0,d2		; save a0
	trap	#do.ioa
	tst.l	d0
	beq.s	ioafer_ok	; all is fine

	move.l	d2,a0		; restore name !!!

	moveq	#-1,d1		; origin
	moveq	#-1,d2		; colourway
	moveq	#1,d3		; allow edit / overwrite
	xjsr	mu_filer
	bne.s	ioafer_err

	tst.w	d3		; ESC
	beq.s	ioafer_err	; yes, so return

	cmp.w	#1,d3		; retry
	beq.s	ioafer_retry

	cmp.w	#2,d3		; overwrite
	beq.s	ioafer_overwrite

	move.l	d3,d0		; show edit
	bra.s	ioafer_ok

ioafer_overwrite
	move.l	(sp)+,d0	; get function code back

	movem.l (sp)+,d1-d3/a1	; and registers
	moveq	#ioa.kovr,d3	; change to overwrite
	bra.s	do_ioafer	; .. and retry

ioafer_retry
	move.l	(sp)+,d0	; get function code back

	movem.l (sp)+,d1-d3/a1	; and registers
	bra.s	do_ioafer	; .. and retry

ioafer_err
	moveq	#-1,d0		; error

ioafer_ok
	addq.l	#4,sp		; remove function
	movem.l (sp)+,d1-d3/a1
	tst.l	d0
	rts


	end
