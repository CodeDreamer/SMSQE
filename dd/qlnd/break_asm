* Test for break			 1985	 Tony Tebby  QJUMP
*
	section nd
*
	xdef	nd_break		test for break
*
	include dev8_dd_qlnd_keys
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
	movem.l (sp)+,d1/d5/d7/a3
	rts

ipc_do
	moveq	#mt.ipcom,d0		do command
	trap	#1
	and.b	-(a3),d1		check if key pressed
	rts
	end
