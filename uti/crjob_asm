; Create a job

	section utility

	xdef	ut_crjob	; create job owned by us
	xdef	ut_crijob	; create independent job
	xdef	ut_crojob	; create job with given owner

	include dev8_keys_qdos_sms

;+++
; Create a job. This routine makes a job header, allocates enough dataspace
; sets the priority and activates it. It does not suspend the owner job.
;
;		Entry				Exit
;	d1	owner (only crojob)		job ID
;	d2	code length			smashed
;	d3	dataspace length		smashed
;	d4	priority
;	d5+					preserved
;	a0					base of job ctrl area
;	a1	start address of job		preserved
;	a2	pointer to job name		smashed
;	a3+					preserved
;
;	Error return:	all usual job error returns
;---
ut_crijob		; create independent job
	moveq	#0,d1
	bra.s	ut_crojob
ut_crjob		; create job owned by us
	moveq	#-1,d1		; we are owner
ut_crojob
	move.l	a1,-(sp)
	suba.l	a1,a1		; no start address
	moveq	#sms.crjb,d0
	trap	#do.sms2	; create job
	move.l	(sp)+,a1
	tst.l	d0
	bne.s	crjob_rt	; failed

	move.w	#$4ef9,(a0)+	; 'BRA.L'
	move.l	a1,(a0)+	; to our start
	move.w	#$4afb,(a0)+	; standard job header
	move.w	(a2)+,d2	; length of job name
	move.w	d2,(a0)+
crjob_cn
	tst.w	d2		; end of name
	beq.s	crjob_ne
	move.b	(a2)+,(a0)+
	subq.w	#1,d2
	bra.s	crjob_cn
crjob_ne
	moveq	#0,d3		; do not hold owner
	move.b	d4,d2		; priority
	moveq	#sms.acjb,d0
	trap	#do.sms2	; activate job
crjob_rt
	tst.l	d0
	rts

	end
