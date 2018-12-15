; Update the save area for a sprite  V1.01     1985  Tony Tebby
;						2005  Marcel Kilgus
;
; 2005-01-25  1.01  Changes for mouse pointer clipping (MK)
;
;	d0   s
;	d1   s
;	d2   s
;	d3   s
;	d4   s
;	d5 c p	width-1 of save area
;	d6 c p	height-1 of save area / draw up (msb)
;	d7   p	horizontal shift for sprite
;
;	a0   s
;	a1 c p	pointer to linkage block
;	a2 c s	old pointer to screen (sp_remove)
;	a3 c u	new pointer to screen (sp_new)
;	a4 c u	pointer to sprite pattern
;	a5 c u	pointer to sprite mask
;	a6 c u	address increment of screen
;
	section driver
;
	include dev8_keys_con
;
	xdef	sp_save
	xdef	sp_new
	xdef	sp_remove
;
sp_save
; dragon for tricky code see ql
	bsr.s	sp_remove
;
; entry to create a new save area
;
sp_new
	move.l	a3,-(sp)
	move.l	a1,-(sp)
	move.w	d6,d3
	move.w	d6,pt_spszy(a1)     remember save area height
	move.l	pt_spsav(a1),a1     ptr to save area
new_row
	move.w	d5,d4
new_rword
	move.l	(a3)+,(a1)+	copy from new area
	dbra	d4,new_rword	... a row
;
	lea	-4(a3,a6.w),a3	... next row
	dbra	d3,new_row	... any left?

	move.l	(sp)+,a1
	move.l	(sp)+,a3
	rts
;
; entry to remove sprite from screen
;
sp_remove
	move.l	a1,-(sp)
	move.w	pt_spszy(a1),d3 height of saved sprite
	move.l	pt_spsav(a1),a1 ptr to save area
rem_row
	move.l	d5,d4
rem_rword
	move.l	(a1)+,(a2)+	copy into old area
	dbra	d4,rem_rword	... a row
;
	lea	-4(a2,a6.w),a2	... next row
	dbra	d3,rem_row
;
	move.l	(sp)+,a1
;
exit
	rts
	end
