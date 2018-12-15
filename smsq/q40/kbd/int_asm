; Keyboard Interrupt handler   V2.00    1998  Tony Tebby  QJUMP

	section kbdint

	xdef	kbd_istable

	xref	kbd_at102cvt

	include 'dev8_keys_q40'
	include 'dev8_keys_hwt'
	include 'dev8_smsq_kbd_keys'

;+++
; Interrupt server table (conventional priorities)
;---
kbd_istable
	dc.w	kbd_iserve-*,2<<8+4,hwt.kbd<<8	; just one server
	dc.w	0


;+++
; This routine handles keyboard interrupts
;---
kbd_iserve
	btst	#q40..kbd,q40_ir	 ; keyboard interrupt?
	beq.s	kbdi_exit		 ; ... no
	moveq	#0,d0
	move.b	q40_kcod,d0		 ; IBM keycode
	st	q40_kack		 ; acknowledge
	tst.b	kb_err(a3)		 ; strip queue on error?
	bgt.s	kbdi_exit		 ; ... yes

	bsr.s	kbd_at102cvt		 ; process key code

kbdi_exit
	move.l	(a5)+,a4		 ; standard exit
	move.l	(a5)+,a3
	jmp	(a4)


	end
