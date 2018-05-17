; DV3 Atari Floppy Disk Write Sector	  1993     Tony Tebby

	section dv3

	xdef	fd_wphys		; write sector (physical layer)

	xref	fd_cmd_wr
	xref	fd_stat

	xref	at_seta
	xref	at_setwf

	include 'dev8_keys_err'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_fd_keys'
	include 'dev8_keys_atari'
	include 'dev8_smsq_smsq_base_keys'

;+++
; Write sector (physical layer) - no error recovery
;
;	d7 c  p drive ID / number
;	a1 c  p address to write from
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0, ERR.MCHK or conventional error
;
;---
fd_wphys
	cmp.l	#$1000000,a1		 ; above DMA range?
	blo.s	fdwp_do 		 ; ... no


fdw.reg reg	a1/a2
	movem.l fdw.reg,-(sp)

	move.l	sms.128kb,a2		 ; use 128 k buffer
	moveq	#$200/16-1,d0		 ; copy sector into true buffer
fdw_copy
	move.l	(a1)+,(a2)+
	move.l	(a1)+,(a2)+
	move.l	(a1)+,(a2)+
	move.l	(a1)+,(a2)+
	dbra	d0,fdw_copy

	move.l	sms.128kb,a1

	bsr.s	fdwp_do

fdw_exit
	movem.l (sp)+,fdw.reg
	rts


fdwp_do
	moveq	#fdc.wsec,d0		 ; write sector
	jsr	fd_cmd_wr

	jmp	fd_stat 		 ; wait for status at end of command

	end
