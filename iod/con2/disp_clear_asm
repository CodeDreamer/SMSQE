; Standard clear display	 2000	Tony Tebby

	section con

	xdef	cn_disp_clear

	include 'dev8_keys_con'

;---
; clear screen
;	a3 c  p pointer to pointer linkage
;+++
cn_disp_clear
cnd.reg reg	a0
	move.l	a0,-(sp)

	move.l	pt_scren(a3),a0
	move.l	pt_scrsz(a3),d0

cnd_cls
	clr.l	(a0)+
	subq.l	#4,d0
	bgt.s	cnd_cls

	move.l	(sp)+,a0
	moveq	#0,d0
	rts

	end
