; DV3 Read SCSI Info from ACSI	 V2.00	  1990   QJUMP

	section dv3

	xdef	ac_inqry
	xdef	ac_rsns

	xref	ac_cmdi
	xref	ac_stat
	xref	ac_nmode
	xref	at_takedma
	xref	at_reldma

	include 'dev8_keys_err'
	include 'dev8_keys_atari'
	include 'dev8_keys_sys'
	include 'dev8_keys_scsi'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'
	include 'dev8_smsq_smsq_base_keys'

;+++
; Read SCSI Sense (page three) into a buffer
;
;	d6 c  p msw drive number
;	a3 c  p pointer to linkage block
;	a4 c  p pointer to physical definition
;
;	error return standard
;---
ac_rsns
ari.reg reg	d1/d2/d3/d7/a2
	movem.l ari.reg,-(sp)
	moveq	#scc.msns,d0
	move.l	#$00030064,d3
	bra.s	ari_do

;+++
; Read SCSI Inquiry in a buffer
;
;	d3 c  p four bytes of parameters
;	d6 c  p msw drive number
;	a3 c  p pointer to linkage block
;	a4 c  p pointer to physical definition
;
;	error return standard
;---
ac_inqry
	movem.l ari.reg,-(sp)
	moveq	#scc.inqr,d0
	move.l	#$00000038,d3
ari_do
	jsr	at_takedma
	blt.s	ari_exnc

	lea	hdl_buff(a3),a1 	 ; load into buffer
	cmp.l	#$1000000,a1		 ; in DMA range?
	blo.s	acri_do
	move.l	sms.128kb,a1		 ; ... no, use 128 k buffer

acri_do
	jsr	ac_cmdi 		 ; command to read inf
	bne.s	ari_mchk

	jsr	ac_stat 		 ; wait for status at end of command
	beq.s	ari_exit

ari_mchk
	moveq	#err.mchk,d0

ari_exit
	jsr	ac_nmode		 ; normal mode
	jsr	at_reldma		 ; DMA no longer in use
ari_exnc
	tst.l	d0
	movem.l (sp)+,ari.reg
	rts

	end
