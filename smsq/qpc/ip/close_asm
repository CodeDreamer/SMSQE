; Close IP	   V1.00				 2004	 Marcel Kilgus

	section ip

	xdef	ip_close

	include 'dev8_keys_qlv'
	include 'dev8_smsq_qpc_ip_data'
	include 'dev8_smsq_qpc_keys'

ip_close
	move.l	a0,-(sp)
	move.l	chn_data(a0),a0
	dc.w	qpc.ipcls
	move.l	(sp)+,a0

	move.w	mem.rchp,a2
	jmp	(a2)

	end
