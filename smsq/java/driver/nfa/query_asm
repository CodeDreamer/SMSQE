; Query SMSQmulator about different things	V1.00 (c) W. Lenerz 2012


; his has practically no use

		section nfa

		include 'dev8_keys_java'

		xdef	query_java
		xref	ut_gxin1
		xref	ut_rtint

query_java
	jsr	ut_gxin1		; get one int
	bne.s	set_out
	clr.l	d1
	move.w	(a6,a1.l),d1		; query number
	moveq	#jt5.qry,d0
	dc.w	jva.trp5
	moveq	#0,d0
	jmp	ut_rtint

set_out rts
	end
