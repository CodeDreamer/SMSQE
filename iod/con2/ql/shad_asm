;	Routine to put a shadow "behind" a window.
;
;	This is an improved version of the previous routine,
;	in that it allows the user to specify x and y shadow
;	displacements: it does not yet alter the displacment depending
;	on the "height" above the windows behind.
;
	section driver
;
	include 'dev8_keys_con'
	include 'dev8_keys_qdos_sms'
;
	xdef	pt_shad
;
;	This is the main entry point: it determines the required size
;	and position of the shadow blocks from the parameters
;	passed and the window size, and calls the shading routine
;	to put them up.
;
;	Registers:
;		Entry			Exit
;	D1	shadow offset (x,y)	smashed
;	A0	base of cdb		preserved
;	A1				smashed
;
reglist reg	d1-d7
;
pt_shad
	movem.l reglist,-(sp)
;
;	Top/bottom shadow
;
shade
	movem.l sd_xhits(a0),d4/d5/d6/d7 ; get parameters
	bsr.s	shad_area		; and shadow size / position
	bsr.s	block_sh		; shade that bit
;
;	Side shadow - calculations as for top, but swapping x and y.
;
	swap	d4
	swap	d5
	swap	d6
	swap	d7
	bsr.s	shad_area
	swap	d2
	swap	d3
	bsr.s	block_sh
	moveq	#0,d0			; no error
	movem.l (sp)+,reglist
	rts
	page
;
;	This routine calculates a shadow block size and origin
;
;	d2  r	shadow sizes
;	d3  r	shadow origin
;	d4 c	hit size
;	d5 c	hit origin
;	d6 c	outline size
;	d7 c	outline origin
;
;	comments refer to top / bottom shadow
;
shad_area
	move.l	d4,d2			set x,y sizes
	sub.w	d6,d2			... y is difference
	neg.w	d2			... positive!!
;
	move.l	d7,d3			assume left
	swap	d3
	move.l	d5,d0
	swap	d0
	cmp.w	d3,d0			is it left?
	bne.s	shad_side		... yes
	move.l	d6,d0			... no, add outline width
	sub.l	d4,d0			... less hit width
	swap	d0
	add.w	d0,d3
shad_side
	swap	d3
;
	move.w	d7,d3			assume top
	cmp.w	d5,d7			is it at top or bottom?
	bne.s	shad_sdone		... top
	add.w	d4,d3			... bottom
shad_sdone
	rts
;
	page
;
;	This routine puts up a block of shading of specified size
;	at a given position. If the shading would overlap the screen
;	edges then it is clipped to fall within the screen.
;
;	Registers:
;		Entry			Exit
;	D2	shadow size
;	D3	shadow origin
;	A0	base of cdb
;
block_sh
	movem.l d5/a0-a2/a4/a5,-(sp)
	move.l	d3,-(a7)		; copy parameters
	move.l	d2,-(a7)		; to make a smashable copy
;
prm.xs	equ	$00			; xsize
prm.ys	equ	$02			; ysize
prm.x	equ	$04			; xorg
prm.y	equ	$06			; yorg
;
	move.l	a7,a1			; set pointer
;
	moveq	#-1,d1			; read...
	move.b	d1,d2
	moveq	#sms.dmod,d0		; ...the display mode
	trap	#1
	lea	pat4_tab(pc),a5 	; assume mode 4
	tst.b	d1			; is it?
	beq.s	mode_ok 		; yes, ok then
	lea	pat8_tab(pc),a5 	; no, use mode 8 pattern
mode_ok
;
	sub.w	#12,a7			; make a bit more working space
	move.w	prm.y(a1),d0		; the y position...
	mulu	pt_bytel(a3),d0 	; ...becomes the offset...
	move.l	sd_scrb(a0),a2		; ...with the screen base...
	add.l	d0,a2			; ...gives the line address
;
prm.xl	equ	-2
prm.xls equ	-4
prm.xm	equ	-6
prm.xms equ	-8
prm.xr	equ	-10
prm.xrs equ	-12
;
	move.w	prm.x(a1),d0		; the x position...
	and.w	#6,d0			; ...becomes the number...
	sub.w	#8,d0			; ...of pixels...
	neg.w	d0			; ...at the left hand edge
	move.w	d0,prm.xls(a1)
;
	move.w	prm.x(a1),d0		; it also becomes...
	lsr.w	#2,d0			; ...the address offset...
	and.w	#$fffe,d0		; ...of the left hand edge
	move.w	d0,prm.xl(a1)
;
	addq.w	 #2,d0			; the middle starts 2 bytes...
	move.w	d0,prm.xm(a1)		; ...higher in memory
;
	move.w	prm.x(a1),d0		; the...
	add.w	prm.xs(a1),d0		; ...right hand edge...
	move.w	d0,d1
	and.w	#6,d0			; ...becomes the number...
	move.w	d0,prm.xrs(a1)		; ...of pixels at the right
;
	lsr.w	#2,d1			; and the address offset...
	and.w	#$fffe,d1		; ...of the right hand edge
	move.w	d1,prm.xr(a1)
;
	move.w	prm.xs(a1),d0		; the width of the middle...
	sub.w	prm.xls(a1),d0		; ...is what's not at the left...
	sub.w	prm.xrs(a1),d0		; ...or the right...
	move.w	d0,prm.xms(a1)
;
	ble.s	no_mid			; there is no middle
	move.w	prm.xm(a1),a4
	lea	0(a2,a4.w),a4		; start of middle
	move.w	prm.ys(a1),d3		; number of lines to fill
	moveq	#0,d5			; fill everything
	lsr.w	#3,d0			; this many columns to fill
	bra.s	end_mid
st_mid
	bsr.s	fil_col
	addq.w	#2,a4
end_mid
	dbra	d0,st_mid
no_mid
;
;	If there was a middle it's now shaded, so we can fill in
;	the left and right edges.
;
	moveq	#8,d0			; get pixel mask number...
	sub.w	prm.xls(a1),d0		; ...on left hand side
	lea	mask_tab(pc),a4 	; turn it into...
	move.w	0(a4,d0.w),d5		; ...a mask
	not.w	d5
	move.w	prm.xl(a1),d0		; get x left offset
	cmp.w	prm.xr(a1),d0		; same as right?
	beq.s	fil_rt			; yes, only one column to do
	lea	0(a2,d0.w),a4		; it's here
	move.w	prm.ys(a1),d3		; this many lines
	bsr.s	fil_col 		; do it
	moveq	#0,d5			; use full right mask
fil_rt
	move.w	prm.xrs(a1),d0		; pixel mask number for rhs
	lea	mask_tab(pc),a4
	or.w	0(a4,d0.w),d5		; modify mask
	cmp.w	#$ffff,d5		; no pixels allowed?
	beq.s	no_rt			; no, don't bother shading then
	move.w	prm.xr(a1),a4		; right column is put in...
	add.l	a2,a4			; ...here
	move.w	prm.ys(a1),d3		; this many lines
	bsr.s	fil_col
no_rt
;
;	Now we've finished, just tidy up and exit
;
exit
	move.l	a1,a7			; remove temporary storage
	addq.l	#8,a7			; and parameter copies
	movem.l (sp)+,d5/a0-a2/a4/a5	; pop smashed registers
	rts
;
;	This subroutine fills in one column of shading.  It assumes
;	that successive rows of the same column are 128 bytes apart.
;	The shade mask has a clear bit wherever shading is permitted.
;
;	Registers:
;		Entry			Exit
;	D3	no. of lines to fill	preserved
;	D5	shade mask		preserved
;	A4	column to fill		preserved
;	A5	pattern to use		preserved
;
fil_col
	movem.l d0/d1/d3/a4/a5,-(sp)
	move.l	a4,d0			; start address...
	and.l	#$40000,d0		; in 256k space!!!!!!!!!!!!!!!!!
	divu	#6,d0			; ...MOD 6 is the first...
	swap	d0			; ...shade pattern number
	bra.s	end_fil
st_fil
	move.w	0(a5,d0.w),d1		; get a pattern
	or.w	d5,d1			; mask unused bits
	and.w	d1,(a4) 		; stuff it into the screen
	add.w	pt_bytel(a3),a4 	; next screen address
	addq.w	#2,d0			; next pattern
	cmp.w	#6,d0			; off end?
	blt.s	end_fil 		; no
	moveq	#0,d0			; restart pattern
end_fil
	dbra	d3,st_fil
	movem.l (sp)+,d0/d1/d3/a4/a5
	rts
;
pat4_tab
	dc.w	%1101101111011011
	dc.w	%0110110101101101
	dc.w	%1011011010110110
;
pat8_tab
	dc.w	%0111110100111100
	dc.w	%1111011111110011
	dc.w	%1101111111001111
;
mask_tab
	dc.w	%1111111111111111
	dc.w	%0011111100111111
	dc.w	%0000111100001111
	dc.w	%0000001100000011
;
	end
