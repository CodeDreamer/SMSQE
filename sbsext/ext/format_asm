* Format command	    1990   Tony Tebby	 QJUMP
*
	section exten
*
*	FORMAT device name	format device
*
	xdef	format

	xref	ut_gxnm1	get exactly one name
	xref	ut_chan1	use default channel 1
	xref	stat_dos	write sectors
*
	include dev8_sbsext_ext_keys
*
format
	jsr	ut_chan1		 ; channel for report
	bne.s	fmt_rts
	move.l	a0,a4			 ; keep it
	jsr	ut_gxnm1		 ; name
	bne.s	fmt_rts

	lea	(a1),a0 		 ; format name
	moveq	#io.formt,d0
	trap	#4
	trap	#2			 ; relative

	move.l	bv_bfbas(a6),d4 	 ; buffer start
	move.l	d4,a1			 ; fill buffer
	and.l	#$0000ffff,d1		 ; word only!
	and.l	#$0000ffff,d2
	tst.l	d0
	beq.l	stat_dos		 ; do stats

fmt_rts
	rts
	end
