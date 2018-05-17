; Error message routines  QL compatible    1989  Tony Tebby

	section uq

	xdef	uq_wersy
	xdef	uq_werms

	xref	uq_wtext

	include 'dev8_keys_sys'
	include 'dev8_keys_qdos_sms'

uq_wersy
	move.l	a0,-(sp)
	sub.l	a0,a0
	bsr.s	uq_werms
	move.l	(sp)+,a0
	rts

uq_werms
	tst.l	d0			 ; anything to do?
	bge.s	uwe_rts 		 ; ... no
uwe.reg reg	d0/a1
	movem.l uwe.reg,-(sp)
	move.l	d0,a1
	moveq	#sms.mptr,d0
	trap	#do.sms2
	bsr.s	uq_wtext
	movem.l (sp)+,uwe.reg
	tst.l	d0
uwe_rts
	rts
	end
