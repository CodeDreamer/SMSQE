; QXL PAR device definition	V2.10	 1999	Tony Tebby

	section spp

	xdef	qxl_pardef

	xref	gu_achpp

	include 'dev8_keys_serparprt'
	include 'dev8_smsq_qxl_keys'
	include 'dev8_smsq_qxl_comm_keys'
	include 'dev8_mac_vecx'
	include 'dev8_mac_assert'

;+++
; PAR driver definition
;
;---
qxl_pardef
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
;	d7    s if d0 = 0 on entry, otherwise preserved
;	a4  r	hardware base
;
;	status returned as d0
;---
qpd_check
	tst.b	d0			 ; check highest?
	bne.s	qpdc_portn		 ; check port n

	move.l	qxl_message,a4
	move.b	qxl_ms_pcset+qxm_lpt(a4),d7 ; bits set
	bra.s	qpdc_eloop
qpdc_loop
	addq.w	#1,d0
	lsr.b	#1,d7			 ; another port?
qpdc_eloop
	bne.s	qpdc_loop
	rts

qpdc_portn
	move.l	d7,-(sp)		 ; save a reg
	moveq	#0,d7
	move.l	qxl_message,a4
	move.b	qxl_ms_pcset+qxm_lpt(a4),d7 ; bits set
	subq.l	#1,d0			 ; check bits 0-7
	btst	d0,d7
	beq.s	qpdc_noport
qpdc_exit
	move.l	(sp)+,d7
	addq.l	#1,d0
	rts

qpdc_noport
	moveq	#-1,d0
	bra.s	qpdc_exit

;+++
; PAR port linkage / hardware initialisation
;
;	a3 c  p linkage for this port
;	a4    s hardware base
;
;	status returned zero
;---
qpd_init
	move.l	#qxm.aldata,d0		; allocate output message buffer
	jsr	gu_achpp
	move.l	a0,spd_hbase(a3)
	move.w	#qxm.txdata<<8-1,d0	; set basic command and port
	add.w	spd_port(a3),d0
	move.w	d0,qxm_datams(a0)
	moveq	#0,d0
	rts

;+++
; PAR port open operation
;
;	d0  r	0
;	a3 c  p PAR port linkage block
;	all other registers preserved
;	status returned 0
;---
qpd_open
	moveq #0,d0
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
	st	spd_oact(a3)		 ; set output to go
	tst.l	d0
	rts

	end
