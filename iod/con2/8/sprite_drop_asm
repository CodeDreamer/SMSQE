; Sprite dropping code	  V1.00    1985   Tony Tebby
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
	add.l	(a5),a5 	and sprite mask
;
	add.w	d0,a4		offset to start
	add.w	d0,a5
;
sp_dropi
;
sp_line
	move.l	d5,d4		set counter
;
long_word
	move.l	(a5)+,d0
	not.l	d0
	and.l	(a3),d0
	move.l	(a4)+,d1
	eor.l	d1,d0
	move.l	d0,(a3)+
	subq.w	#1,d4		next long word
	bgt.s	long_word	... there is another one

next_line
	add.w	a6,a3		move address to next line
	dbra	d6,sp_line	next line
exit
	rts
	end
