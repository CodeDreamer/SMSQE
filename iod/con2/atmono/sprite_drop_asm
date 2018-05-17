* Sprite dropping code	  V1.01    1985   Tony Tebby  QJUMP
*
* 2005-03-27  1.01  Extension for empty mask (MK)
* 2015 Jul 25 1.02  Correctly handle sprites with 0 mask
*
*	d0 c s	byte offset / green composite
*	d1 c s	row offset    / red composite
*	d2   s	green sprite
*	d3   s	red sprite
*	d4   s	sprite mask
*	d5 c s	long word counter / horizontal mask (high word)
*	d6 c s	row counter
*	d7 c p	right shift (low word) / 16-shift (high word)
*
*	a0   s 
*	a3 c s	new pointer to screen
*	a4   s	pointer to sprite pattern
*	a5 c s	pointer to sprite pointers / pointer to sprite mask
*	a6 c p	address increment of screen 
*
	section driver
*
	xdef	sp_drop
	xdef	sp_dropi
	xdef	spm_drop
spm_drop
*
sp_drop
	move.l	a5,a4		set pointer to sprite pattern
	add.l	(a5)+,a4
;	 add.l	 (a5),a5	 and sprite mask  NO! 1.02 wl
*
	move.l	a5,d3
	move.l	(a5),a5
	move.l	a5,d4
	beq.s	spd_nomask	; ... sprite mask ptr is 0
	add.l	d3,a5		; absolute sprite mask ptr
	add.w	d0,a5
spd_nomask
	add.w	d0,a4		offset to start
*
sp_dropi
*
	move.l	d5,-(sp)	save long word counter
	move.l	a5,d0
	beq.s	sp_nomask
*
sp_line
	move.l	(sp),d5 	set counter
*
	moveq	#0,d2		clear the sprite registers
	moveq	#0,d4
*
word
	movep.w 0(a4),d2	set sprite - green / flash
	movep.w 0(a5),d4		   - mask
	addq.l	#4,a4
	addq.l	#4,a5
*
last_word
	ror.l	d7,d2		and move into position
	ror.l	d7,d4
*
	swap	d5		check if this word to be dropped in
	lsl.w	#1,d5
	bcs.s	next_word
*
	move.w	(a3),d0 	get current background from screen
*
	not.w	d4
	and.w	d4,d0		mask out bit where sprite is to go
	not.w	d4
	eor.w	d2,d0		eor in the sprite
	move.w	d0,(a3)+	put it back into the screen
next_word
	swap	d7
	lsr.l	d7,d2		prepare registers for next go
	lsr.l	d7,d4
	swap	d7
*
	swap	d5
	subq.w	#1,d5		next word
	bgt.s	word		... there is another one
	blt.s	next_line	... all gone
	clr.w	d2		... no more sprite definition
	clr.w	d4
	bra.s	last_word
*
next_line
	add.w	a6,a3		move address to next line
	dbra	d6,sp_line	next line
exit
	addq.l	#4,sp		remove line counter
	rts

sp_nomask
	move.l	(sp),d5 	set counter
*
	moveq	#0,d2		clear the sprite registers
	moveq	#0,d4
*
snm_word
	movep.w 0(a4),d2	set sprite - green / flash
	move.w	#$ffff,d4
	addq.l	#4,a4
*
snm_last_word
	ror.l	d7,d2		and move into position
	ror.l	d7,d4
*
	swap	d5		check if this word to be dropped in
	lsl.w	#1,d5
	bcs.s	snm_next_word
*
	move.w	(a3),d0 	get current background from screen
*
	not.w	d4
	and.w	d4,d0		mask out bit where sprite is to go
	not.w	d4
	eor.w	d2,d0		eor in the sprite
	move.w	d0,(a3)+	put it back into the screen
	move.w	d2,(a3)+	put it verbatim to screen
snm_next_word
	swap	d7
	lsr.l	d7,d2		prepare registers for next go
	lsr.l	d7,d4
	swap	d7
*
	swap	d5
	subq.w	#1,d5		next word
	bgt.s	snm_word	... there is another one
	blt.s	snm_next_line	... all gone
	clr.w	d2		... no more sprite definition
	clr.w	d4
	bra.s	snm_last_word
*
snm_next_line
	add.w	a6,a3		move address to next line
	dbra	d6,sp_nomask	next line

	addq.l	#4,sp		remove line counter
	rts

	end
