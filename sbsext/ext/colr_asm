; Set Colours / Window	       V2.00	  1990  Tony Tebby   QJUMP
;	 2.01	 FD	 REMOVED REFERENCES TO MOVEP for Q60/Q40

;	INK #channel, colour
;	PAPER #channel, colour
;	STRIP #channel, colour
;	BORDER #channel, width, colour
;	BLOCK #channel, area, colour
;	WINDOW #channel, area
;	RECOL #channel, colour pattern
;	COLOUR_QL
;	COLOUR_PAL
;	COLOUR_24
;	COLOUR_NATIVE
;	PALETTE_QL #channel,start, number, 24 bit values
;	PALETTE_8 #channel,start, number, 24 bit values
;	BGCOLOUR_24 #channel, colour
;	BGCOLOUR_QL #channel, colour
;	BGIMAGE #channel, image

	section exten

	xdef	ink
	xdef	paper
	xdef	strip
	xdef	border
	xdef	block
	xdef	window
	xdef	recol
	xdef	colour_QL
	xdef	colour_PAL
	xdef	colour_24
	xdef	colour_native
	xdef	palette_ql
	xdef	palette_8
	xdef	bgcolour_ql
	xdef	bgcolour_24
	xdef	bgimage

	xref	ext_lfile

	xref	ut_chan1		 ; get channel default #1
	xref	ut_gtint		 ; get integers
	xref	ut_gtlin		 ; get long integers
	xref	ut_gtin1		 ; get one integer
	xref	ut_gxli1		 ; get exactly one long integer
	xref	ut_trp3r
	xref	ut_trap3

	xref	gu_rchp

	include 'dev8_keys_qdos_io'
	include 'dev8_keys_err'
	include 'dev8_keys_sbasic'

colour_QL
	moveq	#0,d0
	bra.s	colrm_set

colour_PAL
	moveq	#iow.papp,d0
	bra.s	colrm_set

colour_24
	moveq	#iow.papt,d0
	bra.s	colrm_set

colour_native
	moveq	#iow.papn,d0

colrm_set
	move.b	d0,sb_colrm(a6)
	moveq	#0,d0
colr_rts1
	rts

palette_ql
	moveq	#iow.palq,d7
	bra.s	palette_set
palette_8
	moveq	#iow.palt,d7
palette_set
	jsr	ut_chan1	; get channel ID
	bne	colr_rts1
	jsr	ut_gtin1	; get first
	bne	colr_rts1
	addq.w	#8,a3
	move.w	(a6,a1.l),d5
	jsr	ut_gtlin
	bne.s	colr_rts1
	lea	(a6,a1.l),a2
	moveq	#0,d2
	move.w	d3,d2
	bra.s	pal_eloop
pal_loop
	move.l	(a2),d0
	lsl.l	#8,d0		; 24 bits in msbits
	move.l	d0,(a2)+
pal_eloop
	dbra	d3,pal_loop

	moveq	#0,d1
	move.w	d5,d1
	move.l	d7,d0
	bra.l	colr_trs

bgimage
	jsr	ut_chan1	; get channel id
	bne	colr_rts1	; ... oops
	move.l	a0,-(sp)
	jsr	ext_lfile	; load file
	move.l	(sp)+,a0
	bne.s	colr_rts1
	move.l	a2,a1
	moveq	#-1,d1
	moveq	#0,d2
	moveq	#iop.wpap,d0
	jsr	ut_trap3	; do io call
	move.l	a2,a0
	jmp	gu_rchp 	; return bit of heap

bgcolour_ql
	jsr	ut_chan1	; get channel id
	bne.s	colr_rts	; oops
	moveq	#iop.wpap,d7	; set wallpaper colour
	moveq	#-1,d4		; no image
	bra.s	colr_get

bgcolour_24
	jsr	ut_chan1	; get channel id
	bne.s	colr_rts	; oops
	jsr	ut_gxli1	; one long integer
	bne.s	colr_rts
	move.l	(a6,a1.l),d1
	lsl.l	#8,d1
	moveq	#-1,d2
	moveq	#iop.wpap,d0	; set wallpaper colour
	bra.s	colr_trs

ink
	moveq	#iow.sink,d7	; set ink
	bra.s	colr_chn
paper
	moveq	#iow.spap,d7	; set paper
	bsr.s	colr_chn
	moveq	#iow.sstr,d7	; and strip
	bra.s	colr_tr3
strip
	moveq	#iow.sstr,d7
colr_chn
	jsr	ut_chan1	; get channel id
	bne.s	colr_rts	; ... oops

	move.b	sb_colrm(a6),d4 ; extended colour definitions?
	bne.s	colr_lint	; ... yes, long integers

colr_get
	jsr	ut_gtint	; get integers
	bne.s	colr_rts

colr_set
	moveq	#0,d1
	move.w	(a6,a1.l),d1	; first colour
	subq.b	#1,d3		; just one?
	blt.s	colr_ipar
	beq.s	colr_tr3	; ... yes

	move.w	2(a6,a1.l),d0	; two colours
	eor.w	d1,d0
	lsl.w	#3,d0
	add.w	d0,d1
	subq.b	#2,d3		; a pattern?
	blt.s	colr_chk
	bgt.s	colr_ipar

	move.w	4(a6,a1.l),d0	; add pattern
	lsl.w	#6,d0
	add.w	d0,d1
	bra.s	colr_tr3

colr_chk
	or.b	#%11000000,d1	; check pattern

colr_tr3
	move.l	a1,-(sp)
	move.l	d4,d2		; extra key
	move.l	a4,a1		; pointer
	move.l	d7,d0		; operation
	bsr.s	colr_trs
	move.l	(sp)+,a1
	rts

colr_trs
	jmp	ut_trp3r	; do trap

colr_ipar
	moveq	#err.ipar,d0
colr_rts
	rts

colr_lint
	bsr.s	colr_xcolr	; get colours in d1/d5/d6
	cmp.b	#iow.spap,d7	; set paper?
	bne.s	colr_xdo	; no

	movem.l d1/d3/d4/d5/d6/d7,-(sp)
	bsr.s	colr_xdo	; yes, do paper
	movem.l (sp)+,d1/d3/d4/d5/d6/d7
	addq.w	#1,d7		; and strip
	addq.l	#4,sp		; skip return to QL strip code

colr_xdo
	sub.b	#iow.spap,d7
	add.b	d4,d7		; corrected operation

	moveq	#-1,d4		; no stipple
	cmp.b	#1,d3
	blt.s	colr_ipar
	beq.s	colr_tr3	; just main colour

	bsr.s	colr_tr3	; set main colour

	move.l	d5,d1		; contrast
	moveq	#3,d4		; assume check
	cmp.b	#3,d3
	blt.s	colr_tr3	; .. it is
	bgt.s	colr_ipar
	move.l	d6,d4		; set stipple
	bra.s	colr_tr3

colr_xcolr
	jsr	ut_gtlin	; get the parameters
	bne.s	colr_rts4
	cmp.b	#iow.papt,d4
	bne.s	colr_xnt
	movem.l (a6,a1.l),d1/d5/d6	 ; colours
	lsl.l	#8,d1		; fiddled for true colour
	lsl.l	#8,d5
	rts

colr_xnt
	movem.l (a6,a1.l),d1/d5/d6	 ; colours
	rts

colr_rts4
	addq.l	#4,sp
	rts

border
	moveq	#iow.defb,d7
	jsr	ut_chan1	; get channel id
	bne.s	colr_rts	; ... oops

	moveq	#0,d4
	cmp.l	a3,a5		; any width
	beq.s	border_trans	; ... no
	jsr	ut_gtin1	; one int
	bne.s	colr_rts
	move.w	(a6,a1.l),d4	; width
	addq.l	#8,a3		; next param
	cmp.l	a3,a5		; any colour?
	bne.s	border_colr	; ... yes
border_trans
	moveq	#$ffffff80,d1
	bra	colr_tr3	; ... no, transparent

border_colr
	move.b	sb_colrm(a6),d0
	beq	colr_get
	moveq	#iow.borp-iow.papp,d7
	add.b	d0,d7		; extended colour operation

	swap	d4
	move.b	d0,d4
	bsr	colr_xcolr	; colours in d1/d5/d6
	swap	d4		; recover border width

	move.w	d3,d0
	move.w	d4,d3
	moveq	#-1,d4		; scrumple register usage
	subq.w	#2,d0		; just one colour?
	bge.s	border_xstip	; ... no
	move.w	d3,d4		; set single colour
	bra	colr_tr3

border_xstip
	bgt.s	border_xmain	; stipple given - do main colour
	moveq	#3,d6		; checkerboard
border_xmain
	bsr	colr_tr3

	move.l	d5,d1		; contrast
	move.w	d6,d4		; stipple
	swap	d4
	move.w	d3,d4		; width
	bra	colr_tr3


block
	moveq	#iow.blok,d7
	jsr	ut_chan1	; get channel id
	bne	colr_rts	; ... oops

	move.b	sb_colrm(a6),d4
	bne.s	block_xcolr	; extended colour definition

	jsr	ut_gtint	; and params
	bne	colr_rts
	subq.w	#4,d3		; window gone
	ble	colr_ipar

	move.l	a1,a4		; point to window def
	addq.l	#8,a1		; and skip it

	bra	colr_set	; set colour

block_xcolr
	move.b	d4,d0
	sub.b	#iow.papp,d0
	lsr.b	#2,d0
	moveq	#iow.blkp,d7
	add.b	d0,d7		; call for this mode

	move.l	a5,a4
	lea	4*8(a3),a5	; get just the block
	cmp.l	a5,a4
	ble	colr_ipar	; no colour
	jsr	ut_gtint	; and params
	bne	colr_rts
	move.l	a5,a3
	move.l	a4,a5		; next parameters
	move.l	a1,a4		; point to window def

	bsr	colr_xcolr	; colours in d1/d5/d6
	subq.w	#3,d3		; how many colours
	bgt	colr_ipar
	beq.s	block_xcset
	addq.w	#1,d3
	beq.s	block_xccheck
	move.l	d1,d5
	moveq	#-1,d6
	bra.s	block_xcset
block_xccheck
	moveq	#3,d6
block_xcset
	move.l	sb_buffb(a6),a2
	add.l	a6,a2
	movem.l d1/d5/d6,(a2)	; set the colour parameters
	bra	colr_tr3

window
	jsr	ut_chan1	; get channel id
	bne	colr_rts	; ... oops

	clr.w	ch_colmn(a6,d4.l); reset pos on line

	jsr	ut_gtint	; and params
	subq.w	#4,d3		; window
	bne	colr_ipar

	moveq	#iow.defw,d0
	moveq	#0,d2		; no border
	bra	colr_trs	; set window

recol
	jsr	ut_chan1	; get channel id
	bne	colr_rts	; ... oops

	jsr	ut_gtint	; and params
	subq.w	#8,d3		; eight of them
	bne	colr_ipar

	add.l	a6,a1		; compress colours
;	movep.l 9(a1),d0
	move.b	9(a1),d0
	lsl.l	#8,d0
	move.b	11(a1),d0
	lsl.l	#8,d0
	move.b	13(a1),d0
	lsl.l	#8,d0
	move.b	15(a1),d0

	move.l	d0,12(a1)
;	movep.l 1(a1),d0
	move.b	1(a1),d0
	lsl.l	#8,d0
	move.b	3(a1),d0
	lsl.l	#8,d0
	move.b	5(a1),d0
	lsl.l	#8,d0
	move.b	7(a1),d0

	move.l	d0,8(a1)
	addq.l	#8,a1

	moveq	#iow.rclr,d0
	jmp	ut_trap3	; recolour

	end
