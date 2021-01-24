; Q68 Mouse Interrupt (or Poll) Routine  V1.01 (c) W. Lenerz 2017 - 2020

; 1.01 2020-11-12   removed dead code	 (wl)
; THIS CODE IS NOT ROMMABLE unless used as a minimodule (which it is)

; this is called from an interrupt
; mouse button 1 (left) = 1
; mouse button 2 (right) = 2
; both	left & right = 3
; middle button = 4

	section mouse

	xdef	mse_init

	xref	cpy_mmod

	include 'dev8_keys_con'
	include 'dev8_keys_err'
	include 'dev8_keys_iod'
	include 'dev8_keys_sys'
	include 'dev8_keys_qdos_sms'
	include 'dev8_smsq_kbd_keys'
	include 'dev8_keys_java'
	include 'dev8_keys_q68'
	include 'dev8_smsq_smsq_base_keys'

;--- mouse interrupt routine
;
; Coming here from the main interrupt loop, nearly all regs have been stacked
; and may be safely used - with the exception of A6 and D7 which should not be
; modified.
; On entry a6 points to the system variables.
; Since this is called from a loop in the main interrupt routine, exit should
; be made with RTS.
;
; Data from the mouse comes in packets of three bytes : button, x mvt, y mvt.
; (future expansion : posibly 4th byte for scroll wheel)
; For each byte an interrupt is generated. Only when the last byteof a packet
; is collected is the mouse data acted upon, in the meantime the intermediary
; bytes are stored here at label ms_btn & beyond. THIS MEANS THAT THIS CODE IS
; NOT ROMMABLE. At label ms_stat, I keep a count of what bytes were received
; so far.
;
; A mouse interrupt sets bit#0 of mouse_status : if this bit is set, a byte is
; ready to be collected. This bit is cleared automatically  when the value 1
; is written to mouse_unlock (for keys see dev8_keys_q68).
;
;-------------------------

mse.btn equ	2
mse.xmv equ	4
mse.ymw equ	6
mse.scr equ	8

mse_hdr
	dc.w	mse_intr-*
	dc.w	mse_end-*
mse_intr
	move.b	mouse_status,d3
	btst	#m.rcv,d3		; any mouse activity?
	beq.s	mse_done		; no, must have been another int ->
	move.l	sys_clnk(a6),d0 	; get kbd/pointer linkage
	beq.s	mse_done		; there is none?! ->
	move.l	d0,a5			; keep
	clr.l	d1
	move.b	mouse_code,d1		; get mouse code
	ext.w	d1			; mouse code as a word
	lea	ms_stat,a3		; mouse status area
	move.w	(a3),d0 		; current mouse status
	move.w	d1,(a3,d0.w)		; store this value
	addq.w	#2,d0			; next place to store value
	btst	#m.type,d3		; wheel mouse?
	bne.s	whl_mse 		; yes
	cmp.b	#8,d0			; no, got 3rd byte?
	bne.s	mse_add 		; no, not done yet
	clr.w	mse.scr(a3)		; there is no scroll
	bra.s	mse_pckt		; treat packet now
whl_mse cmp.w	#10,d0			; got the entire packet?
	beq.s	mse_pckt		; yes ->
mse_add move.w	d0,(a3) 		; no
	st	mouse_unlock		; release mouse receive status
mse_done
	rts
mse_pckt
; here a (3 or 4 byte) mouse packet is complete.
	move.w	#2,(a3) 		; prepare next packet

	move.l	mse.xmv(a3),d2		; X|Y mouse move
	beq.s	mse_scr 		; no move, check scroll
	move.w	d2,d1			; y move
	swap	d2			; x move
	neg.w	d1			; apparently vertical mvt is inverted
; note d1 and d2 are switched here, compared to std code
	move.w	d2,d0			; supposed count
	bpl.s	mse_ycount		; ... it's positive
	neg.w	d0
mse_ycount
	move.w	d1,d3			; other supposed count
	bge.s	mse_addinc		; ... it's positive
	neg.w	d3
mse_addinc
	add.w	d3,d0			; total count
	beq.s	mse_setinc		; ... none
	add.w	pt_xicnt(a5),d0 	; total so far
	lsr.w	#1,d0			; reduced
	add.b	pt_wake(a5),d0		; wake up speed
	lsr.w	#1,d0
	addq.w	#1,d0			; make sure that there is a move
mse_setinc
	move.w	d0,pt_xicnt(a5) 	; update count
	add.w	d2,pt_xinc(a5)
	add.w	d1,pt_yinc(a5)		; and distance moved

; now check mouse scrollwheel
mse_scr
	move.w	mse.btn(a3),d2		; mouse button
; bra.s mse_btn
	move.w	mse.scr(a3),d1		; mouse wheelmove
	andi.w	#15,d1			;
	move.w	d1,d3			; mouse wheel distance moved
	beq.s	mse_btn 		; no mouse wheel activity
	move.l	sys_ckyq(a6),d0 	; current kbd queue
	beq.s	mse_btn 		; none
	move.l	d0,a2
	move.w	#$D1D9,d1		; ALT+dwn/ALT+up
	move.l	sys_klnk(a6),d0
	beq.s	mse_wct
	move.l	d0,a3			; keyboard linkage
	btst	#kb..ctrl,kb_stat(a3)	; CTRL pressed?
	beq.s	mse_wct 		; ...no
	move.w	#$C1C9,d1		; ALT+right/ALT+left
mse_wct
	btst	#3,d3			; negative?
	beq.s	mse_wup 		; no->
	ror.w	#8,d1
	or.b	#$f0,d3
	neg.b	d3

mse_wup subq.w	#1,d3			; prepare for dbf
mse_wlp bsr.s	ioq_pbyt		; byte into kbd queue
	bne.s	mse_btn
	dbf	d3,mse_wlp

; now check mouse buttons
mse_btn 				; d2.w = button clicked
	andi.b	#7,d2			; only keep lower 3 bits
	cmp.b	#2,d2			; more than right/left click ?
	bgt.s	cntr			; yes, this is button 3, do centre button
cnt	move.b	d2,pt_bpoll(a5) 	; this is new button or old button still depressed
	beq.s	ms_exit 		; ... none
	clr.b	pt_lstuf(a5)		; it isn't a stuff
ms_exit
	st	mouse_unlock		; release mouse receive status
	rts

; copy of ioq_pbyt - leave here so it is copied within the minimodule!

	include dev8_keys_qu
ioq_pbyt
	tst.b	qu_eoff(a2)		end of file?
	bmi.s	ioq_eof 		... yes
	move.l	a3,-(sp)		save scratch
*
	move.l	qu_nexti(a2),a3 	next in
	move.b	d1,(a3)+		set byte
	cmp.l	qu_endq(a2),a3		next off end?
	bne.s	iqp_seti		... no
	lea	qu_strtq(a2),a3 	... yes, reset queue pointer
iqp_seti
	cmp.l	qu_nexto(a2),a3 	is there room?
	beq.s	ioq_nc			... no, set not complete
	move.l	a3,qu_nexti(a2) 	set next in
	move.l	(sp)+,a3		restore scratch register
	moveq	#0,d0			ok
ioq_out rts
ioq_eof
	moveq	#err.eof,d0		end of file
	rts
ioq_nc
	moveq	#err.nc,d0		... not complete
	move.l	(sp)+,a3		restore scratch register
	rts

cntr
; this is a modified copy of pt_button3
; on entry
; a3 = ptr to my mouse status area (below)
; a5 = kbd linkage
; a6 = sysvars
	move.l	sys_ckyq(a6),a2 	; stuff in here
	tas	pt_lstuf(a5)		; was last press a stuff? (this one is)
	blt.s	ms_exit 		; yes, don't do another
	move.b	pt_stuf1(a5),d1 	; get first character
	beq.s	ms_exit 		; isn't one
	bsr.s	ioq_pbyt		; there is one, stuff it
	move.b	pt_stuf2(a5),d1 	; is there another?
	beq.s	ms_exit
	bsr.s	ioq_pbyt		; put next byte in queue
	bra.s	ms_exit

; mouse status area
ms_stat dc.w	2			; what are we waiting for currently
ms_btn	dc.w	0			; button status
ms_xmov dc.w	0			; x movement
ms_ymov dc.w	0
ms_scrl dc.l	0
     

mse_end

; mouse init routine, just link in the external interrupt routine

mse_init
	lea	mse_hdr,a0
	jsr	cpy_mmod
	moveq	#sms.lexi,d0		; link in interrupt routine
	trap	#1
	rts

	end
