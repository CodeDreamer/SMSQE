; QXL_REST.ASM This routine: 	restores all the vectors in segment 0 (using vector_tab)
;				 	restores all hw registers (using hw_tab)
;					restores specific hardware
; 2006.10.01	1.01		use 32 bits tranfer (double word) (BC)
qxl_restore:
        ASSUME ds:DGROUP
	xor 	ax,ax
	mov	es,ax
        ASSUME es:BASESEG
	mov	si, OFFSET vector_tab
	jmp	qrs_nextv

qrs_loopv:
	mov	di,ax			; next address
	movsd				; restore double word

qrs_nextv:
	lodsw
	test	ax,ax			; end of vectors to restore?
	jnz	qrs_loopv

	mov	si, OFFSET hw_tab	; hardware registers
 	jmp	qrs_nexth

qrs_looph:
	mov	dx,ax			; IO address
	lodsb				; data
	out	dx,al			; restore register

qrs_nexth:
	lodsw
	test	ax,ax			; end of hardware to restore?
	jnz	qrs_looph

	call	qxl_vmod_restore	; restore video mode
	call	qxl_comp_restore	; restore COM ports
	call	qxl_mse_restore	; restore mouse
	call	qxl_lptp_restore	; restore LPT ports

	retn
