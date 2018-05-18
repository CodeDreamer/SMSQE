; base area SMSQ Q68 Drivers / 16 bit

	section header

	xref	smsq_end

	include 'dev8_keys_con'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_smsq_smsq_config_keys'


header_base
	dc.l	q68_con-header_base	 ; length of header
	dc.l	0			 ; module length unknown
	dc.l	smsq_end-q68_con	 ; loaded length
	dc.l	0			 ; checksum
	dc.l	0			 ; select
	dc.b	1			 ; 1 level down
	dc.b	0
	dc.w	smsq_name-*

smsq_name
	dc.w	30,'SMSQ Q68 16 bit Console Driver'
	dc.l	'    '
	dc.w	$200a

	section base

	xref	pt_setup

q68_con clr.l	d0
	move.b	sms.conf+sms_ismode,d1	 ; config screen size / mode
	moveq	#ptd.16,d2		 ; 16 bit colour depth
	moveq	#ptm.q40,d3		 ; Q68/Q40 16 bit driver
	move.l	d2,d4			 ; presume you want to install this
	btst	#1,d1			 ; is it a 16 bit mode?
	beq.s	no_inst
	jmp	pt_setup
no_inst moveq	#-1,d4
	jmp	pt_setup



	end
