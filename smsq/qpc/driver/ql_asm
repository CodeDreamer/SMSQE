; Base area SMSQ QPC Drivers / QL display

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
	dc.w	22,'SMSQ QPC QL CON Driver'
	dc.l	'    '
	dc.w	$200a

	section base

	xref	pt_setup

qpc_con
	tst.b	sms.conf+sms_128k	 ; small machine?
	beq.s	qcn_normal

	moveq	#-1,d1			 ; QL screen size
	moveq	#ptd.ql,d2		 ; ql colour depth
	moveq	#ptm.ql4,d3		 ; ql mode 4(8) driver
	moveq	#ptd.ql,d4		 ; install this one
	bra.s	qcn_setup

qcn_normal
	move.l	sms.conf+sms_xres,d1	 ; config screen size
	moveq	#ptd.ql,d2		 ; ql colour depth
	moveq	#ptm.ql4,d3		 ; ql mode 4(8) driver
	move.b	sms.conf+sms_ismode,d4	 ; config screen colour depth
qcn_setup
	jmp	pt_setup

	end
