; Q68 SER DEVICE DEFINITION & physical routines  V2.10	 1999	Tony Tebby
;						 V2.11	 2017	W. Lenert
	section spp

	xdef	q68_serdef

	xref	iob_room
	xref	gu_achpp

	include 'dev8_keys_serparprt'
	include 'dev8_keys_err'
	include 'dev8_keys_q68'
	include 'dev8_smsq_qxl_keys'
	include 'dev8_smsq_qxl_comm_keys'
	include 'dev8_mac_vecx'
	include 'dev8_mac_assert'

;+++
; SER driver definition
;
;---
q68_serdef
	vec	q68_check
	vec	q68_vector
	vec	q68_preset
	vec	q68_init
	vec	q68_istable

;+++
; Preset table defining hardware specific values
; The format of this table is
; entry,length,data
; where
; entry  = the offset within the definition block (keys_serparprt)
; length = how many bytes to fill in
; data	 = the data to set
;
; If you anly want to set a word at an offset that may contain a long word,
; use offset+2 as entry
;---
q68_preset
	assert	spd_pdir,spd_hand-1
	dc.w	spd_pdir,2,q68_prhd	; no parity, no hardware handshake
	dc.w	spd_room+2,2,spd.room	; input buffer spare room
	dc.w	spd_ilen+2,2,spd.ilen	; input buffer length
	dc.w	0

;+++
; Vector table defining hardware specific IO routines
;---
q68_vector
	dc.w	spd_open,q68_open-*-2
	dc.w	spd_close,q68_close-*-2
	dc.w	spd_baud,q68_baud-*-2
	dc.w	spd_iopr,q68_iopr-*-2
	dc.w	spd_oopr,q68_oopr-*-2
	dc.w	0

;+++
; Interrupt server table
;---
q68_istable
	dc.w	0		      ; no server

;+++
; Routine to check whether a particular ports exist
;
;	d0 cr	port to check / 0 if it does not exist
;	d0 cr	0 / highest port number
;	d7    s if d0 = 0 on entry, otherwise preserved
;	a4  r	hardware base
;
;	status returned as d0
;---
q68_check
	tst.b	d0			; check highest?
	bne.s	qsdc_portn		; no, check port n ->
q68_ckok
	moveq	#1,d0			; there is only one ser port
	rts

qsdc_portn
	subq.l	#1,d0			; are we trying for port 1?
	beq.s	q68_ckok		; yes, done->
	clr.l	d0			; nothing else exists
	rts


;+++
; SER port linkage / hardware initialisation
;
;	a3 c  p linkage for this port
;	a4    s hardware base
;
;	status returned zero
;---
q68_init
;	 move.w  #2082,uart_prescale	 ; preset 1200 bauds
	moveq	#0,d0
	rts

;+++
; SER port baud
;
;	d1 c  p baud rate
;	d2 c  s
;	a3 c  p SER port linkage block
;	all other registers preserved
;
;---
baud.reg reg	d3/a1
q68_baud
	movem.l baud.reg,-(a7)
	lea	bauds,a1
	move.l	a1,d0
	moveq	#8,d3
bd_lp	cmp.l	(a1)+,d1
	dbeq	d3,bd_lp
	bne.s	bd_err
	sub.l	d0,a1
	move.l	a1,d0
	subq.l	#4,d0
	lsr.w	#1,d0
bd_fnd	lea	prescale,a1
	move.w	(a1,d0.w),uart_prescale
	moveq	#0,d0
	movem.l (a7)+,baud.reg
	rts
bd_err	moveq	#err.nimp,d0
	movem.l (a7)+,baud.reg
	rts
prescale				; prescaled values according to baud rate
	dc.w	2082,1041,520,259,129,64,42,21,10
bauds					; allowed baud values
	dc.l	1200,2400,4800,9600,19200,38400,57600,115200,230400
		  

;//    1200 baud => 2082
;//    2400 baud => 1041
;//    4800 baud =>  520
;//    9600 baud =>  259
;//   19200 baud =>  129
;//   38400 baud =>   64
;//   57600 baud =>   42
;//  115200 baud =>   21
;//  230400 baud =>   10

	
;+++
; SER port close. If there is no input buffer, it is a nop.
;
;	d0 c  p pointer to input buffer
;	a3 c  p COM port linkage block
;	all other registers preserved
;	status returned according to D0
;---
q68_close
	tst.l	 d0			 ; input queue?
	beq.s	 rx_nint		 ; ... no

;+++
; SER port disable rx
;
;	a3 c  p SER port linkage block
;	all other registers preserved
;	status returned according to D0
;---
q68_rxdi
	move.l	d0,-(sp)
	sf	spd_iact(a3)		 ; input not active
	move.l	(sp)+,d0
rx_nint bclr	#q68..rxstat,uart_status ; no rx interrupt
q68_rts
	rts


;+++
; SER port open operation (enable rx)
;
;	d0  r
;	a3 c  p SER port linkage block
;	all other registers preserved
;	status returned 0
;---
q68_open
	moveq	#0,d0
;	 bra.s	 q68_rxen		  ; enable receive

;+++
; SER port receive input operation (re-enable rx if not enabled)
;
;	a3 c  p SER port linkage block
;	all other registers preserved
;	status returned according to D0
;---
q68_iopr
	tst.b	spd_iact(a3)		; port is already active
	bne.s	q68_ioprts

q68_rxen
	movem.l d0/a2,-(sp)
	move.l	spd_ibuf(a3),d0 	; any buffer?
	ble.s	q68_iopexit		; ... no

	move.l	d0,a2
	jsr	iob_room		; check for enough room
	cmp.l	spd_room(a3),d0
	ble.s	q68_iopexit		; ... no
	st	spd_iact(a3)
	bset	#q68..rxstat,uart_status ; enable receive interrupts
q68_iopexit
	movem.l (sp)+,d0/a2

q68_ioprts
	tst.l	d0
	rts

;+++
; SER port transmit output operation
;
;	a3 c  p SER port linkage block
;	all other registers preserved
;	status returned according to D0
;---
q68_oopr
	st	spd_oact(a3)		; set output to go
	bset	#q68..txstat,uart_status ; interrupts on
	clr.l	d0
	rts

	end
