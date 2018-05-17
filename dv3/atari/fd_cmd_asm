; DV3 Atari FDC send command	V2.00	  1999  Tony Tebby

	section fd

	xdef	fd_cmd			; send command
	xdef	fd_cmd_recal		; send re-calibrate command
	xdef	fd_cmd_seek		; send seek command
	xdef	fd_cmd_stin		; send step in by 5 tracks command
	xdef	fd_cmd_rid		; send read id (side 0) command
	xdef	fd_cmd_rd		; send command to read
	xdef	fd_cmd_wr		; send command to write
	xdef	fd_cmd_wrn		; send command to write, no dma

	xref	fd_selside

	xref	at_seta
	xref	at_setrf
	xref	at_setwf
	xref	at_wait4

	include 'dev8_dv3_keys'
	include 'dev8_dv3_fd_keys'
	include 'dev8_keys_atari'
	include 'dev8_keys_err'
	include 'dev8_mac_assert'

*+++
; This routine sends a step in command to the floppy disk controller.
; Currently it steps in by one track.
;
;	a3 c  p linkage block
;	a4 c  p drive definition block
;
;	status return standard
;
*---
fd_cmd_stin
	moveq	#fdc.stin,d0		 ; step in
	bra.s	fdc_seek

*+++
; This routine sends a recalibrate command to the floppy disk controller.
;
;	a3 c  p linkage block
;	a4 c  p drive definition block
;
;	status return standard
;
*---
fd_cmd_recal
	moveq	#fdc.rest,d0		 ; restore
	bra.s	fdc_seek

*+++
; This routine sends a seek command to the floppy disk controller.
;
;	a3 c  p linkage block
;	a4 c  p drive definition block
;
;	status return standard
;
*---
fd_cmd_seek
	move.w	#dma.fdat,dma_mode	 ; set data reg
	move.w	fdl_trak-1(a3),dma_data  ; with track required
	moveq	#fdc.seek,d0		 ; seek

fdc_seek
	move.w	d0,-(sp)
	move.w	#fdl_tstep-1,d0 	 ; get temporary step rate
	add.w	d7,d0
	move.b	(a3,d0.w),d0
	and.w	#7,d0
	cmp.b	#ddf.hd,ddf_density(a4)  ; density?
	bne.s	fdcs_rget		 ; not HD
	add.b	d0,d0
fdcs_rget
	move.b	fdc_srate(pc,d0.w),d0
	add.w	d0,(sp) 		 ; add step rate bits 00 - 11
	bra.s	fdc_rcmd		 ; do command

fdc_srate dc.b	%10,%10,%10,%11,%11,%00,%00,%00,%01,%01,%01,%01,%01,%01,%01,%01

*+++
; This routine sends a write command to the floppy disk controller
; without setting up the DMA.
;
;	d0 cr	command number / status
;	d2 c  p number of sectors (=1 for sector, large for track)
;	a1 c  p buffer to write from
;	a3 c  p linkage block
;	a4 c  p drive definition block
;
;	status return standard
;
*---
fd_cmd_wrn
	move.w	d0,-(sp)
	bra.s	fdc_wcmd

*+++
; This routine sends a write command to the floppy disk controller.
;
;	d0 cr	command number / status
;	d2 c  p number of sectors (=1 for sector, large for track)
;	a1 c  p buffer to write from
;	a3 c  p linkage block
;	a4 c  p drive definition block
;
;	status return standard
;
*---
fd_cmd_wr
	move.w	d0,-(sp)
	move.w	#dma.fsct,dma_mode	 ; set sector reg
	move.w	fdl_sect-1(a3),dma_data  ; and sector required
	move.b	fdl_side(a3),d0
	jsr	fd_selside		 ; set side
	jsr	at_seta 		 ; set dma address
	jsr	at_setwf		 ; set write

fdc_wcmd
	move.w	#dma.fdwr,dma_mode	 ; set control reg and read
	cmp.b	#60,fdl_trak(a3)
	bls.s	fdc_do			 ; ... outer track
	bclr	#fdc..prc,1(sp) 	 ; ... inner, precompensate
	bra.s	fdc_do

*+++
; This routine sends a simple command to the floppy disk controller.
;
;	d0 cr	command number / status
;	a3 c  p linkage block
;	a4 c  p drive definition block
;
;	status return standard
;
*---
fd_cmd
	move.w	d0,-(sp)		 ; save command
	bra.s	fdc_rcmd

*+++
; This routine sends a read sector ID command to the floppy disk controller.
;
;	d0 cr	read address command / status
;	d2  r	sector count = 1
;	a1 c  p buffer to read into
;	a3 c  p linkage block
;	a4 c  p drive definition block
;
;	status return standard
;
*---
fd_cmd_rid
	move.w	d0,-(sp)
	moveq	#1,d2			 ; up to one sector only
	moveq	#0,d0			 ; select side 0
	bra.s	fdc_dord

*+++
; This routine sends a read command to the floppy disk controller.
;
;	d0 cr	command number / status
;	d2 c  p number of sectors (=1)
;	a1 c  p buffer to read into
;	a3 c  p linkage block
;	a4 c  p drive definition block
;
;	status return standard
;
*---
fd_cmd_rd
	move.w	d0,-(sp)
	move.w	#dma.fsct,dma_mode	 ; set sector reg
	move.w	fdl_sect-1(a3),dma_data  ; and sector required
	move.b	fdl_side(a3),d0
fdc_dord
	jsr	fd_selside		 ; set side
	jsr	at_seta 		 ; set dma address
	jsr	at_setrf		 ; set read

fdc_rcmd
	move.w	#dma.fdrd,dma_mode	 ; set control reg and read

fdc_do
	moveq	#-1,d0
fdc_wstt
	btst	#mfp..dmi,mfp_dmi	 ; wait for interrupt to go away
	dbne	d0,fdc_wstt

	move.w	(sp)+,dma_data		 ; and set data

	moveq	#10,d0			 ; wait for up to 40 us for busy
	jsr	at_wait4
fdc_wait
	assert	dma.fstt,dma.fctl	 ; check status
	moveq	#1<<fds..bsy,d0 	 ; wait for busy
	and.w	dma_data,d0
	bne.s	fdc_done
	btst	#mfp..tai,mfp_tapi	 ; or timeout
	beq.s	fdc_wait
fdc_done
	moveq	#0,d0			 ; always OK
	rts

	end
