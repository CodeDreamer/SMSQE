; DV3 QPC Hard Disk Read Sector      1993     Tony Tebby
;				     2000     Marcel Kilgus
	section dv3

	xdef	hd_rdirect		; direct read sector
	xdef	hd_rsint		; internal read sector

	xref	hd_hold 		; reserve the interface
	xref	hd_release		; release the interface

	include 'dev8_keys_err'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'
	include 'dev8_smsq_qpc_keys'
	include 'dev8_mac_assert'

;+++
; This routine reads a sector from a hard disk for direct sector IO
;
;	d0 cr	cylinder + side + sector / error code
;	d2 c  p number of sectors (=1)
;	d7 c  p drive ID / number
;	a1 c  p address to read into
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0, ERR.NC or ERR.MCHK
;
;---
hd_rdirect

;+++
; This routine reads a sector from a hard disk at an even address
;
;	d0 cr	cylinder + side + sector / error code
;	d2 c  p number of sectors = 1
;	d7 c  p drive ID / number
;	a1 c  p address to read into
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0, ERR.NC or ERR.MCHK
;
;---
hd_rsint
	jsr	hd_hold 		 ; hold
	bne.s	hdr_rts

	bsr.s	hdr_read

hdr_done
	jmp	hd_release		 ; release

;+++
; Read sector (basic operation)
;
;	d0 cr	sector to read / error code
;	d2 cp	number of sectors to read
;	d7 c  p drive ID / number
;	a1 c  p address to read into
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0 or ERR.MCHK
;
;---
hdr_read
	dc.w	qpc.hrsec
	tst.l	d0
hdr_rts
	rts

	end
