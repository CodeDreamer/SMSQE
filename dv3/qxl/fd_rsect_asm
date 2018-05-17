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
	xref	qxl_mess_add

	include 'dev8_keys_err'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_fd_keys'
	include 'dev8_smsq_qxl_keys'
	include 'dev8_smsq_qxl_comm_keys'
	include 'dev8_mac_assert'

*blat macro blv
* xref	 blatt
* move.b [blv],-(sp)
* jsr	 blatt
* add.w  #2,sp
* endm
*
*blatl macro blv
* xref	 blattl
* move.l [blv],-(sp)
* jsr	 blattl
* addq.l #4,sp
* endm

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
fdr.reg reg	a1/a2
stk_a1	equ	0
	movem.l fdr.reg,-(sp)
	move.l	qxl_message,a1
	lea	qxl_ms_phys+qxl_ms_len(a1),a1 ; length of message

	move.w	#8,(a1)+		 ; message length
	move.b	#qxm.rphys,(a1)+	 ; ... message key
	move.b	d7,(a1)+		 ; flp drive
	move.w	#1,(a1)+		 ; only one message at a time
	move.l	d0,(a1) 		 ; file position
	subq.l	#6,a1
	jsr	qxl_mess_add		 ; add the message
fdr_wait_done
; blat #$aa
	tst.w	(a1)			 ; wait for reply (flagged by -ve len)
	bpl.s	fdr_wait_done
; blat #$55

	tst.b	qxm_err-qxl_ms_key(a1)	 ; error?
	bne.s	fdr_error

	addq.l	#qxm_rdata+2,a1
	move.l	stk_a1(sp),a2		 ; OK, copy data
	moveq	#512/16-1,d0

fdr_loop
	move.l	(a1)+,(a2)+
	move.l	(a1)+,(a2)+
	move.l	(a1)+,(a2)+
	move.l	(a1)+,(a2)+
	dbra	d0,fdr_loop

	clr.l	qxl_ms_len-qxm_rdata-512(a1)  ; ready for next message

	moveq	#0,d0
fdr_exit
	movem.l (sp)+,fdr.reg
	rts

fdr_error
	clr.l	(a1)			 ; ready for next message
	moveq	#err.mchk,d0
	bra.s	fdr_exit

	end
