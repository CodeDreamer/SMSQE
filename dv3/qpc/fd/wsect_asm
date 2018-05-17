; DV3 QPC Floppy Disk Write Sector      1993	  Tony Tebby
;				        2000	  Marcel Kilgus

	section dv3

	xdef	fd_wdirect		; direct write sector
	xdef	fd_wsint		; internal write sector

	xref	fd_hold 		; reserve the interface
	xref	fd_release		; release the interface
	xref	fd_ckrw 		; read root sector and check

	include 'dev8_keys_err'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_fd_keys'
	include 'dev8_smsq_qpc_keys'
	include 'dev8_mac_assert'

;+++
; This routine writes a sector to a Floppy disk for direct sector IO
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
fd_wdirect
	jsr	fd_hold 		 ; hold
	bne.s	fdw_rts
	bsr.s	fdw_write		 ; write sector
fdw_done
	jmp	fd_release		 ; and release

;+++
; This routine writes a sector to a Floppy disk for internal operations
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
fd_wsint
	btst	d7,fdl_stpb(a3) 	 ; drive stopped?
	bne.s	fdw_dowrite		  ; ... no

	jsr	fd_ckrw 		 ; check for change
	bne.s	fdw_done

fdw_dowrite

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
fdw_write
	move.w	d2,-(a7)
	move.w	ddf_slen(a4),d2
	dc.w	qpc.fwsec
	move.w	(a7)+,d2
	tst.l	d0
fdw_rts
	rts

	end
