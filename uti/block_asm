; Fill Block in given Window

	section utility

	include dev8_keys_qdos_io
	include dev8_mac_xref

	xdef	ut_block

;+++
;		Entry				Exit
;	d1	percentage			preserved
;	d2	clear window
;	d3+					preserved
;	a0	channel-id			preserved
;	a1					preserved
;	a2+					preserved
;
;	Error return:	all usual error returns
;---
stk.frame   equ   8
blockreg reg  d1-d3/a0-a1
ut_block
	movem.l blockreg,-(sp)
	subq.l	#stk.frame,sp		; room for enquiry result
	move.w	d1,d3

	tst.b	d2
	beq.s	no_clear

	moveq	#iow.clra,d0
	xjsr	gu_iow			; clear sub-window
no_clear
	move.l	sp,a1			; enquiry buffer
	moveq	#0,d1
	moveq	#iow.pixq,d0
	xjsr	gu_iow			; find size of window

	move.w	(a1),d1 		; width
	ext.l	d1
	ext.l	d3
	mulu.w	d3,d1			; first a large value
	divu	#100,d1 		; then divide to give total width

	move.w	d1,(a1) 		; insert width of block

	moveq	#0,d1			; colour
	moveq	#iow.blok,d0
	xjsr	gu_iow

	addq.l	#stk.frame,sp		; adjust stack
	movem.l (sp)+,blockreg
	tst.l	d0
	rts

	end
