; SMSQmulator Mmuse Routine  V1.01 (c) W. Lenerz 2012
; 1.01	handles mouse wheel

; this is called from the polling interrupt
; mouse button 1 (left) = 1
; mouse button 2 (right) = 2
; both	left & right = 3
; middle button = 4

	section mouse

	xdef	mse_poll

	xref	pt_button3
	xref	ioq_pbyt

	include 'dev8_keys_con'
	include 'dev8_keys_iod'
	include 'dev8_keys_sys'
	include 'dev8_keys_qdos_sms'
	include 'dev8_smsq_kbd_keys'
	include 'dev8_keys_java'

;--- mouse polling routine

mse_poll
smsh.reg reg	a4/a5
	movem.l smsh.reg,-(a7)
	move.l	sys_clnk(a6),d0 	; pointer linkage
	beq.s	mse_done
	move.l	d0,a4
	move.l	jva_lkptr,d0		; java -> smsqe comm block
	beq.s	mse_done		; ?? a very bad ooops here!
	add.l	#jva_mpos,d0		; point to mouse area in comm block: mouse pos
	move.l	d0,a5

	move.l	(a5),d1 		; mouse position
	bmi.s	mse_wheel		; can't be neg, so mouse didn't move, check buttons
	move.l	d1,pt_npos(a4)
	move.l	#-1,(a5)		; signal mouse didn't move
	move.l	jva_mrel-jva_mpos(a5),d1; mouse relative mvt

; I have no idea why the following code adapted from the qxl mouse routines works

	move.w	d1,d0			; supposed count
	bpl.s	mse_ycount		; ... its positive
	neg.w	d0
mse_ycount
	swap	d1
	tst.w	d1			; other supposed count
	bge.s	mse_addinc		; ... its positive
	neg.w	d1
mse_addinc
	add.w	d1,d0			; total count
	add.w	pt_xicnt(a4),d0 	; total so far
	lsr.w	#1,d0			; reduced
	add.b	pt_wake(a4),d0		; wake up speed
	lsr.w	#1,d0
	addq.w	#1,d0			; make sure that there is a move
	move.w	d0,pt_xicnt(a4) 	; update count

; check wheel
mse_wheel
	move.l	sys_ckyq(a6),d0 	; any keyboard queue?
	beq.s	mse_bttn		; no!
	move.l	d0,a2			; A2 = current keyboard queue
	move.l	jva_mwhl-jva_mpos(a5),d2; mouse wheel activity : char|nbr rotations
	clr.l	jva_mwhl-jva_mpos(a5)
	move.l	d2,d1
	beq.s	mse_bttn
	swap	d1			; char
mse_whl jsr	ioq_pbyt		; put byte in kbd queue
mse_dwh dbf	d2,mse_whl
; now check mouse buttons
mse_bttn
	move.b	1+jva_mbtn-jva_mpos(a5),d1 ; button clicked
	cmp.b	#2,d1
	bgt.s	cntr			 ; ... if >2 = button 3, do centre button
cnt	move.b	d1,pt_bpoll(a4) 	 ; this is new button or old button still depressed
	beq.s	mse_done		 ; ... none
	clr.b	pt_lstuf(a4)		 ; it isn't a stuff
mse_done
	movem.l (a7)+,smsh.reg
	rts

cntr	movem.l (a7)+,smsh.reg
	jmp	pt_button3

	end
