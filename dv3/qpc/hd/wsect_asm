; DV3 QPC Hard Disk Write Sector      1993	Tony Tebby
;				      2000	Marcel Kilgus
	section dv3

	xdef	hd_wdirect		; direct write sector
	xdef	hd_wsint		; internal write sector

	xref	hd_hold 		; reserve the interface
	xref	hd_release		; release the interface

	include 'dev8_keys_err'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'
	include 'dev8_smsq_qpc_keys'
	include 'dev8_mac_assert'

;+++
; This routine writes a sector to a hard disk for direct sector IO
;
;	d0 cr	cylinder + side + sector / error code
;	d2 c  p number of sectors (=1)
;	d7 c  p drive ID / number
;	a1 c  p address to write into
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0, ERR.NC or ERR.MCHK
;
;---
hd_wdirect
	jsr	hd_hold 		 ; hold
	bne.s	hdw_rts
	bsr.s	hdw_write		 ; write sector
	jmp	hd_release		 ; and release

;+++
; This routine writes a sector to a hard disk for internal operations
;
;	d0 cr	cylinder + side + sector / error code
;	d2 c  p number of sectors = 1
;	d7 c  p drive ID / number
;	a1 c  p address to write into
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0, ERR.NC or ERR.MCHK
;
;---
hd_wsint

;+++
; write sector (basic operation)
;
;	d0 cr	sector to write / error code
;	d2 cp	number of sectors to write
;	d7 c  p drive ID / number
;	a1 c  p address to write into
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0 or ERR.MCHK
;
;---
hdw_write
	dc.w	qpc.hwsec
	tst.l	d0
hdw_rts
	rts

	end
