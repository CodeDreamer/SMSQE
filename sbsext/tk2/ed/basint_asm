* ED - Expand a BASIC line into ASCII	     1985  Tony Tebby	 QJUMP
*    - Edit a line back into program file
*    - Delete a line
*    - Set up a new line with line number
*
	section ed
*
	xdef	ed_xplin
	xdef	ed_putln
	xdef	ed_delln
	xdef	ed_setln

	xref	ed_up
	xref	ed_down

*
	include dev8_sbsext_ed_keys
	include dev8_sbsext_ed_data
*
*	d1   p
*	d2   p
*	d3   p
*	d4 c p	line number
*	d5   p
*	d6   p
*	d7   p
*	a4 c p	pointer to (or before) line in program file
*	a5   p
*
ed_xplin
	movem.l d1-d7/a4/a5,-(sp)
	move.w	d4,d6			to end of list (the same!!)
	moveq	#0,d7			(BASIC expects this)
	sf	bv_print(a6)		do not print expanded line
	move.w	pr..list,a2
	jsr	qlv_jump+pro_list(a2)	but start from list from line
	movem.l (sp)+,d1-d7/a4/a5
	rts
*
ed_setln
	movem.l d1-d3/a0,-(sp)		save volatiles
	move.l	bv_bfbas(a6),a0 	and use Basic buffer
	move.l	a0,a1
	move.w	d4,(a6,a1.l)		put integer in
	move.w	cn..itod,a2
	jsr	(a2)			and convert to string
	move.l	a0,bv_bfp(a6)		then set running pointer
	move.w	d4,ed_cline(a5) 	and say that it is current line
	movem.l (sp)+,d1-d3/a0
edp_rts0
	tst.l	d0
	rts
*
ed_delln
	moveq	#0,d0			set line number
	move.w	edr_lnr(a5,d5.w),d0	any line to delete?
	ble.s	edp_rts0
	move.w	d0,d4
	bsr.s	ed_setln		set up line number only

ed_putln
	st	bv_edit(a6)		set program edited
	clr.w	ed_topln(a5)
	clr.l	ed_topad(a5)	       (it would be better to do only if this li
*					less or equal top line)
	move.l	bv_bfp(a6),a1		get pointer to buffer
	moveq	#' ',d0 		check for spaces
edp_strip
	cmp.l	bv_bfbas(a6),a1 	any buffer left?
	ble.s	ed_delln		... no
	cmp.b	-1(a6,a1.l),d0		space at end?
	bne.s	edp_newl		... no
	subq.l	#1,a1			back one
	bra.s	edp_strip
*
edp_newl
	move.b	#$a,(a6,a1.l)		set newline
	addq.l	#1,a1			and increment pointer
*
	move.l	bv_bfp(a6),-(sp)	save buffer pointer
	move.l	a1,bv_bfp(a6)		and set it
*
	movem.l d1-d7/a0/a4/a5,-(sp)
edp.a5	equ	10*4
	move.l	ed_paini(a5),a2 	initialise parser
	jsr	(a2)
	move.w	pa..table,a2		graph table address
	adda.w	#qlv_jump,a2
	move.w	pa..graph,a4		parse using graph (a2)
	jsr	qlv_jump(a4)
	blt.s	err_bl			... oops
	move.w	pa..strip,a2
	jsr	qlv_jump(a2)		strip spaces
	move.w	ed..newln,a2
	jsr	qlv_jump(a2)		edit in a newline
	bra.s	err_bl			no line number
	move.l	bv_tkp(a6),d0		get token list pointer
	move.l	bv_tkbas(a6),a2
	sub.l	a2,d0			length of token list
	subq.l	#6,d0			is it just a line number?
	beq.s	edp_exit		... yes, (it is deleted)
	move.w	tk_lno(a6,a2.l),d0	... no, set line number
	bra.s	edp_exit
*
err_bl
	move.l	#err.bl,d0		bad line
;	 move.l  bv_bfbas(a6),bv_erpos(a6) error at start of line
	move.l	edp.a5(sp),a5		will be smashed by pa.. functions
	tst.w	ed_chno(a5)		edit channel
	beq.s	edp_exit		... is channel 0
	move.l	bv_chbas(a6),a0
	move.l	(a6,a0.l),a0
	move.w	ut..err,a2
	jsr	(a2)			write error message
	sf	ed_clear(a5)		set window zero not clear

edp_exit
	move.l	bv_bfp(a6),a1		get current eol
	move.b	#' ',-1(a6,a1.l)	remove newline again
	movem.l (sp)+,d1-d7/a0/a4/a5
	move.l	(sp)+,bv_bfp(a6)
	tst.l	d0			did it error?

; The following code is incompatible to QDOS I think
;	 bge.s	 edp_rts		 ... no

;	 movem.l d0/d1/d2,-(sp)
;	 move.l  bv_erpos(a6),d2
;	 sub.l	 bv_bfbas(a6),d2	 error position
;	 subq.l  #ed.xoff,d2
;	 divs	 ed_ncolx(a5),d2	 lsw is row of line
;	 sub.w	 edr_rno(a5,d5.w),d2	 offset
;	 beq.s	 edp_setcol		 ... none
;	 blt.s	 edp_up 		 go up
edp_down
;	 cmp.w	 ed_nrow1(a5),d6 is it at the bottom?
;	 bge.s	 edp_oops
;	 move.l  d2,-(sp)
;	 jsr	 ed_down		 go down a line
;	 move.l  (sp)+,d2
;	 subq.w  #1,d2
;	 bgt.s	 edp_down
;	 bra.s	 edp_setcol
;edp_up
;	 tst.w	 d6		 at top?
;	 beq.s	 edp_oops
;	 move.l  d2,-(sp)
;	 jsr	 ed_up			 go up a line
;	 move.l  (sp)+,d2
;	 addq.w  #1,d2
;	 blt.s	 edp_up

edp_setcol
;	 swap	 d2
;	 addq.w  #ed.xoff,d2
;	 move.w  d2,d7
edp_oops
;	 movem.l (sp)+,d0/d1/d2
;	 tst.l	 d0

edp_rts
	rts
	end
