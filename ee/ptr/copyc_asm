* Pointer copy channel block routine   V1.02	 1986	Tony Tebby   QJUMP
*
* 2020-08-10  1.04  Changed mode information from sys_qlmr to pt_dmode (MK)

	section driver
*
	xdef	pt_copyc
	xdef	pt_linkc
*
	xref	pt_carea
	xref	pt_redef
	xref	pt_alcsv
	xref	pt_smove
	xref	pt_rswmd
*
	include 'dev8_keys_sys'
	include 'dev8_keys_chn'
	include 'dev8_keys_qlv'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_con'
*
* copy console driver, extend and link in
*
*	d1-d7	preserved
*	a0 cr	pointer to channel definition block, old / new
*	a1-a2	preserved
*	a3 c p	pointer to linkage block
*	a4-a6	preserved
*
savregs reg	d1-d7/a1-a5
stk.a3	equ	7*4+3*4-4+2
*
pt_copyc
	movem.l savregs,-(sp)		save regs
	move.w	sr,-(sp)		save status register
	or.w	#$0700,sr		set interrupts disabled
	move.l	a3,a5			save linkage address
	moveq	#sd.extnl,d1		get extension length
	add.l	chn_len(a0),d1		plus allocated length
	move.l	a0,a4
	move.w	mem.achp,a2		and allocate new
	jsr	(a2)
	bne.s	cpc_exom		... oops
*
	lea	4(a0),a3		skip heap entry length
	lea	8(a4),a2		(and driver linkage)
	lea	pt_liodm(a5),a1
	move.l	a1,(a3)+		set dummy driver linkage
	move.l	(a2)+,(a3)+		transfer rest of header - owner...
	move.l	(a2)+,(a3)+		...rflag...
	move.l	(a2)+,(a3)+		...tag/stat/op...
	move.l	(a2)+,(a3)+		...jobwt
*
	lea	sd.extnl(a3),a3 	get start of old entry in new
	moveq	#-$18,d0		header already copied
	add.l	chn_len(a4),d0		... length of old entry
cpc_copy
	move.b	(a2)+,(a3)+		and copy all bytes
	subq.l	#1,d0
	bgt.s	cpc_copy

	move.b	pt_dmode(a3),sd_wmode+sd.extnl(a0) copy this window's mode

*
*	Now move keyboard queue
*
cpc_keyq
	move.l	a4,a1			find old keyboard queue
	add.l	pt_kqoff(a5),a1
	tst.l	(a1)
	beq.s	cpc_rcdb		no queue
*
cpc_kmov
	moveq	#sd.extnl,d0		extension to block
	add.l	a0,d0			new block
	sub.l	a4,d0			... distance block moved
	cmp.l	sys_ckyq(a6),a1 	is this current queue?
	bne.s	cpc_kqpt		no
	add.l	d0,sys_ckyq(a6) 	yes, set current keyboard queue
cpc_kqpt
	add.l	d0,a1			new pointer
	move.l	pt_dumq1(a5),(a1)+	set link pointer
	add.l	d0,(a1)+		move pointer
	add.l	d0,(a1)+		move pointer
	add.l	d0,(a1)+		move pointer
*
* return old channel block to heap
*
cpc_rcdb
	moveq	#0,d0			all OK now
cpc_exom
	move.l	d0,d4
	exg	a0,a4			save new pointer
	move.w	mem.rchp,a2		release old space
	jsr	(a2)
	exg	a4,a0			restore channel pointer
	move.l	d4,d0			and error code
*
	move.w	(sp)+,sr		restore status register
	movem.l (sp)+,savregs		and registers
	tst.l	d0			set error code
	rts

	page
*
*	Link a window into either primary or secondary linked lists.
*	This is called from INIT to link in existing windows, and via
*	IO on the first IO call to the window.	The "fake" dddb is
*	fixed so that the link is only performed once.
*
*	Registers:
*		Entry				Exit
*	A0	pointer to cdb to link		preserved
*	A3	pointer to fake dddb		pointer to real dddb
*
linkreg reg	d0-d7/a1/a2/a4/a5
pt_linkc
	movem.l linkreg,-(sp)
	moveq	#pt_liod-pt_liodm,d0	shift driver and link by this much
	add.l	d0,a3			fix dddb pointer...
	add.l	d0,chn_drvr(a0) 	...and cdb version
	move.l	a3,a5			keep a copy safe
*
	lea	pt_head(a5),a1		pointer to primary linked list
	move.l	a1,a2			save pointer
	move.l	chn_ownr(a0),d1 	set our owner
cpl_lkl
	move.l	(a1),d0 		end?
	beq.s	cpl_lkp 		... yes, link into primary list
	move.l	d0,a1
	cmp.l	chn_ownr-sd_prwlt-sd.extnl(a1),d1 owned by the same job?
	bne.s	cpl_lkl 		... no
*
* link into end of secondary list, and copy mode and lock status
*
	cmp.l	pt_head(a5),a1		is it top window?
	bne.s	cpl_lks
	st	pt_bsupp(a5)		... yes, suppress keypress until up

cpl_lks
	lea	-sd_prwlt-sd.extnl(a1),a1 pointer to channel block
	lea	sd_sewll+sd.extnl(a1),a2  pointer to linked list
	lea	sd_sewll+sd.extnl(a0),a3  pointer to link pointer
*
cpl_swls
	move.l	(a2),(a3)		  link in
	move.l	a3,(a2) 		  
	move.l	a1,sd_pprwn+sd.extnl(a0)  set pointer to primary window
	move.b	sd_wlstt+sd.extnl(a1),sd_wlstt+sd.extnl(a0) copy lock 
	move.b	sd_wmode+sd.extnl(a1),d1		    get mode
	bra.s	cpl_sdef
*
* link into primary link lists
*
cpl_lkp
	move.b	pt_dmode(a5),d1 	  get mode
	tas	sd_prwin+sd.extnl(a0)	  say it is a primary window
	lea	sd_prwlt+sd.extnl(a0),a3  primary link list, top down
	lea	pt_head(a5),a1
	move.l	(a1),(a3)		move link pointer down
	move.l	a3,(a1) 		new primary channel is now head
	move.l	(a3),d0 		was there a head before?
	beq.s	cpl_lkpt		... no, link into tail
	move.l	d0,a1			... yes, next primary window down
	subq.l	#sd_prwlt-sd_prwlb,a3	get bottom up pointer
	move.l	a3,sd_prwlb-sd_prwlt(a1) and link in
	bra.s	cpl_sdef
*
cpl_lkpt
	subq.l	#sd_prwlt-sd_prwlb,a3	get bottom up pointer
	move.l	a3,pt_tail(a5)		and save it in tail list
*
*	Set the window sizes correctly, and revise save and locks 
*
cpl_sdef
	move.l	a5,a3			restore A3 so REDEF works
	add.w	#sd.extnl,a0		and use "real" cdb
	add.w	#sd.extnl,a1		and ditto for primary
*
	tst.b	sd_prwin(a0)		is it a primary?
	bmi.s	cpl_chkm		yes, check its mode
	btst	#sd..well,sd_behav(a1)	no, A1 points to its primary...
	beq.s	cpl_chkm		...which is unmanaged, so REDEF is OK
	movem.l sd_xhits(a1),d2/d3	...which is this big
	movem.l sd_xmin(a0),d6/d7	check current definition against primary
	exg	d6,d7			current defn is wrong way round
	jsr	pt_carea(pc)
	beq.s	cpl_chkm		  inside, do nuffin
	move.l	sd_xhits(a1),sd_xsize(a0) outside, set secondary
	move.l	sd_xhito(a1),sd_xmin(a0)  to primary's hit
cpl_chkm
	cmp.b	sd_wmode(a0),d1 	is window in required mode?
	beq.s	cpl_rdef		yes, check it for size
	jsr	pt_rswmd(pc)		no, reset window's mode
cpl_rdef
	movem.l sd_xhits(a1),d1/d2	keep old primary size
	move.b	sd_wlstt(a1),-(sp)	and old lock status
	jsr	pt_redef(pc)		pretend we did a SD.WDEF
	tst.b	(sp)+			were we locked?
	bge.s	cpl_exit		no, REDEF will have fixed things then
	tst.b	sd_prwin(a0)		are we a primary?
	bmi.s	cpl_exit		yes, must be OK then
	cmp.l	sd_xhits(a1),d1 	same size?
	bne.s	cpl_chsv		no, change save area
	cmp.l	sd_xhito(a1),d2 	same position?
	beq.s	cpl_exit		yes, OK then
*
*	The first operation on a new window has expanded its locked
*	primary, so the primary's save area is wrong.  We need to 
*	allocate a bigger save area and copy the current one into 
*	a small part of the new one.
*
chsvreg reg	d1/d2/a0/a1/a3
*		
cpl_chsv
	move.l	sd_wsave(a1),sd_wsave(a0) ; give new window old save area
	move.b	sd_mysav(a1),sd_mysav(a0) ; and ownership 
	movem.l chsvreg,-(sp)
	move.l	a1,a4			; for the new primary size...
	jsr	pt_alcsv(pc)		; ...allocate a new save area
	move.l	a0,a2			; keep pointer to it
	move.l	d1,d3			; and its size
	movem.l (sp)+,chsvreg
	tst.l	d0			; OK?
	bne.s	cpl_exch		; no, give up
	move.l	a2,sd_wsave(a1) 	; primary has new save area
	subq.l	#8,d3
	subq.l	#8,d3
	move.l	d3,sd_wssiz(a1) 	; that's this big
*
*	We've got a new save area, so copy overlap
*
	move.l	sd_xhits(a0),-(sp)
	move.l	sd_xhito(a0),-(sp)	; keep new window's hit area safe
	movem.l d1/d2,sd_xhits(a0)	; make new window look like old primary
	exg	a1,a0
	jsr	pt_smove(pc)		; move little save area to big one
	exg	a1,a0
	move.l	(sp)+,sd_xhito(a0)	; restore new window's...
	move.l	(sp)+,sd_xhits(a0)	; ...hit area
*
*	Having copied the overlap, we may need to return the old save area
*
	tst.b	sd_mysav(a0)		; was old save area mine?
	beq.s	cpl_exch		; no, don't release it
	movem.l chsvreg,-(sp)
	move.l	sd_wsave(a0),a0 	; return the old save area
	moveq	#sms.rchp,d0
	trap	#1
	movem.l (sp)+,chsvreg
*
cpl_exch
	clr.l	sd_wsave(a0)		; no save area now
*
cpl_exit
	sub.w	#sd.extnl,a0
	movem.l (sp)+,linkreg
	rts
	end
