; Network I/O function-vector initialisation  V0.1   2018  Marcel Kilgus
;
; This is used to split the physical I/O routines, which need to be in ROM
; (because that has different timing from RAM), from the eneric driver/server
; code, which is too big to fit into the new TK2 ROM.

	section nd
;
	xdef	nd_init
;
	xref	nd_send 		; send a packet
	xref	nd_send0		; send a packet (server protocol)
	xref	nd_read 		; read a packet
	xref	nd_read0		; read a packet (server protocol)
	xref	nd_bcast		; read a broadcast

	include dev8_keys_sys
	include dev8_dd_qlnd_keys
;
; a4 = SysVars
;
nd_init
	moveq	#mt.alchp,d0
	moveq	#nio.len,d1
	moveq	#0,d2
	trap	#1
	tst.l	d0
	bne.s	ndi_rts

	move.l	a0,a3			; base address
	move.l	#nio.ver,(a0)+

	lea	nio_table(pc),a1
	moveq	#(nio_end-nio_table)/2-1,d0
nio_copy
	move.l	a1,a2
	add.w	(a1)+,a2
	move.l	a2,(a0)+
	dbf	d0,nio_copy

	move.l	a3,sys_netio(a4)	; store table in sysvar for fast access
	moveq	#0,d0
ndi_rts
	rts

; Must be same structure as in qlnd_keys
nio_table
	dc.w	nd_send-*
	dc.w	nd_send0-*
	dc.w	nd_read-*
	dc.w	nd_read0-*
	dc.w	nd_bcast-*
nio_end

	end
