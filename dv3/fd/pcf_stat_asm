; DV3 PC Compatible FDC check status   V2.00    1991  Tony Tebby

	section fd

	xdef	fd_stat 		; check status of FDC at end of command
	xdef	fd_stat_seek		; check status of seek
	xdef	fd_stat_recal		; check status of recalibrate
	xdef	fd_result		; read result byte
	xdef	fd_fint 		; force interrupt (status ERR.MCHK)

	xref	fd_cmd_sint
	xref	fd_reset

	xref.l	fdc_stat
	xref.l	fdc_data


	include 'dev8_keys_err'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_fd_keys'
	include 'dev8_dv3_fd_pcf_keys'
	include 'dev8_mac_assert'

;+++
; This routine waits for the result phase of a command, reads status byte 0
; and skips the track number.
;
;	status return 0, ERR.MCHK or 2 = seek error
;---
fd_stat_seek
fd_stat_recal
	move.l	a0,-(sp)
	subq.w	#2,sp
fdss_rtry
	jsr	fd_cmd_sint		 ; sense interrupt
	bne.s	fds_rts2		 ; ... oops

	bsr.s	fds_result		 ; get first result byte
	bne.s	fds_rts2

	cmp.b	#fdc.cmpi,d0		 ; illegal command?
	beq.s	fdss_rtry		 ; ... yes, retry

	move.b	d0,(sp) 		 ; keep it

	bsr.s	fds_result		 ; get track number

	moveq	#fdcs.rsk,d0
	and.b	(a0),d0 		 ; seek still in progress?
	bne.s	fdss_rtry

	moveq	#fdc.recb,d0		 ; recalibrate (seek) completion bits
	and.b	(sp),d0
	sub.b	#fdc.reco,d0		 ; OK?
	beq.s	fds_rts2
	moveq	#fdcs.seke,d0		 ; seek error
fds_rts2
	addq.l	#2,sp
	bra.s	fds_exa0

;+++
; This routine waits for the result phase of a command and reads the status
; bytes and other results.
;
;	a3 c  p linkage block
;
;	status return 0, ERR.MCHK or 1 = overrun
;				     2 = data (CRC) error
;				     3 = seek error
;				     4 = no address mark
;---
fd_stat
fds.reg reg	d1/a0/a1
	movem.l fds.reg,-(sp)
	moveq	#6,d1			 ; seven result bytes
	lea	fdl_resb(a3),a1
fds_loop
	bsr.s	fds_result		 ; get a result byte
	bne.s	fds_exit
	move.b	d0,(a1)+		 ; keep it
	dbra	d1,fds_loop

	moveq	#fdc.cmpb,d0		 ; completion bits
	and.b	fdl_stt0(a3),d0 	 ; OK?
	beq.s	fds_ok

	assert	fdc..eot,7
	move.w	#-1+1<<15,d0		 ; end of track is not error
	and.w	fdl_stt1(a3),d0 	 ; any error bits?
	beq.s	fds_ok

	btst	#fdc..orn+8,d0		 ; overrun?
	bne.s	fds_orun		 ; ... yes
	btst	#fdc..crc+8,d0		 ; CRC error?
	bne.s	fds_derr		 ; ... yes
	btst	#fdc..wtr,d0		 ; seek error?
	bne.s	fds_seke		 ; ... yes

	moveq	#fdcs.nadd,d0
	bra.s	fds_exit

fds_derr
	moveq	#fdcs.derr,d0
	bra.s	fds_exit
fds_seke
	moveq	#fdcs.seke,d0
	bra.s	fds_exit
fds_orun
	moveq	#fdcs.orun,d0
	bra.s	fds_exit

fds_ok
	moveq	#0,d0
fds_exit
	movem.l (sp)+,fds.reg
	rts

;+++
; This routine waits for a result byte and reads it into d0.
;
;	d0  r	result byte \ err.mchk
;	status	OK or err.mchk
;---
fd_result
	move.l	a0,-(sp)
	bsr.s	fds_result
fds_exa0
	move.l	(sp)+,a0
	rts

;+++
; This routine waits (up to one second) for a result byte and reads it into d0.
;
;	d0  r	result byte or err.mchk
;	a0  r	pointer to status reg
;	status	OK or err.mchk
;---
fds_result
	move.l	fdl_1sec(a3),d0 	 ; one second timer
	tst.b	fdl_drvs(a3)		 ; run - up?
	beq.s	fds_resultw
	add.l	d0,d0			 ; no, wait longer
;+++
; This routine waits for a result byte and reads it into d0.
;
;	d0 cr	1 second timer or 0 (very long wait) / result byte or err.mchk
;	a0  r	pointer to status reg
;	status	OK or err.mchk
;---
fds_resultw
	lea	fdc_stat,a0		 ; status register

fdsr_wait
	assert	fdc..req,7
	tst.b	(a0)			 ; request?
	bmi.s	fdsr_res		 ; ... ok
	subq.l	#1,d0			 ; one gone
	bne.s	fdsr_wait

	bra.s	fd_fint 		 ; timed out

fdsr_res
	moveq	#fdcs.bit,d0
	and.b	(a0),d0 		 ; status
	cmp.b	#fdcs.res,d0		 ; result byte?
	bne.s	fd_fint 		 ; ... oops
	move.b	fdc_data-fdc_stat(a0),d0 ; result byte
	cmp.b	d0,d0			 ; OK
	rts

;+++
; This routine forcebly interrupts a read or write operation.
;
;	a3 c  p linkage block
;
;	status return ERR.MCHK
;---
fd_fint
	jsr	fd_reset		 ; reset / keep selected
fds_mchk
	moveq	#err.mchk,d0
	rts


	end
