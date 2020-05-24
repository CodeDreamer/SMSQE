; SMSQmulator WIN Driver set drive removable status v. 1.00

;
; copyright (C) w. Lenerz 2020 published under the SMSQE licence
; v. 1.00 initial version

	section nfa

	xdef	win_remv


	include 'dev8_keys_qlv'
	include 'dev8_keys_err'
	include 'dev8_keys_java'

win_remv move.w sb.gtlin,a2
	jsr	(a2)
	bne.s	wr_exit
	moveq	#1,d2			; preset status - is removable
	move.l	(a6,a1.l),d1		; drive nbr
	subq.w	#1,d3			; one more param?
	blt.s	wr_err			; there wasn't even one!
	beq.s	cl_java
	move.l	4(a6,a1.l),d2		; status (0 =  not removable, 1=removable)
cl_java moveq	#jte.remv,d0
	dc.w	jva.trpE		; call java, sets status & d0
wr_exit rts
wr_err	moveq	#err.ipar,d0
	rts

	end
