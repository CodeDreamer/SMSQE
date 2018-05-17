; DOS base section   V1.00     1997  Marcel Kilgus

	section base

	xref	dos_init

	section header

	xref	smsq_end

header_base
	dc.l	dos_initp-header_base	 ; length of header
	dc.l	0			 ; module length unknown
	dc.l	smsq_end-dos_initp	 ; loaded length
	dc.l	0			 ; checksum
	dc.l	0			 ; always select
	dc.b	1			 ; one level down
	dc.b	0
	dc.w	smsq_name-*

smsq_name
	dc.w	18,'DOS device driver '
	dc.l	'    '
	dc.w	$200a

	section init

	include 'dev8_keys_qlv'
	include 'dev8_keys_err'
	include 'dev8_mac_config'
	include 'dev8_mac_basic'
	include 'dev8_mac_proc'

;+++
; Initialise DOS device
;---
dos_initp
	jmp	dos_init

	end
