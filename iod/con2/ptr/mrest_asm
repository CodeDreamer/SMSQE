* Multiple window restore		v1.02   Nov 1986  QJUMP
*
*	Restore multiple windows.  This routine is used for all changes
*	in window arrangement, normally restoring more than one window only
*	when the display mode has been changed explicitly, or when a picked
*	window is of a different mode to the previous top of pile.
*	If an explicit MODE was done, then D7 should contain the job
*	ID of the job which did the MODE call: its windows will be reset
*	in the same way as before, and all others will just be restored.
*
*	Registers:
*		Entry				Exit
*	D0					smashed
*	D7	job ID of MODEing job		preserved
*	A2					smashed
*	A3	pointer to dddb 		preserved
*	A6	^ system variables		preserved
*
	section driver
*
	include 'dev8_mac_assert'
	include 'dev8_keys_err'
	include 'dev8_keys_sys'
	include 'dev8_keys_chn'
	include 'dev8_keys_jcb'
	include 'dev8_keys_qdos_io'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_con'

*
	xref	pt_wrest
	xref	pt_wsave
	xref	pt_shad
	xref	cn_io
*
	xdef	pt_mrest		restore / reset from a2 up
	xdef	pt_mrstj		restore all / reset jobs windows
	xdef	pt_mrall		restore all
	xdef	pt_rswmd
*
reglist reg	d1-d4/a0/a1/a4/a5
*
pt_mrall
	moveq	#-1,d7			restore all
pt_mrstj
	lea	pt_tail(a3),a2		set bottom up link
pt_mrest
	movem.l reglist,-(sp)
rest_loop
	move.l	(a2),d0 		get next window up
	beq.l	rest_end		isn't one, we've finished
	move.l	d0,a2			point A2 to link
	lea	-sd_prwlb(a2),a0	  point A0 to cdb
	cmp.l	chn_ownr-sd.extnl(a0),d7  MODE job's primary?
	beq.s	do_clral		  yes
*
	cmp.b	#sd.slock,sd_wlstt(a0)	should we lock this one?
	beq.s	lock_it 		... yes, do it then
	cmp.b	#sd.sunlk,sd_wlstt(a0)	... no, unlock it perhaps?
	bne.s	chk_mode		no, restore only on mode change then
	moveq	#sd.wunlk,d2		unlock...
	bsr.s	do_lock 		...primary and all secondaries
	btst	#sd..frez,sd_behav(a0)
	beq.s	do_rest
	bsr.l	thaw
*
do_rest
	tst.l	sd_wsave(a0)		is there a save area?
	beq.s	do_clral		no, just clear everything then

	moveq	#-1,d2			don't return heap
	sub.l	a1,a1			use own save area
	jsr	pt_wrest(pc)		restore it
	jsr	pt_shad(pc)		and put shadow on
	bra.s	rest_loop
*
chk_mode
	tst.w	d7			any mode change?
	blt.s	rest_loop		no, so don't need to restore
	move.b	pt_dmode(a3),d2 	is mode current
	cmp.b	sd_wmode(a0),d2 	...current?
	beq.s	do_rest 		yes, so restore
	bra.s	rest_loop
*
lock_it
	moveq	#sd.wlock,d2		lock primary and its secondaries
	bsr.s	do_lock
	btst	#sd..frez,sd_behav(a0)
	beq.s	rest_loop
	bsr.s	freeze
	bra.s	rest_loop
*
do_clral
	bsr	clr_all 		reconstitute all this job's windows
	tst.l	sd_wsave(a0)		; does it have a save area?
	beq	rest_loop		; no
	moveq	#0,d1			; yes, use it...
	jsr	pt_wsave(pc)		; ...to save the window
	bra	rest_loop		and do next primary
*
rest_end
	movem.l (sp)+,reglist
	rts
*
*	Set locks on a primary and its secondaries to a given value.
*
*	Registers:
*		Entry				Exit
*	D0					smashed
*	D2	value to set			preserved
*	A0	primary window			preserved
*	A1					smashed
*	A4					smashed
*
do_lock
	move.l	a0,a4			; smashable pointer
lock_lp
	move.b	d2,sd_wlstt(a4) 	; set lock
	move.l	sd_sewll(a4),d0 	; next window
	beq.s	lock_end		; isn't one
	move.l	d0,a1
	lea	-sd_sewll(a1),a4	; point to it
	bra.s	lock_lp
lock_end
	rts

* Freeze a job tree

freeze
	bsr.s	freeze_set
	bra.s	freeze_2
	move.b	jcb_pacc+1(a0),d0	  ; priority
	beq.s	freeze_rts
	lsl.w	#8,d0			  ; set to zero, and keep it
	move.w	d0,jcb_pacc(a0)
freeze_rts
	rts
freeze_2
	move.b	jcb_prin(a0),d0 	  ; priority
	beq.s	freeze_rts
	clr.b	jcb_prin(a0)		  ; ... now zero
	move.b	d0,jcb_pacc+1(a0)	  ; but saved
	rts

thaw
	bsr.s	freeze_set
	bra.s	thaw_2
	move.w	jcb_pacc(a0),d0 	  ; priority
	tst.b	d0			  ; was zero?
	bne.s	thaw_rts		  ; ... no
	lsr.w	#8,d0
	move.b	d0,jcb_pacc+1(a0)	  ; restore
thaw_rts
	rts
thaw_2
	tst.b	jcb_prin(a0)		  ; was priority zero?
	bne.s	thaw_rts		  ; ... no
	move.b	jcb_pacc+1(a0),jcb_prin(a0) ; set priority
	rts

freeze_set
	move.l	(sp)+,d0
	move.l	chn_ownr-sd.extnl(a0),d1 ; owner ID
	movem.l d1/d2/d3/a0/a1/a2,-(sp)
	move.l	d0,a2
	move.l	d1,d3
	moveq	#sms.info,d0
	trap	#do.sms2		 ; which OS
	move.l	d3,d1
	cmp.l	#'2   ',d2
	bge.s	freeze_loop
	addq.l	#2,a2			 ; the old one

freeze_loop
	move.w	d1,d0			 ; job number
	beq.s	freeze_exit
	move.l	sys_jbtb(a6),a0 	 ; job table
	lsl.w	#2,d0
	move.l	(a0,d0.w),a0		 ; base of job
	jsr	(a2)			 ; do operation
	move.l	(sp),d2 		 ; top job
	moveq	#sms.injb,d0
	trap	#do.sms2
	tst.l	d0
	beq.s	freeze_loop
freeze_exit
	movem.l (sp)+,d1/d2/d3/a0/a1/a2
	rts


*
* Come here to reset a window's attributes: this happens when it is opened
* with one screen mode, then the mode is changed before it is used
*
pt_rswmd
	move.b	pt_dmode(a3),d5 	; keep current mode
	move.b	d1,pt_dmode(a3) 	; and pretend this is current
	move.b	d1,sd_wmode(a0) 	; and set it in cdb
	moveq	#2,d4			; three things to do
	lea	rs_fns(pc),a5
	bsr.s	io_loop 		; do them
	moveq	#0,d4			; and one more
	lea	rs_fns1(pc),a5		; this one
	bsr.s	io_loop
	move.b	d5,pt_dmode(a3) 	; restore old mode
	rts
*
io_loop
	movem.w (a5)+,d0-d2		; get some parameters
	ext.l	d0			; extend the trap key
	move.b	0(a0,d1.w),d1		; and get the parameters from the cdb
	move.b	0(a0,d2.w),d2
	moveq	#-1,d3			; pretend it's first entry
	bsr.s	trap3
	addq.l	#-err.nc,d0		; was it "not complete"?
	bne.s	io_next 		; no
	subq.l	#6,a5			; re-do the call
	clr.b	sys_dfrz(a6)		; with the screen unfrozen
	bra.s	io_loop 		; and do it again
io_next
	dbra	d4,io_loop		; do next I/O
	rts
*
*	Fake an I/O call
*
trap3
	movem.l d4-d7/a0-a5,-(sp)	; save stuff
	jsr	cn_io
	movem.l (sp)+,d4-d7/a0-a5
	rts
*
*	Clear a primary window and all its secondaries, resetting
*	colours and character sizes appropriately.
*
clr_all
	move.l	a0,a4			; keep current primary
*
	move.l	sd_xmin(a0),-(sp)
	move.l	sd_xsize(a0),-(sp)	; save old window size/paper
	move.l	sd_pmask(a0),-(sp)
*
	clr.l	sd_pmask(a0)		; fake a new one
	move.l	sd_xhits(a0),sd_xsize(a0)
	move.l	sd_xhito(a0),sd_xmin(a0)
	moveq	#iow.clra,d0		; clear the hit area to black
	moveq	#-1,d3
	bsr.s	trap3
*
	move.l	(sp)+,sd_pmask(a0)
	move.l	(sp)+,sd_xsize(a0)	; restore old window size/paper
	move.l	(sp)+,sd_xmin(a0)
*
	clr.l	-(sp)			; flag end of list
clp_fwnl
	move.l	sd_sewll(a0),d0 	; get next secondary
	beq.s	clp_efwn		; there isn't one, so stop
	move.l	d0,a0			; it's here
	assert	sd_sewll,0
*	lea	-sd_sewll(a0),a0	; I ASSERT that this isn't required!
	move.l	a0,-(sp)		; save cdb pointer for later
	bra.s	clp_fwnl
*
clp_efwn
	move.l	a4,d0			; start with the primary
win_loop
	move.l	d0,a0			     ; for this window
	move.b	pt_dmode(a3),sd_wmode(a0)    ; change its mode
	clr.b	sd_cattr(a0)		     ; zilch out the attributes
	tst.b	sd_curf(a0)		     ; is cursor enabled?
	sne	sd_curf(a0)		     ; yes, make it invisible

	move.b	sd_borwd+1(a0),sd_xinc+1(a0) ; save border width
	move.b	sd_bcolr(a0),sd_yinc+1(a0)   ; and colour
	moveq	#6,d4			     ; do seven I/O calls
	lea	io_fns(pc),a5		     ; these ones
	bsr	io_loop 		     ; do some I/O
*
	move.l	(sp)+,d0		; pop next cdb
	bne.s	win_loop		; there is one, do it
	move.l	a4,a0			; there isn't, return
	rts
*
*	What we have to do to each window on MODE: this now only happens
*	to the windows of the job that called MODE.
*	The table defines the I/O calls required, together with the offset
*	in the channel definition block where the parameters for that call
*	are to be found. The character attributes are set to 0, and
*	the old border size stored in the cursor increments for later: this
*	is so that transparent borders can be dealt with.
*
io_fns
	dc.w	iow.defb,sd_end,sd_cattr     ; BORDER 0,?
rs_fns
	dc.w	iow.sink,sd_icolr,sd_end     ; INK old
	dc.w	iow.spap,sd_pcolr,sd_end     ; PAPER old
	dc.w	iow.sstr,sd_scolr,sd_end     ; STRIP old
	dc.w	iow.clra,sd_end,sd_end	     ; CLS
	dc.w	iow.defb,sd_yinc+1,sd_xinc+1 ; BORDER old_width,old_colour
rs_fns1
	dc.w	iow.ssiz,sd_cattr,sd_cattr   ; CSIZE 0,0
*
	end
