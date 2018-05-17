; Base area SMSQ QPC Drivers / 8 bit display

	section header

	xref	smsq_end

	include 'dev8_keys_con'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_smsq_smsq_config_keys'

header_base
	dc.l	qpc_con-header_base	 ; length of header
	dc.l	0			 ; module length unknown
	dc.l	smsq_end-qpc_con	 ; loaded length
	dc.l	0			 ; checksum
	dc.l	0			 ; select
	dc.b	1			 ; 1 level down
	dc.b	0
	dc.w	smsq_name-*

smsq_name
	dc.w	25,'SMSQ QPC 8 bit CON Driver'
	dc.l	'    '
	dc.w	$200a

	section base

	xref	pt_setup

qpc_con
	tst.b	sms.conf+sms_128k	 ; small machine?
	bne.s	qcn_ok			 ; ... yes

	move.l	sms.conf+sms_xres,d1	 ; config screen size
	moveq	#ptd.08,d2		 ; 8 bit colour depth
	moveq	#ptm.aur8,d3		 ; 8 bit Aurora driver
	move.b	sms.conf+sms_ismode,d4	 ; config screen colour depth
	jmp	pt_setup

qcn_ok
	moveq	#0,d0
	rts

	end
