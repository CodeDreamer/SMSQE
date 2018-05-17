; QXL Special Display Routines

	section exten

	xdef	disp_update

	xref	ut_gtint

	include 'dev8_keys_err'
	include 'dev8_keys_qdos_sms'
	include 'dev8_smsq_qxl_keys'
	include 'dev8_mac_assert'

;+++
; DISP_UPDATE number to check, number to transfer
;---
disp_update
	jsr	ut_gtint		 ; two integers
	bne.s	qxb_rts
	cmp.w	#2,d3
	bne.s	qxb_ipar

	move.l	(a6,a1.l),d1
	cmp.w	#qxl.vcopy*2,d1 	 ; transfer max 2xdefault
	bls.s	disp_set
	move.w	#qxl.vcopy*2,d1
disp_set
	lsl.w	#8,d1
	lsr.l	#8,d1			 ; two bytes
	moveq	#sms.xtop,d0
	trap	#1
	move.l	qxl_scr_work,a1
	assert	qxl_vchek,qxl_vcopy-1
	move.w	d1,qxl_vchek(a1)	 ; check and transfer rate
	moveq	#0,d0
qxb_rts
	rts

qxb_ipar
	moveq	#err.ipar,d0
	rts
	end
