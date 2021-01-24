; SMS (QPC) Interrupt Server				   2006 Marcel Kilgus
;
; 2006-04-01  1.01  Enabled numlock (MK)
; 2018-01-03  1.02  Beep-end code now on PC side to support shorter beeps (MK)

	section isrv

	xdef	qpc_isrv
	xdef	qpc_event_list

	xref	ioq_pbyt
	xref	kbd_pc84x
	xref	kbd_setnumlock
	xref	spp_sendpar
	xref	spp_sendser
	xref	spp_rxser

	include 'dev8_smsq_qpc_keys'
	include 'dev8_keys_psf'
	include 'dev8_keys_sys'
	include 'dev8_keys_serparprt'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_mac_assert'
	include 'dev8_smsq_kbd_keys'
	include 'dev8_smsq_smsq_config_keys'

qpc_event_list
	dc.w	qpc_event_poll-*
	dc.w	qpc_event_end-*
	dc.w	qpc_event_kbd-*
	dc.w	qpc_event_mouse-*
	dc.w	qpc_event_par-*
	dc.w	qpc_event_stx-*
	dc.w	qpc_event_srx-*

qpc_isrv
	dc.w	qpc.savr		 ; save all registers
qpc_handle_event
	dc.w	qpc.cale		 ; process event

; -----
qpc_event_kbd
	move.l	qpc_kbd_link,d0
	beq	qpc_handle_event
	move.l	d0,a3
	move.l	d1,d2			 ; event data is address of kbd queue
	move.b	qpc_numl,d0
	jsr	kbd_setnumlock

qpc_kbd_loop
	dc.w	qpc.getq		 ; get byte out of keyboard queue
	beq.s	qpc_handle_event

	lea	qpc_sconf,a2
	tst.b	sms_winkbd(a2)
	beq.s	qpc_kbd_smsqe		 ; Use SMSQ/E keyboard driver
	move.l	kb_queue(a3),a2
	jsr	ioq_pbyt		 ; QPC handles character translation
	bra.s	qpc_kbd_loop
qpc_kbd_smsqe
	move.b	d1,d0
	jsr	kbd_pc84x		 ; convert AT keyboard byte
	bra.s	qpc_kbd_loop

qpc_event_par
	jsr	spp_sendpar
	dc.w	qpc.cale

qpc_event_stx
	jsr	spp_sendser
	dc.w	qpc.cale

qpc_event_srx
	jsr	spp_rxser
	dc.w	qpc.cale

qpc_event_mouse
	dc.w	qpc.cale		 ; ********

qpc_event_end
	dc.w	qpc.resr		 ; restore all registers
	rte

; -----
qpc_event_poll
	dc.w	qpc.resr		 ; restore all registers
	movem.l psf.reg,-(sp)
	move.l	sms.sysb,a6		 ; system variable base
	move.b	sys_caps(a6),d7 	 ; QPC1 doesn't set d7
	dc.w	qpc.skled		 ; set keyboard led status
	move.b	d7,sys_caps(a6) 	 ; QPC2 can't set Win's capslock state,
					 ; so write back the real state
	dc.w	qpc.beepn		 ; beeping?
	move.b	d7,sys_qlbp(a6) 	 ; copy status to system variable

	tas	sys_plrq(a6)
	bne.s	isrv_rrte
	addq.w	#1,sys_pict(a6) 	 ; one more poll

	and.w	#$f8ff,sr		 ; re-enable interrupts
	move.l	sms.spoll,a5
	jmp	(a5)

isrv_rrte
	movem.l (sp)+,psf.reg
	rte

	end
