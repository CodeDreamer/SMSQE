; SMSQ Hardware table handling	V2.10	  1999  Tony Tebby

	section hwt

	xdef	hwt_vector		 ; set the vectors

;+++
; Hardware table setup - vectors
;
;	d0    s
;	a3 c  p linkage block
;	a4    s
;	a5 c  u pointer to vector table in driver definition table
;	status return arbirary
;---
hwt_vector
	move.l	a0,-(sp)
	move.l	a5,a0
	move.w	(a5)+,d0		 ; get pointer to table and move on
	beq.s	hwtv_exit		 ; no table

	add.w	d0,a0

hwtv_loop
	move.w	(a0)+,d0		 ; next vector to set
	beq.s	hwtv_exit		 ; no (more) vectors

	move.l	a0,a4
	add.w	(a0)+,a4		 ; address
	move.l	a4,(a3,d0.w)		 ; set vector
	bra.s	hwtv_loop

hwtv_exit
	move.l	(sp)+,a0
	rts


	end
