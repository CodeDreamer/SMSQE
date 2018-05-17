; Cursor and flashing cursor

	section sprite

	xdef	sp_cursor
	xdef	sp_curflash
	xref	sp_zero

sp_cursor
	dc.w	$0100,$0000
	dc.w	6,10,3,4
	dc.l	sp_curs-*
	dc.l	sp_zero-*
	dc.l	0

sp_curflash
	dc.w	$0100,$0A00
	dc.w	6,10,3,4
	dc.l	sp_curs-*
	dc.l	sp_zero-*
	dc.l	sp_flash-*
sp_flash
	dc.w	$0100,$1400
	dc.w	6,10,3,4
	dc.l	sp_zero-*
	dc.l	sp_zero-*
	dc.l	0

sp_curs
	dc.w	$00FC,$0000
	dc.w	$00FC,$0000
	dc.w	$00FC,$0000
	dc.w	$00FC,$0000
	dc.w	$00FC,$0000
	dc.w	$00FC,$0000
	dc.w	$00FC,$0000
	dc.w	$00FC,$0000
	dc.w	$00FC,$0000
	dc.w	$00FC,$0000

	end
