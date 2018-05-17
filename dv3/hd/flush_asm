; DV3 Standard Hard Disk Flush Operations	 1993	   Tony Tebby

	section dv3

	xdef	hd_fflush
	xdef	hd_dflush
	xdef	hd_fstatus

	include 'dev8_keys_err'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'

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
hd_dflush
	tst.b	ddf_mupd(a4)		 ; map updated?
	bne.s	hdf_flush		 ; ... yes, flush right now
	moveq	#-1,d0			 ; urgent flush
;+++
; This routine is called to flush all buffers for a file.
;
; Note that it is impossible for a map to be updated without at
; least part of a directory being modified afterwards.
;
; This, therefore, checks the slave block range only
;
;	d0 cr	non zero for urgent flush
;	d6 c  p file ID
;	d7 c  p drive ID / number
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	all registers except d0 preserved
;	status return standard
;---
hd_fflush
	tst.l	ddf_slbh(a4)		 ; anything to slave?
	beq.s	hdf_ok			 ; ... no, range zapped
hdf_do
	tst.w	d0			 ; type of flush?
	beq.s	hdf_wait		 ; ... unimportant
hdf_flush
	st	hdl_freq(a3)		 ; flush required!
	move.b	#hdl.flsh,hdl_actm(a3)	 ; flush right soon
	bra.s	hdf_nc
hdf_wait
	st	hdl_freq(a3)		 ; flush required!
	tst.b	hdl_actm(a3)		 ; counting?
	bne.s	hdf_nc			 ; ... yes
	move.b	hdl_apnd(a3),hdl_actm(a3) ; count is normal
hdf_nc
	moveq	#err.nc,d0
	rts
hdf_ok
	moveq	#0,d0
	rts

;+++
; This routine is called to check if pending operations are complete.
;
;	d1 cr	0 or -1 (not complete)
;	d2 cr	0 or -1 (not complete)
;	d7 c  p drive ID / number
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return standard
;---
hd_fstatus
	moveq	#0,d0			 ; non urgent
	bsr.s	hd_fflush
	move.l	d0,d1
	move.l	d0,d2
	rts
	end
