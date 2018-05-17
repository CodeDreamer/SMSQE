; Get system specific execution delay	V1.00		  2006  Marcel Kilgus

	section gen_util

	xdef	gu_exdelay

	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_sys'

;+++
; Get system specific value for the delay after executing a job
;
;	d0    r ticks to pause
;---
gu_exdelay
	movem.l d1-d2/a0,-(sp)
	moveq	#sms.info,d0
	trap	#1
	moveq	#0,d0
	move.b	sys_xdly(a0),d0
	bne.s	xd_exit
	moveq	#25,d0			 ; default: pause .5s
xd_exit
	movem.l (sp)+,d1-d2/a0
	rts

	end
