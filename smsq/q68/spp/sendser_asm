; Q68 send data to SER port	 2000	     Tony Tebby
;				 2017	     W. Lenerz

; This just takes a byte out of the SER output queue and puts it into
; the hardware FIFO. Sending is always done w/o parity.

	section spp

	xdef	spp_sendser

	xref	iob_gbyt
	xref	iob_room

	include 'dev8_keys_q68'
	include 'dev8_keys_serparprt'
	include 'dev8_keys_k'

;+++
; Send data to port.
;
;	a6 = sysvars
;
;---

spp_sendser
	btst	#q68..txmpty,uart_status ; send queue empty?
	beq.s	srxp_exit		; no, can't send anything, we're done ->
	move.l	q68_ser_link,a3 	; my linkage block
	tst.b	spd_oact(a3)		; output active?
	beq.s	srxp_exit		; no, nothing to send, then  ->
srxp_loop
	move.l	spd_obuf(a3),d0 	; output queue
	ble.s	srxp_nomore		; there is none ->
	move.l	d0,a2

	jsr	iob_gbyt		; get byte from output queue (special return)
	beq.s	srxp_sbyt		; =0 = got one, send it ->
	blt.s	srxp_nomore		; <0 = none got, we're done ->
					; >0 = eof, possibly
	subq.b	#1,d1			; is ff required?
	blt.s	srxp_exit		; no, done ->
	bgt.s	qss_cz			; yes,	but really it is CTRL Z  ->
	moveq	#k.ff,d1
	bra.s	srxp_sbyt		; send ff ...
qss_cz	moveq	#26,d1			; ... or send CTRL Z
srxp_sbyt
	move.b	d1,uart_txdata		; put byte into hardware port
srxp_exit
	rts
srxp_nomore
	sf	spd_oact(a3)		; inactivate output
	bclr	#q68..txstat,uart_status; and interrupt
	rts
spp_end

	end

	
;;;;;;;;;;;;;;;;;;;;;;
; if sending with parity, use his code AND modify the regs saved (D7)
;	 move.b  spb_prty(a2),d7	 ; set parity
;	 jsr	 iob_gbps		 ; get byte; needs parity in D7
;;;;;;;;;;;;;;;;;
