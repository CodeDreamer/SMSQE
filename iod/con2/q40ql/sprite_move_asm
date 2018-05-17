; Sprite moving code	V1.00	 1985	 Tony Tebby
;		1.01	FD	REMOVED REFERENCES TO MOVEP for Q60/Q40
; 2005-02-13	1.02	changed header comment for sprite clipping (wl)
;
;	d0 c s	column offset / green composite
;	d1 c s	row offset    / red composite
;	d2   s	green sprite
;	d3   s	red sprite
;	d4   s	sprite mask
;	d5 c s	sprite word counter
;	d6 c s	row counter / draw up (msb)
;	d7 c p	right shift (low word) / 16-shift (high word)
;
;	a1 c u	pointer to linkage block
;	a2 c s	old pointer to screen
;	a3 c s	new pointer to screen
;	a4 c u	pointer to sprite pattern
;	a5 c u	pointer to sprite mask
;	a6 c p	address increment of screen
;
	section driver
;
	xdef	sp_draw
	xdef	sp_move
	xref	sp_new
	xref	sp_save
;
sp_draw
	bsr.l	sp_new
	bra.s	sprite
sp_move
	bsr.l	sp_save
sprite
	move.w	d5,-(sp)	save sprite word counter
	tst.l	d6		check which way to draw
	bge	sprite_down
;
; sprite moves up
;
su_line
	move.w	(sp),d5 	set counter
;
	moveq	#0,d2		clear the sprite registers
	moveq	#0,d3
	moveq	#0,d4
;
su_word
	subq.l	#4,a4		move back
	subq.l	#4,a5
;	movep.w 0(a4),d2		set sprite - green
	move.b	(a4),d2
	lsl.w	#8,d2
	move.b	2(a4),d2

;	movep.w 1(a4),d3		     - red
	move.b	1(a4),d3
	lsl.w	#8,d3
	move.b	3(a4),d3

;	movep.w 0(a5),d4		     - mask
	move.b	(a5),d4
	lsl.w	#8,d4
	move.b	2(a5),d4

su_last
	swap	d7
	rol.l	d7,d2		and move into position
	rol.l	d7,d3
	rol.l	d7,d4
;
;	movep.w -4(a1),d0	get current background from save area
	move.b	-4(a1),d0
	lsl.w	#8,d0
	move.b	-2(a1),d0

;	movep.w -3(a1),d1
	move.b	-3(a1),d1
	lsl.w	#8,d1
	move.b	-1(a1),d1
;
	not.w	d4
	and.w	d4,d0		mask out bit where sprite is to go
	and.w	d4,d1
	not.w	d4
	eor.w	d2,d0		eor in the sprite
	eor.w	d3,d1
;	movep.w d0,-4(a3)	put it back into the screen
	move.b	d0,-2(a3)
	lsr.w	#8,d0
	move.b	d0,-4(a3)

;	movep.w d1,-3(a3)
	move.b	d1,-1(a3)
	lsr.w	#8,d1
	move.b	d1,-3(a3)

	subq.l	#4,a3		move screen address on
	subq.l	#4,a1		move save area address on
	swap	d7
	lsl.l	d7,d2		prepare registers for next go
	lsl.l	d7,d3
	lsl.l	d7,d4
;
	subq.w	#1,d5		next long word
	bgt.s	su_word 	... there is another one
	blt.s	su_next 	... all gone
	clr.w	d2		... no more sprite definition
	clr.w	d3
	clr.w	d4
	bra.s	su_last
;
su_next
	sub.w	a6,a3		move address to next line
	dbra	d6,su_line	next line
	bra.s	exit		remove line counter
;
; move sprite down
;
sprite_down
sd_line
	move.w	(sp),d5 	set counter
;
	moveq	#0,d2		clear the sprite registers
	moveq	#0,d3
	moveq	#0,d4
;
sd_word
;	movep.w 0(a4),d2		set sprite - green
	move.b	(a4),d2
	lsl.w	#8,d2
	move.b	2(a4),d2

;	movep.w 1(a4),d3		     - red
	move.b	1(a4),d3
	lsl.w	#8,d3
	move.b	3(a4),d3

;	movep.w 0(a5),d4		     - mask
	move.b	(a5),d4
	lsl.w	#8,d4
	move.b	2(a5),d4

	addq.l	#4,a4
	addq.l	#4,a5
sd_last
	ror.l	d7,d2		and move into position
	ror.l	d7,d3
	ror.l	d7,d4
;
;	movep.w 0(a1),d0	  get current background from save area
	move.b	(a1),d0
	lsl.w	#8,d0
	move.b	2(a1),d0

;	movep.w 1(a1),d1
	move.b	1(a1),d1
	lsl.w	#8,d1
	move.b	3(a1),d1
;
	not.w	d4
	and.w	d4,d0		mask out bit where sprite is to go
	and.w	d4,d1
	not.w	d4
	eor.w	d2,d0		eor in the sprite
	eor.w	d3,d1
;	movep.w d0,0(a3)		put it back into the screen
	move.b	d0,2(a3)
	lsr.w	#8,d0
	move.b	d0,(a3)

;	movep.w d1,1(a3)
	move.b	d1,3(a3)
	lsr.w	#8,d1
	move.b	d1,1(a3)

	addq.l	#4,a3		move screen address on
	addq.l	#4,a1		move save area address on
	swap	d7
	lsr.l	d7,d2		prepare registers for next go
	lsr.l	d7,d3
	lsr.l	d7,d4
	swap	d7
;
	subq.w	#1,d5		next long word
	bgt.s	sd_word 	... there is another one
	blt.s	sd_next 	... all gone
	clr.w	d2		... no more sprite definition
	clr.w	d3
	clr.w	d4
	bra.s	sd_last
;
sd_next
	add.w	a6,a3		move address to next line
	dbra	d6,sd_line	next line
exit
	addq.l	#2,sp		remove line counter
	rts
	end
