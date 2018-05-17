; Draw 3d border	V1.03				2002 Marcel Kilgus
;
; 2003-05-02  1.01  Implemented the err.orng error handling I forgot (MK)
; 2003-11-12  1.02  Fixed top and bottom line borders (MK)
; 2005-04-07  1.03  Double border type fixed

	section wman

	xdef	wm_3db
	xdef	wm_rmbrdr
	xdef	wm_bordersize

	xref	wmc_trap3
	xref	wmc_colour

	include 'dev8_keys_con'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_qdos_io'
	include 'dev8_keys_syspal'
	include 'dev8_keys_wman'
	include 'dev8_keys_err'

; Reset macro counter
s.cnt  setnum  0

; Generate labels
vw	macro	name
[name]	equ	[s.cnt]
s.cnt	setnum	[s.cnt]+2
	endm

; working variables (on stack)
	vw	s.bxs		; block size - must be first!
	vw	s.bys
	vw	s.bxo		; block origin
	vw	s.byo

	vw	s.code		; requested colour code
	vw	s.xws		; window size
	vw	s.yws
	vw	s.xwo
	vw	s.ywo

	vw	s.bwidth	; border width
	vw	s.bcol		; border colour
	vw	s.col1
	vw	s.col2

s.len	equ	[s.cnt] 	; size of block

;+++
; Special 3d border routine
;
;	d0  r	status
;	d1 c p	border colour code
;	d2 c p	border width
;	d3 c p	timeout
;---
wm_3db
	movem.l d1-d5/a0-a3/a6,-(sp)
	suba.w	#s.len,sp	; reserve enough space on stack
	move.l	sp,a1
	move.w	d2,s.bwidth(a1) ; save border width
	move.w	d1,s.bcol(a1)	; save border "colour"

	moveq	#-1,d3
	move.w	#sp.3dlight,d0	; raised colour
	move.w	#sp.3ddark,d2	; lowered colour
	btst	#0,d1
	beq.s	w3d_raised
	exg	d0,d2
w3d_raised
	move.w	d0,s.col1(a1)
	move.w	d2,s.col2(a1)

	moveq	#iow.xtop,d0
	lea	w3d_getdata,a2	; get some data out of CDB (wsize etc).
	trap	#3
	tst.l	d0
	bne.s	w3d_exit

	move.w	s.bcol(a1),d1
	move.w	s.bwidth(a1),d2
	bsr	wm_bordersize
	move.l	s.xws(a1),d1
	cmp.l	d1,d2
	bgt.s	w3d_orng	; does x border size fit in window?
	cmp.w	d1,d2
	bgt.s	w3d_orng	; and y?

	move.w	s.bcol(a1),d0
	move.w	s.bwidth(a1),d1
	andi.w	#%1110,d0
	move.w	wb_mtab(pc,d0.w),d0
	jsr	wb_mtab(pc,d0.w)
	bne.s	w3d_exit

	moveq	#iow.xtop,d0
	lea	w3d_setborder,a2
	trap	#3
	tst.l	d0
w3d_exit
	adda.w	#s.len,sp	; restore stack
	movem.l (sp)+,d1-d5/a0-a3/a6
w3d_rts
	rts

w3d_orng
	moveq	#err.orng,d0
	bra.s	w3d_exit

;+++
; Border draw routines
;
;	d1 cr	border colour / window offset change
;	d2  r	window size change
;---
wb_mtab
	dc.w	wb_traditional-wb_mtab
	dc.w	wb_single-wb_mtab
	dc.w	wb_double-wb_mtab
	dc.w	wb_shadow-wb_mtab
	dc.w	wb_lineleft-wb_mtab
	dc.w	wb_lineright-wb_mtab
	dc.w	wb_linetop-wb_mtab
	dc.w	wb_linebottom-wb_mtab

; ------ QL compatible 2:1 aspect border --------------------------------------
wb_traditional
; left side first
	add.w	d1,d1			; X is double width
	move.w	d1,s.bxs(a1)
	move.w	s.yws(a1),s.bys(a1)	; full height
	clr.l	s.bxo(a1)		; origin 0,0
	bsr	w3d_block1		; colour 1

; then right side
	move.w	s.xws(a1),d0
	sub.w	d1,d0
	move.w	d0,s.bxo(a1)		; only different X origin
	bsr	w3d_block2		; colour 2

; then top and bottom simultaniously
	move.w	s.xws(a1),s.bxs(a1)	; full width first
	move.w	#1,s.bys(a1)		; draw only single lines
	clr.w	s.bxo(a1)		; x origin 0
	clr.l	d2			; top origin is 0
	move.w	s.yws(a1),d2
	subq.w	#1,d2			; bottom origin
wbt_loop
	move.w	d2,s.byo(a1)		; bottom line
	bsr	w3d_block2
	swap	d2
	move.w	d2,s.byo(a1)		; top line
	bsr	w3d_block1
	swap	d2
	add.l	#$0000ffff,d2		; top + 1, bottom - 1
	addq.w	#2,s.bxo(a1)		; next line is a bit smaller
	subq.w	#4,s.bxs(a1)
	subq.w	#2,d1			; another line to draw?
	bne.s	wbt_loop

	move.w	s.bcol(a1),d1
	move.w	s.bwidth(a1),d2
	bra	wbs_traditional 	; return origin/size change

; ----- Single 1:1 border -----------------------------------------------------
wb_single
	bsr	wb_compat		; do compatibility variants
	bne	w3d_rts
; left side first
	move.w	d1,s.bxs(a1)
	move.w	s.yws(a1),s.bys(a1)	; full height
	move.w	d4,s.bxo(a1)		; x origin dependant on compat mode
	clr.w	s.byo(a1)		; y origin 0
	bsr	w3d_block1		; colour 1

; then right side
	move.w	d5,d0
	add.w	d4,d0
	sub.w	d1,d0
	move.w	d0,s.bxo(a1)		; only different X origin
	bsr	w3d_block2		; colour 2

; then top and bottom simultaniously
	move.w	d5,s.bxs(a1)		; full width first
	move.w	#1,s.bys(a1)		; draw only single lines
	move.w	d4,s.bxo(a1)
	clr.l	d2			; top origin is 0
	move.w	s.yws(a1),d2
	subq.w	#1,d2			; bottom origin
wbs_loop
	move.w	d2,s.byo(a1)		; bottom line
	bsr	w3d_block2
	swap	d2
	move.w	d2,s.byo(a1)		; top line
	bsr	w3d_block1
	swap	d2
	add.l	#$0000ffff,d2		; top + 1, bottom - 1
	addq.w	#1,s.bxo(a1)		; next line is a bit smaller
	subq.w	#2,s.bxs(a1)
	subq.w	#1,d1			; another line to draw?
	bne.s	wbs_loop

	move.w	s.bcol(a1),d1
	move.w	s.bwidth(a1),d2
	bra	wbs_1to1		; return origin/size change

; ----- Double 1:1 border -----------------------------------------------------
wb_double
	bsr	wb_compat		; do compatibility variants
	bne	w3d_rts

	move.w	d1,d2
	add.w	d2,d2

; left outer side first
	move.w	d1,s.bxs(a1)
	move.w	s.yws(a1),d0
	sub.w	d2,d0
	move.w	d0,s.bys(a1)
	move.w	d4,s.bxo(a1)		; x origin dependant on compat mode
	move.w	d1,s.byo(a1)
	bsr	w3d_block1

; then left inner side
	add.w	d1,s.bxo(a1)
	bsr	w3d_block2

; right inner side
	move.w	d5,d0
	add.w	d4,d0
	sub.w	d2,d0
	move.w	d0,s.bxo(a1)
	bsr	w3d_block1

; now right outer side
	move.w	s.yws(a1),s.bys(a1)
	add.w	d1,s.bxo(a1)
	clr.w	s.byo(a1)
	bsr	w3d_block2

; inner bottom
	move.w	d5,d0
	sub.w	d1,d0
	move.w	d0,s.bxs(a1)
	move.w	d1,s.bys(a1)
	move.w	d4,s.bxo(a1)
	move.w	s.yws(a1),d0
	sub.w	d2,d0
	move.w	d0,s.byo(a1)
	bsr	w3d_block1

; outer bottom
	add.w	d1,s.byo(a1)
	bsr	w3d_block2

; outer top
	clr.w	s.byo(a1)
	bsr	w3d_block1

; inner top
	sub.w	d2,s.bxs(a1)
	move.w	d1,s.bxo(a1)
	add.w	d4,s.bxo(a1)
	move.w	d1,s.byo(a1)
	bsr	w3d_block2

	move.w	s.bcol(a1),d1
	move.w	s.bwidth(a1),d2
	bra	wbs_double		; return origin/size change

; ----- Single border with shadow ---------------------------------------------
wb_shadow
	move.l	s.xws(a1),d5
	sub.w	d1,d5
	swap	d5
	sub.w	d1,d5
	swap	d5			; d5 is now reduced border size

	move.w	s.bcol(a1),d2
	andi.w	#1,d2
	bne.s	wbsh_lowered

	move.l	d5,d2			; shadow origin
	moveq	#0,d4			; border origin
	bra.s	wbsh_all
wbsh_lowered
	moveq	#0,d2			; shadow origin
	move.w	d1,d4
	swap	d4
	move.w	d1,d4			; border origin
wbsh_all
; black shadow first
	move.w	s.xws(a1),s.bxs(a1)
	move.w	d1,s.bys(a1)
	clr.w	s.bxo(a1)
	move.w	d2,s.byo(a1)
	bsr	w3d_blockb

	move.w	d1,s.bxs(a1)
	move.w	d5,s.bys(a1)
	swap	d2
	move.w	d2,s.bxo(a1)
	move.w	d4,s.byo(a1)
	bsr	w3d_blockb

; left side first
	move.w	d1,s.bxs(a1)
	move.w	d5,s.bys(a1)
	move.l	d4,s.bxo(a1)
	bsr	w3d_block1		; colour 1

; then right side
	swap	d5
	move.w	d5,d0
	add.w	d4,d0
	sub.w	d1,d0
	move.w	d0,s.bxo(a1)		; only different X origin
	bsr	w3d_block2		; colour 2

; then top and bottom simultaniously
	move.w	d5,s.bxs(a1)		; full width first
	swap	d5
	move.w	#1,s.bys(a1)		; draw only single lines
	move.w	d4,s.bxo(a1)
	move.w	d4,d2			; top origin
	swap	d2
	move.w	d5,d2
	add.w	d4,d2
	subq.w	#1,d2			; bottom origin
wbsh_loop
	move.w	d2,s.byo(a1)		; bottom line
	bsr	w3d_block2
	swap	d2
	move.w	d2,s.byo(a1)		; top line
	bsr	w3d_block1
	swap	d2
	add.l	#$0000ffff,d2		; top + 1, bottom - 1
	addq.w	#1,s.bxo(a1)		; next line is a bit smaller
	subq.w	#2,s.bxs(a1)
	subq.w	#1,d1			; another line to draw?
	bne.s	wbsh_loop

	move.w	s.bcol(a1),d1
	move.w	s.bwidth(a1),d2
	bra	wbs_shadow		; return origin/size change

; ----- Lines ----------------------------------------------------------------
wb_lineleft
	moveq	#0,d4			; border origin
	moveq	#-1,d5			; there's a window origin change
	bra.s	wb_verline

wb_lineright
	moveq	#0,d4
	move.w	s.xws(a1),d4
	sub.w	d1,d4
	sub.w	d1,d4
	swap	d4			; border origin
	moveq	#0,d5			; no window origin change

wb_verline
	move.w	d1,s.bxs(a1)
	move.w	s.yws(a1),s.bys(a1)
	move.l	d4,s.bxo(a1)
	bsr	w3d_block1

	add.w	d1,s.bxo(a1)
	bsr	w3d_block2
	add.w	d1,d1
	swap	d1
	clr.w	d1
	move.l	d1,d2
	and.l	d5,d1
	moveq	#0,d0
	rts

wb_linetop
	moveq	#0,d4			; border origin
	moveq	#-1,d5			; there's a window origin change
	bra.s	wb_horline

wb_linebottom
	moveq	#0,d4
	move.w	s.yws(a1),d4
	sub.w	d1,d4
	sub.w	d1,d4			; border origin
	moveq	#0,d5			; no window origin change

wb_horline
	move.w	s.xws(a1),s.bxs(a1)
	move.w	d1,s.bys(a1)
	move.l	d4,s.bxo(a1)
	bsr	w3d_block1

	add.w	d1,s.byo(a1)
	bsr	w3d_block2
	add.w	d1,d1
	andi.l	#$ffff,d1
	move.l	d1,d2
	and.l	d5,d1
	moveq	#0,d0
	rts

;+++
; Compatibility layer. Checks compatbility bits in colour and adjusts window
; and draws additional parts accordingly.
;
;	d0   s
;	d1 c  p  border width
;	d2   s
;	d4  r	 x origin
;	d5  r	 x size
;	a1 c  p  pointer to parameter block
;---
wb_compat
	move.w	s.bcol(a1),d2
	btst	#7,d2
	beq.s	wbc_nobackground
	btst	#6,d2
	bne.s	wbc_outer

	move.w	d1,s.bxo(a1)		; origin is 1:1 border width
	move.w	d1,s.byo(a1)
	move.w	d1,s.bxs(a1)		; x size is also border width
	move.w	s.yws(a1),d0		; height is ws minus 2 times bwidth
	sub.w	d1,d0
	sub.w	d1,d0
	move.w	d0,s.bys(a1)
	bsr	w3d_blockbg		; left part

	move.w	s.xws(a1),d0
	sub.w	d1,d0
	sub.w	d1,d0
	move.w	d0,s.bxo(a1)
	bsr	w3d_blockbg		; right part
	bra.s	wbc_exit

wbc_outer
	clr.l	s.bxo(a1)		; origin is 0,0
	move.w	d1,s.bxs(a1)		; x size is border with
	move.w	s.yws(a1),s.bys(a1)	; height equals ws
	bsr.s	w3d_blockbg		; left part

	move.w	s.xws(a1),s.bxo(a1)
	sub.w	d1,s.bxo(a1)
	bsr.s	w3d_blockbg		; right part

wbc_smaller
	move.w	d1,d4			; different origin
	move.w	s.xws(a1),d5		; and different size
	sub.w	d1,d5
	sub.w	d1,d5
	moveq	#0,d0
	rts

wbc_nobackground
	btst	#6,d2
	bne.s	wbc_smaller
wbc_exit
	moveq	#0,d4			; x origin 0
	move.w	s.xws(a1),d5		; full width
	moveq	#0,d0
	rts

;+++
; Draw block with colour 1
;---
w3d_block1
	movem.l d1/a1-a2,-(sp)
	moveq	#iow.blok,d0
	move.w	s.col1(a1),d1
	jsr	wmc_trap3
	movem.l (sp)+,d1/a1-a2
	tst.l	d0
	bne.s	w3d_error
	rts

;+++
; Draw block with colour 2
;---
w3d_block2
	movem.l d1/a1-a2,-(sp)
	moveq	#iow.blok,d0
	move.w	s.col2(a1),d1
	jsr	wmc_trap3
	movem.l (sp)+,d1/a1-a2
	tst.l	d0
	bne.s	w3d_error
	rts

;+++
; Draw black block
;---
w3d_blockb
	movem.l d1/a1-a2,-(sp)
	moveq	#iow.blok,d0
	moveq	#0,d1
	trap	#3
	movem.l (sp)+,d1/a1-a2
	tst.l	d0
	bne.s	w3d_error
	rts

w3d_error
	addq.l	#4,sp			; forget return address
	rts

;+++
; Draw block with background colour in a mode independant way. Tricky!
;---
w3d_blockbg
	movem.l d1/a1-a2,-(sp)
	moveq	#iow.xtop,d0
	lea	w3d_block2window,a2
	trap	#3
	moveq	#iow.clra,d0
	trap	#3
	move.l	4(sp),a1		; restore a1
	moveq	#iow.xtop,d0
	lea	w3d_restorewindow,a2
	trap	#3
	movem.l (sp)+,d1/a1-a2
	rts

;+++
; Internally a border is done by substracting it from the window layout.
; In order to set a different border width, the logical window size
; must first be "reverse engineered".
; This routine knows how to remove any border (traditional or special)
;
;	d0  r	error code
;
;	all other registers preserved
;---
wm_rmbrdr
	movem.l d0-d3/a1-a2,-(sp)
	moveq	#iow.xtop,d0
	moveq	#-1,d3
	lea	w3d_removeborder,a2
	trap	#3
	movem.l (sp)+,d0-d3/a1-a2
	rts

; Remove border
w3d_removeborder
	move.w	sd_borwd(a0),d2
	beq.s	wrb_old
	clr.w	sd_borwd(a0)		; border will soon be removed
	cmp.b	#$80,sd_bcolr(a0)
	bne.s	wrb_old
	move.w	sd_bcolw(a0),d1
	move.w	d1,d0
	lsr.w	#8,d0
	cmpi.w	#wmc.3db,d0
	beq.s	wrb_new
wrb_old
	moveq	#0,d1
wrb_new
	bsr.s	wm_bordersize
	sub.l	d1,sd_xmin(a0)		; adjust origin
	add.l	d2,sd_xsize(a0) 	; adjust size
	moveq	#0,d0
	rts

;+++
; Get size and origin change of a border code
;
;	d0  r	0
;	d1 cr	colour code / origin change
;	d2 cr	border width / size change
;
;---
wm_bordersize
	move.l	d2,-(sp)
	jsr	wmc_colour
	movem.l (sp)+,d2
	bne.s	wbs_traditional

	move.w	d1,d0
	lsr.w	#8,d0
	cmpi.w	#wmc.3db,d0
	bne.s	wbs_traditional

	move.w	d1,d0
	andi.w	#%1110,d0
	move.w	wbs_mtab(pc,d0.w),d0
	jmp	wbs_mtab(pc,d0.w)

wbs_mtab
	dc.w	wbs_traditional-wbs_mtab
	dc.w	wbs_1to1-wbs_mtab
	dc.w	wbs_double-wbs_mtab
	dc.w	wbs_shadow-wbs_mtab
	dc.w	wbs_lineleft-wbs_mtab
	dc.w	wbs_lineright-wbs_mtab
	dc.w	wbs_linetop-wbs_mtab
	dc.w	wbs_linebottom-wbs_mtab

; 2:1 aspect
wbs_traditional
	move.w	d2,d1
	add.w	d1,d1
	move.w	d1,d0
	swap	d1
	move.w	d2,d1			; origin change

	move.w	d0,d2
	add.w	d2,d2
	swap	d2
	move.w	d0,d2			; size change
	moveq	#0,d0
	rts

; 1:1 aspect, double lines
wbs_double
	add.w	d2,d2

; 1:1 aspect
wbs_1to1
	andi.b	#$c0,d1
	bne.s	wbs_traditional 	; compatibility modes

	move.w	d2,d0
	swap	d2
	move.w	d0,d2
	move.l	d2,d1			; origin
	add.l	d2,d2			; size
	moveq	#0,d0
	rts

; 1:1 aspect with additional shadow
wbs_shadow
	move.w	d2,d0
	swap	d2
	move.w	d0,d2
	move.w	d1,d0
	move.l	d2,d1
	btst	#0,d0
	beq.s	wbsd_raised
	add.l	d1,d1
wbsd_raised
	move.l	d2,d0
	add.l	d2,d2
	add.l	d0,d2			; width * 3
	moveq	#0,d0
	rts

; lines
wbs_lineleft
	add.w	d2,d2
	swap	d2
	clr.w	d2
	move.l	d2,d1
	moveq	#0,d0
	rts

wbs_lineright
	add.w	d2,d2
	swap	d2
	clr.w	d2
	moveq	#0,d1
	moveq	#0,d0
	rts

wbs_linetop
	add.w	d2,d2
	ext.l	d2
	move.l	d2,d1
	moveq	#0,d0
	rts

wbs_linebottom
	add.w	d2,d2
	ext.l	d2
	moveq	#0,d1
	moveq	#0,d0
	rts

;+++
; Just copy window size and origin out of CDB
;---
w3d_getdata
	move.l	sd_xsize(a0),s.xws(a1)
	move.l	sd_xmin(a0),s.xwo(a1)
	moveq	#0,d0
	rts

;+++
; Emulate a border call using the values in D1 and D2
;---
w3d_setborder
	add.l	d1,sd_xmin(a0)		; adjust origin
	sub.l	d2,sd_xsize(a0) 	; adjust size
	move.w	s.bwidth(a1),sd_borwd(a0) ; save border width
	move.w	s.bcol(a1),sd_bcolw(a0)   ; and "colour"!
	move.b	#$80,sd_bcolr(a0)	; additional "it's special" flag

	clr.l	sd_xpos(a0)		; set cursor to top left
	clr.b	sd_nlsta(a0)		; clear pending newline
	bclr	#sd..gmod,sd_cattr(a0)	; ... and graphics mode
	moveq	#0,d0
	rts

;+++
; Set window origin and size to the block size
;---
w3d_block2window
	move.l	s.bxo(a1),d0
	add.l	d0,sd_xmin(a0)
	move.l	s.bxs(a1),sd_xsize(a0)
	moveq	#0,d0
	rts

;+++
; Restore window size
;---
w3d_restorewindow
	move.l	s.xws(a1),sd_xsize(a0)
	move.l	s.xwo(a1),sd_xmin(a0)
	moveq	#0,d0
	rts

	end
