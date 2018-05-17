; HOTKEY Extensions  V2.04	 1988	Tony Tebby   QJUMP

	section header

	xref	smsq_end
	xref.l	hk_vers

header_base
	dc.l	hot_base-header_base	 ; length of header
	dc.l	0			 ; module length unknown
	dc.l	smsq_end-hot_base	 ; loaded length
	dc.l	0			 ; checksum
	dc.l	0			 ; always select
	dc.b	1			 ; 1 level down
	dc.b	0
	dc.w	smsq_name-*

smsq_name
	dc.w	16,'HOTKEY System 2 '
	dc.l	hk_vers
	dc.w	$200a


	section base


	xref	hk_smsq		 ; load this

	xref	hot_init
	xref	hk_init
	xref	hk_sprc

	include 'dev8_ee_hk_data'
	include 'dev8_mac_text'

hot_base
	jsr	hk_init		 ; initialise thing
	jmp	hot_init		 ; initialise HOTKEY system
	end
