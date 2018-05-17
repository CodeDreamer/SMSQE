;	Set colour masks / palettes    V2.00   1998  Tony Tebby
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

	xref	qpc16_palette
	xref	qpc16_palend

cn.inkcl equ	4
cn_crcol dc.l	$00f800f8

;+++
; pre  set both palettes
;
;	a4 c  p pointer to QL palette
;	a5 c  p pointer to 256 palette
;---
cn_palset
	movem.l a0/a4,-(sp)
	lea	qpc16_palette,a0
	move.w	#qpc16_palend-qpc16_palette,d0
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
	add.w	d1,a2
	cmp.w	d0,d1			; start in range?
	bhs.s	cnp_orng
	add.w	d2,d1
	cmp.w	d0,d1			; end in range?
	bhi.s	cnp_orng
	bra.s	cnp_eloop
cnp_loop
	move.l	(a1)+,d3		; 24 bit colour
	bsr.l	cnc_s24b
	move.w	d3,(a2)+
cnp_eloop
	dbra	d2,cnp_loop

	moveq	#0,d0
	rts

cnp_orng
	moveq	#err.orng,d0
	rts


;---------------------------------------------------------------------------
cn_bcnat			 ; border colour (2 byte) to long word mask
	move.b	sd_bcolr(a0),d6
	moveq	#%00111000,d0
	and.b	d6,d0			; any contrast?
	beq.s	cnc_bcnstip
	lsr.w	#6,d6			; set stipple

	moveq	#0,d7
	move.b	sd_bcolw(a0),d7 	; main colour
	bsr.s	cnc_bcset
	swap	d7
	move.b	sd_bcolw+1(a0),d7	; contrast colour
cnc_bcset
	ror.w	#2,d7			; bb...... ..gggrrr
	ror.b	#3,d7			; bb...... rrr..ggg
	rol.w	#8,d7			; rrr..ggg bb......
	lsr.b	#3,d7			; RRR..GGG ...BB...
	move.w	d7,d0
	and.w	#%1100011100011000,d0	; rr...ggg ...bb...
	lsl.b	#1,d0			; rr...ggg ..bb....
	or.b	d7,d0			; rr...ggg ..bbb...
	lsr.w	#3,d0			; ...rr... ggg..bbb
	or.w	d0,d7			; RRRrrGGG gggBBbbb
	ror.w	#8,d7			; ... swap bytes
	moveq	#0,d0
	rts

cnc_bcnstip
	move.w	sd_bcolw(a0),d7
	swap	d7
	move.w	sd_bcolw(a0),d7
cnc_nstip
	moveq	#-1,d6
	moveq	#0,d0
	rts

cn_ql_colr
	moveq	#0,d6
	move.b	d3,d6
	bsr.s	cnc_sql 		; ql colour is converted to 16 bit
	move.w	d3,d7			; set main colour
	swap	d7
	move.w	d3,d7			; and contrast

	move.b	d6,d3
	lsr.b	#3,d3
	moveq	#%111,d0
	and.b	d3,d0			; colour difference
	beq.s	cnc_nstip
	eor.b	d6,d3			; contrast colour
	lsr.b	#6,d6
	bsr.s	cnc_sql 		; ql colour is converted to 16 bit
	move.w	d3,d7			; set contrast colour
	moveq	#0,d0
	rts

cnc_sql
	move.l	a2,-(sp)
	move.l	pt_palql(a3),a2
	and.w	#%111,d3		; look up ql colour 0-7
	add.w	d3,d3
	move.w	(a2,d3.w),d3
	move.l	(sp)+,a2
	rts

cn_pal_mcolr
	bsr.s	cnc_spal		; palette map is converted to 16 bit colour
	bra.l	cn_int_mcolr

cn_pal_ccolr
	bsr.s	cnc_spal
	bra.l	cn_int_ccolr

cnc_spal
	move.l	a2,-(sp)
	and.w	#$00ff,d3		; look up palette colour 0-255
	add.w	d3,d3
	move.l	pt_pal256(a3),a2
	move.w	(a2,d3.w),d3
	move.l	(sp)+,a2
	rts

cn_24b_mcolr
	bsr.s	cnc_s24b		; 24 bit colour is converted to Q40 colour
	bra.s	cn_int_mcolr

cn_24b_ccolr
	bsr.s	cnc_s24b		; 24 bit colour is converted to Q40 colour
	bra.s	cn_int_ccolr

cnc_s24b
	moveq	#11,d0
	lsr.l	d0,d3			; ........ ...RRRRR rrrGGGGG GggBBBBB
	lsl.w	#1,d3			; ........ ...RRRRR rrGGGGGG ggBBBBB.
	lsl.b	#2,d3			; ........ ...RRRRR rrGGGGGG BBBBB...
	lsl.w	#2,d3			; ........ ...RRRRR GGGGGGBB BBB.....
	lsr.l	#5,d3			; ........ ........ RRRRRGGG GGGBBBBB
	ror.w	#8,d3			; swap bytes
	rts

cn_ql_bcolr
	moveq	#%00111000,d0
	and.b	d3,d0
	beq.s	cn_ql_bcsolid		; solid border colour

	move.b	d3,d0
	lsr.b	#3,d0
	eor.b	d3,d0			; contrast colour
	bsr.s	cnc_sql 		; main colour
	move.w	d3,d7
	move.b	d0,d3
	bsr.s	cnc_sql 		; contrast colour
	bra.s	cn_int_bccolr		; set both colours

cn_ql_bcsolid
	bsr.s	cnc_sql 		; set colour
	move.w	d3,d7
	moveq	#0,d0
	rts

cn_pal_bmcolr
	bsr.s	cnc_spal		; convert border colour to 16 bit
	bra.s	cn_int_bmcolr
cn_24b_bmcolr
	bsr.s	cnc_s24b		; convert border colour to 16 bit
	bra.s	cn_int_bmcolr

cn_nat_bmcolr
	ror.w	#8,d3			; swap bytes
cn_int_bmcolr
	moveq	#0,d7
	move.w	d3,d7			; for solid colour 15 bit def
	moveq	#0,d0
	rts

cn_pal_bccolr
	bsr.s	cnc_spal		; convert border colour to native
	bra.s	cn_int_bccolr
cn_24b_bccolr
	bsr.s	cnc_s24b		; convert border colour to native
	bra.s	cn_int_bccolr

cn_nat_bccolr
	ror.w	#8,d3			; swap bytes to internal
cn_int_bccolr
	bsr.s	cnc_sborb		; convert contrast word to border byte
	exg	d3,d7
	bsr.s	cnc_sborb		; convert main word to border byte
	exg	d3,d7
	lsl.w	#8,d7
	move.b	d3,d7			; the two colours combined
	moveq	#0,d0
	rts

cnc_sborb
	ror.w	#8,d3			; swap bytes to PC order
	lsl.b	#3,d3			; ........ ........ rrrrrggg bbbbb...
	ror.w	#8,d3			; ........ ........ bbbbb... rrrrrggg
	rol.b	#3,d3			; ........ ........ bbbbb... rrgggrrr
	rol.w	#2,d3			; ........ ........ bbb..... gggrrrbb
	rts

cn_nat_mcolr
	ror.w	#8,d3			 ; swap bytes
cn_int_mcolr
	move.w	d3,d7			 ; set
	swap	d7			 ; ... main
	move.w	d3,d7			 ; ... and contrast
	moveq	#0,d0
	rts

cn_nat_ccolr
	ror.w	#8,d3			 ; swap bytes
cn_int_ccolr
	move.w	d3,d7			 ; set contrast colour
cnc_ok
	moveq	#0,d0
	rts

	end
