; MEMD_WRITE keyword for the MEM device   V1.00 (c) 2014 W. Lenerz


; MEMD_WRITE driveNumber - writes that drive back to the disk, if possible

	section nfa

	include 'dev8_keys_java'
	include 'dev8_keys_err'
	include 'DEV8_keys_qlv'

	xdef	memd_write

memd_write
	move.w	sb.gtint,a2
	jsr	(a2)
	bne.s	out
	subq.w	#1,d3			; get just 1 int
	bne.s	err_bp
	clr.l	d2
	move.w	(a6,a1.l),d2		; d2 = drive number
	move.l	#'MEM0',d1
	addq.l	#2,$58(a6)
	moveq	#jt5.mwrt,d0
	dc.w	jva.trp5
out	rts
err_bp
	moveq	#err.ipar,d0
	rts

	end
