; SMSQ IBM  KBD PC 84 key emulation keyboard conversion   1999  Tony Tebby
;
; 2006-04-01  1.01  Enable numlock. Provide entry to set numlock for QPC (MK)

	section kbdint

	xdef	kbd_pc84x
	xdef	kbd_setnumlock

	xref	ioq_pbyt

	include 'dev8_smsq_kbd_keys'

kbdi.tsize equ $60
kbdi.alt   equ kbdi.tsize
kbdi.numl  equ kbdi.tsize*2
;+++
;	d0 c	key code (in a byte)
;	d1   s
;	a2   s
;	a3 c  p keybard linkage
;---
kbd_pc84x
	moveq	#$7f,d1
	and.b	d0,d1
	and.b	#$80,d0
; bra.s kbdi_put  ;;;; for no translate
	move.w	kb_intop(a3),a2
	jmp	kbdi_act(pc,a2.l)
kbdi_act
kbdi_normal
	cmp.b	#$60,d1 		 ; escape code?
	blo.s	kbdi_pbnorm		 ; ... no, normal character (d1 = 0)
	beq.s	kbdi_setalt
	cmp.b	#$61,d1 		 ; special
	bne.s	kbdi_rts		 ; ... no, forget it

	move.w	#kbdi_spec-kbdi_act,kb_intop(a3) ; set special action
	rts

kbdi_setalt
	move.w	#kbdi_alt-kbdi_act,kb_intop(a3) ; set alternate action
	rts


kbdi_spec
	cmp.b	#$1d,d1 		 ; only special sequence $1d,$45
	bne.s	kbdi_odd		 ; it is not
	move.w	#kbdi_spec1d-kbdi_act,kb_intop(a3) ; ... yes, set up action
	rts

kbdi_spec1d
	cmp.b	#$45,d1 		 ; only special sequence $1d,$45
	bne.s	kbdi_odd		 ; it is not
	move.b	#$75,d1 		 ; pause
	bra.s	kbdi_put

kbdi_odd
	clr.w	kb_intop(a3)		 ; special codes abandoned
kbdi_rts
	rts

kbdi_alt
	add.w	#kbdi.alt,d1		 ; set alternate set
kbdi_pbnorm
	add.w	kb_numl(a3),d1		 ; numlock table
	move.b	kbdi_table(pc,d1.w),d1	 ; set character
	bmi.s	kbdi_numl		 ; numlock key!!
kbdi_put
	clr.w	kb_intop(a3)		 ; special codes handled
	or.b	d0,d1			 ; add key up / down
	lea	kb_qu(a3),a2
	jmp	ioq_pbyt		 ; put in queue

kbdi_numl
	tst.b	d0			 ; up?
	bne.s	kbdi_rts		 ; ... yes
	eor.w	#kbdi.numl,kb_numl(a3)	 ; toggle numlock
	rts

;+++
; Set internal numlock status
;
;	d0 c  s Numlock status (0 = off, >0 = on)
;	a3 c  p Keyboard linkage
;---
kbd_setnumlock
	tst.b	d0
	beq.s	ksn_off
	clr.w	kb_numl(a3)		 ; 0 means "on" here
	rts
ksn_off
	move.w	#kbdi.numl,kb_numl(a3)
	rts

; Translate XT style scancodes into SMSQ/E internal scancodes (see KBD-Tab.doc)
kbdi_table
		; numlock
	dc.b	$00,$01,$02,$03,$04,$05,$06,$07   $00
	dc.b	$08,$09,$0a,$0b,$0c,$0d,$0e,$0f

	dc.b	$10,$11,$12,$13,$14,$15,$16,$17   $10
	dc.b	$18,$19,$1a,$1b,$1c,$1d,$1e,$1f

	dc.b	$20,$21,$22,$23,$24,$25,$26,$27   $20
	dc.b	$28,$29,$2a,$2b,$2c,$2d,$2e,$2f

	dc.b	$30,$31,$32,$33,$34,$35,$36,$37   $30
	dc.b	$38,$39,$3a,$3b,$3c,$3d,$3e,$3f

	dc.b	$40,$41,$42,$43,$44,$ff,$46,$47   $40
	dc.b	$48,$49,$4a,$4b,$4c,$4d,$4e,$4f

	dc.b	$50,$51,$52,$53,$54,$00,$56,$65   $50
	dc.b	$66,$00,$00,$00,$00,$00,$00,$00

		; numlock alt
	dc.b	$00,$01,$02,$03,$04,$05,$06,$07   $00
	dc.b	$08,$09,$0a,$0b,$0c,$0d,$0e,$0f

	dc.b	$10,$11,$12,$13,$14,$15,$16,$17   $10
	dc.b	$18,$19,$1a,$1b,$5c,$5d,$1e,$1f

	dc.b	$20,$21,$22,$23,$24,$25,$26,$27   $20
	dc.b	$28,$29,$00,$2b,$2c,$2d,$2e,$2f

	dc.b	$30,$31,$32,$33,$34,$55,$00,$37   $30
	dc.b	$58,$39,$00,$3b,$3c,$3d,$3e,$3f

	dc.b	$40,$41,$42,$43,$44,$ff,$75,$67   $40
	dc.b	$68,$69,$4a,$6b,$4c,$6d,$4e,$6f

	dc.b	$70,$71,$72,$73,$54,$00,$56,$65   $50
	dc.b	$66,$00,$00,$00,$00,$00,$00,$00

		; no numlock
	dc.b	$00,$01,$02,$03,$04,$05,$06,$07   $00
	dc.b	$08,$09,$0a,$0b,$0c,$0d,$0e,$0f

	dc.b	$10,$11,$12,$13,$14,$15,$16,$17   $10
	dc.b	$18,$19,$1a,$1b,$1c,$1d,$1e,$1f

	dc.b	$20,$21,$22,$23,$24,$25,$26,$27   $20
	dc.b	$28,$29,$2a,$2b,$2c,$2d,$2e,$2f

	dc.b	$30,$31,$32,$33,$34,$35,$36,$37   $30
	dc.b	$38,$39,$3a,$3b,$3c,$3d,$3e,$3f

	dc.b	$40,$41,$42,$43,$44,$ff,$46,$67   $40
	dc.b	$68,$69,$4a,$6b,$4c,$6d,$4e,$6f

	dc.b	$70,$71,$72,$73,$54,$00,$56,$65   $50
	dc.b	$66,$00,$00,$00,$00,$00,$00,$00

		; no numlock alt
	dc.b	$00,$01,$02,$03,$04,$05,$06,$07   $00
	dc.b	$08,$09,$0a,$0b,$0c,$0d,$0e,$0f

	dc.b	$10,$11,$12,$13,$14,$15,$16,$17   $10
	dc.b	$18,$19,$1a,$1b,$5c,$5d,$1e,$1f

	dc.b	$20,$21,$22,$23,$24,$25,$26,$27   $20
	dc.b	$28,$29,$00,$2b,$2c,$2d,$2e,$2f

	dc.b	$30,$31,$32,$33,$34,$55,$00,$37   $30
	dc.b	$58,$39,$00,$3b,$3c,$3d,$3e,$3f

	dc.b	$40,$41,$42,$43,$44,$ff,$75,$67   $40
	dc.b	$68,$69,$4a,$6b,$4c,$6d,$4e,$6f

	dc.b	$70,$71,$72,$73,$54,$00,$56,$65   $50
	dc.b	$66,$00,$00,$00,$00,$00,$00,$00

	end
