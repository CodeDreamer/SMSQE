; DV3 ACSI Hard Disk Read Sector      1993	Tony Tebby

	section dv3

	xdef	ac_rsect		; read sector
	xdef	ac_rsint		; read sector internal

	xref	ac_cmdr
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
; This routine reads sectors from a hard disk for direct sector IO
;
;	d0 cr	sector number / error code
;	d2 c  p number of sectors
;	d7 c  p drive ID / number
;	a1 c  p address to read into
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0, ERR.NC or ERR.MCHK
;
;---
ac_rsect
	exg	a1,d0			 ; address to read
	btst	#0,d0			 ; even address?
	exg	a1,d0
	beq.s	ac_rsint

hdrs_buff
	movem.l d2/d3/d4/a0/a1,-(sp)	 ; save working reg
	move.w	d2,d3
	subq.w	#1,d3			 ; sector count
	move.l	d0,d4			 ; first sector
	move.l	a1,a0			 ; save address

hdrs_read
	lea	hdl_buff(a3),a1 	 ; use buffer
	move.l	d4,d0
	addq.l	#1,d4
	moveq	#1,d2			 ; one sector
	bsr.s	ac_rsint
	bne.s	hdrs_exit

	move.w	ddf_slen(a4),d0
	bra.s	hdrs_le
hdrs_loop
	move.b	(a1)+,(a0)+		 ; return results
hdrs_le
	dbra	d0,hdrs_loop

	dbra	d3,hdrs_read
	moveq	#0,d0

hdrs_exit
	movem.l (sp)+,d2/d3/d4/a0/a1
	rts

;+++
; This routine reads sectors from a hard disk at an even address
;
;	d0 cr	sector number / error code
;	d2 c  p number of sectors
;	d7 c  p drive ID / number
;	a1 c  p address to read into
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0, ERR.NC or ERR.MCHK
;
;---
ac_rsint
	cmp.l	#$1000000,a1		 ; in DMA range?
	blo.s	hdr_rsec		 ; ... yes

	tst.l	sms.128kb		 ; is there a big buffer?
	beq.s	hdrs_buff		 ; ... no, use little buffer

	jsr	at_takedma		 ; DMA in use
	blt.s	hdr_rts

	move.l	a0,-(sp)
	move.l	a1,-(sp)		 ; ... yes, save address
	move.l	sms.128kb,a1		 ; ... and use 128 k buffer
	bsr.s	hdr_dod
	bne.s	hdr_exit

	move.l	(sp),a0 		 ; transfer address

	move.w	d2,d0
	lsl.w	#5,d0			 ; n*$200/$10
	subq.w	#1,d0
hdr_copy
	move.l	(a1)+,(a0)+
	move.l	(a1)+,(a0)+
	move.l	(a1)+,(a0)+
	move.l	(a1)+,(a0)+
	dbra	d0,hdr_copy

	moveq	#0,d0
hdr_exit
	move.l	(sp)+,a1		 ; transfer address
	move.l	(sp)+,a0
	rts

hdr_rsec
	jsr	at_takedma		 ; DMA in use
	blt.s	hdr_rts

hdr_dod
hdr.reg reg	d3/a1
	movem.l hdr.reg,-(sp)
stk_buff equ	$04

	assert	hdl.hold,-1
	subq.b	#hdl.acss,hdl_acss(a3)	 ; hold poll

	move.l	d0,d3			 ; block

	bsr.s	hdr_do			 ; read
	ble.s	hdr_done
	bsr.s	hdr_retry		 ; try again
	ble.s	hdr_done
	bsr.s	hdr_retry		 ; try again

hdr_done
	beq.s	hdr_exrp
hdr_mchk
	moveq	#err.mchk,d0
hdr_exrp
	addq.b	#hdl.acss,hdl_acss(a3)	 ; restore poll
	jsr	at_reldma		 ; DMA no longer in use
	movem.l (sp)+,hdr.reg
	tst.l	d0

hdr_rts
	rts


;******************

hdr_retry
	move.l	stk_buff+4(sp),a1
hdr_do
	jsr	ac_cmdr 		 ; read command
	bne.l	ac_nmode		 ; oops, restore normal mode

	move.w	ddf_slen(a4),d0
	mulu	d2,d0
	add.l	d0,a1			 ; end of read
	jmp	ac_statd		 ; check status and DMA

	end
