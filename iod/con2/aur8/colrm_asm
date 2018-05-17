;	Set colour masks / palettes Aurora version V1.01   1998  Tony Tebby
;							    2002  Marcel Kilgus
;
; 2013-05-16  1.01  Fixed striped colours in COLOUR_NATIVE mode
;
;	Registers:
;		Entry				Exit
;	D0					0
;	D1/D2					preserved
;	D3	Colour number			preserved
;	D4					(QL colour number if set)
;	D5					preserved
;	D6	Stipple pattern 		Stipple pattern
;	D7					Colour mask
;	A0	CDB				preserved
;
;	All other registers preserved

	section con

	include 'dev8_keys_con'
	include 'dev8_keys_sys'
	include 'dev8_keys_err'
	include 'dev8_mac_assert'

	xdef	cn_palset
	xdef	cn_palq
	xdef	cn_palt

	xdef	cn_ql_colr
	xdef	cn_ql_bcolr
	xdef	cn_pal_mcolr
	xdef	cn_pal_ccolr
	xdef	cn_pal_bmcolr
	xdef	cn_pal_bccolr
	xdef	cn_nat_mcolr
	xdef	cn_nat_ccolr
	xdef	cn_nat_bmcolr
	xdef	cn_nat_bccolr
	xdef	cn_24b_mcolr
	xdef	cn_24b_ccolr
	xdef	cn_24b_bmcolr
	xdef	cn_24b_bccolr

	xdef	cn_bcnat

	xdef	cn_crcol
	xdef	cn.inkcl

	xdef	rgb2aur_tab

	xref	aur256_palette
	xref	aur256_palend

cn.inkcl equ	4			; ink colour default
cn_crcol dc.l	$49494949		; cursor xor mask

;+++
; pre  set both palettes
;
;	a4 c  p pointer to QL palette
;	a5 c  p pointer to 256 palette
;---
cn_palset
	movem.l a0/a4,-(sp)
	lea	aur256_palette,a0
	move.w	#aur256_palend-aur256_palette,d0
cnpal_loop
	move.l	(a0)+,(a4)+
	subq.w	#4,d0
	bgt.s	cnpal_loop

	movem.l (sp)+,a0/a4
	moveq	#0,d0
	rts

; set QL palette

cn_palq
	moveq	#8,d0			; max 8
	move.l	pt_palql(a3),a2
	bra.s	cnp_spal

; set standard palette

cn_palt
	move.w	#$100,d0		; max 256
	move.l	pt_pal256(a3),a2

cnp_spal
	add.w	d1,a2			; offset
	cmp.w	d0,d1			; start in range?
	bhs.s	cnp_orng
	add.w	d2,d1
	cmp.w	d0,d1			; end in range?
	bhi.s	cnp_orng
	bra.s	cnp_eloop
cnp_loop
	move.l	(a1)+,d3		; 24 bit colour
	bsr.l	cnc_s24b
	move.b	d3,(a2)+
cnp_eloop
	dbra	d2,cnp_loop

	moveq	#0,d0
	rts

cnp_orng
	moveq	#err.orng,d0	    rts


;---------------------------------------------------------------------------
; border colour (2 byte) to long word mask
cn_bcnat
	move.w	sd_bcolw(a0),d7 	; main/contrast colour
	swap	d7
	move.w	sd_bcolw(a0),d7

	move.b	sd_bcolr(a0),d6
	moveq	#%00111000,d0
	and.b	d6,d0			; any contrast?
	beq.s	cnc_nstip
	lsr.w	#6,d6			; set stipple

	moveq	#0,d0
	rts

cnc_nstip
	moveq	#-1,d6
	moveq	#0,d0
	rts

; QL colour
cn_ql_colr
	moveq	#0,d6
	move.b	d3,d6
	bsr.s	cnc_sql 		; ql colour is converted to 2*8 bit
	move.w	d3,d7			; set main and contrast colour
	swap	d7
	move.w	d3,d7			; doubled

	move.b	d6,d3
	lsr.b	#3,d3
	moveq	#%111,d0
	and.b	d3,d0			; colour difference
	beq.s	cnc_nstip
	eor.b	d6,d3			; contrast colour
	lsr.b	#6,d6
	bsr.s	cnc_sql 		; ql colour is converted to 2*8 bit
	move.b	d3,d7			; set contrast colour
	swap	d7
	move.b	d3,d7
	moveq	#0,d0
	rts

; QL colour to 2*8 bit
cnc_sql
	movem.l d0/a2,-(sp)
	move.l	pt_palql(a3),a2
	and.w	#%111,d3		; look up ql colour 0-7
	move.w	d3,d0
	move.b	(a2,d0.w),d3
	lsl.w	#8,d3
	move.b	(a2,d0.w),d3
	movem.l (sp)+,d0/a2
	rts

; palette main colour
cn_pal_mcolr
	bsr.s	cnc_spal		; palette map is converted to 8 bit colour
	bra.l	cn_nat_mcolr

; palette contrast colour
cn_pal_ccolr
	bsr.s	cnc_spal
	bra.l	cn_nat_ccolr

; PAL colour to 2*8 bit
cnc_spal
	move.l	a2,-(sp)
	and.w	#$00ff,d3		; look up palette colour 0-255
	move.l	pt_pal256(a3),a2
	move.w	d3,d0
	move.b	(a2,d0.w),d3
	lsl.w	#8,d3
	move.b	(a2,d0.w),d3
	move.l	(sp)+,a2
	rts

; true main colour
cn_24b_mcolr
	bsr.s	cnc_s24b		; 24 bit colour is converted to Aurora colour
	bra.s	cn_nat_mcolr

; true contrast colour
cn_24b_ccolr
	bsr.s	cnc_s24b		; 24 bit colour is converted to Aurora colour
	bra	cn_nat_ccolr

; true to 2*8 bit
cnc_s24b
	lsr.l	#8,d3
	lsr.b	#5,d3			; convert values to 3 bit
b_ok	ror.l	#8,d3
	lsr.b	#5,d3
g_ok	ror.l	#8,d3
	lsr.b	#5,d3
	swap	d3
	lsl.b	#5,d3
	lsl.w	#5,d3
	moveq	#10,d0
	lsr.l	d0,d3
	move.w	d3,d0
	move.l	a2,-(sp)
	lea	rgb2aur_tab(pc),a2
	move.b	(a2,d0.w),d3
	lsl.w	#8,d3
	move.b	(a2,d0.w),d3
	move.l	(sp)+,a2
	rts

; QL border colour
cn_ql_bcolr
	moveq	#%00111000,d0
	and.b	d3,d0
	beq.s	cn_ql_bcsolid		; solid border colour

	move.b	d3,d0
	lsr.b	#3,d0
	eor.b	d3,d0			; contrast colour
	bsr	cnc_sql 		; main colour
	move.w	d3,d7
	move.b	d0,d3
	bsr	cnc_sql 		; contrast colour
	bra.s	cn_nat_bccolr		; set both colours

cn_ql_bcsolid
	bsr	cnc_sql 		; set colour
	move.w	d3,d7
	moveq	#0,d0
	rts

; PAL main border colour
cn_pal_bmcolr
	bsr.s	cnc_spal		; convert border colour to 2*8 bit
	bra.s	cn_nat_bmcolr
cn_24b_bmcolr
	bsr.s	cnc_s24b		; convert border colour to 2*8 bit

cn_nat_bmcolr
	move.w	d3,d7			; for solid colour definition
	moveq	#0,d0
	rts

; PAL contrast border colour
cn_pal_bccolr
	bsr	cnc_spal		; convert border colour to QL
	bra.s	cn_nat_bccolr
cn_24b_bccolr
	bsr.s	cnc_s24b		; convert border colour to QL

cn_nat_bccolr
	move.b	d3,d7			; the two colours combined
	moveq	#0,d0
	rts

; native main colour
cn_nat_mcolr
	move.b	d3,d7			 ; main
	lsl.w	#8,d7
	move.b	d3,d7			 ; contrast is same
	move.w	d7,d0
	swap	d7
	move.w	d0,d7			 ; doubled
	moveq	#0,d0
	rts

; native contrast colour
cn_nat_ccolr
	move.b	d3,d7			 ; set contrast colour
	swap	d7
	move.b	d3,d7			 ; doubled
cnc_ok
	moveq	#0,d0
	rts

; 3*3 bit RGB to aurora colour
rgb2aur_tab
	dc.b	$00,$00,$04,$05,$20,$21,$24,$25,$02,$02,$06,$07,$22,$23,$26,$27
	dc.b	$10,$10,$14,$15,$30,$31,$34,$35,$12,$12,$16,$17,$32,$33,$36,$37
	dc.b	$80,$80,$84,$85,$A0,$A1,$A4,$A5,$82,$82,$86,$87,$A2,$A3,$A6,$A7
	dc.b	$90,$90,$94,$95,$B0,$B1,$B4,$B5,$92,$92,$96,$97,$B2,$B3,$B6,$B7
	dc.b	$01,$01,$04,$05,$20,$21,$24,$25,$03,$03,$06,$07,$22,$23,$26,$27
	dc.b	$11,$11,$14,$15,$30,$31,$34,$35,$13,$13,$16,$17,$32,$33,$36,$37
	dc.b	$81,$81,$84,$85,$A0,$A1,$A4,$A5,$83,$83,$86,$87,$A2,$A3,$A6,$A7
	dc.b	$91,$91,$94,$95,$B0,$B1,$B4,$B5,$93,$93,$96,$97,$B2,$B3,$B6,$B7
	dc.b	$08,$08,$0C,$05,$28,$21,$2C,$25,$0A,$0A,$0E,$07,$2A,$23,$2E,$27
	dc.b	$18,$18,$1C,$15,$38,$31,$3C,$35,$1A,$1A,$1E,$17,$3A,$33,$3E,$37
	dc.b	$88,$88,$8C,$85,$A8,$A1,$AC,$A5,$8A,$8A,$8E,$87,$AA,$A3,$AE,$A7
	dc.b	$98,$98,$9C,$95,$B8,$B1,$BC,$B5,$9A,$9A,$9E,$97,$BA,$B3,$BE,$B7
	dc.b	$09,$09,$09,$0D,$28,$29,$2C,$2D,$0B,$0B,$0B,$0F,$2A,$2B,$2E,$2F
	dc.b	$19,$19,$19,$1D,$38,$39,$3C,$3D,$1B,$1B,$1B,$1F,$3A,$3B,$3E,$3F
	dc.b	$89,$89,$89,$8D,$A8,$A9,$AC,$AD,$8B,$8B,$8B,$8F,$AA,$AB,$AE,$AF
	dc.b	$99,$99,$99,$9D,$B8,$B9,$BC,$BD,$9B,$9B,$9B,$9F,$BA,$BB,$BE,$BF
	dc.b	$40,$40,$44,$44,$60,$29,$64,$2D,$42,$42,$46,$46,$62,$2B,$66,$2F
	dc.b	$50,$50,$54,$54,$70,$39,$74,$3D,$52,$52,$56,$56,$72,$3B,$76,$3F
	dc.b	$C0,$C0,$C4,$C4,$E0,$A9,$E4,$AD,$C2,$C2,$C6,$C6,$E2,$AB,$E6,$AF
	dc.b	$D0,$D0,$D4,$D4,$F0,$B9,$F4,$BD,$D2,$D2,$D6,$D6,$F2,$BB,$F6,$BF
	dc.b	$41,$41,$41,$45,$45,$61,$64,$65,$43,$43,$43,$47,$47,$63,$66,$67
	dc.b	$51,$51,$51,$55,$55,$71,$74,$75,$53,$53,$53,$57,$57,$73,$76,$77
	dc.b	$C1,$C1,$C1,$C5,$C5,$E1,$E4,$E5,$C3,$C3,$C3,$C7,$C7,$E3,$E6,$E7
	dc.b	$D1,$D1,$D1,$D5,$D5,$F1,$F4,$F5,$D3,$D3,$D3,$D7,$D7,$F3,$F6,$F7
	dc.b	$48,$48,$4C,$4C,$68,$68,$6C,$65,$4A,$4A,$4E,$4E,$6A,$6A,$6E,$67
	dc.b	$58,$58,$5C,$5C,$78,$78,$7C,$75,$5A,$5A,$5E,$5E,$7A,$7A,$7E,$77
	dc.b	$C8,$C8,$CC,$CC,$E8,$E8,$EC,$E5,$CA,$CA,$CE,$CE,$EA,$EA,$EE,$E7
	dc.b	$D8,$D8,$DC,$DC,$F8,$F8,$FC,$F5,$DA,$DA,$DE,$DE,$FA,$FA,$FE,$F7
	dc.b	$49,$49,$49,$4D,$4D,$69,$69,$6D,$4B,$4B,$4B,$4F,$4F,$6B,$6B,$6F
	dc.b	$59,$59,$59,$5D,$5D,$79,$79,$7D,$5B,$5B,$5B,$5F,$5F,$7B,$7B,$7F
	dc.b	$C9,$C9,$C9,$CD,$CD,$E9,$E9,$ED,$CB,$CB,$CB,$CF,$CF,$EB,$EB,$EF
	dc.b	$D9,$D9,$D9,$DD,$DD,$F9,$F9,$FD,$DB,$DB,$DB,$DF,$DF,$FB,$FB,$FF

	end
