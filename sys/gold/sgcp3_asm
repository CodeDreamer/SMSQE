; (Super)GoldCard patches. Deciphered by Marcel Kilgus

	section sgc

	xdef	gl_f1f2
	xdef	gl_bvchnt

	include 'dev8_sys_gold_keys'
	include 'dev8_keys_qdos_io'
	include 'dev8_keys_sbasic'
	include 'dev8_keys_sys'

; F1/F2 auto boot
gl_f1f2
	moveq	#-1,d3
	moveq	#$ffffffe8,d1		; F1
	cmpi.w	#sgo.autoh,sgo_f1f2+sgx_work	; reserved for HIRES setting
	beq.s	gl_f1f2_end
	cmpi.w	#sgo.auto1,sgo_f1f2+sgx_work
	beq.s	gl_f1f2_end
	moveq	#$ffffffec,d1		; F2
	cmpi.w	#sgo.auto2,sgo_f1f2+sgx_work
	beq.s	gl_f1f2_end
	moveq	#iob.fbyt,d0		; No auto F1/F2, read key
	trap	#$3
gl_f1f2_end
	moveq	#$0,d0
	rts

; Some name table fix (reserve additional space)
gl_bvchnt
	movem.l d1/d2/d3,-(sp)
	jsr	$12345678		; will be patched to bv_chnt
	movem.l (sp)+,d1/d2/d3
	move.l	sb_nmtbp(a6),a3 	; code that was overwritten by patch
	move.b	#nt.var,nt_nvalp(a6,a3.l)
	rts

	end
