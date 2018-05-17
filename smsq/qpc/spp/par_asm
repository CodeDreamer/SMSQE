; QPC PAR device definition	V2.10	 1999	Tony Tebby
;					  2000	Marcel Kilgus
	section spp

	xdef	qpc_pardef

	xref	gu_achpp

	include 'dev8_keys_serparprt'
	include 'dev8_smsq_qpc_keys'
	include 'dev8_mac_vecx'
	include 'dev8_mac_assert'

;+++
; PAR driver definition
;
;---
qpc_pardef
	vec	qpd_check
	vec	qpd_vector
	vec	qpd_preset
	vec	qpd_init
	vec	qpd_istable

;+++
; Preset table defining hardware specific values
;---
qpd_preset
	assert	spd_pdir,spd_hand-1
	dc.w	spd_pdir,2,$0100      ; output only
	dc.w	0

;+++
; Vector table defining hardware specific IO routines
;---
qpd_vector
	dc.w	spd_open,qpd_open-*-2
	dc.w	spd_close,qpd_close-*-2
	dc.w	spd_oopr,qpd_oopr-*-2
	dc.w	0

;+++
; Interrupt server table
;---
qpd_istable
	dc.w	0		      ; no server

;+++
; Routine to check whether particular ports exist
;
;	d0 cr	port to check / 0 is it does not exist
;	d0 cr	0 / highest port number
;
;	status returned as d0
;---
qpd_check
	dc.w	qpc.pexst
	tst.l	d0
	rts

;+++
; PAR port linkage / hardware initialisation
;
;	a3 c  p linkage for this port
;	a4    s hardware base
;
;	status returned zero
;---
qpd_init
	move.w	spd_port(a3),d0
	dc.w	qpc.pinit
	moveq	#0,d0
	rts

;+++
; PAR port open operation
;
;	a3 c  p PAR port linkage block
;	all other registers preserved
;	status returned as D0
;---
qpd_open
	move.w	spd_port(a3),d0
	dc.w	qpc.popen
	rts

;+++
; PAR port close operation
;
;	a3 c  p PAR port linkage block
;	all other registers preserved
;	status returned according to D0
;---
qpd_close
	tst.l	d0
	rts

;+++
; PAR port transmit output operation
;
;	a3 c  p PAR port linkage block
;	all other registers preserved
;	status returned according to D0
;---
qpd_oopr
	movem.l d0,-(a7)
	st	spd_oact(a3)		 ; set output to go
	move.w	spd_port(a3),d0
	dc.w	qpc.pesnd
	move.l	(a7)+,d0
	rts

	end
