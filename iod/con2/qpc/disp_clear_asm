; QPC clear display	    1999  Tony Tebby

	section con

	xdef	cn_disp_clear

	include 'dev8_smsq_qpc_keys'

;+++
; Clear screen and copy
;---
cn_disp_clear
cnd.reg reg	a0/a1
	movem.l cnd.reg,-(sp)

	move.l	#qpc_scr_work,a1
	move.w	qpc_scrl(a1),d0
	mulu	qpc_scry(a1),d0
	move.l	qpc_scrb(a1),a0

	lsr.l	#4,d0			 ; clear in lumps of 16
	subq.l	#1,d0
cnd_cls
	clr.l	(a0)+
	clr.l	(a0)+
	clr.l	(a0)+
	clr.l	(a0)+
	dbra	d0,cnd_cls

	movem.l (sp)+,cnd.reg
	moveq	#0,d0
	rts

	end
