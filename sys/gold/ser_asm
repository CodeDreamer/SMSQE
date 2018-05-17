; Gold card patch SER Transmit timing

	section patch

	xdef	ser_xdly
;+++
; Patch SER transmit to introduce about 30us after test xmit buffer delay
;---
ser_xdly
	addq.l	#4,(sp) 	 ; skip btst #1,(a1)
	lea	$18020,a1
	btst	#1,(a1) 	 ; test
	bne.s	ser_trts	 ; do nought, no delay required

	movem.l d0-d7/a0-a6,-(sp)
	lea	$20000,a6
	movem.l (a6)+,d0-d7/a0-a5
	movem.l d0-d7/a0-a5,-(a6)
	movem.l (a6)+,d0-d7/a0-a5
	movem.l d0-d7/a0-a5,-(a6)
	movem.l (sp)+,d0-d7/a0-a6  ; delay but do not smash CC
ser_trts
	rts
	end
