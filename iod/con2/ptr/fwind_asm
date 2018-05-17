* Find window enclosing pointer  V1.07	   1986  Tony Tebby   QJUMP
*					    2003  Marcel Kilgus
*
* 2003-02-17  1.07  Adapted for new system sprite routines (MK)
*
* This routine finds the current window (or sub-window) and fills in the
* window record in the pointer linkage area.
* It also determines the sprite to be used.
*
	section driver
*
	xdef	pt_fwind
*
	xref	sp_zero
	xref	pt_limit
	xref	pt_ssref
	xref	pt_cchloc
*
	include dev8_keys_qdos_io
	include dev8_keys_qdos_pt
	include dev8_keys_err
	include dev8_keys_sys
	include dev8_keys_qu
	include dev8_keys_chn
	include dev8_keys_con
	include dev8_keys_sysspr
*
*	d1   sp pointer coordinates
*	d2  r	sprite pointer for (sub-)window (the cached version is in pt_psprt)
*	d3   sp sub-window number of enclosing (sub-)window
*	d4   sp sub-window size
*	d5   sp sub-window origin 
*	d6   sp main window origin
*	d7   sp msbit set if cursor keys suppressed, lsbyte mode of top window
*	a1  r	pointer to cached sprite
*	a2   sp pointer to channel block
*	a3 c  p linkage block pointer
*
reglist reg	d1/d3/d4/d5/d6/d7/a0/a2
pt_fwind
	movem.l reglist,-(sp)		save registers
	move.l	pt_npos(a3),d1		get new position
ptf_retry
	moveq	#-1,d2			set no window
	moveq	#-1,d3			set no sub-window
	moveq	#0,d7			set no cursor key suppression
	clr.b	pt_ckeyw(a3)
*
* first scan all windows in the mode of the top window
*
ptf_scan
	lea	pt_head-sd_prwlt(a3),a1 go through all primaries
	move.l	sd_prwlt(a1),a2
	addq.l	#-sd_prwlt,a2		set channel address
	move.b	sd_wmode(a2),d7 	primary window mode
	move.b	sd_wmove(a2),pt_wmove(a3) window move/query
	bpl.s	pfw_pwstart		... not move
*
pfw_pwloop
	move.l	sd_prwlt(a2),a2 	next channel
	move.l	a2,d0			end of linked list?
	beq.s	pfw_wm			... yes, check for other mode windows
	addq.l	#-sd_prwlt,a2		set channel block address
pfw_pwstart
	cmp.b	sd_wmode(a2),d7 	correct mode?
	bne.s	pfw_pwloop		... no
	tst.b	sd_wlstt(a2)		unlockable?
	bgt.s	pfw_pwloop		... yes
	btst	#sd..botm,sd_behav(a2)	pickable?
	bne.s	pfw_pwloop		$$$ wallpaper
	movem.l sd_xhits(a2),d4/d5	get hit area
	bsr.l	pfw_look		is it in?
	bne.s	pfw_pwloop		... no, look at next
	bra.s	pfw_pfnd		... yes, primary found
*
* now scan all windows regardless of mode
*
pfw_wm
	tst.b	pt_wmove(a3)		query, only this mode please
	bmi.l	pfw_nf
*
	lea	pt_head-sd_prwlt(a3),a2 go through all primaries
pfw_pwmloop
	move.l	sd_prwlt(a2),a2 	next channel
	move.l	a2,d0			end of linked list?
	beq.l	pfw_nf			... yes
	addq.l	#-sd_prwlt,a2		set channel block address
	tst.b	sd_wlstt(a2)		unlockable, therefore invisible
	bgt.s	pfw_pwmloop		... yes
	btst	#sd..botm,sd_behav(a2)	pickable?
	bne.s	pfw_pwmloop		$$$ wallpaper
	movem.l sd_xhits(a2),d4/d5	get hit area
	bsr.l	pfw_look		is it in?
	bne.s	pfw_pwmloop		... no, look at next
*
* we've found the primary, now look at the individual window areas
*
pfw_pfnd
	moveq	#0,d2			set default sprite
	btst	#sd..well,sd_behav(a2)	check behaviour
	bne.s	pfw_manag		... it's managed
*
pfw_unman
	move.l	sd_xsize(a2),d4 	... unmanaged window, check area
	move.l	sd_xmin(a2),d5
	bsr.l	pfw_look		check area
	beq.l	pfw_chid		... found
*
	move.l	sd_sewll(a2),d0 	any (more) secondaries?
	beq.s	pfw_ibrd		... no, in the border
	move.l	d0,a2
	lea	-sd_sewll(a2),a2	NOP!!!!
	bra.s	pfw_unman
*
* managed window - first check the secondaries
*
pfw_manag
	move.l	a2,a1
pfw_msec
	move.l	sd_sewll(a1),d0 	any (more secondaries?)
	beq.s	pfw_cwdf		... no check the primary working defn
	move.l	d0,a1
	lea	-sd_sewll(a1),a1	NOP!!!!
	btst	#sd..well,sd_behav(a1)	is the secondary well-behaved?
	beq.s	pfw_msec		no, ignore it then
	movem.l sd_xhits(a1),d4/d5
	bsr.l	pfw_look
	bne.s	pfw_osec		outside secondary: use busy sprite
*
	move.l	a1,a2			use secondary
*
pfw_cwdf
	move.l	sd_wwdef(a2),d0 	is there a sub-window definition list?
	beq.s	pfw_chid		... no
	move.l	d0,a1
	move.l	(a1)+,a0		pointer to list
	move.l	sw_xorg(a1),d6		save main window origin
	move.l	sw_psprt(a1),d2 	set default sprite pointer
	move.l	a0,d0			is there a list?
	beq.s	pfw_cmdef		... no, check main window definition
pfw_csw
	move.l	(a0)+,d0		end of list?
	beq.s	pfw_cmdef		... yes
	addq.l	#1,d3			next sub-window
	move.l	d0,a1
	movem.l sw_xsize(a1),d4/d5	sub-window definition
	add.l	d6,d5			(absolute)
	bsr.l	pfw_look
	bne.s	pfw_csw
	move.l	sw_psprt(a1),d0 	set pointer sprite
	beq.s	pfw_ckey		... none
	move.l	d0,d2
	bra.s	pfw_ckey
*
* pointer not in any sub-window, check main window
*
pfw_cmdef
	moveq	#-1,d3			reset sub-window number
	move.l	sd_wwdef(a2),a1 	point to definition 
	addq.l	#4,a1
	movem.l sw_xsize(a1),d4/d5
	bsr.l	pfw_look		check main window
	beq.s	pfw_ckey		... ok
	bra.s	pfw_ibrd		... no, it's in the border
*
* pointer not in window, set no window
*
pfw_nf
	sub.l	a2,a2			not in any window
pfw_ibrd
	lea	pt_rec(a3),a1		fill in empty record
	moveq	#-1,d0
	move.l	d0,(a1)+		channel ID
	move.w	d0,(a1)+		sub-window number
	move.l	d0,(a1)+		pointer
	move.l	a2,pt_cchad(a3) 	set channel address
	bra.s	pt_csprt		now set the sprite
*
* pointer in window / sub-window list set cursor key flag
*
pfw_ckey
	btst	#sw..kfl,sw_kflg(a1)	cursor key flag?
	beq.s	pfw_chid		... no
	bset	#31,d7			set it
	bra.s	pfw_chid
*
* pointer outside most recent secondary
*
pfw_osec
	move.w	#sp.busy,d2		use busy sprite
*
* pointer in window, find channel ID
*
pfw_chid
	lea	-sd.extnl(a2),a0	pointer to true channel block
	move.l	sys_chtb(a6),a1 	channel table
	moveq	#-1,d0			channel number
pfw_chlook
	cmp.l	(a1)+,a0		our channel
	dbeq	d0,pfw_chlook		... no
	not.w	d0			 
*
	lea	pt_rec(a3),a1		fill in pointer record
	move.w	chn_tag(a0),(a1)+	channel id
	move.w	d0,(a1)+		
	move.w	d3,(a1)+		sub-window number
	sub.l	d5,d1			relative pointer posn
	move.l	d1,(a1)+
	clr.w	(a1)+			keystrokes
	clr.l	(a1)+			event vector
	move.l	d4,(a1)+		window definition
	move.l	d5,(a1)+
	move.l	a2,(a1)+		channel address
*
* check the sprite mode, and set pointer
*
pt_csprt
	moveq	#0,d4			status normal
*
	tst.b	sys_dfrz(a6)		; whole screen locked?
	bne.s	ptc_lock		; yes, show the lock
	move.b	pt_wmove(a3),d0 	window move/query?
	bmi.s	ptc_wmov		... yes
	tst.l	d2			in window?
	blt	ptc_null		... no, not in window
	move.l	a2,a0			find primary
	tst.b	sd_prwin(a2)		us?
	bne.s	ptc_wmode		... yes
	move.l	sd_pprwn(a2),a0 	... no, find it
	add.w	#sd.extnl,a0
ptc_wmode
	cmp.b	sd_wmode(a0),d7 	primary in correct mode?
	bne.s	ptc_mode		... no
*
	tst.b	sd_wlstt(a2)		is window locked?
	bne.s	ptc_lock		... yes
*
	bsr.l	pt_chekb		check for busy
	beq.s	ptc_busy		... yes
	blt.s	ptc_keyin		... waiting for kbd
****
****	tst.b	sd_wlstt(a2)		is window locked?
****	bne.s	ptc_lock
*		  
	tst.l	d7			disable cursor keys?
	smi	pt_ckeyw(a3)		... yes or no
	move.l	d2,a1
	tst.l	d2			any sprite defined?
	bne.s	ptc_sset

ptc_default
	move.w	#sp.arrow,a1
	bra.s	ptc_sset
ptc_wmov
	lsr.b	#1,d0			lsb set?
	bcc.s	ptc_wsiz		... no, is it change size?
	move.w	#sp.wmovep,a1		window move
	bra.s	ptc_sset
ptc_wsiz
	move.w	#sp.wsizep,a1		window size
	lsr.b	#1,d0			was it?
	bcs.s	ptc_sset		... yes
ptc_wzer
	lea	sp_zero(pc),a1		... no, try zero sprite
	lsr.b	#1,d0			was it?
	bcs.s	ptc_sset		... yes
	move.w	#sp.null,a1		... no, use null
	bra.s	ptc_sset

ptc_lock
	bclr	#sw..kfl,pt_ckey(a3)	set no cursor key suppression
	move.w	#sp.lock,a1		locked
	bra.s	ptc_cqu
ptc_mode
	move.w	#sp.mode,a1		wrong mode
	moveq	#1,d4			status wrong mode
	bra.s	ptc_cqu
ptc_busy
	move.w	#sp.busy,a1		busy
	bra.s	ptc_cqu
ptc_keyin
	move.w	#sp.key,a1		keyboard
	moveq	#-1,d4			status keyboard
	bra.s	ptc_cqu
ptc_null
	move.w	#sp.null,a1		null
ptc_cqu
	move.l	pt_dumq1(a3),a0
	move.l	qu_nexto+sd.dq2(a0),qu_nexti+sd.dq2(a0) ; clear dummy2
*
ptc_sset
	jsr	pt_ssref
	moveq	#-1,d0
	jsr	pt_cchloc
*
ptc_dset
	cmp.l	pt_pspck(a3),d2 	check the sprite
	beq.s	ptc_limit		... it the same
	clr.b	pt_offscr(a3)		... it has changed,
*
ptc_limit
	move.b	d0,pt_mvers(a3) 	max value of SVERS for this version
*
	move.b	d4,pt_cwstt(a3) 	set current window status
	bsr.l	pt_limit		limit sprite to within display
	bne.l	ptf_retry		... outside, try again
	movem.l (sp)+,reglist
	rts
	page
*
* check d1 position against d4/d5
*
pfw_look
	move.l	d1,d0			use temporary pointer
	sub.w	d5,d0			off top?
	blt.s	pfw_or
	sub.w	d4,d0			off bottom?
	bhs.s	pfw_or
	move.l	d1,d0			start again
	sub.l	d5,d0			off left?
	blt.s	pfw_or
	move.w	d4,d0			ensure no borrow
	sub.l	d4,d0			off right?
	bhs.s	pfw_or
	moveq	#0,d0			... in window
	rts
pfw_or
	moveq	#err.orng,d0		... out of window
	rts
	page
*
* check channel (a2) for busy on primary (a0) or any secondary
* returns LS wait kbd, EQ busy, HI wait pointer or not busy yet
*
pt_chekb
ptc_cbusy
	tst.b	chn_stat-sd.extnl(a0)	waiting?
	bne.s	ptc_wait		... yes
	move.l	sd_sewll(a0),d0 	... no, try next secondary
	beq.s	ptc_isbusy		... no more secondaries, is busy
	move.l	d0,a0
	lea	sd_sewll(a0),a0 	... NOP !!!!
	bra.s	ptc_cbusy
*
ptc_wait
	cmp.b	#iob.elin+1,chn_actn-sd.extnl(a0) check action
	bls.s	ptcb_nbsy		... waiting for kbd
	cmp.b	#iop.rptr,chn_actn-sd.extnl(a0) pointer?
	beq.s	ptcb_rdy		... ready
*
ptc_isbusy
	cmp.b	#pt.cwbsy,pt_cwbsy(a3)	busy for long enough?
	beq.s	ptcb_rts		... yes
	addq.b	#1,pt_cwbsy(a3) 	... one more, returns not busy
	rts
*
ptcb_rdy
	moveq	#1,d0			set not busy, not keyboard
ptcb_nbsy
	sf	pt_cwbsy(a3)		current window not busy
ptcb_rts
	rts
	end
