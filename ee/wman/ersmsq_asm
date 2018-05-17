; Window manager point to error string	SMSQ special

	section wman

	xdef	wm_ersmsq
wm_ersmsq
	xdef	wm_erstr

	include 'dev8_keys_qdos_sms'
;+++
; Convert error in d0 to string
;
;	d0 cr	error code
;	a1  r	pointer to error string
;	status return according to D0
;+++
wm_erstr
	move.l	d0,-(sp)
	move.l	d0,a1
	moveq	#sms.mptr,d0		 ; find message
	trap	#do.sms2
	move.l	(sp)+,d0
	rts
	end
