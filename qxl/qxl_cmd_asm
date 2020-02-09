; QXL Process command line / read/write default address file / fetch config defaults
; ES points to DGROUP, DS points to PSP
; 2005.01.10		1.01	correct file open (BC)

qxl_cmd:
        ASSUME es:DGROUP
	mov	es:loadf,0ffh		; load
	mov	es:qxl,0h			; unknown address
	mov	si,080h 			; command line
	xor	ax,ax
	lodsb					; byte length of string
	xor	cx,cx
	test	al,al
	jz	qc_def_addr			; no command
	mov	bp,ax
	mov	ds:[bp+si],cl		; null at end
	mov	cl,al				; length of command
	jmp	qc_look_cmd

; Loop skipping spaces and /

qc_no_load:
	mov	es:loadf,0			; no load
	test	cx,cx				; anything left?
	jz	qc_def_addr			; ... no

qc_look_cmd:
	lodsb
	cmp	al,020h 			; skip leading spaces
qc_end_lcmd:
	loopz	qc_look_cmd
	jz	qc_def_addr
	cmp	al,02fh 			; /?
	jz	qc_no_load

	mov	dx,si
	dec	dx				; flag start

	cmp	cx,2				; two chars left?
	jnz	qc_err_bp			; no
	sub	al,032h 			; first<2?
	js	qc_err_bp
	dec	al				; >3?
	jg	qc_err_bp
	add	al,3
	mov	ah,al
	lodsb					; next character
	sub	al,030h			; <0
	js	qc_err_bp
	cmp	al,9				; <=9
	jle	qc_addr_last		; OK
	and	al,0dfh
	sub	al,041h-030h		; <A
	js	qc_err_bp
	cmp	al,5				; F is not allowed
	jge	qc_err_bp
	add	al,10
qc_addr_last:
	shl	al,4

	mov	es:qxl,ax			; set address

	cmp	ax,0280h			; too small?
	jl	qc_err_bp
	cmp	ax,0360h			; too large?
	jg	qc_err_bp
	lodsb
	cmp	al,030h			; zero at end?
	jnz	qc_err_bp

qc_def_addr:
	mov	ax,es
        ASSUME ds:DGROUP
	mov	ds,ax				; set data segment

; Open file

	mov	dx, OFFSET QXL_DAT	; open old qxl data file
	mov	ax, 3d02h			; open and access rights
	int	21h
	jnc	qc_get_addr			; OK it exists

	mov	dx, OFFSET QXL_DAT	; open new qxl data file (BC, v. 1.01)
        xor     cx,cx                           ; file attributes
	mov	ah, 3ch			        ; open
	int	21h
	jc	qc_file_error

	mov	addr_flid,ax		; save handle
	not	addr_flnew			; new file
	xor	ax,ax
	mov	bx,qxl_code
	mov	al,smsq_qxl_base[bx]	; get configured address (/16)
	shl	ax,4
	jmp	qc_set_addr

qc_get_addr:
	mov	addr_flid,ax		; save handle
	mov	bx,ax
	mov	cx,2h				; two bytes
	mov	dx, OFFSET wrk_buff
	mov	ah,3fh			; read
	int	21h
	jc	qc_file_error
	mov	ax, WORD PTR wrk_buff

qc_set_addr:
	mov	bx,qxl			; address given on command line?
	test	bx,bx
	jnz	qc_save_addr		; ... yes
	mov	qxl,ax			; ... no, set default address from file
	test	addr_flnew,0ffh
	jz	qc_close			; do not save

qc_save_addr:
	mov	bx,addr_flid

	xor	cx,cx
	xor	dx,dx
	mov	ax,4200h			; position to start
	int	21h

	mov	cx,2h				; one byte
	mov	dx, OFFSET qxl
	mov	ah,40h			; write
	int	21h

qc_close:
	mov	bx,addr_flid
	mov	ah,3eh			; close
	int	21h

; pick up configuration info (if any!!!)

qc_conf:
	retn

qc_err_bp:
	xor	ax,ax
	push	ax
	push	es			; DGROUP
	mov	ax, OFFSET bad_para
	push	ax
	push	ds			; BASE
	push	dx			; parameter
	jmp	err_exit_norest

qc_file_error:
	xor	ax,ax
	push	ax
	push	ds
	mov	ax, OFFSET no_file
	push	ax
	jmp	err_exit_norest

