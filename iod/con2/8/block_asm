;	Draw a rectangular block Aurora 8 bit  v1.00   1998  Tony Tebby
;							2002  Marcel Kilgus
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

;
; set block parameters
;
;	d1 cr	width,height  / number of full long words
;	d2 c	origin
;	d3  r	number of rows
;	d4  r	set if extra byte on left (MSW) / extra bytes on right (LSW)
;	d5  r	mask for extra bytes on right
;	a1 cr	base of area / base address of block
;	a2 c	line increment
;
cnb_setuc
	bsr.l	cn_clr2lw		; set colour masks in two long words
cnb_setup
	move.l	a2,d3			; row increment
	mulu	d2,d3			; gives Y offset in area
	add.l	d3,a1			; add it in

	move.w	d1,d3			; number of rows

	btst	#0,d2			; odd line?
	beq.s	cnb_sxof		; no
	exg	d6,d7			; yes, switch pattern lines

cnb_sxof
	swap	d2			; get X origin
	add.w	d2,a1			; base
	swap	d1			; get X size
	sub.w	d1,a2			; increment

	move.l	a1,d0
	lsr.w	#1,d0			; odd byte?
	scs	d4
	bcc.s	cnb_swidth		; ... no
	subq.w	#1,d1			; ... yes, take one byte off width

cnb_swidth
	swap	d4
	move.w	d1,d4
	andi.w	#3,d4			; odd byte count on right
	moveq	#4,d0
	sub.w	d4,d0			; 4 if no odd bytes, 1-3 = 4 - odd byte count
	lsl.w	#3,d0			; * 8 bits
	moveq	#-1,d5
	lsl.l	d0,d5			; complete mask

	asr.w	#2,d1			; number of full long words
	bgt.s	cnb_rts 		; at least a long word in middle
	blt.s	cnb_rts4		; none at all
	btst	#16,d4
	bne.s	cnb_rts 		; just an odd byte on left
	tst.l	d5
	bne.s	cnb_rts 		; just some odd bytes on right

cnb_rts4
	addq.l	#4,sp			; NOP!!
	moveq	#0,d0
cnb_rts
	rts

;	XOR a block in

bm_xblok
	bsr	cnb_setup		; set up the counters
	bra.s	cnx_blke

cn_xblock
	bsr	cnb_setuc		; set up the colours and counters
	bra.s	cnx_blke

cnx_loop
	move.w	d1,d2			; full long words
	btst	#16,d4			; leading byte?
	beq.s	cnx_lle 		; ... no

	eor.b	d6,(a1)+		; do leading byte

	bra.s	cnx_lle
cnx_llp
	eor.l	d6,(a1)+		; fill in some long words
cnx_lle
	dbra	d2,cnx_llp

	move.l	d5,d2			; trailing bytes?
	beq.s	cnx_lpe 		; ... no

	and.l	d6,d2			; do trailing byte(s)
	eor.l	d2,(a1)
	add.w	d4,a1

cnx_lpe
	add.l	a2,a1			; start of next line
	exg	d6,d7			; is other phase of pattern
cnx_blke
	dbra	d3,cnx_loop
cnx_exit
	moveq	#0,d0
	rts

;	Drop a block in

bm_block
	bsr	cnb_setup		; set up the counters
	bra.s	cnb_blke

cn_fblock
	bsr	cnb_setuc		; set up the colours and counters
	bra.s	cnb_blke
*
cnb_loop
	move.w	d1,d2			; full words
	btst	#16,d4			; leading byte?
	beq.s	cnb_lle 		; ... no

	move.b	d6,(a1)+		; do leading byte

	bra.s	cnb_lle
cnb_llp
	move.l	d6,(a1)+		; fill in some words
cnb_lle
	dbra	d2,cnb_llp

	move.l	d5,d2			; trailing bytes?
	beq.s	cnb_lpe 		; ... no

	and.l	d6,d2			; do trailing byte(s)
	move.l	d5,d0
	not.l	d0
	and.l	(a1),d0
	or.l	d0,d2
	move.l	d2,(a1)
	add.w	d4,a1

cnb_lpe
	add.l	a2,a1			; start of next line
	exg	d6,d7			; is other phase of pattern
cnb_blke
	dbra	d3,cnb_loop
cnb_exit
	moveq	#0,d0
	rts

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
