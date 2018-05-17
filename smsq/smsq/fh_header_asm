; SMSQ OS Header V2.02	   1994  Tony Tebby

	section base

	xref.l	smsq_vers
	xref	smsq_base
	xref	smsq_end

	include 'dev8_smsq_smsq_bl_keys'
	include 'dev8_keys_stella_bl'
	include 'dev8_mac_assert'

	section header

header_base
	dc.l	smsq_base-header_base	 ; length of header
	dc.l	0			 ; module length unknown
	dc.l	smsq_end-smsq_base	 ; loaded length
	dc.l	0			 ; checksum
	dc.l	smsq_sel-header_base	 ; select
	dc.b	0			 ; main level
	dc.b	0
	dc.w	smsq_name-*

smsq_name
	dc.w	13,'SMSQ Fast RAM '
	dc.l	smsq_vers
	dc.w	$200a

smsq_sel
	assert	sbl.os,$1f		 ; msb of facility is OS
	assert	sbl.fh,$1e		 ; nmsb is fast ram available
	tst.b	sbl_facility(a5)	 ; OS already loaded
	ble.s	ssel_noload		 ; ... yes, or no fast ram
	tas	sbl_facility(a5)	 ; ... load it
	moveq	#sbl.load,d0
	rts
ssel_noload
	moveq	#sbl.noload,d0		 ; ... yes, skip this one
	rts

	end
