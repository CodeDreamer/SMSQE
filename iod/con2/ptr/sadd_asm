; Set up pointer area addresses 		V2.04   1991  Tony Tebby
;							 2005  Marcel Kilgus
;
; 2003-02-24  2.01  Now also returns sprite width in MSW of d5 (MK)
; 2003-04-30  2.02  Returns -1 in MSW of d5 instead of width if no alpha channel
; 2005-01-25  2.03  Changes for mouse pointer clipping (MK)
; 2005-11-08  2.04  Flags a0 with 0 for pt_sadd and pt_saddp (MK)
;
	section driver
;
	xdef	pt_sadd
	xdef	pt_saddp
	xdef	pt_swind
;
	xref	sp_ptr
	xref.s	pt.spxsw ; shift pixel to sprite word
	xref.s	pt.rpxsw ; round up pixel to sprite word (2^spxsw-1)
;
	include dev8_keys_con
	include dev8_keys_err
	include dev8_keys_sys
	include dev8_keys_qdos_pt
;
; set pointers within window for blob or sprite object
;
;	d0  r	column offset pointer sprite to object
;	d1  r	row offset pointer sprite to object
;	d2  r	column
;	d4   p
;	d3  r	row
;	d5  r	sprite width or -1 / sprite word counter
;	d6  r	row counter
;	d7  r	right shift (low word) / 16-shift (high word)
;
;	a0   p	pointer to channel definition block
;	a1   p
;	a2  r	address of pointer sprite on screen
;	a3 cr	linkage block address / new address of object
;	a5 c u	pointer to object definition / pointer to object pointer
;	a6  r	address increment of screen
;
pt_swind
	moveq	#-1,d5			default to "no alpha channel"
	btst	#pto..alph,pto_ctrl(a5) is there an alpha channel?
	beq.s	psw_noalpha
	move.w	pto_xsiz(a5),d5 	save width for alpha drawing
	swap	d5
psw_noalpha
	addq.w	#pto_xsiz,a5		ignore form etc
	move.w	(a5)+,d2		set width
	moveq	#pt.spxsw,d0
	move.w	#pt.rpxsw,d5		round up
	add.w	d2,d5			... number of sprite words
	lsr.w	d0,d5
	move.w	(a5)+,d3		and height
	move.w	d3,d6			... number of rows-1
	subq.w	#1,d6
;
	move.l	d1,d0			set position
	swap	d0
	sub.w	(a5)+,d0		offset by origin
	blt.s	err_or
	sub.w	(a5)+,d1
	blt.s	err_or
;
	add.w	d0,d2			check rhs
	cmp.w	sd_xsize(a0),d2
	bgt.s	err_or
;
	add.w	d1,d3			check bottom
	cmp.w	sd_ysize(a0),d3
	bgt.s	err_or
;
	add.w	sd_xmin(a0),d0		and offset to window origin
	add.w	sd_ymin(a0),d1
;
	bsr.s	set_ptr 		set the sprite pointers
	cmp.b	d0,d0			return OK
	rts
err_or
	moveq	#err.orng,d0
	rts
	page
;
; set pointers for pointer sprite
;
;	d0  r	column offset pointer sprite to object
;	d1  r	row offset pointer sprite to object
;	d2  r	column
;	d3  r	row
;	d4   s
;	d5  r	sprite width or -1 / sprite word counter (-1)
;	d6  r	row counter
;	d7   s
;
;	a0	0
;	a1  r	pointer to linkage block
;	a2  r	address of pointer sprite on screen
;	a3 cr	linkage block address / new address of object
;	a4  r	pointer to sprite pattern
;	a5 cr	pointer to sprite (pt_saddp), pointer to sprite mask
;	a6  r	address increment of screen
;
pt_sadd
	move.l	pt_psprt(a3),a5 	... definition
pt_saddp
	suba.l	a0,a0			signal "no CDB, put onto screen"
	move.l	a3,a1			put it into a save place
;
	moveq	#-1,d5			default to "no alpha channel"
	btst	#pto..alph,pto_ctrl(a5) is there an alpha channel?
	beq.s	psa_noalpha
	move.w	pto_xsiz(a5),d5 	save width for alpha drawing
	swap	d5
psa_noalpha
	addq.w	#pto_xsiz,a5		ignore form etc
	moveq	#pt.spxsw,d0
	move.w	#pt.rpxsw,d5		round up
	add.w	(a5)+,d5		... number of long words
	lsr.w	d0,d5
	move.w	(a5)+,d6		and height
	subq.w	#1,d6
;
; Simply limit sprite height if cursor is at lower edge of screen
	move.w	pt_yscrs(a3),d0 	y screen size
	sub.w	pt_ypos(a3),d0		space between pos and y limit
	add.w	2(a5),d0		also take origin into account
	subq.w	#1,d0
	cmp.w	d0,d6
	blt.s	psa_limitok
	move.w	d0,d6			limit height of sprite
psa_limitok
;
	movem.w pt_pos(a3),d0/d1	set position
	sub.w	(a5)+,d0		offset position by origin
	sub.w	(a5)+,d1
;
	move.l	a5,a4			set pointer to colour pattern
	add.l	(a5)+,a4
	move.l	a5,d7
	move.l	(a5),a5
	move.l	a5,d4
	beq.s	psa_nomask
	add.l	d7,a5			and sprite mask
psa_nomask
;
; If sprite is above upper edge of screen, limit height and adjust data ptrs
	tst.w	d1			sprite off top of screen?
	bpl.s	set_ptr 		... no
	neg.w	d1
	move.w	d1,d7			lines to cut off sprite
	mulu	d5,d1			long words to cut off
	lsl.l	#2,d1			same in bytes
	add.l	d1,a4			add to colour pattern

	swap	d5			sprite width (-1 if no alpha)
	tst.w	d5			alpha channel?
	bmi.s	psa_noalpha2		... no
	move.w	d7,d1			width * y offset
	mulu	d5,d1			bytes to skip in alpha channel
psa_noalpha2
	swap	d5
	add.l	d1,a5			add to mask
	sub.w	d7,d6			adjust height
	moveq	#0,d1			at top of screen
;
set_ptr
	cmp.b	#ptm.ql8,pt_dmode(a3)	is it 256 mode?
	bne.s	set_ptr1		... no
	bclr	#0,d0			... yes, alternate pixels only
set_ptr1
	move.l	pt_addr(a3),a2		set address of pointer on screen
	bra.l	sp_ptr

	end
