* Set address of file system defaults	V0.1    Tony Tebby   QJUMP
*
	section utils
*
	xdef	ut_fdef
*
	include 'dev8_keys_sys'
	include 'dev8_keys_qdos_sms'
*
*	d2 c p	(offset) pointer to default pointer
*	a4   r	pointer to default string
*
ut_fdef
	moveq	#sms.xtop,d0
	trap	#1
	move.l	sys.defo(a6,d2.w),a4
	moveq	#0,d0
	rts
	end
