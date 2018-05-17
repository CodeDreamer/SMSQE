* Scan visible columns of standard menu  V1.01	  1986  Tony Tebby  QJUMP  
*
	section wman
*
	xdef	wm_sccol
*
	include dev8_keys_wwork
	include dev8_keys_wstatus
*
*	d1 c	pointer position relative to sub-window
*	d2   s	(word) on-screen column number 
*	d3   s	(word) virtual column number 
*	d4   s	(word) current hit area size
*	d5   s	(word) current hit area origin (relative to sub-window)
*	d6   s	(word) -1 left arrow column, 0 real item, +1 right arrow column
*	d7 c	origin of sub-window
*	a0 c	passed to column routine
*	a1 c  p address of column routine (called every column visible)
*	a2    p
*	a3 c  p sub-window definition
*	a4 c	passed to column routine
*
wsc.fram equ	$4
wsc_ccnt equ	$0			column counter
wsc_nprt equ	$2			position of number of part menus
*
wm_sccol
	move.l	a2,-(sp)		 ; save a2
	subq.l	#wsc.fram,sp		make room for frame
	swap	d2
	swap	d3
	swap	d4
	swap	d5
	swap	d6
*
	clr.w	d2			visible column number
	move.l	wwa_part(a3),d0 	get part control pointer
	beq.s	wsc_uctrl		... uncontrolled
	move.l	d0,a2
	move.w	(a2)+,wsc_nprt(sp)	set number of parts
	ble.s	wsc_uctrl		... none
wsc_loop
	move.w	wwa_iatt+wwa_curw(a3),d5 set origin of hit area
	add.w	d5,d5
	add.w	(a2)+,d5		update origin
	move.w	#-1,d6			left arrow column
	move.w	#ww.pnarr,d4		hit area size
	move.w	(a2)+,d3		real column number
	jsr	(a1)			do column for left arrow
	bne.s	wsc_exit
*
	add.w	wwa_xoff(a3),d5 	update origin
	addq.w	#1,d2			one more on-screen column
	move.w	(a2)+,wsc_ccnt(sp)	... set column counter
	beq.s	wsc_rtarr		... none
	bsr.s	wsc_section		do a section
	bne.s	wsc_exit
*
wsc_rtarr
	move.w	(a2),d5 		set arrow position
	subq.w	#1,wsc_nprt(sp) 	last section?
	bgt.s	wsc_dort		... no
	move.w	wwa_xsiz(a3),d5 	... yes, arrows at right
wsc_dort
	move.w	#1,d6			right arrow column
	move.w	#ww.pnarr,d4		hit area size
	sub.w	wwa_iatt+wwa_curw(a3),d5 left a bit
	sub.w	wwa_iatt+wwa_curw(a3),d5 and a bit more
	subq.w	#ww.pnarr,d5
	jsr	(a1)			do column for right arrow
	bne.s	wsc_exit
	addq.w	#1,d2			one more visible column
	tst.w	wsc_nprt(sp)		done all sections?
	bgt.s	wsc_loop		... next
	bra.s	wsc_ok			... done
*
* uncontrolled window
*
wsc_uctrl
	move.w	wwa_iatt+wwa_curw(a3),d5 set origin of hit area
	add.w	d5,d5
	add.w	wwa_xoff(a3),d5
	clr.w	d3			true zero column
	move.w	wwa_ncol(a3),wsc_ccnt(sp) set column counter
	beq.s	wsc_ok
	bsr.s	wsc_section
wsc_exit
	swap	d2
	swap	d3
	swap	d4
	swap	d5
	swap	d6
	tst.l	d0
	addq.w	#wsc.fram,sp		remove frame
	move.l	(sp)+,a2
	rts
wsc_ok
	moveq	#0,d0
	bra.s	wsc_exit
*
* do all columns in section
*
wsc_section
	move.l	a2,-(sp)		save section pointer
stak_ext equ	8
	clr.w	d6			real column
	move.l	wwa_xspc(a3),d0 	x spacing list
	blt.s	wscs_cspc		... constant
	move.l	d0,a2
	moveq	#0,d0
	move.w	d3,d0			column number
	lsl.l	#2,d0			in 2 word entries
	add.l	d0,a2			y spacing
wscs_sloop
	move.w	(a2)+,d4		set hit size
	jsr	(a1)			do column routine
	bne.s	wscs_exit		... oops
	addq.w	#1,d2			next column
	addq.w	#1,d3			next column
	add.w	(a2)+,d5		move on hit area origin
	subq.w	#1,wsc_ccnt+stak_ext(sp) one fewer columns
	bgt.s	wscs_sloop
	bra.s	wscs_exit

wscs_cspc
wscs_cloop
	move.w	wwa_xspc(a3),d4 	set hit size
	neg.w	d4
	jsr	(a1)			do column routine
	bne.s	wscs_exit		... oops
	addq.w	#1,d2			next column
	addq.w	#1,d3			next column
	sub.w	wwa_xspc+2(a3),d5	move on hit area origin
	subq.w	#1,wsc_ccnt+stak_ext(sp) one fewer columns
	bgt.s	wscs_cloop
wscs_exit
	move.l	(sp)+,a2		restore section pointer
	rts
	end
