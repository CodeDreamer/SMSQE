; base area SMSQ Q68 Keyboard Tables   W. Lenerz 2016

	section header

	xref	kbd_base
	xref	smsq_end

	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_ldm'

header_base
	dc.l	kbd_base-header_base	 ; length of header
	dc.l	0			 ; module length unknown
	dc.l	smsq_end-kbd_base	 ; loaded length
	dc.l	0			 ; checksum
	dc.l	0			 ; no select
	dc.b	1			 ; 1 level down
	dc.b	0
	dc.w	smsq_name-*

smsq_name
	dc.w	24,'SMSQ Q68 Keyboard Tables'
	dc.l	'    '
	dc.w	$200a

	end
