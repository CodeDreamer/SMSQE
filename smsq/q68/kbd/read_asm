; Q68 Keyboard polling	  (c) 2016-2020 W. Lenerz
;
; 2020-03-30	1.01	use possible delay loop ; apparently some keyboard adapters (usb->ps/2) do not respect the
;			ps/2 protocol : instead of waiting for the signal that a new byte may be sent, they just
;			wait a bit (apparently for a variable period of time) and then send another byte.



	section kbdint

	xdef	kbd_read

	xref	kbd_at102cvt
	xref	kbd_poll

	include 'dev8_keys_sys'
	include 'dev8_keys_q68'
	include 'dev8_keys_hwt'
	include 'dev8_smsq_kbd_keys'
	include 'dev8_keys_k'
	include 'dev8_keys_con'
	include 'dev8_keys_chn'

;+++
; This routine handles keyboard polling. The polling "server" is set up in
; smsq_q68_kbd_initi_asm
;---
kbd_read

;!!!!!!!!!!!DEBUG		  ----|
;				      |
	genif debug = 1 	      |
	cmp.l	#'gold',q68_jflg      |
	beq.s	java_kbd_read	      |
	endgen			      |
;				      |
;!!!!!!!!!!!!!!!!!		  ----|
		     
	btst	#kbd.rcv,kbd_status	; any keyboard data?
	beq.s	kbdi_exit		; ... no
rd_kbd	moveq	#0,D0
	move.b	kbd_code,d0		; IBM keycode
	st	kbd_unlock		; acknowledge key receipt
					; this should clear the kbd.rcv bit in kbd_status
	tst.b	kb_err(a3)		; strip queue on error?
	bgt.s	kbdi_exit		; ... yes

process bsr	kbd_at102cvt		; process key code

;	 jsr	 kbd_poll
;	 move.l  kbd_dlay,d0		 ; re-try kbd?
;	 beq.s	 kbd_out		 ;   no ->
;	 move.l  q68_timer,d1		 ;   yes, current timer
;loop	 btst	 #kbd.rcv,kbd_status	 ; any keyboard data?
;	 bne.s	 rd_kbd 		 ;  yes, get it
;	 move.l  q68_timer,d2		 ; get current timer
;	 sub.l	 d1,d2
;	 cmp.l	 d0,d2			 ; time expired?
;	 blt.s	 loop			 ; no, loop until it does  (or new keystroke arrives)
;kbd_out rts

kbdi_exit
	jmp	kbd_poll		; handle keystroke(s)





;!!!!!!!!!!!DEBUG		  ----|
	genif debug = 1
	    

;+++
; DEBUG - SMSQ68mulator keyboard routine
;---

	xref	ioq_pbyt
	xref	ioq_test

; wheel on mouse moved - generate 3 keystrokes
wheel
	jsr	ioq_pbyt		 ; put byte in kbd queue
	jsr	ioq_pbyt		 ; put byte in kbd queue
	jsr	ioq_pbyt		 ; put byte in kbd queue
	and.l	#$ff,d1 		 ;
	move.w	d1,sys_lchr(a6) 	 ; save last character
	bra.s	hok_rts

java_kbd_read
smsh.reg reg	d1/d2/a1/a2
	movem.l smsh.reg,-(a7)

	moveq	#0,D0
	btst	#kbd.rcv,kbd_status	; any keyboard data?
	beq.s	hok_rts 		; ... no
	move.b	kbd_code,d0		; IBM keycode
	st	kbd_unlock		; acknowledge key receipt

	bclr	#kbd.rcv,kbd_status
	move.l	sys_ckyq(a6),d0 	; any keyboard queue?
	beq.s	hok_rts 		; no!
	move.l	d0,a2			; A2 = current keyboard queue
	move.l	q68_jkbd,d0		; get current key char(s)
	clr.l	q68_jkbd		; no more
	move.l	sys_klnk(a6),a1 	; keyboard linkage
	move.l	d0,d1			; char(s) gotten
	bne.s	hok_chkc		; there were some
	btst	#0,$29(a1)		; shift pressed?
	beq.s	hok_rts 		; no
	sf	sys_dfrz(a6)		; yes, unfreeze screen
hok_rts
	movem.l (a7)+,smsh.reg
	rts


hok_chkc
	cmp.l	#$d1d1d1,d1
	beq.s	wheel
	cmp.l	#$d9d9d9,d1
	beq.s	wheel
	tst.l	d1			; break?
	bmi.s	hok_break		; ... yes

	cmp.w	sys_swtc(a6),d1 	; switch queue?
	beq.s	hok_swtc		; ... yes

	cmp.b	#k.cf5,d1		; screen freeze?
	beq.s	hok_dfrz		; ... yes

	cmp.b	#k.caps,d1		; CAPSLOCK?
	beq.s	hok_caps

	move.w	d1,sys_lchr(a6) 	; save last character
	move.w	d1,kb_lcod(a1)		; also here
hok_putu
	sf	sys_dfrz(a6)		; unfreeze screen
hok_putc
	cmp.b	#$ff,d1 		; ALT?
	bne.s	hok_put1		; ... no
	swap	d1			;
	jsr	ioq_test		; ... yes, check
	subq.l	#2,d2			; ... ... for room for 2
	blt.s	hok_rts 		; ... no
	swap	d1
	jsr	ioq_pbyt		; put one byte
	lsr.w	#8,d1			; ... and then the other
hok_put1
	jsr	ioq_pbyt		 ; put byte in kbd queue
	bra.s	hok_rts
; break
hok_break
	btst	#sys..sbk,sys_klock(a6)  ; suppressed?
	bne.s	hok_rts
	st	sys_brk(a6)		 ; flag break
hokb_rts
	bra.s	hok_rts

; switch keyboard queue

hok_swtc
	btst	#sys..ssq,sys_klock(a6)  ; suppressed?
	bne.s	hok_rts
	move.l	(a2),d2 		 ; keep next queue
	move.l	d2,a2
	bra.s	hok_ckq

hok_nxq
	move.l	(a2),a2 		 ; next queue
	cmp.l	a2,d2			 ; same as saved queue?
	beq.s	hok_setq
hok_ckq
	tst.b	sd_curf-sd_keyq(a2)	 ; cursor enabled?
	bne.s	hok_setq		 ; ... yes, go to it
	tst.b	chn_stat-sd_keyq(a2)	 ; waiting?
	beq.s	hok_nxq 		 ; ... no
	cmp.b	#4,chn_actn-sd_keyq(a2)  ; input?
	bgt.s	hok_nxq 		 ; ... no

hok_setq
	move.l	a2,sys_ckyq(a6) 	 ; reset keyboard queue
	bra	hok_rts

; screen freeze

hok_dfrz
	btst	#sys..ssf,sys_klock(a6)  ; suppressed?
	bne.s	hokb_rts
	not.b	sys_dfrz(a6)		 ; freeze / unfreeze screen

	bra	hok_rts

; CAPSLOCK

hok_caps
	not.b	sys_caps(a6)		 ; caps lock toggle
	move.l	sys_capr(a6),d0 	 ; keyboard intercept routine?
	ble.s	kbp_capsold		 ; ... no, try old style
	move.l	d0,a1
	jsr	(a1)			 ; ... yes, do it
	bra	hok_rts
kbp_capsold
	tst.l	sys_csub(a6)
	beq.l	hok_rts 		 ; no routine
	jsr	sys_csub(a6)		 ; caps routine
	bra	hok_rts


	endgen
;!!!!!!!!!!!!!!!!!		  ----|



	end
