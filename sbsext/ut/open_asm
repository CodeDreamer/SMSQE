* Open utilities  V0.8	   1984/1985  Tony Tebby   QJUMP
*
	section utils
*
	xdef	ut_opend	open with optional data default
	xdef	ut_opmyd	ditto for my job
	xdef	ut_open2	open and try again with default
	xdef	ut_opdef	open with default at start of (a1)
	xdef	ut_opena	open (a0 absolute)
	xdef	ut_open 	open (a1 relative)
*
	xref	ut_fdef
*
	include dev8_sbsext_ext_keys

buff_off equ	$40		offset in SBASIC buffer for default processing
*
ut_opmyd
	moveq	#myself,d1
ut_opend
	moveq	#sv_datad-sv_fdefo,d2  open and try again with data default
ut_open2
	tst.w	(a6,a1.l)	is there a file at all?
	ble.s	ut_opdef	... no, just open with default
	move.w	d2,-(sp)	save default pointer
	bsr.s	ut_open 	try to open
	move.w	(sp)+,d2
	cmp.w	#err.nf,d0	was it not found?
	beq.s	ut_opdef	... yes, try again with default
	cmp.w	#err.bn,d0	was it bad name?
	bne.s	ut_opn_rts	... no, done
ut_opdef  
	move.l	a4,-(sp)	save a4
	bsr.s	ut_fdef 	find default (a4)
	move.l	bv_bfbas(a6),a0 stick the name (with default) into bf
	add.w	#buff_off+2,a0	leave room for the count
	move.w	(a4)+,d2	get character count of default
	move.w	d2,d0		and total
	bra.s	ut_opd_e
ut_opd_l
	move.b	(a4)+,(a6,a0.l) copy default a byte at a time
	addq.l	#1,a0
ut_opd_e
	dbra	d2,ut_opd_l
	move.l	(sp)+,a4	restore a4
*
	move.w	(a6,a1.l),d2	get character count of name
	ble.s	ut_opn_p
	add.w	d2,d0		and total
	cmp.w	#44,d0		is total>max file name?
	bhi.s	ut_opn_nf	yes, exit straight out with not_found
	addq.l	#2,a1		skip character count
	bra.s	ut_opn_e
ut_opn_l
	move.b	(a6,a1.l),(a6,a0.l) copy one byte at a time
	addq.l	#1,a0
	addq.l	#1,a1
ut_opn_e
	dbra	d2,ut_opn_l
ut_opn_p
	move.l	bv_bfbas(a6),a1 put total count in the start
	add.w	#buff_off,a1
	move.w	d0,(a6,a1.l)
*
ut_open
	move.l	a1,a0		set the pointer to the filename
ut_openr
	trap	#4		a0 is relative
ut_opena
	move.l	a1,-(sp)
	moveq	#io.open,d0	open file
	tst.w	d3		really?
	bge.s	trap2		... yes
	moveq	#io.delet,d0	... no, delete
trap2
	trap	#2
	move.l	(sp)+,a1	restore pointer to the filename
ut_opn_rts
	tst.l	d0		and test the error return
	rts
*
ut_opn_nf
	moveq	#err.nf,d0	name too long to take default
	rts
	end
