; DV3 Java floppy disk read sector
; v. 0.00  2014 May 01 06:53:21
; Wolfgang Lenerz
;
; based on
; DV3 QXL Floppy Disk Read Sector      1993	 Tony Tebby
; 2006.10.20	1.01	BLAT macro definitions commented out - macro wasn't used (wl)

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
	include 'dev8_smsq_qxl_keys'
	include 'dev8_smsq_qxl_comm_keys'
	include 'dev8_mac_assert'
	include 'dev8_keys_java'

;+++
; This routine reads a sector from a Floppy disk for direct sector IO
;
;	d0 cr	cylinder + side + sector / error code
;	d2 c  p number of sectors
;	d7 c  p drive ID / number
;	a1 c  p address to read into
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0 or ERR.MCHK
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
;	d2 c  p number of sectors
;	d7 c  p drive ID / number
;	a1 c  p address to read into
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0 or ERR.MCHK
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
	move.l	d0,-(a7)
	moveq	#jt8.rsec,d0
	dc.w	jva.trp8		; java side handles the rest, including setting the regs on return
	addq.l	#4,a7
	tst.l	d0

	rts
	end
