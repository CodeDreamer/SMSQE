; QPC Receive data from port	 2000	     Tony Tebby
;				 2000	     Marcel Kilgus
	section spp

	xdef	spp_rxser

	xref	iob_pbyt
	xref	iob_room
	xref	iob_eof

	include 'dev8_smsq_qpc_keys'
	include 'dev8_keys_serparprt'

;+++
; Receive data from port
;
;	d1 c  s port number
;
;---
spp_rxser
	move.l	qpc_spp_link,d0 	 ; spp linkage
	beq.s	srxp_exit

	dc.w	qpc.sique+2		 ; get queue address in d2
	move.l	d0,a3
	subq.w	#1,d1
	move.l	spd_pser(a3),a3 	 ; serial ports
	mulu	#spd.len,d1		 ; offset
	add.l	d1,a3			 ; this block

	tst.b	spd_iact(a3)
	beq.s	srxp_exit

	move.l	spd_ibuf(a3),a2
	move.l	a2,d0
	ble.s	srxp_exit

	jsr	iob_room		 ; room left
	move.l	d0,d3
	bne.s	srxp_eloop		 ; >0: enter data loop
	bra.s	srxp_noinput		 ; =0: disable input

srxp_loop
	dc.w	qpc.getq		 ; get byte in d1
	beq.s	srxp_exit		 ; no more data

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
	dbf	d3,srxp_loop

srxp_noinput
	sf	spd_iact(a3)		 ; inactivate input
srxp_exit
	rts

	end
