; QPC KBD polling routine (Windows driver) v1.00	  2013 Marcel Kilgus

	section kbd

	xdef	qpc_kbd_init

	xref	ioq_test
	xref	ioq_gbyt
	xref	ioq_pbyt

	xref	smsq_sreset

	include 'dev8_keys_sys'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_chn'
	include 'dev8_keys_con'
	include 'dev8_keys_k'
	include 'dev8_keys_qu'
	include 'dev8_smsq_kbd_keys'
	include 'dev8_mac_assert'
	include 'dev8_smsq_qpc_keys'
	include 'dev8_smsq_smsq_config_keys'

; This must only be called AFTER mouse_init
qpc_kbd_init
	moveq	#sms.xtop,d0
	trap	#do.sms2

	lea	qpc_sconf,a2
	tst.b	sms_winkbd(a2)
	beq.s	qpc_rts 		 ; Use SMSQ/E keyboard driver

	move.l	sys_klnk(a6),a3 	 ; keyboard linkage
	lea	kbd_poll,a5
	move.l	a5,kb_extlk(a3) 	 ; exchange KBD poll by our version
qpc_rts
	moveq	#0,d0
	rts


; Simple keyboard poll routine for when QPC does the character mapping
kbd_poll
	move.l	qpc_kbd_link,d0
	beq.s	kbp_rts
	move.l	d0,a3

	lea	kb_krwem(a3),a0
	dc.w	qpc.gkeyr		 ; get keyrow status
; Need to unfreeze display on SHIFT, check if it turned on
	move.b	kb_stat(a3),d1
	btst	#kb..shft,d1
	bne.s	kbp_shift		 ; SHIFT was already pressed last time
	btst	#kb..shft,d0
	beq.s	kbp_shift		 ; SHIFT is not pressed now
	clr.b	sys_dfrz(a6)		 ; unfreeze screen on shift
kbp_shift
	move.b	d0,kb_stat(a3)		 ; state of shift keys
kbp_again
	move.l	sys_ckyq(a6),d6 	 ; any keyboard queue?
	beq.s	kbp_rts

	move.l	kb_queue(a3),a2
	jsr	ioq_gbyt
	beq.s	kbp_do			 ; code in queue
kbp_rts
	rts

;-----------------------------------------------------------
; special action keys

; capslock

kbp_caps
	move.l	sys_capr(a6),d0 	 ; keyboard intercept routine?
	ble.s	kbp_capsold		 ; ... no, try old style
	move.l	d0,a1
	jsr	(a1)			 ; ... yes, do it
	bra	kbp_again

kbp_capsold
	tst.l	sys_csub(a6)		 ; capslock routine?
	beq	kbp_again		 ; ... no
	jsr	sys_csub(a6)		 ; ... yes, do it
	bra	kbp_again

; freeze screen

kbp_freezz
	btst	#sys..ssf,sys_klock(a6)  ; suppressed?
	bne	kbp_again
	not.b	sys_dfrz(a6)		 ; ... toggle screen frozen flag
	bra	kbp_again

kbp_dobreak
	btst	#sys..sbk,sys_klock(a6)  ; suppressed?
	bne	kbp_again
	tas	sys_brk(a6)		 ; clean break
	bra	kbp_again

; tab / soft reset
kbp_sreset
	btst	#sys..ssr,sys_klock(a6)  ; soft reset suppressed?
	bne	kbp_again
	jsr	smsq_sreset
	bra	kbp_again

; switch keyboard queue - a bit odd because it needs to be QDOS compatible
kbp_swt
	btst	#sys..ssq,sys_klock(a6)  ; suppressed?
	bne	kbp_again
	move.l	(a2),d2 		 ; keep next queue
	move.l	d2,a2
	bra.s	kbp_ckq

kbp_nxq
	move.l	(a2),a2 		 ; next queue
	cmp.l	a2,d2			 ; same as saved queue?
	beq.s	kbp_setq
kbp_ckq
	tst.b	sd_curf-sd_keyq(a2)	 ; cursor enabled?
	bne.s	kbp_setq		 ; ... yes, go to it
	tst.b	chn_stat-sd_keyq(a2)	 ; waiting?
	beq.s	kbp_nxq 		 ; ... no
	cmp.b	#4,chn_actn-sd_keyq(a2)  ; input?
	bgt.s	kbp_nxq 		 ; ... no

kbp_setq
	move.l	a2,sys_ckyq(a6) 	 ; reset keyboard queue
	bra	kbp_again

kbp_do
	andi.w	#$00ff,d1
	move.l	d6,a2			 ; keyboard queue

; Capslock
	cmp.b	#k.caps,d1
	beq	kbp_caps

; Check for soft reset (CTRL+ALT+SHIFT+TAB)
	cmpi.b	#k.stab,d1		 ; Shift+TAB?
	bne.s	kbp_alt
	moveq	#kb.acs,d0
	and.b	kb_stat(a3),d0
	cmpi.b	#kb.acs,d0		 ; CTRL ALT SHIFT?
	beq.s	kbp_sreset		 ; ... yes, it's soft reset

kbp_alt
	btst	#kb..alt,kb_stat(a3)	 ; alt
	beq.s	kbp_case		 ; no, check special cases

	cmpi.b	#k.alt1l,d1		 ; below min altkey+1?
	blo.s	kbp_alt2		 ; ... yes
	cmpi.b	#k.alt1h,d1		 ; above same?
	bhi.s	kbp_alt2		 ; ... yes
	bra.s	kbp_send		 ; done

kbp_alt2				 ; send two bytes
	ori.w	#$ff00,d1		 ; $ff/char
	bra.s	kbp_send

kbp_case
	btst	#kb..ctrl,kb_stat(a3)	 ; control?
	beq.s	kbp_send

	cmpi.b	#k.freez,d1		 ; is it freez?
	beq	kbp_freezz
	cmp.w	sys_swtc(a6),d1 	 ; is it switch?
	beq	kbp_swt
	cmpi.b	#k.break,d1		 ; is it break?
	beq	kbp_dobreak		 ; ... yes, do it

kbp_send
	clr.b	sys_dfrz(a6)		 ; unfreeze screen
	move.w	d1,sys_lchr(a6) 	 ; save last char(s)
	beq	kbp_again		 ; no character at all
	bpl.s	kbp_pbyt		 ; just one

	move.w	d1,d5			 ; $ff/char,  save character
	jsr	ioq_test		 ; check room
	cmpi.w	#2,d2			 ; at least 2?
	blt	kbp_again		 ; ... no
	moveq	#-1,d1			 ; ... yes, send $ff
	jsr	ioq_pbyt
	move.w	d5,d1			 ; and carry on
kbp_pbyt
	jsr	ioq_pbyt		 ; put character in
	bra	kbp_again

	end
