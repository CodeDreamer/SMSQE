; Q68 SER PAR PRT device initialisation  V2.10	  1999  Tony Tebby
;					 V2.11	  2017  W. Lenerz
	section spp

	xdef	q68_spp_init
;	 xdef	 do_ser

	xref	spp_init
	xref	q68_pardef
	xref	q68_serdef
	xref	cpy_mmod

	xref	spp_rxser
	xref	spp_sendser

	include 'dev8_mac_vecx'
	include 'dev8_keys_q68'
	include 'dev8_keys_qdos_sms'

;+++
; SPP driver initialisation.
;
;	a1-a3  scratch
;	status return standard
;---
q68_spp_init
	lea	q68_par,a1
	lea	q68_ser,a2
	jsr	spp_init
	move.l	a3,q68_ser_link

; link in the interrupt routines
spp_link
;	 lea	 spp_sndh,a0		; do NOT make these into minimodules
;	 jsr	 cpy_mmod		; PC relative code
	lea	rx_xlnk,a0		; linkage area
	lea	spp_rxser,a1		; external interrupt address
	move.l	a1,4(a0)		; set it in linkage area
	moveq	#sms.lexi,d0		; link in interrupt routine
	trap	#1

lnk_rx
;	 lea	 spp_rxserh,a0
;	 jsr	 cpy_mmod
	lea	tx_xlnk,a0		; linkage area
	lea	spp_sendser,a1		; external interrupt address
	move.l	a1,4(a0)		; set it in linkage area
	moveq	#sms.lexi,d0		; link in interrupt routine
	trap	#1
	rts

; the definitions
		    
q68_par
	vec	q68_pardef		 ; PAR driver
	novec
q68_ser
	vec	q68_serdef		 ; SER driver
	novec

; ser interrupt routine linkage area

rx_xlnk dc.l   0		       ; external interrupt linkag
rx_xiad dc.l   0		       ; external interrupt service routine address

tx_xlnk dc.l   0		       ; external interrupt linkag
tx_xiad dc.l   0		       ; external interrupt service routine address

	end
