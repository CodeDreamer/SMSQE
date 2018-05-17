; DV3 QXL Floppy Disk Hold / Release controller   1998     Tony Tebby

	section dv3

	xdef	fd_hold
	xdef	fd_release

	include 'dev8_dv3_keys'
	include 'dev8_dv3_fd_keys'
	include 'dev8_keys_sys'
	include 'dev8_keys_err'
	include 'dev8_mac_assert'

;+++
; This routine is called to take the disk controller
;
;	d0 cr p preserved if OK
;	d7 c  p drive ID / number
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0, err.nc, err.mchk
;---
fd_hold
	bset	#7,sys_stiu(a6) 	 ; take the sector transfer buffer
	bne.s	fdh_nc
	move.b	fdl_apnd(a3),fdl_actm(a3)   ; ... reactivate when we've done
	cmp.b	d0,d0
fdh_rts
	rts

fdh_nc
	moveq	#err.nc,d0
	rts

;+++
; This routine is called to release the disk controller
;
;	d0 c  p
;	d7 c  p drive ID / number
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return accoding to d0
;---
fd_release
	bclr	#7,sys_stiu(a6) 	 ; relase the sector transfer buffer
	tst.l	d0
	rts

	end
