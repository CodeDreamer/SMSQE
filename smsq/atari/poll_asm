; Polling interrupt handler   V2.01    1986  Tony Tebby  QJUMP

	section shd

	xdef	at_poll
	xdef	hw_poll

	include 'dev8_keys_sys'
	include 'dev8_keys_iod'
	include 'dev8_keys_psf'
	include 'dev8_keys_atari'
	include 'dev8_smsq_smsq_base_keys'

;+++
; HW_POLL is fairly dummy
;---
hw_poll
	sf	sys_50i(a6)		 ; clear 50 Hz in use
	rts
;+++
; This routine handles the polling interrupt lists which are used to invoke
; regular, low priority tasks.
;
; This routine operates off a 200Hz interrupt from timer c.
; After after all 50Hz tasks have been completed, the Job scheduler is called.
;
;---
at_poll
	bclr	#mfp..tci,mfp_tcs	 ; clear in service
	movem.l psf.reg,-(sp)
	move.l	sms.sysb,a6		 ; system variables

	move.w	sys_rtcf(a6),d7
	subq.w	#1,sys_rtcf(a6) 	 ; 200 Hz counter
	bpl.s	atp_cpoll
	move.w	#199,sys_rtcf(a6)	 ; and count down again
	addq.l	#1,sys_rtc(a6)		 ; another second gone

atp_cpoll
	subq.b	#1,sys_plrq(a6) 	 ; time for poll
	bpl.s	atp_rter		 ; ... no

	cmp.b	#$25,psf_sr(sp) 	 ; level 5 service
	beq.s	atp_wait		 ; ... no

atp_dopoll
	move.b	#3,sys_plrq(a6) 	 ; reset count down

	addq.w	#1,sys_pict(a6) 	 ; one more poll

	tas	sys_50i(a6)		 ; is 50 Hz in service?
	bne.s	atp_rter		 ; ... yes

	and.w	#$fcff,sr		 ; restore interrupt level 5,6
	move.l	sms.spoll,a5
	jmp	(a5)

atp_wait
	st	sys_plrq(a6)		 ; ... yes, do not continue counting
atp_rter
	movem.l (sp)+,psf.reg		 ; restore regs
	rte				 ; and return


	end
