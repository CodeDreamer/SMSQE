; SBSEXT_TK2_ROM - TK2 ROM header			    2017 TT & MK

	section header

	xref	init
;	 xref	 mdv_io
	xref	ut_fdev

	include 'dev8_keys_qlv'
	include 'dev8_keys_sys'
	include 'dev8_keys_qdos_sms'

rom_header
	dc.l	$4AFB0001
	dc.w	0			; No BASIC extension
	dc.w	rom_init-rom_header	; Initialisation routine
; banner will be included here by the linker

	section init

rom_init
	movem.l a0/a3,-(sp)
;;;	   lea	   rom_header(pc),a1
;;;	   cmpa.l  #$000CC000,a1   ; Are we a mirror at $000CC000?
;;;	     bne.s no_mirror
;;;	   lea	   mdv_io(pc),a2
;;;	   moveq   #0,d0
;;;	   move.w  a2,d0
;;;	   movea.l d0,a2	   ; $000CC000 base -> $0000C000 base
;;;	   moveq   #sys_fsdl,d3
;;;	   bsr	   ut_fdev
;;;	   beq.s   dev_found	   ; Found, we're already active...
;;;no_mirror
	bsr.s	init
dev_found
	movem.l (sp)+,a0/a3
	rts

	end
