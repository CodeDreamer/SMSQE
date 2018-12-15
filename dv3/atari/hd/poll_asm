; DV3 Atari Hard Disk Polling Routine	     1993     Tony Tebby

	section dv3

	xdef	ahd_poll

	xref	hd_pflush

	include 'dev8_keys_sys'
	include 'dev8_keys_atari'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'
	include 'dev8_mac_assert'

;+++
; Hard Disk Polling Interrupt Routine - Atari ST
;
; This provides the basic timing services for the hard disk system.
;
; When there are no pending operations, the timer is used to unlock the door.
;
ahd_poll
	tst.b	sys_dmiu(a6)		 ; dma in use?
	bpl.s	hdp_count		 ; ... no
hdp_rts
	rts

hdp_count
	assert	hdl_acss+1,hdl_actm
	move.w	hdl_acss(a3),d0 	 ; action counter to be decremented?
	ble.s	hdp_rts 		 ; ... no
	tst.b	hdl_freq(a3)		 ; flush required?
	bne.s	hdp_actf
	assert	hdl.acts,1
	subq.b	#hdl.acts,d0
	move.b	d0,hdl_actm(a3) 	 ; timere
	rts
hdp_actf
	subq.b	#hdl.actf,d0		 ; ... yes, decrement a bit quicker
	bge.s	hdp_acts
	moveq	#0,d0			 ; no count now
hdp_acts
	move.b	d0,hdl_actm(a3) 	 ; decremented timer
	beq.s	hdp_flush
	rts

hdp_flush
	jsr	hd_pflush		 ; flush everything
	clr.b	hdl_freq(a3)		 ; flush not required now
	move.b	hdl_apnd(a3),hdl_actm(a3) ; new count
	rts

	end
