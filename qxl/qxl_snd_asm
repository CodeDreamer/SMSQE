; QXL: this module has the QL sound emulation set sound and server
	
        ASSUME ds:DGROUP           
        ASSUME es:DGROUP           

; set up sound

qxl_sound_set:
	mov	qsd_stat,0			; stop server
	test	ah,ah				; start?
	je	qxl_nosound			; stop

	mov	di, OFFSET qsd_high
	
	mov	cx,5				; transfer 5 words
qss_copy:
	lodsw	
	xchg	ah,al
	stosw
	loop	qss_copy
	
	inc	qsd_stat			; go
	jmp	qxl_rxm_loop

qxl_nosound:
	lodsw					; skip dummy bytes
	in	al,prt_datb			; port b
	and	al,0fch
	out	prt_datb,al			; bits 0,1 clear
	jmp	qxl_rxm_loop

; time sound server

qxl_sound:
        ASSUME  ds:DGROUP
	mov	ax,qsd_stat			; active?
	neg	ax
	jz	qsd_done			; ... no
	jns	qsd_count			; ... yes

	mov	qsd_stat,ax			; running now
	mov	cx,qsd_int
	mov	qsd_icnt,cx			; set interval counter
	mov	cx,qsd_wrap
	mov	qsd_wcnt,cx			; set wrap counter
	mov	cx,qsd_low			; start at low
	mov	bx,qsd_step			; positive or negative step
	test	bx,bx
	js	qsd_start			; ... negative, start at low
	mov	cx,qsd_high			; ... positive, start at high
	mov	qsd_pitch,cx
	jmp	qsd_start

qsd_count:
	dec	qsd_icnt			; increment?
	jnz	qsd_done			; ... no, stay as we are

	mov	ax,qsd_int
	mov	qsd_icnt,ax			; reset interval

	mov	cx,qsd_step			; step
	test	cx,cx
	js	qsd_up

	add	cx,qsd_pitch		; new pitch
	cmp	cx,qsd_low			; beyond low (high value)?
	jb	qsd_spitch			; ... no

	dec	qsd_wcnt			; change direction?
	jz	qsd_cdir			; ... yes
	mov	cx,qsd_high			; ... no
	jmp	qsd_spitch

qsd_up:
	add	cx,qsd_pitch		; new pitch
	cmp	cx,qsd_high			; beyond high (low value)?
	ja	qsd_spitch			; ... no

	dec	qsd_wcnt			; change direction?
	jz	qsd_cdir			; ... yes
	mov	cx,qsd_low			; ... no
	jmp	qsd_spitch

qsd_cdir:
	mov	ax,qsd_wrap
	mov	qsd_wcnt,ax			; reset wrap count
	neg	qsd_step			; and go the other way

qsd_start:
qsd_spitch:
	mov	qsd_pitch,cx
	mov	al,tim_sqr2			; square wave timer 2
	out	tim_ctrl,al
	mov	al,cl				; set period
	out	tim_dat2,al
	mov	al,ch
	out	tim_dat2,al

	in	al,prt_datb			; port b
	or	al,003h
	out	prt_datb,al			; bits 0,1 set

qsd_done:
	retn
