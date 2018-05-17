; IP base section   V1.00				 2004	Marcel Kilgus

	section base

	xref	ip_init

	section header

	xref	smsq_end

header_base
	dc.l	ip_initp-header_base	 ; length of header
	dc.l	0			 ; module length unknown
	dc.l	smsq_end-ip_initp	 ; loaded length
	dc.l	0			 ; checksum
	dc.l	0			 ; always select
	dc.b	1			 ; one level down
	dc.b	0
	dc.w	smsq_name-*

smsq_name
	dc.w	18,'IP devices driver '
	dc.l	'    '
	dc.w	$200a

	section init

;+++
; Initialise IP devices
;---
ip_initp
	jmp	ip_init

	end
