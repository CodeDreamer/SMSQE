; SBAS_IPROCB - PROC/FN SBASIC Call	  1994 Tony Tebby

	section sbas

	xdef	bo_dospr	    ; SBASIC procedure calls
	xdef	bo_dosprc

	xdef	bo_formp	    ; formal parameter and locals
	xdef	bo_locvar
	xdef	bo_diml

	xdef	bo_return
	xdef	bo_retexp

	xref	bo_stop

	xref	sb_alard
	xref	sb_dodim

	xref	sb_iloop
	xref	sb_iloop5
	xref	sb_ienimp
	xref	sb_ierset
	xref	sb_ierror
	xref	sb_istopok

	xref	sb_error

	xref	sb_clrprm
	xref	sb_rnt08
	xref	sb_aldat
	xref	sb_aldat8
	xref	sb_redat

	include 'dev8_keys_sbasic'
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

;--------------------------------- routine call
bo_dospr
	move.l	sb_retsp(a6),a2
	move.l	a4,rt_ret(a6,a2.l)	 ; return address
	move.l	rt_def(a6,a2.l),a4	 ; routine address
	jmp	(a5)

;--------------------------------- routine call from cammand line
bo_dosprc
	move.l	sb_retsp(a6),a2
	st	rt_ret(a6,a2.l) 	 ; return to command line
	move.w	rt_def(a6,a2.l),d0	 ; routine line number
	move.w	d0,sb_cline(a6)
	move.b	#1,sb_cstmt(a6)
	sf	sb_cont(a6)
	move.w	#sb.cont,sb_actn(a6)	 ; continue from here
	jmp	sb_istopok


;--------------------------------- formal parameter and locals
bo_formp
	move.l	sb_retsp(a6),a2
	move.l	rt_parm(a6,a2.l),d5	 ; base of parameters
	move.l	rt_local(a6,a2.l),d2	 ; base of locals
	move.l	a4,d0
	neg.l	d0
	move.l	d0,rt_def(a6,a2.l)	 ; save def
	move.w	(a4)+,d4		 ; number of formal parameters
	bra.s	bfp_eloop		 ; ... none, process locals

bfp_loop
	cmp.l	d2,d5			 ; all actual parameters done?
	bge.s	bfp_dparm

	moveq	#0,d3
	move.w	(a4)+,d3		 ; formal parameter

	move.w	nt_usetp(a3,d5.l),d0	 ; swap values
	move.w	nt_usetp(a3,d3.l),nt_usetp(a3,d5.l)
	move.w	d0,nt_usetp(a3,d3.l)
	move.l	nt_value(a3,d5.l),d0
	move.l	nt_value(a3,d3.l),nt_value(a3,d5.l)
	move.l	d0,nt_value(a3,d3.l)

;	 bge.s	 bfp_nparm		  ; not dummy
;$$$$$$ code here to pre-init???? - not if compatible with SuperBASIC
;bfp_nparm
	addq.l	#8,d5			 ; next actual parameter
bfp_eloop
	dbra	d4,bfp_loop		 ; another formal parameter?

	cmp.l	d2,d5			 ; all actual parameters done
	beq.s	blv_do			 ; ... yes

	move.l	a3,-(sp)
	move.l	a5,-(sp)
	move.l	sb_nmtbb(a6),a3 	 ; name table base
	move.l	a3,a5
	add.l	d5,a3			 ; base of spare parameters
	add.l	d2,a5			 ; top of spare parameters
	jsr	sb_clrprm		 ; clear spare parameters
	move.l	(sp)+,a5
	move.l	(sp)+,a3
	bra.s	blv_do

bfp_dparm
	moveq	#0,d3			 ; single value
	move.w	(a4)+,d3		 ; formal parameter
	moveq	#-1,d7
	bsr.s	blv_set 		 ; dummy formal parameter
	dbra	d4,bfp_dparm		 ; another formal parameter?

blv_do
	move.l	d5,rt_local(a6,a2.l)	 ; new top of parameters

blv_loop
	move.l	d5,rt_topn(a6,a2.l)	 ; save top of names
	move.w	#-bo.locvar,d0
	moveq	#-1,d7			 ; assume local var
	add.w	(a4)+,d0
	beq.s	blv_var 		 ; ... it is local var

	subq.w	#bo.diml-bo.locvar,d0	 ; dimension?
	bne.l	blv_done
	movem.l d5/a2,-(sp)
	jsr	(a5)			 ; ... yes, do indexes
	movem.l (sp)+,d5/a2
	moveq	#0,d7			 ; flag array

blv_var
	moveq	#0,d3
	move.w	(a4)+,d3		 ; local
	move.w	d3,d7			 ; and name
	pea	blv_loop

;
; set dummy parameter / local
;
;	d3 c  p pointer to formal parameter / local
;	d5 c  u pointer to top of parameter / local frame
;	a4 c  u pointer to pointer to name index
;
blv_set
	move.l	d5,d0
	addq.w	#8,d5
	add.l	sb_nmtbb(a6),d0
	cmp.l	sb_nmtbp(a6),d0 	 ; top of table?
	beq.s	blv_chkt		 ; ... yes
	lea	(a6,d0.l),a0
	tst.w	(a0)			 ; empty?
	beq.s	blv_stable		 ; ... yes
	bra.l	sb_error		 ; ... no

blv_chkt
	cmp.l	sb_nmtbt(a6),d0 	 ; full?
	blt.s	blv_stptr		 ; no
	movem.l d3/a1,-(sp)
	jsr	sb_rnt08		 ; allocate another bit
	movem.l (sp)+,d3/a1
	move.l	sb_nmtbb(a6),a3 	 ; reset name table pointer
	add.l	a6,a3
	move.l	sb_nmtbp(a6),d0

blv_stptr
	lea	(a6,d0.l),a0
	addq.l	#8,d0
	move.l	d0,sb_nmtbp(a6) 	 ; table is larger now

blv_stable
	move.w	nt_usetp(a3,d3.l),(a0)+  ; name type
	move.w	d7,(a0)+		 ; pointer to local or -1
	move.l	nt_value(a3,d3.l),(a0)+  ; value pointer

	move.l	a6,a0
	add.w	nt_name(a3,d3.l),a0	 ; pointer to name
	add.l	sb_nmlsb(a6),a0
	moveq	#0,d1
	move.b	(a0),d1 		 ; length of name
	add.l	d1,a0
	move.b	(a0),d2 		 ; last character
	tst.l	d7			 ; array?
	bpl.s	blv_array

blv_alloc
	move.l	a2,-(sp)
	jsr	sb_aldat8		 ; initial allocation
	move.l	(sp)+,a2

	sub.b	#'$',d2 		 ; string?
	beq.s	blv_str 		 ; ... yes
	subq.b	#'%'-'$',d2		 ; integer?
	beq.s	blv_int 		 ; ... yes

blv_fp
	move.w	#nt.varfp,nt_usetp(a3,d3.l) ; set type of variable
	move.l	a0,nt_value(a3,d3.l)	 ; normal value
	clr.l	(a0)+			 ; zero float
	move.l	#dt.flfp,(a0)		 ; and flags
	rts

blv_str
	move.w	#nt.varst,nt_usetp(a3,d3.l) ; set type of variable
	move.l	#$000800ff,(a0)+	 ; null string flags
	move.l	a0,nt_value(a3,d3.l)
	clr.l	(a0)
	rts

blv_int
	move.w	#nt.varin,nt_usetp(a3,d3.l) ; set type of variable
	move.l	a0,nt_value(a3,d3.l)	 ; normal value
	clr.w	(a0)+			 ; zero int
	move.l	#dt.flint,(a0)		 ; and flags
	rts

blv_array
	sub.b	#'$',d2 		 ; string?
	beq.s	blv_stra		 ; ... yes
	subq.b	#'%'-'$',d2		 ; integer?
	beq.s	blv_inta		 ; ... yes

blv_fpa
	move.w	#nt.arrfp,nt_usetp(a3,d3.l) ; set type of variable
	bra.s	blv_dim

blv_stra
	move.w	#nt.arrst,nt_usetp(a3,d3.l) ; set type of variable
	bra.s	blv_dim

blv_inta
	move.w	#nt.arrin,nt_usetp(a3,d3.l) ; set type of variable
blv_dim
	movem.l d5/a2,-(sp)
	lea	blv_dexit,a5
	jmp	sb_dodim		 ; do dimension
blv_dexit
	lea	sb_iloop5,a5
	movem.l (sp)+,d5/a2
	rts


blv_done
	subq.l	#2,a4
	jmp	(a5)

bo_locvar
bo_diml
	moveq	#ern4.bloc,d0
	jmp	sb_ierset		  ; misplaced local


;----------------------------------
bo_retexp
	move.l	sb_retsp(a6),a2 	 ; return stack
	cmp.l	sb_retsb(a6),a2 	 ; any?
	beq.s	bor_bad
	move.b	rt_type(a6,a2.l),d0	 ; return type
	subq.b	#rt.fun,d0		 ; the expected type
	bne.s	bor_bad
	tst.w	(a1)			 ; is it pointer to string on stack?
	bpl.s	bor_pf			 ; ... no
	addq.l	#2,a1
	move.l	(a1)+,a0		 ; pointer to string
	moveq	#0,d1
	move.w	(a0),d1 		 ; the length
	jsr	sb_alard		 ; check for room

	move.w	(a0)+,d0
	add.w	d0,a0			 ; the end of the string
	lsr.w	#1,d0			 ; length by two
	bcc.s	bor_copy		 ; no spare at end
	subq.w	#1,a0
	move.w	(a0),-(a1)		 ; the odd byte at the end
bor_copy
	move.w	-(a0),-(a1)
	dbra	d0,bor_copy		 ; include the count
	move.w	#ar.strng,-(a1) 	 ; put string on stack
	bra.s	bor_pf

bor_bad
	moveq	#ern4.bret,d0
	jmp	sb_ierset

;--------------------------------- returns
bo_return
	move.l	sb_retsp(a6),a2 	 ; return stack
	cmp.l	sb_retsb(a6),a2 	 ; any?
	beq.s	bor_bad
	move.b	rt_type(a6,a2.l),d0	 ; return type
	subq.b	#rt.proc,d0		 ; the expected type
	bne.l	bor_gsub		 ; from gosub?

bor_pf
	move.l	rt_ret(a6,a2.l),-(sp)	 ; return address
	lea	-rt.pfsize(a2),a0
	move.l	a0,sb_retsp(a6) 	 ; remove stack frame

	move.l	rt_parm(a6,a2.l),d7	 ; base of actual parameters to clear
	move.l	rt_local(a6,a2.l),d4	 ; base of locals

	move.l	sb_nmtbb(a6),d1
	move.l	rt_topn(a6,a2.l),d5	 ; top of locals
	move.l	d1,d0
	add.l	d5,d0
	cmp.l	sb_nmtbp(a6),d0 	 ; are parameters at top
	bne.s	bor_lvlend
	add.l	d7,d1
	move.l	d1,sb_nmtbp(a6) 	 ; ... yes, remove them
	bra.s	bor_lvlend

bor_lvloop
	subq.l	#nt.len,d5
	moveq	#0,d2
	move.w	nt_name(a3,d5.l),d2	 ; local variable
	bsr.l	bor_clrval		 ; clear it
	move.w	nt_usetp(a3,d5.l),nt_usetp(a3,d2.l) ; restore values
	move.l	nt_value(a3,d5.l),nt_value(a3,d2.l)
	clr.w	nt_usetp(a3,d5.l)	  ; clear parameter

bor_lvlend
	cmp.l	d4,d5			 ; bottom of locals yet?
	bgt.s	bor_lvloop		 ; ... no

	move.l	rt_def(a6,a2.l),d0
	neg.l	d0			 ; address of definition

	move.l	d0,a4			 ; formal parameter definition
	move.w	(a4)+,d4		 ; number of formal parameters
	add.w	d4,a4
	add.w	d4,a4			 ; start at other end
	move.l	d5,d3
	moveq	#0,d2
	bra.s	bor_swlend

bor_swloop
	subq.l	#nt.len,d3		 ; next nt entry
	move.w	-(a4),d2		 ; next formal parameter

	move.w	nt_usetp(a3,d3.l),d0
	move.w	nt_usetp(a3,d2.l),nt_usetp(a3,d3.l)
	move.w	d0,nt_usetp(a3,d2.l)
	move.l	nt_value(a3,d3.l),d0
	move.l	nt_value(a3,d2.l),nt_value(a3,d3.l)
	move.l	d0,nt_value(a3,d2.l)
bor_swlend
	dbra	d4,bor_swloop

	move.w	-(a4),d4		 ; number of parameters
	bra.s	bor_fplend

bor_fploop
	subq.l	#nt.len,d5		 ; next nt entry
	moveq	#0,d3
	move.w	nt_name(a3,d5.l),d3	 ; name of actual parameter
	bmi.s	bor_fpex		 ; expression, null
	lsl.l	#nt.ishft,d3
	cmp.b	#nt.arr,nt_nvalp(a3,d3.l) ; actual parameter is array?
	beq.s	bor_fparray		  ; ... yes

	move.l	nt_value(a3,d5.l),nt_value(a3,d3.l) ; set value
	move.w	#$0f0f,d0
	and.w	nt_usetp(a3,d5.l),d0
	move.w	d0,nt_usetp(a3,d3.l)	   ; reset type (FOR/REP)
	bra.s	bor_fpnext

bor_fparray
	cmp.b	#nt.arr,nt_nvalp(a3,d5.l) ; parameter is (sub)array?
	bne.s	bor_fpnext		  ; ... no, no descriptor to return

bor_fpex
	move.l	d5,d2
	bsr.s	bor_clrval		 ; clear (local) value

bor_fpnext
	clr.w	nt_usetp(a3,d5.l)	 ; clear entry
bor_fplend
	dbra	d4,bor_fploop

	move.l	(sp)+,d7
bor_cont
	move.l	d7,a4			 ; this will not be set by STOP!!
	bgt	sb_iloop
	move.b	#1,sb_cmdl(a6)		 ; return to command line
	jmp	bo_stop 		 ; stop

bor_gsub
	bgt.l	bor_bad 		 ; was function
	lea	-rt.gssize(a2),a0
	move.l	a0,sb_retsp(a6) 	 ; remove stack frame
	move.l	rt_ret(a6,a2.l),d7	 ; return address
	bra.s	bor_cont

; clear (a3,d2.l)

bor_clrval
	move.l	nt_value(a3,d2.l),d0
	ble.s	brc_rts 		 ; no value
	move.l	d0,a0

	st	nt_value(a3,d2.l)
	cmp.b	#nt.arr,nt_nvalp(a3,d2.l) ; var, array or for/rep
	blt.s	brc_var 		 ; var
	bgt.s	brc_forrep

	assert	dt_allc,-4
	move.l	-(a0),d1		 ; length and base
	bne.l	sb_redat
brc_rts
	rts				 ; dummy array!

brc_var
	moveq	#8,d1			 ; assume 8 bytes

	moveq	#$f,d0			 ; parameter type
	and.w	nt_usetp(a3,d2.l),d0
	cmp.w	#nt.st,d0		 ; string
	bgt.l	sb_redat		 ; ... no

	subq.l	#-dt_stalc,a0
	move.w	(a0),d1 		 ; amount to release
	jmp	sb_redat		 ; return data space

brc_forrep
	add.w	#dt_frbase,a0
	move.l	(a0),d1 		 ; allocated size
	jmp	sb_redat

	end
