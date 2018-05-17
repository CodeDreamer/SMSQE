* Find pointer position  V1.07	  1985  Tony Tebby   QJUMP
*
	section driver
*
	xdef	pt_rptr
*
	xref	pt_pick
*
	include dev8_keys_con
	include dev8_keys_k
	include dev8_keys_err
	include dev8_keys_sys
	include dev8_keys_chn
	include dev8_keys_jcbq
	include dev8_keys_qlv
	include dev8_keys_qdos_pt
	include 'dev8_mac_assert'
*
pt_rptr
	move.l	d1,d5			save call coordinates
*
	tst.w	d3			first entry?
	bne.s	ptr_retry		... no

	move.l	sys_jbpt(a6),a5 	job pointer
	move.l	(a5),a5

	clr.l	pp_event(a1)		preset no event
	bra.s	ptr_do
*
ptr_retry
	move.w	chn_jbwt+2-sd.extnl(a0),d0 find job pointer
	lsl.w	#2,d0
	move.l	sys_jbtb(a6),a5
	move.l	(a5,d0.w),a5


ptr_do
	move.l	pp_event(a1),d4 	get previous event
*
	lea	pt_rec(a3),a2		get preset record
	moveq	#pp_end/4-1,d0
ptr_preset
	move.l	(a2)+,(a1)+		set return record
	dbra	d0,ptr_preset
*
	sub.w	#pp_end,a1
*
* check if action possible
*
	move.b	d2,sd_wmove(a0) 	is it special ptr read?
	bpl.s	ptr_pstat		... no
*
* special pointer read
*
	moveq	#pt.kystk,d2		return on keystroke
*
	tst.b	d3			first entry?
	bne.s	ptr_spsh		... no
	clr.b	pt_schfg(a3)		activate scheduler
ptr_spsh
	tst.b	pt_pstat(a3)		pointer visible?
	assert	pt.show,-1
	sne	pt_pstat(a3)		no, show the pointer next time !!!!!!!
	bne.s	rpt_nrd1
ptr_ctop
	tst.b	pt_wmove(a3)		is top job special read?
	bmi.s	pt_rptr2		... yes, carry on
	clr.b	pt_schfg(a3)		... no, activate scheduler
	bsr.l	pt_pick 		and pick us to top
rpt_nrd1
	bra.l	rpt_nrdy		and wait
*
* check window status
*
ptr_pstat
	tst.b	pt_wmove(a3)		is someone else doing special read?
	bmi.s	out_window		... yes, not really my window
	tst.b	pt_pstat(a3)		is sprite suppressed?
	beq.s	pt_rptr2		... visible pointer
;***	    blt.s   ptr_show		    ... show and read it
;***	    bgt.s   ptr_grab		    ... yes
;***	    cmp.l   pt_cchad(a3),a0	    is pointer read in our window?
;***	    beq.s   ptr_show		    ... yes, show it
;***	    bra.s   pt_rptr2		    ... no, just read it
;***
ptr_grab
	move.l	a0,a2
	btst	#sd..prwn,sd_prwin(a0)	is it primary window?
	bne.s	ptr_chkp		... yes
	move.l	sd_pprwn(a0),a2 	... no, find primary
	add.w	#sd.extnl,a2
ptr_chkp
	lea	sd_prwlt(a2),a2 	check if at top
	cmp.l	pt_head(a3),a2
	bne.s	pt_rptr2		... no
ptr_show
	assert	pt.show,-1
	st	pt_pstat(a3)		show the pointer next time
pt_rptr2
	tst.b	pt_schfg(a3)		scheduler done?
	beq.l	rpt_nrdy		... no
	page
*
* check offscreen
*
	tst.b	pt_offscr(a3)		off screen?
	beq.s	rpt_lock
	bset	#pt..ofsc,d4		... yes
	btst	#pt..ofsc,d2		is job interested in offscreen?
	bne.s	out_window		... yes
*
* check the lock
*
rpt_lock
	tst.b	sd_wlstt(a0)		is it locked?
	bmi.s	out_window		... yes
*
	cmp.l	pt_cchad(a3),a0 	check if pointer is in this window
	beq.s	in_window		... it is!!
*
out_window
	tst.b	sd_wmove(a0)		window move?
	bmi.s	in_window		... yes, everywhere is in window
*
;***	and.b	#pt.offsc+pt.move,d4	clear all but offscreen and move ***why?
	and.b	#pt.offsc+pt.inwin+pt.outwn+pt.move,d2	mask in window events
	bset	#pt..outw,d4		set out of window event
;***	    move.l  pt_pos(a3),d1	    and position
;***	    bra.l   rpt_done		    not in a window, do not look at key pres
	moveq	#-1,d1
	move.l	d1,pp_chid(a1)		clear window bits
	move.w	d1,pp_sbwnr(a1)
	move.l	d1,pp_xpos(a1)
	bra.l	rpt_exit		not in a window, do not look at key
*
in_window

	or.b	#pt.inwin+pt.keyup,d4	set normal events
*
	clr.b	pt_cwbsy(a3)		can't possibly be busy

	bclr	#sd.pick,sd_pick(a0)	just picked?
	beq.s	rpt_queue		... no
	moveq	#k.wake,d1		... yes, wake up!!
	bra.s	rpt_skey

rpt_queue
	move.l	pt_dumq1(a3),a2 	get next character from queue
	lea	sd.dq2(a2),a2
rpt_gbyt
	move.l	a3,-(sp)
	move.w	ioq.gbyt,a3
	jsr	(a3)
	move.l	(sp)+,a3
	blt.s	rpt_pres		... no key

	tst.b	pt_ckeyw(a3)		are space and enter normal?
	bne.s	rpt_keyo		... yes
	cmp.b	#k.enter,d1		... no, is it enter
	beq.s	rpt_gbyt
	cmp.b	#k.space,d1		is it space?
	beq.s	rpt_gbyt

rpt_keyo
	btst	#pt..outw,pp_pevnt(a1)	 ; have we been out of window?
	bne.s	rpt_gbyt		 ; ... yes strip queue

	tst.b	sd_wmove(a0)		special read?
	bpl.s	rpt_skey		... no, carry on
	cmp.b	#k.maxch,d1		odd character?
	bhi.s	rpt_pres		... yes, forget it

rpt_skey
	move.b	d1,pp_kstrk(a1) 	set keystroke read
	bset	#pt..kyst,d4		set keystroke event
*
rpt_pres
	move.b	pt_bpres(a3),pp_kpres(a1) key pressed
	bne.s	rpt_ispr		... there is

	move.b	pp_kstrk(a1),d1 	keystroke?
	beq.s	rpt_exit		... no
	cmp.b	#k.do,d1		hit or do?
	bhi.s	rpt_exit		... no
	move.b	d1,pp_kpres(a1) 	... yes, so must be pressed!!

rpt_ispr
	bclr	#pt..kyup,d4		... there is one, clear not depressed
*
	tst.b	pt_kprtm(a3)		auto repeat
	bgt.s	rpt_exit		... wait
	beq.s	rpt_arep		... set auto repeat time
	move.b	sys_rdel+1(a6),pt_kprtm(a3) set initial delay
	bra.s	rpt_kpev		set keypress event
rpt_arep
	move.b	sys_rtim+1(a6),pt_kprtm(a3) set auto repeat time
rpt_kpev
	bset	#pt..kypr,d4		set keypress event
*
rpt_exit
	move.l	pt_pos(a3),d1		set absolute position
	cmp.l	d1,d5			pointer moved?
	beq.s	rpt_extst		... no
	bset	#pt..move,d4		set move event has occurred
rpt_extst
	tst.b	pt_schfg(a3)		scheduler ready?
	bne.s	rpt_done		... yes
rpt_nrdy
	clr.w	d2			return on no event
;***	    tst.b   pt_pstat(a3)	    is pointer visible?
;***	    beq.s   rpt_done		    yes, no need to show it
;***	    assert  pt.show,-1
;***	    tst.b   sd_wlstt(a0)	     ; am I locked?
;***	    bmi.s   rpt_done		     ; ... yes
;***	    st	    pt_pstat(a3)	     ; ... no, show pointer next time
*
rpt_done
	move.l	d4,pp_event(a1) 	set event
	move.l	d2,d0			get msbs
	rol.l	#8,d0
	and.b	jcb_evts(a5),d0 	job event?
	beq.s	rpt_event
	eor.b	d0,jcb_evts(a5) 	clear events taken
	move.b	d0,pp_event(a1) 	more events set
	bra.s	exit_ok

rpt_event
	and.b	d4,d2			check events
	bne.s	exit_ok 		... event occurred
	moveq	#err.nc,d0
	rts
exit_ok
	btst	#pt..inwn,d4		in window?
	beq.s	exit_ok0		... no
	move.b	pt_relax(a3),d0 	make
	neg.b	d0			... very
	move.b	d0,pt_reltm(a3) 	... relaxed
exit_ok0
	bclr	#7,sd_wmove(a0) 	clear window move sprite flag
	beq.s	exit_ok1		... already clear
	clr.b	pt_schfg(a3)		... no, redo scheduler
exit_ok1
	moveq	#0,d0
	rts
	end
