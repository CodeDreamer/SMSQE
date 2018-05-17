; Define wallpaper				v1.03	   1999  Tony Tebby
;							    2002  Marcel Kilgus
;
; 2002-02-13  1.01  Adapted for on-the-fly wallpaper (MK)
; 2002-05-30  1.02  Returns last used colour in D1. Fixes mode change issue (MK)
; 2011-03-02  1.03  Save a few registers around mem.rchp for QDOS systems (MK)

;
	section driver
;
	xdef	pt_wpap

	xref	pt_wchka
	xref	pt_wremv
	xref	pt_mrall
	xref	pt_fillsv

	xref.s	pt.spxlw
	xref.s	pt.rpxlw

	xref	cn_ql_colr
	xref	cn_24b_mcolr
;
	include dev8_keys_con
	include dev8_keys_iod
	include dev8_keys_chn
	include dev8_keys_err
	include dev8_keys_qlv
	include dev8_keys_qdos_sms

;+++
; This version handles background images (wallpaper) exclusively in the current
; display mode and screen size (D2 must be -1). This also implies that there is
; no real point in specifying a colour if an image is given.
; The colour must either be a 24 bit colour (msbits) or a QL stipple.
;
;	D1 cr	background colour (or -1) / last set background colour
;	D2 c s	background image type (0 for native, -1 for none)
;	A1 c  p background image
;---
pt_wpap
ptw.reg reg	d2/a0-a1
stk_d2	equ	$00
stk_a1	equ	$08

	movem.l ptw.reg,-(sp)
	moveq	#-1,d4
	tst.l	d2			; native mode image?
	beq.s	ptw_colour		; ... yes, set colour

	cmp.l	d4,d2			; image?
	bne.l	ptw_ipar		; ... yes, wrong mode

	cmp.l	d4,d1			; colour given?
	bne.s	ptw_colour		; ... yes

	bsr.l	ptw_remove		; remove old background
	bra.l	ptw_redraw

ptw_colour
	cmp.l	d4,d1			; colour given?
	bne.s	ptw_cdef		; ... yes
	move.l	pt_bgcld(a3),d1 	; use old colour
ptw_cdef
	move.l	d1,pt_bgcld(a3) 	; ... save old colour
	moveq	#0,d3
	move.b	d1,d3			; QL colour?
	beq.s	ptw_c24 		; ... no
	jsr	cn_ql_colr
	bra.s	ptw_setcolour
ptw_c24
	moveq	#-1,d6			; no stipple
	move.l	d1,d3
	jsr	cn_24b_mcolr
ptw_setcolour
	move.w	d6,pt_bgstp(a3) 	; set stipple
	move.l	d7,pt_bgclm(a3) 	; set colour

; First release existing background (if any)
; It's easier to start from scratch
ptw_setup
	bsr.l	ptw_remove

	tst.l	stk_d2(sp)		; image given?
	bne.s	ptw_redraw		; no, don't create wallpaper window

	moveq	#sd.wpapr,d1		; create a wallpaper window
	bsr.l	ptw_crchan
	bne.l	ptw_exit		; ...oops

	lea	sd_prwlb(a0),a4
	move.l	pt_tail(a3),a1		; point to bottom
	move.l	a1,(a4) 		; bottom up list
	move.l	a4,pt_tail(a3)
	addq.l	#sd_prwlt-sd_prwlb,a4
	move.l	a4,sd_prwlt-sd_prwlb(a1) ; link at end of top down list

	move.l	a0,a4			; keep channel base safe

	move.l	pt_ssize(a3),d4 	; outline area width (pixels)
	move.l	d4,d1
	swap	d1
	moveq	#pt.spxlw,d0
	add.w	#pt.rpxlw,d1		; round up to...
	lsr.w	d0,d1			; ...width in long words
	addq.w	#1,d1			; one spare
	move.w	d1,d6			; save that
	mulu	d4,d1			; space required in words
	asl.l	#2,d1			; space in bytes
;
	move.l	a3,a5			; allocate common heap
	moveq	#sms.achp,d0
	moveq	#0,d2
	trap	#1
	move.l	a5,a3
	tst.l	d0			; OK?
	bne.s	ptw_removexit		; ... no
;
	move.l	a0,sd_wsave(a4) 	; set pointer in cdb
	move.l	d1,sd_wssiz(a4) 	; set length in cdb
	st	sd_mysav(a4)		; set save area owner

	move.l	a4,a0
	move.l	stk_d2(sp),d2
	move.l	stk_a1(sp),a1

	jsr	pt_fillsv		; fill the save area

ptw_redraw
	moveq	#sd.wbprm,d1
	bsr.s	ptw_crchan		; allocate channel block
	bne.s	ptw_exit		; ...oops

	move.l	a0,-(sp)
	moveq	#1,d3			; lock all windows
	jsr	pt_wchka
	move.l	(sp),a0

	jsr	pt_wremv		; remove dummy window

	moveq	#0,d3			; reset locks
	jsr	pt_wchka
	jsr	pt_mrall

	move.l	(sp)+,a0		; remove dummy channel block
	suba.l	#sd.extnl,a0
	movem.l a2-a3,-(sp)		; save smashed registers on QDOS systems
	move.w	mem.rchp,a1
	jsr	(a1)
	movem.l (sp)+,a2-a3
ptw_exok
	move.l	pt_bgcld(a3),d1 	; return last colour set
	moveq	#0,d0
ptw_exit
	movem.l (sp)+,ptw.reg
	rts

ptw_removexit
	move.l	d0,-(sp)
	bsr.s	ptw_remove
	move.l	(sp)+,d0
	bra.s	ptw_exit
;
ptw_ipar
	moveq	#err.ipar,d0
	bra.s	ptw_exit

;+++
; Allocate and fill a channel definition block.
;
;	D0  r	0 or error code
;	D1 c s	window type (sd.wpapr, sd.wbprm)
;	A0  r	address of block
;	A3 c  p DDDB
;---
ptw_crchan
ptcc.reg reg	d1-d3/a1-a3
stk_d1	equ	0
stk_a3	equ	5*4
chblock setnum	sd.extnl+sd_keyq+8
	movem.l ptcc.reg,-(sp)
	move.l	#[chblock],d1		; allocate enough space for one block
	move.w	mem.achp,a2
	jsr	(a2)			; allocate heap
	tst.l	d0
	bne.l	ptcc_exit		; ...oops

	move.l	stk_a3(sp),a3
	lea	iod_iolk(a3),a2
	move.l	a2,chn_drvr(a0) 	; set driver

	add.w	#sd.extnl,a0

	move.l	stk_d1(sp),d1
	move.b	d1,sd_behav(a0) 	; wallpaper
	move.b	pt_dmode(a3),sd_wmode(a0)
	st	sd_wlstt(a0)		; locked

	clr.l	sd_xmin(a0)		; set origin
	clr.l	sd_xouto(a0)
	clr.l	sd_xhito(a0)
	move.l	pt_ssize(a3),d1
	move.l	d1,sd_xsize(a0) 	; set size
	move.l	d1,sd_xouts(a0)
	move.l	d1,sd_xhits(a0)

	move.l	pt_scren(a3),sd_scrb(a0)  ; active area is in here
	move.w	pt_scinc(a3),sd_linel(a0) ; and with this row increment

	moveq	#0,d0
ptcc_exit
	movem.l (sp)+,ptcc.reg
	rts

ptwr.reg reg	d2-d3/a0-a3
ptw_remove
	movem.l ptwr.reg,-(sp)
	move.l	pt_tail(a3),a4		; point to bottom
	cmp.b	#sd.wpapr,sd_behav-sd_prwlb(a4) ; wallpaper?
	bne.s	ptw_rset		; ... no, set return value

	move.l	(a4),a1 		; next one up
	move.l	a1,pt_tail(a3)		; unlink from bottom up
	clr.l	sd_prwlt-sd_prwlb(a1)

	move.l	sd_wsave-sd_prwlb(a4),d0; save area
	beq.s	ptw_rchn		; ... none
	move.l	d0,a0
	moveq	#sms.rchp,d0		; return it
	trap	#1

ptw_rchn
	lea	-sd_prwlb-sd.extnl(a4),a0
	move.w	mem.rchp,a2
	jsr	(a2)			; return channel block

ptw_rset
	movem.l (sp)+,ptwr.reg
	rts

	end
