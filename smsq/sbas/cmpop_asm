; SBAS_CMPOP - Convert Compiler Tokens to Operations  V2.00  1994 Tony Tebby
; 2.01 2019.09.10 string variable cannot be REPeat variable  (wl)

	section sbas

	xdef	sb_cmpop
	xdef	sb_cmpop_indx

	xref	sb_alwrk
	xref	sb_cmpopp
	xref	sb_cmpopx
	xref	sb_cmpopx_int
	xref	sb_cmpopx_ic
	xref	sb_ermess
	xref	sb_ernimp

	xref	qa_nintd

	include 'dev8_keys_sbasic'
	include 'dev8_keys_err4'
	include 'dev8_smsq_sbas_comp_keys'
	include 'dev8_smsq_sbas_inter_keys'
	include 'dev8_mac_assert'

sbc_vtype
	moveq	#0,d3
	move.w	(a3)+,d3		 ; name table index
;	 move.l  sb_nutbb(a6),a2	  ; name usage table
;	 btst	 #us..var,(a2,d3.w)	  ; can it be variable?
;	 beq.s	 sbcv_rts		  ; ... no

	move.w	d3,d1			 ; parser index in d1.w
	lsl.l	#nt.ishft,d3		 ; inter index in d3.l
	move.l	d3,d2
	add.l	sb_nmtbb(a6),d2 	 ; offset
	moveq	#0,d0
	move.w	nt_name(a6,d2.l),d0	 ; name
	add.l	sb_nmlsb(a6),d0
	moveq	#0,d2
	move.b	(a6,d0.l),d2		 ; length of name
	add.l	d2,d0
	move.b	(a6,d0.l),d0		 ; last character
	moveq	#nt.fp,d2		 ; assume fp in d2.l
	sub.b	#'$',d0 		 ; string?
	beq.s	sbcv_str		 ; ... yes
	subq.b	#'%'-'$',d0		 ; integer?
	beq.s	sbcv_int		 ; ... yes
	rts
sbcv_str
	moveq	#nt.st,d2		 ; string in d2.l
	rts
sbcv_int
	moveq	#nt.in,d2		 ; integer in d2.l
	rts


;+++
; SBASIC convert to operation tokens
;
;	name table index
;	d6   s	line number / statement number
;	a5 c  p pointer to base of compiler token block
;	a6 c  p pointer to SuperBASIC variables
;
;	All other registers smashed
;	Status return standard
;---
sb_cmpop
	move.l	sb_cmdlb(a6),a2 	 ; structure stack below command line
	lea	-st.elen(a6,a2.l),a2
	st	(a2)			 ; empty structure stack
	move.l	a2,sb_strtp(a6)
	move.l	sb_buffb(a6),a2 	 ; buffer base
	lea	st.elen(a6,a2.l),a2
	move.l	a2,sb_strtl(a6) 	 ; stack limit

	clr.l	sb_unrlp(a6)		 ; no unresolved loops

	move.l	sb_ptokb-sb_ptokb(a5),a3 ; compiler tokens
	move.l	sb_ptokp-sb_ptokb(a5),a4 ; top of program
	moveq	#$40,d0
	add.l	a4,d0
	sub.l	a3,d0			 ; for the moment allocate twice
	add.l	d0,d0			 ; compiler token space + a bit
	tst.b	sb_cmdl(a6)		 ; command line?
	bne.s	sco_alloc		 ; ... yes
	move.l	d0,d2			 ;
	lea	sb_dtstb(a6),a1 	 ; for data statements
	jsr	sb_alwrk
	move.l	d2,d0

	clr.l	sb_pdata(a6)		 ; no previous data
	clr.l	sb_dttbe(a6)		 ; or statements


sco_alloc
	lea	sb_progb-sb_ptokb(a5),a1 ; alloc for program (or command line)
	jsr	sb_alwrk

sco_loop
	cmp.l	a4,a3			 ; program all done?
	bge.s	sco_done
	move.w	(a3)+,d0		 ; next token
	bgt.s	sco_ident

	move.w	d0,(a0)+		 ; line number or statement number
	beq.s	sco_stmt		 ; statement number

	move.w	d0,d6			 ; line number
	not.w	d6
	swap	d6
	clr.w	d6			 ; first statement
sco_stmt
	addq.w	#1,d6			 ; next statement
	bra.s	sco_loop

sco_done
	bsr.s	sco_istruct		 ; in structure?

	addq.w	#1,d6			 ; end of program
	move.l	sb_unrlp(a6),d0 	 ; any unresolved loops?

	bra.s	sco_unrlend
sco_unrloop
	move.l	d0,a2
	move.l	(a2),d0 		 ; set next
	move.l	d6,(a2) 		 ; set to end of program
	tst.l	d0
sco_unrlend
	bne.s	sco_unrloop

	move.w	#bo.stop,(a0)+		 ; put stop at end
	move.l	a0,sb_progp-sb_ptokb(a5)
	moveq	#0,d0
sco_rts
	rts

sco_istruct
	move.l	sb_strtp(a6),a2 	 ; structure stack
	moveq	#ern4.inif,d0		 ; assume incomplete if
	move.b	(a2),d1 		 ; type at top of stack
	bmi.s	sco_rts
	addq.l	#4,sp			 ; do not return

	beq.s	sco_erset		 ; incomplete if
	moveq	#ern4.insl,d0		 ; assume incomplete select
	subq.b	#st.def,d1
	blt.s	sco_erset		 ; ... it is
	bgt.s	sco_inwhen
	moveq	#ern4.indf,d0		 ; may be incomplete define
	bra.s	sco_erset		 ; ... it is
sco_inwhen
	moveq	#ern4.inwh,d0		 ; incomplete when

sco_erset
	add.w	#err4.null,d0
	rts

sco_ident
	move.w	sco_stttab(pc,d0.w),d0
	jmp	sco_stttab(pc,d0.w)

; base of table

op.base equ	tkc.mist
opnum	setnum	op.base

veco	macro	name
	dc.w	sco_[name]-sco_stttab

	ifnum	tkc.[name] = [opnum] goto vecoexit
	error	tkc.[name] is not token [opnum]

vecoexit  maclab
opnum	setnum	[opnum]+2
	endm


sco_stttab equ *-op.base
	veco	mist
	veco	dfpr
	veco	dffn
	veco	lcal
	veco	dim
	veco	ret
	veco	endd
	veco	if
	veco	else
	veco	endi
	veco	for
	veco	rep
	veco	repu
	veco	next
	veco	nxtu
	veco	exit
	veco	xitu
	veco	endf
	veco	ndfu
	veco	endr
	veco	ndru
	veco	sel
	veco	onsl
	veco	onsu
	veco	ends
	veco	when
	veco	endw
	veco	goto
	veco	gosb
	veco	ongo
	veco	ongs
	veco	data
	veco	rest
	veco	letv
	veco	leta
	veco	call
	veco	ambs

;----------------------------------------------- MISTake
sco_mist
	move.w	#bo.mistake,(a0)+
	bra	sco_loop

;----------------------------------------------- DEFINE PROC / FN
sco_dfpr
sco_dffn
	move.w	#bo.goadd,(a0)+ 	 ; add skip past
	move.w	(a3)+,d1		 ; name index
	move.l	sb_strtp(a6),a2 	 ; structure stack
	move.b	st_type(a2),d0		 ; type
	bgt.s	sdf_check		 ; there is one

	move.w	#st.def<<8,d2		 ; structure type
	bsr.l	sco_pstruct		 ; and setup structure stack
	clr.l	(a0)+			 ; first def
	bra.s	sdf_parm

sdf_check
	subq.b	#st.def,d0		 ; correct type?
	bne.s	sdf_bdef
	cmp.w	st_indx(a2),d1		 ; correct define?
	bne.s	sdf_bdef
	move.l	st_def(a2),a1		 ; previous definition
	move.l	a1,(a0)+		 ; link

sdf_parm
	clr.w	(a0)+			 ; dummy end of statement
	addq.w	#1,d6			 ; another statement on line
	move.l	#bo.formp<<16,(a0)+
	move.l	a0,a2			 ; formal parameter list
	bra.s	sdf_eploop

sdf_ploop
	lsl.w	#nt.ishft,d1		 ; interpreter name index
	move.w	d1,(a0)+
sdf_eploop
	move.w	(a3)+,d1		 ; name?
	bgt.s	sdf_ploop		 ; ... yes
	move.l	a0,d2
	sub.w	a2,d2
	lsr.w	#1,d2			 ; number of parameters
	move.w	d2,-(a2)		 ; in token

	subq.w	#2,a3			 ; backspace

sloc_nxts
	move.l	a3,a1			 ; do not update a3 unless we have local
sloc_nxtl
	move.w	(a1)+,d0		 ; end of statement?
	ble.s	sloc_nxtl		 ; ... yes
	cmp.w	#tkc.lcal,d0		 ; local?
	bne.s	sloc_done		 ; ... no

sloc_do
	move.l	a1,a3
	move.w	(a3)+,d3		 ; name index
	lsl.w	#nt.ishft,d3
	cmp.w	#tkc.lpar,(a3)		 ; bracket?
	beq.s	sloc_dim		 ; bracket - dimension it
	move.w	#bo.locvar,(a0)+
	move.w	d3,(a0)+		 ; local variable
	bra.s	sloc_nxts

sloc_dim
	addq.l	#2,a3			 ; skip bracket
	swap	d3
	clr.w	d3
	move.w	#bo.diml,(a0)+		 ; dimension local variable
sloc_index
	addq.w	#1,d3			 ; another dimension
	bsr.l	sb_cmpopx_int		 ; get expression
	move.l	d0,d0
	bne.l	sb_ermess
	cmp.w	#tkc.rpar,(a3)+ 	 ; all indices done?
	bne.s	sloc_index

	move.w	#bo.rts4,(a0)+		 ; RTS at end
	move.l	d3,(a0)+
	bra.s	sloc_nxts

sloc_done
	move.l	#bo.break,(a0)+ 	 ; dummy end of statement / check break
	addq.w	#1,d6			 ; another statement on line
	bra	sco_loop

sdf_bdef
	moveq	#ern4.bdef,d0
	bra	sco_erset


;----------------------------------------------- LOCAL
sco_lcal
	moveq	#ern4.bloc,d0
	bra	sco_erset		 ; misplaced local


;---------------------------------------------- DIMENSION
sco_dim
	move.w	(a3)+,d3		 ; name index
	lsl.w	#nt.ishft,d3
	swap	d3
	clr.w	d3
	addq.w	#2,a3			 ; skip bracket
sdim_index
	addq.w	#1,d3			 ; another dimension
	bsr.l	sb_cmpopx_int		 ; get expression
	move.l	d0,d0
	bne.l	sb_ermess
	cmp.w	#tkc.rpar,(a3)+ 	 ; all indices done?
	bne.s	sdim_index

	move.w	#bo.dim,(a0)+		 ; dimension it
	move.l	d3,(a0)+

	cmp.w	#tkc.dim,(a3)+		 ; another?
	beq.s	sco_dim 		 ; ... yes

	subq.w	#2,a3
	bra	sco_loop

;----------------------------------------------- RETURN
sco_ret
	move.w	#bo.return,(a0)+	 ; assume return, pure and simple
	tst.w	(a3)
	ble	sco_loop		 ; ... it is

	subq.l	#2,a0
	jsr	sb_cmpopx		 ; compile expression
	move.w	#bo.retexp,(a0)+	 ; return expression
	bra	sco_loop

;----------------------------------------------- END DEFINE
sco_endd
	move.l	sb_strtp(a6),a2 	 ; structure stack
	cmp.b	#st.def,st_type(a2)	 ; correct type?
	bne.s	sed_bedf

	move.w	#bo.return,(a0)+	 ; set return, pure and simple

	move.l	d6,d1			 ; this statement
	addq.w	#1,d1			 ; next
	move.l	st_def(a2),d0		 ; definition(ish)
sed_loop
	move.l	d0,a1
	move.l	(a1),d0
	move.l	d1,(a1)
	tst.l	d0
	bne.s	sed_loop

	add.w	#st.elen,a2
	move.l	a2,sb_strtp(a6)
	bra	sco_loop

sed_bedf
	moveq	#ern4.bedf,d0		 ; misplaced end
sco_mend
	tst.b	st_type(a2)		 ; empty stack?
	bpl	sco_done		 ; ... no, create appropriate message
	bra	sco_erset

;-----------------------------------------------
sco_if
	jsr	sb_cmpopx		 ; compile expression
	cmp.l	#tkc.goto<<6+tkc.fp6,2(a3) ; IF ... GOTO nnn
	bne.s	scoi_do 		 ; ... no
	cmp.w	#tkc.endi,$a(a3)	 ; end of IF?
	bne.s	scoi_do
	addq.l	#4,a3			 ; skip colon, goto
	move.w	#bo.gonz,(a0)+		 ; goto on non-zero
	jsr	sb_cmpopx_ic		 ; get integer
	nop				 ; ... never returns here
	move.w	d1,(a0)+		 ; goto here
	clr.w	(a0)+
	addq.w	#2,a3			 ; skip END IF
	bra	sco_loop

scoi_do
	move.w	#bo.goz,(a0)+		 ; standard GOTO
	moveq	#st.if<<8,d2
	moveq	#0,d1
	bsr.l	sco_pstruct
	move.l	d0,(a0)+		 ; fill in address later
	bra	sco_loop

;-------------------------------------------------
sco_else
	move.l	sb_strtp(a6),a2 	 ; structure stack

	tst.w	st_type(a2)		 ; IF?
	bne.s	scoe_bad		 ; ... no

	tst.l	st_else(a2)		 ; have we already had an else?
	bne.s	scoe_bad
	move.w	#bo.goadd,(a0)+ 	 ; goto
	move.l	a0,st_else(a2)
	clr.l	(a0)+			 ; nowhere
	move.l	st_def(a2),a2
	moveq	#1,d0
	add.l	d6,d0
	move.l	d0,(a2) 		 ; and jump to next statement
	bra	sco_loop

scoe_bad
	moveq	#ern4.else,d0		  ; bad else
	bra	sco_erset

;--------------------------------------------------------------
sco_endi
	move.l	sb_strtp(a6),a2 	 ; structure stack

	tst.w	st_type(a2)		 ; IF?
	bne.s	scei_bad		 ; ... no, bad

	move.l	st_else(a2),d0		 ; have we had an else?
	bne.s	scie_set
	move.l	st_def(a2),d0
scie_set
	add.w	#st.elen,a2
	move.l	a2,sb_strtp(a6)
	move.l	d0,a2
	move.l	d6,(a2) 		 ; jump to here
	bra	sco_loop

scei_bad
	moveq	#ern4.endi,d0		 ; bad endif
	bra	sco_mend

sco_ckst
	moveq	#-1,d0
	rts

;------------------------------------------------
sco_for
	bsr.l	sbc_vtype		 ; check that it can be variable
	subq.b	#nt.fp,d2		 ; floating point?
	bne.s	scof_int		 ; ... no
	move.w	#bo.forfp,(a0)+
	bsr.l	scof_link
	move.w	d3,(a0)+
	move.l	a0,-(sp)		 ; save address of here
	move.w	d3,-(sp)		 ; and name index
	addq.w	#2,a0			 ; this will be relative jump
scof_floop
	addq.w	#2,a3			 ; skip = (actually ,)
	jsr	sb_cmpopx		 ; compile expression for from
	cmp.w	#tkc.to,(a3)+		 ; assume to
	bne.s	scof_fval		 ; ... no to, it is value
	jsr	sb_cmpopx		 ; compile expression for to
	cmp.w	#tkc.step,(a3)		 ; assume no step
	beq.s	scof_fstep		  ; there is!!
	move.l	#bo.confp<<16+$0801,(a0)+ ; push 1
	move.l	#$40000000,(a0)+
	bra.s	scof_frng
scof_fstep
	addq.l	#2,a3
	jsr	sb_cmpopx		 ; expression for step
scof_frng
	move.w	#bo.ffpr,(a0)+		 ; set up range
	bra.s	scof_findx

scof_fval
	subq.l	#2,a3			 ; backspace
	move.w	#bo.ffpv,(a0)+		 ; set up value

scof_findx
	move.w	(sp),(a0)+		 ; set name index
	tst.w	(a3)			 ; end of statement?
	bgt.s	scof_floop
	bra.s	scof_sskip

scof_int
	subq.b	#nt.in-nt.fp,d2 	 ; integer?
	bne.l	scof_badn		 ; ... no

	move.w	#bo.forint,(a0)+
	bsr.s	scof_link
	move.w	d3,(a0)+
	move.l	a0,-(sp)		 ; save address of here
	move.w	d3,-(sp)		 ; and name index
	addq.w	#2,a0			 ; this will be relative jump
scof_iloop
	addq.w	#2,a3			 ; skip = (actually ,)
	clr.w	-(sp)			 ; say all constant
	jsr	sb_cmpopx_int		 ; compile expression for from
	st	(sp)
	cmp.w	#tkc.to,(a3)+		 ; assume to
	bne.s	scof_ival		 ; ... no to, it is value
	jsr	sb_cmpopx_int		 ; compile expression for to
	st	(sp)
	cmp.w	#tkc.step,(a3)		 ; assume no step
	beq.s	scof_istep		 ; there is!!
	move.l	#bo.conin<<16+1,(a0)+	 ; push 1
	bra.s	scof_irng
scof_istep
	addq.l	#2,a3
	jsr	sb_cmpopx_int		 ; expression for step
	st	(sp)
scof_irng
	tst.w  (sp)+			 ; all constant?
	bne.s	scof_ivrng		 ; ... no, variable range
	subq.l	#$8,a0			 ; back space
	move.w	#bo.fcir,-4(a0) 	 ; set constant integer range
	move.w	2(a0),(a0)+		 ; to constant
	move.w	4(a0),(a0)+		 ; step constant
	bra.s	scof_inext

scof_ivrng
	move.w	#bo.fintr,(a0)+ 	 ; set up range
	bra.s	scof_iindx

scof_ival
	subq.l	#2,a3			 ; backspace
	tst.w	(sp)+			 ; constant?
	bne.s	scof_ivval		 ; ... no
	move.w	#bo.fciv,-4(a0) 	 ; ... yes set constant
	bra.s	scof_inext

scof_ivval
	move.w	#bo.fintv,(a0)+ 	 ; set up value

scof_iindx
	move.w	(sp),(a0)+		 ; set name index
scof_inext
	tst.w	(a3)			 ; end of statement?
	bgt.s	scof_iloop		 ; ... no

scof_sskip
	move.w	#bo.fend,(a0)+		 ; end of ranges
	addq.l	#2,sp			 ; remove name index
	move.l	(sp)+,a2		 ; address of relative skip
	move.l	a0,d0
	sub.w	a2,d0			 ; relative slip
	move.w	d0,(a2) 		 ; set it
	bra	sco_loop

scof_link
	move.l	sb_unrlp(a6),d0 	 ; link into unresolved loop list
	move.l	a0,sb_unrlp(a6)
	move.l	d0,(a0)+
	rts

scof_badn
	move.l	#ern4.bfor,d0
	bra	sco_erset

;--------------------------------------------
sco_rep

; v .2.01 ---
	bsr.l	sbc_vtype		 ; get variable type
	subq.b	#nt.st,d2		 ; string ?
	beq	scof_badn		 ; ... yes, not allowed
	subq.w	#2,a3			 : a3 was increased in sbc_vtype
; ---
	move.w	#bo.rep,(a0)+		 ; standard repeat
	bsr.s	scof_link		 ; link in

sco_snindx				 ; label also called from somewhere else
	move.w	(a3)+,d1		 ;
	lsl.w	#nt.ishft,d1
	move.w	d1,(a0)+		 ; and index
	bra	sco_loop

;--------------------------------------------
sco_repu
	move.w	#bo.scrub8,(a0)+	 ; the next 2 long words are for comp
	bsr.s	scof_link
	move.l	d6,(a0)+
	bra	sco_loop

;-----------------------------------------------
sco_next
	move.w	#bo.next,(a0)+		 ; next
	bra.s	sco_snindx

;----------------------------------------------
sco_exit
	move.w	#bo.exit,(a0)+		 ; exit
	bra.s	sco_snindx

;-----------------------------------------------
sco_nxtu
	move.w	#bo.next,d1		 ; next
	bra.s	scox_unrlp

;-----------------------------------------------
sco_xitu
	move.w	#bo.exit,d1		 ; exit

scox_unrlp
	move.l	sb_unrlp(a6),d0 	 ; unresolved loops
	beq.s	scon_nloop
	move.l	d0,a2
	move.w	-2(a2),d0		 ; token
	assert	bo.forst,bo.forfp-2,bo.forint-4,bo.rep-6
	cmp.w	#bo.forst,d0
	blt.s	scon_urep		 ; it is unnamed REP
	move.w	d1,(a0)+
	move.w	4(a2),(a0)+		 ; it is named, set name
	bra	sco_loop

scon_urep
	move.w	#bo.goadd,(a0)+ 	 ; jump
	cmp.w	#bo.next,d1
	beq.s	scon_urepc		 ; ... next, look for start
	bsr.s	scof_link		 ; ... exit, link in
	bra	sco_loop

scon_urepl
	move.l	d0,a2			 ; next in list
	move.w	-2(a2),d0

scon_urepc
	cmp.w	#bo.scrub8,d0		 ; is next scrub 8
	bne.s	scon_urepn		 ; ... no
	move.l	4(a2),(a0)+		 ; ... yes, goto it
	bra	sco_loop

scon_urepn
	move.l	(a2),d0
	bne.s	scon_urepl

scon_nloop
	moveq	#ern4.unlp,d0		 ; unable to find unresolved loop
	bra	sco_erset

;----------------------------------------------
sco_endf
	lea	sb_unrlp(a6),a2 	 ; unresolved loop list
	move.w	#bo.endf,(a0)+		 ; end for
	move.w	(a3)+,d1
	lsl.w	#nt.ishft,d1
	move.w	d1,(a0)+		 ; name

	bra.s	scoef_rnext
scoef_rloop
	move.l	d0,a2
	cmp.w	4(a2),d1		 ; the right name?
	bne.s	scoef_rnext

	assert	bo.forst,bo.forfp-2,bo.forint-4
	move.w	-2(a2),d0		 ; type of loop
	sub.w	#bo.forst,d0
	blt.s	scoef_rnext		 ; ... not for
	subq.w	#bo.forint-bo.forst,d0
	bgt.s	scoef_rnext		 ; ... not for

	move.l	(a2),(a1)		 ; link past
	move.l	d6,d0
	addq.w	#1,d0
	move.l	d0,(a2) 		 ; and set end of loop address
	move.l	a1,a2			 ; move back in list
scoef_rnext
	move.l	a2,a1			 ; save this
	move.l	(a2),d0 		 ; and move to next
	bne.s	scoef_rloop
	bra	sco_loop

;--------------------------------------------------
sco_ndfu
	lea	sb_unrlp(a6),a2 	 ; unresolved loop list
	move.w	#bo.endf,(a0)+		 ; end for

scoef_nnext
	move.l	a2,a1			 ; save this
	move.l	(a2),d0 		 ; and move to next
	beq.s	scon_nloop		 ; ... none!
scoef_nloop
	move.l	d0,a2
	assert	bo.forst,bo.forfp-2,bo.forint-4
	move.w	-2(a2),d0		 ; type of loop
	sub.w	#bo.forst,d0
	blt.s	scoef_nnext		 ; ... not for
	subq.w	#bo.forint-bo.forst,d0
	bgt.s	scoef_nnext		 ; ... not for

scoef_sname
	move.l	(a2),(a1)		 ; link past
	move.l	d6,d0
	addq.w	#1,d0
	move.l	d0,(a2)+		 ; and set end of loop address
	move.w	(a2),(a0)+		 ; and for loop index
	bra	sco_loop

;------------------------------------------------------
sco_endr
	lea	sb_unrlp(a6),a2 	 ; unresolved loop list

	move.w	#bo.endr,(a0)+		 ; end repeat
	move.w	(a3)+,d1
	lsl.w	#nt.ishft,d1
	move.w	d1,(a0)+		 ; name

	bra.s	scoer_rnext
scoer_rloop
	move.l	d0,a2
	cmp.w	4(a2),d1		 ; the right name?
	bne.s	scoer_rnext

	cmp.w	#bo.rep,-2(a2)		 ; type of loop
	bne.s	scoer_rnext		 ; ... not rep

	move.l	(a2),(a1)		 ; link past
	move.l	d6,d0
	addq.w	#1,d0
	move.l	d0,(a2) 		 ; and set end of loop address
	move.l	a1,a2			 ; move back in list
scoer_rnext
	move.l	a2,a1			 ; save this
	move.l	(a1),d0 		 ; and move to next
	bne.s	scoer_rloop
	bra	sco_loop

;--------------------------------------------------
sco_ndru
	lea	sb_unrlp(a6),a2 	 ; unresolved loop list
	move.w	#bo.endr,(a0)+		 ; end repeat

scoer_nname
	move.l	d6,d1			 ; here
	addq.w	#1,d1			 ; and next statement

scoer_nnext
	move.l	a2,a1			 ; save this
scoer_nna1
	move.l	(a1),d0 		 ; and move to next
	beq.l	scon_nloop		 ; ... none!
scoer_nloop
	move.l	d0,a2
	assert	bo.forst,bo.forfp-2,bo.forint-4,bo.rep-6
	move.w	-2(a2),d0		 ; type of loop
	sub.w	#bo.forst,d0
	blt.s	scoer_unname		 ; ... unnamed repeat
	subq.w	#bo.rep-bo.forst,d0
	beq.s	scoef_sname		 ; ... named repeat, treat as FOR
	bra.s	scoer_nnext

scoer_unname
	move.l	(a2),(a1)		 ; link past
	add.w	#bo.forst-bo.scrub8,d0	 ; start of loop
	beq.s	scoer_undone

	move.l	d1,(a2) 		 ; set end of loop address in exit
	bra.s	scoer_nna1

scoer_undone
	move.w	#bo.goadd,-2(a0)	 ; go back to start of loop
	move.l	4(a2),(a0)+
	bra	sco_loop


sco_pstruct
	moveq	#0,d0
	move.l	sb_strtp(a6),a2 	 ; structure stack
	cmp.l	sb_strtl(a6),a2
	blt.s	sco_xstrct		 ; too many nested structures
	move.l	d0,-(a2)		 ; set empty structure
	move.l	d0,-(a2)
	move.l	a0,-(a2)		 ; definition
	move.w	d1,-(a2)		 ; name index
	move.w	d2,-(a2)		 ; type
	move.l	a2,sb_strtp(a6)
	rts

sco_xstrct
	addq.l	#4,sp
	moveq	#ern4.xstr,d0		 ; set message
	bra	sco_erset

;------------------------------------------------ SELect bits
sco_sel
	pea	4(a0)			 ; end of expression is just variable
	jsr	sb_cmpopx		 ; compile expression
	cmp.l	(sp)+,a0		 ; was it just variable
	bne.s	ssel_sexpr		 ; ... not 4 bytes
	cmp.w	#bo.pushu,-4(a0)	 ; was it just variable
	bgt.s	ssel_sexpr		 ; ... no

	subq.l	#2,a3			 ; ... yes, this is name index
	bsr	sbc_vtype		 ; get variable type
	move.w	#bo.selfp,d0
	cmp.w	#nt.in,d2		 ; integer?
	bne.s	ssel_set		 ; ... no
	addq.w	#bo.selint-bo.selfp,d0	 ; ... yes
	bra.s	ssel_set
ssel_sexpr
	move.w	#bo.selfp,d0		 ; expression is FP
	moveq	#nt.fp,d2
	moveq	#-1,d1			 ; expression

ssel_set
	move.w	d0,(a0)+		 ; set SELECT start and type
	add.w	#st.sel<<8,d2		 ; structure type
	bsr.s	sco_pstruct		 ; and setup structure stack
	move.w	2(a3),d0		 ; next clause
	sub.w	#tkc.onsl,d0
	blt.s	ssel_bads
	subq.w	#tkc.onsu-tkc.onsl,d0
	ble	sco_loop

ssel_bads
	moveq	#ern4.bsel,d0
	bra	sco_erset

;------------------------------------------------------
sco_onsl
	move.l	sb_strtp(a6),a2 	 ; structure stack
	move.w	st_indx(a2),d0
	cmp.w	(a3)+,d0		 ; correct name?
	beq.s	ssel_olink		 ; ... yes
	bra.s	ssel_bads

sco_onsu
	move.l	sb_strtp(a6),a2 	 ; structure stack
ssel_olink
	cmp.b	#st.sel,st_type(a2)	 ; select?
	bne.s	ssel_bads
	move.l	st_on(a2),d0		 ; previous on?
	beq.s	ssel_olset		 ; ... no
	move.l	d0,a1			 ; ... yes
	move.w	-(a0),d0		 ; move end of line
	move.l	#bo.goadd,(a0)+ 	 ; end of statement + GO TO past
	move.l	a1,(a0)+		 ; link back
	move.w	d0,(a0)+		 ; and end of statement / line
	bmi.s	ssel_lset		 ; it was on previous line
	addq.l	#1,d6
ssel_lset
	move.l	d6,2(a1)		 ; link to this one
ssel_olset
	move.l	a0,st_on(a2)		 ; set link to this
	move.w	#bo.on,(a0)+		 ; assume all constant ranges
	clr.l	(a0)+			 ; and space for our link


	move.w	st_type(a2),d0		 ; is it IF?
	beq.s	ssel_bads

	addq.w	#1,st_count(a2) 	 ; one more on

	cmp.w	#tkc.rmdr,(a3)		 ; next is remainder?
	bne.s	ssel_ranges		 ; ... no

	move.l	#$00040000,(a0)+	 ; offset 4, no ranges
	st	st_onrem(a2)		 ; set onrem
	addq.l	#2,a3
	bra	sco_loop

ssel_ranges
	addq.l	#4,a0			 ; hole for offset and count
	movem.l a0/a3,-(sp)		 ; save pointers
	clr.w	-(sp)			 ; and count

	sub.w	#st.sel<<8+nt.in,d0	 ; correct type?
	beq.l	ssel_intr		 ; ... yes, integer
	addq.w	#nt.in-nt.fp,d0 	 ; correct type?
	beq.s	ssel_fpr		 ; ... yes
	add.w	#$a,sp			 ; ... no, clean stack
	bra.s	ssel_bads

ssel_fpr
	addq.w	#1,(sp) 		 ; count ranges
	move.w	#bo.ofpr,(a0)+		 ; floating point range
	cmp.w	#tkc.fp6,(a3)+		 ; float?
	bne.l	ssel_expr		 ; no, give up
	move.w	(a3)+,d2
	move.l	(a3)+,d1		 ; from (or value)
	move.w	(a3),d0 		 ; following
	ble.s	ssel_fpval		 ; ... nothing, must be value
	cmp.w	#tkc.cmma,d0		 ; comma?
	beq.s	ssel_fpval		 ; ... yes, must be value
	cmp.l	#tkc.to<<16+tkc.fp6,(a3)+ ; TO fp?
	bne.l	ssel_expr		 ; ... no, must be expression
	move.w	d2,(a0)+
	move.l	d1,(a0)+		 ; from FP
	move.w	(a3)+,(a0)+
	move.l	(a3)+,(a0)+		 ; to FP
	bra.s	ssel_fpnrng

ssel_fpval
	move.l	d1,d0			 ; mantissa
	swap	d0
	ext.l	d0			 ; mant >>16
	asr.l	#7,d0			 ; mant >>23
	bge.s	ssel_fpvlim
	neg.l	d0			 ; ensure positive.
ssel_fpvlim
	bsr.s	ssel_fplim
	neg.l	d0
	bsr.s	ssel_fplim

ssel_fpnrng
	move.w	(a3),d0 		 ; following
	ble.l	ssel_rdone		 ; ... nothing, ranges done
	cmp.w	#tkc.cmma,d0		 ; comma?
	bne.s	ssel_expr		 ; ... no, expression ranges
	addq.l	#2,a3
	bra.s	ssel_fpr

ssel_fplim
	move.l	d1,d3			 ; value
	move.w	d2,d4
	sub.l	d0,d3			 ; adjustment
	bvs.s	ssel_fplo		 ; overflow, increase exponent
	add.l	d3,d3			 ; check if normalised
	bvs.s	ssel_fpls		 ; ... yes
	subq.w	#1,d4			 ; ... no, it should be now
	bpl.s	ssel_fplset		 ; ... it is
	asr.l	#1,d3			 ; shift back
	addq.w	#1,d4
	bra.s	ssel_fplset

ssel_fplo
	asr.l	#1,d3			 ; shift mantissa
	bchg	#31,d3			 ; and adjust sign
	addq.w	#1,d4			 ; ... adjust exponent
	bra.s	ssel_fplset

ssel_fpls
	roxr.l	#1,d3			 ; and shift mantissa back

ssel_fplset
	move.w	d4,(a0)+
	move.l	d3,(a0)+		 ; set fp
	rts

ssel_intr
	addq.w	#1,(sp) 		 ; count ranges
	move.w	#bo.ointr,(a0)+ 	 ; integer range
	cmp.w	#tkc.fp6,(a3)+		 ; float?
	bne.s	ssel_expr		 ; no, give up
	move.w	(a3)+,d2
	move.l	(a3)+,d1		 ; from (or value)
	jsr	qa_nintd
	bne.s	ssel_expr
	move.w	d1,(a0)+

	move.w	(a3),d0 		 ; following
	ble.s	ssel_intval		 ; ... nothing, must be value
	cmp.w	#tkc.cmma,d0		 ; comma?
	beq.s	ssel_intval		 ; ... yes, must be value
	cmp.l	#tkc.to<<16+tkc.fp6,(a3)+ ; TO fp?
	bne.s	ssel_expr		 ; ... no, must be expression
	move.w	(a3)+,d2
	move.l	(a3)+,d1		 ; from (or value)
	jsr	qa_nintd
	bne.s	ssel_expr
ssel_intval
	move.w	d1,(a0)+		 ; to INT

	move.w	(a3),d0 		 ; following
	ble.s	ssel_rdone		 ; ... nothing, ranges done
	cmp.w	#tkc.cmma,d0		 ; comma?
	bne.s	ssel_expr		 ; ... no, expression ranges
	addq.l	#2,a3
	bra.s	ssel_intr

ssel_expr
	movem.l 2(sp),a0/a3		 ; recover pointers
	clr.w	(sp)			 ; reset count
	move.w	#bo.onxpr,-$a(a0)	 ; change to expression ranges
ssel_exloop
	addq.w	#1,(sp)
	jsr	sb_cmpopx		 ; expression
	move.l	#bo.rts2<<16,(a0)+	 ; RTS
	move.w	(a3)+,d0
	cmp.w	#tkc.to,d0		 ; range?
	bne.s	ssel_exnxt		 ; ... no
	pea	-2(a0)			 ; pointer to offset
	jsr	sb_cmpopx		 ; TO expression
	move.l	(sp)+,a2
	move.l	a0,d0
	sub.l	a2,d0
	move.w	d0,(a2) 		 ; set offset past this expression
	move.w	#bo.rts,(a0)+		 ; RTS
	move.w	(a3)+,d0
ssel_exnxt
	tst.w	d0			 ; end of select?
	bgt.s	ssel_exloop		 ; ... no
	subq.l	#2,a3			 ; ... yes

ssel_rdone
	move.w	(sp)+,d0
	move.l	(sp)+,a2		 ; start of ranges
	addq.l	#4,sp
	move.l	d0,-(a2)		 ; set number of ranges + junk in offset
	move.l	a0,d0
	sub.l	a2,d0
	move.w	d0,(a2) 		 ; offset to code
	bra	sco_loop		 ; ON done

;-----------------------------------------------------------------
sco_ends
	move.l	sb_strtp(a6),a2 	 ; structure stack
	cmp.b	#st.sel,st_type(a2)	 ; correct type?
	bne.s	ssel_bend

	move.l	st_on(a2),d0		 ; previous on?
	beq.s	ssel_ends		 ; ... no, nothing at all to do
	move.l	d0,a1			 ; ... yes
	move.l	d6,2(a1)		 ; link to end
	move.w	#bo.selend,(a0)+	 ; end select!!

	move.w	st_count(a2),d0 	 ; number of ONs
	subq.w	#2,d0			 ; number of GOTOs - 1
	blt.s	ssel_ends
	move.l	d6,d1			 ; this statement
	addq.w	#1,d1			 ; next

ssel_endp
	move.l	-6(a1),d2		 ; previous goto
	move.l	d1,-6(a1)		 ; fill in this pointer to end
	move.l	d2,a1
	dbra	d0,ssel_endp

ssel_ends
	add.w	#st.elen,a2
	move.l	a2,sb_strtp(a6)
	bra.l	sco_loop

ssel_bend
	moveq	#ern4.ends,d0		 ; bad end select
	bra	sco_mend

;------------------------------------------------ WHEN
sco_when
	tst.b	sb_cmdl(a6)		 ; command line?
	bne.s	swh_cmd 		 ; ... yes, cancel
	cmp.w	#tkc.endw,2(a3) 	 ; end of statement, endwhen?
	beq.s	swh_nowherr		 ; ... yes

	move.w	#bo.wherr,(a0)+ 	 ; when error
	move.l	d6,d0
	swap	d0
	move.w	d0,(a0)+
	move.w	#bo.goadd,(a0)+ 	 ; skip past
	move.l	sb_strtp(a6),a2 	 ; structure stack
	cmp.b	#st.when,st_type(a2)	 ; type
	beq.s	swh_rcwh		 ; there is already a when

	move.w	#st.when<<8,d2		 ; structure type
	bsr.l	sco_pstruct		 ; and setup structure stack
	clr.l	(a0)+			 ; jump
	bra.l	sco_loop

swh_nowherr
swh_cmd
	move.w	#bo.nowherr,(a0)+	 ; no when error
	bra.l	sco_loop

swh_rcwh
	moveq	#ern4.rcwh,d0
	bra	sco_erset

;------------------------------------------------ END WHEN
sco_endw
	cmp.w	#bo.nowherr,-4(a0)	 ; empty?
	beq	sco_loop		 ; ... yes
	move.l	sb_strtp(a6),a2 	 ; structure stack
	cmp.b	#st.when,st_type(a2)	 ; type
	bne.s	sew_endw		 ; there is no when

	move.w	#bo.endwh,(a0)+
	move.l	d6,d1			 ; this statement
	addq.w	#1,d1			 ; next
	move.l	st_def(a2),a1		 ; definition(ish)
	move.l	d1,(a1)
	add.w	#st.elen,a2
	move.l	a2,sb_strtp(a6)
	bra	sco_loop

sew_endw
	moveq	#ern4.endw,d0
	bra	sco_mend		 ; misplaced end


;------------------------------------------------  GOTO GOSUB
sco_gosb
	move.w	#bo.gsadd,d4		 ; base type is gosub
	tst.b	sb_cmdl(a6)		 ; command line?
	beq.s	sgo_ckaddr		 ; ... no, could do address
	bra.s	sgo_linec		 ; ... yes, must be line

;------------------------------------------------
sco_goto
	move.w	#bo.goadd,d4		 ; base type is goto
	tst.b	sb_cmdl(a6)		 ; command line?
	bne.s	sgo_linec		 ; and always do line

sgo_ckaddr
	jsr	sb_cmpopx_ic		 ; check for integer const expression
	bra.s	sgo_line		 ; ... it is not

	move.w	d4,(a0)+		 ; goto
	move.w	d1,(a0)+		 ; line
	clr.w	(a0)+			 ; statement
	bra	sco_loop

sgo_linec
	addq.w	#bo.golinec-bo.goline,d4 ; use command line set
sgo_line
	addq.w	#bo.goline-bo.goadd,d4
	jsr	sb_cmpopx		 ; compile expression
	move.w	d4,(a0)+		 ; and goto it
	bra	sco_loop

;------------------------------------------------
sco_ongs
	jsr	sb_cmpopx		 ; get control expression
	tst.b	sb_cmdl(a6)		 ; is it command line?
	bne.s	sogs_linec		 ; ... yes
	move.l	a0,-(sp)		 ; save token address
	move.w	#bo.ogsadd,(a0)+	 ; it may be on gosub address
	move.l	d6,d0
	addq.w	#1,d0			 ; return to here
	move.l	d0,(a0)+		 ; return
	bra.s	sogo_check

sogs_linec
	move.w	#bo.ogslinec,(a0)+
	move.w	#bo.gslinec,d4		 ; goto type
	moveq	#-1,d0
	move.l	d0,(a0)+		 ; return to command line
	bra.s	sogo_ldo

;------------------------------------------------
sco_ongo
	jsr	sb_cmpopx		 ; get control expression
	tst.b	sb_cmdl(a6)		 ; is it command line?
	bne.s	sogo_linec		 ; ... yes
	move.l	a0,-(sp)		 ; save token address
	move.w	#bo.ogoadd,(a0)+	 ; it may be on go address
sogo_check
	movem.l a0/a3,-(sp)		 ; save these to backspace
	move.l	#bo.skip6<<16+bo.goadd,d4
	moveq	#0,d5
	move.w	d5,(a0)+

sogo_aloop
	addq.l	#2,a3			 ; skip dummy sep
	jsr	sb_cmpopx_ic		 ; integer constant?
	bra.s	sogo_back		 ; ... no
	addq.w	#1,d5			 ; one more destination
	move.l	d4,(a0)+		 ; gotos
	move.w	d1,(a0)+
	clr.w	(a0)+
	tst.w	(a3)			 ; end of statement?
	bgt.s	sogo_aloop

sogo_set
	move.l	(sp)+,a2		 ; address of count
	addq.l	#8,sp			 ; forget the rest
	move.w	d5,(a2) 		 ; set count
	bra	sco_loop

sogo_back
	movem.l (sp)+,a0/a3		 ; restore
	move.l	(sp)+,a1
	moveq	#bo.ogoline-bo.ogoadd,d0
	add.w	d0,(a1) 		 ; change operation
	add.w	d0,d4			 ; and goto type
	bra.s	sogo_ldo

sogo_linec
	move.w	#bo.ogolinec,(a0)+
	move.w	#bo.golinec,d4		 ; goto type

sogo_ldo
	move.l	a0,-(sp)
	moveq	#0,d5			 ; count destinations
	move.w	d5,(a0)+

sogo_lloop
	move.w	#bo.gorel,(a0)+ 	 ; skip this goto
	move.l	a0,-(sp)
	addq.w	#2,a0
	addq.l	#2,a3			 ; skip dummy sep
	jsr	sb_cmpopx		 ; compile expression
	move.l	(sp)+,a1
	bne.l	scc_rts4		 ; ... oops
	addq.w	#1,d5
	move.w	d4,(a0)+		 ; set this jump type
	move.l	a0,d0
	sub.l	a1,d0			 ; relative branch
	move.w	d0,(a1) 		 ; set
	tst.w	(a3)			 ; end of statement?
	bgt.s	sogo_lloop

	move.l	(sp)+,a2		 ; address of count
	move.w	d5,(a2) 		 ; set count
	bra	sco_loop

;------------------------------------------------
sco_data
	tst.b	sb_cmdl(a6)		 ; in command line?
	bne.s	sdt_dtcl		 ; ... yes
	tst.w	(a3)			 ; end of statement?
	ble.l	sco_loop		 ; ... yes, ignore it

	move.l	a0,sb_progp(a6)
	move.l	sb_dtstp(a6),a0 	 ; ... no, swap context

	move.l	d6,d2			 ; this line / statement
	swap	d2
	move.l	sb_pdata(a6),d5 	 ; previous data statement
	move.l	d5,d1
	swap	d1
	bne.s	sdt_line
	move.l	#bo.rts2<<16,(a0)+	 ; first RTS
sdt_line
	move.l	a0,-(sp)		 ; where to put length
	cmp.w	d2,d1			 ; the same line number?
	beq.s	sdt_stmt
	clr.w	d5			 ; first statement on line
	not.w	d2
sdt_stloop
	move.w	d2,(a0)+		 ; new line / statement
	addq.l	#8,sb_dttbe(a6) 	 ; one more statement in the table
	moveq	#0,d2
	addq.w	#1,d5
sdt_stmt
	cmp.w	d6,d5			 ; dummy statement required?
	blt.s	sdt_stloop		 ; ... yes

	move.l	d6,sb_pdata(a6) 	 ; save previous
	bra.s	sdt_itset

sdt_itloop
	move.l	a0,-(sp)
sdt_itset
	move.l	a0,-(sp)
	jsr	sb_cmpopx		 ; get expression
	bne.s	sdt_rts8
	move.l	#bo.rts2<<16,(a0)+	 ; RTS2 token
	move.l	a0,d0
	sub.l	(sp)+,d0		 ; length +4
	move.l	(sp)+,a2		 ; where to put it
	move.w	d0,-(a2)
	cmp.w	#tkc.cmma,(a3)+ 	 ; another?
	beq.s	sdt_itloop

	subq.l	#2,a3

sdt_done
	move.l	a0,sb_dtstp(a6) 	 ; swap context back
	move.l	sb_progp(a6),a0
	bra	sco_loop

sdt_rts8
	addq.l	#8,sp
	rts
sdt_dtcl
	moveq	#ern4.dtcl,d0		 ; data in command line is nonsense
	bra	sco_erset

;------------------------------------------------
sco_rest
	tst.w	(a3)			 ; end of statement?
	ble.s	srs_one 		 ; ... yes
	jsr	sb_cmpopx_int		 ; ... no, compile expression
	bra.s	srs_set
	bra.s	srs_set

srs_one
	move.l	#bo.conin<<16+1,(a0)+	 ; constant 1
srs_set
	move.w	#bo.restore,(a0)+	 ; restore
	bra	sco_loop


;--------------------------------------------------
sco_read
	tst.w	(a3)			 ; any parameters?
	ble.l	sco_loop		 ; ... no, ignore it

	tst.b	sb_cmdl(a6)		 ; compiling command line?
	beq.s	srd_do			 ; ... no
	st	sb_cmppg(a6)		 ; ... yes, program required as well
srd_do
	move.w	#bo.readf,(a0)+ 	 ; read first item
srd_prm
	move.w	(a3)+,d0		 ; must be name next
	move.w	(a3)+,d3
	lsl.w	#nt.ishft,d3		 ; name index
	sub.w	#tkc.refn,d0		 ; name?
	beq.s	srd_name		 ; ... yes
	addq.w	#tkc.refn-tkc.rfna,d0	 ; indexed?
	bne.s	srd_brds		 ; ... no

	bsr.l	sco_index
	bne.s	slv_rts
	tst.l	d1			 ; all but last index type zero
	bne.s	srd_brds
	move.w	#bo.asgel,(a0)+ 	 ; assign array element
	move.w	d3,(a0)+
	move.w	d4,(a0)+
	move.w	d5,(a0)+
	move.l	d2,(a0)+
	bra.s	srd_check

srd_name
	move.w	#bo.asgu,(a0)+		 ; assign unknown
	move.w	d3,(a0)+
srd_check
	move.w	(a3)+,d0
	ble.s	srd_done		 ; end
	cmp.w	#tkc.sepf,d0		 ; separator?
	blt.s	srd_brds		 ; ... no
	cmp.w	#tkc.sepl,d0
	bhi.s	srd_brds		 ; ... no
	move.w	#bo.read,(a0)+		 ; read another
	bra.s	srd_prm

srd_done
	move.w	#bo.readd,(a0)+ 	 ; end of read
	subq.w	#2,a3
	bra	sco_loop

srd_brds
	moveq	#ern4.brds,d0		      ; bad read statement
	bra	sco_erset

;------------------------------------------------
sco_letv
	move.w	(a3)+,-(sp)		 ; save name index
	addq.l	#2,a3			 ; skip =
	jsr	sb_cmpopx		 ; get expression
	bne.s	slv_rts2
	move.w	#bo.asgu,(a0)+		 ; assign unknown!
	move.w	(sp)+,d3
	lsl.w	#nt.ishft,d3
	move.w	d3,(a0)+		 ; ... this name
	bra	sco_loop

slv_rts2
	addq.l	#2,sp
slv_rts
	rts
;------------------------------------------------
sco_leta
	move.w	(a3)+,d3		 ; name index
	lsl.w	#nt.ishft,d3
sco_letax
	bsr.s	sla_do			 ; do assign to array
	beq	sco_loop
	rts

sla_do
	move.l	a0,-(sp)		 ; start of statement
	bsr.s	sco_index			ed
	move.l	(sp)+,a1		 ; start of statement
	bne.s	slv_rts
	tst.l	d1			 ; all but last should be zero
	bne.s	sin_asar
	move.l	sp,a2
	move.l	d2,-(sp)
	movem.w d3/d4/d5,-(sp)
	move.w	#bo.asgel,-(sp) 	 ; assign array element

sla_save
	move.w	-(a0),-(sp)		 ; save assign
	cmp.l	a1,a0			 ; back to start?
	bgt.s	sla_save

	move.l	a2,-(sp)		 ; top of stack

	addq.l	#2,a3			 ; ought to be =
	jsr	sb_cmpopx		 ; compile expression

	move.l	(sp)+,a2
sla_rest
	move.w	(sp)+,(a0)+		 ; restore assign
	cmp.l	a2,sp			 ; all restored?
	blt.s	sla_rest

	tst.l	d0
	rts

sin_asar
	moveq	#ern4.asar,d0
	bra.s	sin_erset
sin_xind
	moveq	#ern4.xind,d0
sin_erset
	bra	sco_erset
;+++
; This little routine compiles an index list.
; It returns NZ if it cannot be compiled.
;	d1  r	index mask not including final index
;	d2  r	index mask
;	d4  r	number of indices
;	d5  r	number of values
;---
sb_cmpop_indx
sco_index
	addq.l	#2,a3			 ; skip bracket
	moveq	#0,d2
	moveq	#0,d4			 ; clear array attributes
	moveq	#0,d5
	cmp.w	#tkc.rpar,(a3)		 ; any index at all?
	bne.s	sin_loop		 ; ... yes
	addq.l	#2,a3			 ; skip right bracket
	moveq	#0,d0
	rts

sin_loop
	cmp.w	#8*4,d4 		 ; more than eight indices?
	bhs.s	sin_xind
	move.w	(a3),d0 		 ; start of next index
	cmp.w	#tkc.rpar,d0		 ; no index?
	beq.s	sin_maxl		 ; ... no
	cmp.w	#tkc.cmma,d0		 ; missing index?
	beq.s	sin_maxl		 ; ... yes
sin_cktoi
	cmp.w	#tkc.to,d0		 ; TO?
	bne.s	sin_from		 ; ... no, get from index
	addq.w	#2,a3
	move.w	(a3),d0 		 ; start of to index
	cmp.w	#tkc.rpar,d0		 ; no index?
	beq.s	sin_maxl		 ; ... no
	cmp.w	#tkc.cmma,d0		 ; missing index?
	bne.s	sin_nlto		 ; ... no
sin_maxl
	moveq	#%001,d1		 ; whole range
	bra.s	sin_next
sin_nlto
	jsr	sb_cmpopx_int		 ; get index
	move.l	d0,d0			 ; do not care if integer or not
	bne.s	sin_eind
	moveq	#%101,d1		 ; range to
	addq.w	#1,d5			 ; number of values
	bra.s	sin_next

sin_from
	jsr	sb_cmpopx_int		 ; get index
	move.l	d0,d0			 ; do not care if integer or not
	bne.s	sin_eind
	cmp.w	#tkc.to,(a3)		 ; value to?
	bne.s	sin_value		 ; ... no single value
	addq.w	#2,a3
	move.w	(a3),d0 		 ; start of next index
	cmp.w	#tkc.rpar,d0		 ; no index?
	beq.s	sin_vto 		 ; ... no
	cmp.w	#tkc.cmma,d0		 ; missing index?
	bne.s	sin_range		 ; ... no
sin_vto
	moveq	#%011,d1		 ; range from
	addq.w	#1,d5			 ; number of values
	bra.s	sin_next
sin_range
	jsr	sb_cmpopx_int		 ; get index
	move.l	d0,d0			 ; do not care if integer or not
	bne.s	sin_eind
	moveq	#%111,d1		 ; range from to
	addq.w	#2,d5			 ; number of values
	bra.s	sin_next

sin_value
	moveq	#%000,d1		 ; no range
	addq.w	#1,d5			 ; one value
sin_next
	lsl.l	d4,d1			 ; put index desc in the right place
	exg	d1,d2			 ; keep previous range
	or.l	d1,d2
	addq.w	#4,d4
	move.w	(a3)+,d0		 ; next item
	cmp.w	#tkc.cmma,d0		 ; another index?
	beq	sin_loop		 ; ... yes
	lsr.w	#2,d4			 ; number of indices
	cmp.w	#tkc.rpar,d0		 ; OK?
	bne.s	sin_eind
	moveq	#0,d0
	rts

sin_eind
	moveq	#ern4.eind,d0
	bra	sco_erset


;-------------------- Procedure Call
sco_call
	bsr.l	sbc_nmuse
	ble.s	sco_callp
sco_callx
	move.l	nt_value(a6,d0.l),a2	 ; line number / address of routine
	cmp.b	#nt.mcprc,d2
	beq.s	sco_callc
	cmp.b	#nt.sbprc,d2
	beq.s	sco_calls

sco_callp
	move.w	#bo.pcall,(a0)+ 	 ; generic call
	move.w	d3,-(sp)
	move.w	d3,(a0)+		 ; index
	bsr.s	scc_cnxtp
	move.w	#bo.dopr,(a0)+
	move.w	(sp)+,(a0)+		 ; and index
	bra	sco_loop

sco_calls
	tst.b	sb_cmdl(a6)
	bne.s	sco_callsc

	move.w	#bo.spcall,(a0)+	 ; sb proc call
	bsr.s	scc_callp
	move.w	#bo.dospr,(a0)+ 	 ; do it
	bra	sco_loop

sco_callsc
	move.w	#bo.spcallc,(a0)+	 ; sb proc call from command line
	bsr.s	scc_callp
	move.w	#bo.dosprc,(a0)+	 ; do it
	bra	sco_loop

sco_callc
	cmp.l	#'READ',4(a2)		 ; is it read?
	beq	sco_read		 ; ... yes, special
	move.w	#bo.cpcall,(a0)+	 ; mc proc call
	move.w	d3,(a0)+		 ; index
	bsr.s	scc_cnxtp
	move.w	#bo.docpr,(a0)+ 	 ; do it
	bra	sco_loop

scc_callp
	move.l	a2,(a0)+		 ; address / index
scc_cnxtp
	tst.w	(a3)			 ; end of statement?
	ble.s	scc_rts
	jsr	sb_cmpopp		 ; get parameter
	beq.s	scc_cnxtp		 ; ... ok

scc_rts4
	addq.l	#4,sp			 ; skip return
scc_rts
	rts

;----------------------------------------------
sco_ambs
	bsr.s	sbc_nmuse
	beq	sco_letax		 ; type is value
	bgt	sco_callx		 ; type is procedeure
	addq.b	#us.proc,d2		 ; ambiguous procedure?
	bne	sco_letax		 ; ... no

	move.w	#bo.ambs,(a0)+		 ; ambiguous statement
	move.w	d3,(a0)+		 ; with this index
	move.l	a0,-(sp)		 ; save addresses
	move.l	a3,-(sp)
	clr.w	(a0)+

	move.w	#bo.pcall,(a0)+ 	 ; generic call
	move.w	d3,-(sp)
	move.w	d3,(a0)+		 ; index
	bsr.s	scc_cnxtp
	move.w	#bo.dopr,(a0)+
	move.w	(sp)+,d3		 ; and index
	move.w	d3,(a0)+
	move.l	(sp)+,a3		 ; backspace
	move.l	(sp)+,a2		 ; address of rel goto
	move.w	#bo.gorel,(a0)+ 	 ; skip past next
	move.l	a0,-(sp)		 ; saving address
	clr.w	(a0)+
	move.l	a0,d0
	sub.l	a2,d0
	move.w	d0,(a2) 		 ; set relative jump

	bsr	sla_do			 ; compile assignment
	move.l	(sp)+,a2		 ; address of gorel param
	bne.s	scc_rts 		 ; oops

	move.l	a0,d0
	sub.l	a2,d0
	move.w	d0,(a2) 		 ; set gorel
	bra	sco_loop


; This little routine sets the usage of a name
;
;	returns d2.b (and ccr)	-ve: ambiguous (-us.fun or -us.proc)
;				  0: variable or array
;				+ve: nt.xxprc nt.xxfun
;
;		also	d0	offset of nt entry from (a6)
;			d1	parser index
;			d3	interpreter index
;			a1	pointer to usage table entry
sbc_nmuse
	moveq	#0,d3
	move.w	(a3)+,d3		 ; name table index
	move.l	d3,d1			 ; parser index in d1.l
	lsl.l	#nt.ishft,d3		 ; inter index in d3.l
	move.l	d3,d0
	add.l	sb_nmtbb(a6),d0 	 ; offset in d0
	move.l	sb_nutbb(a6),a1
	add.l	d1,a1			 ; usage table
	moveq	#us.fun+us.proc,d2	 ; proc or fun?
	and.b	(a1),d2
	beq.s	sbcn_rts		 ; ... no, must be var or array only

	cmp.b	(a1),d2 		 ; just proc or fun?
	beq.s	sbcn_pf 		 ; ... yes, no other usage
	neg.b	d2			 ; ambiguous
sbcn_rts
	rts
sbcn_pf
	move.b	nt_nvalp(a6,d0.l),d2	 ; proc / fun usage
	rts



	end
