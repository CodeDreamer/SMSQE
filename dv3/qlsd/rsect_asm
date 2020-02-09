; DV3 QLSD SDHC Card Read Sector  1.01	  2016-2018 W. Lenerz	& Marcel Kilgus
;
;	      1.01  Added multiblock read
; 2018-06-12  1.02  Added hd_rscard_api (MK)

	section dv3

	xdef	hd_rdirect		; direct read sector
	xdef	hd_rsint		; internal read sector
	xdef	hd_rscard		; read card sector
	xdef	hd_rscard_api		; read card sector (external)
	xdef	hd_ckroot		; read root sector

	xref	hd_hold 		; reserve the interface
	xref	hd_release		; release the interface
	xref	card_select
	xref	card_deselect
	xref	card_wait_reply
	xref	snd_cmd 		; send a command to sdhc

	include 'dev8_dv3_qlsd_keys'
	include 'dev8_keys_err'

readreg reg	d1/d2/d3/d6/d7/a1/a2/a4

;+++
; External API for reading absolute sectors from a SD card.
;
;	d0 cr	sector number (abs or rel to partition) / error code
;	d2 c  p number of sectors to read (1 = 1 sector, 2 = 2 sects etc)
;	d7 c  p card number (1 to 3)
;	a1 c  p address to read into, MUST be large enough to hold the total nbr of sectors requested
;	a3 c  p linkage block
;
;	status return 0 or ERR.MCHK
;---
hd_rscard_api
	andi.w	#3,d7
	beq.s	err_ipar
	bset	#7,d7			; mark as "card number" for card_select
	move.l	a6,-(sp)
	move.l	qlsd_sysvars(a3),a6	; so caller doesn't have to provide it
	bsr.s	hd_rscard
	move.l	(sp)+,a6
	rts

err_ipar
	moveq	#err.ipar,d0
hdr_exit
	rts

;+++
; This routine reads a sector from an sdhc card.
;
; For hd_rscard the sector number is absolute to the SD card, all other take
; a sector number relative to the mounted partition file.
;
; All normal read accesses to the card go through here.
; Read is done via background transfer.
;
;	d0 cr	sector number (abs or rel to partition) / error code
;	d1    p
;	d2 c  p number of sectors to read (1 = 1 sector, 2 = 2 sects etc)
;	d7 c  p drive ID / number  (from 1 to 8)
;	a1 c  p address to read into, MUST be large enough to hold the total nbr of sectors requested
;	a3 c  p linkage block
;	a4 c  p drive definition (not for hd_rscard)
;	a6 c  p system variables
;
;	status return 0 or ERR.MCHK
;	The sector number is for sectors 512 bytes long.
;---
hd_ckroot
	moveq	#0,d0			; root sector
	moveq	#1,d2			; one sector only
hd_rdirect
hd_rsint
	add.l	sdf_partoffset(a4),d0	; sector relative to container file!

; Read card sector without partition offset (a4 not needed in this case)
hd_rscard
	jsr	hd_hold 		; hold the drive for me
	bne.s	hdr_exit		; can't
	movem.l readreg,-(a7)

	move.l	d0,d6			; sector to get

	bsr	card_select		; select the card. Sets a2/a4!
	bne.s	hdr_rts 		; ...didn't work

	moveq	#17,d7			; cmd17 = read single block
	subq.l	#1,d2			; go around d2 times
	beq.s	send_command
	moveq	#18,d7			; cmd18 = read multiple blocks

; first send command17 or 18 to the card
send_command
	move.b	d7,d0			; cmd17 or cmd18
	move.l	d6,d1			; sector nbr to read
	bsr	snd_cmd 		; send command to card
	bne.s	hdr_rts 		; ooops
	tst.b	d1			; return must be 0
	bne.s	hdr_err 		; ooops again

read_loop
	bsr	card_wait_reply 	; wait for reply from card
	bne.s	hdr_rts 		; timeout

; got a correct answer back from card, it's ready for transfer
; now read 512 times one byte
	move.w	#511,d3 		; read 512 bytes
get_loop
	tst.b	spi_bg_read(a4) 	; transfer byte
	move.b	spi_read(a2),d1 	; get byte
	move.b	d1,(a1)+
	dbf	d3,get_loop

; get rid of the 2 crcs sent
	tst.b	spi_bg_read(a4)
	tst.b	spi_bg_read(a4)
	dbf	d2,read_loop

	cmp.b	#18,d7			; was it multi-block?
	bne.s	hdr_ok			; ... no

	moveq	#12,d0			; ... yes, send cmd12 = stop reading
	moveq	#0,d1			; no parameter
	bsr	snd_cmd 		; send command to card

hdr_ok	moveq	#0,d0
hdr_rts
	bsr	card_deselect		; de-select the card and re-set ccr
	movem.l (a7)+,readreg
	jmp	hd_release		; will jump to hd_release & test d0
hdr_err
	moveq	#err.mchk,d0		; signal drive wasn't found
	bra.s	hdr_rts

	end
