* Allocate in Moveable Program Area  V2.00    1986  Tony Tebby  QJUMP
*
	section sms
*
	xdef	sms_ampa
*
	xref	mem_xtpa		extend tpa copying SuperBASIC
	xref	sms_rte 		return
*
	include dev8_keys_sys
*
*	d0  r	0 or out of memory
*	d1 cr	space required / space allocated
*	a0   s
*
*	all other registers preserved
*
sms_ampa
	move.l	sp,sys_psf(a6)		set stack frame (for move superBASIC)
	bsr.l	mem_xtpa
	bne.s	sam_exit		... oops
	add.l	d1,sys_tpab(a6) 	restore TPA pointer
	moveq	#0,d0			set cc
sam_exit
	bra.l	sms_rte
	end
