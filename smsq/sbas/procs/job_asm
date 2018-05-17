; SBAS_PROCS_JOB - SBASIC Job Procedures  V2.00    1994  Tony Tebby
; 2004-04-02	2.01	quit takes optional long parameter (pjw)

	section exten

	xdef	sbasic
	xdef	quit
	xdef	job_name

	xref	gu_mexec
	xref	ut_gxnm1
	xref	ut_gtint
	xref	ut_gxli1

	include 'dev8_keys_sbasic'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_k'
	include 'dev8_keys_err'
	include 'dev8_mac_assert'
;+++
; Start SBASIC daughter
;---
sbasic
	move.l	sp,a4			 ; entry stack value
	clr.w	-(sp)			 ; no string
	jsr	ut_gtint		 ; get integer parameter
	bne.s	sbs_exit
	moveq	#0,d6			 ; default for nothing on stack
	moveq	#4,d5

	subq.w	#1,d3			 ; how many parameters?
	bgt.s	sbs_ipar		 ; too many
	blt.s	sbs_create		 ; ... none

	move.w	(a6,a1.l),d0		 ; parameter
	divu	#10,d0			 ; convert to two digits
	lsl.l	#6,d0
	lsl.w	#2,d0			 ; scale the bits
	swap	d0
	not.l	d0
	move.l	d0,-(sp)		 ; and put on stack
	moveq	#1,d6
	moveq	#8,d5

sbs_create
	move.w	d6,-(sp)		 ; number of channels
	move.w	d5,-(sp)		 ; size of stack

	moveq	#-1,d1			 ; owned by me
	moveq	#$08,d2 		 ; standard priority
	swap	d2
	move.l	#sb.jobsz,d3		 ; dataspace
	move.l	sb_sbjob(a6),a0 	 ; the job
	move.l	sp,a1			 ; the parameters
	jsr	gu_mexec
sbs_exit
	move.l	a4,sp			 ; restore the stack
	rts
sbs_ipar
	moveq	#err.ipar,d0
	bra.s	sbs_exit

;+++
; Terminate Sbasic program with a(n error) code (pjw 2oo4)
;
*	QUIT [code]
;
; where code is any long integer (optional)
;---

quit
	moveq	#0,d3			; assume no return code
	cmpa.l	a3,a5			; parameters?
	bls.s	quit_now		;  none => ok
	bsr	ut_gxli1		; get one long integer
	bne.s	quit_rts		;  oops! notify this job of parameter error
	move.l	0(a6,a1.l),d3		; d3 = return code

quit_now
	moveq	#-1,d1
	moveq	#sms.frjb,d0
	trap	#do.sms2
quit_rts
	rts

job_name
	cmp.l	#sb.flag,sb_flag(a6)	 ; SBASIC?
	bne.s	jnm_ok			 ; ... no

	lea	sb_offs+6-sb_vars(a6),a0 ; header flag
	cmp.w	#$4afb,(a0)+		 ; has it a name?
	bne.s	jnm_ok

	jsr	ut_gxnm1
	bne.s	jnm_rts 		 ; ... bad
	add.l	a6,a1
	move.w	(a1)+,d2		 ; name length
	moveq	#40,d0			 ; max?
	cmp.w	d0,d2
	ble.s	jnm_set
	move.w	d0,d2
jnm_set
	move.w	d2,(a0)+		 ; length
jnm_loop
	move.l	(a1)+,(a0)+		 ; it does not matter about overrun
	subq.w	#4,d2
	bgt.s	jnm_loop

jnm_ok
	moveq	#0,d0
jnm_rts
	rts

	end
