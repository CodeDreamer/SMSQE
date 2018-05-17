; SMSQmulator HDOPs   V1.00  (c)  w. lenerz 2012

; based on HDOPs  1988  Tony Tebby   QJUMP

; v. 1.00 bbep implemented
; v. 0.00

	section kbd

	xdef	sms_hdop

	xref	hdop_keyr


	include 'dev8_keys_java'
	include 'dev8_keys_err'
	include 'dev8_smsq_smsq_base_keys'

; this is the code called from trap #1 sms.hdop

sms_hdop
	move.l	sms.rte,-(sp)		 ; return to RTE
	move.l	a3,-(sp)		 ; this is popped up in the return routine
	move.b	(a3),d0 		 ; hdop command
	cmp.b	#9,d0			 ; is it kbd read?
	beq.l	hdop_keyr		 ; ... yes
	cmp.b	#$a,d0			 ; is it beep?
	beq.s	hdop_beep		 ; ... yes
	cmp.b	#$b,d0			 ; beep off?
	beq.s	hdop_bpof		 ; ... yes
	move.l	(a7)+,a3
	moveq	#0,d1			 ; no return
	moveq	#err.ipar,d0		 ; ... because it's none of these
	rts

hdop_beep
	move.l	(a7)+,a3
	moveq	#jt5.beep,d0
	dc.w	jva.trp5
	rts

hdop_bpof

	move.l	(a7)+,a3
	moveq	#jt5.bpof,d0
	dc.w	jva.trp5
	rts
	end
