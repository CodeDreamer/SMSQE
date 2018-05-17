; Atari wait utilities	      1990	Tony Tebby  QJUMP

	section dv3

	xdef	fd_p2ms 	 ; 2 ms wait for tunnel erase
	xdef	at_wait4
	xdef	at_wait80

	xdef	at_p10u
	xdef	at_pd0u

	include 'dev8_keys_atari'

;+++
; This routine pauses approx 2 milliseconds
;
;	all registers preserved, status acording to d0
;
;---
fd_p2ms
	bsr.s	fd_p1ms
	nop
fd_p1ms
	move.l	d0,-(sp)
	moveq	#-1,d0
	bra.s	atp_set
;+++
; This routine pauses approx d0 us (10-1024 us)
;
;	all registers preserved, status acording to d0
;
;---
at_pd0u
	move.l	d0,-(sp)
	lsr.w	#2,d0			 ; 4us units
	bra.s	atp_set
;+++
; This routine pauses approx 10 us (8us+overheads)
;
;	all registers preserved, status acording to d0
;
;---
at_p10u
	move.l	d0,-(sp)
	moveq	#2,d0			 ; 2*
atp_set
	bsr.s	at_wait4		 ; 4us
atp_wait
	btst	#mfp..tai,mfp_tapi	 ; wait for timeout
	beq.s	atp_wait
	move.l	(sp)+,d0
	rts

;+++
; This routine sets up a wait in 4 microsecond units
;
;	d0  c  p byte number of 4 microsecond units
;
;	all registers preserved status arbitrary
;
;---
at_wait4
	clr.b	at_mfp+mfp_actl 	 ; stop timer
	move.b	#mfp.tapc,mfp_tapi	 ; not pending now
	move.b	d0,at_mfp+mfp_adat	 ; count
	move.b	#mfp.4us,at_mfp+mfp_actl ; 4 us increments
	rts
;+++
; This routine sets up a wait in 80 microsecond units
;
;	d0  c  p byte number of 80 microsecond units
;
;	all registers preserved status arbitrary
;
;---
at_wait80
	clr.b	at_mfp+mfp_actl 	 ; stop timer
	move.b	#mfp.tapc,mfp_tapi	 ; not pending now
	move.b	d0,at_mfp+mfp_adat	 ; count
	move.b	#mfp.80us,at_mfp+mfp_actl ; 80 us increments
	rts
	end
