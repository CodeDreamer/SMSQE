; Setup name of IP channel  V1.02     2004  Marcel Kilgus
;   1.02 check for UDP channel	       2020  W. Lenerz
; v.1.01 adapted for SMSQmulator

	section ip

	xdef	ip_cnam

	xref	udp_name
	xref	udd_name

	include 'dev8_keys_java'
	include 'dev8_smsq_java_ip_data'

ip_cnam move.l	d3,-(a7)
	moveq	#-jt9.cnm,d0		; presume UDP
	move.l	udp_name,d3
	cmp.l	chn_typ(a0),d3
	beq.s	cont
	move.l	udd_name,d3
	cmp.l	chn_typ(a0),d3		; checfor UDD
	beq.s	cont
	moveq	#jt9.cnm,d0		; but it isn't, so it's tcp

cont	move.l	(sp),d3
	move.l	a0,(sp)
	move.l	chn_data(a0),a0 	; internal data
	dc.w	jva.trp9
	move.l	(sp)+,a0
	rts


	end
