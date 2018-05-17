; SMS (QPC) Send serial data

	section comm

	xdef	spp_sendser

	xref	iob_gbps

	include 'dev8_smsq_qpc_keys'
	include 'dev8_keys_serparprt'
	include 'dev8_keys_k'
	include 'dev8_mac_assert'

;+++
; This routine sets up messages to send serial data to the PC
;
;	d1 c  s port number
;
;---
spp_sendser
	move.l	qpc_spp_link,d0 	 ; spp linkage
	beq.s	qss_rts

	dc.w	qpc.soque+2		 ; get queue address in d2
	move.l	d0,a3
	move.w	d1,d4			 ; port number
	subq.w	#1,d1
	move.l	spd_pser(a3),a3 	 ; serial ports
	mulu	#spd.len,d1		 ; offset
	add.l	d1,a3			 ; this block

	tst.b	spd_oact(a3)		 ; output active?
	beq.s	qss_rts

	move.l	spd_obuf(a3),d0 	 ; output buffer
	ble.s	qss_nomore		 ; no data to go
	move.l	d0,a2
	move.b	spb_prty(a2),d7 	 ; parity

qss_bloop
	dc.w	qpc.fulq+2		 ; PC-queue full?
	beq.s	qss_squeue		 ; ... try to send it

	jsr	iob_gbps		 ; get byte
	bgt.s	qss_eof 		 ; ... eof, close port
	blt.s	qss_nomore		 ; ... nothing, send the message

	dc.w	qpc.putq+1		 ; put byte in send queue
	bra.s	qss_bloop

qss_nomore
	sf	spd_oact(a3)		 ; deactivate output
qss_squeue
	dc.w	qpc.ssend+4		 ; Send queue now
qss_rts
	rts

qss_eof
	move.l	a2,d0
	bne.s	qss_bloop
	subq.b	#1,d1
	blt.s	qss_close		 ; ... no eof flag, just close port
	bgt.s	qss_cz			 ; ... send CTRL Z
	moveq	#k.ff,d1
	bra.s	qss_closeput
qss_cz
	moveq	#26,d1			 ; send CTRL Z
qss_closeput
	dc.w	qpc.putq+1		 ; stuff eof flag in queue
qss_close
	sf	spd_oact(a3)		 ; deactivate output
	dc.w	qpc.ssend+4		 ; Send queue (if not empty)
	ori.w	#$100,d4		 ; Close output port
	dc.w	qpc.sclse+4		 ; Close port
	rts

	end
