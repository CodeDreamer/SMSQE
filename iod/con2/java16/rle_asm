; Decompress RLE compressed data   V1.00		   2002 Marcel Kilgus

	section driver

	xdef	pt_derle

	xref	gu_achpp

;+++
; Decompressed RLE compressed data
;
; RLE header:	0  'RLEx' with x either '1', '2' or '4' (item size in bytes)
;		4  size of uncompressed data
;		8+ compressed data
;
; Algorithm can work on an item size of byte, word and longword (s.above)
;
; The RLE data stream always consists of one byte and some items following.
; If the leading byte is in range of 0<=x<128 then x+1 uncompressed items
; are following the byte. Otherwise the next item is to be taken 257-x
; times.
;
; A new buffer is allocated if a6 was 0 on entering
;
;	d0  r	< 0 error, > 0 size of data
;	a2 c  p start of compressed data
;	a6 cr	address of buffer with uncompressed data
;---
regs	reg	d1-d3/a0/a2

pt_derle
	movem.l regs,-(sp)
	moveq	#0,d2
	move.l	(a2)+,d1		; ID
	cmpi.l	#'RLE1',d1
	beq.s	prle_rle
	addq.w	#2,d2
	cmpi.l	#'RLE2',d1
	beq.s	prle_rle
	addq.w	#2,d2
	cmpi.l	#'RLE4',d1
	bne.s	prle_norle
prle_rle
	move.l	a6,d0			; buffer given?
	bne.s	prle_buffergiven
	move.l	(a2),d0 		; space needed for uncompressed data
	jsr	gu_achpp		; allocated
	bmi.s	prle_exit
	move.l	a0,a6
prle_buffergiven
	move.l	a6,a0
	move.l	(a2)+,d1		; uncompressed size
	move.l	d1,-(sp)
	move.w	pt_decomp(pc,d2.w),d2
	jsr	pt_decomp(pc,d2.w)
	move.l	(sp)+,d0		; return size of data
prle_exit
	movem.l (sp)+,regs
	rts

prle_norle
	moveq	#0,d0
	bra.s	prle_exit

pt_decomp
	dc.w	prle_1-pt_decomp
	dc.w	prle_2-pt_decomp
	dc.w	prle_4-pt_decomp

; Uncompress byte sized data
prle1_loop
	moveq	#0,d0
	move.b	(a2)+,d0
	bmi.s	prle1_comp		; compressed data
	sub.l	d0,d1			; subtract from total data size
	bcs.s	prle_rts		; emergency exit
	subq.l	#1,d1			; it's x+1 actually
prle1_ucloop
	move.b	(a2)+,(a0)+		; just copy uncompressed data
	dbf	d0,prle1_ucloop
	bra.s	prle_1
prle1_comp
	move.l	#257,d2
	sub.l	d0,d2
	sub.l	d2,d1			; subtract from total size
	bcs.s	prle_rts		; emergency exit
	subq.l	#1,d2			; dbf loop makes it one less
	move.b	(a2)+,d0
prle1_cloop
	move.b	d0,(a0)+		; insert item d2 times
	dbf	d2,prle1_cloop
prle_1
	tst.l	d1
	bhi.s	prle1_loop
prle_rts
	rts




; NOTE : this code necessarily presumes that the processor used is at least
; an 68020 as at some stage there may be word sized MOVEs
; from uneven addresses


; Uncompress 2 byte sized data
prle_2
	lsr.l	#1,d1			; size in bytes -> size in items
	bra.s	prle2_start

prle2_loop
	moveq	#0,d0
	move.l	a2,d3			; A2 BEFORE the move.b!!!
	move.b	(a2)+,d0		; a2 MAY POINT TO ODD ADDRESS after this
	bmi.s	prle2_comp		; compressed data
	sub.l	d0,d1			; subtract from total data size
	bcs.s	prle_rts		; emergency exit
	subq.l	#1,d1			; it's x+1 actually
	btst	#0,d3			; was a2 uneven which means it's now even)?
	bne.s	prle2_ucloop		; no, A2 now  is even, use fast loop
uneven2 move.b	(a2)+,(a0)+
	move.b	(a2)+,(a0)+
	dbf	d0,uneven2		; A2 MAY POINT OT ODD ADDRESS!
	bra.s	prle2_start

prle2_ucloop
	move.w	(a2)+,(a0)+		; just copy uncompressed data
	dbf	d0,prle2_ucloop 	; A2 MAY POINT OT ODD ADDRESS!
	bra.s	prle2_start
prle2_comp
	move.l	#257,d2
	sub.l	d0,d2
	sub.l	d2,d1			; subtract from total size
	bcs.s	prle_rts		; emergency exit
	subq.l	#1,d2			; dbf loop makes it one less
	btst	#0,d3			; A2 pointed to odd address (and now to even)?
	bne.s	iseven21		; no->....
	move.b	(a2)+,d0
	lsl.w	#8,d0
	move.b	(a2)+,d0
	bra.s	prle2_cloop
iseven21
	 move.w  (a2)+,d0
prle2_cloop
	move.w	d0,(a0)+		; insert item d2 times
	dbf	d2,prle2_cloop
prle2_start
	tst.l	d1
	bhi.s	prle2_loop
	rts

; Uncompress 4 byte sized data
prle_4
	lsr.l	#2,d1			; size in bytes -> size in items
	bra.s	prle4_start

prle4_loop
	moveq	#0,d0
	move.l	a2,d3
	move.b	(a2)+,d0
	bmi.s	prle4_comp		; compressed data
	sub.l	d0,d1			; subtract from total data size
	bcs	prle_rts		; emergency exit
	subq.l	#1,d1			; it's x+1 actually
	btst	#0,d3		       ; was a2 uneven?
	bne.s	prle4_ucloop		; no, use fast loop
uneven4 move.b	(a2)+,(a0)+
	move.b	(a2)+,(a0)+
	move.b	(a2)+,(a0)+
	move.b	(a2)+,(a0)+
	dbf	d0,uneven4		; A2 MAY POINT OT ODD ADDRESS!
	bra.s	prle4_start
prle4_ucloop
	move.l	(a2)+,(a0)+		; just copy uncompressed data
	dbf	d0,prle4_ucloop
	bra.s	prle4_start
prle4_comp
	move.l	#257,d2
	sub.l	d0,d2
	sub.l	d2,d1			; subtract from total size
	bcs	prle_rts		; emergency exit
	subq.l	#1,d2			; dbf loop makes it one less
	btst	#0,d3			; was A2 uneven
	bne.s	iseven42
	move.b	(a2)+,d0
	swap	d0
	move.w	(a2)+,d0
	lsl.l	#8,d0
	move.b	(a2)+,d0
	bra.s	prle4_cloop

iseven42
	move.l	(a2)+,d0
prle4_cloop
	move.l	d0,(a0)+		; insert item d2 times
	dbf	d2,prle4_cloop
prle4_start
	tst.l	d1
	bhi.s	prle4_loop
	rts
	end
