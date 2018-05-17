; SER OPEN    V2.02	    1989   Tony Tebby	 QJUMP

	section ser

	xdef	ser_open

	xref	par_open
	xref	ser_rxen

	include 'dev8_keys_par'

;+++
; SER OPEN, just calls PAR open and then if OK and input buffer set, enables
; receive.
;---
ser_open
	jsr	par_open		 ; do standard open
	bne.s	sero_exit
	bra.s	sero_rxen
	nop
	dc.w	3,'SER'
sero_rxen
	move.l	a3,prc_link(a0) 	 ; set linkage
	tst.l	prc_ibuf(a0)		 ; any receive buffer?
	beq.s	sero_exit		 ; ... no
	clr.w	prd_cdct(a3)		 ; clear cd eof count
	bra.l	ser_rxen		 ; and enable

sero_exit
	rts
	end
