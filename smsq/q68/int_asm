; Q68 Interrupt server	V1.02  (c) W. Lenerz 2016
;
; based on
; SMSQmulator Interrupt server	V1.00  (c) W. Lenerz 2012
;
; 2018-11-22	1.02  correct nbr of ticks (49, not 50) for clock counter (wl)

	section qd

	xdef	q68_int2
	xdef	q68_int2h
	xref	spp_rxser


	include 'dev8_keys_sys'
	include 'dev8_keys_psf'
	include 'dev8_keys_q68'
	include 'dev8_keys_qlhw'
	include 'dev8_keys_iod'
	include 'dev8_smsq_smsq_base_keys'

;+++
; Q68 Interrupt Server
; For the time being, there are 4 interrupts that may be generated:
; The 50 Hz frame, SER receive, SER transmit,  mouse interrupt.
;
; When a frame interrupt occurs, bit#3 of pc_intr is set. It is not set by the
; other interrupts. A mouse interrupt sets bit#0 of mouse-status. This bit is
; cleared when a value is written to mouse-unlock. Similar schemes exist
; for serial ports' rx and tx interrupts.
;
;---
q68_int2h				; minimodule header
	dc.w	q68_int2-*
	dc.w	q68_end-*+2

q68_int2
; set primary stack frame and locate system variable base
	movem.l psf.reg,-(sp)		; save main working registers
	move.l	sms.sysb,a6		; system variable base

;!!!!!!!!!!!DEBUG	      --------|
	genif	debug = 1	      |
	cmp.l	#'gold',q68_jflg      | ; On smsq68mulator, mouse doesn't generate interrupt
	bne.s	check		      |
	bsr.s	do_lexi2	      | ; check mouse, ser
	move.b	#pc.intrf,pc_intr     | ; show frame interrup
	endgen			      | ;
;!!!!!!!!!!!!!!!!!	      --------|

check
	moveq	#pc.intrf,d7
	and.b	pc_intr,d7		; interrupt from general interrupt reg
	beq.s	do_lexi 		; it's not a frame interrupt ->

; adjust clock
frame	subq.w	#1,sys_rtcf(a6) 	; 50 Hz counter
	bpl.s	shd_poll		; ... ok
	move.w	#49,sys_rtcf(a6)	; and count down again (1.02, was #50 before)
	addq.l	#1,sys_rtc(a6)		; another second gone

shd_poll
	addq.w	#1,sys_pict(a6) 	; one more poll
	move.l	sms.spoll,a5
	jmp	(a5)			; polling interrupt   (calls smsq_shd_poll_asm)

; do external interrupts
do_lexi
	bsr.s	do_lexi2
	movem.l (sp)+,psf.reg
	rte

do_lexi2
	movem.l d0-d6/a0-a5,-(sp)
	lea	sys_exil(a6),a0 	 ; list of ext int actions (mouse, ser)

qdie_sloop
	move.l	(a0),d0
	beq.s	qdie_done		 ; ... done
	move.l	d0,a0
	lea	-iod_xilk(a0),a3	 ; base of linkage
	move.l	iod_xiad(a3),a4 	 ; address of interrupt routine
	move.l	a0,-(sp)
	jsr	(a4)			 ; do routine
	move.l	(sp)+,a0		 ; restore
	bra.s	qdie_sloop
qdie_done
	movem.l (sp)+,d0-d6/a0-a5
	rts
q68_end

	end
