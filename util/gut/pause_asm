; Pause my Job	 V2.00	  1988  Tony Tebby

	section gen_util

	xdef	gu_pause

	include 'dev8_keys_qdos_sms'
;+++
; Pause my Job, preserving registers
;
;	d0 c  r ticks to pause / error code
;	status returned according to D0
;---
gu_pause
	movem.l d1/d2/d3/a0/a1,-(sp)
	move.w	d0,d3			 ; pause
	moveq	#sms.myjb,d1
	moveq	#sms.ssjb,d0		 ; suspend job
	sub.l	a1,a1
	trap	#do.sms2
	movem.l (sp)+,d1/d2/d3/a0/a1
	tst.l	d0
	rts
	end
