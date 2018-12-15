* Sprite moving code	V1.00	 1985	 Tony Tebby  QJUMP
*

*	d0 c s	column offset / green composite
*	d1 c s	row offset	/ red composite
*	d2   s	green sprite
*	d3   s	red sprite
*	d4   s	sprite mask
*	d5 c s	sprite word counter
*	d6 c s	row counter / draw up (msb)
*	d7 c p	right shift (low word) / 16-shift (high word)
*
*	a1 c u	pointer to save area
*	a2 c s	old pointer to screen
*	a3 c s	new pointer to screen
*	a4 c u	pointer to sprite pattern
*	a5 c u	pointer to sprite mask
*	a6 c p	address increment of screen 
*
	section driver
*
	xdef	sp_draw
	xdef	sp_move
	xdef	spm_move
spm_move
	xref	sp_new
	xref	sp_save


	include dev8_keys_con

*
sp_draw
	bsr.l	sp_new
	bra.s	sprite
sp_move
	bsr.l	sp_save
sprite
	move.w	d5,-(sp)	  save long word counter
	tst.l	d6		  check which way to draw
	bge.s	sprite_down
*
* sprite moves up
*
su_line
	move.w	(sp),d5   set counter
*
	moveq	#0,d2	  clear the sprite registers
	moveq	#0,d4
*
su_word
	subq.l	#4,a4	  move back
	subq.l	#4,a5
	movep.w 0(a4),d2	  set sprite - green
	movep.w 0(a5),d4		   - mask
su_last
	swap	d7
	rol.l	d7,d2	  and move into position
	rol.l	d7,d4
*
	move.w	-(a1),d0	 get current background from save area
*
	not.w	d4
	and.w	d4,d0	  mask out bit where sprite is to go
	not.w	d4
	eor.w	d2,d0	  eor in the sprite
	move.w	d0,-(a3)	  put it back into the screen
	swap	d7
	lsl.l	d7,d2	  prepare registers for next go
	lsl.l	d7,d4
*
	subq.w	#1,d5	  next word
	bgt.s	su_word   ... there is another one
	blt.s	su_next   ... all gone
	clr.w	d2		  ... no more sprite definition
	clr.w	d4
	bra.s	su_last
*
su_next
	sub.w	a6,a3	  move address to next line
	dbra	d6,su_line	  next line
	bra.s	exit	  remove line counter
*
* move sprite down
*
sprite_down
sd_line
	move.w	(sp),d5   set counter
*
	moveq	#0,d2	  clear the sprite registers
	moveq	#0,d4
*
sd_word
	movep.w 0(a4),d2	  set sprite - green
	movep.w 0(a5),d4		   - mask
	addq.l	#4,a4
	addq.l	#4,a5
sd_last
	ror.l	d7,d2	  and move into position
	ror.l	d7,d4
*
	move.w	(a1)+,d0	  get current background from save area
*
	not.w	d4
	and.w	d4,d0	  mask out bit where sprite is to go
	not.w	d4
	eor.w	d2,d0	  eor in the sprite
	move.w	d0,(a3)+	  put it back into the screen
	swap	d7
	lsr.l	d7,d2	  prepare registers for next go
	lsr.l	d7,d4
	swap	d7
*
	subq.w	#1,d5	  next long word
	bgt.s	sd_word   ... there is another one
	blt.s	sd_next   ... all gone
	clr.w	d2		  ... no more sprite definition
	clr.w	d4
	bra.s	sd_last
*
sd_next
	add.w	a6,a3	  move address to next line
	dbra	d6,sd_line	  next line
exit
	addq.l	#2,sp	  remove line counter
	rts
	end
