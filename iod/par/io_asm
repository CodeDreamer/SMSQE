; PAR IO operations  V2.02     1989  Tony Tebby   QJUMP

	section par

	xdef	par_io

	xref	iob_iox

	include 'dev8_keys_par'

;+++
; PAR device IO operations.
; All IO operations are handled by the buffered serial IO routine IOB_IOXL.
; On every output, the routine PAR_ACTV is called to ensure that the
; data from the queue is being transmitted.
;---
par_io
	lea	prc_ibuf(a0),a2 	 ; point to IO queue/buffer pointers
	moveq	#1,d7
	and.w	prc_xlat(a0),d7
	ror.l	#1,d7			 ; set translate flag
	move.w	prc_lfcr(a0),d7
	lsl.w	#8,d7
	jmp	iob_iox 		 ; buffered IO with XLATE
	end
