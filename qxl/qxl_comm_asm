; QXL_COMM.ASM this routine handles the QXL-PC communications
; 2006.10.01	1.01	uses double word (32 bits) transfer (BC)	


        ASSUME ds:DGROUP
        ASSUME es:DGROUP
;	AX	smashed
;	BX	smashed
;	CX	smashed
;	DX	smashed
;	BP	smashed
;	SI	smashed
;	DI	smashed

qxl_comm:
	inc	comm_count
	mov	di, OFFSET qxl_sbuffer ; message buffer

	mov	bx, OFFSET kbd_buff	; look at keyboard buffer
	mov	ax, [bx+buff_get]
	cmp	ax, [bx+buff_put]
	je	qcm_kbd_done
	mov	ax, 0100h+qxm_kbdd ; assume 1 byte of data
	stosw
	call	buff_gbyte
	stosb
; call deb_whex
	call	buff_gbyte
	stosb
	je	qcm_kbd_done	; it was just one byte
	inc	BYTE PTR [di-3]	; two bytes
; call deb_whex

qcm_kbd_done:
	mov	cx, com_maxport
	test	cx,cx				; any ports?
	jz	qcm_flow

qcm_rxploop:
	mov	bx,cx
      dec	bx				; internally, ports are from 0 - maxport-1
	add	bx,bx
	test	[bx+com_rxdata],0ffh	; received data to pass over?
	jz	qcm_rxeploop

	mov	ax,1				; port bit
	dec	cl
	shl	ax,cl
	inc	cl
	xchg	ah,al
	test	flowqx_com,ax
	jz	qcm_rxeploop

	mov	[bx+com_rxdata],0		; say no more data in queue

	mov	al,qxm_rxdata		; received data message
	mov	ah,cl
	add	ah,07fh			; port number $80 to ...
	stosd					; place holder for length

	push	bx
	mov	bx,[bx+com_ibuff]
	xor	dx,dx				; byte count
qcm_rxbloop:
	call	buff_gbyte			; get data byte
	jz	qcm_rxndata
	stosb
	add	dl,1
	jnz	qcm_rxbloop			; max transfer 256 bytes
	adc	dh,dh				; true count

	pop	bx
	mov	[bx+com_rxdata],1		; say more data in queue
	push  bx

qcm_rxndata:
	pop	bx
	mov	ax,dx
	add	ax,3
	and	al,0fch			; ax is length rounded up
	sub	di,dx
	xchg	dl,dh				; little endian
	mov	[di-2],dx			; set length
	add	di,ax				; next message

	test	BYTE PTR [bx+com_rxoff],0ffh
	jle	qcm_rxeploop

	mov	BYTE PTR [bx+com_rxoff],0
	mov	dx,[bx+com_n]
	add	dl,com_mcr
	mov	al,com_erts
	out	dx,al				; re-enable RTS

qcm_rxeploop:
	loop	qcm_rxploop


qcm_flow:
	mov	si, OFFSET flowpc_head
	lodsw                   ; flow control message
	test	ax,ax			; any
	jz	qcm_messages	; ... no
	xor	ax,ax
	mov	[si-2], ax		; sent now
	movsd
	movsd				; 8 byte message

qcm_messages:
	mov	si, qxl_smquo	; messages queue out pointer

qcm_smess:
	cmp	si, qxl_smqui	; any send messages?
	jz	qcm_cstart		; nothing (more) to send

	lodsw				; next message

	mov	dx,si			; save queue pointer
	mov	si,ax			; pointer to message
	mov	bp,si			; save message pointer
	lodsw
	mov	cx,ax			; message length
        shr     cx,1                    ; in dwords
	add	ax,ax			; in bytes
	add	ax,di			; end of message
	cmp	ax, OFFSET qxl_sbufftop	; too big for buffer?
	jnb	qcm_cstart		; ... yes, forget this one for now

	cmp	dx, OFFSET qxl_smqutop ; end of queue?
	jb	qcm_mspup
	mov	dx, OFFSET qxl_smqu ; ... reset to start

qcm_mspup:
	mov	qxl_smquo,dx	; update message queue pointer

	rep movsd			; copy all of message into buffer

	mov	si,dx			; next message
	xor 	dx,dx
	mov	[bp],dx		; message taken
	jmp	qcm_smess

qcm_cstart:
	xor	dx,dx
	mov	[di],dx		; mark end of messages
	mov	qxl_sbuffp,di	; save end of buffer

	mov	bx,qxl_tick		; timer ticks
	sub	qxl_tick,bx		; taken
	cmp	bx,00080h		; overflow 127?
	jb	qcm_xmess		; ... no
	mov	bx,0007fh		; ... yes, limit

qcm_xmess:
	cmp	di, OFFSET qxl_sbuffer
	je	qcm_rmess
	or	bl,080h		; set xmit message flag

qcm_rmess:
	mov	dx,qxl		; register address
	mov	di, OFFSET qxl_rbuffer ; read buffer

qcm_isync:
	mov	ax,bx
;$$	out	dx,ax			; slug
	out	dx,ax			; extra wait first time
;$$	out	dx,ax			; slug
	out	dx,ax			; send flag and send flag . . .
	in 	ax,dx
	test	ax,ax			; QXL ready?
	jz	qcm_isync		; ... no
	jmp	qcm_rdata		; ... yes

qcm_rloop:
	out	dx,ax			; data ack / nack
	in	ax,dx			; sync
;$$	out	dx,ax			; slug

qcm_rsync:
	mov	ax,bx
;$$	out	dx,ax			; slug
	out	dx,ax			; send flag and send flag . . .
	in 	ax,dx
	test	ax,ax			; QXL ready?
	jz	qcm_rsync		; ... no

qcm_rdata:
	jg	qcm_ssync		; no data to come

	xor	ax,ax
	out	dx,ax
;$$	out	dx,ax			; slug

	sub	cx,cx
	in	ax,dx			; data
	rol	cx,1
	xor	cx,ax			; checksum
	xchg	ah,al
	stosw
;$$	xor	ax,ax
;$$	out	dx,ax			; slug

	in	ax,dx			; data
	rol	cx,1
	xor	cx,ax			; checksum
	xchg	ah,al
	stosw
;$$	xor	ax,ax
;$$	out	dx,ax			; slug
	in	ax,dx			; data
	rol	cx,1
	xor	cx,ax			; checksum
	xchg	ah,al
	stosw
;$$	xor	ax,ax
;$$	out	dx,ax			; slug
	in	ax,dx			; data
	rol	cx,1
	xor	cx,ax			; checksum
	xchg	ah,al
	stosw
;$$	xor	ax,ax
;$$	out	dx,ax			; slug
	in	ax,dx			; data
	rol	cx,1
	xor	cx,ax			; checksum
	xchg	ah,al
	stosw
;$$	xor	ax,ax
;$$	out	dx,ax			; slug
	in	ax,dx			; data
	rol	cx,1
	xor	cx,ax			; checksum
	xchg	ah,al
	stosw
;$$	xor	ax,ax
;$$	out	dx,ax			; slug
	in	ax,dx			; data
	rol	cx,1
	xor	cx,ax			; checksum
	xchg	ah,al
	stosw
;$$	xor	ax,ax
;$$	out	dx,ax			; slug
	in	ax,dx			; data
	rol	cx,1
	xor	cx,ax			; checksum
	xchg	ah,al
	stosw
;$$	xor	ax,ax
;$$	out	dx,ax			; slug
	in	ax,dx			; data
	rol	cx,1
	xor	cx,ax			; checksum
	xchg	ah,al
	stosw
;$$	xor	ax,ax
;$$	out	dx,ax			; slug
	in	ax,dx			; data
	rol	cx,1
	xor	cx,ax			; checksum
	xchg	ah,al
	stosw
;$$	xor	ax,ax
;$$	out	dx,ax			; slug
	in	ax,dx			; data
	rol	cx,1
	xor	cx,ax			; checksum
	xchg	ah,al
	stosw
;$$	xor	ax,ax
;$$	out	dx,ax			; slug
	in	ax,dx			; data
	rol	cx,1
	xor	cx,ax			; checksum
	xchg	ah,al
	stosw
;$$	xor	ax,ax
;$$	out	dx,ax			; slug
	in	ax,dx			; data
	rol	cx,1
	xor	cx,ax			; checksum
	xchg	ah,al
	stosw
;$$	xor	ax,ax
;$$	out	dx,ax			; slug
	in	ax,dx			; data
	rol	cx,1
	xor	cx,ax			; checksum
	xchg	ah,al
	stosw
;$$	xor	ax,ax
;$$	out	dx,ax			; slug
	in	ax,dx			; data
	rol	cx,1
	xor	cx,ax			; checksum
	xchg	ah,al
	stosw
;$$	xor	ax,ax
;$$	out	dx,ax			; slug
	in	ax,dx			; data
	rol	cx,1
	xor	cx,ax			; checksum
	xchg	ah,al
	stosw
;$$	xor	ax,ax
;$$	out	dx,ax			; slug

	in	ax,dx			; read checksum
	cmp	ax,cx			; OK?
	jnz	qcm_rderr

	mov	ax,080h		; OK flag
	jmp	qcm_rloop

qcm_rderr:
	sub	di,32			; backspace data pointer
	mov	ax,1			; bad flag
	jmp	qcm_rloop

qcm_ssync:
      xor	ax,ax
	stosw 			; put 0 at end
	test	bl,bl			; data to send?
	jns	qcm_cexit		; ... no

	mov	ax,bx
	out 	dx,ax			; make sure that it is there
	sub	bx,bx			; not end of data flag
	mov	ax,bx
	out	dx,ax			; idle value

	mov	si, OFFSET qxl_sbuffer+32; message buffer (allow for backspace at start)

qcm_sloop:
	xor	cx,cx			; clear checksum
;$$	mov	ax,bx
;$$	out	dx,ax			; slug
	in	ax,dx			; checksum flag
	test	ax,ax
	mov	ax,bx			; end of data flag
	out	dx,ax
	jz	qcm_sloop		; not yet ready
	js	qcm_seod		; OK
	sub	si,32			; bad transfer, backspace 32 bytes
	jmp	qcm_send

qcm_seod:
	test	bl,bl			; all gone?
	jg	qcm_cexit		; ... yes

qcm_send:
	rol	cx,1		; word 0
	lodsb
	mov	bh,al			; high byte of (QXL) word
	xor	ch,al

;$$	out	dx,ax			; slug
	out	dx,ax			; byte to transfer
	in	ax,dx
	mov	al,bh			; not fast
	out	dx,ax			; not fast

	lodsb
	mov	bh,al			; low byte of (QXL) word
	xor	cl,al

;$$	out	dx,ax			; slug
	out	dx,ax			; byte to transfer
	in	ax,dx
	mov	al,bh			; not fast
	out	dx,ax			; not fast

	rol	cx,1		; word 1
	lodsb
	mov	bh,al			; high byte of (QXL) word
	xor	ch,al

;$$	out	dx,ax			; slug
	out	dx,ax			; byte to transfer
	in	ax,dx
	mov	al,bh			; not fast
	out	dx,ax			; not fast

	lodsb
	mov	bh,al			; low byte of (QXL) word
	xor	cl,al

;$$	out	dx,ax			; slug
	out	dx,ax			; byte to transfer
	in	ax,dx
	mov	al,bh			; not fast
	out	dx,ax			; not fast

	rol	cx,1		; word 2
	lodsb
	mov	bh,al			; high byte of (QXL) word
	xor	ch,al

;$$	out	dx,ax			; slug
	out	dx,ax			; byte to transfer
	in	ax,dx
	mov	al,bh			; not fast
	out	dx,ax			; not fast

	lodsb
	mov	bh,al			; low byte of (QXL) word
	xor	cl,al

;$$	out	dx,ax			; slug
	out	dx,ax			; byte to transfer
	in	ax,dx
	mov	al,bh			; not fast
	out	dx,ax			; not fast

	rol	cx,1		; word 3
	lodsb
	mov	bh,al			; high byte of (QXL) word
	xor	ch,al

;$$	out	dx,ax			; slug
	out	dx,ax			; byte to transfer
	in	ax,dx
	mov	al,bh			; not fast
	out	dx,ax			; not fast

	lodsb
	mov	bh,al			; low byte of (QXL) word
	xor	cl,al

;$$	out	dx,ax			; slug
	out	dx,ax			; byte to transfer
	in	ax,dx
	mov	al,bh			; not fast
	out	dx,ax			; not fast

	rol	cx,1		; word 4
	lodsb
	mov	bh,al			; high byte of (QXL) word
	xor	ch,al

;$$	out	dx,ax			; slug
	out	dx,ax			; byte to transfer
	in	ax,dx
	mov	al,bh			; not fast
	out	dx,ax			; not fast

	lodsb
	mov	bh,al			; low byte of (QXL) word
	xor	cl,al

;$$	out	dx,ax			; slug
	out	dx,ax			; byte to transfer
	in	ax,dx
	mov	al,bh			; not fast
	out	dx,ax			; not fast

	rol	cx,1		; word 5
	lodsb
	mov	bh,al			; high byte of (QXL) word
	xor	ch,al

;$$	out	dx,ax			; slug
	out	dx,ax			; byte to transfer
	in	ax,dx
	mov	al,bh			; not fast
	out	dx,ax			; not fast

	lodsb
	mov	bh,al			; low byte of (QXL) word
	xor	cl,al

;$$	out	dx,ax			; slug
	out	dx,ax			; byte to transfer
	in	ax,dx
	mov	al,bh			; not fast
	out	dx,ax			; not fast

	rol	cx,1		; word 6
	lodsb
	mov	bh,al			; high byte of (QXL) word
	xor	ch,al

;$$	out	dx,ax			; slug
	out	dx,ax			; byte to transfer
	in	ax,dx
	mov	al,bh			; not fast
	out	dx,ax			; not fast

	lodsb
	mov	bh,al			; low byte of (QXL) word
	xor	cl,al

;$$	out	dx,ax			; slug
	out	dx,ax			; byte to transfer
	in	ax,dx
	mov	al,bh			; not fast
	out	dx,ax			; not fast

	rol	cx,1		; word 7
	lodsb
	mov	bh,al			; high byte of (QXL) word
	xor	ch,al

;$$	out	dx,ax			; slug
	out	dx,ax			; byte to transfer
	in	ax,dx
	mov	al,bh			; not fast
	out	dx,ax			; not fast

	lodsb
	mov	bh,al			; low byte of (QXL) word
	xor	cl,al

;$$	out	dx,ax			; slug
	out	dx,ax			; byte to transfer
	in	ax,dx
	mov	al,bh			; not fast
	out	dx,ax			; not fast

	rol	cx,1		; word 8
	lodsb
	mov	bh,al			; high byte of (QXL) word
	xor	ch,al

;$$	out	dx,ax			; slug
	out	dx,ax			; byte to transfer
	in	ax,dx
	mov	al,bh			; not fast
	out	dx,ax			; not fast

	lodsb
	mov	bh,al			; low byte of (QXL) word
	xor	cl,al

;$$	out	dx,ax			; slug
	out	dx,ax			; byte to transfer
	in	ax,dx
	mov	al,bh			; not fast
	out	dx,ax			; not fast

	rol	cx,1		; word 9
	lodsb
	mov	bh,al			; high byte of (QXL) word
	xor	ch,al

;$$	out	dx,ax			; slug
	out	dx,ax			; byte to transfer
	in	ax,dx
	mov	al,bh			; not fast
	out	dx,ax			; not fast

	lodsb
	mov	bh,al			; low byte of (QXL) word
	xor	cl,al

;$$	out	dx,ax			; slug
	out	dx,ax			; byte to transfer
	in	ax,dx
	mov	al,bh			; not fast
	out	dx,ax			; not fast

	rol	cx,1		; word a
	lodsb
	mov	bh,al			; high byte of (QXL) word
	xor	ch,al

;$$	out	dx,ax			; slug
	out	dx,ax			; byte to transfer
	in	ax,dx
	mov	al,bh			; not fast
	out	dx,ax			; not fast

	lodsb
	mov	bh,al			; low byte of (QXL) word
	xor	cl,al

;$$	out	dx,ax			; slug
	out	dx,ax			; byte to transfer
	in	ax,dx
	mov	al,bh			; not fast
	out	dx,ax			; not fast

	rol	cx,1		; word b
	lodsb
	mov	bh,al			; high byte of (QXL) word
	xor	ch,al

;$$	out	dx,ax			; slug
	out	dx,ax			; byte to transfer
	in	ax,dx
	mov	al,bh			; not fast
	out	dx,ax			; not fast

	lodsb
	mov	bh,al			; low byte of (QXL) word
	xor	cl,al

;$$	out	dx,ax			; slug
	out	dx,ax			; byte to transfer
	in	ax,dx
	mov	al,bh			; not fast
	out	dx,ax			; not fast

	rol	cx,1		; word c
	lodsb
	mov	bh,al			; high byte of (QXL) word
	xor	ch,al

;$$	out	dx,ax			; slug
	out	dx,ax			; byte to transfer
	in	ax,dx
	mov	al,bh			; not fast
	out	dx,ax			; not fast

	lodsb
	mov	bh,al			; low byte of (QXL) word
	xor	cl,al

;$$	out	dx,ax			; slug
	out	dx,ax			; byte to transfer
	in	ax,dx
	mov	al,bh			; not fast
	out	dx,ax			; not fast

	rol	cx,1		; word d
	lodsb
	mov	bh,al			; high byte of (QXL) word
	xor	ch,al

;$$	out	dx,ax			; slug
	out	dx,ax			; byte to transfer
	in	ax,dx
	mov	al,bh			; not fast
	out	dx,ax			; not fast

	lodsb
	mov	bh,al			; low byte of (QXL) word
	xor	cl,al

;$$	out	dx,ax			; slug
	out	dx,ax			; byte to transfer
	in	ax,dx
	mov	al,bh			; not fast
	out	dx,ax			; not fast

	rol	cx,1		; word e
	lodsb
	mov	bh,al			; high byte of (QXL) word
	xor	ch,al

;$$	out	dx,ax			; slug
	out	dx,ax			; byte to transfer
	in	ax,dx
	mov	al,bh			; not fast
	out	dx,ax			; not fast

	lodsb
	mov	bh,al			; low byte of (QXL) word
	xor	cl,al

;$$	out	dx,ax			; slug
	out	dx,ax			; byte to transfer
	in	ax,dx
	mov	al,bh			; not fast
	out	dx,ax			; not fast

	rol	cx,1		; word f
	lodsb
	mov	bh,al			; high byte of (QXL) word
	xor	ch,al

;$$	out	dx,ax			; slug
	out	dx,ax			; byte to transfer
	in	ax,dx
	mov	al,bh			; not fast
	out	dx,ax			; not fast

	lodsb
	mov	bh,al			; low byte of (QXL) word
	xor	cl,al

;$$	out	dx,ax			; slug
	out	dx,ax			; byte to transfer
	in	ax,dx
	mov	al,bh			; not fast
	out	dx,ax			; not fast

	mov	al,ch			; msb checksum
;$$	out	dx,ax			; slug
	out	dx,ax			; byte to transfer
	in	ax,dx
	mov	al,ch			; not fast
	out	dx,ax			; not fast

	mov	al,cl			; lsb checksum
;$$	out	dx,ax			; slug
	out	dx,ax			; byte to transfer
	in	ax,dx
	mov	al,cl			; not fast
	out	dx,ax			; not fast

	cmp	si,qxl_sbuffp	; any more to send?
	jb	qcm_snext		; ... yes
	inc	bl			; ... no, end of data
qcm_snext:
	jmp	qcm_sloop

qcm_cexit:
	xor	ax,ax			; comms reg idle value
	out	dx,ax
	retn
