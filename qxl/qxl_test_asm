; QXL_TEST.ASM Set video mode and fill screen
	
	.MODEL small

baseseg SEGMENT AT 0
	ORG 0
dummy	WORD ?

	.SEQ
	.CODE
	.DATA

; VESA comms area (word + 512+256 bytes)

vesa_wind	WORD  -1	; current VESA window offset
vesa_save	WORD	0	; VGA / VESA save segment
vesa_info	BYTE  "VBE2"; VESA info buffer
vesa_vers	WORD	0	; VESA version
vesa_eom	WORD	0,0	; manufacturer
vesa_cap	WORD	0,0	; capabilities
vesa_mptr   WORD 	0,0	; table of modes
vesa_mem	WORD	0	; 64k memory blocks
vesa_srev	WORD	0	; software revision
vesa_vend	WORD	0,0	; vendor name
vesa_prod	WORD	0,0	; product name
vesa_prev	WORD	0,0	; product revision
		WORD	256-17 dup (01234h)

vesa_mattr	WORD	0	; mode attributes
vesa_mwaa	BYTE	0	; window a attributes	
vesa_mwab	BYTE	0	; window b attributes
vesa_mgran	WORD	0	; window offset granularity in 1kbyte units
vesa_msize	WORD	0	; window size in 1kbyte units
vesa_msega	WORD	0	; window a segment
vesa_msegb	WORD	0	; window b segment
vesa_mcall	WORD	0,0	; address of window control routine
vesa_mbpl	WORD  0	; bytes per scan line
vesa_mhpix	WORD	0	; horizontal pixels

		BYTE  256-17 DUP (056h)
vesa_grsft	BYTE	0	; granularity shift to 64 kbytes

vesa_mode	BYTE	0,081h,042h,082h, 044h,084h,048h,088h, 0,0,0,0,            0,0,0,0
		BYTE	0,081h,081h,0,    082h,082h,0,084h,    084h,0,088h,088h
vesa_colr	BYTE	0,001h,000h,001h, 000h,001h,000h,001h, 0,0,0,0,            0,0,0,0
		BYTE	0,002h,003h,0,	002h,003h,0,002h,    003h,0,002h,003h
vesa_max = 01bh

stack	 SEGMENT PARA STACK 'STACK'
	 BYTE	 256  DUP ('STACK   ')
stack	 ENDS

	.CODE
start:
	mov	bx,es
	mov	ax,DGROUP
	mov	es,ax
	ASSUME	es:DGROUP
	mov	ax,ss
	mov	bx,es
	mov	cx,es
	sub	ax,bx
	shl	ax,1
	shl	ax,1
	shl	ax,1
	shl	ax,1
	cli
	add	sp,ax
	ASSUME	SS:DGROUP
	mov	ss,cx
	sti
	cld					; only ever use post inc addressing

	ASSUME	DS:DGROUP
	
	mov	bx, 00105h			; bx is vesa mode number
	call	qxl_vga_mode

	
loop1:
	
	mov	ah,8				; wait for key
	int	021h
	
	cmp	al,020h			; space
	je	done				; all done
	
	push	ax
	mov	bx,00105h
	call	qxl_vga_mode

	pop	dx
	and	dx,0fh			; select segment

	push	dx
	xor	bx,bx				; set (bh=0) window a (bl=0)
	mov	cl,vesa_grsft		; granularity shift
	shl	dx,cl				; window offset in granularity units
	call	FAR PTR dummy

qxl_vesa_call4: 

	mov	es,vesa_msega		; video segment

	xor	ax,ax
	xor	di,di
loop2:
	stosb
	inc	ax
	jnz	loop2
	
	pop	dx
	jmp	loop1		

done:
	mov	ah,8				; wait for key
	int	021h

	.exit


; set video mode

qxl_vga_mode:
	mov	cx,bx
	mov	ax,04f01h			; mode enquiry
	mov	di, OFFSET vesa_mattr
	push	bx
	push	ds
	pop	es
	int	010h				; BIOS				
	pop	bx

	mov	ax,vesa_msega		; set segment

	mov	cl,-1
	mov	ax,vesa_mgran		; granularity
qvm_shft:
	inc	cl
	add	al,al
	cmp	al,64				; shift until granularity > 64
	jna	qvm_shft
	
	mov	vesa_grsft,cl		; shift granularity -> 64k

	mov	ax,vesa_mcall		; function call routine
	mov	cx,vesa_mcall+2
	mov	WORD PTR cs:qxl_vesa_call4-4,ax ; patched in
	mov	WORD PTR cs:qxl_vesa_call4-2,cx

	mov	ax,04f02h			; set mode
	int	010h				; BIOS				

	mov	ax,04f06h			; set line length
	xor	bl,bl
	mov	cx,vesa_mhpix
	test	cx,cx				; set
	jz	qvm_start
	int	010h				; BIOS				

qvm_start:
	mov	ax,04f07h			; set start
	xor	bx,bx
	mov	cx,bx
	mov	dx,bx
	int	010h				; BIOS				
	

qvm_exit:
	retn


; Set palette

qxl_palette:
	ASSUME	DS:DGROUP
	inc 	si				; spare
	lodsw
	xor	bx,bx
	mov	cx,bx
	mov	bl,al				; start
	mov	cl,ah				; number of entries
	mov	dx,si				; table address
	mov	ax,ds
	mov	es,ax				; table segment
	mov	ax,3				; round up to long word
	add	ax,cx
	add	ax,cx
	add	ax,cx				; 3 x number of entries
	and	ax,0fffch		
	add	si,ax				; next message
	mov	ax,01012h			; set palette
	int	10h				; BIOS
	retn

	END start