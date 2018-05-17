; SBAS_PROCS_RESET - SBASIC Reset   V2.01    2000  Tony Tebby

	section exten

	xdef	reset

	xref	smsq_sreset

	include 'dev8_keys_qdos_sms'

;+++
; RESET
;---
reset
	moveq	#sms.xtop,d0
	trap	#1			 ; go to supervisor mode
	jmp	smsq_sreset		 ; do standard soft reset

	end
