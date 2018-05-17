; SBAS_CMPADD - Resolve Addresses     V2.00     1994 Tony Tebby

	section sbas

	xdef	sb_cmpadd

	xref	sb_sttadd
	xref	sb_ixtable

	include 'dev8_keys_sbasic'
	include 'dev8_smsq_sbas_comp_keys'

;+++
; SBASIC Resolve addresses.
;
;	a5 c  p pointer to compiler token base
;	a6 c  p pointer to SuperBASIC variables
;
;	All other registers smashed
;	Status return standard
;---
sb_cmpadd
	move.l	sb_progb-sb_ptokb(a5),a1 ; program token
	move.l	sb_progp-sb_ptokb(a5),a4 ; and top
	lea	sb_ixtable,a3		 ; extension table
	moveq	#0,d4

sca_loop
	add.w	d4,a1
	cmp.l	a4,a1			 ; all processed?
	bge.s	sca_done		 ; ... yes

	move.w	(a1)+,d4		 ; next token
	move.w	(a3,d4.w),d4		 ; flag / number of extension words
	blt.s	sca_addr		 ; flag
	ext.w	d4
	bge.s	sca_loop		 ; just move on
	move.w	(a1)+,d4		 ; skip variable list
	add.w	d4,d4
	bra.s	sca_loop

sca_addr
	move.l	(a1),d0
	jsr	sb_sttadd		 ; convert statement to address
	move.l	d0,(a1)

	ext.w	d4
	bra.s	sca_loop

sca_done
	moveq	#0,d0
	rts
	end
