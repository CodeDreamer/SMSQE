* Pointer CLOSE   V1.03    1985  Tony Tebby   QJUMP
*
	section driver
*
	xdef	pt_close
	xdef	pt_closu
*
	xref	pt_wremv
	xref	pt_slock
	xref	pt_setp
	xref	pt_smove
	xref	pt_pick
*
	include dev8_keys_sys
	include dev8_keys_qlv
	include dev8_keys_qdos_sms
	include dev8_keys_con
*
reglist reg	d4-d7/a4/a5
*
pt_close
	move.l	sys_chtb(a6),a1
	cmp.l	(a1),a0 		channel 0?
	bne.s	ptc_close		... no
	tst.w	sys_chtg(a6)		system init?
	beq.s	ptc_close		... no
	moveq	#0,d0
	rts

ptc_close
	movem.l reglist,-(sp)
*
ptc_keyq
	move.l	pt_kqoff(a3),d0
	lea	sd.extnl(a0,d0.l),a1	get keyboard queue pointer
	cmp.l	sys_ckyq(a6),a1 	is it current queue?
	bne.s	ptc_unlk		... no
	move.l	pt_dumq1(a3),a1 	move pointer to dummy channel...
	lea	sd.dq2(a1),a1		...number 2
	move.l	a1,sys_ckyq(a6)
*
ptc_unlk
	move.w	mem.rlst,a2		set unlink address
	lea	sd.extnl(a0),a4 	set offset channel linkage address
	tst.b	sd_prwin(a4)		is it a primary window?
	blt.s	ptc_clpr		... yes, unlink
*
*	Unlink a secondary
*
	move.l	sd_pprwn(a4),a1 	get primary window
	lea	sd.extnl(a1),a5 	keep a pointer to it
	lea	sd_prwlt(a5),a1
	cmp.l	pt_head(a3),a1		is this primary top?
	bne.s	ptc_hits
	st	pt_bsupp(a3)		suppress keypress until up
*
ptc_hits
	movem.l sd_xhits(a5),d0-d3
	movem.l d0-d3,sd_xhits(a4)	copy sizes of primary
	bsr	ptc_rmsv		  remove our save area
	move.l	sd_wsave(a5),sd_wsave(a4) and copy primary's save area
	clr.b	sd_mysav(a4)		  which isn't ours
*
	lea	sd_sewll(a5),a1 	point to start of secondary list
	lea	sd_sewll(a4),a0 	our link pointer
	jsr	(a2)			unlink
	bra	ptc_chkb		and see what to do about areas
*
*	Come to here if we want to close a primary window. If possible, we
*	must promote a secondary window to the primary status.	If it was
*	well-behaved then it isn't now.
*
ptc_clpr
	move.l	sd_sewll(a4),d0 	make first secondary into new primary
	beq.s	ptc_ulkp		there isn't one, unlink primary
	move.l	d0,a0			point A0 at new primary
	sub.w	#sd_sewll,a0		normal cdb of it
	move.l	a0,a5			  keep a copy
	move.l	sd_prwlt(a4),sd_prwlt(a0) links forward and backward are same
	move.l	sd_prwlb(a4),sd_prwlb(a0)
	move.b	#sd.bbprm,sd_prwin(a0)	  set flag showing we are now primary
*
*	New primary inherits old one's save area
*
	exg	a4,a5
	bsr	ptc_rmsv		remove new primary's existing save area
	exg	a4,a5
*
ptc_cpsv
	move.l	sd_wsave(a4),sd_wsave(a5) use existing save area
	move.l	sd_wssiz(a4),sd_wssiz(a5) which is this big
	move.b	sd_mysav(a4),sd_mysav(a5) and may be ours
	clr.b	sd_mysav(a4)		  but certainly isn't the old primary's!
*
*	Fix whatever is pointing at us
*
	lea	sd_prwlb(a0),a1 	point to bottom up link
	move.l	(a1)+,a2		who is above us?
	move.l	a2,d0
	bne.s	ptc_lnkt		somebody, link to them
	lea	pt_head-4(a3),a2	no-one, link to head
ptc_lnkt
	move.l	a1,4(a2)		fill in top down link of window above
*
	move.l	(a1),a2 	       who is below us?
	subq.l	#4,a1
	move.l	a2,d0
	bne.s	ptc_lnkb		someone, link to them
	lea	pt_tail+4(a3),a2	no-one, link to tail
ptc_lnkb
	move.l	a1,-4(a2)		fill in top down link of window above
*
*	Now point all the secondaries at new primary
*
	lea	sd_sewll(a0),a2 	point to linked list of secondaries
	lea	-sd.extnl(a0),a1	point to start of new primary
ptc_prl
	move.l	(a2),d0 		last secondary?
	beq.s	ptc_chkb		yes, now set areas
	move.l	d0,a2
	move.l	a1,sd_pprwn-sd_sewll(a2)	change pointer to primary
	bclr	#sd..well,sd_behav-sd_sewll(a2) and it's badly behaved now
	bra.s	ptc_prl
*
*	This primary is the only window, so remove it from the lists
*	altogether.
*
ptc_ulkp
	lea	sd_prwlt(a4),a0 	unlink primary
	lea	pt_head(a3),a1		from top down list
	jsr	(a2)
	lea	sd_prwlb(a4),a0 	and...
	lea	pt_tail(a3),a1		...from bottom up list
	jsr	(a2)
	bra.s	ptc_wrmv		remove it from the screen
*
*	We now have the closed window (A4) unlinked from its old list, and
*	A5 pointing to the old list's primary.  If the closed window is a
*	well-behaved secondary then we mustn't alter the old primary's areas,
*	otherwise we should.
*
ptc_chkb
	cmp.b	#sd.wbsec,sd_behav(a4)	well-behaved secondary?
	beq.s	ptc_rchp		yes, don't fiddle areas or remove
	move.l	a5,a0
	jsr	pt_setp(pc)		set hit and outline areas correctly
*
*	If the closed window was within the old primary, then we don't need
*	to remove it.  This is detected by the new primary having the same 
*	outline as the old.
*
	movem.l sd_xouts(a4),d2/d3
	cmp.l	sd_xouts(a5),d2 	size the same?
	bne.s	ptc_cpov		no, copy overlaps
	cmp.l	sd_xouto(a5),d3 	origin the same?
	beq.s	ptc_slck		yes, just set locks
*
ptc_cpov
	tst.b	sd_wlstt(a5)		is new primary locked?	**** TT
	bpl.s	ptc_wrmv		... no, no need to update save **** TT
	move.l	a4,a1			source is old primary
	move.l	a5,a0			dest is new primary
	jsr	pt_smove(pc)		restore overlap from old to new
*
*	Remove the window.
*
ptc_wrmv
	move.l	a4,a0			put closed into A0
	jsr	pt_wremv(pc)		and remove it
	tst.l	d1			any left in current mode?
	bpl.s	ptc_omod		yes, stay in old mode
	move.l	pt_head(a3),a2		get head
	move.l	a2,d0
	beq.s	ptc_rchp		... no windows left
	move.b	sd_wmode-sd_prwlt(a2),d1 and its mode
	bra.s	ptc_nmod		new mode
*
*	There is a window in the current mode, so make sure the highest such
*	is top.
*
ptc_omod
	move.l	pt_head(a3),d0		point to head
	move.b	pt_dmode(a3),d1 	get current mode
ptc_mktl
	move.l	d0,a2
ptc_nmod
	cmp.b	sd_wmode-sd_prwlt(a2),d1 is this in the current mode?
	beq.s	ptc_mktp		 yes, make it top
	move.l	(a2),d0 		 no, next one down
	bne.s	ptc_mktl		 there is one, try it
*
ptc_mktp
	move.l	a0,d1			save closed cdb
	lea	-sd_prwlt(a2),a0	point to picked cdb
	jsr	pt_pick(pc)
	move.l	d1,a0
*
ptc_slck
	jsr	pt_slock(pc)		reset all the locks
*
* remove channel block and save area
*
ptc_rchp
	bsr.s	ptc_rmsv		remove any save area
	tst.b	sd_fmod(a4)		 ; fill?
	beq.s	ptc_rcdb		 ; ... no
	move.l	sd_fbuf(a4),a0		 ; yes, get rid of it
	bsr.s	pt_rmem
*
ptc_rcdb
	lea	-sd.extnl(a4),a0	remove channel from heap
	bsr.s	pt_rmem
*
	movem.l (sp)+,reglist
ptc_rts
	rts
*
*	Enter here to close unused window
*
pt_closu
pt_rmem
	move.w	mem.rchp,a2
	jmp	(a2)			just return the heap!
*
*	Remove the save area of a window: A4 points to its cdb
*
rmreg	reg	d1-d3/a0-a3
*


ptc_rmsv
	move.l	sd_wsave(a4),d0
	beq.s	ptc_rmex		no save area, just return
	tst.b	sd_mysav(a4)		save area, is it mine?
	beq.s	ptc_rmex		no, don't return it
*
	movem.l rmreg,-(sp)
	move.l	d0,a0			
	moveq	#sms.rchp,d0		yes, return it
	trap	#1
	movem.l (sp)+,rmreg
*
ptc_rmex
	rts
*
	end
