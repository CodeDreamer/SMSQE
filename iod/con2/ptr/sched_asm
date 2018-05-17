* Pointer scheduler loop routine  V1.13    1985  Tony Tebby	   QJUMP
*					   1986  Jonathan Oakley  QJUMP
*					   2006  Marcel Kilgus
*
* 2005-11-08  1.12  Free a0 for use in pt_sadd, call bg-refresh (MK)
* 2006-05-15  1.13  New CTRL+C behaviour (MK)
*
	section driver
*
	xdef	pt_sched
	xdef	pt_setq
	xdef	pt_pbyte
*
	xref	pt_sadd
	xref	sp_move
	xref	pt_hides
	xref	sp_draw
	xref	pt_fwind
	xref	pt_pick
	xref	pt_markp
	xref	pt_cptr
	xref	kbd_keyrow
	xref	pt_bgrefresh

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
*
pt_sched
	tst.l	pt_head(a3)		; any windows at all?
	beq.l	pts_rts

	jsr	pt_bgrefresh		; refresh buried windows if necessary

	tst.w	pt_swwin(a3)
	bmi.s	pts_checkswitch 	; -ve if old behaviour is wanted
	moveq	#7,d1			; keyrow with left CTRL key
	jsr	kbd_keyrow
	moveq	#%00000010,d6		; left CTRL key
	and.b	d1,d6
	moveq	#8,d1			; keyrow with right CTRL key
	jsr	kbd_keyrow
	andi.b	#%00000001,d1		; right CTRL key
	or.b	d6,d1
	bne.s	pts_checkswitch 	; ... one of them is still pressed

	clr.w	pt_swwin(a3)		; start new window cycle on next switch
*
*	Check to see if there has been a Ctrl-C recently: if there
*	was, then we'll have SYS_CKYQ pointing to dummy queue 1
*
pts_checkswitch
	move.l	pt_dumq1(a3),a1 	; point to dummy queue 1
	cmp.l	sys_ckyq(a6),a1 	; is dummy 1 current?
	bne.s	pts_chkp		; no, check pointer
;;	  tst.b   pt_pstat(a3)		  ; is the pointer suppressed?
;;	  ble.s   pts_pkbt		  ; no, just pick bottom
;;	  move.b  #pt.supcc,pt_pstat(a3)  ; make visible some time in future
;;pts_pkbt

	move.w	pt_swwin(a3),d1
	bmi.s	pts_tradctrlc		; -ve if old behaviour is wanted

; new CTRL+C code: pick windows from top to bottom until CTRL is released
	move.l	pt_head(a3),a0
pts_swloop
	move.l	(a0),d0 		; next in pile
	beq.s	pts_tradctrlc		; no more, keep picking bottommost
	move.l	d0,a0
	btst	#sd..botm,sd_behav-sd_prwlt(a0)   ; $$$ wallpaper?
	bne.s	pts_swloop		; ... yes, skip over it
	subq.w	#1,d1
	bcc.s	pts_swloop

	addq.w	#1,pt_swwin(a3)
	bra.s	pts_switch

; traditional CTRL+C code: pick lowest window in pile
pts_tradctrlc
	move.l	pt_tail(a3),a0		; end of pile, pick lowest window
	btst	#sd..botm,sd_behav-sd_prwlb(a0)   ; $$$ wallpaper?
	beq.s	pts_cdbbot		; ... no
	move.l	(a0),a0
pts_cdbbot
	addq.l	#sd_prwlt-sd_prwlb,a0	; now in top-down list
pts_switch
	lea	-sd_prwlt(a0),a0	; ...cdb of window
	bsr.l	pts_pkdo		; pick it
	jmp	pt_cptr(pc)		; and center pointer in it
	page

pts_shift dc.b	0,0,0,0,0,1,1,2,2,3,3	; speed 10(not official) to 0
pts_accbt dc.b	0,0,1,2,4,2,4,2,4,2,4
*
*	Check to see if pointer has moved or requires to be created
*
pts_chkp
	tst.b	d3			has there been a poll?
	beq.l	pts_nmove		... no
	add.b	d3,pt_svers(a3) 	... yes, sprite may change
	move.b	pt_bpoll(a3),pt_bcurr(a3); get poll value of button press

	move.w	pt_xicnt(a3),d6
	beq.s	pts_kprtm

	sub.w	d6,pt_xicnt(a3)

	moveq	#0,d7
	move.w	pt_xaccl(a3),d7 	 ; accelerator
	cmp.w	pt_yaccl(a3),d7 	 ; ensure that they are the same
	beq.s	pts_saccl		 ; ... they are
	blt.s	pts_syaccl
	move.w	pt_yaccl(a3),d7
	move.w	d7,pt_xaccl(a3) 	 ; keep the smaller accel
pts_syaccl
	move.w	d7,pt_yaccl(a3)

pts_saccl
	move.b	pts_shift(pc,d7.w),d1	 ; shift
	move.b	pts_accbt(pc,d7.w),d7	 ; accelerate
	swap	d7
	move.w	d1,d7			 ; saved

	subq.w	#1,d6			 ; 0 ... N
	lsr.w	d7,d6			 ; scale the number of incs
	addq.w	#1,d6

	subq.b	#1,d3			 ; any missing polls?
	beq.s	pts_kprtm		 ; ... no
	moveq	#1,d6			 ; no acceleration

pts_kprtm
	tst.b	pt_kprtm(a3)		 ; keypress timed out?
	ble.s	pts_relax		 ; ... yes
	subq.b	#1,pt_kprtm(a3)
pts_relax
	tst.b	pt_reltm(a3)		 ; pointer relaxed?
	beq.s	pts_cstat		 ; ... yes
	blt.s	pts_relu
	subq.b	#1,pt_reltm(a3) 	 ; relax a bit
	bra.s	pts_cstat		 ; carry on

pts_relu
	addq.b	#1,pt_reltm(a3)
pts_cstat
	move.b	pt_pstat(a3),d0 	 ; pointer visible?
	beq.s	pts_keys		 ; ... yes
	bgt.l	pts_supp		 ; ... no, suppressed
	addq.b	#1,d0
	beq.s	pts_crsp		 ; ... create sprite
	subq.b	#1,pt_pstat(a3) 	 ; get ready to suppress it
	bvc.s	pts_keys		 ; ... carry on
	moveq	#1,d0			 ; ... suppress now
	bra.l	pts_supp

pts_crsp
	move.b	d0,pt_pstat(a3) 	... no, but ready now
	bsr.l	pts_create		create new sprite
*
pts_keys
	moveq	#0,d4
	tst.b	pt_ckeyw(a3)		any reason to read cursor keys at all?
	bne.s	pts_kmchk
	moveq	#1,d1			cursor keyrow
	jsr	kbd_keyrow

	moveq	#key.htdo,d4		 ; just hit and do
	tst.b	pt_ckey(a3)		 ; cursor keys?
	bne.s	pts_skrw		 ; ... no
;	 tst.b	 pt_pstat(a3)		  ; pointer suppressed?
;	 bmi.s	 pts_skrw		  ; ... yes
	moveq	#$ffffff00+key.htdo+key.curs,d4 ; accept hit, do and cursor

pts_skrw
	and.b	d1,d4			 ; save keyrow
	moveq	#14,d1			 ; keypad row
	jsr	kbd_keyrow
	lsr.b	#1,d1			 ; bit 1->0
	and.b	#1,d1
	or.b	d1,d4			 ; add in keypad enter
	beq.s	pts_kmchk		 ; nought
*
	moveq	#7,d1			 ; read the control keys
	jsr	kbd_keyrow
	and.b	#key.ctrl,d1		 ; just them
	seq	d1
	and.b	d1,d4			 ; and blat out all cursor row if any
*
	bclr	#key..hit,d4		 ; is space depressed?
	beq.s	pts_enter		 ; ... no
	move.b	#k.hit,pt_bcurr(a3)	 ; set button to hit (1)
	bra.s	pts_kmchk
pts_enter
	bclr	#key..do,d4		 ; is enter depressed?
	beq.s	pts_kmchk		 ; ... no
	move.b	#k.do,pt_bcurr(a3)	 ; set button to do (2)

pts_kmchk
;;	  tst.b   pt_freez(a3)		   ; frozen?
;;	  beq.s   pts_kmove		   ; ... no
;;	  subq.b  #1,pt_freez(a3)
;;	  clr.l   pt_inc(a3)
;;	  bra.s   pts_mchk

pts_kmove
	movem.w pt_npos(a3),d2/d3	get position
	moveq	#-1,d5			mask of movements
*
	tst.b	d4			cursor keys?
	bne.s	pts_accel		... some, true cursor
*
pts_decel
	clr.w	pt_accel(a3)		... none, reset minimum speed
	clr.b	pt_keyrw(a3)
	bra.s	pts_mouse
*
pts_accel
	moveq	#0,d6			no movement
	clr.w	pt_xicnt(a3)
	clr.l	pt_inc(a3)
	moveq	#0,d0			accelerator
	move.w	pt_accel(a3),d0
	cmp.b	pt_keyrw(a3),d4 	same row as before?
	beq.s	pts_keyr		... yes
	lsr.w	#1,d0			slow down
pts_keyr
	move.b	d4,pt_keyrw(a3) 	set keyrow read
	addq.w	#1,d0			accelerate a bit
	move.w	d0,pt_accel(a3)
	divu	pt_kaccl(a3),d0 	reduce by accelerator
	addq.w	#1,d0
*
	lsl.b	#1,d4			bit 7
	bcc.s	pts_right
	add.w	d0,d3
	bclr	#pt..otop,d5		clear edge bit
pts_right
	lsl.b	#3,d4			bit 4
	bcc.s	pts_up
	add.w	d0,d2
	bclr	#pt..oleft,d5		clear edge bit
pts_up
	lsl.b	#2,d4			bit 2
	bcc.s	pts_left
	sub.w	d0,d3
	bclr	#pt..obot,d5		clear edge bit
pts_left
	lsl.b	#1,d4			bit 1
	bcc.s	pts_ckps
	sub.w	d0,d2
	bclr	#pt..oright,d5		clear edge bit
	bra.s	pts_ckps		and set position
*
pts_mouse
	tst.w	d6			 ; any move?
	beq.s	pts_ckps		 ; ... no

	lsl.w	d7,d2			 ; shift the position
	lsl.w	d7,d3
	add.w	pt_pfrx(a3),d2		 ; add fraction
	add.w	pt_pfry(a3),d3		 ; add fraction
	move.l	pt_inc(a3),d1		 ; get mouse increments
	sub.l	d1,pt_inc(a3)
*
	swap	d7			 ; accelerator
	move.w	d7,d4
	add.w	d6,d4			 ; scaled number of interrupts
	muls	d1,d4			 ; multiplied up
	beq.s	pts_setny
	bgt.s	pts_clrtop
	bclr	#pt..obot,d5		 ; clear off bottom
	bra.s	pts_setny
pts_clrtop
	bclr	#pt..otop,d5		 ; clear off top
pts_setny
	addq.w	#1,d7
	divs	d7,d4			 ; ... and reduced
	subq.w	#1,d7
	add.w	d4,d3
	bge.s	pts_msx
	moveq	#0,d3
pts_msx
	swap	d1
	move.w	d7,d4
	add.w	d6,d4			 ; scaled number of interrupts
	muls	d1,d4			 ; multiplied up
	beq.s	pts_setnx
	bgt.s	pts_clrleft
	bclr	#pt..oright,d5		 ; clear off right
	bra.s	pts_setnx
pts_clrleft
	bclr	#pt..oleft,d5		 ; clear off left
pts_setnx
	addq.w	#1,d7
	divs	d7,d4			 ; ... and reduced
	add.w	d4,d2
	bge.s	pts_rspos
	moveq	#0,d2
pts_rspos
	swap	d7			 ; get shift back
	moveq	#-1,d4
	lsl.w	d7,d4
	not.w	d4
	move.w	d4,d1
	and.w	d2,d1			 ; factional x
	and.w	d3,d4			 ; fractional y
	lsr.w	d7,d2			 ; x
	lsr.w	d7,d3
	move.w	d1,pt_pfrx(a3)		 ; save fraction
	move.w	d4,pt_pfry(a3)		 ; save fraction

pts_ckps
	and.b	d5,pt_offscr(a3)	 ; clear offscreen bits
	tst.b	pt_ptlim(a3)		 ; limited movement?
	beq.s	pts_spos		 ; ... no
	lea	pt_minxy(a3),a0
	cmp.w	(a0)+,d2		 ; less than min x?
	bge.s	pts_miny
	move.w	-2(a0),d2
pts_miny
	cmp.w	(a0)+,d3		 ; less than min y?
	bge.s	pts_maxx
	move.w	-2(a0),d3
pts_maxx
	cmp.w	(a0)+,d2		 ; greater than max x?
	ble.s	pts_maxy
	move.w	-2(a0),d2
pts_maxy
	cmp.w	(a0)+,d3		 ; greater than max y?
	ble.s	pts_spos
	move.w	-2(a0),d3
*
pts_spos
	movem.w d2/d3,pt_npos(a3)	set current position
	bra.s	pt_smove
*
* no move, is scheduler required?
*
pts_nmove
	moveq	#0,d6			 ; no move
	tst.b	pt_schfg(a3)		 ; scheduler op required?
	bmi.l	pts_rts
*
* the pointer position has now been moved
*
pt_smove
pts_mchk
	move.l	pt_pos(a3),d1		get old position
	tst.b	pt_pstat(a3)		pointer visible?
	bne.l	pts_wake		... no, try wakup

***	tst.b	pt_schfg(a3)		check the scheduler flag
	st	pt_schfg(a3)		and say ready
***	beq.s	pts_fwind		... must redo the window checks
***	tst.b	pt_cwbsy(a3)		window busy?
***	bne.s	pts_fwind		... yes, check the sprite again
***	cmp.l	pt_npos(a3),d1		has it moved?
***	bne.s	pts_fwind		... yes
***	move.b	pt_mvers(a3),d0 	is the sprite dynamic?
***	beq.l	pts_sky 		... no
***	cmp.b	pt_svers(a3),d0 	yes, do we need to change it?
***	bhi.l	pts_sky 		... no, not yet
****pts_fwind
	bsr.l	pt_fwind		find current window
	move.l	pt_npos(a3),pt_pos(a3)
	move.l	pt_cchad(a3),d0 	 ; current channel address
	cmp.l	pt_ochad(a3),d0 	 ; changed?
	beq.s	pts_ckspr		 ; ... no
	move.l	d0,pt_ochad(a3) 	 ; ... yes, reset channel
	or.b	#ptb.psup,pt_bsupp(a3)	 ; and suppress keypress
pts_ckspr
	cmp.l	pt_pspck(a3),d2 	check the sprite
	bne.s	pts_recreate		... it has changed

	cmp.l	pt_npos(a3),d1		sprite the same, is the position the sam
	beq.l	pts_sky 		... yes
*
	tst.b	pt_reltm(a3)		pointer relaxed?
	ble.s	pts_move		... yes
	move.b	pt_relax(a3),pt_reltm(a3) ... no, keep it excited
pts_move
move.reg reg	a3/a5/a6
move.a3  equ	0
	movem.l move.reg,-(sp)		save vital regs
	move.l	pt_addr(a3),a2		set old screen address
	bsr.l	pt_sadd 		set addresses
	move.l	move.a3(sp),a0		restore linkage block address
	move.l	a3,pt_addr(a0)		and save new one
	bsr.l	sp_move 		and move it
	movem.l (sp)+,move.reg
	bra.l	pts_sky 		set keystroke, pick set queue
*
pts_recreate
	moveq	#0,d4			hide but keep visiblity flag set
	bsr.l	pt_hides		hide the old sprite
	bsr.l	pts_newsp		make new one
	bra.l	pts_sky 		and set key
*
* pointer suppressed, wake up or stuff kbd
*
pts_supp
	addq.b	#1,d0
	cmp.b	#pt.clrdq,d0		; clear dummy?
	bne.s	pts_clrky		; no
	move.l	pt_dumq1(a3),a2 	; point to dummy queue 1
	move.l	qu_nexto+sd.dq2(a2),qu_nexti+sd.dq2(a2) ; clear it
	moveq	#1,d0			; start again

pts_clrky
	move.b	d0,pt_pstat(a3) 	; new status

	or.b	#ptb.ssup,pt_bsupp(a3)	; suppress keystrokes
	sf	pt_ptlim(a3)		; and pointer limits

	move.b	pt_bpoll(a3),d3 	; button pressed?
	beq.l	pts_wakcr		; no
	bsr.l	pt_fwind		; yes, find window
	move.l	pt_npos(a3),pt_pos(a3)
	tst.b	pt_cwstt(a3)		; keyboard or busy?
	bgt.l	pts_exit		; ... no
*
* stuff the current keyboard queue
*
	move.l	sys_ckyq(a6),a2 	; look at current keyboard queue
	tst.b	pt_supcr(a3)		; already stuffing?
	blt.l	pts_exit		; ... suppressed
	bgt.s	ptss_cmov		; ... yes
	subq.b	#k.hit,d3		; ... no, is key "hit"?
	beq.s	ptss_cset		; yes, set cursor stuffing
*
	subq.b	#1,pt_supcr(a3) 	; suppress repeats
	tst.b	pt_bstat(a3)		; button already down?
	bne.s	ptss_cmov		; ... yes, suppress
	move.b	#ptb.pres,pt_bstat(a3)	; set status
	clr.b	sys_dfrz(a6)		; unfreeze screen
	moveq	#k.enter,d1		; and stuff an enter
	bsr.s	ptss_pbyt
	bra	pts_exit		; that's all folks
*
ptss_cset
	addq.b	#1,pt_supcr(a3) 	; now stuffing
	move.l	qu_nexto(a2),qu_nexti(a2) ; clear queue
ptss_cmov
	move.l	pt_inc(a3),d5		; any movement?
	beq.l	pts_exit		; ... no
*
	moveq	#-1,d7			; set movement flag
	moveq	#2,d4
	add.b	pt_wake(a3),d4		; use wake up speed
	lsr.b	#1,d4			; halved
	moveq	#1,d3
	add.b	d4,d3			; halved again
	lsr.b	#1,d3
*
ptss_loop
	swap	d5
	tst.w	d5			; left or right?
	ble.s	ptss_lft		; ... left
	cmp.w	d3,d5			; enough increments?
	blt.s	ptss_nolr
	moveq	#k.right,d1		; right
	sub.w	d3,d5			; and reduce
	bra.s	ptss_px
ptss_lft
	beq.s	ptss_nolr
	moveq	#k.left,d1		; left
	add.w	d3,d5			; and reduce
	ble.s	ptss_px
	sub.w	d3,d5			; none left
*
ptss_nolr
	swap	d7
	clr.w	d7			; no x left
	swap	d7
	bra.s	ptss_updn
*
ptss_px
	bsr.s	ptss_pbyt		; put x/y cursor key
*
ptss_updn
	swap	d5
	tst.w	d5			; up or down
	beq.s	ptss_noud
	ble.s	ptss_up 		; ... up
	cmp.w	d4,d5			; enough increments left?
	blt.s	ptss_noud		; ... no
	moveq	#k.down,d1		; down
	sub.w	d4,d5			; and reduce
	bra.s	ptss_py
ptss_up
	moveq	#k.up,d1		; up
	add.w	d4,d5			; and reduce
	bgt.s	ptss_rsy
*
ptss_py
	bsr.s	ptss_pbyt		; send up/down
	bra.s	ptss_loop
*
ptss_rsy
	sub.w	d4,d5			; reset y
ptss_noud
	clr.w	d7			; no more y
	tst.l	d7
	bne.s	ptss_loop		; some more x
*
ptss_exit
	move.l	d5,pt_inc(a3)		; adjust increment
	bra.l	pts_rts
*
pt_pbyte
ptss_pbyt
	move.l	a3,-(sp)		; keep linkage block
	move.w	ioq.pbyt,a3		; put byte in queue
	jsr	(a3)
	move.l	(sp)+,a3
	rts

pts_wakcr
*
* wake cursor on fast movements with buttons up
*
pts_wake
	tst.b	pt_bcurr(a3)		; button or space/enter down?
	bne.l	pts_exit
pts_wakeck
	clr.w	pt_xicnt(a3)
	clr.l	pt_inc(a3)		; clear increments
	clr.b	pt_supcr(a3)		; clear suppress cursor stuffing flag
	clr.b	pt_bstat(a3)		; and button status
	cmp.b	pt_wake(a3),d6		; wake up?
	bls.l	pts_exit		; ... no
	tst.b	pt_reltm(a3)		; relaxed?
	blt.l	pts_exit		; ... very
	clr.b	pt_pstat(a3)		; set visible
	move.b	pt_relax(a3),pt_reltm(a3) set relaxation timer
	bsr.l	pts_create
	bra.l	pts_setq
	page
*
*	Do old/new key presses and
*	Check whether the window containing the pointer should be picked.
*
pts_sky
	move.b	pt_bcurr(a3),d1 	 ; new button pressed
	move.b	d1,d2
	beq.s	pts_kyup		 ; ... up

	tst.b	pt_bsupp(a3)		 ; suppressed
	beq.s	pts_kstk		 ; ... no, keep stroke
	bgt.s	pts_clst		 ; ... yes, clear stroke
	moveq	#0,d2			 ; ... and press!
	bra.s	pts_clst
pts_kstk
	tst.b	pt_bstat(a3)		 ; was up?
	beq.s	pts_pres		 ; ... yes
pts_clst
	clr.b	d1			 ; not a key stroke
pts_pres
	move.b	d2,pt_bpres(a3) 	 ; ... but it is a press
	move.b	#ptb.pres,pt_bstat(a3)	 ; ... and status
	tst.b	d1
	beq.s	pts_pkw 		 ; no key
	clr.b	sys_dfrz(a6)
	move.l	pt_dumq1(a3),a1 	 ; point to...
	lea	sd.dq2(a1),a2		 ; dummy queue 2
	cmp.l	sys_ckyq(a6),a2
	bne.s	pts_pkw
	bsr.l	ptss_pbyt		 ; add to queue
	bra.s	pts_pkw

pts_kyup
	move.b	pt_bstat(a3),d2 	 ; was up?
	bgt.s	pts_bounce		 ; ... no, bouncing now

	assert	pt_bstat,pt_bsupp-1,pt_bpres-2,pt_bcurr-3
	clr.l	pt_bstat(a3)		 ; no suppression, no press
	sf	pt_ptlim(a3)		 ; no limits
	st	pt_kprtm(a3)
	bra.s	pts_pkw

pts_bounce
	move.b	#ptb.bnce,pt_bstat(a3)	 ; set bounce status
*
pts_pkw
	tst.b	pt_wmove(a3)		are we doing a window move/query?
	blt.s	pts_setq		... yes, set the queue
	move.l	pt_cchad(a3),d0 	in a window?
	beq.s	pts_setq		... no, set the queue
	move.l	d0,a0			point to channel
	btst	#sd..prwn,sd_prwin(a0)	are we looking at a primary?
	bne.s	pts_ctop		yes, OK then
	move.l	sd_pprwn(a0),a0 	point to the primary
	add.w	#sd.extnl,a0		the real cdb
pts_ctop
	move.l	pt_head(a3),d0		look at head
	beq.s	pts_setq		there isn't one!
	move.l	d0,a2
	sub.w	#sd_prwlt,a2		and its real cdb
*
	tst.b	pt_cwstt(a3)		is it keyboard?
	blt.s	pts_cpick		... yes, pick on hit
*
	cmp.l	a0,a2			is picked window top already?
	beq.s	pts_setq		... yes

	tst.b	sd_wlstt(a0)		is it locked?
	beq.s	pts_pick		... no, pick it to top anyway
	bgt.s	pts_setq		... no, unpickable
pts_cpick
	tst.b	d1			 ; hit or do?
	beq.s	pts_setq		 ; ... no

pts_pick
	cmp.b	#k.do,d1		 ; do pick?
	bne.s	pts_pkdo		 ; ... no
	jsr	pt_markp		 ; ... yes, mark it picked
pts_pkdo
	clr.b	pt_reltm(a3)		 ; relax it
	move.l	pt_dumq1(a3),a1
	move.l	qu_nexto+sd.dq2(a1),qu_nexti+sd.dq2(a1) ; clear dummy queue 2
	or.b	#ptb.psup,pt_bsupp(a3)	 ; suppress presses
	clr.b	pt_bpres(a3)		 ; including this one
	st	pt_supcr(a3)		 ; suppress cursor emulation until keyup
	jsr	pt_pick(pc)		 ; and pick it
*
pt_setq
pts_setq
	move.l	pt_dumq1(a3),a1 	 ; point to...
	lea	sd.dq2(a1),a2		 ; ...dummy queue 2
	move.l	a2,sys_ckyq(a6) 	 ; with the current queue pointer
pts_exit
;xxx	    clr.l   pt_inc(a3)		    ; clear mouse increments
pts_rts
	rts
;
; keyrow defs
;
key.ctrl equ	%00000111
key..hit equ	6
key..do  equ	0
key.htdo equ	%01000001
key.curs equ	%10010110

	page
pts_create
	bsr.l	pt_fwind		; find new sprite for window
	move.l	pt_npos(a3),pt_pos(a3)
pts_newsp
	move.l	d2,pt_pspck(a3) 	; set check for current sprite
	move.l	a1,pt_psprt(a3) 	; and current sprite
newsp.reg reg	d6/d7/a3/a5/a6
newsp.a3  equ	8
	movem.l newsp.reg,-(sp)
	bsr.l	pt_sadd 		; set sprite addresses
	move.l	newsp.a3(sp),a0 	; restore linkage block address
	move.l	a3,pt_addr(a0)		; save new address
	bsr.l	sp_draw 		; draw sprite
	movem.l (sp)+,newsp.reg
	rts
	end
