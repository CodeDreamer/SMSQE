;	Set colours		     V2.00   1998 Tony Tebby  1998
;
;	Registers:
;		Entry				Exit
;	D1	colour required 		preserved
;	A0	pointer to cdb			preserved
;	A1					smashed
;
	section con

	include 'dev8_keys_con'

	xdef	cn_spap
	xdef	cn_sstr
	xdef	cn_sink
	xdef	cn_papp
	xdef	cn_strp
	xdef	cn_inkp
	xdef	cn_papt
	xdef	cn_strt
	xdef	cn_inkt
	xdef	cn_papn
	xdef	cn_strn
	xdef	cn_inkn

	xdef	cn_scurc

	xref	cn_ql_colr
	xref	cn_pal_mcolr
	xref	cn_pal_ccolr
	xref	cn_24b_mcolr
	xref	cn_24b_ccolr
	xref	cn_nat_mcolr
	xref	cn_nat_ccolr

cnc_paper
	moveq	#sd_pcolr,d5
	moveq	#sd_pmask,d4
	rts
cnc_strip
	moveq	#sd_scolr,d5
	moveq	#sd_smask,d4
	rts
cnc_ink
	moveq	#sd_icolr,d5
	moveq	#sd_imask,d4
	rts

cn_spap
	bsr.s	cnc_paper
	bra.s	cnc_ql
cn_sstr
	bsr.s	cnc_strip
	bra.s	cnc_ql
cn_sink
	bsr.s	cnc_ink
	bra.s	cnc_ql

cn_papp
	bsr.s	cnc_paper
	bra.s	cnc_pal
cn_strp
	bsr.s	cnc_strip
	bra.s	cnc_pal
cn_inkp
	bsr.s	cnc_ink
	bra.s	cnc_pal

cn_papt
	bsr.s	cnc_paper
	bra.s	cnc_true
cn_strt
	bsr.s	cnc_strip
	bra.s	cnc_true
cn_inkt
	bsr.s	cnc_ink
	bra.s	cnc_true

cn_papn
	bsr.s	cnc_paper
	bra.s	cnc_nat
cn_strn
	bsr.s	cnc_strip
	bra.s	cnc_nat
cn_inkn
	bsr.s	cnc_ink
	bra.s	cnc_nat

cnc_ql
	move.b	d1,d3
	jsr	cn_ql_colr
cnc_setql
	move.b	d1,(a0,d5.w)		; QL colour
	move.l	d7,(a0,d4.w)		; mask
cn_scurc
	moveq	#0,d0
	rts

cnc_pal
	lea	cn_pal_mcolr,a4        ; palette map to colour
	lea	cn_pal_ccolr,a5
	bra.s	cnc_ext_colr

cnc_true
	lea	cn_24b_mcolr,a4        ; true colour to colour
	lea	cn_24b_ccolr,a5
	bra.s	cnc_ext_colr

cnc_nat
	lea	cn_nat_mcolr,a4        ; native mode to colour
	lea	cn_nat_ccolr,a5

cnc_ext_colr
	move.l	d1,d3			; this is the colour
	tst.w	d2			; main colour or contrast
	bpl.s	cnc_ext_contrast
	jsr	(a4)			; set main colour
	clr.b	(a0,d5.w)		; no stipple
	move.l	d7,(a0,d4.w)		; mask
	moveq	#0,d0
	rts

cnc_ext_contrast
	move.w	d2,d6			; stipple
	move.l	(a0,d4.w),d7		; plain colour
	jsr	(a5)			; set contrast colour
	add.b	d6,d6
	addq.b	#1,d6			; set arbitrary contrast colour
	lsl.w	#5,d6			; QL type stipple
	move.b	d6,(a0,d5.w)		; stipple
	move.l	d7,(a0,d4.w)		; mask
	moveq	#0,d0
	rts


	end
