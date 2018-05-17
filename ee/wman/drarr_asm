* Window Manager Draw Arrows	V1.01	 1986	Tony Tebby   QJUMP
*					  2002	Marcel Kilgus
*
* 2002-11-13  1.01  Allocated more space for pattern on stack (MK)
*
	section wman
*
	xdef	wm_drupa	draw up arrows
	xdef	wm_drdna	draw down arrows
	xdef	wm_drlta	draw left arrows
	xdef	wm_drrta	draw right arrows
*
	xref	wm_ssclr	set single colour blob
*
	xref	wm_uparr
	xref	wm_dnarr
	xref	wm_ltarr
	xref	wm_rtarr
*
	include dev8_keys_wwork
	include dev8_keys_qdos_io
*
*	d0   s
*	d1   s
*	d2   s	0
*	d3   s	-1
*	d4 c  p area size
*	d5 c  p area origin
*	d6   sp position increment
*	d7   sp blob count
*	a0 c  p window channel ID
*	a4 c  p window definition
*
reglist reg	d4-d7/a1/a2
pattern equ	$60			pattern frame
frame	equ	$60+4			pattern + pointer to blob
blob	equ	$60
*
wm_drupa
	movem.l reglist,-(sp)
	pea	wm_uparr(pc)		draw up arrows
	bra.s	wm_drscr
wm_drdna
	movem.l reglist,-(sp)
	addq.w	#ww.scarr-1,d5		for down arrow, origin at bottom
	pea	wm_dnarr(pc)		draw down arrows
*
* draw scrolling arrows
*
wm_drscr
	sub.w	#pattern,sp
	clr.w	d4
	swap	d4			width of scroll area
	sub.w	#ww.scawd,d4		less arrow width
	blt.s	wda_exit
	moveq	#ww.scasp,d6
	divu	d6,d4			divided by spacing
	move.w	d4,d7			... number to draw (-1)
*
	add.l	#ww.scawd<<16,d4	width +
	lsr.l	#1,d4			... remainder (/2)
	clr.w	d4			... is offset to first one
	add.l	d4,d5
	swap	d6			and spacing

	move.w	wwa_psac+wwa.clen(a3),d1 ; scroll arrow colour
	bra.s	wda_do
*
*
wm_drlta
	movem.l reglist,-(sp)
	pea	wm_ltarr(pc)		draw left arrows
	bra.s	wm_drpan
wm_drrta
	movem.l reglist,-(sp)
	add.l	#(ww.pnarr-1)<<16,d5	for right arrow, origin at right
	pea	wm_rtarr(pc)		draw right arrows
*
* draw panning arrows
*
wm_drpan
	sub.w	#pattern,sp
	sub.w	#ww.pnaht,d4		 ; height less arrow height
	blt.s	wda_exit
	ext.l	d4
	moveq	#ww.pnasp,d6
	divu	d6,d4			 ; divided by spacing
	move.w	d4,d7			 ; ... number to draw (-1)
*
	swap	d4
	add.w	#ww.pnaht,d4
	lsr.w	#1,d4			 ; remainder (/2)
	add.w	d4,d5			 ; is offset of first
	move.w	wwa_psac(a3),d1 	 ; pan arrow colour
*
*
wda_do
	move.l	sp,a2			set pointer to colour pattern
*
	move.l	ww_wstat(a4),a1
	bsr.l	wm_ssclr
*
wda_loop
	moveq	#iop.wblb,d0		write blob
	move.l	d5,d1			... position
	moveq	#0,d2
	moveq	#-1,d3
	move.l	blob(sp),a1		... pointer to blob
	trap	#3
*
	add.l	d6,d5			update position
	dbra	d7,wda_loop
*
wda_exit
	add.w	#frame,sp
	movem.l (sp)+,reglist
	rts
	end
