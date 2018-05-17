; Atari DMA set address and count     1990	Tony Tebby  QJUMP

	section adma

	xdef	at_seta
	xdef	at_setra
	xdef	at_setrf
	xdef	at_setwa
	xdef	at_setwf

	include 'dev8_keys_atari'
	include 'dev8_smsq_smsq_base_keys'

;+++
; This routine sets DMA address
;
;	d0   s
;	a1 c  p address
;
;	status return arbitrary
;
;---
at_seta
	move.l	a1,d0
	move.b	d0,dma_low		 ; set dma address
	ror.w	#8,d0
	move.b	d0,dma_mid
	swap	d0
	move.b	d0,dma_high
	rts

;+++
; This routine sets DMA address for write ACSI and count
;
;	d2 c  p word number of sectors
;
;	status return arbitrary
;
;---
at_setwa
	move.w	#dma.awct,dma_mode
	move.w	#dma.arct,dma_mode	 ; toggle to clear internal status
	move.w	#dma.awct,dma_mode

	move.w	d2,dma_scnt
	rts
;+++
; This routine sets DMA address for write floppy and count
;
;	d0   s	smashed
;	d2 c  p word number of sectors
;
;	status return arbitrary
;
;---
at_setwf
	move.w	#dma.fwct,dma_mode
	move.w	#dma.frct,dma_mode	 ; toggle to clear internal status
	move.w	#dma.fwct,dma_mode

	move.w	d2,dma_scnt
	rts
;+++
; This routine sets DMA address for read ACSI and count
;
;	d0   s	smashed
;	d2 c  p word number of sectors
;
;	status return arbitrary
;
;---
at_setra
	move.w	#dma.arct,dma_mode
	move.w	#dma.awct,dma_mode	 ; toggle to clear internal status
	move.w	#dma.arct,dma_mode

	move.w	d2,dma_scnt
	jmp	sms.cinvd
;+++
; This routine sets DMA address for read floppy and count
;
;	d2 c  p word number of sectors
;
;	status return arbitrary
;
;---
at_setrf
	move.w	#dma.frct,dma_mode
	move.w	#dma.fwct,dma_mode	 ; toggle to clear internal status
	move.w	#dma.frct,dma_mode

	move.w	d2,dma_scnt
	jmp	sms.cinvd
	end
