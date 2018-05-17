; DV3 Standard Floppy Disk write Sector      1998     Tony Tebby

	section dv3

	xdef	fd_wdirect		; direct sector write
	xdef	fd_wsint		; write sector (internal)

	xref	fd_hold 		; reserve the controller
	xref	fd_release		; release the controller
	xref	fd_select		; select drive
	xref	fd_ckrw 		; read root sector and check
	xref	fd_seekw		; seek to track for write
	xref	fd_wphys		; write sector (physical)
	xref	fd_reseek		; re-seek after error

	xref	dv3_slen		; set sector length

	include 'dev8_keys_sys'
	include 'dev8_keys_err'
	include 'dev8_keys_qlhw'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_fd_keys'
	include 'dev8_mac_assert'

;+++
; This routine write sectors to a Floppy disk for direct sector IO
;
;	d0 cr	cylinder + side + sector / error code
;	d2 c  p number of sectors
;	d7 c  p drive ID / number
;	a1 c  p address to write from
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0, ERR.NC or ERR.MCHK
;
;---
fd_wdirect
	jsr	fd_hold 		 ; hold and select
	bne.s	fdw_rts
	bsr.s	fdw_writes		 ; write
	jmp	fd_release		 ; and release

;+++
; This routine writes sectors to a Floppy disk for internal operations
;
;	d0 cr	cylinder + side + sector / error code
;	d2 c  p number of sectors
;	d7 c  p drive ID / number
;	a1 c  p address to write from
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0, ERR.NC or ERR.MCHK
;
;---
fd_wsint
	cmp.b	fdl_selc(a3),d7 	 ; drive selected?
	beq.s	fdw_ckrw		 ; ... yes

	move.l	d0,-(sp)
	jsr	fd_select		 ; select
	move.l	(sp)+,d0

fdw_ckrw
	btst	d7,fdl_stpb(a3) 	 ; drive stopped?
	bne.s	fdw_dowrite		 ; ... no

	jsr	fd_ckrw 		 ; check disk not changed
	bne.s	fdw_rts

fdw_dowrite

;+++
; Write sector with seek error recovery
;
;	d0 cr	sector to write / error code
;	d2 cp	number of sectors to write
;	d7 c  p drive ID / number
;	a1 c  p address to write from
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0 or ERR.MCHK
;
;---
fdw_writes
	move.l	d0,fdl_sadd(a3) 	 ; sector required
	bsr.s	fdw_write		 ; write sector
	ble.s	fdw_rts 		 ; ... OK or bad medium
	jsr	fd_reseek		 ; re-seek
	bne.s	fdw_mchk		 ; ... bad medium
	bsr.s	fdw_writen		 ; re-write
	beq.s	fdw_rts 		 ; OK
fdw_mchk
	moveq	#err.mchk,d0
fdw_rts
	rts


;+++
; (Seek and) write sector with write error recovery
;
;	d0  r	error code
;	d2 cp	number of sectors to write
;	d7 c  p drive ID / number
;	a1 c  p address to write from
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0, ERR.MCHK or positive conventional status return
;
;---
fdw_write
	move.b	fdl_trak(a3),d0 	 ; track required
	cmp.b	fdf_ctrk(a4),d0 	 ; the right track?
	beq.s	fdw_writen		 ; ... yes
	jsr	fd_seekw		 ; ... no, seek


;+++
; write sector (no seek) with write error recovery
;
;	d0  r	error code
;	d2 cp	number of sectors to write
;	d7 c  p drive ID / number
;	a1 c  p address to write from
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0, ERR.MCHK or positive conventional status return
;
;---
fdw_writen
fdw_try1
	jsr	fd_wphys		 ; physical write sector
	ble.s	fdw_rts
	assert	fdcs.orun,fdcs.derr-1,fdcs.seke-2,fdcs.nadd-3
	cmp.b	#fdcs.derr,d0
	blt.s	fdw_try1		 ; always retry overrun
	bgt.s	fdw_rts 		 ; seek error or no address mark


fdw_try2				 ; retry data error once
	jsr	fd_wphys		 ; physical write sector
	ble.s	fdw_rts
	assert	fdcs.orun,fdcs.derr-1,fdcs.seke-2,fdcs.nadd-3
	cmp.b	#fdcs.orun,d0
	beq.s	fdw_try2		 ; always retry overrun
	rts

	end
