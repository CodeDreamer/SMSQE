; OPW.xxx compatible routines				1989 Tony Tebby
;							 2002 Marcel Kilgus

	section wman

	xdef	wm_opw

	xref	gu_fopen
	xref	wmc_trap3

	include 'dev8_keys_qdos_io'
	include 'dev8_keys_qdos_ioa'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_qlv'

con	dc.w	3,'CON'
scr	dc.w	3,'SCR'

;+++
; Emulate OPW.WIND, OPW.CON and OPW.SCR vectored routines
;
;	D0 c	OPW.WIND, OPW.CON or OPW.SCR
;	D1    s
;	D2    s
;	D3    s
;	A0 cr	ptr to name (OPW.WIND only) / channel ID
;	A1 c  s ptr to parameter block
;	A2    s
;	A3    s
;---
wm_opw
	sub.w	#opw.wind,d0
	beq.s	uq_opwin
	subq.w	#2,d0
	beq.s	uq_opcon
	bra.s	uq_opscr

uq_opwin
	bsr.s	uow_open
	bne.s	uow_rts

	moveq	#iow.defb,d0
	bra.s	uow_defw

uq_opcon
	lea	con,a0
	bra.s	uow_opw
uq_opscr
	lea	scr,a0
uow_opw
	bsr.s	uow_open
	bne.s	uow_rts
	moveq	#iow.defw,d0
uow_defw
	move.w	(a1)+,d1
	move.w	(a1)+,d2
	addq.w	#4,a1
	bsr.s	uow_io
	bne.s	uow_rts

	moveq	#iow.sink,d0
	moveq	#0,d1
	move.w	-(a1),d1
	bsr.s	uow_io
	moveq	#iow.spap,d0
	move.w	-(a1),d1
	bsr.s	uow_io
	moveq	#iow.sstr,d0
	bsr.s	uow_io
	moveq	#iow.clra,d0
uow_io
	movem.l d1/d3/a1,-(sp)
	moveq	#forever,d3		 ; wait forever
	jsr	wmc_trap3
	movem.l (sp)+,d1/d3/a1
	tst.l	d0
uow_rts
	rts

uow_open
	moveq	#0,d3
	jmp	gu_fopen

	end
