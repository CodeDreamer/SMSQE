; SER/PAR IO operations  V2.10	   1999  Tony Tebby

	section spp

	xdef	spp_io

	xref	iob_iox

	include 'dev8_keys_err'
	include 'dev8_keys_serparprt'

;+++
; SER/PAR device IO operations.
; All IO operations are handled by the standard serial IO routine IOB.IOX.
; For every byte sent, the output activate routine is called to ensure that the
; data from the queue is being transmitted.
;---
spp_io
	move.l	spc_link(a0),a3 	 ; set linkage

	moveq	#1,d7
	and.w	spc_xlat(a0),d7 	 ; translate
	ror.l	#1,d7
	move.w	spc_lfcr(a0),d7
	lsl.w	#8,d7
	move.b	spc_prty+1(a0),d7	 ; and parity
	lea	spc_ibuf(a0),a2 	 ; set buffer pointers

	jsr	iob_iox
	bgt.s	spp_eof 		 ; end of file
	rts
spp_eof
	moveq	#err.eof,d0
	rts
	end
