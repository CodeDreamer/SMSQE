; Utility open a file using parameter	 1994	 Tony Tebby

	section utils

	xdef	ut_fopen	open key in d3, retry key in d7
	xdef	ut_fopin	open for input (data default)
	xdef	ut_fopnp	open with prompt if exists

	xref	ut_gtnm1	get a name
	xref	ut_opdefx	open with default if required
	xref	ut_fhead	read file header

	include dev8_sbsext_ext_keys
	include 'dev8_sbsext_ut_opdefx_keys'

ut_fopin
	moveq	#io.share,d3		open shared (data)
ut_fopnp
	moveq	#uod.datd+uod.prmt,d2	... prompt if exists
ut_fopen
	movem.l d2/d3,-(sp)		save open keys
	bsr.l	ut_gtnm1		and get filename
	movem.l (sp)+,d2/d3
	bne.s	utf_rts 		... oops
	jmp	ut_opdefx		open
utf_rts
	rts

	end
