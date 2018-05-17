; Helper routine to make SMSQ/E code QDOS PE compatible   2005  Marcel Kilgus

	section driver

	xdef	cn_io

	include 'dev8_keys_con'
cn_io
	movem.l a3/a4,-(sp)
	move.l	pt_clink(a3),a3 	get console driver linkage address
	move.l	pt_aio(a3),a4		and io entry address
	jsr	(a4)
	movem.l (sp)+,a3/a4
	rts

	end
