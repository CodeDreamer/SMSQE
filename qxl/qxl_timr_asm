; QXL_TIMR.ASM Timer server and setup and restore routines

; Timer Interrupt server

; Set up timer server

qxl_timer_setup:
        ASSUME ds:DGROUP
        ASSUME es:DGROUP

	mov	cx, OFFSET qxl_timer_server
	mov	bp, OFFSET qts_patch4-4
	mov	si, OFFSET int_timi
	call	qxl_setvc		 ; set up server

	mov	al,tim_sqr0	; square wave is normal
	out	tim_ctrl,al
	mov	al,tim_timl	; time (500us?) low byte
	out	tim_dat0,al
	mov	al,tim_timh	; time (500us?) high byte
	out	tim_dat0,al

	retn

; The timer server restore

qxl_timer_restore:
        ASSUME ds:DGROUP
        ASSUME es:DGROUP

	xor	al,al				 ; clear the counter (default)
	out	tim_dat0,al
	out	tim_dat0,al
	retn

; The timer server itself

qxl_timer_server:
	push	ds
	push	ax
	push	bx
	push	cx
	mov	ax, DGROUP
        ASSUME ds:DGROUP
	mov	ds,ax
	inc	qxl_tick			; count ticks
	sub	timer_cnt,tim_time	; normal tick?
	jc	qts_pc_timer		; ... yes, do PC timer server

	sub	sound_cnt,tim_time*8	; sound tick?
	jnc	qts_dotime
	call	qxl_sound			; ... yes, do sound

qts_dotime:
	mov	al,pic_eof			; (end of interrupt)
	out	pic_ctrl,al

	test	qxl_busy,0ffffh		; comms in service?
	jnz	qts_busy			; ... yes, I hope it is not for long

	mov	qxl_busy,qxl_maxbusy	; reset busy counter (so we know we are busy)

	mov	ax,ds
	mov	bx,sp				; === As we do not know who we might
	mov	cx,ss				; === be interrupting, we hade better
	mov	ss,ax				; === SET STACK SEGMENT = DGROUP
	mov	sp, OFFSET qts_stack	; === and stackpointer
	push	bx				; === save sp
	push	cx				; === and ss (on our stack!!!)

	push	dx
	push	si
	push	di
	push	bp
	push	es
	push	ds
	pop	es

	cld
	sti					; enable interrupts
	call	qxl_comm			; do communications
	call	qxl_rxmess			; process messages received
	cli

	pop	es
	pop	bp
	pop	di
	pop	si
	pop	dx

	pop	cx				; ===
	pop	bx				; === restore the call stack
	mov	ss,cx				; ===
	mov	sp,bx				; ===

	mov	qxl_busy,0			; we are no longer busy

qts_exit:
	pop	cx
	pop	bx
	pop	ax
	pop	ds
	iretf

qts_busy:
	dec	qxl_busy			; one more failure
	jnz	qts_exit			; keep trying
	dec	qxl_busy
	jmp	err_nresp			; no response

qts_pc_timer:				; continue normal server
	pop	cx
	pop	bx
	pop	ax
	pop	ds
	jmp	FAR PTR int_timi
qts_patch4:

	retn
