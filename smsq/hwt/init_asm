; SMSQ Hardware table handling	V2.10	  1999  Tony Tebby

	section hwt

	xdef	hwt_init		 ; call init

;+++
; Hardware table setup - initialisation routine
;
;	d0    s
;	a3 c  p linkage block
;	a4    s
;	a5 c  u pointer to vector table in driver definition table
;	status return arbirary
;---
hwt_init
	move.w	(a5)+,d0		 ; get pointer to init and move on
	beq.s	hwti_exit		 ; no table

	jmp	-2(a5,d0.w)

hwti_exit
	rts

	end
