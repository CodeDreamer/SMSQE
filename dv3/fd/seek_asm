; DV3 Standard Floppy Disk Seek      1993     Tony Tebby

	section dv3

	xdef	fd_seek 		; seek
	xdef	fd_recal		; recalibrate
	xdef	fd_stepin5		; step in 5 tracks
	xdef	fd_seekri		; seek with read ID
	xdef	fd_recalri		; recalibrate with read RI
	xdef	fd_seekw		; seek before write

	xref	fd_cmd_seek		; seek command
	xref	fd_cmd_stin		; step in (5) command
	xref	fd_cmd_recal		; recalibrate command
	xref	fd_stat_seek		; seek status
	xref	fd_stat_recal		; recalibrate status
	xref	fd_rid			; read ID
	xref	fd_newdensity		; set new density

	xref	fd_p2ms 		; pause 2 milliseconds

	include 'dev8_keys_err'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_fd_keys'

;+++
; This routine seeks before a write operation
; It ensures that, except for track 0, all seeks are inwards
;
;	d0  r	error code
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0, ERR.MCHK or conventional error
;
;---
fd_seekw
	move.b	fdl_trak(a3),d0 	 ; track required
	cmp.b	fdf_ctrk(a4),d0 	 ; in or out?
	bge.s	fd_seekri		 ; ... in

	subq.b	#1,fdl_trak(a3) 	 ; one track before
	bmi.s	fdsw_rest		 ; ... track zero
	bsr.s	fd_seekri		 ; seek out
fdsw_rest
	addq.b	#1,fdl_trak(a3)

;+++
; This routine seeks and reads a sector ID
;
;	d0  r	error code
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0, ERR.MCHK or conventional error
;
;---
fd_seekri
	move.b	fdl_trak(a3),d0
	bsr.s	fd_seek 		 ; seek to track required
	bne.s	fdsr_rts

	jsr	fd_rid			 ; read ID
	bne.s	fdsr_rts		 ; ... oops

	move.b	fdl_trak(a3),d0
fdsr_check
	sub.b	fdf_ctrk(a4),d0 	 ; the right track?
	beq.s	fdsr_rts

	moveq	#fdcs.seke,d0
fdsr_rtd0
	tst.l	d0
fdsr_rts
	rts

;+++
; This routine steps in by 5 tracks (recalibrate error recovery)
;
;	d0  r	error code
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0, ERR.MCHK or conventional error
;
;---
fd_stepin5
	st	fdf_ctrk(a4)		 ; rubbish track
	jsr	fd_cmd_stin		 ; step in command
	beq.l	fd_stat_seek		 ; wait for status
	rts

;+++
; This routine seeks to fdl_trak
;
;	d0  r	error code
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0, ERR.MCHK or conventional error
;
;---
fd_seek
	addq.b	#1,fdf_serr(a4) 	 ; increment seek error allowance
	bcc.s	fds_seek
	st	fdf_serr(a4)		 ; limit to $ff
fds_seek
	move.b	fdl_trak(a3),fdf_ctrk(a4) ; we are here

	bclr	#7,fdl_eras(a3) 	 ; erase on?
	beq.s	fds_do
	jsr	fd_p2ms 		 ; pause 2 milliseconds
fds_do
	jsr	fd_cmd_seek		 ; seek command
	beq.l	fd_stat_seek		 ; wait for status
	st	fdf_ctrk(a4)		 ; rubbish track
	rts

;+++
; This routine recalibrates and reads a sector ID.
; If the sector ID cannot be read and the fdl_ridd flag is set
; it tries other densities
;
;	d0  r	error code
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0, ERR.MCHK or conventional error
;
;---
fd_recalri
	bsr.s	fd_recal		 ; recalibrate
	bne.s	fdsr_rts		 ; ... oops

	jsr	fd_rid			 ; read ID
	beq.s	fdsr_check		 ; ... OK, check track
	blt.s	fdsr_rts
	tst.b	fdl_ridd(a3)		 ; try other densities?
	beq.s	fdsr_rtd0		 ; ... no

	move.b	ddf_density(a4),fdl_buff(a3) ; save density
fdrr_density
	jsr	fd_newdensity		 ; set new density
	move.b	ddf_density(a4),d0
	cmp.b	fdl_buff(a3),d0 	 ; back to current density?
	beq.s	fdrr_nadd		 ; ... yes, no address mark at all
	jsr	fd_rid			 ; read ID
	beq.s	fdsr_check		 ; ... OK, check track
	blt.s	fdsr_rts
	bra.s	fdrr_density		 ; try other densities

fdrr_nadd
	moveq	#fdcs.nadd,d0
	rts

;+++
; This routine recalibrates.
;
;	d0  r	error code
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0, ERR.MCHK or conventional error
;
;---
fd_recal
	bclr	#7,fdl_eras(a3) 	 ; erase on?
	beq.s	fdr_do
	jsr	fd_p2ms 		 ; pause 2 milliseconds
fdr_do
	clr.b	fdf_ctrk(a4)		 ; we should be here
	jsr	fd_cmd_recal		 ; recalibrate command
	bne.s	fdrc_notrack
	jsr	fd_stat_recal		 ; wait for completion
	beq.s	fdrc_rts

	jsr	fd_cmd_recal		 ; recalibrate command
	bne.s	fdrc_notrack
	jsr	fd_stat_recal		 ; wait for completion
	beq.s	fdrc_rts

fdrc_notrack
	st	fdf_ctrk(a4)		 ; where are we?
	moveq	#err.mchk,d0
fdrc_rts
	rts

	end
