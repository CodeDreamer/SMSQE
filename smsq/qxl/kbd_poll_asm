; SMSQ general KBD polling routine v1.01   1988 / 1993 / 2000 Tony Tebby
;					       2003 Marcel Kilgus
; 2000-06-24	1.01	Handles left and right shift key separately.
; 2006-09-28	1.02	Allows normal code for CAPS + SFHT/CTRL/ALT
;			uses LED routine qmp_updt_led (BC)

; This takes a stream of keyboard codes from the internal keyboard queue.
; These codes have bit 7 set for key up and special escape codes.
; It also handles 'button 3' keyboard stuffing for combined mouse kbd drivers.

	section kbd

	xdef	kbd_poll

	xref	kbd_krtab
	xref	kbd_atab

	xref	cv_upcas
	xref	ioq_test
	xref	ioq_gbyt
	xref	ioq_pbyt

	xref	pt_button3

	xref	qmp_updt_led

	xref	smsq_sreset
	xref	smsq_hreset

	include 'dev8_keys_sys'
	include 'dev8_keys_chn'
	include 'dev8_keys_con'
	include 'dev8_keys_k'
	include 'dev8_keys_qu'
	include 'dev8_smsq_kbd_keys'
	include 'dev8_mac_assert'


kbd_poll
	assert	kb_err,kb_b3
	tst.b	kb_err(a3)		 ; errors? (signalled by interrupt server)
	beq.s	kbp_kq			 ; ... no
	blt.s	kbp_b3			 ; ... not really - it is a button 3
	subq.b	#1,kb_err(a3)		 ; wait a little bit
	bra.s	kbp_rts

kbp_b3
	jsr	pt_button3		 ; do a button 3
	sf	kb_b3(a3)		 ; ... and clear it

kbp_kq
	move.l	sys_ckyq(a6),d6 	 ; any keyboard queue?
	beq.s	kbp_rts

	move.l	kb_queue(a3),a2
	jsr	ioq_gbyt
	beq.s	kbp_do			 ; code in queue

	move.w	kb_lcod(a3),d5		 ; key still down?
	beq.s	kbp_nextk		 ; ... no
	subq.w	#1,sys_rcnt(a6) 	 ; count down
	bgt.s	kbp_nextk
	move.l	d6,a2			 ; anything in queue?
	jsr	ioq_test
	beq.s	kbp_nextk		 ; ... something there

	move.w	sys_rtim(a6),sys_rcnt(a6); auto repeat time
	move.w	sys_lchr(a6),d1 	 ; old char
	st	kb_arep(a3)		 ; auto repeat!!
	bra.l	kbp_send		 ; ... do it

kbp_rts
	rts

kbp_nrep
	move.w	#$7fff,sys_rcnt(a6)	 ; do not auto repeat (very often!)
kbp_nextk
kbp_again
	move.l	kb_queue(a3),a2
	jsr	ioq_gbyt
	bne.s	kbp_rts 		  ; no key available

kbp_do
	and.w	#$ff,d1
	move.w	d1,d2			 ; d1 = key code and key state
	and.w	#$7f,d2 		 ; d2 = key code only

	lea	kbd_krtab,a1		 ; get base of keyrow table
	move.b	(a1,d2.w),d0		 ; get bit/row
	move.w	d0,d3
	and.w	#$f,d0			 ; row only
	lsr.b	#4,d3
	btst	#kb..up,d1		 ; key up?
	bne.s	kbp_keyu
	bset	d3,kb_krwem(a3,d0.w)	 ; ... no, key down
	bra.s	kbp_action
kbp_keyu
	bclr	d3,kb_krwem(a3,d0.w)	 ; key up

kbp_action
	lea	kbd_atab,a1
	moveq	#0,d0
	move.b	(a1,d2.w),d0		 ; action
	add.w	kbp_act(pc,d0.w),d0
	jmp	kbp_act(pc,d0.w)

kbp_act
	dc.w	kbp_norm-*
	dc.w	kbp_shiftl-*
	dc.w	kbp_shiftr-*
	dc.w	kbp_ctrll-*
	dc.w	kbp_ctrlr-*
	dc.w	kbp_alt-*
	dc.w	kbp_altgr-*
	dc.w	kbp_caps-*
	dc.w	kbp_slock-*
	dc.w	kbp_nrep-*
	dc.w	kbp_sys-*
	dc.w	kbp_undo-*
	dc.w	kbp_break-*
	dc.w	kbp_tab-*

;---------------------------------------------------------------------
; shift keys

kbp_shiftl
	moveq	#kb..shfl,d2		 ; left shift key
	bra.s	kbp_shift
kbp_shiftr
	moveq	#kb..shfr,d2		 ; right shift key
kbp_shift
	moveq	#kb..shft,d3		 ; combined status
	moveq	#kb.shftb,d0
	clr.b	sys_dfrz(a6)		 ; unfreeze display
	jsr	qmp_updt_led
	bra.s	kbp_twokeys

kbp_ctrll
	moveq	#kb..ctll,d2		 ; left ctrl key
	bra.s	kbp_ctrl
kbp_ctrlr
	moveq	#kb..ctlr,d2		 ; right ctrl key
kbp_ctrl
	moveq	#kb..ctrl,d3		 ; combined status
	moveq	#kb.ctrlb,d0
kbp_twokeys				 ; handler for SHIFT and CTRL
	assert	kb..up,7
	tst.b	d1			 ; key up?
	bmi.s	kbp_twokeyup		 ; ... yes
	bset	d2,kb_stat(a3)
	bra.s	kbp_sspec		 ; key pressed -> set combined status
kbp_twokeyup
	bclr	d2,kb_stat(a3)
	and.b	kb_stat(a3),d0		 ; both keys up?
	beq.s	kbp_cspec		 ; ... yes, clear combined status
	bra	kbp_again

kbp_alt
	moveq	#kb..alt,d3		 ; alt key
	bra.s	kbp_spec
kbp_altgr
	moveq	#kb..agr,d3		 ; alt gr key
kbp_spec
	assert	kb..up,7
	tst.b	d1			 ; key up?
	bmi.s	kbp_cspec		 ; ... yes
kbp_sspec
	bset	d3,kb_stat(a3)
	bra.s	kbp_stab
kbp_cspec
	bclr	d3,kb_stat(a3)
kbp_stab
	move.w	#kb.gcs,d3		 ; table is alt gr control and shift dep
	and.b	kb_stat(a3),d3
	bmi.s	kbp_agtab
	lsl.w	#kb..tabs,d3		 ; set table pointer
	move.w	d3,kb_tab(a3)
	bra	kbp_again
kbp_agtab
	move.w	#4<<kb..tabs,kb_tab(a3)
	bra	kbp_again

; capslock

kbp_caps
	assert	kb..up,7
					 ; Added 1.02
	tst.w	kb_stat(a3)		 ; If any of SHIFT, CONTROL, ALT . .
	bne	kbp_norm		 ; . .	set a normal code
					 ; End of addition



	tst.b	d1			 ; key up?
	bmi	kbp_again		 ; ... yes
	not.b	sys_caps(a6)
	jsr	qmp_updt_led
	move.l	sys_capr(a6),d0 	 ; keyboard intercept routine?
	ble.s	kbp_capsold		 ; ... no, try old style
	move.l	d0,a1
	jsr	(a1)			 ; ... yes, do it
	bra	kbp_nrep

kbp_capsold
	tst.l	sys_csub(a6)		 ; capslock routine?
	beq	kbp_nrep		 ; ... no
	jsr	sys_csub(a6)		 ; ... yes, do it
	bra	kbp_nrep

;-----------------------------------------------------------
; special action keys

; freeze screen

kbp_slock
	bsr.l	kbp_keystroke		 ; check for keystroke
kbp_freezz
	btst	#sys..ssf,sys_klock(a6)  ; suppressed?
	bne	kbp_nrep
	not.b	sys_dfrz(a6)		 ; ... toggle screen frozen flag
	jsr	qmp_updt_led
	bra	kbp_nrep

; system request

kbp_sys
	bsr.l	kbp_keystroke		 ; check for keystroke
	bra	kbp_nrep		 ; sys req ignored

; undo / break hard reset

kbp_undo
kbp_break
	bsr.l	kbp_keystroke		 ; check for keystroke
	moveq	#kb.acs,d0
	and.b	kb_stat(a3),d0
	cmp.b	#kb.acs,d0		 ; CTRL ALT SHIFT?
	beq.s	kbp_hreset		 ; ... yes, do hard reset
	btst	#kb..ctrl,kb_stat(a3)	 ; CTRL?
	beq.l	kbp_dokey		 ; ... no, do normal key

kbp_dobreak
	btst	#sys..sbk,sys_klock(a6)  ; suppressed?
	bne	kbp_nrep
	tas	sys_brk(a6)		 ; clean break
	bra	kbp_nrep

kbp_hreset
	btst	#sys..shr,sys_klock(a6)  ; hard reset suppressed?
	bne	kbp_nrep
	jsr	smsq_hreset
	bra	kbp_nrep

; tab / soft reset

kbp_tab
	bsr.s	kbp_keystroke		 ; check for keystroke
	moveq	#kb.acs,d0
	and.b	kb_stat(a3),d0
	cmp.b	#kb.acs,d0		 ; CTRL ALT SHIFT?
	bne.l	kbp_dokey		 ; ... no, normal key

kbp_sreset
	btst	#sys..ssr,sys_klock(a6)  ; soft reset suppressed?
	bne	kbp_nrep
	jsr	smsq_sreset
	bra	kbp_nrep

; switch keyboard queue - a bit odd because it needs to be QDOS compatible

kbp_swt
	btst	#sys..ssq,sys_klock(a6)  ; suppressed?
	bne	kbp_nrep
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
	move.l	a2,d6
	move.w	#$7fff,sys_rcnt(a6)	 ; do not auto repeat (very often!)
	rts				 ; do not try again straight away

;------------------------------------------------------
; Suppress silly keyboard repeat for normal and special keystrokes

kbp_keystroke
	bclr	#kb..up,d1		 ; key up?
	beq.s	kbp_kdown		 ; ... no, down
	cmp.w	kb_lcod(a3),d1		 ; same code as last down?
	bne.s	kbp_kjunk
	clr.w	kb_lcod(a3)
	tst.b	kb_arep(a3)		 ; autorepeated character?
	beq.s	kbp_kjunk		 ; ... no
	move.l	d6,a2
	move.l	qu_nexti(a2),qu_nexto(a2); ... yes, clear queue
kbp_kjunk
	addq.l	#4,sp			 ; skip return
	bra	kbp_again		 ; and do next key

kbp_kdown
	cmp.w	kb_lcod(a3),d1		 ; same key?
	beq.s	kbp_kjunk		 ; ... yes, ignore it

	move.w	d1,kb_lcod(a3)		 ; key down
	clr.b	kb_arep(a3)		 ; not auto-repeated
	move.w	sys_rdel(a6),sys_rcnt(a6); auto repeat delay
	rts

;------------------------------------------------------
; process normal keystrokes

kbp_norm		 ; normal keystroke
	bsr	kbp_keystroke		 ; check for keystroke

kbp_dokey
	move.l	kb_ktab(a3),a1		 ; key translate table
	add.w	kb_tab(a3),a1		 ; the right one

	move.l	d6,a2			 ; keyboard queue

	move.b	(a1,d1.w),d1		 ; get QL key code
	move.l	kb_nstab(a3),a1 	 ; is it a non-spacing ident?
	moveq	#0,d0
	move.b	(a1,d1.w),d0
	beq.s	kbp_cknsid		 ; ... no, check if it was already?
	move.w	d0,kb_nsid(a3)		 ; ... yes, save it
	bra.l	kbp_nrep		 ; ... and throw away

kbp_cknsid
	move.w	kb_nsid(a3),d0		 ; have we a non-spacing ident?
	beq.s	kbp_ckesc		 ; ... no
	clr.w	kb_nsid(a3)		 ; ... not now
	add.w	#$100,a1		 ; chars to modify
kbp_nsloop
	move.b	(a1)+,d2
	beq.s	kbp_ckesc		 ; end of list
	cmp.b	d2,d1			 ; this one?
	bne.s	kbp_nsloop		 ; ... no
	move.b	-1(a1,d0.w),d1		 ; ... yes, real character
	bra.s	kbp_ckalt

kbp_ckesc
	cmp.b	#k.esc,d1		 ; do not auto repeat ESC key
	bne.s	kbp_ckalt
	move.w	#$7fff,sys_rcnt(a6)	 ; do not auto repeat (very often!)

kbp_ckalt
	btst	#kb..alt,kb_stat(a3)	 ; alt
	beq.s	kbp_case		 ; no, check special cases

	clr.b	sys_dfrz(a6)		 ; unfreeze screen
	jsr	qmp_updt_led
	cmp.b	#k.alt1l,d1		 ; below min altkey+1?
	blo.s	kbp_alt2		 ; ... yes
	cmp.b	#k.alt1h,d1		 ; above same?
	bhi.s	kbp_alt2		 ; ... yes
	addq.b	#1,d1			 ; ... no, add one
	bra.s	kbp_send		 ; done

kbp_alt2		 ; send two bytes
	or.w	#$ff00,d1		 ; $ff/char
	cmp.b	#'S',sys_idnt(a6)	 ; SMSQ/E type?
	beq.s	kbp_send
	bra.s	kbp_docaps

kbp_case
	btst	#kb..ctrl,kb_stat(a3)	 ; control?
	beq.s	kbp_docaps		 ; ... no do capslock if set

	cmp.b	#k.freez,d1		 ; is it freez?
	beq	kbp_freezz
	cmp.w	sys_swtc(a6),d1 	 ; is it switch?
	beq	kbp_swt
	cmp.b	#k.break,d1		 ; is it break?
	beq	kbp_dobreak		 ; ... yes, do it

kbp_docaps
	clr.b	sys_dfrz(a6)		 ; unfreeze screen
	jsr	qmp_updt_led
	tst.w	sys_caps(a6)		 ; check capslock
	beq.s	kbp_send		 ; ... not caps
	move.w	d1,d2			 ; save altkey flag
	jsr	cv_upcas		 ; upper case it
	clr.b	d2
	or.w	d2,d1			 ; and restore altkey flag

kbp_send
	move.w	d1,sys_lchr(a6) 	 ; save last char(s)
	beq	kbp_again		 ; no character at all
	bpl.s	kbp_pbyt		 ; just one

	move.w	d1,d5			 ; $ff/char,  save character
	jsr	ioq_test		 ; check room
	cmp.w	#2,d2			 ; at least 2?
	blt	kbp_again		 ; ... no
	moveq	#-1,d1			 ; ... yes, send $ff
	jsr	ioq_pbyt
	move.w	d5,d1			 ; and carry on
kbp_pbyt
	jsr	ioq_pbyt		 ; put character in
	bra	kbp_again

	end
