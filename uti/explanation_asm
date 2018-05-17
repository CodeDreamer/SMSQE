; Show explanatory text for window objects	1998 Jochen Merz

	section utility

	include dev8_keys_wstatus
	include dev8_keys_qdos_pt
	include dev8_keys_wwork
	include dev8_keys_wman
	include dev8_keys_con
	include dev8_keys_colour
	include dev8_mac_menu_long
	include dev8_keys_qlv
	include dev8_keys_qdos_io
	include dev8_mac_xref

	xdef	ut_explanation

;+++
; This routine should be called right after the return from the RPTR call.
; It will show an explanatory text near the current object, which disappears
; when the pointer is moved or a key is pressed.
;
;		Entry				Exit
;
;;;;	   D7.b    colourways  ;;;;
;	A0	window channel ID		preserved
;	A1	status area			preserved
;	A2	window manager vector		preserved
;	A3	ptr to explanation table
;	A4	working definition
;
; The explanation table is defined as follows:
;
;	dc.l	ptr to appl.sw explanation table-* (or 0) (not implemented)
;	dc.l	ptr to loose menu item expl. table-* (or 0)
;	dc.l	ptr to info window expl. table-* (or 0) (not implemented)
;
;    appl.sw explanation table:
;	dc.w	highest subwindow nr. for which expl. exists
;	dc.w	ptr to explanatory text string for first sw-* (or 0)
;	...
;
;    loose menu explanation table:
;	dc.w	highest item nr. for which expl. exists
;	dc.w	ptr to explanatory text string for first item-* (or 0)
;	...
;
;    info window expl. table:
;	dc.w	highest info window nr. for which expl. exists
;	dc.w	ptr to info item table-* (or 0)
;	...
;
;    info item expl. table:
;	dc.w	highest info item nr. for which expl. exists
;	dc.w	ptr to info item text string-* (or 0)
;	...
;---
ut_explanation

xsize	equ	$00
ysize	equ	$02
xorg	equ	$04
yorg	equ	$06
reglist reg	d0-d3/a0-a4
stk.a2	equ	24
stk.a0	equ	16

	movem.l reglist,-(sp)
	tst.l	wsp_swnr(a1)		; pointer within subwindow?
	bpl.s	expl_return
	move.l	wsp_xpos(a1),d1 	; current pointer position
	move.l	ww_plitm(a4),d0 	; loose menu item list?
	beq.s	expl_return		; does not exist!
	move.l	d0,a2			; loose menu item list
	addq.l	#4,a3
	move.l	(a3),d0 		; do we have an explanation list?
	beq.s	expl_return		; no, that's it!

	moveq	#0,d2			; item number counter
expl_litem_loop
	move.l	d1,d0			; first try y
	sub.w	yorg(a2),d0		; off top?
	blt.s	expl_next		; ... yes
	cmp.w	ysize(a2),d0		; off bottom?
	bge.s	expl_next		; ... yes
*
	swap	d0			; now try x
	sub.w	xorg(a2),d0		; off left?
	blt.s	expl_next		; ... yes
	cmp.w	xsize(a2),d0		; off right?
	bge.s	expl_next		; ... yes
					; ... no, it's in
	add.l	(a3),a3 		; loose menu explanation table
	cmp.w	(a3)+,d2		; item in list?
	bgt.s	expl_return		; no, off
	add.w	d2,d2			; index into table
	add.w	d2,a3
	add.w	(a3),a3 		; string should start here
	tst.w	(a3)			; any string here?
	beq.s	expl_return
	move.l	stk.a2(sp),a2		; retrieve wman vector
	bra.s	expl_show_item

expl_next
	addq.w	#1,d2
	add.w	#wwl.elen,a2		; next item
	tst.w	(a2)			; end of list?
	bmi.s	expl_return		; yes, done
	bra.s	expl_litem_loop

expl_return
	movem.l (sp)+,reglist
	moveq	#0,d0
	rts

;+++
; Show explanatory item
;
;		Entry				Exit
;	a1	status area of underlying window
;	a3	ptr to explanatory string
;---
expl_show_item
	movem.l d4/a1/a3,-(sp)

	move.l	a3,a1			; this is our string
	xjsr	ut_gwszs		; get window size for the string

	move.w	d1,d0
	mulu	#6,d0			; nr. of characters
	addq.w	#2,d0			; 2 plus a bit of space
	move.l	d1,d4
	swap	d4
	subq.w	#1,d4			; two lines?
	beq.s	no_space_for_sprites
	move.w	d0,d4			; position of sprite
	addq.w	#4,d4
	add.w	#12,d0			; enlarge window to accomodate sprites
no_space_for_sprites

	swap	d1
	mulu	#10,d1			; nr of lines
	swap	d1
	move.w	d0,d1
	swap	d1
	move.l	d1,d3

	move.l	#wwa.explanation,d1
	xjsr	ut_alchp	; allocate some memory
	move.l	a0,a4		; it's the working definition

	move.l	d3,d1
	lea	wst_explanation(a6),a1
	lea	men_explanation,a3
	xjsr	ut_wm_setup
	moveq	#-1,d1
	jsr	wm.pulld(a2)

	movem.l a1-a2,-(sp)
;;;	   moveq   #3,d1		   ; now get right colours from table
;;;	   and.l   d7,d1
;;;	   add.w   d1,d1
;;;	   add.w   d1,d1
;;;	   move.l  colour_tab(pc,d1.w),d1
;;;	   bra.s   skip_table

;;;colour_tab
;;;	   dc.l    $00040007
;;;	   dc.l    $00020000
;;;	   dc.l    $00020007
;;;	   dc.l    $00040000

;;;skip_table
	moveq	#1,d2
	move.w	#c.hbord,d1
	moveq	#iow.defb,d0
	jsr	wm.trap3(a2)		 ; we need it for extended colours

	move.w	#c.hink,d1
	moveq	#iow.sink,d0
	jsr	wm.trap3(a2)		 ; we need it for extended colours

	move.w	#c.hback,d1
	moveq	#iow.spap,d0
	jsr	wm.trap3(a2)		 ; we need it for extended colours

	move.w	#c.hback,d1
	moveq	#iow.sstr,d0
	jsr	wm.trap3(a2)		 ; we need it for extended colours

	moveq	#iow.clra,d0
	xjsr	gu_iow

	move.l	4+8+4(sp),a1
	move.w	ut.wtext,a2
	jsr	(a2)
	movem.l (sp)+,a1-a2

	tst.w	d4
	beq.s	no_sprites

	move.l	a1,-(sp)
	xlea	mes_msleft,a1
	move.w	d4,d1
	swap	d1
	move.w	#1,d1
	moveq	#iop.wspt,d0
	xjsr	gu_iow
	xlea	mes_msright,a1
	add.w	#10,d1
	moveq	#iop.wspt,d0
	xjsr	gu_iow
	move.l	(sp)+,a1
no_sprites

	moveq	#0,d1			; set relative to nothing
	moveq	#1,d2			; the relative flag
	moveq	#iop.sptr,d0
	xjsr	gu_iow

	movem.l d1/a1/a2,-(sp)
	moveq	#0,d1			; ignore
	moveq	#0,d2			; .. no bytes to be modified
	sub.l	a1,a1
	moveq	#iop.slnk,d0
	xjsr	gu_iow
	move.l	pt_dumq1(a1),a1
	lea	sd.dq2(a1),a1
	move.l	a1,d4			; dummy queue address
	movem.l (sp)+,d1/a1/a2

	move.l	a1,a3
	lea	ws_point(a1),a1 	; the pointer record
	moveq	#pt.move+pt.kystk,d2	; termination vector
	moveq	#iop.rptr,d0
	xjsr	gu_iow
	move.b	pp_kstrk(a1),d2 	; keep keystroke
	move.l	a3,a1

	xjsr	ut_unset		; unset window definition first
	xjsr	gu_fclos		; then close channel

	moveq	#1,d0			; allow for reschedule
	xjsr	gu_pause

	moveq	#0,d1
	move.b	d2,d1			; terminated by keystroke?
	beq.s	no_keystroke
	move.l	a2,-(sp)
	move	sr,d3
	trap	#0			; make atomic
	move.l	d4,a2			; pointer to queue
	move.w	ioq.pbyt,a3
	jsr	(a3)			; put byte into queue
	move.w	d3,sr			; back to user mode
	move.l	(sp)+,a2

no_keystroke
	movem.l (sp)+,d4/a1/a3

	bra	expl_return

window_samesection  macro   name
	common	wal_[name]
	common	wst_[name]
	section utility
	xdef	men_[name]
men_[name]
currw	setstr	[name]
wsizes	setstr	{}
maxitem setnum	0
wal_[currw] setnum 0
	endm

    window_samesection	explanation
	size	2,0,4,4
	origin	0,0
	wattr	0,1,c.hbord,c.hback	;0,0
	sprite	invisible
	border	1,c.mhigh		;0
	iattr	c.mpunav,c.miunav,0,0	; unavailable
	iattr	c.mpavbl,c.miavbl,0,0	; available
	iattr	c.mpslct,c.mislct,0,0	; selected
	help	0

    size_opt	a
	size	2,0,4,4
	info	0
	loos	0
	appn	0
    s_end

	setwrk

	end
