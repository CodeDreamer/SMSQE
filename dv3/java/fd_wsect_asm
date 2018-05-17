; DV3 Java floppy disk read sector
; v. 0.00  2014 May 01 06:53:21
; Wolfgang Lenerz
;
; based on

; DV3 QXL Floppy Disk Write Sector      1993	  Tony Tebby
; 2006.10.20	1.01	BLAT macro definitions commented out - macro wasn't used (wl)

	section dv3

	xdef	fd_wdirect		; direct write sector
	xdef	fd_wsint		; internal write sector

	xref	fd_hold 		; reserve the interface
	xref	fd_release		; release the interface
	xref	fd_ckrw 		; read root sector and check

	include 'dev8_keys_err'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_fd_keys'
	include 'dev8_smsq_qxl_keys'
	include 'dev8_smsq_qxl_comm_keys'
	include 'dev8_mac_assert'
	include 'dev8_keys_java'


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
;	status return 0 or ERR.MCHK
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
;	status return 0 or ERR.MCHK
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
	move.l	d0,-(a7)
	moveq	#jt8.wsec,d0
	dc.w	jva.trp8		; java side handles the rest, including setting the regs on return
	addq.l	#4,a7
	tst.l	d0
fdw_rts
	rts

	end
