; NFA/SFA USE use name	  v. 2.04	(c) L. Lenerz  2012-2017

; 2.04 implements win_drive
; 2.03 ?
; v 2.02 when xxx_USE device is called, pass this on to Java
    


; based on
; RAM Disk USE use name   V2.01     1989  Tony Tebby  QJUMP
;
    
	section exten

	xdef	nfa_use
	xdef	sfa_use
	xdef	win_drive
	xdef	win_use
	xdef	flp_use
	xdef	mem_use
			
	xref	iou_use
	xref	ut_gxin1
	xref	ut_gxst1


	include 'dev8_keys_java'


;+++
;      NFA_USE xxx
;---
mem_use
	move.l	#'MEM0',d7
	bra.s	comn_use
			
flp_use
	move.l	#'FLP0',d7
	bra.s	comn_use

win_drive				; win drive needs 2 params
	move.l	a3,d7
	add.l	#16,d7
	cmp.l	d7,a5
	bne.s	set_err
win_use
	move.l	#'WIN0',d7
	bra.s	comn_use
nfa_use
	move.l	#'NFA0',d7
	bra.s	comn_use
sfa_use
	move.l	#'SFA0',d7

comn_use
	moveq	#8,d0
	add.l	a3,d0
	sub.l	a5,d0			 ; just one parameter?
	blt.s	set_nam
dev_usen
	jsr	iou_use
	moveq	#jt5.use,d0
	dc.w	jva.trp5		; get data to java
	rts

set_nam subq.l	#8,a5
	jsr	ut_gxin1		; get one int
	bne.s	set_out
	move.w	(a6,a1.l),d6		; drive number
	move.l	a5,a3
	addq.l	#8,a5
	jsr	ut_gxst1		; get one string
	bne.s	set_out
	moveq	#jt5.driv,d0
	dc.w	jva.trp5		; get data to java
	tst.l	d0
set_out rts

set_err moveq	#-15,d0
	rts
	end
