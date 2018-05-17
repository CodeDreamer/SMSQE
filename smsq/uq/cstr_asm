; Compare strings, QL compatible    1990  Tony Tebby

	section uq

	xdef	uq_cstr
	xdef	uq_cstra
	xdef	uq_cstrl
	xdef	uq_otab
	xdef	uq_otabn

ucsl_entry
	exg	a2,a3			 ; end of a0 string in a3
	move.l	a3,d5
	sub.l	a0,d5			 ; length of a0 string
	sub.l	a2,d5
	add.l	a1,d5			 ; a1 string shorter?
	ble.s	ucsl_set		 ; ... no
	sub.w	d5,a3			 ; ... yes, compare up to here

ucsl_set
	add.l	a6,a0
	add.l	a6,a1
	add.l	a6,a3			 ; absolute addresses

	lea	uq_otab,a2		 ; include case
	tst.l	d4
	bpl.s	ucs_value		 ; ... yes
	lea	uq_otabn,a2		 ; ... no case
	bra.s	ucs_value

;+++
; Liberator entry at ut.cstr-6
; Compares two strings (a0-a2,a6.l) and (a1-a3,a6.l)
;
;	d0  r			 returned -1 (a0)<(a1)
;					   0 (a0)=(a1)
;					   1 (a0)>(a1)
;	d4 c  p comparison type  +ve=compare values of embedded numbers
;				 -ve=as above ignoring case
;
;	a0 c  p pointer to string characters
;	a1 c  p pointer to string characters
;	a2 c  p pointer to end (a0,a6)
;	a3 c  p pointer to end (a1,a6)
;
;	status return zero
;---
uq_cstrl
ucs.reg reg	d1/d2/d3/d4/d5/a0/a1/a2/a3
	movem.l ucs.reg,-(sp)
	bra.s	ucsl_entry

;+++
; Compares two strings (a0,a6.l) and (a1,a6.l)
;
;	d0 cr	comparison type  0=straight
;				 1=ignore case
;				 2=compare values of embedded numbers
;				 3=both above
;				 returned -1 (a0)<(a1)
;					   0 (a0)=(a1)
;					   1 (a0)>(a1)
;	a0 c  p pointer to string (rel a6)
;	a1 c  p pointer to string (rel a6)
;	a6 c  p base register
;
;	status return zero
;---
uq_cstr
	movem.l ucs.reg,-(sp)
	add.l	a6,a0
	add.l	a6,a1
	bra.s	ucs_setup

;+++
; Compares two strings (a0) and (a1)
;
;	d0 cr	comparison type  0=straight
;				 1=ignore case
;				 2=compare values of embedded numbers
;				 3=both above
;				 returned -1 (a0)<(a1)
;					   0 (a0)=(a1)
;					   1 (a0)>(a1)
;	a0 c  p pointer to string (abs)
;	a1 c  p pointer to string (abs)
;
;	status return zero
;---
uq_cstra
	movem.l ucs.reg,-(sp)

ucs_setup
	lea	uq_otab,a2		 ; include case
	lsr.b	#1,d0
	bcc.s	ucs_saddr		 ; ... yes
	lea	uq_otabn,a2		 ; ... no case

ucs_saddr
	move.w	(a0)+,d5		 ; length of a0 string
	lea	(a0,d5.w),a3		 ; end of a0 string
	sub.w	(a1)+,d5		 ; a1 string shorter?
	ble.s	ucs_which		 ; ... no
	sub.w	d5,a3			 ; ... yes, compare up to here

ucs_which
	lsr.b	#1,d0
	bcs.s	ucs_value		 ; compare using values

	moveq	#0,d0			 ; make name 0 word OK
	moveq	#0,d1			 ; make name 1 word OK
	moveq	#0,d2			 ; make numbers the same

ucs_cmpl
	cmp.l	a3,a0			 ; any more?
	bge.l	ucs_end0		 ; ... end of name 0

	move.b	(a0)+,d1
	move.b	0(a2,d1.w),d0		 ; get order char from name 0
	move.b	(a1)+,d1
	cmp.b	0(a2,d1.w),d0		 ; is it same as name 1?
	beq.s	ucs_cmpl		 ; ... yes, carry on
	bra.s	ucs_ucmp		 ; ... no, unsigned compare

ucs_value
	moveq	#0,d0			 ; make name 0 word OK
	moveq	#0,d1			 ; make name 1 word OK
	moveq	#0,d2			 ; make numbers the same
	moveq	#'0',d3
	moveq	#'9',d4

ucs_cmpv
	cmp.l	a3,a0			 ; any more?
	bge.s	ucs_end0		 ; ... end of name 0

	move.b	(a0)+,d1
	cmp.b	d4,d1
	bhi.s	ucs_cmpc
	cmp.b	d3,d1
	bhs.s	ucs_dig0
ucs_cmpc
	move.b	0(a2,d1.w),d0		 ; get order char from name 0
	move.b	(a1)+,d1
	cmp.b	0(a2,d1.w),d0		 ; is it same as name 1?
	beq.s	ucs_cmpv		 ; ... yes, carry on
	bra.s	ucs_ucmp		 ; ... no, compare directly

ucs_dig0
	move.b	d1,d0			 ; digit value
	move.b	(a1)+,d1		 ; what about name 1
	cmp.b	d4,d1
	bhi.s	ucs_digc
	cmp.b	d3,d1
	bhs.s	ucs_dig1		 ; it's a digit too
ucs_digc
	move.b	0(a2,d0.w),d0		 ; get order char from name 0
	cmp.b	0(a2,d1.w),d0		 ; is it same as name 1?
	bra.s	ucs_ucmp

ucs_ndig
	cmp.l	a3,a0			 ; end of string
	bge.s	ucs_end0
	move.b	(a0)+,d0		 ; next digit name 0
	cmp.b	d4,d0
	bhi.s	ucs_endd
	cmp.b	d3,d0
	blo.s	ucs_endd		 ; not a digit
	move.b	(a1)+,d1		 ; name 0 was digit, what about name 1?
	cmp.b	d4,d1
	bhi.s	ucs_expl		 ; ... not digit, name 0 is greater
	cmp.b	d3,d1
	blo.s	ucs_expl		 ; ... not digit, name 0 is greater
ucs_dig1
	tst.b	d2			 ; number order set?
	bne.s	ucs_ndig		 ; ... yes, look for next digit
	move.b	d0,d2
	sub.b	d1,d2			 ; ... no, set number order
	bra.s	ucs_ndig

ucs_endd
	move.b	(a1),d1 		 ; end if digits in name 0
	cmp.b	d4,d1			 ; look at name 1
	bhi.s	ucs_ckd2		 ; ... not digit, check for greater
	cmp.b	d3,d1
	bhs.s	ucs_exmi		 ; ... is digit, name 1 is greater
ucs_ckd2
	move.b	d2,d0			 ; which is greater?
	bne.s	ucs_scmp		 ; ... one or the other
	subq.l	#1,a0			 ; backspace name 0
	bra.s	ucs_cmpv		 ; numbers the same, carry on checking

ucs_end0
	tst.w	d5			 ; end of string 0 is 1 same length?
	bne.s	ucs_scmp
	move.b	d2,d0

ucs_scmp
	blt.s	ucs_exmi		 ; unsigned comparison
	bgt.s	ucs_expl
	bra.s	ucs_exz

ucs_ucmp
	blo.s	ucs_exmi		 ; unsigned comparison
ucs_expl
	moveq	#1,d0			 ; name0 > name 1
ucs_exz
	movem.l (sp)+,ucs.reg
	rts
ucs_exmi
	moveq	#-1,d0			 ; name0 < name 1
	movem.l (sp)+,ucs.reg
	rts

uq_otab
	dc.b	$00,$01,$02,$03,$04,$05,$06,$07,$08,$09,$0A,$0B,$0C,$0D,$0E,$0F
	dc.b	$10,$11,$12,$13,$14,$15,$16,$17,$18,$19,$1A,$1B,$1C,$1D,$1E,$1F
	dc.b	$60,$61,$62,$63,$64,$65,$66,$67,$68,$69,$6A,$6B,$6C,$6D,$6E,$6F
	dc.b	$92,$93,$94,$95,$96,$97,$98,$99,$9A,$9B,$70,$71,$72,$73,$74,$75

	dc.b	$76,$9C,$A5,$A7,$AB,$AD,$B4,$B6,$B8,$BA,$C0,$C2,$C4,$C6,$C8,$CC
	dc.b	$D5,$D7,$D9,$DB,$DE,$E0,$E7,$E9,$EB,$ED,$EF,$77,$78,$79,$7A,$7B
	dc.b	$7C,$9D,$A6,$A8,$AC,$AE,$B5,$B7,$B9,$BB,$C1,$C3,$C5,$C7,$C9,$CD
	dc.b	$D6,$D8,$DA,$DC,$DF,$E1,$E8,$EA,$EC,$EE,$F0,$7D,$7E,$7F,$80,$81

	dc.b	$9F,$A4,$F6,$B1,$CF,$D4,$F4,$E3,$AA,$CB,$F2,$F8,$A0,$A1,$A2,$AF
	dc.b	$B2,$B3,$BC,$BD,$BE,$BF,$D0,$D1,$D2,$E4,$E5,$E6,$DD,$82,$83,$84
	dc.b	$9E,$A3,$F5,$B0,$CE,$D3,$F3,$E2,$A9,$CA,$F1,$F7,$F9,$FA,$FB,$FC
	dc.b	$FD,$FE,$FF,$85,$86,$87,$88,$89,$8A,$8B,$8C,$8D,$8E,$8F,$90,$91

	dc.b	$20,$21,$22,$23,$24,$25,$26,$27,$28,$29,$2A,$2B,$2C,$2D,$2E,$2F
	dc.b	$30,$31,$32,$33,$34,$35,$36,$37,$38,$39,$3A,$3B,$3C,$3D,$3E,$3F
	dc.b	$40,$41,$42,$43,$44,$45,$46,$47,$48,$49,$4A,$4B,$4C,$4D,$4E,$4F
	dc.b	$50,$51,$52,$53,$54,$55,$56,$57,$58,$59,$5A,$5B,$5C,$5D,$5E,$5F

uq_otabn
	dc.b	$00,$01,$02,$03,$04,$05,$06,$07,$08,$09,$0A,$0B,$0C,$0D,$0E,$0F
	dc.b	$10,$11,$12,$13,$14,$15,$16,$17,$18,$19,$1A,$1B,$1C,$1D,$1E,$1F
	dc.b	$60,$61,$62,$63,$64,$65,$66,$67,$68,$69,$6A,$6B,$6C,$6D,$6E,$6F
	dc.b	$92,$93,$94,$95,$96,$97,$98,$99,$9A,$9B,$70,$71,$72,$73,$74,$75

	dc.b	$76,$9D,$A6,$A8,$AC,$AE,$B5,$B7,$B9,$BB,$C1,$C3,$C5,$C7,$C9,$CD
	dc.b	$D6,$D8,$DA,$DC,$DF,$E1,$E8,$EA,$EC,$EE,$F0,$77,$78,$79,$7A,$7B
	dc.b	$7C,$9D,$A6,$A8,$AC,$AE,$B5,$B7,$B9,$BB,$C1,$C3,$C5,$C7,$C9,$CD
	dc.b	$D6,$D8,$DA,$DC,$DF,$E1,$E8,$EA,$EC,$EE,$F0,$7D,$7E,$7F,$80,$81

	dc.b	$9F,$A4,$F6,$B1,$CF,$D4,$F4,$E3,$AA,$CB,$F2,$F8,$A0,$A1,$A2,$AF
	dc.b	$B2,$B3,$BC,$BD,$BE,$BF,$D0,$D1,$D2,$E4,$E5,$E6,$DD,$82,$83,$84
	dc.b	$9F,$A4,$F6,$B1,$CF,$D4,$F4,$E3,$AA,$CB,$F2,$F8,$F9,$FA,$FB,$FC
	dc.b	$FD,$FE,$FF,$85,$86,$87,$88,$89,$8A,$8B,$8C,$8D,$8E,$8F,$90,$91

	dc.b	$20,$21,$22,$23,$24,$25,$26,$27,$28,$29,$2A,$2B,$2C,$2D,$2E,$2F
	dc.b	$30,$31,$32,$33,$34,$35,$36,$37,$38,$39,$3A,$3B,$3C,$3D,$3E,$3F
	dc.b	$40,$41,$42,$43,$44,$45,$46,$47,$48,$49,$4A,$4B,$4C,$4D,$4E,$4F
	dc.b	$50,$51,$52,$53,$54,$55,$56,$57,$58,$59,$5A,$5B,$5C,$5D,$5E,$5F
	end
