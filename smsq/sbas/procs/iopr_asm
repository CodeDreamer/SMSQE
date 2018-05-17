; SBAS_PROCS_IOPR - SBASIC Set IO Priority V2.00    1994  Tony Tebby

	section exten

	xdef	io_priority

	xref	ut_gxin1

	include 'dev8_keys_qdos_sms'

;+++
; IO_PRIORITY n
;---
io_priority
	jsr	ut_gxin1
	move.w	(a6,a1.l),d2
	moveq	#sms.iopr,d0		 ; set io priority
	trap	#do.smsq
	rts
	end
