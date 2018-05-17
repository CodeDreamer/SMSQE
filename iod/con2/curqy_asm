;	Cursor position enquiry V2.00   1992 Tony Tebby
; 2004-03-27	2.01	ensure compatibility with cn_curspr (wl)
;
;	Returns the size of the active area, and the cursor position in that
;	area, in pixel or character co-ordinates: character co-ordinates
;	are rounded down, and the characters are assumed to be the size
;	of the current cursor.
;
	section con
;
	include 'dev8_keys_con'
;
	xref	cn_dopnl
	xref	cn_curtg
;
	xdef	cn_pixqy
	xdef	cn_chrqy
;
cn_pixqy
	tst.b	sd_nlsta(a0)		; newline required?
	beq.s	cnq_pix
	lea	cnq_pix,a2
	bra.s	cnq_do

cnq_pix
	move.l	sd_xsize(a0),(a1)	; get window size
	move.l	sd_xpos(a0),4(a1)	; cursor position...
	moveq	#0,d0			; no error
	rts
;
cn_chrqy
	tst.b	sd_nlsta(a0)		; newline required?
	beq.s	cnq_chr
	lea	cnq_chr,a2
cnq_do
	tst.b	sd_curf(a0)		; cursor visible?
	ble.s	cnq_nl			;   ... no
	st	sd_curf(a0)		; set to invisible
	jsr	cn_curtg		; remove cursor
	sf	sd_curf(a0)		; show that is ** should ** be visible
cnq_nl
	jsr	cn_dopnl		; ... new line
	jsr	(a2)			; do operation
	tst.b	sd_curf(a0)		; cursor should be visible?
	bgt.l	cn_curtg		;  ... yes, get cursor back
	rts

cnq_chr
	move.l	sd_xinc(a0),d0		; get "character" (cursor) size
	bsr.s	cnq_x2ch		; transform X co-ordinates
	addq.l	#2,a0
	addq.l	#2,a1			; point to Y
	bsr.s	cnq_x2ch		; and transform that
	subq.l	#2,a0
	subq.l	#2,a1
	moveq	#0,d0			; no error
	rts
;
cnq_x2ch
	swap	d0
	move.w	sd_xsize(a0),d2 	; X size of window
	ext.l	d2
	divu	d0,d2			; in character co-ordinates
	move.w	d2,(a1)
;
	move.w	sd_xpos(a0),d2		; X position of cursor
	ext.l	d2
	divu	d0,d2			; in character co-ordinates
	move.w	d2,4(a1)
;
	rts
;
	end
