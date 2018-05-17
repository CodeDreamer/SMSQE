; base area SMSQmulator / 8bit (Aurora) display  V1.00 (c) W. Lenerz 2013


; based on : base area SMSQ Drivers / QL display

	section header

	xref	smsq_end

	include 'dev8_keys_con'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_smsq_smsq_config_keys'
	include 'dev8_mac_assert'

header_base
	dc.l	gl_con-header_base	 ; length of header
	dc.l	0			 ; module length unknown
	dc.l	smsq_end-gl_con 	 ; loaded length
	dc.l	0			 ; checksum
	dc.l	0			 ; select
	dc.b	1			 ; 1 level down
	dc.b	0
	dc.w	smsq_name-*

smsq_name
	dc.w	26,'SMSQ JAVA 8 bit CON Driver'
	dc.l	'    '
	dc.w	$200a

	section base

	xref	pt_setup

gl_con
	move.b	sms.conf+sms_issize,d1	 ; config size
	moveq	#ptd.08,d2		 ; 8 bit colour depth
	moveq	#ptm.aur8,d3		 ; aurora 8 bit driver
	move.b	sms.conf+sms_ismode,d4	 ; mode ql or 8 bit
	jmp	pt_setup

	end
