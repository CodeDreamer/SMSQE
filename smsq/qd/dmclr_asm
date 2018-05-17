* Clear display on mode       V2.01    1994  Tony Tebby  QJUMP
*
	section qd
*
	xdef	qd_dmclr

	include dev8_keys_sys
	include dev8_keys_chn
	include dev8_keys_con
	include dev8_keys_iod
	include dev8_keys_qdos_io
	include dev8_mac_assert
*
*	d0-d7/a0-a5 smashed
*
qd_dmclr
	move.l	sys_clnk(a6),a3 		console linkage
	move.l	sys_chtb(a6),a5 		channel table base

qdm_loop
	move.l	(a5)+,d0
	bmi.s	qdm_next
	move.l	d0,a0
	moveq	#-iod_iolk,d0
	add.l	chn_drvr(a0),d0
	cmp.l	d0,a3				console?
	bne.s	qdm_next

	sf	sd_cattr(a0)			no char attributes

	assert	sd_pcolr-iow.spap,sd_scolr-iow.sstr,sd_icolr-iow.sink
	lea	sd_pcolr(a0),a4
	moveq	#iow.spap,d6
	moveq	#2,d5				set three colours
qdm_colr
	move.b	(a4)+,d1
	bsr.s	qdm_call
	addq.w	#1,d6
	dbra	d5,qdm_colr

	tst.l	sd_xinc(a0)			any increments?
	beq.s	qdm_next			... no, dummy, do not set

	moveq	#0,d1
	moveq	#0,d2
	moveq	#iow.ssiz,d6
	bsr.s	qdm_call

	move.b	(a4),d1 			border colour
	move.w	sd_borwd(a0),d2
	moveq	#iow.defb,d6
	bsr.s	qdm_call

	moveq	#iow.clra,d6			and clear
	bsr.s	qdm_call

qdm_next
	cmp.l	sys_chtt(a6),a5 		any more channels in table
	blo.s	qdm_loop

	rts

qdm_call
	moveq	#0,d0
	move.b	d6,d0
	moveq	#0,d3
	movem.l d4/d5/d6/a3/a4/a5,-(sp)
	move.l	iod_ioad(a3),a4
	jsr	(a4)
	movem.l (sp)+,d4/d5/d6/a3/a4/a5
	rts

	end
