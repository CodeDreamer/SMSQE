; Polling interrupt handler   V2.01    1986  Tony Tebby  QJUMP

	section shd

	xdef	shd_poll

	xref	shd_pshd	; polling reschedule (could do better)

	include 'dev8_keys_sys'
	include 'dev8_keys_iod'
	include 'dev8_keys_psf'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_mac_creg'
;+++
; This routine handles the polling interrupt which is used to invoke
; regular, low priority tasks.
; The last polled task is the hardware specific poll routine.
; This is followed by the Job scheduler itself,
; which is only invoked if the interrupted operation is not atomic.
;
; On entry the primary stack frame should be saved, and the stack should be
; clean except for the primary stack frame.
;
; All registers except the primary stack frame and the status are preserved.
;
;	a6 c  p pointer to system variables
;---
shd_poll
	movem.l psf.oreg,-(sp)		 ; save all the other regs

	lea	sys_poll(a6),a0 	 ; list of polling actions

spl_sloop
	moveq	#0,d3
	move.w	sys_pict(a6),d3
	move.l	(a0),d0
	beq.s	spl_done		 ; ... done
	move.l	d0,a0
	lea	-iod_pllk(a0),a3	 ; base of linkage
	move.l	iod_plad(a3),a4 	 ; address of polling routine
	move.l	a0,-(sp)
	jsr	(a4)			 ; do routine
	move.l	(sp)+,a0		 ; restore
	bra.s	spl_sloop

spl_done
	move.l	sms.hpoll,a5
	jsr	(a5)			 ; hardware related polling operations

	tst.w	sys_castt(a6)		 ; cache and scheduler suppressed?
	bgt.s	spl_wait		 ; ... yes

	movem.l (sp)+,psf.oreg
	btst	#psf..sup,psf_sr(sp)	 ; supervisor mode when interrupted?
	beq.s	shd_pshd		 ; ... no
	st	sys_rshd(a6)		 ; ... yes, request reschedule
	movem.l (sp)+,psf.reg		 ; and give up
	rte

spl_wait
	subq.w	#1,sys_castt(a6)	 ; count down
	bgt.s	spl_exit
	clr.w	sys_castt(a6)		 ; cache enabled now (unnecessary)

	jsr	sms.cenab		 ; enable both caches

spl_exit
	movem.l (sp)+,psf.oreg
	movem.l (sp)+,psf.reg
	rte
	end
