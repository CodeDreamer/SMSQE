* Remove job  V2.00    1986  Tony Tebby  QJUMP
*
	section sms
*
	xdef	sms_rmjb		remove job
	xdef	sms_frjb		force remove job
*
	xref	sms_ckjx		check job exists
	xref	sms_nxjb		next job in tree
	xref	sms_rte
	xref	sms_rtok
	xref	mem_rtpa		remove from tpa
	xref	mem_rchp		remove from common heap
	xref	ioa_topc
*
	include dev8_keys_err
	include dev8_keys_sys
	include dev8_keys_chp
	include dev8_keys_jcbq
	include dev8_keys_iod
*
*	d0  r	0, not complete or invalid job
*	d1 c s	job ID to remove
*	d3 c  p error code
*	a0   s	
*	a6 c  p base of system variables
*
*	all other registers preserved
*
reglist reg	d1-d6/a1-a4		save a lot of regs in case of close
*
srj_nc
	move.l	(sp)+,d1		restore d1
	move.l	(sp)+,a1
srj_exnc
	moveq	#err.nc,d0		jobs active
	bra.s	sms_rte1
*
*
*
sms_rmjb
	bsr.l	sms_ckjx		check if job exists
sms_rte1
	bne.l	sms_rte 		... oops
	move.l	a1,-(sp)		save a1
	move.l	d1,-(sp)		save job id
	move.l	d1,d0			top job is current job
	beq.s	srj_nc			... cannot remove job 0
*
srj_aloop
	tst.b	jcb_pinc(a0)		active?
	bne.s	srj_nc			... yes
	bsr.l	sms_nxjb		next job
	tst.l	d1			last?
	bne.s	srj_aloop
	move.l	(sp)+,d1		restore job id
	move.l	(sp)+,a1		and a1
*
sms_frjb
	move.l	sp,sys_psf(a6)		set stack frame (for move superBASIC)
	bsr.l	sms_ckjx		check if job exists
	bne.s	sms_rte1		... oops
	tst.l	d1
	beq.s	srj_exnc
	movem.l reglist,-(sp)		set register list
*
	move.l	d1,d4			this job is top job
srj_mloop
	st	jcb_strt(a0)		mark job
	tst.b	jcb_wjob(a0)
	beq.s	srj_mnext
	move.l	jcb_wjid(a0),d5 	is there a waiting job?
	exg	d1,d5			and current
	bsr.l	sms_ckjx		exists?
	bne.s	srj_mrset		... no
	move.w	jcb_wait(a0),d0 	waiting ...
	addq.w	#-jcb.wjob,d0		... for job?
	bne.s	srj_mrset		... no
	clr.w	jcb_wait(a0)		... yes, unsuspend
	move.l	d3,jcb_d0(a0)		and set error return
	move.w	sr,d0			and ccr
	move.b	d0,jcb_ccr(a0)
srj_mrset
	move.l	d5,d1			reset IDs
srj_mnext
	move.l	d4,d0
	bsr.l	sms_nxjb
	tst.l	d1			last?
	bne.s	srj_mloop
*
* all jobs to be removed have been marked, now remove them and all their bits
*
	moveq	#0,d2			top job in table
	move.l	sys_jbtb(a6),a1
*
srj_rloop
	tst.b	(a1)			 ; exists?
	bmi.s	srj_nxjt		 ; ... no
	move.l	(a1),a0 		 ; address of job
	tst.b	jcb_strt(a0)		 ; flagged?
	bpl.s	srj_tpjb		 ; ... no
	cmp.l	sys_jbpt(a6),a1 	 ; current job?
	bne.s	srj_sjbt		 ; ... no
	clr.l	sys_jbpt(a6)		 ; ... yes, no current job now
srj_sjbt
	st	(a1)			 ; no job any more
	swap	d1
	move.w	jcb_tag(a0),d1		 ; set tag
	swap	d1

	move.l	a0,-(sp)		 ; save job base
	move.l	sys_chpb(a6),a2 	 ; common heap base
	moveq	#0,d3

srj_rchp
	cmp.l	sys_fsbb(a6,d3.l),a2	off top of heap?
	bhs.s	srj_rtpa		... yes
	move.l	a2,a0
	add.l	chp_len(a2),a2		next heap entry
	cmp.l	chp_ownr(a0),d1 	owned by this job?
	bne.s	srj_rchp		... no
	move.l	chp_drlk(a0),d0 	any driver?
	bgt.s	srj_close		... yes, close it
	blt.s	srj_rchp
	bsr.l	mem_rchp		... no, return it
	bra.s	srj_rchp		... next
*	
srj_close
	move.l	d0,a3			set linkage
	lea	-iod_iolk(a3),a3	base of linkage block
	move.l	iod_clos(a3),a4
	movem.l d1/d2/d3/a1/a2,-(sp)	save the registers we are using
	jsr	(a4)			close routine
	movem.l (sp)+,d1/d2/d3/a1/a2	next
	bra.s	srj_rchp
*
srj_rtpa
	tst.w	d3			 ; heap or TPA?
	bne.s	srj_rjob		 ; top of search
	moveq	#sys_rpab-sys_fsbb,d3	 ; new top
	move.l	sys_tpab(a6),a2
	bra.s	srj_rchp		 ; look in TPA this time

srj_rjob
	move.l	(sp)+,a0		 ; job base
	bsr.l	mem_rtpa		 ; remove job area
	bra.s	srj_nxjt

srj_tpjb
	move.w	d1,d2			 ; set new top job

srj_nxjt
	addq.l	#4,a1			 ; next hole
	addq.w	#1,d1			 ; next number
	cmp.w	sys_jbtp(a6),d1 	 ; at top yet?
	ble.s	srj_rloop		 ; ... no
*
	move.w	d2,sys_jbtp(a6) 	 ; reset top job
	jsr	ioa_topc		 ; and top channel
	movem.l (sp)+,reglist
*
	st	sys_rshd(a6)		re-schedule
	bra.l	sms_rtok		done
	end
