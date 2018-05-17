; SMSQ Q40 DV3 Main Routine

	section header

	xref	smsq_end
	xref.l	dv3_vers

header_base
	dc.l	dv3_base-header_base	 ; length of header
	dc.l	0			 ; module length unknown
	dc.l	smsq_end-dv3_base	 ; loaded length
	dc.l	0			 ; checksum
	dc.l	0			 ; always select
	dc.b	1			 ; 1 level down
	dc.b	0
	dc.w	smsq_name-*

smsq_name
	dc.w	12,'SMSQ Q40 DV3'
	dc.l	dv3_vers
	dc.w	$200a

	section base

	xref	dv3_init
	xref	fd_init
	xref	hd_init
	xref	dv3_addfd
	xref	qlf_table

;	 include 'dev8_keys_qdos_sms'
;	 include 'dev8_keys_sys'
;	 include 'dev8_smsq_smsq_config_keys'
;	 include 'dev8_smsq_smsq_base_keys'

dv3_base
	bra.l	d3r_init

	section init

d3r_init
	jsr	dv3_init	 ; initialise
	lea	qlf_table,a1
	jsr	dv3_addfd
	jsr	fd_init
	jmp	hd_init
	end
