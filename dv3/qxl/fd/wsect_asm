; DV3 QXL Floppy Disk Write Sector      1993	  Tony Tebby
; 2006.10.20	1.01	BLAT macro definitions commented out - macro wasn't used (wl)

	section dv3

	xdef	fd_wdirect		; direct write sector
	xdef	fd_wsint		; internal write sector

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
fdw.reg reg	a1/a2
stk_a1	equ	0
	movem.l fdw.reg,-(sp)
	move.l	qxl_message,a2
	lea	qxl_ms_phys+qxl_ms_len(a2),a2 ; length of message

	move.w	#$208,(a2)+		 ; message length
	move.b	#qxm.wphys,(a2)+	 ; ... message key
	move.b	d7,(a2)+		 ; flp drive
	move.w	#1,(a2)+		 ; only one message at a time
	move.l	d0,(a2)+		 ; file position

	moveq	#512/16-1,d0
fdw_loop
	move.l	(a1)+,(a2)+
	move.l	(a1)+,(a2)+
	move.l	(a1)+,(a2)+
	move.l	(a1)+,(a2)+
	dbra	d0,fdw_loop

	lea	-($208+2)(a2),a1	 ; back to start
	jsr	qxl_mess_add		 ; add the message
fdw_wait_done
; blat #$aa
	tst.w	(a1)			 ; wait for reply (flagged by -ve len)
	bpl.s	fdw_wait_done
; blat #$55

	tst.b	qxm_err-qxl_ms_len(a1)	 ; error?
	bne.s	fdw_error

	clr.l	(a1)			 ; ready for next message

	moveq	#0,d0
fdw_exit
	movem.l (sp)+,fdw.reg
fdw_rts
	rts

fdw_error
	clr.l	(a1)			 ; ready for next message
	moveq	#err.mchk,d0
	bra.s	fdw_exit

	end
