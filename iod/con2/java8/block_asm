;	Draw a rectangular block Aurora 8 bit  v1.00   1998  Tony Tebby
;							2002  Marcel Kilgus
;
;	1.02 (c) W. Lenerz 2016 only cn_block is accelerated
;
;	The block size and origin are assumed to have been adjusted for
;	correct position within the area to be set.
;
;	Registers:
;		Entry				Exit
;	D0					smashed
;	D1	block size			smashed
;	D2	block origin			smashed
;	D3-D5					smashed
;	D6	stipple
;	D7	colour mask			smashed
;	A1	area base address		smashed
;	A2	area row increment		smashed
;
;	All other registers preserved

	section con

	xdef	cn_fblock
	xdef	cn_xblock

	xdef	bm_block
	xdef	bm_xblok

	xdef	cn_clr2lw
	include dev8_keys_java

bm_xblok
	moveq	#jt5.xbkbm,d0
	dc.w	jva.trp5

cn_xblock
	moveq	#jt5.xorbk,d0
	dc.w	jva.trp5

cn_fblock
	moveq	#jt5.fblk,d0
	dc.w	jva.trp5

bm_block
	moveq	#jt5.fbkbm,d0
	dc.w	jva.trp5

;+++
; set colour masks
;
;	d6 cr	stipple / even row colours
;	d7 cr	colours / odd row colours
;---
cn_clr2lw
	add.w	d6,d6
	move.w	cnc_table(pc,d6.w),d0
	move.l	d7,d6			 ; colour in both long words
	jmp	cnc_table(pc,d0.w)

	dc.w	cnc_solid-cnc_table
cnc_table
	dc.w	cnc_1of4-cnc_table
	dc.w	cnc_horiz-cnc_table
	dc.w	cnc_vert-cnc_table
	dc.w	cnc_check-cnc_table

cnc_1of4
	ror.w	#8,d6
	move.b	d6,d7			 ; odd row is solid
	swap	d7
	move.b	d6,d7
	ror.w	#8,d6
	rts

cnc_horiz
	ror.w	#8,d7			 ; odd row is contrast
	move.b	d6,d0
	move.b	d7,d6			 ; odd row is main colour
	swap	d6
	move.b	d7,d6
	move.b	d0,d7
	swap	d7
	ror.w	#8,d7
	move.b	d0,d7
	rts

cnc_check
	ror.w	#8,d6			 ; even row is different
	swap	d6
	ror.w	#8,d6
cnc_solid
cnc_vert
	rts

	end
