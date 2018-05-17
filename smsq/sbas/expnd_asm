; Expand line of BASIC		 1992 Tony Tebby

	section sbas

	xdef	sb_expnd   ; QL compatibility vector
	xdef	sb_expln
	xdef	sb_list
	xdef	sb_lista

	xref	sbp_keyt
	xref	sbp_symt
	xref	sbp_opt
	xref	sbp_mont
	xref	sbp_sept

	xref	sb_lfind
	xref	sb_rar32

	xref	gu_smul
	xref	cr_fpdec
	xref	cr_iwdec
	xref	cr_ilhex
	xref	cr_ilbin
	xref	qr_nlint

	include 'dev8_keys_sbasic'
	include 'dev8_keys_k'
	include 'dev8_smsq_sbas_parser_keys'

;+++
; List all of a SuperBASIC program
;
;	smashes lots
;---
sb_lista
	moveq	#0,d4		 ; first line
	moveq	#-1,d6		 ; last line

;+++
; List part of a SuperBASIC program
;
;	d4 c  s first line
;	d6 c  s last line
;
;	smashes lots
;---
sb_list
	st	sb_pline(a6)
	moveq	#0,d7
;+++
; Expand one or more lines of SuperBASIC printing if sb_pline is set
;
;	d4 c  s first line
;	d6 c  s last line
;	d7 c  s set if EDIT
;	a0 c  p channel ID if sb_pline is set
;---
sb_expln
;****	     clr.w   sb_length(a6)	      ; look from start  *****

;+++
; Expand one or more lines of SuperBASIC printing QL compatibility entry
;
;	d4 c  s first line
;	d6 c  s last line
;	d7 c  s set if EDIT
;	a0 c  p channel ID if sb_pline is set
;	a4 c  s pointer to program file
;---
sb_expnd
	move.l	sb_srceb(a6),a4 	 ; look from start **** for the moment
;+++
; Expand one or more lines of SuperBASIC printing if sb_pline is set
;
;	d4 c  s first line
;	d6 c  s last line
;	d7 c  s set if DLINE!! - ignore this
;	a0 c  p channel ID if sb_pline is set
;---
sb_expa4
;  TK2 ED enters here
;  For the moment we just start from the beginning, but later we may keep a
;  pointer to the line and the line length, we can then start anywhere.
;
	move.l	a0,-(sp)		 ; save channel
	jsr	sb_lfind		 ; find line
	blt.l	sbx_ok
; there are a lot of strange checks for #2 here
	bra.l	sbx_elp
sbx_loop
	move.l	a4,a1
	move.l	sb_buffb(a6),a0 	 ; fill the buffer with our nonsense
	addq.l	#2,a1			 ; first token
	move.w	(a6,a1.l),d0		 ; is it line number
	cmp.w	#tkw.lno,d0
	bne.s	sbx_xtok		 ; ... no
	addq.w	#2,a1
	jsr	cr_iwdec		 ; convert to integer
	bra.l	sbx_spc 		 ; and put in space

sbx_xtok
	moveq	#$4f,d1
	and.b	(a6,a1.l),d1		 ; masked byte token
	add.b	d1,d1			 ; doubled
	bvs.s	sbx_float		 ; floating point
	addq.l	#2,a1			 ; skip token
	move.w	sbx_tab(pc,d1.w),d1
	jmp	sbx_tab(pc,d1.w)

sbx_tab
	dc.w	sbx_spcs-sbx_tab
	dc.w	sbx_keys-sbx_tab
	dc.w	sbx_nop-sbx_tab
	dc.w	sbx_nop-sbx_tab

	dc.w	sbx_syms-sbx_tab
	dc.w	sbx_ops-sbx_tab
	dc.w	sbx_mono-sbx_tab
	dc.w	sbx_nop-sbx_tab

	dc.w	sbx_name-sbx_tab
	dc.w	sbx_nop-sbx_tab
	dc.w	sbx_nop-sbx_tab
	dc.w	sbx_strg-sbx_tab

	dc.w	sbx_text-sbx_tab
	dc.w	sbx_nop-sbx_tab
	dc.w	sbx_seps-sbx_tab
	dc.w	sbx_nop-sbx_tab

sbx_float
	move.l	a1,a4
	jsr	sb_rar32		 ; reserve RI space
	move.l	sb_arthp(a6),a1
	subq.l	#6,a1			 ; room for FP
	move.l	2(a6,a4.l),2(a6,a1.l)
	move.w	(a6,a4.l),(a6,a1.l)
	and.b	#$0f,(a6,a1.l)		 ; remove token
	cmp.b	#$e8,(a6,a4.l)		 ; dec, hex or bin
	bhi.s	sbx_dec
	addq.l	#1,a0
	move.l	a0,-(sp)		 ; save a0
	cmp.b	#$e0,(a6,a4.l)
	bhs.s	sbx_hex
sbx_bin
	move.b	#'%',-1(a6,a0.l)
	jsr	qr_nlint		 ; nearest long int
	jsr	cr_ilbin		 ; convert to binary
	moveq	#31,d0			 ; check up to 32 chars for zero
	bra.s	sbx_stripz
sbx_hex
	move.b	#'$',-1(a6,a0.l)
	jsr	qr_nlint		 ; nearest long int
	jsr	cr_ilhex		 ; convert to hex
	moveq	#7,d0			 ; check up to 8 chars for zero
sbx_stripz
	move.l	(sp),a1 		 ; start of number
	moveq	#'0',d1
sbx_lknz
	addq.l	#1,a1
	cmp.b	-1(a6,a1.l),d1		 ; skip zeros
	dbne	d0,sbx_lknz

	move.l	(sp)+,a0		 ; where to put non zeros
sbx_cpnz
	move.b	-1(a6,a1.l),(a6,a0.l)	 ; copy character
	addq.l	#1,a1
	addq.l	#1,a0
	subq.w	#1,d0
	bge.s	sbx_cpnz
	bra.s	sbx_nflt

sbx_dec
	jsr	cr_fpdec		 ; convert
sbx_nflt
	lea	6(a4),a1		 ; next token
	bra.l	sbx_ntok

sbx_name
	move.w	(a6,a1.l),d1		 ; name number
	addq.l	#2,a1
	lsl.l	#3,d1			 ; index name table
	add.l	sb_nmtbb(a6),d1
	move.l	sb_nmlsb(a6),a2
	add.w	nt_name(a6,d1.l),a2	 ; name pointer
	move.b	(a6,a2.l),d0
sbx_nmlp
	addq.l	#1,a2
	move.b	(a6,a2.l),(a6,a0.l)
	addq.l	#1,a0
	subq.b	#1,d0
	bgt.s	sbx_nmlp
	bra.l	sbx_ntok

sbx_strg
sbx_text
	move.b	d0,(a6,a0.l)		 ; delimiter
	beq.s	sbx_ctxt
	addq.l	#1,a0
sbx_ctxt
	move.w	(a6,a1.l),d1		 ; length
	addq.l	#2,a1
	bra.s	sbx_txle
sbx_txlp
	move.b	(a6,a1.l),(a6,a0.l)
	addq.l	#1,a0
	addq.l	#1,a1
sbx_txle
	dbra	d1,sbx_txlp

	move.l	a1,d1
	and.w	#1,d1
	add.w	d1,a1			 ; round up

	move.b	d0,(a6,a0.l)		 ; delimiter
	beq.s	sbx_ntok		 ; ... none
	addq.l	#1,a0
	bra.s	sbx_ntok

sbx_spcs
	move.b	#' ',(a6,a0.l)		 ; multiple spaces
	addq.l	#1,a0
	subq.b	#1,d0
	bne.s	sbx_spcs
	bra.s	sbx_ntok

sbx_keys
	lea	sbp_keyt,a4
	moveq	#' ',d1
	bra.s	sbx_cstr

sbx_syms
	lea	sbp_symt,a4
	bra.s	sbx_cstr0

sbx_ops
	lea	sbp_opt,a4
	bra.s	sbx_cstr0

sbx_mono
	lea	sbp_mont,a4
	bra.s	sbx_cstr0

sbx_seps
	lea	sbp_sept,a4

sbx_cstr0
	moveq	#0,d1
sbx_cstr
	and.w	#$ff,d0 		 ; find the item number
	add.w	d0,d0			 ; index the tables
	add.w	d0,a4
	add.w	(a4),a4 		 ; relative pointer
	move.w	(a4)+,d0		 ; number of characters

sbx_clp
	move.b	(a4)+,(a6,a0.l)
	addq.l	#1,a0
	subq.w	#1,d0
	bgt.s	sbx_clp
	beq.s	sbx_addch

sbx_nop
sbx_spc
	moveq	#' ',d1
sbx_addch
	move.b	d1,(a6,a0.l)		 ; add space
	beq.s	sbx_ntok
	addq.l	#1,a0
sbx_ntok
	move.w	(a6,a1.l),d0		 ; next token
	cmp.w	#tkw.eol,d0		 ; end of line?
	bne.l	sbx_xtok

	lea	2(a1),a4		 ; set next line
	cmp.b	#' ',-1(a6,a0.l)	 ; space at end?
	bne.s	sbx_adnl		 ; ... no
	subq.l	#1,a0			 ; ... yes, overwrite with NL
sbx_adnl
	move.b	#k.nl,(a6,a0.l) 	 ; NL beyond end!

	move.l	a0,sb_buffp(a6) 	 ; set buffer pointer
	tst.b	sb_pline(a6)		 ; printing?
	beq.s	sbx_elp 		 ; ... no

	move.l	a0,d2
	move.l	sb_buffb(a6),a1
	sub.l	a1,d2
	addq.w	#1,d2			 ; include nl
	add.l	a6,a1
	move.l	(sp),a0
	jsr	gu_smul 		 ; write characters
	bne.s	sbx_exit

sbx_elp
	cmp.l	sb_srcep(a6),a4 	 ; end of program?
	bhs.s	sbx_ok
	cmp.w	4(a6,a4.l),d6		 ; end of list range?
	bhs	sbx_loop

sbx_ok
	moveq	#0,d0
sbx_exit
	move.l	(sp)+,a0
	rts
	end
