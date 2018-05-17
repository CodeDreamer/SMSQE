; SBAS_PFNAME - Sort out procedure, function and dim names     1993 Tony Tebby

	section sbas

	xdef	sb_prname    ; sort out procedure definition
	xdef	sb_fnname    ; sort out function definition
	xdef	sb_dmname    ; sort out dimension

	xdef	sb_setarr    ; set name to array type
	xdef	sb_setpf

	xref	sb_clrval
	xref	sb_clrstk
	xref	sb_dumarray

	include 'dev8_keys_sbasic'
	include 'dev8_smsq_sbas_parser_keys'
	include 'dev8_mac_assert'

;+++
; This routine is called after the name of an array in a DIM statement.
; It scans back, looking for the name index and then sets (if necessary and
; possible) the name table with a (dummy) array entry.
;
;	a3    s
;	a4 c  p pointer to parsed tokens (rel a6)
;
;---
sb_dmname
	jsr	sb_clrstk		 ; clear the return stack
	assert	0,sb.edt-$ff,sb.edtn-$80
	tas	sb_edt(a6)		 ; edited! to redo name types

	move.l	a4,a3			 ; look for name
	bsr.l	sbpf_skip		 ; skipping spaces

	moveq	#0,d1
	move.w	(a6,a3.l),d1		 ; name table index

;+++
; This routine sets the name (index d1.l) to array type (if possible)
;
;	d0  r	zero
;	d1 c  s name table index
;---
sb_setarr
	move.l	a3,-(sp)
	lsl.w	#nt.ishft,d1		 ; index name table
	add.l	sb_nmtbb(a6),d1
	move.l	d1,a3
	move.b	nt_nvalp(a6,d1.l),d0
	move.w	#1<<nt.unset+1<<nt.var+1<<nt.rep+1<<nt.for,d1
	btst	d0,d1			 ; value?
	beq.s	sdn_ok			 ; ... no - do nothing for the moment

	move.l	a0,-(sp)
	jsr	sb_clrval		 ; clear the value
	move.l	(sp)+,a0
	move.l	a3,d1

	move.b	#nt.arr,nt_nvalp(a6,d1.l); set usage
	lea	sb_dumarray,a3
	move.l	a3,nt_value(a6,d1.l)	 ; and value
sdn_ok
	move.l	(sp)+,a3
	moveq	#0,d0
	rts


;+++
; This routine is called after the name of a function in a DEF statement.
; It scans back, looking for the name index and line number, and then fills in
; the name table with a function definition
;
;	d3    s
;	a3    s
;	a4 c  p pointer to parsed tokens (rel a6)
;
;---
sb_fnname
	jsr	sb_clrstk		 ; clear return stack
	moveq	#nt.sbfun,d0
	bra.s	sbpf_set

;+++
; This routine is called after the name of a procedure in a DEF statement.
; It scans back, looking for the name index and line number, and then fills in
; the name table with a procedure definition
;
;	d3    s
;	a3    s
;	a4 c  p pointer to parsed tokens (rel a6)
;
;---
sb_prname
	jsr	sb_clrstk		 ; clear return stack
	moveq	#nt.sbprc,d0
sbpf_set
	move.l	a4,a3			 ; look for name

	bsr.s	sbpf_skip		 ; skipping spaces

	moveq	#0,d1
	move.w	(a6,a3.l),d1		 ; name table index

	subq.w	#2,a3			 ; skip name index

	bsr.s	sbpf_skip		 ; back a bit (skipping name token)

	bsr.s	sbpf_skip		 ; back a bit (skipping proc / fn)

	bsr.s	sbpf_skip		 ; back a bit (skipping def)

	cmp.w	#tkw.lno,-2(a6,a3.l)	 ; line number
	bne.s	sbpf_err
	move.w	(a6,a3.l),d3		 ; is this

;+++
; This routine sets the name (index d1.l) to proc/fun type
;
;	d0 cr	nt.sbprc or nt.sbfun \ error code
;	d1 c  s name table index
;	d3 c  s line number
;
;---
sb_setpf
	lsl.w	#nt.ishft,d1		 ; index name table
	add.l	sb_nmtbb(a6),d1
	cmp.b	nt_nvalp(a6,d1.l),d0	 ; check usage
	beq.s	sbpf_done

	movem.l d0/d1/a0/a3,-(sp)
	move.l	d1,a3
	move.b	nt_nvalp(a6,a3.l),d0	 ; usage
	move.w	#1<<nt.unset+1<<nt.var+1<<nt.arr+1<<nt.rep+1<<nt.for,d1
	btst	d0,d1			 ; value?
	beq.s	sbpf_rest		 ; ... no
	jsr	sb_clrval		 ; clear the value
sbpf_rest
	movem.l (sp)+,d0/d1/a0/a3

sbpf_done
	move.b	d0,nt_usetp(a6,d1.l)	 ; set usage and not type
	move.w	d3,nt_value(a6,d1.l)	 ; and value is line
	move.w	#2,nt_value+2(a6,d1.l)	 ; and statement
	moveq	#0,d0
	rts

sbpf_err
	moveq	#sbe.idef,d0
	rts

sbpf_skip
	subq.l	#2,a3			 ; back a bit (skipping name)
	cmp.b	#tkb.spce,(a6,a3.l)
	beq.s	sbpf_skip
	rts


	end
