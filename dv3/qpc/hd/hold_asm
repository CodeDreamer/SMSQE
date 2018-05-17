; DV3 QPC Hard Disk Hold / Release controller   1998	  Tony Tebby

	section dv3

	xdef	hd_hold
	xdef	hd_release

	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'
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
hd_hold
	bset	#7,sys_stiu(a6) 	 ; take the sector transfer buffer
	bne.s	hdh_nc
	cmp.b	d0,d0
hdh_rts
	rts

hdh_nc
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
hd_release
	move.b	hdl_apnd(a3),hdl_actm(a3)   ; ... let it go
	bclr	#7,sys_stiu(a6) 	 ; relase the sector transfer buffer
	tst.l	d0
	rts

	end
