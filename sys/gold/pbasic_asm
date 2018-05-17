; Gold card patch SuperBASIC

	section patch

	xdef	sb_chan
;+++
; Patch for SuperBASIC channel, check only lsw of channel ID
;---
sb_chan
	bge.s	sb_neg		 ; check outside table jumped upon
	move.l	$0(a6,a0.l),d0	 ; move jumped upon
	tst.w	d0		 ; check channel number only
	rts
sb_neg
	moveq	#-6,d0		 ; set channel unset
	rts
	end
