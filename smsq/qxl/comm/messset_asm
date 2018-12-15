; SMS (QXL) Message Setup		 1998 Tony Tebby
; 2006.10.20	1.01	BLAT macro definitions commented out - macro wasn't used (wl)
	section comm

	xdef	qxl_mess_set

	xref	qxl_scrcopy
	xref	spp_sendser

	include 'dev8_smsq_qxl_keys'
	include 'dev8_keys_qu'

* xref	 blatt
*blat macro blv
* move.b [blv],-(sp)
* jsr	 blatt
* add.w  #2,sp
* endm
*
* xref	 blattl
*blatl macro blv
* move.l [blv],-(sp)
* jsr	 blattl
* addq.l #4,sp
* endm

;+++
; This routine sets up the messages to send
;
;---
qxl_mess_set
;
; first set up the screen update messages
;
	jsr	qxl_scrcopy
;
; now set up the serial comms
;
	jsr	spp_sendser
;
; now build the general message buffer
;
	move.l	qxl_qxpc_mess,a4
	move.l	qxl_qxpc_eom(a4),a5	 ; message pointer
;
; add the flow control message if necessary
;
	move.l	qxl_message,a2
	lea	qxl_ms_flow+qxl_ms_len(a2),a2
	tst.w	(a2)			 ; length set?
	beq.s	qms_ms_queue
	clr.w	(a2)+
	move.l	(a2)+,(a5)+		 ; add flow control to buffer
	move.l	(a2)+,(a5)+
;
; take the messages out of the message queue
;
qms_ms_queue
	move.l	qxl_qxpc_qu,a2		 ; message queue
qms_loop
; blat #$41
	move.l	qu_nexto(a2),a3 	 ; next out
	cmp.l	qu_nexti(a2),a3 	 ; any thing there?
	beq.s	qms_done		 ; ... no
	move.l	(a3)+,a1		 ; pointer to next message
	move.l	a5,a0
	move.w	(a1),d0
	add.w	d0,a0
	cmp.l	qxl_qxpc_eob(a4),a0
	bhs.s	qms_done		 ; won't go
	cmp.l	qu_endq(a2),a3		 ; next off end?
	bne.s	qms_gone		 ; ... no
	lea	qu_strtq(a2),a3 	 ; ... yes, reset queue pointer
qms_gone
	move.l	a3,qu_nexto(a2) 	 ; set next out

	clr.w	(a1)+			 ; mark message taken
; blat (a1)
qms_take
	move.l	(a1)+,(a5)+		 ; take it
	subq.w	#4,d0
	bgt.s	qms_take		 ; for short messages, this is the best

	bra	qms_loop

qms_done
	clr.w	(a5)			 ; end of messages
	move.l	a5,qxl_qxpc_eom(a4)	 ; set message pointer
	rts

	end
