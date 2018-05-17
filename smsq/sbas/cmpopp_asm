; SBAS_CMPOPP - Convert Parameter(s) to Operations   V2.00     1994 Tony Tebby

	section sbas

	xdef	sb_cmpopp
	xdef	sb_cmpop_fn

	xref	sb_cmpopx
	xref	sb_cmpop_indx
	xref	sb_ernimp

	include 'dev8_keys_sbasic'
	include 'dev8_smsq_sbas_comp_keys'
	include 'dev8_smsq_sbas_inter_keys'
	include 'dev8_mac_assert'

;+++
; SBASIC convert function / array reference to operation tokens
;
;	d1   s
;	d6 c  p line number / statement number
;	a0 c  u pointer to program operations
;	a2  r	pointer to operation token
;	a3 c  u pointer to compiler operations
;	a5    p
;	a6 c  p pointer to SuperBASIC variables
;
;	Status return standard
;---
sb_cmpop_fn
scf.reg reg	d2/d3/d4/d5
	movem.l scf.reg,-(sp)
	moveq	#0,d3
	move.w	(a3)+,d3		 ; name index
	move.l	sb_nutbb(a6),a1
	bra.s	scf_do
sb_cmpop_fnd3
	movem.l scf.reg,-(sp)
scf_do
	moveq	#0,d7			 ; assume array
	add.w	d3,a1
	lsl.w	#nt.ishft,d3
	btst	#us..fun,(a1)		 ; can it be function?
	beq.s	scf_rarr		 ; ... no, reference array

	moveq	#2,d7			 ; assume mc function
	move.l	d3,d0
	add.l	sb_nmtbb(a6),d0
	cmp.b	#nt.mcfun,nt_nvalp(a6,d0.l)
	beq.s	scf_ckamb		 ; it is mc fun
	moveq	#4,d7			 ; assume sbasic
	tst.b	sb_cmdl(a6)		 ; command line?
	beq.s	scf_ckamb		 ; ... no
	moveq	#6,d7			 ; sbasic from command line

scf_ckamb
	cmp.b	#us.fun,(a1)		 ; is guaranteed function?
	beq.s	scf_dofn		 ; ... yes

scf_rarr
	move.l	a3,-(sp)
	move.l	a0,-(sp)		 ; save pointers before process index
	move.l	d7,-(sp)
	jsr	sb_cmpop_indx		 ; get indices
	bne.s	scf_rarr_fail		 ; ... bad
	move.l	(sp)+,d7		 ; was it array only?
	bne.s	scf_amba		 ; ... no

	move.l	a0,a2			 ; address of token
	move.w	#bo.pusha,(a0)+
	move.w	d3,(a0)+		 ; index
	move.w	d4,(a0)+		 ; number of indices
	move.w	d5,(a0)+
	move.l	d2,(a0)+
	addq.l	#8,sp			 ; clean up the stack
	bra.s	scf_exit

scf.amb equ	8		; internal key for first ambiguous
scf_prepop
	dc.w		 0,bo.cfnref,bo.sfnref,bo.sfnrefc
	dc.w	bo.cfnaref,bo.cfnaref,bo.sfnaref,bo.sfnarefc
scf_postop
	dc.w	  bo.pusha,bo.pushcf,bo.pushsf,bo.pushsfc
	dc.w	bo.pushcfa,bo.pushcfa,bo.pushsfa,bo.pushsfac

scf_rarr_fail
	move.l	(sp)+,d7		 ; this will become ambiguous!!
	moveq	#-1,d4			 ; number of indices is neg!!

scf_amba
	move.l	(sp)+,a0		 ; ... ambiguous, restore pointers
	move.l	(sp)+,a3
	move.l	d2,-(sp)
	move.w	d5,-(sp)
	move.w	d4,-(sp)
	move.w	d3,-(sp)

	assert	bo.stpin-bo.stpar,bo.cfpin-bo.cfpar,bo.expin-bo.expar
	add.l	#(bo.expin-bo.expar)<<16+scf.amb,d7 ; ambiguous tokens

scf_dofn
	move.w	scf_prepop(pc,d7.w),(a0)+ ; introductory code
	move.l	d3,d0
	add.l	sb_nmtbb(a6),d0
	move.l	nt_value(a6,d0.l),(a0)+  ; address
	move.l	d7,-(sp)
	addq.w	#2,a3			 ; skip left bracket
	tst.w	(sp)
	beq.s	scf_elp
	move.w	d3,(a0)+		 ; for ambiguous, add name index
	bra.s	scf_elp

scf_lp
	subq.l	#2,a3
	move.w	(sp),d0 		 ; par or pin
	bsr.s	sb_cmpoppi		 ; get parameter or index
	bne.s	scf_done
scf_elp
	cmp.w	#tkc.rpar,(a3)+ 	 ; end of params?
	bne.s	scf_lp

	moveq	#0,d0			 ; ... ok

scf_done
	move.l	a0,a2			 ; address of token
	move.l	(sp)+,d7
	move.w	scf_postop(pc,d7.w),(a0)+ ; push result
	subq.w	#scf.amb,d7		 ; ambiguous?
	blt.s	scf_exit
	move.w	(sp)+,(a0)+		 ; name index
	move.l	(sp)+,(a0)+		 ; number of indices and values
	move.l	(sp)+,(a0)+		 ; bit map
scf_exit
	movem.l (sp)+,scf.reg
	tst.l	d0
	rts

;+++
; SBASIC convert parameter to operation tokens
;
;	d1   s
;	d2   s
;	d3   s
;	d5   s
;	d6 c  p line number / statement number
;	a0 c  u pointer to program operations
;	a2   s
;	a3 c  u pointer to compiler operations
;	a6 c  p pointer to SuperBASIC variables
;
;	Status return standard
;---
sb_cmpopp
	moveq	#0,d0
;+++
; SBASIC convert parameter / index to operation tokens
;
;	d0 c s	0 (parameter), bo.xxpin-bo.xxpar (parameter or index)
;	d1   s
;	d2   s
;	d3   s
;	d6 c  p line number / statement number
;	a0 c  u pointer to program operations
;	a2   s
;	a3 c  u pointer to compiler operations
;	a6 c  p pointer to SuperBASIC variables
;
;	Status return standard
;---
sb_cmpoppi
	moveq	#0,d1			 ; no hash
	cmp.w	#tkc.hash,(a3)		 ; hash?
	bne.s	scp_parm		 ; no, get parameter
	addq.l	#2,a3
	bset	#nt..hash,d1		 ; hash
scp_parm
	move.w	d1,-(sp)		 ; save hash
	move.w	d0,-(sp)		 ; and bo.xxpin-bo.xxpar
	moveq	#-tkc.lpar,d0
	add.w	(a3)+,d0		 ; next item is left bracket?
	beq.l	scp_expr		 ; yes, expression
	blt.l	scp_null		 ; ... null

	subq.w	#tkc.rfna-tkc.lpar,d0	 ; could be array?
	beq.l	scp_array		 ; ... yes

	assert	tkc.rfna,tkc.refn-2,tkc.conf-4
	subq.w	#tkc.conf-tkc.rfna,d0	 ; name or constant?
	blt.s	scp_name		 ; ... name, could be exp
	sub.w	#tkc.conl-tkc.conf,d0	 ; constant
	bgt.l	scp_expr		 ; ... no

	assert	tkc.conl,tkc.str
	beq.s	scp_str 		 ; ... string
	addq.w	#-tkc.fp6+tkc.conl,d0	 ; ... fp?
	bne.s	scp_int

	moveq	#-tkc.opf,d0
	add.w	6(a3),d0		 ; constant followed by expr?
	bge.l	scp_expr		 ; yes, it is an expression
	add.w	#tkc.opf-tkc.lpar,d0	 ; index post op?
	beq.l	scp_expr		 ; ... yes
	move.w	#bo.fppar,d0		 ; constant FP
	move.w	(a3)+,d2
	move.l	(a3)+,d3
	bsr.l	scp_opsep
	move.w	d2,(a0)+
	move.l	d3,(a0)+
	moveq	#0,d0
	rts

scp_int
	moveq	#-tkc.opf,d0
	add.w	2(a3),d0		 ; constant followed by expr?
	bge.l	scp_expr		 ; yes, it is an expression
	add.w	#tkc.opf-tkc.lpar,d0	 ; index post op?
	beq.l	scp_expr		 ; ... yes
	move.w	#bo.inpar,d0		 ; constant integer
	move.w	(a3)+,d2
	bsr.l	scp_opsep
	move.w	d2,(a0)+
	moveq	#0,d0
	rts

scp_str
	moveq	#-tkc.opf,d0
	add.w	4(a3),d0		 ; constant followed by expr?
	bge.l	scp_expr		 ; yes, it is an expression
	add.w	#tkc.opf-tkc.lpar,d0	 ; index post op?
	beq.l	scp_expr		 ; ... yes
	move.w	#bo.stpar,d0		 ; constant string
	move.l	(a3)+,d3
	bsr.l	scp_opsep
	move.l	d3,(a0)+
	moveq	#0,d0
	rts

scp_name
	moveq	#-tkc.opf,d0
	add.w	2(a3),d0		 ; name followed by expr?
	bge.l	scp_expr		 ; yes, it is an expression
	add.w	#tkc.opf-tkc.lpar,d0	 ; index post op?
	beq.l	scp_expr		 ; ... yes
	move.w	(a3)+,d1		 ; name index
	move.w	d1,d3
	lsl.w	#nt.ishft,d3
	move.l	sb_nutbb(a6),a1
	add.w	d1,a1
	cmp.b	#us.var,(a1)		 ; guaranteed variable?
	beq.s	scp_nmpar		 ; ... yes

	cmp.b	#us.fun,(a1)		 ; function only?
	beq.s	scp_expr4		 ; ... yes, treat as function

	move.w	#bo.nfpar,d0
	add.w	(sp),d0
	move.w	d0,(a0)+		 ; name, array	or function
	move.w	d3,(a0)+
	bra.s	scp_expar		 ; and evaluate, just in case

scp_nmpar
	move.w	#bo.nmpar,d0		 ; name parameter
	bsr.s	scp_opsep
	move.w	d3,(a0)+
	moveq	#0,d0
	rts

scp_array
	moveq	#0,d3
	move.w	(a3)+,d3		 ; name index
	move.l	sb_nutbb(a6),a1
	btst	#us..ary,(a1,d3.w)	 ; can it be array?
	beq.s	scp_expr4		 ; ... no, expression

	movem.l a0/a3,-(sp)		 ; save pointers
	bsr.l	sb_cmpop_fnd3		 ; process array / function reference

	moveq	#-tkc.opf,d0
	add.w	(a3),d0 		 ; array ref followed by expr?
	bge.s	scp_expra0a3		 ; yes, it is an expression
	add.w	#tkc.opf-tkc.lpar,d0	 ; index post op?
	beq.s	scp_expra0a3		 ; ... yes

	addq.l	#8,sp			 ; throw away pointers
	assert	bo.arpar-bo.pusha,bo.cfpar-bo.pushcf,bo.sfpar-bo.pushsf,bo.sfparc-bo.pushsfc
	moveq	#bo.arpar-bo.pusha,d0
	add.w	(a2),d0
	move.w	d0,d1
	add.w	(sp),d1 		 ; adjust for par/pin
	move.w	d1,(a2) 		 ; change from push to par/pin
	cmp.w	#bo.arpar,d0		 ; is it array (guaranteed)?
	bne.s	scp_expar		 ; ... no
	lea	2(a0),a2		 ; copy up to make room for separator
	move.l	-(a0),-(a2)		 ; bit mask
	move.l	-(a0),-(a2)		 ; number of indices and values
	move.l	-(a0),-(a2)		 ; operation and name token
	bsr.s	scp_opsep		 ; set operation and name token
	lea	10(a0),a0		 ; and reset program pointer
	rts

scp_null
	subq.l	#2,a3
	move.w	#bo.nlpar,d0		 ; null parameter
	bsr.s	scp_opsep
	rts

scp_expra0a3
	movem.l (sp)+,a0/a3		 ; restore pointers
scp_expr4
	subq.l	#2,a3			 ; backspace
scp_expr
	subq.l	#2,a3			 ; backspace to start of expression
scp_expe
	jsr	sb_cmpopx		 ; compile expression
	bne.s	scp_errex		 ; ... oops

scp_expar
	move.w	#bo.expar,d0		 ; expression parameter
	bsr.s	scp_opsep
	rts

scp_opsep
	move.l	(sp)+,a2		 ; return address
	add.w	(sp)+,d0		 ; adjusted operation
	move.w	d0,(a0)+
	move.w	(sp)+,d1		 ; recover hash
	move.w	(a3),d0 		 ; check separator
	sub.w	#tkc.cmma-2,d0		 ; at least comma?
	ble.s	scp_stsep
	cmp.w	#nt.to<<1,d0		 ; greater than TO?
	bhi.s	scp_stsep		 ; ... yes
	lsl.w	#nt.seps-1,d0		 ; ... no, shift separator up
	or.w	d0,d1			 ; true separator
	addq.l	#2,a3
scp_stsep
	move.w	d1,(a0)+
	moveq	#0,d0
	jmp	(a2)

scp_errex
	addq.l	#4,sp			 ; clean up stack
	rts


	end
