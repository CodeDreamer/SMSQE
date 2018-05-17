; Check for DEF ORIC/DEF FN + name in strings	Wolfgang Lenerz

	section utility
	xdef	ut_chkproc

;+++
; This routine checks whether a string contains a valid DEF PROC/DEF FN + name
; in all of their legal variations.
; These MUST be the first instructions on the line, i.e. a line such as:
; "a=a+1: DEF FN whatever" will NOT be rcognized!
; This routine does NOT check for a possible ":" after the
; DEFine clause, so there should be no multiple instructions either:
; "def fn whatever: a=a+1"
; will be recognized as a valid DEFine, but the name of the function
; will be deemed to be "whatever: a=a+1" instead of "whatever" only .....
; (a simple check for ":" could arrange that)
;
;		Entry			Exit
;	d2				=0: a valid 'define..' was found
;					=1: line is not 'define..' but another valid line
;					=-1: syntax error in a define clause
;	d3.w				length of FN/PROC name
;					(or corrupted if d2<> 0)
;	d4.l				bit 31: 0 proc or 1 func
;	a3	ptr to standard string	ptr to NAME of fn/proc
;					(w/o length word, length is in d3)	===
;					or corrupted if d2 <>0
;---


maj	equ	%11011111	; constant for upper casing
chkregs reg	d1		; corrupted in strpsp

none
	movem.l (sp)+,chkregs	; return if no DEFine
	moveq	#1,d2
	rts

ut_chkproc
	movem.l chkregs,-(sp)	; keep regs
	move.w	(a3)+,d3	; length of string
	cmp.w	#7,d3		; minimum length ('def fn a')
	blt.s	none		; can't be a valid define ->...

	bsr	strpspnum	; strip possible leading spaces and line nrs
				; (changes d3 & puts next char in d2)

	andi.b	#maj,d2 	; into upper case
	cmp.b	#'D',d2 	; is it 'D' ?
	bne.s	none		; no, can't be 'def', perhaps end def

	move.b	(a3)+,d2	; next char
	andi.b	#maj,d2
	cmp.b	#'E',d2 	; must be 'E',
	bne.s	none		; it isn't, perhaps 'end def'?
	subq.w	#1,d3		; new length

	move.b	(a3)+,d2	; next char
	andi.b	#maj,d2
	cmp.b	#'F',d2 	; must be 'F',
	bne.s	none		; it isn't, perhaps 'end def'?
	subq.w	#1,d3		; new length

; here the compulsory start of a DEFine was found. Let's see whether
; the optional rest of this keyword is also there.
; This can stop at any letter of 'ine' but MUST then be followed by a space

	move.b	(a3)+,d2	; now find the possible rest of DEFine
	andi.b	#maj,d2 	;
	cmp.b	#'I',d2 	; Is it 'I'
	bne.s	see_which	; no, see whether it is space->...
	subq.w	#1,d3		;
	move.b	(a3)+,d2		;
	andi.b	#maj,d2
	cmp.b	#'N',d2 	; 'N'
	bne.s	see_which
	subq.w	#1,d3
	move.b	(a3)+,d2
	andi.b	#maj,d2
	cmp.b	#'E',d2 	; 'E'
	bne.s	see_which
	subq.w	#1,d3
	move.b	(a3)+,d2	; next letter
; here we now heve the end of the DEF(ine) - there MUST be a space here

see_which
	move.b	-1(a3),d2	; get this without Upper case (space)
	cmp.b	#' ',d2 	; there MUST be a space
	bne	none		; it isn't, so it's not a simple define
	subq.w	#1,d3
	beq	error_line	; oops, only "define" w/o proc or fn

; now we have found the "define", let's see whether it is followed by
; PROC or FN in all valid combinations

	bsr	strpsp		; strip all possible spaces after 'def'
	bne	error_line	; oops, there wasn't anything alse->...

	tst.l	d4		; proc or function?
	bpl	defproc 	; proc

	andi.b	#maj,d2 	; char already returned by strpsp
	cmp.b	#'F',d2 	; is it "F"unction?
	bne	error_line	; no, perhaps Proc ->...

	move.b	(a3)+,d2
	subq.w	#1,d3
	beq	error_line	; oops, string not long enough->...

	andi.b	#maj,d2
	cmp.b	#'N',d2 	; is the F followed by N?
	beq.s	testfn2 	; yes, so must be followed by space->..
	cmp.b	#'U',d2 	; else MUST be U+N
	bne	error_line
	move.b	(a3)+,d2
	subq.w	#1,d3
	beq.s	error_line
	andi.b	#maj,d2
	cmp.b	#'N',d2
	bne.s	error_line
; as of here, optional chars for FuN(ction)
	move.b	(a3)+,d2
	subq.w	#1,d3
	beq.s	error_line
	andi.b	#maj,d2
	cmp.b	#'C',d2 	; is it 'C'?
	bne.s	testfn		; no, so might be end of keywrd->...
	move.b	(a3)+,d2
	subq.w	#1,d3
	beq.s	error_line
	andi.b	#maj,d2
	cmp.b	#'T',d2 	; 'T'?
	bne.s	testfn
	move.b	(a3)+,d2
	subq.w	#1,d3
	beq.s	error_line
	andi.b	#maj,d2
	cmp.b	#'I',d2 	; 'I'
	bne.s	testfn
	move.b	(a3)+,d2
	subq.w	#1,d3
	beq.s	error_line
	andi.b	#maj,d2
	cmp.b	#'O',d2 	; 'O'
	bne.s	testfn
	move.b	(a3)+,d2
	subq.w	#1,d3
	beq.s	error_line
	andi.b	#maj,d2
	cmp.b	#'N',d2 	; 'N'
	bne.s	testfn
testfn2
	move.b	(a3)+,d2	; get letter after end of keyword
	subq.w	#1,d3
	beq.s	error_line
; here we found a valid varition of FN / PROC; now there MUST be a space
testfn
	move.b	-1(a3),d2
	cmp.b	#' ',d2 	; is there a space?
	bne.s	error_line	; no, so bad syntax

get_name
	bsr	strpsp		; get rid of possible multiple spaces
	bne.s	error_line	; oops->...
	subq.l	#1,a3		; point to name of proc/fn now
	addq.w	#1,d3		; add last character again
	moveq	#0,d2		; show define found
exit	movem.l (sp)+,chkregs
	rts

error_line
	moveq	#-1,d2
	bra.s	exit

; here there is a valid DEFine, no FN after it: check whether it is PROC(edure)

defproc
	andi.b	#maj,d2
	cmp.b	#'P',d2 	; is it 'P'rocedure?
	bne	error_line	; no, so bad line->...
	move.b	(a3)+,d2
	subq.w	#1,d3		; new length
	beq	error_line	; oops, string not long enough->...
	andi.b	#maj,d2
	cmp.b	#'R',d2 	; 'R'
	bne	error_line
	move.b	(a3)+,d2
	subq.w	#1,d3
	beq	error_line
	andi.b	#maj,d2
	cmp.b	#'O',d2 	; 'O'
	bne.s	error_line
	move.b	(a3)+,d2
	subq.w	#1,d3
	beq.s	error_line
	andi.b	#maj,d2
	cmp.b	#'C',d2 	; 'C'
	bne.s	error_line

; as of here, optional chars for PROC(edure)
	move.b	(a3)+,d2
	subq.w	#1,d3
	beq.s	error_line
	andi.b	#maj,d2
	cmp.b	#'E',d2 	; 'E' ?
	bne.s	testfn
	move.b	(a3)+,d2
	subq.w	#1,d3
	beq.s	error_line
	andi.b	#maj,d2
	cmp.b	#'D',d2 	; 'D'
	bne	testfn
	move.b	(a3)+,d2
	subq.w	#1,d3
	beq.s	error_line
	andi.b	#maj,d2
	cmp.b	#'U',d2 	; 'U'
	bne	testfn
	move.b	(a3)+,d2
	subq.w	#1,d3
	beq.s	error_line
	andi.b	#maj,d2
	cmp.b	#'R',d2 	, 'R'
	bne	testfn
	move.b	(a3)+,d2
	subq.w	#1,d3
	beq	error_line
	andi.b	#maj,d2
	cmp.b	#'E',d2 	; 'E'
	bne	testfn
	bra	testfn2

strpsp
	move.b	(a3)+,d2	; fetch next char
	subq.w	#1,d3
	beq.s	errorl
	cmp.b	#' ',d2 	; strip spaces
	beq.s	strpsp
strpout
	moveq	#0,d1		; okay, fine
	rts
errorl
	moveq	#-1,d1		; end of line
	rts

strpspnum
	move.b	(a3)+,d2	; fetch next char
	subq.w	#1,d3
	beq.s	errorl
	cmp.b	#'?',d2 	; spaces, numbers, : etc.
	ble.s	strpspnum
	bra.s	strpout


	end
