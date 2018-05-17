; SER IO operations  V1.00     1989  Tony Tebby   QJUMP

	section ser

	xdef	ser_io

	xref	iob_iox

	include 'dev8_keys_err'
	include 'dev8_keys_qlv'
	include 'dev8_keys_k'
	include 'dev8_keys_par'

;+++
; SER device IO operations.
; All IO operations are handled by the standard serial IO routine IOB.IOX.
; For every byte sent, the routine SER_ACTV is called to ensure that the
; data from the queue is being transmitted.
;---
ser_io
	move.l	prc_link(a0),a3 	 ; set linkage

	moveq	#1,d7
	and.w	prc_xlat(a0),d7 	 ; translate
	ror.l	#1,d7
	move.w	prc_lfcr(a0),d7
	lsl.w	#8,d7
	move.b	prc_prty+1(a0),d7	 ; and parity
	lea	prc_ibuf(a0),a2 	 ; set buffer pointers

	jsr	iob_iox
	bgt.s	ser_eof 		 ; end of file
	rts
ser_eof
	moveq	#err.eof,d0
	rts
	end
