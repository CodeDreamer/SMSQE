; DV3 Standard Floppy Disk Seek Error Recovery	  1993     Tony Tebby

	section dv3

	xdef	fd_reseek
	xdef	fd_reseek0

	xref	fd_seekri		; seek with up to 2 read IDs
	xref	fd_recalri		; recalibrate with up to 2 read IDs
	xref	fd_stepin5		; step in 5 tracks
	xref	fd_srred		; reduce step rate
	xref	fd_srredp		; reduce step rate permanently
	xref	fd_srmin		; minimum step rate
	xref	fd_srrest		; restore step rate
	xref	fd_wait 		; wait n ticks

	include 'dev8_keys_err'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_fd_keys'

;+++
; This routine reseeks to fdl_trak after a read / write error
;
;	d0  r	error code
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0 or ERR.MCHK
;
;---
fd_reseek
	tst.b	fdl_trak(a3)		 ; trying to read from track 0?
	beq.s	fd_reseek0		 ; ... yes

	bsr.s	fd_reseek0		 ; try recalibrating
	blt.s	fdrs_exit		 ; ... bad

	jsr	fd_seekri		 ; now re-seek
	ble.s	fdrs_exit		 ; ... OK or bad medium

fdrs_srate
	jsr	fd_srred		 ; reduce step rate
	blt.s	fdrs_srrest		 ; ... no more

	bsr.s	fd_reseek0		 ; try recalibrating
	blt.s	fdrs_srrest		 ; ... bad

	jsr	fd_seekri		 ; now re-seek
	blt.s	fdrs_srrest		 ; ... bad medium
	bgt.s	fdrs_srate		 ; try another step rate

	sub.b	#fdf.srec,fdf_serr(a4)	 ; seek error recovery record
	bhi.s	fdrs_srrest		 ; ... isolated error
	jsr	fd_srredp		 ; step rate reduced permanently
	st	fdf_serr(a4)		 ; restart count
	bra.s	fdrs_d0

fdrs_srrest
	jsr	fd_srrest		 ; restore step rate

fdrs_d0
	ble.s	fdrs_exit		 ; ... OK or bad medium
	moveq	#err.mchk,d0

fdrs_exit
	rts

;+++
; This routine seeks to track 0 with retries
;
;	d0  r	error code
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0 or ERR.MCHK
;
;---
fd_reseek0
	jsr	fd_recalri		 ; recalibrate and read ID
	ble.s	fdrs_exit		 ; ... OK or bad medium

	jsr	fd_stepin5		 ; setp in 5 tracks
	jsr	fd_recalri		 ; recalibrate and read ID
	ble.s	fdrs_exit		 ; ... OK or bad medium

	jsr	fd_stepin5		 ; setp in 5 tracks
	moveq	#3,d0
	jsr	fd_wait 		 ; pause 50 ms
	jsr	fd_recalri		 ; recalibrate and read ID
	ble.s	fdrs_exit		 ; ... OK or bad medium


	move.l	fdl_tstep(a3),-(sp)	 ; current temporary step rates
	jsr	fd_srmin		 ; try minimum step rate
	jsr	fd_stepin5		 ; setp in 5 tracks
	moveq	#3,d0
	jsr	fd_wait 		 ; pause 50 ms
	jsr	fd_recalri		 ; recalibrate and read ID
	move.l	(sp)+,fdl_tstep(a3)	 ; restore temporary step rates
	move.l	d0,-(sp)		 ; save error code
	jsr	fd_srrest		 ; restore step rate
	move.l	(sp)+,d0
	bra.s	fdrs_d0

	end
