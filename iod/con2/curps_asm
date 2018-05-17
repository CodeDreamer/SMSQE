;	Set cursor position		V2.00	 1998 Tony tebby
;
;	Pixel or character positioning of cursor: the position required is
;	given relative to the origin of the active area of the channel.
;
;	Registers:
;		Entry				Exit
;	D1	x position
;	D2	y position
;	A0	pointer to cdb
;
	section con
;
	include 'dev8_keys_con'
	include 'dev8_keys_sys'
	include 'dev8_mac_assert'
;
	xref	cn_chken
;
	xdef	cn_scchr		; character co-ords
	xdef	cn_scpix		; pixel co-ords
	xdef	cn_sccol		; column
	xdef	cn_scnwl		; new line
	xdef	cn_scpcl		; previous column
	xdef	cn_scncl		; next column
	xdef	cn_scprw		; previous row
	xdef	cn_scnrw		; next row
;
cn_scnwl
	move.w	sd_xpos(a0),d1		; back to start of line
	neg.w	d1
	move.w	sd_yinc(a0),d2		; one line down
	bra.s	cnc_adps
;
cn_scpcl
	move.w	sd_xinc(a0),d1		; move by X size...
	neg.w	d1			; ...backwards
	moveq	#0,d2			; but not by Y
	bra.s	cnc_adps
;
cn_scncl
	move.w	sd_xinc(a0),d1		; move by X size
	moveq	#0,d2			; but not by Y
	bra.s	cnc_adps
;
cn_scprw
	moveq	#0,d1			; no X move
	move.w	sd_yinc(a0),d2		; move by Y...
	neg.w	d2			; upwards
	bra.s	cnc_adps
;
cn_scnrw
	moveq	#0,d1			; no X move
	move.w	sd_yinc(a0),d2		; move by Y
;
;	Come here with a cursor position change, in pixel co-ordinates,
;	in D1/D2.
;
cnc_adps
	add.w	sd_xpos(a0),d1		; add in absolute position...
	add.w	sd_ypos(a0),d2		; ...in both directions
	bra.s	cnc_set 		; and set it, if possible
;
cn_sccol
	move.w	sd_ypos(a0),d2		; same line
	bra.s	cnc_xtop		; new X, to be changed to pixels
;
cn_scchr
	mulu	sd_yinc(a0),d2		; multiply up by...
cnc_xtop
	mulu	sd_xinc(a0),d1		; ...cursor size, gives pixel position

cnc_set
	bclr	#sd..gmod,sd_cattr(a0)	; not graphics mode now

;
;	Come here with a new cursor position, in relative pixel
;	co-ordinates, in D1/D2
;
cn_scpix
	move.l	a1,-(sp)
	movem.w d1/d2,-(sp)		; stack the new origin
	movem.l sd_xmin(a0),d1/d2	; allowed area
	exg	d1,d2
	add.l	d2,(sp) 		; absolute origin
	move.l	sd_xinc(a0),-(sp)	; complete descriptor of cursor
	move.l	sp,a1			; point to it
	jsr	cn_chken(pc)		; is it in the area?
	addq.l	#4,sp			; (pop size)
	bne.s	cnp_gcur		; no
	assert	sd.sfout,1
	bclr	#0,sd_sflag(a0) 	; now in area
cnp_set
	move.l	(sp),d2
	sub.l	sd_xmin(a0),d2
	cmp.b	#ptm.ql8,pt_dmode(a3)	; which mode are we in?
	bne.s	cnp_sd2 		; ... not 8 colour
	bclr	#16,d2			; ... 8 colour, multiples of 2 only
cnp_sd2
	move.l	d2,sd_xpos(a0)		; yes, put origin into cdb
cnp_exit
	clr.b	sd_nlsta(a0)		; and clear pending newline
	tst.l	d0			; for internal calls
	addq.l	#4,sp			; pop origin
	move.l	(sp)+,a1
	rts

cnp_gcur
	assert	sd..gmod,7
	tst.b	sd_cattr(a0)		; graphics cursor?
	bpl.s	cnp_exit		; ... no
	or.b	#sd.sfout,sd_sflag(a0)	; flag it outside
	moveq	#0,d0
	bra.s	cnp_set
	end
