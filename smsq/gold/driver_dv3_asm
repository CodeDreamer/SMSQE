; base area SMSQ GOLD DV3 Drivers

	section header

	xref	smsq_end
	xref	dv3_vers

header_base
	dc.l	gl_dv3-header_base	 ; length of header
	dc.l	0			 ; module length unknown
	dc.l	smsq_end-gl_dv3 	 ; loaded length
	dc.l	0			 ; checksum
	dc.l	0			 ; always select
	dc.b	1			 ; 1 level down
	dc.b	0
	dc.w	smsq_name-*

smsq_name
	dc.w	21,'SMSQ GOLD DV3 Drivers '
	dc.l	dv3_vers
	dc.w	$200a


	section base

	xref	dv3_init
	xref	fd_init
	xref	dv3_addfd
	xref	qlf_table

	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_sys'
	include 'dev8_smsq_smsq_config_keys'
	include 'dev8_smsq_smsq_base_keys'

gl_dv3
	bra.l	d3r_init
 dc.w 0 ; pad for ACSI alignment

	section const
	section language
	section init

d3r_init
; move.l #$0000aaaa,$20700
	jsr	dv3_init	 ; initialise
	lea	qlf_table,a1
	jsr	dv3_addfd
	jsr	fd_init
; move.l #$0000aaaa,$20704
	rts

	end
