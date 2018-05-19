* ED - Entry point for BASIC program editor    1985  T.Tebby  QJUMP
*
	section ed
*
	xdef	ed
*
	xref	ed_keyin
	xref	ed_wrall
	xref	ed_snorm
	xref	ut_chan
	xref	ut_ckprc
	xref	ut_trp3r
	xref	gu_achp0
	xref	err_bp
*
	include dev8_sbsext_ed_keys
	include dev8_sbsext_ed_data
	include dev8_keys_con
*
* Permanent register usage
*
*	d5	row*8	(index into row table)
*	d6	row
*	d7	column
*	a0	edit window ID
*	a5	workspace address
*	a6	base of basic variables
*
ed
	bsr.l	ut_ckprc		check not in procedure
	bne.s	ed_rts1 		... oops
	moveq	#2,d6			assume BASIC channel 2
	bsr.l	ut_chan 		and look for # number
	bne.s	ed_rts1 		... oops
	move.w	d6,d5
*
	clr.b	bv_auto(a6)		no normal editing please
	clr.b	bv_arrow(a6)

	moveq	#0,d4			assume start at
	move.w	bv_edlin(a6),d4 	... next edit line
	move.w	ca..gtint,a2
	jsr	(a2)			get one integer
	bne.s	ed_rts1
	subq.w	#1,d3			just one?
	bgt.l	err_bp			... oops
	blt.s	ed_alchp		... not even one
	move.w	(a6,a1.l),d4		set start line
*
ed_alchp
	move.l	a0,a4			set channel
	move.l	bv_bfbas(a6),a1
	moveq	#sd.chenq,d0
	jsr	ut_trp3r
	bne.l	ed_rts			... not a window
	moveq	#ed_rtab/8+1,d0 	base of table + one spare row
	move.l	bv_bfbas(a6),a1
	add.w	2(a6,a1.l),d0
	lsl.l	#3,d0			base of table + 8 * number of rows
	jsr	gu_achp0		allocate working heap
	move.l	a0,a5			set work area pointer
ed_rts1
	bne.s  ed_rts		       ... no
*
* All set, now we can initialise everything
*
	move.w	d5,ed_chno(a5)		edit channel number

	sf	bv_cont(a6)		do not continue
	move.w	#4,bv_stopn(a6) 	... in fact stop

	moveq	#mt.inf,d0
	trap	#1
	cmpi.l	#'1.03',d2
	bls.s	need_offset
	movea.w pa..ini,a2
	adda.w	#qlv_jump,a2
	bra.s	got_vector
need_offset
	movea.w pa..graph,a2
	adda.w	#qlv_jump+pao_ini,a2
got_vector
*
	move.l	a2,ed_paini(a5) 	set address
*
	move.l	a4,a0			set our channel ID
	moveq	#sd.clear,d0		clear screen
	moveq	#-1,d3
	trap	#3

	st	ed_clear(a5)		but do not clear 0

	moveq	#sd.extop,d0		now set window size to character multipl
	move.l	a5,a1			and set up values in data structure
	lea	ed_setwn(pc),a2
	trap	#3
	tst.l	d0			ok
	bne.s	ed_exit

;	 trap #15
;	 move.l  a1,a0
;	 move.l  a5,a1
;	 bsr.s	 ed_setwn2
;	 move.l  a4,a0

	moveq	#0,d3			start at row 0 of line d4
	moveq	#0,d5			at top rhs
	moveq	#0,d6
	moveq	#0,d7
	bsr.l	ed_wrall		write all lines in window starting d4
*
	bsr.l	ed_keyin		get input keystroke
*
	moveq	#sd.extop,d0		restore the old window size
	move.l	a5,a1
	lea	ed_rstwn(pc),a2
	trap	#3
	moveq	#0,d0
*
*	move.w	edr_lnr(a5),d4		top row number
*	move.w	d4,bv_lsbas(a6) 	and set listing range
*	move.w	d4,bv_lsaft(a6)
*	subq.w	#1,d4
*	move.w	d4,bv_lsbef(a6)
*
ed_exit
	move.l	d0,d4			save the error return

	move.w	edr_lnr(a5),bv_edlin(a6) next line to edit
*
	moveq	#mt.rechp,d0		return the workspace
	move.l	a5,a0
	trap	#1
*
	move.l	d4,d0			set error return
ed_rts
	rts
*
	page
*
* EXTOP to set up window and related data structure
*
ed_setwn
;	 move.l  a0,a1
;	 moveq	 #0,d0
;	 rts
;ed_setwn2
	lea	ed_xsize(a1),a3 	save x and y sizes
	move.l	sd_xsize(a0),(a3)+
	move.l	sd_xinc(a0),(a3)+	and set x and y increments
*
	movem.w sd_xsize(a0),d0/d1	get x and y size
	movem.w sd_xinc(a0),d4/d5	and increments
	divu	d4,d0			set number of characters across
	move.w	d0,d2			
	divu	d5,d1			... and down
	move.w	d1,d3
*
	move.w	d0,(a3)+		set number of columns
	move.w	d1,(a3)+		number of rows
*
	subq.w	#1,d0			number of columns-1
	move.w	d0,(a3)+
	subq.w	#1,d1			number of rows-1
	move.w	d1,(a3)+
*
	subq.w	#ed.xoff-1,d0		set number of columns in extra rows
	move.w	d0,(a3)+
*
	subq.w	#5,d0			ensure at least 5 characters left in row
	blt.s	err_or
	subq.w	#1,d1			at least 2 rows
	ble.s	err_or
*
	mulu	d4,d2			set window width to multiple of xinc
	mulu	d5,d3			and window height to multiple of yinc
	movem.w d2/d3,sd_xsize(a0)
	bra.s	rts_ok
*
ed_rstwn
	move.l	sd_xsize(a0),d0 	set position to bottom rh corner
	sub.l	sd_xinc(a0),d0
	move.l	d0,sd_xpos(a0)
	move.l	ed_xsize(a1),sd_xsize(a0) reset size
	st	sd_nlsta(a0)		 ... and flag newline
rts_ok
	moveq	#0,d0
	rts
err_or
	moveq	#err.or,d0		set out of range
	rts
	end
