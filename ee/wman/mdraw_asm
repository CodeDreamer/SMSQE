* Draw standard menu   V1.01	 1986	Tony Tebby   QJUMP
* 2013-12-26	v.1.02		use wm_swdfn to reset wdw definition, setting csize (wl)
*

	section wman
*
	xdef	wm_mdraw		full draw
	xdef	wm_mpdrw		part draw
*
	xref	wm_swdef
	xref	wm_swdfn
	xref	wm_csize
	xref	wm_scrow
	xref	wm_sccol
	xref	wm_drmob
*
	include dev8_keys_qdos_pt
	include dev8_keys_wwork
*
*	d0  r	error return
*	d3 c  p 0 byte draw all, -byte selective (mdraw) 
*	d4 c  p start of range (mpdrw)
*	d5 c  p end of range (mpdrw)
*	a0 c  p channel ID of window
*	a3 c  p pointer to sub-window definition
*	a4 c  p pointer to working definition
*
*		all other registers preserved
*
reglist  reg	d2-d7/a1/a2/a4
stk_act  equ	0
stk_xbot equ	4	selective redraw limits
stk_ybot equ	6
stk_xtop equ	8
stk_ytop equ	$a
frame	 equ	$c
*
wm_mpdrw
	movem.l reglist,-(sp)		save registers
	move.l	a0,-(a7)
	moveq	#0,d3			... unselective
	bra.s	wmd_doall
*
wm_mdraw
	movem.l reglist,-(sp)		save registers
	move.l	a0,-(a7)
	moveq	#-1,d4			no limits
	moveq	#-1,d5
*
wmd_doall
	movem.l d3/d4/d5,-(sp)		set action and limits in frame
*
	bsr.l	wm_swdfn		reset window definition & set csizes
	bne.s	wmd_exit
*
	move.l	sp,a4			set register frame pointer
	lea	wmd_dorw(pc),a1
	bsr.l	wm_scrow		do all rows
wmd_exit
	add.w	#frame,sp		remove frame
	move.l	(a7)+,a0
	bsr.l	wm_csize		reset csizes to 0,0
	movem.l (sp)+,reglist		restore registers
	rts
	page
*
* do a row
*
wmd_dorw
	tst.w	d6			genuine row?
	bne.s	wmd_ok			... no
	cmp.w	stk_ybot(a4),d3 	below bottom?
	blt.s	wmd_ok			... yes
	cmp.w	stk_ytop(a4),d3 	at top?
	bhs.s	wmd_ok
	lea	wmd_doob(pc),a1 	set new action
	bsr.l	wm_sccol		do all columns
	lea	wmd_dorw(pc),a1 	reset our action
	rts
wmd_ok
	moveq	#0,d0
	rts
*
* do an object
*
wmd_doob
	tst.w	d6			genuine column?
	bne.s	wmd_ok			... no
	cmp.w	stk_xbot(a4),d3 	off left?
	blt.s	wmd_ok			... yes
	cmp.w	stk_xtop(a4),d3 	off right?
	bhs.s	wmd_ok			... yes
	movem.l d3/d4/d5/a1/a2,-(sp)	save regs
	move.l	wwa_rowl(a3),a1 	get row list pointer
	swap	d4			get x,y
	swap	d5
	move.l	d3,d0
	clr.w	d0
	swap	d0
	lsl.l	#3,d0			index rowlist
	add.l	d0,a1
	move.l	(a1)+,a2		pointer to row
	mulu	#wwm.olen,d3		pointer to object
	add.l	d3,a2
	cmp.l	(a1),a2 		off end?
	bge.s	wmd_noob		... yes
	move.l	stk_act(a4),d3		set action
	bsr.l	wm_drmob		draw menu object
	bra.s	wmd_exob
wmd_noob
	moveq	#0,d0
wmd_exob
	movem.l (sp)+,d3/d4/d5/a1/a2	restore regs
	rts
	end
