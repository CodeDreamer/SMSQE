;	Draw a rectangular block 16 bit colour	v2.00   1998  Tony Tebby
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
;	d4  r	set if extra word on left
;	d5  r	set is extra word on right
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
	swap	d2			; get X origin = word address $$$$
	add.w	d2,d2
	add.w	d2,a1			; base
	swap	d1			; get X size
	sub.w	d1,a2
	sub.w	d1,a2			; increment

	move.l	a1,d0
	lsr.w	#2,d0			; odd word?
	scs	d4
	bcc.s	cnb_swidth		; ... no
	subq.w	#1,d1			; ... yes, take one word off width

cnb_swidth
	asr.w	#1,d1			; number of full long words
	scs	d5			; odd one on right
	bgt.s	cnb_rts 		; at least a long word in middle
	blt.s	cnb_rts4		; none at all at all
	bcs.s	cnb_rts 		; just an odd word on right
	tst.b	d4
	bne.s	cnb_rts 		; just an odd word on left

cnb_rts4
	addq.l	#4,sp			; NOP!!
	moveq	#0,d0
cnb_rts
	rts

;	XOR a block in

bm_xblok
	bsr	cnb_setup		; set up the counters
	beq.s	cnx_one 		; special case for small blocks
	bra.s	cnx_blke

cn_xblock
	bsr	cnb_setuc		; set up the colours and counters
	beq.s	cnx_one 		; special case for small blocks
	bra.s	cnx_blke

cnx_loop
	move.w	d1,d2			; full long words
	tst.b	d4			; leading word?
	beq.s	cnx_lle 		; ... no

	eor.w	d6,(a1)+		; do leading word

	bra.s	cnx_lle
cnx_llp
	eor.l	d6,(a1)+		; fill in some long words
cnx_lle
	dbra	d2,cnx_llp

	tst.b	d5			; trailing word?
	beq.s	cnx_lpe 		; ... no

	swap	d6
	eor.w	d6,(a1)+		; do trailing word
	swap	d6

cnx_lpe
	add.l	a2,a1			; start of next line
	exg	d6,d7			; is other phase of pattern
cnx_blke
	dbra	d3,cnx_loop
cnx_exit
	moveq	#0,d0
	rts

;	Come here if everything fits into a long word

cnx_one
	tst.b	d4			; odd word address
	beq.s	cnx_wole		; ... yes    ; dragon, test and do xor
	tst.b	d5			; odd word at end?
	bne.s	cnx_oneo		; ... yes, offset long word

; one even word

	swap	d6
	bra.s	cnx_wele
cnx_welp
	eor.w	d6,(a1)+		; XOR a line in
	exg	d6,d7			; new line
	add.l	a2,a1			; is here
cnx_wele
	dbra	d3,cnx_welp
	swap	d6
	moveq	#0,d0
	rts

; offset long word

cnx_oneo
	swap	d6
	bra.s	cnx_lwle
cnx_lwlp
	eor.l	d6,(a1)+		; XOR a line in
	exg	d6,d7			; new line
	add.l	a2,a1			; is here
cnx_lwle
	dbra	d3,cnx_lwlp
	swap	d6
	moveq	#0,d0
	rts

; one odd word

cnx_wolp
	eor.w	d6,(a1)+		; XOR a line in
	exg	d6,d7			; new line
	add.l	a2,a1			; is here
cnx_wole
	dbra	d3,cnx_wolp
	moveq	#0,d0
	rts


;	Drop a block in

bm_block
	bsr	cnb_setup		; set up the counters
	beq.s	cnb_one 		; special case for small blocks
	bra.s	cnb_blke

cn_fblock
	bsr	cnb_setuc		; set up the colours and counters
	beq.s	cnb_one 		; special case for small blocks
	bra.s	cnb_blke
*
cnb_loop
	move.w	d1,d2			; full long words
	tst.b	d4			; leading word?
	beq.s	cnb_lle 		; ... no

	move.w	d6,(a1)+		; do leading word

	bra.s	cnb_lle
cnb_llp
	move.l	d6,(a1)+		; fill in some long words
cnb_lle
	dbra	d2,cnb_llp

	tst.b	d5			; trailing word?
	beq.s	cnb_lpe 		; ... no

	swap	d6
	move.w	d6,(a1)+		; do trailing word
	swap	d6

cnb_lpe
	add.l	a2,a1			; start of next line
	exg	d6,d7			; is other phase of pattern
cnb_blke
	dbra	d3,cnb_loop
cnb_exit
	moveq	#0,d0
	rts

;	Come here if everything fits into a long word

cnb_one
	tst.b	d4			; odd word address
	beq.s	cnb_wole		; ... yes    ; dragon, test and do xor
	tst.b	d5			; odd word at end?
	bne.s	cnb_oneo		; ... yes, offset long word

; one even word

	swap	d6
	bra.s	cnb_wele
cnb_welp
	move.w	d6,(a1)+		; drop a line in
	exg	d6,d7			; new line
	add.l	a2,a1			; is here
cnb_wele
	dbra	d3,cnb_welp
	swap	d6
	moveq	#0,d0
	rts

; offset long word

cnb_oneo
	swap	d6
	bra.s	cnb_lwle
cnb_lwlp
	move.l	d6,(a1)+		; drop a line in
	exg	d6,d7			; new line
	add.l	a2,a1			; is here
cnb_lwle
	dbra	d3,cnb_lwlp
	swap	d6
	moveq	#0,d0
	rts

; one odd word

cnb_wolp
	move.w	d6,(a1)+		; drop a line in
	exg	d6,d7			; new line
	add.l	a2,a1			; is here
cnb_wole
	dbra	d3,cnb_wolp
	moveq	#0,d0
	rts

; set colour masks
;
;	d6 cr	stipple / even row colours
;	d7 cr	colours / odd row colours
;
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
	swap	d6
	move.w	d6,d7			 ; odd row is solid
	swap	d6
	rts

cnc_horiz
	swap	d7			 ; odd row is contrast
	move.w	d6,d0
	move.w	d7,d6			 ; odd row is main colour
	move.w	d0,d7
	rts

cnc_check
	swap	d6			 ; even row is different
cnc_solid
cnc_vert
	rts

	end
