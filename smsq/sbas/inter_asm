; SBAS_INTER - Interpreter Main Loop and Tables         1993 Tony Tebby
;
; 2017-04-05  1.01  Fixed LRESPR-within-PROCedure crash (MK)

	section sbas

	xdef	sb_inter    ; interpreter
	xdef	sb_ixtable  ; interpreter token extension table

	xdef	sb_ireset   ; interpreter loop, resets d6,a3,a5, checks break
	xdef	sb_ibreak   ; interpreter loop, checks break
	xdef	sb_iloop5   ; interpreter loop (useful if a5 smashed)
	xdef	sb_iloop    ; interpreter loop (useful for Bxx)

	xdef	sb_ienimp   ; interpreter "hole"
	xdef	sb_ierror   ; interpreter general error
	xdef	sb_ierset   ; interpreter error on stack
	xdef	sb_istop    ; interpreter stop (error in D0)
	xdef	sb_istopok  ; interpreter stop, OK

	xref	sb_sttadd   ; statement to address
	xref	sb_addstt   ; and vv

	xref	sb_ret2adr  ; return stack to absolute addresses

	xref	sb_loopadd  ; set loop var addresses
	xref	sb_loopstt  ; set loop var statement nrs

	xref	sb_dpset    ; set data pointer
	xref	sb_dlsiset  ; set data line, statement and item

	xref	sb_ermess   ; set interpreter message
	xref	sbi_alar    ; allocate initial arithmetic stack

	xref	sb_clrstk   ; clear all of stack
	xref	sb_clrcmp   ; clear all compiled bits
	xref	sb_clnret   ; clean up unused entries on return stack

	include 'dev8_keys_sbasic'
	include 'dev8_keys_err'
	include 'dev8_keys_err4'
	include 'dev8_smsq_sbas_inter_keys'
	include 'dev8_keys_qdos_sms'
	include 'dev8_mac_assert'

sb_ierror
	moveq	#ern4.fatl,d0		 ; fatal error
sb_ierset
	add.w	#err4.null,d0		 ; make standard error code
	bra.s	sb_istop

sb_ienimp
	moveq	#err.nimp,d0		 ; not implemented
	bra.s	sb_istop

sb_dobreak
	moveq	#err.nc,d0
	bra.s	sb_istop
sb_istopok
	moveq	#0,d0
sb_istop
	move.l	d0,-(sp)		 ; save error code

	move.l	sb_readp(a6),d0 	 ; in read?
	beq.s	sis_dline		 ; ... no
	move.l	d0,a4			 ; ... yes, restore address
	move.l	sb_readd(a6),sb_ndata(a6) ; and next data item
sis_dline
	jsr	sb_dlsiset		 ; set data line etc.

	clr.w	sb_line(a6)		 ; no line number

	tst.b	sb_cmdl(a6)		 ; command line?
	bgt.s	sis_setasp		 ; ... it will be, no clean up required
	beq.s	sis_clean		 ; ... no, always clean up
	tst.l	(sp)			 ; ... in command line, was it error?
	beq.s	sis_addr		 ; ... no, no automatic stack clean up
sis_clean
	jsr	sb_clnret		 ; clean up unused entries on ret stack

sis_addr
	move.l	a4,d0
	subq.l	#2,d0			 ; this is in "current" statement
	jsr	sb_addstt		 ; find line and statement number
	move.b	d0,sb_stmt(a6)		 ; current statement
	swap	d0
	move.w	d0,sb_line(a6)
	bgt.s	sis_arth		 ; in program

	tst.w	sb_cmdt2(a6)		 ; token offset required (for SBASIC fn)
	beq.s	sis_arth		 ; ... no

	move.l	a4,d0			 ; next token
	sub.l	a2,d0			 ; in this statement address
	move.w	d0,sb_cmdt2(a6)
	bra.s	sis_setasp

sis_arth
	tst.l	(sp)			 ; if no error, leave stack alone
	beq.s	sis_setasp
	moveq	#0,d7			 ; assume clean arithmetic stack

	move.l	sb_retsp(a6),a2
	bra.s	sis_rteloop
sis_rtloop
	move.b	rt_type(a6,a2.l),d0	 ; return stack type
	ble.s	sis_rtgsarr		 ; gosub or array
	sub.w	#rt.pfsize,a2
	cmp.b	#rt.fun,rt_type+rt.pfsize(a6,a2.l) ; is this function return stack entry?
	bne.s	sis_rteloop
	move.l	#$00ffffff,d7		 ; ... yes, this is arith stack size
	and.l	rt_arstk+rt.pfsize(a6,a2.l),d7
	bra.s	sis_rteasp

sis_rtgsarr
	assert	rt.gssize,rt.sasize
	subq.w	#rt.gssize,a2
sis_rteloop
	cmp.l	sb_retsb(a6),a2 	 ; end of rt stack?
	bgt.s	sis_rtloop		 ; ... no

sis_rteasp
	move.l	sb_arthb(a6),a1
	sub.l	d7,a1
	move.l	a1,sb_arthp(a6) 	 ; reset arithmetic stack
	bra.s	sis_setloop

sis_setasp
	sub.l	a6,a1			 ; save the current asp
	move.l	a1,sb_arthp(a6)

sis_setloop
	jsr	sb_loopstt		 ; convert loop addresses to statmt nrs

	tst.b	sb_inint(a6)		 ; failed with out of memory?
	blt.s	sis_nint		 ; ... no

	jsr	sb_clrcmp		 ; clear compiled bits

sis_nint
	sf	sb_inint(a6)		 ; no longer in interpreter

	move.l	(sp)+,d0
	move.l	sb_prstp(a6),a7 	 ; stack pointer
	add.l	a6,a7			 ; correct

	rts

;+++
; This is the SBASIC Interpreter Main Loop
;
; Routines called from this loop can assume the following register values
;
;	D6 is limit of arithmetic stack (with some bytes spare)
;	A1 is pointer to arithmetic stack
;	A2 is entry address
;	A3 is pointer to name table
;	A4 is pointer to program
;	A5 is address of next token loop (return address)
;	A6 is pointer to system variables
;
;---
sb_inter
	move.l	a7,d0
	sub.l	a6,d0			 ; relative pointer to stack
	move.l	d0,sb_prstp(a6) 	 ; save it

	tas	sb_break(a6)
	tas	sb_cont(a6)

	moveq	#sms.cbas,d0
	trap	#do.sms2		 ; set this to be "current" SBASIC

	jsr	sb_ret2adr		 ; ret stack to abs adresses	** 1.01
	jsr	sb_loopadd		 ; convert loop statements to address

	jsr	sbi_alar		 ; initial alloc of arithmetic stack

	jsr	sb_dpset		 ; set data pointer
	clr.l	sb_readp(a6)		 ; not in read command

	moveq	#0,d0
	move.w	sb_nline(a6),d0
	smi	sb_cmdl(a6)		 ; say command line
	bge.s	sbi_pstart		 ; in program
	moveq	#0,d0			 ; in command line

	move.b	sb_nstmt(a6),d0 	 ; statement in command line
	beq.s	sbi_cstart		 ; start at begining
	move.w	sb_cmdt2(a6),d5 	 ; in middle of statement?
	beq.s	sbi_nstart		 ; ... no, start at beginning of next
	clr.w	sb_cmdt2(a6)		 ; ... yes, clear flag
	move.w	d5,a4			 ; offset
	bra.s	sbi_stadd

sbi_cstart
	clr.w	sb_cmdt2(a6)		 ; not in the middle of a statement
sbi_pstart
	swap	d0
	move.b	sb_nstmt(a6),d0
sbi_nstart
	addq.w	#1,d0			 ; nstmt is numbered from 0
	sub.l	a4,a4			 ; no offset
sbi_stadd
	jsr	sb_sttadd		 ; find address
	add.l	d0,a4			 ; and add to offset

sbi_start

sb_ireset
	moveq	#ar.ovrn,d6		 ; arithmetic stack allowance
	add.l	sb_arthl(a6),d6 	 ; and limit
	add.l	a6,d6

	move.l	sb_nmtbb(a6),a3
	add.l	a6,a3			 ; name table
	lea	sb_iloop,a5

	st	sb_inint(a6)		 ; now in interpreter

bo_break
sb_ibreak
	tst.b	sb_break(a6)		 ; break?
	beq	sb_dobreak

	move.w	(a4)+,d0		 ; next token
	move.l	a5,a2
	add.w	(a5,d0.w),a2		 ; where to go
	jmp	(a2)

sb_iloop5
	lea	sb_iloop,a5
sb_iloop
	move.w	(a4)+,d0		 ; next token
	move.l	a5,a2
	add.w	(a5,d0.w),a2		 ; where to go
	jmp	(a2)

	dc.w	0,0			 ; a bit of padding to align table



; As well as the vector table, this produces a table which has a line number
; flag in the msbyte and a number of token extension bytes in the lsbyte

tinit	    macro
	section sbas_x
sb_ixtable
	dcb.b  sbi_table-sb_iloop,0
	section sbas
xtable	    setstr  { dc.b }
	    endm

vector	    macro   action, exten, linecase

	    ifnum   bo.[action] = *-sb_iloop  goto set_xtable
bo1 setnum bo.[action]
bo2 setnum *-sb_iloop
	    error   bo.[action] [bo1] is not vector bo_[action] [bo2]
set_xtable  maclab
name	    setstr  bo_[action]
	    ifstr [action] = break goto vec_def
	    xref    [name]    ;  [.def([name])]
vec_def     maclab

	    dc.w    [name]-sb_iloop

	    ifstr {[linecase]} <> {} goto settable
linecase    setstr  {0}
settable    maclab
xtable	    setstr    {[xtable][linecase],[exten],}
	    endm

tflush	    macro
	section sbas_x
 [.left(xtable,[.len(xtable)]-1)]
	section sbas
xtable	    setstr  { dc.b }
	    endm


;+++
; This is the vector table of SBASIC interpreter actions
;---
sbi_table
    tinit
	vector	mistake,0  ; give up

	vector	stop,0	   ; end of program

	vector	break,0    ; check for break

	vector	pushs,2    ; push values
	vector	pushf,2
	vector	pushi,2
	vector	pushl,2
	vector	pushv,2
	vector	pushu,2
	vector	pusha,bop.arrs
    tflush
	vector	pushcf,0   ; function call
	vector	pushsf,0
	vector	pushsfc,0
	vector	pushcfa,bop.arrs  ; function call
	vector	pushsfa,bop.arrs
	vector	pushsfac,bop.arrs
	vector	inline,4   ; inline function call

	vector	const,4    ; push constant
	vector	confp,6
	vector	conin,2
	vector	conli,4
    tflush
	vector	neg,0	   ; monadic arithmetic operations
	vector	bnot,0
	vector	not,0
	vector	index,0
	vector	indvt,0
	vector	indtv,0
	vector	indvtv,0
    tflush
	vector	add,0	   ; diadic arithmetic operations
	vector	sub,0
	vector	mul,0
	vector	divf,0
	vector	power,0
	vector	divi,0
	vector	mod,0
	vector	concat,0
	vector	instr,0
	vector	band,0
	vector	bor,0
	vector	bxor,0
	vector	and,0
	vector	or,0
	vector	xor,0
	vector	cpaeq,0
	vector	cpeq,0
	vector	cpne,0
	vector	cpgt,0
	vector	cpge,0
	vector	cple,0
	vector	cplt,0
    tflush
	vector	asgs,2		   ; assignments
	vector	asgf,2
	vector	asgi,2
	vector	asgl,2
	vector	asgv,2
	vector	asgu,2
	vector	asgel,bop.arrs

	vector	cpcall,2	   ; procedure, function and array setup
	vector	spcall,4,op.addr
	vector	spcallc,4
	vector	pcall,2
	vector	cfnref,4
	vector	sfnref,4,op.addr
	vector	sfnrefc,4
	vector	cfnaref,6
	vector	sfnaref,6,op.addr
	vector	sfnarefc,6

    tflush
	vector	stpar,6 	   ; parameters
	vector	fppar,8
	vector	inpar,4
	vector	nmpar,4
	vector	nfpar,6
	vector	arpar,bop.arrs+2
	vector	cfpar,4
	vector	sfpar,4
	vector	sfparc,4
	vector	cfapar,bop.arrs+4
	vector	sfapar,bop.arrs+4
	vector	sfaparc,bop.arrs+4
	vector	nlpar,2
	vector	expar,2
    tflush
	vector	stpin,6 	   ; parameters or indices
	vector	fppin,8
	vector	inpin,4
	vector	nmpin,4
	vector	nfpin,6
	vector	arpin,bop.arrs+2
	vector	cfpin,4
	vector	sfpin,4
	vector	sfpinc,4
	vector	cfapin,bop.arrs+4
	vector	sfapin,bop.arrs+4
	vector	sfapinc,bop.arrs+4
	vector	nlpin,2
	vector	expin,2
    tflush

	vector	docpr,0 	   ; routine calls
	vector	dospr,0
	vector	dosprc,0
	vector	dopr,2
	vector	dim,4

	vector	formp,-2	   ; formal parameter and locals
	vector	locvar,2
	vector	diml,0

	vector	return,0
	vector	retexp,0

	vector	skip6,0 	   ; internal jumps
	vector	rts,0
	vector	rts2,2
	vector	rts4,4
	vector	scrub8,8
	vector	gorel,2
	vector	ambs,4

    tflush
	vector	goadd,4,op.addr    ; gotos
	vector	ogoadd,2
	vector	goline,0
	vector	ogoline,2
	vector	golinec,0
	vector	ogolinec,2
	vector	gsadd,4,op.addr
	vector	ogsadd,6,op.addr
	vector	gsline,0
	vector	ogsline,6,op.addr
	vector	gslinec,0
	vector	ogslinec,6

	vector	goz,4,op.addr
	vector	gonz,4,op.addr

    tflush
	vector	selfp,0 	   ; SELect
	vector	selint,0
	vector	selend,0
	vector	on,8,op.addr
	vector	onxpr,8,op.addr
	vector	ofpr,12
	vector	ointr,4

    tflush
	vector	forst,8,op.addr    ; loops
	vector	forfp,8,op.addr
	vector	forint,8,op.addr
	vector	rep,6,op.addr
	vector	fstv,2
	vector	fstr,2
	vector	ffpv,2
	vector	ffpr,2
	vector	fintv,2
	vector	fintr,2
	vector	fciv,2
	vector	fcir,6
	vector	fend,0
	vector	endf,2
	vector	endr,2
	vector	next,2
	vector	exit,2
    tflush

	vector	restore,0	   ; data
	vector	readf,0
	vector	read,0
	vector	readd,0

	vector	wherr,2 	   ; when error
	vector	endwh,0
	vector	nowherr,0
    tflush
	end
