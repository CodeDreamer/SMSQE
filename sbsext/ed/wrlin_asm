* ED - write lines on page     1985   Tony Tebby   QJUMP
*
	section ed
*
	xdef	ed_rewrt
	xdef	ed_wprev
	xdef	ed_wnext
	xdef	ed_wrall
	xdef	ed_wrbot
	xdef	ed_wrtop
	xdef	ed_trap3
	xdef	edw_d5
*
	xref	ed_scrup
	xref	ed_scrdn
	xref	ed_fline
	xref	ed_stlin
	xref	ed_bscan
	xref	ed_xplin
*
	include dev8_sbsext_ed_keys
	include dev8_sbsext_ed_data
*
*	d2 c	number of rows to write (ed_wrest)
*	d3 cr	start row in line / end row in line
*	d4 cr	start line number / end line number
*	d5 c p	pointer to row data structure
*	d6 c p	row number
*	d7   p	column number
*
* rewrite all rows of the line at (d6)
*
ed_rewrt
	move.w	d6,-(sp)	save window row pointer
	move.w	edr_rno(a5,d5.w),d3 and row number
	sub.w	d3,d6		set top row of line
	bge.s	edrw_sadd
	moveq	#0,d6		... off top
edrw_sadd
	move.w	d6,d5		set row address
	lsl.w	#3,d5
	move.w	edr_rno(a5,d5.w),d3 set first row number to be written
	tst.w	d4		any line to be written?
	beq.s	edrw_fill	... no, just fill up
edrw_dor
	bsr.l	edw_row 	write row of line
	tst.w	d3		is next a first row of line?
	beq.s	edrw_sdn	... yes, scroll down if any old rows left
	cmp.w	ed_nrows(a5),d6 was this last row in window?
	bge.l	edw_slast	... yes, fill in last row pointers
	tst.w	edr_rno(a5,d5.w) is this on screen part of next line?
	bne.s	edrw_dor	... no, overwrite
	move.w	d6,d2
	bsr.l	ed_scrup	... yes, scroll up to get blank line
	bra.s	edrw_dor
*
edrw_fill
	move.w	edr_lnr(a5,d5.w),d4	set old line number
	addq.w	#1,d4			(next line)
	addq.w	#1,edr_rno(a5,d5.w)	ensure this row is overwritten
edrw_sdn
	move.w	ed_nrows(a5),d0 	find off window row
	lsl.w	#3,d0
	move.w	edr_lnr(a5,d0.w),d1	line number of off window
	beq.s	edrw_sdl		... zero, OK
	cmp.w	d4,d1			is it before next line?
	bge.s	edrw_sdl		... no, OK
	move.w	d4,edr_lnr(a5,d0.w)	... yes, set to next line
	clr.w	edr_rno(a5,d0.w)
edrw_sdl
	tst.w	edr_rno(a5,d5.w)	is this row a new line?
	beq.s	edw_exit		... yes
	move.w	d6,d2		
	bsr.l	ed_scrdn		... no, get rid of it
	cmp.w	(sp),d6 		is this row at or beyond cursor?
	bgt.s	edrw_sdl		... no
	subq.w	#1,(sp) 		... yes, so move cursor up
*	bge.s	edrw_sdl		!!!!!! believe these not needed
*	clr.w	(sp)			... oops cursor off top
	bra.s	edrw_sdl
	page
*
* Write previous window
*
ed_wprev
	move.w	ed_nrow1(a5),d1 scan back one less than window height
	bsr.l	ed_bscan
	bra.s	ed_wrall	and write all
*
* Write next window
*
ed_wnext
	move.w	ed_nrow1(a5),d1 find last line/row in window
	asl.w	#3,d1
	move.w	edr_rno(a5,d1.w),d3 
	move.w	edr_lnr(a5,d1.w),d4
	beq.s	edw_rts 	no more at bottom
	bra.s	ed_wrall	rewrite whole screen
*
* Find top line and save pointers
*
edw_ftop
	bsr.l	ed_fline	find line
	move.l	d1,d4		set actual line number
	bra.l	ed_stlin	and save top line pointers
*
* Write all of screen
*
ed_wrall
	bsr.s	edw_ftop	find top line
	move.w	ed_nrows(a5),d2 write nrows to bottom
	page
*
* Write bottom of screen
*
ed_wrbot
	move.w	d6,-(sp)	save row pointer
	move.w	ed_nrows(a5),d6 bottom of screen
	sub.w	d2,d6		first line to be written
	move.w	d6,d5
	lsl.w	#3,d5		and address of data structure
*
edw_loop
	bsr.s	edw_row
	cmp.w	ed_nrow1(a5),d6 was it last row?
	ble.s	edw_loop	... no, carry on
edw_slast
	move.w	d3,edr_rno(a5,d5.w) save line number for one row off bottom
	move.w	d4,edr_lnr(a5,d5.w)
edw_exit
	move.w	(sp)+,d6	reset row pointers
edw_d5
	move.w	d6,d5
	lsl.w	#3,d5
	moveq	#0,d0
edw_rts
	rts
*
* Write a new row of line at top
*
ed_wrtop
	move.w	d6,-(sp)	save row pointer
	moveq	#0,d5		set cursor to top
	moveq	#0,d6
*
	moveq	#1,d1		scan back one row
	bsr.l	ed_bscan
	move.w	d1,d4		actual new back row
	cmp.w	edr_rno(a5),d3	is row number the same?
	bne.s	edwt_do 	... no, ok
	cmp.w	edr_lnr(a5),d4	and line number?
	bne.s	edwt_do 	... no, ok
	moveq	#0,d4		blank row at top
edwt_do
	bsr.s	edw_row
	bra.s	edw_exit	and reset row pointers
	page
*
* Write one row
*
edw_row
	moveq	#sd.pos,d0	set cursor position
	moveq	#0,d1		first column
	move.w	d6,d2		next row
	tst.w	d3		next is first row of next line?
	beq.s	edw_pos 	... yes, start at beginning
	moveq	#ed.xoff,d1	... no, indent
edw_pos
	bsr.s	ed_trap3
	moveq	#sd.clrln,d0	clear this row
	bsr.s	ed_trap3
*
	tst.w	d4		is this a non existant line?
	beq.s	edw_zpnt	... yes, just set pointers zero
	blt.s	edw_llen	... no, but is slightly imaginary
	cmp.w	ed_cline(a5),d4 is this line current?
	beq.s	edw_llen
	bsr.l	ed_fline	find this line
	bne.s	edw_zpnt	... off end, just set pointers zero
	move.w	d1,d4
	move.w	d4,ed_cline(a5) and save it
	bsr.l	ed_xplin	and expand the line into the BASIC buffer
edw_llen
	move.l	bv_bfp(a6),d0	get length of line
	sub.l	bv_bfbas(a6),d0
edw_fpnt
	move.w	d3,d1		row within line
	mulu	ed_ncolx(a5),d1 start of row
	bne.s	edw_xtra	... not at start of line
	move.w	ed_ncols(a5),d2 end of first row
	bra.s	edw_limit
edw_xtra
	addq.w	#ed.xoff,d1	offset start of extra row
	move.w	d1,d2
	add.w	ed_ncolx(a5),d2
edw_limit
	bsr.s	edw_rpnt	set row/line pointers
*
	addq.w	#1,d3		move to next row of line
	cmp.w	d0,d2		... is this last row of line?
	blt.s	edw_spnt	... no
	move.w	d0,d2		reset end pointer
	moveq	#0,d3		set first row
	addq.w	#1,d4		of next line
	bvc.s	edw_chk
	moveq	#0,d4		. no (more) lines
	bra.s	edw_spnt
edw_chk
	bgt.s	edw_spnt	... it was real
	neg.w	d4		... new line, actual next
	addq.w	#2,d4
edw_spnt
	movem.w d1/d2,edr_chs-edr.len(a5,d5.w) set start and end pointers
*
	moveq	#io.sstrg,d0	send string
	move.l	bv_bfbas(a6),a1 set up to write row
	add.w	d1,a1		... start address
	sub.w	d1,d2		... length
	ble.s	edw_back	oops, nothing there at all (shortened line)
	trap	#4
*
* TRAP #3 saving d3.w
*
ed_trap3
	move.w	d3,-(sp)
	moveq	#-1,d3
	trap	#3
	move.w	(sp)+,d3
	rts
*
* Zero pointers for end of file
*
edw_zpnt
	moveq	#0,d4		no line number
	moveq	#0,d3		... or row
edw_rpnt
	move.w	d3,edr_rno(a5,d5.w) set row/line numbers
	move.w	d4,edr_lnr(a5,d5.w)
	addq.w	#1,d6		next row
	addq.w	#edr.len,d5
	rts
edw_back
	subq.w	#1,d6		previous row
	addq.w	#edr.len,d5
	rts
	end
