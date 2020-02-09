; QXL_FLOW.ASM Handles the QXL-PC flow control message
; 2006.10.01	1.01	use 32 bit transfer (BC)


        ASSUME ds:DGROUP
        ASSUME es:DGROUP

;	SI	updated
;	DI	smashed

qxl_flow:
; push	si
; mov	di, OFFSET flowqx_mess
; mov	si, OFFSET comm_count
; call	deb_cnt_buff
; pop	si
	mov	di, OFFSET flowqx_mess
	stosw
	movsw
	movsd
; push	si
; mov	di, OFFSET flowqx_mess
; mov	si, OFFSET comm_count
; call	deb_cnt_buff
; pop	si

	jmp	qxl_rxm_loop
