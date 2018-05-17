; READ / EOF	  V2.00      1990  Tony Tebby	 QJUMP
;
;	READ
;	EOF (#n)
;
	section exten

	xdef	read
	xdef	eof
	xdef	eofw

	xref	ut_chan0		 ; get channel default #0
	xref	ut_rtfd1
	xref	ut_trap3

	include 'dev8_keys_qdos_io'
	include 'dev8_keys_err'
	include 'dev8_keys_sbasic'

read
	moveq	#err.nimp,d0
read_rts
	rts
	dc.l	'READ'

eofw
	moveq	#-1,d7
eof
	cmp.l	a3,a5			 ; is it channel?
	bne.s	eof_chan		 ; ... no
	move.l	sb_ndata(a6),d1 	 ; ... yes, ndata=-1 at end
	bra.s	eof_set

eof_chan
	jsr	ut_chan0
	bne.s	read_rts
	moveq	#iob.test,d0
	move.l	d7,d3
	trap	#3
	moveq	#err.eof,d1
	sub.l	d1,d0
	seq	d1
eof_set
	moveq	#1,d0
	and.l	d0,d1
	jmp	ut_rtfd1
	end
