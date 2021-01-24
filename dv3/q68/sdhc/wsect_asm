; DV3 SDHC Write Sector for Q68  1.03	 2016-2020 W. Lenerz
;
; 1.04 2020-05-12 implement 16 bit write (needs new firmware) (wl)
; 1.03 2018-05-09 save D6, use sysvars for card type (wl)
; 1.02 delete multisector write = SMSQE actually NEVER writes more than one sector, anyway
;      can use SD (not SDHC) cards (wl)
; 1.01 added multisector write (wl)

; loosely based on
; DV3 QXL Hard Disk Read Sector      1993     Tony Tebby

	section dv3

	xdef	hd_wdirect		; direct write sector
	xdef	hd_wsint		; internal write sector

	xref	hd_hold 		; reserve the interface
	xref	hd_release		; release the interface
	xref	snd_cmd 		; send a command to sdhc

	include 'dev8_keys_java'
	include 'DEV8_dv3_keys'
	include 'DEV8_keys_err'
	include 'DEV8_keys_sys'
	include 'DEV8_dv3_hd_keys'
	include 'dev8_keys_q68'

sendreg reg	d1-d7/a1-a3


;+++
; This routine writes a sector to an sd(hc) card, normally for direct sector I/O.
;+++
hd_wdirect
	tst.w	ddf_ftype(a4)		; really direct sector I/O?
	bge.s	hd_wsint		; no
	add.l	ddf_psoff(a4),d0	; yes, add "partition start"
	bra.s	hd_wsint

;+++
; This routine writes a sector to an sd(hc) card via background transfer.
;
;	d0 cr	sector number / error code
;	d2 c  p number of sectors (at least 1!)
;	d7 c  p drive ID / number  (from 0 to 7)
;	a1 c  p address to read from
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0 or ERR.TRNS
;
;	The sector number is for sectors 512 bytes long. The interface needs
;	the sector (or "block") number.
;---

hdw_inus
	rts
hd_wsint
	jsr	hd_hold 		; hold the drive for me
	bne	hdw_inus		; can't
	movem.l sendreg,-(a7)
	lea	hdl_unit-1(a3),a2	; point to "unit" = card for ths drive
	clr.l	d4
	clr.l	d1
	move.b	(a2,d7.w),d1
	move.l	#mmc1_cs,a2		; point to card physical addresses
	add.w	d1,a2			; a2 points to the correct addresses for this drive
	divu	#q68.coff,d1
	add.w	#sys_q8ct,d1
	move.b	(a6,d1.w),d4		; get card type (and shift amount)
	blt	hdw_err 		; card isn't initialized
no_l	st	drv_cs(a2)		; enable the card

;;;;; debug code --------------------------------
	genif	debug = 1

	cmp.l	#'gold',q68_jflg
	bne.s	nodebug
	move.l	d4,d1
	move.l	ddf_psoff(a4),d4

					; java interface
	move.l	d0,-(a7)		; will be popped up in java
	moveq	#jd3.wsec,d0
	dc.w	jva.trpa		; d0 will be set
	bra.s	hdw_dis
nodebug
	endgen
;;;;; end debug code  ------------------------------
						
	move.l	d0,d7			; (first) sector to write
	tst.b	q68_v2			; hardware v2?
	beq	eight_bit		; no->

	moveq	#2,d5			; token to write for 16 bit access
	subq.w	#1,d2			; prepare for dbf
	moveq	#-2,d6			; preset start write token

; first send command24	($58) to the card
; ($58, 4 bytes with the address to read from, $ff)
wrtloop
	move.l	d7,d1
	tst.w	d4			; use block address (sdhc) or byte address (sd)
	beq.s	blk_add 		; block address
	lsl.l	d4,d1			; byte, so * 512 (for SD, not SDHC cards)
blk_add moveq	#24,d0			; command24 ($58, 4 bytes with the address to read from, $ff)
	bsr	snd_cmd 		; send command now
	bne.s	hdw_dis 		; ooops
	tst.b	d1			; return needs to be 0
	bne.s	hdw_dis 		; ooops again

lp	st	drv_xfer(a2)		; get another byte back from card
	move.b	drv_read(a2),d0 	; (to send some clocks)
	move.b	d6,drv_writ(a2) 	; "start write" token
	st	drv_xfer(a2)

; now write 512 bytes, 16 bytes a pop
	lea	drv_xfer(a2),a3
	add.w	#drv_writ,a2
	move.w	#31,d3
sendlp
	move.w	(a1)+,(a2)		; copy word to write port
	move.b	d5,(a3) 		; start transfer
	move.w	(a1)+,(a2)
	move.b	d5,(a3)
	move.w	(a1)+,(a2)
	move.b	d5,(a3)
	move.w	(a1)+,(a2)
	move.b	d5,(a3)
	move.w	(a1)+,(a2)
	move.b	d5,(a3)
	move.w	(a1)+,(a2)
	move.b	d5,(a3)
	move.w	(a1)+,(a2)
	move.b	d5,(a3)
	move.w	(a1)+,(a2)
	move.b	d5,(a3)
	dbf	d3,sendlp		; do it for 512 bytes
		
	move.w	d3,(a2) 		; d3.w =$ffff due to dbf
	move.b	d5,(a3) 		; 2 CRCs

	st	(a2)			; dumy write

	sub.w	#drv_writ,a2		; point start port again
waitlp	st	(a3)
	cmp.b	#$ff,drv_read(a2)	; still busy?
	bne.s	waitlp			; yes ->

	addq.l	#1,d7			; next sector to write
	dbf	d2,wrtloop		;

hdw_ok	moveq	#0,d0			; done and ok
hdw_dis clr.b	drv_cs(a2)		; disable the card
hdw_out movem.l (a7)+,sendreg
	tst.l	d0			; necessary due to clr.b
	jmp	hd_release		; release & return
hdw_err
	moveq	#err.mchk,d0
	bra.s	hdw_dis

;
; 8 bit writes, for hardware v.1.00 and 1.01
;

eight_bit
	moveq	#-2,d6
	subq.w	#1,d2
wrtlp8	move.l	d7,d1
	tst.w	d4			; use block address (sdhc) or byte address (sd)
	beq.s	blk_ad8 		; block address
	lsl.l	d4,d1			; byte, so * 512
blk_ad8 moveq	#24,d0
	bsr	snd_cmd 		; send command now
	bne.s	hdw_dis 		; ooops
	tst.b	d1			; return needs to be 0
	bne.s	hdw_dis 		; ooops again

	st	drv_xfer(a2)		; get another byte back from card
	move.b	drv_read(a2),d0 	; (to send some clocks)

	move.b	d6,drv_writ(a2) 	; "start write" token
	st	drv_xfer(a2)

; now write 512 times one byte, 8 bytes a pop
	lea	drv_xfer(a2),a3
	add.w	#drv_writ,a2
	move.w	#63,d3
sendlp8
	move.b	(a1)+,(a2)		; copy byte to write port
	st	(a3)			; start transfer
	move.b	(a1)+,(a2)
	st	(a3)
	move.b	(a1)+,(a2)
	st	(a3)
	move.b	(a1)+,(a2)
	st	(a3)
	move.b	(a1)+,(a2)
	st	(a3)
	move.b	(a1)+,(a2)
	st	(a3)
	move.b	(a1)+,(a2)
	st	(a3)
	move.b	(a1)+,(a2)
	st	(a3)
	dbf	d3,sendlp8		; do it for 512 bytes
		
	move.b	d3,(a2) 		; d3.b =$ff due to dbf
	st	(a3)
	move.b	d3,(a2)
	st	(a3)			; send 2 fake CRCs
	st	(a2)			; dumy write
	sub.w	#drv_writ,a2		; point start port again

waitlp8 st	(a3)
	cmp.b	#$ff,drv_read(a2)	; still busy?
	bne.s	waitlp8 		; yes ->

	addq.l	#1,d7			; next sector to write
	dbf	d2,wrtlp8
	bra	hdw_ok

	end
