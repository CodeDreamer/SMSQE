; DV3 QLSD Hold / Release controller				 2018 M. Kilgus
;
; 2018-06-10  1.01  Mark card as recently used on release (MK)

	section dv3

	xdef	hd_hold
	xdef	hd_release

	include 'dev8_dv3_qlsd_keys'
	include 'dev8_keys_sys'
	include 'dev8_keys_err'
	include 'dev8_mac_assert'

;+++
; This routine is called to take the disk controller
;
;	d0 cr p preserved if OK
;	d7 c  p drive ID / number
;	a3 c  p linkage block
;	a6 c  p system variables
;
;	status return 0, err.nc, err.mchk
;---
hd_hold
	bset	#7,sys_qlsd(a6) 	; prevent poll from accessing device
	bne.s	hdh_nc
	cmp.b	d0,d0
hdh_rts
	rts

hdh_nc
	moveq	#err.nc,d0		; keep trying
	rts

;+++
; This routine is called to release the disk controller
;
;	d0 c  p
;	d7 c  p drive ID / number
;	a3 c  p linkage block
;	a6 c  p system variables
;
;	status return accoding to d0
;---
hd_release
	tst.l	d0				; error?
	bne.s	release_device			; yes, don't mark card as used
	bclr	#7,d7				; direct card access?
	bne.s	release_device			; yes, don't mark as used (by fs)

	movem.l d7/a2,-(sp)
	lea	hdl_unit-1(a3),a2		; offset >8 bit, so explicit lea
	move.b	(a2,d7.w),d7			; card nbr (1...3)
	bset	d7,qlsd_crdused(a3)		; mark card as used
	movem.l (sp)+,d7/a2

release_device
	move.b	hdl_apnd(a3),hdl_actm(a3)	; ... let it go
	bclr	#7,sys_qlsd(a6) 		; release device
	tst.l	d0
	rts

	end
