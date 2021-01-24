; QXL Receive data from port	 2000	     Tony Tebby

	section spp

	xdef	spp_rxser

	xref	qxl_mess_prnext
	xref	iob_pbyt
	xref	iob_room
	xref	iob_eof

	include 'dev8_smsq_qxl_keys'
	include 'dev8_smsq_qxl_comm_keys'
	include 'dev8_keys_serparprt'

;+++
; Receive data from port
;
;	a4 c  u pointer to message
;
;---
spp_rxser
	moveq	#$f,d1
	and.w	(a4)+,d1		 ; port
	move.w	(a4)+,d2		 ; message length
	move.l	a4,a1			 ; data
	moveq	#3,d0
	add.w	d2,d0
	and.w	#$fffc,d0
	add.w	d0,a4			 ; next message

	move.l	qxl_spp_link,d0 	 ; spp linkage
	beq.s	srxp_exit

	move.l	d0,a3
	move.l	spd_pser(a3),a3 	 ; serial ports
	mulu	#spd.len,d1		 ; offset
	add.l	d1,a3			 ; this block

 
	move.l	spd_ibuf(a3),a2
	move.l	a2,d0
	bgt.s	srxp_eloop
	bra.s	srxp_exit

srxp_loop
	move.b	(a1)+,d1

	move.b	spb_fz(a2),d0		 ; <FF> or CTRL Z
	subq.b	#1,d0
	ble.s	srxp_ckcr		 ; ... not CTRL Z
	move.b	d1,d0
	tst.b	spb_prty(a2)		 ; parity?
	beq.s	srxp_ckcz		 ; ... no
	bclr	#7,d0			 ; ... ignore it
srxp_ckcz
	cmp.b	#26,d0			 ; is it CTRL Z?
	bne.s	srxp_ckcr
	jsr	iob_eof 		 ; mark end of file
	st	spd_ibuf(a3)		 ; no input buffer pointer now
	bra.s	srxp_noinput

srxp_ckcr
	move.b	spb_cr(a2),d0		 ; cr to lf?
	beq.s	srxp_pbyt		 ; ... no
	cmp.b	d0,d1			 ; cr?
	bne.s	srxp_pbyt		 ; ... no
	move.b	spb_lf(a2),d1		 ; ... yes
	beq.s	srxp_eloop		 ; cr ignored

srxp_pbyt
	jsr	iob_pbyt		 ; put byte in buffer
srxp_eloop
	dbra	d2,srxp_loop

	jsr	iob_room
	cmp.l	#qxm.rmdata,d0		 ; enough?
	bgt.s	srxp_exit		 ; ... yes

srxp_noinput
	sf	spd_iact(a3)		 ; inactivate input
	move.w	spd_port(a3),d1
	subq.w	#1,d1			 ; port number
	move.l	qxl_message,a2
	bclr	d1,qxl_ms_flow+qxm_com+1(a2) ; dragon 8 ports only
	beq.s	srxp_exit		 ; already no flow
	move.l	#qxm.flowlen<<16+qxm.flowqx<<8,qxl_ms_flow+qxl_ms_len(a2) ; send message next time
srxp_exit
	jmp	qxl_mess_prnext

	end
