; Window save / restore  V 2.01    1999  Tony Tebby
;
; 2005-11-11  2.01  Uses line increment from CDB (MK)
;
	section driver
;
	xdef	pt_wsave
	xdef	pt_wsavh
	xdef	pt_wsavo
	xdef	pt_wrstm
	xdef	pt_wrest
;
	xref	pt_hides
	xref	pt_mblock
	xref.s	pt.spxlw ; shift pixel to long word
	xref.s	pt.rpxlw ; round up pixel to long word (2^spxlw-1)
	xref.l	pt.samsk ; save area origin mask
;
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_chp'
	include 'dev8_keys_chn'
	include 'dev8_keys_iod'
	include 'dev8_keys_con'
	include 'dev8_keys_err'
;
;	Save a window in a save area: either its own as pointed to
;	by SD_WSAVE, or a specially allocated one.
;
;	d1 c	length of supplied area (0 if sd_wsave to be used)
;	a0 c p	base address of channel block / pointer to heap
;	a1 c	pointer to area to save in (if d1<>0)
;	a2   s	pointer to screen
;	a3 c	pointer to linkage block
;	a4   s	saved base address of channel block
;
savregs reg	d1-d7/a0-a4
pt_wsave
	cmp.b	#sd.wbsec,sd_behav(a0)	; is it well behaved secondary
	bne.s	pt_wsavh		; ... no, save hit area
;
pt_wsavo
	movem.l savregs,-(sp)
	moveq	#sd_xouts-sd_xhits,d5	; save outline area
	bra.s	pws_do
;
pt_wsavh
	movem.l savregs,-(sp)
	moveq	#0,d5			; save hit area
;
;
;	First make sure the pointer is invisible
;
pws_do
	moveq	#pt.supsr,d4		; suppress for save restore
	jsr	pt_hides(pc)		; and hide it
;
	move.l	a0,a4			keep channel base safe
	move.l	d1,d4			and length of supplied area
	move.w	sd_xouts(a4),d1 	outline area width (pixels)
	moveq	#pt.spxlw,d0
	add.w	#pt.rpxlw,d1		round up to...
	lsr.w	d0,d1			...width in long words
	addq.w	#1,d1			one spare
	move.w	d1,d6			save that
	mulu	sd_youts(a4),d1 	space required in words
	beq.s	pts_ok			none, don't bother then
	asl.l	#2,d1			space in bytes
;
	tst.l	d4			are we given a save area?
	beq.s	find_save		no, find a save area
	cmp.l	d4,d1			is it big enough?
	bgt.s	find_save		no
	bsr.s	bin_save		yes, sling out the existing area
	move.l	a1,a0			point to the new one
	move.l	d4,d1			it's this big
	sf	d2			it's not ours
	bra.s	do_save 		and save into it
;
find_save
	move.l	sd_wsave(a4),a0 	do we have a save area?
	move.l	a0,d0
	beq.s	get_save		no, get one
	cmp.l	sd_wssiz(a4),d1 	is the existing space enough?
	ble.s	enuf_save		yes
	move.l	d1,d4			no, keep the amount required...
	bsr.s	bin_save		...bin the old area...
	move.l	d4,d1			...and get some more
;
get_save
	move.l	a3,-(sp)		allocate common heap
	moveq	#sms.achp,d0
	move.l	chp_ownr-sd.extnl(a4),d2 keep the space myself
	trap	#1
	move.l	(sp)+,a3
	move.l	d0,d3
	beq.s	got_save		... ok
	addq.l	#-err.ijob,d0		invalid job?
	beq.s	pts_exit		... yes, cannot save
;
	lea	ipc_rude(pc),a3 	make a rude noise
	moveq	#sms.hdop,d0
	trap	#1
	move.l	d3,d0			restore error
	bra.s	pts_exit
;
got_save
	lea	pt_nuldr(a3),a1 	null driver
	move.l	a1,chp_drlk-chp.len(a0) in header
	sub.l	#chp.len,d1		amount of space
	st	d2			is ours
;
do_save
	move.l	a0,sd_wsave(a4) 	set pointer in cdb
	move.l	d1,sd_wssiz(a4) 	set length in cdb
	move.b	d2,sd_mysav(a4) 	set save area owner
;
enuf_save
	move.l	sd_xhito(a4,d5.w),d7	offset to save with
	bsr	pts_cprm		calculate save parameters
;
	jsr	pt_mblock		move from screen to save
;
pts_ok
	moveq	#0,d0
pts_exit
	movem.l (sp)+,savregs
pts_rts
	rts
;
;	Remove an existing save area if it is owned by us
;
;	A4	^ cdb
;
bin_save
	tst.b	sd_mysav(a4)		is it mine to discard?
	beq.s	no_bin			no, just exit
	move.l	sd_wsave(a4),d0 	point to space to release
	beq.s	no_bin			there isn't one
	clr.l	sd_wsave(a4)		flag no current save area
	move.l	d0,a0
	move.l	a3,-(sp)
	moveq	#sms.rchp,d0
	trap	#1
	move.l	(sp)+,a3
no_bin
	rts
	page
;
;	Restore the contents of a save area into the screen memory, in the
;	location given by the window's hit area.
;
;	d2 c	byte set if do not remove item from heap
;	a0 c p	base address of channel block / pointer to heap
;	a1 c	save area to restore from (0 if sd_wsave)
;	a2   s	pointer to screen
;	a4   s	saved base address of channel block
;
rstregs reg	d1-d7/a0-a4
;
pt_wrest
	movem.l rstregs,-(sp)
	moveq	#0,d5			; restore hit area
	cmp.b	#sd.wbsec,sd_behav(a0)	; really?
	bne.s	pwr_oset		; ... yes
	moveq	#sd_xouts-sd_xhits,d5
pwr_oset
	move.l	sd_xhito(a0,d5.w),d0	; window not moved
	bra.s	pwr_do
;
pt_wrstm
	movem.l rstregs,-(sp)
	moveq	#0,d5			; restore hit area
;
;	first make sure the pointer is invisible
;
pwr_do
	moveq	#pt.supsr,d4		; suppress for save / restore
	jsr	pt_hides(pc)		; and hide it
;
	move.l	a0,a4			keep channel base safe
	move.l	d0,d7			and old origin
;
remove
	move.l	a1,d0			given save area to restore from?
	bne.s	use_giv 		yes
	move.l	sd_wsave(a4),a0 	point to save area
	move.b	sd_mysav(a4),d0 	can we throw it away?
	not.b	d0
	or.b	d0,d2			only if it is ours and we want to
	move.l	a0,a1			save pointer
	tst.b	d2			discard our space?
	bne.s	do_rest 		no
	clr.l	sd_wsave(a4)		yes, flag it
	bra.s	do_rest
use_giv
	move.l	a1,a0			use given area

do_rest
	move.l	a0,d0			just where are we restoring from...?
	beq.s	ptr_ok			AHA! Nowhere. I won't do it then.
;
rmvreg	reg	d2/a1/a4
	movem.l rmvreg,-(sp)		save some useful things
;
	bsr.s	pts_cprm		calculate save parameters
	exg	d2,d3
	exg	a2,a3			swap them to get restore parameters
	exg	a4,a5
;
	jsr	pt_mblock		move from save to screen
	movem.l (sp)+,rmvreg
;
	tst.b	d2			is item to be removed from heap?
	bne.s	ptr_ok			... no
	move.l	a1,a0			set address of heap item
	moveq	#sms.rchp,d0		and return it to the common heap
	trap	#1

; what about the shadow

ptr_ok
	moveq	#0,d0
ptr_exit
	movem.l (sp)+,rstregs
	rts
	page
;
;	Calculate parameters for saving a window
;
;	Registers:
;		Entry				Exit
;	D0					smashed
;	D1					size of window
;	D2					origin of window
;	D3					origin of window in save area
;	D5	offset hit to outline
;	A0	base of save area		preserved
;	A2					row increment in screen
;	A3					row increment in save area
;	A4	^ cdb				base of screen
;	A5					base of save area
;
pts_cprm
	move.l	sd_xhits(a4,d5.w),d1	set size
	move.l	d1,d2			size
	swap	d2
	moveq	#pt.spxlw,d0
	add.w	#pt.rpxlw,d2		round up to...
	lsr.w	d0,d2			...width in long words
	addq.w	#1,d2			one spare
	lsl.w	#2,d2
	move.w	d2,a3			is destination increment

	move.l	sd_xhito(a4,d5.w),d2	set origin
	move.l	d7,d3			and origin in...
	and.l	#pt.samsk,d3		...save area
;;;	   move.l  chn_drvr-sd.extnl(a4),a2
;;;	   move.w  pt_bytel-iod_iolk(a2),a2   screen increment
	move.w	sd_linel(a4),a2 	screen increment
;
;
	move.l	a0,a5			destination base address
	move.l	sd_scrb(a4),a4		source (screen) base
;
	rts
;
ipc_rude
	dc.b	$a			; sound
	dc.b	8			; 8 bytes of parameters
	dc.l	%1010101010101010	; send all 8 bytes of each
	dc.b	200			; pitch1 = 200
	dc.b	0
	dc.b	0,0
	dc.b	0,20			; duration = 5120
	dc.b	0,0
	dc.b	1			; no reply
	ds.w	0
;
	end
