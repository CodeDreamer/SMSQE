; SMSQ/E for QPC built in DV3 drivers

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
	dc.w	12,'SMSQ QPC DV3'
	dc.l	dv3_vers
	dc.w	$200a

	section base

	xref	dv3_init
	xref	fd_init
	xref	hd_init
	xref	dv3_addfd
;	 xref	 qlf_table
	xref	qw1_table

	xref	dev_init

dv3_base
	bra.l	init

	section init
init
	jsr	dv3_init	 ; initialise
	jsr	hd_init
	jsr	fd_init
;	 lea	 qlf_table,a1
	lea	qw1_table,a1	 ; includes QLW1 driver
	jsr	dv3_addfd

	jmp	dev_init

	end
