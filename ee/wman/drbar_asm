; Draw PAN and SCROLL bars   V1.10		 1989	Tony Tebby  QJUMP
;							Marcel Kilgus
;
; 2002-13-11  1.02  Adapted for high colour use (MK)
; 2002-23-12  1.10  Completely changed the way bars are drawn. New routines
;		    use a window border to display section (MK)
; 2002-27-12  1.11  Experimental implementation of the so far unused
;		    "bar background" property (MK)

	section wman

	xdef	wm_drbar
	xdef	wm_upbar

	xref	wmc_trap3

	include 'dev8_keys_wwork'
	include 'dev8_keys_wstatus'
	include 'dev8_keys_qdos_io'

;+++
; Update pan/scroll bars
;
;	d0 cr	msw/lsw x/y section to update (-1 none)
;	a0 c  p channel ID of window
;	a3 c  p pointer to sub-window definition
;	a4 c  p pointer to working definition
;
;		all other registers preserved
;---
s.ws	equ	0			 ; working size
s.wo	equ	4			 ; working origin
s.bs	equ	8			 ; bar size
s.bo	equ	12			  ; bar origin
s.bw	equ	16			  ; bar border width
s.bd	equ	18			 ; basically just double border width
frame	equ	20			 ; frame size

wm_upbar
reglist reg d1-d7/a1-a4
	movem.l reglist,-(sp)
	sub.l	#frame,sp
	move.l	sp,a1
	move.l	d0,d6			; keep section info
	swap	d6
	tst.w	d6			; do any section?
	blt.s	wub_doy 		; ... no
	move.l	wwa_part(a3),d0 	; any x control?
	beq.s	wub_doy 		; ... no
	move.l	d0,a2
	tst.w	(a2)			; any sections?
	beq.s	wub_doy

	moveq	#0,d7			; item border
	moveq	#0,d5

	move.l	wwa_psbc(a3),d4 	; colours
	beq.s	wub_doy 		; ... none
	swap	d4

	move.w	wwa_xsiz(a3),d0 	; pan bar window size
	move.w	wwa_xorg(a3),d2
	moveq	#ww.pnbar,d1
	move.w	wwa_yorg(a3),d3
	add.w	wwa_ysiz(a3),d3
	add.w	wwa_watt+wwa_borw(a3),d3
	movem.w d0-d3,s.bs(a1)		; save bar def
	movem.w d5/d7,s.bw(a1)

	move.w	d6,d3
	move.w	(a2)+,d6
	sub.w	d3,d6			; number of sections after our section
	mulu	#wss.ssln,d3
	add.l	d3,a2			; our section control
	sne	d3
	ext.w	d3
	neg.w	d3

	move.w	wwa_ncol(a3),d5 	; number of columns
	bsr.l	wdb_oner

wub_doy
	moveq	#0,d7
	moveq	#0,d5
	swap	d6
	tst.w	d6			 ; y section to be drawn
	blt.l	wdb_ok			 ; ... no
	move.l	wwa_part+wwa.clen(a3),d0 ; any y control?
	beq.l	wdb_ok			 ; ... no
	move.l	d0,a2
	tst.w	(a2)
	beq.l	wdb_ok			 ; ... no sections

	move.l	wwa_psbc+wwa.clen(a3),d4 ; colours
	beq.l	wdb_ok			 ; ... none
	swap	d4

	move.w	wwa_ysiz(a3),d1 	 ; pan bar window size
	move.w	wwa_yorg(a3),d3
	moveq	#ww.scbar,d0
	move.w	wwa_watt+wwa_borw(a3),d2
	add.w	d2,d2
	add.w	wwa_xorg(a3),d2
	add.w	wwa_xsiz(a3),d2
	movem.w d0-d3,s.bs(a1)		 ; save bar def
	movem.w d5/d7,s.bw(a1)

	move.w	d6,d3
	move.w	(a2)+,d6
	sub.w	d3,d6			 ; number of sections after our section
	mulu	#wss.ssln,d3
	add.l	d3,a2			 ; our section control
	sne	d3
	ext.w	d3
	neg.w	d3

	move.w	wwa_nrow(a3),d5 	 ; number of rows
	moveq	#2,d7			 ; ... offset to y position
	bsr.l	wdb_oner
	bra.l	wdb_ok

;+++
; Draw pan/scroll bars
;
;	d0  r	error return
;	a0 c  p channel ID of window
;	a3 c  p pointer to sub-window definition
;	a4 c  p pointer to working definition
;
;		all other registers preserved
;---
wm_drbar
	move.l	wwa_part+wwa.clen(a3),d0 ; get y part control pointer
	or.l	wwa_part(a3),d0 	 ; get x part control pointer
	bne.s	wdb_do			 ; ... controlled
	rts

wdb_do
	movem.l reglist,-(sp)
	sub.l	#frame,sp
	move.l	sp,a1
	move.l	wwa_part(a3),d0 	 ; any x control?
	beq.s	wdb_doy 		 ; ... no
	move.l	d0,a2

	move.w	wwa_watt+wwa_borw(a3),d7 ; item border
	move.w	d7,d5
	add.w	d5,d5			 ; doubled

	move.l	wwa_psbc(a3),d4 	 ; colours
	beq.s	wdb_doy 		 ; ... none
	swap	d4

	move.w	wwa_xsiz(a3),d0 	 ; pan bar window size
	move.w	wwa_xorg(a3),d2
	moveq	#ww.pnbar,d1
	move.w	wwa_yorg(a3),d3
	add.w	wwa_ysiz(a3),d3
	add.w	d7,d3
	movem.w d0-d3,s.bs(a1)		 ; save bar def
	movem.w d5/d7,s.bw(a1)
	move.w	(a2)+,d6		 ; how many sections?
	ble.s	wdb_clrx		 ; ... none

	move.w	wwa_ncol(a3),d5 	 ; number of columns
	moveq	#0,d7			 ; ... no offset to x position
	bsr	wdb_range
	bra.s	wdb_doy

wdb_clrx
	move.l	s.bs(a1),s.ws(a1)
	move.l	s.bo(a1),s.wo(a1)
	bsr	wdb_wdef
	bne.s	wdb_doy
	moveq	#iow.clra,d0		 ; clear all
	bsr	wdb_trap3

wdb_doy
	move.l	wwa_part+wwa.clen(a3),d0 ; any y control?
	beq.s	wdb_ok			 ; ... no
	move.l	d0,a2

	move.w	wwa_watt+wwa_borw(a3),d7 ; item border
	move.w	d7,d5
	add.w	d5,d5			 ; doubled
	move.l	wwa_psbc+wwa.clen(a3),d4 ; scroll bar | scroll bar section colours
	beq.s	wdb_ok			 ; ... none
	swap	d4			 ; bar section | bar colours

	move.w	wwa_ysiz(a3),d1 	 ; pan bar window size
	move.w	wwa_yorg(a3),d3
	moveq	#ww.scbar,d0
	move.w	wwa_xorg(a3),d2
	add.w	wwa_xsiz(a3),d2
	add.w	d5,d2
	movem.w d0-d3,s.bs(a1)		 ; save bar def
	movem.w d5/d7,s.bw(a1)
	move.w	(a2)+,d6		 ; how many sections?
	ble.s	wdb_clry		 ; ... none

	move.w	wwa_nrow(a3),d5 	 ; number of rows
	moveq	#2,d7			 ; ... offset to y position
	bsr.s	wdb_range
	bra.s	wdb_ok

wdb_clry
	move.l	s.bs(a1),s.ws(a1)
	move.l	s.bo(a1),s.wo(a1)
	bsr	wdb_wdef
	bne.s	wdb_ok
	moveq	#iow.clra,d0		 ; no sections, clear area
	bsr	wdb_trap3

wdb_ok
	moveq	#0,d0
	add.l	#frame,sp
	movem.l (sp)+,reglist
wdb_rts
	rts


wdb_range
	moveq	#0,d3
wdb_rloop
	bsr.s	wdb_oner
	bne.s	wdb_rts
	moveq	#1,d3			 ; offset for extra sections
	tst.w	d6
	bgt.s	wdb_rloop
	rts

wdb_oner
	add.w	(a2)+,d3		 ; start pixel position
	move.w	(a2)+,d1		 ; start item number
	move.w	(a2)+,d2		 ; number of items visible in section
	move.w	wwa_xsiz(a3,d7.w),d0	 ; end pixel position?
	subq.w	#1,d6			 ; last section?
	beq.s	wdb_srng		 ; ... yes
	move.w	(a2),d0 		 ; ... no, end position
	subq.w	#1,d0
wdb_srng
	sub.w	d3,d0			 ; pixel range

	movem.l d0-d2/d7/a1,-(sp)
	move.l	s.bs(a1),s.ws(a1)
	move.l	s.bo(a1),s.wo(a1)
	move.w	d0,s.ws(a1,d7.w)
	add.w	d3,s.wo(a1,d7.w)
	move.w	wwa_watt+wwa_borw(a3),d7
	bsr.s	wdb_wdef		 ; set background
	bne.s	wdb_wderr
	moveq	#iow.spap,d0
	move.w	d4,d1			 ; scroll bar colour
	bsr	wdb_trap3
	moveq	#iow.clra,d0
	bsr	wdb_trap3		 ; clear it
wdb_wderr
	movem.l (sp)+,d0-d2/a1/d7
	bne.s	wdb_rts

	sub.w	d2,d5			 ; item range not occupied by visible
	cmp.w	d1,d5			 ; less than start?
	bge.s	wdb_vrng		 ; ... no
	move.w	d1,d5			 ; ... yes, reset it
wdb_vrng
	move.w	d4,-(sp)
	move.w	d5,d4
	add.w	d2,d4			 ; back to total range again

	mulu	d0,d2			 ; find length of block
	beq.s	wdb_incb		 ; ... none, increase the block length
	subq.w	#1,d2
	divu	d4,d2			 ; rounded up
wdb_incb
	addq.w	#1,d2
	move.w	(sp)+,d4
	sub.w	d2,d0			 ; pixel range not occupied by visible

	mulu	d0,d1			 ; find start of range
	beq.s	wbd_offb
	divu	d5,d1			 ; rounded down
wbd_offb
	add.w	d3,d1
	move.l	s.bs(a1),s.ws(a1)
	move.l	s.bo(a1),s.wo(a1)
	add.w	d1,s.wo(a1,d7.w)	 ; set start of block
	move.w	d2,s.ws(a1,d7.w)	 ; and width

	move.l	d1,-(sp)
	move.l	ww_xorg(a4),d1
	add.l	d1,s.wo(a1)		 ;	... abs
	move.l	d4,d1
	swap	d1
	moveq	#1,d2			 ; single border
	moveq	#iow.defw,d0
	bsr.s	wdb_trap3
	move.w	wwa_watt+wwa_papr(a3),d1
	cmp.w	d4,d1			 ; bar background same as window bg?
	beq.s	wdb_nobarback
	moveq	#iow.spap,d0
	bsr.s	wdb_trap3
	moveq	#iow.clra,d0
	bsr.s	wdb_trap3		 ; clear it
wdb_nobarback
	movem.l (sp)+,d1
	rts

wdb_wdef
	movem.l d1-d3/d5,-(sp)
	movem.w s.ws(a1),d0-d3
	move.w	d7,d5
	add.w	d5,d5
	add.w	d5,d0
	add.w	d5,d0			 ; real x size
	add.w	d5,d1			 ;	y size
	sub.w	d5,d2			 ;	x origin
	add.w	ww_xorg(a4),d2		 ;	... abs
	sub.w	d7,d3			 ;	y origin
	add.w	ww_yorg(a4),d3		 ;	... abs

	movem.w d0-d3,s.ws(a1)		 ; window def

	move.w	wwa_watt+wwa_borc(a3),d1
	move.w	d7,d2
	moveq	#iow.defw,d0
	bsr.s	wdb_trap3
	movem.l (sp)+,d1-d3/d5
	rts

wdb_trap3
	movem.l d3/a1,-(sp)
	moveq	#forever,d3
	jsr	wmc_trap3
	movem.l (sp)+,d3/a1
	tst.l	d0
	rts

	end
