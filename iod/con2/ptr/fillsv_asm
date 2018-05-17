; Fill save area		    v2.11   1999 Tony Tebby
;					     2002 Marcel Kilgus
;
; 2002-02-13  2.11  Changes for on-thy-fly wallpaper colour (MK)
;
;	Fill the save area with the image (a1) or the colour in the linkage
;	the lines in the image must be rounded up to the nearest long word
;	If no save area is given the screen will be used as destination.
;
;	D2	image type 0 = native, -1 = none
;	A0	^ cdb			preserved
;	A1	^ image
;	A3	^ dddb			preserved
;
	section driver
;
	include 'dev8_keys_con'
	include 'dev8_keys_iod'
	include 'dev8_keys_chn'
;
	xdef	pt_fillsv

	xref	pt_mblock
	xref	cn_fblock

	xref.s	pt.spxlw
	xref.s	pt.rpxlw
	xref.l	pt.samsk
;
pt_fillsv
ptf.reg reg	d1-d7/a1-a5
stk_d2	equ	$04
	movem.l ptf.reg,-(sp)

	move.w	pt_bgstp(a3),d6 	; stipple
	move.l	pt_bgclm(a3),d7 	; colour

	moveq	#pt.rpxlw,d3		;
	add.w	sd_xhits(a0),d3 	; save area size, rounded up
	move.w	#pt.spxlw,d0		; to number of bytes
	lsr.l	d0,d3			; to nearest long word
	lsl.w	#2,d3			; in bytes
	move.l	d3,a2			; source increment

	move.l	sd_wsave(a0),d0 	; get save area
	beq.s	ptf_screen		; none, use screen directly

	move.l	d0,a5			; destination is save area
	lea	4(a2),a3		; destination increment
	move.l	#pt.samsk,d3
	and.l	sd_xhito(a0),d3 	; x-origin is offset this much in save

	move.w	sd_yhito(a0),d3
	andi.w	#1,d3			; origin at odd line?
	beq.s	ptf_do
	sub.l	a3,a5			; yes, adjust base. This is to correct
	bra.s	ptf_do			; the stipple line order

ptf_screen
	move.l	pt_scren(a3),a5 	; screen is destination
	move.w	pt_scinc(a3),a3 	; destination increment
	move.l	sd_xhito(a0),d3 	; absolute origin on screen

ptf_do
	move.l	sd_xhits(a0),d1 	; size

	tst.l	stk_d2(sp)		; image?
	bne.s	ptf_block

	move.l	a1,a4			; source address
	moveq	#0,d2			; source origin

	jsr	pt_mblock		; move an area
	bra.s	ptf_ok

ptf_block
	move.l	a3,a2			; line increment
	move.l	a5,a1			; base of area
	move.l	d3,d2			; origin
	jsr	cn_fblock

ptf_ok
	movem.l (sp)+,ptf.reg
	moveq	#0,d0
	rts
;
	end
