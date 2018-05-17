; SMSQ Format device	  V2.00    1986  Tony Tebby  QJUMP

	section ioa

	xdef	ioa_frmt

	xref	ioa_ffsd	 ; find filing system device
	xref	sms_rte

	include dev8_keys_iod
;+++
;	d0  r	error return 0, or not found
;	a0 c s	device_medium name
;	a6 c  p base of system variables
;
;	all other registers preserved
;---
ioa_frmt
reglist reg	d3-d6/a1-a4
	movem.l reglist,-(sp)
	move.l	a0,a1			 ; for ffsd
	bsr.l	ioa_ffsd		 ; find filing system device
	bne.s	ifm_exit
	move.l	a0,a1			 ; for format
	move.l	a2,a4
	move.l	d5,d1			 ; drive number
	move.l	iod_frmt(a3),a2
	jsr	(a2)			 ; and format it

ifm_exit
	movem.l (sp)+,reglist
	bra.l	sms_rte
	end
