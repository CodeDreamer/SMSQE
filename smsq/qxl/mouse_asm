; QXL Mouse Routines

	section driver

	xdef	mouse_init

	xref	pt_button3

	include 'dev8_smsq_qxl_keys'
	include 'dev8_smsq_qxl_comm_keys'
	include 'dev8_keys_con'
	include 'dev8_keys_iod'
	include 'dev8_keys_sys'
	include 'dev8_keys_qdos_sms'
	include 'dev8_smsq_kbd_keys'
	include 'dev8_mac_assert'

mouse_init
	moveq	#sms.xtop,d0
	trap	#do.sms2
	move.l	sys_klnk(a6),a3 	; keyboard linkage
	move.l	iod_plad(a3),kb_extlk(a3) ; save keyboard polling routine
	lea	mse_poll,a5
	move.l	a5,iod_plad(a3) 	; insert mouse before
	moveq	#0,d0
	rts

;--- mouse polling routine

mse_poll
	move.l	sys_clnk(a6),d0 	 ; pointer linkage
	beq.s	mse_done
	move.l	d0,a4
	move.l	qxl_message,a5
	lea	qxl_ms_mouse(a5),a5

	move.w	qxm_dx(a5),d1		; x increment
	sub.w	d1,qxm_dx(a5)
	move.w	qxm_dy(a5),d2		; y increment
	sub.w	d2,qxm_dy(a5)

	move.w	d1,d0			 ; supposed count
	bpl.s	mse_ycount		 ; ... its positive
	neg.w	d0
mse_ycount
	move.w	d2,d3			 ; other supposed cont
	bge.s	mse_addinc		 ; ... its positive
	neg.w	d3
mse_addinc
	add.w	d3,d0			 ; total count
	beq.s	mse_setinc		 ; ... none
	add.w	pt_xicnt(a4),d0 	 ; total so far
	lsr.w	#1,d0			 ; reduced
	add.b	pt_wake(a4),d0		 ; wake up speed
	lsr.w	#1,d0
	addq.w	#1,d0			 ; make sure that there is a move
	move.w	d0,d0			 ; ... spare for patch
mse_setinc
	move.w	d0,pt_xicnt(a4) 	 ; update count
	add.w	d1,pt_xinc(a4)
	add.w	d2,pt_yinc(a4)		 ; and distance moved

	moveq	#3,d0
	and.b	qxm_button(a5),d0
	move.b	mse_but(pc,d0.w),d3	 ; set the button number
	bmi.l	pt_button3		 ; ... do centre button

mse_button
	move.b	d3,pt_bpoll(a4) 	 ; this is new button
	beq.s	mse_done		 ; ... none
	clr.b	pt_lstuf(a4)		 ; it isn't a stuff

mse_done
	move.l	kb_extlk(a3),a4
	jmp	(a4)			 ; continue keyboard polling routine

mse_but dc.b  0,1,2,-1			; button decode

	end
