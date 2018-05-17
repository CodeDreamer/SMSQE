; Pointer do button 3 press   V2.10     1999  Tony Tebby

	section ptr

	xdef	pt_button3

	xref	ioq_pbyt

	include 'dev8_keys_con'
	include 'dev8_keys_sys'
	include 'dev8_smsq_smsq_base_keys'
;+++
; This should be called from any mouse interrup or polling routine to
; handle mouse button 3 presses
;
;	status return 0
;---
pt_button3
pb3.reg reg	d1/a1/a2/a3
	movem.l pb3.reg,-(sp)
	move.l	sms.sysb,a3
	move.l	sys_clnk(a3),d0
	beq.s	pb3_exit
	move.l	sys_ckyq(a3),a2 	 ; stuff in here
	move.l	d0,a3			 ; console linkage

	tas	pt_lstuf(a3)		 ; was last press a stuff? (this one is)
	blt.s	pb3_exit		 ; yes, don't do another

	move.b	pt_stuf1(a3),d1 	 ; get first character
	beq.s	pb3_exit		 ; isn't one
	jsr	ioq_pbyt		 ; there is one, stuff it
	move.b	pt_stuf2(a3),d1 	 ; is there another?
	beq.s	pb3_exit
	jsr	ioq_pbyt		 ; put next byte in queue

pb3_exit
	moveq	#0,d0
	movem.l (sp)+,pb3.reg
	rts

	end
