* Do repeated net operation	 1985	 Tony Tebby  QJUMP
*
	section nd
*
	xdef	nd_rept 		do repeated operation (a2)
	xdef	nd_break		test for break
*
	xref	ms_ntabt		network abort message
*
	include dev8_dd_nd_keys
*
nd_rept
*
* wait for mdv to stop (otherwise timing will be very wrong)
*
ndr_mdrun
	tst.b	sv_mdrun(a6)		is a microdrive running?
	bne.s	ndr_mdrun		... wait for it to stop
*
	move.w	#2500,d7		try 2500 times in all (about 25 seconds)
	tst.b	nd_dest(a0)		is it send broadcast?
	bne.s	ndr_try 		... no
	lsr.w	#1,d7			check immediately
*
ndr_try
	jsr	(a2)			do operation
	beq.s	ndr_rts 		... done
	blt.s	ndr_tbrk		try again if nc
	move.w	#1499,d0		otherwise wait 7ms
	dbra	d0,*
ndr_tbrk
	cmp.w	#2000,d7		wait about 5-6 seconds before check break
	bgt.s	ndr_tend
*
	bsr.s	nd_break		check break
	bne.s	ndr_abort
ndr_tend
	dbra	d7,ndr_try
*
ndr_abort
	movem.l d1/d2/a0/a1/a2,-(sp)	   save IO regs
	lea	ms_ntabt(pc),a1 	get address of abort message
	add.w	(a1),a1
	sub.l	a0,a0			write to zero
	move.w	ut.mtext,a2
	jsr	(a2)
	movem.l (sp)+,d1/d2/a0/a1/a2	   restore IO regs
*
	moveq	#err.nc,d0		not complete
*
ndr_rts
	rts
*
* Test for break
*
* IPC commands to read rows of the keyboard
*
	dc.w	$02			position of CTRL
ipc_rr7 dc.b	9,1,0,0,0,0,7,2 	read CTRL key row
	dc.w	$40			position of SPACE
ipc_rr1 dc.b	9,1,0,0,0,0,1,2 	read SPACE key row
*
nd_break
	movem.l d1/d5/d7/a3,-(sp)	      save volatiles
	lea	ipc_rr7(pc),a3		check CTRL key row
	bsr.s	ipc_do
	beq.s	ndb_exit		 not pressed
	lea	ipc_rr1(pc),a3		check SPACE key row
	bsr.s	ipc_do
ndb_exit
	movem.l (sp)+,d1/d7/d5/a3
	rts

ipc_do
	moveq	#mt.ipcom,d0		do command
	trap	#1
	and.b	-(a3),d1		check if key pressed
	rts
	end
