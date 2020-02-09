; QXL_LPTP.ASM Parallel port server and setup
	
; check which LPT ports are available (for the moment 1-3 only) and set them up

qxl_lptp_setup:
        ASSUME ds:DGROUP
	push	es
	xor	ax,ax
	mov	es,ax
        ASSUME es:BASESEG
	
        mov     bx, OFFSET qxl_lptbuff
	mov	cx,-1
	mov	dx,cx
qls_loop:
	inc	cx			 ; next LPT
	cmp	cx,3			 ; all done?
	je	qls_done

	mov	bp,cx
	add	bp,bp			 ; index tables

	mov	ax,es:add_lpt1[bp] ; address of LPTn
	test	ax,ax
	jz	qls_loop		 ; ... none
	
	mov	lpt_notbusy,-1	 ; say ports not busy
	mov	dx,cx			 ; highest port
	mov	lpt_n[bp],ax	 ; set address
	mov   lpt_buff[bp],bx	 ; and buffer address
	add	bx,qxl_buffalloc

	mov	al,1			 ; set bit
	shl	ax,cl
	or	mes1_lpt,al		 ; mark in setup message
      or	flowpc_lpt, al	 ; and in flow control
	jmp	qls_loop
	
qls_done:
	pop	es
        ASSUME es:DGROUP

	inc	dx
	test	dx,dx			 ; highest port number 1 to n
	jz	qls_ret
	mov	lpt_maxport,dx

	mov	dx,pic_mask
	in	al,dx
	or	al,080h		 ; enable irq7 (bit 7)
	out	dx,al

	mov	cx, OFFSET qxl_lptp_server ; but install server anyway!!
	mov	si, OFFSET int_rq7i
	call	qxl_setvr		 ; set up server
;
; LPT reset (set) ports
;
qxl_lptp_reset:
	mov	lpt_notbusy,-1
	mov	lpt_txdata,0

	mov	cx,lpt_maxport
qls_resetp:
	mov   bp,cx
	dec	bp
	add	bp,bp
	mov	bx,[bp+lpt_buff]
	test	bx,bx
	jz	qls_rseloop
	mov   ax,qxl_buffsize
	call	buff_set		; set up buffer
	mov	[bp+lpt_room],ax 	; room in buffer
qls_rseloop:
	loop	qls_resetp

qls_ret:
	retn

;
; LPT restore ports - no state was saved, cannot restore
;
qxl_lptp_restore:
      retn

;
; LPT server, in this version, the interrupt server merely marks the LPT ports as not busy
;
qxl_lptp_server:
	push	ds
	push	ax
        ASSUME ds:DGROUP
	mov	ax, DGROUP
	mov	ds,ax
	mov	al,pic_eof		; end of interrupt
	out	pic_ctrl,al
	mov	lpt_notbusy,al	; enable printer again
	pop	ax
	pop	ds
	iretf

;
; LPT open routine - for the moment NOP
;
qxl_lptp_open:
	jmp	qxl_rxm_loop

;
; LPT close routine - for the moment NOP
;
qxl_lptp_close:
	jmp	qxl_rxm_loop

;
; LPT transmit routine (called from comms server) - buffers the data in the message
;
qxl_lptp_tx:
	mov	bp,bx

	mov	cx,ax
	add	ax,3			 ; round up
	and	al,0fch		
	add	ax,si			 ; next message
	push	ax

	mov	bx,[bp+lpt_buff]   ; buffer
	test	bx,bx
	jz	qlt_done

qlt_loop:
	lodsb
	call	buff_pbyte		 ; save data
	jz	qlt_stop
	dec	[bp+lpt_room]	 ; less room
	loop	qlt_loop

	cmp	[bp+lpt_room],qxl_buffmin
	jg	qlt_data
qlt_stop:
	mov	al,-2
	mov	cx,bp
	shr	cx,1
	shl	al,cl
	and	flowpc_lpt,al	 ; clear handshake bit
	mov	flowpc_head,flowpc_len ; and send message

qlt_data:
	mov	lpt_txdata,-1	 	 ; say that there is data in queue

qlt_done:
	pop	si			 ; next message
	jmp	qxl_rxm_loop

;
; LPT send routine - called from main loop to transfer data from buffer to port
; In this version the LPT port interrupt flag is not checked / cleared
;
qxl_do_lptp:
        ASSUME ds:DGROUP
        ASSUME es:DGROUP

	xor	ax,ax
	mov	lpt_txdata,al		; say data taken (we may reset this later)

	mov	cx, lpt_maxport
	test	cx,cx				; any ports?
	jz	qld_done

qld_ploop:
	mov	bp,cx
      dec	bp				; internally, ports are from 0 - maxport-1
	add	bp,bp
	mov	dx,[bp+lpt_n]		; any printer port?
	test	dx,dx
	jz	qld_eploop

qld_bloop:
	inc	dx		      	; read port status
	in	al,dx	
	dec	dx
	test	al,al			      ; bit 7 high
	jns	qld_nready		      ; ... no
	mov	bx,[bp+lpt_buff]
	call	buff_gbyte			; get data byte
	jz	qld_ndata
	inc   [bp+lpt_room]
	out	dx,al			      ; send data byte
	add	dx,2
	mov	al,lpt_stblow	      ; and strobe
	out	dx,al
	out	dx,al
	mov	al,lpt_stbhigh
	out	dx,al
	sub	dx,2
	jmp	qld_bloop

qld_nready:
	dec	lpt_txdata			; there is still data to go
qld_ndata:				
	cmp	[bp+lpt_room],qxl_buffmin
	jle	qld_eploop
	mov	al,1
	dec	cl
	shl	al,cl
	inc	cl
	test	flowpc_lpt,al
	jnz	qld_eploop
	or	flowpc_lpt,al	 ; set handshake bit
	mov	flowpc_head,flowpc_len ; and send message

qld_eploop:
	loop	qld_ploop

qld_done:
	retn
