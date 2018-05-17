; Find a string within a string 	 1992 Jochen Merz  V0.01

	section utility

	xdef	ut_instr

;+++
; Find a string within another string.
;
;		Entry			Exit
;	d0.w				position or -1 for not found
;	d2.b	if clear, ignore case	preserved
;	a0	ptr to main string	preserved
;	a1	ptr to search string	preserved
;---
ins.reg reg	  d1-d3/a0-a3
ut_instr
	movem.l ins.reg,-(sp)
	moveq	#0,d0			; start search at first character
ins_nxmn
	move.w	d0,d3
	add.w	(a1),d3 		; position plus search string length
	cmp.w	(a0),d3 		; longer than main string?
	bhi.s	ins_nofnd		; yes, so it can't be found

	lea	2(a0,d0.w),a2		; start comparison here
	lea	2(a1),a3		; that's the search string
	moveq	#0,d3
ins_char
	move.b	(a3)+,d1		; get character
	cmp.b	(a2)+,d1		; is it the same?
	beq.s	ins_nxch

	tst.b	d2			; ignore case?
	bne.s	no_match   ;;;ins_nxch	; no, string not here

	bchg	#5,d1			; toggle case
	cmp.b	-1(a2),d1		; now the same?
	beq.s	ins_nxch

no_match
	addq.w	#1,d0			; no match at this position
	bra.s	ins_nxmn		; check next character of main string

ins_nxch
	addq.w	#1,d3
	cmp.w	(a1),d3 		; whole search string checked?
	bne.s	ins_char		; no, next character

	bra.s	ins_found

ins_nofnd
	moveq	#-1,d0			; not found
ins_found
	movem.l (sp)+,ins.reg
	rts

	end
