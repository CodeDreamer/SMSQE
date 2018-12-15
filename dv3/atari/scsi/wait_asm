; Atari SCSI wait utility     1990	Tony Tebby  QJUMP

	section adma

	xdef	sc_wait80
	xdef	sc_p20m

	include 'dev8_keys_atari'

;+++
; pause for 20 milliseconds
;
sc_p20m
	move.l	d0,-(sp)
	moveq	#$fffffff0,d0		 ; 240 * 80 us
	bsr.s	sc_wait80		 ; 80us
scp_wait
	btst	#mfp..tai,at_mfp2+mfp_pnda ; wait for timeout
	beq.s	scp_wait
	move.l	(sp)+,d0
	rts

;+++
; This routine sets up a wait in 80 microsecond units using mfp2
;
;	d0  c  p byte number of 80 microsecond units
;
;	all registers preserved status arbitrary
;
;---
sc_wait80
	clr.b	at_mfp2+mfp_actl	 ; stop timer
	move.b	#mfp.tapc,at_mfp2+mfp_pnda ; not pending now
	move.b	d0,at_mfp2+mfp_adat	 ; count
	move.b	#mfp.80us,at_mfp2+mfp_actl ; 80 us increments
	rts
	end
