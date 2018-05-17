; HOME thing initialization for SMSQ/E	 V1.02	(c) 2005 W. Lenerz

; 2005-10-23	1.00		initial version
; 2005-11-01	1.01		intermediary version
; 2005-11-01	1.02		made into module header


	section init

	xref	home_thing
	xref	gu_thjmp		; set up thing
	xref	gu_achpp		; get mem
	xref.l	hom_vers		; my version
	xref	hom_procs		; basic procedures
	xref	h_fr_mem
	xref	smsq_end

	include dev8_keys_thg
	include dev8_keys_iod
	include dev8_keys_err
	include dev8_keys_k
	include dev8_keys_qdos_sms
	include dev8_smsq_home_data
	include dev8_mac_thg
	include dev8_mac_assert
	include dev8_keys_chp
	include 'dev8_keys_qdos_ioa'
	include 'dev8_keys_sys'
	include 'dev8_keys_qlv'

	section header

header_base
	dc.l	home_init-header_base	 ; length of header
	dc.l	0			 ; module length unknown
	dc.l	smsq_end-home_init	 ; loaded length
	dc.l	0			 ; checksum
	dc.l	0			 ; always select
	dc.b	0			 ; main level
	dc.b	0
	dc.w	smsq_name-*
smsq_name
	dc.w	16,'SMSQE HOME Thing'
	dc.l	hom_vers
	dc.w	$200a
	ds.w	0

	section base


;---------------------------------------------------------------------
;
; Initialise the thing itself, (it uses the standard USE, FREE etc calls)
; This is the code called when LRESPR'd
;
;---------------------------------------------------------------------

home_init

	lea	hom_procs,a1		; set basic
	move.w	sb.inipr,a2
	jsr    (a2)			; link them in


	lea	hom_tnam,a0
	moveq	#sms.zthg,d0
	jsr	gu_thjmp		; zap previous thing of same name

	move.l	#home_end,d0		; allocate linkage
	jsr	gu_achpp		; get mem

	move.l	a0,a1			; keep, will be ptr to thing

	lea	home_thing,a0		; our Thing
	move.l	a0,th_thing(a1) 	; ... set pointer to it
	lea	th_verid(a1),a0
	move.l	#hom_vers,(a0)+ 	; and version
	lea	hom_tnam,a3		; thing name
	move.w	(a3)+,(a0)+		; copy
	move.l	(a3)+,(a0)+
	lea	hmt_free(a1),a0 	; free routine pointer (NOT for this thing)
	lea	h_fr_mem,a3
	move.l	a3,(a0)+		; set free routine
	moveq	#sms.lthg,d0
	jmp	gu_thjmp		; link in thing now

hom_tnam dc.b	0,4,'HOME',$a

	end
