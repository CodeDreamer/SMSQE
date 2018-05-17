* Window remove 			v1.06   1986  J.R.Oakley  QJUMP
*						 2002  Marcel Kilgus
*
* 2002-03-09  1.03  Changed for new on-the-fly wallpaper (MK)
* 2003-06-19  1.04  removed useless xref for cn_io (wl)
* 2006-03-22  1.05  Restore dddb before pt_fillsv. JS rom could smash a3 (MK)
* 2011-03-02  1.06  Save A3 during sms.rchp for QDOS systems (MK)
*
*	Remove a window.  To do this we must put the window at the bottom of
*	the pile, then go through the pile from bottom to top "restoring"
*	the contents of each primary window into the buried window's save area.
*	Finally the contents of the save area can be restored to the screen.
*
*	Registers:
*		Entry			Exit
*	D1				top bit set for no windows in mode D1.b
*	A0	cdb of removed window	preserved
*	A3	dddb for pointer	preserved
*
	section driver
*
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_qdos_io'
	include 'dev8_keys_sys'
	include 'dev8_keys_con'
*
	xref	pt_smove
	xref	pt_tstov
	xref	pt_wsave
	xref	pt_wrest
	xref	pt_fillsv
	xref.s	pt.spxlw ; shift pixel to long word
	xref.s	pt.rpxlw ; round up pixel to long word (2^spxlw-1)
*
	xdef	pt_alcsv
	xdef	pt_wremv
*
*	Allocate a save area for a window's hit area
*
*	Registers:
*		Entry				Exit
*	D0					error code
*	D1					size allocated
*	D2/D3					smashed
*	A0					area allocated
*	A1-A3					smashed
*	A4	window cdb			preserved
*
pt_alcsv
	moveq	#pt.spxlw,d0
	moveq	#pt.rpxlw,d1		; round up ...
	add.w	sd_xouts(a4),d1 	; ... width in pixels
	lsr.w	d0,d1			; ... width in long words
	addq.w	#1,d1			; one spare
	mulu	sd_youts(a4),d1 	; space required in words
	asl.l	#2,d1			; space in bytes

	moveq	#0,d2			; allocate for job 0
	moveq	#sms.achp,d0
	trap	#1
	tst.l	d0
	rts
*
reglist reg	d2/d3/a0-a5
stk.a3	equ	5*4
*
pt_wremv
	movem.l reglist,-(sp)
	move.l	a0,a4
	move.l	sd_wsave(a4),d0 	; first return the save area
	beq.s	alc_out 		; there isn't one
	tst.b	sd_mysav(a4)		; is it mine?
	beq.s	alc_out 		; no, mustn't return it then
	move.l	d0,a0
	move.l	a3,-(sp)		; QDOS smashes A3!
	moveq	#sms.rchp,d0		; return to the common heap
	trap	#1
	move.l	(sp)+,a3
*
alc_out
	move.b	pt_dmode(a3),d3
	cmp.b	sd_wmode(a4),d3 	; is the mode current?
	bne.l	exit			; no, don't restore, 'cos it's invisible
	tst.b	sd_wlstt(a4)		; unlockable?
	bgt.l	exit			; yes, dont restore
	move.l	sd_xouto(a4),sd_xhito(a4)
	move.l	sd_xouts(a4),sd_xhits(a4) ; save area big enough for outline
	bsr	pt_alcsv		  ; allocate it
	beq.s	ssave			; OK, set save area pointer
*
*	If we can't use a save area, then we must clear the outline to
*	background colour and use the screen directly.
*
oioreg	reg	d4-d7/a4
*
	lea	pt_head-sd_prwlt(a3),a0 ; point to head 
ptw_svul
	move.l	sd_prwlt(a0),d0 	; next window
	beq.s	ptw_clrw		; no more, clear outline
	move.l	d0,a0			; point to it
	sub.w	#sd_prwlt,a0		; the "real" cdb
	tst.b	sd_wlstt(a0)		; is it locked?
	bne.s	ptw_svul		; yes, or unlockable: do next
	cmp.b	#sd.wpapr,sd_behav(a0)	; wallpaper?
	beq.s	ptw_svul		; yes, next please
	movem.l sd_xouts(a4),d1/d2	; does it overlap this?
	add.l	d2,d1
	jsr	pt_tstov(pc)		; find out
	bne.s	ptw_svul		; no, it doesn't
*
	moveq	#0,d1			; use own save area
	jsr	pt_wsave(pc)		; and save if possible
	bra.s	ptw_svul		; do next window

*
ptw_clrw
	sub.l	a0,a0			; there's no save area
*
ssave
	move.l	stk.a3(sp),a3		; restore dddb
	move.l	a0,sd_wsave(a4) 	; point to save area
	st	sd_mysav(a4)		; it's all mine!
*
	move.l	a4,a0
	moveq	#-1,d2
	jsr	pt_fillsv		; fill area with background

	lea	pt_tail(a3),a4		; point to window at bottom of pile
	movem.l sd_xouts(a0),d1/d2
	add.l	d2,d1			; get edge co-ordinates
	moveq	#-1,d3			; top of D3 flags "no windows in mode"
	move.b	pt_dmode(a3),d3 	; and current mode to bottom
rest_loop
	move.l	(a4),d0 		; next window up
	beq.s	rest_end		; there isn't one, we've finished
	move.l	d0,a4
	lea	-sd_prwlb(a4),a1	; point to its CDB
	cmp.l	a0,a1			; is it us?
	beq.s	rest_loop		; yes, mustn't restore ourselves!
	cmp.b	sd_wmode(a1),d3 	; is its mode same as current?
	bne.s	rest_loop		; no, don't restore it
	tst.b	sd_wlstt(a1)		; is it unlockable?
	bgt.s	rest_loop		; yes, don't restore
	bclr	#31,d3			; something's in this mode
	exg	a0,a1			; compare correct sizes
	jsr	pt_tstov(pc)		; do the windows overlap?
	exg	a0,a1
	bne.s	rest_loop		; no, do nothing
*
	jsr	pt_smove(pc)		; restore into our save area
	bra.s	rest_loop
*
*	Now restore the window's hit area to the screen. Since we've
*	fiddled it, it's actually the outline...
*
rest_end
	moveq	#0,d2			; return the save area if it is ours
	sub.l	a1,a1			; restore from internal save area
	jsr	pt_wrest(pc)
*
exit
	move.l	d3,d1			; current mode/any windows left
	movem.l (sp)+,reglist
	rts
*
	end
