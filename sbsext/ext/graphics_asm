; QL Graphics	    V2.00    	1990  Tony Tebby   QJUMP
;
;	SCALE scale,x,y
;	POINT x,y {, x,y}
;	LINE  (x,y) (to x,y) {(, x,y) to x,y}
;	ARC   (x,y) to x,y,angle {(, x,y) to x,y,angle}
;	ELLIPSE x,y,radius (ecc,angle)
;	POINT_R x,y {, x,y}
;	LINE_R	(x,y) (to x,y) {(, x,y) to x,y}
;	ARC   (x,y) to x,y,angle {(, x,y) to x,y,angle}
;	ELLIPSE_R x,y,radius (ecc,angle)
;
	section exten

	xdef	scale
	xdef	point
	xdef	line
	xdef	arc
	xdef	ellipse
	xdef	point_r
	xdef	line_r
	xdef	arc_r
	xdef	ellipse_r

	xref	ut_gtfp1		 ; get one floating point
	xref	ut_chan1
	xref	ut_trp3r
	xref	ut_chkri

	include 'dev8_keys_err'
	include 'dev8_keys_sbasic'
	include 'dev8_keys_qlv'
	include 'dev8_keys_qdos_io'

scale
	bsr.l	gr_chan1		 ; get channel
	bsr.l	gr_fp			 ; get scale
	bne.s	gr_rts1
	bsr.s	gr_coord		 ; get x,y
	clr.l	(a6,d4.l)
	clr.l	4(a6,d4.l)
	clr.l	8(a6,d4.l)		 ; clear position
	moveq	#iog.scal,d0
	jmp	ut_trp3r

point_r
	moveq	#-1,d7			  ; d7 usually 0!!
point
	bsr.l	gr_chan1		 ; get channel
point_loop
	cmp.l	a3,a5			 ; any params?
	beq.s	gr_rts1
	move.l	d6,sb_arthp(a6) 	 ; restore RIP
	bsr.s	gr_coord		 ; get x,y
	moveq	#iog.dot,d0
	jsr	ut_trp3r
	beq.s	point_loop
gr_rts1
	rts

arc_r
	moveq	#-1,d7
arc
	st	d7
	bra.s	line_arc

line_r
	moveq	#-1,d7
line
	sf	d7
line_arc
	bsr.l	gr_chan1		 ; get channel
line_loop
	cmp.l	a3,a5			 ; any params?
	beq.s	gr_rts1
	move.l	d6,sb_arthp(a6) 	 ; restore RIP
	cmp.b	#5,d5			 ; TO?
	bne.s	line_from		 ; ... no
	bsr.s	gr_zero 		 ; ... yes, current position
	bra.s	line_to
line_from
	bsr.s	gr_coord		 ; get x,y ...
	cmp.b	#5,d5			 ; TO?
	bne.s	line_loop		 ; ... no
line_to
	bsr.s	gr_coord		 ; TO x,y
	moveq	#iog.line,d0
	tst.b	d7			 ; ... line?
	beq.s	line_trp3
	bsr.s	gr_fp			 ; ... arc, angle required
	bne.s	gr_rts
	moveq	#iog.arc,d0
line_trp3
	jsr	ut_trp3r
	beq.s	line_loop
	rts

gr_coord
	addq.l	#6,d4
	bsr.s	gr_co1			 ; x coordinate
	subq.l	#6,d4
	bsr.s	gr_co1
	rts

gr_zero
	move.l	sb_arthp(a6),a1
	sub.w	#12,a1
	move.w	(a6,d4.l),(a6,a1.l)	   ; set y coordinate
	move.l	2(a6,d4.l),2(a6,a1.l)
	move.w	6(a6,d4.l),6(a6,a1.l)	   ; set x coordinate
	move.l	8(a6,d4.l),8(a6,a1.l)
	move.l	a1,sb_arthp(a6)
	rts

gr_co1
	bsr.s	gr_fp			 ; one floating point
	bne.s	gr_rts8
	tst.l	d7			 ; relative?
	bpl.s	gr_upco 		 ; ... no
	subq.l	#6,a1
	move.w	(a6,d4.l),(a6,a1.l)	 ; add coordinate
	move.l	2(a6,d4.l),2(a6,a1.l)
	moveq	#qa.add,d0
	jsr	qa.op*3+qlv.off

gr_upco
	move.w	(a6,a1.l),(a6,d4.l)	 ; update coordinate
	move.l	2(a6,a1.l),2(a6,d4.l)
	rts


gr_fp
	move.b	1(a6,a3.l),d5		 ; next separator
	lsr.b	#4,d5
	jsr	ut_gtfp1		 ; get a floating point
	addq.l	#8,a3			 ; and the next
	rts

gr_rts8
	addq.l	#4,sp
gr_rts4
	addq.l	#4,sp
gr_rts
	rts

gr_chan1
	move.l	#300,d1
	jsr	ut_chkri		 ; check RI

	move.b	1(a6,a3.l),d5		 ; type of first, separator of next
	move.b	d5,d0
	add.b	d5,d5
	lsr.b	#5,d5			 ; separator
	tst.b	d0			 ; #?
	blt.s	gr_gtchn		 ; ... yes
	and.b	#$f,d0			 ; null?
	beq.s	gr_chnul		 ; ... yes
	moveq	#0,d5			 ; ... no, scrap separator
	bra.s	gr_gtchn
gr_chnul
	addq.l	#8,a3			 ; skip null
gr_gtchn
	jsr	ut_chan1		 ; get channel
	bne.s	gr_rts4 		 ; ... oops
	move.l	sb_arthp(a6),d6 	 ; preserve RIP
	addq.l	#ch_grphy,d4		 ; point to graphics long words
	rts

ellipse_r
	moveq	#-1,d7
ellipse
	bsr.s	gr_chan1		 ; get channel
elli_loop
	cmp.l	a3,a5			 ; more parameters?
	beq.s	gr_rts			 ; ... no
	move.l	d6,sb_arthp(a6) 	 ; restore RIP
	bsr.l	gr_coord		 ; get x,y ...
	bsr.s	gr_fp			 ; ... and radius
	bne.s	gr_rts

	cmp.b	#2,d5			 ; Semicolon?
	beq.s	elli_circ		 ; ... yes, circle
	cmp.l	a3,a5			 ; more params?
	beq.s	elli_circ		 ; ... no, circle

	bsr.s	gr_fp			 ; ecc
	bne.s	gr_rts
	bsr.s	gr_fp			 ; angle
	bne.s	gr_rts

	bra.s	elli_trp3

elli_circ
	sub.w	#12,a1
	clr.w	$a(a6,a1.l)		  ; 0,rad,1 on stack
	move.l	#$08014000,6(a6,a1.l)
	clr.l	2(a6,a1.l)
	clr.w	0(a6,a1.l)

elli_trp3
	move.w	12(a6,a1.l),d0		 ; swap around
	move.w	6(a6,a1.l),12(a6,a1.l)
	move.w	d0,6(a6,a1.l)
	move.l	14(a6,a1.l),d0		 ; swap around
	move.l	8(a6,a1.l),14(a6,a1.l)
	move.l	d0,8(a6,a1.l)

	moveq	#iog.elip,d0
	jsr	ut_trp3r
	beq.s	elli_loop
	rts



	end
