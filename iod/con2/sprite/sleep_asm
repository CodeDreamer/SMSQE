* Sprite ssleep
*
*	Mode 4
*	+------|-------+
*	|aaaaaa        |
*	|   aa	       |
*	|  aa  aaaa    |
*	- aa	 a     -
*	|aaaaaa a   aaa|
*	|      aaaa  a |
*	|	    aaa|
*	+------|-------+
*
	section sprite
	xdef	sp_sleep
	xref	sp_zero
sp_sleep
	dc.w	$0100,$0000
	dc.w	14,7,6,3
	dc.l	sc4_sleep-*
	dc.l	sp_zero-*
	dc.l	shc_sleep-*
sc4_sleep
	dc.w	$FCFC,$0000
	dc.w	$1818,$0000
	dc.w	$3333,$C0C0
	dc.w	$6060,$8080
	dc.w	$FDFD,$1C1C
	dc.w	$0303,$C8C8
	dc.w	$0000,$1C1C
*
shc_sleep
	incbin	'dev8_iod_con2_sprite_sleep_spr'

	end
