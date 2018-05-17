; Set Owner of channel	  V2.00    1994  Tony Tebby  QJUMP

	section ioa

	xdef	ioa_sown

	xref	io_ckchn
	xref	sms_rte

	include 'dev8_keys_chn'

;+++
;	d0  r	error return 0, or not found
;	d1 c  p new owner
;	a0 c s	channel id
;	a6 c  p base of system variables
;
;	all other registers preserved
;---
ioa_sown
reglist reg	a0/a3
	movem.l reglist,-(sp)
	bsr.l	io_ckchn		 ; check the channel ID (a0,a3)
	bne.s	iso_exit
	move.l	d1,chn_ownr(a0)
	moveq	#0,d0

iso_exit
	movem.l (sp)+,reglist
	bra.l	sms_rte

	end
