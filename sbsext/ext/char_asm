* Character sets      V0.9    Tony Tebby   QJUMP
*
*	CHAR_DEF [#n,] addr, addr	sets the default founts
*	CHAR_USE [#n,] addr, addr	sets the character founts
*	CHAR_INC [#n,] x_inc, y_inc	sets the character increments
*
	section exten
*
	xdef	char_def
	xdef	char_use
	xdef	char_inc
*
	xref	ut_chan1		get channel default #1
	xref	ut_gxli2		get exactly two long integers
	xref	ut_gxin2		get exactly two integers
	xref	ut_trp3r		trap #3 relative
	xref	ut_trap3		trap #3 (d3=-1)
*
	include dev8_sbsext_ext_keys
*
char_def
	move.l	#'DEFF',d7
char_use
	bsr.l	ut_chan1		get channel id
	bne.s	char_rts		... oops
	bsr.l	ut_gxli2		get two long integers
	bne.s	char_rts
	moveq	#sd.fount,d0		set fount(s)
	move.l	4(a6,a1.l),a2		second
	move.l	(a6,a1.l),a1		then first
	move.l	d7,d2			set key
	bra.l	ut_trap3		and do trap #3
*
char_inc
	bsr.l	ut_chan1		get channel id
	bne.s	char_rts		... oops
	bsr.l	ut_gxin2		get two integers
	bne.s	char_rts
	moveq	#sd.extop,d0		set increments with extop
	lea	char_sinc(pc),a2
	bra.l	ut_trp3r		do trap
*
char_sinc
	move.l	(a1),sd_xinc(a0)	parameters were on a1
	moveq	#0,d0			no error
char_rts
	rts
	end
