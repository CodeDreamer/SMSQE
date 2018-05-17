; Close PIPE Channel   V2.01	 1991	Tony Tebby

	section pipe

	xdef	pipe_close
	xdef	pipe_remove

	xref	gu_rchp
	xref	iou_rchb

	include 'dev8_keys_qlv'
	include 'dev8_keys_qu'
	include 'dev8_keys_chp'
	include 'dev8_iod_pipe_data'
	include 'dev8_mac_assert'
;+++
; PIPE channel close operations.
;---
pipe_close
	move.l	pic_qin(a0),d0		 ; input queue?
	bne.s	pipc_in 		 ; ... yes
	move.l	pic_qout(a0),d0 	 ; pipe itself
	beq.s	pipc_rchp		 ; non queue at all (directory)
	move.l	d0,a2			 ; the output queue
	subq.w	#1,pin_nout-pin_qu(a2)	 ; named pipe?
	bgt.s	pipc_rchp		 ; ... yes, there are other users
	beq.s	pipc_lout		 ; ... yes, last output
	tst.l	(a2)			 ; any input channel?
	beq.s	pipc_rchp		 ; ... no, return to heap
	tas	(a2)			 ; ... yes, set end of file
	move.l	chp_flag(a0),d0 	 ; flag to be set
	beq.s	pipc_ok 		 ; strange!!
	move.l	d0,a2
	st	(a2)			 ; set
	clr.l	chp_flag(a0)		 ; ... no flag now
	bra.s	pipc_ok 		 ; and leave pipe lying around

pipc_in
	move.l	d0,a2			 ; point to queue
	bsr.s	pipc_rchp		 ; when returning our channel
	subq.w	#1,pin_nin-pin_qu(a2)	 ; named pipe?
	bgt.s	pipc_ok 		 ; ... yes, there are other users
	beq.s	pipc_lin		 ; ... yes, last input
	tst.b	(a2)			 ; is other channel closed?
	blt.s	pipc_ra2		 ; ... yes, return it
	clr.l	(a2)			 ; ... no, mark us gone
	bra.s	pipc_ok

pipc_ra2
	lea	-pic_qu(a2),a0		 ; point to channel
pipc_rchp
	jsr	iou_rchb		 ; return channel block
pipc_ok
	moveq	#0,d0
pipc_exit
	rts

pipc_lout
	bsr.s	pipc_rchp		 ; last output, return channel
	tas	(a2)			 ; and set end of file

pipc_lin
	assert	pin_nin+2,pin_nout
	tst.l	pin_nin-pin_qu(a2)	 ; any users at all?
	bne.s	pipc_ok 		 ; ... yes
	move.l	qu_nexti(a2),d0
	sub.l	qu_nexto(a2),d0 	 ; empty queue?
	bne.s	pipc_ok 		 ; ... no

	lea	pin_link-pin_qu(a2),a0	 ; unlink pipe
pipe_remove
	assert	pin_link,0
	lea	pil_list(a3),a1
	move.w	mem.rlst,a2
	jsr	(a2)

	jmp	gu_rchp 		 ; return heap
	end
