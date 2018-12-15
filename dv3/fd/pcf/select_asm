; DV3 PC Compatible Floppy Disk Select Drive	 1998	   Tony Tebby

	section fd

	xdef	fd_select		; select
	xdef	fd_reset		; reset/reselect controller
	xdef	fd_newdensity		; re-select with new density
	xdef	fd_start		; start drive
	xdef	fd_deselect		; deselect

	xref	fd_srate
	xref	fd_srtemp
	xref	fd_result
	xref	fd_wait

	xref.l	fdc_dctl
	xref.l	fdc_rate
	xref.s	fdc.mots

	include 'dev8_dv3_keys'
	include 'dev8_dv3_fd_keys'
	include 'dev8_dv3_fd_pcf_keys'
	include 'dev8_mac_assert'

	 dc.b	fdc.rsd 			   ; assumed rate (density -1)
fdsl_rat dc.b	fdc.rsd,fdc.rsd,fdc.rhd,fdc.red    ; 250k, 250k, 500k, 1M rate
	 dc.b	0

fds.reset equ	8	; .14-.16 sec to reset

;+++
; Set new density.
;
;	d7 c  p drive number (1...)
;	a3 c  p linkage block
;	a4 c  p drive definition block
;
;	status return zero
;---
fd_newdensity
	move.b	ddf_density(a4),d0	 ; current density
	subq.b	#1,d0
	bpl.s	fdn_set 		 ; ... OK
	move.b	fdl_mxdns(a3),d0	 ; ... restart at max
fdn_set
	move.b	d0,ddf_density(a4)	 ; set new density

;+++
; Reset controller and and reselect drive
;
;	d7 c  p drive number (1...)
;	a3 c  p linkage block
;	a4 c  p drive definition block
;
;	status return OK
;---
fd_reset
	moveq	#fds.reset,d0		 ; reset time
	bsr.s	fds_modes
	jmp	fd_srtemp		 ; set the step rate again

;+++
; Select disk drive. Sets FDL_SELC. Blats FDL_CTRK.
;
;	d7 c  p drive number (1...)
;	a3 c  p linkage block
;	a4 c  p drive definition block
;
;	status return zero or err.mchk
;---
fd_select
	moveq	#0,d0			 ; ... no reset
	bsr.s	fds_modes		 ; set mode and select
	jsr	fd_srate		 ; set step rate
	bne.s	fdsl_rts

	tst.b	fdl_selc(a3)		 ; is a drive selected?
	bne.s	fdsl_sel
	move.b	fdl_rnup(a3),fdl_drvs(a3) ; runup time

fdsl_sel
	move.b	d7,fdl_selc(a3) 	 ; this drive is selected
	st	fdf_ctrk(a4)		 ; track unknown
	moveq	#0,d0
fdsl_rts
	rts
;+++
; Set drive mode dependent registers and select drive
;
;	d0 cr	pause after reset (0 for no reset)
;	d7 c  p drive number (1...)
;	a3 c  p linkage block
;	a4 c  p drive definition block
;
;	status return OK
;---
fds_modes
	move.l	a0,-(sp)
	move.l	d1,-(sp)
	lea	fdc_dctl,a0

	move.b	ddf_density(a4),d1	 ; density 0,1=std, 2=high, 3=extended
	ext.w	d1
	move.b	fdsl_rat(pc,d1.w),fdc_rate-fdc_dctl(a0) ; set data rate

	add.w	#fdl_sltab-1,d7
	move.b	(a3,d7.w),d1		 ; select drive
	sub.w	#fdl_sltab-1,d7

	tst.b	d0			 ; any reset?
	beq.s	fdsm_set		 ; ... no

	move.b	d1,(a0) 		 ; drive control / reset
	jsr	fd_wait 		 ; wait for reset

fdsm_set
	bset	#fdc..rst,d1
	move.b	d1,(a0) 		 ; drive control / unreset

	move.l	(sp)+,d1
	move.l	(sp)+,a0
fdsl_ok
	moveq	#0,d0
	rts

;+++
; Deselect disk drive. Clears FDL_SELC.
;
;	a3 c  p linkage block
;
;	Status return OK
;
;---
fd_deselect
	moveq	#fdc.desl,d0
fd_setmc
	move.b	d0,fdc_dctl		 ; clear out drive control but no reset
	sf	fdl_selc(a3)		 ; nothing selected
	bra.s	fdsl_ok
;+++
; Start disk drive (no drive selected) - a bit like deselect!
; Clears FDL_SELC.
;
;	a3 c  p linkage block
;
;	status return OK
;---
fd_start
	moveq	#fdc.mots,d0		  ; set motor on and reset
	move.b	fdl_rnup(a3),fdl_drvs(a3) ; run-up status
	bra.s	fd_setmc

	end
