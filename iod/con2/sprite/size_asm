* Sprite size
*
*	Mode 4
*	+------|-----+
*	|aaaaaaaaaaaa|
*	|aaggggggggaa|
*	|aaggggggggaa|
*	-aaggaaaaaaaa-
*	|aaggaaggggaa|
*	|aaggaaggggaa|
*	|aaaaaaaaaaaa|
*	+------|-----+
*
	section sprite
	xdef	sp_wsize
	xref	sp_zero
	xref	s24_wsize
sp_wsize
	dc.w	$0100,$0000
	dc.w	12,7,6,3
	dc.l	sc4_size-*
	dc.l	sp_zero-*
	dc.l	s24_wsize-*
sc4_size
	dc.w	$FFFF,$F0F0
	dc.w	$C0FF,$30F0
	dc.w	$C0FF,$30F0
	dc.w	$CFFF,$F0F0
	dc.w	$CCFF,$30F0
	dc.w	$CCFF,$30F0
	dc.w	$FFFF,$F0F0
*
	end
