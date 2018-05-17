; Setup name of PAR Channel   V2.00     1996  Tony Tebby

	section par

	xdef	par_cnam
	xdef	ser_cnam

	xref	par_pprty
	xref	par_phand
	xref	par_ptrn
	xref	par_pcr
	xref	par_pff

	include 'dev8_keys_err'
	include 'dev8_keys_par'
	include 'dev8_mac_assert'

;+++
; Set up ser channel name in (a1)
;---
ser_cnam
	move.l	prc_link(a0),a3 	 ; real linkage

;+++
; Set up par channel name in (a1)
;---
par_cnam
	move.w	d2,d4
	cmp.w	#10,d4			 ; space for name
	ble.l	pcn_ipar		 ; none

	clr.w	(a1)+			 ; no name
	move.l	a1,a2
	tst.b	prd_serx(a3)		 ; SER
	bgt.s	pcn_ser 		 ; ... yes

	move.l	#'PAR ',(a2)		 ; PAR
	bra.s	pcn_parm

pcn_ser
	tst.l	prc_ibuf(a0)		 ; input buffer?
	beq.s	pcn_stx 		 ; ... no
	tst.l	prc_obuf(a0)		 ; output buffer?
	beq.s	pcn_srx 		 ; ... no
	move.l	#'SER ',(a2)
	bra.s	pcn_parm
pcn_stx
	move.l	#'STX ',(a2)
	bra.s	pcn_parm
pcn_srx
	move.l	#'SRX ',(a2)

pcn_parm
	addq.l	#3,a2

	lea	prc_parm(a0),a4
	assert	prc_parm,prc_num,prc_prty-2,prc_xlat-4,prc_lfcr-6,prc_fz-8

	move.w	(a4)+,d0		 ; port
	tst.b	prd_serx(a3)
	ble.s	pcn_prty		 ; no number
	add.b	#'0',d0
	move.b	d0,(a2)+
pcn_prty
	lea	par_pprty+2,a5
	move.w	(a4)+,d0		 ; parity
	tst.b	prd_serx(a3)		 ; ser?
	ble.s	pcn_trans
	lsr.w	#1,d0
	beq.s	pcn_flow		 ; no parity
	move.b	-1(a5,d0.w),(a2)+	 ; parity
pcn_flow
	move.b	prd_hand(a3),d0
	addq.b	#1,d0			 ; handshake
	move.b	par_phand-par_pprty(a5,d0.w),(a2)+
pcn_trans
	moveq	#1,d0
	and.w	(a4)+,d0		 ; translate
	move.b	par_ptrn-par_pprty(a5,d0.w),(a2)+

	move.w	(a4)+,d0		 ; lf/cr
	lsr.w	#1,d0
	move.b	par_pcr-par_pprty(a5,d0.w),(a2)+

	move.w	(a4)+,d0		 ; ff
	beq.s	pcn_done
	move.b	par_pff-par_pprty-1(a5,d0.w),(a2)+

pcn_done
	sub.l	a1,a2			 ; set length of name
	move.w	a2,-(a1)
	moveq	#0,d0
	rts

pcn_ipar
	moveq	#err.ipar,d0
	rts

	end
