; Gold card MG Graphics patch

	section patch

	xdef	gl_graph


;+++
; Patch for MG Graphics
;---
gl_graph
	clr.w	(a7)			 ; return was here!
	bra.s	glg_loop		 ; new instruction
glg_point
	subq.w	#1,(sp)
	beq.s	glg_last
	jsr	$2384
glg_loop
	move.w	a4,d4
	blt.s	glg_last
	subq.w	#2,d4
	bgt.s	glg_td7
	move.l	d1,d4
	sub.l	$ffffffe8(a6),d4
	bge.s	glg_dd4
	neg.l	d4
glg_dd4
	subq.l	#1,d4
	bgt.s	glg_td7
	move.l	d0,d4
	sub.l	$ffffffec(a6),d4
	bge.s	glg_tmode
	neg.l	d4
glg_tmode
	btst	#$3,$28034
	beq.s	glg_d1d4
	subq.l	#1,d4
glg_d1d4
	subq.l	#1,d4
	ble.s	glg_last
glg_td7
	tst.l	d7
	bne.s	glg_2364
	tst.l	d6
	beq.s	glg_last
glg_2364
	bge.s	glg_2372
	tst.b	$ffffffdc(a6)
	bgt.s	glg_2372
	jsr	$23e8
	bra.s	glg_loop
glg_2372
	tst.l	d6
	bge.s	glg_point
	tst.b	$ffffffdc(a6)
	blt.s	glg_point
	jsr	$242e
	bra.s	glg_loop
glg_last
	addq.l	#4,a7
	jmp	$2384

	end
