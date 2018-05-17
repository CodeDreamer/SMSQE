* Find free space in common heap / TPA	V3.10	 1986	Tony Tebby  QJUMP
*						  2002	Marcel Kilgus
*
* 2002-05-20  3.10  Previously just returned the size of the common heap gap,
*		    now scans the free block list of chp and tpa for the
*		    largest one (MK)
*
	section mem
*
	xdef	mem_frch
	xdef	mem_frtp
*
	xref	sms_rte
*
	include dev8_keys_sys
	include dev8_keys_sbt
	include dev8_keys_chp
*
*	d0  rs	0, no errors
*	d1  r	free space, limited to 64K default
*	a6 c  p pointer to system vars
*
*	all other registers preserved
*
*************************************************
*	NOTE: returns directly through SMS return
*************************************************
*
mem_frtp
mem_frch
	move.l	sys_sbab(a6),d1 	 ; first calculate gap
	sub.l	sys_fsbb(a6),d1
	sub.l	#$400,d1		 ; two slave blocks spare
	bgt.s	mfr_scanlists
	moveq	#0,d1			 ; no room in gap
mfr_scanlists
	lea	sys_chpf-chp_nxfr(a6),a2 ; scan free space lists
	bsr.s	mfr_scan
	lea	sys_tpaf-chp_nxfr(a6),a2
	bsr.s	mfr_scan

	cmp.l	sys_mxfr(a6),d1 	 ; more than max?
	ble.s	mfr_ok			 ; ... no
	move.l	sys_mxfr(a6),d1
mfr_ok
	moveq	#0,d0
	bra.l	sms_rte

mfr_scan
	move.l	chp_nxfr(a2),d0 	 ; next
	beq.s	mfr_scanend
	add.l	d0,a2
	move.l	chp_len(a2),d0		 ; length
	cmp.l	d0,d1
	bgt.s	mfr_scan
	move.l	d0,d1
	bra.s	mfr_scan

mfr_scanend
	rts

	end
