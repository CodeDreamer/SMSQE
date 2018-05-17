; base area SMSQ Q40 Drivers / 16 bit

	section header

	xref	smsq_end

	include 'dev8_keys_con'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_smsq_smsq_config_keys'


header_base
	dc.l	q40_con-header_base	 ; length of header
	dc.l	0			 ; module length unknown
	dc.l	smsq_end-q40_con	 ; loaded length
	dc.l	0			 ; checksum
	dc.l	0			 ; select
	dc.b	1			 ; 1 level down
	dc.b	0
	dc.w	smsq_name-*

smsq_name
	dc.w	30,'SMSQ Q40 16 bit Console Driver'
	dc.l	'    '
	dc.w	$200a

	section base

	xref	pt_setup

Q40_con
	move.b	sms.conf+sms_ismode,d1	 ; config screen size / mode
	moveq	#ptd.16,d2		 ; 16 bit colour depth
	moveq	#ptm.q40,d3		 ; Q40 16 bit driver
	move.b	d1,d4
	lsr.b	#1,d4			 ; mode 0 or 1
	mulu	#ptd.16,d4		 ; mode ql or 16 bit
	jmp	pt_setup

	end
