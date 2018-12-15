; Gold Card SER device initialisation  V2.03    1994  Tony Tebby

	section ser

	xdef	ser_init

	xref.l	ser_vers
	xref	iou_idset
	xref	iou_idlk
	xref	ser_oopr
	xref	ser_iopr


	include 'dev8_keys_sys'
	include 'dev8_keys_iod'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_par'
	include 'dev8_mac_vecx'
	include 'dev8_mac_assert'
	include 'dev8_smsq_smsq_base_keys'
;+++
; SER driver initialisation.
;
;	a3 c  p   PAR linkage block / SER1 linkage block
;	status return 0
;---
ser_init
	moveq	#sms.xtop,d0
	trap	#do.smsq

; set up PRT

	assert	prd_sere,prd_pare-1,prd_prtp-2
	move.l	#$ffff0100,prd_prtl+prd_sere(a3) ; par/ser enabled, port 1
	move.w	#prd_prtd-prd_prt-prd_ser1,prd_prt+prd_ser1(a3) ; ser is prt
	move.w	#4,prd_prtd(a3)
	move.l	#'SER1',prd_prtd+2(a3)

; set up SER

	lea	prd_ser1-iod.sqhd(a3),a0 ; base of ser block
	lea	ser_link,a3		 ; setup ser linkage
	jsr	iou_idset
	jsr	iou_idlk		 ; link in

	addq.b	#2,prd_port(a3) 	 ; two ports

	lea	ser_oopr,a1		 ; output activate
	lea	ser_iopr,a2		 ; input re-activeate (XON XOFF only)

	lea	(a3),a0 		 ; base of ser1 linkage

	lea	sms.qlser,a5
	move.l	a0,d0
	swap	d0
	jsr	sms.wbase		 ; ... saved MSW
	swap	d0
	jsr	sms.wbase

	bsr.s	seri_setlk		 ; set up ser1 constants
	subq.b	#1,prd_serx(a0) 	 ; adjust first port number
	lea	prd_ser2-prd_ser1(a3),a0 ; ... and ser2
seri_setlk
	move.l	a1,prd_oopr(a0) 	 ; set output operation
	move.l	a2,prd_iopr(a0) 	 ; and input operation
	lea	ser_setlk,a4
seri_setlp
	move.w	(a4)+,d0		 ; next constant
	beq.s	seri_done
	move.l	(a4)+,(a0,d0.w) 	 ; to set
	bra.s	seri_setlp
seri_done
	moveq	#0,d0
	rts

; TABLES

ser_link
	dc.l	0	   ; pre-allocated
	dc.l	1<<iod..ssr+1<<iod..scn serial and name

	novec		   ; no xint
	novec		   ; no poll
	vecx	ser_sched  ; sched
	vecx	ser_io	   ; and a full set of opens
	vecx	ser_open
	vecx	ser_close

	vecx	ser_cnam

ser_setlk
	assert	prd_sere,prd_serx-2,prd_hand-3
	dc.w	prd_sere,$ff00,$02ff  ; set SER enabled, port and handshake
	dc.w	prd_room,0,prd.room   ; set room
	dc.w	prd_ilen,0,prd.ilen   ; and size
	assert	prd_hwt,prd_timo-1,prd_txbd-2,prd_rxbd-3
	dc.w	prd_hwt,$0001,$0101   ; type, timeout, baud
	dc.w	0

	end
