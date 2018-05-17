; SBAS_PARSE - SuperBASIC Parser	       1992 Tony Tebby

	section sbas

	xdef	sb_palin     ; parse a line

	xdef	sb_paini     ; init
	xdef	sb_parse     ; parse
	xdef	sb_strip     ; strip spare spaces
	xdef	sb_paerr     ; error!

	xdef	sb_parcl

	xref	sb_graph     ; syntax graphs
	xref	sb_pastr     ; parse string item
	xref	sb_pakey     ; parse keywords
	xref	sb_panky     ; parse non-keywords
	xref	sb_lnam2     ; locate name
	xref	sb_gnam2     ; global name
	xref	sb_anam2     ; add name

	xref	sbp_nchr
	xref	sbp_intb

	xref	sb_rescl     ; reserve command line
	xref	sb_rbk20     ; reserve backtrack stack
	xref	sb_rgr04     ; reserve graph stack
	xref	sb_rar32     ; resrve RI stack

	xref	cr_deciw
	xref	cr_decfp
	xref	cr_hexil
	xref	cr_binil
	xref	qa_lfloat

	include 'dev8_keys_sbasic'
	include 'dev8_keys_err'
	include 'dev8_keys_err4'
	include 'dev8_smsq_sbas_parser_keys'
	include 'dev8_keys_k'
	include 'dev8_mac_assert'

;+++
; Control routine for parsing
;---
sb_palin
	bsr.s	sb_paini	 ; initialise
	lea	sb_graph,a2
	bsr.l	sb_parsln	 ; parse
	beq.s	sb_strip	 ; strip spaces
	rts

;+++
; Initialise parser data area
;---
sb_paini
;sbtrns is only required if sb vars are above data areas
;sbtrns     tas     sb_grphb(a6)	       ; Turbo - this unturbos the graph stack
	move.l	sb_backb(a6),sb_backp(a6)  ; clear backtrack stack
	move.l	sb_grphb(a6),sb_grphp(a6)  ; clear temporary graph stack
	move.l	sb_cmdlb(a6),sb_cmdlp(a6)  ; clear command line
	rts

;+++
; Strip spare spaces
;
;	a0/a2	scratch
;	status arbitrary
;
;---
sb_strip
	move.l	sb_cmdlb(a6),a0
	move.l	sb_cmdlp(a6),a2
	cmp.l	a2,a0			 ; any tokens at all?
	bge.s	sbst_exit		 ; ... no
	cmp.b	#tkb.spce,(a6,a0.l)	 ; starts with spaces?
	bne.s	sbst_cke		 ; ... no
	subq.l	#2,a2			 ; ... yes, strip them
sbst_clp
	move.w	2(a6,a0.l),(a6,a0.l)
	addq.l	#2,a0
	cmp.l	a2,a0
	blt.s	sbst_clp

sbst_cke
; no check at end, 'cos parser has already stripped them
sbst_sp
	move.l	a2,sb_cmdlp(a6) 	 ; reset end pointer
sbst_exit
	moveq	#0,d0
	rts

;+++
; Parser error processing: creates MISTake line
; d1/d2/d3/d4/d5/a0/a1/a2/a3 (at least) smashed
; returns 0	    (MISTake line successfully created)
;	  err.isyn  (too garbled)
;---
sb_paerr
	moveq	#8,d1			 ; enough room for odd tokens
	add.l	sb_buffp(a6),d1
	sub.l	sb_buffb(a6),d1 	 ; and the line
	bsr.l	sbp_rscl

	move.l	sb_cmdlb(a6),a1 	 ; command line
	cmp.w	#tkw.lno,(a6,a1.l)	 ; line number?
	bne.s	sbpe_err		 ; ... yes

	addq.l	#4,a1
	move.l	sb_buffb(a6),a0 	 ; use cr_deciw to set / skip line no
	jsr	cr_deciw
	move.l	#tkw.mist<<16+tkw.text,2(a6,a1.l) ; add MISTAKE, TEXT
	addq.l	#8,a1			 ; skip to where text will be
	cmp.b	#' ',(a6,a0.l)		 ; starts with space?
	bne.s	sbpe_sln
	addq.l	#1,a0			 ; ... yes, skip it
sbpe_sln
	move.l	sb_buffp(a6),d0
	sub.l	a0,d0			 ; length of text (including newline)
	subq.w	#1,d0
	move.w	d0,-2(a6,a1.l)		 ; set length
	bra.s	sbpe_cte

sbpe_ctxt
	move.b	(a6,a0.l),(a6,a1.l)	 ; copy text
	addq.l	#1,a0
	addq.l	#1,a1
sbpe_cte
	dbra	d0,sbpe_ctxt

	move.l	a1,d0
	and.w	#1,d0
	add.w	d0,a1			 ; round up

	move.w	#tkw.eol,(a6,a1.l)
	addq.l	#2,a1
	move.l	a1,sb_cmdlp(a6) 	 ; set token list end
	moveq	#0,d0
	rts

sbpe_err
	moveq	#err.isyn,d0
	rts
	page

;+++
; Parser
;
;	d0  r	error code
;	d1-d3	scratch
;	a0  r	pointer to furthest along buffer
;	a1	scratch
;	a2 c  s pointer to parser graphs
;	a3	scratch
;	a4	scratch
;
;	status return	0	 OK
;			err.isyn syntax error
;			-ve	 specific errors
;
;---
sb_parse
	clr.w	sb_line(a6)		 ; do not add line number
sb_parsln
	clr.l	sb_pcern(a6)		 ; no error yet
	clr.l	sb_pcerp(a6)

	jsr	sb_rar32		 ; ensure room for FP conversion
	move.l	sb_buffb(a6),a0 	 ; pointer to line to parse
	move.l	sb_cmdlb(a6),a4 	 ; pointer to where to put it all
	clr.l	(a6,a4.l)		 ; blat it

	sf	sb_cmdst(a6)		 ; no command line statement nr now

	jsr	sb_rgr04		 ; reserve null graph entry
	move.l	sb_grphb(a6),a1
	sub.w	#sgr.len,a1
	clr.l	sgr_btrk(a6,a1.l)
	move.l	a1,sb_grphp(a6)

	moveq	#4,d1			 ; reserve enough for line number
	bsr.l	sbp_rscl

	clr.l	d7
	subq.l	#1,a0
	moveq	#' ',d0
sbp_stsp
	addq.l	#1,a0
	cmp.b	(a6,a0.l),d0		 ; skip space
	beq.s	sbp_stsp

	move.l	a0,a1
	moveq	#k.nl,d0
sbp_lnl
	addq.l	#1,a1
	cmp.b	-1(a6,a1.l),d0		 ; look for NL at end
	bne.s	sbp_lnl

	move.l	#' ',d0
sbp_espc
	subq.l	#1,a1
	cmp.b	-1(a6,a1.l),d0		 ; skip end spaces
	beq.s	sbp_espc
	addq.l	#1,a1
	move.b	#k.nl,-1(a6,a1.l)	 ; genuine end
	move.l	a1,sb_buffp(a6) 	 ; truly stripped

sbp_lnum
	move.l	a0,d2			 ; buffer pointer
	lea	4(a4),a1		 ; where to put line number
	jsr	cr_deciw		 ; convert
	beq.s	sbp_line		 ; ... there is a line number
	cmp.l	a0,d2			 ; error in number or no number?
	bne.l	sbp_elno		 ; ... error

	move.w	sb_line(a6),d1		 ; add line number?
	beq.s	sbp_stloop		 ; ... no
	move.w	d1,2(a6,a4.l)

sbp_line
	move.w	d1,sb_line(a6)		 ; 1-32767
	ble.l	sbp_elno		 ; ... no, oops
	move.w	#tkw.lno,(a6,a4.l)	 ; put token in
	addq.l	#4,a4
	cmp.b	#' ',(a6,a0.l)		 ; space after line number
	bne.s	sbp_stloop
	addq.l	#1,a0

sbp_stloop
	moveq	#0,d2			 ; no item decoded yet
sbp_loop
	tst.l	d2			 ; item already decoded?
	bne.l	sbp_ckit		 ; ... yes, just check it

sbp_stlp
	moveq	#8,d1			 ; reserve enough fixed length tokens
	bsr.l	sbp_rscl

; first check if it is spaces

sbp_cksp
	moveq	#0,d1
	move.b	(a6,a0.l),d1		 ; start of next item

	cmp.b	#' ',d1 		 ; is it spaces?
	bne.s	sbp_look

	moveq	#$7d,d0
sbp_cntsp
	addq.l	#1,a0
	cmp.b	#' ',(a6,a0.l)		 ; another space?
	dbne	d0,sbp_cntsp		 ; ... yes

	not.w	d0			 ; 1 space = ff82, $7f spaces = 0
	add.w	#tkb.spce<<8+$7f,d0	 ; space token
	move.w	d0,(a6,a4.l)		 ; set spaces token
	addq.w	#2,a4
	addq.b	#1,d0
	bmi.s	sbp_cksp		 ; could be more spaces to come

	move.b	(a6,a0.l),d1		 ; start of next item

sbp_look

; check if this is a special bit of code

sbp_scod
	assert	sbg.call,sbg.code,0
	tst.w	(a2)			 ; special code
	bgt.s	sbp_cknm		 ; ... standard code
	blt.l	sbp_exgr		 ; ... an exit

	moveq	#0,d0
	bsr	sbp_sbtr

	lea	sbg_call(a2),a1
	add.w	(a1),a1
	jsr	(a1)			 ; ... call it
	beq.l	sbp_ckit		 ; set item
	blt.l	sbp_error		 ; error found
	addq.l	#sbg.gent,a2		 ; next
	bra.s	sbp_scod		 ; try code again

; now we will see whether this is a keyword or name

sbp_cknm
	lea	sbp_nchr,a3		 ; table of name characters
	tst.b	(a3,d1.w)		 ; character type
	ble.s	sbp_cknk		 ; not a letter, try non-keyword item

	move.l	a0,a1			 ; save start of keyword / name
sbp_cknl
	addq.l	#1,a0
	move.b	(a6,a0.l),d1
	tst.b	(a3,d1.w)		 ; another keyword / name character
	bne.s	sbp_cknl

	assert	k.dollar,k.percnt-1
	sub.b	#k.dollar,d1
	blo.s	sbp_stnl		 ; not a dollar
	subq.b	#k.percnt-k.dollar,d1
	bhi.s	sbp_stnl
	addq.l	#1,a0			 ; include dollar or percent
sbp_stnl
	move.l	a0,d2
	sub.l	a1,d2			 ; a1 points to chars / d2 is length

	jsr	sb_pakey		 ; check keys  (changes A3)
	beq.l	sbp_ckit		 ; ok, done, check item permitted

sbp_lname
	jsr	sb_lnam2		 ; find name given a1,d2
	beq.s	sbp_name
	jsr	sb_gnam2		 ; try global name given a1,d2
	beq.s	sbp_name
	jsr	sb_anam2		 ; add name given a1,d2
sbp_name
	move.w	#tkw.name,(a6,a4.l)
	move.l	a3,d0
	sub.l	sb_nmtbb(a6),d0 	 ; index name table
	lsr.l	#3,d0
	move.w	d0,2(a6,a4.l)
	addq.l	#4,a4
	moveq	#tki.name,d2		 ; name item type
	bra.l	sbp_ckit

; next check non-keyword items

sbp_cknk
	jsr	sb_panky
	beq.l	sbp_ckit

; we are running out of possibilities - try string

	moveq	#tki.quot,d2
	cmp.b	#tks.quot,d1		 ; this type of string?
	beq.s	sbp_str 		 ; ... yes
	cmp.b	#tks.apst,d1		 ; or this?
	bne.s	sbp_ckval		 ; ... no
	moveq	#tki.apst,d2
sbp_str
	add.w	#tkb.strg<<8,d1
	jsr	sb_pastr		 ; get string
	beq.s	sbp_ckit
	bra.l	sbp_error

; Floating point is all that there is left

sbp_ckval
	move.l	sb_arthp(a6),a1
	cmp.b	#'$',d1 		 ; hex?
	beq.s	sbp_hex
	cmp.b	#'%',d1 		 ; or binary?
	beq.s	sbp_bin
	jsr	cr_decfp		 ; check fp
	bne.l	sbp_urec
	move.w	#$f000,d2
	bra.s	sbp_setval

sbp_hex
	addq.l	#1,a0			 ; skip $
	move.l	a0,d3
	jsr	cr_hexil		 ; convert from hex
	move.w	#$e000,d2
	bra.s	sbp_float

sbp_bin
	addq.l	#1,a0			 ; skip %
	move.l	a0,d3
	jsr	cr_binil		 ; convert from binary
	move.w	#$d000,d2

sbp_float
	cmp.l	d3,a0			 ; anything converted?
	beq.l	sbp_urec		 ; ... no
	add.l	a6,a1
	jsr	qa_lfloat		 ; float long int
	sub.l	a6,a1

sbp_setval
	or.w	(a6,a1.l),d2
	move.w	d2,(a6,a4.l)		 ; copy it with FP flag
	move.l	2(a6,a1.l),2(a6,a4.l)
	addq.l	#6,a4
	moveq	#tki.val,d2
	bra.s	sbp_ckit

sbp_goto
	addq.l	#sbg_next,a2		 ; next item
	add.w	(a2),a2
	bra.s	sbp_rtry

sbp_nopt
	addq.l	#sbg.gent,a2
sbp_ckit
sbp_rtry
	nop
	move.b	(a2),d1 		 ; call, option or exit?
	blt.s	sbp_exgr		 ; ... exit
	beq.l	sbp_sgrp		 ; ... sub graph
	move.w	sbg_mini(a2),d0 	 ; range
	beq.s	sbp_goto		 ; ... none, goto
	cmp.b	d0,d2			 ; in max range
	bhi.s	sbp_nopt		 ; ... no
	lsr.w	#8,d0
	cmp.b	d0,d2			 ; in min range?
	blo.s	sbp_nopt		 ; ... no

	swap	d2
	clr.w	d2			 ; no substitute
	move.b	sbg_subi(a2),d0 	 : (BYTE in WORD)
	add.b	d0,d0			 ; substitute?
	beq.s	sbp_ckbt		 ; ... no

	move.w	-2(a6,a4.l),d2		 ; keep old tokens
	lea	sbp_intb,a1
	move.w	(a1,d0.w),-2(a6,a4.l)	 ; substitute new token

sbp_ckbt
	subq.b	#sbg.nret,d1		 ; was it nret?
	bne.s	sbp_stbt

	move.l	sb_grphp(a6),a1
	move.l	sgr_btrk(a6,a1.l),a1
	add.l	sb_backb(a6),a1 	 ; clear backtrack stack for this level
	move.l	a1,sb_backp(a6) 	   ; reset backtrack
	bra.s	sbp_next

; save item position in buffer, graph and token list in back track stack

sbp_stbt
	move.l	d2,d0
sbp_stb0
	bsr	sbp_sbtr
sbp_next
	moveq	#0,d2			 ; next item not decoded
sbp_nxti
	addq.l	#sbg_next,a2		 ; next item
	add.w	(a2),a2
	tst.b	(a2)
	bpl.l	sbp_loop		 ; ... not exit

sbp_exgr
	cmp.b	#sbg.err,(a2)		 ; is it error?
	beq.s	sbp_sete		 ; ... yes, set it

	move.l	sb_grphp(a6),a1
	addq.l	#sgr.len,a1
	move.l	a1,sb_grphp(a6)
	cmp.l	sb_grphb(a6),a1 	 ; end of outermost graph?
	bhs.l	sbp_done		 ; ... yes

	move.l	-sgr.len+sgr_btrk(a6,a1.l),a1 ; backtrack stack pointer
	add.l	sb_backb(a6),a1
	move.l	sbk_grph(a6,a1.l),a2	 ; just get graph
	add.w	#sbk.len,a1
	move.l	a1,sb_backp(a6) 	   ; reset backtrack
	bra.s	sbp_nxti		 ; goto next


sbp_sgrp		    ; sub graphs and code ends up here
	moveq	#0,d0
	move.b	sbg_flag(a2),d0
	beq.s	sbp_stb0		 ; was code, done it
	move.b	d2,d0
	swap	d0
	bsr.l	sbp_sbtr		 ; save status for backtrack
	bsr.l	sbp_rstg		 ; reserve room on t graph (sets a1)
	move.l	sb_backp(a6),d0 	 ; stack backtrack stack pointer
	sub.l	sb_backb(a6),d0
	move.l	d0,(a6,a1.l)
	addq.l	#sbg_call,a2		 ; call graph
	add.w	(a2),a2
	bra	sbp_rtry

sbp_sete
	move.b	sbg_err(a2),d0		 ; set error code
	ext.w	d0
	ext.l	d0
	bra.s	sbp_error

sbp_urec
	moveq	#0,d0			 ; do not understand this error

sbp_error
	cmp.l	sb_pcerp(a6),a0 	 ; this far along line
	blo.s	sbp_back		 ; ... we've already been further
	bhi.s	sbp_serr
	tst.l	sb_pcern(a6)		 ; error already identified?
	bne.s	sbp_back		 ; ... yes, keep current code
sbp_serr
	move.l	d0,sb_pcern(a6) 	 ; this far
	move.l	a0,sb_pcerp(a6)
sbp_back
	move.l	sb_backp(a6),a1
	cmp.l	sb_backb(a6),a1
	bge.s	sbp_gvup		 ; cannot backtrack further, give up
	movem.l (a6,a1.l),d2/a0/a2/a3/a4 ; backtrack
	add.l	sb_buffb(a6),a0
	add.l	sb_grphb(a6),a3
	add.l	sb_cmdlb(a6),a4
	move.l	a3,sb_grphp(a6) 	 ; including temporary graph
	add.w	#sbk.len,a1
	move.l	a1,sb_backp(a6)

	tst.w	d2			 ; any saved token?
	beq.s	sbp_bkit		 ; ... no
	move.w	d2,(a6,a4.l)		 ; restore token

sbp_bkit
	swap	d2
	bne	sbp_nopt		 ; retry for another option

	addq.l	#sbg.gent,a2		 ; was code, move on and ...
	bra	sbp_look		 ; ... look at item again

sbp_gvup
	move.l	sb_pcern(a6),d0 	 ; error number
	bne.s	sbp_emes
	moveq	#err.isyn,d0
	bra.s	sbp_exit
sbp_elno
	move.l	a0,sb_pcerp(a6) 	 ; set error position
	moveq	#sbe.lno,d0
sbp_emes
	add.w	#err4.null,d0		 ; standard error message
sbp_exit
	tst.l	d0
sbp_rts
	rts

; because of a nasty in SuperBASIC, we need to strip out spaces after keywords!

sbp_done
	move.l	sb_cmdlb(a6),a4 	 ; starting here
	add.l	a6,a4			 ; IMMOVEABLE
	move.l	a4,a2

sqp_ntok
	moveq	#$4f,d1
	and.b	(a2),d1 		 ; masked byte token

	move.w	(a2)+,d0
	move.w	d0,(a4)+		 ; copy token

	add.b	d1,d1			 ; byte doubled
	bvs.s	sqp_float		 ; floating point
	move.w	sqp_tab(pc,d1.w),d1
	jmp	sqp_tab(pc,d1.w)

sqp_tab
	dc.w	sqp_ntok-sqp_tab	 ; spaces
	dc.w	sqp_key-sqp_tab 	 ; keys
	dc.w	sqp_ntok-sqp_tab
	dc.w	sqp_ntok-sqp_tab

	dc.w	sqp_sym-sqp_tab 	 ; symbols
	dc.w	sqp_ntok-sqp_tab	 ; ops
	dc.w	sqp_ntok-sqp_tab	 ; monops
	dc.w	sqp_ntok-sqp_tab

	dc.w	sqp_name-sqp_tab	 ; name
	dc.w	sqp_ntok-sqp_tab
	dc.w	sqp_ntok-sqp_tab
	dc.w	sqp_strg-sqp_tab	 ; string

	dc.w	sqp_text-sqp_tab	 ; text
	dc.w	sqp_lno-sqp_tab 	 ; line number
	dc.w	sqp_ntok-sqp_tab	 ; separator
	dc.w	sqp_ntok-sqp_tab

sqp_float
	move.l	(a2)+,(a4)+		 ; copy the rest
	bra	sqp_ntok

sqp_key
	cmp.b	#tkb.spce,(a2)		 ; spaces?
	bne.s	sqp_ntok		 ; no, very unusual, but ok
	move.w	(a2)+,d0		 ; space token
	subq.b	#1,d0			 ; one fewer spaces
	beq.s	sqp_ntok		 ; ... none left
	move.w	d0,(a4)+
	bra.s	sqp_ntok

sqp_name
	move.w	(a2)+,(a4)+		 ; name number
	bra	sqp_ntok

sqp_strg
sqp_text
sqp_ctxt
	move.w	(a2)+,d1		 ; length
	move.w	d1,(a4)+
	beq	sqp_ntok
sqp_txlp
	move.w	(a2)+,(a4)+
	subq.w	#2,d1
	bgt.s	sqp_txlp

	bra	sqp_ntok

sqp_lno
	move.w	(a2)+,(a4)+
	bra	sqp_ntok

sqp_sym
	cmp.w	#tkw.eol,d0		 ; end of line?
	bne.s	sqp_ntok		 ; ... no

	sub.l	a6,a4
	move.l	a4,sb_cmdlp(a6) 	 ; set end pointer
	moveq	#0,d0
	rts

sbp_sbtr
	move.l	sb_backp(a6),a1 	 ; save backtrack info
	sub.w	#sbk.len,a1
	cmp.l	sb_backl(a6),a1 	 ; enough room?
	blt.s	sbp_rstr
	movem.l a0/a4,-(sp)
	move.l	sb_grphp(a6),a3
	sub.l	sb_buffb(a6),a0
	sub.l	sb_grphb(a6),a3
	sub.l	sb_cmdlb(a6),a4
	movem.l d0/a0/a2/a3/a4,(a6,a1.l)
	move.l	a1,sb_backp(a6)
	movem.l (sp)+,a0/a4
	rts

sbp_rstr
	movem.l d0/d2,-(sp)
	jsr	sb_rbk20		 ; reserve backtrack
	movem.l (sp)+,d0/d2
	bra.s	sbp_sbtr		 ; and try again


sbp_rstg
	move.l	sb_grphp(a6),a1
	cmp.l	sb_grphl(a6),a1
	bgt.s	sbp_stg
	move.l	d2,-(sp)
	jsr	sb_rgr04		 ; reserve graph stack
	move.l	(sp)+,d2
	move.l	sb_grphp(a6),a1
sbp_stg
	subq.l	#sgr.len,a1		 ; set new graph stack pointer
	move.l	a1,sb_grphp(a6)
	rts

;+++
; reserve (parsed) command line for parser
; adjusts a0 (text) and a4 (tokens) if moved
;---
sb_parcl
sbp_rscl
	move.l	sb_cmdlt(a6),d0 	 ; top of area
	sub.l	a4,d0			 ; amount spare
	cmp.l	d0,d1			 ; enough?
	ble.s	sbp_rrts		 ; ... yes
	move.l	a4,sb_cmdlp(a6) 	 ; set command line running pointer
	sub.l	sb_buffb(a6),a0
	move.l	d2,-(sp)
	jsr	sb_rescl		 ; before reserving space
	move.l	(sp)+,d2
	add.l	sb_buffb(a6),a0 	 ; line may move
	move.l	sb_cmdlp(a6),a4 	 ; reset command line running pointer
sbp_rrts
	rts
	end
