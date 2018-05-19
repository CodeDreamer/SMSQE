; get_str3
; get ONE string from the basic interpreter updating A3


; entry 				exit
; d0					0  or error
; a1 top of math stack			pointer to string on math stack
; a3 ptr to start of parameter		updated to point to next parameter
; a5 ptr to end of all parameters	preserved
;
; all other registers presered

	section procs

	xdef	get_str

	include dev8_keys_sbasic
	include dev8_keys_qlv
	include dev8_keys_err

myregs2 reg	d1-d4/d6/d7/a0/a2/a5
get_str movem.l myregs2,-(a7)
	moveq	#0,d0			; no string yet
	cmp.l	a3,a5			; any param at all?
	beq.s	err			; no->
	move.b	1(a6,a3.l),d0		; type of parameter
	andi.b	#$f,d0
	subq.b	#1,d0			; was it a string?
	beq.s	is_str			; yes, get it
	move.l	d0,d7			; zero upper word of d7
	move.w	2(a6,a3.l),d7		; point to name table
	lsl.l	#3,d7			; * 8 (eight bytes per entry)
	add.l	sb_nmtbb(a6),d7 	; add offset

	move.w	2(a6,d7.l),a0		; point to value now
	add.l	sb_nmlsb(a6),a0 	; add offset
	moveq	#0,d1
	move.b	(a6,a0.l),d1		; length of string
	move.l	d1,d7			; keep
	addq.w	#3,d1
	bclr	#0,d1			; add length word & make even
	move.l	a0,d6			; keep
	move.w	qa.resri,a2
	jsr	(a2)			; get space on ari stack
	move.l	sb_arthp(a6),a1 	, point to top of ari stack
	move.l	d6,a0			; pointer to string
	move.w	d7,(a6,a1.l)		; set length
	beq.s	ok			; no length, done
	addq.l	#2,a1			; point first char
	subq.l	#1,d7			; dbf
loop	addq.l	#1,a0			; point next char
	move.b	(a6,a0.l),(a6,a1.l)	; transfer it
	addq.l	#1,a1			; space for next char
	dbf	d7,loop
	move.l	sb_arthp(a6),a1
	bra.s	ok

err	moveq	#err.ipar,d0
	bra.s	out

is_str	lea	8(a3),a5		; point to end of this param
	move.w	sb.gtstr,a2
	move.w	(a6,a3.l),d7		; keep type word!
	jsr	(a2)			; get string
	move.w	d7,(a6,a3.l)
	subq.w	#1,d3			; one string param
	bne.s	err			; ooops
	moveq	#0,d1
	move.w	(a6,a1.l),d1
	addq.w	#3,d1
	bclr	#0,d1
	add.l	d1,sb_arthp(a6)
ok	moveq	#0,d0
out	movem.l (a7)+,myregs2
	rts
	end
