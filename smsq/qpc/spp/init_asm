; QPC SER PAR PRT device initialisation  V2.10	  1999  Tony Tebby

	section spp

	xdef	qpc_spp_init
	xdef	qpc_spp_inits

	xref	spp_init
	xref	qpc_pardef
	xref	qpc_serdef

	include 'dev8_smsq_qpc_keys'
	include 'dev8_mac_vecx'

;+++
; SPP driver initialisation.
;
;	a1-a3  scratch
;	status return standard
;---
qpc_spp_init
	moveq	#0,d0
	dc.w	qpc.sexst		 ; get number of ser ports
qpc_ser_init
	dc.w	qpc.sinit
	subq.b	#1,d0
	bne.s	qpc_ser_init

	moveq	#0,d0
	dc.w	qpc.pexst		 ; get number of par ports
qpc_par_init
	dc.w	qpc.pinit
	subq.b	#1,d0
	bne.s	qpc_par_init

	lea	qpc_par,a1
	lea	qpc_ser,a2
	jmp	spp_init		 ; set up serial and parallel ports

qpc_par
	vec	qpc_pardef		 ; PAR driver
	novec
qpc_ser
	vec	qpc_serdef		 ; SER driver
	novec

;+++
; SPP driver initialisation in supervisor mode
;
;	a1-a3  scratch
;	status return standard
;---
qpc_spp_inits
	move.l	a3,qpc_spp_link
	rts

	end
