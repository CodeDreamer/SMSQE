; DV3 Read SCSI Info   V2.00    1990	QJUMP

	section dv3

	xdef	sc_inqry
	xdef	sc_rsns

	xref	sc_cmdi
	xref	sc_statrd
	xref	sc_nmode

	include 'dev8_keys_err'
	include 'dev8_keys_atari'
	include 'dev8_keys_scsi'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'

;+++
; Read SCSI Sense (page three) into HDL_BUFF
;
;	d6 c  p msw drive number
;	a3 c  p pointer to linkage block
;	a4 c  p pointer to physical definition
;
;	error return standard
;---
sc_rsns
sri.reg reg	d1/d2/d3/d7/a1/a2
	movem.l sri.reg,-(sp)
	moveq	#scc.msns,d0
	move.l	#$00030064,d3
	bra.s	sri_do

;+++ 
; Read SCSI Inquiry data in HDL_BUFF
;
;	d3 c  p four bytes of parameters
;	d6 c  p msw drive number
;	a3 c  p pointer to linkage block
;	a4 c  p pointer to physical definition
;
;	error return standard
;---
sc_inqry
	movem.l sri.reg,-(sp)
	moveq	#scc.inqr,d0
	move.l	#$00000038,d3
sri_do
	subq.b	#hdl.acss,hdl_acss(a3)	 ; hold poll

	jsr	sc_cmdi 		 ; command to read inf
	bne.s	sri_mchk

	jsr	sc_statrd		 ; wait for status at end of command
	beq.s	sri_exit

sri_mchk
	moveq	#err.mchk,d0

sri_exit
	jsr	sc_nmode		 ; normal mode
	addq.b	#hdl.acss,hdl_acss(a3)	 ; restore poll
	tst.l	d0
	movem.l (sp)+,sri.reg
	rts
	end
