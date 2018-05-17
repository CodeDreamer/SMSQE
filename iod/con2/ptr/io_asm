* Pointer IO intercept	V1.09	 1985	Tony Tebby   QJUMP
*
* 2003-02-16  1.08  bugfix (WL)
* 2005-11-16  1.09  calls new background I/O handler (MK)
*
	section driver
*
	xdef	pt_io
	xdef	pt_hides
	xdef	io_tstw
	xdef	pt_do_con		; expose internal points to bgio
	xdef	pti_do
	xdef	pti_hide
*
	xref.l	pt.vers
	xref	cn_io
	xref	pt_wpap
	xref	pt_flims
	xref	pt_psave
	xref	pt_prest
	xref	pt_saddp
	xref	pt_rptr
	xref	pt_rpixl
	xref	pt_wblb1
	xref	pt_lblob
	xref	pt_wspr1
	xref	pt_spray
	xref	pt_outln
	xref	pt_splm
	xref	pt_sptr
	xref	pt_pickw
	xref	pt_swdef
	xref	pt_wdef
	xref	pt_wsave
	xref	pt_wrest
	xref	pt_bgio
	xref	sp_remove
*
	include 'dev8_keys_err'
	include 'dev8_keys_sys'
	include 'dev8_keys_qdos_io'
	include 'dev8_keys_qdos_pt'
	include 'dev8_keys_con'
*
* returns gt if in
*
io_tstw
	addq.l	#2,a0			; point to window's y coordinates
	bsr.s	itw_lim 		; check limits
	subq.l	#2,a0
	ble.s	itw_rts 		; no overlap
	swap	d1
	swap	d2			; check x
itw_lim
	move.w	sd_xmin(a0),d0
	cmp.w	d0,d1			; compare window xmin with xmax
	ble.s	itw_rts 		; it's greater, so no overlap
	add.w	sd_xsize(a0),d0 	; get window xmax
	cmp.w	d2,d0			; and compare with xmin
itw_rts
	rts
*
pt_io
	add.w	#sd.extnl,a0		update a0 to match old definition
*
	cmp.b	#iop.rptr,d0		is it read pointer?
	beq.l	pt_iojmp		... yes, always do-able
	clr.b	sd_wmove(a0)		... no, clear move flag
*
	tst.b	sys_dfrz(a6)		is display frozen?
	bne.s	ptio_nc 		... yyyyyyes
	tst.b	pt_wmove(a3)		is window move sprite active?
	bmi.s	ptio_nc 		... yes
*
	tst.b	sd_wlstt(a0)		is window locked?
	bpl.s	pti_do			... no, do the operation

*
*	Some operations are feasible even if the window is locked
*
	cmp.b	#iop.pick,d0		are we picking?
	beq.s	pti_do			yes, ignore lock then
	cmp.b	#iop.wpap,d0		wallpaper?
	beq.s	pti_do			... yes, ignore lock then

	bsr	pt_bgio 		try background I/O
	bra	ptio_exit

ptio_nc
	moveq	#err.nc,d0
	bra.l	ptio_exit
*	 rts				 1.08

*
* do io operation
*
pti_do
	cmp.w	#iob.elin,d0		is it an input?
	bhi.s	pti_do1 		... no
	bclr	#sd.pick,sd_pick(a0)	... yes, no pick/wake

pti_grab
	move.l	a0,a4
	btst	#sd..prwn,sd_prwin(a0)	is it primary window?
	bne.s	pti_chkp		... yes
	move.l	sd_pprwn(a0),a4 	... no, find primary
	add.w	#sd.extnl,a4
pti_chkp
	lea	sd_prwlt(a4),a4 	check if at top
	cmp.l	pt_head(a3),a4
	bne.s	ptio_nc 		... no
*
pti_cptr
	tst.b	pt_pstat(a3)		is the pointer invisible or suppressed?
	bne.s	pti_getq		... yes, grab the queue
	tst.b	pt_reltm(a3)		... has pointer relaxed yet?
	bgt.s	ptio_nc      ;;;;
;	 ble.s	 pti_csup		 ... ... yes
;	 tst.w	 d3			 ... first entry?
;	 bne.s	 ptio_nc		 ... no
pti_csup
	moveq	#pt.supky,d4		... yes, suppress
	bsr.l	pti_hide		... and hide it
*
*	Grab keyboard queue if it's available
*
pti_getq
	move.b	#pt.supky,pt_pstat(a3)	... suppress for keystroke
	move.l	pt_dumq1(a3),a4 	point at...
	lea	sd.dq2(a4),a4		...second dummy queue
	cmp.l	sys_ckyq(a6),a4 	is it current?
	bne.l	pt_do_con		no, can't grab it
	move.l	pt_kqoff(a3),a4 	queues are this far offset
	lea	0(a0,a4.l),a4		so here's ours
	move.l	a4,sys_ckyq(a6) 	grab grab
	bra.s	pt_do_con
*
pti_do1
	cmp.w	#iop.outl,d0	      test if operation requires sprite remove
	bge.s	pt_rsprit
	cmp.w	#iop.rpxl,d0
	beq.s	pt_rsprit
	cmp.w	#iop.slnk,d0
	bge.l	pt_do_io
*
* remove the pointer sprite
*
pt_rsprit
	tst.b	pt_pstat(a3)		is sprite visible?
	bgt.s	pt_do_con		... no, suppressed
	beq.s	pt_rspdo		... yes, remove it
	cmp.b	#pt.show,pt_pstat(a3)	show next time?
	blt.s	pt_do_con		... no
	move.b	#pt.supio,pt_pstat(a3)	... yes, kill that
	bra.s	pt_do_con

pt_rspdo
	cmp.b	#iow.defw,d0		window redefinition?
	bne.s	pt_rschk		... no, check sprite against window
	tst.w	d2			any border?
	bne.s	pt_rhide		... yes, any size could fail $$$$$$$$$
*
pt_rschk
ovreg	reg	d0-d2
	movem.l ovreg,-(sp)
	move.l	pt_psprt(a3),a4 	point at sprite
	move.l	pt_pos(a3),d2		this is where its origin is
	sub.l	pto_org(a4),d2		so this is its screen origin
	moveq	#$fffffff0,d0
	swap	d0
	and.l	d0,d2			origin of save area
	move.l	#$001f0000,d1		round up width (and add one)
	add.l	d2,d1			TT special !!!
	and.l	d0,d1
	add.l	pto_size(a4),d1 	and how far across it goes
	move.l	d1,d0
	swap	d0
	cmp.w	pt_ssizx(a3),d0 	off right?
	blt.s	io_ovchk
	ext.l	d2			; ... check whole screen width
io_ovchk
	bsr.l	io_tstw 		is it in our window?
	movem.l (sp)+,ovreg
	ble.s	pt_do_con		... no
*
pt_rhide
	tst.b	pt_reltm(a3)		relaxed?
	bgt.l	ptio_nc 		... no
	moveq	#pt.supio,d4		... yes, suppress for io timeout 
	bsr.l	pti_hide		... and hide it
pt_do_con
	cmp.w	#iop.wpap,d0		can we do it?
	bge.s	pt_do_io		yes, we will then
*
*	Use existing I/O code: but if it's SD.WDEF, do some ourselves
*	If it is set_size, set increments on return
*
	cmp.w	#iow.ssiz,d0		is it set_size?
	bne.s	chk_wdef		... no
	tst.b	d1			is it enquiry?
	blt.s	ptio_siok		... yes
	jsr	cn_io			... no, do normal
	beq.s	ptio_sinc
ptio_siok
	moveq	#0,d0
ptio_sinc
	move.l	sd_xinc(a0),d1		set increments
	bra.l	ptio_exit
*
chk_wdef
	cmp.b	#iow.defb,d0		is it define border?
	beq.s	do_defb
	cmp.b	#iow.defw,d0		is it define window?
	bne.s	do_norm 		no

	clr.b	pt_schfg(a3)		something moved
	tst.b	sd_curf(a0)		cursor visible?
	bgt.s	ptio_cwd		... yes
	jsr	pt_wdef(pc)		... no, do wdef ourselves
	bra.s	ptio_bdr
ptio_cwd
	movem.w d1/d2,-(sp)		save regs
	moveq	#iow.dcur,d0		disable cursor
	jsr	cn_io
	jsr	pt_wdef(pc)		do wdef ourselves
	move.l	d0,-(sp)
	moveq	#iow.ecur,d0		re-enable cursor
	jsr	cn_io
	move.l	(sp)+,d0
	movem.w (sp)+,d1/d2  
ptio_bdr
	bne.l	ptio_exit
	moveq	#iow.defb,d0		do the border as with the old code
*
do_defb
	clr.b	pt_schfg(a3)		border changed
do_norm
	jsr	cn_io			do standard console driver
	bra.l	ptio_exit
*
	page
*
pt_do_io
	cmp.w	#iop.wrst,d0	out of range
	bgt.l	err_bp
	cmp.w	#iop.outl,d0	outline?
	bne.s	pt_do_cr
	btst	#sd..prwn,sd_prwin(a0)	is it primary window?
	beq.s	pt_do_cr		... no

	movem.l d0/d1/d2/a0/a1/a2,-(sp)
pt_dcsec
	move.l	sd_sewll(a0),d0 	; get next secondary
	beq.s	pt_dcsdone		; no more, done
	move.l	d0,a0			; point to...
	sub.w	#sd_sewll,a0		; ...normal cdb
	tst.b	sd_curf(a0)		; cursor visible?
	ble.s	pt_dcsec		; ... no
	moveq	#iow.dcur,d0		; hide cursor
	jsr	cn_io
	st	sd_curf(a0)		; but leave enabled
	bra.s	pt_dcsec

pt_dcsdone
	movem.l (sp)+,d0/d1/d2/a0/a1/a2
pt_do_cr
	tst.b	sd_curf(a0)		cursor visible?
	ble.s	pt_iojmp		... no

	movem.l d0/d1/d2/a1/a2,-(sp)	save regs
	moveq	#iow.dcur,d0		disable cursor
	jsr	cn_io
	addq.b	#1,sd_curf(a0)		but mark still enabled
	movem.l (sp)+,d0/d1/d2/a1/a2
	bsr.s	pt_iojmp		do operation
	add.w	#sd.extnl,a0
	tst.b	sd_curf(a0)
	ble.s	ptio_exit
	clr.b	sd_curf(a0)
	movem.l d0/d1/d2/a1/a2,-(sp)	*** 1.08 (in old version, regs saved...
	moveq	#iow.ecur,d0		re-enable cursor
	jsr	cn_io
	movem.l (sp)+,d0/d1/d2/a1/a2	*** 1.08 ... were not regs restored)
	tst.l	d0
	bra.s	ptio_exit

pt_iojmp
	sub.w	#iop.wpap,d0
	add.w	d0,d0
	move.w	jump_tab(pc,d0.w),d0
	jsr	jump_tab(pc,d0.w)
ptio_exit
	sub.w	#sd.extnl,a0
ptio_rts
	rts
*
jump_tab
	dc.w	pt_wpap-jump_tab
	dc.w	pt_flims-jump_tab
	dc.w	pt_psave-jump_tab
	dc.w	pt_prest-jump_tab
	dc.w	pt_slink-jump_tab
	dc.w	pt_pinf-jump_tab
	dc.w	pt_rptr-jump_tab
	dc.w	pt_rpixl-jump_tab
	dc.w	pt_wblb1-jump_tab
	dc.w	pt_lblob-jump_tab
	dc.w	err_bp-jump_tab
	dc.w	pt_wspr1-jump_tab
	dc.w	pt_spray-jump_tab
	dc.w	err_bp-jump_tab
	dc.w	pt_splm-jump_tab
	dc.w	pt_outln-jump_tab
	dc.w	pt_sptr-jump_tab
	dc.w	pt_pickw-jump_tab
	dc.w	pt_swdef-jump_tab
	dc.w	pt_wsave-jump_tab
	dc.w	pt_wrest-jump_tab
err_bp
	moveq	#err.ipar,d0
	rts
err_nc
	moveq	#err.nc,d0
	rts
*
pt_slink
	lea	(a3,d1.w),a2		address to update
	bra.s	pt_slend
*
pt_sloop
	move.b	(a1)+,(a2)+		update it a bit
pt_slend
	dbra	d2,pt_sloop
	move.l	a3,a1			return base of dddb
rts_ok
	moveq	#0,d0
	rts
*
pt_pinf
	move.l	#pt.vers,d1		set pointer version number
	move.l	pt_wman(a3),a1		and interface vector
	bra.s	rts_ok
	page
*
pti_hide
	sf	pt_cwbsy(a3)		clear window busy count
pt_hides
	tst.b	pt_pstat(a3)		is it visible?
	bne.s	pth_setv		... no
	clr.b	pt_wmove(a3)		clear move flag
	movem.l d0-d7/a0-a6,-(sp)	**** check this register list (esp a5)
	move.l	pt_addr(a3),a2		set old screen address
	move.l	pt_pspck(a3),a5 	set old screen sprite (for coords)
	bsr.l	pt_saddp		set sprite addresses
	bsr.l	sp_remove		remove sprite
	movem.l (sp)+,d0-d7/a0-a6
	st	pt_pstat(a3)
pth_setv
	cmp.b	pt_pstat(a3),d4 	more or less visible 
	bhi.s	pth_rts 		... already less visible
	move.b	d4,pt_pstat(a3) 	reset visibility flag
pth_rts
	rts

	end
