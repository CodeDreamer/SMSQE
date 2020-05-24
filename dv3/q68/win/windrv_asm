; Q68 WIN_DRIVE & others Extension thing  v. 1.02 (c) W. Lenerz 2017
;
; 1.02 2020-03-30 no need to read card when setting drive via win_drive
; 1.01 removed reference to keys_java and cv_upcas, list clearing optimisation (mk)

	section exten

	xdef	win_drv
	xdef	hdt_doact
	xdef	hd_byte

	xref	hd_hold 		; reserve the interface
	xref	hd_release		; release the interface
	xref	dv3_usep
	xref	dv3_acdef
	xref	dv3_close		; close channel routine
	xref	norm_nm 		; copy and normalize name
	xref	win_chk

	xref	inicrd

	xref	gu_achpp
	xref	gu_rchp
				
	include 'dev8_keys_q68'
	include 'dev8_keys_iod'
	include 'dev8_keys_thg'
	include 'dev8_keys_sys'
	include 'dev8_keys_err'
	include 'dev8_mac_thg'
	include 'dev8_mac_assert'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'
	include 'dev8_keys_qlv'


hd_drv	 dc.w	thp.ubyt	 ; drive
	 dc.w	thp.ubyt	 ; card nbr
	 dc.w	thp.call+thp.str ; name string
	 dc.w	0

hd_byte dc.w	thp.ubyt,0

;+++
; WIN_DRIVE drive, card_nbr,name$
;---
; When the user issues the win_drive command, the card may have changed or not.
; To find out whether the card concerned has changed, try to read sector 0 from
; it. If I can read the sector, the card hasn't changed. If I can't, the card
; has changed. I must then initialise the card and see whether I can reach it.
; I must also close all drive defns for that card, if there were any.
;

; first of all, check whether I can read the first sector of the drive
; if I can, the card hasn't changed

no_hold moveq	#err.fdiu,d0
wr_exit rts

win_drv thg_extn {DRIV},win_safe,hd_drv
hdt.reg reg	d1-d7/a0-a5

	movem.l hdt.reg,-(sp)
	bsr.l	hdt_doact		; call following code as device driver

; code called as device driver, in supervisor mode with a3 pointing to device
; definition block and a4 pointing to drive defn (if it exists)
; on entry, codition code = Z if a drive definition is found
;	d0 c	0 or error if no drive definition was found
;	d7 c  p drive ID / number  (from 1 to 8)
;	a1 c  p address of thing  (param ptr after that for the drive)
;	a3 c  p device driver linkage block
;	a4 c  p drive definition block (if it exists)
;

	move.l	(a1)+,d1		; card (1 or 2)
	subq.w	#1,d1
	blt.s	err_crd 		; wrong card nbr
	cmp.w	#1,d1			; if it isn't 0 must be 1
	bgt.s	err_crd 		; but it isn't
	lsl.l	#q68_dshft,d1
	move.l	d0,d4			; keep
goodnbr lea	$10(a1),a1
	move.l	a1,d6			; keep parameter pointer
	move.w	(a1),d2 		; is there a name?
	blt.s	err_crd 		; ???????
	beq.s	sdc_fnd 		; there isn't so erase the current one
	cmp.w	#12,d2			; an 8.3 name?
	bgt.s	hdt_badp		; no, error

sdc_fnd tst.l	d4			; is there a drive definition?
	bne.s	fndend			; no, so this is fine right now
	tst.b	ddf_nropen(a4)		; files open on this drive?
	bne.s	hdt_inus		; yes, can't do it!!!!!

; try to find this drive definition and delete it
	lea	sys_fsdd(a6),a0
	moveq	#$f,d0
fndlp	cmp.l	(a0)+,a4		; is this the one?
	beq.s	fndddb			; yes
	dbf	d0,fndlp		; no, try again
	bra.s	fndend			; hmm, strange that I didn't find it!
fndddb	clr.l	-(a0)			; drive has no defn block any more
	move.l	a4,a0
	movem.l d3/a0-a3/a6,-(a7)
	move.w	mem.rchp,a2		; release this block
	jsr	(a2)
	movem.l (a7)+,d3/a0-a3/a6

; I found and deleted this drive definition block (if any)
; now set the drive name in the config block
fndend
	move.l	d6,a0			; parameter pointer, string
	lea	hdl_unit-1(a3),a2
	move.b	d1,(a2,d7.w)		; this drive is on that card
	move.l	a1,d6
	move.l	hdl_targ(a3),a1 	; point to name strings
	move.w	d7,d1			; drive number
	subq.l	#1,d1
	lsl.w	#4,d1			; offset in drive name table
	add.w	d1,a1			; point to config name
	bsr	norm_nm 		; copy and normalize name
	move.l	d6,a1
	moveq	#0,d0
	rts
wr_out	move.l	a1,a0
	jmp	gu_rchp

hdt_inus
	moveq	#err.fdiu,d0
	bra.s	wr_out

; card wasn't 1 or 2
err_crd
	moveq	#err.orng,d0
	bra.s	wr_out

hdt_badp
	moveq	#err.ipar,d0
	bra.s	wr_out

; we come here if the card doesn't exist (couldn't read sector 0) or isn't
; initialized.
; In this case, check whether any of my drives are marked as being on
; this card, and if so, delete their drive defn block and show drive as not
; being assigned in device definition block list.

; check whether a drive is on that card
; D1 = card (0 or q68_coff)
; d7 = drive (1...8)

nocrd2	moveq	#1,d3			; first drive number to check
chklp1	lea	hdl_unit-1(a3),a2
	cmp.b	(a2,d3.w),d1		; is drive on this card?
	bne.s	chkendl 		; no, so no need to check
; find corresponding drive definition block
	lea	sys_fsdd(a6),a2
	move.l	a3,d4
	add.l	#iod_iolk,d4		; driver as it is in the chan def blk
	moveq	#$f,d0
dfd_fdrv
	move.l	(a2)+,a4
	cmp.l	ddf_ptddl(a4),a3	 ; our driver?
	bne.s	dfd_next		 ; ... no
	cmp.b	ddf_dnum(a4),d3 	 ; our drive?
	beq.s	chkchn			 ; ... yes, check channels open to it
dfd_next
	dbf	d0,dfd_fdrv
	bra.s	chkendl 		; no corresponding drive defn found

; as of here, a4 = drive defn block, -4(a2) = entry in defn blokck table
; close all channels to this defn blk
chkchn
	move.l	sys_chtb(a6),a5 	; point to first channel
	move.l	sys_chtt(a6),d2
ch_lp1	move.l	(a5)+,d0
	bmi.s	ch_next 		; channel is closed
	move.l	d0,a0
	cmp.l	d3c_drvr(a0),d4 	; is it for our driver?
	bne.s	ch_next 		; no

	assert	d3c_flid,d3c_drid-4,d3c_drnr-6,d3c_ddef-8
	cmp.l	d3c_ddef(a0),a4 	; is this a channel to this defn blk ?
	bne.s	ch_next 		; no

	move.b	#d3c.asect,d3c_atype(a0); yes, pretend this is a direct sector access channel
	jsr	dv3_close		; close this channel now
ch_next cmp.l	d2,a5			; unless we're done with all channels
	blt.s	ch_lp1

; delete corresponding drive defn block
	move.l	a4,a0
	clr.l	-(a2)			; clear pointer
	movem.l d3/a1/a3/a6,-(a7)
	move.w	mem.rchp,a2		; release this block
	jsr	(a2)
	movem.l (a7)+,d3/a1/a3/a6

chkendl addq.w	#1,d3
	cmp.b	#9,d3			; do for all 8 drives
	bne.s	chklp1
	rts


; general do action

hdt_doact
	move.l	(sp)+,a0		 ; action routine
	lea	-ddl_thing(a2),a3	 ; master linkage

	move.l	(a1)+,d7		 ; drive number
	ble.s	hdt_fdnf		 ; ... oops
	cmp.w	#8,d7
	bhi.s	hdt_fdnf
	move.w	#hdl_part-1,d3
	add.l	d7,d3			 ; offset  in linkage

hdt_actest
	tst.b	(a3,d3.w)		 ; any partition?
	bge.s	hdt_acdef
	move.l	ddl_slave(a3),a3	 ; another slave
	move.l	a3,d0
	bne.s	hdt_actest
	lea	-ddl_thing(a2),a3	 ; master linkage
	moveq	#0,d3			 ; no partition

hdt_acdef
	jsr	dv3_acdef		 ; action on definition

hdt_exit
	movem.l (sp)+,hdt.reg
	rts

hdt_fdnf
	moveq	#err.fdnf,d0		 ; no such drive number
	bra.s	hdt_exit

;-------------------------------------------------------------------

;+++
; WIN_SAFE n
;---
; This checks that it is safe to remove a card.  It is safe to remove a card
; if no channels are open to a drive on this card. If any channel is open
; to a drive on the card, this command fails with an in_use error.
; Before doing anything else, this flushes the buffers for the device.
; As a precautionary measure, if there is no channel open to any drive on this
; card, but there are drive definition blocks for drives on this card, the
; drive definition blocks are removed.
win_safe thg_extn {SAFE},card_init,hd_byte
	  
	movem.l hdt.reg,-(sp)
	bsr.s	hdt_doact		; call following code as device driver

; this is now called in supervisor mode
	
	move.l	d7,d1			; card nbr
	subq.w	#1,d1
	blt	err_crd 		; wrong card nbr
	cmp.w	#1,d1			; if it isn't 0, must be 1
	bgt	err_crd 		; but it isn't
	lsl.w	#q68_dshft,d1		; 0 or q68_coff

; flush all buffers
; check all drive definition blocks for channels open
	moveq	#0,d7			;
	moveq	#0,d3
	lea	hdl_unit-1(a3),a0; point to logical to phys table

	lea	sys_fsdd(a6),a2
	moveq	#0,d4
	move.l	sys_chtt(a6),d2
saf_lp
	move.l	(a2)+,a4		; drive defn block
	cmp.l	ddf_ptddl(a4),a3	; our driver?
	bne.s	saf_nxt 		; ... no
	move.b	ddf_dnum(a4),d7 	; drive number
	cmp.b	(a0,d7.w),d1		; is drive on this card?
	bne.s	saf_nxt 		; no, so no need to check
	bset	d4,d3			; this drv dfn blk needs to be removed
; flush all buffers for this drive
; (this will actually flush buffers for all drives of this device)
flush	moveq	#-1,d0
	jsr	ddl_fflush(a3)		; flush all buffers for both cards (!)
	addq.l	#-err.nc,d0		; not complete?
	beq.s	flush			; ... yes, wait
	subq.l	#-err.nc,d0		; any other error?
	bne.l	hdt_inus		; ... yes, oops

; check for channels open to this defn blk
; this check will fail if 256 files are open on device
;	 tst.b	 ddf_nropen(a4) 	 ; files open on this drive?
;	 bne	 hdt_inus		 ; yes, removal is not safe

; check for channels open to this defn blk
	move.l	a3,d6
	add.l	#iod_iolk,d6
	move.l	sys_chtb(a6),a5 	; point to first channel
ch_lp2	move.l	(a5)+,d0		; ptr to chan defn blk
	bmi.s	nxt_ch			; channel was closed
	move.l	d0,a0
	cmp.l	d3c_drvr(a0),d6 	; is it for our driver?
	bne.s	nxt_ch			; no
	cmp.l	d3c_ddef(a0),a4 	; is this a channel to this defn blk ?
	beq	hdt_inus		; yes! leave with error
nxt_ch	cmp.l	d2,a5			; unless we're done with all channels
	blt.s	ch_lp2

saf_nxt addq.w	#1,d4			; next drv defn block
	cmp.w	#$10,d4
	bne.s	saf_lp

; d3 now is a bitmap of drv defn blocks to be deleted
	moveq	#$f,d4			; loop counter,there are 16 blocks max
	lea	sys_fsdd+$40-4(a6),a5	; end of drv defn blk list
del_lp	btst	d4,d3			; remove this?
	beq.s	do_dellp		; no
	move.l	(a5),a0
	clr.l	(a5)			; clear pointer
	movem.l d3/a1/a3/a6,-(a7)
	move.w	mem.rchp,a2		; release this block
	jsr	(a2)
	movem.l (a7)+,d3/a1/a3/a6
do_dellp
	subq.l	#4,a5
	dbf	d4,del_lp
	moveq	#0,d0
	rts

; -----------------------------------------------------------------


;+++
; CARD_INIT n (n = card number, 1 or 2)
;---
card_init thg_extn {INIT},win_chk,hd_byte
	movem.l hdt.reg,-(sp)
	move.l	(a1),d7 		; card to init
	bsr	hdt_doact		; call following code as device driver

	move.l	d7,d1			; card (1 or 2)
	subq.w	#1,d1
	blt.s	bad_crd 		; wrong card nbr
	cmp.w	#1,d1			; if it isn't 0 must be 1
	bgt.s	bad_crd 		; but it isn't
	lsl.w	#q68_dshft,d1
	move.b	d1,hdl_unit-1(a3)
	clr.l	d7

; check whether I can read the first sector of the card
	move.l	#512,d0 		; reserve mem
	jsr	gu_achpp
	bne	wr_exit
	moveq	#1,d2
	move.l	a0,a1
	jsr	hdl_rsint(a3)		; read sector 0 of card
	beq	wr_out			; ok, rel. mem, card already initialised
	move.l	a1,a0
	jsr	gu_rchp 		; release mem

	bsr	nocrd2			; close all channels, delete defn blks
					; for all drives on this card
	jmp	inicrd			; try to init drive

bad_crd moveq	#err.orng,d0
ci_exit jmp	hd_release

ci_inus moveq	#err.fdiu,d0		; and come back with in use
	rts


	end
