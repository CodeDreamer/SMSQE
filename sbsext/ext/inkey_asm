; INKEY$ / PAUSE       V2.00	  1990  Tony Tebby   QJUMP
;
;	INKEY$ (#channel, timeout)
;	PAUSE timeout
;
	section exten

	xdef	inkey$
	xdef	pause

	xref	ut_chan0		 ; get channel default #0
	xref	ut_gxin1		 ; get one integer
	xref	ut_retst

	include 'dev8_keys_qdos_io'
	include 'dev8_sbsext_ext_keys'

pause
	moveq	#-1,d5			 ; default wait
	bsr.s	inky_do
	moveq	#0,d0			 ; no error return SuperBASIC compat
	rts
inkey$
	moveq	#0,d5			 ; default no wait
inky_do
	jsr	ut_chan0		 ; get channel id
	bne.s	inky_rts		 ; ... oops
	move.l	bv_rip(a6),a1		 ; set RI stack in case
	cmp.l	a3,a5			 ; any parameter?
	beq.s	inky_inp

	jsr	ut_gxin1		 ; get param
	bne.s	inky_rts

	move.w	(a6,a1.l),d5		 ; parameters
	addq.l	#2,a1			 ; clean stack

inky_inp
	move.l	a1,-(sp)
	moveq	#iob.fbyt,d0		 ; fetch byte
	move.w	d5,d3
	trap	#do.io
	move.l	(sp)+,a1
	tst.l	d0
	beq.s	inky_set
	subq.l	#2,a1			 ; null
	clr.w	(a6,a1.l)
	bra.s	inky_ret
inky_set
	subq.l	#4,a1
	move.w	#1,(a6,a1.l)		 ; single byte string
	move.b	d1,2(a6,a1.l)
inky_ret
	jmp	ut_retst		 ; return it
inky_rts
	rts
	end
