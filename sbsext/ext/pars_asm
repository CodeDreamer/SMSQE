* Type of parameters   V1.0    1985   Tony Tebby   QJUMP
* 2003-09-28		1.01		parnam$ checks for correct version (wl)

*	PARTYP (name)			return the type of the variable
*	PARUSE (name)			and its usage
*	PARNAM$ (number)		returns the name of the actual parameter
*	PARSTR$ (parm,number)		returns the string value or the name
*
	section exten
*
	xdef	partyp
	xdef	paruse
	xdef	parnam$
	xdef	parstr$
*
	xref	ut_par1 		check for just one parameter
	xref	ut_par2 		check for two parameters
	xref	ut_gxin1		get exactly one integer
	xref	ut_gtst1		get string
	xref	ut_gnmpt		get name from nt onto RI stack
	xref	ut_rtint		return integer d1
	xref	ut_rtstr		return string (a4)
	xref	ut_retst		return string on RI stack
	xref	zero_w			zero word
*
	include dev8_sbsext_ext_keys
*
partyp
	moveq	#1,d7			type is one byte on from a3
	bra.s	par_common
paruse
	moveq	#0,d7			usage is at a3
par_common
	bsr.l	ut_par1 		just one parameter
*
	add.l	d7,a3			get appropriate byte
	moveq	#$f,d1			(only the 4 lsbits)
	and.b	(a6,a3.l),d1		on arithmetic stack
	bra.l	ut_rtint		and return (no error)
*
*	parameter name / value
*
parstr$
	bsr.l	ut_par2 		check for two parameters
	moveq	#$f,d0
	and.b	1(a6,a3.l),d0		get first parameter type
	subq.b	#1,d0			is it string?
	bne.s	pars_nm8		... no
*
	move.w	(a6,a3.l),d7		save type
	bsr.l	ut_gtst1		and get a string
	move.w	d7,(a6,a3.l)		restore type
	tst.l	d0			OK?
	bra.s	pars_str		return string or null
*
pars_nm8
	addq.l	#8,a3			skip first parameter
parnam$
	bsr.l	ut_gxin1		get exactly one integer
	bne.s	pars_rts		... oops
	addq.l	#2,bv_rip(a6)		reset RI stack
	move.w	(a6,a1.l),d3		set parameter pointer
	ble.s	pars_bp 		... 0 or negative!!!
	subq.w	#1,d3			starting from one
	lsl.w	#3,d3			indexing the name table
*
	move.l	bv_rtp(a6),a2		get return table pointer
	cmp.l	bv_rtbas(a6),a2 	any entries?
	ble.s	pars_bp 		... no
	tst.b	rt_type(a6,a2.l)	is top entry a proc or fun?
	beq.s	pars_bp 		... no
	move.l	rt_parm(a6,a2.l),a3	get pointer to parameters
	add.w	d3,a3			and pointer to the parameter we need
	cmp.l	rt_local(a6,a2.l),a3	within range?
	bge.s	pars_null
	moveq	#mt.inf,d0		check version
	trap	#1
	sub.l	#'1.04',d2		1.04? *** v. 1.01
	blt.s	pars_gnam		... no, early version, pointer rel a6
	add.l	bv_ntbas(a6),a3 	set pointer to table
pars_gnam
	bsr.l	ut_gnmpt		get name from name table
pars_str
	beq.l	ut_retst		... good, return a string
pars_null
	lea	zero_w(pc),a4		... bad, return null
	bra.l	ut_rtstr
*
pars_bp
	moveq	#err.bp,d0
pars_rts
	rts
	end
