; SPP Set Baud Rate	  1999       Tony Tebby

	section spp

	xdef	spp_baud
	xref	spp_open

	include 'dev8_keys_sys'
	include 'dev8_keys_serparprt'
	include 'dev8_keys_iod'
	include 'dev8_keys_err'
	include 'dev8_smsq_smsq_base_keys'
;+++
; Set Baud Rate
;
;	d1 c p	baud rate (n*10) + 0, 1, 2 .... 16
;
;	status return 0 or err.ipar
;---
spp_baud
sppb.reg reg	 d1/d2/d7/a3/a4
	movem.l sppb.reg,-(sp)
	move.l	d1,d0
	divu	#19200,d0		 ; check if n*19200
	swap	d0
	cmp.w	#16,d0
	bls.s	sppb_port

	and.l	#$ffff,d1		 ; make word only

sppb_port
	move.l	d1,d0
	divu	#300,d0 		 ; get port
	moveq	#0,d2
	move.w	d0,d2			 ; baud rate / 300
	clr.w	d0
	swap	d0

	sub.l	d0,d1			 ; baud rate not including port

	tst.w	d0			 ; port specified?
	beq.s	sppb_look
	subq.w	#1,d0

sppb_look
	lea	spp_open,a4
	lea	sys_iodl(a6),a3 	 ; ... and linked list of device drvs
sppb_loop
	move.l	(a3),d7
	beq.s	sppb_nimp		 ; none at all!!
	move.l	d7,a3
	cmp.l	iod_open-iod_iolk(a3),a4 ; the right driver?
	bne.s	sppb_loop

	cmp.w	spd_nser-iod_iolk(a3),d0 ; port exists?
	bge.s	sppb_nimp
	move.l	spd_pser-iod_iolk(a3),a3 ; serial block
	mulu	#spd.len,d0
	add.l	d0,a3			 ; the right linkage
	move.l	spd_baud(a3),d0 	 ; baud routine?
	beq.s	sppb_nimp		 ; ... no
	tst.w	spd_port(a3)		 ; port defined
	ble.s	sppb_nimp		 ; ... no
	move.l	d0,a4
	jsr	(a4)


sppb_exit
	movem.l (sp)+,sppb.reg
	move.l	sms.rte,a5		 ; return
	jmp	(a5)

sppb_nimp
	moveq	#err.nimp,d0
	bra.s	sppb_exit



	end
