; GOLD Card Read keyboard

	section kbd

	xdef	kbd_read
	xdef	kbd_vers

kbd_vers equ	'2.02'

	xref	ql_hcmdw
	xref	ql_hcmdr

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

hdop_rkb dc.b	1,4,8,4 		 ; keyboard get 4 bit reply
;+++
; QL keyboard routine (called from poll)
;---
kbd_read
	move.l	sys_ckyq(a6),d0
	beq.s	hok_rts
	move.l	d0,a2			 ; current keyboard queue
	lea	hdop_rkb,a3
	bsr.l	ql_hcmdw
	moveq	#8,d5			 ; repeat
	and.b	d1,d5
	moveq	#7,d6			 ; 0-7 chars
	and.b	d1,d6
	beq.s	hok_repc
	move.w	sys_rdel(a6),sys_rcnt(a6) ; new character - start delay

hok_loop
	clr.w	sys_lchr(a6)
	moveq	#12,d2			 ; receive 12 bits
	bsr.l	ql_hcmdr
	bsr.l	hok_dcde
	bsr.s	hok_chkc		 ; ... check the character
hok_elp
	subq.b	#1,d6
	bgt.s	hok_loop
hok_rts
	rts

hok_repc
	tst.b	d5			 ; key held down?
	beq.s	hok_rts 		 ; ... no

	subq.w	#1,sys_rcnt(a6) 	 ; auto repeat
	bgt.s	hok_rts 		 ; ... not ready
	jsr	ioq_tstg		 ; any byte in queue?
	beq.s	hok_rts 		 ; ... yes

	move.w	sys_lchr(a6),d1 	 ; ... no, recover last character
	beq.s	hok_rts 		 ; !!!!
	move.w	sys_rtim(a6),sys_rcnt(a6) ; next repeat
	bra.s	hok_putc

hok_chkc
	tst.l	d1			 ; break?
	bmi.s	hok_break		 ; ... yes

	cmp.w	sys_swtc(a6),d1 	 ; switch queue?
	beq.s	hok_swtc		 ; ... yes

	cmp.b	#k.cf5,d1		 ; screen freeze?
	beq.s	hok_dfrz		 ; ... yes

	cmp.b	#k.caps,d1		 ; CAPSLOCK?
	beq.s	hok_caps

	move.w	d1,sys_lchr(a6) 	 ; save last character
hok_putu
	sf	sys_dfrz(a6)		 ; unfreeze screen
hok_putc
	cmp.b	#$ff,d1 		 ; ALT?
	bne.s	hok_put1		 ; ... no
	swap	d1			 ;
	jsr	ioq_test		 ; ... yes, check
	subq.l	#2,d2			 ; ... ... for room for 2
	blt.s	hok_rts 		 ; ... no
	swap	d1
	bsr.s	hok_put1		 ; put one byte
	lsr.w	#8,d1			 ; ... and then the other
hok_put1
	jmp	ioq_pbyt		 ; put byte

; break

hok_break
	btst	#sys..sbk,sys_klock(a6)  ; suppressed?
	bne.s	hokb_rts
	st	sys_brk(a6)		 ; flag break
hokb_rts
	rts

; switch keyboard queue

hok_swtc
	btst	#sys..ssq,sys_klock(a6)  ; suppressed?
	bne.s	hokb_rts
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
	rts

; screen freeze

hok_dfrz
	btst	#sys..ssf,sys_klock(a6)  ; suppressed?
	bne.s	hokb_rts
	not.b	sys_dfrz(a6)		 ; freeze / unfreeze screen
	rts

; CAPSLOCK

hok_caps
	not.b	sys_caps(a6)		 ; caps lock toggle
	move.l	sys_capr(a6),d0 	 ; keyboard intercept routine?
	ble.s	kbp_capsold		 ; ... no, try old style
	move.l	d0,a1
	jmp	(a1)			 ; ... yes, do it

kbp_capsold
	tst.l	sys_csub(a6)
	beq.l	hokd_rts		 ; no routine
	jmp	sys_csub(a6)		 ; caps routine

; decode keyboard  d1.w = 0000 xSCA xxRR RCCC	   RRR = 7-row, CCC = col

hok.cspc equ	$236			 ; keycode for ctrl space
hok.reset equ	$713			 ; ... reset
hok_dcde
	cmp.w	#hok.cspc,d1
	beq.l	hokd_brk
	cmp.w	#hok.reset,d1
	beq.l	hok_reset
	lsl.b	#2,d1			 ; remove upper two bits of code
	lsr.w	#1,d1			 ; get ALT
	add.b	d1,d1			 ; remove it
	scs	d0			 ; but flag
	lsr.w	#3,d1			 ; unknown bit in msb of byte
	roxl.b	#1,d1			 ; shift / ctrl / keycode in byte
	move.l	kb_ktab(a1),a0		 ; current keyboard table
	move.b	(a0,d1.w),d1		 ; keystroke

	movem.l d0/d2/a1/a3,-(sp)
	move.l	a1,a3
	move.l	kb_nstab(a3),a1 	 ; is it a non-spacing ident?
	moveq	#0,d0
	and.w	#$00ff,d1
	move.b	(a1,d1.w),d0
	beq.s	kbp_cknsid		 ; ... no, check if it was already?
	move.w	d0,kb_nsid(a3)		 ; ... yes, save it
	movem.l  (sp)+,d0/d2/a1/a3
	moveq	#0,d1
	rts

kbp_cknsid
	move.w	kb_nsid(a3),d0		 ; have we a non-spacing ident?
	beq.s	kbp_nnspc		 ; ... no
	clr.w	kb_nsid(a3)		 ; ... not now
	add.w	#$100,a1		 ; chars to modify
kbp_nsloop
	move.b	(a1)+,d2
	beq.s	kbp_nnspc		 ; end of list
	cmp.b	d2,d1			 ; this one?
	bne.s	kbp_nsloop		 ; ... no
	move.b	-1(a1,d0.w),d1		 ; ... yes, real character

kbp_nnspc
	movem.l  (sp)+,d0/d2/a1/a3

	tst.b	d0			 ; ALT?
	beq.s	hokd_caps
	cmp.b	#k.alt1l,d1		 ; below lowest add 1 for ALT?
	blo.s	hok_altb		 ; ... yes
	cmp.b	#k.alt1h,d1		 ; above highest add 1 for ALT?
	bhi.s	hok_altb
	addq.b	#1,d1			 ; add one for alt
	rts

hok_altb
	lsl.w	#8,d1			 ; ALT
	subq.b	#1,d1			 ; $FF + char
	rts

hokd_caps
	tst.b	sys_caps(a6)		 ; CAPS?
	beq.s	hokd_rts		 ; ... no
	lea	cv_uctab,a0
	move.b	(a0,d1.w),d1		 ; uppercase it
hokd_rts
	rts
hokd_brk
	moveq	#-1,d1			 ; flag break
	rts

hok_reset
	btst	#sys..ssr,sys_klock(a6)  ; suppressed?
	bne.s	hokd_nul
	jsr	smsq_sreset
hokd_nul
	moveq	#0,d0
	rts

	end
