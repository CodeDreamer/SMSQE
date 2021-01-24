; Reset the size of a window		V2.00   1998  Tony Tebby

	section con

	include 'dev8_keys_con'

	xref	cn_chken
	xref	cn_defbd

	xdef	cn_defwn

cn_defwn
	movem.w d1/d2,-(sp)
	move.l	pt_xscrs(a3),d1 	; max allowable size
	moveq	#0,d2			; origin at 0,0
	jsr	cn_chken		; is the definition allowable?
	movem.w (sp)+,d1/d2
	bne.s	cnd_exit		; no
	movem.l (a1),d3/d4

	clr.l	sd_xpos(a0)		; set cursor to top left
	clr.b	sd_nlsta(a0)		; clear pending newline
	bclr	#sd..gmod,sd_cattr(a0)	; ... and graphics mode

	movem.l d3/d4,sd_xsize(a0)	; size
	clr.w	sd_borwd(a0)		; with no border
	tst.w	d2			; border
	bne.l	cn_defbd		; set border/active area as required
cnd_exit
	rts

	end
