;	Clear the area to paper colour	v0.00   1998 Tony Tebby
;
;	Registers:
;		Entry				Exit
;	A0	pointer to cdb
;
	section con

	include 'dev8_keys_con'

	xref	cn_ssuba
	xref	cn_fblock

	xdef	cn_clral
	xdef	cn_clrtp
	xdef	cn_clrbt
	xdef	cn_clrcl
	xdef	cn_clrcr
	xdef	cn_clrhit

clrreg	reg	a1/a2
cn_clrtp
	moveq	#cn.bptop,d3		; all of top
	bra.s	cnc_suba		; clear that
cn_clrbt
	moveq	#cn.bpbot,d3		; all of bottom
	bra.s	cnc_suba
cn_clrcl
	moveq	#cn.bpcln,d3		; all of cursor line
	bra.s	cnc_suba
cn_clrcr
	moveq	#cn.bpcrt,d3		; right hand end of cursor line
cnc_suba
	jsr	cn_ssuba(pc)
	bra.s	cnc_ntry
*
cn_clrhit
	movem.l sd_xhits(a0),d1/d2	; clear hit area
	exg	d1,d2
	bra.s	cnc_ntry

cn_clral
	bclr	#sd..gmod,sd_cattr(a0)	; not graph mode now
	movem.l sd_xmin(a0),d1/d2	; set area to clear
	exg	d1,d2
	clr.b	sd_nlsta(a0)		; no pending newline now...
	clr.l	sd_xpos(a0)		; ...'cos cursor's at top left
*
cnc_ntry
	movem.l clrreg,-(sp)
	move.l	sd_scrb(a0),a1		; set base address
	move.w	sd_linel(a0),a2 	; and row increment

	move.l	sd_pmask(a0),d7 	; colour mask
	move.w	#%11111000,d6
	and.b	sd_pcolr(a0),d6 	; ql contrast colour
	bne.s	cnc_stip
	moveq	#-1,d6			; ... none
cnc_stip
	asr.w	#6,d6			; set stipple number or -1

	jsr	cn_fblock(pc)		; fill the block

	moveq	#0,d0
cnc_exit
	movem.l (sp)+,clrreg
	rts

	end
