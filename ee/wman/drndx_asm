; Draw indexes
; 2020-08-11  1.01  first implementation of the so far
;		    unacheved indexes drawing. All new (AH)
; 2020-08-19  1.02  udapted after fixing QPTR bugs - QPTR v0.15 Ok (AH)

	section wman

	xdef	wm_drndx

	xref	wm_scrow
	xref	wm_sccol
	xref	wm_drmob
	xref	wm_trap3

	include dev8_keys_qdos_io
	include dev8_keys_wwork

;+++
; Draw indexes items
;
;	d0  r	error return
;	a0 c  p channel ID of window
;	a3 c  p pointer to sub-window definition
;	a4 c  p to working definition
;
;		all other registers preserved
;---

s.bw	equ	0			; block width
s.bh	equ	2			; heigth
s.bx	equ	4			; x orig
s.by	equ	6			; y
frame	equ	8			; frame size

reglist reg	d1-d7/a1-a4

wm_drndx
	move.l	wwa_xind(a3),d0 	; pointer to x (column) index list
	or.l	wwa_yind(a3),d0 	; pointer to y (row) index list
	bne.s	wdn_do			; ... indexes found
	rts
wdn_do
	movem.l reglist,-(sp)
	subq.l	#frame,sp		; make room for FILL block

	lea	ww_xsize(a4),a1 	; pointer to main window def
	moveq	#iow.defw,d0		; ... redefine it
	moveq	#0,d2			; ... no border
	moveq	#-1,d3			; ... no timeout
	trap	#3
	tst.l	d0
	bne.s	wdn_exit

	moveq	#iow.ssiz,d0		; set char size & spacing
	moveq	#0,d1
	moveq	#0,d2
	move.b	wwa_xcsz(a3),d1 	; x csize from menu sub window defn
	move.b	wwa_ycsz(a3),d2 	; y csize
	trap	#3
	tst.l	d0
	bne.s	wdn_exit

	move.l	wwa_xind(a3),d0 	; any x (column) index?
	beq.s	wdn_doy 		; ... no, try y (row)

	move.w	wwa_insz+wwa.clen(a3),d4 ; index y hit size
	move.w	wwa_yorg(a3),d5 	 ; y orig of sub-win
	sub.w	wwa_insp+wwa.clen(a3),d5 ; ... shift above

	move.w	wwa_xsiz(a3),s.bw(sp)	; block width
	move.w	d4,s.bh(sp)		; ... heigth
	move.w	wwa_xorg(a3),s.bx(sp)	; x pos
	move.w	d5,s.by(sp)		; y
	movea.l sp,a1			; pointer to block def
	bsr.s	wdn_clear		; clear indexes area

	lea	wdn_docol(pc),a1	; our action routine
	bsr.l	wm_sccol		; do all visible columns

wdn_doy
	move.l	wwa_yind(a3),d0 	; any y (row) index?
	beq.s	wdn_exit		; ... no, exit
	move.w	wwa_xorg(a3),d5 	; x orig for sub-win
	sub.w	wwa_insp(a3),d5 	; ... shift to left edge
	move.w	wwa_insz(a3),d4 	; index x hit size

	move.w	d4,s.bw(sp)		; block width
	move.w	wwa_ysiz(a3),s.bh(sp)	; ... heigth
	move.w	d5,s.bx(sp)		; x pos
	move.w	wwa_yorg(a3),s.by(sp)	; y
	movea.l sp,a1			; pointer to block def
	bsr.s	wdn_clear		; clear indexes area

	swap	d4			; set x size
	swap	d5			; set x orig
	lea	wdn_dorow(pc),a1	; our action routine
	bsr.l	wm_scrow		; do all visible rows

wdn_exit
	addq.l	#frame,sp
	movem.l (sp)+,reglist		; restore registers
	rts

wdn_clear
	move.w	wwa_watt+wwa_papr(a3),d1 ; set block colour to background
	moveq	#-1,d3			; no timeout
	moveq	#iow.blok,d0		; fill block trap
	bsr.l	wm_trap3		; do it GD2 way
	rts

wdn_docol
	tst.w	d6			; genuine row?
	bne.s	wdn_ok			; ... no
	movem.l d3/d4/d5/a1/a2,-(sp)	; save regs
	swap	d4			; get x,y sizes
	add.w	wwa_xorg(a3),d5 	; x orig from main window
	swap	d5			; set x,y origs
	movea.l wwa_xind(a3),a2 	; pointer to x (col) index list
wdn_dobj
	mulu	#wwm.olen,d3		; offset to object
	adda.l	d3,a2			; pointer to object
	moveq	#0,d3			; should draw please
	bsr.l	wm_drmob		; draw menu object
	movem.l (sp)+,d3/d4/d5/a1/a2	; restore regs
	rts
wdn_ok
	moveq	#0,d0
	rts

wdn_dorow
	tst.w	d6			; genuine row?
	bne.s	wdn_ok			; ... no
	movem.l d3/d4/d5/a1/a2,-(sp)	; save regs
	add.w	wwa_yorg(a3),d5 	; y orig from main window
	movea.l wwa_yind(a3),a2 	; pointer to y (row) index list
	bra.s	wdn_dobj		; from there

	end
