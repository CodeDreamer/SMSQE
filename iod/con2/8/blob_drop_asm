; Blob dropping code  Aurora Version		 V2.00	 1992	Tony Tebby
;							  2003	Marcel Kilgus
;
;	d0 c s	byte offset / composite colour
;	d1 c s	row offset
;	d2 c s	horizontal pattern repeat / horizontal offset
;	d3 c s	vertical pattern repeat / vertical offset
;	d4   s	blob mask
;	d5 c s	sprite word counter / horizontal mask (high word)
;	d6 c s	row counter
;	d7 c p	right shift (low word) / 16-shift (high word)
;
;	a0   s
;	a1   s	running pointer to pattern
;	a2 c s	pointer to pattern definition / pointer to blob
;	a3 c s	new pointer to screen
;	a4   s	pointer to colour pattern
;	a5 c s	pointer to blob pointers / pointer to pattern mask
;	a6 c p	address increment of screen

	section driver

	xdef	bl_drop

bl_drop
	exg	a2,a5		get blob and pattern in logical order
	addq.l	#4,a2		skip null pointer to colour pattern
	add.l	(a2),a2 	set blob address
	add.w	d0,a2

	move.l	d5,-(sp)	save pointers

	addq.l	#4,a5		skip pattern form
	moveq	#0,d4
	move.w	(a5)+,d4	horizontal repeat
	ext.l	d2
	divu	d4,d2		horizontal offset
	move.l	d2,d5		save offset in msw d5
	move.w	d4,d2
 
	move.w	(a5)+,d0	vertical repeat
	addq.l	#4,a5		skip zeros
	move.l	a5,a4
	add.l	(a5)+,a4	set colour pattern pointer
	tst.l	(a5)		is there a mask?
	add.l	(a5),a5 	(set pattern mask pointer)
	beq.l	solid		no, do solid colour code

	ext.l	d3		y origin
	add.l	d3,d1
	divu	d0,d1		vertical offset
	swap	d1		... is remainder
	move.w	d1,d5		save offset in lsw d5
	mulu	d4,d1		offset to first line in pattern

	move.w	d0,d3
	swap	d3
	move.w	d4,d3		d3  Ny (msw)  Dy (lsw)

	swap	d2		d2  Nx (msw)  Dx (lsw)

	movem.l d2/d3/a4/a5,-(sp) save counters and pattern pointer
	add.w	d1,a4		offset pointers
	add.w	d1,a5
	move.l	a4,a0
	move.l	a5,a1

	swap	d2
	swap	d3
	sub.w	d5,d3		adjust y counter by offset
	swap	d5
	sub.w	d5,d2		and x counter
	move.w	d2,-(sp)

stk_ox	equ	0
stk_nx	equ	2
stk_dx	equ	4
stk_ny	equ	6
stk_dy	equ	8
stk_cp	equ	$a
stk_pm	equ	$e
stk_d5	equ	$12
frame	equ	$16

bl_line
	add.w	stk_dx(sp),a0		set start of pattern
	add.w	stk_dx(sp),a1
	move.l	stk_d5(sp),d5		set counter
	lsl.l	#2,d5			(called in words!!)

bl_word
	move.b	(a2)+,d4		set mask - blob
	and.b	(a1)+,d4		and mask = pattern

	move.b	(a3),d0 		screen
	not.b	d4
	and.b	d4,d0
	not.b	d4
	and.b	(a0)+,d4		pattern
	eor.b	d4,d0
	move.b	d0,(a3)+		update screen

	subq.w	#1,d2			decrement pointer to pattern
	bgt.s	next_d5
	move.w	stk_nx(sp),d2		reset horizontal pattern count
	move.l	a4,a0			and pointers
	move.l	a5,a1
next_d5
	subq.w	#1,d5			next byte
	bgt.s	bl_word 		... there is another one

next_line
	move.w	stk_ox(sp),d2		reset x counter
	add.w	a6,a3			move address to next line
	add.w	stk_dy(sp),a4		and pattern pointer
	add.w	stk_dy(sp),a5
	subq.w	#1,d3
	bgt.s	next_a1
	move.w	stk_ny(sp),d3		reset vertical pattern count
	move.l	stk_cp(sp),a4		and pointers
	move.l	stk_pm(sp),a5
next_a1
	move.l	a4,a0
	move.l	a5,a1
	dbra	d6,bl_line		next line
	add.w	#frame,sp		remove counters
	rts

;	Come here if there's no mask: the pattern is solid

solid
	ext.l	d3		vertical position
	add.l	d3,d1
	divu	d0,d1		vertical offset
	swap	d1		... is remainder
	move.w	d1,d5		save offset in lsw d5
	mulu	d4,d1		offset to first line in pattern

	move.w	d0,d3
	swap	d3
	move.w	d4,d3		d3  Ny (msw)  Dy (lsw)

	swap	d2		d2  Nx (msw)  Dx (lsw)

	movem.l d2/d3/a4/a5,-(sp) save counters and pattern pointer
	add.w	d1,a4		offset pointers
	move.l	a4,a0

	swap	d2
	swap	d3
	sub.w	d5,d3		adjust y counter by offset
	swap	d5
	sub.w	d5,d2		and x counter
	move.w	d2,-(sp)

sbl_line
	add.w	stk_dx(sp),a0		set start of pattern
	move.l	stk_d5(sp),d5		set counter
	lsl.l	#2,d5			(was in words)

sbl_word
	move.b	(a2)+,d4		set mask - blob

	move.b	(a3),d0 		screen
	not.b	d4
	and.b	d4,d0
	not.b	d4
	and.b	(a0)+,d4		pattern
	eor.b	d4,d0
	move.b	d0,(a3)+		update screen

	subq.w	#1,d2			decrement pointer to pattern
	bgt.s	snext_d5
	move.w	stk_nx(sp),d2		reset horizontal pattern count
	move.l	a4,a0			and pointers
	move.l	a5,a1
snext_d5
	subq.w	#1,d5			next word
	bgt.s	sbl_word		... there is another one

snext_line
	move.w	stk_ox(sp),d2		reset x counter
	add.w	a6,a3			move address to next line
	add.w	stk_dy(sp),a4		and pattern pointer
	subq.w	#1,d3
	bgt.s	snext_a1
	move.w	stk_ny(sp),d3		reset vertical pattern count
	move.l	stk_cp(sp),a4		and pointers
snext_a1
	move.l	a4,a0
	dbra	d6,sbl_line		next line
	add.w	#frame,sp		remove counters
	rts
	end
