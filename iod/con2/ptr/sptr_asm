* Set pointer position	 V1.01	  1986   Tony Tebby	QJUMP
*
	section driver
*
	xdef	pt_sptr
	xdef	pt_cptr
*
	xref	pt_fwind
*
	include 'dev8_keys_con'
	include 'dev8_keys_err'
	include 'dev8_mac_assert'
*
pt_cptr
	move.l	sd_sewll(a0),d0 	; any secondary?
	beq.s	ptcp_set		; ... no
	move.l	a0,-(sp)
ptcp_scloop
	move.l	d0,a0			; ... yes
	assert	sd_sewll,0
	btst	#sd..well,sd_behav(a0)	; well behaved?
	bne.s	ptcp_sec		; ... yes
	move.l	sd_sewll(a0),d0
	bne.s	ptcp_scloop
	bra.s	ptcp_prim		; no suitable secondary, do primary

ptcp_sec
	bsr.s	ptcp_set
	move.l	(sp)+,a0
	rts

ptcp_prim
       move.l	(sp)+,a0		; restore primary

ptcp_set
	move.l	sd_xhits(a0),d1 	; hit size
	lsr.l	#1,d1			; /2 gives centre of window
	bclr	#15,d1			; in case X was odd
	bra.s	ptsp_hit		; add origin and set pointer
*
pt_sptr
	tst.b	d2			window relative?
	beq.s	ptsp_do 		... no
	bgt.s	ptsp_hit		... hit relative
	add.l	sd_xmin(a0),d1		set position in screen coordinates
	bra.s	ptsp_do
ptsp_hit
	add.l	sd_xhito(a0),d1 	set position
ptsp_do
	move.l	pt_ssize(a3),d0
	cmp.w	d0,d1			y out of range?
	bhs.s	pt_or			... yes
	cmp.l	d0,d1			x out of range?
	bhs.s	pt_or
	move.l	d1,pt_npos(a3)		set new position
*
;;	move.b	#pt.freez,pt_freez(a3)	freeze the pointer for a bit

	clr.b	pt_schfg(a3)		scheduler busy
	clr.b	pt_offscr(a3)		cannot SET offscreen
	move.l	a1,-(sp)
	jsr	pt_fwind		... but find window anyway
	move.l	(sp)+,a1
	moveq	#0,d0
	rts
pt_or
	moveq	#err.orng,d0		out of range
	rts
	end
