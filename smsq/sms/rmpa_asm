* Return to Moveable Program Area  V2.00    1986  Tony Tebby  QJUMP
*
	section sms
*
	xdef	sms_rmpa
*
	xref	mem_ctpa		contract tpa
	xref	sms_rtok		return
*
	include dev8_keys_sys
	include dev8_keys_sbt
*
*	d0  r	0, no errors
*	d1 cr	space to be returned/ space returned
*
*	all other registers preserved
*
sms_rmpa
	move.l	sp,sys_psf(a6)		set stack frame (for move superBASIC)
	and.l	#-sbt.size,d1		round down return
	beq.s	srm_exok		... none
	sub.l	d1,sys_tpab(a6) 	move tpa base down
	bsr.l	mem_ctpa		contract tpa
srm_exok
	bra.l	sms_rtok
	end
