; QXL_VMOD.ASM Set video mode / palette

; check which modes are available (and save current mode)
; 2005.01.10	1.01	optimisation (BC)
; 2006.10.01	1.02	use 32 bits transfer (BC)


qxl_vmod_check:
        ASSUME ds:DGROUP
        ASSUME es:DGROUP

	push	es

	mov	ax,04f00h			 ; general VESA enquiry
	mov	di, OFFSET vesa_info
	int	010h				 ; BIOS

	cmp	vesa_info, 056h		 ; VESA block?
	jne	qvc_vga			 ; ... no
	cmp	vesa_info+1, 045h		 ; VESA block?
	jne	qvc_vga			 ; ... no
	cmp	vesa_info+2, 053h		 ; VESA block?
	jne	qvc_vga			 ; ... no
	cmp	vesa_info+3, 041h 	 ; VESA block?
	jne	qvc_vga			 ; ... no

        ASSUME es:NOTHING
	mov	si, vesa_mptr		 ; mode list

qvc_loop:
	mov	es, vesa_mptr+2
	mov	cx, es:[si]			 ; next mode in list
	add	si,2
	cmp	cx,-1				 ; end of table?
	je	qvc_vesa

	cmp	cl,vesa_max			 ; recognised mode?
	ja	qvc_loop			 ; ... no
	xor	bx,bx
	mov	bl,cl
	test	vesa_mode[bx],0ffh	 ; acceptable?
	jz	qvc_loop

	mov	ax,04f01h			 ; mode enquiry
	mov	di, OFFSET vesa_mattr
	push	si
	push	bx
	push	ds
	pop	es
	int	010h
	pop	bx
	pop	si

	mov	ax, vesa_mattr		 ; check attributes
	and	ax,019h
	cmp	ax,019h
	jne	qvc_loop			 ; not acceptable
	cmp	vesa_mwaa,007h		 ; read and write?
	jne	qvc_loop
	cmp	vesa_msize,64		 ; standard size?
	jne	qvc_loop

	mov	al,vesa_mode[bx]
	mov	bl,vesa_colr[bx]
	or	mes1_vmode[bx],al		 ; set bits in message

	jmp	qvc_loop

qvc_vesa:
	mov	ax,04f04h			 ; VESA save / restore
	mov	cx,0000fh			 ; all
	xor	dl,dl				 ; find space
	int	010h				 ; BIOS
	call	qvc_alloc
	mov	ax,04f04h			 ; VESA save / restore
	xor	bx,bx
	mov	cx,0000fh			 ; all
	mov	dl,1				 ; save
	int	010h				 ; BIOS

	pop	es
	retn

qvc_vga:
	mov	ax,01c00h			 ; VGA save space
	mov	cx,00007h			 ; all
	int	010h				 ; BIOS
	call	qvc_alloc
	mov	ax,01c01h			 ; VGA save
	xor	bx,bx
	mov	cx,00007h			 ; all
	int	010h				 ; BIOS
pop	es
	retn

qvc_alloc:
	shl	bx,2				; instead of (shl bx,1) x 2 (v.1.01)
	mov	ah,048h			; allocate
	int	21h
	mov	vesa_save,ax		; save segment
	mov	es,ax
	retn

qxl_vmod_restore:
      push  es
        ASSUME ds:DGROUP
	mov	ax,vesa_save		; save segment
	mov	es,ax

	test	vesa_vers,0ffffh		; VESA?
	jz	qvr_vga			; ... no

	mov	ax,04f04h			; VESA save / restore
	xor	bx,bx
	mov	cx,0000fh			; all
	mov	dl,2				; restore
	int	010h				; BIOS

	mov	ax,04f03h			; mode query
	int	010h				; BIOS
	mov	ax,0001fh			; VGA mode range
	and	ax,bx				; (VGA mode)
	cmp	ax,bx
	jz	qvr_vgrset			; it is VGA

	mov	ax,04f02h			; mode set
	int	010h				; BIOS

	pop	es
	retn

qvr_vga:
	mov	ax,01c02h			; VGA restore
	xor	bx,bx
	mov	cx,00007h			; all
	int	010h				; BIOS

	mov	ah,00fh			; current mode
	int	010h				; BIOS
	and	al,07fh			; mode

qvr_vgrset:
	xor	ah,ah				; re-set it!!
	int	010h

	pop	es
	retn


;----------------------------------------------------------------------------------------------
;------------- Message from QXL - set video mode

qxl_vmod_set:

        ASSUME ds:DGROUP
        ASSUME es:DGROUP
	mov	bh,ah				; save color mode				;
	lodsw
	mov	bl,al
	mov	vmod_set,bx
	jmp	qxl_rxm_loop

Qxl_do_vmod:
      mov   bx,vmod_set
	cmp	bx,00001h			; pixel plane 640 x 480 or 800 x 600?
	jbe	qvm_vga			; ... yes

	xchg  bh,bl
	add	bl,bl
	add	bl,bl				; colour mode * 4
	add	bl,bh				; + resolution
	sub	bh,bh
	mov	cl,BYTE PTR [bx+vesa_mset] ; VESA mode
	mov	ch,1

	mov	ax,04f01h			; mode enquiry
	mov	di, OFFSET vesa_mattr
	push	cx
	int	010h				; BIOS
	pop	bx				; mode reg for set mode

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
	int	10h				; BIOS

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

	mov	vesa_wind,-1	 	; set window first time
	jmp	qvm_done

qvm_vga:
	mov	al,06ah			; 800 x 600
	test	bx,bx 			; VGA 640x480?
	jne	qvm_vgam			; ... no
	mov	al,012h			; 640 x 480

qvm_vgam:
	sub	ah,ah				; set mode
	int	010h				; BIOS

	mov	dx,3ceh
	mov	al,5
	out	dx,al		   		; select mode register
	inc	dx
	xor	al,al
	out	dx,al				; mode zero

	dec	dx
	mov	al,6
	out	dx,al
	inc	dx
	mov	al,01h			; set graphic mode
	out	dx,al

	dec	dx
	mov	al,8
	out	dx,al
	inc	dx
	mov	al,0ffh			; set full bit mask
	out	dx,al

	dec	dx
	mov	al,2
	out	dx,al
	inc	dx
	mov	al,00fh			; set all planes
	out	dx,al

	mov	vesa_wind,0		 	; only one window

qvm_done:
        mov     ax, OFFSET mes_vmack    ; acknowledge
	mov	cx, 4/2
	call	qxl_smess			; send this message
	jc	qvm_done
	mov	vmod_set,0ffffh		; now set

	retn

;----------------------------------------------------------------------------------------------
;------------- Message from QXL - set palette

qxl_palette:
        ASSUME ds:DGROUP
        ASSUME es:DGROUP
	test	ah,ah			 	; VGA?
	jnz	qpl_vga

	lodsw
	xor	bx,bx
	mov	cx,bx
	mov	bl,al				; start
	mov	cl,ah				; number of entries
	mov	dx,si				; table address
	mov	ax,3				; round up to long word
	add	ax,cx
	add	ax,cx
	add	ax,cx				; 3 x number of entries
	and	ax,0fffch
	add	si,ax				; next message
	mov	ax,01012h			; set palette
	int	10h				; BIOS
	jmp	qxl_rxm_skip

qpl_vga:
	add	si,2			 	; next message
	mov	dx, OFFSET qxl_pal4
	cmp	ah,8				; mode 8?
	jne	qxl_vdopal			; ... no
	mov	dx, OFFSET qxl_pal8
qxl_vdopal:
	mov	ax,1002h	    		; set palette registers
	int	10h
	jmp	qxl_rxm_skip


;----------------------------------------------------------------------------------------------
;------------- Message from QXL - update VGA

qxl_vga_update:
        ASSUME ds:DGROUP

	mov	dx,3c4h
	mov	al,2				; map mask
	out	dx,al
	inc	dx
	mov	al,ah		   		; required plane(s)
	out	dx,al

;----------------------------------------------------------------------------------------------
;------------- Message from QXL - update VESA

qxl_vesa_update:
        ASSUME ds:DGROUP
        ASSUME es:NOTHING
	push	es
	lodsw					; window offset
	xchg	ah,al
; inc  comm_junk
; cmp	ax,comm_junk
; je	junkok
; push	si
; mov	comm_junk+2,ax
; mov	si, OFFSET comm_junk
; call	deb_wbuff
; pop	si
; mov	comm_junk,ax
;junkok:
; xor	ax,ax
	cmp	ax,vesa_wind		; old window offset
	je	qva_addr			; unchanged
	mov	vesa_wind,ax		; new window offset

	xor	bx,bx				; set (bh=0) window a (bl=0)
	mov	cl,vesa_grsft		; granularity shift
	shl	ax,cl				; window offset in granularity units
	mov	dx,ax
	call	FAR PTR int_timi
qxl_vesa_call4:

qva_addr:
	mov	es,vesa_msega		; video segment

	lodsw
	xchg	ah,al
	mov	di,ax				; start address
	lodsw
	xchg	ah,al
	mov	cx,ax				; run length
        shr     cx,1

 ;cmp 	cx,257
 ;jb	lok
 ;push	si
 ;push di
 ;sub  si,2
 ;mov  di,si
 ;sub  di,6
 ;call	deb_cnt_buff
 ;pop	di
 ;pop	si
;lok:
        rep movsd                               ; copy run

	pop	es
	jmp	qxl_rxm_loop		; done
