* Open routine for pointer routines    1991  Tony Tebby
*
* 2004-03-19  1.01  Reserves sd.spcur more bytes (screen save area) for cdb (wl)
* 2016-04-16  1.02  Initialise alpha blending weight to 255 (opaque) (MK)
*
	section driver
*
	xdef	pt_open
*
	xref	pt_wchka
	xref	pt_ptop
	xref	pt_mrall
	xref	pt_setq

	xref	pt_carea
	xref	pt_redef
	xref	pt_alcsv
	xref	pt_smove
	xref	pt_rswmd

	xref	cn_chken
	xref	cn_cksize_s
	xref	cn_spap
	xref	cn_sstr
	xref	cn_sink

	xref.s	cn.inkcl
*
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_con'
	include 'dev8_keys_sys'
	include 'dev8_keys_chn'
	include 'dev8_keys_qlv'
	include 'dev8_keys_qu'
	include 'dev8_keys_err'
	include 'dev8_keys_chp' 	***** should be replaced by kbd defn
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_smsq_smsq_config_keys'
	include 'dev8_keys_sysspr'

opreg	reg	d1-d7/a1-a5
opfr	equ	$0c
stk_d1	equ	opfr

opkey	equ	$08			position of keyboard queue length
*
pto_exup
	bra.l	pto_exfr
pt_open
	movem.l opreg,-(sp)
	move.l	a3,a4			; keep pointer to dddb
	sub.w	#opfr,a7		; stack frame for size required
	move.l	a7,a3			; point to parameter block

pto_con
	move.w	iou.dnam,a2
	jsr	(a2)			; decode name
	bra.s	pto_scr 		; not found, try scr
	bra.s	pto_chkc		; oops ... check for $SCR
	bra.s	pto_scon		; and an OK
*
	dc.w	3,'CON_'
	dc.w	5
	dc.w	' _',448
	dc.w	' X',180
	dc.w	' A',32
	dc.w	' X',16
	dc.w	' _',128
*
pto_scr
	move.w	iou.dnam,a2
	jsr	(a2)			; decode name
	bra.s	pto_exup		; not found
	bra.s	pto_exup		; oops
	bra.s	pto_setu		; and an OK
*
	dc.w	3,'SCR_'
	dc.w	4
	dc.w	' _',448
	dc.w	' X',180
	dc.w	' A',32
	dc.w	' X',16
*
pto_chkc
	cmp.l	#'$SCR',6(a0)		; full screen?
	bne.s	pto_exup
	move.l	pt_xscrs(a4),(a3)	; size
	clr.l	4(a3)			; origin
	move.w	#128,opkey(a3)		; keyboard

pto_scon
	addq.w	#1,opkey(a3)		; one spare in queue
	moveq	#qu.hdlen-4+1,d0	; allocate space for queue (+spare)
	add.w	opkey(a3),d0		; +length
	bvs.l	pto_orng
*
*
pto_setu
	move.l	a4,a3			; restore dd block
	move.l	d0,d7			; save queue allocation
	move.l	pt_xscrs(a3),d1 	; point to maximum allowed size
	moveq	#0,d2			; origin is 0,0
	move.l	a7,a1			; and size we got
	jsr	cn_chken(pc)		; check to see if the latter's OK
	bne	pto_exfr		; it isn't
*
*	Set up channel definition block
*
alcreg	reg	d3/a1-a2
	movem.l alcreg,-(sp)
	move.l	#sd.extnl+sd_keyq+sd.spcur,d1  ; allocate enough space
	add.l	d7,d1
	move.w	mem.achp,a2
	jsr	(a2)			; allocate heap
	movem.l (sp)+,alcreg
	tst.l	d0
	bne.l	pto_exfr		; ...oops
	add.w	#sd.extnl,a0

	tst.l	d7			; any queue?
	beq.s	pto_sspr		; ... no
	lea	sd_keyq(a0),a2		; set it
	clr.l	d1
	move.w	opkey(a7),d1		; length
	move.w	ioq.setq,a4
	jsr	(a4)			; set it up

	move.l	pt_dumq1(a3),qu_nextq(a2) ; dummy queue 1
*
* set cursor sprite
*

pto_sspr				; check for sprite cursor now
	move.l	pt_cjob(a3),d0		; pointer to job table
	beq.s	pto_ssiz		; there is none???
	move.l	stk_d1(sp),d1		; job ID
	move.l	a2,-(a7)
	move.l	d0,a2
	move.l	d1,d0
	swap	d0
	lsl.w	#2,d1
	cmp.w	(a2,d1.w),d0		; is this entry for my job?
	beq.s	pto_mjob		;  ...yes
	swap	d0			;  ... no, tag in upper word
	clr.w	d0
	move.b	sms.conf+sms_curd,d0	; use sprite cursor or not
	move.l	d0,(a2,d1.w)		; set tag + flag now
pto_mjob
	tst.w	2(a2,d1.w)		; my entry in the table
	beq.s	pto_nspr		; do not use sprite cursor

	lea	sd_keyq(a0),a2		; point to keyqueue
	move.l	qu_endq(a2),d0		; pointer to end of queue
	beq.s	no_con			; none, may be scr channel
	addq.l	#1,d0			; jump over possible rubbish...
	bclr	#0,d0			; and make even
	move.l	d0,a2			; this is pointer sprite for channel
no_con
	move.l	#sp.cursor,sd.croff(a2) ; preset normal pointer sprite
pto_nspr
	move.l	(sp)+,a2

pto_ssiz
	movem.l (a7),d1/d2		; get size
	move.l	d1,sd_xsize(a0) 	; set size
	move.l	d2,sd_xmin(a0)		; ... and origin
	move.l	pt_scren(a3),sd_scrb(a0)    ; active area is in here
	move.w	pt_scinc(a3),sd_linel(a0)  ; and with this row increment
*
	moveq	#0,d1			  ; set paper colour 0
	jsr	cn_spap(pc)
	moveq	#0,d1
	jsr	cn_sstr(pc)
	move.w	#cn.inkcl,d1		  ; and ink colour default
	jsr	cn_sink(pc)
*
	move.l	pt_sfnt1(a3),sd_font(a0) ; set fonts
	move.l	pt_sfnt2(a3),sd_font+4(a0)

	move.l	#$08076400,sd_scal(a0)	 ; graphics scaling

	move.b	#$ff,sd_alpha(a0)	 ; default is opaque drawing

	move.l	#$0006000a,d2		 ; this is cursor size
	cmp.b	#ptm.ql8,pt_dmode(a3)	 ; which mode are we in?
	bne.s	cno_cksize		 ; ... not 8 colour
	lsl.l	#1,d2			 ; double width
	lsr.w	#1,d2
	move.b	#1<<sd..dwdt,sd_cattr(a0)

cno_cksize
	jsr	cn_cksize_s		 ; set and check size

	move.l	stk_d1(sp),d1		 job ID!!
	lea	-sd.extnl(a0),a0	 and channel block
	move.l	a0,-(sp)		 save channel address

	lea	pt_head(a3),a0		 point to list of primaries
chkn_lp
	move.l	(a0),d0 		 next primary
	beq.s	new_job 		 isn't one, there's a new job in town!
	move.l	d0,a0			 point to its link
	cmp.l	chn_ownr-sd_prwlt-sd.extnl(a0),d1
	beq.s	pto_link		 owned by current job, should be OK
	bra.s	chkn_lp
*
new_job
	moveq	#1,d3			 new job, lock all windows
	jsr	pt_wchka(pc)
	beq.s	pto_setq		 ... ok, set the queue to dummy
	bsr.l	pt_ptop 		 ... bad, repick the top
	move.l	(sp)+,a0		 return the channel def
	move.w	mem.rchp,a2
	jsr	(a2)
	moveq	#err.imem,d0		 oops
	bra.l	pto_exfr

pto_setq
	bsr.l	pt_setq 		set keyboard queue
	bsr.l	pt_mrall		and finish off wchka

pto_link
	move.l	(sp)+,a0		restore channel address
	move.l	a3,a5			keep a dddb safe
	move.l	stk_d1(sp),d1		job ID
*
	lea	pt_head(a5),a1		pointer to primary linked list
	move.l	a1,a2			save pointer
pto_lkl
	move.l	(a1),d0 		end?
	beq.s	pto_lkp 		... yes, link into primary list
	move.l	d0,a1
	cmp.l	chn_ownr-sd_prwlt-sd.extnl(a1),d1 owned by the same job?
	bne.s	pto_lkl 		... no
*
* link into end of secondary list, and copy mode and lock status
*
	cmp.l	pt_head(a5),a1		is it top window?
	bne.s	pto_lks
	st	pt_bsupp(a5)		... yes, suppress keypress until up

pto_lks
	lea	-sd_prwlt-sd.extnl(a1),a1 pointer to channel block
	lea	sd_sewll+sd.extnl(a1),a2  pointer to linked list
	lea	sd_sewll+sd.extnl(a0),a3  pointer to link pointer
*
pto_swls
	move.l	(a2),(a3)		  link in
	move.l	a3,(a2) 		  
	move.l	a1,sd_pprwn+sd.extnl(a0)  set pointer to primary window
	move.b	sd_wlstt+sd.extnl(a1),sd_wlstt+sd.extnl(a0) copy lock 
	move.b	sd_wmode+sd.extnl(a1),d5		    get mode
	bra.s	pto_sdef
*
* link into primary link lists
*
pto_lkp
	move.b	pt_dmode(a5),d5 	get mode
	tas	sd_prwin+sd.extnl(a0)	  say it is a primary window
	lea	sd_prwlt+sd.extnl(a0),a3  primary link list, top down
	lea	pt_head(a5),a1
	move.l	(a1),(a3)		move link pointer down
	move.l	a3,(a1) 		new primary channel is now head
	move.l	(a3),d0 		was there a head before?
	beq.s	pto_lkpt		... no, link into tail
	move.l	d0,a1			... yes, next primary window down

	subq.l	#sd_prwlt-sd_prwlb,a3	get bottom up pointer
	move.l	a3,sd_prwlb-sd_prwlt(a1) and link in
	bra.s	pto_sdef
*
pto_lkpt
	subq.l	#sd_prwlt-sd_prwlb,a3	get bottom up pointer
	move.l	a3,pt_tail(a5)		and save it in tail list
*
*	Set the window sizes correctly, and revise save and locks 
*
pto_sdef
	move.l	a5,a3			restore A3 so REDEF works
	add.w	#sd.extnl,a0		and use "real" cdb
	add.w	#sd.extnl,a1		and ditto for primary
*
	tst.b	sd_prwin(a0)		is it a primary?
	bmi.s	pto_chkm		yes, check its mode
	btst	#sd..well,sd_behav(a1)	no, A1 points to its primary...
	beq.s	pto_chkm		...which is unmanaged, so REDEF is OK
	movem.l sd_xhits(a1),d2/d3	...which is this big
	movem.l sd_xmin(a0),d6/d7	check current definition against primary
	exg	d6,d7			current defn is wrong way round
	jsr	pt_carea(pc)
	beq.s	pto_chkm		  inside, do nuffin
	move.l	sd_xhits(a1),sd_xsize(a0) outside, set secondary
	move.l	sd_xhito(a1),sd_xmin(a0)  to primary's hit
pto_chkm
	cmp.b	sd_wmode(a0),d5 	is window in required mode?
	beq.s	pto_rdef		yes, check it for size
	move.b	d5,d1
	jsr	pt_rswmd(pc)		no, reset window's mode
pto_rdef
	movem.l sd_xhits(a1),d1/d2	keep old primary size
	move.b	sd_wlstt(a1),-(sp)	and old lock status
	jsr	pt_redef(pc)		pretend we did a SD.WDEF
	tst.b	(sp)+			were we locked?
	bge.s	pto_exok		no, REDEF will have fixed things then
	tst.b	sd_prwin(a0)		are we a primary?
	bmi.s	pto_exok		yes, must be OK then
	cmp.l	sd_xhits(a1),d1 	same size?
	bne.s	pto_chsv		no, change save area
	cmp.l	sd_xhito(a1),d2 	same position?
	beq.s	pto_exok		yes, OK then
*
*	The first operation on a new window has expanded its locked
*	primary, so the primary's save area is wrong.  We need to 
*	allocate a bigger save area and copy the current one into 
*	a small part of the new one.
*
chsvreg reg	d1/d2/a0/a1/a3
*		
pto_chsv
	move.l	sd_wsave(a1),sd_wsave(a0) ; give new window old save area
	move.b	sd_mysav(a1),sd_mysav(a0) ; and ownership 
	movem.l chsvreg,-(sp)
	move.l	a1,a4			; for the new primary size...
	jsr	pt_alcsv(pc)		; ...allocate a new save area
	move.l	a0,a2			; keep pointer to it
	move.l	d1,d3			; and its size
	movem.l (sp)+,chsvreg
	tst.l	d0			; OK?
	bne.s	pto_exch		; no, give up
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
	beq.s	pto_exch		; no, don't release it
	movem.l chsvreg,-(sp)
	move.l	sd_wsave(a0),a0 	; return the old save area
	moveq	#sms.rchp,d0
	trap	#1
	movem.l (sp)+,chsvreg
*
pto_exch
	clr.l	sd_wsave(a0)		; no save area now

pto_exok
	moveq	#0,d0
	sub.w	#sd.extnl,a0
pto_exfr
	add.w	#opfr,a7
	movem.l (sp)+,opreg
	rts
*
pto_orng
	moveq	#err.orng,d0
	bra.s	pto_exfr
*
	end
