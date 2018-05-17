;	uti_beep_asm

	include dev8_keys_qdos_sms

	section utility

	xdef	beep_ok
	xdef	beep_err,beep_err1


*++++
*	OK-Beep
*
*	all regs preserved
*----
dat_ok
	dc.b	$0a,$08 		; sound, 8 parameters
	dc.l	%1010101010101010	; all 8 bits of each
	dc.b	8			; pitch1=8
	dc.b	0			; pitch2=0
	dc.b	0,0			; no interval
	dc.b	4000&$FF,4000>>8	; length=4000
	dc.b	0,0			; no step, wrap, random or fuzz
	dc.b	1			; no reply
*
	ds.w	0
*++++
*		Error-Beep
*
*	all regs preserved
*----
dat_err
	dc.b	$0a,$08 		; sound, 8 parameters
	dc.l	%1010101010101010	; all 8 bits of each
	dc.b	120			; pitch1=120
	dc.b	0			; pitch2=0
	dc.b	0,0			; no interval
	dc.b	12000&$FF,12000>>8	; length=12000
	dc.b	0,0			; no step, wrap, random or fuzz
	dc.b	1			; no reply
*
	ds.w	0

dat_err1
	dc.b	$0a,$08 		; sound, 8 parameters
	dc.l	%1010101010101010	; all 8 bits of each
	dc.b	120			; pitch1=120
	dc.b	0			; pitch2=0
	dc.b	0,0			; no interval
	dc.b	4000&$FF,4000>>8	; length=4000
	dc.b	0,0			; no step, wrap, random or fuzz
	dc.b	1			; no reply
*
	ds.w	0

beepreg 	reg	d0-d1/d5/d7/a3

beep_ok
	movem.l beepreg,-(sp)
	lea	dat_ok,a3		; beep ok
	bra.s	beep_cont

beep_err
	movem.l beepreg,-(sp)
	lea	dat_err,a3		; beep error
	bra.s	beep_cont

beep_err1
	movem.l beepreg,-(sp)
	lea	dat_err1,a3		; beep error 1

beep_cont
	moveq	#sms.hdop,d0
	trap	#do.sms2
	movem.l (sp)+,beepreg
	rts



	end
