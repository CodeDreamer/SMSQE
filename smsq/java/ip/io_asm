; IP IO routines     V1.00				  2004  Marcel Kilgus
; 1.02 check for UDP (wl)
; 1.01 adapted for java wl

	section ip

	xdef	ip_io

	xref   udp_name
	xref   udd_name

	include 'dev8_keys_err'
	include 'dev8_keys_socket'
	include 'dev8_keys_java'
	include 'dev8_smsq_java_ip_data'

ip_io
	move.l	a0,-(sp)

ip_doio
	move.l	d0,-(sp)		; will be popped up in java
	moveq	#-jt9.io,d0
	move.l	udp_name,d4
	cmp.l	chn_typ(a0),d4
	beq.s	cont
	move.l	udd_name,d4
	cmp.l	chn_typ(a0),d4
	beq.s	cont
	moveq	#jt9.io,d0

cont	move.l	chn_data(a0),a0 	; Internal data
	dc.w	jva.trp9
ip_exit
	move.l	(sp)+,a0
	rts

	end
