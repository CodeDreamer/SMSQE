* Limit sprite to be within screen area 	1.01	     Tony Tebby
*							2005 Marcel Kilgus
*
* 2005-26-01  1.01  Allow sprite clipping in Y direction (MK)
*
*
*	d0   s
*	d1  r	sprite position
*	d2    p pointer to sprite
*	a0   s	= d2
*	a3 c  p pointer to linkage block
*
	section driver
*
	xdef	pt_limit
*
	include dev8_keys_qdos_pt
	include dev8_keys_con
*
pt_limit
	move.l	pt_npos(a3),d1		get new pointer position
	move.l	d2,a0			pointer to sprite
	moveq	#0,d2
*
y_low
	cmp.w	#1,d1			at upper edge?
	bgt.s	y_high			... no
	move.w	#1,d1			... yes, reset y
	moveq	#1<<pt..otop,d2 	on limit
	bra.s	set_y
y_high
	move.w	pt_ssizy(a3),d0 	bottom of screen
	subq.w	#1,d0
	cmp.w	d0,d1			is mouse position below this?
	blt.s	set_y			... no
	move.w	d0,d1			... yes, reset y
	moveq	#1<<pt..obot,d2 	on limit

set_y
	swap	d1
*
	sub.w	pto_xorg(a0),d1 	is origin left of the screen?
	bgt.s	x_high			... no
	clr.w	d1			... yes, reset x
	or.b	#1<<pt..oleft,d2	on limit
	bra.s	set_x
x_high
	move.w	pt_ssizx(a3),d0 	find rhs of screen less width
	sub.w	pto_xsiz(a0),d0
	cmp.w	d0,d1			is lhs of sprite right of this?
	blt.s	set_x			... no
	move.w	d0,d1			... yes, reset x
	or.b	#1<<pt..oright,d2	on limit

set_x
	add.w	pto_xorg(a0),d1 	move pointer back to origin of sprite
	swap	d1
	moveq	#0,d0			set ok
	cmp.l	pt_npos(a3),d1		moved?
	sne	d0			... yes
	or.b	d2,pt_offscr(a3)	set offscreen
	move.l	a0,d2
;;;;;;;;;	 move.l  d1,pt_pos(a3)		 set in position
	move.l	d1,pt_npos(a3)		save new position
	tst.b	d0
	rts
	end
