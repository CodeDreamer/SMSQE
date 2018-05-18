; Q68 Receive data from port	 2000	     Tony Tebby
;				 2017	     W. Lenerz


; this just takes a byte out of the hardware FIFO and puts it into
; the SER input queue

	section spp

	xdef	spp_rxser

	xref	iob_pbyt
	xref	iob_room
	xref	iob_eof

	include 'dev8_keys_q68'
	include 'dev8_keys_serparprt'

;+++
; Receive data from port
;
;	d1 c  s port number
;
;---

rxregs	reg	d0/d1/d3/a1/a2/a3
spp_rxser
	btst	#q68..rxmpty,uart_status ; receive buffer empty?
	bne.s	srxp_exit		; yes, done ->
	move.b	uart_rxdata,d1		; always get data
	move.l	q68_ser_link,a3
	tst.b	spd_iact(a3)		; is input active?
	beq.s	srxp_exit		; no nothing to get, then : bye ->
	move.l	spd_ibuf(a3),d0 	; input queue
	ble.s	srxp_exit		; there is none ->
	move.l	d0,a2

	jsr	iob_room		; room left
	move.l	d0,d3
	ble.s	srxp_noinput		; =0: no room left - disable input
	cmp.l	#15,d3			; not more than 16 bytes a pop
	blt.s	srxp_loop
	moveq	#14,d3
	bra.s	rx_chk
srxp_loop
	move.b	uart_rxdata,d1		; get data
; now check for CTRL Z etc
rx_chk	move.b	spb_fz(a2),d0		; <FF> or CTRL Z
	subq.b	#1,d0
	ble.s	srxp_ckcr		; ... not CTRL Z
	move.b	d1,d0
	tst.b	spb_prty(a2)		; parity?
	beq.s	srxp_ckcz		; ... no
	bclr	#7,d0			; ... ignore it
srxp_ckcz
	cmp.b	#26,d0			; is it CTRL Z?
	bne.s	srxp_ckcr
	jsr	iob_eof 		; mark end of file
	st	spd_ibuf(a3)		; no input buffer pointer now
	bra.s	srxp_noinput
srxp_ckcr
	move.b	spb_cr(a2),d0		; cr to lf?
	beq.s	srxp_pbyt		; ... no
	cmp.b	d0,d1			; cr?
	bne.s	srxp_pbyt		; ... no
	move.b	spb_lf(a2),d1		; ... yes
	beq.s	srxp_eloop		; cr ignored
srxp_pbyt
	jsr	iob_pbyt		; put byte in buffer
srxp_eloop
	move.b	uart_status,d1		; ser port status
	andi.b	#q68.rxand,d1		; receive buffer empty ?
	bne.s	srxp_exit
	dbf	d3,srxp_loop
srxp_exit
	rts

srxp_noinput
	sf	spd_iact(a3)		; inactivate input
	rts

spp_rxend

	end
