; QXL SER device definition	V2.10	 1999	Tony Tebby

	section spp

	xdef	qxl_serdef

	xref	iob_room
	xref	gu_achpp
	xref	spp_smess

	include 'dev8_keys_serparprt'
	include 'dev8_smsq_qxl_keys'
	include 'dev8_smsq_qxl_comm_keys'
	include 'dev8_mac_vecx'
	include 'dev8_mac_assert'

;+++
; SER driver definition
;
;---
qxl_serdef
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
	dc.w	spd_ilen+2,2,qxm.bfdata
	dc.w	spd_minbf,2,qxm.bfdata
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
	tst.b	d0			 ; check highest?
	bne.s	qsdc_portn		 ; check port n

	move.l	qxl_message,a4
	move.w	qxl_ms_pcset+qxm_com(a4),d7 ; bits set
	bra.s	qsdc_eloop
qsdc_loop
	addq.w	#1,d0
	lsr.w	#1,d7			 ; another port?
qsdc_eloop
	bne.s	qsdc_loop
	rts

qsdc_portn
	move.l	d7,-(sp)		 ; save a reg
	moveq	#0,d7
	move.l	qxl_message,a4
	move.w	qxl_ms_pcset+qxm_com(a4),d7 ; bits set
	subq.l	#1,d0			 ; check bits 0-7
	btst	d0,d7
	beq.s	qsdc_noport
qsdc_exit
	move.l	(sp)+,d7
	addq.l	#1,d0
	rts

qsdc_noport
	moveq	#-1,d0
	bra.s	qsdc_exit

;+++
; SER port linkage / hardware initialisation
;
;	a3 c  p linkage for this port
;	a4    s hardware base
;
;	status returned zero
;---
qsd_init
	move.l	#2*qxm.aldata,d0  ; allocate output/input message buffers
	jsr	gu_achpp
	move.l	a0,spd_hbase(a3)
	move.w	#qxm.txdata<<8+$7f,d0 ; set basic command and port
	add.w	spd_port(a3),d0
	move.w	d0,qxm_datams(a0)
	moveq	#0,d0
	rts

;+++
; SER port baud
;
;	d2  r  0
;	d2 c  s baud rate / 300
;	a3 c  p SER port linkage block
;	all other registers preserved
;
;---
qsd_baud
	move.l	#1536,d0		 ; 4*clock / 300
	divu	d2,d0			 ; 4*rate divider
	cmp.w	#2,d0			 ; high rate?
	bls.s	qbd_high
	lsr.w	#2,d0			 ; low rate
	bra.s	qbd_set
qbd_high
	bset	#15,d0			 ; high rate
qbd_set
	clr.l	-(sp)
	swap	d0
	move.w	#qxm.sport<<8+$7f,d0	 ; set baud message
	bra.s	qsd_smess

;+++
; SER port open operation (enable rx)
;
;	d0  r
;	a3 c  p SER port linkage block
;	all other registers preserved
;	status returned 0
;---
qsd_open
	bsr.s	qsd_rxen		 ; enable receive
	moveq	#0,d0
	move.l	d0,-(sp)
	move.w	#qxm.oport<<8+$7f,d0	 ; open port message
	bra.s	qsd_smess

;+++
; SER port close. If there is no input buffer, it is a nop.
;
;	d0 c  p pointer to input buffer
;	a3 c  p COM port linkage block
;	all other registers preserved
;	status returned according to D0
;---
qsd_close
       tst.l	d0			; input queue?
       beq.s	qsd_rts 		; ... no

;+++
; SER port disable rx
;
;	a3 c  p SER port linkage block
;	all other registers preserved
;	status returned according to D0
;---
qsd_rxdi
	move.l	d0,-(sp)
	sf	spd_iact(a3)		 ; input not active
	moveq	#0,d0
	move.w	#qxm.cport<<8+$7f,d0	 ; close port message
qsd_smess
	add.w	spd_port(a3),d0
	swap	d0
	jsr	spp_smess		 ; send port control message
	move.l	(sp)+,d0
qsd_rts
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
	cmp.l	#qxm.rmdata,d0		 ; enough?
	ble.s	qsd_iopexit		 ; ... no

	st	spd_iact(a3)
	move.w	spd_port(a3),d0
	subq.w	#1,d0			 ; port number
	move.l	qxl_message,a2
	bset	d0,qxl_ms_flow+qxm_com+1(a2) ; dragon 8 ports only
	bne.s	qsd_iopexit		 ; already ok
	move.l	#qxm.flowlen<<16+qxm.flowqx<<8,qxl_ms_flow+qxl_ms_len(a2) ; send message next time

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
	st	spd_oact(a3)		 ; set output to go
	tst.l	d0
	rts

;+++
; SER port check CD
;
;	a3 c  p SER port linkage block
;	all other registers preserved
;	status returned NZ for CD active
;---
qsd_cdchk
	tst.l	(sp)
	rts

	end
