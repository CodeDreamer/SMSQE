* ED - Window scrolling operations    1985  Tony Tebby   QJUMP
*
	section ed
*
	xdef	ed_scrup
	xdef	ed_scrdx
	xdef	ed_scrdn
*
	xref	ed_wrbot
	xref	ed_trap3
*
	include dev8_sbsext_ed_keys
	include dev8_sbsext_ed_data
*
*	d2	row to scroll up or down wrt (line vacated or overwritten)
*
ed_scrup
	move.w	ed_nrows(a5),d0 scroll up all lines (including dummy at end)
	move.w	d0,d1		starting at end
	asl.w	#3,d1
	sub.w	d2,d0		but only going up as far as d2
	add.w	d0,d0
	bra.s	ed_ulend
ed_uloop
	move.l	ed_rtab-4(a5,d1.w),ed_rtab+4(a5,d1.w)
	subq.l	#4,d1
ed_ulend
	dbra	d0,ed_uloop
*
	moveq	#1,d1		scroll up
*
eds_do
	moveq	#sd.scrol,d0	assume simple scroll
	move.w	d1,-(sp)	save scroll direction
	subq.w	#1,d2		is it top line?
	blt.s	eds_dos 	... yes, just scroll all
*
	moveq	#sd.pos,d0	position the cursor
	moveq	#0,d1		set x (y already in d2)
	bsr.s	eds_trap3
*
	moveq	#sd.scrbt,d0	scroll bottom only
*
eds_dos
	move.w	(sp)+,d1	set scroll direction
	muls	ed_yinc(a5),d1	... and distance
eds_trap3
	bra.l	ed_trap3	and scroll
*
ed_scrdx
	move.w	ed_nrow1(a5),d0 number of lines to scroll
	sub.w	d2,d0		less stationary bit
	add.w	d0,d0		2 long words per row
	move.w	d2,d1		set start position
	addq.w	#1,d1		(one row down)
	lsl.w	#3,d1

	bra.s	ed_dlend
ed_dloop
	move.l	ed_rtab(a5,d1.w),ed_rtab-8(a5,d1.w)
	addq.l	#4,d1
ed_dlend
	dbra	d0,ed_dloop
*
	move.w	d1,a2		save last line address
	moveq	#-1,d1		scroll down
	bra.s	eds_do
*
ed_scrdn
	movem.w d3/d4,-(sp)	save row/line numbers
	bsr.s	ed_scrdx
	moveq	#1,d2		write one row at bottom
	move.w	edr_rno(a5,a2.l),d3 row and
	move.w	edr_lnr(a5,a2.l),d4 line number
	bsr.l	ed_wrbot
eds_exit
	movem.w (sp)+,d3/d4	restore row/line
	rts
	end
