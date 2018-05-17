; Job Control	      V2.00    1986  Tony Tebby  QJUMP

	section shd

	xdef	shd_ssjb		; suspend a job
	xdef	shd_usjb		; unsuspend a job
	xdef	shd_acjb		; activate a job
	xdef	shd_spjb		; set priority of a job

	xdef	shd_relf
	xdef	shd_susp

	xref	sms_ckjx		; check the job exists
	xref	sms_cjid		; current job
	xref	sms_rtok
	xref	sms_rte

	include 'dev8_keys_err'
	include 'dev8_keys_sys'
	include 'dev8_keys_jcbq'
	include 'dev8_mac_creg'
	include 'dev8_smsq_smsq_base_keys'
;+++
; Suspend job
;
;	d0  r	0 or invalid job
;	d1 cr	job ID
;	d2 c  p (w) priority (acjb, spjb)
;	d3 c  p (w) timeout (ssjb), suspend (acjb)
;	a1 c  p address of flag byte (ssjb)
;	a0  r	base of jcb
;
;	all other registers preserved
;---
shd_ssjb
	bsr.l	shd_ckjx		 ; check the job exists
	move.l	a1,jcb_rflg(a0) 	 ; set new flag address
shd_susp
	move.w	d3,d0			 ; forever?
	bmi.s	ssj_forever		 ; ... yes
	mulu	sys_polf(a6),d0 	 ; ... adjust timer
	divu	#sys.polf,d0
	add.w	sys_pict(a6),d0 	 ; add current missing polls
	bpl.s	ssj_setw		 ; ... ok wait
	move.w	#$7fff,d0		 ; greatest positive
	bra.s	ssj_setw
ssj_forever
	moveq	#-1,d0			 ; ensure -ve is -1
ssj_setw
	move.w	d0,jcb_wait(a0) 	 ; and set wait
	bra.s	shd_done

; unsuspend

shd_usjb
	bsr.s	shd_ckjx		 ; check the job exists
	bsr.s	shd_relf		 ; release flag
	clr.w	jcb_wait(a0)		 ; un-suspend
	bra.s	shd_done

shd_relf
	move.l	jcb_rflg(a0),d0 	 ; get flag address
	beq.s	shd_rts 		 ; ... none
	clr.l	jcb_rflg(a0)		 ; (and clear address)
	exg	d0,a1
	clr.b	(a1)			 ; clear flag
	move.l	d0,a1
shd_rts
	rts

; activate

shd_acjb
	bsr.s	shd_ckjx		 ; check the job exists
	tst.b	jcb_pinc(a0)		 ; executing?
	bne.s	shd_nc			 ; ... yes, can't activate
	tst.l	sys_jbpt(a6)		 ; any job yet?
	bge.s	shd_cache		 ; ... yes
	clr.l	sys_jbpt(a6)		 ; ... no, but there will soon be

shd_cache
	tst.w	sys_castt(a6)		 ; cache in use?
	bmi.s	shd_spc 		 ; ... no
	move.w	#sys.casup,sys_castt(a6) ; suppressed now
	jsr	sms.cdisb		 ; disabled

shd_spc
	move.l	jcb_strt(a0),jcb_pc(a0)  ; set program counter
	tst.w	d3			 ; suspend?
	bge.s	shd_spr 		 ; ... no

	move.l	a0,a5			 ; save this job pointers
	move.l	d1,d7
	bsr.l	sms_cjid		 ; now do current job
	move.w	#jcb.wjob,jcb_wait(a0)	 ; wait for job
	st	jcb_wjob(a5)
	move.l	d1,jcb_wjid(a5) 	 ; set waiting ID
	move.l	a5,a0			 ; restore pointers
	move.l	d7,d1
	bra.s	shd_spr 		 ; set priority

;  set priority

shd_spjb
	bsr.s	shd_ckjx		 ; check the job exists
shd_spr
	move.b	d2,jcb_pinc(a0) 	 ; set priority

shd_done
	st	sys_rshd(a6)		 ; reschedule
	bra.l	sms_rtok		 ; return ok


shd_ckjx
	bsr.l	sms_ckjx		 ; check the job exists
	beq.s	shd_rts 		 ; ... ok
	addq.l	#4,sp			 ; pop return address
shd_rte
	bra.l	sms_rte 		 ; ... and give up

shd_nc
	moveq	#err.nc,d0		 ; not complete
	bra.s	shd_rte
	end
