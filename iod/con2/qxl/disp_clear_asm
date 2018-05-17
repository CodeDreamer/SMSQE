; QXL clear display	    1999  Tony Tebby
; 2005.01.10	1.01	completely revamped version (BC)
	section con

	xdef	cn_disp_clear
	xdef	cn_scrn_clear
	xdef	cn_copy_clear

	include 'dev8_smsq_qxl_keys'

cnd.reg reg	a0/a1

;---
; clear screen and copy
;+++
cn_disp_clear
	bsr.s	cn_scrn_clear
	bsr.s	cn_copy_clear
	moveq	#0,d0
	rts

;---
; clear screen
;+++
cn_scrn_clear
	movem.l cnd.reg,-(sp)
	move.l	qxl_scr_work,a1
	move.l	qxl_scrb(a1),a0
	bsr.s	cdn_cls
	movem.l (sp)+,cnd.reg
	rts

;---
; clear copy
;+++
cn_copy_clear
	movem.l cnd.reg,-(sp)
	move.l	qxl_scr_work,a1
	move.l	qxl_cpyb(a1),a0
	bsr.s	cdn_cls
	movem.l (sp)+,cnd.reg
	rts

cdn_cls
	move.w	qxl_scrl(a1),d0
	mulu	qxl_scry(a1),d0
	lsr.l	#6,d0				; clear in lumps of 64
	subq.l	#1,d0

cdn_cls_loop
	clr.l	(a0)+
	clr.l	(a0)+
	clr.l	(a0)+
	clr.l	(a0)+
	clr.l	(a0)+
	clr.l	(a0)+
	clr.l	(a0)+
	clr.l	(a0)+
	clr.l	(a0)+
	clr.l	(a0)+
	clr.l	(a0)+
	clr.l	(a0)+
	clr.l	(a0)+
	clr.l	(a0)+
	clr.l	(a0)+
	clr.l	(a0)+
	dbra	d0,cdn_cls_loop
	rts

	end
