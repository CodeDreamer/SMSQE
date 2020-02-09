; QXL
; 2005.01.10	1.01	optimisation
; 2006.10.01	1.02	data segment mopve to data.asm file
; BOOT Loader

qxl_bload:
        ASSUME ds:DGROUP
        ASSUME es:DGROUP

qbl_loop:
	dec	qbl_retry			; retry counter
	jz	err_nresp			; oops

	xor	al,al
	mov	cx,64				; later, use timer for this
	mov	dx,qxl
	add	dl,8
qbl_reset:
	out	dx,al				; reset
	loop	qbl_reset

	mov	cx,10000			; later, use timer for this
	sub	dl,8
qbl_unreset:
	out	dx,al				; unreset
	loop	qbl_unreset

; Set up to load loader

	mov	dx,qxl
	mov	si, OFFSET QXL_RESV
	call	qbl_wlong			; reset vectors
	call	qbl_wlong

	mov	si, OFFSET QXL_NOP
	call	qbl_wlong
	call	qbl_wlong
	sub	si,8
	call	qbl_wlong
	call	qbl_wlong

	mov	si, OFFSET QXL_LBAS
	call	qbl_wlong			; lea ...,(A1)
	call	qbl_wljmp			; move.l d0,(a1) - exercise the DRAM
	mov	cx,15
qbl_exerl:
	sub	si,4				; backspace
	call	qbl_wljmp
	loop	qbl_exerl

	mov	bx, OFFSET QXL_LOAD	; loader
	mov	cx, OFFSET QXL_ELOAD
	sub	cx,bx				; length of loader
        shr     cx,2                            ; in long words (v.1.01 BC)

qbl_loadl:
	mov	si, OFFSET QXL_MOVE
	call	qbl_wljmp			; nop; move.l #...,(a1)+
	mov	si,bx
	call	qbl_wlong			; #.....
	mov	bx,si
	loop	qbl_loadl

	mov	si, OFFSET QXL_LSTT

	call	qbl_wljmp			; lea start
	call	qbl_wljmp
	call	qbl_wljmp			; tst.b (a1); tst.b (a1)

	mov	cx,8
qbl_dummy_start:
	sub	si,4
	call	qbl_wlong			; tst.b (a1); tst.b (a1)
	loop	qbl_dummy_start

; The loader is loaded, now load the code

	xor	bx,bx				; a zero to synchronise
	call	qbl_sbyte

	mov	cx,040h
qbl_idle_z:
	in	al,dx				; read loader start word
	loop	qbl_idle_z

	cmp	al,034h			; correct flag?   (was ax,1234h)
	jnz	qbl_loop			; ... no

	mov	al,0f1h			; send f1 flag
	out	dx,al
	in	al,dx

	mov	ax,ds
	mov	es,ax				; set start sector
	mov	bx,qxl_code
	mov	ax, WORD PTR smsq_len+1[bx] ; middle bytes of length
	mov	cl,ah
	mov	ch,al				; swap to get length / 256
	shr	cx,1				;			   / 512
	mov	qbl_sect,cx

qbl_sloop:
	mov	si,qxl_code			; base of QXL code

	mov	cx,0200h
	mov	bl,ch				; high byte
	call	qbl_cd_byte
	mov	bl,cl				; low byte
	call	qbl_cd_byte

	xor	bx,bx
	xor	di,di
qbl_bloop:
	mov	al,es:[si]			; next byte
	inc	si
	mov	bl,al
	add	di,bx				; checksum
	call	qbl_cd_byte
	loop	qbl_bloop

	mov	cx,es
	add	cx,020h			; sending 32 paragraphs at a time
	mov	es,cx

	in	al,dx				; (was ax)
	sub	ax,di				; correct checksum?
	test	al,al				; (was ax)
	jnz	qbl_loop
	sub	qbl_sect,1
	jns	qbl_sloop

	mov	ax,ds
	mov	es,ax				; reset ES

	call	qbl_cd_zero			; zero length to follow
	call	qbl_cd_zero			; and another

	xor	al,al
	out	dx,al				; idle at zero
	retn

qbl_cd_zero:
	xor	bl,bl				; zero byte

; Subroutine to code and send a byte (bl) with zero preamble
; Returns confirmation in ax

qbl_cd_byte:
	xor	ax,ax		    ; send zero
	call	qbl_sbyte

	mov	al,bl
	test	al,al		    ; negative?
	js	qbl_sbyte	    ; ... yes, output byte
	add	al,2
	jns	qbl_sbyte	    ; $00 to $7d sent as $02 to $7f

	mov	al,1		    ; $7e and $7f sent as $01, byte
	call	qbl_sbyte	    ; $01

	xor	ax,ax		    ; zero
	call	qbl_sbyte
	mov	al,bl		    ; byte to send

; Subroutine to send a byte (taking a long time)

qbl_sbyte:
	out	dx,al
	out	dx,al
	out	dx,al
	out	dx,al
	out	dx,al
	out	dx,al
	out	dx,al
	out	dx,al		 ; about 6 microseconds
	in	al,dx		 ; get last byte sent
	retn

; subroutine to write a boot long word preceded by a JMP and nops

qbl_wljmp:
	mov	di,si		    ; save si
	mov	si, OFFSET QXL_JMP
	call	qbl_wlong		    ; JMP
	call	qbl_wlong		    ; nop, nop
	call	qbl_wlong		    ; nop, nop

	mov	si,di		    ; restore si

; subroutine to write a boot long word pointed to by DS:SI
; output address is in DX

qbl_wlong:
	lodsb
	out	dx,al		    ; send four bytes
	lodsb
	out	dx,al
	lodsb
	out	dx,al
	lodsb
	out	dx,al

	in	al,dx		    ; handshake

	retn
