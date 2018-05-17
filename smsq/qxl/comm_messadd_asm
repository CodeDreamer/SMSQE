; QXL Add Message to General Message Queue     1998 Tony Tebby

	section comm

	xdef	qxl_mess_add
	xdef	qxl_mess_addi

	include 'dev8_smsq_qxl_keys'
	include 'dev8_keys_qu'

;+++
; This routine adds a message to the general message queue, it always waits
;
;	a1 c  p pointer to (length of) message
;
;	status return 0
;---
qxl_mess_add
	movem.l a2/a3,-(sp)
	move.l	qxl_qxpc_qu,a2

	move.l	qu_nexti(a2),a3 	 ; next in
	move.l	a1,(a3)+		 ; set pointer
	cmp.l	qu_endq(a2),a3		 ; next off end?
	bne.s	qma_seti		 ; ... no
	lea	qu_strtq(a2),a3 	 ; ... yes, reset queue pointer
qma_wait
	cmp.l	qu_nexto(a2),a3 	 ; is there room?
	beq.s	qma_wait		 ; ... no, wait until there is
qma_seti
	move.l	a3,qu_nexti(a2) 	 ; set next in
	moveq	#0,d0			 ; ok
qma_exit
	movem.l (sp)+,a2/a3
	rts

;+++
; This routine is called within the interrupt server to add a message to
; the general message queue.
; It should be called BEFORE the message is set up and if it returns a negative
; status, the message should not be set up this time around.
;
;	a1 c  p pointer to (length of) message
;
;	status return -ve if there is not enough room
;---
qxl_mess_addi
	movem.l a2/a3,-(sp)
	move.l	qxl_qxpc_qu,a2

	move.l	qu_nexti(a2),a3 	 ; next in
	move.l	a1,(a3)+		 ; set pointer
	cmp.l	qu_endq(a2),a3		 ; next off end?
	bne.s	qma_seti		 ; ... no
	lea	qu_strtq(a2),a3 	 ; ... yes, reset queue pointer
	cmp.l	qu_nexto(a2),a3 	 ; is there room?
	bne.s	qma_seti		 ; ... yes
	moveq	#-1,d0			 ; ok
	bra.s	qma_exit

	end
