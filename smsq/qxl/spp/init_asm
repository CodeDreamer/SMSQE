; QXL SER PAR PRT device initialisation  V2.10	  1999  Tony Tebby

	section spp

	xdef	qxl_spp_init
	xdef	qxl_spp_inits

	xref	spp_init
	xref	qxl_pardef
	xref	qxl_serdef

	include 'dev8_smsq_qxl_keys'
	include 'dev8_mac_vecx'

;+++
; SPP driver initialisation.
;
;	a1-a3  scratch
;	status return standard
;---
qxl_spp_init
	lea	qxl_par,a1
	lea	qxl_ser,a2
	jmp	spp_init		 ; set up serial and parallel ports

qxl_par
	vec	qxl_pardef		 ; PAR driver
	novec
qxl_ser
	vec	qxl_serdef		 ; SER driver
	novec

;+++
; SPP driver initialisation in supervisor mode
;
;	a1-a3  scratch
;	status return standard
;---
qxl_spp_inits
	move.l	a3,qxl_spp_link
	move.l	qxl_message,a5
	move.l	qxl_ms_pcset(a5),spd_qxlflow(a3) ; set startup flow

	rts

	end
