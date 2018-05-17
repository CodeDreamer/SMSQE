; Check Status of Atari DMA	    1990     Tony Tebby  QJUMP

	section adma

	xdef	at_stat
	xdef	at_stata

	xref	at_wait80

	include 'dev8_keys_atari'
	include 'dev8_keys_err'

;+++
; This routine waits for interrupt at end of operation and checks the DMA
; address for completion
;
;	a1 c  p end DMA address
;	a3 c  p pointer to linkage block
;
;	status return 0, 1 (no interrupt) or ERR.NC (dma not complete)
;
;---
at_stata
	bsr.s	at_stat 		 ; wait for completion
	bne.s	ats_rts
	move.b	dma_high,d0		 ; get address
	swap	d0
	move.b	dma_mid,d0
	lsl.w	#8,d0
	move.b	dma_low,d0
	cmp.l	a1,d0			 ; correct address?
	beq.s	ats_ok

	moveq	#err.nc,d0		 ; not complete
ats_rts
	rts

;+++
; This routine waits for interrupt at end of operation.
;
;	a3 c  p pointer to linkage block
;
;	status return standard 0 or 1
;
;---
at_stat
	move.w	#1000,d0		  ; wait 1000*20 == 20secs
ats_stttim
	swap	d0
	move.b	#239,d0 		 ; wait 240*83 == 20ms
	jsr	at_wait80

ats_sttw
	btst	#mfp..hdi,mfp_hdpi	 ; wait for interrupt
	bne.s	ats_ok
	btst	#mfp..tai,mfp_tapi	 ; or timeout
	beq.s	ats_sttw
	swap	d0
	dbra	d0,ats_stttim
	moveq	#1,d0
	rts

ats_ok
	move.b	#mfp.hdpc,mfp_hdpi	 ; interrupt not pending now
	moveq	#0,d0
	rts


	end $$$$$$$$$$$$$$$$$$$$$$$

ats_sttw
	btst	#mfp..dmi,mfp_dmi	 ; for interrupt
	beq.s	ats_ok
	btst	#mfp..tai,mfp_tapi	 ; or timeout
	beq.s	ats_sttw
	swap	d0
	dbra	d0,ats_stttim		 ; and again
	moveq	#1,d0
ats_rts
	rts

ats_ok
	moveq	#0,d0
	rts
	end
