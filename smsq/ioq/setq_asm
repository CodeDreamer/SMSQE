* Queue maintenance: setup a queue	V2.01	 Tony Tebby  QJUMP
* correct header info : d1 is preserved  2.01	  W. Lenerz
*
	section ioq
*
	xdef	ioq_setq
*
	include dev8_keys_qu
*
*	d1 c  p  length of queue (long word, not including header)
*	a2 c  p pointer to queue 
*	a3   sp
*
*	all other registers preserved
*	no error return, condition codes arbitrary
*
reglist reg	a2/a3
*
ioq_setq
	movem.l reglist,-(sp) 
*
	lea	qu_strtq(a2,d1.l),a3	end of queue
	clr.l	(a2)+			no link or eoff
	move.l	a3,(a2)+		set end of queue
	sub.l	d1,a3			... back to start
	move.l	a3,(a2)+		set in pointer
	move.l	a3,(a2)+		and out pointer
*
	movem.l (sp)+,reglist
	rts
	end
