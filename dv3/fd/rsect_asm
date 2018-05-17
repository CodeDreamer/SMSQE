; DV3 Standard Floppy Disk Read Sector	    1998     Tony Tebby

	section dv3

	xdef	fd_rdirect		; direct sector read
	xdef	fd_rsint		; read sector (internal)
	xdef	fd_ckroot		; internal check root sector
	xdef	fd_rroot		; internal read root sector

	xref	fd_hold 		; reserve the controller
	xref	fd_release		; release the controller
	xref	fd_ckrw 		; read root sector and check
	xref	fd_seek 		; seek to track
	xref	fd_rphys		; read sector (physical)
	xref	fd_reseek		; re-seek after error
	xref	fd_reseek0		; recalibrate with error recovery

	xref	dv3_slen		; set sector length

	include 'dev8_keys_sys'
	include 'dev8_keys_err'
	include 'dev8_keys_qlhw'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_fd_keys'
	include 'dev8_mac_assert'

;+++
; This routine read sectors from a Floppy disk for direct sector IO
;
;	d0 cr	cylinder + side + sector / error code
;	d2 c  p number of sectors
;	d7 c  p drive ID / number
;	a1 c  p address to read into
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0, ERR.NC or ERR.MCHK
;
;---
fd_rdirect
	jsr	fd_hold 		 ; hold and select
	beq.s	fdr_doread
	rts

;+++
; This routine reads sectors from a Floppy disk for internal operations
;
;	d0 cr	cylinder + side + sector / error code
;	d2 c  p number of sectors
;	d7 c  p drive ID / number
;	a1 c  p address to read into
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0, ERR.NC or ERR.MCHK
;
;---
fd_rsint
	jsr	fd_hold 		 ; hold and select
	bne.s	fdr_rts

	btst	d7,fdl_stpb(a3) 	 ; drive stopped?
	bne.s	fdr_doread		 ; ... no

	jsr	fd_ckrw 		 ; check disk not changed
	bne.s	fdr_done

fdr_doread
	bsr.s	fdr_reads		 ; read with seek error recovery

fdr_done
	jmp	fd_release		 ; ... let it go

;+++
; This routine reads the root sector to check for new medium
;
;	d0  r	error code
;	d2   s
;	d7 c  p drive ID / number
;	a1 c  p address to read into
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0, ERR.NC or ERR.MCHK
;
;---
fd_rroot
	jsr	fd_hold 		 ; hold and select
	bne.s	fdr_rts

	tst.b	ddf_lock(a4)		 ; locked?
	beq.s	fdrr_new		 ; ... no, could be new medium
	bsr.s	fd_ckroot		 ; ... yes, do not change density
	bra.s	fdr_done

fdrr_new
	moveq	#1,d2			 ; number of sectors
	move.l	d2,fdl_sadd(a3) 	 ; ... and the root sector number!

	st	fdl_ridd(a3)		 ; retry read ID with different densities
	jsr	fd_reseek0		 ; find track 0 and density
	sf	fdl_ridd(a3)
	bne.s	fdr_done		 ; bad

	move.b	fdf_slen(a4),ddf_slflag(a4) ; set sector length flag
	jsr	dv3_slen

	bsr.s	fdr_readn		 ; read with read error recovery
	ble.s	fdr_done		 ; ... OK or bad medium
	pea	fdr_done
	bra.s	fdr_readn		 ; try again

;+++
; Check root sector (from fd_ckwr called from within read / write sector)
;
;	d0  r	error code
;	d2   s
;	d7 c  p drive ID / number
;	a1 c  p address to read into
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0 or ERR.MCHK
;
;---
fd_ckroot
	moveq	#1,d0
	moveq	#1,d2			 ; one sector only

;+++
; Read sector with seek error recovery
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
fdr_reads
	move.l	d0,fdl_sadd(a3) 	 ; sector required

	bsr.s	fdr_read		 ; read sector
	ble.s	fdr_rts 		 ; ... OK or bad medium
	jsr	fd_reseek		 ; re-seek
	bne.s	fdr_mchk		 ; ... bad medium
	bsr.s	fdr_readn		 ; re-read
	beq.s	fdr_rts 		 ; OK
fdr_mchk
	moveq	#err.mchk,d0
fdr_rts
	rts


;+++
; (Seek and) read sector with read error recovery
;
;	d0  r	error code
;	d2 cp	number of sectors to read
;	d7 c  p drive ID / number
;	a1 c  p address to read into
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0, ERR.MCHK or positive conventional status return
;
;---
fdr_read
	move.b	fdl_trak(a3),d0 	 ; track required
	cmp.b	fdf_ctrk(a4),d0 	 ; the right track?
	beq.s	fdr_readn		 ; ... yes
	jsr	fd_seek 		 ; ... no, seek


;+++
; Read sector (no seek) with read error recovery
;
;	d0  r	error code
;	d2 cp	number of sectors to read
;	d7 c  p drive ID / number
;	a1 c  p address to read into
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0, ERR.MCHK or positive conventional status return
;
;---
fdr_readn
fdr_try1
	jsr	fd_rphys		 ; physical read sector
	ble.s	fdr_rts
	assert	fdcs.orun,fdcs.derr-1,fdcs.seke-2,fdcs.nadd-3
	cmp.b	#fdcs.derr,d0
	blt.s	fdr_try1		 ; always retry overrun
	bgt.s	fdr_rts 		 ; seek error or no address mark


fdr_try2				 ; retry data error once
	jsr	fd_rphys		 ; physical read sector
	ble.s	fdr_rts
	assert	fdcs.orun,fdcs.derr-1,fdcs.seke-2,fdcs.nadd-3
	cmp.b	#fdcs.orun,d0
	beq.s	fdr_try2		 ; always retry overrun
	rts

	end
