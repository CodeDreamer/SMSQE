; DV3 Atari Floppy Disk Read Sector	 1993	   Tony Tebby

	section dv3

	xdef	fd_rphys		; read sector (physical layer)

	xref	fd_cmd_rd
	xref	fd_stat

	xref	at_seta
	xref	at_setrf

	include 'dev8_keys_err'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_fd_keys'
	include 'dev8_keys_atari'
	include 'dev8_smsq_smsq_base_keys'

;+++
; Read sector (physical layer) - no error recovery
;
;	d2 cp	number of sectors to read
;	d7 c  p drive ID / number
;	a1 c  p address to read into
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0, ERR.MCHK or conventional error
;
;---
fd_rphys
	cmp.l	#$1000000,a1		 ; above DMA range?
	blo.s	fdrp_do 		 ; ... no


fdr.reg reg	a1/a2
	movem.l fdr.reg,-(sp)
	move.l	a1,a2
	move.l	sms.128kb,a1		 ; use 128 k buffer
	bsr.s	fdrp_do
	bne.s	fdr_exit

	moveq	#$200/16-1,d0		 ; copy sector into true buffer
fdr_copy
	move.l	(a1)+,(a2)+
	move.l	(a1)+,(a2)+
	move.l	(a1)+,(a2)+
	move.l	(a1)+,(a2)+
	dbra	d0,fdr_copy

	moveq	#0,d0

fdr_exit
	movem.l (sp)+,fdr.reg
	rts


fdrp_do
	moveq	#fdc.rsec,d0		 ; read sector
	pea	fd_stat
	jmp	fd_cmd_rd

	end
