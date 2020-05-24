; Gold card refresh display. Apparently not used

	section patch

	xdef	gl_refresh

	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_iod'
	include 'dev8_keys_sys'

gl_refresh
	moveq	#$10,d1
	moveq	#0,d2
	moveq	#sms.achp,d0		; allocate 16 bytes#
	trap	#do.sms2
	lea	refresh,a1
	move.l	a1,iod_plad(a0)
	addq.l	#iod_pllk,a0
	moveq	#sms.lpol,d0
	trap	#do.sms2
	rts


refresh
	move.b	sys_qlmr(a6),d1 	 ; screen mode
	btst	#1,d1
	bne.s	give_up 		 ; screen off
	move.l	#$20000,d0
	move.w	(a3),d0
	lsl.w	#1,d0			 ; scrub bit 15
	lsl.b	#1,d1			 ; second screen is now in X
	roxr.w	#1,d0			 ; X is in bit 15

	move.l	d0,a0			 ; base to refresh
	move.l	(a0),(a0)+		 ; 64 bytes
	move.l	(a0),(a0)+
	move.l	(a0),(a0)+
	move.l	(a0),(a0)+
	move.l	(a0),(a0)+
	move.l	(a0),(a0)+
	move.l	(a0),(a0)+
	move.l	(a0),(a0)+
	move.l	(a0),(a0)+
	move.l	(a0),(a0)+
	move.l	(a0),(a0)+
	move.l	(a0),(a0)+
	move.l	(a0),(a0)+
	move.l	(a0),(a0)+
	move.l	(a0),(a0)+
	move.l	(a0),(a0)+

	move.w	a0,(a3) 		 ; save next address to refresh
give_up
	rts
	end
