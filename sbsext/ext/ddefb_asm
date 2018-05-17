* Delete filing system drive definition blocks	V0.0    Tony Tebby  QJUMP
*
	section exten
*
*	DEL_DEFB			delete drive definition blocks
*
	xdef	del_defb
*
	include dev8_sbsext_ext_keys
*
del_defb
	trap	#0			 goto supervisor mode
	move.l	a6,-(sp)		 save BASIC base address
	moveq	#mt.inf,d0
	trap	#1			 get sysvars
	move.l	a0,a6
*
ddf_mdv
	tst.b	sv_mdrun(a6)		 wait for mdvs to stop running
	bne.s	ddf_mdv
*
	move.w	#150,d0 		 wait 3 seconds for asynch to go away
	add.w	sv_pict(a6),d0
ddf_wait
	cmp.w	sv_pict(a6),d0
	bhs.s	ddf_wait
*
	moveq	#$f,d7			 release all 16 definition blocks
	lea	sv_fsdef+$40(a6),a4
ddf_loop
	move.l	-(a4),d0		 next block
	beq.s	ddf_eloop		 ... empty
	move.l	d0,a0
	tst.b	fs_files(a0)		 any files open?
	bne.s	ddf_eloop		 ... yes
	move.w	mm.rechp,a2		 ... no, release this block
	jsr	(a2)
	clr.l	(a4)			 and clear pointer
ddf_eloop
	dbra	d7,ddf_loop
*
	move.l	(sp)+,a6		 restore BASIC base
	and.w	#$d8ff,sr		 back to user mode
	rts
	end
