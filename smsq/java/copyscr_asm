* Copy screen from $20000 to actual screen.
* v.1.02  Copyright (c) W. Lenerz 2016-2017
*
*  1.01 revamped for jva_qlscremu
*  1.00 initial version



	section misc

	include 'dev8_keys_java'
	include 'DEV8_keys_err'
	include 'DEV8_keys_qlv'
	include 'dev8_mac_proc2'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_sbasic'


	xdef	cpyscr_init

	xref	ut_gxin1
	xref	ut_rtint


cpyscr_init
	lea	proc_tab(pc),a1
	move.w	sb.inipr,a2
	jmp	(a2)

proc_tab
	proc_stt
	proc_def JVA_QLSCREMU
	proc_end
	proc_stt
	proc_def JVA_IS_QLSCREMU% , is_emu
	proc_end


;***********************************************************
;
; JVA_QLSCREMU mode {,xorig,yorig}
;
; start/stop copying the screen.
;
; NOT MPLEMENTED : x and y orig where to copy the screen to (default : 0,0)
;
;***********************************************************

jva_qlscremu
	jsr	ut_gxin1
	bne.s	out			; get exactly one int
	move.w	(a6,a1.l),d1		; mode
	addq.l	#2,a1
	move.l	a1,sb_arthp(a6) 	; clean up stack
	blt.s	nimp			; automatic not implemented yet
	moveq	#0,d0
common	dc.w	jva.trpc
out	rts
nimp	moveq	#err.nimp,d0
	rts

is_emu
	moveq	#1,d0
	bsr.s	common
	jmp	ut_rtint


	end
