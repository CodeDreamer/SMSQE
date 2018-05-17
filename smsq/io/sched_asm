; IOSS scheduler action   V2.00    1986  Tony Tebby  QJUMP

	section io

	xdef	io_sched
	xdef	io_cinvi

	xref	sms_ckjx		 ; check job exists

	include 'dev8_keys_err'
	include 'dev8_keys_sys'
	include 'dev8_keys_chn'
	include 'dev8_keys_iod'
	include 'dev8_keys_qdos_io'
	include 'dev8_keys_jcbq'

io_sched
	move.l	sys_chpt(a6),a5 	 ; pointer to last channel checked
	move.w	sys_iopr(a6),d6 	 ; IO priority
	moveq	#0,d7
	move.w	sys_chtp(a6),d7 	 ; number of channels to check
	lsl.w	#2,d7
	add.l	sys_chtb(a6),d7 	 ; pointer off top of used channel table
	pea	ios_try2		 ; try twice

ios_loop
	addq.l	#4,a5
ios_loop4
	cmp.l	d7,a5			 ; off top of current table?
	bgt.l	ios_rts 		 ; ... yes, done
	move.l	(a5),d0 		 ; next channel
	bmi.s	ios_loop
	move.l	d0,a0			 ; channel base
	tst.b	chn_stat(a0)		 ; waiting?
	beq.s	ios_loop		 ; ... no
	move.w	chn_jbwt+2(a0),d0	 ; job waiting
	move.w	chn_jbwt(a0),d1 	 ; job tag
	lsl.w	#2,d0			 ; index table in long words
	move.l	sys_jbtb(a6),a4 	 ; base of job table
	move.l	(a4,d0.w),d0		 ; job address
	bmi.l	ios_njob		 ; ... unset
	move.l	d0,a4			 ; ... set
	cmp.w	jcb_tag(a4),d1
	bne.l	ios_njob		 ; ... wrong job, kill operation
	move.w	jcb_wait(a4),d4 	 ; is job waiting
	beq.s	ios_njob		 ; ... no, how is this?

	moveq	#0,d0
	move.b	chn_actn(a0),d0 	 ; set action
	movem.l jcb_d1(a4),d1/d2	 ; d1, d2
	moveq	#-1,d3			 ; d3
	movem.l jcb_a1(a4),a1/a2	 ; a1, a2
	tst.b	chn_stat(a0)		 ; relative
	bgt.s	ios_do
	add.l	jcb_a6(a4),a1		 ; a1 rel a6

ios_do
	movem.l d6/d7/a4/a5,-(sp)	 ; save our regs
	move.w	d4,d7			 ; set wait
	move.l	chn_drvr(a0),a3
	lea	-iod_iolk(a3),a3	 ; linkage base
	move.l	iod_ioad(a3),a4
	jsr	(a4)			 ; call
	movem.l (sp)+,d6/d7/a4/a5	 ; restore

	tst.b	chn_stat(a0)		 ; a1 rel a6?
	bgt.s	ios_cinvi		 ; ... no
	sub.l	jcb_a6(a4),a1
ios_cinvi
	cmp.b	#iof.load,chn_actn(a0)	 ; load?
	bne.s	ios_upr 		 ; ... no
	cmp.l	jcb_d1(a4),d1		 ; data transferred?
	beq.s	ios_upr 		 ; ... no
io_cinvi
	move.w	d0,d0			 ; invalidate cache patch
ios_upr
	movem.l d0/d1,jcb_d0(a4)	 ; update registers
	move.l	a1,jcb_a1(a4)
	addq.l	#-err.nc,d0		 ; not complete?
	beq.s	ios_done1
	clr.w	jcb_wait(a4)		 ; ... no, continue
	sf	chn_stat(a0)
	subq.l	#-err.nc,d0
	move.w	sr,d0
	move.b	d0,jcb_ccr(a4)		 ; set condition code
ios_done1
	subq.w	#1,d6
	bgt	ios_loop		 ; do another
	addq.l	#4,sp			 ; ignore special return
	move.l	a5,sys_chpt(a6) 	 ; set pointer for next call
ios_rts
	rts


ios_njob
	sf	chn_stat(a0)		 ; no job waiting
	bra	ios_loop

ios_try2
	move.l	sys_chtb(a6),a5 	 ; start at base
	move.l	sys_chpt(a6),d7 	 ; and go up to pointer
	bsr	ios_loop4		 ; and return when complete
	rts
	end
