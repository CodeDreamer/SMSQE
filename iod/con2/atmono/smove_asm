* Screen move				v1.02   May 1987  J.R.Oakley  QJUMP
*
*	Move the overlapping part from one save area to another.  These are
*	called SAVE and SCREEN in the comments, since the routine will move
*	a save area to another "save area" which is actually in the screen
*	memory.
*
*	Registers:
*		Entry			Exit
*	A0	^ screen cdb		preserved
*	A1	^ save cdb		preserved
*	A3	^ dddb			preserved
*
	section driver
*
	include 'dev8_keys_con'
	include 'dev8_keys_iod'
	include 'dev8_keys_chn'
*
	xref	pt_mblock
	xref	pt_hides
*
	xdef	pt_smove
*
reglist reg	d1-d7/a0-a4
*
pt_smove
	movem.l reglist,-(sp)
*
*	Mung the data given into a form suitable for the restore or swap
*	routines.  This consists of figuring out where the overlap actually
*	is, and generating start addresses and line offsets for each save
*	area, and a long words/line and line count.
*
*	Registers:
*		Entry				Exit
*	D0					smashed
*	D1					size of overlap
*	D2					origin of source
*	D3					origin of dest
*	D4					smashed
*	A0	^ dest cdb			preserved
*	A1	^ source cdb			preserved
*	A2					row increment in source
*	A3					row increment in dest
*	A4					base of source save area
*	A5					base of dest save area
*
	bsr	psm_getx		; get minimum and maximum x co-ords
	ble.l	psm_exit		; no size!
	addq.l	#2,a0
	addq.l	#2,a1			; point to y
	bsr.l	psm_getx		; and get y
	subq.l	#2,a0
	subq.l	#2,a1			; back to cdbs
	ble.s	psm_exit		; no size!
*
	tst.b	sd_wlstt(a1)		; source locked?
	blt.s	psm_slck		; yes, save area is valid
	tst.l	sd_wsave(a0)		; no, restore from screen...
	beq.s	psm_slck		; ...unless we're restoring to it!
	bsr	psm_hspr		; better hide the sprite
	move.l	sd_scrb(a1),a4		; use screen as source
	move.l	chn_drvr-sd.extnl(a1),a2
	move.w	pt_bytel-iod_iolk(a2),a2 ; and row increment
	add.l	sd_xhito(a1),d2 	; and real origin
	bra.s	psm_sdst
psm_slck
	move.l	sd_wsave(a1),d0 	; get source save area
	beq.s	psm_exit		; none, give up
	move.l	d0,a4
	moveq	#$3f,d0 				    ; $$$$$
	add.w	sd_xhits(a1),d0 	; source size, rounded up
	lsr.w	#3,d0			; to number of bytes ; $$$$$
	and.w	#$fffc,d0		; to nearest long word
	move.w	d0,a2			; that's row increment
*
	moveq	#$f,d0			   ; $$$$$
	and.w	sd_xhito(a1),d0 	; x-origin is offset this much in save
	swap	d0			 
	add.l	d0,d2			; so we really need to copy from here
*
psm_sdst
	move.l	sd_wsave(a0),d0 	; dest always uses save area
	bne.s	psm_svds		; there is one
*
	bsr.s	psm_hspr		; hide the sprite
	move.l	sd_scrb(a0),a5		; restore directly to screen
	move.l	chn_drvr-sd.extnl(a0),a3
	move.w	pt_bytel-iod_iolk(a0),a3 ; and row increment
	add.l	sd_xhito(a0),d3 	; and real origin
	bra.s	psm_move		; do the move
*
psm_svds
	move.l	d0,a5
	moveq	#$3f,d0 			 ; $$$$$
	add.w	sd_xhits(a0),d0 	; dest size, rounded up
	lsr.w	#3,d0			; to number of bytes  ; $$$$$
	and.w	#$fffc,d0		; to nearest long word
	move.w	d0,a3			; that's row increment
*
	moveq	#$f,d0				; $$$$$
	and.w	sd_xhito(a0),d0 	; x-origin offset
	swap	d0
	add.l	d0,d3			; add it in
*
psm_move
	jsr	pt_mblock		 ; and move an area
*
psm_exit
	movem.l (sp)+,reglist
	rts
	page
*
*	Calculate minimum and maximum x-coordinates, relative to the top
*	left-hand corner of the hit areas of source and destination
*	window/save areas.  These will be adjusted later to align with word
*	boundaries.
*
*	Registers:
*		Entry				Exit
*	D1					swapped, LSW=x size
*	D2					swapped, LSW=min x in source
*	D3					swapped, LSW=min x in dest
*	D4					smashed
*	A0	^ dest cdb			preserved
*	A1	^ source cdb			preserved
*
psm_getx
	swap	d1
	swap	d2
	swap	d3
*
	move.w	sd_xhito(a1),d3 	; start of source...
	move.w	sd_xhito(a0),d2 	; ...and of dest
	move.w	d3,d1			; assume dest is smaller
	move.l	d1,d4
	move.w	d2,d4			; and source larger
	cmp.w	d2,d1		       
	blt.s	psm_dsts		; yes, it is
	exg	d4,d1			; no, exchange source and dest origins
psm_dsts
	sub.w	d1,d2
	sub.w	d1,d3			; one of them starts at the left
*
*	Now find width of overlap
*
	move.w	sd_xhits(a1),d0 	; get the source...
	move.w	sd_xhits(a0),d1 	; ...and dest sizes
	add.w	sd_xhito(a1),d0 	; convert to... 
	add.w	sd_xhito(a0),d1 	; ...edge co-ordinates
	cmp.w	d0,d1			; we want the smaller
	blt.s	psm_swid		; dest is smaller
	move.w	d0,d1			; source is smaller, transfer to size
psm_swid
	sub.w	d4,d1			; overlap is this wide
psm_mmex
	rts
*
psm_hspr
	moveq	#pt.supsr,d4
	jmp	pt_hides(pc)
*
	end
