; Setup name of IP channel  V1.00			   2004  Marcel Kilgus

	section ip

	xdef	ip_cnam

	include 'dev8_smsq_qpc_keys'
	include 'dev8_smsq_qpc_ip_data'

ip_cnam
	move.l	a0,-(sp)
	move.l	chn_data(a0),a0 	; Internal data
	dc.w	qpc.ipcnm
	move.l	(sp)+,a0
	rts

	end
