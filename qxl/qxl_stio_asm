; QXL_STIO.ASM Set up IO servers
; 2001.01.10		parameter passing corrected

qxl_setio:
        ASSUME ds:DGROUP
        ASSUME es:DGROUP

	mov	dx,pic_mask			 ; save PIC mask
	call	qxl_savehb


	call	qxl_lptp_setup		 ; set up LPT ports
	call	qxl_mse_setup		 ; set up mouse server
	call	qxl_comp_setup		 ; set up COM ports
	call	qxl_kbd_setup		 ; set up keyboard

	call	qxl_vmod_check		 ; check valid video modes

	retn

qxl_resetio:
        ASSUME ds:DGROUP
        ASSUME es:DGROUP

	call	qxl_lptp_reset		 ; reset LPT ports
	call	qxl_mse_reset		 ; reset mouse server
	call	qxl_comp_reset		 ; reset COM ports
	call	qxl_kbd_reset		 ; reset keyboard

	retn


; Set vector utility (replaces the original server) si = vector, cx = new server

qxl_setvr:
        ASSUME ds:DGROUP
        ASSUME es:DGROUP
	mov	di,vector_tabp		 ; vector table pointer
	push	ds
	xor   ax,ax
	mov	ds,ax
        ASSUME ds:BASESEG
	mov	ax,si
	stosw				 	 ; vector to patch

	movsw					 ; save old instruction in vector table
	mov	[si-2],cx			 ; new instruction

	movsw					 ; save old segment in vector table
	mov	[si-2],cs			 ; new segment

	pop	ds
        ASSUME ds:DGROUP
	mov	vector_tabp,di		 ; update vector pointer

	retn

; Set vector utility (continues the original server) si = vector, cx = new server, bp = exit

qxl_setvc:
        ASSUME ds:DGROUP
        ASSUME es:DGROUP
	mov	di,vector_tabp		 ; vector table pointer
	push	ds
	xor   ax,ax
	mov	ds,ax
        ASSUME ds:BASESEG
	mov	ax,si
	stosw				 	 ; vector to patch

	lodsw					 ; old instruction
	mov	cs:[bp],ax			 ; ... in continuation address
	stosw					 ; ... and vector table
	mov	[si-2],cx			 ; new instruction

	lodsw					 ; old segment
	mov	cs:2[bp],ax			 ; ... in continuation address
	stosw					 ; ... and vector table
	mov	[si-2],cs			 ; new segment

	pop	ds
        ASSUME ds:DGROUP
	mov	vector_tabp,di		 ; update vector pointer

	retn

; Save hardware byte utility: dx = hw address

qxl_savehb:
        ASSUME ds:DGROUP
        ASSUME es:DGROUP
	mov	di,hw_tabp			 ; hardware table pointer
	mov	ax,dx				 ; (BC, v. 1.01)
	stosw					 ; register modified
	in	al,dx
	stosb					 ; old contents
	mov	hw_tabp,di
	retn
