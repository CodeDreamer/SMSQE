; SMS (QXL) Message Processing		  1998 Tony Tebby
; 2005.01.10	1.01	added QXL restart (BC)
; 2006.10.01	1.02	added led updates, creates qmp_kbd_llck & uses it (BC)
; 2006.10.20	1.03	BLAT macro definitions commented out - macro wasn't used (wl)

	section comm

	xdef	qxl_mess_pr
	xdef	qxl_mess_prnext

	xdef	qmp_updt_led

	xref	spp_rxser
	xref	cv_dtmrt
	xref	ioq_pbyt
	xref	kbd_pc84x
	xref	qxl_mess_add
	xref	pt_xmode
	xref	cn_copy_clear

	include 'dev8_smsq_qxl_keys'
	include 'dev8_smsq_qxl_comm_keys'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_sys'
	include 'dev8_keys_con'
	include 'dev8_mac_assert'

qmp.reg reg d0/d1/a1-a3

* xref	 blatt
*blat macro blv
* move.b [blv],-(sp)
* jsr	 blatt
* add.w  #2,sp
* endm

* xref	 blattl
*blatl macro blv
* move.l [blv],-(sp)
* jsr	 blattl
* addq.l #4,sp
* endm

;+++
; This routine processes the messages received
;
;	a6 c  p system variable base
;
;---
qxl_mess_pr
	move.l	qxl_pcqx_mess,a4	 ; messages received

qxl_mess_prnext
qmp_loop
	moveq	#0,d0
	move.b	(a4),d0
	beq.s	qmp_exit
	move.w	qmp_table-1(pc,d0.w),d0
	jmp	qmp_table(pc,d0.w)

qmp_table
	dc.w	qmp_setup-qmp_table	 ; PC setup	 $01
	dc.w	qmp_flow-qmp_table	 ; Flow control
	dc.w	qmp_rtcu-qmp_table	 ; RTC
	dc.w	qmp_kbdd-qmp_table	 ; Keyboard data

	dc.w	qmp_vmack-qmp_table	 ; Video mode set acknowledge
	dc.w	qmp_rstrt-qmp_table	 ; QXL restart
	dc.w	qmp_exit-qmp_table	 ; nop
	dc.w	qmp_exit-qmp_table	 ; nop

	dc.w	qmp_nop4-qmp_table	 ; port opened	 $11
	dc.w	qmp_nop4-qmp_table	 ; port closed!
	dc.w	qmp_nop4-qmp_table	 ; port status
	dc.w	spp_rxser-qmp_table	 ; rx data

	dc.w	qmp_physr-qmp_table	 ; physical sector read
	dc.w	qmp_rpfail-qmp_table	 ; physical sector read failed
	dc.w	qmp_physw-qmp_table	 ; physical sector written
	dc.w	qmp_wpfail-qmp_table	 ; physical sector write failed

	dc.w	qmp_exit-qmp_table	 ; report plug-in  $21
	dc.w	qmp_exit-qmp_table	 ; message from plug-in
	dc.w	qmp_exit-qmp_table	 ; channel opened
	dc.w	qmp_exit-qmp_table	 ; nop

	dc.w	qmp_exit-qmp_table	 ; data read
	dc.w	qmp_exit-qmp_table	 ; data read failed
	dc.w	qmp_exit-qmp_table	 ; data written
	dc.w	qmp_exit-qmp_table	 ; data write failed

	dc.w	qmp_drivf-qmp_table	 ; drive formatted  $31
	dc.w	qmp_mouse-qmp_table	 ; mouse update

qmp_exit
	move.l	qxl_pcqx_mess,a4
	clr.w	(a4)			 ; unnecessary, but shows that we have done
	rts

qmp_nop4
	addq.l	#4,a4
	bra	qmp_loop

qmp_setup  ; *** do not junk setup messages
	move.l	qxl_message,a5		 ; copy message to save area
; tst.b $2817f
; beq.s xx
; blat (a4)
; blatl a5
;xx
	move.l	(a4)+,qxl_ms_pcset(a5)
	move.l	(a4)+,qxl_ms_pcset+4(a5)
	bra	qmp_loop

qmp_flow
	move.l	(a4)+,d1
	addq.l	#4,a4
	move.l	qxl_spp_link,d0 	 ; put ser/par flow control in spp link
	beq	qmp_loop
	move.l	d0,a3
	move.l	d1,spd_qxlflow(a3)
	bra	qmp_loop

qmp_rtcu
	lea	(a4),a1 		 ; date
	add.w	#1980-qxm.rtcu<<8,(a4)	 ; adjust year
	jsr	cv_dtmrt		 ; set date
	move.l	d1,sys_rtc(a6)
	addq.l	#8,a4
	bra	qmp_loop

qmp_kbdd
	lea	(a4),a0
	move.w	(a0)+,d2
	moveq	#0,d0
	move.b	d2,d0
	addq.w	#5,d0
	and.w	#$fc,d0
	add.l	d0,a4			 ; next message
	move.l	qxl_kbd_link,d0
	beq	qmp_loop
	move.l	d0,a3

qmp_kbdloop
	move.b	(a0)+,d0
	jsr	kbd_pc84x		 ; convert AT keyboard byte
	subq.b	#1,d2
	bne.s	qmp_kbdloop
	bra	qmp_loop

qmp_vmack
	addq.l	#4,a4			 ; next message
	move.l	qxl_scr_work,a2
	clr.b	qxl_vhold(a2)		 ; display updates no longer held
	lea	qmp_qrt_flg(pc),a5
	tst.b	(a5)				; QXL restart?
	beq	qmp_loop			; no
	cmp.b	#ptd.16,qxl_vcolr(a2)		; 16 bit depth?
	beq	qmp_loop			; yes
	move.l	sys_clnk(a6),a3 		; console linkage
	move.b	pt_dmode(a3),d1
	jsr	pt_xmode
	lea	qmp_kbd_llck(pc),a2
	st	(a2)				; force led update at restart
	bsr.s	qmp_updt_led
	sf	(a5)				; QXL restart finished
	bra	qmp_loop

;+++
; This routine sends the qxm.flowqx message when a led update is needed
;
;	a6 c  p system variable base
;
;---
qmp_updt_led
	movem.l qmp.reg,-(sp)
	move.w	sys_caps(a6),d0 		; read sys_caps...
	move.b	sys_dfrz(a6),d0 		; ...and sys_dfrz
	rol.w	#1,d0
	and.b	#$03,d0
	lea	qmp_kbd_llck(pc),a2
	cmp.b	(a2),d0 			; status change?
	beq.s	qmp_updt_led_exit		; no
	move.b	d0,(a2) 			; save current status
	move.l	qxl_message,a3
	lea	qxl_ms_flow+qxl_ms_len(a3),a1
	move.l	#qxm.flowlen<<16+qxm.flowqx<<8,(a1)	; ... msg length and key
	move.b	d0,qxm_fkbd-qxl_ms_len(a1)

qmp_updt_led_exit
	movem.l (sp)+,qmp.reg
	rts

qmp_kbd_llck
	dc.b	$00				; last kbd lock status

qmp_qrt_flg
	dc.b	$00

qmp_rstrt
	addq.l	#4,a4				; next message
	lea	qmp_qrt_flg(pc),a5		; QXL restart...
	st	(a5)				; ...in progress
	move.l	qxl_scr_work,a2 		; screen work area
	st	qxl_vhold(a2)			; hold display updates
	jsr	cn_copy_clear
	move.l	qxl_message,a3			; message area
	lea	qxl_ms_vmode+qxl_ms_len(a3),a1	; mode message length
	move.l	#4<<16+qxm.vmode<<8,(a1)	; ... message length and key
	move.b	qxl_vcolr(a2),d0		; internal colour
	beq.s	qrt_sclr			; QL mode
	subq.b	#1,d0				; VGA mode is one different

qrt_sclr
	move.b	d0,qxm_vclr-qxl_ms_len(a1)		; set colour
	move.b	qxl_vsize(a2),d0
	bpl.s	qrt_ssiz				; QL mode?
	addq.b	#1,d0					; yes

qrt_ssiz
	move.b	d0,qxm_vres-qxl_ms_len(a1)		; set size
	jsr	qxl_mess_add				; add message to queue
	bra	qmp_loop

;
; Physical sector successfully read
;
qmp_physr
	move.l	qxl_message,a5
	lea	qxl_ms_phys(a5),a2
	assert	qxm_rdata,4
	move.l	(a4)+,(a2)+		 ; the message
	moveq	#512/16-1,d0
qpr_loop
	move.l	(a4)+,(a2)+
	move.l	(a4)+,(a2)+
	move.l	(a4)+,(a2)+
	move.l	(a4)+,(a2)+
	dbra	d0,qpr_loop
qmp_physack
	tst.b	qxl_junk
	bne	qmp_loop		 ; if junking messages, do not mark it

	st	qxl_ms_len+qxl_ms_phys(a5) ; done flag

	bra	qmp_loop

;
; Physical sector read fail or physical sector write ack
;
qmp_rpfail
qmp_physw
qmp_wpfail
	move.l	qxl_message,a5
	move.l	(a4)+,qxl_ms_phys(a5)	   ; the message
	bra	qmp_physack

;
; Format ack
;
qmp_drivf
	move.l	qxl_message,a5
	lea	qxl_ms_phys(a5),a2
	move.l	(a4)+,(a2)+		 ; the message
	move.l	(a4)+,(a2)+		 ; the total
	move.l	(a4)+,(a2)+		 ; good
	bra	qmp_physack
;
; Mouse update
;
qmp_mouse
	move.l	qxl_message,a5
	lea	qxl_ms_mouse(a5),a2
	move.w	(a4)+,(a2)+		 ; the message and the buttons
	move.l	(a4)+,d0
	add.l	d0,(a2)+		 ; update the totals
	addq.l	#2,a4			 ; the spare at the end
	bra	qmp_loop
	end
