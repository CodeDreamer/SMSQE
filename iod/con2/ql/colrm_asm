;	Set colour masks	V2.00   1998  Tony Tebby
;
;	Registers:
;		Entry				Exit
;	D0					0
;	D1/D2					preserved
;	D3	Colour number			preserved
;	D4					(QL colour number if set)
;	D5					preserved
;	D6	Stipple pattern 		preserved
;	D7					Colour mask
;	A0	CDB				preserved
;
;	All other registers preserved

	section con

	include 'dev8_keys_con'
	include 'dev8_keys_err'
	include 'dev8_keys_sys'
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

cn.inkcl equ	4
cn_crcol dc.l	$00ff00ff


cn_palset
	moveq	#0,d0
	rts
cn_palq
cn_palt
	moveq	#err.nimp,d0
	rts


cn_bcnat			 ; border colour (2 byte) to long word mask)
	move.w	sd_bcolw(a0),d3 	; standard QL
	bsr.s	cn_ql_colr		; set d7
	moveq	#0,d6
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

cn_pal_mcolr
	bsr.s	cnc_spal		; palette map is converted to QL colour
	bra.s	cn_nat_mcolr

cn_pal_ccolr
	bsr.s	cnc_spal
	bra.l	cn_nat_ccolr

cnc_spal
	and.w	#%111,d3		; look up palette colour 0-7
	move.b	cnc_pal(pc,d3.w),d3
	rts

cnc_pal
	dc.b	0,7,2,4,1,3,6,5

cn_24b_mcolr
	bsr.s	cnc_s24b		; 24 bit colour is converted to QL colour
	bra.s	cn_nat_mcolr

cn_24b_ccolr
	bsr.s	cnc_s24b		; 24 bit colour is converted to QL colour
	bra.s	cn_nat_ccolr

cnc_s24b
	swap	d3			; Bbbbbbbb ........ Rrrrrrrr Gggggggg
	lsr.b	#7,d3			; Bbbbbbbb ........ Rrrrrrrr .......G
	rol.w	#1,d3			; Bbbbbbbb ........ rrrrrrr. ......GR
	rol.l	#1,d3			; bbbbbbb. .......r rrrrrr.. .....GRB
	rts


cn_pal_bmcolr
	bsr.s	cnc_spal		; convert border colour to QL
	bra.s	cn_nat_bmcolr
cn_24b_bmcolr
	bsr.s	cnc_s24b		; convert border colour to QL

cn_nat_bmcolr
cn_ql_bcolr
	moveq	#0,d7
	move.b	d3,d7			; for QL native mode, just save border colour
	moveq	#0,d0
	rts

cn_pal_bccolr
	bsr.s	cnc_spal		; convert border colour to QL
	bra.s	cn_nat_bccolr
cn_24b_bccolr
	bsr.s	cnc_s24b		; convert border colour to QL
cn_nat_bccolr
	move.b	d6,d0
	lsl.b	#6,d0			; pattern
	eor.b	d7,d3			; contrast
	lsl.b	#3,d3
	or.b	d3,d7
	or.b	d0,d7
	moveq	#0,d0
	rts

cn_ql_colr
	moveq	#0,d6
	move.b	d3,d6
	bsr.s	cnc_mcolr		; set main colour
	move.b	d6,d3
	lsr.b	#3,d3
	moveq	#%111,d0
	and.b	d3,d0			; colour difference
	beq.l	cnc_nstip
	eor.b	d6,d3			; contrast colour
	lsr.b	#6,d6
	bra.s	cnc_ccolr

cn_nat_mcolr
cnc_mcolr
	and.w	#%111,d3		 ; three lsbits
	add.w	d3,d3			 ; look up colour
	cmp.b	#ptm.ql8,pt_dmode(a3)	 ; which mode are we in?
	bne.s	cnc_smain		 ; mode 4, OK
	add.w	#m8tab-m4tab,d3 	 ; mode 8, set the address
*
cnc_smain
	move.w	m4tab(pc,d3.w),d7	 ; get the main colour mask
	swap	d7
	move.w	m4tab(pc,d3.w),d7
	moveq	#0,d0
	rts

cn_nat_ccolr
cnc_ccolr
	movem.l d6/a1,-(sp)
	and.w	#%111,d3		 ; three lsbits
	add.w	d3,d3			 ; look up colour
	lea	m4tab(pc),a1		 ; assume mode 4
	cmp.b	#ptm.ql8,pt_dmode(a3)	 ; which mode are we in?
	bne.s	cnc_scont		 ; mode 4, OK
	lea	m8tab(pc),a1		 ; mode 8, set the address
*
cnc_scont
	move.w	(a1,d3.w),d0		 ; get the contrast colour mask

	add.w	d6,d6
	and.w	#%110,d6
	move.w	stpj(pc,d6.w),d6
	jmp	stpj(pc,d6.w)

m4tab
col_tab equ	*-m4tab
	dc.w	%0000000000000000
	dc.w	%0000000000000000
	dc.w	%0000000011111111
	dc.w	%0000000011111111
	dc.w	%1111111100000000
	dc.w	%1111111100000000
	dc.w	%1111111111111111
	dc.w	%1111111111111111
maskl	equ	*-m4tab
	dc.w	%1010101010101010
maskr	equ	*-m4tab
	dc.w	%0101010101010101

m8tab
	assert	col_tab,*-m8tab
	dc.w	%0000000000000000
	dc.w	%0000000001010101
	dc.w	%0000000010101010
	dc.w	%0000000011111111
	dc.w	%1010101000000000
	dc.w	%1010101001010101
	dc.w	%1010101010101010
	dc.w	%1010101011111111
	assert	maskl,*-m8tab
	dc.w	%1100110011001100
	assert	maskr,*-m8tab
	dc.w	%0011001100110011


stpj
	dc.w	stp0-stpj
	dc.w	stp1-stpj
	dc.w	stp2-stpj
	dc.w	stp3-stpj
stp0
	and.w	maskr(a1),d0		 ; change rhs pixels
	and.w	maskl(a1),d7
	eor.w	d0,d7
	swap	d7			 ; in lower row only
	bra.s	cnc_exit
stp1
	move.w	d0,d7			 ; change all pixels in upper row only
	bra.s	cnc_exit
stp2
	and.w	maskr(a1),d0		 ; change rhs pixels
	and.w	maskl(a1),d7
	eor.w	d0,d7
	move.w	d7,d0
	swap	d7
	move.w	d0,d7			 ; in all rows
	bra.s	cnc_exit
stp3
	and.w	maskl(a1),d0		 ; change lhs pixels
	and.w	maskr(a1),d7
	eor.w	d0,d7
	swap	d7			 ; in lower row only
	move.w	(a1,d3.w),d0		 ; restore contrast colour mask
	and.w	maskr(a1),d0		 ; change rhs pixels
	and.w	maskl(a1),d7
	eor.w	d0,d7			 ; in upper row

cnc_exit
	movem.l (sp)+,d6/a1
cnc_ok
	moveq	#0,d0
	rts


	end
