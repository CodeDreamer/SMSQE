; SUPER GOLD CARD PATCHES

	section sgc

	xdef	sgc_xbasic

	xref	gl_hires
	xref	ut_gtint

	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_sys'
	include 'dev8_keys_qlv'
	include 'dev8_keys_iod'
	include 'dev8_sys_gold_keys'

sgc_xbasic
	lea	gc_procs,a1
	tst.w	glx_ptch+glk.card
	bne.s	basic_gc		; only GoldCard
	lea	sgc_procs,a1		; yay! SGC
basic_gc
	move.w	sb.inipr,a2
	jsr	(a2)
	moveq	#0,d0
	rts

sgc_procs
	dc.w	10
	dc.w	cache_on-*
	dc.b	8,'CACHE_ON'
	dc.w	cache_off-*
	dc.b	9,'CACHE_OFF'
	dc.w	scr2dis-*
	dc.b	7,'SCR2DIS'
	dc.w	scr2en-*
	dc.b	6,'SCR2EN'
	dc.w	auto_tk2f1-*
	dc.b	10,'AUTO_TK2F1'
	dc.w	auto_tk2f2-*
	dc.b	10,'AUTO_TK2F2'
	dc.w	auto_dis-*
	dc.b	8,'AUTO_DIS'
	dc.w	flp_jiggle-*
	dc.b	10,'FLP_JIGGLE'
	dc.w	0,0,0

gc_procs
	dc.w	6
	dc.w	gc_tk2f1-*
	dc.b	10,'AUTO_TK2F1'
	dc.w	gc_tk2f2-*
	dc.b	10,'AUTO_TK2F2'
	dc.w	gc_dis-*
	dc.b	8,'AUTO_DIS'
	dc.w	gc_jiggle-*
	dc.b	10,'FLP_JIGGLE'
	dc.w	0,0,0

; FLP_JIGGLE
gc_jiggle
	bsr.l	do_jiggle
	andi.b	#$80,d0
	addq.b	#4,d0			; get or set bit 0
	bsr.s	gc_change_config
	moveq	#0,d0
	rts

; AUTO_TK2F1
gc_tk2f1
	moveq	#$ffffff87,d0		; clear bit 3
	bsr.s	gc_change_config
	moveq	#6,d0			; set bit 2
	bsr.s	gc_change_config
	moveq	#0,d0
	rts

; AUTO_TK2F2
gc_tk2f2
	moveq	#$ffffff86,d0		; clear bit 2
	bsr.s	gc_change_config
	moveq	#7,d0			; set bit 3
	bsr.s	gc_change_config
	moveq	#$0,d0
	rts

; AUTO_DIS
gc_dis
	moveq	#7,d0			; clear bit 3
	bsr.s	gc_change_config
	moveq	#6,d0			; clear bit 2
	bsr.s	gc_change_config
	moveq	#0,d0
	rts

; Change GC config in RTC chip
gc_change_config
	movem.l d1/a2,-(sp)
	move	sr,d1
	swap	d1
	move.l	glx_ptch+glk.base,a2	; hardware base
	trap	#0
	ori.w	#$700,sr
	move.b	#$30,glo_rtcc(a2)	; enable RTC interrupt register
	move.b	glo_rtcs(a2),d1 	; read interrupt register
	tst.b	d0
	bpl.s	gc_cfg_clr
	bset	d0,d1
	bra.s	gc_cfg_save

gc_cfg_clr
	bclr	d0,d1
gc_cfg_save
	move.b	d1,glo_rtcs(a2)
	move.b	#$10,(a2)		; disable RTC interrupt register
	swap	d1
	move	d1,sr
	movem.l (sp)+,d1/a2
	rts

; CACHE_ON
cache_on
	moveq	#9,d0
	bra.s	cache_set

; CACHE_OFF
cache_off
	moveq	#0,d0
cache_set
	sne	sgx_work+sgo_cache
	moveq	#0,d0
	trap	#1			; this will apply the change
	rts

; SCR2DIS
scr2dis
	moveq	#1,d0
	bra.s	scr2all
; SCR2EN
scr2en
	moveq	#0,d0
scr2all
	sne	d0
	move.l	glx_ptch+glk.base,a2	; get hardware base
	beq.s	scr2en2
	sf	sgo_scr2(a2)
	bra.s	scr2save

scr2en2
	tst.b	sgo_scr2(a2)
scr2save
	andi.b	#$80,d0
	addq.b	#5,d0			; set/clear bit 1
	bsr.s	sgc_change_config
	moveq	#$0,d0
	rts

; Change SGC config in RTC chip
sgc_change_config
	movem.l d1/a2,-(sp)
	move	sr,d1
	swap	d1
	move.l	glx_ptch+glk.base,a2
	trap	#0
	ori.w	#$700,sr
	sf	glo_rtcc(a2)		; SGC RTC write sequence
	sf	glo_dow(a2)
	sf	glo_d01(a2)
	move.b	#$30,glo_rtcc(a2)	; enable interrupt register
	move.b	glo_rtcs(a2),d1 	; read interrupt register
	tst.b	d0
	bpl.s	sgc_cfg_clr
	bset	d0,d1
	bra.s	sgc_save

sgc_cfg_clr
	bclr	d0,d1
sgc_save
	sf	glo_rtcc(a2)		; SGC RTC write sequence
	sf	glo_dow(a2)
	sf	glo_d01(a2)
	move.b	d1,glo_rtcs(a2) 	; write back interrupt register

	sf	glo_rtcc(a2)		; SGC RTC write sequence
	sf	glo_dow(a2)
	sf	glo_d01(a2)
	move.b	#$10,glo_rtcc(a2)	; disable interrupt register
	swap	d1
	move	d1,sr
	movem.l (sp)+,d1/a2
	rts

; AUTO_TK2F1 SGC
auto_tk2f1
	moveq	#$ffffff87,d0		; set bit 3
	bsr.s	sgc_change_config
	moveq	#6,d0			; clear bit 2
	bsr.s	sgc_change_config
	clr.l	sgx_work+sgo_hires
	moveq	#0,d0
	rts

; AUTO_TK2F2
auto_tk2f2
	moveq	#$ffffff86,d0		; set bit 2
	bsr.s	sgc_change_config
	moveq	#7,d0			; clear bit 3
	bsr.s	sgc_change_config
	clr.l	sgx_work+sgo_hires
	moveq	#0,d0
	rts

; AUTO_DIS
auto_dis
	moveq	#7,d0			; clear bit 3
	bsr.s	sgc_change_config
	moveq	#6,d0			; clear bit 2
	bsr.l	sgc_change_config
	clr.l	sgx_work+sgo_hires
	moveq	#0,d0
	rts

; Unused piece of code
hires_init
	move.b	gl_hires,d0
	bpl.s	hires_ok
	tst.w	glx_ptch+glk.card	; GC or SGC?
	bne.s	hires_ok		; ... GC
	lea	hr_procs,a1
	move.w	sb.inipr,a2
	jsr	(a2)
hires_ok
	moveq	#$0,d0
	rts

hr_procs
	dc.w	2
	dc.w	auto_tk2hires-*
	dc.b	13,'AUTO_TK2HIRES'
	dc.w	0,0,0,0

; AUTO_TK2HIRES
auto_tk2hires
	moveq	#$ffffff87,d0		; set bit 3
	bsr.l	sgc_change_config
	moveq	#$ffffff86,d0		; set bit 2
	bsr.l	sgc_change_config
	move.l	#sgo.hires,sgx_work+sgo_hires
	moveq	#$0,d0
	rts

; FLP_JIGGLE
flp_jiggle
	bsr.s	do_jiggle
	andi.b	#$80,d0
	addq.b	#4,d0
	bsr.l	sgc_change_config
	moveq	#0,d0
	rts

do_jiggle
	bsr.l	ut_gtint
	bne.s	err4

	tst.w	d3
	beq.s	err4			; no parameter is bad
	subq.w	#2,d3
	bhi.s	err4			; more than 2 is also bad

	bsr.s	get_flp_ddb
	bne.s	err4

	lea	$cc-iod_iolk(a0),a0	; FLP jiggle
	tst.w	d3
	blt.s	jiggle_all		; set same value for all drives

	move.w	(a6,a1.l),d2		; drive number
	ble.s	err4
	subq.w	#4,d2
	bhi.s	err4
	moveq	#$7f,d0
	and.b	3(a0,d2.w),d0
	tst.w	2(a6,a1.l)
	beq.s	jiggle_single_set
	tas	d0
jiggle_single_set
	move.b	d0,3(a0,d2.w)
	addq.l	#4,sp			; skip rest of FLP_JIGGLE, don't save config
	moveq	#0,d0
	rts

jiggle_all
	move.l	#$7f7f7f7f,d0		; set value for all drives
	tst.w	0(a6,a1.l)
	beq.s	jiggle_all_clear
	not.l	d0
	or.l	(a0),d0
	bra.s	jiggle_all_set
jiggle_all_clear
	and.l	(a0),d0
jiggle_all_set
	move.l	d0,(a0)
	rts

err4
	addq.l	#4,sp
err
	moveq	#-15,d0
	rts

get_flp_ddb
	move.l	#'FLP0',d7
	moveq	#sms.info,d0
	trap	#1
	lea	sys_fsdl(a0),a0
ddb_loop
	move.l	(a0),d0
	beq.s	err
	move.l	d0,a0
	cmp.l	iod_dnam-iod_iolk+2(a0),d7
	bne.s	ddb_loop
	rts

	end
