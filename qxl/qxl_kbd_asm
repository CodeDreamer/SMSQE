; QXL_KBD.ASM Keyboard server and setup
; 2006.10.01	1.01    uses 32 bits transfer,"turn off typematic" function suppressed , optimised

qxl_kbd_setup:
        ASSUME ds:DGROUP
        ASSUME es:DGROUP
	mov	cx, OFFSET qxl_kbd_server
	mov	bp, OFFSET qxl_kbd_servend-4
	mov	si, OFFSET int_kbdi
	call	qxl_setvc		 ; set up server

; QXL reset keyboard

qxl_kbd_reset:
	xor 	ax,ax
	mov	kbd_save,al
	retn

qxl_kbd_server:
	push	ds
	push	es
	push	ax			 ; save accumulator
	push	bx
	mov	ax, DGROUP
	mov	ds,ax
	mov	es,ax
        ASSUME ds:DGROUP
        ASSUME es:DGROUP
        call    qld_wait_read
	in	al,kbd_data		 ; code from KBD
	test	al,al			 ; null?
	jz	qks_exit
	cmp	al,046h		 ; scroll lock?
	jnz	qks_send		 ; ... no
	cmp	kbd_save,01dh	 ; left control was last code?
	jnz	qks_send 		 ; ... no
	mov	kbd_stop,-1		 ; set stop flag
	jmp	qks_exit

qks_send:
        mov	kbd_save,al		 ; saved keystroke key
	mov	bx, OFFSET kbd_buff ; and put in buffer
	call	buff_pbyte

qks_exit:
	mov	al,pic_eof			; (end of interrupt)
	out	pic_ctrl,al
	pop	bx
	pop	ax
	pop	es
	pop	ds
	iretf

	jmp	FAR PTR int_kbdi ; !!!!!
qxl_kbd_servend:
