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

cn.inkcl equ	7
cn_crcol dc.l	$ffffffff

cn_palq
cn_palt
	moveq	#err.nimp,d0
	rts
cn_palset
	moveq	#0,d0
	rts


cn_bcnat			 ; border colour (2 byte) to long word mask)
	move.w	sd_bcolw(a0),d3 	; standard QL
	bsr.l	cn_ql_colr		; set d7
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
	bra.s	cnc_mcolr

cn_pal_ccolr
	bsr.s	cnc_spal
	bra.l	cnc_ccolr

cnc_spal
	and.w	#%111,d3		; look up palette colour 0-7
	move.b	cnc_pal(pc,d3.w),d3
	rts

cnc_pal
	dc.b	0,7,2,4,1,3,6,5

cn_24b_mcolr
	bsr.s	cnc_s24b		; 24 bit colour is converted to QL colour
	bra.s	cnc_mcolr

cn_24b_ccolr
	bsr.s	cnc_s24b		; 24 bit colour is converted to QL colour
	bra.s	cnc_ccolr

cnc_s24b
	swap	d3			; Bbbbbbbb ........ Rrrrrrrr Gggggggg
	lsr.b	#7,d3			; Bbbbbbbb ........ Rrrrrrrr .......G
	rol.w	#1,d3			; Bbbbbbbb ........ rrrrrrr. ......GR
	rol.l	#1,d3			; bbbbbbb. .......r rrrrrr.. .....GRB
	rts

cn_nat_mcolr
	bsr.s	cnc_snat		; 1 bit colour is converted to QL colour
	bra.s	cnc_mcolr

cn_nat_ccolr
	bsr.s	cnc_snat		; 1 bit colour is converted to QL colour
	bra.s	cnc_ccolr

cnc_snat
	and.b	#1,d3
	lsr.b	#2,d3
	rts


cn_pal_bmcolr
	bsr.s	cnc_spal		; convert border colour to QL
	bra.s	cnc_bmcolr
cn_24b_bmcolr
	bsr.s	cnc_s24b		; convert border colour to QL
	bra.s	cnc_bmcolr
cn_nat_bmcolr
	bsr.s	cnc_snat		; convert border colour to QL

cn_ql_bcolr
cnc_bmcolr
	moveq	#0,d7
	move.b	d3,d7			; for QL native mode, just save border colour
	moveq	#0,d0
	rts

cn_pal_bccolr
	bsr.s	cnc_spal		; convert border colour to QL
	bra.s	cnc_bccolr
cn_24b_bccolr
	bsr.s	cnc_s24b		; convert border colour to QL
	bra.s	cnc_bccolr
cn_nat_bccolr
	bsr.s	cnc_snat		; convert border colour to QL

cnc_bccolr
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
	beq.s	cnc_nstip
	eor.b	d6,d3			; contrast colour
	lsr.b	#6,d6
	bra.s	cnc_ccolr

cnc_mcolr
	and.w	#%110,d3		 ; two of three lsbits
	add.w	d3,d3			 ; look up colour
	move.l	cnc_mtab(pc,d3.w),d7	 ; get the main colour mask
	moveq	#0,d0
	rts

cnc_ccolr
	moveq	#7,d0
	and.b	d7,d0
	move.b	cnc_mcrest(pc,d0.w),d0	 ; main colour!!   00000gr0
	and.b	#%110,d3
	eor.b	d0,d3			 ; colour difference
	lsl.b	#2,d3			 ;		   000gr000
	add.b	d3,d0			 ; add in contrast 000grgr0
	move.b	d6,d3
	lsl.b	#5,d3			 ;		   0ss00000
	add.b	d3,d0			 ; and stipple	   0ssgrgr0
	add.b	d0,d0
	move.l	cnc_mtab(pc,d0.w),d7
	moveq	#0,d0
	rts


cnc_mcrest dc.b 0,2,2,2,4,4,6,6 	 ; recover ql colour from stipple
;
cnc_mtab
	dc.l	$00000000		 ; black
	dc.l	$44441111		 ; red
	dc.l	$aaaa5555		 ; green
	dc.l	$ffffffff		 ; white

	dc.l	$00001111
	dc.l	$55551111
	dc.l	$aaaadddd
	dc.l	$ffffdddd

	dc.l	$00004444
	dc.l	$5555bbbb
	dc.l	$aaaa4444
	dc.l	$ffffbbbb

	dc.l	$00005555
	dc.l	$55556666
	dc.l	$aaaa9999
	dc.l	$ffff5555

; horiz stripes

	dc.l	$00000000
	dc.l	$44441111
	dc.l	$aaaa5555
	dc.l	$ffffffff

	dc.l	$00005555
	dc.l	$aaaa0000
	dc.l	$5555ffff
	dc.l	$ffffaaaa

	dc.l	$0000aaaa
	dc.l	$aaaaffff
	dc.l	$55550000
	dc.l	$ffff5555

	dc.l	$0000ffff
	dc.l	$aaaaaaaa
	dc.l	$55555555
	dc.l	$ffff0000

; vertical stripes

	dc.l	$00000000
	dc.l	$44441111
	dc.l	$aaaa5555
	dc.l	$ffffffff

	dc.l	$44441111
	dc.l	$11114444
	dc.l	$7777bbbb
	dc.l	$cccc3333

	dc.l	$88882222
	dc.l	$ddddeeee
	dc.l	$22228888
	dc.l	$99996666

	dc.l	$aaaaaaaa
	dc.l	$eeeedddd
	dc.l	$bbbb7777
	dc.l	$55555555

; stipples

	dc.l	$00000000
	dc.l	$44441111
	dc.l	$aaaa5555
	dc.l	$ffffffff

	dc.l	$22228888
	dc.l	$88882222
	dc.l	$ddddeeee
	dc.l	$66669999

	dc.l	$11114444
	dc.l	$bbbb7777
	dc.l	$44441111
	dc.l	$3333cccc

	dc.l	$5555aaaa
	dc.l	$bbbb7777
	dc.l	$eeeedddd
	dc.l	$aaaa5555

	end
