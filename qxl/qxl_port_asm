; QXL_PORT.ASM LPT and COM port redirection

; called with port number - 1 in ah (+$80 for COM)
; calls port specific routine with port index ((number-1)*2) in bx and mode in al or count in ax 	
qxl_port_open:
	xor	bx,bx
	mov	bl,ah
	lodsw
	add	bl,bl			; lpt / com
	jc	qxl_comp_open	; ... open com
	jmp	qxl_lptp_open			

qxl_port_close:
	xor	bx,bx
	mov	bl,ah
	lodsw
	add	bl,bl			; lpt / com
	jc	qxl_comp_close	; ... close com
	jmp	qxl_lptp_close			

qxl_port_set:
	xor	bx,bx
	mov	bl,ah
	lodsw
	xchg	al,ah			; baud rate counter
	add	bl,bl			; lpt / com
	jc	qxl_comp_set	; ... set baud rate
	jmp	qxl_rxm_loop			

qxl_port_tx:
; push ax
; push si
; push di
; sub	si, 2
; mov  di, si
; call	deb_cnt_buff
; pop  di
; pop  si
; pop  ax
 	xor	bx,bx
	mov	bl,ah
	lodsw
	xchg	al,ah			; number of bytes to send
	add	bl,bl			; lpt / com
	jc	qxl_comp_tx		; ... send data
	jmp	qxl_lptp_tx			
	
	