; Sprite moving code	V1.00	 1985	 Tony Tebby
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
;	a1 c p	pointer to linkage block
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
	xref	sp_dropi
;
sp_draw
; dragon for tricky code see QL
	bsr.l	sp_new
	jmp	sp_dropi

sp_move
; dragon for tricky code see QL
	bsr.l	sp_save
	jmp	sp_dropi
	end
