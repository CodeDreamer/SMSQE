; Close SER / PAR  channel   V2.10     1999  Tony Tebby

	section spp

	xdef	spp_close

	xref	iob_eof
	xref	iou_rchb
	xref	gu_rchp

	include 'dev8_keys_serparprt'

;+++
; Close SER / PAR channel. This sets end of file in the output buffer,
; throws the input buffer away, and throws the channel block away.
;---
spp_close
	move.l	spc_link(a0),a3 	 ; set linkage

	move.l	spc_obuf(a0),d0 	 ; set end of file
	beq.s	sppc_ibuf
	move.l	d0,a2
	jsr	iob_eof
	move.l	spd_oopr(a3),a4
	jsr	(a4)			 ; re-activate output if required

sppc_ibuf
	move.l	spc_ibuf(a0),d0 	 ; receive queue pointer
	beq.s	sppc_close		 ; ... there was not
	clr.l	spd_ibuf(a3)		 ; ... clear the queue pointer
sppc_close
	move.l	spd_close(a3),a4
	jsr	(a4)			 ; shutdown / disable input

	beq.s	sppc_rchb		 ; ... there was no input queue
	exg	a0,d0
	jsr	gu_rchp 		 ; return input buffer
	move.l	d0,a0

sppc_rchb
	moveq	#0,d0
	jmp	iou_rchb		 ; return channel block
	end
