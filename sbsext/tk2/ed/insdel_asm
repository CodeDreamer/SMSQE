* ED - Inserting and deleting characters    1985  Tony Tebby	QJUMP
*
	section ed
*
	xdef	ed_insch
	xdef	ed_delch
	xdef	ed_setps
*
	xref	ed_right
	xref	ed_scrup
	xref	ed_scrdx
*
	include dev8_sbsext_ed_keys
	include dev8_sbsext_ed_data
*
*	d1 c s	character to be inserted
*
* find character position
*
ed_fcpos
	move.w	edr_rno(a5,d5.w),d1 get row number
	mulu	ed_ncolx(a5),d1 set row*cols
	add.w	d7,d1		add column position to row*ncols
*
	move.l	bv_bfbas(a6),a2 get base of buffer
	add.w	d1,a2		... character pointer
	move.l	bv_bfp(a6),a3	... end pointer
*
	bsr.l	ed_setps	reposition the cursor
*
	move.l	a2,d1		character pointer for convenience
	cmp.l	a3,a2		condition EQ at end, GE off end
	rts
*
* insert character
*
ed_insch
	movem.w d5/d6,-(sp)	save cursor position
	move.b	d1,d4		and character
	bsr.s	ed_fcpos	find character position
	bge.s	edi_chks	... off end, check space for this position
	move.l	a3,d1		... within line, check space for eol
edi_chks
	addq.l	#2,d1		have a bit of spare
	sub.l	a3,d1		extra space required
	move.w	bv..chrix,a1
	jsr	bvo_chbf(a1)	check for space
*
	cmp.l	a3,a2		is this insert in line
	bge.s	edi_eol 	... no, off end
	tst.b	ed_overw(a5)	overwrite?
	beq.s	edi_ins 	... no, insert
	bsr.l	edi_putc	... yes, put character
	bra.s	edi_currt	and cursor right
*
edi_ins
	move.l	a3,a1		move buffer up
edi_uloop
	move.b	(a6,a1.l),1(a6,a1.l) one byte
	subq.l	#1,a1		from the end
	cmp.l	a2,a1		to the buffer pointer
	bge.s	edi_uloop
	addq.l	#1,a3
	bsr.l	edi_panr	pan right end
	bsr.l	edi_putc	and put character
*
* set d4 as length of line
*
	move.l	a3,d4		set end of line
	sub.l	bv_bfbas(a6),d4
	subq.w	#1,d4
edi_rloop
	cmp.w	edr_che(a5,d5.w),d4 is this the last row?
	bgt.s	edi_nrow	... no, do next row
*
	move.w	edr_rno(a5,d5.w),d1 find rhend of row in buffer address
	mulu	ed_ncolx(a5),d1
	add.w	ed_ncols(a5),d1
	cmp.w	edr_che(a5,d5.w),d1 is row full? 
	bne.s	edi_extrw	... no, just add one to che and cursor right
	tst.w	edr_rno+edr.len(a5,d5.w) is next a continuation row?
	bne.s	edi_nrow	... yes, just put character in it
	bsr.l	edi_crtx	... no, create an extra row
*
edi_nrow
	addq.w	#1,d6		next row
	addq.w	#edr.len,d5
	cmp.w	ed_nrows(a5),d6 is it visible?
	bge.s	edi_currt	... no, just move cursor right
	move.l	bv_bfbas(a6),a2 set
	add.w	edr_chs(a5,d5.w),a2 pointer to first character on line
	bsr.s	edi_mpan	set margin and pan
	bsr.s	edi_wrtc	and write character
	bra.s	edi_rloop
*
edi_eol
	bsr.s	edi_putc	put the character
edi_efill
	cmp.l	a2,a3		at end yet
	bge.s	edi_eset	... yes
	move.b	#' ',(a6,a3.l)	put in a space
	addq.l	#1,a3		next
	bra.s	edi_efill
edi_eset
	addq.l	#1,a3		end of buffer pointer
	move.l	a3,d1
	sub.l	bv_bfbas(a6),d1
	move.w	d1,edr_che(a5,d5.w) set end of row
	bra.s	edi_currt
*
edi_extrw
	addq.w	#1,edr_che(a5,d5.w) extra character in row
edi_currt
	move.l	a3,bv_bfp(a6)	reset end of buffer pointer
	movem.w (sp)+,d5/d6	restore cursor
	cmp.w	ed_ncol1(a5),d7 is cursor at rhs
	bne.s	edi_exit	... no
	tst.w	edr_rno+edr.len(a5,d5.w) is next a new line?
	bne.s	edi_exit	... no
	move.w	edr_che(a5,d5.w),d4 ... yes, set end of line
	bsr.s	edi_crend	and create extra line
edi_exit
	bra.l	ed_right	move cursor right
	page
*
edi_mpan
	bsr.s	ed_setpm	set position to margin
edi_panr
	moveq	#sd.panrt,d0	pan right hand end
	move.w	ed_xinc(a5),d1	... by x increment
	bra.s	edi_trp3
edi_putc
	move.b	d4,(a6,a2.l)	set character in buffer
edi_wrtc
	move.b	(a6,a2.l),d1	write charcter
	moveq	#io.sbyte,d0
edi_trp3
	moveq	#-1,d3
	trap	#3
	rts
ed_setpm
	moveq	#ed.xoff,d1	margin
	bra.s	ed_setp1
ed_setps
	move.w	d7,d1		x position
ed_setp1
	move.w	d6,d2		y position
	moveq	#sd.pos,d0
	bra.s	edi_trp3
edd_mpan
	bsr.s	ed_setpm	set to margin
edd_panr
	moveq	#sd.panrt,d0	pan right hand end
	move.w	ed_xinc(a5),d1
	neg.w	d1		left
	bra.s	edi_trp3
	page
*
* create empty extension row of line
*      
*	d4 c p	end buffer pointer
*
edi_crend
	cmp.w	ed_nrow1(a5),d6 insert at end?
	blt.s	edi_crtx	... no, scroll up
	moveq	#0,d2		... yes, scroll whole window
	bsr.l	ed_scrdx	down (and extend)
	subq.w	#1,d6		move cursor up a row
	subq.w	#edr.len,d5
	bra.s	edi_crset
edi_crtx
	move.w	d6,d2		scroll up rest of screen
	addq.w	#1,d2
	bsr.l	ed_scrup
edi_crset
	move.l	edr_lnr(a5,d5.w),edr_lnr+edr.len(a5,d5.w) move line / row numbers
	addq.w	#1,edr_rno+edr.len(a5,d5.w) next row
	move.w	d4,edr_chs+edr.len(a5,d5.w) empty
	move.w	d4,edr_che+edr.len(a5,d5.w)
	rts
	page
*
* delete characters
*
ed_delch
	movem.w d5/d6,-(sp)
	bsr.l	ed_fcpos		get pointers
	bge.s	edd_exit		... off end
	tst.b	ed_overw(a5)		is it overwrite?
	beq.s	edd_remv		... no, remove a character
	moveq	#' ',d4 		... yes, overwrite with spaces
	bsr.s	edi_putc
	bra.s	edd_exit
edd_remv
	move.l	a2,a1			start at current position
edd_dloop
	move.b	1(a6,a1.l),(a6,a1.l)	move a byte
	addq.l	#1,a1 
	cmp.l	a3,a1			until end of buffer
	blt.s	edd_dloop
	bsr.s	ed_setps		set position 
	bsr.s	edd_panr		and pan right hand end of line
*
* set d4 as end of line
*
	move.l a3,d4			end
	sub.l  bv_bfbas(a6),d4		... less base
edd_rloop
	cmp.w  edr_che(a5,d5.w),d4	is this end of line?
	beq.s  edd_lrow 		... yes, finish off
	move.l bv_bfbas(a6),a2		get address of character
	add.w  edr_che(a5,d5.w),a2	to fill in rhs
	subq.l #1,a2			(one character to left)
	move.w ed_ncol1(a5),d1		set cursor posn
	bsr.s  ed_setp1 		to rhs
	bsr.l  edi_wrtc 		write character
*
	cmp.w  ed_nrow1(a5),d6		end of window?
	bge.s  edd_done
	addq.w #1,d6			next row
	addq.w #edr.len,d5
	bsr.l  edd_mpan 		panned into margin
	bra.s  edd_rloop
edd_lrow
	subq.w #1,edr_che(a5,d5.w)	one fewer characters in last row
edd_done
	subq.l #1,bv_bfp(a6)		buffer now shorter
edd_exit
	movem.w (sp)+,d5/d6
	rts
*
	end
