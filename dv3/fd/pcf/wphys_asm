; DV3 PC Compatible Floppy Disk Write Sector	  1993     Tony Tebby

	section dv3

	xdef	fd_wphys		; write sector (physical layer)

	xref	fd_cmd_rw
	xref	fd_stat
	xref	fd_fint

	xref.l	fdc_stat
	xref.l	fdc_data
	xref.s	fdc.intl

	include 'dev8_keys_err'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_fd_keys'
	include 'dev8_dv3_fd_pcf_keys'
	include 'dev8_mac_assert'

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
fdw.reg reg	d1/d2/a0/a1/a2
	movem.l fdw.reg,-(sp)

	lea	fdc_stat,a0		 ; status register address
	lea	fdc_data-fdc_stat(a0),a2 ; data register address

	move.w	ddf_slen(a4),d1 	 ; sector length
	subq.w	#1,d1			 ; allow for dbra
	blt.l	fdw_mchk		 ; !!!

	move.w	sr,-(sp)
	move.w	#fdc.intl,sr		 ; ... can we get rid of this???

	moveq	#fdc.wrsc,d0		 ; write sector
	jsr	fd_cmd_rw
	bne.s	fdw_mchs

	moveq	#fdcs.wrd,d2		 ; write status

	move.l	fdl_1sec(a3),d0 	 ; 1 second timer ish
fdw_wait
	cmp.b	(a0),d2 		 ; write?
	beq.s	fdw_put 		 ; ... yes
	bgt.s	fdw_stat		 ; ... failed, status is ready

	cmp.b	(a0),d2 		 ; write?
	beq.s	fdw_put 		 ; ... yes

	subq.l	#2,d0			 ; count down
	bgt.s	fdw_wait
	bra.s	fdw_time

fdw_put
	move.b	(a1)+,(a2)		 ; put byte
	dbra	d1,fdw_wait		 ; loop

fdw_stat
	move.w	(sp)+,sr

	jsr	fd_stat 		 ; wait for status at end of command

fdw_exit
	movem.l (sp)+,fdw.reg
	rts

fdw_time
	move.w	(sp)+,sr
	jsr	fd_fint 		 ; interrupt command
	bra.s	fdw_exit

fdw_mchs
	move.w	(sp)+,sr
fdw_mchk
	moveq	#err.mchk,d0
	bra.s	fdw_exit

	end
