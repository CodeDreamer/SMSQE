	section gw
* sets up pixel address and masks
	xdef	GW_PIXAD

	xref	cn_clr2lw

	include 'dev8_Minerva_INC_GW'
	include 'dev8_Minerva_INC_SV'
	include 'dev8_Minerva_INC_SD'

* D0 -ip - x coordinate (lsw)
* D1 -ip - y coordinate (lsw)
* D2 -	o- colour masks for other line
* D3 -	o- colour masks for this line (lsw for pixel at x coordinate)
* A0 -ip - channel definition block
* A5 -	o- screen address of word containing pixel
* A6 -ip - graphics stack frame

gw_pixad
gwp.reg reg	d0/d6/d7
	movem.l gwp.reg,-(sp)
	move.w	D1,D2		copy y coordinate
	add.w	SD_XMIN(A0),D0	adjust for window position
	add.w	SD_YMIN(A0),D2

	move.l	SD_SCRB(A0),A5	get screen base address
	move.w	SD_LINEL(A0),D3
	move.w	D3,LINEL(A6)	set line length
	neg.w	D3
	move.w	D3,LINEM(A6)	and its negation
	muls	D2,D3		multiply line length by line number (NB signed)
	sub.l	D3,A5		pixel line address (Note D3 was negated)

	add.w	D0,A5
	add.w	D0,A5		finally, pixel word address


	move.l	sd_imask(a0),d7 	; colour mask
	move.w	#%11111000,d6
	and.b	sd_icolr(a0),d6 	; ql contrast colour
	bne.s	gwp_stip
	moveq	#-1,d6			; ... none
gwp_stip
	asr.w	#6,d6			; set stipple number or -1

	jsr	cn_clr2lw	get colours in two long words (d6,d7)

	move.l	d7,d2
	move.l	d6,d3

	movem.l (sp)+,gwp.reg
	rts

	end
