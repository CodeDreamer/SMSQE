; SBAS_ISUBA - Sub-Array Creation    1994 Tony Tebby

	section sbas

	xdef	sb_isuba
	xdef	sb_isubas
	xdef	sb_isubai

	xref	sb_fint
	xref	sb_aldat

	xref	sb_ierset

	include 'dev8_keys_sbasic'
	include 'dev8_keys_err4'
	include 'dev8_mac_assert'

;+++
; Copy and process array descriptor
;
;	D1	scratch
;	D2	scratch
;	D3 c  u lsw is variable type (string may be set to substring or integer)
;	D4-D5	lsw scratch
;	D6    p is limit of arithmetic stack (with some bytes spare)
;	D7	scratch
;	A0  r	is pointer to new descriptor
;	A1 c  u is pointer to arithmetic stack
;	A2	scratch
;	A3 c  p is pointer to name table
;	A4 c  u is pointer to program (number of indices)
;	A5 c  s old array descriptor
;	A6 c  p is pointer to system variables
;
; If the result is a single value, the status is returned zero.
; Otherwise, the status is returned positive.
;---
sb_isuba
	move.w	(a4)+,d4		 ; number of indices
	move.w	(a4)+,d5		 ; number of values
	beq.s	sis_setmask
	move.l	a6,a2			 ; use buffer to convert
	add.l	sb_cmdlb(a6),a2

sis_cmloop
	cmp.w	#ar.int,(a1)+		 ; is it an integer already?
	beq.s	sis_move		 ; ... yes
	jsr	sb_fint
sis_move
	move.w	(a1)+,-(a2)		 ; move
	subq.w	#1,d5
	bgt.s	sis_cmloop

sis_setmask
	move.l	(a4)+,d2

;+++
; Copy and process array descriptor, indexes set up
;
;	D1	scratch
;	D2 c  s index types
;	D3 c  u lsw is variable type (string may be set to substring or integer)
;	D4 c  s lsw number of indices
;	D5	lsw scratch
;	D6    p is limit of arithmetic stack (with some bytes spare)
;	D7	scratch
;	A0  r	is pointer to new descriptor
;	A1 c  u is pointer to arithmetic stack
;	A2	scratch
;	A3 c  p is pointer to name table
;	A4 c  p is pointer to program
;	A5 c  s is pointer to old array descriptor
;	A6 c  p is pointer to system variables
;
; If the result is a single value, the status is returned zero.
; Otherwise, the status is returned positive.
;---
sb_isubas
	moveq	#4,d1
	move.w	4(a5),d0		 ; 1/4 space required
	beq.l	sis_iind
	add.w	d0,d1
	lsr.w	#1,d1			 ; rounded
	lsl.w	#3,d1			 ; to multiple of 8
	move.l	a2,d5
	jsr	sb_aldat
	move.l	d5,a2
	move.l	d1,(a0)+		 ; length in heap

;+++
; Process array descriptor (could be in place)
;
;	D1	scratch
;	D2 c  s index types
;	D3 c  u lsw is variable type (string may be set to substring or integer)
;	D4 c  s lsw number of indices
;	D5	lsw scratch
;	D6    p is limit of arithmetic stack (with some bytes spare)
;	D7	scratch
;	A0 c  p is pointer to new descriptor (=a5)
;	A1 c  u is pointer to arithmetic stack
;	A2 c	pointer to indices (integers, ms index first)
;	A3 c  p is pointer to name table
;	A4 c  p is pointer to program
;	A5 c  s is pointer to old descriptor (=a0)
;	A6 c  p is pointer to system variables
;
; If the result is a single value, the status is returned zero.
; Otherwise, the status is returned positive.
;---
sb_isubai
	move.l	d6,-(sp)		 ; and a reg
	move.l	a0,-(sp)		 ; save pointer to desc
	move.l	(a5)+,(a0)+		 ; base
	move.w	(a5)+,d7		 ; number of indices
	move.w	d7,d5
	addq.l	#2,a0
	moveq	#0,d1			 ; offset to base
	bra.s	sis_lend

sis_loop
	subq.w	#1,d5			 ; number of indices left
	bgt.s	sis_doind		 ; OK
	cmp.w	#nt.st,d3		 ; is it last index of string?
	ble.s	sis_lsind		 ; ... yes, (or substring)

sis_doind
	lsr.l	#1,d2			 ; range?
	bcc.s	sis_nind		 ; ... no, no index range
	moveq	#0,d0
	move.w	(a5)+,d6		 ; maximum index
	lsr.l	#1,d2			 ; lower limit of range?
	bcc.s	sis_urnge		 ; ... no
	move.w	(a2)+,d0		 ; ... yes
sis_urnge
	lsr.l	#1,d2			 ; upper limit of range?
	bcc.s	sis_crnge		 ; ... no
	cmp.w	(a2),d6 		 ; too large?
	blo.s	sis_inor		 ; ... yes
	move.w	(a2)+,d6		 ; ... no
sis_crnge
	lsr.l	#1,d2			 ; skip spare flag bit
	sub.w	d0,d6			 ; size of range (-1)
	beq.s	sis_nindr		 ; none
	blt.s	sis_inor		 ; ... at all

	move.w	d6,(a0)+		 ; new index limit
	move.w	(a5)+,d6		 ; multiplier
	move.w	d6,(a0)+
	mulu	d6,d0
	add.l	d0,d1
	bra.s	sis_lend

sis_nind
	lsr.l	#3,d2			 ; skip flag bits
	move.w	(a2)+,d0		 ; index
	cmp.w	(a5)+,d0		 ; in range?
	bhi.s	sis_inor		 ; ... no

sis_nindr
	subq.w	#1,d7			 ; one fewer indices in result
	mulu	(a5)+,d0		 ; additional offset
	add.l	d0,d1

sis_lend
	dbra	d4,sis_loop		 ; take another index

	tst.w	d5
	bge.s	sis_cend
	bra.s	sis_iind		 ; oops! too many indices in list

sis_copy
	move.l	(a5)+,(a0)+		 ; copy remaining indices
sis_cend
	dbra	d5,sis_copy

	cmp.w	#nt.fp,d3		 ; floating point?
	blt.s	sis_setoff		 ; ... string
	bgt.s	sis_setint

	move.l	d1,d0
	add.l	d0,d1
	add.l	d0,d1			 ; offset *3
sis_setint
	add.l	d1,d1			 ; offset *6 or *2
sis_setoff
	move.l	(sp)+,a0
	move.l	(sp)+,d6
	move.l	a0,a2
	add.l	d1,(a2)+		 ; set new offset
	move.w	d7,(a2) 		 ; and number of indices
	rts

sis_iind
	moveq	#ern4.iind,d0
	jmp	sb_ierset
sis_inor
	moveq	#ern4.inor,d0
	jmp	sb_ierset

sis_lsind
	bne.s	sis_lssind		 ; last substring index
	tst.w	d4			 ; last given index?
	bne.s	sis_iind		 ; ... no
	clr.w	d3			 ; set to substring
	lsr.w	#1,d2			 ; range?
	bcc.s	sis_snrnge		 ; ... no

	addq.l	#2,d1			 ; adjust accumulator

sis_ssrnge
	moveq	#1,d0			 ; from one
	move.w	(a5)+,d6		 ; to maximum index
	lsr.l	#1,d2			 ; lower limit of range?
	bcc.s	sis_ssurnge		 ; ... no
	move.w	(a2)+,d0		 ; ... yes
sis_ssurnge
	lsr.l	#1,d2			 ; upper limit of range?
	bcc.s	sis_defto		 ; ... no
	cmp.w	(a2),d6 		 ; too large?
	blo.s	sis_inor		 ; ... yes
	move.w	(a2)+,d6		 ; ... no
	bra.s	sis_ssset
sis_defto
	cmp.b	#1,d7			 ; only one index left?
	bne.s	sis_ssset		 ; ... no, use dimension for TO
	move.l	-6(a0),a5		 ; base
	move.w	-2(a5,d1.l),d6		 ; string length for TO

sis_ssset
	subq.w	#1,d0			 ; adjust lower index
	blt.s	sis_inor
	sub.w	d0,d6			 ; size of range
	blt.s	sis_inor		 ; ... none at all
	move.w	d6,(a0)+
	move.w	#1,(a0)+		 ; last index
	add.l	d0,d1			 ; accumulate offset
	bra	sis_setoff

sis_snrnge
	moveq	#0,d0
	move.w	(a2)+,d0
	beq.s	sis_slen		 ; the length
	cmp.w	(a5)+,d0		 ; in range?
	bhi.s	sis_inor
	move.l	#$00010001,(a0)+	 ; last index limit and multiplier
	addq.w	#1,d0
	add.l	d0,d1			 ; offset
	bra	sis_setoff

sis_slen
	addq.w	#nt.in,d3
	subq.w	#1,d7			 ; one fewer indices in result
	bra	sis_setoff

sis_lssind
	tst.w	d4			 ; last given index?
	bne.s	sis_iind		 ; ... no
	lsr.w	#1,d2			 ; range?
	bcs.s	sis_ssrnge		 ; ... yes
	moveq	#0,d0
	move.w	(a2)+,d0
	move.w	d0,d6			 ; n TO n
	cmp.w	(a5)+,d6		 ; upper in range?
	bls.s	sis_ssset
	bra	sis_inor

	end
