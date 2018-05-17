* Pick a window to the top     1986  Tony Tebby   QJUMP
*
	section driver
*
	xdef	pt_pickw
*
	xref	pt_pick
	xref	pt_ptop
	xref	pt_plock
	xref	pt_cptr
	xref	pt_markp
*
	include dev8_keys_con
	include dev8_keys_err
	include dev8_keys_k
	include dev8_keys_chn
	include dev8_keys_qdos_pt
	include dev8_keys_qdos_io
*
*	d1 c s	(.w) < 0 pick bottom, unlock, freeze, else pick jobs primary
*	d2 c p	(.w) = k.wake, wake up
*
pt_pickw
	move.l	a0,-(sp)
	move.l	pt_tail(a3),a0		find the cdb of the bottom window
	btst	#sd..botm,sd_behav-sd_prwlb(a0)   ; $$$ wallpaper?
	beq.s	ptp_isbot		; ... no
	move.l	(a0),a0
ptp_isbot
	tst.w	d1			just get the bottom or unlock?
	blt.s	ptp_pbot		... yes
*
ptp_cjob
	cmp.l	chn_ownr-sd.extnl-sd_prwlb(a0),d1 the right job?
	beq.s	ptp_pick		... yes
	move.l	(a0),a0 		... no, next
	move.l	a0,d0			any more windows?
	bne.s	ptp_cjob		... yes, try again
	moveq	#err.ijob,d0
	bra.s	ptp_exit
*
ptp_pbot
	addq.w	#-iopp.bot,d1		really pick?
	bne.s	ptp_nolk		... no, no lock or freeze
ptp_pick
	lea	-sd_prwlb(a0),a0	get usual channel block base
	cmp.w	#k.wake,d2		wake up?
	bne.s	ptp_pkdo
	jsr	pt_markp		and say picked
ptp_pkdo
	clr.b	pt_schfg(a3)		make scheduler move pointer
	jsr	pt_pick(pc)		pick that window
	jsr	pt_cptr(pc)		centre the pointer in it
ptp_exit
	move.l	(sp)+,a0
	rts
*
ptp_nolk
	move.l	(sp),a0 		reset channel base
	addq.w	#iopp.bot-iopp.nlk,d1	unlock?
	bne.s	ptp_freeze		... no
	moveq	#sd.wnolk,d2		set no locks
	sub.l	a2,a2
	bsr.l	pt_plock
*
	bsr.l	pt_ptop 		repick top
	bra.s	ptp_exit

ptp_freeze
	addq.w	#iopp.nlk-iopp.frz,d1	freeze
	bne.s	ptp_ipar
	bset	#sd..frez,sd_behav(a0)
	moveq	#0,d0
	bra.s	ptp_exit

ptp_ipar
	moveq	#err.ipar,d0
	bra.s	ptp_exit
	end
