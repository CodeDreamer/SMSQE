; SMS2 QXL card ND drivers

	section header

	xref	smsq_end

header_base
	dc.l	nd_base-header_base	 ; length of header
	dc.l	0			 ; module length unknown
	dc.l	smsq_end-nd_base	 ; loaded length
	dc.l	0			 ; checksum
	dc.l	0			 ; always select
	dc.b	1			 ; 1 level down
	dc.b	0
	dc.w	smsq_name-*

smsq_name
	dc.w	20,'SMSQ QXL Net Driver '
	dc.l	'    '
	dc.w	$200a

	section base

	xref	nd_qxl
nd_base
	jmp	nd_qxl

	end
