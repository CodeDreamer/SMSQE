; Sprite dropping code	  V1.02 			   1985  Tony Tebby
;
;	      1.01  Removed references to MOVEP for Q60/Q40 (FD)
; 2005-03-27  1.02  Extension for empty (solid) mask (MK)

;
;	d0 c s	byte offset / green composite
;	d1 c s	row offset    / red composite
;	d2   s	green sprite
;	d3   s	red sprite
;	d4   s	sprite mask
;	d5 c s	sprite word counter / horizontal mask (high word)
;	d6 c s	row counter
;	d7 c p	right shift (low word) / 16-shift (high word)
;
;	a0   s
;	a3 c s	new pointer to screen
;	a4   s	pointer to sprite pattern
;	a5 c s	pointer to sprite pointers / pointer to sprite mask
;	a6 c p	address increment of screen
;
	section driver
;
	xdef	sp_drop
	xdef	sp_dropi
;
sp_drop
	move.l	a5,a4		set pointer to sprite pattern
	add.l	(a5)+,a4
	move.l	a5,d3
	move.l	(a5),a5
	move.l	a5,d4
	beq.s	spd_nomask	; ... sprite mask ptr is 0
	add.l	d3,a5		; absolute sprite mask ptr
	add.w	d0,a5		; offset to start
spd_nomask
	add.w	d0,a4		; offset to start
;
sp_dropi
;
	move.l	d5,-(sp)	save sprite word counter
	move.l	a5,d0		; any mask?
	beq	sp_nomask
;
sp_line
	move.l	(sp),d5 set counter
;
	moveq	#0,d2		clear the sprite registers
	moveq	#0,d3
	moveq	#0,d4
;
long_word
;	movep.w 0(a4),d2	  set sprite - green / flash
	move.b	(a4),d2
	lsl.w	#8,d2
	move.b	2(a4),d2

;	movep.w 1(a4),d3		     - red / blue
	move.b	1(a4),d3
	lsl.w	#8,d3
	move.b	3(a4),d3

;	movep.w 0(a5),d4		     - mask
	move.b	(a5),d4
	lsl.w	#8,d4
	move.b	2(a5),d4

	addq.l	#4,a4
	addq.l	#4,a5
;
last_word
	ror.l	d7,d2		and move into position
	ror.l	d7,d3
	ror.l	d7,d4
;
	swap	d5		check if this word to be dropped in
	lsl.w	#1,d5
	bcs.s	next_word
;
;	movep.w 0(a3),d0	  get current background from screen
	move.b	(a3),d0
	lsl.w	#8,d0
	move.b	2(a3),d0

;	movep.w 1(a3),d1
	move.b	1(a3),d1
	lsl.w	#8,d1
	move.b	3(a3),d1

	not.w	d4
	and.w	d4,d0		mask out bit where sprite is to go
	and.w	d4,d1
	not.w	d4
	eor.w	d2,d0		eor in the sprite
	eor.w	d3,d1
;	movep.w d0,0(a3)	  put it back into the screen
	move.b	d0,2(a3)
	lsr.w	#8,d0
	move.b	d0,(a3)

;	movep.w d1,1(a3)
	move.b	d1,3(a3)
	lsr.w	#8,d1
	move.b	d1,1(a3)

	addq.l	#4,a3		move screen address on
next_word
	swap	d7
	lsr.l	d7,d2		prepare registers for next go
	lsr.l	d7,d3
	lsr.l	d7,d4
	swap	d7
;
	swap	d5
	subq.w	#1,d5		next long word
	bgt.s	long_word ... there is another one
	blt.s	next_line ... all gone
	clr.w	d2		... no more sprite definition
	clr.w	d3
	clr.w	d4
	bra.s	last_word
;
next_line
	add.w	a6,a3		move address to next line
	dbra	d6,sp_line	next line
exit
	addq.l	#4,sp		remove line counter
	rts

sp_nomask
	move.l	(sp),d5 	set counter
;
	moveq	#0,d2		clear the sprite registers
	moveq	#0,d3
	moveq	#0,d4
;
snm_long_word
;	movep.w 0(a4),d2	  set sprite - green / flash
	move.b	(a4),d2
	lsl.w	#8,d2
	move.b	2(a4),d2

;	movep.w 1(a4),d3		     - red / blue
	move.b	1(a4),d3
	lsl.w	#8,d3
	move.b	3(a4),d3

	move.w	#$ffff,d4

	addq.l	#4,a4
;
snm_last_word
	ror.l	d7,d2		and move into position
	ror.l	d7,d3
	ror.l	d7,d4
;
	swap	d5		check if this word to be dropped in
	lsl.w	#1,d5
	bcs.s	snm_next_word
;
;	movep.w 0(a3),d0	  get current background from screen
	move.b	(a3),d0
	lsl.w	#8,d0
	move.b	2(a3),d0

;	movep.w 1(a3),d1
	move.b	1(a3),d1
	lsl.w	#8,d1
	move.b	3(a3),d1

	not.w	d4
	and.w	d4,d0		mask out bit where sprite is to go
	and.w	d4,d1
	not.w	d4
	eor.w	d2,d0		eor in the sprite
	eor.w	d3,d1
;	movep.w d0,0(a3)	  put it back into the screen
	move.b	d0,2(a3)
	lsr.w	#8,d0
	move.b	d0,(a3)

;	movep.w d1,1(a3)
	move.b	d1,3(a3)
	lsr.w	#8,d1
	move.b	d1,1(a3)

	addq.l	#4,a3		move screen address on
snm_next_word
	swap	d7
	lsr.l	d7,d2		prepare registers for next go
	lsr.l	d7,d3
	lsr.l	d7,d4
	swap	d7
;
	swap	d5
	subq.w	#1,d5		next long word
	bgt.s	snm_long_word	... there is another one
	blt.s	snm_next_line	... all gone
	clr.w	d2		... no more sprite definition
	clr.w	d3
	clr.w	d4
	bra.s	snm_last_word
;
snm_next_line
	add.w	a6,a3		move address to next line
	dbra	d6,sp_nomask	next line

	addq.l	#4,sp		remove line counter
	rts

	end
