* scheduler entry  V2.01   1986  Tony Tebby  QJUMP
*
	section shd
*
	xdef	shd_schd	normal reschedule
	xdef	shd_pshd	polling reschedule
*
	xref	io_sched	retry io operations
	xref	shd_relf	release flag
	xref	shd_break
	xref	sms_rtdo
*
	include dev8_keys_sys
	include dev8_keys_jcbq
	include dev8_keys_iod
	include dev8_keys_psf
	include dev8_keys_sbasic
	include dev8_smsq_smsq_base_keys
*
* No job at all
*
shd_mpty
	clr.w	sys_pict(a6)		 ; no polls
	jmp	sms_rtdo		 ; ... and return
*
* job gone
*
shd_nojb
	clr.l	sys_jbpt(a6)		 ; no current job
	lea	psf_top(sp),sp		 ; remove stack frame
	bra.s	shd_rshd		 ; and reschedule
*
* polling reschedule
*
shd_pshd
	and.w	#$f8ff,sr		 ; re-enable interrupts
*
* normal reschedule
*
shd_schd
	clr.b	sys_rshd(a6)		 ; clear scheduler request

	move.l	sys_jbpt(a6),d7 	 ; current job
	beq.s	shd_nojb
	bmi.s	shd_mpty		 ; no job at all yet!!

	move.l	d7,a5			 ; pointer to job table
	move.l	(a5),d7
	bmi.s	shd_nojb		 ; ... job has gone!!
*
	move.l	d7,a5
	move.l	(sp)+,d7		 ; set d7
	movem.l d0-d7/a0-a4,jcb_save(a5) ; save registers
	lea	jcb_a5(a5),a5
	move.l	(sp)+,(a5)+		 ; a5
	move.l	(sp)+,(a5)+		 ; a6
	move.l	usp,a4			 ; usp
	move.l	a4,(a5)+
	move.w	(sp)+,(a5)+		 ; sr
	move.l	(sp)+,(a5)		 ; pc
*
	move.w	#$a279,d7		 ; re-randomise
	mulu	sys_rand(a6),d7
	rol.w	#1,d7
	move.w	d7,sys_rand(a6)

	move.l	sys_jbtb(a6),a0
	move.l	(a0),a0
	move.l	jcb_exv(a0),sys_ertb(a6) ; exception re-direction vector

shd_rshd
	moveq	#0,d3			 ; polling interrupt accumulator
shd_brk
	move.w	d3,-(sp)		 ; save accumulator
	bclr	#7,sys_brk(a6)		 ; break?
	beq.s	shd_nbrk
	jsr	shd_break

shd_nbrk
	move.w	sys_pict(a6),d3 	 ; polling interrupt count
	sub.w	d3,sys_pict(a6) 	 ; reset
	add.w	d3,(sp)
	move.w	d3,-(sp)
	lea	sys_shdl(a6),a0 	 ; list of scheduler actions
*
shd_sloop
	moveq	#0,d3
	move.w	(sp),d3 		 ; polling interrupt count
	move.l	(a0),d0
	beq.s	shd_io			 ; ... done, retry io
	move.l	d0,a0
	lea	-iod_shlk(a0),a3	 ; base of linkage
	move.l	iod_shad(a3),a4 	 ; address of scheduler routine
	move.l	a0,-(sp)
	jsr	(a4)			 ; do routine
	move.l	(sp)+,a0		 ; restore
	bra.s	shd_sloop
*
* retry io operations
*
shd_io
	bsr.l	io_sched

	move.l	(sp)+,d3		 ; total polls missed
	beq.s	shd_prior		 ; ... none, it was normal entry
	cmp.w	sys_slug(a6),d3
	blt.s	shd_brk

*
* look for highest priority
*
shd_prior
	sub.l	a5,a5			 ; no un-suspended job found
	moveq	#0,d5			 ; highest priority
	moveq	#0,d6			 ; total priority increment
	move.l	sys_jbpt(a6),d7
	bgt.s	shd_pcurr		 ; start at current job
	move.l	sys_jbtb(a6),d7 	 ; start at job 0
shd_pcurr
	move.l	d7,a1
	move.l	a1,d1
	sub.l	sys_jbtb(a6),d1
	lsr.w	#2,d1			 ; current job number
	move.w	sys_jbtp(a6),d2 	 ; top job

shd_ploop
	move.l	(a1),d0 		 ; next jcb
	bmi.s	shd_plend
	move.l	d0,a0
	move.w	jcb_wait(a0),d0 	 ; waiting?
	blt.s	shd_plend		 ; ... yes, forever
	beq.s	shd_princ		 ; ... no, increment priority
	sub.w	d3,d0			 ; ... yes, count down
	bgt.s	shd_swait
	jsr	shd_relf		 ; release flag
	moveq	#0,d0
shd_swait
	move.w	d0,jcb_wait(a0) 	 ; reset wait
	bne.s	shd_plend		 ; ... still waiting
*
shd_princ      ; here D0 = 0
	move.b	jcb_pinc(a0),d0 	 ; total priority
	beq.s	shd_plend		 ; inactive
	add.w	d0,d6			 ; total priority increment
	add.b	jcb_pacc(a0),d0 	 ; accumulated priority
	bcc.s	shd_pset
	moveq	#-1,d0			 ; max
shd_pset
	move.b	d0,jcb_pacc(a0) 	 ; set priority accumulator
*
	cmp.b	d5,d0			 ; higher priority
	blo.s	shd_plend		 ; ... lower
	move.b	d0,d5			 ; highest priority
	move.l	a1,a5			 ; ... and pointer
*
shd_plend
	addq.l	#4,a1			 ; next table pointer
	addq.w	#1,d1
	cmp.w	d2,d1			 ; last?
	ble.s	shd_pcheck		 ; ... no
	moveq	#0,d1			 ; start back at 1
	move.l	sys_jbtb(a6),a1
shd_pcheck
	cmp.l	d7,a1
	bne.s	shd_ploop
*
* scheduler return to job (table pointer a5)
*
shd_go
	move.l	a5,sys_jbpt(a6) 	 ; set job pointer
	beq.l	shd_rshd		 ; ... none
	move.l	(a5),a5
	clr.b	jcb_pacc(a5)		 ; set priority increment to zero
shd_rest
	clr.b	sys_rshd(a6)		 ; just re-scheduled
	clr.b	jcb_evtw(a5)		 ; not waiting for event
	move.l	jcb_exv(a5),sys_ertb(a6) ; set exception table
	lea	jcb_save(a5),a5 	 ; restore all registers
	movem.l (a5)+,d0-d7/a0-a4	 ; ... most
	lea	jcb_pc-jcb_a5(a5),a5
	move.l	(a5),-(sp)		 ; pc
	move.w	-(a5),-(sp)		 ; sr
	move.l	-(a5),a6		 ; usp
	move.l	a6,usp
	move.l	-(a5),a6		 ; a6
	move.l	-(a5),a5		 ; a5
	rte				 ; return
	end
