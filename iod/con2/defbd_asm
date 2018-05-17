;	Define a border 		V2.01   1998 Tony Tebby
;
; 2003-10-06  2.01  Fixed extended colour calls with width=0. (MK)
;
;	A border is put around the inside edge of the hit area, and the active
;	area shrunk appropriately.
;
;	Registers:
;		Entry				Exit
;	D1	colour
;	D2	width
;	A0	cdb
;
	section con

	include 'dev8_keys_err'
	include 'dev8_keys_con'

	xdef	cn_defbd
	xdef	cn_borp
	xdef	cn_bort
	xdef	cn_born

	xdef	cn_cksize
	xdef	cn_cksize_p
	xdef	cn_cksize_s

	xref	cn_ql_bcolr
	xref	cn_pal_bmcolr
	xref	cn_pal_bccolr
	xref	cn_24b_bmcolr
	xref	cn_24b_bccolr
	xref	cn_nat_bmcolr
	xref	cn_nat_bccolr

	xref	cn_bcnat

	xref	cn_fblock

cn_defbd
	move.b	d1,d3
	jsr	cn_ql_bcolr		; colour
	moveq	#0,d6
	move.b	d1,d6			; QL compatibility
	bra.s	cnb_set

cn_borp
	lea	cn_pal_bmcolr,a4	; palette map to border colour
	lea	cn_pal_bccolr,a5
	bra.s	cnb_ext_colr

cn_bort
	lea	cn_24b_bmcolr,a4	; true colour to border colour
	lea	cn_24b_bccolr,a5
	bra.s	cnb_ext_colr

cn_born
	lea	cn_nat_bmcolr,a4	; native mode to border colour
	lea	cn_nat_bccolr,a5

cnb_ext_colr
	move.l	d1,d3			; this is the colour
	tst.l	d2			; main colour or contrast
	bpl.s	cnb_ext_contrast
	jsr	(a4)			; set border main colour
	moveq	#0,d6			; no stipple
	bra.s	cnb_setx

cnb_ipar
	moveq	#err.ipar,d0
	rts

cnb_ext_contrast
	move.l	d2,d6			; stipple
	swap	d6
	cmp.w	#3,d6
	bhi.s	cnb_ipar
	move.w	sd_bcolw(a0),d7 	; plain colour
	jsr	(a5)			; set contrast colour
	add.b	d6,d6
	addq.b	#1,d6			; set arbitrary contrast colour
	lsl.b	#5,d6			; QL type stipple

cnb_setx
	tst.w	d2			; for extended colour calls
	bpl.s	cnb_set
	move.b	d6,sd_bcolr(a0) 	; set colour
	move.w	d7,sd_bcolw(a0)
	moveq	#0,d0
	rts

cnb_set
cnb.reg reg	d1/d2
	movem.l cnb.reg,-(sp)

	and.w	#$00ff,d2		; byte only border width
	move.b	d6,sd_bcolr(a0) 	; set colour
	move.w	d7,sd_bcolw(a0)
	move.w	sd_borwd(a0),d5
	cmp.w	d5,d2			; has border changed?
	beq.s	cnb_sbwd		; no
	clr.l	sd_xpos(a0)		; yes, set cursor to top left
	clr.b	sd_nlsta(a0)		; clear pending newline
	bclr	#sd..gmod,sd_cattr(a0)	; ... and graphics mode
cnb_sbwd
	move.w	d2,sd_borwd(a0) 	; set border width

	movem.l sd_xmin(a0),d3/d4	; get window area size/origin
	exg	d3,d4
	tst.w	d5			; and remove old border
	beq.s	cnb_adj
	sub.w	d5,d4
	add.w	d5,d5
	add.w	d5,d3
	swap	d3
	swap	d4
	sub.w	d5,d4
	add.w	d5,d5
	add.w	d5,d3
	swap	d3
	swap	d4

cnb_adj
	move.l	d2,d5
	move.l	d3,d1			; size in d1/d2
	move.l	d4,d2			; we'll need the size again
	move.w	d5,d0			; smashable border width
	add.w	d0,d4			; possible new Y origin
	add.w	d0,d0			; this much...
	sub.w	d0,d3			; ...off height
	bmi.s	cnb_exip		; ...oops
	swap	d4
	add.w	d0,d4			; new x origin
	swap	d4
	add.w	d0,d0			; this much...
	swap	d3			; ...off...
	sub.w	d0,d3			; ...width
	bmi.s	cnb_exip		; ...oops
	swap	d3
	move.l	d3,sd_xsize(a0) 	; new descriptor is OK
	move.l	d4,sd_xmin(a0)
;
	moveq	#0,d0
	cmp.b	#$80,sd_bcolr(a0)	; transparent border?
	beq.s	cnb_size		; yes, don't draw anything
	tst.b	d5			; any border?
	beq.s	cnb_size		; ... no
;
;
;	Having set up the parameters, we can draw the border, and modify
;	the active area.
;
	jsr	cn_bcnat		; set colours for block
	move.w	d5,d1			; narrow block at top
	bsr.s	cnb_block		; fill it
	add.w	d5,d2			; top of window
	add.w	sd_ysize(a0),d2 	; bottom of window
	bsr.s	cnb_block		; do another
;
	add.w	d5,d5			; this wide
	move.w	d5,d1
	swap	d1
	move.w	sd_ysize(a0),d1 	; tall blocks now
	move.w	sd_ymin(a0),d2		; at top left
	bsr.s	cnb_block
	swap	d2
	add.w	d5,d2			; left of window
	add.w	sd_xsize(a0),d2 	; right of window
	swap	d2
	bsr.s	cnb_block
;
cnb_size
	bsr.s	cn_cksize
	moveq	#0,d0
cnb_exit
	movem.l (sp)+,cnb.reg
	rts

cnb_block
	move.l	sd_scrb(a0),a1		; point to area
	move.w	sd_linel(a0),a2 	; and get its row increment
	movem.l d1-d7,-(sp)
	jsr	cn_fblock
	movem.l (sp)+,d1-d7
	rts

;
cnb_exip
	moveq	#err.orng,d0
	bra.s	cnb_exit


;+++
; set x,y increments (d2) and check window size
;
;	d0	arbitrary
;	d2  p	x,y increments
;
; returns LO is window too small
;---
cn_cksize_s
	move.l	d2,sd_xinc(a0)		; increments

;+++
; check window size against increments (all except d0,d2 preserved)
;
;	d0	arbitrary
;
; returns LO is window too small
;---
cn_cksize_p

;+++
; check window size against d2
;
;	d0	arbitrary
;	d2  r	x,y increments
;
; returns LO is window too small
;---
cn_cksize
	move.l	sd_xinc(a0),d2		; increments

;+++
; check window size against d2
;
;	d0	arbitrary
;	d2 c  p x,y increments
;
; returns LO is window too small
;---
cn_ckszd2
	move.l	sd_xsize(a0),d0 	; xsize
	cmp.w	d2,d0			; high enough?
	blo.s	cnb_setsf
	cmp.l	d2,d0
cnb_setsf
	slo	sd_sflag(a0)		; set too small flag
	rts

	end
