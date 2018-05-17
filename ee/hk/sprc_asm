; Set preamble code  V2.00     1988   Tony Tebby   QJUMP

	section hotkey

	xdef	hk_sprc

	xref	hk_gdstart

	include 'dev8_ee_hk_xhdr'

;+++
; Set preamble code in header with calls to guardian
;
;	d1 c  p msw border / lsw grabber memory (0 none, -ve ask)
;	d2 c  p guardian window size (0 none, -ve unlocked)
;	d3 c  p guardian window origin
;	a0 cr	running pointer to header
;	status return zero
;---
hk_sprc
	pea	hk_gdstart
	move.w	#hkh.jsrl,(a0)+ 	 ; jsr
	move.l	(sp)+,(a0)+		 ; ... guardian
	move.l	d2,(a0)+		 ; set guardian
	move.l	d3,(a0)+
	move.l	d1,(a0)+		 ; with this border and memory
	clr.w	(a0)+		;***** Hotkey code assumes that this header
				;*&*** is a fixed length
	moveq	#0,d0
	rts
	end
