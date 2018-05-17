; Get name parameter

	section utility

	xdef	bi_gtnam
;+++
; Get name parameter
;
;		Entry				Exit
;	d3.w					no of parameters (always 1)
;	a1	arithmetic stack pointer	updated
;	a3	start of parameter pointer	points to next parameter
;	a5	end of parameter pointer	preserved
;
;	Error returns: err.ipar
;	Condition codes set on return
;---
bi_gtnam

bv.chrix equ	$11a
ca.gtstr equ	$116
err.bp	 equ	-15

	movem.l a2-a3/a5,-(sp)
	cmp.l	a5,a3
	beq.s	error_bp
	moveq	#$f,d0
	and.b	1(a6,a3.l),d0
	subq.b	#1,d0
	bne.s	get_nost	  no string, should be name
	move.l	a5,-(sp)
	lea	8(a3),a5
	move.w	ca.gtstr,a2
	jsr	(a2)
	move.l	(sp)+,a5
	tst.l	d0
	bne.s	get_end
;;;	   moveq   #3,d1	   ; this code seems to be redundant!!!
;;;	   add.w   0(a6,a1.l),d1
;;;	   bclr    #0,d1
;;;	   add.l   d1,$58(a6)
get_ok
	move.l	a1,$58(a6)
	moveq	#1,d3
	moveq	#0,d0
get_end
	movem.l (sp)+,a2-a3/a5
	addq.l	#8,a3
	rts
error_bp
	moveq	#err.bp,d0
	bra	get_end

get_nost
	moveq	#err.bp,d0
	moveq	#0,d1
	move.w	2(a6,a3.l),d1
	bmi.s	get_end
	lsl.l	#3,d1
	add.l	$18(a6),d1
	moveq	#0,d6
	move.w	2(a6,d1.l),d6
	add.l	$20(a6),d6
	moveq	#0,d1
	move.b	0(a6,d6.l),d1
	addq.l	#1,d1
	bclr	#0,d1
	move.w	d1,d4
	addq.l	#2,d1
	move.w	bv.chrix,a2
	jsr	(a2)
	move.l	$58(a6),a1
	add.w	d4,d6
get_ncby
	subq.l	#1,a1
	move.b	0(a6,d6.l),0(a6,a1.l)
	subq.l	#1,d6
	dbra	d4,get_ncby
	subq.l	#1,a1
	clr.b	0(a6,a1.l)
	bra	get_ok

	end
