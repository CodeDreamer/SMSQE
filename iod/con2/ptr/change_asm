;	Change size / colour resolution       V2.01   2000 Tony Tebby
;
; 2005-11-15  2.01  First kills jobs before wchka messes with outside wins (MK)
;
;	Registers:
;		Entry				Exit
;	D0					error code
;	D5	parameter passed to cn_disp_size routine
;	D6	parameter passed to cn_disp_size routine
;	D7	(byte, extended to word) colour resolution 0-4 passed to cn_disp_size
;	A3	linkage block
;
	section driver

	include 'dev8_keys_con'
	include 'dev8_keys_err'
	include 'dev8_keys_sys'
	include 'dev8_keys_iod'
	include 'dev8_keys_qdos_io'

	xdef	pt_changer

	xref	cn_disp_size

	xref	pt_hides
	xref	pt_wchka
	xref	pt_mrall
	xref	pt_install
	xref	pt_scansize
	xref	pt_scancrop
	xref	pt_wpap

;---
; Change display size / colour depth
;
;	d5 c  p passed to cn_disp_size
;	d6 c  p passed to cn_disp_size
;	d7 c  p colour depth
;	a3 c  p console linkage
;---
pt_changer
pch.reg reg	d0-d7/a0-a5
stk_d5d7 equ	5*4
stk_d6d7 equ	6*4
	movem.l pch.reg,-(sp)

	moveq	#pt.supmd,d4
	jsr	pt_hides		; hide sprite

	moveq	#-1,d1
	bsr.s	pch_wpap
	move.l	d1,(sp) 		; save old colour

	movem.l stk_d6d7(sp),d1/d2
	cmp.b	pt_cdpth(a3),d2 	; new colour depth?
	beq.s	pch_size		; ... no, just change size

	jsr	pt_install		; install new driver
	bra.s	pch_exitwp

pch_size
	moveq	#1,d3			; lock all
	jsr	pt_wchka

	movem.l stk_d5d7(sp),d5/d6/d7
	ext.w	d7
	jsr	cn_disp_size
	jsr	pt_scansize

	jsr	pt_scancrop		 ; remove jobs outside new screen
	moveq	#0,d3			 ; reset locks
	jsr	pt_wchka
	jsr	pt_mrall

pch_exitwp
	move.l	(sp),d1 		 ; old wallpaper
	moveq	#-1,d2
	move.l	d1,d0
	beq.s	pch_exitok
	bsr.s	pch_wpap

pch_exitok
	clr.l	(sp)
	movem.l (sp)+,pch.reg
	rts

pch_wpap
	moveq	#iop.wpap,d0
	moveq	#-1,d2
	move.l	sys_chtb(a6),a0
	move.l	(a0),a0 		 ; use channel 0
	move.l	iod_ioad(a3),a1
	jmp	(a1)

	end
