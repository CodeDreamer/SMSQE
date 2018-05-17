* Queue maintenance: test a queue   V2.00    Tony Tebby  QJUMP
*
	section ioq
*
	xdef	ioq_test
	xdef	ioq_tstg		test if byte ready to get

	xref	ioq_empty		test empty queue for eof
	xref	ioq_nc			set not complete
	xref	ioq_eof 		set end of file
*
	include dev8_keys_err
	include dev8_keys_qu
*
*	d0  r	error condition (0, eof or not complete)
*	d1  r	next byte in queue (if d0=0)
*	d2  r	free_space in queue (long word)   (ioq_test only)
*	a2 c  p pointer to queue 
*	a3   sp
*
*	all other registers preserved
*
*
ioq_test
	move.l	a3,-(sp)		save scratch register
	moveq	#-qu_strtq-1,d2 	find spare
	add.l	qu_endq(a2),d2		total spare
	sub.l	a2,d2
*
	move.l	qu_nexti(a2),d0 	next in
	move.l	qu_nexto(a2),a3 	next out
	move.b	(a3),d1 		set next character
*
	sub.l	a3,d0			number in queue
	beq.s	ioq_empty		... none
	bgt.s	iqt_sspr		... there are some, set spare
	add.l	d2,d0			wrapped around, add total
	addq.l	#1,d0			... length including the one missing
iqt_sspr
	sub.l	d0,d2			set spare
	moveq	#0,d0			... done
	move.l	(sp)+,a3		restore scratch register
	rts
*
* just test queue for getting a byte
*
ioq_tstg
	move.l	a3,-(sp)		save scratch register
	move.l	qu_nexto(a2),a3 	next out
	cmp.l	qu_nexti(a2),a3 	any thing there?
	beq.s	ioq_empty		... no
	move.b	(a3),d1 		set next character
	moveq	#0,d0			ok
	move.l	(sp)+,a3		restore scratch register
	rts
	end
