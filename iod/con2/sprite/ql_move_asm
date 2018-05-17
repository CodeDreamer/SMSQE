* Sprite move
*
*	Mode 4
*	+------|-----+
*	|aaaaaaaa    |
*	|aaggggaa    |
*	|aaggggaa    |
*	|aaggaaaaaaaa|
*	-aaaaaaaaggaa-
*	|    aaggggaa|
*	|    aaggggaa|
*	|    aaaaaaaa|
*	+------|-----+
*
	section sprite
	xdef	sp_wmove
	xref	sp_zero

sp_wmove
	dc.w	$0100,$0000
	dc.w	12,8,6,4
	dc.l	sc4_move-*
	dc.l	sp_zero-*
	dc.l	0
sc4_move
	dc.w	$FFFF,$0000
	dc.w	$C3FF,$0000
	dc.w	$C3FF,$0000
	dc.w	$CFFF,$F0F0
	dc.w	$FFFF,$30F0
	dc.w	$0C0F,$30F0
	dc.w	$0C0F,$30F0
	dc.w	$0F0F,$F0F0
*
	end
