; QPC SER device definition	V2.10	 1999	Tony Tebby
;					 2000	Marcel Kilgus
	section spp

	xdef	qpc_serdef

	xref	iob_room
	xref	gu_achpp

	include 'dev8_keys_serparprt'
	include 'dev8_smsq_qpc_keys'
	include 'dev8_mac_vecx'
	include 'dev8_mac_assert'

;+++
; SER driver definition
;
;---
qpc_serdef
	vec	qsd_check
	vec	qsd_vector
	vec	qsd_preset
	vec	qsd_init
	vec	qsd_istable

;+++
; Preset table defining hardware specific values
;---
qsd_preset
	assert	spd_pdir,spd_hand-1
	dc.w	spd_pdir,2,$ffff      ; any parity, hardware handshake
	dc.w	spd_ilen+2,2,spd.ilen ; input buffer length
	dc.w	spd_room+2,2,spd.room ; input buffer spare room
	dc.w	spd_minbf,2,spd.room  ; minimum input buffer length
	dc.w	0

;+++
; Vector table defining hardware specific IO routines
;---
qsd_vector
	dc.w	spd_open,qsd_open-*-2
	dc.w	spd_close,qsd_close-*-2
	dc.w	spd_baud,qsd_baud-*-2
	dc.w	spd_iopr,qsd_iopr-*-2
	dc.w	spd_oopr,qsd_oopr-*-2
	dc.w	spd_cdchk,qsd_cdchk-*-2
	dc.w	0

;+++
; Interrupt server table
;---
qsd_istable
	dc.w	0		      ; no server

;+++
; Routine to check whether particular ports exist
;
;	d0 cr	port to check / 0 is it does not exist
;	d0 cr	0 / highest port number
;	d7    s if d0 = 0 on entry, otherwise preserved
;	a4  r	hardware base
;
;	status returned as d0
;---
qsd_check
	dc.w	qpc.sexst
	tst.l	d0
	rts

;+++
; SER port linkage / hardware initialisation
;
;	a3 c  p linkage for this port
;	a4    s hardware base
;
;	status returned zero
;---
qsd_init
	move.w	spd_port(a3),d0
	dc.w	qpc.sinit
	moveq	#0,d0
	rts

;+++
; SER port baud
;
;	d0  r	0 or err_ipar
;	d1 c  p baud rate
;	a3 c  p SER port linkage block
;	all other registers preserved
;
;---
qsd_baud
	move.w	spd_port(a3),d0
	dc.w	qpc.sbaud
	rts

;+++
; SER port open operation (enable rx)
;
;	d5 c  p SER operation mode
;	a3 c  p SER port linkage block
;	all other registers preserved
;	status returned according to D0
;---
qsd_open
	move.w	spd_port(a3),d0
	tst.b	d5
	bgt.s	qsd_oout
	ori.w	#$100,d0		 ; open output port
qsd_oout
	tst.b	d5
	blt.s	qsd_oin
	ori.w	#$200,d0		 ; open input port
qsd_oin
	move.b	spd_hand(a3),d1 	 ; handshake mode
	dc.w	qpc.sopen
	bne.s	qsd_oend
	bsr.s	qsd_rxen		 ; enable receive
qsd_oend
	rts

;+++
; SER port close operation (disable rx)
;
;	d0 c  p SER input buffer
;	a0 c  p SER port channel block
;	a3 c  p SER port linkage block
;	all other registers preserved
;	status returned according to D0
;---
qsd_close
	move.l	d0,-(sp)
	beq.s	qsd_cout		 ; any input-queue at all?
	sf	spd_iact(a3)		 ; input not active
	move.w	spd_port(a3),d0
	dc.w	qpc.srxdi
	move.w	spd_port(a3),d0
	ori.w	#$200,d0		 ; close input port
	dc.w	qpc.sclse
qsd_cout
	move.l	(sp)+,d0
	rts

;+++
; SER port receive input operation (re-enable rx if not enabled)
;
;	a3 c  p SER port linkage block
;	all other registers preserved
;	status returned according to D0
;---
qsd_iopr
	tst.b	spd_iact(a3)
	bne.s	qsd_ioprts

qsd_rxen
	movem.l d0/a2,-(sp)
	move.l	spd_ibuf(a3),d0 	 ; any buffer?
	ble.s	qsd_iopexit		 ; ... no

	move.l	d0,a2
	jsr	iob_room		 ; enough room
	cmp.l	#16,d0			 ; enough?
	ble.s	qsd_iopexit		 ; ... no

	st	spd_iact(a3)
	move.w	spd_port(a3),d0
	dc.w	qpc.srxen

qsd_iopexit
	movem.l (sp)+,d0/a2

qsd_ioprts
	tst.l	d0
	rts

;+++
; SER port transmit output operation
;
;	a3 c  p SER port linkage block
;	all other registers preserved
;	status returned according to D0
;---
qsd_oopr
	move.l	d0,-(a7)
	st	spd_oact(a3)		 ; set output to go
	move.w	spd_port(a3),d0
	dc.w	qpc.sesnd
	move.l	(a7)+,d0
	rts

;+++
; SER port check CD
;
;	a3 c  p SER port linkage block
;	all other registers preserved
;	status returned NZ for CD active
;---
qsd_cdchk
	move.l	d0,-(sp)
	move.w	spd_port(a3),d0
	dc.w	qpc.scdst
	movem.l (sp)+,d0		; don't touch status
	rts

	end
