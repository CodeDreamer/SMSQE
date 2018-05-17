* Pick window				v1.04   Sept 1986  J.R.Oakley	QJUMP
*
* 2011-03-02  1.04  Save a3 around sms.rchp, QDOS may smash that (MK)
*
*	Put a given window on top of the pile.	This routine is entered
*	from PT_PICKW, to bring the bottom window to the top,
*	from the scheduler loop when a Ctrl-C has changed the current
*	queue, from the pointer layer when a buried window has been hit,
*	or from the I/O routine when a window's size has changed.
*
*	Registers:
*		Entry				Exit
*	D0					error code
*	A0	pointer to cdb			preserved
*	A3	pointer to dddb 		preserved
*
	section driver
*
	include dev8_keys_con
	include dev8_keys_err
	include dev8_keys_sys
	include dev8_keys_qlv
	include dev8_keys_qdos_sms
*
	xdef	pt_pick
	xdef	pt_ptop
	xdef	pt_plock
*
	xref	pt_xmode
	xref	pt_wchka
	xref	pt_mrstj
* 
reglist reg	d1-d7/a0-a2/a4/a5
*
pt_ptop
	movem.l reglist,-(sp)
	moveq	#0,d3			; set locks
	jsr	pt_wchka(pc)		; check all
	moveq	#-1,d7			; no mode change
	bra.l	do_rest 		; and restore
*
pt_pick
	movem.l reglist,-(sp)
	move.l	a0,a4			; save cdb of picked window
	tst.b	sd_prwin(a4)		; is it actually a primary?
	blt.s	chk_top 		; yes, start picking
	move.l	sd_pprwn(a4),a4 	; no, point to...
	add.w	#sd.extnl,a4		; ...the primary's cdb
*
chk_top
	moveq	#-1,d7			; say mode change
	clr.w	d7
	move.b	pt_dmode(a3),d4 	; current mode
	cmp.b	sd_wmode(a4),d4 	; changed?
	bne.s	ptp_wchk		; yes, always redo checks
*
	moveq	#-1,d7			; no mode change
	move.l	pt_head(a3),a2		; look to see who's head
	lea	-sd_prwlt(a2),a2	; at the cdb, not the link
	moveq	#0,d0			; set no error if picking head
	cmp.l	a4,a2			; are we trying to pick head?
	beq.s	ex_pick 		; yes, don't need to relink or restore
*
*	First we must go through the list, from the top down, locking all
*	windows whose hit area is overlapped by our outline, or whose
*	mode differs from ours.
*	Note that windows cannot be unlocked by bringing another window
*	to the top, only by closing others or picking them.
*
ptp_wchk
	move.b	sd_wmode(a4),pt_dmode(a3) ; set new current mode
	move.l	sd_prwlb(a4),-(sp)	; save bottom up...
	move.l	sd_prwlt(a4),-(sp)	; ...and top down pointers
*
	bsr	ptp_mktp		; make the window top
*
	moveq	#0,d3			; save any windows we lock
	jsr	pt_wchka(pc)		; check and lock all
	bne.s	no_pick 		; ...oops, can't pick that one
	addq.l	#8,sp			; remove old pointers
*
	tst.w	d7			; mode change?
	bmi.s	do_rest 		; no, just restore
	move.l	pt_sbase(a3),a0 	; clear the screen
	move.l	pt_bytes(a3),d0
	subq.l	#1,d0
	lsr.l	#4,d0
	moveq	#0,d1
cls_loop
	move.l	d1,(a0)+		; clear the screen
	move.l	d1,(a0)+		; clear the screen
	move.l	d1,(a0)+		; clear the screen
	move.l	d1,(a0)+		; clear the screen
	dbra	d0,cls_loop

	move.b	pt_dmode(a3),d1 	; set the mode in hardware
	jsr	pt_xmode
*
do_rest
	jsr	pt_mrstj(pc)		; restore everything (with mode?)
	move.l	pt_dumq1(a3),a0 	; point to...
	lea	sd.dq2(a0),a0		; ...second dummy queue
	move.l	a0,sys_ckyq(a6) 	; keyboard queue is up for grabs 
*
ex_pickok
	moveq	#0,d0
ex_pick
	movem.l (sp)+,reglist		; and then the registers
	rts
*
*	Come here if we can't pick the required window.
*
no_pick
	move.b	d4,pt_dmode(a3) 	; reset to old mode
	bsr	ptp_unlk		; unlink it from top
	move.l	(sp)+,sd_prwlt(a4)	; put back the old links
	move.l	(sp)+,sd_prwlb(a4)
*
	lea	sd_prwlb(a4),a1 	; point to upward link
	move.l	(a1)+,a2		; who is above us?
	move.l	a2,d0
	bne.s	ptp_lnkt		; somebody, link to them
	lea	pt_head-4(a3),a2	; no-one, link to head
ptp_lnkt
	move.l	a1,4(a2)		; fill in top down link of window above
*
	move.l	(a1),a2 		; who is below us?
	subq.l	#4,a1
	move.l	a2,d0
	bne.s	ptp_lnkb		; someone, link to them
	lea	pt_tail+4(a3),a2	; no-one, link to tail
ptp_lnkb
	move.l	a1,-4(a2)		; fill in top down link of window above
*
*	and we own the save area then we should also return the memory.
*
	lea	pt_head-sd_prwlt(a3),a0 ; fix from top down
	sub.l	a2,a2
fix_loop
	move.l	sd_prwlt(a0),d0 	; next window
	beq.s	fix_end 		; isn't one
	lea	-sd_prwlt(a2,d0.l),a0	; point to cdb
	cmp.b	#sd.slock,sd_wlstt(a0)	; status to be locked?
	beq.s	fix_rhp 		; yes, reset unlock
	cmp.b	#sd.sunlk,sd_wlstt(a0)	; status to be unlocked?
	bne.s	fix_loop		; no change
*
fix_slock
	moveq	#sd.wlock,d2		; lock it
	bra.s	fix_dolk
*
fix_rhp
	tst.b	sd_mysav(a0)		; my save area?
	beq.s	fix_ulk 		; no, just unlock
	movem.l a0-a3,-(sp)
	move.l	sd_wsave(a0),a0 	; heap to return
	moveq	#sms.rchp,d0
	trap	#1			; return it
	movem.l (sp)+,a0-a3
fix_ulk
	moveq	#sd.wunlk,d2		; unlock
fix_dolk
	bsr.s	ptp_lock
	bra.s	fix_loop
*
fix_end
	moveq	#err.nc,d0		; not done
	bra	ex_pick
*
*	Set locks on primary and its secondaries.
*
*	Registers:
*		Entry				Exit
*	D0					smashed
*	D2	status to set			preserved
*	A0	window to set			preserved
*	A1					smashed
*	A2	0
*
pt_plock
ptp_lock
	move.l	a0,a1			; smashable copy of window
lk_loop
	move.b	d2,sd_wlstt(a1) 	; set a lock
	move.l	sd_sewll(a1),d0 	; next
	beq.s	lk_end			; isn't one
	lea	-sd_sewll(a2,d0.l),a1	; point to it
	bra.s	lk_loop
lk_end
	rts
*
*	Re-link a primary window into the linked lists of primaries, so that it
*	becomes the top window.
*
*	Registers:
*		Entry				Exit
*	D1					smashed
*	A0					smashed
*	A1					smashed
*	A2					smashed
*	A3	pointer to dddb 		preserved
*	A4	pointer to cdb			preserved
*
ptp_mktp
	bsr.s	ptp_unlk		; unlink from where it is
*
	move.w	mem.llst,a2		; now link it in
	lea	pt_head(a3),a1		; at the top of the pile
	lea	sd_prwlt(a4),a0
	jsr	(a2)
	move.l	(a0),a1 		 ; point to next item down
	move.l	a1,d1			 ; (is there one?)
	lea	sd_prwlb-sd_prwlt(a1),a1 ; and its bottom up pointer
	bne.s	ptp_sblk		 ; link to it, if there is one...
	lea	pt_tail(a3),a1		 ; ..or to the tail if not
ptp_sblk
	lea	sd_prwlb(a4),a0 	 ; and link via our bottom up pointer
	jmp	(a2)
*
*	Unlink a primary window from the lists.
*
*	Registers:
*		Entry				Exit
*	A0-A2					smashed
*	A3	pointer to dddb 		preserved
*	A4	pointer to cdb			preserved
*
ptp_unlk
	move.w	mem.rlst,a2		; first, unlink it from where it is
	lea	pt_head(a3),a1		; in the top down list
	lea	sd_prwlt(a4),a0 	; via the top down link pointer
	jsr	(a2)
	lea	pt_tail(a3),a1		; and in the bottom up list
	lea	sd_prwlb(a4),a0 	; via the bottom up link pointer
	jsr	(a2)
	rts
*
	end
