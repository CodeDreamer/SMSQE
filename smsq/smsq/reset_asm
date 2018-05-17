; SMSQ_RESET SMSQ standard hard and soft resets  V2.0   2000  Tony Tebby

	xdef	smsq_sreset
	xdef	smsq_hreset
	xdef	smsq_xreset

	include 'dev8_smsq_smsq_base_keys'

	section reset

; To suppress resets globally, just replace these routines by a return

smsq_sreset
	lea	-1,a4			 ; set all RAM
;
; special entry setting RAMTOP
;
smsq_xreset
	jmp	sms.base		 ; and start from soft reset routine

smsq_hreset
	jmp	sms.base+4		 ; and call hard reset routine

	end
