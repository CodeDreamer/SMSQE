; BUFFER handling routines
; 2006.10.01	1.01    buffer test added (at buff_test) (BC)

;
;   ax	buffer length / buffer free
;   al	byte in / out
;   bx	base of buffer
;
buf_stt = 0   ; start of buffer
buf_end = 2   ; end of buffer
buf_put = 4   ; put pointer
buf_get = 6   ; get pointer
buf_start = 8 ; start of buffer

        ASSUME ds:DGROUP
        ASSUME es:DGROUP

buff_set:
	push	cx
	mov	cx,buff_start
	add	cx,bx			  ; start of buffer

	mov	[bx+buff_stt],cx
	mov	[bx+buff_put],cx
	mov	[bx+buff_get],cx
	add	cx,ax			  ; end of buffer
	mov	[bx+buff_end],cx
	dec	ax			  ; free space is one less
	pop	cx
	retn

; BUFF_PBYTE returns EQ/Z if failed!

buff_pbyte:
	push	di
	mov	di,[bx+buff_put] 	  ; put pointer
	mov	[di],al 		  ; put
	inc	di
	cmp	di,[bx+buff_end] 	  ; at end?
	jb	bufp_chk		  ; ... no
	mov	di,[bx+buff_stt]		  ; ... yes, reset to start
bufp_chk:
	cmp	di,[bx+buff_get] 	  ; full?
	je	bufp_ov			  ; ... yes
	mov	[bx+buff_put],di 	  ; ... no, move pointer on
	pop	di
	retn
bufp_ov:
	xor	ax,ax
	pop	di
	retn

;
; BUFF_PBROOM Put byte and check room returns S if failed, Z/EQ if full
;
buff_pbroom:
	push	di
	mov	di,[bx+buff_put] 	  ; put pointer
	mov	[di],al 		  ; put
	inc	di
	cmp	di,[bx+buff_end] 	  ; at end?
	jb	bufpr_chk		  ; ... no
	mov	di,[bx+buff_stt]		  ; ... yes, reset to start
bufpr_chk:
	mov	ax,[bx+buff_get] 	  ; get pointer
	sub	ax,di			  ; total room in middle
	je	bufpr_ov		  ; ... out of room
	mov	[bx+buff_put],di 	  ; move pointer on
	dec	ax
	jns	bufpr_exit
	add	ax,[bx+buff_end] 	  ; overlap, we need to add the length
	sub	ax,[bx+buff_stt]
bufpr_exit:
	pop	di
	retn
bufpr_ov:
	mov	ax,-1
	test	ax,ax
	pop	di
	retn

;
; BUFF_ROOM
;
;buff_room:
;	mov	ax,[bx+buff_get] 	  ; get pointer
;	sub	ax,[bx+buff_put] 	  ; total room in middle
;	dec	ax
;	jns	bufr_exit
;	add	ax,[bx+buff_end] 	  ; overlap, we need to add the length
;	sub	ax,[bx+buff_stt]
;bufr_exit:
;	retn

;
; BUFF_GBYTE returns EQ if failed!
;
buff_gbyte:
	push	si
	mov	si,[bx+buff_get]		  ; get pointer
        cmp     si,[bx+buff_put]                  ; anything to get?
	jz	bufg_exit
	lodsb				  ; get
	cmp	si,[bx+buff_end]		  ; at end?
	jb	bufg_ok			  ; ... no
	mov	si,[bx+buff_stt]		  ; ... yes, reset to start
	test	si,si			  ; set nz
bufg_ok:
	mov	[bx+buff_get],si		  ; move pointer on
bufg_exit:
	pop	si
	retn

;
; BUFF_TEST returns EQ if empty
;
buff_test:
	push	si
	mov	si,[bx+buff_get]		  ; get pointer
        cmp     si,[bx+buff_put]                  ; anything to get?
	pop	si
	retn


