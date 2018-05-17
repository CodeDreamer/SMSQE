	section gw
* set text cursor using graphics coords
	xdef	GW_GCUR

	xref	GW_PSORG,GW_PSCAL,GW_ASP
	xref	RI_ABEXB
	xref	cn_scpix;  SD_SETC

	include 'dev8_Minerva_INC_RI'
	include 'dev8_Minerva_INC_SD'

* stack offsets for use by interpreter

py	equ	-6*1
px	equ	-6*2
gy	equ	-6*3
gx	equ	-6*4
xorg	equ	-6*5
yorg	equ	-6*6

gw_gcur
	lea	-GX(A1),A4	point to bottom of stack
	jsr	GW_PSORG(PC)	push the window origin
	jsr	GW_ASP(PC)	get the aspect ratio
	jsr	GW_PSCAL(PC)	push the window size and scale
	lea	MAIN,A3 	the main interpreter block
	bsr.s	ABEXB		-, -, -, -, px', py'
	lea	PX(A4),A1	point to the answers: px', py'
	subq.l	#MAIN-NINT,A3	the little interpreter block
	bsr.s	ABEXB
	move.w	(A1)+,D1	x = int(px)
	bsr.s	ABEXB
	move.w	(A1)+,D2	y = int(py)
	bset	#SD..GCHR,SD_CATTR(A0) set graphics positioned char flag
	jmp	cn_scpix       SD_SETC(PC)     set the text cursor

abexb
	jmp	RI_ABEXB(PC)

nint
 dc.b RI.NINT,RI.TERM
main
* scale, height-1, aspect, yorg, xorg, gx, gy, px, py
 dc.b RI.OVER,RI.SWAP,RI.DIV		(height-1)/scale
* (height-1)/scale, height-1, aspect, yorg, xorg, gx, gy, px, py
 dc.b YORG,GY,RI.SUB,RI.OVER,RI.MULT
 dc.b RI.ROLL,RI.ADD			y' = (height-1)*(1-(y-yorg)/scale)
* y', (height-1)/scale, aspect, -, xorg, gx, -, px, py
 dc.b PY,RI.ADD,RI.STORE+YORG		t = py + y'
* (height-1)/scale, aspect, py', xorg, gx, -, px, py
 dc.b GX,XORG,RI.SUB,RI.MULT,RI.MULT	x' = (x-xorg)*aspect*(height-1)/scale
* x', py', -, -, -, px, py
 dc.b PX,RI.ADD,RI.STORE+PX		px' = px + x'
* py', -, -, -, px', py
 dc.b RI.STORE+PY			py' = t
* -, -, -, -, px', py'
 dc.b RI.TERM

* N.B. The above is all arranged so that errors in scaling the graphics
* coordinates will effectively result in px, py being unchanged.

	end
