; SMSQ IBM  KBD AT102 key keyboard conversion   1998  Tony Tebby

	section kbdint

	xdef	kbd_at102cvt

	xref	ioq_pbyt

	include 'dev8_smsq_kbd_keys'

;+++
;	d0 c	key code (in a word)
;	d1   s
;	a2   s
;	a3 c  p keybard linkage
;---
kbd_at102cvt
	move.w	kb_intop(a3),d1
	jmp	kbdi_act(pc,d1.w)
kbdi_act
kbdi_normal
	cmp.b	#$e0,d0 		 ; escape code?
	blo.l	kbdi_pbnorm		 ; ... no, normal character (d1 = 0)
	beq.s	kbdi_setalt		 ; ... yes, modified character
	cmp.b	#$f0,d0 		 ; key up?
	beq.s	kbdi_setup		 ; ... yes
	cmp.b	#$e1,d0 		 ; special?
	bne.s	kbdi_rts		 ; ... no, forget it

	move.w	#kbdi_spec-kbdi_act,kb_intop(a3) ; set special action
kbdi_rts
	rts

kbdi_setalt
	move.w	#kbdi_alt-kbdi_act,kb_intop(a3) ; set alternate action
	rts

kbdi_setup
	move.w	#kbdi_up-kbdi_act,kb_intop(a3) ; set up action
	rts

kbdi_spec
	cmp.b	#$F0,d0 		 ; is it key up?
	bne.s	kbdi_spec14d		 ; ... no
	move.w	#kbdi_spec14u-kbdi_act,kb_intop(a3) ; ... yes, set up action
	rts

kbdi_spec14d
	cmp.b	#$14,d0 		 ; only special sequence $e1,$14,$77
	bne.s	kbdi_odd		 ; it is not
	move.w	#kbdi_spec77d-kbdi_act,kb_intop(a3) ; ... yes, set up action
	rts

kbdi_spec77d
	cmp.b	#$77,d0 		 ; only special sequence $e1,$14,$77
	bne.s	kbdi_odd		 ; it is not
	move.b	#$75,d1 		 ; pause down
	bra.s	kbdi_put

kbdi_spec14u
	cmp.b	#$14,d0 		 ; only special sequence $e1,$f0,$14,$f0,$77
	bne.s	kbdi_odd		 ; it is not
	move.w	#kbdi_spec77p-kbdi_act,kb_intop(a3) ; ... yes, set up action
	rts

kbdi_spec77p
	cmp.b	#$f0,d0 		 ; only special sequence $e1,$f0,$14,$f0,$77
	bne.s	kbdi_odd		 ; it is not
	move.w	#kbdi_spec77u-kbdi_act,kb_intop(a3) ; ... yes, set up action
	rts

kbdi_spec77u
	cmp.b	#$77,d0 		 ; only special sequence $e1,$f0,$14,$f0,$77
	bne.s	kbdi_odd		 ; it is not
	move.b	#$75+$80,d1		 ; pause up
	bra.s	kbdi_put

kbdi_odd
	clr.w	kb_intop(a3)		 ; special codes abandoned
	rts

kbdi_alt
	moveq	#0,d1
	add.w	#kbdi_atable-kbdi_table,d0 ; set alternate set
	cmp.w	#kbdi_atable-kbdi_table+$f0,d0 ; is it key up?
	bne.s	kbdi_pbnorm		 ; ... no
	move.w	#kbdi_altup-kbdi_act,kb_intop(a3) ; ... yes, set up action
	rts

kbdi_altup
	add.w	#kbdi_atable-kbdi_table,d0 ; set alternate set
kbdi_up
	move.w	#$80,d1 		 ; set key up
kbdi_pbnorm
	or.b	kbdi_table(pc,d0.w),d1	 ; set character (and key up)
kbdi_put
	clr.w	kb_intop(a3)		 ; special codes handled
	lea	kb_qu(a3),a2
	jmp	ioq_pbyt		 ; put in queue


kbdi_table
	dc.b	$00,$43,$00,$3f,$3d,$3b,$3c,$66   $00
	dc.b	$00,$44,$42,$40,$3e,$0f,$29,$00
	dc.b	$00,$38,$2a,$00,$1d,$10,$02,$00   $10
	dc.b	$00,$00,$2c,$1f,$1e,$11,$03,$00

	dc.b	$00,$2e,$2d,$20,$12,$05,$04,$00   $20
	dc.b	$00,$39,$2f,$21,$14,$13,$06,$00
	dc.b	$00,$31,$30,$23,$22,$15,$07,$00   $30
	dc.b	$00,$00,$32,$24,$16,$08,$09,$00

	dc.b	$00,$33,$25,$17,$18,$0b,$0a,$00   $40
	dc.b	$00,$34,$35,$26,$27,$19,$0c,$00
	dc.b	$00,$00,$28,$00,$1a,$0d,$00,$00   $50
	dc.b	$3a,$36,$1c,$1b,$00,$2b,$00,$00

	dc.b	$00,$56,$00,$00,$00,$00,$0e,$00   $60
	dc.b	$00,$4f,$00,$4b,$47,$00,$00,$00
	dc.b	$52,$53,$50,$4c,$4d,$48,$01,$45   $70
	dc.b	$65,$4e,$51,$4a,$37,$49,$46,$00

	dc.b	$00,$00,$00,$41,$00,$00,$00,$00   $80

kbdi_atable
	dc.b	$00,$00,$00,$00,$00,$00,$00,$00   $00
	dc.b	$00,$00,$00,$00,$00,$00,$00,$00
	dc.b	$00,$58,$00,$00,$5d,$00,$00,$00   $10
	dc.b	$00,$00,$00,$00,$00,$00,$00,$00

	dc.b	$00,$00,$00,$00,$00,$00,$00,$00   $20
	dc.b	$00,$00,$00,$00,$00,$00,$00,$00
	dc.b	$00,$00,$00,$00,$00,$00,$00,$00   $30
	dc.b	$00,$00,$00,$00,$00,$00,$00,$00

	dc.b	$00,$00,$00,$00,$00,$00,$00,$00   $40
	dc.b	$00,$00,$55,$00,$00,$00,$00,$00
	dc.b	$00,$00,$00,$00,$00,$00,$00,$00   $50
	dc.b	$00,$00,$5c,$00,$00,$56,$00,$00

	dc.b	$00,$00,$00,$00,$00,$00,$00,$00   $60
	dc.b	$00,$6f,$00,$6b,$67,$00,$00,$00
	dc.b	$72,$73,$70,$00,$6d,$68,$00,$00   $70
	dc.b	$65,$00,$71,$00,$54,$69,$75,$00

	end
