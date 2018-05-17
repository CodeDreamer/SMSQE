* Return from SMS2   V2.01    1986  Tony Tebby  QJUMP
*
	section sms
*
	xdef	sms_rte
	xdef	sms_rtok
	xdef	sms_rtdo
	xdef	sms_ikey
*
	xref	shd_schd
*
	include dev8_keys_err
	include dev8_keys_psf
	include dev8_keys_sys
*
* invalid SMS call key
*
sms_ikey
	moveq	#err.ipar,d0		invalid parameter
	bra.s	sms_rte
*
* good SMS return
*
sms_rtok
	moveq	#0,d0
sms_rte
	tst.b	sys_rshd(a6)		check for reschedule
	beq.s	sms_rtdo		... no
*
	btst	#psf..sup,psf_sr(sp)	call in supervisor mode?
	beq.l	shd_schd		... no, reschedule
sms_rtdo
	movem.l (sp)+,psf.reg		restore registers
	rte				and return
	end
