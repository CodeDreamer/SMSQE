; Close SER channel   V2.01     1989  Tony Tebby

	section ser

	xdef	ser_close

	xref	ser_rxdi
	xref	ser_oact
	xref	iob_eof
	xref	iou_rchb
	xref	gu_rchp

	include 'dev8_keys_par'
;+++
; Close SER channel. This sets end of file in the output buffer,
; throws the input buffer away, and throws the channel block away.
;---
ser_close
	move.l	prc_link(a0),a3 	 ; set linkage

	move.l	prc_obuf(a0),d0 	 ; set end of file
	beq.s	serc_ibuf
	move.l	d0,a2
	jsr	iob_eof
	jsr	ser_oact		 ; re-activate output if required
serc_ibuf
	move.l	prc_ibuf(a0),d0 	 ; no receive queue now
	beq.s	serc_rchb		 ; ... there was not anyway
	clr.l	prd_ibuf(a3)
	jsr	ser_rxdi		 ; disable input
	exg	a0,d0
	jsr	gu_rchp 		 ; return it
	move.l	d0,a0
serc_rchb
	moveq	#0,d0
	jmp	iou_rchb		 ; return channel block
	end
