; SBAS_IEXPR - Interpreter Expression Evaluation     1992 Tony Tebby
;
; 2006-04-01  1.01  Fixed return of 'date$()(20)', i.e. index of last char  (MK)
; 2017-01-06  !!! abandoned change !!! 1.02  Check x is valid for a$(x to) and date$()(x to)	     (MK)
; 2017-12-30  1.03  Fixed "retry-in-floating point" mechanism on overflow   (MK)

	section sbas

	xdef	sbi_alar    ; interpreter allocate initial RI stack
	xdef	sb_alard    ;			   RI stack (d1)

	xdef	bo_pushs    ; push values
	xdef	bo_pushf
	xdef	bo_pushi
	xdef	bo_pushl
	xdef	bo_pushv
	xdef	bo_pushu
	xdef	bo_pusha

	xdef	bo_pushcf   ; function evaluation
	xdef	bo_pushsf
	xdef	bo_pushsfc
	xdef	bo_pushcfa  ; function evaluation or push array element
	xdef	bo_pushsfa
	xdef	bo_pushsfac
	xdef	bo_inline   ; inline function reference

	xdef	bo_const    ; push constant
	xdef	bo_confp
	xdef	bo_conin
	xdef	bo_conli

	xdef	bo_neg	    ; arithmetic operations
	xdef	bo_bnot
	xdef	bo_not
	xdef	bo_index
	xdef	bo_indvt
	xdef	bo_indtv
	xdef	bo_indvtv
	xdef	bo_add
	xdef	bo_sub
	xdef	bo_mul
	xdef	bo_divf
	xdef	bo_power
	xdef	bo_divi
	xdef	bo_mod
	xdef	bo_concat
	xdef	bo_instr
	xdef	bo_band
	xdef	bo_bor
	xdef	bo_bxor
	xdef	bo_and
	xdef	bo_or
	xdef	bo_xor
	xdef	bo_cpaeq
	xdef	bo_cpeq
	xdef	bo_cpne
	xdef	bo_cpgt
	xdef	bo_cpge
	xdef	bo_cple
	xdef	bo_cplt

	xdef	bo_asgs 	    ; assignments
	xdef	bo_asgf
	xdef	bo_asgi
	xdef	bo_asgl
	xdef	bo_asgv
	xdef	bo_asgu
	xdef	bo_asgel

	xdef	bo_cfpar
	xdef	bo_sfpar
	xdef	bo_sfparc
	xdef	bo_cfpin
	xdef	bo_sfpin
	xdef	bo_sfpinc

	xdef	bo_arroff
	xdef	bo_arrsoff

	xdef	sb_stint
	xdef	sb_anyfp2

	xref	sb_iloop
	xref	sb_iloop5
	xref	sb_ireset
	xref	sb_ienimp
	xref	sb_ierset
	xref	sb_istop
	xref	sb_istopok

	xref	sb_fint
	xref	sb_icall
	xref	sb_icall_pas

	xref	sb_sttadd
	xref	sb_setpfr
	xref	sb_isubai

	xref	sb_resar
	xref	sb_aldat
	xref	sb_aldat8
	xref	sb_redat

	xref	ca_decfp
	xref	ca_iwdec
	xref	ca_ildec
	xref	ca_fpdec
	xref	cv_deciw
	xref	cv_sldiv

	xref	qa_addd
	xref	qa_subd
	xref	qa_muld
	xref	qa_divd
	xref	qa_powfd
	xref	qa_powin
	xref	qa_nint
	xref	qa_nlint
	xref	qa_float
	xref	qa_lfloat

	xref	uq_instr
	xref	uq_cinstr
	xref	uq_cstra

	include 'dev8_keys_sbasic'
	include 'dev8_smsq_sbas_inter_keys'
	include 'dev8_keys_qlv'
	include 'dev8_keys_err'
	include 'dev8_keys_err4'
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

sbi_alar
	moveq	#ar.allc,d1		 ; initial allocation
	moveq	#ar.ovrn,d6
	bra.s	bo_alarx

sb_alard
	add.l	d6,d1			 ; limit + space
	cmp.l	d1,a1
	bge.s	boa_rts 		 ; plenty of room
	sub.l	d6,d1
	bra.s	bo_alard

bo_alar
	moveq	#ar.allc,d1		 ; allocate a reasonable amount
bo_alard
	moveq	#ar.ovrn,d6
	add.l	d6,d1			 ; overrun allowance
	sub.l	a6,a1
	move.l	a1,sb_arthp(a6) 	 ; set arithmetic pointer
bo_alarx
	jsr	sb_resar		 ; allocate a bit
	move.l	sb_arthp(a6),a1
	add.l	sb_arthl(a6),d6
	add.l	a6,a1
	add.l	a6,d6
boa_rts
	rts

;******************************************************************************
;
;	Push Variable Values
;
bo_vaddr
	cmp.l	d6,a1			 ; enough room?
	bge.s	bov_do
	bsr	bo_alar
bov_do
	move.w	(a4)+,d0		 ; name index
	move.l	nt_value(a3,d0.w),a0
	jmp	2(a2)

;--------------------------------
bo_pushs			 ; push string
	bra.s	bo_vaddr		 ; set up address
	move.l	a0,-(a1)		 ; push address
	move.w	#ar.adds,-(a1)		 ; push type
	jmp	(a5)

;--------------------------------
bo_pushf			 ; push float
	bra.s	bo_vaddr		 ; set up address
	move.l	2(a0),-(a1)		 ; push mantissa
	move.w	(a0),-(a1)		 ; exponent
	move.w	#ar.float,-(a1) 	 ; push type
	jmp	(a5)

;--------------------------------
bo_pushi			 ; push integer
	bra.s	bo_vaddr		 ; set up address
	move.w	(a0),-(a1)		 ; push integer
	move.w	#ar.int,-(a1)		 ; push type
	jmp	(a5)

;--------------------------------
bo_pushl			 ; push long
	bra.s	bo_vaddr		 ; set up address
	move.l	(a0),-(a1)		 ; push long integer
	move.w	#ar.long,-(a1)		 ; push type
	jmp	(a5)

bopv_iexpr
	bra.s	bo_iexpr

;--------------------------------
bo_pushv			 ; push variable
	bra.s	bo_vaddr		 ; set up address
;;;;	bra.s	bopv_do
;;;; fall through here!!
bopv_vchk
bopv_do
	moveq	#$0f,d1
	and.b	nt_vtype(a3,d0.w),d1	 ; variable type
	move.b	bopv_tab(pc,d1.w),d1	 ; offset to routine to process it.
	jmp	bo_pushs+2(pc,d1.w)

bopv_tab
	dc.b	bopv_iexpr-bo_pushs-2	       ; null
	dc.b	bo_pushs-bo_pushs	       ; string
	dc.b	bo_pushf-bo_pushs	       ; float
	dc.b	bo_pushi-bo_pushs	       ; integer
	dc.b	bo_pushl-bo_pushs	       ; long
	ds.w	0
;--------------------------------
bo_pushu			 ; push unknown
	bra.s	bo_vaddr		 ; set up address
	move.b	nt_nvalp(a3,d0.w),d1	 ; usage
	move.w	#1<<nt.var+1<<nt.rep+1<<nt.for,d2
	btst	d1,d2

	bne.s	bopv_do 		 ; ... ok, it's a variable
	subq.b	#nt.arr,d1		 ; array?
	bgt.s	bopu_fn 		 ; no, function
	blt.s	bopv_vchk		 ; it is unset or null
	moveq	#$0f,d1 		 ; it is array
	and.b	nt_vtype(a3,d0.w),d1	 ; variable type
	subq.b	#nt.st,d1		 ; string?
	bgt.s	bo_iexpr
	blt.s	bop_subs		 ; substring

	cmp.w	#1,dt_nindx(a0) 	 ; one dimension?
	bne.s	bo_iexpr
	move.l	dt_offs(a0),-(a1)	 ; push address
	move.w	#ar.adds,-(a1)		 ; push type
	jmp	(a5)

bop_subs
	cmp.w	#1,dt_nindx(a0) 	 ; one dimension?
	bne.s	bo_iexpr

	move.w	dt_index+dt_mxind(a0),d4 ; max index is substring length
	move.w	d4,d1
	sub.l	a1,d1
	add.l	d6,d1			 ; amount of extra space required
	ble.s	bops_set		 ; fine

	move.w	d4,d1
	jsr	bo_alard		 ; allocate some more ri stack

bops_set
	move.l	dt_offs(a0),a0
	add.w	d4,a0			 ; end of characters
	moveq	#1,d1
	and.w	d4,d1			 ; do we need to round up?
	sub.w	d1,a1			 ; yes, pad
	move.w	d4,d1
	bra.s	bops_elp
bops_lp
	move.b	-(a0),-(a1)
bops_elp
	dbra	d1,bops_lp

	move.w	d4,-(a1)		 ; string length
	move.w	#ar.strng,-(a1) 	 ; and type
	jmp	(a5)

;........................................
bo_iexpr
	moveq	#err.iexp,d0
	jmp	sb_istop
bo_iovfl
	moveq	#err.ovfl,d0
	jmp	sb_istop

;******************************************************************************
;
;	Function and Array References
;
bopu_fn
	subq.b	#nt.mcfun-nt.arr,d1	 ; M/C function?
	bne.s	bopu_sfn		 ; ... no
	move.l	sb_nmtbp(a6),a3 	 ; ... yes, no parameters
	move.l	a3,a5
	move.l	sb_retsp(a6),d2 	 ; preserve return stack
	jsr	sb_icall_pas		 ; a0 already set
	bra.s	bofn_ret

bopu_sfn
	addq.b	#nt.mcfun-nt.sbfun,d1	 ; SBASIC function
	bne.l	bar_bref		 ; ... no
	tst.b	sb_cmdl(a6)		 ; command line?
	bne.s	bopu_sfc		 ; ... yes
	move.l	a0,d0			 ; line and statement number
	jsr	sb_sttadd		 ;
	move.l	d0,d4
	pea	bo_pushsf		 ; carry on with this one

bopu_sfdo
	move.l	#rt.fun<<24,d3		 ; type of frame required
	moveq	#0,d5
	jmp	sb_setpfr		 ; set return stack frame

bopu_sfc
	move.l	a0,d4			 ; line / statement number
	pea	bo_pushsfc
	bra.s	bopu_sfdo

;--------------------------------
bo_pushcfa		      ; code function eval or push array element
	move.l	sb_retsp(a6),a2
	move.b	rt_type(a6,a2.l),d1	 ; type
	blt.l	bofp_array
	add.w	#bop.arrs,a4		  ; skip name index etc.

;--------------------------------
bo_pushcf		      ; code function eval
bo_cfpar
bo_cfpin
	jsr	sb_icall

bofn_ret
	tst.l	d0			 ; was OK?
	bne.s	bcf_error

	move.l	sb_arthp(a6),a1
	add.l	a6,a1			 ; restore arith stack
	move.w	d4,-(a1)		 ; set type
	jmp	sb_ireset

bcf_error
	add.l	sb_arthb(a6),d7 	 ; reset arithmetic stack
	move.l	d7,sb_arthp(a6) 	 ; reset RI stack
	jmp	sb_istop		 ; ... stop with error


;--------------------------------
bo_pushsfa			 ; SBASIC function ref or push array element
	move.l	sb_retsp(a6),a2
	move.b	rt_type(a6,a2.l),d1	 ; type
	blt.s	bofp_array
	add.w	#bop.arrs,a4		  ; skip name index etc.

;--------------------------------
bo_pushsf			 ; SBASIC function ref
bo_sfpar
bo_sfpin
;bo_dospr
	move.l	sb_retsp(a6),a2
	move.l	sb_arthb(a6),d0
	add.l	a6,d0
	sub.l	a1,d0
	or.l	d0,rt_arstk(a6,a2.l)	 ; save arith stack size for errors

	move.l	a4,rt_ret(a6,a2.l)	 ; return address
	move.l	rt_def(a6,a2.l),a4	 ; routine address
	jmp	(a5)

;--------------------------------
bo_pushsfac			 ; SBASIC function ref or push array element
	move.l	sb_retsp(a6),a2
	move.b	rt_type(a6,a2.l),d1	 ; type
	blt.s	bofp_array
	add.w	#bop.arrs,a4		  ; skip name index etc.

;--------------------------------
bo_pushsfc			 ; SBASIC function ref
bo_sfparc
bo_sfpinc
;bo_dosprc
	move.l	sb_retsp(a6),a2
	move.l	sb_arthb(a6),d0
	add.l	a6,d0
	sub.l	a1,d0
	or.l	d0,rt_arstk(a6,a2.l)	 ; save arith stack size for errors
	st	rt_ret(a6,a2.l) 	 ; return to command line
	move.w	rt_def(a6,a2.l),d0	 ; routine line number
	move.w	d0,sb_cline(a6)
	move.b	#1,sb_cstmt(a6)
	st	sb_cmdt2(a6)		 ; save token position
	sf	sb_cont(a6)
	move.w	#sb.cont,sb_actn(a6)	 ; continue from here
	jmp	sb_istopok

bar_bref
	moveq	#ern4.bref,d0		 ; not array or function
	jmp	sb_ierset


bofp_array
	subq.l	#rt.sasize,a2
	move.l	a2,sb_retsp(a6)  ; clean up return stack
;---------------------------------
bo_pusha			 ; push array element
	move.w	(a4)+,d3		 ; name index
	move.w	(a4)+,d4		 ; number of indices
	move.w	(a4)+,d5		 ; number of values
	beq.s	bpa_type

	move.l	a6,a2			 ; use buffer to convert
	add.l	sb_cmdlb(a6),a2

bpa_cmloop
	cmp.w	#ar.int,(a1)+		 ; is it an integer already?
	beq.s	bpa_move		 ; ... yes
	jsr	sb_fint
bpa_move
	move.w	(a1)+,-(a2)		 ; move
	subq.w	#1,d5
	bgt.s	bpa_cmloop

bpa_type
	move.b	nt_nvalp(a3,d3.w),d0	 ; usage
	moveq	#$f,d1
	and.b	nt_vtype(a3,d3.w),d1	 ; and type
	move.l	nt_value(a3,d3.w),a0

	subq.b	#nt.arr,d0		 ; array?
	bne.l	bpa_var 		 ; ... no

	move.l	(a0)+,d5		 ; base of data
	moveq	#0,d3			 ; and offset

	subq.b	#nt.st,d1
	ble.s	bpa_stra		 ; string array

	cmp.w	(a0)+,d4		 ; the right number of indices?
	bne.l	bel_iind
	move.l	(a4)+,d0		 ; index types
	bne.l	bel_iind

	bsr.l	bo_arroff		 ; calculate offset

	move.l	d5,a2			 ; base in address reg
	subq.b	#nt.fp-nt.st,d1 	 ; was it FP?
	bgt.s	bpai_push		 ; ... no, int

	add.l	d3,d3
	move.l	d3,d0
	add.l	d3,d3
	add.l	d0,d3			 ; 6*offset
	add.l	d3,a2			 ; address

	cmp.l	d6,a1			 ; enough room?
	bge.s	bpaf_do
	bsr	bo_alar
bpaf_do
	move.l	2(a2),-(a1)		 ; set fp
	move.w	(a2),-(a1)
	move.w	#ar.float,-(a1)
	jmp	(a5)

bpai_push
	add.l	d3,d3
	add.l	d3,a2

	cmp.l	d6,a1			 ; enough room?
	bge.s	bpai_do
	bsr	bo_alar
bpai_do
	move.w	(a2),-(a1)		 ; set int
	move.w	#ar.int,-(a1)
	jmp	(a5)

bpa_sstra
bpass.fr equ	$30
	sub.w	#bpass.fr,sp		 ; make room for descriptor
	lea	-4(a0),a5		 ; old descriptor
	move.l	sp,a0			 ; new descriptor
	move.l	(a4)+,d2
	moveq	#0,d3			 ; substring type
	jsr	sb_isubai		 ; set up new sub array
	move.l	(sp)+,a0		 ; base of substring
	cmp.w	#1,(sp)+		 ; one dimension only
	bne.l	bel_iind
	move.w	(sp)+,d2		 ; length
	cmp.w	#1,(sp) 		 ; contiguous?
	bne.l	bel_iind
	lea	1(a0,d2.w),a0		 ; one past end
	add.w	#bpass.fr-8,sp
	lea	sb_iloop,a5
	bra.l	bpass_set

bpa_stra
	blt.s	bpa_sstra		 ; sub string array
	move.w	(a0)+,d0
	sub.w	d4,d0			 ; the right number of indices?
	beq.s	bpa_sas 		 ; ... yes, substring
	subq.w	#1,d0			 ; one spare?
	bne.l	bel_iind		 ; ... no
	move.l	(a4)+,d0		 ; index types
	bne.l	bel_iind

	bsr.l	bo_arrsoff		 ; string offset (one fewer dimensions)
	move.l	d5,a2
	add.l	d3,a2

	cmp.l	d6,a1			 ; enough room?
	bge.s	bpas_do
	bsr	bo_alar
bpas_do
	move.l	a2,-(a1)		 ; pointer to string
	move.w	#ar.adds,-(a1)
	jmp	(a5)

bpa_sas
	move.l	(a4)+,d1		 ; index mask
	bne.s	bpa_sbstr		 ; non zero, must be substring asg
	bsr.l	bo_arroff		 ; ordinary offset
	move.l	d5,a0
	tst.w	d0			 ; last index zero?
	bne.s	bpas_pchar2		 ; ... no

	add.l	d3,a0
	cmp.l	d6,a1			 ; enough room?
	bge.s	bpasl_do
	bsr	bo_alar
bpasl_do
	move.w	(a0),-(a1)		 ; set length
	move.w	#ar.int,-(a1)
	jmp	(a5)

bpas_pchar2
	addq.l	#2,a0			 ; skip length
bpas_pchar
	add.l	d3,a0
	cmp.l	d6,a1			 ; enough room?
	bge.s	bpas1_do
	bsr	bo_alar
bpas1_do
	subq.w	#2,a1
	move.b	-(a0),(a1)		 ; set char and rubbish
	move.l	#ar.strng<<16+1,-(a1)
	jmp	(a5)

bpa_sbstr
	subq.w	#1,d4			 ; calculate offset not using last ind
	beq.s	bpa_sbadd		 ; no other indices
	move.w	d4,d0			 ; number of indices
	lsl.w	#2,d0
	ror.l	d0,d1			 ; last mask in lsb
	bsr.l	bo_arrsoff
	add.l	d3,d5			 ; address
bpa_sbadd
	moveq	#0,d4			 ; hard limit
	move.w	(a0)+,d4		 ; limit
	move.l	d5,a0			 ; string to use
	move.w	(a0)+,d5		 ; length

bpass_from
	moveq	#1,d3			 ; assume from 1
	lsr.l	#2,d1			 ; get from bit in carry
	bcc.s	bpass_to		 ; default
	move.w	(a2)+,d3		 ; real start
	ble.l	bel_inor		 ; ... oops
bpass_to
	lsr.l	#1,d1
	bne.l	bel_iind		 ; there are other ranges!!
	bcc.s	bpass_defto		 ; default to
	move.w	(a2)+,d0		 ; real to
	cmp.w	d4,d0			 ; in range?
	bls.s	bpass_sto		 ; ... yes
	tst.l	d4			 ; soft limit?
	bmi.s	bpass_defto		 ; ... yes
	bra.l	bel_inor		 ; ... no
bpass_sto
	move.w	d0,d5			 ; ... yes, use index or limit
bpass_defto
	addq.w	#1,d5
	move.w	d5,d2			 ; end
	sub.w	d3,d2			 ; less start - 1 = length of substring
	blt.l	bel_iind		 ; ... less than none
	beq.s	bpass_len
	add.w	d5,a0			 ; one too far
bpass_set
	moveq	#0,d7
	move.w	d2,d7
	move.l	d6,d0			 ; limit
	add.l	d7,d6			 ; room for string?
	cmp.l	d6,a1
	bgt.s	bpass_copy		 ; ... yes
	move.l	d7,d1
	bsr	bo_alard		 ; ... no allocate some

bpass_copy
	move.w	d7,d2
	btst	#0,d7			 ; odd length?
	bne.s	bpass_cloop		 ; yes, copy one too many

	subq.l	#1,a0			 ; copy the right number
	subq.w	#1,d7

bpass_cloop
	move.b	-(a0),-(a1)		 ; copy characters
	dbra	d7,bpass_cloop
bpass_len
	move.w	d2,-(a1)
	move.w	#ar.strng,-(a1)
	jmp	(a5)


bpa_var
	addq.b	#nt.arr-nt.var,d0	 ; variable?
	bne.l	bel_narr		 ; ... no
	subq.b	#nt.st,d1		 ; must be string
	bne.l	bel_narr		 ; ... no

	subq.w	#1,d4			 ; one index only please
	bne.l	bel_iind		 ; ... oops

	moveq	#-1,d4			 ; soft limit
	move.w	(a0)+,d4		 ; limit
	move.w	d4,d5			 ; and default end
	move.l	(a4)+,d1		 ; range?
	bne	bpass_from		 ; ... yes
	moveq	#0,d3
	move.w	(a2)+,d3		 ; index
	ble	bpa_v0
	cmp.w	d4,d3			 ; in range?
	ble	bpas_pchar		 ; set character
	bra	bel_inor

bpa_v0
	blt	bel_inor
	move.w	d4,-(a1)		 ; stack length
	move.w	#ar.int,-(a1)
	jmp	(a5)

;--------------------------------
bo_inline			 ; inline function reference

	jmp	sb_ienimp

;******************************************************************************
;
;	Push Constants
;
boc_alar
	bsr	bo_alar 	 ; allocate some more memory
	jmp	4(a2)		 ; ... and continue

;--------------------------------
bo_const			 ; push constant string
	cmp.l	d6,a1			 ; enough room?
	blt.s	boc_alar		 ; ... no
	move.l	(a4)+,-(a1)		 ; string address
	move.w	#ar.adds,-(a1)
	jmp	(a5)

;--------------------------------
bo_confp			 ; push constant float
	cmp.l	d6,a1			 ; enough room?
	blt.s	boc_alar		 ; ... no
	subq.l	#6,a1
	move.w	(a4)+,(a1)		 ; exponent
	move.l	(a4)+,2(a1)		 ; mantissa
	move.w	#ar.float,-(a1)
	jmp	(a5)

;--------------------------------
bo_conin			 ; push constant integer
	cmp.l	d6,a1			 ; enough room?
	blt.s	boc_alar		 ; ... no
	move.w	(a4)+,-(a1)		 ; integer
	move.w	#ar.int,-(a1)
	jmp	(a5)

;--------------------------------
bo_conli			 ; push constant long integer
	cmp.l	d6,a1			 ; enough room?
	blt.s	boc_alar		 ; ... no
	move.l	(a4)+,-(a1)		 ; long integer
	move.w	#ar.long,-(a1)
	jmp	(a5)

;******************************************************************************
;
;	Monadic Ops
;
;--------------------------------
bo_neg				 ; negate
	move.w	(a1),d0
	subq.b	#ar.float,d0		 ; most likely to be float
	bne.s	bong_sel		 ; ... but it isn't
bong_fp
	neg.l	4(a1)			 ; negate the mantissa
	bvs.s	bong_ov 		 ; was negative and it's overflowed
	bmi.s	bong_norm		 ; was positive, it might need renorm
	jmp	(a5)			 ; ... fine

bong_norm
	btst	#30,4(a1)		 ; unnormalised?
	bne.s	bong_shft		 ; ... yes
	jmp	(a5)

bong_shft
	sub.w	#1,2(a1)		 ; ... yes, smaller exponent please
	bge.s	bong_dble		 ; ... and double mantissa
	clr.w	2(a1)			 ; exponent has underflowed as well
	jmp	(a5)

bong_dble
	lsl	6(a1)
	roxl	4(a1)			 ; multiply by 2
	jmp	(a5)

bong_ov
	move.l	(a1)+,d1
	lsr	(a1)			 ; halve mantissa
	roxr	2(a1)
	addq.w	#1,d1			 ; correct exponent
	move.l	d1,-(a1)
	cmp.w	#$1000,d1		 ; exponent overflowed?
	bhs	bo_iovfl		 ; ... yes
	jmp	(a5)

;..............................................

bong_sel
	bgt.s	bong_int		 ; some form of integer
	bsr.l	bo_sxfp 		 ; float the string
	move.w	#ar.float,-(a1)
	bra.s	bong_fp

bong_int
	subq.w	#ar.long-ar.float,d0	 ; type of int?
	blt.s	bong_short		 ; short

	neg.l	2(a1)			 ; long integer
	bvs.s	bong_fll		 ; overflow
	jmp	(a5)

bong_fll
	move.l	#$4000000,2(a1)
	move.w	#$0820,(a1)		 ; 2^32
	move.w	#ar.float,-(a1)
	jmp	(a5)

bong_short
	neg.w	2(a1)			 ; short integer
	bvs.s	bong_fli		 ; overflow
	jmp	(a5)

bong_fli
	move.l	#$4000000,(a1)
	move.w	#$0810,-(a1)		 ; 32768
	move.w	#ar.float,-(a1)
	jmp	(a5)

;--------------------------------
bo_bnot 			 ; Bitwise NOT
	cmp.w	#ar.int,(a1)		 ; is it an integer already?
	bne.s	bobn_int		 ; ... no, int it
	not.w	2(a1)			 ; not it
	jmp	(a5)
bobn_int
	blt.s	bobn_nint		 ; not a long int
	not.l	2(a1)			 ; long counts as int
	jmp	(a5)
bobn_nint
	cmp.w	#ar.strng,(a1)+ 	 ; was it a string
	ble.s	bobn_sint		 ; ... yes

	jsr	qa_nint 		 ; set to nearest integer on stack
	beq.s	bobn_not
	bra	bo_iexpr

bobn_sint
	bsr.s	bo_stint

bobn_not
	not.w	(a1)			 ; ... not it
	move.w	#ar.int,-(a1)
	jmp	(a5)

;--------------------------------
bo_not				 ; Logical NOT
	cmp.w	#ar.int,(a1)+		 ; is it an integer already?
	bne.s	bont_nint		 ; ... no
bont_check
	tst.w	(a1)+			 ; true?
	beq.s	bont_true		 ; ... no, it is now
bont_false
	move.l	#ar.int<<16,-(a1)
	jmp	(a5)
bont_true
	move.l	#ar.int<<16+1,-(a1)
	jmp	(a5)

bont_nint
	bgt.s	bont_long		 ; long int
	cmp.w	#ar.strng,-2(a1)	 ; was it string
	bgt.s	bont_fp
	bsr.s	bos_float
bont_fp
	addq.l	#2,a1			 ; skip exponent
bont_long
	tst.l	(a1)+			 ; value of long
	bne.s	bont_false
	beq.s	bont_true


;******************************************************************************
;
;	Type Conversion Utilities
;
; sb_anyfp2 call    a1 past type
;	    return  d0,d7,a0 smashed, fp on stack
;
sb_anyfp2
bo_anyfp2
	subq.l	#2,a1
	move.w	(a1)+,d0
	subq.w	#ar.strng,d0		 ; string type?
	ble.s	bo_stfp 		 ; ... yes
	subq.w	#ar.int-ar.strng,d0	 ; int or long int
	beq.l	qa_float		 ; ... word
	bgt.l	qa_lfloat		 ; ... long
	rts				 ; ... it was fp all the time

bo_sxfp 				 ; string to float
	tst.w	(a1)+			 ; type of string
bo_stfp
					 ; if -ve on entry, it is pointer to str
bos_float
	blt.s	bos_fpst		 ; pointer to string
	move.l	a1,a0			 ; string
	bsr.s	bos_decf		 ; float
	lea	(a1),a0
	addq.l	#1,d7
	bclr	#0,d7
	move.l	d7,a1			 ; top of old string
	move.l	2(a0),-(a1)
	move.w	(a0),-(a1)		 ; copy value up
	rts

bos_fpst
	move.l	(a1)+,a0		 ; string

bos_decf
	moveq	#0,d7
	move.w	(a0)+,d7
	add.l	a0,d7			 ; end of string
	jsr	ca_decfp		 ; float
	bne	sb_istop
	rts

sb_stint
bo_stint				 ; string on stack to integer
					 ; if -ve on entry, it is pointer to str
	blt.s	bos_ipst		 ; pointer to string
	move.l	a1,a0			 ; string
	bsr.s	bos_deci		 ; int
	move.w	(a1),d1
	addq.l	#1,d7
	bclr	#0,d7
	move.l	d7,a1			 ; top of old string
	move.w	d1,-(a1)		 ; copy value
	tst.l	d0
	rts

bos_ipst
	move.l	(a1)+,a0		 ; string

bos_deci
	moveq	#0,d7
	move.w	(a0)+,d7
	add.l	a0,d7			 ; end of string
	jsr	cv_deciw		 ; int
	bne	sb_istop
	move.w	d1,-(a1)
	tst.l	d0
	rts

;******************************************************************************
;
;	Arithmetic Operations
;
bof_pre 			 ; prepare for normal floating point operations
	move.w	(a1)+,d0		 ; type of first value
	subq.w	#ar.float,d0		 ; hope its a float?
	beq.s	bof_pop 		 ; ... yes, pop it
	bgt.s	bof_int 		 ; some type of integer
	addq.w	#ar.float,d0
	bsr.s	bo_stfp 		 ; it is now float
bof_pop
	move.w	(a1)+,d2
	move.l	(a1)+,d1

	addq.l	#8,a2			 ; fp <op> fp
	cmp.w	#ar.float,(a1)+ 	 ; we need FP now
	bne.s	bof_fp2
	jmp	(a2)			 ; ... do fp+fp
bof_fp2
	pea	(a2)			 ; return to fp <op> fp
	bra	bo_anyfp2		 ; float any

bof_int
	subq.w	#ar.long-ar.float,d0	 ; long?
	beq.s	bof_long

	move.w	(a1)+,d1
	cmp.w	#ar.int,(a1)+		 ; int <op> int?
	bne.s	bof_inta
	move.w	(a1)+,d2
	jmp	4(a2)			 ; ... yes!!
bof_inta
	blt.s	bof_ifp 		 ; not long either

	ext.l	d1
	move.l	(a1)+,d2
	jmp	6(a2)			 ; long <op> long

bof_long
	move.l	(a1)+,d1
	cmp.w	#ar.int,(a1)+		 ; long <op> long?
	ble.s	bof_lnga		 ; ... no
	move.l	(a1)+,d2
	jmp	6(a2)			 ; ... yes!!

bof_lnga
	blt.s	bof_lfp 		 ; not short either
	move.w	(a1)+,d2
	ext.l	d2			 ; make long <op> long
	jmp	6(a2)

bof_ifp
	subq.l	#2,a1			 ; restore type
	move.w	d1,-(a1)
	jsr	qa_float		 ; int to float
	bra	bof_pop

bof_lfp
	subq.l	#2,a1
	move.l	d1,-(a1)
	jsr	qa_lfloat		 ; long to float
	bra	bof_pop

;--------------------------------
bo_add				 ; add
	bra.s	bof_pre
	nop
	bra.s	bfa_int 		 ; int+int
	bra.s	bfa_long		 ; long+long

	jsr	qa_addd
	bne.s	bf_error
	move.w	#ar.float,-(a1)
	jmp	(a5)

bfa_int
	add.w	d1,d2			 ; result
	bvs.s	bfa_iov
	move.w	d2,-(a1)
	move.w	#ar.int,-(a1)		 ; ... is integer
	jmp	(a5)

bfa_iov
	roxr.w	#1,d2			 ; recover carry
	ext.l	d2
	roxl.l	#1,d2			 ; recover value
	bra.s	bfa_lok

bfa_long
	add.l	d1,d2			 ; result
	bvs.s	bfa_lov
bfa_lok
	move.l	d2,-(a1)
	move.w	#ar.long,-(a1)		 ; ... is long
	jmp	(a5)

bfa_lov
	sub.l	d1,d2			 ; restore
bof_ov
	move.l	d2,-(a1)
	jsr	qa_lfloat
	move.l	d1,-(a1)
	jsr	qa_lfloat		 ; float both
	move.w	(a1)+,d2
	move.l	(a1)+,d1
	jmp	8(a2)			 ; do original op with floats

;--------------------------------
bo_sub				 ; subtract
	bra.l	bof_pre
	bra.s	bfs_int 		 ; int-int
	bra.s	bfs_long		 ; long-long

	jsr	qa_subd
	bne.s	bf_error
	move.w	#ar.float,-(a1)
	jmp	(a5)

bfs_int
	sub.w	d1,d2			 ; result
	bvs.s	bfs_iov
	move.w	d2,-(a1)
	move.w	#ar.int,-(a1)		 ; ... is integer
	jmp	(a5)

bfs_iov
	roxr.w	#1,d2			 ; recover carry
	ext.l	d2
	roxl.l	#1,d2			 ; recover value
	bra.s	bfs_lok

bfs_long
	sub.l	d1,d2			 ; result
	bvs.s	bfs_lov
bfs_lok
	move.l	d2,-(a1)
	move.w	#ar.long,-(a1)		 ; ... is long
	jmp	(a5)

bfs_lov
	add.l	d1,d2			 ; restore
	bra	bof_ov

bf_error
	move.w	#ar.float,-(a1)
	bra.l	sb_istop

;--------------------------------
bo_mul				 ; multiply
	bra.l	bof_pre
	bra.s	bfm_int 		 ; int*int
	bra.s	bfm_long		 ; long*long

	jsr	qa_muld
	bne.s	bf_error
	move.w	#ar.float,-(a1)
	jmp	(a5)

bfm_int
	muls	d1,d2			 ; result
	move.w	d2,a0
	cmp.l	a0,d2			 ; check for word only
	bne.s	bfm_lok 		 ; ... no, long
	move.w	d2,-(a1)
	move.w	#ar.int,-(a1)		 ; ... is integer
	jmp	(a5)

bfm_long
	move.w	d2,a0			 ; long*long can only give long if
	cmp.l	a0,d2			 ; one or both msws are insignificant
	beq.s	bfm_s2			 ; d2 is actually short
	exg	d1,d2			 ; ... try otherway round
	move.w	d2,a0
	cmp.l	a0,d2
	bne.l	bof_ov			 ; it will overflow, float both
bfm_s2
	move.w	d2,d4			 ; is d2 negative?
	slt	d5
	bgt.s	bfm_p1			 ; make d1 positive as well
	beq.s	bfm_zero		 ; ... no, zero
	neg.w	d4

bfm_p1
	move.l	d1,d3			 ; is d1 negative?
	bgt.s	bfm_ml			 ; ... ready to multiply
	beq.s	bfm_zero		 ; ... no zero
	not.b	d5
	neg.l	d3

bfm_ml
	move.l	d3,d0
	swap	d0
	mulu	d4,d0			 ; multiply top end
	swap	d0
	bmi	bof_ov			 ; too large
	tst.w	d0			 ; top end in the right place, overflow
	bne	bof_ov			 ; should be zero
	mulu	d4,d3			 ; bottom end
	add.l	d0,d3
	bvs	bof_ov			 ; ... oops
	move.l	d3,d2
	tst.b	d5			 ; negative result?
	beq.s	bfm_lok 		 ; ... no
	neg.l	d2
bfm_lok
	move.l	d2,-(a1)		 ; long result
	move.w	#ar.long,-(a1)
	jmp	(a5)

bfm_zero
	moveq	#0,d3
	bra.s	bfm_lok

;--------------------------------
bo_divf 			 ; divide (floating point)
	lea	qa_divd,a2

bo_2fp
	cmp.w	#ar.float,(a1)+ 	 ; fp at top
	beq.s	b2f_f1
	bsr	bo_anyfp2		 ; float any
b2f_f1
	move.w	(a1)+,d2		 ; exponent
	move.l	(a1)+,d1

	cmp.w	#ar.float,(a1)+ 	 ; fp at top
	beq.s	b2f_f2
	bsr	bo_anyfp2		 ; float any
b2f_f2
	jsr	(a2)			 ; operation
	bne.l	bf_error
	move.w	#ar.float,-(a1)
	jmp	(a5)			 ; done

;--------------------------------
bo_power			 ; raise to power
	cmp.w	#ar.int,(a1)		 ; int at top of stack?
	beq.s	bfp_int 		 ; ... v^n
	lea	qa_powfd,a2
	bra.s	bo_2fp
bfp_int
	move.l	(a1)+,d1		 ; lsw is power
	cmp.w	#ar.float,(a1)+ 	 ; fp at top
	beq.s	bfp_do
	bsr	bo_anyfp2		 ; float any
bfp_do
	move.w	d1,-(a1)
	jsr	qa_powin		 ; fp^int
	bne.l	bf_error
	move.w	#ar.float,-(a1)
	jmp	(a5)			 ; done

;******************************************************************************
;
;	Integer Divide Operations
;
;	First op may be int or long, second is int or long
;
boid_pre
	move.w	(a1)+,d3
	subq.w	#ar.int,d3		 ; first is int?
	beq.s	boid_int		 ; ... yes
	bgt.s	boid_long		 ; ... long
	addq.w	#ar.int-ar.strng,d3	 ; string?
	ble.s	boid_stint		 ; ... yes

boid_fp
	jsr	qa_nlint
	bne.l	sb_istop
boid_long
	move.l	(a1)+,d2		 ; long divisor
	beq	bo_iovfl
	move.w	d2,a0
	cmp.l	d2,a0			 ; is it a word?
	beq.s	boid_inxt

	move.w	(a1)+,d3
	subq.w	#ar.int,d3		 ; second is long?
	bgt.s	boid_ll 		 ; ... yes
	beq.s	boid_li 		 ; ... nearly
	addq.w	#ar.int-ar.strng,d3	 ; string?
	ble.s	boid_sli		 ; ... yes

	jsr	qa_nlint
	bne.l	sb_istop
boid_ll
	move.l	(a1)+,d1
	bra.s	boid_ldiv

boid_sli
	bsr	bo_stint		 ; string to integer
boid_li
	move.w	(a1)+,d1
	ext.l	d1
boid_ldiv
	move.l	d2,d0
	jsr	cv_sldiv		 ; do division
	jmp	4(a2)			 ; and operation

boid_iov
	ext.l	d2			 ; short failed, extend divisor
	bra.s	boid_ldiv		 ; and do long division!

boid_stint
	bsr	bo_stint		 ; string to integer
boid_int
	move.w	(a1)+,d2		 ; integer divisor
	beq	bo_iovfl		 ; ... oops
boid_inxt
	move.w	(a1)+,d3
	subq.w	#ar.int,d3		 ; second is long?
	bgt.s	boid_il 		 ; ... yes
	beq.s	boid_ii 		 ; ... nearly
	addq.w	#ar.int-ar.strng,d3	 ; string?
	ble.s	boid_sii		 ; ... yes

	jsr	qa_nlint
	bne.l	sb_istop

boid_il
	move.l	(a1)+,d1
	bra.s	boid_idiv
boid_sii
	bsr	bo_stint
boid_ii
	move.w	(a1)+,d1
	ext.l	d1
boid_idiv
	divs	d2,d1			 ; do division
	bvs.s	boid_iov		 ; integer overflow - treat as 2 longs
	move.w	d1,d0			 ; quotient in d0
	swap	d1			 ; remainder in d1
	tst.w	d1			 ; any remainder?
	beq.s	boid_doni
	eor.w	d1,d2			 ; remainder eor divisor
boid_doni
	jmp	6(a2)			 ; normal int by int

;--------------------------------
bo_divi 			 ; divide (integer)
	bra.l	boid_pre		 ; prepare
	bra.s	bod_li			 ; long by long
	bpl.s	bod_pint		 ; short OK
	subq.w	#1,d0			 ; adjust quotient
	bvs.s	bod_xint		 ; ... oops, extend
bod_pint
	move.w	d0,-(a1)		 ; result is quotient
	move.w	#ar.int,-(a1)
	jmp	(a5)

bod_li
	move.w	d0,a0			 ; check if quotient is int
	cmp.l	d0,a0
	beq.s	bod_pint		 ; ... yes
bod_pli
	move.l	d0,-(a1)		 ; result is quotient
	move.w	#ar.long,-(a1)
	jmp	(a5)

bod_xint
	addq.w	#1,d0			 ; restore
	ext.l	d0			 ; extend
	subq.l	#1,d0			 ; and subtract again
	bra.s	bod_pli

;--------------------------------
bo_mod				 ; modulus
	bra.l	boid_pre		 ; prepare
	bra.s	bom_li			 ; long by long
	bpl.s	bom_pint		 ; short OK
	eor.w	d1,d2			 ; restore divisor
	add.w	d2,d1			 ; adjust remainder
bom_pint
	move.w	d1,-(a1)		 ; result is remainder
	move.w	#ar.int,-(a1)
	jmp	(a5)

bom_li
	move.w	d1,a0			 ; check if remainder is int
	cmp.l	d1,a0
	beq.s	bom_pint		 ; ... yes
	move.l	d1,-(a1)		 ; result is quotient
	move.w	#ar.long,-(a1)
	jmp	(a5)

;******************************************************************************
;
;	String index post-ops, preamble
;
bos_psp2
	cmp.w	#ar.int,(a1)+		 ; is TOS an integer already?
	beq.s	bpp_getu		 ; ... yes
	jsr	sb_fint
bpp_getu
	move.w	(a1)+,d4
	swap	d4

bos_pspr
	cmp.w	#ar.int,(a1)+		 ; is TOS an integer already?
	beq.s	bpp_geti		 ; ... yes
	jsr	sb_fint
bpp_geti
	move.w	(a1)+,d4

	moveq	#2,d2			 ; buffer position for string
	moveq	#0,d3			 ; return value
	moveq	#2,d1			 ; return adjustment
	bsr.l	bos_ptos
	move.l	d5,a2
	move.w	(a2)+,d2
	rts

bo_index
	bsr.s	bos_pspr		 ; fetch index and prepare string
	subq.w	#1,d4			 ; index from zero
	cmp.b	d2,d4			 ; in range?
	bhs.s	bos_inor		 ; ... no
	move.b	(a2,d4.w),d0		 ; get indexed character now... ** 1.01 **
	clr.b	-(a1)			 ; ... because this might overwrite it
	move.b	d0,-(a1)
	move.l	#ar.strng<<16+1,-(a1)	 ; string with 1 character
	jmp	(a5)

bo_indvt
	bsr.s	bos_pspr		 ; fetch index and prepare string
	move.w	d4,d3
	move.w	d2,d4
	subq.w	#1,d3			 ; index from zero
	bge.s	bsi_indx		 ; ... ok
	bra.s	bos_inor

bo_indtv
	bsr.s	bos_pspr		 ; fetch index and prepare string
	moveq	#0,d3
	cmp.w	d2,d4			 ; end position in range
	bls.s	bsi_indx		 ; ... ok
	bra.s	bos_inor

bo_indvtv
	bsr.s	bos_psp2		 ; fetch index range and prepare string
	subq.w	#1,d4			 ; index from zero
	blt.s	bos_inor
	move.w	d4,d3
	swap	d4
	cmp.w	d2,d4			 ; end position in range
	bhi.s	bos_inor

bsi_indx
	move.w	d4,d2
	sub.w	d3,d2			 ; number of chars
	ble.s	bos_zinor		  ; ...oops
	move.l	d2,d1
	subq.w	#1,d1
	moveq	#1,d0
	and.w	d2,d0			 ; odd char

	add.w	d4,a2			 ; start of copy in string
	cmp.l	a1,a2			 ; here?
	beq.s	bsi_fwd 		 ; ... yes, strings could overlap

	sub.l	d0,a1			 ; start of copy in stack

bsi_loop
	move.b	-(a2),-(a1)
	dbra	d1,bsi_loop

bsi_done
	move.w	d2,-(a1)
	move.w	#ar.strng,-(a1)
	jmp	(a5)


bsi_fwd
	sub.w	d2,a1			 ; start at other end
	sub.l	d0,a1

	sub.w	d2,a2
	move.l	a1,a0			 ; keep a1 safe
bsi_floop
	move.b	(a2)+,(a0)+
	dbra	d1,bsi_floop
	bra.s	bsi_done

bos_zinor
	beq.s	bsi_done
bos_inor
	bra.l	bel_inor


;******************************************************************************
;
;	String Concatenate and Search
;


; Prepare string operations (d4) pointer to string TOS, (d5) pointer to string
; NOS and (a1) pointer to the arithmetic stack with the two strings 'popped'
; Any parameters which are not in the form of a string, are converted to strings
; in the SBASIC buffer. There is a problem in concatenating strings on the
; arithmetic stack: in the final string the order is NOS, TOS. For this reason,
; this routine returns d3
;
;	0 if neither string is on the stack (the most likely case),
;	2 if TOS only is on the stack,
;	4 if NOS only is on the stack,
;	6 if both are on the stack.
;
;
bos_prep
	moveq	#2,d2			 ; buffer position
	moveq	#0,d3			 ; return value
	moveq	#2,d1			 ; return adjustment
	bsr.s	bos_ptos

	moveq	#4,d1			 ; adjustment for NOS
	move.l	d5,d4

bos_ptos
	move.w	(a1)+,d0		 ; type of TOS
	blt.s	bos_pst1		 ; pointer to string, great!
	subq.w	#ar.float,d0		 ; string FP or int
	blt.s	bos_sst1		 ; stacked string is bad news
	bsr.s	bos_conv
	sub.l	a2,a0			 ; length
	move.w	a0,-(a2)
	move.l	a2,d5
	rts

bos_conv
	move.l	sb_buffb(a6),a0 	 ; buffer base
	add.l	a6,a0
	add.l	d2,a0			 ; plus offset
	moveq	#$22,d2 		 ; next time it is larger
	subq.w	#ar.int-ar.float,d0	 ;
	blt.s	bos_fp			 ; bad bad, need lots of stack
	move.l	a0,a2
	beq.l	ca_iwdec		 ; int
	bgt.l	ca_ildec		 ; long

bos_fp
	move.l	sb_cmdlb(a6),a2 	 ; transfer stack to here
	lea	-6(a6,a2.l),a2
	move.w	(a1)+,(a2)
	move.l	(a1)+,2(a2)
	move.l	a1,-(sp)		 ; save a1
	move.l	a2,a1
	move.l	a0,a2
	jsr	ca_fpdec		 ; floating point
	move.l	(sp)+,a1
	rts

bos_sst1
	add.l	d1,d3			 ; flag the bad news
	move.l	a1,d5			 ; TOS is here
	moveq	#1,d0
	add.w	(a1)+,d0
	bclr	#0,d0
	add.w	d0,a1			 ; point to NOS
	rts

bos_pst1
	move.l	(a1)+,d5		 ; TOS is here
	rts

;--------------------------------
bo_concat			 ; concatenate strings
	bsr.s	bos_prep
	move.l	d4,a0
	move.l	d5,a2			 ; our working address regs
	move.w	(a0)+,d4		 ; length of TOS
	moveq	#0,d5
	move.w	(a2)+,d5		 ; and TOS (long)
	move.l	d5,d7
	add.w	d4,d7			 ; total string length (long)
	cmp.w	#$7ffd,d7
	bhi	bo_iovfl		 ; ... string overflow
	moveq	#1,d0
	and.w	d7,d0
	sub.w	d0,a1			 ; end of final string

	subq.w	#4,d3			 ; how bad is stack?
	blt.s	boc_alfs		 ; not at all, but we need to allocate
	beq.s	boc_nmov		 ; NOS is on, we may need to move it

	move.l	d7,d1			 ; allocate space for final string
	add.l	d5,d1			 ; plus NOS
	bsr.s	bos_alar

	move.l	a1,d1			 ; save the stack pointer

	sub.l	d7,a1			 ; save the NOS characters here
	sub.l	d5,a1
	subq.l	#6,a1
	move.w	d5,d0
	bra.s	boc_snle
boc_snlp
	move.b	(a2)+,(a1)+
boc_snle
	dbra	d0,boc_snlp

	move.l	a1,a2			 ; new top of NOS
	move.l	d1,a1			 ; restore stack pointer
	bra.s	boc_ctos		 ; and concatenate

boc_nmov
	move.l	d7,d1			 ; allocate space for final string
	bsr.s	bos_alar
	sub.w	d7,a1			 ; start of final characters
	cmp.l	a1,a2			 ; pathological case?
	beq.s	boc_mtpath		 ; ... yes
	bra.s	boc_mnle		 ; ... no, move NOS to right place
boc_mnlp
	move.b	(a2)+,(a1)+
boc_mnle
	dbra	d5,boc_mnlp

	bra.s	boc_mtle		 ; and move TOS onto the end

boc_mtpath
	add.w	d5,a1			 ; move to end without copying
	bra.s	boc_mtle

boc_mtlp
	move.b	(a0)+,(a1)+
boc_mtle
	dbra	d4,boc_mtlp

	sub.w	d7,a1
	bra.s	boc_push


boc_alfs
	add.w	d5,a2			 ; back end of nos
	add.l	d7,d1			 ; allocate space for final string
	bsr.s	bos_alar
boc_ctos
	add.w	d4,a0			 ; copy tos
	bra.s	boc_ctle
boc_ctlp
	move.b	-(a0),-(a1)
boc_ctle
	dbra	d4,boc_ctlp

	bra.s	boc_cnle		 ; copy nos
boc_cnlp
	move.b	-(a2),-(a1)
boc_cnle
	dbra	d5,boc_cnlp

boc_push
	move.w	d7,-(a1)		 ; string length
	move.w	#ar.strng,-(a1) 	 ; and type
	jmp	(a5)

bos_alar
	move.l	d6,d0			 ; stack limit
	add.l	d1,d0			 ; + string
	cmp.l	d0,a1			 ; enough room?
	blt	bo_alard		 ; ... no, alard will add a bit of spare
	rts

;--------------------------------
bo_instr			 ; find string within string
	bsr.l	bos_prep
	move.l	a1,d7			 ; save stack pointer
	move.l	d4,a0
	move.l	d5,a1			 ; our working address regs

	tst.b	sb_cinst(a6)		 ; case dependent?
	bmi.s	bois_cinstr		 ; ... yes
	jsr	uq_instr		 ; use instr
	bra.s	bois_tidy
bois_cinstr
	jsr	uq_cinstr
bois_tidy
	move.l	d7,a1
	move.w	d0,-(a1)
	move.w	#ar.int,-(a1)
	jmp	(a5)

;******************************************************************************
;
;	Bitwise Logical Operations
;
;	First op may be int or long, second must be the same
;
boi_int
	addq.w	#ar.int-ar.strng,d3	 ; string?
	ble	bo_stint		 ; ... yes, convert to int
	jsr	qa_nint 		 ; ... nearest integer
	bne.l	sb_istop
	rts

bob_pre
	move.w	(a1)+,d3
	subq.w	#ar.int,d3		 ; first is int?
	beq.s	bob_intt		 ; ... yes
	bgt.s	bob_ltos		 ; ... long
	bsr.s	boi_int
bob_intt
	move.w	(a1)+,d2
bob_nos
	move.w	(a1)+,d3
	subq.w	#ar.int,d3		 ; second is word?
	beq.s	bob_intn		 ; ... yes
	bgt.s	bob_lnos		 ; ... no
	bsr.s	boi_int
bob_intn
	move.w	(a1)+,d1
	jmp	2(a2)

bob_ltos
	move.l	(a1)+,d2		 ; long op
	move.w	d2,a0
	cmp.l	d2,a0
	beq.s	bob_nos
bob_iovfl
	bra	bo_iovfl

bob_lnos
	move.l	(a1)+,d1		 ; long op
	move.w	d1,a0
	cmp.l	d1,a0
	bne.s	bob_iovfl
	jmp	2(a2)

;--------------------------------
bo_band 			 ; bitwise and
	bra.s	bob_pre
	and.w	d2,d1
	bra.s	bo_pw

;--------------------------------
bo_bor				 ; bitwise or
	bra.s	bob_pre
	or.w	d2,d1
	bra.s	bo_pw
	jmp	(a0)

;--------------------------------
bo_bxor 			 ; bitwise xor
	bra.s	bob_pre
	eor.w	d2,d1
	bra.s	bo_pw


;******************************************************************************
;
;	Logical Operations
;
;	All types of operand are converted to integer 0 or 1
;
bol_pre
	bsr.s	bol_one 		 ; one operand
	move.w	d1,d2			 ; TOS in D2
bol_one
	move.w	(a1)+,d3
	subq.w	#ar.int,d3		 ; is int?
	beq.s	bol_int 		 ; ... yes
	bgt.s	bol_long		 ; ... long
	addq.w	#ar.int-ar.strng,d3
	bgt.s	bol_float		 ; it is a float
	bsr	bo_stfp 		 ; or it will be
bol_float
	addq.l	#2,a1
bol_long
	tst.l	(a1)+
	bne.s	bol_true
	moveq	#0,d1
	rts
bol_int
	tst.w	(a1)+
	bne.s	bol_true
	moveq	#0,d1
	rts
bol_true
	moveq	#1,d1
	rts

;--------------------------------
bo_and				 ; logical and
	bsr.s	bol_pre
	and.w	d2,d1
	bra.s	bo_pw			 ; push word

;--------------------------------
bo_or				 ; logical or
	bsr.s	bol_pre
	or.w	d2,d1
	bra.s	bo_pw			 ; push word

;--------------------------------
bo_xor				 ; logical xor
	bsr.s	bol_pre
	eor.w	d2,d1
	bra.s	bo_pw			 ; push word

boc_pw
	and.w	#1,d1
bo_pw
	move.w	d1,-(a1)		 ; push word
	move.w	#ar.int,-(a1)
	jmp	(a5)

;******************************************************************************
;
;	Comparison Operators
;
;	The variable types are coerced using one the scales of precedence
;
;	float		      float
;	long		      string
;	int
;
;	Int and long are treated as equivalent.
;	Otherwise, if they are different, they are coerced to float

;--------------------------------
bo_cpaeq			 ; compare approximately equal
	bsr.s	boc_pre 		 ; prepare
	bsr.l	boc_appeq		 ; compare approximately
	seq	d1
	bra.s	boc_pw
;--------------------------------
bo_cpeq 			 ; compare equal
	bsr.s	boc_prec		 ; prepare and compare
	seq	d1
	bra.s	boc_pw
;--------------------------------
bo_cpne 			 ; compare not equal
	bsr.s	boc_prec		 ; prepare and compare
	sne	d1
	bra.s	boc_pw
;--------------------------------
bo_cpgt 			 ; compare greater than
	bsr.s	boc_prec		 ; prepare and compare
	sgt	d1
	bra.s	boc_pw
;--------------------------------
bo_cpge 			 ; compare greater than or equal
	bsr.s	boc_prec		 ; prepare and compare
	sge	d1
	bra.s	boc_pw
;--------------------------------
bo_cple 			 ; compare less than or equal
	bsr.s	boc_prec		 ; prepare and compare
	sle	d1
	bra.s	boc_pw
;--------------------------------
bo_cplt 			 ; compare less than
	bsr.s	boc_prec		 ; prepare and compare
	slt	d1
	bra.s	boc_pw

;	The preparatory code determines the type and value of each operand
;
;			   float      long	 int	    string
;	NOS d1	TOS d4	   mantissa   value.l	 value.l    pointer
;	    d2	    d5	   exponent
;	    d3	    d7	   1	      0 	 0	    -1
;
boc.fp	equ	1
boc.int equ	0
boc.str equ	-1

boc_stfp
	moveq	#0,d7
	move.w	(a0)+,d7		 ; length of string
	add.l	a0,d7
	jmp	ca_decfp		 ; convert to floating point

boc_prec
	pea	boc_cmp 		 ; compare on return
boc_pre
	bsr.s	boc_one 		 ; get TOS
	move.l	d1,d4			 ; save these to get the other one
	move.w	d2,d5
	move.w	d3,d7
	bsr.s	boc_one 		 ; get NOS

	cmp.b	d3,d7			 ; type match
	bne.s	boc_coerce		 ; no
	rts

boc_coerce
	move.l	a1,a2			 ; save stack pointer
	move.l	sb_buffb(a6),a1 	 ; use buffer - for conversions
	lea	$60(a6,a1.l),a1 	 ; enough
	tst.b	d3			 ; was NOS float
	bgt.s	boc_atos		 ; ... yes
	beq.s	boc_inos		 ; ... int / long int
	move.l	d1,a0			 ; pointer to string
	move.b	d7,d3			 ; save TOS key
	bsr.s	boc_stfp		 ; string to float
	bne.s	boc_err
	move.w	(a1)+,d2
	move.l	(a1)+,d1		 ; result
	move.b	d3,d7			 ; and TOS key
	bra.s	boc_atos

boc_inos
	move.l	d1,-(a1)
	jsr	qa_lfloat		 ; float long int
	move.w	(a1)+,d2
	move.l	(a1)+,d1		 ; result

boc_atos
	tst.b	d7			 ; was TOS float
	bgt.s	boc_rtsa1		 ; ... yes
	beq.s	boc_itos		 ; ... int
	move.l	d4,a0			 ; pointer to string
	bsr.s	boc_stfp		 ; string to float
	beq.s	boc_ptos
boc_err
	jmp	sb_istop		 ; error in string to float conversion

boc_itos
	move.w	d4,-(a1)
	jsr	qa_float		 ; float int
boc_ptos
	move.w	(a1)+,d5
	move.l	(a1)+,d4		 ; result
	moveq	#boc.fp,d7
boc_rtsa1
	move.l	a2,a1
	rts

boc_one
	move.w	(a1)+,d0		 ; type of first value
	subq.w	#ar.float,d0		 ; hope its a float?
	beq.s	boc_fp			 ; ... yes, pop it
	bgt.s	boc_int 		 ; some type of integer

	moveq	#boc.str,d3		 ; some sort of string
	addq.w	#ar.float,d0
	blt.s	boc_plng		 ; pointer to string
	move.l	a1,d1			 ; string on stack
	move.w	(a1)+,d0
	addq.w	#1,d0
	bclr	#0,d0
	add.w	d0,a1			 ; next on stack
	rts

boc_int
	moveq	#boc.int,d3		 ; integer
	subq.w	#ar.int-ar.float,d0	 ; type of int
	bgt.s	boc_plng
	move.w	(a1)+,d1		 ; short
	ext.l	d1			 ; ... extended
	rts

boc_plng
	move.l	(a1)+,d1		 ; long or pointer to string
	rts

boc_fp
	moveq	#boc.fp,d3		 ; it's a float
	move.w	(a1)+,d2
	move.l	(a1)+,d1
	rts

boc_cmp
	tst.b	d7			 ; type of comparison
	bgt.s	boc_cfp 		 ; floating point
	blt.s	boc_str 		 ; string
boc_long
	cmp.l	d4,d1			 ; NOS vs TOS
	rts

boc_cfp
	move.l	d1,d0			 ; try mantissas
	sub.l	d4,d0
	bvs.s	boc_rts 		 ; if overflow, they were opposite sign
	tst.l	d1			 ; are we comparing negatives
	beq.s	boc_cfpt		 ; NOS is zero, comparison is -TOS
	tst.l	d4
	beq.s	boc_cfpn		 ; TOS is zero, comparison is NOS
	bmi.s	boc_cfxr		 ; mantissa is negative, reverse exp
	cmp.w	d5,d2
	bne.s	boc_rts 		 ; exponents are different
	cmp.l	d4,d1			 ; exponents the same, compare mantissas
	rts
boc_cfxr
	cmp.w	d2,d5			 ; -ve mantissa, reverse exponent check
	bne.s	boc_rts 		 ; exponents are different
	cmp.l	d4,d1			 ; exponents the same, compare mantissas
	rts
boc_cfpt
	neg.l	d4			 ; check is -TOS
	rts
boc_cfpn
	tst.l	d1			 ; check is +TOS
boc_rts
	rts

boc_appeq			 ; approximately equals
	tst.b	d7			 ; type of comparison
	beq.s	boc_long
	blt.s	boc_astr		 ; string

	move.l	d4,-(a1)
	move.w	d5,-(a1)
	jsr	qa_subd 		 ; subtract
	move.w	(a1)+,d0		 ; new exponent
	move.l	(a1)+,a0		 ; (keep ccr)
	beq.s	boc_rts 		 ; 0 is good enough for me
	add.w	#23,d0			 ; difference * appx 10^7
	cmp.w	d2,d0			 ; is it smaller than NOS?
	blt.s	boc_az			 ; ... yes
	cmp.w	d5,d0			 ; is it smaller than TOS?
	ble.s	boc_az			 ; ... yes
	move.l	a0,d0
	neg.l	d0			 ; ... no, our subtraction was wrong way
	rts
boc_az
	moveq	#0,d0
	rts

boc_astr
	moveq	#utc.ncnr,d0		 ; approximate equality
	bra.s	boc_cstr
boc_str
	moveq	#utc.nmbr,d0		 ; normal comparison
boc_cstr
	move.l	a1,d7
	move.l	d1,a0
	move.l	d4,a1			 ; compare strings registers
	jsr	uq_cstra		 ; compare strings
	move.l	d7,a1
	rts

;******************************************************************************
;

;--------------------------------
bo_asgs 			 ; assign to string variable
	jmp	sb_ienimp

; finds a string and sets d5 to value of a1 on exit
;
;	d2  r	length of string
;	d5  r	updated stack
;	a0    s
;	a1 cr	pointer to item type / pointer to characters of string
;
sb_fstr
	move.w	(a1)+,d0
	subq.w	#ar.strng,d0
	bmi.s	bias_ptr		 ; ... pointer to string
	bne.s	bias_conv

	moveq	#1,d5			 ; ret
	move.w	(a1)+,d2
	add.w	d2,d5
	add.l	a1,d5			 ; stack after popping string
	bclr	#0,d5
	rts


bias_conv
	move.l	sb_buffb(a6),a0 	 ; use buffer
	add.l	a6,a0
	move.l	a0,d2

	move.l	d1,-(sp)
	pea	bias_a0
	subq.w	#ar.int-ar.strng,d0	 ; type to convert from
	blt.l	ca_fpdec		 ; float
	beq.l	ca_iwdec		 ; word
	bra.l	ca_ildec		 ; long int
bias_a0
	move.l	(sp)+,d1
	move.l	a1,d5			 ; save stack pointer
	move.l	d2,a1			 ; and fiddle pointer
	move.l	a0,d2
	sub.l	a1,d2
	rts

bias_ptr
	move.l	(a1)+,a0		 ; set pointer to string
	move.l	a1,d5			 ; save stack pointer
	move.l	a0,a1
	move.w	(a1)+,d2		 ; length of string
	rts


;--------------------------------
bo_asgf 			 ; assign to floating point
;--------------------------------
bo_asgi 			 ; assign to integer variable
;--------------------------------
bo_asgl 			 ; assign to long integer variable
;--------------------------------
bo_asgv 			 ; assign to variable
	jmp	sb_ienimp

sb_basg
	moveq	#ern4.basg,d0
	jmp	sb_ierset

;--------------------------------
bo_asgu 			 ; assign to unknown
	move.w	(a4)+,d3
	move.b	nt_nvalp(a3,d3.w),d1	 ; usage
	move.w	#1<<nt.var+1<<nt.rep+1<<nt.for,d0
	btst	d1,d0
	bne.s	bia_type		 ; ... ok, it's a variable
	subq.b	#nt.arr,d1		 ; array
	blt.s	bia_vset		 ; ... no, it was null or unset
	bne.s	sb_basg 		 ; ... nothing at all

	move.l	nt_value(a3,d3.w),a0
	move.l	(a0)+,a2		 ; pointer to value
	cmp.w	#1,(a0)+		 ; one dim?
	bne.s	sb_basg 		 ; ... no

	moveq	#$f,d0
	and.b	nt_vtype(a3,d3.w),d0
	subq.b	#nt.st,d0		 ; string?
	beq.l	bels_gstr		 ; ... yes, assign to it
	bgt.s	sb_basg 		 ; ... no

	move.w	(a0),d4 		 ; size of substring
	bsr	sb_fstr 		 ; we need string
	move.l	a2,a0			 ; pointer to chars
	bra.l	belss_asg		 ; assign it

bia_vset
	move.w	#$0f0f,d0
	and.w	nt_usetp(a3,d3.w),d0	 ; assign to null?
	beq.s	bia_asgn		 ; ... yes, forget it
	move.b	#nt.var,nt_nvalp(a3,d3.w) ; it is now variable

bia_type
	moveq	#$f,d0
	and.b	nt_vtype(a3,d3.w),d0	 ; variable type
	subq.w	#nt.fp,d0		 ; most common type of variable
	beq.s	bia_fp
	bgt.s	bia_in

bia_str
	bsr	sb_fstr 		 ; get string pointers

	moveq	#dt_stchr-dt_stalc+7,d1  ; add 6 bytes and round up
	add.w	d2,d1
	and.w	#$fff8,d1		 ; to multiple of 8
	move.l	nt_value(a3,d3.w),a0	 ; existing allocation
	cmp.w	dt_stalc(a0),d1 	 ; variable allocation, enough?
	bls.s	bias_set

	move.l	a0,d4			 ; save base of old allocation

	jsr	sb_aldat		 ; allocate hole
	assert	dt_stalc+4,dt_flstr+1,dt_stlen

	move.w	d1,(a0)+		 ; allocation
	move.w	#$00ff,(a0)+		 ; flags
	move.l	a0,nt_value(a3,d3.w)	 ; where the item is

	exg	a0,d4
	subq.l	#-dt_stalc,a0
	move.w	(a0),d1
	jsr	sb_redat		 ; release old allocation
	move.l	d4,a0

bias_set
	move.w	d2,(a0)+		 ; length
bias_copy
	move.w	(a1)+,(a0)+		 ; copy bytes
	subq.w	#2,d2			 ; two at a time
	bgt.s	bias_copy

	move.l	d5,a1			 ; set arith stack pointer
bia_asgn
	jmp	(a5)

bia_in
	move.w	(a1)+,d1		 ; type of expression
	subq.w	#ar.int,d1		 ; correct type?
	beq.s	biai_do
	bgt.s	biai_lint		 ; long integer
	addq.w	#ar.int-ar.strng,d1	 ; string?
	ble.s	biai_stint		 ; ... yes
	jsr	qa_nint 		 ; ... no, float
	beq.s	biai_do
	jmp	sb_istop

biai_stint
	pea	biai_do
	bra	bo_stint

biai_lint
	move.l	(a1)+,d1
	move.w	d1,d0
	ext.l	d0
	cmp.l	d0,d1			 ; long to int truncated?
	bne	bo_iovfl		 ; ... yes
	subq.l	#2,a1			 ; ... ok, leave it

biai_do
	move.l	nt_value(a3,d3.w),a0	 ; existing allocation
	move.w	(a1)+,(a0)		 ; value
	jmp	(a5)

bia_fp
	move.w	(a1)+,d1		 ; type of expression
	subq.w	#ar.float,d1		 ; correct type?
	beq.s	biaf_do
	pea	biaf_do
	bgt.s	biaf_float

	addq.w	#ar.float,d1		 ; convert from string
	bra	bo_stfp

biaf_float
	subq.w	#ar.int-ar.float,d1	 ; int?
	beq	qa_float		 ; ... yes
	bra	qa_lfloat		 ; ... no, long

biaf_do
	move.l	nt_value(a3,d3.w),a0	 ; existing allocation
	move.w	(a1)+,(a0)+		 ; value
	move.l	(a1)+,(a0)+
	jmp	(a5)


;--------------------------------
bo_asgel			 ; assign to array element
	move.w	(a4)+,d3		 ; name index
	moveq	#0,d4
	move.w	(a4)+,d4		 ; number of indices
	move.w	(a4)+,d5		 ; number of values
	beq.s	bel_sstack

	move.l	a6,a2			 ; use buffer to convert
	add.l	sb_cmdlb(a6),a2

bel_cmloop
	cmp.w	#ar.int,(a1)+		 ; is it an integer already?
	beq.s	bel_move		 ; ... yes
	jsr	sb_fint
bel_move
	move.w	(a1)+,-(a2)		 ; move
	subq.w	#1,d5
	bgt.s	bel_cmloop

bel_sstack
	move.b	nt_nvalp(a3,d3.w),d0	 ; usage
	moveq	#$f,d1
	and.b	nt_vtype(a3,d3.w),d1	 ; and type
	move.l	nt_value(a3,d3.w),a0

	subq.b	#nt.arr,d0		 ; array?
	bne.l	bel_var 		 ; ... no

	move.l	(a0)+,-(sp)		 ; base of data
	moveq	#0,d3			 ; and offset

	subq.b	#nt.fp,d1
	blt.s	bel_stra		 ; string array

	cmp.w	(a0)+,d4		 ; the right number of indices?
	bne.l	bel_iind
	move.l	(a4)+,d0		 ; index types
	bne.l	bel_iind

	bsr.l	bo_arroff		 ; calculate offset

	move.l	(sp)+,a2		 ; base in address reg
	tst.b	d1			 ; was it FP?
	bgt.s	beli_get		 ; ... no, int

	add.l	d3,d3
	move.l	d3,d0
	add.l	d3,d3
	add.l	d0,d3			 ; 6*offset
	add.l	d3,a2			 ; address

	cmp.w	#ar.float,(a1)+ 	 ; float already?
	beq.s	belf_set		 ; ... yes
	bsr	bo_anyfp2		 ; ... no, convert it
belf_set
	move.w	(a1)+,(a2)+		 ; set fp
	move.l	(a1)+,(a2)
	jmp	(a5)

beli_get
	add.l	d3,d3
	add.l	d3,a2
	cmp.w	#ar.int,(a1)+		 ; is it an integer already?
	beq.s	beli_set		 ; ... yes
	jsr	sb_fint
beli_set
	move.w	(a1)+,(a2)		 ; set int
	jmp	(a5)

bel_stra
	move.w	(a0)+,d0
	sub.w	d4,d0			 ; the right number of indices?
	beq.s	bel_sas 		 ; ... yes, substring
	subq.w	#1,d0			 ; one spare?
	bne.l	bel_iind4		 ; ... no
	move.l	(a4)+,d0		 ; index types
	bne.l	bel_iind4

	bsr.l	bo_arrsoff		 ; string offset (one fewer dimensions)

	move.l	(sp)+,a2
	add.l	d3,a2

bels_gstr
	move.l	a0,a5			 ; save next dimension
	bsr	sb_fstr
	move.l	a5,a0

	moveq	#0,d3
	move.w	(a0)+,d3		 ; the last dimension
	sub.w	d3,d2			 ; truncate?
	bhi.s	bels_set
	lea	2(a2),a0		 ; space fill
	cmp.l	a1,a0			 ; in place?
	beq.s	bels_done		 ; ... yes
	add.w	d3,a0
	add.w	d2,d3			 ; string length

	move.l	#'    ',d0		 ; long word may overwrite length

bels_clear
	move.l	d0,-(a0)		 ; clear a long word at a time
	addq.w	#4,d2
	bmi.s	bels_clear

bels_set
	move.w	d3,(a2)+		 ; length
	ror.l	#1,d3
	bra.s	bels_ecopy
bels_copy
	move.w	(a1)+,(a2)+		 ; copy bytes
bels_ecopy
	dbra	d3,bels_copy

	tst.l	d3			 ; odd nr of chars
	bpl.s	bels_done		 ; no last
	move.b	(a1)+,(a2)+		 ; add last character

bels_done
	move.l	d5,a1
	jmp	sb_iloop5

bel_sas
	move.l	d1,d0
	move.l	(a4)+,d1		 ; index mask
	bne.s	bel_sbstr		 ; non zero, must be substring asg
	bsr.l	bo_arroff		 ; ordinary offset
	move.l	(sp)+,a0
	tst.w	d0			 ; last index zero?
	bne.s	bels_gchar		 ; ... no

	cmp.w	#ar.int,(a1)+		 ; is value an integer already?
	beq.s	bels_len		 ; ... yes
	jsr	sb_fint
bels_len
	move.w	(a1)+,d0
	bmi.s	bels_or
	move.w	d0,(a0,d3.l)		 ; ... yes, set int
	jmp	(a5)

bels_or
	moveq	#err.orng,d0		 ; out of range
	jmp	sb_istop

bels_gchar
	move.l	a0,a5			 ; save array base
	bsr	sb_fstr 		 ; and fetch string pointers
	move.l	a5,a0

bels_schar
	moveq	#' ',d1 		 ; assume null string
	tst.w	d2			 ; any char in string?
	beq.s	bels_pchar		 ; ... no
	move.b	(a1),d1 		 ; ... yes
bels_pchar
	move.b	d1,1(a0,d3.l)		 ; set char
	move.l	d5,a1
	jmp	sb_iloop5

bel_sbstr
	addq.w	#nt.fp-nt.st,d0 	 ; string or substring
	lsl.l	#1,d4
	lsr.w	#1,d0
	roxr.l	#1,d4			 ; substring flag in msb d4

	moveq	#0,d3
	subq.w	#1,d4			 ; calculate offset not using last ind
	beq.s	bel_sbadd		 ; ... no others
	move.w	d4,d0
	lsl.w	#2,d0
	asr.l	d0,d1			 ; last mask in lsb
	bsr.l	bo_arrsoff
bel_sbadd
	add.l	(sp)+,d3		 ; address
	move.w	(a0)+,d4		 ; max index

	bsr	sb_fstr 		 ; fetch string pointers
	move.l	d3,a0			 ; string to set

belss_from
	moveq	#1,d3			 ; assume from 1
	lsr.w	#2,d1			 ; get from bit in carry
	bcc.s	belss_to		 ; default
	move.w	(a2)+,d3		 ; real start
	beq.s	bel_inor		 ; ... oops
belss_to
	lsr.w	#1,d1
	bcc.s	belss_defto		 ; default to
	move.w	(a2)+,d0		 ; real to
	cmp.w	d4,d0			 ; in range?
	bls.s	belss_sto
	bra.s	bel_inor		 ; ... no
belss_defto
	move.w	d4,d0			 ; dimension
	tst.l	d4			 ; string?
	bmi.s	belss_sto		 ; ... no
	move.w	(a0),d0 		 ; ... yes, use string length
belss_sto
	move.w	d0,d4			 ; ... yes, use it
	subq.w	#1,d3			 ; real start
	sub.w	d3,d4			 ; length of substring
	ble.s	bel_zok 		 ; ... none

	add.w	d3,a0			 ; start of copy
	tst.l	d4			 ; subsrtring?
	bmi.s	belss_asg		 ; ... yes, assign
	addq.l	#2,a0			 ; ... no, move to characters
;+++
; assign string to substring
;	d2	length of string
;	d4	length of sub-string
;	d5	a1 on exit
;	a0	chars of string
;	a1	chars of sub-string
;---
belss_asg
	cmp.w	d4,d2			 ; is string long enough?
	bge.s	belss_allss		 ; ... yes
	sub.w	d2,d4			 ; spaces to fill
	bra.s	belss_chars

belss_cloop
	move.b	(a1)+,(a0)+		 ; copy character
belss_chars
	dbra	d2,belss_cloop

	subq.w	#1,d4
	moveq	#' ',d0
belss_sloop
	move.b	d0,(a0)+		 ; copy space
	dbra	d4,belss_sloop
	move.l	d5,a1
	jmp	sb_iloop5

belss_aloop
	move.b	(a1)+,(a0)+		 ; copy character
belss_allss
	dbra	d4,belss_aloop
	move.l	d5,a1
	jmp	sb_iloop5

bel_zok
	move.l	d5,a1
	beq.l	sb_iloop5		 ; OK continue
	bra.s	bel_iind
bel_iind4
	addq.l	#4,sp
bel_iind
	moveq	#ern4.iind,d0
	bra.s	bel_ierset

bel_inor
	moveq	#ern4.inor,d0
	bra.s	bel_ierset

bel_narr
	moveq	#ern4.narr,d0
bel_ierset
	jmp	sb_ierset

barr_loop
	move.w	(a2)+,d0		 ; next index
	cmp.w	(a0)+,d0		 ; in range?
	bhi	bel_inor		 ; ... no
	mulu	(a0)+,d0
	add.l	d0,d3			 ; offset
bo_arroff
	subq.w	#1,d4
	bgt.s	barr_loop

	moveq	#0,d0
	move.w	(a2)+,d0		 ; last index
	cmp.w	(a0)+,d0		 ; in range?
	bhi	bel_inor		 ; ... no
	add.l	d0,d3			 ; offset
	rts

bo_arrsoff
bars_loop
	move.w	(a2)+,d0		 ; next index
	cmp.w	(a0)+,d0		 ; in range?
	bhi	bel_inor		 ; ... no
	mulu	(a0)+,d0
	add.l	d0,d3			 ; offset
	subq.w	#1,d4
	bgt.s	bars_loop
	rts

bel_var
	addq.b	#nt.arr-nt.var,d0	 ; variable?
	bne.s	bel_narr		 ; ... no
	subq.b	#nt.st,d1		 ; must be string
	bne.s	bel_narr		 ; ... no

	subq.w	#1,d4			 ; one index only please
	bne.s	bel_iind		 ; ... oops

	move.l	a0,a5			 ; save array base
	bsr	sb_fstr 		 ; fetch string pointers
	move.l	a5,a0

	move.w	(a0),d4 		 ; limit
	move.l	(a4)+,d1		 ; range?
	bne	belss_from		 ; ... yes
	moveq	#0,d3
	move.w	(a2)+,d3		 ; index
	bgt	bels_schar		 ; set character
	bra	bel_iind
	end
