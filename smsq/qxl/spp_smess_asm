; QXL Send ser/par port control message   2000       Tony Tebby

	section spp

	xdef	spp_smess

	xref	qxl_mess_add

	include 'dev8_smsq_qxl_keys'
	include 'dev8_smsq_qxl_comm_keys'

;+++
; Send control message (in d0)
;
;	d0 cr	messsage / 0
;
;---
spp_smess
	move.l	a1,-(sp)
	move.l	qxl_message,a1
	lea	qxl_ms_port+qxl_ms_len(a1),a1
sps_wait
	tst.w	(a1)			 ; message available?
	bne.s	sps_wait		 ; ... no

	move.w	#4,(a1) 		 ; message length
	move.l	d0,2(a1)		 ; message
	jsr	qxl_mess_add
	move.l	(sp)+,a1
	moveq	#0,d0
	rts

	end
