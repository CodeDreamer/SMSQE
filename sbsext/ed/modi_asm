* ED - start, end and abort modification    1985  Tony Tebby  QJUMP
*
	section ed
*
	xdef	ed_sttmd
	xdef	ed_endmd
	xdef	ed_abtmd
	xdef	ed_getln
*
	xref	ed_fline
	xref	ed_xplin
	xref	ed_rewrt
	xref	ed_wrall
	xref	ed_snorm
	xref	ed_shigh
	xref	ed_putln
	xref	ed_dline
	xref	ed_dscr
	xref	ed_trap3
*
	include dev8_sbsext_ed_keys
	include dev8_sbsext_ed_data

*
* get line (for move left by word)
*
ed_getln
	tst.b	ed_chang(a5)		is line already modified?
	bmi.s	edm_rts2		... yes, do nought
*
	move.w	edr_lnr(a5,d5.w),d4	is it a real line?
	bgt.s	edm_getl		... yes, get a line
	st	edr_lnr(a5,d5.w)	... no, make sure that it is neg line nr
	move.l	bv_bfbas(a6),bv_bfp(a6) clear buffer
	clr.l	ed_cline(a5)
edm_rts2
	rts
*
edm_getl
	cmp.w	ed_cline(a5),d4 	is current line the one we want
	beq.s	edm_rts2		... yes, we've got it
	bsr.l	ed_fline		... no, find line
	bsr.l	ed_xplin		and expand it
	move.w	d4,ed_cline(a5) 	set current line
	rts
*
* abort modification of line
*
ed_abtmd
	tst.b	ed_chang(a5)		is line modified?
	beq.s	edm_rts1		... no, give up
	bsr.s	edm_norm		... yes, reset to normal
edm_rest
	move.w	edr_lnr(a5,d5.w),d4	get current line
	bgt.s	edm_fget
	jmp	ed_dscr 		... nothing there before

*
* start modification of line
*
ed_sttmd
	tas	ed_chang(a5)		is line already modified?
	bne.s	edm_rts1		... yes, do nought
	bsr.l	ed_shigh
*
	move.w	edr_lnr(a5,d5.w),d4	is it a real line?
	bgt.s	edm_get 		... yes, get a line
	st	edr_lnr(a5,d5.w)	... no, make sure that it is neg line nr
	move.l	bv_bfbas(a6),bv_bfp(a6) clear buffer
	clr.l	ed_cline(a5)
	moveq	#sd.clrln,d0		and line
	bra.l	ed_trap3
edm_rts1
	rts
*
edm_get
	cmp.w	ed_cline(a5),d4 	is current line the one we want
	beq.s	edsm_wlin		... yes, write line
edm_fget
	bsr.l	ed_fline		... no, find line
	bsr.l	ed_xplin		and expand it
edm_scl
	move.w	d4,ed_cline(a5) 	set current line
edsm_wlin
	bra.l	ed_rewrt		and write line

edm_norm
	bra.l	ed_snorm		clear change flag, set normal colours
*
* end modification of line
*
ed_endmd
	tst.b	ed_chang(a5)		is line modified?
	beq.s	edm_rts1		... no, give up
	bsr.l	ed_putln		put line back
	blt.s	edm_rts 		... oops
	move.w	d0,d4			get actual line number
	move.w	d4,ed_cline(a5) 	this is current line
	bsr.s	edm_norm		... empty, set normal colours, no change
	tst.w	d4			line?
	ble.l	ed_dscr 		... none, delete from screen

	move.w	ed_nrows(a5),d2 	set end row pointer
	lsl.w	#3,d2
	move.w	edr_lnr(a5,d5.w),d0	and set old line number here
	ble.s	edm_newl		... was zero, a new line
	cmp.w	d0,d4			... not zero, the same?
	beq.s	edm_fget		... yes, get the new version of the line
*
	bsr.s	edm_wind		check if new line in window
	blt.s	edm_rest		... no, restore
	bra.s	edm_redraw		... yes, redraw complete window
*
edm_newl
	move.w	d6,d1			look for previous line
	sub.w	edr_rno(a5,d5.w),d1	... first row of this one
	subq.w	#1,d1			last of previous
	blt.s	edm_tnext		... off top, test the next line
	lsl.w	#3,d1
	beq.s	edm_cpln		... top line
	tst.w	edr_lnr(a5,d1.w)	is previous line blank?
	beq.s	edm_nwind		... yes
edm_cpln
	cmp.w	edr_lnr(a5,d1.w),d4	is this line after the previous
	ble.s	edm_nwind		... no, test if new line in window

*
edm_tnext
	move.w	d5,d1			get pointer to table
edm_tnloop
	addq.w	#edr.len,d1		next row
	cmp.w	d2,d1			off end?
	bgt.s	edm_fget		... yes, line is in order
	tst.w	edr_rno(a5,d1.w)	... no, is it a first row?
	bne.s	edm_tnloop		... no, carry on
	move.w	edr_lnr(a5,d1.w),d0	is this end of file?
	beq.s	edm_fget		... yes, in order
	cmp.w	d0,d4			is new line before this one?
	blt.s	edm_fget		... yes, in order
*
edm_nwind
	bsr.s	edm_wind		is new line in window?
	blt	ed_dscr 		... no, just delete this line from scree
*
edm_redraw
	moveq	#-edr.len,d0		find top most non zero line
edm_ftop
	addq.l	#edr.len,d0
	move.w	edr_lnr(a5,d0.w),d4	set top of screen pointer
	ble.s	edm_ftop
	move.w	edr_rno(a5),d3
	bra.l	ed_wrall		and write all of screen
*
edm_wind
	cmp.w	edr_lnr(a5),d4		new line off the top?
	blt.s	edm_rts 		... yes
	move.w	edr_lnr(a5,d2.w),d0
	beq.s	edm_rts 		bottom is end of file
	cmp.w	d4,d0			off the bottom
edm_rts
	rts
*
	end
