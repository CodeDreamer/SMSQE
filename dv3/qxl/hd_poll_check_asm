; DV3 QXL Hard Disk Polling Routine	   1993     Tony Tebby

	section dv3

	xdef	hd_poll_check

	xref	hd_poll

	include 'dev8_keys_sys'

;+++
; Hard disk polling Interrupt Routine - QXL
;
hd_poll_check
	tst.b	sys_stiu(a6)		 ; sector transfer buffer in use?
	bpl.l	hd_poll 		 ; ... no
	rts

	end
