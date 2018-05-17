* Link items into system lists	V2.00	 1986	Tony Tebby  QJUMP
*
	section sms
*
	xdef	sms_lexi
	xdef	sms_lpol
	xdef	sms_lshd
	xdef	sms_liod
	xdef	sms_lfsd
*
	xref	mem_llst
	xref	sms_rtok
*
	include dev8_keys_sys
	include dev8_keys_qdos_sms
*
*	d0  r	return OK
*	a0 c  p address of link
*
*	all other registers preserved
*
sms_lexi
	moveq	#sys_exil,d0
	bra.s	sms_link
sms_lpol
	moveq	#sys_poll,d0
	bra.s	sms_link
sms_lshd
	moveq	#sys_shdl,d0
	bra.s	sms_link
sms_liod
	moveq	#sys_iodl,d0
	bra.s	sms_link
sms_lfsd
	moveq	#sys_fsdl,d0
*
sms_link
	move.l	a1,d7			save a1
	lea	(a6,d0.l),a1
	bsr.l	mem_llst		link in
	move.l	d7,a1
	bra.l	sms_rtok
	end
