; DV3 SDHC Write Sector for Q68  1.02		 2018 W. Lenerz + M. Kilgus
;
;	      1.01  Implemented multiblock write (MK)
; 2018-05-29  1.02  Added hw_wscard/hw_wscard_api (MK)

	section dv3

	xdef	hd_wdirect		; direct write sector
	xdef	hd_wsint		; internal write sector
	xdef	hd_wscard		; card write sector
	xdef	hd_wscard_api		; card write sector (external)

	xref	hd_hold 		; reserve the interface
	xref	hd_release		; release the interface
	xref	card_select
	xref	card_deselect
	xref	card_wait_ready
	xref	snd_cmd 		; send a command to sdhc


	include 'dev8_dv3_qlsd_keys'
	include 'dev8_keys_err'

sendreg reg	d1-d4/d6/d7/a1/a2/a4

;+++
; External API for writing absolute sectors to a SD card
;
;	d0 cr	sector number / error code
;	d2 c  p number of sectors (at least 1!)
;	d7 c  p drive ID / number  (from 1 to 8)
;	a1 c  p address to read from
;	a3 c  p linkage block
;
;	status return 0 or ERR.TRNS
;---
hd_wscard_api
	andi.w	#3,d7
	beq.s	err_ipar
	bset	#7,d7			; mark as "card number" for card_select
	move.l	a6,-(sp)
	move.l	qlsd_sysvars(a3),a6	; so caller doesn't have to provide it
	bsr.s	hd_wscard
	move.l	(sp)+,a6
	rts

err_ipar
	moveq	#err.ipar,d0
hdw_exit
	rts

;+++
; This routine writes a sector to a sdhc card via background transfer.
;
; For hd_wscard the sector number is absolute to the SD card, all other take
; a sector number relative to the mounted partition file.
;
;	d0 cr	sector number / error code
;	d2 c  p number of sectors (at least 1!)
;	d7 c  p drive ID / number  (from 1 to 8)
;	a1 c  p address to read from
;	a3 c  p linkage block
;	a4 c  p drive definition (not for hd_wscard)
;
;	status return 0 or ERR.TRNS
;
;	The sector number is for sectors 512 bytes long.
;---
hd_wdirect
hd_wsint
	add.l	sdf_partoffset(a4),d0	; sector relative to container file!

; Write sector without partition offset (a4 not needed in this case)
hd_wscard
	jsr	hd_hold 		; hold the drive for me
	bne	hdw_exit		; can't
	movem.l sendreg,-(a7)

	move.l	d0,d6			; address to read from

	bsr	card_select		; select the card. Sets a2/a4!
	bne.s	hdw_rts 		; ...didn't work

	moveq	#24,d0			; cmd24 = write single block
	move.w	#$fe,d7 		; start token for single block
	subq.w	#1,d2			; prepare for dbf
	beq.s	send_command		; ...only single block
	moveq	#25,d0			; cmd25 = write multiple blocks
	move.w	#$fc,d7 		; start token for multi-block

; first send command24 or 25 to the card
send_command
	move.l	d6,d1			; block to write
	bsr	snd_cmd 		; send command now
	bne.s	hdw_rts 		; ooops
	tst.b	d1			; return needs to be 0
	bne.s	hdw_err 		; ooops again

write_loop
	bsr	card_wait_ready
	bne.s	hdw_rts
	tst.b	(a4,d7.w)		; send start block write token

; now write 512 times one byte
	move.w	#511,d3 		; for 512 bytes
	clr.l	d1
sendlp	move.b	(a1)+,d1
	tst.b	(a4,d1.w)
	dbf	d3,sendlp		; do it for 512 bytes
		
	tst.b	$ff(a4) 		;
	tst.b	$ff(a4) 		; send 2 dummy crcs
	dbf	d2,write_loop

	bsr	card_wait_ready
	bne.s	hdw_rts

	cmp.b	#$fc,d7 		; was it multi-block?
	bne.s	hwd_ok			; ...no

	tst.b	$fd(a4) 		; terminate multi-write token
hwd_ok
	moveq	#0,d0			; done and ok
hdw_rts
	bsr	card_deselect		; disable the card and re-set ccr
	movem.l (a7)+,sendreg
	jmp	hd_release		; release & return
hdw_err
	moveq	#err.mchk,d0		; signal drive wasn't found
	bra.s	hdw_rts

	end
