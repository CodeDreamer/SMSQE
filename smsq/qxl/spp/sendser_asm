; SMS (QXL) Send serial data

	section comm

	xdef	spp_sendser

	xref	qxl_mess_add
	xref	iob_gbyt

	include 'dev8_smsq_qxl_keys'
	include 'dev8_smsq_qxl_comm_keys'
	include 'dev8_keys_serparprt'
	include 'dev8_keys_k'
	include 'dev8_mac_assert'

;+++
; This routine sets up messages to send serial data to the PC
;
;	d0/d1/d2/d3/d4/d5/d6/d7/a0/a1/a2/a3/a4/a5 smashed
;
;---
spp_sendser
	move.l	qxl_spp_link,d0 	 ; do all ports
	beq.s	qss_rts
	move.l	d0,a3

	move.l	spd_qxlflow(a3),d5	 ; flow control
	swap	d5			 ; par in lsb
	move.w	spd_nser(a3),d6
	move.w	spd_npar(a3),d7
	beq.s	qss_ser 		 ; no par

qss_loop
	lsr.w	#1,d5			 ; PC ready?
	bcc.s	qss_next

	tst.b	spd_oact(a3)		 ; output active?
	beq.s	qss_next

	move.l	spd_hbase(a3),a1	 ; message
	tst.l	(a1)			 ; message available?
	bne.s	qss_next		 ; ... no, try again next time
	addq.l	#qxm_txdata+qxm_datams,a1 ; data in message

	moveq	#0,d4			 ; byte count
	move.l	spd_obuf(a3),d0 	 ; output buffer
	beq.s	qss_more		 ; no data to go - set oact to d4 = 0
	move.l	d0,a2

qss_bloop
;;;; dragon - very inefficient
	jsr	iob_gbyt		 ; get byte
	beq.s	qss_abyt		 ; ... something
	blt.s	qss_smess		 ; ... nothing, send the message

	subq.b	#1,d1			 ; is ff required?
	blt.s	qss_smess		 ; ... no, send this message
	bgt.s	qss_cz			 ; ... but really it is CTRL Z
	moveq	#k.ff,d1
	bra.s	qss_abyt		 ; send ff
qss_cz
	moveq	#26,d1			 ; send CTRL Z

qss_abyt
	move.b	d1,(a1)+		 ; put byte in message
	addq.w	#1,d4
	cmp.w	#qxm.mxdata,d4		 ; more room?
	blt.s	qss_bloop		 ; ... yes

qss_smess
	sub.l	d4,a1
	assert	0,qxm_ndata-2,qxm_txdata-4
	move.w	d4,-(a1)		 ; number of bytes to send
	beq.s	qss_more		 ; none!!

	subq.w	#qxm_ndata,a1
	addq.w	#qxm_txdata+3,d4
	and.w	#$fffc,d4
	move.w	d4,-(a1)
	jsr	qxl_mess_add

	cmp.w	#qxm.mxdata+qxm_txdata,d4 ; full message
	seq	d4			 ; if yes, keep output active

qss_more
	move.b	d4,spd_oact(a3) 	 ; set if more data in output buffer

qss_next
	add.w	#spd.len,a3
	subq.w	#1,d7			 ; next port
	bgt.s	qss_loop		 ; ... there is one


qss_ser
	move.w	d6,d7			 ; ser ports?
	beq.s	qss_rts
	moveq	#0,d6			 ; no more!!
	swap	d5			 ; ser flow control
	bra.s	qss_loop

qss_rts
	rts
	end
