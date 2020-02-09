; QXL_RXMS.ASM Process messages received (setup, and start of loop)
	
qxl_rxm_skip:
qrm_skip:
	lodsw					; next message key / sub key
	cmp	al,-1
	jz	qrm_skip			; skip until not -1
	xor	bx,bx
	mov	bl,al
	jmp	WORD PTR [bx + qrm_table] ; second byte in AH

qxl_rxmess:
        ASSUME ds:DGROUP
        ASSUME es:DGROUP
	mov	si, OFFSET qxl_rbuffer	; all the messages are here

qxl_rxm_loop:
qrm_loop:
; test	BYTE PTR [si], 0ffh
; jz	qrm_exit
; cmp	BYTE PTR [si], 00ah
; je	ndb1
; cmp	BYTE PTR [si], 00ch
; je	ndb1
; cmp	BYTE PTR [si], 018h
; je	ndb1
; push	si
; mov	di, si
; mov	si, OFFSET comm_count
; call	deb_cnt_buff
; pop	si
;ndb1:
	lodsw					; next message key / sub key
	xor	bx,bx
	mov	bl,al
	jmp	WORD PTR [bx + qrm_table] ; second byte in AH

qrm_table:
	WORD OFFSET	qrm_exit		; 00 end of messages
	WORD OFFSET	qxl_flow		; 02 flow control
	WORD OFFSET	qxl_rtc_set		; 04 set RTC
	WORD OFFSET qxl_pcset		; 06 request PC setup
	WORD OFFSET	qxl_vmod_set	; 08 set video mode
	WORD OFFSET	qxl_vga_update	; 0a update vga display
	WORD OFFSET qxl_vesa_update	; 0c update vesa display
	WORD OFFSET qxl_palette		; 0e set palette
	WORD OFFSET qxl_port_open	; 10 open port 
	WORD OFFSET qxl_port_close	; 12 close port
	WORD OFFSET qxl_port_set	; 14 set port
	WORD OFFSET qxl_port_tx		; 16 port tx data
	WORD OFFSET qxl_read_phys	; 18 read physical sector
	WORD OFFSET qrm_exit		; 1a nop
	WORD OFFSET qxl_write_phys	; 1c write physical sector
	WORD OFFSET qrm_exit		; 1e nop
	WORD OFFSET qrm_exit		; 20 nop 
	WORD OFFSET qrm_exit		; 22 message to plug in
	WORD OFFSET qrm_exit		; 24 open channel
	WORD OFFSET qrm_exit		; 26 close channel
	WORD OFFSET qrm_exit		; 28 read data
	WORD OFFSET qrm_exit		; 2a nop 
	WORD OFFSET qrm_exit		; 2c write data
	WORD OFFSET qrm_exit		; 2e nop 
	WORD OFFSET qxl_format_phys	; 30 format drive
	WORD OFFSET qrm_exit		; 32 nop 
	WORD OFFSET qxl_sound_set	; 34 set up Beep type sound

qrm_exit:
	retn
