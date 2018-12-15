; SER receive enable / disable	 V2.00	   1989  Tony Tebby   QJUMP

	section ser

	xdef	ser_rxen
	xdef	mfp_rxen
	xdef	scc_rxen
	xdef	ser_rxdi
	xdef	mfp_rxdi
	xdef	scc_rxdi

	xref	iob_room
	xref	ser_oact

	include 'dev8_keys_atari'
	include 'dev8_keys_atari_scc'
	include 'dev8_keys_k'
	include 'dev8_mac_assert'
	include 'dev8_keys_par'

;+++
; SER receive enable. If not already enabled, it checks for room before
; enabling receive.
;
;	a3 c  p SER linkage block
;	all other egisters preserved
;	status returned according to D0
;---
ser_rxen
mfp_rxen
scc_rxen
	move.l	d0,-(sp)
	tst.b	prd_iact(a3)		 ; already ready?
	bne.s	srxe_exit		 ; ... yes
	move.l	prd_ibuf(a3),d0 	 ; any buffer?
	ble.s	srxe_exit		 ; ... no
	move.l	a2,-(sp)
	move.l	d0,a2
	jsr	iob_room		 ; enough room

	cmp.l	prd_room(a3),d0
	ble.s	srxe_exa2		 ; ... no

	move.w	sr,-(sp)		 ; save status
	or.w	#$0700,sr		 ; no interrupts

	tst.b	prd_hand(a3)		 ; xon/xoff?
	ble.s	srxe_hs 		 ; ... no

	move.b	#k.xon,prd_xonf(a3)	 ; set xon char to be sent

	jsr	ser_oact		 ; activate output

srxe_hs
	tst.b	prd_hwt(a3)		 ; hardware type
	bgt.s	srxe_rte		 ; no hh
	blt.s	srxe_scc		 ; it is SCC

	move.b	#mod.ctls,mod_ctls	 ; select control
	move.b	mod_ctlr,d0
	assert	0,mod..rts-3,mod..dtr-4
	and.b	#%11100111,d0		 ; control bits low
	move.b	d0,mod_ctlw		 ; set them
	bra.s	srxe_act

srxe_scc
	move.w	prd_hwb(a3),a2		 ; control register
	move.b	#scc_wtctl,(a2)
	move.b	#scct.rts+scct.enb+scct.8bit+scct.dtr,(a2) assert RTS & DTR

srxe_act
	st	prd_iact(a3)		 ; receive active now

srxe_rte
	move.w	(sp)+,sr		 ; restore status
srxe_exa2
	move.l	(sp)+,a2
srxe_exit
	move.l	(sp)+,d0
	rts

;+++
; SER receive disable.
;---
ser_rxdi
mfp_rxdi
scc_rxdi
	move.w	sr,-(sp)		 ; save status
	move.l	d1,-(sp)
	or.w	#$0700,sr		 ; no interrupts
	tst.b	prd_hwt(a3)		 ; hardware type
	bgt.s	srxd_nact		 ; no hh
	blt.s	srxd_scc
	move.b	#mod.ctls,mod_ctls	 ; select control
	move.b	mod_ctlr,d1
	or.b	#(1<<mod..rts+1<<mod..dtr),d1 ; control bits high
	move.b	d1,mod_ctlw		 ; set them
	bra.s	srxd_nact

srxd_scc
	exg	d1,a2
	move.w	prd_hwb(a3),a2
	move.b	#scc_wtctl,(a2)
	move.b	#scct.enb+scct.8bit,(a2) ; kill RTS & DTR
	exg	a2,d1

srxd_nact
	sf	prd_iact(a3)		 ; not active now
	move.l	(sp)+,d1
	move.w	(sp)+,sr		 ; restore status
	rts

	end
