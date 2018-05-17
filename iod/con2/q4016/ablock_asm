;	Draw a rectangular block mode 33 with alpha blending v1.00  2016 WL/MK
;
;	The block size and origin are assumed to have been adjusted for
;	correct position within the area to be set.
;
;	Registers:
;		Entry				Exit
;	D0					smashed
;	D1	block size			smashed
;	D2	block origin			smashed
;	D3-D4					smashed
;	D5	alpha weight			smashed
;	D6	stipple 			smashed
;	D7	colour mask			smashed
;	A1	area base address		smashed
;	A2	area row increment		smashed
;
;	All other registers preserved

	section con

	xdef	cn_ablock
	xdef	bm_ablock
	xdef	bm_apixel

	xref	cn_clr2lw

;
; set block parameters
;
;	d1 c	width,height
;	d2 c	origin
;	d5 cr	alpha weight / number of rows
;	a1 cr	base of area / base address of block
;	a2 c	line increment
;
cnb_setuc
	bsr.l	cn_clr2lw		; set colour masks in two long words
cnb_setup
	move.l	a2,d0			; row increment
	mulu	d2,d0			; gives Y offset in area
	add.l	d0,a1			; add it in

	andi.w	#$ff,d5 		; alpha weight
	swap	d5			; ... in upper word
	move.w	d1,d5			; number of rows
	beq.s	cnb_rts4

	btst	#0,d2			; odd line?
	beq.s	cnb_xchk		; no
	exg	d6,d7			; yes, switch pattern lines
cnb_xchk
	swap	d2			; get X origin
	btst	#0,d2
	beq.s	cnb_sxof
	swap	d6
	swap	d7
cnb_sxof
	add.w	d2,d2
	add.w	d2,a1			; base
	swap	d1			; get X size
	tst.w	d1
	beq.s	cnb_rts4
	sub.w	d1,a2
	sub.w	d1,a2			; increment

	subq.w	#1,d1			; d1 and d5 are > 0
	subq.w	#1,d5			; prepare for dbf , nbr of rows
cnb_rts
	rts
cnb_rts4
	addq.l	#4,sp			; NOP!!
	moveq	#0,d0
	rts


sl.reg	reg	a0/a3-a5

;
cn_ablock
	bsr.s	cnb_setuc
	bra.s	xx_ablock
bm_ablock
	bsr.s	cnb_setup
xx_ablock
	cmp.l	d6,d7
	bne	stiple_colour
	move.l	d6,d0
	swap	d0
	cmp.w	d0,d7
	bne	stiple_colour

; This is the faster "solid colour" code
	movem.l sl.reg,-(sp)
	move.w	d1,a3
	move.w	d7,d3
	move.w	d7,d4
	and.w	#%0000000000111110,d3	; split into components
	and.w	#%0000011111000000,d4
	and.l	#%1111100000000000,d7

	eori.l	#$00ff0000,d5		; reverse alpha so we can keep d3/d4/d7 const

	move.w	(a1),a0 		; colour of "previous pixel"
	addq.w	#1,a0			; make sure previous colour <> current colour
*
cnb_loop
	swap	d5			; get alpha weight
	move.w	a3,d6			; nbr of pixels -1
cnb_llp
*
	move.w	(a1),d1 		; current colour on screen
	cmp.w	d1,a0			; same as previous?
	beq.s	set_clr 		; yes, use previous result
	move.w	d1,a0
	move.w	d1,d2
	move.w	d1,d0

	and.w	#%0000000000111110,d0	; split into components
	and.w	#%0000011111000000,d1
	and.l	#%1111100000000000,d2
	sub.w	d3,d0			; dest - source
	sub.w	d4,d1
	sub.l	d7,d2
	asr.l	#8,d2			; lower it, we only have a 16 bit mul
	muls	d5,d0			; (dest - source) * -alpha
	muls	d5,d1
	muls	d5,d2
	asr.l	#8,d0			; normalize
	asr.l	#8,d1
	and.w	#%1111111111111110,d0	; and cut
	and.w	#%1111111111000000,d1
	and.w	#%1111100000000000,d2
	add.w	d3,d0			; source + (dest - source) * -alpha
	add.w	d4,d1
	add.w	d7,d2
	or.w	d1,d0
	or.w	d2,d0

set_clr move.w	d0,(a1)+
	dbra	d6,cnb_llp
	add.l	a2,a1			; start of next line

	swap	d5			; get counter back
	dbra	d5,cnb_loop

	movem.l (sp)+,sl.reg
	moveq	#0,d0
	rts


st.reg	   reg	d1-d7
st.xpix    equ	2			; d1 low
st.scache1 equ	4			; source pixel cache
st.scache2 equ	8
st.dcache1 equ	12			; blended pixel cache
st.dcache2 equ	16
st.col1    equ	20			; d6
st.col2    equ	24			; d7

; Alpha blend stipples... a bit slower
stiple_colour
	movem.l st.reg,-(sp)
	moveq	#0,d6			; colour index

	move.l	(a1),d0
	not.l	d0			; make sure cache is invalid
	move.l	d0,st.scache1(sp)
	move.l	a2,d2
	add.w	d1,d2			; nbr of pixels - 1
	add.w	d1,d2
	move.l	2(a1,d2.l),d0		; first pixel in second line
	not.l	d0			; also invalidate that one
	move.l	d0,st.scache2(sp)
sc_yloop
	andi.b	#$fd,d6 		; start line with first colour
	swap	d5			; get alpha weight back
	swap	d6			; other halve is x counter
	move.w	st.xpix(sp),d6		; nbr of pixels -1
sc_xloop
	swap	d6
	move.w	(a1),d0
	cmp.w	st.scache1(sp,d6.w),d0
	beq.s	sc_usecache

	move.w	d0,st.scache1(sp,d6.w)
	move.w	st.col1(sp,d6.w),d4
	bsr.s	bm_apixel
	move.w	d0,st.dcache1(sp,d6.w)
	bra.s	sc_nextpixel

sc_usecache
	move.w	st.dcache1(sp,d6.w),(a1)+
sc_nextpixel
	eori.b	#2,d6			; other colour
	swap	d6
	dbf	d6,sc_xloop
	swap	d6

	add.l	a2,a1			; start of next line
	eori.b	#4,d6			; swap colours of lines

	swap	d5
	dbf	d5,sc_yloop

	movem.l (a7)+,st.reg
	moveq	#0,d0
	rts


;	Alpha blend a single pixel
;
;	Registers:
;		Entry				Exit
;	D0					pixel value
;	D1					smashed
;	D2					smashed
;	D3					smashed
;	D4	colour				smashed
;	D5	alpha weight			extended to 16 bit
;	D7					smashed
;	A1	screen address			updated to next pixel
;
;	All other registers preserved

bm_apixel
	andi.w	#$ff,d5
	move.w	d4,d0		; source
	move.w	(a1),d3 	; destination
	move.w	d0,d1		; rrrrrggggggbbbbb
	move.w	d0,d2
	move.w	d3,d4
	move.w	d3,d7
	and.w	#%0000000000111110,d0	; split into components
	and.w	#%0000011111000000,d1
	and.l	#%1111100000000000,d2
	and.w	#%0000000000111110,d3	; split into components
	and.w	#%0000011111000000,d4
	and.l	#%1111100000000000,d7

	sub.w	d3,d0		; source - dest
	sub.w	d4,d1
	sub.l	d7,d2
	asr.l	#8,d2		; lower it, we only have a 16 bit mul
	muls	d5,d0		; (source - dest) * alpha
	muls	d5,d1
	muls	d5,d2
	asr.l	#8,d0		; normalize
	asr.l	#8,d1
	and.w	#%1111111111111110,d0	; and cut
	and.w	#%1111111111000000,d1
	and.w	#%1111100000000000,d2
	add.w	d3,d0		; dest + (source - dest) * alpha
	add.w	d4,d1
	add.w	d7,d2
	or.w	d1,d0
	or.w	d2,d0

	move.w	d0,(a1)+
	rts

	end
