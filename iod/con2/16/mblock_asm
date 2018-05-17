; Move area (generalised PAN/SCROLL)  V2.01    Tony Tebby
;
	section con

	xdef	cn_mblock
	xdef	pt_mblock

;
;	d1 c  s size of section to move
;	d2 c  s old origin in source area
;	d3 c  s new origin in destination area
;	d4    s scratch
;	d5    s scratch
;	d6/d7	preserved
;	a0/a1	preserved
;	a2 c	row increment of source area
;	a3 c	row increment of destination area
;	a4 c	base address of source area
;	a5 c	base address of destination area
;	a6/a7	preserved
;
pt_mblock
cn_mblock

; set source address

	move.l	a2,d0			 ; row increment
	mulu	d2,d0
	add.l	d0,a4			 ; start of row
	swap	d2
	add.w	d2,a4
	add.w	d2,a4			 ; first pixel

; set destination address

	move.l	a3,d0			 ; row increment
	mulu	d3,d0
	add.l	d0,a5			 ; start of row
	swap	d3
	add.w	d3,a5
	add.w	d3,a5			 ; first pixel

	cmp.l	a4,a5			 ; copy from bottom or top
	bgt.s	cmb_down		 ; top down

	move.w	d1,d3			 ; row count
	swap	d1
	move.w	d1,d0
	add.w	d0,d0
	sub.w	d0,a2
	sub.w	d0,a3			 ; corrected increment

	moveq	#0,d2
	move.l	a4,d0
	lsr.w	#2,d0			 ; odd word at start?
	scs	d4
	bcc.s	cmbu_ckend		 ; ... no
	moveq	#-1,d2			 ; ... yes, set flag
	subq.w	#1,d1			 ; take one word off width

cmbu_ckend
	bclr	#0,d1			 ; odd word at end?
	sne	d5			 ; ... yes
	asr.w	#1,d1			 ; number of full long words
	bge.s	cmbu_blke
	blt.s	cmbu_rts

cmbu_loop
	move.w	d1,d2			 ; full long words
	tst.b	d4			 ; leading word?
	bpl.s	cmbu_lle		 ; ... no

	move.w	(a4)+,(a5)+		 ; do leading word

	bra.s	cmbu_lle
cmbu_llp
	move.l	(a4)+,(a5)+		 ; copy some long words
cmbu_lle
	dbra	d2,cmbu_llp

	tst.b	d5			 ; trailing word?
	beq.s	cmbu_lpe		 ; ... no

	move.w	(a4)+,(a5)+		 ; do trailing word

cmbu_lpe
	add.l	a2,a4			 ; start of next line
	add.l	a3,a5
cmbu_blke
	dbra	d3,cmbu_loop

cmbu_rts
	rts


cmb_down
	move.w	d1,d3			 ; row count

	move.l	a2,d0			 ; start at end of source
	mulu	d1,d0
	add.l	d0,a4

	move.l	a3,d0			 ; start at end of dest
	mulu	d1,d0
	add.l	d0,a5

	swap	d1
	move.w	d1,d0
	add.w	d0,d0

	sub.w	d0,a2
	sub.w	d0,a3			 ; corrected increment

	moveq	#0,d2
	move.l	a4,d0
	lsr.w	#2,d0			 ; odd word at start?
	scs	d4
	bcc.s	cmbd_ckend		 ; ... no
	moveq	#-1,d2			 ; ... yes, set flag
	subq.w	#1,d1			 ; take one word off width

cmbd_ckend
	bclr	#0,d1			 ; odd word at end?
	sne	d5			 ; ... yes
	asr.w	#1,d1			 ; number of full long words
	bge.s	cmbd_lpe
	blt.s	cmbd_rts

cmbd_loop
	move.w	d1,d2			 ; full long words
	tst.b	d4			 ; leading word?
	bpl.s	cmbd_lle		 ; ... no

	move.w	-(a4),-(a5)		 ; do leading word

	bra.s	cmbd_lle
cmbd_llp
	move.l	-(a4),-(a5)		 ; copy some long words
cmbd_lle
	dbra	d2,cmbd_llp

	tst.b	d5			 ; trailing word?
	beq.s	cmbd_lpe		 ; ... no

	move.w	-(a4),-(a5)		 ; do trailing word

cmbd_lpe
	sub.l	a2,a4			 ; start of next line
	sub.l	a3,a5
	dbra	d3,cmbd_loop

cmbd_rts
	rts



	end
