; Q68 SER DEVICE DEFINITION & physical routines  V2.10	 1999	Tony Tebby
;						 V2.11	 2017	W. Lenert
	section spp

	xdef	q68_pardef

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
q68_pardef
	vec	q68_check
	vec	q68_vector
	vec	q68_preset
	vec	q68_init
	vec	q68_istable


;+++
; Vector table defining hardware specific IO routines
; They all ust return err.nimp
;---
q68_vector
	dc.w	spd_open,q68_open-*-2
	dc.w	spd_close,q68_close-*-2
	dc.w	spd_oopr,q68_oopr-*-2
	dc.w	0
	 

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

;+++
; PAR port close. If there is no input buffer, it is a nop.
;
;	d0 c  p pointer to input buffer
;	a3 c  p COM port linkage block
;	all other registers preserved
;	status returned according to D0
;---
q68_close

;+++
;  Port linkage / hardware initialisation
;
;	a3 c  p linkage for this port
;	a4    s hardware base
;
;	status returned zero
;---
q68_init
	clr.l	d0
	rts
 
;+++
; PAR port transmit output operation
;
;	a3 c  p SER port linkage block
;	all other registers preserved
;	status returned according to D0
;---
q68_oopr
;+++
; PAR port open operation (enable rx)
;
;	d0  r
;	a3 c  p SER port linkage block
;	all other registers preserved
;	status returned 0
;---
q68_open
	moveq	#err.nimp,d0
	rts

	end
