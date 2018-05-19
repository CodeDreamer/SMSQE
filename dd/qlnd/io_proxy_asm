; Network I/O function-vector proxy  V0.1		 2018	Marcel Kilgus
;
	section nd
;
	xdef	nd_send 		; send a packet
	xdef	nd_send0		; send a packet (server protocol)
	xdef	nd_read 		; read a packet
	xdef	nd_read0		; read a packet (server protocol)
	xdef	nd_bcast		read a broadcast

	include dev8_keys_sys
	include dev8_dd_qlnd_keys

; a6 = system variables
;
; We are sure this is true as nd_setup also needs a6
nd_send
	moveq	#nio_send,d0
	bra.s	nd_jump
nd_send0
	moveq	#nio_send0,d0
	bra.s	nd_jump
nd_read
	moveq	#nio_read,d0
	bra.s	nd_jump
nd_read0
	moveq	#nio_read0,d0
	bra.s	nd_jump
nd_bcast
	moveq	#nio_bcast,d0
nd_jump
	subq.l	#4,sp			; Space for routine address
	move.l	a6,-(sp)		; We cannot alter any register except d0
	move.l	sys_netio(a6),a6
	cmpi.l	#nio.ver,(a6)
	bne.s	err_nimp
	move.l	(a6,d0.w),4(sp) 	; Address of I/O routine
	move.l	(sp)+,a6
	rts				; Jump to I/O routine

err_nimp
	move.l	(sp)+,d0
	move.l	(sp)+,a6
	moveq	#err.ni,d0
	rts

	end
