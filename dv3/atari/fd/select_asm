; DV3 Atari Floppy Disk Select Drive	 1998	   Tony Tebby

	section fd

	xdef	fd_select		; select drive (and side)
	xdef	fd_reset		; reset / reselect (null)
	xdef	fd_selside		; select drive / side
	xdef	fd_selside0		; select drive / side 0
	xdef	fd_newdensity		; re-select with new density
	xdef	fd_deselect		; deselect

	xref	fd_srate		; (re)set step rate
	xref	fd_fint 		; force interrupt to start drive

	include 'dev8_dv3_keys'
	include 'dev8_dv3_fd_keys'
	include 'dev8_keys_atari'
	include 'dev8_mac_assert'
	include 'dev8_keys_sys'

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
	moveq	#0,d0
	move.b	ddf_density(a4),d0	 ; current density
	subq.b	#1,d0
	bpl.s	fdn_set 		 ; ... OK
	move.b	fdl_mxdns(a3),d0	 ; ... restart at max
fdn_set
	move.b	d0,ddf_density(a4)	 ; set new density

;--------------------
fds_density
	moveq	#sys.mtyp,d0
	and.b	sys_mtyp(a6),d0
	cmp.b	#sys.mmste,d0		 ; mega STE or TT?
	blo.s	fdn_ok			 ; ... no

	cmp.b	#ddf.hd,ddf_density(a4)  ; high density?
	seq	d0			 ; ... yes
	assert	fdc.hd,3
	lsr.b	#6,d0
	move.w	d0,fdc_dens		 ; set density for this drive
fdn_ok
	moveq	#0,d0
	rts

;+++
; reset / reselect
;---
fd_reset
	move.b	fdl_selc(a3),d7 	 ; re-select current drive

;+++
; Select disk drive. Sets FDL_SELC. Blats FDF_CTRK.
;
;	d7 c  p drive number (1...)
;	a3 c  p linkage block
;	a4 c  p drive definition block
;
;	status return zero
;---
fd_select
	move.b	d7,fdl_selc(a3) 	 ; drive selected
	bsr.s	fds_selside0		 ; select drive and side 0
	bsr.s	fds_density		 ; set density
	jsr	fd_srate		 ; set step rate

	tst.b	fdl_drvs(a3)		 ; motor running?
	bge.s	fdsl_nz
	move.b	fdl_rnup(a3),fdl_drvs(a3) ; will be soon
	jsr	fd_fint 		; get it going
fdsl_nz
	st	fdf_ctrk(a4)		 ; blat track
	moveq	#0,d0
fdsl_exit
	rts
;+++
; Deselect
;
;	a3 c  p linkage block
;
;	status return OK
;---
fd_deselect
	move.l	d7,-(sp)
	moveq	#0,d7
	move.b	d7,fdl_selc(a3) 	 ; nothing selected now
	bsr.s	fds_selside0
	move.l	(sp)+,d7
	moveq	#0,d0
	rts


;+++
; Select disk drive (and side 0)
;
;	d7 c  p drive number (1...) (zero for deselect)
;	a3 c  p linkage block
;	a4 c  p drive definition block
;
;	status return OK
;---
fd_selside0
fds_selside0
	moveq	#0,d0

;+++
; Select disk side (and drive)
;
;	d0 c s	side 0 or 1
;	d7 c  p drive number (1...) (zero for deselect)
;	a3 c  p linkage block
;	a4 c  p drive definition block
;
;	status return OK
;---
fd_selside
	move.w	sr,-(sp)
	or.w	#$0700,sr		 ; atomic

	move.b	d0,-(sp)
	move.b	#fdc.ctls,fdc_ctls	 ; select control register
	moveq	#fdc.bits,d0
	or.b	fdc_ctlr,d0		 ; fdc bits high
	sub.b	(sp)+,d0		 ; set side
	bclr	d7,d0			 ; set drive
	move.b	d0,fdc_ctlw		 ; and write to port

	move.w	(sp)+,sr
	moveq	#0,d0
	rts

	end
