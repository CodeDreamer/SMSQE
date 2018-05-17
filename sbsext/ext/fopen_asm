* File open operations	V0.7	 1984/1985  Tony Tebby  QJUMP
*
*	OPEN #n,name			open a file for w/r
*	OPEN_IN #n,name 				read only
*	OPEN_NEW #n,name				w/r (new file)
*	OPEN_OVER #n,name				w/r (overwrites)
*	OPEN_DIR #n,name				directory
*
*	FTEST (name)			function to test for file
*
*	FOPEN (#n,name) 		functions to open file	w/r
*	FOP_IN (#n,name)					read only
*	FOP_NEW (#n,name)					w/r (new file)
*	FOP_OVER (#n,name)					w/r (overwrites)
*	FOP_DIR (#n,name)					directory
*
	section exten
*
	xdef	open
	xdef	open_in
	xdef	open_new
	xdef	open_over
	xdef	open_dir
*
	xdef	ftest
*
	xdef	fopen
	xdef	fop_in
	xdef	fop_new
	xdef	fop_over 
	xdef	fop_dir
*
	xref	ut_par1 		check for one parameter
	xref	ut_par2 		check for two parameters
	xref	ut_chanf		find or make a channel entry
	xref	ut_chanv		find a vacant channel entry
	xref	ut_fopnp		open a file (no prompt or retry)
	xref	ut_fopin		open an input file
	xref	ut_rtfd1		return floating point value of d1
	xref	ut_fclos		close file
*
	include dev8_sbsext_ext_keys
*
*	d4   s	pointer to channel table
*	d6   s	channel number
*	d7   s	open key
*
open
	moveq	#io.old,d7
	bra.s	op_parm
open_in
	moveq	#io.share,d7
	bra.s	op_parm
open_new
	moveq	#io.new,d7
	bra.s	op_parm
open_over
	moveq	#io.overw,d7
	bra.s	op_parm
open_dir
	moveq	#io.dir,d7
op_parm
	bsr.l	ut_par2 		check for two parameters only
op_parm2
	bsr.l	ut_chanf		find the channel
	bne.s	fop_rts 		... oops
op_fopen
	move.w	d7,d3			get the open key
	swap	d7			... and the retry key
	bsr.l	ut_fopnp		and open the file
	bne.s	op_rts
	move.l	a0,(a6,d4.l)		set channel ID
	tst.l	d0			and set status
op_rts
	rts
*
ftest
	bsr.l	ut_par1 		check for one parameter only
	bsr.l	ut_fopin		to check, open in
	bne.s	fop_ret 		... not opened
	bsr.l	ut_fclos		and close again
	bra.s	fop_ret
*
fopen
	moveq	#io.old,d7
	bra.s	fop_parm
fop_in
	moveq	#io.share,d7
	bra.s	fop_parm
fop_new
	moveq	#io.new,d7
	bra.s	fop_parm
fop_over
	moveq	#io.overw,d7
	bra.s	fop_parm
fop_dir
	moveq	#io.dir,d7
fop_parm
	or.l	#$ffff0000,d7	do not retry
	move.l	a5,d0		check the number of parameters
	sub.l	a3,d0
	ble.s	fop_bp		... none
	sub.w	#$10,d0 	1 or 2?
	bgt.s	fop_bp		... more
	beq.s	fop_par2	... 2, get the channel ID
	bsr.l	ut_chanv	find a vacant channel
	bne.s	fop_rts 	... oops
	bsr.s	op_fopen	open the channel
	bne.s	fop_ret 	... error return the code
	move.w	d6,d0		... ok, return channel number
	bra.s	fop_ret
fop_par2
	bsr.s	op_parm2	get channel number and open
fop_ret
	move.l	d0,d1		set error code
	bra.l	ut_rtfd1	... and return it
fop_bp
	moveq	#err.bp,d0
fop_rts
	rts
	end
