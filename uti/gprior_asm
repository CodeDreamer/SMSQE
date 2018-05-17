; Get priority of (current) job 	1997 Jochen Merz

	section utility

	include dev8_keys_qdos_sms

	xdef	ut_gprior	; get priority of given job
	xdef	ut_gcprior	; get priority of current job

;+++
; Get priority of current job
;
;		Entry			Exit
;	d0.b				priority
;	d1				smashed
;---
ut_gcprior
	moveq	#sms.myjb,d1
;+++
; Get priority of given job
;
;		Entry			Exit
;	d0.b				priority
;	d1.l	Job-ID			smashed
;---
ut_gprior
	movem.l d2-d3/a0-a1,-(sp)	; keep registers
	moveq	#sms.injb,d0		; we would like to get the priority
	moveq	#-1,d1			; me
	moveq	#0,d2
	trap	#do.smsq
	move.b	d3,d0			; job's current priority
	movem.l (sp)+,d2-d3/a0-a1
	rts

	end
