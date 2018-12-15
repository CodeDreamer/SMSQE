; DV3 QXL Floppy Polling Routine        1993	  Tony Tebby

	section dv3

	xdef	fd_poll

	xref	fd_pflush

	include 'dev8_dv3_keys'
	include 'dev8_dv3_fd_keys'
	include 'dev8_keys_sys'
	include 'dev8_smsq_qxl_keys'
	include 'dev8_mac_assert'

;+++
; Floppy Polling Interrupt Routine - QXL
;
fd_poll
	tst.b	sys_stiu(a6)		 ; sector transfer buffer in use?
	bmi.s	fdp_rts 		 ; ... yes

	tst.b	fdl_actm(a3)		 ; any action required
	ble.s	fdp_rts

	tst.b	fdl_freq(a3)		 ; flush required?
	beq.s	fdp_stop		 ; ... no

	subq.b	#fdl.actf,fdl_actm(a3)	 ; countdown to flush
	bgt.s	fdp_rtna		 ; keep it running

	clr.b	fdl_actm(a3)		 ; no count now

	jsr	fd_pflush		 ; flush everything
	sf	fdl_freq(a3)		 ; no flush required
	move.b	fdl_apnd(a3),fdl_actm(a3)
fdp_rtna
fdp_rts
	rts

fdp_stop
	subq.b	#fdl.acts,fdl_actm(a3)	 ; countdown to stop
	bgt.s	fdp_rts
	clr.b	fdl_actm(a3)		 ; stopped
	clr.b	fdl_stpb(a3)
	rts


	end
