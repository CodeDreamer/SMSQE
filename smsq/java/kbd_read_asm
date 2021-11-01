; SMSQmulator Read keyboard / mouse  V 1.03 (c) W. Lenerz 2020

; read by SMSQE during polling interrupt.

; this "reads" the keyboard coming from the emulator & puts the keystrokes
; into the keyboard queue
; 1.03 read kbd loops until no more chars in java kbd queue (queue in & out in java are synchronized)
; 1.02 mouse wheel code is transferred to driver_mouse
; 1.01 special long word input when mouse moved
; 1.00 initial version

	section kbd

	xdef	kbd_read
	xdef	kbd_vers

kbd_vers equ	'1.03'


	xref	ioq_tstg
	xref	ioq_test
	xref	ioq_pbyt

	xref	cv_uctab

	xref	smsq_sreset

	include 'dev8_smsq_kbd_keys'
	include 'dev8_keys_k'
	include 'dev8_keys_sys'
	include 'dev8_keys_con'
	include 'dev8_keys_chn'
	include 'dev8_keys_java'


;+++
; QL keyboard routine (called from poll)
;---


smsh.reg reg	d1/d2/a1/a2
kbd_read movem.l smsh.reg,-(a7)
	move.l	sys_ckyq(a6),d0 	; any keyboard queue?
	beq.s	hok_rts 		; no!
	move.l	d0,a2			; A2 = current keyboard queue
;	 move.l  jva_lkptr,d0		 ; address of java emul linkage block
;	 beq.s	 hok_rts		 ; there is none (????)
;	 move.l  a1,-(sp)
;	 move.l  d0,a1			 ; point to java linkage block
;	 move.l  jva_lkkbd(a1),d0	 ; get current key char(s)
;	 clr.l	 jva_lkkbd(a1)		 ; no more
;	 move.l  (sp)+,a1
getkey	moveq	#jte.kbd,d0
	dc.w	jva.trpE		; returns key(s) in D1, cond codes set
	tst.l	d1
	beq.s	hok_done

	move.l	sys_klnk(a6),a1 	; keyboard linkage
;	 move.l  d0,d1			 ; char(s) gotten
;	 bne.s	 hok_chkc		 ; there were some
	btst	#0,$29(a1)		; shift pressed?
	beq.s	nofrz			 ; no
	sf	sys_dfrz(a6)		; yes, unfreeze screen
nofrz	bsr.s	hok_chkc		; do until queue empty
	bra.s	getkey


hok_done movem.l (a7)+,smsh.reg
hok_rts rts


hok_chkc
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
	jsr	ioq_pbyt		; put byte in kbd queue
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
	end
