;	Draw a rectangular block	v2.00   1998  Tony Tebby
;
;	2016 Apr 21  2.01   cn_ablock label added
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
;	D6	stipple 			smashed
;	D7	colour mask			smashed
;	A1	area base address		smashed
;	A2	area row increment		smashed
;
;	All other registers preserved

	section con

	xdef	cn_fblock
	xdef	cn_xblock
	xdef	cn_ablock

	xdef	bm_block
	xdef	bm_xblok

	xdef	cn_maskl
	xdef	cn_maskr

cn.npxwd equ	8
cn.npxlw equ	16
cn.spxbt equ	2
cn.spxlw equ	4

cnb_setuc
	move.l	d7,d6
	swap	d7
	move.w	d6,d7			; odd row colour
	move.l	d6,d0
	swap	d0
	move.w	d0,d6			; even row colour
cnb_setup
	move.l	a2,d3			; row increment
	mulu	d2,d3			; gives Y offset in area
	add.l	d3,a1			; add it in

	btst	#0,d2			; odd line?
	beq.s	cnb_sxof		; no
	exg	d6,d7			; yes, switch pattern lines

cnb_sxof
	swap	d2			; get X origin
	moveq	#cn.npxwd-1,d4		; keep the odd bits at the left
	and.w	d2,d4
	sub.w	d4,d2			; remaining bits...
	lsr.w	#cn.spxbt,d2		; ...become a byte offset
	add.w	d2,a1

	swap	d1			; get X size
	move.w	d4,d0			; odd bits...
	add.w	d1,d0			; ...on right
	moveq	#cn.npxlw-1,d5		; will make rhs mask
	and.w	d0,d5

	add.w	d4,d1			; ...this many pixels taken care of

	exg	d6,d4
	exg	d7,d5			; keep pattern masks safe
	move.l	d6,d0
	jsr	cn_maskl		; make left mask
	move.l	d7,d0
	jsr	cn_maskr		; and right mask
	exg	d6,d4
	exg	d7,d5

	subq.w	#1,d1
	asr.w	#cn.spxlw,d1		; this many unmasked long words + 1
	blt.s	cnb_rts4		; nothing at all!!
	subq.w	#1,d1
	blt.s	cnb_setx
	move.l	d1,d3
	swap	d3			; get longword and line counts
	asl.w	#2,d1
	sub.w	d1,a2			; this much for the unmasked
	subq.l	#8,a2			; this for the masked

cnb_setx
	rts
cnb_rts4
	addq.l	#4,sp			; NOP!!
cnb_exit
	moveq	#0,d0
	rts

;	XOR a block in

bm_xblok
	bsr	cnb_setup		; set up the masks
	blt.s	cnx_nlws		; special case for small blocks
	bra.s	cnx_blke

cn_xblock
	bsr	cnb_setuc		; set up the colours and masks
	blt.s	cnx_nlws		; special case for small blocks
	bra.s	cnx_blke

cnx_loop
	move.l	d6,d0			; get block pattern
	and.l	d4,d0			; as much as we're going to use
	eor.l	d0,(a1)+		; fix this bit

	move.l	d3,d2			; whole long words to do
	bmi.s	cnx_lpe 		; none at all
	swap	d2			; get count to low word
	moveq	#7,d0			; count of long words
	and.w	d2,d0			; ... remainder
	lsr.w	#3,d2			; ... div 8
	add.w	d0,d0
	neg.w	d0
	jmp	cnx_lle(pc,d0.w)
cnx_llp
	eor.l	d6,(a1)+		; fill in eight long words
	eor.l	d6,(a1)+
	eor.l	d6,(a1)+
	eor.l	d6,(a1)+
	eor.l	d6,(a1)+
	eor.l	d6,(a1)+
	eor.l	d6,(a1)+
	eor.l	d6,(a1)+
cnx_lle
	dbra	d2,cnx_llp

	move.l	d6,d0			; get block pattern
	and.l	d5,d0			; as much as we're going to use
	eor.l	d0,(a1)+		; fix this bit

cnx_lpe
	add.l	a2,a1			; start of next line
	exg	d6,d7			; is other phase of pattern
cnx_blke
	dbra	d3,cnx_loop
	bra	cnb_exit

;	Come here if everything fits into a long word

cnx_nlws
	and.l	d5,d4			; only one mask
	and.l	d4,d6			; even line pattern/mask
	and.l	d4,d7			; and odd line
	swap	d1			; get lines to fill in
	bra.s	cnx_nlle
cnx_nllp
	eor.l	d6,(a1) 		; XOR a line in
	exg	d6,d7			; new line
	add.l	a2,a1			; is here
cnx_nlle
	dbra	d1,cnx_nllp
	bra	cnb_exit

;	Drop a block in

bm_block
	bsr	cnb_setup		; set up the masks
	blt.s	cnb_nlws		; special case for small blocks
	bra.s	cnb_blke

cn_ablock
cn_fblock
	bsr	cnb_setuc		; set up the colours and masks
	blt.s	cnb_nlws		; small block, special case
	bra.s	cnb_blke		; and loop around stuffing it in

;	We should now have:
;
;	D3	longword | line count
;	D4	lhs mask
;	D5	rhs mask
;	D6	even pattern
;	D7	odd pattern
;	A1	first longword to modify
;	A2	end-of-line increment
;
cnb_loop
	move.l	d6,d0			; get block pattern
	and.l	d4,d0			; as much as we're going to use
	move.l	d4,d1			; what we're...
	not.l	d1			; ...not going to use...
	and.l	(a1),d1 		; ...is in the area
	or.l	d1,d0
	move.l	d0,(a1)+		; fix this bit

	move.l	d3,d2			; whole long words to do
	bmi.s	cnb_lpe 		; none at all
	swap	d2			; get count to low word
	moveq	#7,d0			; count of long words
	and.w	d2,d0			; ... remainder
	lsr.w	#3,d2			; ... div 8
	neg.w	d0
	add.w	d0,d0
	jmp	cnb_lle(pc,d0.w)
cnb_llp
	move.l	d6,(a1)+		; fill in eight long words
	move.l	d6,(a1)+
	move.l	d6,(a1)+
	move.l	d6,(a1)+
	move.l	d6,(a1)+
	move.l	d6,(a1)+
	move.l	d6,(a1)+
	move.l	d6,(a1)+
cnb_lle
	dbra	d2,cnb_llp

	move.l	d6,d0			; get block pattern
	and.l	d5,d0			; as much as we're going to use
	move.l	d5,d1			; what we're...
	not.l	d1			; ...not going to use...
	and.l	(a1),d1 		; ...is in the area
	or.l	d1,d0
	move.l	d0,(a1)+		; fix this bit

cnb_lpe
	add.l	a2,a1			; start of next line
	exg	d6,d7			; is other phase of pattern
cnb_blke
	dbra	d3,cnb_loop
	bra	cnb_exit

;	Come here if everything fits into a long word

cnb_nlws
	and.l	d5,d4			; only one mask
	move.l	d4,d5
	not.l	d5			; keep an inverted version
	swap	d1			; get lines to fill in
	bra.s	cnb_nlle
cnb_nllp
	move.l	d4,d0			; mask...
	and.l	d6,d0			; ...pattern
	move.l	d5,d3			; and same...
	and.l	(a1),d3 		; ...for area contents
	or.l	d3,d0			; make new contents
	move.l	d0,(a1) 		; and fill in

	exg	d6,d7			; new line
	add.l	a2,a1			; is here
cnb_nlle
	dbra	d1,cnb_nllp
	bra	cnb_exit

;
;	Given a mask number, convert to the required bit mask for the
;	side (left or right).
;
;	Registers:
;		Entry				Exit
;	D0	mask number			smashed
;	D6					mask (left)   \   only one
;	D7					mask (right)  /   set
;
mask_tab
	dc.l	$ffffffff
	dc.l	$7f7fffff
	dc.l	$3f3fffff
	dc.l	$1f1fffff
	dc.l	$0f0fffff
	dc.l	$0707ffff
	dc.l	$0303ffff
	dc.l	$0101ffff
	dc.l	$0000ffff
	dc.l	$00007f7f
	dc.l	$00003f3f
	dc.l	$00001f1f
	dc.l	$00000f0f
	dc.l	$00000707
	dc.l	$00000303
	dc.l	$00000101

cn_maskl
	add.w	d0,d0
	add.w	d0,d0
	move.l	mask_tab(pc,d0.w),d6
	rts

cn_maskr
	add.w	d0,d0
	beq.s	cn_maska
	add.w	d0,d0
	move.l	mask_tab(pc,d0.w),d7
	not.l	d7
	rts
cn_maska
	moveq	#-1,d7
	rts
	end
