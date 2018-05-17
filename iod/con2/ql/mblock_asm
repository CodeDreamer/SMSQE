; Move area (generalised PAN/SCROLL)  V2.01    Tony Tebby
;						Jonathan Oakley
;				       2.02   added pt_cmbblk (wl)
	section con
;
	xref	cn_maskl
	xref	cn_maskr
;
	xdef	cn_mblock
	xdef	pt_mblock
	xdef	pt_cmbblk
;
;	d1 c	size of section to move
;	d2 c	old origin in source area
;	d3 c	new origin in destination area
;	a2 c	row increment of source area
;	a3 c	row increment of destination area
;	a4 c	base address of source area
;	a5 c	base address of destination area
;	a6/a7	preserved
;
pt_mblock
cn_mblock
pt_cmbblk
	move.w	a2,d0			calculate source address
	mulu	d2,d0			row address
	add.l	d0,a4
	swap	d2			now column
	move.w	d2,d0
	lsr.w	#3,d0			shift out bits
	add.w	d0,a4			and set column address
	add.w	d0,a4

	move.w	a3,d0			calculate destination address
	mulu	d3,d0			row address
	add.l	d0,a5
	swap	d3			now column
	move.w	d3,d0
	lsr.w	#3,d0			shift out bits
	add.w	d0,a5			and set column address
	add.w	d0,a5

	moveq	#0,d4
	move.w	d3,d4			set pixel offset
	sub.w	d2,d4

	moveq	#%111,d0		get start pixel $$$
	and.w	d3,d0
	move.l	d1,d5			width
	swap	d5
;;	  add.w   #16,d5		$$$
	add.w	d0,d5			end pixel
	moveq	#%1111,d2		get end pixel (in long word)
	and.w	d5,d2
	add.w	#$f,d5			round up to long word $$$
	lsr.w	#4,d5			... length
	beq.l	pmw_done		none

	move.w	d1,d3			number of rows
	subq.w	#1,d3
	beq.l	pmw_done		none

	and.w	#%0111,d4		set pan distance $$$
	bne.l	pmw_pan 		... there is a bit

	jsr	cn_maskl(pc)		set left hand mask

	move.w	d2,d0
	jsr	cn_maskr(pc)		set right hand mask

	cmp.l	a4,a5			is it up?
	bgt.s	pmw_down		... no

; simple operations: no pan or pan by word increment
;
;	d3	row count-1
;	d5	long word count in row (-2)
;	d6	first edge mask (long word, screen format)
;	d7	second edge mask (long word, screen format)
;	a2	row increment (source)
;	a3	row increment (destination)
;	a4	source window address
;	a5	destination window address
;
; move up
;
pmw_up
	move.w	d5,d1			adjust increments
	lsl.w	#2,d1
	sub.w	d1,a2
	sub.w	d1,a3
;
	subq.w	#2,d5			do masks overlap?
	bge.s	pmw_uploop		... no
	and.l	d7,d6			... yes, combine masks
;
pmw_uploop
	bsr.s	pmw_suedge		do edge for scroll
;
	move.w	d5,d0			set number of long words to copy
	blt.s	pmw_uplend		one edge only

	moveq	#3,d1			do by four long words
	and.w	d0,d1
	lsr.w	#2,d0
	neg.w	d1
	add.w	d1,d1
	jmp	pmw_urlend(pc,d1.w)
;
pmw_urloop
	move.l	(a4)+,(a5)+		copy all of row
	move.l	(a4)+,(a5)+		copy all of row
	move.l	(a4)+,(a5)+		copy all of row
	move.l	(a4)+,(a5)+		copy all of row
pmw_urlend
	dbra	d0,pmw_urloop
;
	exg	d6,d7			swap masks
	bsr.s	pmw_suedge		do edge
	exg	d7,d6
;
pmw_uplend
	add.l	a2,a4			move to next row
	add.l	a3,a5
	dbra	d3,pmw_uploop		and carry on
	bra.l	pmw_done
;
; do edge for scroll up
;
pmw_suedge
	move.l	(a4)+,d1		get LHS
	and.l	d6,d1			mask edge
	move.l	(a5),d0 		get old screen contents
	not.l	d6			invert mask
	and.l	d6,d0			mask edge
	not.l	d6
	or.l	d0,d1
	move.l	d1,(a5)+		set edge
	rts
;
; move down
;
pmw_down
	move.w	d5,d1			row length
	lsl.w	#2,d1			in bytes

	move.w	a2,d0			find end
	mulu	d3,d0			start of last row
	add.l	d0,a4
	add.w	d1,a4			end of last row

	move.w	a3,d0			ditto for destination
	mulu	d3,d0
	add.l	d0,a5
	add.w	d1,a5

	sub.w	d1,a2			adjust increments
	sub.w	d1,a3

	subq.w	#2,d5			do masks overlap?
	bge.s	pmw_dnloop		... no
	and.l	d6,d7			... yes, combine masks

pmw_dnloop
	bsr.s	pmw_sdedge		do edge

	move.w	d5,d0
	blt.s	pmw_dnlend

	moveq	#3,d1			do by four long words
	and.w	d0,d1
	lsr.w	#2,d0
	neg.w	d1
	add.w	d1,d1
	jmp	pmw_drlend(pc,d1.w)

pmw_drloop
	move.l	-(a4),-(a5)		copy all of row
	move.l	-(a4),-(a5)		copy all of row
	move.l	-(a4),-(a5)		copy all of row
	move.l	-(a4),-(a5)		copy all of row
pmw_drlend
	dbra	d0,pmw_drloop

	exg	d6,d7			swap masks
	bsr.s	pmw_sdedge		do edge
	exg	d7,d6

pmw_dnlend
	sub.l	a2,a4			move to previous row
	sub.l	a3,a5
	dbra	d3,pmw_dnloop		and carry on
	bra.l	pmw_done

; do edges for scroll down

pmw_sdedge
	move.l	-(a4),d1		get RHS
	and.l	d7,d1			mask edge
	move.l	-(a5),d0		get old screen contents
	not.l	d7			invert mask
	and.l	d7,d0			mask edge
	not.l	d7
	or.l	d0,d1
	move.l	d1,(a5) 		set edge
	rts

	page

; panning operation QL mode: pan by 1 to 7 pixels
;
;	d3	row count-1
;	d4	bit shift (1 to 7)
;	d5	long word count in row (-2)
;	d6	first edge mask (word)
;	d7	second edge mask (word)
;	a1	+ or - 4 long word increment
;	a2	row increment (source)
;	a3	row increment (destination)
;	a4	source window address
;	a5	destination window address
;
;
pmw_pan
	cmp.w	d0,d4			is shift greater than lh mask?
	ble.s	pmw_pmask		... no
	addq.w	#2,a4			... yes, move source pointer on
	bset	#31,d4			... and set flag

pmw_pmask
	moveq	#-1,d6			set lh mask
	lsr.w	d0,d6

	moveq	#-1,d7			set rh mask
	tst.w	d2
	beq.s	pmw_pndir		... full
	lsr.w	d2,d7
	not.w	d7

pmw_pndir
	move.w	d5,d1			length of line
	asl.w	#2,d1			... in bytes

	cmp.l	a4,a5			left or right
	bgt.s	pmw_pnrt		... right

	sub.w	d1,a2			reduce increments
	sub.w	d1,a3
	move.w	#4,a1			and go in increasing order
	bra.s	pmw_pnsrow

; pan right should be separate code

pmw_pnrt
	move.w	a2,d0			find end
	mulu	d3,d0			start of last row
	add.l	d0,a4
	add.w	d1,a4			end of last row
	subq.l	#8,a4			!!!!

	move.w	a3,d0			ditto for destination
	mulu	d3,d0
	add.l	d0,a5
	add.w	d1,a5
	subq.l	#4,a5			last in last row

	move.w	d1,d0
	sub.w	a2,d0			adjust increments
	move.w	d0,a2
	sub.w	a3,d1
	move.w	d1,a3

	move.w	#-4,a1			and set go back
	bset	#15,d4			mark swap

	exg	d6,d7			masks the other way

pmw_pnsrow
	subq.w	#2,d5			do masks overlap?
	bge.s	pmw_pnloop		... no
	and.w	d7,d6			... yes

pmw_pnloop
	tst.w	d4			left to right?
	bpl.s	pmw_pnlft
	movep.l 0(a4),d1		get edge (green)
	movep.l 1(a4),d2		get edge (red)
	bra.s	pmw_pnlsft
pmw_pnlft
	tst.l	d4			very left required?
	bpl.s	pmw_leget		... no
	move.b	-2(a4),d1		(we've already added 2 to a4 so it's OK)
	swap	d1			get very edge
	move.b	-1(a4),d2
	swap	d2
pmw_leget
	movep.w 0(a4),d1		get edge (green)
	movep.w 1(a4),d2		get edge (red)

pmw_pnlsft
	ror.l	d4,d1
	ror.l	d4,d2			shifted
	add.l	a1,a4

	and.w	d6,d1			mask edges
	and.w	d6,d2
	movep.w 0(a5),d0		get screen contents (green)
	not.w	d6
	and.w	d6,d0			... masked
	or.w	d1,d0			combined
	movep.w d0,0(a5)		... and written
	movep.w 1(a5),d0		get screen contents (red)
	and.w	d6,d0			... masked
	not.w	d6			
	or.w	d2,d0			combined
	movep.w d0,1(a5)		and written
	add.l	a1,a5

	move.w	d5,d0			set number of long words to copy
	blt.s	pmw_pnlend		one edge only
	bra.s	pmw_prlend

pmw_prloop
	movep.w 0(a4),d1		get next (green)
	ror.l	d4,d1			shifted
	movep.w 1(a4),d2		get next (red)
	ror.l	d4,d2			shifted
	tst.w	d4			right round?
	bpl.s	pmw_prlinc		... no
	swap	d1
	swap	d2
pmw_prlinc
	add.l	a1,a4
	movep.w d1,0(a5)
	movep.w d2,1(a5)		and written
	add.l	a1,a5
pmw_prlend
	rol.l	d4,d1			restore upper word
	rol.l	d4,d2
	tst.w	d4			swap?
	bmi.s	pmw_prldbra
	swap	d1
	swap	d2
pmw_prldbra
	dbra	d0,pmw_prloop

	movep.w 0(a4),d1		get edge (green)
	ror.l	d4,d1			shifted
	movep.w 1(a4),d2		get edge (red)
	ror.l	d4,d2			shifted
	tst.w	d4			swap?
	bpl.s	pmw_pninc		... no
	swap	d1
	swap	d2

pmw_pninc
	add.l	a1,a4

	and.w	d7,d1			mask edges
	and.w	d7,d2
	movep.w 0(a5),d0		get screen contents (green)
	not.w	d7
	and.w	d7,d0			... masked
	or.w	d1,d0			combined
	movep.w d0,0(a5)		... and written
	movep.w 1(a5),d0		get screen contents (red)
	and.w	d7,d0			... masked
	not.w	d7			
	or.w	d2,d0			combined
	movep.w d0,1(a5)		and written
	add.l	a1,a5

pmw_pnlend
	add.l	a2,a4			move to next row
	add.l	a3,a5
	dbra	d3,pmw_pnloop		and carry on

pmw_done
	rts

	end
