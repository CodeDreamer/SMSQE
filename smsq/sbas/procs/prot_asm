; SBAS_PROCS_PROT - SBASIC Prot/Slug bits  V2.00    1994  Tony Tebby

	section exten

	xdef	prot_date
	xdef	prot_mem
	xdef	slug

	xref	ut_gxin1

	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_sys'

;+++
; SLUG n
;---
slug
	lea	sys_slug+1,a0	  (slug 0 to 255 only)
	bra.s	prot
;+++
; PROT_DATE 0 or 1
;---
prot_date
	lea	sys_prtc,a0
	bra.s	prot
;+++
; PROT_MEM 0 to 7
;---
prot_mem
	lea	sys_pmem,a0

prot
	jsr	ut_gxin1		 ; one param only
	bne.s	prot_rts

	move.w	(a6,a1.l),d1
	moveq	#sms.xtop,d0		 ; system extop
	trap	#do.smsq
	move.b	d1,(a6,a0.l)
	moveq	#0,d0
prot_rts
	rts
	end
