; SMSQ QPC Host Module
;
	include 'dev8_keys_sys'
	include 'dev8_smsq_smsq_ini_keys'
	include 'dev8_keys_stella_bl'

	section host

host
	bra.l	jump	     ; $00
qpc_base dc.b	$2b	     ; $04
	 dc.b	0,0,0
	 dc.l	0,0	     ; $08
	 dc.l	0,0,0,0      ; $10
smsq_len dc.l	0	     ; $20

	xref.l	smsq_vers

jump
	sf	ini_ouch
	lea	$28000,sp		 ; dummy stack
	lea	host_end,a0
	add.l	sbl_mbase(a0),a0
	jmp	(a0)

	section trailer
fixup
	dc.l	smsq_len-*		 ; set length
	dc.l	0			 ; no offset
	dc.l	0			 ; done

	dc.l	' END'
host_end
trailer
	dc.l	0			 ; no header
	dc.l	trailer-host		 ; length of module
	dc.l	0			 ;
	dc.l	0			 ; checksum
	dc.l	fixup-host		 ; fixup
	dc.l	*+4-host

	end
