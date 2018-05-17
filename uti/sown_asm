; Set Owner of channel	  V2.00    1994  Tony Tebby  QJUMP

	section ioa

	xdef	ioa_sown

	include dev8_keys_err
	include dev8_keys_sys
	include dev8_keys_chn
	include dev8_keys_iod
	include dev8_keys_qdos_sms

;+++
;	d0  r	error return 0, or not found
;	d1 c  p new owner
;	a0 c s	channel id
;
;	all other registers preserved
;---
ioa_sown
reglist reg	d1-d3/d7/a0/a3/a6
	movem.l reglist,-(sp)
	moveq	#sms.info,d0
	trap	#do.smsq
	move.l	a0,a6
	move.l	(sp),d1
	move.l	16(sp),a0
	bsr.s	io_ckchn		 ; check the channel ID (a0,a3)
	bne.s	iso_exit
	move.l	d1,chn_ownr(a0)
	moveq	#0,d0

iso_exit
	movem.l (sp)+,reglist
	rts

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
