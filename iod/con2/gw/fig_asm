	section gw
* common routine to draw graphics figures
*
* 2003-03-04  1.01  Made screen aspect ratio variable. Parts have been
*		    converted to SMSQ syntax to be able to include keys_con (MK)
* 2016-04-16  1.02  Copy alpha blending weight to data block (MK)

	xdef	GW_FIG,GW_PSORG,GW_PSCAL,GW_ASP

	xref	GW_TRANS,GW_PITT,GW_PIXAD,GW_PIXEL
	xref	RI_ABEX,RI_ABEXB,RI_K_B

	include 'dev8_Minerva_INC_GW'
;	 include 'dev8_Minerva_INC_SD'
	include 'dev8_Minerva_INC_RI'
	include 'dev8_Minerva_INC_MT'
	include 'dev8_Minerva_INC_assert'
	include 'dev8_keys_qdos_io'
	include 'dev8_keys_con'
	include 'dev8_keys_chn'
	include 'dev8_keys_iod'
 
* This code is entered with D0 and A1 as follows:
*	      (A1) stack: top (low address),   ...,   bottom (high address)
* IOG.DOT							Y,	  X
* IOG.LINE			 Y finish,     X finish,  Y start,  X start
* IOG.ARC     subtended angle,	 Y finish,     X finish,  Y start,  X start
* IOG.ELIP     rotation angle, minor axis, eccentricity, Y centre, X centre

	assert	IOG.DOT,IOG.LINE-1,IOG.ARC-2,IOG.ELIP-3

xs	equ	-6*1	start point x-coord
ys	equ	-6*2	start point y-coord
xf	equ	-6*3	finish point x-coord
yf	equ	-6*4	finish point y-coord
a	equ	-6*5	angle

ec	equ	-6*3	eccentricity of ellipse, normally greater than one
ax	equ	-6*4	axis of ellipse, normally minor with eccentricity >= 1

ca	equ	-6*5	cosine(angle)
sa	equ	-6*6	sine(angle)

base	equ	SA

xo	equ	-6*7	x origin
yo	equ	-6*8	y origin
h1	equ	-6*9	screen height - 1
sc	equ	-6*10	scale / (height - 1)
masp	equ	-6*11	aspect ratio, divided by two if mode 8
asp	equ	-6*12	aspect ratio

v	equ	-6*13	coefficient of x
u	equ	-6*14	coefficient of y
gamma	equ	-6*15	coefficient of x*y
beta	equ	-6*16	coefficient of y^2
alpha	equ	-6*17	coefficient of x^2

gw_fig
	link	A6,#LNK 	make data frame
	movem.l SAV,-(SP)	save required registers at base of frame
	lea	MENU-IOG.DOT(PC,D0.W),A5
	move.b	(A5),D0 	pick up pointer byte
	add.w	D0,A5		point to menu list for operation
	move.b	(A5)+,D0	pick up byte that says how many bytes at A1
	lea	0(A1,D0.W),A4	point A4 to base of stack
	move.b	(A5)+,D0	pick up first proc type byte
	moveq	#8,D7		start octant count register (OK for elipse)
	jsr	PREP(PC,D0.W)	go to first section of code for operation
	move.l	A4,A1		just in case caller would like it reset
	moveq	#0,D0		always return OK, even if we drew nothing
	movem.l (SP)+,SAV	restore a few registers
	unlk	A6		release the data frame
	rts

menu dc.b MEN_P-*,MEN_L-*-1,MEN_A-*-2,MEN_E-*-3

men_p dc.b -YS,PRE_P-PREP,SCA_P-M,PRO_P-PROC point
men_l dc.b -YF,PRE_L-PREP,SCA_P-M,PRO_L-PROC,SWA_P-M,SCA_P-M,$EC line
men_a dc.b -A,PRE_A-PREP,OCTS_A-M,SINCOS-M,SCA_P-M,PRO_A-PROC
	dc.b SWA_P-M,SCA_P-M,CALC_A-M,OPT_X-M,$DC arc
men_e dc.b -A,PRE_E-PREP,SINCOS-M,SCA_E-M,PRO_E-PROC
	dc.b CALC_E-M,OPT_X-M,$3D ellipse
 ds.w 0 Note: the type byte need no longer be so obscure.

gw_psorg
	assert	6,SD_XORG-SD_YORG
	move.l	SD_YORG+8(A0),-(A1)
	move.l	SD_YORG+4(A0),-(A1)
	move.l	SD_YORG+0(A0),-(A1)
	rts

gw_pscal
	move.w	SD_YSIZE(A0),-(A1)
	subq.w	#1,(A1)
	moveq	#RI.FLOAT,D0
	jsr	RI_ABEX(PC)
	move.l	SD_SCAL+2(A0),-(A1)
	move.w	SD_SCAL(A0),-(A1)
	rts

gw_asp		; push screen aspect ratio onto stack
; if NTSC
;	move.l	#$6623B79C,-(A1) 2/(4/3*575/512*51.2/51.95) * 1.173
;	move.w	#$801,-(A1)
; else
*	 move.l  #$56B851EC,-(A1) 2 / ( (4/3) * (575/512) * (51.2/51.95) )
*	 move.w  #$801,-(A1)	  (acually rounded(?) to 1.355)
* The aspect ratio, according to the above, should have been pushing
* $56BBE1BA onto the stack! As we can multiply/divide quicker (now) if the
* least significant half of the mantissa is zero (and keeping as close, one
* part in 32647, to the WRONG value, but on the RIGHT side of it) ...
;;;	   clr.w   -(A1)
;;;	   move.l  #$80156B9,-(A1)  2 / ( (4/3) * (575/512) * (51.2/51.95) )
; endif
	move.l	a0,-(sp)
	move.l	chn_drvr-sd.extnl(a0),a0
	move.w	pt_asprt+4-iod_iolk(a0),-(a1)
	move.l	pt_asprt-iod_iolk(a0),-(a1)
	move.l	(sp)+,a0
	rts

prep

pre_e
	bsr.s	ABEXM		go convert angle to sine/cosine
	moveq	#ORD_E-M,D0
	cmp.w	#$801,EC(A4)	we want major >= minor ...
	bcc.s	ECCOK		... so if eccentricity is less than 1.0 ...
	moveq	#INV_E-M,D0	... first rotate by PI/2 and flip eccentricity
eccok
	bsr.s	ABEXN		do eccentricity calcs
	bra.s	SETOCT

pre_a
	bsr.s	ABEXM		get octants (also halves the angle)
	and.w	(A1),D7 	check if int(angle/(pi/4)) bit 3 is set
	beq.s	NOFLIP		not set, it's OK (this works for BIG angles!)
	not.w	(A1)		if set, invert it all (gets -ve OK)
noflip
	move.w	(A1)+,D7	found number of octants in subtended angle
* This only needs to be a low estimate, as the algorithm understands that arcs
* are a pain, and it will let them search on for a bit to find their end point.
	bsr.s	ABEXM		convert angle to sine/cosine

pre_l
	and.b	#7,D7		line -> 0, arc -> 0..7

setoct
	move.b	D7,OCTS(A6)	set the octant count
pre_p

	lea	BASE(A4),A1	put A1 at standard position
	bsr.s	GW_PSORG	copy origins to stack
	bsr.s	GW_PSCAL	copy scale data to stack
	moveq	#SETSC-M,D0	adjust the scale stuff
	bsr.s	ABEXN

* Set up miscellaneous bits of the frame data

	btst	#SD..XOR,SD_CATTR(A0)
	sne	OVER(A6)	flag $FF = "OVER -1"
	move.b	SD_FMOD(A0),FMOD(A6) flag <>0 = "FILL 1"
	move.b	SD_ALPHA(a0),ALPHW(a6)	Alpha blending weight

	assert	SD_XMIN,SD_YMIN-2,SD_XSIZE-4,SD_YSIZE-6
	assert	XMIN,YMIN-2,XSIZE-4,YSIZE-6
	movem.l SD_XMIN(A0),D0-D1
	movem.l D0-D1,XMIN(A6)	window top left corner and size

* Set up stuff that's dependent on the current screen mode

	moveq	#MT.DMODE,D0
	moveq	#-1,D1
	moveq	#-1,D2
	trap	#1
	asr.b	#4,D1		Shift bit 3 into X. X = 0/1, mode 512/256
	subx.w	D0,D0
	bsr.s	GW_ASP		put on a copy of the aspect ratio for scaling
	add.w	D0,(A1) 	divide scale by factor of two if mode 256
	bsr.s	GW_ASP		put on aspect ratio
	neg.w	D0
	addq.w	#1,D0	      
	move.w	D0,XINC(A6)	2 for mode 256 or 1 for mode 512

	bsr.s	ABEXM		scale the start point input params
	lea	X0(A6),A2	where to put start point
	bsr.s	XYEXT		pick off and adjust start point
	move.b	(A5)+,D0
	jmp	PROC(PC,D0.W)	jump to what to do next

abexm
	move.b	(A5)+,D0	use next menu byte to decide what calcs to do
abexn
	ext.w	D0
	beq.s	ANRTS		dummy entries have D0=0 at this point
	lea	M,A3
	add.w	D0,A3
	jsr	RI_ABEXB(PC)
	beq.s	ANRTS
	addq.l	#4,SP		don't accept errors, draw nothing
anrts
	rts

xyext
	move.l	(A1)+,D1	get x/y (doubled)
	asr.w	#1,D1		make y proper, propagating sign
	subx.w	D2,D2		carry = rounding
	sub.w	D2,D1		do y rounding
	swap	D1		put y in msw for now
	move.w	XINC(A6),D0	get x-increment, 1/2
	asr.w	D0,D1		make x near proper, propagating sign
	subx.w	D2,D2		carry = rounding
	sub.w	D2,D1		do x rounding
	subq.w	#1,D0		change shift to 0/1
	lsl.w	D0,D1		put x to final place (lsb=0 for mode 8)
	swap	D1		swap back to right way round
	move.l	D1,(A2)+	store x/y
	rts

proc

* IOG.DOT: call just the bits we need, and forget all the dross.

pro_p
	movem.w X0(A6),D0-D1
	jsr	GW_PIXAD(PC)
	jmp	GW_PIXEL(PC)

* Process the ellipse
pro_e
	move.l	D1,(A2)+	repeat start point as end point
	cmp.w	#$800,AX(A4)	is the narrow bit less than one pixel?
	bcs.s	ANRTS		yes - we'll not bother to draw anything!
	; There is the idea of drawing two shallow arcs, but I don't know (LWR)
	; However, eventually it would be a good idea to break an ellipse into
	; sections, anyway, so we can draw HUGE ones without wandering miles
	; offscreen in the process.
allset
	bsr.s	ABEXM		do the main calculations
	bsr.s	ABEXM		produce the rounding factor
	bra.s	ABGUV

* IOG.LINE: simple one
pro_l
	bsr.s	ABEXM		swap in end point
	bsr.s	ABEXM		process end point
	bsr.s	XYEXT		fetch end point
	clr.l	D2		alpha = 0
	clr.l	D3		beta = 0
	clr.l	D4		gamma = 0
	assert	X0,Y0-2,X1-4,Y1-6
	movem.w X0(A6),D0-D1/D6-D7 get start and end points (sign extended)
	sub.l	D0,D6
	cmp.b	#2,XINC+1(A6)	mode 512?
	beq.s	USET		(note: 512 mode start/end x already even)
	add.l	D6,D6		u = delta x * 2 / mode
uset
	sub.l	D1,D7
	add.l	D7,D7		v = delta y * 2
	bra.s	TRANS		go do it!

* Process an arc
pro_a
	bsr.s	ABEXM		swap in end point
	bsr.s	ABEXM		process end point
	bsr.s	XYEXT		fetch end point
	movem.w X0(A6),D0-D4	pick up start and end points
	sub.l	D0,D2		x1 - x0
	sub.l	D1,D3		y1 - y0
	movem.l D2-D3,-(A1)	put them on the stack
	bra.s	ALLSET

* We wish to arrange for the values used in squch and diach to have as many
* significant bits as we can, to keep accuracy.
* Also, in order to have NO rounding errors, the values we set up need alpha
* and beta multiples of four and gamma, u and v even.

* Further to the above, the following are the worst case values that we will
* need to cope with signed arithmetic on:

*	alpha + beta + 2*gamma
*	alpha/2 + gamma/2 + u + v
*	beta/2 + gamma/2 + u + v
*	alpha/4 + u/2 + beta/2 + gamma/2 + v
*	beta/4 + v/2 + alpha/2 + gamma/2 + u

* We actually know that alpha, beta and gamma are less than one, except for
* an arc, when alpha and beta are less than two, but gamma is zero.
* The main interest is the values of u and v. If these are less than one, we've
* got a pretty degenerate figure to draw!

* Finally, when the ellipse was set up, it tried to arrange itself a start
* point on the minor axis. However, aspect ratio scaling will disturb this,
* meaning we need an extra guard bit to stop the algorithm screwing up.

* We have already arranged the top of the arithmetic stack to contain:

* abs(u')+abs(v')+8,alpha/2,beta/2,gamma,u,v,...

abguv
	move.w	(A1),D5
	addq.l	#6,A1
	sub.w	#$800+27,D5	dividing power of two

	bsr.s	OPTIM
	add.l	D7,D7
	move.l	D7,D2		alpha
	bsr.s	OPTIM
	add.l	D7,D7
	move.l	D7,D3		beta
	bsr.s	OPTIM
	move.l	D7,D4		gamma
	bsr.s	OPTIM
	move.l	D7,D6		u
	bsr.s	OPTIM		v

	movem.w X0(A6),D0-D1	put start coords them into their registers
trans
	move.b	(A5)+,TYPE(A6)	finally set the call type: L=EC A=DC E=3D

* Now transform from coefficients to Pitteway algorithm variables
* and set up the initial values. Also find the starting octant of
* the figure, the pixel address and masks, and rearrange the registers

	jsr	GW_TRANS(PC)

* Now the figure may be drawn. Call PITTEWAY !!

	jmp	GW_PITT(PC)

optim
	sub.w	D5,(A1) 	scale parameters to improve numerics
	bpl.s	GOOPT		are we getting a tiny value (TOO tiny!)
	clr.w	(A1)		yes, just zap exponent so NLINT is happy
goopt
	moveq	#RI.NLINT,D0
	jsr	RI_ABEX(PC)
	move.l	(A1)+,D7
	add.l	D7,D7		most want to be even
	rts

* Execution blocks for initial arc / elipse parameter arithmetic.

octs_a
* Note: For arc, we are only interested in the half angle.
* a*2, yf, xf, ys, xs
 dc.b RI.HALVE,RI.DUP,RI.K,RI.PI-3,RI.DIV,RI.INT,RI.TERM
* int(a*2/(pi/4)).w, a, yf, xf, ys, xs

	assert	A,CA,SA+6
sincos
* N.B. we've turned the y-axis upside down!
* a, ...
 dc.b RI.DUP,RI.COS,RI.SWAP	  ca = cosine(a)
 dc.b RI.SIN,RI.NEG,RI.TERM	  sa = -sine(a)
* sa, ca, ...
 
inv_e
* -ca, sa, ax/ec, 1/ec, yc, xc
 dc.b RI.NEG,RI.SWAP			angle = angle' + pi/2
 dc.b AX,EC,RI.MULT,RI.STORE+AX,RI.TERM ax = ax' * ec' = minor axis
* sa, ca, ax, -, yc, xc

ord_e
* sa, ca, ax, ec, yc, xc
 dc.b EC,RI.RECIP,RI.STORE+EC,RI.TERM	ec = 1/ec
* sa, ca, ax, -, yc, xc

* Execution blocks for scaling

setsc
* scale, h-1, yo, xo, asp, masp,...
 dc.b RI.OVER,RI.SWAP,RI.DIV,RI.TERM
* (h-1)/scale, h-1, yo, xo, asp, masp,...

* N.B. Note that both start and end point use the same calculation. This
* ensures that sequences of lines and arcs, where the end point of one is the
* start point of the next, will be guaranteed to come up with the same pixel.

sca_e
* ..., sa, ca, ax, -, yc, xc
 dc.b AX,SA,RI.MULT,XS,RI.ADD,RI.STORE+XS	xs = xc + v
* ..., sa, ca, ax, ec, yc, xs
 dc.b AX,CA,RI.MULT,YS,RI.ADD,RI.STORE+YS	ys = yc + u (later negation)
 dc.b AX,SC,RI.MULT,RI.STORE+AX 		ax = ax*(h-1)/scale
sca_p
* ..., (h-1)/scale, h-1, yo, xo, asp, masp, ...
 dc.b YO,YS,RI.SUB,SC,RI.MULT
 dc.b H1,RI.ADD,RI.DOUBL,RI.INT 		int(2*((y-yf)*(h-1)/scale+h-1))
 dc.b XS,XO,RI.SUB,SC,RI.MULT
 dc.b ASP,RI.MULT,RI.DOUBL,RI.INT,RI.TERM	int(2*(x-xo)*(h-1)/scale*asp)
* int(x').w, int(y').w, ...

swa_p
 dc.b YF,RI.STORE+YS,XF,RI.STORE+XS replace start point with end point

m dc.b RI.TERM
* execution blocks are referenced byte relative to M. An entry in the menu of
* zero will in fact skip doing any calculations at all. This is used so that we
* can break up the calcuations into convienient bits.

* Execution block for arc arithmetic
calc_a
* int(x1-x0).l, int(y1-y0).l, ...
 dc.b RI.FLONG,ASP,RI.DIV,RI.STORE+XF re-incorporate aspect ratio with delta x
 dc.b RI.FLONG
* yf-ys, ... 
 dc.b RI.DUP,CA,RI.MULT 	(yf - ys) * ca
 dc.b XF,SA,RI.MULT		(xf - xs) * sa
 dc.b RI.SUB,MASP,RI.DIV		v = ((yf-ys) * ca - (xf-xs) * sa)/masp
 dc.b RI.SWAP,SA,RI.MULT	(yf - ys) * sa
 dc.b XF,CA,RI.MULT		(xf - xs) * ca
 dc.b RI.ADD				u = (xf-xs) * ca + (yf-ys) * sa
* u, v, ...
 dc.b RI.ZERO				gamma = 0
 dc.b SA,MASP,RI.SQUAR,RI.DIV		beta/2 = 2 * sa / 2 / masp^2
 dc.b SA,RI.TERM			alpha/2 = 2 * sa / 2
* alpha/2, beta/2, gamma, u, v, ...

opt_x
* alpha/2, beta/2, gamma, u, v, ...
 dc.b U,RI.ABS,V,RI.ABS,RI.ADD,RI.N,8,RI.ADD	abs(u')+abs(v')+8 (for round)
* abs(u')+abs(v')+8, alpha/2, beta/2, gamma, u, v, ...
 dc.b RI.TERM

* Execution block for ellipse calculations (last, to give M more room)
calc_e
* ...
 dc.b AX,SA,RI.MULT,MASP,RI.DIV 	v = ax * sa / masp
* v, ...
 dc.b AX,CA,RI.MULT			u = ax * ca
* u, v, ...
 dc.b RI.ONE,EC,RI.SQUAR,RI.SUB 	ee = 1 - 1 / ec^2
* ee, u, v, ...
 dc.b RI.DUP,SA,RI.MULT
* ee*sa, ee, u, v, ...
 dc.b RI.DUP,SA,RI.MULT
* ee*sa^2, ee*sa, ee, u, v, ...
 dc.b RI.SWAP,CA,RI.MULT,MASP,RI.DIV	gamma = ee * sa * ca / masp
* gamma, ee*sa^2, ee, u, v, ...
 dc.b RI.ONE,RI.ROLL,RI.SUB,RI.HALVE	alpha/2 = (1 - ee * sa^2) / 2
* (1-ee*sa^2)/2, gamma, ee, u, v, ...
 dc.b RI.ROLL,RI.HALVE,RI.ONE,RI.SWAP,RI.SUB
* 1-ee/2, (1-ee*sa^2)/2, gamma, u, v, ...
 dc.b RI.OVER,RI.SUB
* (1-ee*ca^2)/2, (1-ee*sa^2)/2, gamma, u, v, ...
 dc.b MASP,RI.SQUAR,RI.DIV		beta/2 = (1-ee*ca^2) / masp^2 / 2
* beta, alpha, gamma, u, v, ...
 dc.b RI.SWAP,RI.TERM
* alpha, beta, gamma, u, v, ...

	end
