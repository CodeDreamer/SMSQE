; DV3 Standard Floppy Disk Flush Operations	   1993     Tony Tebby

	section dv3

	xdef	fd_fflush
	xdef	fd_dflush
	xdef	fd_fstatus

	include 'dev8_keys_err'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_fd_keys'

;+++
; This routine is called to flush all buffers for a drive
;
;	d7 c  p drive ID / number
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	all registers except d0 preserved
;	status return standard
;---
fd_dflush
	tst.b	ddf_mupd(a4)		 ; map updated?
	bne.s	fdf_flusha		 ; ... yes, flush now
	tst.l	ddf_slbh(a4)		 ; anything to slave?
	bne.s	fdf_flusha		 ; ... yes, flush now
fdf_ok
	moveq	#0,d0
	rts

;+++
; This routine is called to flush all buffers for a file.
;
; Note that it is impossible for a map to be updated without at
; least part of a directory being modified afterwards.
;
; This, therefore, checks the slave block range only
;
;	d0 cr	(word) non zero for urgent flush
;	d6 c  p file ID
;	d7 c  p drive ID / number
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	all registers except d0 preserved
;	status return standard
;---
fd_fflush
	tst.l	ddf_slbh(a4)		 ; anything to slave?
	beq.s	fdf_ok			 ; ... no, range zapped
fdf_do
	tst.w	d0			 ; type of flush?
	beq.s	fdf_wait		 ; ... unimportant
fdf_flusha
	st	fdl_freq(a3)		 ; flush required!
	move.b	#fdl.flsh,fdl_actm(a3)	 ; flush right soon
	bra.s	fdf_nc
fdf_wait
	st	fdl_freq(a3)		 ; flush required!
	tst.b	fdl_actm(a3)		 ; counting?
	bne.s	fdf_nc			 ; ... yes
	move.b	fdl_apnd(a3),fdl_actm(a3) ; count is normal
fdf_nc
	moveq	#err.nc,d0
	rts

;+++
; This routine is called to check if pending operations are complete.
;
;	d1 cr	0 or -1 (not complete)
;	d2 cr	0 or -1 (not complete)
;	d3 c	timeout
;	d7 c  p drive ID / number
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return standard
;---
fd_fstatus
	move.w	d3,d0			 ; non urgent for check without wait
	bsr.s	fd_fflush
	move.l	d0,d1
	move.l	d0,d2
	rts
	end
