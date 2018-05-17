*	Check window overlaps, locking any that require it and unlocking
*	those that should not be locked.
*
*	Registers:
*		Entry				Exit
*	D3	0 if want to save locked	preserved
*		1 if lock all
*		-1 if not saving locked
*	A3	pointer to dddb 		preserved
*
	section driver
*
	include dev8_keys_sys
	include 'dev8_keys_con'
*
	xref	pt_tstov
	xref	pt_wsave
*
	xdef	pt_wchka
*
reglist reg	d4/a4
*
pt_wchka
	movem.l reglist,-(sp)
	lea	pt_tail-sd_prwlb(a3),a1 ; point to tail
	sub.l	a2,a2
wca_chlp
	move.l	sd_prwlb(a1),d0 	; get next window up
	beq.s	wca_clpe		; no more, finished checks
	lea	-sd_prwlb(a2,d0.l),a1	; point to its cdb
	tst.b	sd_wlstt(a1)		; unlockable?
	bgt.s	wca_chlp		; ...yes
	bsr.s	pt_wchk1		; check it
	bne.s	wca_exe 		; ...oops
	bra.s	wca_chlp
wca_clpe
	moveq	#0,d0			; no problems
wca_exe
	movem.l (sp)+,reglist
	rts
*
*	Given a current window, check that it is locked correctly.
*	If its mode is not the current screen mode, or it is overlapped,
*	then it should be locked, otherwise unlocked.
*
*	Registers:
*		Entry				Exit
*	D0					error code
*	D1/D2					smashed
*	D3	as for WCHKA			preserved
*	D4-D7					smashed
*	A0					smashed
*	A1	cdb of window to check		cdb of its primary
*	A2					0
*
pt_wchk1
	tst.b	sd_prwin(a1)		; is this window a primary?
	blt.s	wc1_chkl		; yes, OK then
	move.l	sd_pprwn(a1),a1 	; get its primary
	add.w	#sd.extnl,a1		; point to cdb
*
wc1_chkl
	tst.b	d3			; locking all?
	bgt.s	wc1_lock		; yes, lock it if unlocked
*
wc1_doch
	move.b	pt_dmode(a3),d0
	cmp.b	sd_wmode(a1),d0 	; is the mode correct?
	bne.s	wc1_lock		; no, lock it
*
	tst.b	sd_prwin(a1)		; is this window a primary?
	blt.s	wc1_chko		; yes, OK then
	move.l	sd_pprwn(a1),a1 	; get its primary
	add.w	#sd.extnl,a1		; point to cdb
*
wc1_chko
	move.l	sd_xouto(a1),d0 	; check if within screen
	add.l	sd_xouts(a1),d0
	cmp.w	pt_yscrs(a3),d0 	; too low?
	bgt.s	wc1_nolock		; ... yes, mark unlockable
	cmp.l	pt_xscrs(a3),d0 	; too large?
	ble.s	wc1_chkow		; ... no
wc1_nolock
	move.b	#sd.wnolk,sd_wlstt(a1)
	st	sd_wmode(a1)		; and stupid mode
	bra.s	wc1_ex0

wc1_chkow
******	movem.l sd_xhits(a1),d1/d2	; compare our hit area with others
	movem.l sd_xouts(a1),d1/d2   **** compare outlines for safety's sake ***
	add.l	d2,d1			; get edges
	move.l	a1,a0			; start with window above us
	sub.l	a2,a2
wc1_lp
	move.l	sd_prwlb(a0),d0 	; get next window up
	beq.s	wc1_unlk		; no more, must unlock
	lea	-sd_prwlb(a2,d0.l),a0	; point to its cdb
	tst.b	sd_wlstt(a0)		; unlockable?
	bgt.s	wc1_lp			; ...yes
	move.b	pt_dmode(a3),d0
	cmp.b	sd_wmode(a0),d0 	; should it be active?
	bne.s	wc1_lp			; no, it's in the wrong mode
*
	jsr	pt_tstov(pc)		; does it overlap us?
	bne.s	wc1_lp			; no, carry on
*
*	Come here if we are to be locked by a window in the correct
*	mode and overlapping us.  If we are already locked, then we
*	need do nothing, otherwise we must attempt to save ourselves.
*
wc1_lock
	tst.b	sd_wlstt(a1)		; are we already locked?
	bne.s	wc1_ex0 		; yes (or unlockable), all is OK then
*
	tst.b	d3			; are we allowed to save?
	bmi.s	wc1_nosv		; no, don't try
	move.l	a1,a0			; point to window
	moveq	#0,d1			; no supplied save area
	jsr	pt_wsave(pc)		; save the window
	bne.s	wc1_exe 		; couldn't do it, return the error
wc1_nosv
	move.b	#sd.slock,sd_wlstt(a1)	; flag "to be locked"
	bra.s	wc1_ex0 		; and exit OK
*
*	Come here if we are not locked by another window.  If we are
*	already unlocked, then there's nothing to do, otherwise we should
*	flag ourselves "to be unlocked".  We will be restored when unlocked,
*	as this will be called repeatedly to set all windows, but fail, and
*	we don't want to smash the screen under those circumstances.
*
wc1_unlk
	tst.b	sd_wlstt(a1)		; locked?
	beq.s	wc1_ex0 		; no, OK then
	move.b	#sd.sunlk,sd_wlstt(a1)	; flag "to be unlocked"
*
wc1_ex0
	moveq	#0,d0
wc1_exe
	rts
*
	end
