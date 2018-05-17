; DV3 ACSI Hard Disk Write Sector	 1993	   Tony Tebby

	section dv3

	xdef	ac_wsect		 ; write sector to ACSI disk
	xdef	ac_wsint		 ; ditto - on interrupt

	xref	ac_cmdw
	xref	ac_nmode
	xref	ac_statd
	xref	at_takedma
	xref	at_reldma

	include 'dev8_keys_sys'
	include 'dev8_keys_err'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'
	include 'dev8_smsq_smsq_base_keys'
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
ac_wsect
hdwp.reg reg	d2/d3/d4/a0/a1
	jsr	at_takedma
	blt.s	hdwp_rts

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

	tst.l	d0
	dbne	d3,hdwp_write		 ; another sector

hdwp_exit
	jsr	at_reldma		 ; DMA no longer in use
	movem.l (sp)+,hdwp.reg

hdwp_rts
	rts

hdwp_even
	subq.b	#hdl.acss,hdl_acss(a3)	 ; hold poll
	bsr.s	hd_write
	addq.b	#hdl.acss,hdl_acss(a3)	 ; restore poll
	bra.s	hdwp_exit

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
ac_wsint
hd_write
	cmp.l	#$1000000,a1		 ; in DMA range?
	blo.s	hdw_dod
	movem.l d3/a0/a1,-(sp)

	move.l	sms.128kb,a0		 ; ... no, use 128 k buffer
	move.w	d2,d3
	lsl.w	#5,d3			 ; n*$200/$10
	subq.w	#1,d3
acw_copy
	move.l	(a1)+,(a0)+
	move.l	(a1)+,(a0)+
	move.l	(a1)+,(a0)+
	move.l	(a1)+,(a0)+
	dbra	d3,acw_copy

	move.l	sms.128kb,a1		 ; 128 k buffer

	bsr.s	hdw_dod
	movem.l (sp)+,d3/a0/a1
	rts

hdw_dod
hdw.reg reg	d3/a1
stk_buff equ	$04
	movem.l hdw.reg,-(sp)

	subq.b	#hdl.acss,hdl_acss(a3)	 ; hold poll

	move.l	d0,d3			 ; block

	bsr.s	hdw_do			 ; write block
	ble.s	hdw_done
	bsr.s	hdw_retry		 ; retry write
	ble.s	hdw_done
	bsr.s	hdw_retry		 ; retry write

hdw_done
	beq.s	hdw_exrp
hdw_mchk
	moveq	#err.mchk,d0
hdw_exrp
	addq.b	#hdl.acss,hdl_acss(a3)	 ; restore poll
	movem.l (sp)+,hdw.reg
	tst.l	d0
	rts

hdw_retry
	move.l	stk_buff+4(sp),a1	 ; restore buffer pointer
hdw_do
	jsr	ac_cmdw 		 ; write command
	bne.l	ac_nmode		 ; oops, restore normal mode

	move.w	ddf_slen(a4),d0
	mulu	d2,d0
	add.l	d0,a1			 ; end of read
	jmp	ac_statd		 ; check status and DMA

	end
