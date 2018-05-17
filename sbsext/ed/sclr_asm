* ED - set window colours    1985  Tony Tebby	QJUMP
*
	section ed
*
	xdef	ed_shigh
	xdef	ed_snorm
*
	include dev8_sbsext_ed_keys
	include dev8_sbsext_ed_data
	include dev8_keys_con

*
ed_snorm
	sf	ed_chang(a5)		clear change flag
	tst.b	ed_high(a5)		... already normal?
	beq.s	edc_ok
	sf	ed_high(a5)
	bra.s	edc_sclr

ed_shigh
	tst.b	ed_high(a5)		... already high?
	bne.s	edc_ok
	st	ed_high(a5)

edc_sclr
	lea	edc_extop,a2
	moveq	#sd.extop,d0		swap colours
	moveq	#-1,d3
	trap	#3
	bra.s	edc_ok

edc_extop
	move.l	sd_imask(a0),d0
	move.l	sd_pmask(a0),sd_imask(a0)
	move.l	d0,sd_pmask(a0)
	move.l	d0,sd_smask(a0)

	move.b	sd_icolr(a0),d0
	move.b	sd_pcolr(a0),sd_icolr(a0)
	move.b	d0,sd_pcolr(a0)
	move.b	d0,sd_scolr(a0)

edc_ok
	moveq	#0,d0
	rts
	end
