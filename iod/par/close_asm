; Close PAR channel   V2.01     1989  Tony Tebby

	section par

	xdef	par_close

	xref	par_actv
	xref	iob_eof

	include 'dev8_keys_qlv'
	include 'dev8_keys_par'
;+++
; Close PAR channel. This returns the channel block to the heap.
; It sets EOF and ensures that the interrupt routine is active.
;---
par_close
	move.l	prc_obuf(a0),a2
	jsr	iob_eof
	jsr	par_actv		; set it going if needed
	move.w	mem.rchp,a2
	jmp	(a2)
	end
