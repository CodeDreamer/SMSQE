; IOD_CON2_16_SHAD_ASM - Mode 32&33 shadow routine V1.01  2002  Marcel Kilgus
; Based on original QL mode routine by Tony Tebby.
;
; 2005-04-07  1.01  Does not put shadow twice at the intersection (MK)
;
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

	xref	pt_smove
	xref	pt_tstov
	xref	pt_fillsv
	xref.s	pt.spxlw
	xref.s	pt.rpxlw

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
	move.l	d2,-(sp)
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
	move.l	(sp)+,d0		; x,y shadow size
	sub.w	d0,d2			; subtract overlapping area
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
;	at a given position.
;	Unfortunatelly it's possible that this routine is called
;	several times without the background being refreshed,
;	leading to unwanted multiple shadow effects. Therefore the
;	background is first reconstructed using the window pile and
;	then manipulated afterwards.
;
;	Registers:
;		Entry			Exit
;	D2	shadow size
;	D3	shadow origin
;	A0	base of cdb
;
block_sh
	movem.l d1/d5/a0-a5,-(sp)
	tst.w	d2			; exit if x or y size is 0
	beq	no_shadow
	swap	d2
	tst.w	d2
	beq	no_shadow

	movem.l d2-d3/a3,-(sp)
	moveq	#sms.achp,d0
	move.l	#sd.extnl+sd_end,d1	; room for a fake cdb
	moveq	#0,d2
	trap	#1
	movem.l (sp)+,d2-d3/a3
	tst.l	d0
	bmi	no_shadow		; don't do anything
	adda.l	#sd.extnl,a0

	moveq	#pt.spxlw,d0
	moveq	#pt.rpxlw,d1
	add.w	d2,d1
	lsr.w	d0,d1
	addq.w	#1,d1
	asl.l	#2,d1
	move.w	d1,sd_linel(a0) 	; line increment of save area
	swap	d2
	mulu	d2,d1			; size of save area

	move.l	d1,sd_wssiz(a0) 	; fill in some cdb informations
	movem.l d2-d3,sd_xhits(a0)
	movem.l d2-d3,sd_xouts(a0)

	movem.l d2-d3/a0/a3,-(sp)
	moveq	#sms.achp,d0
	moveq	#0,d2
	trap	#1
	move.l	a0,a1
	movem.l (sp)+,d2-d3/a0/a3
	tst.l	d0
	bmi	release_cdb		; exit, shadow is not that important

	move.l	a1,sd_wsave(a0) 	; update cdb
	st	sd_mysav(a0)

	jsr	pt_fillsv		; fill area with background

	lea	pt_tail(a3),a4
	move.l	d2,d1
	move.l	d3,d2			; upper left corner
	add.l	d2,d1			; lower right corner
	move.b	pt_dmode(a3),d3 	; current mode
rest_loop
	move.l	(a4),d0 		; next window up
	beq.s	rest_end		; there isn't one, we've finished
	move.l	d0,a4
	lea	-sd_prwlb(a4),a1	; point to its CDB
	cmp.b	sd_wmode(a1),d3 	; is its mode same as current?
	bne.s	rest_loop		; no, don't restore it
	tst.b	sd_wlstt(a1)		; is it unlockable?
	bgt.s	rest_loop		; yes, don't restore
	exg	a0,a1			; compare correct sizes
	jsr	pt_tstov(pc)		; do the windows overlap?
	exg	a0,a1
	bne.s	rest_loop		; no, do nothing
*
	jsr	pt_smove(pc)		; restore into our save area
	bra.s	rest_loop

rest_end
	movem.w sd_xouto(a0),d0-d1	; x and y origin
	add.w	d0,d0			; 2 * x for 16bit
	mulu	pt_scinc(a3),d1 	; y * line increment
	move.l	pt_sbase(a3),a2
	add.w	d0,a2
	add.l	d1,a2			; now base of shadow on screen

	moveq	#0,d5
	move.b	pt_dmode(a3),d5 	; Should be ptm.pc16 or ptm.q40
	subi.b	#ptm.pc16,d5		; 0 = QPC, 1 = Q40
	add.b	d5,d5			; 0 = QPC, 2 = Q40
	move.w	sh_mask(pc,d5.w),d5	; and-mask for RGB reduce
	movem.w sd_xouts(a0),d2-d3	; x and y size
	move.l	sd_wsave(a0),a1 	; reconstructed background
shad_y
	movem.l d2/a1-a2,-(sp)
shad_x
	move.w	(a1)+,d0		; gggbbbbbrrrrrggg (QPC example)
	ror.w	#1,d0			; ggggbbbbbrrrrrgg
	and.w	d5,d0			; ggg0bbbb0rrrr0gg = 1/2 RGB factors
	move.w	d0,(a2)+
	subq.w	#1,d2
	bne.s	shad_x

	movem.l (sp)+,d2/a1-a2
	adda.w	sd_linel(a0),a1        ; next line of background
	adda.w	pt_scinc(a3),a2        ; next line on screen
	subq.w	#1,d3
	bne.s	shad_y

	move.l	a0,a4
	move.l	sd_wsave(a0),a0        ; release save area
	moveq	#sms.rchp,d0
	trap	#1
	move.l	a4,a0
release_cdb
	suba.l	#sd.extnl,a0
	moveq	#sms.rchp,d0	       ; release cdb
	trap	#1
no_shadow
	movem.l (sp)+,d1/d5/a0-a5
	rts

sh_mask
	dc.w	%1110111101111011	; QPC mask
	dc.w	%0111101111011110	; Q40 mask

	end
