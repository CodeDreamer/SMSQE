; SMSQmulator Host module  V 1.02 (c) W. Lenerz 2012
;
; this is the very start of the SMSQE binary
; based on
; SMSQ GOLD Card Host module	v1.01
; 2003-10-05  1.01  Fixed copy problem that could hang SMSQ/E later (MK)

	section host

	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_stella_bl'
	include 'dev8_keys_java'

	data	4			; flag for LRESPR
base
	move.w	#$2700,sr		; no interrupts
	move.l	a7,d7			; top of ram
	sub.l	#jva.ssp,d7		; minus space for ssp = new ramptop
	lea	trailer,a0
	pea	base			; set base address for debug
	add.l	sbl_mbase(a0),a0
	jmp	(a0)			; jump to "smsq_smsq_loader_asm"

	section trailer

trailer
	dc.l	0			; no header
	dc.l	trailer-base		; length of module
	dc.l	0			; length
	dc.l	0			; checksum
	dc.l	0			; no fixup
	dc.l	0

	end
