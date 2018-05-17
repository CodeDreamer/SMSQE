; DV3 SCSI Hard Disk Read Sector      1993	Tony Tebby

	section dv3

	xdef	sc_rsect		; read sector
	xdef	sc_rsint		; read sector internal

	xref	sc_cmdr
	xref	sc_nmode
	xref	sc_statrd

	include 'dev8_keys_sys'
	include 'dev8_keys_err'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'
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
sc_rsect
	exg	a1,d0			 ; address to read
	btst	#0,d0			 ; even address?
	exg	a1,d0
	beq.s	sc_rsint

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
	bsr.s	sc_rsece
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
sc_rsint
	cmp.l	sys_ramt(a6),a1 	 ; above RAMTOP?
	bhs.s	hdrs_buff

sc_rsece
	assert	hdl.hold,-1
	subq.b	#hdl.acss,hdl_acss(a3)	 ; hold poll

hdr.reg reg	d3
	move.l	d3,-(sp)
	move.l	d0,d3			 ; block

	bsr.s	hdr_read		 ; read
	ble.s	hdr_done
	bsr.s	hdr_read		 ; try again
	ble.s	hdr_done
	bsr.s	hdr_read		 ; try again

hdr_done
	beq.s	hdr_exit
hdr_mchk
	moveq	#err.mchk,d0
hdr_exit
	addq.b	#hdl.acss,hdl_acss(a3)	 ; restore poll
	move.l	(sp)+,d3
	tst.l	d0
	rts

;******************

hdr_read
	jsr	sc_cmdr 		 ; read command
	bne.l	sc_nmode		 ; oops, restore normal mode

	jmp	sc_statrd		 ; check status

	end
