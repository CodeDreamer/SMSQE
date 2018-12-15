; DV3 PC Compatible Floppy Disk Read Sector	 1993	   Tony Tebby

	section dv3

	xdef	fd_rphys		; read sector (physical layer)

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
fdr.reg reg	d1/d2/a0/a1/a2
	movem.l fdr.reg,-(sp)

	lea	fdc_stat,a0		 ; status register address
	lea	fdc_data-fdc_stat(a0),a2 ; data register address

	move.w	ddf_slen(a4),d1 	 ; sector length
	subq.w	#1,d1			 ; allow for dbra
	blt.l	fdr_mchk		 ; !!!

	move.w	sr,-(sp)
	move.w	#fdc.intl,sr		 ; ... can we get rid of this???

	moveq	#fdc.rdsc,d0		 ; read sector
	jsr	fd_cmd_rw
	bne.s	fdr_mchs

	moveq	#fdcs.rdd,d2		 ; read status

	move.l	fdl_1sec(a3),d0 	 ; 1 second timer ish
fdr_wait
	cmp.b	(a0),d2 		 ; read?
	beq.s	fdr_get 		 ; ... yes
	bgt.s	fdr_stat		 ; ... failed, status is ready

	cmp.b	(a0),d2 		 ; read?
	beq.s	fdr_get 		 ; ... yes

	subq.l	#2,d0			 ; count down
	bgt.s	fdr_wait
	bra.s	fdr_time

fdr_get
	move.b	(a2),(a1)+		 ; get byte
	dbra	d1,fdr_wait		 ; loop

fdr_stat
	move.w	(sp)+,sr

	jsr	fd_stat 		 ; wait for status at end of command

fdr_exit
	movem.l (sp)+,fdr.reg
	rts

fdr_time
	move.w	(sp)+,sr
	jsr	fd_fint 		 ; interrupt command
	bra.s	fdr_exit

fdr_mchs
	move.w	(sp)+,sr
fdr_mchk
	moveq	#err.mchk,d0
	bra.s	fdr_exit

	end
