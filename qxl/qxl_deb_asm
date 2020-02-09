; INCLUDE for DEBUGGING

; deb_buff - si pointer to text
;	     - di pointer to buffer
deb_buff:
	push	si
	call	deb_wtxt
	mov	si,di
	call	deb_wbuff
	mov	si, OFFSET crlf
	call	deb_wtxt
	pop	si
	retn

; deb_cnt_buff	- si pointer to count
;			- di pointer to buffer

deb_cnt_buff:
	push	ax
	push	bx
	push	cx
	push	dx
	push	si
	push	di
	push	bp
	mov	ax,[si]
	mov	al,ah
	call	dbw_whex
	mov	ax,[si]
	call	dbw_whex
	call	dbw_wspace

	mov	si,di
	call	deb_wbuff
	mov	si, OFFSET crlf
	call	deb_wtxt
	jmp	deb_wexit

; deb_wchr - al character

deb_wchr:
	push	ax
	push	bx
	push	cx
	push	dx
	push	si
	push	di
	push	bp
	call	dbw_wbyte
	jmp	deb_wexit

; deb_wtxt - si pointer to text

deb_wtxt:
	push	ax
	push	bx
	push	cx
	push	dx
	push	si
	push	di
	push	bp
deb_wtxtloop:
	lodsb			 ; next char
	test	al,al
	jz	deb_wexit
	call	dbw_wbyte
	jmp	deb_wtxtloop

; deb_whex - al value

deb_whex:
	push	ax
	push	bx
	push	cx
	push	dx
	push	si
	push	di
	push	bp
	call  dbw_whex
	jmp	deb_wexit

; deb_wbuff - si pointer to buffer

deb_wbuff:
	push	ax
	push	bx
	push	cx
	push	dx
	push	si
	push	di
	push	bp

	mov	ax,si
	mov	al,ah
	call	dbw_whex
	mov	ax,si
	call	dbw_whex
	call	dbw_wspace
	mov	cx,16
	jmp	deb_wshxloop

; deb_wbhex - si pointer to buffer

deb_wbhex:
	push	ax
	push	bx
	push	cx
	push	dx
	push	si
	push	di
	push	bp

	mov	cx,16

deb_wshxloop:
	push	cx
	lodsb
	call	dbw_whex
	pop	cx
	loop	deb_wshxloop

deb_wexit:
	pop	bp
	pop	di
	pop	si
	pop	dx
	pop	cx
	pop	bx
	pop	ax
	retn

dbw_wspace:
	mov	al,020h
	jmp	dbw_wbyte

dbw_whex:
	call	dbw_btohex
	push	ax
	mov	al,dl
	call	dbw_wbyte
	pop	ax
dbw_wbyte:
	mov	ah,al
	mov	dx,lpt_n			; any printer port 1?
	test	dx,dx
	jz	dbw_outnone
	inc	dx				; read port status
dbw_outwait:
	in	al,dx
	test	al,al				; bit 7 high
	jns	dbw_outwait 	      ; ... no
	mov	al,ah
	dec	dx
	out	dx,al				; send data byte
	add	dx,2
	mov	al,lpt_stblow	      ; and strobe
	out	dx,al
	out	dx,al
	mov	al,lpt_stbhigh
	out	dx,al
dbw_outnone:
	retn

dbw_btohex:    ; call byte in al, ret first char in dl, second in al
	mov	dh,al
	mov	cl,4
	shr	al,cl				; first hex char
	call	dbw_ntohex
	mov	dl,al
	mov	al,dh
	and	al,0fh			; second hex char
dbw_ntohex:
	cmp	al,0ah			; letter?
	jb	dbw_xadd0
	add	al,7
dbw_xadd0:
	add	al,030h			; ascii CHAR
	retn
