* Define window outline    V1.05     1986  Jonathan Oakley  QJUMP
* 2003 Feb 16		    1.06	keep A1, as per documentation (wl)

	section driver
*
	include 'dev8_keys_con'
	include 'dev8_keys_err'
	include 'dev8_mac_assert'
*
	xdef	pt_outln
*
	xref	pt_shad
	xref	pt_carea
	xref	pt_trncs
	xref	pt_pick
	xref	pt_slock
	xref	pt_wsavh
	xref	pt_wsavo
	xref	pt_wrest
	xref	pt_wrstm
	xref	pt_wremv
*
pt_outln
	cmp.l	pt_cchad(a3),a0 	; move on current channel?
	bne.s	pto_do			; ... no
	assert	ptb.psup,-1
	st	pt_bsupp(a3)		; ... yes, suppress keypress and strokes

pto_do
	tst.b	d2			; is it move?
	beq.s	pt_doutln		; ... no
	btst	#sd..well,sd_behav(a0)	; already outlined?
	beq.s	poum_ipar
	move.l	a1,-(sp)		; save pointer
	move.l	d1,d7
	moveq	#0,d1			; save in new area if poss
	move.l	sd_wsave(a0),a4 	; save save area address
	clr.l	sd_wsave(a0)
	move.l	(a1),d0
	cmp.l	sd_xhits(a0),d0 	; size change?
	sne	-(sp)
	jsr	pt_wsavh(pc)		; save window
	bne.s	poum_exit		; ... oops
pto_moutl
	move.l	d7,d1
	move.l	sd_wsave(a0),-(sp)	; save new save area address
	move.l	a4,sd_wsave(a0) 	; set old save area
	move.l	sd_xhito(a0),-(sp)	; save old x origin
	bsr.s	pt_doutln		; reset outline
*
	move.l	d0,d4			; error
	move.l	(sp)+,d0		; old x origin (in save area)
	moveq	#0,d2			; remove save area
	sub.l	a1,a1
	move.l	sd_wsave(a0),d7 	; might need to keep save area
	move.l	(sp)+,sd_wsave(a0)	; restore from new save area
	jsr	pt_wrstm(pc)		; restore moved window

	tst.b	sd_prwin(a0)		; primary window?
	bmi.s	poum_rer		; yes, just return
	move.l	d7,sd_wsave(a0) 	; no, keep secondary's save area
*
poum_rer
	move.l	d4,d0			; set error from outln
	bne.s	poum_exit		; ... there was one
	tst.b	(sp)			; bad move?
	beq.s	poum_exit		; ... no
	moveq	#err.ipar,d0		; size has changed
poum_exit
	addq.w	#2,sp
	move.l	(sp)+,a1
	tst.l	d0
	rts

poum_ipar
	moveq	#err.ipar,d0
	rts

	page
*
* reset the outline
*
pt_doutln
	movem.l (a1),d6/d7		; get window size/origin parameters
	addq.l	#1,d6			; size = -1?
	beq.s	pou_ns			; ... yes  ** 1.06
	move.l	a1,-(sp)		; keep a1  ** 1.06
	bsr	pou_soutd6
	bra.s	pou_rt2

pou_ns	btst	#sd..well,sd_behav(a0)	; already outlined?
	bne.s	pou_rtsok
	movem.l sd_xhits(a0),d6/d7	; ... yes, use current hit area
	move.l	a1,-(sp)		; make sure this is kept even if secondary wdw
	move.l	sd_xsize(a0),-(sp)	; save size
	move.l	sd_xmin(a0),-(sp)
	bsr.s	pou_soutl
	move.l	(sp)+,sd_xmin(a0)	; reset size
	move.l	(sp)+,sd_xsize(a0)
pou_rt2 move.l	(sp)+,a1		: get A1 back
	tst.l	d0
	rts

pou_rtsok
	moveq	#0,d0
	rts
pou_soutd6
	subq.l	#1,d6
pou_soutl
	moveq	#16,d0
	bclr	d0,d1
	bclr	d0,d6			; x must be even
	bclr	d0,d7
	move.l	d6,d4			; shadowless
	move.l	d7,d5
*
	move.l	d1,d0			; and save the shadow
	bsr.l	add_shad		; add x shadow...
	bsr.l	add_shad		; ...and y shadow
*
	move.l	pt_ssize(a3),d2 	; get x and y max
	move.l	#0,d3			; and min
	tst.b	sd_prwin(a0)		; is it a primary?
	bmi.s	pou_chk 		; ... yes
	move.l	sd_pprwn(a0),a2 	; get primary window
	movem.l sd_xhits+sd.extnl(a2),d2/d3 ; and primary hit area
pou_chk
	jsr	pt_carea(pc)		; check areas
	bne.s	pou_rts 		; ...oops
*
	move.l	d1,d3			; save shadow parameters
	bset	#sd..well,sd_behav(a0)	; is it well behaved? (it is now)
	beq.s	pou_ssiz		; no, don't remove
	cmp.b	#sd.wbsec,sd_behav(a0)	; is it secondary?
	beq.s	pou_srem		; ... yes
	jsr	pt_wremv(pc)		; ... no, remove all trace from screen
	bra.s	pou_ssiz
pou_srem
	moveq	#-1,d2			; keep save area
	sub.l	a1,a1			; use existing save area
	jsr	pt_wrest(pc)		; restore area under secondary
*
pou_ssiz
	movem.l d4/d5/d6/d7,sd_xhits(a0)  ;set hit area, shadow area
*
	move.w	sd_borwd(a0),d6 	; adjust for border...!
	move.w	d6,d7
	add.w	d6,d6
	swap	d6
	move.w	d7,d6
	add.l	d6,d5
	add.l	d6,d6
	sub.l	d6,d4
*
	move.l	d4,sd_xsize(a0) 	; and window size
	move.l	d5,sd_xmin(a0)		;
	tst.b	sd_prwin(a0)		; is it a primary?
	bge.s	pou_sec 		; no, a secondary
	jsr	pt_trncs(pc)		; truncate secondaries if required
	jsr	pt_pick(pc)		; pick to top
	jsr	pt_slock(pc)		; set locks
	bra.s	pou_shad
pou_sec
	move.l	d1,-(sp)		; save shadow size
	moveq	#0,d1			; create new save area
	jsr	pt_wsavo(pc)		; save area beneath outline
	movem.l (sp)+,d1		; restore shadow without setting flags
pou_shad
	beq.l	pt_shad 		; ok, put shadows on
*
pou_rts
	rts
*
add_shad
	swap	d0
	swap	d6
	swap	d7
	tst.w	d0			; what's the shadow?
	bpl.s	shad_pl 		; positive
	add.w	d0,d7			; negative, knock it off the origin
	neg.w	d0			; and get the absolute value
shad_pl
	add.w	d0,d6			; add shadow to size
	rts
	end
