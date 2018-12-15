* Update the save area for a sprite  V1.00     1985  Tony Tebby  QJUMP

* 2015 Jul 31  1.01  corrected sp_remove
* 2015 Jul 25  1.01  use pt_spsav wherever needed
*
*
*	d0 c s	old-new horizontal long word pointer
*	d1 c s	old-new row pointer
*	d2   s
*	d3   s
*	d4   s
*	d5 c p	width-1 of save area
*	d6 c p	height-1 of save area / draw up (msb)
*	d7   p	horizontal shift for sprite
*
*	a0   s
*	a1 c u	pointer to save area (actually, pointer to linkage area)
*	a2 c s	old pointer to screen
*	a3 c u	new pointer to screen
*	a4 c u	pointer to sprite pattern
*	a5 c u	pointer to sprite mask
*	a6 c u	address increment of screen 
*
	section driver
*
	xdef	sp_save
	xdef	sp_new
	xdef	sp_remove

	include dev8_keys_con

sp_save
	cmp.w	pt_spszy(a1),d6 save area height different from sprite height?
	bne	do_all		yes, do it all new

	ext.l	d6		  set normal direction
	cmp.l	a2,a3	  find out which direction to move
	bgt.l	move_down	  ... downwards
	beq.l	exit	  ... not at all
*
* move up
*
	move.w	d6,d2	  set diff between height and movement
	sub.w	d1,d2	  is movement>=height?
	blt.l	do_all	  ... yes
	ext.l	d0		  check horiz movement (save sign of move)
	bge.s	u_chk_horiz
	neg.w	d0		  and make positive
u_chk_horiz
	cmp.w	d0,d5	  is movement>=width?
	blt.l	do_all	  ... yes
*
* now move all the pointers to the end of the sprite
*
	move.l	pt_spsav(a1),a1 save area pointer     ********
	bset	#31,d6	  flag reverse direction
	move.w	d5,d4	  width-1
	move.w	d6,d3	  height-1
	addq.w	#1,d3	  height
	mulu	d3,d4	  height*(width-1)
	add.w	d4,d4	  *2
	add.w	d4,d4	  *4
	add.w	d4,a4	  added to sprite pattern
	add.w	d4,a5	  added to sprite mask
*
	lsl.w	#2,d3	  4*height
	add.w	d3,d4	  4*height*width
	lsr.w	#1,d4	  2* ...
	add.w	d4,a1	  added to save area
*
	lsr.w	#2,d3	  height
	exg	a6,d0
	swap	d0
	mulu	d0,d3	  row length*height
	swap	d0
	sub.w	d0,d3	  ... less one rowish
	exg	a6,d0
	add.w	d3,a2	  added to old and new addresses
	add.w	d3,a3
*
	move.l	a1,a0	  set temporary address register
	move.l	a3,-(sp)	  save new pointer to screen
	move.l	a1,-(sp)	  save save area pointer
*
* restore the top bit of the old area
*
	move.w	d1,d3	  copy old rows to the screen
	bra.s	u_rs_rend
u_rs_row
	move.w	d5,d4	  length of row
u_rs_rword
	move.w	-(a0),-(a2)	  copy
	dbra	d4,u_rs_rword	... a row
*
	sub.w	a6,a2	  ... next row
u_rs_rend
	dbra	d3,u_rs_row
	page
*
* now shuffle the save area across (d2 is height-vertical movement)
*
	swap	d1		  save the vertical movement
	move.w	d5,d1	  the width
	sub.w	d0,d1	  ... less horizontal movement
	move.w	d1,d3
	addq.w	#1,d3
	lsl.w	#1,d3	  ... in bytes $$$$
	add.w	d3,a6	  to increase the screen increment
	tst.l	d0		  is it right or left?
	bmi.s	up_right
*
	sub.w	d3,a3	  adjust the pointer to the new screen
*
* now move up and left
*
ul_move
	move.w	d0,d4
	bra.s	ul_rs_cend
ul_rs_cword
	move.w	-(a0),-(a2)	  restore the right hand column
ul_rs_cend
	dbra	d4,ul_rs_cword
*
	move.w	d1,d4
ul_mv_cword
	move.w	-(a0),-(a1)	  move within the save area
	dbra	d4,ul_mv_cword
*
	move.w	d0,d4
	bra.s	ul_sv_cend
ul_sv_cword
	move.w	-(a3),-(a1)	  and save the new left hand column
ul_sv_cend
	dbra	d4,ul_sv_cword
*
	sub.w	a6,a2	  update the screen pointers
	sub.w	a6,a3
	dbra	d2,ul_move
	add.w	d3,a3	  re-adjust the new screen pointer
	bra.s	up_rest
	page
*
* move up and right
*
up_right
	sub.w	d3,a2	  adjust the old screen pointer
ur_move
	move.w	d0,d4
	bra.s	ur_sv_cend
ur_sv_cword
	move.w	-(a3),-(a1)	  save the right hand column
ur_sv_cend
	dbra	d4,ur_sv_cword
*
	move.w	d1,d4
ur_mv_cword
	move.w	-(a0),-(a1)	  move within the save area
	dbra	d4,ur_mv_cword
*
	move.w	d0,d4
	bra.s	ur_rs_cend
ur_rs_cword
	move.w	-(a0),-(a2)	  and restore the old left hand column
ur_rs_cend
	dbra	d4,ur_rs_cword
*
	sub.w	a6,a2	  update the screen pointers
	sub.w	a6,a3
	dbra	d2,ur_move
*
	add.w	d3,a2	  restore the old screen pointer
*
up_rest
	swap	d1		  restore vertical move
	sub.w	d3,a6	  ... address increment
	page
*
* now save the bottom of the new area
*
	bra.s	u_sv_rend
u_sv_row
	move.w	d5,d4
u_sv_rword
	move.w	-(a3),-(a1)	  copy
	dbra	d4,u_sv_rword	... a row
*
	sub.w	a6,a3	  next row
u_sv_rend
	dbra	d1,u_sv_row
	bra.l	move_exit
*
move_down
	neg.w	d1		  make movement positive
	move.w	d6,d2	  set diff between height and movement
	sub.w	d1,d2	  is movement>=height?
	blt.l	do_all	  ... yes
	ext.l	d0		  check horiz movement (save sign of move)
	bge.s	d_chk_horiz
	neg.w	d0		  and make positive
d_chk_horiz
	cmp.w	d0,d5	  is movement>=width?
	blt.l	do_all	  ... yes
;
	move.l	pt_spsav(a1),a1 save area pointer   ***********
	move.l	a1,a0	  set temporary address registers
	move.l	a3,-(sp)	  save new pointer to screen
	move.l	a1,-(sp)	  save save area pointer
*
* restore the top bit of the old area
*
	move.w	d1,d3	  copy old rows to the screen
	bra.s	d_rs_rend
d_rs_row
	move.w	d5,d4	  length of row
d_rs_rword
	move.w	(a0)+,(a2)+	  copy
	dbra	d4,d_rs_rword	... a row
*
	add.w	a6,a2	  ... next row
d_rs_rend
	dbra	d3,d_rs_row
	page
*
* now shuffle the save area across (d2 is height-vertical movement)
*
	swap	d1		  save the vertical movement
	move.w	d5,d1	  the width
	sub.w	d0,d1	  ... less horizontal movement
	move.w	d1,d3
	addq.w	#1,d3
	lsl.w	#1,d3	  ... in bytes
	add.w	d3,a6	  to increase the screen increment
	tst.l	d0		  is it left or right?
	bpl.s	down_left
*
	add.w	d3,a3	  adjust the pointer to the new screen
*
* now move down and right
*
dr_move
	move.w	d0,d4
	bra.s	dr_rs_cend
dr_rs_cword
	move.w	(a0)+,(a2)+	  restore the left hand column
dr_rs_cend
	dbra	d4,dr_rs_cword
*
	move.w	d1,d4
dr_mv_cword
	move.w	(a0)+,(a1)+	  move within the save area
	dbra	d4,dr_mv_cword
*
	move.w	d0,d4
	bra.s	dr_sv_cend
dr_sv_cword
	move.w	(a3)+,(a1)+	  and save the new right hand column
dr_sv_cend
	dbra	d4,dr_sv_cword
*
	add.w	a6,a2	  update the screen pointers
	add.w	a6,a3
	dbra	d2,dr_move
	sub.w	d3,a3	  re-adjust the new screen pointer
	bra.s	down_rest
	page
*
* move down and left
*
down_left
	add.w	d3,a2	  adjust the old screen pointer
dl_move
	move.w	d0,d4
	bra.s	dl_sv_cend
dl_sv_cword
	move.w	(a3)+,(a1)+	  save the left hand column
dl_sv_cend
	dbra	d4,dl_sv_cword
*
	move.w	d1,d4
dl_mv_cword
	move.w	(a0)+,(a1)+	  move within the save area
	dbra	d4,dl_mv_cword
*
	move.w	d0,d4
	bra.s	dl_rs_cend
dl_rs_cword
	move.w	(a0)+,(a2)+	  and restore the old right hand column
dl_rs_cend
	dbra	d4,dl_rs_cword
*
	add.w	a6,a2	  update the screen pointers
	add.w	a6,a3
	dbra	d2,dl_move
*
	sub.w	d3,a2	  restore the old screen pointer
*
down_rest
	swap	d1		  restore vertical move
	sub.w	d3,a6	  ... address increment
	page
*
* now save the bottom of the new area
*
	bra.s	d_sv_rend
d_sv_row
	move.w	d5,d4
d_sv_rword
	move.w	(a3)+,(a1)+	  copy
	dbra	d4,d_sv_rword	... a row
*
	add.w	a6,a3	  next row
d_sv_rend
	dbra	d1,d_sv_row
*
move_exit
	move.l	(sp)+,a1	  restore save area pointer
	move.l	(sp)+,a3	  restore new pointer to screen
	rts
	page
do_all
	bsr.s	sp_remove
*
* entry to create a new save area
*
sp_new
	ext.l	d6		  mark direction as normal
	move.w	d6,d3						*****
	move.w	d6,pt_spszy(a1) remember height of save area	*****
	move.l	pt_spsav(a1),a1 save area pointer		*****
	move.l	a3,-(sp)
	move.l	a1,-(sp)
new_row
	move.w	d5,d4
new_rword
	move.w	(a3)+,(a1)+	  copy from new area
	dbra	d4,new_rword	  ... a row
*
	add.w	a6,a3	  ... next row
	dbra	d3,new_row	  ... any left?
	bra.s	move_exit
*
* entry to remove sprite from screen
*
sp_remove
	move.l	a1,-(sp)
	move.w	pt_spszy(a1),d3 height of save area
	move.l	pt_spsav(a1),a1 save area pointer		*****
rem_row
	move.l	d5,d4
rem_rword
	move.w	(a1)+,(a2)+	  copy into old area
	dbra	d4,rem_rword	  ... a row
*
	add.w	a6,a2	  ... next row
	dbra	d3,rem_row
*
	move.l	(sp)+,a1
	rts
*
exit
	move.l	pt_spsav(a1),a1 save area pointer		*****
	rts

	end

		  
