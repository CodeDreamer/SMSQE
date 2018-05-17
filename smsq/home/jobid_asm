; Check job ID for Home Thing	 V1.00 (c) W. Lenerz 2005

; 2005-11-02	1.00	initial version

; This routine will be different for QDOS & SMSQE
; SMSQE doesn't need supervisor mode, but QDOS does, since this changes reg A6
; at least temporarily




	section home

	xdef	checkid
	xref	sms_ckid
	include dev8_keys_qdos_sms


; a0  r  JCB for the job
; d1 cr  job ID to check (perhaps -1 or -2)/ true job id of job to check
; d2   s


checkid
	movem.l a6/d7,-(sp)		; keep
	move.l	d1,d7			; keep initial job id
	moveq	#sms.info,d0
	trap	#1			; get job ID into D1, sysvars into A0
	move.l	a0,a6			; A6 needs to be the sysvars
	cmp.l	#-2,d7			; special flag?
	beq.s	nosubst 		       ; yes, keep job ID of current job
	move.l	d7,d1			; we want to check this job id
nosubst
	jsr	sms_ckid		; check
	movem.l  (sp)+,a6/d7		; get regs back and return
	rts


	end
