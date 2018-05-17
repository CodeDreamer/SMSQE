* Allocate in Resident Procedure Area!!!  V2.01    1986  Tony Tebby  QJUMP
*
	section sms
*
	xdef	sms_arpa
*
	xref	mem_atpa		allocate in tpa
	xref	sms_rte 		return
*
	include dev8_keys_chp
	include dev8_keys_sys
*
*	d0  r	out of memory
*	d1 cr	space required / space allocated
*	a0  r	space allocated
*
*	all other registers preserved
*
sms_arpa
	move.l	sp,sys_psf(a6)		set stack frame (for move superBASIC)
	moveq	#chp.len,d7
	add.l	d7,d1			allocate extra for header
	bsr.l	mem_atpa
	bne.s	sar_exit		... oops
	clr.l	chp_ownr(a0)		set owner
	sub.l	d7,d1			set actual space available
	add.l	d7,a0			and point to it
	moveq	#0,d0			ok
sar_exit
	bra.l	sms_rte
	end
