; DV3 QPC Floppy Disk Read Sector      1993	 Tony Tebby
;				       2000	 Marcel Kilgus

	section dv3

	xdef	fd_rdirect		; direct read sector
	xdef	fd_rsint		; internal read sector
	xdef	fd_ckroot		; internal check root sector
	xdef	fd_rroot		; internal read root sector

	xref	fd_hold 		; reserve the interface
	xref	fd_release		; release the interface
	xref	fd_ckrw 		; read root sector and check

	include 'dev8_keys_err'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_fd_keys'
	include 'dev8_smsq_qpc_keys'
	include 'dev8_mac_assert'

;+++
; This routine reads a sector from a Floppy disk for direct sector IO
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
fd_rdirect
	jsr	fd_hold 		 ; hold
	beq.s	fdr_doread
fdr_rts
	rts

;+++
; This routine reads a sector from a Floppy disk at an even address
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
fd_rsint
	jsr	fd_hold 		 ; hold
	bne.s	fdr_rts

	btst	d7,fdl_stpb(a3) 	 ; drive stopped?
	bne.s	fdr_doread		 ; ... no

	jsr	fd_ckrw
	bne.s	fdr_done

fdr_doread
	bsr.s	fdr_read

fdr_done
	jmp	fd_release		 ; release

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
	bsr.s	fd_ckroot		 ; read root sector
	bra.s	fdr_done

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
fdr_read
	move.w	d2,-(a7)
	move.w	ddf_strk(a4),d2 	 ; sectors per track
	dc.w	qpc.frsec
	move.w	(a7)+,d2
	tst.l	d0
	rts

	end
