; QXL SMS HDOPs    1988  Tony Tebby   QJUMP

	section sms

	xdef	sms_hdop

	xref	hdop_keyr
	xref	hdop_beep
	xref	hdop_bpof

	include 'dev8_keys_err'
	include 'dev8_smsq_smsq_base_keys'

sms_hdop
	move.l	sms.rte,-(sp)		 ; return
	move.l	a3,-(sp)
	move.b	(a3)+,d0		 ; hdop command
	cmp.b	#9,d0			 ; is it kbd read?
	beq.l	hdop_keyr		 ; ... yes
	cmp.b	#$a,d0			 ; is it beep?
	beq.s	hdop_beep		 ; ... yes
	cmp.b	#$b,d0			 ; beep off?
	beq.l	hdop_bpof		 ; ... yes
	move.l	(sp)+,a3
	moveq	#0,d1			 ; no return value
	moveq	#err.ipar,d0		 ; ... because it's none of these
	rts
	end
