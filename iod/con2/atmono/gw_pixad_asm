	section gw
* sets up pixel address and masks
	xdef	GW_PIXAD

	include 'dev8_Minerva_INC_GW'
	include 'dev8_Minerva_INC_SV'
	include 'dev8_Minerva_INC_SD'

* D0 -ip - x coordinate (lsw)
* D1 -ip - y coordinate (lsw)
* D2 -	o- pixel mask within word (msw preserved)
* D3 -	o- colour masks (lsw for line at y coordinate)
* A0 -ip - channel definition block
* A5 -	o- screen address of word containing pixel
* A6 -ip - graphics stack frame

gw_pixad
	move.w	D0,-(SP)	save x coordinate
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

	move.l	SD_IMASK(A0),D3 basic colour mask
	lsr.b	#1,D2
	bcs.s	NOSWAP
	swap	D3		if line number is even then swap colour masks
noswap

	move.w	#$8000,D2 basic pixel mask for 2 colour
rotate
	ror.w	D0,D2		rotate pixel mask over pixel position in word

	asr.w	#4,D0		word number in line
	add.w	D0,D0		byte offset in line
	add.w	D0,A5		finally, pixel word address

	move.w	(SP)+,D0	reload original x coordinate
	rts

	end
