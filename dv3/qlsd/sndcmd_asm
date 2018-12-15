; QLSD Send a command to an SDHC   1.01         2018 W. Lenerz + M. Kilgus
;
; 2018-06-10  1.01  card_select optionally takes a card number (MK)

	section dv3

	xdef	card_select
	xdef	card_deselect
	xdef	card_wait_ready
	xdef	card_wait_reply
	xdef	snd_cmd

	include 'dev8_keys_err'
	include 'dev8_dv3_qlsd_keys'

;+++
; Select card
;
;	d7 cr	drive number 1..8 or card nmbr+128 / card number 1..3
;	a2  r	pointer to interface base
;	a3 c  p pointer to DDB
;	a4  r	pointer to spi_xfer
;---
card_select
	bclr	#7,d7
	bne.s	was_card_number
	lea	hdl_unit-1(a3),a2
	move.b	(a2,d7.w),d7		; card nbr (1...3)
was_card_number
	move.l	#if_base,a2
	move.l	#spi_xfer,a4
	tst.b	spi_select0(a2,d7.w)	; select the card
	tst.b	spi_bg_read(a4) 	; dummy clock to force DO enabled
	bra.s	card_wait_ready

;+++
; Select card
;
;	a2 c  p pointer to interface base
;	a3 c  p pointer to DDB
;	a4 c	pointer to spi_xfer
;---
card_deselect
	tst.b	spi_select0(a2) 	; disable the card
	tst.b	spi_bg_read(a4) 	; dummy clock to force DO into high-Z
	tst.l	d0			; make error ccr active again
	rts

;+++
; Wait for card to become ready
;
;	d0  r	error code
;	d3    s
;	a2 c  p pointer to interface base
;	a3 c  p pointer to DDB
;	a4 c	pointer to spi_xfer
;---
card_wait_ready
	moveq	#$ff,d0 		; wait for $ff
card_wait_generic
	move.l	hdl_1sec(a3),d3 	; one second should be plenty
wait_loop
	tst.b	spi_bg_read(a4) 	; prep for transfer
	cmp.b	spi_read(a2),d0 	; get byte, check for $ff or $fe
	beq.s	wait_ok 		; card is ok
	subq.l	#2,d3			; we've got two h/w accesses...
	bgt.s	wait_loop		; ... wait some more
	moveq	#err.mchk,d0		; card has problems
	rts
wait_ok
	moveq	#0,d0
	rts

;+++
; Wait for data to become ready
;
;	d0  r	error code
;	d3    s
;	a2 c  p pointer to interface base
;	a3 c  p pointer to DDB
;	a4 c	pointer to spi_xfer
;---
card_wait_reply
	moveq	#$fe,d0
	bra.s	card_wait_generic

;+++
; This routine sends a command to an sdhc card.
; On entry d0.b contains the command to issue, d1.l the 4 command bytes
;
;	d0 cr	command byte / error code
;	d1 cr	4 bytes arguments for command / return byte
;	d3    s
;	a2 c  p pointer to interface base
;	a3 c  p pointer to DDB
;	a4 c	pointer to spi_xfer
;
;	status return 0 or ERR.MCHK (card didn't reply correctly)
;---
snd_cmd
	tst.b	spi_bg_read(a4) ; send some pulses

	moveq	#$40,d3 	; our commands have bit 6 set and 7 cleared
	or.b	d0,d3		; command byte
	tst.b	(a4,d3.w)	; send first byte

	rol.l	#8,d1
	move.b	d1,d3
	tst.b	(a4,d3.w)	; send 2nd byte
	rol.l	#8,d1
	move.b	d1,d3
	tst.b	(a4,d3.w)	; send 3rd byte
	rol.l	#8,d1
	move.b	d1,d3
	tst.b	(a4,d3.w)	; send 4th byte
	rol.l	#8,d1
	move.b	d1,d3
	tst.b	(a4,d3.w)	; send 5th byte

	move.b	#spi_bg_read,d3
	tst.b	(a4,d3.w)	; send 6th & last byte (CRC is always $FF)

	cmp.b	#12,d0		; stop cmd12?
	bne.s	wait_answer	; ... no
	tst.b	(a4,d3.w)	; ... yes, skip stuff byte

wait_answer
	move.l	hdl_1sec(a3),d0 ; 1 second timer
sdcmdlp tst.b	(a4,d3.w)
	move.b	spi_read(a2),d1 ; get reply, which must be <> -1 to be valid
	cmp.b	#$ff,d1 	; is it valid?
	bne.s	got_rply	; ...yes, done
	subq.l	#4,d0		; We have two h/w read and maybe 1s is too long
	bgt.s	sdcmdlp 	; ...no, try again
	moveq	#err.mchk,d0	; card didn't reply in time
	rts
got_rply
	moveq	#0,d0
	rts

	end
