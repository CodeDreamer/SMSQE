; Pan a menu sub-window  V1.05	  1986  Tony Tebby   QJUMP

	section wman

	xdef	wm_pan

	xref	wm_swdef
	xref	wm_mdraw
	xref	wm_mpdrw
	xref	wm_index
	xref	wm_drbar

	include 'dev8_keys_wwork'
	include 'dev8_keys_wstatus'
	include 'dev8_keys_qdos_io'

;	d0  r	error return
;	d2 c s	item number for pan/scroll
;	d3 cr	hit on pan/scroll bar - MSW hit position, LSW length
;	d0	   row/column counter
;	d1
;	d2	   section top/left
;	d3	   section height/width
;	d4	   column/row redraw start range
;	d5	   column/row redraw end range
;	d6	   pan distance
;	d7	   clear distance
;	a0 c  p channel ID
;	a1   sp window definition pointer
;	a2   sp part window control block
;	a3 c  p pointer to sub-window definition
;	a4 c sp pointer to working definition / pointer to spacing lists


xsize	equ	$00
ysize	equ	$02
xorg	equ	$04
yorg	equ	$06

reglist reg	d5-d7/a1/a2/a3/a4
frame	equ	$08
stk_a4	equ	$20

wm_pan
	movem.l reglist,-(sp)		 ; save register list
	subq.l	#frame,sp
	bsr.l	wm_swdef		 ; set sub window
	move.l	wwa_part(a3),a2 	 ; find part window control block
	move.w	(a2)+,d7		 ; number of sections
	subq.w	#1,d7			 ; ... highest section number
	moveq	#$7f,d1 		 ; find section number
	and.w	d2,d1
	moveq	#wss.ssln,d0
	mulu	d1,d0
	add.w	d0,a2			 ; pointer to section

	move.w	d2,d1			 ; save item number
	move.l	d3,d4			 ; and hit on bar
	move.l	wwa_ncol(a3),d5 	 ; preset redraw end
	swap	d5

;    set section parameters  d2 start / d3 end

	move.w	wss_spos(a2),d2 	 ; left of section
	move.w	wwa_curw+wwa_iatt(a3),d6 ; x border width
	add.w	d6,d6
	add.w	wwa_xoff(a3),d6 	 ; ... plus offset

	add.w	d6,d2			 ; real start

	move.w	wss_spos+wss.ssln(a2),d3 ; end of section
	cmp.b	d1,d7			 ; last section?
	bne.s	wpn_sbot		 ; ... no
	move.w	wwa_xsiz(a3),d3
wpn_sbot
	sub.w	d6,d3

	btst	#wsi..bar,d1		 ; hit on bar?
	beq.l	wpn_arrow		 ; ... no, arrow or key
	btst	#wsi..xt,d1		 ; do on bar?
	beq.l	wpn_sbar		 ; ... no
	tst.l	d4			 ; do on join?
	bge.s	wpn_split		 ; ... no

	sub.w	wss_spos(a2),d2 	 ; find new start of section
	add.w	wss_spos-wss.ssln(a2),d2
	move.l	a2,d0			 ; save current pointer
wpn_jloop
	addq.b	#1,d1			 ; next section
	cmp.b	d7,d1			 ; last section?
	bgt.s	wpn_jset		 ; ... yes
	move.w	6(a2),(a2)+
	move.w	6(a2),(a2)+
	move.w	6(a2),(a2)+
	bra.s	wpn_jloop

wpn_jset
	move.l	d0,a2
	subq.l	#wss.ssln,a2		 ; previous section
	move.w	wss_sstt(a2),d0
	bsr.l	wpn_cnts
	sub.w	d0,d1
	move.w	d1,wss_ssiz(a2) 	 ; set size of section
	move.l	wwa_part(a3),a2
	subq.w	#1,(a2) 		 ; set new number of sections
	bra.s	wpn_drall		 ; and redraw whole window

wpn_split
	addq.w	#1,d7
	cmp.w	wwa_nxsc(a3),d7 	 ; number of control sections
	bge.l	wpn_exok		 ; at max already
	swap	d1			 ; keep section number
	swap	d4			 ; position of split
	add.w	d2,d4			 ; relative to window
	exg	d3,d4			 ; set temporary end of section
	sub.w	d6,d3			 ; actual limit
	move.w	wss_sstt(a2),d0 	 ; first item
	bsr.l	wpn_cnts		 ; count up
	cmp.w	d0,d1			 ; any in section?
	ble.l	wpn_exok		 ; ... no

	swap	d2			 ; keep start of this section
	move.w	d7,d2			 ; end of this section
	add.w	d6,d2			 ; real end
	add.w	d6,d2
	move.w	d4,d3			 ; the old end
	swap	d0
	move.w	d1,d0
	bsr.l	wpn_cnts		 ; count up
	cmp.w	d0,d1			 ; any in section?
	ble.l	wpn_exok		 ; ... no

	swap	d1			 ; split section number
	move.l	wwa_part(a3),a2
	move.w	(a2),d7
	addq.w	#1,(a2)+		 ; one more section
	moveq	#wss.ssln,d3
	mulu	d7,d3
	lea	wss.ssln(a2,d3.l),a2	 ; end of control table
	bra.s	wpn_scpe
wpn_scpy
	move.w	-8(a2),-(a2)
	move.w	-8(a2),-(a2)
	move.w	-8(a2),-(a2)
wpn_scpe
	subq.b	#1,d7
	cmp.b	d1,d7
	bgt.s	wpn_scpy

	swap	d1			 ; set new section
	sub.w	d0,d1
	move.w	d1,-(a2)		 ; number of items
	move.w	d0,-(a2)		 ; start item
	sub.w	d6,d2
	move.w	d2,-(a2)		 ; start pixel position
	sub.w	wss_sstt-wss.ssln(a2),d0
	move.w	d0,-(a2)		 ; number of items in split section
wpn_drall
	moveq	#iow.clra,d0
	bsr.l	wpn_trp3		 ; clear window
	moveq	#0,d3
	move.l	stk_a4(sp),a4
	jsr	wm_mdraw		 ; redraw menu
	jsr	wm_index		 ; and index items
	bra.l	wpn_exit


wpn_sbar
	move.w	d5,d0			 ; find item at middle of section
	add.w	d0,d0
	swap	d4
	mulu	d4,d0
	swap	d4
	divu	d4,d0
	sub.w	wss_ssiz(a2),d0
	addq.w	#1,d0
	asr.w	#1,d0
	bge.s	wpn_rdbar
	moveq	#0,d0
wpn_rdbar
	cmp.w	wss_sstt(a2),d0 	 ; any change?
	beq.l	wpn_exok
	bra.s	wpn_rdraw

wpn_arrow
	cmp.w	#1,wss_ssiz(a2) 	 ; single item?
	bgt.s	wpn_dir 		 ; ... no
	bclr	#wsi..xt,d1		 ; ... yes, do same as hit!
wpn_dir
	btst	#wsi..dnr,d1		 ; is direction down / right?
	bne.s	wpn_rght		 ; ... right

	move.w	wss_sstt(a2),d0 	 ; old section start
	beq.l	wpn_exok		 ; ... already at top
	btst	#wsi..xt,d1		 ; extra?
	beq.s	wpn_lft1		 ; ... no

	move.w	d0,d4			 ; new end row
	addq.w	#1,d4

wpn_left
	sub.w	#1,d0			 ; go up
	blt.s	wpn_brt 		 ; ... too far
	bsr.l	wpn_cnts
	cmp.w	d4,d1			 ; past it?
	beq.s	wpn_rdraw		 ; ... there, redraw
	blt.s	wpn_brt 		 ; ... yes, go back right
	bra.s	wpn_left

wpn_brt
	addq.w	#1,d0			 ; back one
	bra.s	wpn_rdraw		 ; and redraw

wpn_lft1
	subq.w	#1,d0
	move.w	d0,wss_sstt(a2) 	 ; set new section start

	moveq	#0,d4			 ; set redraw start y
	move.w	d0,d4			 ; and x

	bsr.l	wpn_cnts		 ; count rows in section
	sub.w	d0,d1
	move.w	d1,wss_ssiz(a2) 	 ; number of rows
	move.w	d4,d5
	addq.w	#1,d5			 ; reset redraw end x (just one)

	sub.w	d3,d7			 ; set amount to clear
	neg.w	d7
	sub.w	d2,d3			 ; set total window zzzzzz
	move.w	wwm_spce(a4),d6 	 ; and pan distance
	bra.s	wpn_pan


wpn_rght
	move.w	wss_sstt(a2),d0 	 ; section start
	moveq	#0,d4			 ; set redraw start y
	move.w	d0,d4
	add.w	wss_ssiz(a2),d4 	 ; and x

	cmp.w	wwa_ncol(a3),d4 	 ; off end?
	bge.l	wpn_exok		 ; ... yes

	btst	#wsi..xt,d1		 ; extra?
	beq.s	wpn_dn1 		 ; ... no
	subq.w	#1,d4
	move.w	d4,d0			 ; new top

wpn_rdraw
	clr.l	d4			 ; redraw start
	move.w	d0,d4
	bsr.l	wpn_cnts
	move.w	d0,wss_sstt(a2) 	 ; set top
	move.w	d1,d5			 ; redraw end
	sub.w	d0,d1
	move.w	d1,wss_ssiz(a2) 	 ; and section size
	moveq	#0,d6			 ; pan none
	sub.w	d2,d3			 ; window zzzzzz
	move.w	d3,d7			 ; clear all
	bra.s	wpn_pan

wpn_dn1
	addq.w	#1,d0			 ; new start
	move.w	d0,wss_sstt(a2) 	 ; set new start

	bsr.l	wpn_cnts		 ; count rows in section

	move.w	d1,d5			 ; reset end of redraw
	sub.w	d0,d1			 ; number in section
	move.w	d1,wss_ssiz(a2) 	 ; ... set it

	moveq	#0,d7			 ; clear none
	sub.w	d2,d3			 ; window zzzzzz
	move.w	wwm_spce-wwm.slen(a4),d6 ; pan by old spacing
	neg.w	d6

; pan the section

wpn_pan
	move.l	stk_a4(sp),a4		 ; reset pointer to working definition
	subq.l	#2,sp			 ; y origin of pan area
	add.w	wwa_xorg(a3),d2 	 ; set x origin of pan area
	add.w	ww_xorg(a4),d2
	move.w	d2,-(sp)

	subq.l	#2,sp			 ; y size
	move.w	d3,-(sp)		 ; x size

	tst.w	d6			 ; any pan?
	beq.s	wpn_clra		 ; ... no, clear all
	sub.w	d7,xsize(sp)		 ; do not bother with panning odd bit
	ble.s	wpn_clear
	moveq	#iow.pana,d0		 ; pan
	bsr.l	wpn_ally		 ; all y sections
wpn_clear
	move.w	d7,xsize(sp)		 ; set clear size
	ble.s	wpn_rdef		 ; ... nothing to clear
	add.w	d3,xorg(sp)		 ; set clear origin
	sub.w	d7,xorg(sp)
wpn_clra
	moveq	#iow.clra,d0		 ; clear bit of
	bsr.l	wpn_ally		 ; all y sections

wpn_rdef
	move.l	wwa_xsiz(a3),xsize(sp)	 ; reset window
	move.l	wwa_xorg(a3),d0
	add.l	ww_xorg(a4),d0		 ; absolute origin
	move.l	d0,xorg(sp)
	move.l	sp,a1
	bsr.s	wpn_wdef
	addq.l	#8,sp			 ; remove junk

	swap	d4			 ; limits right way round
	swap	d5
	jsr	wm_mpdrw		 ; redraw defined part of menu
					 ; redraw bars
	jsr	wm_drbar

wpn_exok
	moveq	#0,d0
wpn_exit
	moveq	#0,d3			 ; cancel actions
	moveq	#0,d4
	addq.l	#frame,sp
	movem.l (sp)+,reglist
	tst.l	d0
	rts

; screen io calls

wpn_wdef
	moveq	#iow.defw,d0		 ; define window
	moveq	#0,d2			 ; no border
wpn_trp3
	move.l	d3,-(sp)		 ; save d3
	moveq	#-1,d3
	trap	#3			 ; do trap
	move.l	(sp)+,d3
	tst.l	d0			 ; error?
	rts

; count rows in section

reg.cnts reg	d6/a0/a2
stk_cnts equ	$10
wpn_cnts
	movem.l reg.cnts,-(sp)
	move.l	wwa_xspc(a3),d6 	 ; spacing list pointer
	blt.s	wpn_cspc		 ; ... constant
	move.l	d6,a4
	moveq	#0,d7
	move.w	d0,d7
	lsl.l	#2,d7			 ; index spacing list
	add.l	d7,a4
	sub.l	a0,a0			 ; keep going
	bra.s	wpn_cdo
wpn_cspc
	lea	stk_cnts(sp),a4 	 ; dummy spacing list is here
	swap	d6
	neg.w	d6			 ; both bits must be positive
	swap	d6
	neg.w	d6
	move.l	d6,(a4)+
	move.l	d6,(a4)
	move.w	#-4,a0			 ; keep backspacing
wpn_cdo
	move.l	a4,a2
	move.w	d0,d1			 ; find last row
	move.w	d2,d6
	move.w	d6,d7			 ; save up to here
	bra.s	wpn_clend
wpn_cloop
	add.w	(a2),d6 		 ; end of this hit area
	cmp.w	d6,d3			 ; room for this hit area?
	blt.s	wpn_cdone		 ; ... no
	addq.w	#1,d1			 ; ... yes
	move.w	d6,d7			 ; we've got this one
	sub.w	(a2)+,d6
	add.w	(a2)+,d6		 ; move on to start of next
	add.l	a0,a2			 ; adjust pointer
wpn_clend
	cmp.w	d5,d1			 ; at end of list?
	blt.s	wpn_cloop		 ; ... no
wpn_cdone
	movem.l  (sp)+,reg.cnts
	rts

; IO operations on all y sections

reg.ally reg	d0/d4/d7/a2
stk_op	 equ	$00
stk_wdef equ	$14
wpn_ally
	movem.l reg.ally,-(sp)		 ; save operation
	move.l	wwa_part+wwa.clen(a3),d7 ; y part control
	beq.s	wpn_nox 		 ; none
	move.l	d7,a2
	move.w	(a2)+,d7
	beq.s	wpn_nox

	move.w	wwa_yoff(a3),d4 	 ; height of arrow row

wpn_nxtx
	move.w	wss_spos(a2),d1 	 ; start
	add.w	d4,d1
	move.w	wwa_ysiz(a3),d0 	 ; rhs
	subq.w	#1,d7			 ; last?
	ble.s	wpn_yhgt
	addq.w	#wss.ssln,a2
	move.w	wss_spos(a2),d0 	 ; end
wpn_yhgt
	sub.w	d4,d0			 ; set height
	sub.w	d1,d0
	bra.s	wpn_wsec 
	
;	   no y control block, do whole height

wpn_nox
	move.w	wwa_ysiz(a3),d0 	 ; height
	moveq	#0,d1

wpn_wsec
	lea	stk_wdef(sp),a1
	move.w	d0,ysize(a1)		 ; height to set definition
	add.w	ww_yorg(a4),d1		 ; absolute y origin
	add.w	wwa_yorg(a3),d1
	move.w	d1,yorg(a1)		 ; set it
	bsr.l	wpn_wdef

	move.w	d6,d1			 ; pan distance (if it is)
	move.l	stk_op(sp),d0
	bsr.l	wpn_trp3

	tst.w	d7			 ; more sections?
	bgt.s	wpn_nxtx
	movem.l (sp)+,reg.ally
	rts
	end
