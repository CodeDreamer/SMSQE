; HOTKEY polling routine    V2.01    1988   Tony Tebby   QJUMP

	section hotkey

	xdef	hk_poll

	xref	hk_sstbf
	xref	hk_rjob
	xref	hk_fitmk

	include 'dev8_keys_qu'
	include 'dev8_keys_sys'
	include 'dev8_keys_qlv'
	include 'dev8_ee_hk_data'
	include 'dev8_mac_assert'
;+++
; Hotkey polling routine.
; (Transfers old stuffer buffer to new stuffer buffer)
; Invokes Hotkey program on recognised ALT keystroke
;---
hk_poll
	btst	#sys..shk,sys_klock(a6)  ; suppressed?
	bne.s	hkp_rts
	tst.l	hkd_jbid(a3)		 ; job id
	beq.s	hkp_rts 		 ; ... none

	move.w	hkd_sbfc(a3),d2 	 ; anything put into old HOT buffer
	beq.s	hkp_ckq 
	lea	hkd_sbfc(a3),a1 	 ; set pointer
	clr.w	(a1)+			 ; and clear old buffer
	jsr	hk_sstbf		 ; stuff new style

hkp_ckq
	move.l	sys_ckyq(a6),a2 	 ; set keyboard queue pointer
	move.l	a3,a4
	move.w	ioq.test,a3		 ; test the queue
	jsr	(a3)
	move.l	a4,a3
	bne.s	hkp_ckrq		 ; ... nothing there

	addq.b	#1,d1			 ; something, is it $FF
	bne.s	hkp_ckrq		 ; ... no, check request

; found ALT

	move.l	qu_nexto(a2),a1 	 ; now check the next character
	addq.l	#1,a1
	cmp.l	qu_endq(a2),a1		 ; off end?
	blt.s	hkp_tempty		 ; ... no, test empty
	lea	qu_strtq(a2),a1 	 ; ... yes, reset out pointer
hkp_tempty
	move.l	qu_nexti(a2),d2 	 ; keep nextin pointer
	cmp.l	d2,a1			 ; in and out the same?
	beq.s	hkp_ckrq		 ; ... yes, can't read next character
	moveq	#0,d1
	move.b	(a1),d1 		 ; get next character
	jsr	hk_fitmk		 ; and item
	tst.w	d2
	ble.s	hkp_rts 		 ; ... none, (or off)
	move.w	d1,hkd_key(a3)		 ; set (or overwrite) key action
	move.l	qu_nexto(a2),qu_nexti(a2); remove pending
	st	hkd_req(a3)		 ; request action

hkp_ckrq
	assert	hkd_act+1,hkd_req
	tst.w	hkd_act(a3)		 ; busy?
	bgt.l	hk_rjob 		 ; ... should be, release hotkey job
hkp_rts
	rts
	end
