; KEYROW	 V2.00	    1990  Tony Tebby	QJUMP
;
;	KEYROW (row)
;
	section exten

	xdef	keyrow

	xref	ut_gxin1		 ; get one integer
	xref	ut_retin

	include 'dev8_keys_err'
	include 'dev8_keys_sbasic'
	include 'dev8_keys_qdos_sms'

keyrow
	jsr	ut_gxin1		 ; one integer
	bne.s	keyr_rts

	move.l	sb_buffb(a6),a3 	 ; parameter block
	add.l	a6,a3
	move.l	a3,a0			 ; pointer to fill

	move.w	#$0901,(a0)+		 ; keyrow
	clr.l	(a0)+			 ; four bits
	move.b	1(a6,a1.l),(a0)+
	move.b	#%10,(a0)+		 ; eight bit reply

	moveq	#sms.hdop,d0
	trap	#do.sms2

keyr_ret
	and.w	#$ff,d1
	move.w	d1,(a6,a1.l)
	jmp	ut_retin		 ; return it

keyr_rts
	rts
	end
