; Standard Pointer Setup   V2.01    2000 Tony Tebby

	section driver

	xdef	pt_setup

	xref	pt_init
	xref	pt_install
	xref	cn_procv

	include 'dev8_keys_qdos_sms'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_smsq_smsq_config_keys'


	xref.s	pt.cdepth

pt_setup
	moveq	#pt.cdepth,d2
	jsr	pt_init 		 ; initialise the pointer interface
	blt.s	pts_rts
	bsr.s	pts_install		 ; install driver with config size
	jmp	cn_procv

pts_install
	moveq	#sms.xtop,d0
	trap	#do.sms2
	lea	sms.conf+sms_idisp+12,a0
	moveq	#5,d0
pts_sdisp
	move.w	-(a0),-(sp)
	clr.w	-(sp)
	dbra	d0,pts_sdisp

	move.l	a7,d1			 ; size in d1
	jsr	pt_install
	add.w	#24,sp
pts_rts
	rts
	end
