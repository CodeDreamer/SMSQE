; (Super)GoldCard trap and exception handling

	section patch

	xdef	sgc_qdos_trp0
	xdef	sgc_min_trp0
	xdef	sgc_trp2
	xdef	sgc_trp3
	xdef	sgc_trp4
	xdef	sgc_trp5
	xdef	sgc_trp6
	xdef	sgc_trp7
	xdef	sgc_trp8
	xdef	sgc_trp9
	xdef	sgc_trpa
	xdef	sgc_trpb
	xdef	sgc_trpc
	xdef	sgc_trpd
	xdef	sgc_trpe
	xdef	sgc_trpf
	xdef	sgc_ilin
	xdef	sgc_alin
	xdef	sgc_flin
	xdef	sgc_int2_rte

	xdef	sgc_trap1
	xdef	gl_trap1

	xref	gl_rdate
	xref	gl_sdate
	xref	gl_adate

	include 'dev8_keys_qdos_sms'
	include 'dev8_sys_gold_keys'

; trap #0 (QDOS)
sgc_qdos_trp0
	move.l	d0,-(sp)
	moveq	#9,d0
	dc.l	$4e7b0002		; clear cache
	move.l	(sp)+,d0
	bset	#$5,(a7)
	bclr	#$7,(a7)
	rte

; trap #0 (Minerva)
sgc_min_trp0
	dc.w	min_trp0_patch-*

	move.l	d0,-(sp)
	moveq	#9,d0
	dc.l	$4e7b0002		; clear cache
	move.l	(sp)+,d0
min_trp0_patch
	jmp	$12345678

sgc_trp2
	dc.w	$c
	move.l	d0,-(sp)
	moveq	#9,d0
	dc.l	$4e7b0002		; clear cache
	move.l	(sp)+,d0
	jmp	$12345678

; trap #3
sgc_trp3
	dc.w	trp3_patch-*

	move.l	d0,-(sp)
	moveq	#9,d0
	dc.l	$4e7b0002		; clear cache
	move.l	(sp)+,d0
trp3_patch
	jmp	$12345678

; trap #4
sgc_trp4
	dc.w	trp4_patch-*

	move.l	d0,-(sp)
	moveq	#9,d0
	dc.l	$4e7b0002		; clear cache
	move.l	(sp)+,d0
trp4_patch
	jmp	$12345678		; jump address to be patched

; trap #5
sgc_trp5
	dc.w	trp5_patch-*

	move.l	d0,-(sp)
	moveq	#9,d0
	dc.l	$4e7b0002		; clear cache
	move.l	(sp)+,d0
trp5_patch
	jmp	$12345678		; jump address to be patched

; trap #6
sgc_trp6
	dc.w	trp6_patch-*

	move.l	d0,-(sp)
	moveq	#9,d0
	dc.l	$4e7b0002		; clear cache
	move.l	(sp)+,d0
trp6_patch
	jmp	$12345678		; jump address to be patched

; trap #7
sgc_trp7
	dc.w	trp7_patch-*

	move.l	d0,-(sp)
	moveq	#9,d0
	dc.l	$4e7b0002		; clear cache
	move.l	(sp)+,d0
trp7_patch
	jmp	$12345678		; jump address to be patched

; trap #8
sgc_trp8
	dc.w	trp8_patch-*

	move.l	d0,-(sp)
	moveq	#9,d0
	dc.l	$4e7b0002		; clear cache
	move.l	(sp)+,d0
trp8_patch
	jmp	$12345678		; jump address to be patched

; trap #9
sgc_trp9
	dc.w	trp9_patch-*

	move.l	d0,-(sp)
	moveq	#9,d0
	dc.l	$4e7b0002		; clear cache
	move.l	(sp)+,d0
trp9_patch
	jmp	$12345678		; jump address to be patched

; trap #10
sgc_trpa
	dc.w	trpa_patch-*

	move.l	d0,-(sp)
	moveq	#9,d0
	dc.l	$4e7b0002		; clear cache
	move.l	(sp)+,d0
trpa_patch
	jmp	$12345678		; jump address to be patched

; trap #11
sgc_trpb
	dc.w	trpb_patch-*

	move.l	d0,-(sp)
	moveq	#9,d0
	dc.l	$4e7b0002		; clear cache
	move.l	(sp)+,d0
trpb_patch
	jmp	$12345678		; jump address to be patched

; trap #12
sgc_trpc
	dc.w	trpc_patch-*

	move.l	d0,-(sp)
	moveq	#9,d0
	dc.l	$4e7b0002		; clear cache
	move.l	(sp)+,d0
trpc_patch
	jmp	$12345678		; jump address to be patched

; trap #13
sgc_trpd
	dc.w	trpd_patch-*

	move.l	d0,-(sp)
	moveq	#9,d0
	dc.l	$4e7b0002		; clear cache
	move.l	(sp)+,d0
trpd_patch
	jmp	$12345678		; jump address to be patched

; trap #14
sgc_trpe
	dc.w	trpe_patch-*

	move.l	d0,-(sp)
	moveq	#9,d0
	dc.l	$4e7b0002		; clear cache
	move.l	(sp)+,d0
trpe_patch
	jmp	$12345678		; jump address to be patched

; trap #15
sgc_trpf
	dc.w	trpf_patch-*

	move.l	d0,-(sp)
	moveq	#9,d0
	dc.l	$4e7b0002		; clear cache
	move.l	(sp)+,d0
trpf_patch
	jmp	$12345678		; jump address to be patched

; Illegal instruction
sgc_ilin
	dc.w	ilin_patch-*

	move.l	d0,-(sp)
	moveq	#9,d0
	dc.l	$4e7b0002		; clear cache
	move.l	(sp)+,d0
ilin_patch
	jmp	$12345678		; jump address to be patched

; Line a emulation
sgc_alin
	dc.w	alin_patch-*

	move.l	d0,-(sp)
	moveq	#9,d0
	dc.l	$4e7b0002		; clear cache
	move.l	(sp)+,d0
alin_patch
	jmp	$12345678		; jump address to be patched

; Line f emulation
sgc_flin
	dc.w	flin_patch-*

	move.l	d0,-(sp)
	moveq	#9,d0
	dc.l	$4e7b0002		; clear cache
	move.l	(sp)+,d0
flin_patch
	jmp	$12345678		; jump address to be patched

; Interrupt 2 RTE return
sgc_int2_rte
	moveq	#9,d7			; default: enable/clear cache
	btst	#$5,$c(sp)
	bne.s	glr_sgc_rtec		; supervisor mode -> cache stays enabled
	tst.b	sgo_cache+sgx_work	; cache configured?
	bne.s	glr_sgc_rtec
	moveq	#0,d7			; disable cache
glr_sgc_rtec
	dc.l	$4e7b7002
	movem.l (sp)+,d7/a5/a6
	rte

	xdef	ms_ntabt

ms_ntabt dc.w	ntabt_ms-*
ntabt_ms dc.w	12
	 dc.b	'Net aborted',$a

st1_nslug
	move.l	#sgo.slugd,sgo_slug+sgx_work
st1_hdop
	moveq	#sms.hdop,d0
st1_cont
	jmp	$12345678

;+++
; SuperGoldCard trap #1 intercept
;---
sgc_trap1
	move.l	d0,-(sp)
	moveq	#9,d0
	dc.l	$4e7b0002		; enable/clear cache
	move.l	(sp)+,d0
	cmp.w	#sms.rrtc,d0		; less than read real time clock?
	blo.s	st1_other
	beq.s	st1_rrtc
	cmp.w	#sms.artc,d0		; greater than adjust real time clock?
	bhi.s	st1_cont
	blo.s	st1_srtc

st1_artc
	bsr.l	gl_adate		; adjust date
	bra.s	st1_rtcx

st1_rrtc
	bsr.l	gl_rdate		; read date
st1_rtcx
	btst	#$5,(sp)
	bne.s	st1_rte 		; supervisor mode call
	tst.b	sgo_cache+sgx_work	; cache?
	bne.s	st1_rte
	move.l	d0,-(sp)
	moveq	#0,d0			; disable cache
	dc.l	$4e7b0002
	move.l	(sp)+,d0
st1_rte
	rte

	move.w	d7,-(a7)
	tst.l	d0
	move.w	sr,d7
	move.b	d7,3(a7)
	move.w	(a7)+,d7
	rte

st1_srtc
	bsr.l	gl_sdate		; set date
	bra.s	st1_rtcx

st1_other
	cmp.w	#sms.hdop,d0
	bne.s	st1_cont		; not hdop
	cmp.b	#9,(a3) 		; keyrow?
	bne.s	st1_cont
	moveq	#0,d0
	move.w	sgo_slug+sgx_work,d0
	not.w	d0
	beq.s	st1_hdop
	cmp.w	sgo_slug+2+sgx_work,d0
	bne.l	st1_nslug

	mulu	#$197,d0		; x197

st1_pause
	tst.l	$18000
	subq.l	#1,d0			; 06
	bne.s	st1_pause		; 06 = 8us

	bra.l	st1_hdop

gt1_nslug
	move.l	#sgo.slugd,sgo_slug+sgx_work
gt1_hdop
	moveq	#sms.hdop,d0
gt1_cont
	jmp	$12345678

;+++
; GoldCard trap #1 intercept
;---
gl_trap1
	cmp.w	#sms.rrtc,d0		 ; less than read real time clock?
	blo.s	gt1_other
	beq.s	gt1_rrtc
	cmp.w	#sms.artc,d0		 ; greater than adjust real time clock?
	bhi.s	gt1_cont
	blo.s	gt1_srtc

gt1_artc
	bsr.l	gl_adate		 ; adjust date
	bra.s	st1_rte

gt1_rrtc
	bsr.s	gl_rdate		 ; read date
	bra.s	st1_rte

gt1_srtc
	bsr.l	gl_sdate		 ; set date
	bra.s	st1_rte

gt1_other
	cmp.w	#sms.hdop,d0
	bne.s	gt1_cont		 ; not hdop
	cmp.b	#9,(a3) 		 ; keyrow?
	bne.s	gt1_cont
	moveq	#0,d0
	move.w	sgo_slug+sgx_work,d0
	not.w	d0
	beq.s	gt1_hdop
	cmp.w	sgo_slug+2+sgx_work,d0
	bne.s	gt1_nslug

	lsl.l	#7,d0			 ; x128

gt1_pause
	rol.w	#8,d0
	rol.w	#8,d0
	rol.w	#8,d0
	rol.w	#8,d0
	lsl.l	#3,d0
	lsr.l	#3,d0
	subq.l	#1,d0			 ; 06
	bne.s	gt1_pause		 ; 06 = 8us

	bra.s	gt1_hdop

	end
