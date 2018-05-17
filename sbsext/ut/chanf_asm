* Find entry for a BASIC channel V0.4	 1984 Tony Tebby   QJUMP
*
	section utils
*
	xdef	ut_chanv		find vacant hole
	xdef	ut_chanf		find or create #n
	xdef	ut_chanp		preset channel entry at D4
*
	xref	ut_chget		get channel
*
	include dev8_sbsext_ext_keys
*
ut_chanv
	moveq	#ch.lench,d3
	moveq	#3,d6			start at channel #3
	moveq	#3*ch.lench,d4
	add.l	bv_chbas(a6),d4 	start address in table
utcv_loop
	cmp.l	bv_chp(a6),d4		is pointer off end of table?
	bge.s	utc_extn		... yes, extend the table
	tst.w	ch.id+2(a6,d4.l)	check if vacant
	blt.s	utc_pres		... yes, preset it
	addq.w	#1,d6			next entry
	add.l	d3,d4
	bra.s	utcv_loop
*
ut_chanf
	bsr.l	ut_chget		get channel
	beq.s	utc_close		... its open!!
	moveq	#err.no,d1		not open?
	cmp.l	d1,d0
	bne.s	utc_rts 		... no

ut_chanp
utc_extn
	move.l	d4,d1			is channel within table?
	sub.l	bv_chp(a6),d1
	blt.s	utc_pres		... yes, preset entry
*
	sub.l	bv_chbas(a6),d4
	moveq	#ch.lench,d3		space for one extra channel
	add.l	d3,d1			... no, allocate enough space 
	move.l	bv..chrix*3+qlv.off+2,a2
	jsr	bvo_chch(a2)		in channel area
	add.l	bv_chbas(a6),d4
*
	move.l	bv_chp(a6),a2		now fill the id in each entry
	moveq	#-1,d0			with -1
utc_sid  
	move.l	d0,ch.id(a6,a2.l)	put it in
	add.w	#ch.lench,a2		move to next entry
	cmp.l	d4,a2			off end yet?
	ble.s	utc_sid
	move.l	a2,bv_chp(a6)		save new top pointer
	bra.s	utc_pres
*
utc_close
	moveq	#io.close,d0		close channel
	trap	#2
*
utc_pres
	moveq	#ch.lench,d3		preset all entrys
	add.l	d3,d4			move to end
utc_cldef
	subq.l	#4,d4
	clr.l	(a6,d4.l)		to zero
	subq.l	#4,d3
	bgt.s	utc_cldef
*
	not.l	ch.id(a6,d4.l)	except channel ID
	move.w	#80,ch.width(a6,d4.l) and default width
	moveq	#0,d0
utc_rts
	rts
	end
