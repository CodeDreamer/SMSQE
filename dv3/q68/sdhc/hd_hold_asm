; DV3 Q68  Hold / Release sector transfer buffer  1.02	 2017 - 2020 W. Lenerz

; based on

; DV3 QXL Hard Disk Hold / Release controller   1998	  Tony Tebby

; 1.02 mark card as recently used (adapted from MK's code for qlsd)
; 1.01 adapted for Q68, use sys_cdiu (wl)

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
	bset	#7,sys_cdiu(a6) 	 ; take the sector transfer buffer and
	bne.s	hdh_nc			 ; ... lock access to the card
	cmp.b	d0,d0
hdh_rts
	rts

hdh_nc
	moveq	#err.nc,d0
	rts

;+++
; This routine is called to release the card controller
;
;	d0 c  p
;	d7 c  p drive ID / number
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return accoding to d0
;---
hd_release
	tst.l	d0				; error?
	bne.s	 release			; yes, don't mark card as used

	movem.l d7/a2,-(sp)
	lea	hdl_unit-1(a3),a2		; card nbrs for drives
	move.b	(a2,d7.w),d7			; card nbr (1 or 2))
	beq.s	crdset				; card 0
	moveq	#1,d7				; card 1
crdset	bset	d7,ddl_rcnt(a3) 		; mark card as recently used
	movem.l (sp)+,d7/a2


release move.b	hdl_apnd(a3),hdl_actm(a3)	; ... let it go
	bclr	#7,sys_cdiu(a6) 		; release the sector xfer buffer & card
	tst.l	d0
	rts

	end
