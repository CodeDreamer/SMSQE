; SBAS_CMPOPX - Convert Expression to Operations   V2.00     1994 Tony Tebby

	section sbas

	xdef	sb_cmpopx
	xdef	sb_cmpopx_int
	xdef	sb_cmpopx_ic

	xref	sb_cmpop_fn
	xref	sb_cmpopp
	xref	sb_ermess

	xref	qa_nint

	include 'dev8_keys_sbasic'
	include 'dev8_smsq_sbas_comp_keys'
	include 'dev8_smsq_sbas_inter_keys'
	include 'dev8_mac_assert'


;+++
; SBASIC fetch integer expression.
; For the moment, this routine will only produce an integer expression if
; it is constant.
;
;	d1  r	integer if expression constant
;	d6 c  p line number / statement number
;	a0 c  u pointer to program operations
;	a3 c  u pointer to compiler tokens
;	a5    p
;	a6 c  p pointer to SuperBASIC variables
;
;	Returns at +2 if constant
;	Status return standard
;---
sb_cmpopx_int
	bsr.s	sb_cmpopx_ic
	bra.s	sb_cmpopx		 ; not constant, fetch expression
	move.w	#bo.conin,(a0)+ 	 ; constant integer
	move.w	d1,(a0)+
	addq.l	#2,(sp) 		 ; move on
	moveq	#0,d0
	rts

;+++
; SBASIC check if expression is constant (and if it is, get it and int it)
;
;	d1  r	integer if expression constant
;	d6 c  p line number / statement number
;	a0 c  u pointer to program operations
;	a3 c  u pointer to compiler tokens
;	a6 c  p pointer to SuperBASIC variables
;
;	Status return undefined if expression
;		      OK if integer
;	Returns at +2 if constant
;---
sb_cmpopx_ic
	moveq	#-tkc.int,d0
	add.w	(a3)+,d0		 ; next item is integer constant?
	blt.s	sci_expr		 ; no, expression
	beq.s	sci_int 		 ; ... yes, already integer
	subq.w	#tkc.fp6-tkc.int,d0	 ; FP?
	bgt.s	sci_expr		 ; ... no

	moveq	#-tkc.opf,d0
	add.w	6(a3),d0		 ; constant followed by expr?
	bge.s	sci_expr		 ; yes, it is an expression
	add.w	#tkc.opf-tkc.lpar,d0	 ; index post op?
	beq.s	sci_expr		 ; ... yes

	subq.l	#6,sp
	move.l	a7,a1
	move.w	(a3)+,(a1)
	move.l	(a3)+,2(a1)
	jsr	qa_nint 		 ; nearest int
	addq.l	#6,sp
	bne.s	sci_exer		 ; deal with error at run time
	move.w	(a1)+,d1
	addq.l	#2,(sp) 		 ; move on return
	moveq	#0,d0
	rts

sci_int
	moveq	#-tkc.opf,d0
	add.w	2(a3),d0		 ; constant followed by expr?
	bge.s	sci_expr		 ; yes, it is an expression
	add.w	#tkc.opf-tkc.lpar,d0	 ; index post op?
	beq.s	sci_expr		 ; ... yes

	move.w	(a3)+,d1		 ; integer value
	addq.l	#2,(sp) 		 ; move on return
	moveq	#0,d0
	rts

sci_exer
	subq.l	#6,a3			 ; restore pointer to fp
sci_expr
	subq.l	#2,a3			 ; restore pointer to token
	rts



;+++
; SBASIC convert expression to operation tokens
;
;	d1  s
;	d6 c  p line number / statement number
;	a0 c  u pointer to program operations
;	a3 c  u pointer to compiler tokens
;	a6 c  p pointer to SuperBASIC variables
;
;	Status return standard
;---
sb_cmpopx
	clr.l	-(sp)			 ; end of expression

	cmp.w	#tkc.lpar,(a3)		 ; can it be expression?
	blt.s	scx_erxp		 ; ... no

scx_loop
	move.w	(a3)+,d1		 ; next token
	moveq	#-tkc.lpar,d0
	add.w	d1,d0			 ; (offset)

	bgt.s	scx_item		 ; item
	blt.s	scx_ok			 ; end

	bsr.s	sb_cmpopx		 ; recursive to handle expression
	bne.s	scx_done		 ; unravel
	cmp.w	#tkc.rpar,(a3)+ 	 ; should be right par
	bne.s	scx_erxp

scx_post
	move.w	(a3)+,d1		 ; next token
	moveq	#-tkc.lpar,d0
	add.w	d1,d0			 ; (offset)
	bgt.s	scx_item		 ; item
	beq.s	scx_posti		 ; ... do post op

scx_rpar
scx_ok
	moveq	#0,d0

scx_done
	subq.w	#2,a3			 ; backspace pointer to compiler token
scx_strip
	move.l	(sp)+,d1		 ; operator off stack
	move.w	d1,(a0)+		 ; in program
	bne.s	scx_strip
	subq.l	#2,a0			 ; remove the junk
	tst.l	d0
	rts

scx_erxp2
	addq.l	#2,sp
scx_erxp
	jsr	sb_ermess
	bra.s	scx_done

scx_posti
	cmp.w	#tkc.to,(a3)		 ; is it TO index?
	bne.s	scx_postf
	cmp.l	#tkc.to<<16+tkc.rpar,(a3)+ ; whole range?
	beq.s	scx_post		 ; ... yes, forget it
	subq.l	#2,a3			 ; get expression
	bsr.l	sb_cmpopx_int		 ; recursive to handle expression
	move.l	d0,d0
	bne.s	scx_erxp
	moveq	#bo.indtv,d0
	bra.s	scx_postr

scx_postf
	bsr.l	sb_cmpopx_int
	move.l	d0,d0
	bne.s	scx_erxp		 ; ... oops
	moveq	#bo.index,d0		 ; most likely case
	cmp.w	#tkc.to,(a3)		 ; is it range?
	bne.s	scx_postr		 ; ... no
	addq.w	#2,a3
	moveq	#bo.indvt,d0		 ; another case
	cmp.w	#tkc.rpar,(a3)		 ; is it?
	beq.s	scx_postr		 ; ... yes

	bsr.l	sb_cmpopx_int
	move.l	d0,d0
	bne.s	scx_erxp		 ; ... oops
	moveq	#bo.indvtv,d0		 ; full range

scx_postr
	move.w	d0,(a0)+		 ; set token
	cmp.w	#tkc.rpar,(a3)+ 	 ; should be right par
	beq.s	scx_post
	bra.s	scx_erxp


scx_item
	move.w	scx_table(pc,d0.w),d0
	jmp	scx_table(pc,d0.w)


scx_neg 		     ; - monadic
scx_bnot		     ; ~~
scx_not 		     ; NOT
	add.w	d1,d1
	move.l	sb_optab(pc,d1.w),-(sp)  ; operation token and precedence
	bra	scx_loop

scx_plus		     ; +
scx_minu		     ; -
scx_mulf		     ; *
scx_divf		     ; /
scx_ge			     ; >=
scx_gt			     ; >
scx_apeq		     ; ==
scx_eq			     ; =
scx_ne			     ; <>
scx_le			     ; <=
scx_lt			     ; <
scx_bor 		     ; ||
scx_band		     ; &&
scx_bxor		     ; ^^
scx_pwr 		     ; ^
scx_cnct		     ; &
scx_or			     ; OR
scx_and 		     ; AND
scx_xor 		     ; XOR
scx_mod 		     ; MOD
scx_div 		     ; DIV
scx_inst		     ; INSTR

	add.w	d1,d1
	move.l	sb_optab(pc,d1.w),d1	 ; operation token and precedence
	swap	d1
scx_oploop
	cmp.w	(sp),d1 		 ; take this one from stack?
	bgt.s	scx_opdone		 ; ... no
	move.l	(sp)+,d0
	move.w	d0,(a0)+		 ; add operator to program
	bra.s	scx_oploop
scx_opdone
	swap	d1
	move.l	d1,-(sp)		 ; put this one on stack
	bra	scx_loop


opx.base  equ	  tkc.rpar

opnum	setnum	opx.base

ovec	macro	name
	dc.w	scx_[name]-scx_table

	ifnum	tkc.[name] = [opnum] goto ovecexit
	error	tkc.[name] is not token [opnum]

ovecexit  maclab
opnum	setnum	[opnum]+2
	endm


scx_table equ *-opx.base+tkc.lpar
	ovec	rpar	; )

	ovec	rfna	; (ref) function or array + name index
	ovec	refn	; (reference) name + name index

	ovec	int	; integer + 2 byte
	ovec	fp6	; floating point constant + 6 byte fp
	ovec	str	; string constant + pointer to string

	ovec	neg	; - monadic
	ovec	bnot	; ~~
	ovec	not	; NOT

	ovec	plus	; +
	ovec	minu	; -
	ovec	mulf	; *
	ovec	divf	; /
	ovec	ge	; >=
	ovec	gt	; >
	ovec	apeq	; ==
	ovec	eq	; =
	ovec	ne	; <>
	ovec	le	; <=
	ovec	lt	; <
	ovec	bor	; ||
	ovec	band	; &&
	ovec	bxor	; ^^
	ovec	pwr	; ^
	ovec	cnct	; &
	ovec	or	; OR
	ovec	and	; AND
	ovec	xor	; XOR
	ovec	mod	; MOD
	ovec	div	; DIV
	ovec	inst	; INSTR



scx_rfna		     ; (reference) function or array + name index
	jsr	sb_cmpop_fn
	beq	scx_post
	bra	scx_done

scx_refn		     ; (reference) name + name index
	move.w	#bo.pushu,(a0)+
	move.w	(a3)+,d1
	lsl.w	#nt.ishft,d1
	move.w	d1,(a0)+
	bra	scx_post

scx_int 		     ; integer constant + 2 byte int
	move.w	#bo.conin,(a0)+
	move.w	(a3)+,(a0)+
	bra	scx_post
scx_fp6 		     ; floating point constant + 6 byte fp
	move.w	#bo.confp,(a0)+
	move.w	(a3)+,(a0)+
	move.l	(a3)+,(a0)+
	bra	scx_post
scx_str 		     ; string constant + pointer to string
	move.w	#bo.const,(a0)+
	move.l	(a3)+,(a0)+
	bra	scx_post


; Arithmetic Expression Precedence Table

; precedence table

monop	equ	10
concat	equ	9
instr	equ	8
power	equ	7
muldiv	equ	6
addsub	equ	5
rel	equ	4
not	equ	3
and	equ	2
or	equ	1
xor	equ	1

; base of table

opp.base equ	 tkc.neg

opnum	setnum	opp.base

optab	macro	name,class,optok
	dc.w	[class],[optok]

	ifnum	[name] = [opnum] goto prexit
	error	[name] is not token [opnum]

prexit	maclab
opnum	setnum	[opnum]+2
	endm

;+++
; This table defines the precedence of arithmetic operators.
; The entry point is offset so that the table can be indexed directly by
; the compiler token number.
;---
sb_optab equ	*-opp.base-opp.base		     ; offset table

	optab	tkc.neg  monop	   bo.neg	     ; - monadic
	optab	tkc.bnot not	   bo.bnot	     ; ~~
	optab	tkc.not  not	   bo.not	     ; NOT

	optab	tkc.plus addsub    bo.add	     ; +
	optab	tkc.minu addsub    bo.sub	     ; -
	optab	tkc.mulf muldiv    bo.mul	     ; *
	optab	tkc.divf muldiv    bo.divf	     ; /
	optab	tkc.ge	 rel	   bo.cpge	     ; >=
	optab	tkc.gt	 rel	   bo.cpgt	     ; >
	optab	tkc.apeq rel	   bo.cpaeq	     ; ==
	optab	tkc.eq	 rel	   bo.cpeq	     ; =
	optab	tkc.ne	 rel	   bo.cpne	     ; <>
	optab	tkc.le	 rel	   bo.cple	     ; <=
	optab	tkc.lt	 rel	   bo.cplt	     ; <
	optab	tkc.bor  or	   bo.bor	     ; ||
	optab	tkc.band and	   bo.band	     ; &&
	optab	tkc.bxor xor	   bo.bxor	     ; ^^
	optab	tkc.pwr  power	   bo.power	     ; ^
	optab	tkc.cnct concat    bo.concat	     ; &
	optab	tkc.or	 or	   bo.or	     ; OR
	optab	tkc.and  and	   bo.and	     ; AND
	optab	tkc.xor  xor	   bo.xor	     ; XOR
	optab	tkc.mod  muldiv    bo.mod	     ; MOD
	optab	tkc.div  muldiv    bo.divi	     ; DIV
	optab	tkc.inst instr	   bo.instr	     ; INSTR

	end
