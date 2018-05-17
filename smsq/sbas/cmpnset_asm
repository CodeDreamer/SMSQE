; SBAS_CMPNSET - Set Up Name Usage Table  V2.00     1994 Tony Tebby

	section sbas

	xdef	sb_cmpnset

	xref	sb_alwrk
	xref	sb_setvar
	xref	sb_clrval

	include 'dev8_keys_sbasic'
	include 'dev8_mac_assert'

;+++
; SBASIC Set up name usage table
;
;	d0/a0/a1/a3/a4 smashed
;	a6 c  p pointer to SuperBASIC variables
;
;	Status return arbitrary
;---
sb_cmpnset
	move.l	sb_nmtbb(a6),a3 	 ; name table
	move.l	sb_nmtbp(a6),a4
	move.l	a4,d0
	sub.l	a3,d0
	lsr.l	#nt.ishft,d0		 ; name usage table size
	lea	sb_nutbb(a6),a1
	jsr	sb_alwrk		 ; allocate it

	bra.s	scu_ntlend

scu_ntloop
	move.b	nt_nvalp(a6,a3.l),d0	 ; usage
	move.b	scu_table(pc,d0.w),d0
	bpl.s	scu_settb
	addq.b	#2,d0			 ; FOR/REP ?
 beq.s scu_null ;;;; in this version, FOR and REP loops are reset by sb_inter
;	 bgt.s	 scu_pf 		  ; ... no, Proc / fun

;	 assert  0,sb.edt-$ff,sb.edtn-$80
;	 btst	 #6,sb_edt(a6)		  ; just redo names?
;	 beq.s	 scu_null		  ; ... yes
;
;	 move.l  a0,-(sp)
;	 jsr	 sb_clrval		  ; clear the value to clear the loop
;	 move.l  (sp)+,a0

scu_pf
	jsr	sb_setvar		 ; sbasic proc / fun, reset to variable

scu_null
	moveq	#0,d0

scu_settb
	or.b	d0,(a0)+
scu_ntnext
	addq.l	#nt.len,a3
scu_ntlend
	cmp.l	a4,a3			 ; all done?
	blt.s	scu_ntloop

	move.l	a0,sb_nutbp(a6)
	rts


scu_table
	dc.b	0
	dc.b	0
	dc.b	0
	dc.b	1<<us..ary
	dc.b	-1
	dc.b	-1
	dc.b	-2
	dc.b	-2
	dc.b	1<<us..proc
	dc.b	1<<us..fun
	end
