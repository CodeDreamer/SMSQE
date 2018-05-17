	section gw
* draw in a pixel, filling a line if need be (identical to QL version)
	xdef	GW_PIXEL

	xref	bm_block,bm_xblok	CS_FILL,CS_OVER

	include 'dev8_Minerva_INC_GW'
	include 'dev8_Minerva_INC_SD'
	include 'dev8_Minerva_INC_HP'
	include 'dev8_Minerva_INC_assert'

* The fill buffer is arranged to have space for up to 256 lines with each
* entry being a pair of words.
* The first word is the minimum pixel so far seen on the line and the second is
* minimum pixel never seen so far.
* One special case is the initial zero longword, indicating that no pixel has
* yet been seen for this line.
* Another special case crops up if a pixel at 32767 (or even 32766 in mode 8)
* is requested, when the pixel is effectively treated as zero width in order to
* avoid any problems.
* An improvement to the system would be to re-allocate the fill buffer whenever
* the drawing area of a window is changed, and make it have just enough lines.
* This would then allow for windows taller than 256 lines, which will crash the
* current code!
* There is another flaw in this code, in that a mode change from 4 to 8 can
* result in a bit of a mess if an existing filled area starts or finishes at an
* odd pixel, and is then extended. It is not clear exactly how to avoid this.

* Note that the CS routines require D0-D3 and A1 as parameters, but preserve
* all registers.

* D0 -ip - x coordinate (lsw)
* D1 -ip - y coordinate (lsw)
* D2 -ip - mask within word fro pixel (lsw)
* D3 -ip - colour masks (msw/lsw)
* A5 -ip - screen word address for pixel
* A6 -ip - stack frame
* D4 destroyed

reglist reg	d0-d3/d5-d7/a0-a2  (not d4!!) D0-D3/A0-A1

gw_pixel
	assert	XSIZE,YSIZE-2
	move.l	XSIZE(A6),D4
	cmp.w	D4,D1		if y outside window then do nothing
	bcc.s	RTS0
	swap	D4
	tst.b	FMOD(A6)
	bne.s	DO_FILL	if fill mode is on then go to it
dodot
	cmp.w	D4,D0		if x outside window then do nothing
	bcc.s	RTS0
	move.w	D3,D4		set up colour mask
	and.w	D2,D4
	tst.b	OVER(A6)
	beq.s	SETIT
	eor.w	D4,(A5)	xor it in
rts0
	rts

setit
	move.w	D2,D5		write colour on top
	or.w	(A5),D5	fetch old stuff, but set our bit all ones
	sub.w	D2,D5		then knock them out
	add.w	D4,D5		and put in what we want
	move.w	D5,(A5)
	rts

first
	move.w	D2,-(A1)
	move.w	D0,-(A1)	store single pixel range
single
	movem.l (SP)+,REGLIST
	bge.s	DODOT		now go back to put in this dot
	rts

do_fill
	movem.l REGLIST,-(SP)	... save a few registers !

	move.l	CHNP(A6),A0	retrieve the channel defn ptr

	move.w	D1,A1
	add.l	A1,A1
	lea	HP_END(A1,A1.L),A1
	add.l	SD_FBUF(A0),A1	256 word pairs, one pair for each line

	move.w	XINC(A6),D2	calculate right edge of pixel
	add.w	D0,D2
	bvc.s	NOTHUGE
	move.w	D0,D2
nothuge

	tst.l	(A1)+
	beq.s	FIRST
	cmp.w	-(A1),D0
	bge.s	RIGHT
	cmp.w	-(A1),D0
	bge.s	INSIDE

	move.w	(A1),D2	get left edge of existing area
	move.w	D0,(A1)	replace with this pixel to extend it left
check
	bge.s	LOWOK		if low is negative we must bring it up to zero
	clr.w	D0
lowok
	cmp.w	D2,D4		if right edge escapes window, trim it down
	bge.s	HIGHOK
	move.w	D4,D2
highok
	sub.w	D0,D2		now establish exactly how much we're doing
	cmp.w	XINC(A6),D2
	ble.s	SINGLE		if single then do it quick

	add.w	XMIN(A6),D0	add x origin
	add.w	YMIN(A6),D1	add y origin

		;***********************
	swap	d0		origin
	move.w	d1,d0
	move.w	d2,d1		size
	swap	d1
	move.w	#1,d1
	move.l	d0,d2		origin again

	btst	#0,d2
	bne.s	masks
	swap	d3
masks
	move.w	d3,d7		masks
	swap	d7
	move.w	d3,d7
	move.l	d3,d6
	swap	d3
	move.w	d3,d6

	move.l	sd_scrb(a0),a1
	move.w	sd_linel(a0),a2
	 ; *********

	tst.b	OVER(A6)
	beq.s	FILL_IT
	jsr	bm_xblok	  CS_OVER(PC)
done
;***	    addq.l  #8,SP
inside
	movem.l (SP)+,REGLIST
	rts

right
	move.w	(A1),D0	get old right edge as start of fill area
	move.w	D2,(A1)	extent right to new edge
	tst.w	D0
	bra.s	CHECK

fill_it
	jsr	bm_block	 CS_FILL(PC)
	bra.s	DONE

	end
