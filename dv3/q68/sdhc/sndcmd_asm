; Q68 Send a command to an SDHC   1.01  W. Lenerz 2017

	section dv3

	xdef	snd_cmd

	include 'dev8_keys_q68'
	include 'DEV8_keys_err'

;+++
; This routine sends a command to an sdhc card. The command is 6 bytes long.
; On entry d0 contains the first 4 bytes and d1.w the last two.
;
;	d0 cr	.b command byte / error code
;	d1 cr	command data / return byte
;	d3    s
;	a2 c  p pointer to interface base
;	a3    s
;
;	status return 0 or ERR.MCHK (card didn't reply correctly)
;---

snd_cmd lea	drv_xfer(a2),a3
	add.w	#drv_writ,a2
	st	(a2)		; send -1 (8 clock pulses)
	st	(a3)		; now

	moveq	#$40,d3
	add.b	d0,d3
	move.b	d3,(a2) 	; send first byte
	st	(a3)

	rol.l	#8,d1
	move.b	d1,(a2) 	; send 2nd byte
	st	(a3)
	rol.l	#8,d1
	move.b	d1,(a2) 	; send 3rd byte
	st	(a3)
	rol.l	#8,d1
	move.b	d1,(a2) 	; send 4th byte
	st	(a3)
	rol.l	#8,d1
	move.b	d1,(a2) 	; send fifth byte
	st	(a3)

	st	(a2)
	st	(a3)		; CRC is $FF

	sub.w	#drv_writ,a2
	cmp.b	#12,d0		; stop read command cmd12?
	bne.s	nodum		; ... no->
	st	(a3)		; ... yes, so...
	move.b	drv_read(a2),d1 ; ... get dummy byte
nodum	move.w	#250,d3 	; *** was 150 ***
sdcmdlp st	(a3)
	move.b	drv_read(a2),d1 ; get reply, which must be <> -1 to be valid
	cmp.b	#-1,d1		; is it valid?
	bne.s	got_rply	; ... yes, done
	dbf	d3,sdcmdlp	; ... no, try again
	moveq	#err.mchk,d0	; card didn't reply in time
	rts
got_rply
	moveq	#0,d0
	rts

	end
