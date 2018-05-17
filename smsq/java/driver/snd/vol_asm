; SSSS set sound volume   V1.00 (c) 2014 W. Lenerz

	section volume


	include 	dev8_keys_err
	include 	dev8_keys_java
	include 	dev8_keys_sbasic
	include 	dev8_keys_qlv


	xdef	jvavol

jvavol
	move.w	sb.gtint,a2
	jsr	(a2)			; get one int
	bne.s	out
	subq.w	#1,d3
	bne.s	errbp
	move.w	(a6,a1.l),d1
	addq.l	#2,sb_arthp(a6)
	moveq	#jt5.svol,d0
	dc.w	jva.trp5
out	rts

errbp	moveq	#err.ipar,d0
	rts


	end
