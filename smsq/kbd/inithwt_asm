; SMSQ KBD initialisation     1993  Tony Tebby   QJUMP

	section kbd

	xdef	kbd_inithwt

	xref	kbd_inits
	xref	kbd_istable
	xref	hwt_iserve

	include 'dev8_mac_vecx'

kbd_drd
	vecx	kbd_istable		 ; short driver definition
;+++
; initialise vectors, link in polling keyboard code
;
;	a3  r	base of keyboard linkage
;---
kbd_inithwt
	lea	kbdi_hwt,a0		 ; standard initialisation with hw table
	jmp	kbd_inits

kbdi_hwt
	lea	kbd_drd,a5		 ; driver def is only the server
	moveq	#0,d1			 ; no port number
	jmp	hwt_iserve		 ; set up the interrupt server

	end
