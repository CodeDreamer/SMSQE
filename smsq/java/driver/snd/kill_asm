; KILLSOUND kills QSSS sound & a job named "SOUNDFILE JOB" V1.01 (c) W. Lenerz 2004

; copyright (c) - see the licence in the documentation

; v. 1.01	2004 Nov 27 18:28:34


	section sound

	xdef	killsound
	xref	sndname
	include 	dev8_keys_qdos_io
	include 	dev8_keys_qdos_ioa
	include 	dev8_smsq_q40_hdop_data
	include 	dev8_keys_68000
	include 	dev8_keys_err
	include 	dev8_keys_sss
	include 	dev8_keys_sbasic
	include 	dev8_keys_qdos_sms
	include 	dev8_keys_qlv
		
jb_info
	move.l	d1,d4			; save Job ID
	moveq	#sms.injb,d0		; get Job information
	moveq	#0,d2			; scan whole tree
	trap	#1
	move.l	d1,d5			; save next Job ID
	addq.l	#6,a0			; move to flag
	tst.l	d0			; check errors
	rts

killsound
	lea	sndname,a4
	moveq	#0,d1			; start from job 0
utj_loop
	bsr.s	jb_info 		; get Job information
	bne.s	killsound		;  ... oops, try again
	cmp.w	#$4afb,(a0)+		; check flag
	bne.s	nxtjob			; none, can't be me
	move.l	a4,a1
	move.w	(a0)+,d0
	cmp.w	(a1)+,d0		; compare name length
	bra.s	do_cplp
cmp_lp	cmp.b	(a0)+,(a1)+		; compare name itself
do_cplp dbne	d0,cmp_lp
	beq.s	killjob 		; ... name found
;	 bne.s	 sf_nosss		  ; ... name not found
;	 bne.s	 killsnd		 ; ... name not found
nxtjob
	move.l	d5,d1			; restore next job id
	bne.s	utj_loop		; ... and check name of next job
	bra.s	killsnd 		; ob not found
killjob moveq	#sms.frjb,d0
	clr.l	d3
	move.l	d4,d1			; kill this job
	trap	#1			; kill job now

killsnd move.l	exv_i4,a3		; point to Interrupt level 4
	move.l	-(a3),a2		; this could be SSSS vector
	cmp.l	#sss.flag,-(a3) 	; is it really?
	bne.s	sf_nosss		; no!
	jsr	sss_kill(a2)
	moveq	#0,d0
	rts
sf_nosss
	moveq	#err.nimp,d0
	rts

	end
