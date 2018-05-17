; SBAS_CMPTK - SBASIC convert source to compiler tokens v1.01
;
; 2012-04-29  1.01  Fixed "for i=0 to a(0,0)" bug			   (MK)

	section sbas

	xdef	sb_cmptk

	xref	sb_alwrk
	xref	sb_xpwrk

	xref	sb_cmneg

	xref	sb_setarr
	xref	sb_setpf

	xref	sb_ermess
	xref	sb_ernimp

	include 'dev8_keys_err'
	include 'dev8_keys_sbasic'
	include 'dev8_smsq_sbas_parser_keys'
	include 'dev8_smsq_sbas_comp_keys'
	include 'dev8_mac_assert'

mo setnum 0 ; stops macro assembler blowing up on comment
po setnum 0

; macro for fetching next token, skipping spaces
; depending on the value of "fasttk", this is inline or a subroutine call

get_ptok macro	rts
	ifnum	[fasttk] = 0 goto get_pslow
getp[.l]
	move.b	(a2),d5 		 ; get next token (type)
	move.w	(a2)+,d6		 ; and full token
	cmp.b	d4,d5			 ; space?
	beq.s	getp[.l]		 ; ... yes, get next
	ifstr	{[rts]} <> rts goto get_pend
	rts
	goto	get_pend


get_pslow maclab
	ifstr	{[rts]} <> rts goto get_pbsr
	bra	get_nptk		 ; branch to next token subrountine
	goto	get_pend

get_pbsr maclab
	bsr	get_nptk		 ; bsr to next token subroutine

get_pend maclab
	endm


; macro for fetch next token routine

get_ptsb macro
	ifnum	[fasttk] <> 0 goto get_psend

fasttk	setnum	1
get_nptk
	get_ptok			 ; get token
	rts
fasttk	setnum	0
get_psend maclab
	endm


fasttk	setnum	0			 ; slow
	get_ptsb			 ; fetch token subroutine - if required

	page
;+++
; SBASIC convert to compiler tokens
;
; This routine has fairly primitive memory checking. As the compiler token
; version will normally generate a smaller program than the SuperBASIC
; compatible parser token source, there are few checks for the compiler token
; memory being exceded.
; The only tokens which increase in size are floating point numbers. These
; increase from 6 bytes to 8 bytes.
; There is also the possibility that END tokens may need to be added to the end
; of a line.
; The memory is only checked when inserting a floating point number or when
; adding END tokens
;
;	d4   s	lsbyte parser space token
;	d5   s	next parser token (first byte)
;	d6   s	next parser token (word)
;	d7 c s	pointer to top of source (rel A6 on entry)
;	a0  r	pointer to base of compiled tokens (for debugger)
;	a2 c s	pointer to source (rel A6 on entry)
;	a3   s	pointer to compiler tokens
;	a4   s	pointer to compiler tokens at start of line
;	a5 c  p pointer to compiler token block
;	a6 c  p pointer to SuperBASIC variables
;
;	All other registers smashed
;	Status return standard
;---
sb_cmptk
	add.l	a6,a2
	add.l	a6,d7			 ; pointers absolute

	moveq	#sb.ptokx,d0		 ; extra space
	move.l	d7,d1
	sub.l	a2,d1			 ; program source size
	add.l	d1,d0
	asr.l	#8,d1			 ; (16 Mbyte programs only)
	addq.l	#1,d1			 ; round up
	mulu	sb_ptokx-sb_ptokb(a5),d1 ; extra bit
	add.l	d1,d0

	lea	(a5),a1 		 ; where to put pointer
	jsr	sb_alwrk
	move.l	a0,a3			 ; tokens here please

	clr.l	sb_sttbe-sb_ptokb(a5)	 ; count statement table entries

	move.l	sp,sb_prstp(a6) 	 ; save stack address (absolute)
	bra.l	sbc_eloop

sbc_lnlp
	move.w	sb_line(a6),d0		 ; line number
	addq.w	#1,d0
	move.w	d0,sb_line(a6)		 ; ... next line
sbc_lno
	cmp.w	#tkw.lno,(a2)		 ; starts with a line number?
	bne.s	sbc_slno		 ; ... no, use internal line number
	move.l	(a2)+,d0
sbc_slno
	move.w	d0,sb_pcerp(a6) 	 ; this might be error line!!
	not.w	d0
	move.w	d0,(a3)+		 ; set line number token
sbc_doln
	move.l	a3,a4			 ; start of line
	moveq	#tkb.spce+$ffffff00,d4	 ; space token
sbc_stlp
	get_ptok			 ; first token in statement
	cmp.w	#tkw.coln,d6		 ; colon?
	beq.s	sbc_stlp		 ; ... yes
	cmp.w	#tkw.eol,d6		 ; end of line?
	beq.l	sbc_elin		 ; ... yes

	cmp.w	#tkw.rem,d6		 ; rem?
	beq.s	sbc_rlin		 ; ... yes
	bsr.l	sbc_stmt		 ; process a statement
	blt.s	sbc_erex
	beq.s	sbc_estmt
	move.l	a2,d1			 ; save position in line
	bclr	#16,d0			 ; open or close clause?
	beq.s	sbc_open		 ; open

sbc_close
	cmp.l	sb_prstp(a6),sp 	 ; anything on stack
	beq.s	sbc_estmt		 ; ... no
	swap	d0			 ; type of clause matches?
	cmp.w	(sp),d0 		 ;
	bne.s	sbc_cuname		 ; ... no
	tst.l	d0			 ; any name given?
	bmi.s	sbc_rclaus		 ; ... no, remove clause
	swap	d0
	cmp.l	(sp),d0 		 ; all correct?
	bne.s	sbc_estmt		 ; ... no
	bra.s	sbc_rclaus		 ; ... yes

sbc_cuname
	bclr	#1,d0			 ; was it unnamed close?
	beq.s	sbc_estmt		 ; ... no
	cmp.w	(sp),d0 		 ; ... yes, does type match?
	bne.s	sbc_estmt		 ; ... no

sbc_rclaus
	addq.l	#4,sp			 ; ... match
	bra.s	sbc_estmt

sbc_opskp
	get_ptok			 ; ... next
sbc_open
	cmp.w	#tkw.coln,d6		 ; a colon?
	beq.s	sbc_opskp		 ; ... yes, skip it
	cmp.w	#tkw.rem,d6		 ; a remark?
	beq.s	sbc_rmstmt		 ; ... yes, not in_line
	cmp.w	#tkw.eol,d6		 ; end of line?
	beq.s	sbc_estmt		 ; ... yes, not in-line

	move.l	d0,-(sp)		 ; ... in-line
	move.l	d1,a2			 ; restore position
	move.w	#tkw.coln,d6		 ; and colon

sbc_estmt
	clr.w	(a3)+			 ; end of statement
	addq.l	#8,sb_sttbe-sb_ptokb(a5) ; one more statement for the table
	cmp.w	#tkw.coln,d6		 ; colon?
	beq.s	sbc_stlp		 ; ... yes
	cmp.w	#tkw.eol,d6		 ; end of line?
	beq.s	sbc_elin		 ; ... yes

	bsr	sb_ermess		 ; ... no, error
sbc_erex
	move.l	sb_prstp(a6),sp 	 ; reset stack
	rts

sbc_rmstmt
	clr.w	(a3)+			 ; end of statement will be removed
	addq.l	#8,sb_sttbe-sb_ptokb(a5) ; one more statement for the table
sbc_rlin
	get_ptok			 ; skip spaces to string
	cmp.w	#tkw.eol,d6		 ; end of line?
	beq.s	sbc_elin		 ; ... yes

	moveq	#3,d0
	add.w	(a2)+,d0		 ; end of REM
	bclr	#0,d0			 ; rounded
	add.l	d0,a2

sbc_elin
	subq.l	#2,a3			 ; remove statement separator
	cmp.l	a4,a3			 ; anything on line
	beq.s	sbc_scrub		 ; ... no, forget it
	move.l	sb_prstp(a6),d0
	sub.l	sp,d0			 ; amount of tokens to transfer
	ble.s	sbc_nxlin		 ; ... none

	add.l	d0,d0			 ; plus some spare
	bsr.s	sbc_ckd0		 ; check for room

sbc_ecpy
	clr.w	(a3)+			 ; end of statement
	addq.l	#8,sb_sttbe-sb_ptokb(a5) ; one more statement for the table
	move.w	(sp)+,(a3)+
	move.w	(sp)+,d0		 ; any name token?
	bmi.s	sbc_ecpye		 ; ... no
	move.w	d0,(a3)+		 ; ... yes, copy it as well
sbc_ecpye
	cmp.l	sb_prstp(a6),sp 	 ; another?
	blt.s	sbc_ecpy		 ; ... yes, copy token
	bra.s	sbc_nxlin

sbc_scrub
	subq.l	#2,a3			 ; backspace to remove marker / lno

sbc_nxlin
	addq.l	#2,a2			 ; skip line link
sbc_eloop
	cmp.l	d7,a2
	blt	sbc_lnlp

	move.l	a3,sb_ptokp-sb_ptokb(a5) ; set token list pointer
	clr.w	(a3)			 ; and sentinal at end
	clr.w	sb_pcerp(a6)		 ; no error
	move.l	sb_ptokb-sb_ptokb(a5),a0
	move.l	sb_prstb(a6),sb_prstp(a6) ; restore stack pointers
	moveq	#0,d0
	rts

	page


; routine for check room

sbc_ckroom
	moveq	#10,d0			 ; room for the most generous token
sbc_ckd0
	add.l	d7,d0
	sub.l	a2,d0			 ; amount of room required
	add.l	a3,d0			 ; top required
	sub.l	sb_ptokt-sb_ptokb(a5),d0 ; enough?
	blt.s	sbck_rts

	lea	(a5),a1 		 ; where to update pointers
	move.l	a3,sb_ptokp-sb_ptokb(a5) ; current running pointer
	sub.l	a3,a4
	move.l	sb_ptokt-sb_ptokb(a5),d0
	sub.l	(a1),d0
	move.l	d0,-(sp)
	lsr.l	#2,d0			 ; 25% extra
	add.l	(sp)+,d0		 ; re-allocate
	jsr	sb_xpwrk
	move.l	sb_ptokp-sb_ptokb(a5),a3 ; new pointer
	add.l	a3,a4			 ; new start of line
sbck_rts
	rts

	page

; This is the main controlling part of the compiler token routine
; It processes a statement. The first token has been read on entry to this
; routine.

sbc_stmt
	moveq	#0,d3			 ; used for flags
	cmp.b	#tkb.name,d5		 ; is it a name token?
	beq	sbc_nsst		 ; ... yes, statement starts with name
	cmp.b	#tkb.key,d5		 ; keyword?
	beq.s	sbcs_key
	cmp.w	#tkw.lequ,d6		 ; = (implicit select on) ?
	beq.l	sbc_selu
sbcs_err
	bra.l	sb_ermess

sbcs_key
	ext.w	d6
	add.w	d6,d6
	move.w	sbcs_table(pc,d6.w),d6
	jmp	sbcs_table(pc,d6.w)

sbcs_table equ	*-tkk.end-tkk.end
	dc.w	sbc_end-sbcs_table	   $01	    END
	dc.w	sbc_for-sbcs_table	   $02	    FOR
	dc.w	sbc_if-sbcs_table	   $03	    IF
	dc.w	sbc_rep-sbcs_table	   $04	    REPeat
	dc.w	sbc_sel-sbcs_table	   $05	    SELect
	dc.w	sbc_when-sbcs_table	   $06	    WHEN
	dc.w	sbc_def-sbcs_table	   $07	    DEFine
	dc.w	sbcs_err-sbcs_table	   $08	      PROCedure
	dc.w	sbcs_err-sbcs_table	   $09	      FuNction
	dc.w	sbc_go-sbcs_table	   $0a	    GO
	dc.w	sbcs_err-sbcs_table	   $0b	      TO
	dc.w	sbcs_err-sbcs_table	   $0c	      SUB
	dc.w	sbcs_err-sbcs_table
	dc.w	sbcs_err-sbcs_table	   $0e	      ERRor
	dc.w	sbcs_err-sbcs_table
	dc.w	sbcs_err-sbcs_table
	dc.w	sbc_rest-sbcs_table	   $11	    RESTORE
	dc.w	sbc_next-sbcs_table	   $12	    NEXT
	dc.w	sbc_exit-sbcs_table	   $13	    EXIT
	dc.w	sbc_else-sbcs_table	   $14	    ELSE
	dc.w	sbc_on-sbcs_table	   $15	    ON
	dc.w	sbc_ret-sbcs_table	   $16	    RETurn
	dc.w	sbcs_err-sbcs_table	   $17	      REMAINDER
	dc.w	sbc_data-sbcs_table	   $18	    DATA
	dc.w	sbc_dim-sbcs_table	   $19	    DIM
	dc.w	sbc_loc-sbcs_table	   $1a	    LOCal
	dc.w	sbc_let-sbcs_table	   $1b	    LET
	dc.w	sbcs_err-sbcs_table	   $1c	      THEN
	dc.w	sbcs_err-sbcs_table	   $1d	      STEP
	dc.w	sbcs_err-sbcs_table	   $1e	      REMark (already done)
	dc.w	sbc_mist-sbcs_table	   $1f	    MISTake

sbc_end        ;     $01      END
	get_ptok			 ; end what?
	moveq	#0,d0
	moveq	#0,d1
	move.b	d6,d1
	move.b	sbce_table(pc,d1.w),d0
	move.w	d0,(a3)+		 ; end token
	addq.w	#1,d0
	swap	d0
	not.w	d0			 ; DO return

	get_ptok			 ; get what follows

	assert	tkk.for,tkk.if-1,tkk.rep-2,tkk.sel-3,tkk.when-4,tkk.def-5
	subq.b	#tkk.if,d1		 ; if?
	blt.s	sbce_loop		 ; end of loop
	beq.s	sbce_done		 ; end if
	subq.b	#tkk.sel-tkk.if,d1
	blt.s	sbce_loop		 ; end of loop
	beq.s	sbce_done		 ; end select
	subq.b	#tkk.def-tkk.sel,d1	 ; end define?
	bne.s	sbce_ok 		 ; ... no, done
	cmp.w	#tkw.name,d6		 ; name follows?
	bne.s	sbce_done		 ; ... no
	addq.l	#2,a2			 ; ... yes, skip it
	bra.s	sbce_ptok

sbce_ok
	moveq	#0,d0
	rts

sbce_loop
	cmp.w	#tkw.name,d6		 ; name follows?
	bne.s	sbce_nname		 ; ... no
	move.w	(a2)+,d0
	move.w	d0,(a3)+		 ; + index

	move.l	sb_nutbb(a6),a0
	bset	#us..var,(a0,d0.w)	 ; set usage

sbce_ptok
	get_ptok
sbce_done
	tst.l	d0
	rts

sbce_nname
	addq.w	#tkc.uoff,-2(a3)	 ; say no name follows

	assert	tkc.uoff,2
	assert	tkc.rep&tkc.uoff,0
	bset	#17,d0			 ; make return code unnamed as well
	bra.s	sbce_done

sbce_table equ	*-tkk.for
	dc.b	tkc.endf,tkc.endi,tkc.endr,tkc.ends,tkc.endw,tkc.endd


sbc_for        ;     $02      FOR
	move.w	#tkc.for,(a3)+		 ; FOR
	get_ptok			 ; should be name
	move.w	(a2)+,d3
	move.w	d3,(a3)+		 ; name index
	move.l	sb_nutbb(a6),a0
	bset	#us..var,(a0,d3.w)	 ; set usage
	get_ptok			 ; =
sbcf_loop
	move.w	#tkc.cmma,(a3)+ 	 ; separate with commas
	bsr	sbc_nxex		 ; get expression FROM
	cmp.w	#tkw.to,d6		 ; TO?
	bne.s	sbcf_next		 ; ... no
	move.w	#tkc.to,(a3)+		 ; ... yes
	bsr	sbc_nxex		 ; TO
	cmp.w	#tkw.step,d6		 ; STEP?
	bne.s	sbcf_next		 ; ... no
	move.w	#tkc.step,(a3)+ 	 ; ... yes
	bsr	sbc_nxex		 ; STEP
sbcf_next
	cmp.w	#tkw.cmma,d6		 ; comma?
	beq.s	sbcf_loop		 ; ... yes

	move.l	#tkc.endf<<16,d0
	move.w	d3,d0			 ; opening statement
	tst.l	d0
	rts

sbc_if	       ;     $03      IF
	move.w	#tkc.if,(a3)+		 ; say it is IF
	bsr	sbc_nxex		 ; get the controlling expression
	cmp.w	#tkw.then,d6		 ; followed by then?
	bne.s	sbci_done
	cmp.w	#tkw.eol,(a2)		 ; then at end? (spaces stripped!)
	beq.s	sbci_ptok		 ; ... yes, ignore it
	move.w	#tkw.coln,d6		 ; ... no, convert to colon
	bra.s	sbci_done

sbci_ptok
	get_ptok
sbci_done
	move.l	#tkc.endi<<16+$ffff,d0
	rts

sbc_rep        ;     $04      REPeat
	move.w	#tkc.rep,(a3)+		 ; REPeat
	get_ptok			 ; should be name (if anything)
	move.l	#tkc.endr<<16+$ffff,d0	 ; assume nothing
	bra	sbce_loop		 ; and check for name as for END REP

sbc_sel        ;     $05      SELect
	move.w	#tkc.sel,(a3)+		 ; select
	get_ptok			 ; may be followed by ON
	cmp.w	#tkw.on,d6		 ; ... is it?
	bne.s	sbcs_expr		 ; ... no, part of expression
	get_ptok
sbcs_expr
	bsr.l	sbc_expr		 ; controlling variable / expression
	cmp.w	#tkw.lequ,d6		 ; in line?
	bne.s	sbcs_done
	subq.l	#2,a2			 ; back space to =
	move.w	#tkw.coln,d6		 ; and add statement
sbcs_done
	move.l	#tkc.ends<<16+$ffff,d0
	rts

sbc_when       ;     $06      WHEN
	move.w	#tkc.when,(a3)+ 	 ; when
	get_ptok			 ; skip error (it is the only poss)
	get_ptok rts

sbc_def        ;     $07      DEFine
	moveq	#16,d0			 ; bra before define, break test after
	add.l	d0,sb_sttbe-sb_ptokb(a5) ; two more statements for the table
	get_ptok
	moveq	#tkc.dfpr,d2
	moveq	#us..proc,d0
	cmp.w	#tkw.proc,d6		 ; def proc?
	beq.s	sbcd_do 		 ; ... yes
	moveq	#tkc.dffn,d2
	moveq	#us..fun,d0
sbcd_do
	move.w	d2,(a3)+
	get_ptok			 ; should be name
	moveq	#0,d1
	move.w	(a2)+,d1
	move.w	d1,(a3)+		 ; name index
	move.l	sb_nutbb(a6),a1
	bset	d0,(a1,d1.w)		 ; set usage
	assert	nt.sbfun-nt.sbprc,us..fun-us..proc
	addq.b	#nt.sbfun-us..fun,d0	 ; nt type
	move.w	sb_pcerp(a6),d3 	 ; line number
	jsr	sb_setpf		 ; set proc / fun

	get_ptok			 ; left parenth?
	cmp.w	#tkw.lpar,d6
	bne.s	sbcd_ok 		 ; ... no
	get_ptok			 ; name or close bracket
	cmp.w	#tkw.rpar,d6		 ; close bracket?
	beq.s	sbcd_done		 ; ... yes
	bra.s	sbcd_name
sbcd_loop
	get_ptok			 ; name token
sbcd_name
	move.w	(a2)+,d1
	move.w	d1,(a3)+		 ; name index
	bset	#us..fprm,(a1,d1.w)	 ; name is formal parameter
	get_ptok			 ; comma or close bracket
	cmp.w	#tkw.rpar,d6		 ; close bracket?
	bne.s	sbcd_loop
sbcd_done
	get_ptok			 ; should be end of line
sbcd_ok
	move.l	#tkc.endd<<16+$ffff,d0	 ; end def required for in-line
	rts

sbc_go	       ;     $0a      GO
	get_ptok			 ; GO TO or GO SUB
	add.w	#tkc.goto>>1-tkw.to+$10000,d6
	add.w	d6,d6
	move.w	d6,(a3)+
	bra	sbc_nxex		 ; fetch expression to goto

sbc_rest       ;     $11      RESTORE
	move.w	#tkc.rest,(a3)+ 	 ; restore
	get_ptok			 ; end of line or statement?
	cmp.w	#tkw.coln,d6		 ; end of statement?
	beq.s	sbcx_rts		 ; ... yes
	cmp.w	#tkw.eol,d6		 ; or line?
	beq.s	sbcx_rts		 ; ... yes
	bra.l	sbc_expr		 ; fetch expression to restore to

sbc_next       ;     $12      NEXT
	move.w	#tkc.next,(a3)+
	bra.s	sbcx_cknam

sbc_exit       ;     $13      EXIT
	move.w	#tkc.exit,(a3)+
sbcx_cknam
	get_ptok
	cmp.w	#tkw.name,d6		 ; name follows?
	bne.s	sbcx_nname		 ; ... no
	move.w	(a2)+,(a3)+		 ; ... yes, index
sbcx_ptok
	get_ptok
sbcx_done
	moveq	#0,d0
sbcx_rts
	rts

sbcx_nname
	addq.w	#tkc.uoff,-2(a3)	 ; no name follows
	bra.s	sbcx_done

sbc_else       ;     $14      ELSE
	move.w	#tkc.else,(a3)+ 	 ; just else
	get_ptok			 ; end of line?
	cmp.w	#tkw.eol,d6
	beq.s	sbcx_done		 ; ... yes
	subq.l	#2,a2			 ; ... no, fake a new statement
	move.w	#tkw.coln,d6
	move.b	#tkb.key,d5
	bra.s	sbcx_done


sbc_on	       ;     $15      ON
	move.w	#tkc.onsl,(a3)+ 	 ; assume ON x =
	move.l	a3,-(sp)		 ; save pointer to initial token
	move.l	a2,-(sp)		 ; and where we are
	bsr.l	sbc_nxex		 ; and get expression
	move.l	(sp)+,a1
	move.l	(sp)+,a0
	bne	sbcs_err

	cmp.w	#tkw.go,d6		 ; ON GOTO, GOSUB?
	bne.l	sbc_selon		 ; ... no

sbc_ogo
	get_ptok			 ; TO or SUB
	add.w	#tkc.ongo>>1-tkw.to+$10000,d6
	add.w	d6,d6
	move.w	d6,-(a0)		 ; reset token
	bra.l	sbc_exlsep		 ; get expression list


sbc_ret        ;     $16      RETurn
	move.w	#tkc.ret,(a3)+
	get_ptok			 ; expression?
	bsr.l	sbc_xeos
	ble.s	sbcr_rts
sbcr_ok
	moveq	#0,d0
sbcr_rts
	rts

sbc_data       ;     $18      DATA
	move.w	#tkc.data,(a3)+ 	 ; data
	bra.l	sbc_exlist		 ; followed by expression list


sbc_dim        ;     $19      DIM
	move.l	#tkc.dim<<16+us..ary<<8,-(sp)
sdm_nloop
	move.w	(sp),(a3)+		 ; set token
	get_ptok			 ; get name

	moveq	#0,d1
	move.w	(a2)+,d1
	move.w	d1,(a3)+
	move.l	sb_nutbb(a6),a1
	add.w	d1,a1

	get_ptok			 ; and left parenth
	cmp.w	#tkw.lpar,d6		 ; ... is it?
	bne.s	sdm_var
	move.w	#tkc.lpar,(a3)+ 	 ; start of index list

	move.b	2(sp),d0
	bset	d0,(a1) 		 ; set var type, array or local array
	bne.s	sdm_iloop		 ; it is already
	subq.b	#us..lary,d0		 ; local?
	beq.s	sdm_iloop

	jsr	sb_setarr		 ; set name table to array type
 
sdm_iloop
	bsr.l	sbc_nxex		 ; expression
	cmp.w	#tkw.cmma,d6		 ; comma?
	bne.s	sdm_end
	move.w	#tkc.cmma,(a3)+ 	 ; ... yes
	bra.s	sdm_iloop

sdm_end
	move.w	#tkc.rpar,(a3)+ 	 ; end of list (worth checking?)
	get_ptok
	bra.s	sdm_next

sdm_var
	bset	#us..lvar,(a1)		 ; set var type, array or local array

sdm_next
	cmp.w	#tkw.cmma,d6		 ; comma?
	beq.s	sdm_nloop		 ; ... yes
	addq.w	#4,sp
	bra.s	sbcr_ok


sbc_loc        ;     $1a      LOCal
	move.l	#tkc.lcal<<16+us..lary<<8,-(sp)
	bra.s	sdm_nloop

sbc_mist       ;     $1f      MISTake
	move.w	#tkc.mist,(a3)+ 	 ; mistake

	get_ptok			 ; skip to text
	moveq	#1,d0
	add.w	(a2)+,d0		 ; end of text
	bclr	#0,d0			 ; rounded
	add.l	d0,a2
	bra	sbcx_ptok

sbc_selon
	move.l	a0,a3
	move.l	a1,a2			 ; reset pointers
	get_ptok			 ; skip name
	move.w	(a2)+,(a3)+		 ; name index
	get_ptok			 ; skip =
	cmp.w	#tkw.lequ,d6		 ; it was, wasn't it?
	beq.s	sbcs_ckrem		 ; and check for remainder
	bra	sbcs_err

sbc_selu       ;	'='    (select) ON unnamed
	move.w	#tkc.onsu,(a3)+ 	 ; unnamed select on
sbcs_ckrem
	addq.l	#8,sb_sttbe-sb_ptokb(a5) ; one more statement for the table
	get_ptok
	cmp.w	#tkw.rmdr,d6		 ; remainder?
	bne.s	sbcs_range		 ; ... no
	move.w	#tkc.rmdr,(a3)+ 	 ; ... yes
	bra	sbcx_ptok		 ; ... done

sbcs_range
	bsr.l	sbc_expr		 ; expression
	cmp.w	#tkw.to,d6		 ; to?
	bne.s	sbcs_erng		 ; ... no
	move.w	#tkc.to,(a3)+		 ; ... yes
	bsr.l	sbc_nxex
sbcs_erng
	cmp.w	#tkw.cmma,d6		 ; comma?
	bne	sbcr_ok 		 ; ... no
	move.w	#tkc.cmma,(a3)+ 	 ; ... yes
	get_ptok
	bra.s	sbcs_range		 ; take next range

	page

; LET is an extra entry into "name at start of statement" which sets D3 to flag
; that only LET is permitted.

sbc_let
	moveq	#1,d3			 ; flag LET
	get_ptok
	cmp.b	#tkb.name,d5		 ; is it a name token?
	bne.l	sb_ermess		 ; ... no

; Routine to deal with a name at the start of a statement.
; This could be an assignment or a procedure call.
; If is is of the form
;	name assign= ....	      it is an assignment,
;	name (.....) assign= .....    it could be either.
;
; Otherwise it is a procedure call.
; Unfortunately, the parser does not record which it has found. This token
; converter, therefore, assumes that if the first significant symbol after the
; name is an assignment operator or a left bracket, that the statement is an
; assignment. Otherwise it is a procedure call.
;
; If, when processing an assignement to an array element, it reaches the end of
; the index list, and there has been more than one index, it is accepted as an
; array assignement. If the next significant token is not an assignment operator,
; it changes the first token to a call and continues as a procedure call.
; Otherwise it changes the first token to ambiguous.

sbc_nsst
	move.w	(a2)+,d3		 ; save name index
	get_ptok
	cmp.w	#tkw.lequ,d6		 ; LET x = is the easy case
	beq.s	sbc_lequ		 ; ... OK
	cmp.w	#tkw.lpar,d6		 ; name ( is the ambiguous one
	bne.s	sbc_proc		 ; ... it's a procedure call

	move.l	a3,a1
	sub.l	(a5),a1 		 ; position in program
	move.l	a1,-(sp)

	move.w	#tkc.leta,(a3)+ 	 ; assume array assignment
	move.w	d3,(a3)+

	moveq	#0,d3			 ; we clear to check for separators

	bsr.s	sbc_indx		 ; read indices

	move.l	(sp)+,a1		 ; recover start of line
	add.l	(a5),a1

	tst.b	d3			 ; more than one index?
	bne.s	sbc_equ 		 ; ... yes, it is an assignment

	cmp.w	#tkw.lequ,d6		 ; is next token an assignment?
	bne.s	sbc_sprc		 ; ... no set to procedure
	move.w	#tkc.ambs,(a1)		 ; mark it as ambiguous for the moment
	bra.s	sbc_equ 		 ; and continue as assignment

sbc_lequ
	move.w	#tkc.letv,(a3)+ 	 ; assign to variable
	move.l	sb_nutbb(a6),a1
	or.b	#us.var,(a1,d3.w)	 ; set usage is variable
	move.w	d3,(a3)+

sbc_equ
	move.w	#tkc.eq,(a3)+		 ; we put in a '=' here, it is not
					 ; required for assignment, but is if
					 ; the statement turns out to be a call
	bra.l	sbc_nxex		 ; fetch expression to assign

sbc_sprc
	move.w	#tkc.call,(a1)		 ; change to call procedure
	bsr	sbx_pop 		 ; continue the expr from post op
	bra.s	sbc_parm		 ; and check for more parameters

sbc_proc
	move.w	#tkc.call,(a3)+ 	 ; call procedure
	move.w	d3,(a3)+
	bra.s	sbc_parm		 ; and process parameters

	page

; Routine to deal with name in expression.
; It assumes that the name is not an array and generates a tkc_refn (reference
; name) token. If the next significant token is an open bracket, it changes the
; token to a tkc_rfna, and calls the routine to process parameters and indices.

sbc_name
	move.w	#tkc.refn,(a3)+ 	 ; name reference
	move.w	(a2)+,(a3)+		 ; and index
	get_ptok
	cmp.w	#tkw.lpar,d6		 ; next is left parenthesis?
	beq.s	sbc_nind		 ; ... yes
	rts

sbc_nind
	move.w	#tkc.rfna,-4(a3)	 ; it is a function or array reference

; Routine to deal with array indices and function parameters.
; It removes the opening bracket, calls the routine to process a parameter list
; and removes the closing bracket.
;
; There is one case where an ambiguity needs to be resolved: (expr) looks like
; (parm list). If there is only one parameter, it is still ambiguous. But, if
; there are any separators, then d3 will be set.

sbc_indx
	move.w	#tkc.lpar,(a3)+ 	 ; start of index list
	bsr.s	sbc_parmtk		 ; get parameter list
	move.w	#tkc.rpar,(a3)+ 	 ; end of list (worth checking?)
	get_ptok rts			 ; get next token and return

; This is part of the sbp_parm loop which transfers a separator

sbc_nsep
	st	d3			 ; we've a separator
	ext.w	d6			 ; word 1 to 5
	add.w	d6,d6			 l word 2 to 10
	add.w	#tkc.cmma-2,d6		 ; our separator token
	move.w	d6,(a3)+

; Routine to deal with parameter list. First get the first token of expression.
; This transforms expressions, continuing until end of statement or close
; bracket.

sbc_parmtk
	get_ptok

; Routine to deal with parameter list. Next token is in d5/d6.
; This transforms expressions, continuing until end of statement or close
; bracket.

sbc_parm
	cmp.b	#tkb.sep,d5		 ; separator
	beq.s	sbc_nsep
	cmp.w	#tkw.coln,d6		 ; end of statement?
	beq.s	sbp_rts 		 ; ... yes
	cmp.w	#tkw.eol,d6		 ; or line?
	beq.s	sbp_rts 		 ; ... yes
	cmp.w	#tkw.rpar,d6		 ; close bracket?
	beq.s	sbp_rts

	cmp.w	#tkw.hash,d6		 ; starts with a hash?
	bne.s	sbp_pexp		 ; ... no
	move.w	#tkc.hash,(a3)+ 	 ; ... yes
	get_ptok
sbp_pexp
	move.w	d3,-(sp)		 ; save separator flag
	bsr.s	sbc_expr		 ; get an expression
	move.w	(sp)+,d3
	tst.l	d0
	beq.s	sbc_parm
sbp_rts
	rts

; Process expression list (preceded by separator)

sbc_exlsep
	move.w	#tkc.cmma,(a3)+ 	 ; put in separator

; Process expression list

sbc_exlist
	bsr.s	sbc_nxex		 ; next expression
	bne.s	sbp_rts
	cmp.w	#tkw.cmma,d6		 ; comma?
	beq.s	sbc_exlsep		 ; ... yes, carry on

	moveq	#0,d0
	rts

; check for end of statement or expression
; returns +ve if end of statement

sbc_xeos
	cmp.w	#tkw.coln,d6		 ; colon?
	beq.s	sbcx_eos		 ; ... yes
	cmp.w	#tkw.eol,d6		 ; end of line?
	bne.s	sbc_expr		 ; ... no, expression required
sbcx_eos
	moveq	#1,d0
	rts

sbc_nxex			 ; get next token then expression
	get_ptok

; Routine to deal with an expressions.
; Where possible, it eliminates monadic operators by combining them with a
; following constant.
; It reduces the type of constants to the simplest possible.
;
; The general form of an expression is:     [mo] item [po] {op [mo] item [po]}
;		  where 	 mo is:     + - ~~ NOT
;				 po is:     ( list of expressions )
;				 op is:     any operator
;			       item is:     a constant
;					    a name
;					    an array or function
;					    an expression in parentheses

sbc_expr

; first the optional monadic operator

	cmp.b	#tkb.mono,d5		 ; monadic operator?
	bne.s	sbx_item		 ; ... no, must be an item
	moveq	#0,d0
	move.b	d6,d0			 ; save monop
	get_ptok			 ; and get next token

	assert	tkm.neg,tkm.pos-1,tkm.bnot-2,tkm.not-3
	subq.w	#tkm.pos,d0		 ; is monop +
	beq.s	sbx_item		 ; ... yes, ignore it
	bgt.s	sbx_mono		 ; bnot or not
	cmp.b	#tkb.flot,d5		 ; followed by float?
	blo.s	sbx_mono		 ; ... no, add monadic operator

sbx_mneg
	pea	sbc_pf6
	jmp	sb_cmneg		 ; fetch and negate float

	assert	tkm.neg,tkm.pos-1,tkm.bnot-2,tkm.not-3
	dc.b	tkc.neg 		 ; negate
sbx_mtok
	dc.b	0			 ; ... plus should not occur
	dc.b	tkc.bnot		 ; bitwise not
	dc.b	tkc.not 		 ; NOT

sbx_mono
	clr.b	(a3)+
	move.b	sbx_mtok(pc,d0.w),(a3)+  ; monadic op
	bra.s	sbx_item

; now the item which must be here

sbx_mnxt				 ; monadic operation done, get next
	get_ptok
sbx_item
	cmp.b	#tkb.flot,d5		 ; float?
	blo.s	sbx_cstr		 ; ... no

	and.w	#$0fff,d6
	move.w	d6,d2
	move.l	(a2)+,d1
sbc_pf6
	bsr	sbc_ckroom
	move.w	#tkc.fp6,(a3)+		 ; put float
	move.w	d2,(a3)+
	move.l	d1,(a3)+
	bra.s	sbx_nxop

sbx_cstr
	cmp.b	#tkb.strg,d5		 ; string
	bne.s	sbx_cnam		 ; no

	move.w	#tkc.str,(a3)+		 ; ... no, it is a string
	move.l	a2,(a3)+
	moveq	#1,d0
	add.w	(a2)+,d0		 ; length +1
	bclr	#0,d0			 ; to round up
	add.w	d0,a2
	bra.s	sbx_nxop

sbx_cnam
	cmp.w	#tkw.name,d6		 ; name?
	bne.s	sbx_cexp		 ; ... no
	move.l	d3,-(sp)		 ; sbc_name might smash d3
	bsr	sbc_name		 ; ... yes, process name
	move.l	(sp)+,d3
	bra.s	sbx_pop

sbx_cexp
	cmp.w	#tkw.lpar,d6		 ; left parenthesis
	bne	sb_ermess		 ; ... no
	move.w	#tkc.lpar,(a3)+
	bsr	sbc_nxex		 ; process expression
	move.w	#tkc.rpar,(a3)+
	cmp.w	#tkw.rpar,d6		 ; right parenthesis
	bne	sb_ermess		 ; ... no

; There may be another operator (or post op)

sbx_nxop
	get_ptok			 ; next token
sbx_pop
	cmp.w	#tkw.lpar,d6		 ; is it left parenthesis (postop)
	bne.s	sbx_op

	move.w	#tkc.lpar,(a3)+
	get_ptok
	cmp.w	#tkw.sto,d6		 ; TO?
	beq.s	sbx_ppto		 ; ... yes

	bsr	sbc_expr		 ; next expression
	bne.s	sbx_rts
	cmp.w	#tkw.sto,d6		 ; TO?
	bne.s	sbx_ppcrpar		 ; ... no
sbx_ppto
	move.w	#tkc.to,(a3)+		 ; ... yes

	get_ptok
	cmp.w	#tkw.rpar,d6		 ; TO then right par?
	beq.s	sbx_pprpar		 ; ... yes
	bsr	sbc_expr
	bne.s	sbx_rts

sbx_ppcrpar
	cmp.w	#tkw.rpar,d6		 ; must end at right par
	bne	sb_ermess		 ; ... no
sbx_pprpar
	move.w	#tkc.rpar,(a3)+ 	 ; ... yes
	bra.s	sbx_nxop

sbx_op
	cmp.b	#tkb.op,d5		 ; is it an operator?
	bne.s	sbx_rtsok

	moveq	#0,d0
	move.b	d6,d0
	move.b	sbx_otok-1(pc,d0.w),d0	 ; operator token / 2
	add.w	d0,d0
	move.w	d0,(a3)+
	bra	sbc_nxex

sbx_rtsok
	moveq	#0,d0
sbx_rts
	rts

sbx_otok
	dc.b	tkc.plus>>1	 $01	 ; +
	dc.b	tkc.minu>>1	 $02	 ; -
	dc.b	tkc.mulf>>1	 $03	 ; *
	dc.b	tkc.divf>>1	 $04	 ; /
	dc.b	tkc.ge>>1	 $05	 ; >=
	dc.b	tkc.gt>>1	 $06	 ; >
	dc.b	tkc.apeq>>1	 $07	 ; ==
	dc.b	tkc.eq>>1	 $08	 ; =
	dc.b	tkc.ne>>1	 $09	 ; <>
	dc.b	tkc.le>>1	 $0a	 ; <=
	dc.b	tkc.lt>>1	 $0b	 ; <
	dc.b	tkc.bor>>1	 $0c	 ; ||
	dc.b	tkc.band>>1	 $0d	 ; &&
	dc.b	tkc.bxor>>1	 $0e	 ; ^^
	dc.b	tkc.pwr>>1	 $0f	 ; ^
	dc.b	tkc.cnct>>1	 $10	 ; &
	dc.b	tkc.or>>1	 $11	 ; OR
	dc.b	tkc.and>>1	 $12	 ; AND
	dc.b	tkc.xor>>1	 $13	 ; XOR
	dc.b	tkc.mod>>1	 $14	 ; MOD
	dc.b	tkc.div>>1	 $15	 ; DIV
	dc.b	tkc.inst>>1	 $16	 ; INSTR
	ds.w	0

	end
