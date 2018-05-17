; SMSQ - Set Printer Aborted Message   V2.00    1994  Tony Tebby

	section iou

	xdef	iob_smsq
iob_smsq
	xdef	iob_ptab

	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_msg8'

;+++
; This routine sets the printer aborted message
;
;	a1  r	message
;--
iob_ptab
	move.w	#msg8.abrt,a1
	moveq	#sms.mptr,d0
	trap	#do.sms2
	rts
	end
