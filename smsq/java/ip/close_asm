
; Close IP	   V1.00				 2004	 Marcel Kilgus
; adaptep for SMSQmumlator (wl)
; 1.01 check for UDP (wl)

	section ip

	xdef	ip_close
	xref	udp_name
	xref	udd_name

	include 'dev8_keys_qlv'
	include 'dev8_smsq_java_ip_data'
	include 'dev8_keys_java'

ip_close
	move.l	d3,-(a7)
	moveq	#-jt9.cls,d0
	move.l	udp_name,d3
	cmp.l	chn_typ(a0),d3
	beq.s	cont
	move.l	udd_name,d3
	cmp.l	chn_typ(a0),d3
	beq.s	cont
	moveq	#jt9.cls,d0
	
cont	move.l	(sp),d3
	move.l	a0,(sp)
	move.l	chn_data(a0),a0
	dc.w	jva.trp9
	move.l	(sp)+,a0

	move.w	mem.rchp,a2
	jmp	(a2)

	end
