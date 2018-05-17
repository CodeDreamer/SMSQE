* Standard Shrink Common Heap  V2.00    1994  Tony Tebby  QJUMP
*
	section sms
*
	xdef	sms_schp
*
	xref	mem_rchp		return to heap
	xref	sms_rtok		return
*
	include dev8_keys_chp
*
*	d0	0 no errors detected
*	d1 cr	space required, space retained
*	a0 c  p base address of area to return
*
*	all other registers preserved
*
sms_schp   ;moveq	 #0,d0
;	 bra.l	 sms_rtok
	move.l	a0,-(sp)		save base
	sub.w	#chp.len,a0		backspace to header
	moveq	#chp.len*2-1,d0
	add.l	d0,d1
	moveq	#-chp.len,d0

	and.l	d0,d1			minimum space required
	move.l	chp_len(a0),d0		old length
	sub.l	d1,d0			amount to release
	ble.s	ssc_ok

	move.l	d1,chp_len(a0)		new length
	add.l	d1,a0			new free
	move.l	d0,chp_len(a0)		and its length
	clr.l	chp_flag(a0)
	bsr.l	mem_rchp

ssc_ok
	move.l	(sp)+,a0
	moveq	#-chp.len,d1
	add.l	chp_len-chp.len(a0),d1
	bra.l	sms_rtok
	end
