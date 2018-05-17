; Null Header

	section header

header_base
	dc.l	null_end-header_base	 ; length of header
	dc.l	0			 ; module length unknown
	dc.l	0			 ; loaded length
	dc.l	0			 ; checksum
	dc.l	0			 ; always select
	dc.b	0			 ; top level
	dc.b	0
	dc.w	null_name-*

null_name
	dc.w	12,'Null Module '
	dc.l	'    '
	dc.w	$200a
null_end
	end
