; DIMN		V2.00	   1990  Tony Tebby   QJUMP
;
;	DIMN (array,index)
;
	section	exten

	xdef	dimn

	xref	ut_gxin1		 ; get one integer
	xref	ut_retin

	include	'dev8_keys_err'
	include	'dev8_keys_sbasic'

dimn
	moveq	#0,d5			 ; return 0
	move.l	sb_arthp(a6),a1		 ; to here
	moveq	#1,d4			 ; assume dimn one
	addq.l	#8,a3			 ; next	param
	cmp.l	a3,a5
	blt.s	dimn_ipar		 ; no param at all
	beq.s	dimn_look
	jsr	ut_gxin1		 ; one integer
	bne.s	dimn_rts
	move.w	(a6,a1.l),d4
	addq.l	#2,a1
	ble.s	dimn_ret		 ; out of range
dimn_look
	cmp.b	#3,-8(a6,a3.l)		 ; dimensioned?
	bne.s	dimn_ret
	move.l	-4(a6,a3.l),a0		 ; pointer to descriptor
	add.l	sb_datab(a6),a0
	cmp.w	4(a6,a0.l),d4		 ; dimension out of range?
	bgt.s	dimn_ret
	lsl.w	#2,d4
	add.w	d4,a0
	move.w	2(a6,a0.l),d5		 ; actual dim
dimn_ret
	sub.w	#2,a1
	move.w	d5,(a6,a1.l)
	jmp	ut_retin		 ; return it

dimn_ipar
	moveq	#err.ipar,d0
dimn_rts
	rts
	end
