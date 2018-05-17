; IO Calls D1/D2       V2.00	  1990  Tony Tebby   QJUMP
;
;	CSIZE #channel, x, y
;	AT #channel, y, x
;	CURSOR #channel, x, y (CURSOR #channel, xg, yg, xp, yp)
;
	section exten

	xdef	csize
	xdef	at
	xdef	cursor

	xref	ut_chan1		 ; get channel default #1
	xref	ut_gtint		 ; get integers
	xref	ut_gtfp
	xref	ut_trap3

	include 'dev8_keys_qdos_io'
	include 'dev8_keys_err'

csize
	moveq	#iow.ssiz,d7		 ; set size
	bra.s	iod2_do

at
	move.l	#1<<31+iow.scur,d7	 ; position cursor (reversed coord)
	bra.s	iod2_do

cursor
	moveq	#iow.spix,d7		 ; get channel id
	jsr	ut_chan1
	bne.s	iod2_rts
	moveq	#4*8,d0
	add.l	a3,d0
	sub.l	a5,d0			 ; four params?
	bne.s	iod2_parm
	jsr	ut_gtfp 		 ; floating point params
	moveq	#iog.sgcr,d0		 ; for graphics positioning
	trap	#4			 ; a1 rel a6
	bra.s	iod2_trs

iod2_do
	jsr	ut_chan1		 ; get channel id
	bne.s	iod2_rts		 ; ... oops
iod2_parm
	jsr	ut_gtint		 ; get param
	bne.s	iod2_rts
	subq.w	#2,d3			 ; two of them?
	bne.s	iod2_ipar

	movem.w (a6,a1.l),d1/d2 	 ; parameters

	move.l	d7,d0			 ; operation
	bgt.s	iod2_trs		 ; ... ok
	exg	d1,d2			 ; ... other way round

iod2_trs
	jmp	ut_trap3		 ; do trap

iod2_ipar
	moveq	#err.ipar,d0		 ; invalid par
iod2_rts
	rts
	end
