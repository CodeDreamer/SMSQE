* QDOS specific scheduler loop routine	 V1.12	  1985  Tony Tebby	  QJUMP
*						  1986  Jonathan Oakley  QJUMP
* 2020-08-10  1.12  Update pt_dmode for CON2 code to use (MK)

	section driver
*
	xdef	pt_sched_qdos
*
	xref	pt_sched
	xref	pt_wchka
	xref	pt_mrstj
*
	include dev8_mac_assert
	include dev8_keys_qdos_sms
	include dev8_keys_chn
	include dev8_keys_sys
	include dev8_keys_qu
	include dev8_keys_k
	include dev8_keys_qlv
	include dev8_keys_con
*
pt_sched_qdos
	move.l	pt_mtest(a3),a5 	get display mode test address
	tst.b	sd_cattr(a5)		check if mode changed
	blt	pt_sched		... no, normal scheduler
*
	st	sd_cattr(a5)		  set mode not changed
******	move.l	pt_romdr(a3),chn_drvr(a5) restore ROM driver just in case
pt_schdm

	moveq	#1<<sysqm..8,d1
	and.b	sys_qlmr(a6),d1 	now either 0 or 8
	assert	ptm.ql4,ptm.ql8-8,0
	move.b	d1,pt_dmode(a3) 	save new mode

	move.b	#pt.supmd,pt_pstat(a3)	  set pointer suppressed
	assert	pt_bstat,pt_bsupp-1,pt_bpres-2,pt_bcurr-3
	move.l	#ptb.psup<<16,pt_bstat(a3)
	clr.b	pt_reltm(a3)		  and relax
*
*	First restore all primary windows, from the bottom up
*
	moveq	#sms.info,d0		who just went MODE?
	trap	#1
	move.l	d1,d7			he did, keep his job ID
*

	lea	pt_head(a3),a0		fix his mode now
mode_chg
	move.l	(a0),d0 			   next window
	beq.s	mode_end			   isn't one, oddly enough
	move.l	d0,a0				   point to it
	cmp.l	chn_ownr-sd_prwlt-sd.extnl(a0),d7  owned by MODE job?
	bne.s	mode_chg			   no, try next
	move.b	pt_dmode(a3),sd_wmode-sd_prwlt(a0) set its mode
*
mode_end
; When we are here a MODE call has been made, which means the screen was
; already cleared by the OS. We cannot save any more window contents as
; they are already gone!
	moveq	#-1,d3			don't save newly locked windows
	jsr	pt_wchka(pc)		check all for correct mode
*
	jmp	pt_mrstj(pc)		restore/reset all windows

	end
