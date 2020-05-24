; DV3 Q68 SDHC card Polling Routine	   2017-2020	  W. Lenerz
; 2020-03-29	 1.01 incorporated MK's idea of a timer
; based on
; DV3 QXL Hard Disk Polling Routine	   1993     Tony Tebby

	section dv3

	xdef	hd_poll_check

	xref	hd_poll

	include 'dev8_keys_sys'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'

;+++
; Hard disk polling Interrupt Routine - Q68
; this is currently imperfect, as it doesn't distiguish between both drives
; so if one is being used, the other won't be.
;
hd_poll_check
	tst.b	hdl_actm(a3)		; action counter timed out?
	bne.s	ck_use			; ... no, check for poll
	clr.b	ddl_rcnt(a3)		; ... yes, clear card usage flags

ck_use	tst.b	sys_cdiu(a6)		; sector transfer buffer in use?
	beq.l	hd_poll 		; ... no
	rts

	end
