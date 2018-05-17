; Write message routine  QL compatible	  1989  Tony Tebby

	section uq

	xdef	uq_wtext

	include 'dev8_keys_qdos_io'

uq_wtext
uwt.reg reg	d1/d2/a1
	moveq	#forever,d3		 ; assume wait
	move.l	a0,d0			 ; channel 0?
	bne.s	uwt_do			 ; ... no
	moveq	#no.wait,d3		 ; ... yes, no wait
	bsr.s	uwt_do			 ; try
	beq.s	uwt_rts
	move.l	#$00010001,a0		 l try channel 1!!!!
uwt_do
	movem.l uwt.reg,-(sp)
	move.w	(a1)+,d2		 ; string length
	moveq	#iob.smul,d0		 ; send multiple
	trap	#do.io
	movem.l (sp)+,uwt.reg
	tst.l	d0
uwt_rts
	rts
	end
