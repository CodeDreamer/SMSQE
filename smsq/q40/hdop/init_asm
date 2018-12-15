; Q40 SMS HDOP initialisation	  1988  Tony Tebby

	section init

	xdef	hdop_init

	xref	hdop_poll

	include dev8_keys_qdos_sms
	include dev8_keys_iod
	include dev8_smsq_q40_hdop_data

hdop_init			    ; link in polling hdop code
	move.l	#ho_end,d1		 ; create space for linkage
	moveq	#sms.achp,d0
	moveq	#0,d2			 ; permanent
	trap	#do.sms2
	tst.l	d0
	bne.s	hoi_rts 		 ; oops

	move.l	a0,a3

	lea	iod_plad(a3),a0 	 ; IPC polling
	lea	hdop_poll,a1		 ; routine
	move.l	a1,(a0)

	lea	iod_pllk(a3),a0 	 ; link it in
	moveq	#sms.lpol,d0
	trap	#do.sms2
hoi_rts
	tst.l	d0
	rts
	end
