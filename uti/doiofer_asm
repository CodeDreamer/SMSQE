; File IO error interaction

	section utility


	include dev8_keys_qdos_ioa
	include dev8_mac_xref

	xdef	do_iofer	; Do file-io

;+++
; Do file-io, and ask in error case for Retry, Overwrite, Edit or Abort.
;
;	 ENTRY			EXIT
; D0.l	 required action	-1 Error or ESC  / 3 Edit name
;---
do_iofer
	movem.l d1-d3/a1,-(sp)	; keep registers for another go
	move.l	d0,-(sp)	; and keep required action
	xjsr	gu_iow
	beq.s	iofer_ok	; all is fine

	moveq	#-1,d1		; origin
	moveq	#-1,d2		; colourway
	moveq	#1,d3		; allow edit/overwrite
	xjsr	mu_filer
	bne.s	iofer_err

	tst.w	d3		; ESC
	beq.s	iofer_err	; yes, so return

	cmp.w	#1,d3		; retry
	beq.s	iofer_retry

	cmp.w	#2,d3		; overwrite
	beq.s	iofer_overwrite

	move.l	d3,d0		; show edit
	bra.s	iofer_ok

iofer_retry
	move.l	(sp)+,d0	; get function code back

	movem.l (sp)+,d1-d3/a1	; and registers
	bra.s	do_iofer	; .. and retry

iofer_overwrite
	move.l	(sp)+,d0	; get function code back

	movem.l (sp)+,d1-d3/a1	; and registers
	moveq	#ioa.kovr,d3	; change to overwrite
	bra.s	do_iofer	; .. and retry

iofer_err
	moveq	#-1,d0		; error

iofer_ok
	addq.l	#4,sp		; remove function
	movem.l (sp)+,d1-d3/a1
	tst.l	d0
	rts



	end
