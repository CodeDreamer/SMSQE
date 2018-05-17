; Setup name of SER/PAR/PRT Channel   V2.00     1999	Tony Tebby

	section spp

	xdef	spp_cnam

	xref	spp_pprty
	xref	spp_phand
	xref	spp_ptrn
	xref	spp_pcr
	xref	spp_pff

	include 'dev8_keys_err'
	include 'dev8_keys_serparprt'
	include 'dev8_mac_assert'

;+++
; Set up ser/par channel name in (a1)
;---
spp_cnam
	clr.w	(a1)+			 ; no name
	move.l	a1,a2
	move.w	d2,d4
	cmp.w	#10,d4			 ; space for name
	ble.l	sppn_ipar		 ; none

	move.l	spc_link(a0),a4 	 ; real linkage
	cmp.l	spd_pser(a3),a4 	 ; assume ser
	bge.s	sppn_ser

	move.w	spd_npar,d1		 ; number of ports
	move.l	#'PAR ',(a2)		 ; PAR
	bra.s	sppn_parm

sppn_ser
	move.w	spd_nser(a3),d1 	 ; number of ports
	tst.l	spc_ibuf(a0)		 ; input buffer?
	beq.s	sppn_stx		 ; ... no
	tst.l	spc_obuf(a0)		 ; output buffer?
	beq.s	sppn_srx		 ; ... no
	move.l	#'SER ',(a2)
	bra.s	sppn_parm
sppn_stx
	move.l	#'STX ',(a2)
	bra.s	sppn_parm
sppn_srx
	move.l	#'SRX ',(a2)

sppn_parm
	addq.l	#3,a2

	move.l	a4,a3			 ; real linkage
	lea	spc_parm(a0),a4
	assert	spc_parm,spc_num,spc_prty-2,spc_xlat-4,spc_lfcr-6,spc_fz-8

	move.w	(a4)+,d0		  ; port
	subq.w	#1,d1			  ; how many
	ble.s	sppn_prty		  ; only one
	add.b	#'0',d0
	move.b	d0,(a2)+
sppn_prty
	lea	spp_pprty+2,a5
	move.w	(a4)+,d0		 ; parity / master/slave
	lsr.w	#1,d0
	beq.s	sppn_flow		 ; none
	move.b	-1(a5,d0.w),(a2)+
sppn_flow
	move.b	spd_hand(a3),d0
	addq.b	#1,d0			 ; handshake
	move.b	spp_phand-spp_pprty(a5,d0.w),(a2)+
sppn_trans
	moveq	#1,d0
	and.w	(a4)+,d0		 ; translate
	move.b	spp_ptrn-spp_pprty(a5,d0.w),(a2)+

	move.w	(a4)+,d0		 ; lf/cr
	lsr.w	#1,d0
	move.b	spp_pcr-spp_pprty(a5,d0.w),(a2)+

	move.w	(a4)+,d0		 ; ff
	beq.s	sppn_done
	move.b	spp_pff-spp_pprty-1(a5,d0.w),(a2)+

sppn_done
	sub.l	a1,a2			 ; set length of name
	move.w	a2,-(a1)
	moveq	#0,d0
	rts

sppn_ipar
	moveq	#err.ipar,d0
	rts

	end
