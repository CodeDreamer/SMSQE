; SMSQmulator Interrupt server	V1.00  (c) W. Lenerz 2012

	section qd

	xdef	qd_int2

	include 'dev8_keys_sys'
	include 'dev8_keys_psf'
	include 'dev8_keys_qlhw'
	include 'dev8_keys_iod'
	include 'dev8_smsq_smsq_base_keys'

;+++
; SMSQmulator Interrupt Server	 - there is only one interrupt FTTB - 50 Hz frame
;---
qd_int2

; set primary stack frame and locate system variable base
	movem.l psf.reg,-(sp)		 ; save main working registers
	move.l	sms.sysb,a6		 ; system variable base
shd_poll
	addq.w	#1,sys_pict(a6) 	 ; one more poll
	move.l	sms.spoll,a5
	jmp	(a5)			 ; polling interrupt   (calls smsq_shd_poll_asm)

	end
