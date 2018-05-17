; SMS (QXL) Set up comms queue	    1998 Tony Tebby

	section comm

	xdef	qxl_queue_set

	include 'dev8_keys_qu'

;+++
; This routine sets up queue
;
;	a0 c  u base of queue header, base of queue
;	a1   s
;	a2 c  p top of queue
;
;	no error return, condition codes arbitrary
;---
qxl_queue_set
	clr.l	(a0)+			 ; no link or eoff
	move.l	a2,(a0)+		 ; set end of queue
	lea	qu_strtq-8(a0),a1	 ; ... back to start
	move.l	a1,(a0)+		 ; set in pointer
	move.l	a1,(a0)+		 ; and out pointer
	rts
	end
