* Set default or given channel V0.4   PUBLIC DOMAIN by Tony Tebby  QJUMP
*
* call parameters   : a3 and a5 standard pointers to name table for parameters
*		    : d6 channel number
* return parameters : d4 pointer to channel table
*		    : d6 channel number
*		      a0 channel id
*
	section utils
*
	xdef	ut_chan 	default d6
	xdef	ut_chan0	default #0
	xdef	ut_chan1	default #1
	xdef	ut_chget	#n must be present
	xdef	ut_chlook	actually d6
	xdef	ut_chd4
*
	xref	ut_chanp
	xref	ut_gtin1	get one integer
	xref	err_bp
*
	include dev8_sbsext_ext_keys
*
ut_chget
	moveq	#-1,d6		no default
	bra.s	ut_chan
ut_chan0
	moveq	#0,d6		default is channel #0
	bra.s	ut_chan
ut_chan1
	moveq	#1,d6		default is channel #1
ut_chan
	cmpa.l	a3,a5		are there any parameters?
	ble.s	ut_chlook	... no
	btst	#7,1(a6,a3.l)	has the first parameter a hash?
	beq.s	ut_chlook	... no
*
	move.l	a1,-(sp)
	bsr.l	ut_gtin1	get one integer
	bne.s	ut_chexit	was it ok?
	addq.l	#8,a3		and move past it
	moveq	#-1,d6		set no default
	move.w	0(a6,a1.l),d6	get value in d6 to replace the default
	addq.l	#2,bv_rip(a6)	reset ri stack pointer
	move.l	(sp)+,a1
*
ut_chlook
	move.w	d6,d4		get channel number
	blt.l	err_bp		... oops
ut_chd4
	move.l	a1,-(sp)
	mulu	#$28,d4 	make d4 (long) pointer to channel table
	add.l	bv_chbas(a6),d4 
	cmp.l	bv_chp(a6),d4	is it within the table?
	bge.s	ut_chdef	... no
	move.l	0(a6,d4.l),a0	set channel id
	move.w	a0,d0		is it open?
	bpl.s	ut_chok 	... yes

ut_chdef
	tst.l	d6		defaulted?
	bmi.s	ut_chno 	... no
	moveq	#0,d6		channel 0
	move.l	bv_chbas(a6),d4
	cmp.l	bv_chp(a6),d4	any channel 0
	bge.s	ut_chopen	... open it
	move.l	0(a6,d4.l),a0
	move.w	a0,d0		... open?
	bpl.s	ut_chok
ut_chopen
	jsr	ut_chanp	preset channel
	lea	con_def,a1
	move.l	a3,-(sp)
	jsr	ut..con*3+qlv.off	and open
	move.l	(sp)+,a3
	bne.s	ut_chexit
	move.l	a0,0(a6,d4.l)
	bra.s	ut_chok

ut_chno
	moveq	#err.no,d0	channel not open
	bra.s	ut_chexit

con_def dc.b	$ff,$1,$0,$7
	dc.w	$100,$3e,$80,$60

ut_chok
	moveq	#0,d0		no error
ut_chexit
	move.l	(sp)+,a1
	rts

	end
