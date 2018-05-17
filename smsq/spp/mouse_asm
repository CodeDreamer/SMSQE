; SMSQ SER/PAR/PRT serial mouse   V2.10     1999  Tony Tebby

	section spp

	xdef	spp_mouse

	xref	pt_button3

	xref	iob_anew
	xref	iob_gbyt
	xref	gu_achpp

	include 'dev8_keys_mse'
	include 'dev8_keys_iod'
	include 'dev8_keys_buf'
	include 'dev8_keys_con'
	include 'dev8_keys_sys'
	include 'dev8_keys_qlv'
	include 'dev8_keys_serparprt'
	include 'dev8_keys_qdos_sms'
	include 'dev8_mac_assert'
;+++
; Serial mouse driver.
; This driver modifies the standard serial port driver.
; The routine calls the mouse detection routine for each port in turn.
; If a mouse is detected
;	this routine
;	  sets the the port number to zero to prevent the port being used
;	  sets up a linkage block
;	  sets up an input buffer
;	  links the mouse polling routine in
; If a mouse is not detected
;	the mouse detection routine resets the port
; Thereafter, the normal input server puts the data read from the mouse into
; into the input buffer (7 bit data) and the scheduler routine decodes the
; packets.
;
;	a3 c  p SER/PAR/PRT linkage block
;	status return 0
;---
spp_mouse
sppm.reg reg	d1/d2/d3/d4/d5/d6/a0/a1/a2/a3/a4
	moveq	#sms.xtop,d0
	trap	#do.sms2

	movem.l sppm.reg,-(sp)
	move.w	spd_nser(a3),d6
	move.l	spd_pser(a3),a3
	move.w	#spd.len,d0
	mulu	d6,d0
	add.l	d0,a3			 ; off end of linkage blocks
	bra.s	sppm_eloop

sppm_loop
	move.l	spd_mouse(a3),d0
	ble.s	sppm_eloop
	move.l	d0,a4
	jsr	(a4)			 ; check for mouse
	beq.s	sppm_set		 ; ... found
sppm_eloop
	sub.w	#spd.len,a3
	dbra	d6,sppm_loop
	bra.s	sppm_ok

sppm_set
	clr.w	spd_port(a3)		 ; invalidate the port

	move.l	#mse.len,d0
	jsr	gu_achpp		 ; allocate (pseudo channel) block
	bne.s	sppm_exit

	move.l	#mse.id,mse_id(a0)	 ; set flag
	move.w	#mse.pack,mse_packp(a0)  ; initialise the packet pointer
	move.l	a3,mse_slnk(a0) 	 ; use ser port output op to enable

	lea	mse_buff(a0),a2
	lea	spd_ibuf(a3),a1
	move.l	#mse.buff,d0		 ; buffer size
	jsr	iob_anew		 ; allocate a new one
	bne.s	sppm_exit		 ; ... give up
	clr.l	buf_ptrg(a2)		 ; ... say permanent

	lea	sppm_poll,a4
	move.l	a4,iod_plad(a0) 	 ; set up scheduler
	lea	iod_pllk(a0),a0
	moveq	#sms.lpol,d0		 ; and link in
	trap	#do.sms2

sppm_ok
	moveq	#0,d0			 ; error is quite normal
sppm_exit
	movem.l (sp)+,sppm.reg
sppm_rts1
	rts

;--- mouse polling routine

sppm_poll
	move.l	sys_clnk(a6),d0 	  ; pointer linkage
	beq.s	sppm_rts1
	move.l	d0,a4
	move.l	mse_buff(a3),a2
	move.l	mse_slnk(a3),a5
sppm_gbyte
	jsr	iob_gbyt		 ; get a byte from the buffer
	bne.l	sppm_check		 ; nothing more to fetch
	tst.b	spd_iact(a5)		 ; input active?
	bne.s	sppm_proc		 ; ... yes process byte
	exg	a3,a5
	move.l	spd_iopr(a3),a1
	jsr	(a1)			 ; re-enable handshaking
	exg	a3,a5

sppm_proc
	move.w	mse_packp(a3),d2	 ; packet pointer
	bclr	#6,d1			 ; start byte
	beq.s	sppm_nbyte		 ; next byte
	clr.w	d2
sppm_nbyte
	cmp.w	#mse.pack,d2		 ; packet overfull?
	bge.s	sppm_gbyte
	move.b	d1,mse_pack(a3,d2.w)	 ; add to packet
	addq.w	#1,d2
	move.w	d2,mse_packp(a3)

	subq.w	#3,d2			 ; 3 byte packet complete?
	bne.s	sppm_gbyte
	move.b	mse_pack(a3),d0 	 ; first byte (d0 was 0))
	move.b	d0,d1
	lsl.b	#6,d1			 ; ms bits of x inc
	lsr.b	#2,d0
	move.b	d0,d2
	lsl.b	#6,d2			 ; ms bits of y
	lsr.b	#2,d0			 ; buttons
	move.b	sppm_but(pc,d0.w),mse_button(a3) ; set the button number
	bpl.s	sppm_inc		 ; not centre
	clr.b	mse_button(a3)		 ; ... centre, clear left / right
	move.b	#mse.mid,mse_b3(a3)	 ; fake centre button
sppm_inc
	or.b	mse_pack+1(a3),d1	 ; x increment
	or.b	mse_pack+2(a3),d2	 ; y increment
	ext.w	d1
	move.w	d1,d0			 ; supposed count
	bpl.s	sppm_ycount		 ; ... its positive
	neg.w	d0
sppm_ycount
	ext.w	d2			 ; y increment / count
	move.w	d2,d3			 ; other supposed cont
	bge.s	sppm_addinc		 ; ... its positive
	neg.w	d3
sppm_addinc
	cmp.w	d0,d3			 ; is y count greater?
	ble.s	sppm_setinc		 ; ... no
	move.w	d3,d0			 ; ... yes
sppm_setinc
	add.w	d0,pt_xicnt(a4) 	 ; update count
	add.w	d1,pt_xinc(a4)
	add.w	d2,pt_yinc(a4)		 ; and distance moved

	bra	sppm_gbyte

sppm_check
	cmp.b	#mse.mid,mse_b3(a3)	 ; is middle button pressed?
	bne.s	sppm_button		 ; ... no
	clr.b	mse_b3(a3)		 ; ... yes, clear it
	jmp	pt_button3		 ; and do button 3 code

sppm_button
	move.b	mse_button(a3),pt_bpoll(a4) ; this is new key
	beq.s	sppm_rts		 ; ... none
	clr.b	pt_lstuf(a4)		 ; it isn't a stuff
	clr.b	mse_b3(a3)		 ; kill centre button

sppm_rts
	rts

sppm_but dc.b  0,2,1,-1 		 ; button decode


	end
