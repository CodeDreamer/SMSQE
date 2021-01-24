; Q68 Keyboard interrupt handler   1.00 (c) 2020 W. Lenerz

; 2020-10-27	1.01	removed debug code

; based on Keyboard Interrupt handler	V2.00	 1998	Tony Tebby  QJUMP

	section kbdint

	xdef	kbd_intr

	xref	kbd_at102cvt

	include 'dev8_keys_q68'
	include 'dev8_keys_hwt'
	include 'dev8_keys_sys'
	include 'dev8_smsq_kbd_keys'
	include 'dev8_keys_qdos_sms'
  

; set up the interrupt handler

kbd_intr				; setup the interrupt "server"
	lea	kbd_iserve,a1		; interrupt routine
	lea	kbd_xlnk,a0
	move.l	a1,4(a0)
	moveq	#sms.lexi,d0
	trap	#1			; link it in
	st	kbd_status		; show that we use interrupts
	st	kbd_unlock		; keyboard may start up
	rts

; the following means that this code is not rommable.
kbd_xlnk dc.l	0			; external interrupt linkage
kbd_xiad dc.l	0			; absolute ext. int. service routine address

;+++
; This routine handles keyboard interrupts.
;---

kbd_iserve
	btst	#kbd.rcv,kbd_status	; any keyboard data?
	beq.s	kbd_out 		; ... no
	moveq	#0,D0
	move.b	kbd_code,d0		; IBM keycode
	st	kbd_unlock		; acknowledge key receipt
	move.l	sys_klnk(a6),a3 	; set pointer to linkage
					; this should clear the kbd.rcv bit in kbd_status
	tst.b	kb_err(a3)		; strip queue on error?
	bgt.s	kbd_out 		; ... yes

kbd	bra	kbd_at102cvt		; process key code
kbd_out rts

	end
