* Initialise SMSQ trap vectors	 V2.00	  1986  Tony Tebby  QJUMP
*
	section init
*
	xdef	init_trp
*
	xref	init_wbase

	xref	qd_trap0
	xref	qd_trap1 
	xref	qd_trap2 
	xref	qd_trap3 
	xref	trp3_cinvi
	xref	io_cinvi
	xref	qd_trap4
	xref	qd_t1tab
*
	include 'dev8_keys_ev'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_keys_psf'
it_wbpre
	subq.w	#2,a5			 ; predecrement
	jsr	sms.wbase		 ; write word
	subq.w	#2,a5
	rts

*
*   d0/d1/d2/a0/a1/a2/a3/a4/a5 all smashed
*
init_trp
	lea	itrp_tab(pc),a1 	trap definition table
	move.w	(a1)+,d2		number of traps
	move.w	(a1)+,a0		start of traps
*
itrp_loop
	move.l	a1,a5			 ; next address
	add.w	(a1)+,a5		 ; where it is
	lea	sms.ctrap,a2		 ; trap patch
	move.w	(a2)+,d1		 ; length of trap patch
	add.w	d1,a2			 ; end of trap patch
	move.w	(a1)+,d0		 ; assume 0 (no movem), 4 (movem) or -1
	blt.s	itrp_set		 ; do not patch
	add.w	d0,a5			 ; start of code
	bgt.s	itrp_move		 ; not zero, skip first four bytes

	tst.w	d1			 ; no movem, any patch?
	beq.s	itrp_set
	lea	itrp_save+4,a3		 ; no movem, restore d7
	move.w	-(a3),d0
	bsr.s	it_wbpre
itrp_nmlp
	move.w	-(a2),d0		 ; copy cache code
	bsr.s	it_wbpre
	subq.w	#2,d1
	bgt.s	itrp_nmlp

	move.w	-(a3),d0		 ; save d7
	bsr.s	it_wbpre
	bra.s	itrp_set

itrp_mvlp
	move.w	-(a2),d0		 ; copy cache code
	bsr.s	it_wbpre
itrp_move
	subq.w	#2,d1
	bge.s	itrp_mvlp

	move.l	itrp_movem,d0
	bsr.s	it_wbpre		 ; put entry code back
	swap	d0
	bsr.s	it_wbpre

itrp_set
	exg	a5,a0
	jsr	init_wbase		 ; set vector
	exg	a5,a0

itrp_lend
	dbra	d2,itrp_loop

	move.w	sms.cinpt,d0		 ; invalidate cache after load?
	beq.s	itrp_t1
	lea	trp3_cinvi,a5		 ; internal invalidate cache for trap #3
	jsr	sms.wbase
	lea	io_cinvi,a5		 ; internal invalidate cache for io retry
	jsr	sms.wbase

itrp_t1
	lea	qd_t1tab,a2		trap 1 table
	lea	sms.t1max,a5		goes here
	move.w	(a2)+,d1
	move.w	d1,d0
	jsr	sms.wbase

itrp_t1loop
	move.l	a2,a0
	add.w	(a2)+,a0		real vector
	jsr	init_wbase
	subq.w	#1,d1
	bgt.s	itrp_t1loop

	rts


itrp_movem movem.l psf.reg,-(sp)

*
itrp_tab
	dc.w	5-1		5 traps
	dc.w	ev_trp0 	starting at trap #0
	dc.w	qd_trap0-*,0
	dc.w	qd_trap1-*,4
	dc.w	qd_trap2-*,4
	dc.w	qd_trap3-*,4
	dc.w	qd_trap4-*,-1

itrp_save
	move.l	d7,-(sp)
	move.l	(sp)+,d7
	end
