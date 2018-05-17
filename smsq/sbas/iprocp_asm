; SBAS_IPROCP - PROC/FN (and Arrays) Parameters    1994 Tony Tebby

	section sbas

	xdef	bo_stpar	    ; parameters
	xdef	bo_fppar
	xdef	bo_inpar
	xdef	bo_nmpar
	xdef	bo_nfpar
	xdef	bo_arpar
	 xref	bo_cfpar	    ;**** in sbas_iexpr
	 xref	bo_sfpar	    ;**** in sbas_iexpr
	 xref	bo_sfparc	    ;**** in sbas_iexpr
	xdef	bo_cfapar
	xdef	bo_sfapar
	xdef	bo_sfaparc
	xdef	bo_nlpar
	xdef	bo_expar

	xdef	bo_stpin	    ; parameters or indices
	xdef	bo_fppin
	xdef	bo_inpin
	xdef	bo_nmpin
	xdef	bo_nfpin
	xdef	bo_arpin
	 xref	bo_cfpin	    ;**** in sbas_iexpr
	 xref	bo_sfpin	    ;**** in sbas_iexpr
	 xref	bo_sfpinc	    ;**** in sbas_iexpr
	xdef	bo_cfapin
	xdef	bo_sfapin
	xdef	bo_sfapinc
	xdef	bo_nlpin
	xdef	bo_expin

	xref	bo_const
	xref	bo_confp
	xref	bo_conin
	xref	bo_pushu
	xref	bo_pusha
	xref	bo_pushcf
	xref	bo_pushsf
	xref	bo_pushsfc
	xref	bo_pushcfa
	xref	bo_pushsfa
	xref	bo_pushsfac

	xref	bo_arroff
	xref	bo_arrsoff

	xref	sb_isubas

	xref	sb_iloop5
	xref	sb_iloop
	xref	sb_istop
	xref	sb_ierset
	xref	sb_ienimp

	xref	sb_fint

	xref	sb_rnt08
	xref	sb_aldat
	xref	sb_aldat8
	xref	sb_redat

	xref	qa_lfloat

	include 'dev8_keys_sbasic'
	include 'dev8_keys_err'
	include 'dev8_keys_err4'
	include 'dev8_smsq_sbas_inter_keys'
	include 'dev8_mac_assert'

;+++
; Within this routine
;
;	D6 is limit of arithmetic stack (with some bytes spare)
;	A1 is pointer to arithmetic stack
;	A2 is entry address
;	A3 is pointer to name table
;	A4 is pointer to program
;	A5 address of next token loop
;	A6 is pointer to system variables
;---

;--------------------------------- check context
; If the context is a function, the return is standard+2.
; If the context is an array ref, the seperator may be skipped, and the word
; after the call is added to the return address.

bpin_cont2
	move.l	sb_retsp(a6),a2
	tst.b	rt_type(a6,a2.l)	 ; type of top frame
	bgt.s	bpin_c2do		 ; proc/fun
	add.w	#rt.pfsize-rt.sasize,a2  ; array
bpin_c2do
	tst.b	rt_type-rt.pfsize(a6,a2.l) ; +ve for parameter, -ve for index
	move.l	(sp)+,a2		 ; return address
	bmi.s	bpin_index		 ; ... it is index
	jmp	2(a2)

bpin_contnsep
	move.l	sb_retsp(a6),a2
	tst.b	rt_type(a6,a2.l)	 ; +ve for parameter, -ve for index
	move.l	(sp)+,a2		 ; return address
	bmi.s	bpin_index		 ; ... it is index
bpin_jump
	jmp	2(a2)
	rts

bpin_contsep
	move.l	sb_retsp(a6),a2
	tst.b	rt_type(a6,a2.l)	 ; +ve for parameter, -ve for index
	move.l	(sp)+,a2		 ; return address
	bpl.s	bpin_jump		 ; ... it is parameter
	addq.l	#2,a4			 ; skip separator

bpin_index
	add.w	(a2),a2 		 ; add the offset
	jmp	(a2)

;--------------------------------- constant parameters
bo_stpin
	bsr	bpin_contsep		 ; check top frame
	dc.w	bo_const-*
bo_stpar
	move.w	(a4)+,-(sp)		 ; separator
	move.l	a1,d3			 ; save a1
	move.l	(a4)+,a1		 ; pointer to string
	bra.l	bxp_sstr


bo_fppin
	bsr	bpin_contsep		 ; check top frame
	dc.w	bo_confp-*
bo_fppar
	move.w	(a4)+,-(sp)		 ; separator
	move.w	(a4)+,d2		 ; float
	move.l	(a4)+,d3
	bra.l	bxp_sfp

bo_inpin
	bsr	bpin_contsep		 ; check top frame
	dc.w	bo_conin-*
bo_inpar
	move.w	(a4)+,-(sp)		 ; separator
	move.w	(a4)+,d2		 ; integer
	bra.l	bxp_sin

;---------------------------------- name parameters
bo_nfpin
	bsr	bpin_contnsep		 ; check top frame
	dc.w	bo_pushu-*
bo_nfpar
	move.w	(a4)+,d3		 ; name table index
	addq.l	#2,a4			 ; skip bo.expar
	move.w	(a4)+,-(sp)
	move.b	nt_nvalp(a3,d3.w),d0	 ; usage
	move.w	#1<<nt.unset+1<<nt.var+1<<nt.for+1<<nt.rep,d1
	btst	d0,d1			 ; variable?
	bne.s	bonm_do 		 ; ... yes
	subq.b	#nt.arr,d0		 ; array
	beq.s	bonm_array
	blt.s	bon_ipar

	subq.b	#nt.sbfun-nt.arr,d0	 ; SuperBASIC function?
	beq.s	bon_fun
	subq.b	#nt.mcfun-nt.sbfun,d0	 ; MC function?
	bne.s	bon_ipar
bon_fun
	subq.l	#6,a4			 ; restore table index and bo.expar
	addq.l	#2,sp
	lea	bo_pushu,a2		 ; function
	jmp	(a2)

bon_ipar
	moveq	#err.ipar,d0
	jmp	sb_istop

bonm_array
	move.l	nt_value(a3,d3.w),a0	 ; pointer to value
	move.l	a0,a2			 ; save base
	moveq	#4,d1
	move.w	4(a0),d7		 ; 1/4 space required
	beq.s	bonm_do
	add.w	d7,d1
	lsr.w	#1,d1			 ; rounded
	lsl.w	#3,d1			 ; to multiple of 8
	move.l	a0,d5			 ; save descriptor
	jsr	sb_aldat
	move.l	d1,(a0)+		 ; length in heap
	move.l	a0,d2			 ; save base of descriptor
	move.l	d5,a2
	move.w	(a2)+,(a0)+		 ; two bytes extra
bona_copy
	move.l	(a2)+,(a0)+		 ; an index
	subq.w	#1,d7
	bge.s	bona_copy

	move.l	d2,a0			 ; pointer to value
	bra.s	bonm_type

;------------------------------------
bo_nmpin
	bsr	bpin_contsep		 ; check top frame
	dc.w	bo_pushu-*
bo_nmpar
	move.w	(a4)+,-(sp)		 ; separator
	move.w	(a4)+,d3		 ; name table index
bonm_do
	move.l	nt_value(a3,d3.w),a0	 ; pointer to value
bonm_type
	move.w	#$0f0f,d1
	and.w	nt_usetp(a3,d3.w),d1	 ; and type
	lsr.w	#nt.ishft,d3
	bra.l	bxp_table


;------------------------------------------ array parameters
bo_arpar2
	move.w	(a4),d3 		 ; name index
	move.l	(a4)+,d4		 ; number of indices
	tst.w	d4
	bmi.l	bap_iind		 ; bad list
	move.w	(a4)+,d5		 ; number of values
	move.l	(a4)+,d2		 ; type of indices
	addq.l	#2,a4			 ; skip bo.expar
	move.w	(a4)+,-(sp)		 ; get separator
	subq.l	#rt.sasize,a2
	move.l	a2,sb_retsp(a6) 	 ; and remove return stack entry
	bra.s	bap_do

;--------------------------------------------
bo_arpin
	bsr	bpin_contsep		 ; check top frame
	dc.w	bo_pusha-*
bo_arpar			 ; array parameter
	move.w	(a4)+,-(sp)		 ; get separator
	move.w	(a4),d3 		 ; name index
	move.l	(a4)+,d4		 ; name index + number of indices
	move.w	(a4)+,d5		 ; number of values
	move.l	(a4)+,d2		 ; type of indices
bap_do
	tst.w	d5			 ; any	values
	beq.s	bap_type

	move.l	a6,a2			 ; use buffer to convert
	add.l	sb_cmdlb(a6),a2

bap_cmloop
	cmp.w	#ar.int,(a1)+		 ; is it an integer already?
	beq.s	bap_move		 ; ... yes
	jsr	sb_fint
bap_move
	move.w	(a1)+,-(a2)		 ; move
	subq.w	#1,d5
	bgt.s	bap_cmloop

bap_type
	move.l	nt_value(a3,d3.w),a0	 ; value
	move.b	nt_nvalp(a3,d3.w),d0	 ; usage
	moveq	#$f,d1
	and.b	nt_vtype(a3,d3.w),d1	 ; and type

	subq.b	#nt.arr,d0		 ; array?
	bne.s	bap_var 		 ; ... no

	move.l	(a0)+,d5		 ; base of data
	moveq	#0,d3			 ; and offset

	cmp.w	(a0)+,d4		 ; the right number of indices?
	bne.s	bap_cksuba		 ; ... no
	tst.l	d2			 ; index types
	bne.s	bap_suba		 ; ranges

	cmp.b	#nt.st,d1
	ble.s	bap_suba		 ; (sub) string array

	jsr	bo_arroff		 ; calculate offset

	move.l	d5,a0			 ; base in address reg
	subq.w	#nt.fp,d1		 ; was it FP?
	bne.s	bapi_set		 ; ... no, int

	move.l	d3,d0
	add.l	d3,d3
	add.l	d0,d3			 ; 3*offset
bapi_set
	add.l	d3,d3			 ; 2*offset
bap_setav
	add.l	d3,a0			 ; genuine value
	add.w	#nt.varfp,d1		 ; and type
bap_table
	swap	d4			 ; and name
	move.w	d4,d3
	lsr.w	#nt.ishft,d3		 ; name table index
	bra.l	bxp_table

bap_cksuba
	blt.s	bap_suba		 ; fewer indices
bap_iind
	moveq	#ern4.iind,d0
	jmp	sb_ierset

bap_suba
	lea	-6(a0),a5		 ; backspace descriptor pointer
	move.w	d1,d3			 ; array type
	jsr	sb_isubas		 ; process sub array
	lea	sb_iloop5,a5		 ; restore a5
	bne.s	bap_subat		 ; some dimensions remain

	move.l	(a0),d5 		 ; pointer to data
	move.l	-(a0),d1
	jsr	sb_redat		 ; return descriptor
	move.l	d5,a0			 ; pointer
	move.w	d3,d1
	add.w	#nt.var<<8,d1		 ; type
	bra	bap_table

bap_subat
	move.w	d3,d1
	add.w	#nt.arr<<8,d1		 ; type
	bra	bap_table


bap_narr
	moveq	#ern4.narr,d0
	jmp	sb_ierset

bap_inor
	moveq	#ern4.inor,d0
	jmp	sb_ierset

bap_var
	addq.b	#nt.arr-nt.var,d0	 ; variable?
	bne.s	bap_narr		 ; ... no
	subq.b	#nt.st,d1		 ; must be string
	bne.s	bap_narr		 ; ... no

	subq.w	#1,d4			 ; one index only please
	bne.s	bap_iind		 ; ... oops

	move.w	(a2),d3 		 ; is it index zero?
	beq.s	bap_iind		 ; ... yes

	move.w	(a0)+,d4		 ; limit
	tst.l	d2			 ; range?
	bne.s	bapv_from		 ; ... yes
	cmp.w	d4,d3
	bhi.s	bap_inor		 ; out of range
	add.w	d3,a0			 ; point to character
	move.l	a1,d3			 ; and save arith stack pointer
	subq.l	#1,a1
	move.b	-1(a0),-(a1)		 ; single character
	moveq	#1,d2			 ; one byte string
	bra.l	bxp_ssstr		 ; set character

bapv_from
	moveq	#1,d3			 ; assume from 1
	lsr.w	#2,d2			 ; get from bit in carry
	bcc.s	bapv_to 		 ; default
	move.w	(a2)+,d3		 ; real start
	ble.s	bap_inor		 ; ... oops
bapv_to
	lsr.w	#1,d2
	bcc.s	bapv_defto		 ; default to
	move.w	(a2)+,d0		 ; real to
	cmp.w	d4,d0			 ; in range?
	bhi.s	bapv_defto		 ; ... no, default
	move.w	d0,d4			 ; ... yes, use it
bapv_defto
	addq.w	#1,d4
	move.w	d4,d2			 ; end
	sub.w	d3,d2			 ; less start - 1 = length of substring
	blt.l	bap_iind		 ; ... less than none

	move.l	a1,d3			 ; saved arith stack
	add.w	d4,a0			 ; one too far
	move.w	d2,d0
	btst	#0,d0			 ; odd length?
	bne.s	bapv_cloop		 ; yes, copy one too many

	subq.l	#1,a0			 ; copy the right number
	bra.s	bapv_clend

bapv_cloop
	move.b	-(a0),-(a1)		 ; copy characters to stack
bapv_clend
	dbra	d0,bapv_cloop
	bra.s	bxp_ssstr

;--------------------------------------------
bo_cfapin
	bsr	bpin_cont2		 ; check next frame down
	dc.w	bo_pushcfa-*
bo_cfapar
	move.l	sb_retsp(a6),a2 	 ; return stack
	tst.b	rt_type(a6,a2.l)	 ; array?
	bmi	bo_arpar2		 ; ... yes
	add.w	#bop.arrs,a4		 ; ... no, skip array stats
	bra.l	bo_cfpar		 ; evaluate function

;---------------------------------------------
bo_sfapin
	bsr	bpin_cont2		 ; check next frame down
	dc.w	bo_pushsfa-*
bo_sfapar
	move.l	sb_retsp(a6),a2 	 ; return stack
	tst.b	rt_type(a6,a2.l)	 ; array?
	bmi	bo_arpar2		 ; ... yes
	add.w	#bop.arrs,a4		 ; ... no, skip array stats
	bra.l	bo_sfpar		 ; evaluate function

;----------------------------------------------
bo_sfapinc
	bsr	bpin_cont2		 ; check next frame down
	dc.w	bo_pushsfac-*
bo_sfaparc
	move.l	sb_retsp(a6),a2 	 ; return stack
	tst.b	rt_type(a6,a2.l)	 ; array?
	bmi	bo_arpar2		 ; ... yes
	add.w	#bop.arrs,a4		 ; ... no, skip array stats
	bra.l	bo_sfparc		 ; evaluate function

;----------------------------------------------
bo_nlpin
	bsr	bpin_contsep		 ; check top frame
	dc.w	sb_iloop-*
bo_nlpar
	move.w	(a4)+,-(sp)		 ; get separator
	moveq	#0,d1			 ; null is null
	moveq	#-1,d3			 ; and no name
	move.l	d3,a0			 ; or value
	bra.s	bxp_table

bxp_pstr
	move.l	(a1)+,d3		 ; pop pointer
	exg	a1,d3			 ; and use it, saving a1
	bra.s	bxp_sstr

;-----------------------------------------------
bo_expin
	bsr	bpin_contsep		 ; check top frame
	dc.w	sb_iloop-*
bo_expar
	move.w	(a4)+,-(sp)		 ; get separator
	move.w	(a1)+,d0		 ; type of expression
	blt.s	bxp_pstr		 ; pointer to string!!!
	subq.w	#ar.float,d0		 ; most common expression
	beq.s	bxp_fp
	bgt.s	bxp_in
bxp_str
	moveq	#0,d3			 ; update a1
bxp_sstr
	move.w	(a1)+,d2		 ; length of string
bxp_ssstr
	moveq	#7+6,d1 		 ; add 6 bytes and round up
	add.w	d2,d1
	and.w	#$fff8,d1		 ; to multiple of 8
	jsr	sb_aldat		 ; allocate hole
	assert	dt_stalc+4,dt_flstr+1,dt_stlen
	move.w	d1,(a0)+		 ; allocation
	move.w	#$00ff,(a0)+		 ; flags
	move.l	a0,a2			 ; where the item is
	move.w	#nt.varst,d1		 ; type is string

	move.w	d2,(a2)+		 ; length
	beq.s	bxp_sra1		 ; none, set table entry
bxp_stcopy
	move.w	(a1)+,(a2)+		 ; copy bytes
	subq.w	#2,d2			 ; two at a time
	bgt.s	bxp_stcopy

bxp_sra1
	tst.l	d3			 ; reset a1?
	beq.s	bxp_expr
	move.l	d3,a1			 ; ... yes
	bra.s	bxp_expr

bxp_in
	subq.w	#ar.long-ar.float,d0	 ; int or long
	beq.s	bxp_li			 ; long
	move.w	(a1)+,d2
bxp_sin
	jsr	sb_aldat8		 ; allocate a simple hole
	move.l	a0,a2			 ; where the item is
	move.w	#nt.varin,d1		 ; type is integer
	assert	dt_value,dt_flint-2
	move.w	d2,(a2)+		 ; value
	move.l	#dt.flint,(a2)+ 	 ; flag
	bra.s	bxp_expr
bxp_li
	jsr	qa_lfloat		 ; convert long to float
bxp_fp
	move.w	(a1)+,d2
	move.l	(a1)+,d3
bxp_sfp
	jsr	sb_aldat8		 ; allocate a simple hole
	move.l	a0,a2			 ; where the item is
	move.w	#nt.varfp,d1		 ; type is float
	assert	dt_value,dt_flfp-6
	move.w	d2,(a2)+		 ; value exp +
	move.l	d3,(a2)+		 ; mant
	move.w	#dt.flfp,(a2)+		 ; flag

bxp_expr
	moveq	#-1,d3			 ; no name

; set new table entry for parameter
;
;	d1 c	usage / variable type
;	d3 c	name
;	a0 c	value
;
bxp_table
	move.l	sb_retsp(a6),a2 	 ; return stack
	move.l	rt_tparm(a6,a2.l),d2	 ; next parameter
	move.l	d2,d0
	addq.l	#8,d0
	move.l	d0,rt_tparm(a6,a2.l)
	add.l	sb_nmtbb(a6),d2
	cmp.l	sb_nmtbp(a6),d2 	 ; top of table?
	beq.s	bxp_chkt		 ; ... yes

	lea	(a6,d2.l),a2
	tst.w	(a2)			 ; empty?
	beq.s	bxp_stable		 ; ... yes
	bra.l	sb_ierset		 ; ... no

bxp_chkt
	cmp.l	sb_nmtbt(a6),d2 	 ; full?
	blt.s	bxp_stptr		 ; no
	movem.l d1/d3/a1,-(sp)
	jsr	sb_rnt08		 ; allocate another bit
	movem.l (sp)+,d1/d3/a1
	move.l	sb_nmtbb(a6),a3 	 ; reset name table pointer
	add.l	a6,a3
	move.l	sb_nmtbp(a6),d2

bxp_stptr
	lea	(a6,d2.l),a2
	addq.l	#8,d2
	move.l	d2,sb_nmtbp(a6) 	 ; table is larger now
 
bxp_stable
	or.w	(sp)+,d1		 ; name type + separator
	move.w	d1,(a2)+		 ; set it
	move.w	d3,(a2)+		 ; name
	move.l	a0,(a2)+		 ; and value

	jmp	(a5)

	end
