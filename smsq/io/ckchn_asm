* Check channel ID   V2.00    1986  Tony Tebby  QJUMP
*
	section io
*
	xdef	io_ckchn
*
	include dev8_keys_err
	include dev8_keys_sys
	include dev8_keys_chn
	include dev8_keys_iod
*
*	d0    p if channel id ok
*	d0  r	... otherwise error not open
*	d7   s	pointer to channel table
*	a0 cr	channel id / channel address
*	a3  r	linkage block address
*
io_ckchn
	move.l	a0,d7			put channel id safe
	cmp.w	sys_chtp(a6),d7 	off top of table?
	bhi.s	icc_ichn		... yes
	lsl.w	#2,d7			index table
	move.l	sys_chtb(a6),a3 	base of table
	add.w	d7,a3
	tst.b	(a3)			open?
	bmi.s	icc_ichn
	move.l	(a3),a0 		address of channel
	swap	d7			check tag
	cmp.w	chn_tag(a0),d7
	bne.s	icc_ichn
*
	move.l	chn_drvr(a0),a3 	driver linkage
	lea	-iod_iolk(a3),a3	base of linkage block
	rts
*
icc_ichn
	moveq	#err.ichn,d0		invalid channel id
	rts
	end
