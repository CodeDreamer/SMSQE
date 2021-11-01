; Scroll a menu sub-window  V1.04    1986  Tony Tebby	 QJUMP
* 2020-08-13	1.06	modif for index items partial drawing (AH)

	section wman

	xdef	wm_scrol

	xref	wm_swdef
	xref	wm_mdraw
	xref	wm_mpdrw
	xref	wm_index
;	xref	wm_drbar	;(AH)

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
;	d6	   scroll distance
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
frame	equ	$08			 ; dummy spacing list
stk_a4	equ	$20

wm_scrol
	movem.l reglist,-(sp)		 ; save register list
	subq.l	#frame,sp
	jsr	wm_swdef		 ; set sub window
	move.l	wwa_part+wwa.clen(a3),a2 ; find part window control block
	move.w	(a2)+,d7		 ; number of sections
	subq.w	#1,d7			 ; ... highest section number
	moveq	#$7f,d1 		 ; find section number
	and.w	d2,d1
	moveq	#wss.ssln,d0
	mulu	d1,d0
	add.w	d0,a2			 ; pointer to section

	move.w	d2,d1			 ; keep item number
	move.l	d3,d4			 ; and hit on bar
	move.l	wwa_ncol(a3),d5 	 ; preset redraw end

;	set section parameters	d2 start / d3 end

	move.w	wss_spos(a2),d2 	 ; top of section
	move.w	wwa_yoff(a3),d6 	 ; offset
	add.w	wwa_curw+wwa_iatt(a3),d6 ; ... plus current item border width
	add.w	d6,d2

	move.w	wss_spos+wss.ssln(a2),d3 ; end of section
	cmp.b	d1,d7			 ; last section?
	bne.s	wsu_sbot		 ; ... no
	move.w	wwa_ysiz(a3),d3
wsu_sbot
	sub.w	d6,d3			 ; allow margin

	btst	#wsi..bar,d1		 ; hit on bar?
	beq.l	wps_arrow		 ; ... no, arrow or key
	btst	#wsi..xt,d1		 ; do on bar?
	beq.l	wps_sbar		 ; ... no
	tst.l	d4			 ; do on join?
	bge.s	wps_split		 ; ... no

	sub.w	wss_spos(a2),d2 	 ; find new start of section
	add.w	wss_spos-wss.ssln(a2),d2
	move.l	a2,d0			 ; save current pointer
wps_jloop
	addq.b	#1,d1			 ; next section
	cmp.b	d7,d1			 ; last section?
	bgt.s	wps_jset		 ; ... yes
	move.w	6(a2),(a2)+
	move.w	6(a2),(a2)+
	move.w	6(a2),(a2)+
	bra.s	wps_jloop

wps_jset
	move.l	d0,a2
	subq.l	#wss.ssln,a2		 ; previous section
	move.w	wss_sstt(a2),d0
	bsr.l	wps_cnts
	sub.w	d0,d1
	move.w	d1,wss_ssiz(a2) 	 ; set size of section
	move.l	wwa_part+wwa.clen(a3),a2
	subq.w	#1,(a2) 		 ; set new number of sections
	bra.s	wps_drall		 ; and redraw whole window

wps_split
	addq.w	#1,d7
	cmp.w	wwa_nysc(a3),d7 	 ; number of control sections
	bge.l	wps_exok		 ; at max already
	swap	d1			 ; keep section number
	swap	d4			 ; position of split
	add.w	d2,d4			 ; relative to window
	exg	d3,d4			 ; set temporary end of section
	sub.w	d6,d3			 ; actual limit
	move.w	wss_sstt(a2),d0 	 ; first item
	bsr.l	wps_cnts		 ; count up
	cmp.w	d0,d1			 ; any in section?
	ble.l	wps_exok		 ; ... no

	swap	d2			 ; keep start of this section
	move.w	d7,d2			 ; end of this section
	add.w	d6,d2			 ; real end
	add.w	d6,d2
	move.w	d4,d3			 ; the old end
	swap	d0
	move.w	d1,d0
	bsr.l	wps_cnts		 ; count up
	cmp.w	d0,d1			 ; any in section?
	ble.l	wps_exok		 ; ... no

	swap	d1			 ; split section number
	move.l	wwa_part+wwa.clen(a3),a2
	move.w	(a2),d7
	addq.w	#1,(a2)+		 ; one more section
	moveq	#wss.ssln,d3
	mulu	d7,d3
	lea	wss.ssln(a2,d3.l),a2	 ; end of control table
	bra.s	wps_scpe
wps_scpy
	move.w	-8(a2),-(a2)
	move.w	-8(a2),-(a2)
	move.w	-8(a2),-(a2)
wps_scpe
	subq.b	#1,d7
	cmp.b	d1,d7
	bgt.s	wps_scpy

	swap	d1			 ; set new section
	sub.w	d0,d1
	move.w	d1,-(a2)		 ; number of items
	move.w	d0,-(a2)		 ; start item
	sub.w	d6,d2
	move.w	d2,-(a2)		 ; start pixel position
	sub.w	wss_sstt-wss.ssln(a2),d0
	move.w	d0,-(a2)		 ; number of items in split section
wps_drall
	moveq	#iow.clra,d0
	bsr.l	wps_trp3		 ; clear window
	moveq	#0,d3
	move.l	stk_a4(sp),a4
	jsr	wm_mdraw		 ; redraw menu
	jsr	wm_index		 ; and index items, bars and arrows
	bra.l	wps_exit


wps_sbar
	move.w	d5,d0			 ; find item at middle of section
	add.w	d0,d0
	swap	d4
	mulu	d4,d0
	swap	d4
	divu	d4,d0
	sub.w	wss_ssiz(a2),d0
	addq.w	#1,d0
	asr.w	#1,d0
	bge.s	wps_rdbar
	moveq	#0,d0
wps_rdbar
	cmp.w	wss_sstt(a2),d0 	 ; any change?
	beq.l	wps_exok		 ; ... no
	bra.s	wps_rdraw

wps_arrow
	cmp.w	#1,wss_ssiz(a2) 	 ; single item?
	bgt.s	wps_dir 		 ; ... no
	bclr	#wsi..xt,d1		 ; ... yes, do same as hit!
wps_dir
	btst	#wsi..dnr,d1		 ; is direction down / right?
	bne.s	wps_down		 ; ... down

	move.w	wss_sstt(a2),d0 	 ; old section start
	beq.l	wps_exok		 ; ... already at top
	btst	#wsi..xt,d1		 ; extra?
	beq.s	wps_up1 		 ; ... no

	move.w	d0,d4			 ; new bottom row
	addq.w	#1,d4

wps_uplp
	sub.w	#1,d0			 ; go up
	blt.s	wps_bdn 		 ; ... too far
	bsr.l	wps_cnts
	cmp.w	d4,d1			 ; past it?
	beq.s	wps_rdraw		 ; ... there, redraw
	blt.s	wps_bdn 		 ; ... yes, go back down
	bra.s	wps_uplp

wps_bdn
	addq.w	#1,d0			 ; back one
	bra.s	wps_rdraw		 ; and redraw

wps_up1
	subq.w	#1,d0
	move.w	d0,wss_sstt(a2) 	 ; set new section start

	moveq	#0,d4			 ; set redraw start x
	move.w	d0,d4			 ; and y

	bsr.l	wps_cnts		 ; count rows in section
	sub.w	d0,d1
	move.w	d1,wss_ssiz(a2) 	 ; number of rows
	move.w	d4,d5
	addq.w	#1,d5			 ; reset redraw end y (just one)

	sub.w	d3,d7			 ; set amount to clear
	neg.w	d7
	sub.w	d2,d3			 ; set total window height
	move.w	wwm_spce(a4),d6 	 ; and scroll distance
	bra.s	wps_scroll


wps_down
	move.w	wss_sstt(a2),d0 	 ; section start
	moveq	#0,d4			 ; set redraw start x
	move.w	d0,d4
	add.w	wss_ssiz(a2),d4 	 ; and y

	cmp.w	wwa_nrow(a3),d4 	 ; off end?
	bge.l	wps_exok		 ; ... yes

	btst	#wsi..xt,d1		 ; extra?
	beq.s	wps_dn1 		 ; ... no
	subq.w	#1,d4
	move.w	d4,d0			 ; new top

wps_rdraw
	clr.l	d4			 ; redraw start
	move.w	d0,d4
	bsr.l	wps_cnts
	move.w	d0,wss_sstt(a2) 	 ; set top
	move.w	d1,d5			 ; redraw end
	sub.w	d0,d1
	move.w	d1,wss_ssiz(a2) 	 ; and section size
	moveq	#0,d6			 ; scroll none
	sub.w	d2,d3			 ; window height
	move.w	d3,d7			 ; clear all
	bra.s	wps_scroll


wps_dn1
	addq.w	#1,d0			 ; new start
	move.w	d0,wss_sstt(a2) 	 ; set new start

	bsr.l	wps_cnts		 ; count rows in section

	move.w	d1,d5			 ; reset end of redraw
	sub.w	d0,d1			 ; number in section
	move.w	d1,wss_ssiz(a2) 	 ; ... set it

	moveq	#0,d7			 ; clear none
	sub.w	d2,d3			 ; window height
	move.w	wwm_spce-wwm.slen(a4),d6 ; scroll by old spacing
	neg.w	d6

; scroll the section

wps_scroll
	move.l	stk_a4(sp),a4		 ; reset pointer to working definition
	add.w	wwa_yorg(a3),d2 	 ; set y origin of scroll area
	add.w	ww_yorg(a4),d2
	move.l	d2,-(sp)		 ; and dummy x org

	move.w	d3,-(sp)		 ; y size
	subq.w	#2,sp			 ; and x size

	tst.w	d6			 ; any scroll?
	beq.s	wps_clra		 ; ... no, clear all
	sub.w	d7,ysize(sp)		 ; do not scroll cleared bit
	ble.s	wps_clear
	moveq	#iow.scra,d0		 ; scroll
	bsr.l	wps_allx		 ; all x sections
wps_clear
	move.w	d7,ysize(sp)		 ; set clear size
	ble.s	wps_rdef		 ; ... nothing to clear
	add.w	d3,yorg(sp)		 ; set clear origin
	sub.w	d7,yorg(sp)
wps_clra
	moveq	#iow.clra,d0		 ; clear bit of
	bsr.l	wps_allx		 ; all x sections

wps_rdef
	move.l	wwa_xsiz(a3),xsize(sp)	 ; reset window
	move.l	wwa_xorg(a3),d0
	add.l	ww_xorg(a4),d0		 ; absolute origin
	move.l	d0,xorg(sp)
	move.l	sp,a1
	bsr.s	wps_wdef
	addq.l	#8,sp			 ; remove junk

	jsr	wm_mpdrw		 ; redraw defined part of menu
	jsr	wm_index		 ; and index items, bars and arrows
;;	jsr	wm_drbar		 ; redraw bars (AH)

wps_exok
	moveq	#0,d0
wps_exit
	moveq	#0,d3			 ; cancel actions
	moveq	#0,d4
	addq.l	#frame,sp
	movem.l (sp)+,reglist
	tst.l	d0
	rts

; screen io calls

wps_wdef
	moveq	#iow.defw,d0		 ; define window
	moveq	#0,d2			 ; no border
wps_trp3
	move.l	d3,-(sp)		 ; save d3
	moveq	#-1,d3
	trap	#3			 ; do trap
	move.l	(sp)+,d3
	tst.l	d0			 ; error?
	rts

; count rows in section

reg.cnts reg	d6/a0/a2
stk_cnts equ	$10
wps_cnts
	movem.l reg.cnts,-(sp)
	move.l	wwa_yspc(a3),d6 	 ; spacing list pointer
	blt.s	wps_cspc		 ; ... constant
	move.l	d6,a4
	moveq	#0,d7
	move.w	d0,d7
	lsl.l	#2,d7			 ; index spacing list
	add.l	d7,a4
	sub.l	a0,a0			 ; keep going
	bra.s	wps_cdo
wps_cspc
	lea	stk_cnts(sp),a4 	 ; dummy spacing list is here
	swap	d6
	neg.w	d6
	swap	d6
	neg.w	d6
	move.l	d6,(a4)+
	move.l	d6,(a4)
	move.w	#-4,a0			 ; keep backspacing
wps_cdo
	move.l	a4,a2
	move.w	d0,d1			 ; find last row
	move.w	d2,d6
	move.w	d6,d7			 ; save up to here
	bra.s	wps_clend
wps_cloop
	add.w	(a2),d6 		 ; end of this hit area
	cmp.w	d6,d3			 ; room for this hit area?
	blt.s	wps_cdone		 ; ... no
	addq.w	#1,d1			 ; ... yes
	move.w	d6,d7			 ; we've got this one
	sub.w	(a2)+,d6
	add.w	(a2)+,d6		 ; move on to start of next
	add.l	a0,a2			 ; adjust pointer
wps_clend
	cmp.w	d5,d1			 ; at end of list?
	blt.s	wps_cloop		 ; ... no
wps_cdone
	movem.l  (sp)+,reg.cnts
	rts

; IO operations on all x sections

reg.allx reg	d0/d4/d7/a2
stk_op	 equ	$00
stk_wdef equ	$14
wps_allx
	movem.l reg.allx,-(sp)		 ; save operation
	move.l	wwa_part(a3),d7 	 ; x part control
	beq.s	wps_nox 		 ; none
	move.l	d7,a2
	move.w	(a2)+,d7
	beq.s	wps_nox

	move.w	wwa_xoff(a3),d4 	 ; x offset

wps_nxtx
	move.w	wss_spos(a2),d1 	 ; start
	add.w	d4,d1
	move.w	wwa_xsiz(a3),d0 	 ; rhs
	subq.w	#1,d7			 ; last?
	ble.s	wps_xwid
	addq.w	#wss.ssln,a2
	move.w	wss_spos(a2),d0 	 ; end
wps_xwid
	sub.w	d4,d0			 ; set width
	sub.w	d1,d0
	bra.s	wps_wsec

;	   no x control block, do whole width

wps_nox
	move.w	wwa_xsiz(a3),d0 	 ; width
	moveq	#0,d1

wps_wsec
	lea	stk_wdef(sp),a1 	 ; window definition
	move.w	d0,xsize(a1)		 ; width to set definition
	add.w	ww_xorg(a4),d1		 ; absolute x origin
	add.w	wwa_xorg(a3),d1
	move.w	d1,xorg(a1)		 ; set it
	bsr.l	wps_wdef

	move.w	d6,d1			 ; scroll distance (if it is)
	move.l	stk_op(sp),d0
	bsr	wps_trp3

	tst.w	d7			 ; more sections?
	bgt.s	wps_nxtx
	movem.l (sp)+,reg.allx
	rts
	end
