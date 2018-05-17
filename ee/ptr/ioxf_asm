; Helper routine to make SMSQ/E code QDOS PE compatible   2005  Marcel Kilgus

	section driver

	xdef	pt_ioxf
	xref	pt_linkc
	xref	pt_io

pt_ioxf
	jsr	pt_linkc(pc)		; link into lists
	jmp	pt_io

	end
