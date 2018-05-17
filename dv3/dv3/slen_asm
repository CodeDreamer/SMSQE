; DV3 set sector lengths	  V3.00 	  1992 Tony Tebby

	section dv3

	xdef	dv3_slen

	include 'dev8_dv3_keys'

;+++
; DV3 set sector lengths usinf ddf_slflag
;
;	a4 c  p drive definition
;	all registers preserved
;---
dv3_slen
	move.l	d0,-(sp)
	moveq	#16+7,d0		 ; sector length 0 = 128 bytes
	add.b	ddf_slflag(a4),d0
	bset	d0,d0			 ; set in upper half
	clr.w	d0
	swap	d0
	move.w	d0,ddf_slen(a4) 	 ; set length
	subq.w	#1,d0
	move.w	d0,ddf_smask+2(a4)	 ; and mask
	lsl.l	#7,d0
	swap	d0
	move.w	d0,ddf_xslv(a4) 	 ; extra slave blocks
	move.l	(sp)+,d0
	rts
	end
