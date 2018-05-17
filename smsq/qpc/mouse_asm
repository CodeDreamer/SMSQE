; QPC Mouse Routines  v1.01				  2006  Marcel Kilgus
;
; 2006-03-08  1.01  Does horizontal wheel scroll if CTRL is pressed (MK)

	section driver

	xdef	mouse_init

	xref	pt_button3
	xref	ioq_pbyt

	include 'dev8_smsq_qpc_keys'
	include 'dev8_keys_con'
	include 'dev8_keys_iod'
	include 'dev8_keys_sys'
	include 'dev8_keys_qdos_sms'
	include 'dev8_smsq_kbd_keys'
	include 'dev8_smsq_smsq_base_keys'
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
	beq	mse_done
	move.l	d0,a4

	move.l	qpc_mpos,d1
	cmp.l	qpc_mops,d1
	beq.s	mse_nopcmove

	move.l	d1,d2
	sub.w	qpc_moyp,d2
	bpl.s	mse_yc
	neg.w	d2
mse_yc	move.w	d2,d0
	swap	d2
	sub.w	qpc_moxp,d2
	bpl.s	mse_xc
	neg.w	d2
mse_xc	add.w	d2,d0
	move.l	d1,pt_npos(a4)
	move.l	d1,qpc_mops

	add.w	pt_xicnt(a4),d0 	 ; total so far
	lsr.w	#1,d0			 ; reduced
	add.b	pt_wake(a4),d0		 ; wake up speed
	lsr.w	#1,d0
	addq.w	#1,d0			 ; make sure that there is a move

	move.w	d0,pt_xicnt(a4)        ; update count
	bra.s	mse_buttons
mse_nopcmove
	cmp.l	pt_npos(a4),d1
	beq.s	mse_buttons
	move.l	pt_npos(a4),d1
	move.l	d1,qpc_mpos
	move.l	d1,qpc_mops
	dc.w	qpc.smpos+1
;;;	   bra.s   mse_buttons

; Old mouse routine (QPC1 compatible). Currently not used
;;;	   move.w  qpc_movx,d1		   ; x increment
;;;	   sub.w   d1,qpc_movx
;;;	   move.w  qpc_movy,d2		   ; y increment
;;;	   sub.w   d2,qpc_movy

;;;	   move.w  d1,d0		    ; supposed count
;;;	   bpl.s   mse_ycount		    ; ... its positive
;;;	   neg.w   d0
;;;mse_ycount
;;;	   move.w  d2,d3		    ; other supposed cont
;;;	   bge.s   mse_addinc		    ; ... its positive
;;;	   neg.w   d3
;;;mse_addinc
;;;	   add.w   d3,d0		    ; total count
;;;	   beq.s   mse_setinc		    ; ... none
;;;	   add.w   pt_xicnt(a4),d0	    ; total so far
;;;	   lsr.w   #1,d0		    ; reduced
;;;	   add.b   pt_wake(a4),d0	    ; wake up speed
;;;	   lsr.w   #1,d0
;;;	   addq.w  #1,d0		    ; make sure that there is a move
;;;	   move.w  d0,d0		    ; ... spare for patch
;;;mse_setinc
;;;	   move.w  d0,pt_xicnt(a4)	    ; update count
;;;	   add.w   d1,pt_xinc(a4)
;;;	   add.w   d2,pt_yinc(a4)	    ; and distance moved

mse_buttons
	moveq	#7,d0
	and.b	qpc_mbut,d0
	move.b	mse_but(pc,d0.w),d3	 ; set the button number
	bmi.l	pt_button3		 ; ... do centre button

mse_button
	move.b	d3,pt_bpoll(a4) 	 ; this is new button
;	 beq.s	 mse_done		  ; ... none
	clr.b	pt_lstuf(a4)		 ; it isn't a stuff

	move.w	qpc_mwhl,d3		 ; any wheel activity?
	beq.s	mse_done		 ; nope

	move.w	#$D9D1,d1		 ; ALT+up/ALT+down
	btst	#kb..ctrl,kb_stat(a3)	 ; CTRL pressed?
	beq.s	mse_dowheel		 ; ...no
	btst	#1,d0			 ; ct
	move.w	#$C9C1,d1		 ; ALT+left/ALT+right
mse_dowheel
	tst.w	d3
	bgt.s	mse_wheelup
	ror.w	#8,d1			 ; other key
	neg.w	d3
mse_wheelup
	subq.w	#1,d3
	clr.w	qpc_mwhl
	movem.l a1-a3,-(sp)
	move.l	sms.sysb,a3
	move.l	sys_clnk(a3),d0
	beq.s	mse_wheeldone
	move.l	sys_ckyq(a3),a2
	move.l	d0,a3
mse_wheelloop
	jsr	ioq_pbyt
	dbf	d3,mse_wheelloop
mse_wheeldone
	movem.l (sp)+,a1-a3

mse_done
	move.l	kb_extlk(a3),a4
	jmp	(a4)			 ; continue keyboard polling routine

mse_but dc.b  0,1,2,-1,-1,-1,-1,-1	 ; button decode

	end
