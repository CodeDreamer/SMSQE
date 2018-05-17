; SMSQ Hardware table handling	V2.10	  1999  Tony Tebby

	section hwt

	xdef	hwt_preset		 ; preset values

;+++
; Hardware table setup - preset values
;
;	d0    s
;	a3 c  p linkage block
;	a4    s
;	a5 c  u pointer to vector table in driver definition table
;	status return arbirary
;---
hwt_preset
	move.l	a0,-(sp)
	move.l	a5,a0
	move.w	(a5)+,d0		 ; get pointer to table and move on
	beq.s	hwtp_exit		 ; no table

	add.w	d0,a0

hwtp_loop
	move.w	(a0)+,d0		 ; next location to set
	beq.s	hwtp_exit		 ; no more to set

	lea	(a3,d0.w),a4
	move.w	(a0)+,d0		 ; length to set

hwtp_word
	move.w	(a0)+,(a4)+		 ; copy a word of preset data
	subq.w	#2,d0
	bgt.s	hwtp_word

	bra.s	hwtp_loop

hwtp_exit
	move.l	(sp)+,a0
	rts


	end
