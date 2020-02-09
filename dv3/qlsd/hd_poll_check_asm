; DV3 QLSD polling routine
;
; 2018-06-10  1.01  Clear new crdused flag on timeout (MK)

	section dv3

	xdef	hd_poll_check

	xref	hd_poll

	include 'dev8_dv3_qlsd_keys'
	include 'dev8_keys_sys'

;+++
; Polling Interrupt Routine
;---
hd_poll_check
	tst.b	hdl_actm(a3)		; action counter timed out?
	bne.s	hd_check_lock		; ... no, check for poll
	clr.b	qlsd_crdused(a3)	; ... yes, clear card usage flags
hd_check_lock
	tst.b	sys_qlsd(a6)		; QLSD in use?
	bpl.l	hd_poll 		; ... no
	rts

	end
