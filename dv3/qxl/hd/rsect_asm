; DV3 QXL Hard Disk Read Sector      1993     Tony Tebby
; 2006.10.20	1.01	BLAT macro definitions commented out - macro wasn't used (wl)
	section dv3

	xdef	hd_rdirect		; direct read sector
	xdef	hd_rsint		; internal read sector

	xref	hd_hold 		; reserve the interface
	xref	hd_release		; release the interface
	xref	qxl_mess_add

	include 'dev8_keys_err'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'
	include 'dev8_smsq_qxl_keys'
	include 'dev8_smsq_qxl_comm_keys'
	include 'dev8_mac_assert'

*blat macro blv
* xref	 blatt
* move.b [blv],-(sp)
* jsr	 blatt
* add.w  #2,sp
* endm

*blatl macro blv
* xref	 blattl
* move.l [blv],-(sp)
* jsr	 blattl
* addq.l #4,sp
* endm

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
hdr.reg reg	a1/a2
stk_a1	equ	0
	movem.l hdr.reg,-(sp)
	move.l	qxl_message,a1
	lea	qxl_ms_phys+qxl_ms_len(a1),a1 ; length of message

	move.w	#8,(a1)+		 ; message length
	move.b	#qxm.rphys,(a1)+	 ; ... message key
	move.b	d7,(a1) 		 ; win drive
	add.b	#$82,(a1)+		 ; is a file
	move.w	#1,(a1)+		 ; only one message at a time
	move.l	d0,(a1) 		 ; file position
	subq.l	#6,a1
	jsr	qxl_mess_add		 ; add the message
hdr_wait_done
; blat #$aa
	tst.w	(a1)			 ; wait for reply (flagged by -ve len)
	bpl.s	hdr_wait_done
; blat #$55

	tst.b	qxm_err-qxl_ms_len(a1)	 ; error?
	bne.s	hdr_error

	addq.l	#qxm_rdata+2,a1
	move.l	stk_a1(sp),a2		 ; OK, copy data
	moveq	#512/16-1,d0

hdr_loop
	move.l	(a1)+,(a2)+
	move.l	(a1)+,(a2)+
	move.l	(a1)+,(a2)+
	move.l	(a1)+,(a2)+
	dbra	d0,hdr_loop

	clr.l	qxl_ms_len-qxm_rdata-512(a1)	 ; ready for next message

	moveq	#0,d0
hdr_exit
	movem.l (sp)+,hdr.reg
hdr_rts
	rts

hdr_error
	clr.l	(a1)			 ; ready for next message
	moveq	#err.mchk,d0
	bra.s	hdr_exit

	end
