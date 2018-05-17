* Draw window contents	V1.03			 1986	Tony Tebby   QJUMP
*						  2002	Marcel Kilgus
*
* 2002-13-11  1.02  Adapted for high colour use (MK)
* 2002-21-12  1.03  Added support for non standard border sizes (MK)
*
	section wman
*
	xdef	wm_wdraw
	xdef	wm_trap3
	xdef	wm_ssubw
*
	xref	wm_entry
	xref	wm_ldraw
	xref	wm_drinf
	xref	wm_bordersize
	xref	wmc_trap3
*
	include dev8_keys_qdos_io
	include dev8_keys_wwork
	include dev8_keys_wstatus
*
*	d0  r	error return
*	d7   sp window origin
*	a0  r	channel ID of window
*	a1   sp pointer to window status area
*	a2   sp pointer to sub-window lists
*	a3   sp pointer to objects
*	a4 c  p pointer to working definition
*
*		all other registers preserved
*
reglist  reg	d1-d7/a1/a2/a3/a4
stk_stat equ	$14 
*
wm_wdraw
	movem.l reglist,-(sp)		save registers
	moveq	#-1,d0
	move.l	ww_wstat(a4),a1 	pointer to status area
	move.w	d0,ws_citem(a1) 	no current item
	move.l	d0,ws_ptpos(a1) 	relating to silly pointer position
*
	move.l	ww_chid(a4),a0		get channel ID
	move.l	ww_xorg(a4),d7		and window origin
	moveq	#0,d3			draw all...
	jsr	wm_drinf(pc)		...information windows
	bne.s	wdr_exit		... oops
	moveq	#0,d3			force redraw of all items
	move.l	ww_wstat(a4),a1 	we need status area
	bsr.l	wm_ldraw		draw loose menu items
	bne.s	wdr_exit		... oops
	bsr.s	wdr_appl		draw application windows
wdr_exit
	movem.l (sp)+,reglist
	rts
	page
*
* draw application sub windows
*
wdr_appl
	move.l	ww_pappl(a4),d0 	pointer to application window list
	beq.s	wdra_rts		... none
	move.l	d0,a2
wdr_aloop
	move.l	(a2),a3 		pointer to application window
	move.l	a3,d0			end?
	beq.s	wdra_rts
	bsr.s	wdr_subw		initialise sub-window
	bne.s	wdra_rts
	move.l	(a2)+,a3		reset pointer to sub-window definition
	move.l	wwa_draw(a3),a1 	get draw pointer
	move.l	a1,d0			any?
	beq.s	wdr_aloop		... no
*
	move.l	a2,-(sp)		save list pointer
	lea	wm_entry(pc),a2 	set window manager vector
	jsr	(a1)			a0 channel ID, a4 working def,
*					a3 sub-window definition, d7 window orig
	move.l	(sp)+,a2		restore list pointer
	beq.s	wdr_aloop		... ok
wdra_rts
	rts
*
* initialise sub-window
*
wm_ssubw
wdr_subw
	move.l	ww_wstat(a4),a1 	pointer to status area
	lea	ws_subdf(a1),a1 	set sub-window definition
	move.l	(a3)+,(a1)+		size
	move.l	(a3)+,(a1)		and origin
	add.l	d7,(a1)+		... absolute
	addq.l	#wwa_borw+wwi_watt-8,a3 set attributes
	move.w	(a3),d2 		border width
	move.w	2(a3),d1		border colour
	jsr	wm_bordersize
	sub.l	d1,-(a1)		... adjust window
	add.l	d2,-(a1)

	movem.l d3/a1,-(sp)
	move.w	4(a3),d1		set paper before border!
	moveq	#iow.spap,d0
	moveq	#-1,d3
	jsr	wmc_trap3
	movem.l (sp)+,d3/a1

	move.w	(a3)+,d2		again border width
	move.w	(a3)+,d1		and colour
	moveq	#iow.defw,d0
	bsr.s	wdr_trp3

	move.w	(a3)+,d1		set strip
	moveq	#iow.sstr,d0
	bsr.s	wdr_trp3
	tst.b	wwa_clfg-wwa_papr-2(a3)  ; clear?
	bmi.s	wdrs_ok 		 ; ... no
	moveq	#iow.clra,d0		 ; ... yes, clear sub-window
	bsr.s	wdr_trp3
wdrs_ok
	moveq	#0,d0
	rts				 ; ... note special rts from wdr_trp3
	page
*
* internal trap #3
*
wm_trap3
wdr_trp3
	move.l	d3,-(sp)
	moveq	#-1,d3			no timeout
	jsr	wmc_trap3
	move.l	(sp)+,d3
	tst.l	d0			error
	beq.s	wdrt_rts 
	addq.l	#4,sp			remove return
wdrt_rts
	rts
	end
