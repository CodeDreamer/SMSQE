; NFA/SFA USE use name query   v. 2.03	     (c) W. Lenerz 2012

; based on
; RAM Disk USE use name   V2.01     1989  Tony Tebby  QJUMP

; v. 2.03 put A1 in ari stack pointer after java call , remove use for flp drive
; v. 2.02 when xxx_USE device is called, pass this on to Java

	section exten

	xdef	nfa_useq
	xdef	sfa_useq
	xdef	win_useq
	xdef	mem_useq

	xref	ut_gxin1
	xref	ut_chkri
	xref	ut_retst

	include 'dev8_keys_java'
	include 'dev8_keys_sbasic'


;+++
;      NFA_USE$ xxx
;---
mem_useq
	move.l	#'MEM0',d7
	bra.s	comn_use
win_useq
	move.l	#'WIN0',d7
	bra.s	comn_use
nfa_useq
	move.l	#'NFA0',d7
	bra.s	comn_use
sfa_useq
	move.l	#'SFA0',d7

comn_use
	move.l	#260,d1
	jsr	ut_chkri		; ensure room for 256 bytes on maths stack
	move.w	#0,(a6,a1.l)		; preset wrong parameter or no parameter
	jsr	ut_gxin1		; get exactly one int, or nothing
	moveq	#jt5.drvq,d0
	dc.w	jva.trp5		; get data to java & back on the ari stack
	move.l	a1,sb_arthp(a6)
	tst.l	d0			; ooops, bad parameter
	bne.s	out
	jmp	ut_retst		; return data
out	rts

	end
