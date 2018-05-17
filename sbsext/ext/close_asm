* Close BASIC channels	 V0.0	 1985	 Tony Tebby
*
	section exten
*
	xdef	close
*
	xref	ut_chget		get channel
	xref	ut_chlook		look for channel in table
*
	include dev8_sbsext_ext_keys
*
*	d4   s	pointer to channel table
*	d6   s	channel number
*	a0   s	channel ID
*
close
	cmp.l	a3,a5			any parameters?
	beq.s	cls_all 		... none, close all
cls_ploop
	bsr.l	ut_chget		get a channel
	bsr.s	cls_chan		and close it
	cmp.l	a3,a5			end of list?
	bgt.s	cls_ploop		... no
	rts				... done
cls_all
	moveq	#-1,d6			do not default
	addq.w	#4,d6			start with channel #3
cls_aloop
	bsr.l	ut_chlook		look in table
	bsr.s	cls_chan
	addq.w	#1,d6			next channel number
	cmp.l	bv_chp(a6),d4		end of table yet?
	blt.s	cls_aloop
	rts
*
cls_chan
	bne.s	cls_no			if error, check for not open (OK)
	moveq	#-1,d0			channel ID -1 for closed file
	move.l	d0,(a6,d4.l)
	moveq	#io.close,d0		close channel
	trap	#2
cls_ok
	moveq	#0,d0
	rts
cls_no
	moveq	#err.no,d1		not open value
	cmp.l	d0,d1			was it not open?
	beq.s	cls_ok			... yes, ok
	addq.l	#4,sp			... no, quit
	rts
	end
