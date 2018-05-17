; SMS2 serial driver tewst

	section base

	xref	iob_smsq		 ; SMSQ IOB bits

	xref	qxl_spp_init
	xref	qxl_spp_inits

	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_qlv'
	include 'dev8_smsq_smsq_base_keys'


	section init
	include 'dev8_smsq_qxl_keys'

init
  dc.l $4afbedeb
	jsr	qxl_spp_init

	moveq	#sms.xtop,d0
	trap	#do.sms2

	jmp	qxl_spp_inits

	end
