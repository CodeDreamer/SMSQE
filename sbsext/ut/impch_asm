* Implicit channel handling   V0.0    1985  Tony Tebby   QJUMP
*
	section utils 
*
	xdef	ut_impch	     open implicit channel (d3,d6)
	xdef	ut_impc3	     open implicit channel (d3) or default #3
	xdef	ut_impin	     open implicit channel input or default #3
*
	xref	ut_chan 		get #channel
	xref	ut_fopnp		open channel (with prompt if exists)
	xref	ut_fclos		close implicit channel
*
	include dev8_sbsext_ext_keys
*
*	d3 c	open key
*	d6 c	default channel number
*
***** must be called with 4 bytes already on the stack
*
ut_impin
	moveq	#io.share,d3		open channel share
ut_impc3
	moveq	#3,d6			default #3
ut_impch
	cmp.l	a3,a5			any arguments?
	ble.l	ut_chan 		... no, just set default
	cmp.w	#sp.backs,(a6,a3.l)	null param with \?
	bne.l	ut_chan 		... no, set default or given #n
*
	addq.l	#8,a3			skip null parameter
	move.l	d7,-(sp)
	bsr.l	ut_fopnp		open file
	movem.l (sp)+,d7
	bne.s	uti_rts 		... oops
	addq.l	#8,a3			move to next parameter
*
	pea	ut_impcl(pc)		set address of return
	move.l	8(sp),-(sp)		move other return addresses down
	move.l	8(sp),-(sp)
	move.l	a0,$c(sp)		and save channel
	moveq	#0,d0			OK
uti_rts
	rts
*
* close implicit channel
*
ut_impcl
	move.l	(a7)+,a0		get channel of stack
	addq.l	#4,sp			and old rubbish
	bra.l	ut_fclos		now close file
	end
