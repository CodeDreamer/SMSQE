; Lower case character table/conversion       V2.01    1986  Tony Tebby   QJUMP

	section cv

	xdef	cv_locas
	xdef	cv_lctab

;+++
; Convert character to lower case
;
;	Registers:
;		Entry				Exit
;	D1.w	character (MSB ignored) 	lower-case equivalent
;---
cv_locas
	and.w	#$00ff,d1		; ensure upper byte clear
	move.b	cv_lctab(pc,d1.w),d1	; and lowercase the character
	rts
;+++
; 256-byte table of lower-case equivalents for characters.
;---
cv_lctab
	dc.b	$00,$01,$02,$03,$04,$05,$06,$07
	dc.b	$08,$09,$0A,$0B,$0C,$0D,$0E,$0F
	dc.b	$10,$11,$12,$13,$14,$15,$16,$17
	dc.b	$18,$19,$1A,$1B,$1C,$1D,$1E,$1F

	dc.b	$20,$21,$22,$23,$24,$25,$26,$27
	dc.b	$28,$29,$2A,$2B,$2C,$2D,$2E,$2F
	dc.b	$30,$31,$32,$33,$34,$35,$36,$37
	dc.b	$38,$39,$3A,$3B,$3C,$3D,$3E,$3F

	dc.b	$40,$61,$62,$63,$64,$65,$66,$67
	dc.b	$68,$69,$6A,$6B,$6C,$6D,$6E,$6F
	dc.b	$70,$71,$72,$73,$74,$75,$76,$77
	dc.b	$78,$79,$7A,$5B,$5C,$5D,$5E,$5F

	dc.b	$60,$61,$62,$63,$64,$65,$66,$67
	dc.b	$68,$69,$6A,$6B,$6C,$6D,$6E,$6F
	dc.b	$70,$71,$72,$73,$74,$75,$76,$77
	dc.b	$78,$79,$7A,$7B,$7C,$7D,$7E,$7F

	dc.b	$80,$81,$82,$83,$84,$85,$86,$87
	dc.b	$88,$89,$8A,$8B,$8C,$8D,$8E,$8F
	dc.b	$90,$91,$92,$93,$94,$95,$96,$97
	dc.b	$98,$99,$9A,$9B,$9C,$9D,$9E,$9F

	dc.b	$80,$81,$82,$83,$84,$85,$86,$87
	dc.b	$88,$89,$8A,$8B,$AC,$AD,$AE,$AF
	dc.b	$B0,$B1,$B2,$B3,$B4,$B5,$B6,$B7
	dc.b	$B8,$B9,$BA,$BB,$BC,$BD,$BE,$BF

	dc.b	$C0,$C1,$C2,$C3,$C4,$C5,$C6,$C7
	dc.b	$C8,$C9,$CA,$CB,$CC,$CD,$CE,$CF
	dc.b	$D0,$D1,$D2,$D3,$D4,$D5,$D6,$D7
	dc.b	$D8,$D9,$DA,$DB,$DC,$DD,$DE,$DF

	dc.b	$E0,$E1,$E2,$E3,$E4,$E5,$E6,$E7
	dc.b	$E8,$E9,$EA,$EB,$EC,$ED,$EE,$EF
	dc.b	$F0,$F1,$F2,$F3,$F4,$F5,$F6,$F7
	dc.b	$F8,$F9,$FA,$FB,$FC,$FD,$FE,$FF
	end
