; Q40 determine processor / hardware type	 1999	Tony Tebby

	section init

	xdef	q40_prtype

	xref	gu_exvt

	include 'dev8_keys_68000'
	include 'dev8_keys_sys'
	include 'dev8_mac_creg'

;+++
; Determines the processor/hardware type
;
;	d6 r	four bytes:	msbyte	processor number 40, 60 hex
;
;					co-processor	 01 Internal PMMU
;							+04 Internal FPU
;
;					special procs	 00
;
;				lsbyte	machine 	 11
;---
q40_prtype
	move.l	#sys.mq40<<16+$40<<8,d6  ; 68040, no mmu/fpu, Q40

	lea	qpt_freset,a0		 ; reset the floating point unit
	move.l	#exv_flin<<16+exv_ilin,d0 ; trap illegal instruction, unimpl fp
	jsr	gu_exvt

	moveq	#-1,d0
	clr.w	d0
	pcreg	urp
	moveq	#-1,d1
	clr.w	d1
	gcreg	urp			; has the URP been set?
	cmp.l	d0,d1
	bne.s	qpt_4060		; ... no
	moveq	#0,d0
	pcreg	urp
	moveq	#0,d1
	gcreg	urp			; has the URP been set?
	cmp.l	d0,d1
	bne.s	qpt_4060		; ... no

	or.b	#sys.immu,d6		; yes, we have an MMU

qpt_4060
	lea	qpt_sel60,a0		; select 060 superscalar
	moveq	#exv_ilin,d0		; trap illegal instruction
	jsr	gu_exvt

	swap	d6			; get values round the right way
	moveq	#0,d0
	rts


qpt_freset
	lea	qfr_nul,a0
	dc.w	$f350			; restore FP state to 0
	or.b	#sys.ifpu,d6
	rts

qpt_sel60
	moveq	#1,d0			; set superscalar
	pcreg	pcr			; in program configuration register
	or.w	#$60<<8,d6		; say it is 060
	rts

qfr_nul dc.l	0
	end
