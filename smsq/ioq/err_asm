* Queue maintenance: error returns   V2.00    Tony Tebby  QJUMP
*
	section ioq
*
	xdef	ioq_empty		test for eof on empty queue
	xdef	ioq_nc			set not complete
	xdef	ioq_eof 		set end of file
*
	include dev8_keys_err
	include dev8_keys_qu
*
* empty queue, test for eof
*
ioq_empty
	tst.b	(a2)			end of file?
	bmi.s	ioq_eof 		... yes
ioq_nc
	moveq	#err.nc,d0		... not complete
	move.l	(sp)+,a3		restore scratch register
	rts
ioq_eof
	moveq	#err.eof,d0		end of file
	move.l	(sp)+,a3		restore scratch register
	rts
	end
