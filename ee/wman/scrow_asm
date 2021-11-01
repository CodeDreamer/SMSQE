* Scan visible rows of standard menu  V1.01    1986  Tony Tebby  QJUMP
* 2020-08-13	1.02	preserve d4 high word (moveq to move.w) (AH)  
*
	section wman
*
	xdef	wm_scrow
*
	include dev8_keys_wwork
	include dev8_keys_wstatus
*
*	d1 c	pointer position relative to sub-window
*	d2   s	(word) on-screen row number 
*	d3   s	(word) virtual row number 
*	d4   s	(word) current hit area size
*	d5   s	(word) current hit area origin (relative to sub-window)
*	d6   s	(word) -1 down arrow row, 0 real item, +1 up arrow row
*	d7 c	origin of sub-window
*	a0 c	passed to row routine
*	a1 c  p address of row routine (called every row visible)
*	a2  rs	row list pointer
*	a3 c  p sub-window definition
*	a4 c	passed to row routine
*
wsr.fram equ	$4
wsr_rcnt equ	$0			row counter
wsr_nprt equ	$2			position of number of part menus
*
wm_scrow
	subq.l	#wsr.fram,sp		make room for frame
*
	moveq	#0,d2			visible row number
	move.l	wwa_part+wwa.clen(a3),d0 get part control pointer
	beq.s	wsr_uctrl		... uncontrolled
	move.l	d0,a2
	move.w	(a2)+,wsr_nprt(sp)	set number of parts
	ble.s	wsr_uctrl		... none
wsr_loop
	move.w	wwa_iatt+wwa_curw(a3),d5 set origin of hit area
	add.w	(a2)+,d5		update origin
	moveq	#-1,d6			up arrow row
	move.w	#ww.scarr,d4		hit area size (AH)
	move.w	(a2)+,d3		real row number
	jsr	(a1)			do row for up arrow
	bne.s	wsr_exit
*
	add.w	wwa_yoff(a3),d5 	update origin
	addq.w	#1,d2			one more on-screen row
	move.w	(a2)+,wsr_rcnt(sp)	set row counter
	beq.s	wsr_dnarr		... none
	bsr.s	wsr_section		do a section
	bne.s	wsr_exit
*
wsr_dnarr
	move.w	(a2),d5 		set arrow position
	subq.w	#1,wsr_nprt(sp) 	last section?
	bgt.s	wsr_dodn		... no
	move.w	wwa_ysiz(a3),d5 	... yes, arrows at bottom
wsr_dodn
	moveq	#1,d6			down arrow row
	move.w	#ww.scarr,d4		hit area size (AH)
	sub.w	wwa_iatt+wwa_curw(a3),d5 up a bit
	subq.w	#ww.scarr,d5
	jsr	(a1)			do row for down arrow
	bne.s	wsr_exit
	addq.w	#1,d2			one more visible row
	tst.w	wsr_nprt(sp)		done all sections?
	bgt.s	wsr_loop		... next
	bra.s	wsr_ok			... done
*
* uncontrolled window
*
wsr_uctrl
	move.w	wwa_yoff(a3),d5
	add.w	wwa_iatt+wwa_curw(a3),d5 set origin of hit area
	moveq	#0,d3			true row zero
	move.w	wwa_nrow(a3),wsr_rcnt(sp) set row counter
	beq.s	wsr_ok
	bsr.s	wsr_section
wsr_exit
	addq.w	#wsr.fram,sp		remove frame
	rts
wsr_ok
	moveq	#0,d0
	bra.s	wsr_exit
*
* do all rows in section
*
wsr_section
	move.l	a2,-(sp)		save section pointer
stak_ext equ	$8
	moveq	#0,d6			real row
	move.l	wwa_yspc(a3),d0 	y spacing list
	blt.s	wsrs_cspc		... constant spacing
	move.l	d0,a2
	moveq	#0,d0
	move.w	d3,d0			row number
	lsl.l	#2,d0			in 2 word entries
	add.l	d0,a2			y spacing
wsrs_sloop
	move.w	(a2)+,d4		set hit size
	jsr	(a1)			do row routine
	bne.s	wsrs_exit		... oops
	addq.w	#1,d2			next row
	addq.w	#1,d3			next row
	add.w	(a2)+,d5		move on hit area origin
	subq.w	#1,wsr_rcnt+stak_ext(sp) one fewer rows
	bgt.s	wsrs_sloop
	bra.s	wsrs_exit

wsrs_cspc
wsrs_cloop
	move.w	wwa_yspc(a3),d4 	set hit size
	neg.w	d4			it was negative!
	jsr	(a1)			do row routine
	bne.s	wsrs_exit		... oops
	addq.w	#1,d2			next row
	addq.w	#1,d3			next row
	sub.w	wwa_yspc+2(a3),d5	move on hit area origin
	subq.w	#1,wsr_rcnt+stak_ext(sp) one fewer rows
	bgt.s	wsrs_cloop

wsrs_exit
	move.l	(sp)+,a2		restore section pointer
	rts
	end
