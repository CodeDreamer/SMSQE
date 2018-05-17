* Standard mode routine      V2.01    1994  Tony Tebby  QJUMP
*
	section qd
*
	xdef	qd_dmode

	xref	sms_rtok

	include 'dev8_smsq_smsq_base_keys'

qd_dmode
	move.l	sms.dmode,a5	       ; vectored dmode routine
	pea	sms_rtok
	jmp	(a5)

	end
