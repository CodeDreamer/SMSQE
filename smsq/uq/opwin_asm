; Open Window routines	QL compatible	 1989	Tony Tebby

	section uq

	xdef	uq_opwin
	xdef	uq_opcon
	xdef	uq_opscr

	xref	gu_iowp
	xref	gu_fopen

	include 'dev8_keys_qdos_io'
	include 'dev8_keys_qdos_ioa'
	include 'dev8_keys_qdos_sms'

con	dc.w	3,'CON'
scr	dc.w	3,'SCR'

uow_open
	moveq	#0,d3
	jmp	gu_fopen

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
	move.b	(a1)+,d1
	move.b	(a1)+,d2
	addq.w	#2,a1
	bsr.s	uow_io
	bne.s	uow_rts

	moveq	#sms.dmod,d0
	moveq	#-1,d1
	moveq	#-1,d2
	trap	#do.sms2
	move.b	d1,d3
	subq.b	#2,d3		 ; monochrome is mode 2

	moveq	#iow.sink,d0
	moveq	#0,d1
	move.b	-(a1),d1
	bsr.s	uow_iom
	moveq	#iow.spap,d0
	move.b	-(a1),d1
	bsr.s	uow_iom
	moveq	#iow.sstr,d0
	bsr.s	uow_iom
	moveq	#iow.clra,d0
uow_io
	jmp	gu_iowp
uow_rts
	rts
uow_iom
	tst.b	d3		 ; mono?
	bne.s	uow_io		 ; ... no
	move.b	uow_mono(pc,d1.w),d1
	bra.s	uow_io

uow_mono dc.b	0,0,0,0,7,7,7,7

	end
