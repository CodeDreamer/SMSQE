; QXL: this module handles the real time clock
;2006.10.01	1.01	uses 32 bits transfer (BC)

        ASSUME ds:DGROUP
        ASSUME es:DGROUP

; check if time changed

qxl_rtc_check:
	mov	ax,02c00h			; get time
	int	21h
	cmp	dh,rtc_second
	jz	qrtc_rts

; send time to QXL

qrtc_wsend:
	test	rtc_mlen,0ffh		; message waiting to go?
	jnz   qrtc_wsend

	cmp	rtc_mcmd,qxm_srtc		; set RTC?
	je	qrtc_set			; ... yes

	mov	rtc_second,dh
	mov	rtc_minute,cl
	mov	rtc_hour,ch

	mov	ax,02a00h			; get date
	int	21h

	mov	rtc_day,dl
	mov	rtc_month,dh
	sub	cx,1980
	mov	rtc_year,cl

	mov	rtc_mcmd,qxm_rtcu
	mov	cx,8/2			; 8 byte message
	mov	ax, OFFSET rtc_mlen
qrtc_smess:
	call	qxl_smess			; send message
	jc	qrtc_smess


qrtc_rts:
	retn



; set real time clock

qrtc_set:
	xor   dl,dl
	mov	dh,rtc_second
	mov	cl,rtc_minute
	mov	ch,rtc_hour

	mov	ax,02d00h			; set time
	int	21h

	mov	dl,rtc_day
	mov	dh,rtc_month
	xor	cx,cx
	mov	cl,rtc_year
	add	cx,1980

	mov	ax,02b00h			; set date
	int	21h

	xor	ax,ax
	mov	rtc_mcmd,al			; clear message read flag

	retn

; set up to set clock

qxl_rtc_set:

	test	rtc_mlen, 0ffffh			; is message waiting to go??
	jnz	qrtc_panic				; ... yes, panic

	mov	di, OFFSET rtc_mcmd
	stosw						; save command / year
	movsw						; and transfer the rest
	movsd

qrtc_sdone:
	jmp	qxl_rxm_loop

qrtc_panic:
	add	si,6					; skip message
	jmp	qrtc_sdone
