*	Reset the pointer areas, hit and outline, for a window and its
*	corresponding primary.
*
* 2005-11-14  1.01  Correctly restores screen for unbehaved windows (MK)
*
*	Registers:
*		Entry				Exit
*	D4-D7					smashed
*	A0	pointer to cdb to set		preserved
*
	section driver
*
	include 'dev8_keys_con'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_err'
	include 'dev8_mac_assert'
*
	xdef	pt_wdef 		define window area (sd.wdef)
	xdef	pt_redef		check after window redefined
	xdef	pt_setp 		reset primary hit and outline areas
	xdef	pt_slock		set locks
	xdef	pt_carea		check area
	xdef	pt_trncs		check after primary outline set
*
	xref	pt_wchka
	xref	pt_mrall
	xref	pt_wremv
	xref	gu_achp0
	xref	gu_rchp
*
pt_wdef
	movem.l d2/d3,-(sp)
	movem.l (a1),d6/d7		get the passed parameters
	bclr	#16,d6			even up...
	bclr	#16,d7			...the x's
*
	btst	#sd..well,sd_behav(a0)	is it well behaved?
	beq.s	pt_wdefo		... no
	movem.l sd_xhits(a0),d2/d3	... yes, limits are its own hit area
	bra.s	pt_wdefc
*
pt_wdefo
	moveq	#0,d3			set origin of whole screen
	move.l	pt_ssize(a3),d2 	and limits
	tst.b	sd_prwin(a0)		is this a primary?
	bmi.s	pt_wdefc		yes, the screen is the limit then
	move.l	sd_pprwn(a0),a1 	no, point to the primary
	add.w	#sd.extnl,a1
	btst	#sd..well,sd_behav(a1)	is our primary well-behaved?
	beq.s	pt_wdefc		no, we may modify it then
	movem.l sd_xhits(a1),d2/d3	yes, we're limited to be within it
pt_wdefc
	bsr.s	pt_carea		check areas
	bne.s	pwd_exit		not included
*
	bsr.s	ptw_sdef		do everything needed to set active area
*
	btst	#sd..well,sd_behav(a0)	is it well-behaved?
	bne.s	pwd_exok		... yes
	bsr.s	pt_redef		... no, reset hit and outline
pwd_exok
	clr.b	pt_schfg(a3)		clear scheduler done flag
	moveq	#0,d0
pwd_exit
	movem.l (sp)+,d2/d3
	rts
*
*	Set active area, clear border and cursor position, pending
*	newline and graphics position flag
*
*	Registers:
*		Entry				Exit
*	D6	size				preserved
*	D7	origin				preserved
*	A0	cdb				preserved
*
ptw_sdef
	move.l	d6,sd_xsize(a0) 	fill in size
	move.l	d7,sd_xmin(a0)		and origin
	clr.w	sd_borwd(a0)		clear the border size
	assert	sd_xpos,sd_ypos-2
	clr.l	sd_xpos(a0)		reset cursor position
	clr.b	sd_nlsta(a0)		and clear pending newline
	bclr	#7,sd_cattr(a0) 	and graphic positioning (bug fix!)
	rts
*
* Check whether window area is within another window area
*
*	d2 c s	size x,y	allowed limits 
*	d3 c s	origin x,y
*	d6 c  p size x,y	area to be checked
*	d7 c  p origin x,y
*
pt_carea
	add.l	d3,d2
	bsr.s	pt_ca1			check one lot of coordintaes
	bne.s	pca_or			... oops
	swap	d2			now the other
	swap	d3
pt_ca1
	cmp.w	d7,d3			origin in window?
	bgt.s	pca_or			... no
	sub.w	d7,d2			
	sub.w	d6,d2			is max in window?
	blt.s	pca_or			... no
	swap	d6
	bmi.s	pca_or
	swap	d7
	moveq	#0,d0			... ok
	rts
pca_or
	swap	d6
	swap	d7
	moveq	#err.orng,d0
	rts
	page
*
* Re-define the hit and outline areas of a window and its
* corresponding primary: window may itself be a primary or
* secondary window. A check is made on the lock status of
* windows above and below on the pile, and appropriate
* action taken when new overlaps are formed.
*
*	Registers:
*		Entry				Exit
*	D3-D7					smashed
*	A0	pointer to cdb of window	preserved
*
pt_redef
	btst	#sd..well,sd_behav(a0)	well behaved?
	bne	pt_slock		yes, no need to set areas

; MK special! If outline of primary window gets changed in any way, create fake
; CDB with data of old window and then "remove" the fake window from screen.
; This way all the area not covered anymore by the window will be restored,
; unlike with the old routine
rdef.reg reg	d1-d3/a0-a2
	movem.l rdef.reg,-(sp)
	move.l	a0,a1
	tst.b	sd_prwin(a0)		is this a primary window?
	blt.s	prd_chkout		yes, go ahead
	move.l	sd_pprwn(a0),a1 	point A1 at primary window
	add.w	#sd.extnl,a1		normal cdb value!
prd_chkout
	move.l	sd_xouts(a1),-(sp)	remember outline
	move.l	sd_xouto(a1),-(sp)
	bsr.s	pta_seth
	bsr	pt_setp
	movem.l (sp)+,d1-d2		d1 = origin, d2 = size
	cmp.l	sd_xouto(a1),d1
	bne.s	prd_redraw
	cmp.l	sd_xouts(a1),d2
	beq.s	prd_old 		outline same, no need to redraw

prd_redraw
	move.l	#sd.extnl+sd_end,d0
	jsr	gu_achp0
	bne.s	prd_old 		failed, just do traditional code

	adda.w	#sd.extnl,a0		fill in fake cdb
	move.l	d1,sd_xouto(a0)
	move.l	d2,sd_xouts(a0)
	move.b	sd_wmode(a1),sd_wmode(a0)
	move.b	#sd.wunlk,sd_wlstt(a0)
	move.l	sd_scrb(a1),sd_scrb(a0)
	move.w	sd_linel(a1),sd_linel(a0)
	clr.l	sd_wsave(a0)
	jsr	pt_wremv		remove fake window
	suba.w	#sd.extnl,a0
	jsr	gu_rchp 		release fake CDB
prd_old
	moveq	#0,d3
	jsr	pt_wchka		check that overlaps are OK
	jsr	pt_mrall
prd_exit
	movem.l (sp)+,rdef.reg
	rts

*
* now set all the locks
*
pt_slock
	movem.l d1/d2/a0-a2,-(sp)
*
	moveq	#0,d3			allow saves
	jsr	pt_wchka(pc)		check that the overlaps are OK
************************* what no check?????
	jsr	pt_mrall(pc)		restore any non-overlapped windows
*
	movem.l (sp)+,d1/d2/a0-a2
	rts
*
*	Set hit and outline areas of a secondary window.
*
pta_seth
	move.w	sd_borwd(a0),d4 	get border width
*
	addq.l	#2,a0			first do y dimensions
	bsr.s	pta_shx
	subq.l	#2,a0			then x
	add.w	d4,d4			with twice as much border
*
pta_shx
	move.w	sd_xmin(a0),d5		get top (or lhs)
	move.w	d5,d6			... and bottom or rhs
	add.w	sd_xsize(a0),d6
	sub.w	d4,d5			adjust for border
	add.w	d4,d6
	move.w	d5,sd_xhito(a0) 	set hit origin
	move.w	d5,sd_xouto(a0)
	sub.w	d5,d6			... width
	move.w	d6,sd_xhits(a0) 	set it
	move.w	d6,sd_xouts(a0)
	rts
*
*	Reset primary window's hit and outline areas.
*
*	Registers:
*		Entry				Exit
*	D4-D6					smashed
*	A0	pointer to a window		preserved
*
pt_setp
	movem.l a0-a2,-(sp)
*
	tst.b	sd_prwin(a0)		is this a primary window?
	blt.s	pta_chkb		yes, check its behaviour
	move.l	sd_pprwn(a0),a0 	point A0 at primary window
	add.w	#sd.extnl,a0		normal cdb value!
pta_chkb
	btst	#sd..well,sd_behav(a0)	well behaved?
	bne.s	pta_exit		yes, it should be OK then
	bsr.s	pta_seth		no, set its hit area
*
	move.l	sd_xhito(a0),d4 	get hit area origin
	add.l	d4,sd_xhits(a0) 	and make size into edge
*
	move.l	d4,sd_xouto(a0) 	outline is hit area
	move.l	sd_xhits(a0),sd_xouts(a0)
*
do_agg
	move.l	a0,a1			start after primary window
	sub.l	a2,a2
agg_lp
	move.l	sd_sewll(a1),d4 	next secondary
	beq.s	pta_stpe		no more
	lea	-sd_sewll(a2,d4.l),a1	point to start of it
*
	cmpm.w	(a0)+,(a1)+		it's quicker than two ADDQ's!
	bsr.s	pta_agg 		aggregate y
	subq.l	#2,a0			but there isn't a predecrement CMPM
	subq.l	#2,a1
	bsr.s	pta_agg 		and do x
	bra.s	agg_lp
*
pta_stpe
	move.l	sd_xhito(a0),d4 	get hit area top left...
	sub.l	d4,sd_xhits(a0) 	...and make bottom right into size
*
	move.l	sd_xouto(a0),d4 	get outline top left...
	sub.l	d4,sd_xouts(a0) 	...and make bottom right into size
*
pta_exit
	movem.l (sp)+,a0-a2		restore and return
	rts
*
*	Aggregate hit and outline areas into the primary window: uses
*	the TTtrick of offsetting the cdb base to do the y co-ordinates.
*
*	Registers:
*		Entry				Exit
*	D4,D5					smashed
*	A0	primary window cdb		preserved
*	A1	secondary window cdb		preserved
*
pta_agg
	move.w	sd_xhito(a1),d5 	get secondary's x hit origin (lh end)
	cmp.w	sd_xhito(a0),d5 	is secondary's...
	bgt.s	xlh_ok			...bigger? OK then.
	move.w	d5,sd_xhito(a0) 	update primary hit lhs
xlh_ok
	add.w	sd_xhits(a1),d5 	same for right hand end
	cmp.w	sd_xhits(a0),d5
	blt.s	xrh_ok
	move.w	d5,sd_xhits(a0)
*
xrh_ok
	move.w	sd_xhito(a1),d5 	get lhs...
	cmp.w	sd_xouto(a0),d5 	do lhs of outline
	bgt.s	xlo_ok
	move.w	d5,sd_xouto(a0)
xlo_ok
	add.w	sd_xhits(a1),d5 	this far across...
	cmp.w	sd_xouts(a0),d5
	blt.s	xro_ok
	move.w	d5,sd_xouts(a0)
xro_ok
	rts
*
*	If a primary's outline is reset we must ensure that all secondaries
*	fall within it.  Those that still do are not modified. Those
*	that are now outside the primary's hit area are re-defined
*	to the primary's hit area: if they own a save area it is discarded:
*	and they are set to 'badly behaved'.
*
*	Registers:
*		Entry				Exit
*	A0	pointer to primary cdb		preserved
*
trncreg reg	d0-d7/a0-a3
pt_trncs
	movem.l trncreg,-(sp)
	movem.l sd_xhits(a0),d4/d5	; secondaries must be within this
ptt_loop
	move.l	sd_sewll(a0),d0 	; get next secondary
	beq.s	ptt_done		; no more, exit
	move.l	d0,a0			; point to...
	sub.w	#sd_sewll,a0		; ...normal cdb
	movem.l sd_xhits(a0),d6/d7	; get hit size of secondary
	move.l	d4,d2			; and primary to compare
	move.l	d5,d3
	bsr	pt_carea		; check them
	beq	ptt_loop		; OK, try next
*
	bclr	#sd..well,sd_behav(a0)	; secondary isn't well-behaved now 
	movem.l d4/d5,sd_xhits(a0)	; set hit area...
	movem.l d4/d5,sd_xouts(a0)	; ...outline...
	move.l	d4,d6
	move.l	d5,d7
	bsr	ptw_sdef		; ...and active area
	move.l	sd_wsave(a0),d0 	; has it a save area?
	beq.s	ptt_loop		; no, no problem
	clr.l	sd_wsave(a0)		; well, it's invalid now
	tst.b	sd_mysav(a0)		; its own?
	beq.s	ptt_loop		; no, mustn't discard it
	move.l	a0,d6			; keep cdb safe
	move.l	d0,a0			; return the save area
	moveq	#sms.rchp,d0		; to the common heap
	trap	#1
	move.l	d6,a0			; point back to cdb
	bra.s	ptt_loop		; and try next
*
ptt_done
	movem.l (sp)+,trncreg
	rts

*
	end
