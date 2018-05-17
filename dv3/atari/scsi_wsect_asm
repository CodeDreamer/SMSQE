; DV3 SCSI Hard Disk Write Sector	 1993	   Tony Tebby

	section dv3

	xdef	sc_wsect		 ; write sector to ACSI disk
	xdef	sc_wsint		 ; ditto - on interrupt

	xref	sc_cmdw
	xref	sc_nmode
	xref	sc_statwr

	include 'dev8_keys_err'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'
	include 'dev8_mac_assert'
;+++
; This routine writes sectors to a hard disk for direct sector IO
;
;	d0 cr	sector number / error code
;	d2 c  p number of sectors
;	d7 c  p drive ID / number
;	a1 c  p address to write from
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0, ERR.NC or ERR.MCHK
;
;---
sc_wsect
hdwp.reg reg	d2/d3/d4/a0/a1
	movem.l hdwp.reg,-(sp)
	move.l	a1,d3
	lsr.w	#1,d3
	bcc.s	hdwp_even		 ; even address

	move.w	d2,d3			 ; count
	subq.w	#1,d3
	move.l	d0,d4			 ; start sector

	move.l	a1,a0			 ; save address
	lea	hdl_buff(a3),a1 	 ; use buffer

hdwp_write
	move.w	ddf_slen(a4),d2
	bra.s	hdwp_le
hdwp_loop
	move.b	(a0)+,(a1)+		 ; get new sector contents
hdwp_le
	dbra	d2,hdwp_loop

	lea	hdl_buff(a3),a1

	move.l	d4,d0			 ; this sector
	addq.l	#1,d4			 ; ... next sector
	moveq	#1,d2
	bsr.s	hd_write
	dbne	d3,hdwp_write		 ; another sector

hdwp_exit
	movem.l (sp)+,hdwp.reg
	rts

hdwp_even
	pea	hdwp_exit

;+++
; This routine writes sectors to a hard disk (internal)
;
;	d0 cr	sector number / error code
;	d2 c  p number of sectors
;	d7 c  p drive ID / number
;	a1 c  p address to write from
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0, ERR.NC or ERR.MCHK
;
;---
sc_wsint
hd_write
hdw.reg reg	d3
	move.l	d3,-(sp)
	subq.b	#hdl.acss,hdl_acss(a3)	 ; hold poll
	move.l	d0,d3			 ; block
	bsr.s	scw_write		 ; write
	ble.s	scw_done
	bsr.s	scw_write		 ; and try again
	ble.s	scw_done
	bsr.s	scw_write		 ; and try again

scw_done
	beq.s	scw_exit
scw_mchk
	moveq	#err.mchk,d0
scw_exit
	addq.b	#hdl.acss,hdl_acss(a3)	 ; restore poll
	move.l	(sp)+,d3
	tst.l	d0
	rts


scw_write
	jsr	sc_cmdw 		 ; write command
	bne.l	sc_nmode		 ; oops, restore normal mode
	jmp	sc_statwr		 ; check status

	end
