* Sprite sprite_wake
*
*	Mode 4
*	+------|-------+
*	|	      a|
*	|	     a |
*	|      a   aa  |
*	|     aa aaa   |
*	-    aaaaaa    -
*	|   aaa aa     |
*	|  aa	a      |
*	| a	       |
*	|a	       |
*	+------|-------+
*
	section sprite
	xdef	sp_wake
	xref	sp_zero

sp_wake
	dc.w	$0100,$0000
	dc.w	14,9,6,4
	dc.l	sc4_sprite_wake-*
	dc.l	sp_zero-*
	dc.l	0
sc4_sprite_wake
	dc.w	$0000,$0404
	dc.w	$0000,$0808
	dc.w	$0202,$3030
	dc.w	$0606,$E0E0
	dc.w	$0F0F,$C0C0
	dc.w	$1D1D,$8080
	dc.w	$3131,$0000
	dc.w	$4040,$0000
	dc.w	$8080,$0000
*
	end
