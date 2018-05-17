* ED - do enter key	 1985	Tony Tebby    QJUMP
*
	section ed
*
	xdef	ed_enter
*
	xref	ed_sttmd
	xref	ed_scrup
	xref	ed_down
	xref	ed_setln
	xref	ed_insch
*
	include dev8_sbsext_ed_keys
	include dev8_sbsext_ed_data
*
*
ede_fprev
	tst.w	d6			at top?
	beq.s	ede_rno 		... yes
	subq.w	#1,d6			... no, move up
	subq.w	#edr.len,d5
*
ed_enter
	move.w	edr_lnr(a5,d5.w),d4	set current line number
	ble.s	ede_fprev		... none, find previous
ede_loop
	bsr.l	ed_down 		move down a row
ede_rno
	tst.w	edr_rno(a5,d5.w)	a new line?
	bne.s	ede_loop		... no, go again
*
	move.w	edr_lnr(a5,d5.w),d0	get next line number
	ble.s	ede_set10		... off end
	sub.w	d4,d0			and difference
	cmp.w	#20,d0			big enough for 10 increment
	bge.s	ede_set10		... yes
	asr.w	#1,d0			... no, add half
	beq.s	ede_rts 		no room at all
	bra.s	ede_sttmd
ede_set10
	moveq	#10,d0			increment 10
ede_sttmd
	add.w	d0,d4			add increment
	bvc.s	ede_stttlo
	sub.w	d0,d4			... overflow
	lsr.w	#1,d0			try smaller increment
	bne.s	ede_sttmd
	bra.s	ede_rts 		... no room
ede_stttlo
	tst.w	edr_lnr(a5,d5.w)	is line already blank?
	beq.s	ede_slno
	move.w	d4,-(sp)		save new line number
	move.w	d6,d2
	bsr.l	ed_scrup		make room
	move.w	(sp)+,d4
ede_slno
	move.w	d4,edr_lnr(a5,d5.w)	set line number for row
	bsr.l	ed_setln		set line number
	bsr.l	ed_sttmd		highlight
	neg.w	edr_lnr(a5,d5.w)	the line is not really there
	move.w	edr_lnr(a5,d5.w),ed_cline(a5) ... current line
	move.l	bv_bfp(a6),d7		set the cursor position
	sub.l	bv_bfbas(a6),d7
	moveq	#' ',d1 		and add a space
	bra.l	ed_insch
ede_rts
	rts
	end
