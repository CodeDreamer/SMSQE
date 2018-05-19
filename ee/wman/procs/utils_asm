* Condensed version of the sbsext utilities to save space	 Marcel Kilgus
	section utils
*
	xdef	ut_rtint	return integer d1
	xdef	ut_gtint	get integers
	xdef	ut_gtlin	get long integers
	xdef	ut_gtin1	get one integer (however many params)
	xdef	ut_gnst1	get one string (no check)
	xdef	ut_gtnm1	get one name/string (however many params)
	xdef	ut_gxli1	check exactly one param and get long int
	xdef	ut_gxli2	check exactly two params and get long ints
	xdef	ut_chan1	default #1
	xdef	ut_fjob 	find a job (given the name)

*
	include dev8_sbsext_ext_keys

* Put integer onto RI stack and return	V0.1    1985  Tony Tebby   QJUMP
ut_rtint
	jsr	ut_ckri6		check for space
	subq.l	#2,a1
	move.w	d1,(a6,a1.l)		copy the integer
	moveq	#3,d4			set integer type
	move.l	a1,bv_rip(a6)
	moveq	#0,d0
	rts

* Put name on RI stack			V0.0    1985	Tony Tebby  QJUMP
*
*	d3 c s	pointer to name wrt a6 (ut_nmtos)
*	a1  r	pointer to RI stack
*	a4 c  p pointer to name wrt a6 (ut_rtnam)
*
ut_nmtos
	moveq	#3,d1			get the length of the name as a long word
	add.b	(a6,d3.l),d1
	bclr	#0,d1			rounded up (+2)
	bsr.s	ut_chkri
*
	add.l	d1,d3			move to end of string (ish)
ut_nam_loop
	subq.l	#1,d3			and copy it down
	subq.l	#1,a1
	move.b	-1(a6,d3.l),(a6,a1.l)
	subq.w	#1,d1
	bgt.s	ut_nam_loop
	clr.b	(a6,a1.l)
	rts

* Check number of parameters    1985	Tony Tebby  QJUMP
ut_parn
	add.l	a3,d0			number + bottom pointer
	sub.l	a5,d0			less top
	beq.s	utp_rts 		... should be zero
*	 
	addq.l	#4,sp			... remove return
err_bp
	moveq	#err.bp,d0
utp_rts
	rts

* Check for room on RI stack  V0.4     1985  Tony Tebby  QJUMP
*
*	d0   s
*	d1 c  p space required
*	d2-d7 p
*	a0    p
*	a1  r	RI stack pointer
*	a2-a5 p
*
ut_ckri6
	movem.l d1/d2/d3,-(sp)
	moveq	#6,d1
	bra.s	utc_do
ut_chkri
	movem.l d1/d2/d3,-(sp)
utc_do
	move.w	bv..chrix,a1
	jsr	(a1)
	movem.l (sp)+,d1/d2/d3		and restore them
ut_setri
	move.l	bv_rip(a6),a1
	rts

* Routines to get parameters  V0.01    1985  Tony Tebby   QJUMP
ut_gtint
	moveq	#ca..gtint-ca..gtint,d3 get integers
	bra.s	utg_do
ut_gtlin
	moveq	#ca..gtlin-ca..gtint,d3 get long integers
	bra.s	utg_do
ut_gxli2
	moveq	#$10,d0 		check for two params
	bra.s	ut_gxlin		and get long integers
ut_gxli1
	moveq	#$08,d0 		check for one param
ut_gxlin
	bsr.s	ut_parn 		check for n params
	moveq	#ca..gtlin-ca..gtint,d3 get long integers
	bra.s	utg_do
ut_gtin1
	moveq	#ca..gtint-ca..gtint,d3 get integer
	bra.s	ut_get1
ut_gnst1
	moveq	#ca..gtstr-ca..gtint,d3 get string
	bra.s	ut_getn1		... no check

ut_get1
	cmp.l	a3,a5			is there at least one parameter?
	ble.s	err_bp			... no, oops
ut_getn1
	move.l	a5,-(sp)		save top pointer
	lea	8(a3),a5		set new top
	bsr.s	utg_do			get parameter
	move.l	(sp)+,a5		restore top
	rts
utg_do
	movem.l d4/d6/a0/a2,-(sp)	save these!!
	move.w	d3,a2
	move.w	ca..gtint(a2),a2	get operation address
	jsr	(a2)			do operation
	movem.l (sp)+,d4/d6/a0/a2	and restore
	rts


* Get a string on the stack   V0.6    1985  Tony Tebby   QJUMP
ut_gtnm1
	cmp.l	a3,a5			; is there at least one param?
	bgt.s	ut_gtnam		; yes, there is
ut_gtnul
	bsr.s	ut_ckri6		; make room for string
	sub.l	#2,a1			; total length 2
	clr.w	(a6,a1.l)		; zero length
	bra.s	utgn_ok
*
ut_gtnam
	moveq	#$f,d0			; extract type of name
;;;	   and.b   1(a6,a3.l),d0
	move.w	(a6,a3.l),d1						    0.6
	cmp.w	#$0300,d1		; substring?			    0.6
	beq.s	ut_gtnst		; ... yes, get it		    0.6
	and.b	d1,d0							    0.6
	beq.s	ut_gtnul		; a null!!
*
	tst.w	2(a6,a3.l)		; is there a name?
	bmi.s	ut_gtnst		; ... no, always get string

	subq.b	#1,d0			; is it a string?
	bne.s	ut_gnmpt		; ... no, get the name instead

ut_gtnst
	bsr.s	ut_gnst1		; get one string (no check)
	bne.s	utgn_rts
	bsr.s	ut_remst		; remove string
	bra.s	utgn_ok
*
ut_gnmpt
	moveq	#0,d1
	move.w	2(a6,a3.l),d1		; get the pointer to the real entry
	blt.s	ut_gtnul		; ... expression is no good
	lsl.l	#3,d1			; in multiples of 8 bytes
	add.l	bv_ntbas(a6),d1
ut_ntnam
	moveq	#0,d3
	move.w	2(a6,d1.l),d3		; thus the pointer to the name
	add.l	bv_nlbas(a6),d3
	bsr.l	ut_nmtos		; put name on stack
utgn_ok
	moveq	#0,d0
utgn_rts
	rts

* Remove string from RI stack	V0.0    1985	Tony Tebby   QJUMP
*
*	d1   s	total length occupied
*	a1 c p	pointer to RI stack
*
ut_remst
	moveq	#3,d1			round up to word, including char count
	add.w	(a6,a1.l),d1		get length
	bclr	#0,d1
	add.l	d1,bv_rip(a6)		change RIP but not a1
	rts

* Set default or given channel V0.4   PUBLIC DOMAIN by Tony Tebby  QJUMP
*
* call parameters   : a3 and a5 standard pointers to name table for parameters
*		    : d6 channel number
* return parameters : d4 pointer to channel table
*		    : d6 channel number
*		      a0 channel id
ut_chan1
	moveq	#1,d6		default is channel #1
	cmpa.l	a3,a5		are there any parameters?
	ble.s	ut_chlook	... no
	btst	#7,1(a6,a3.l)	has the first parameter a hash?
	beq.s	ut_chlook	... no
*
	move.l	a1,-(sp)
	bsr.l	ut_gtin1	get one integer
	bne.s	ut_chexit	was it ok?
	addq.l	#8,a3		and move past it
	moveq	#-1,d6		set no default
	move.w	0(a6,a1.l),d6	get value in d6 to replace the default
	addq.l	#2,bv_rip(a6)	reset ri stack pointer
	move.l	(sp)+,a1
*
ut_chlook
	move.w	d6,d4		get channel number
	blt.l	err_bp		... oops
ut_chd4
	move.l	a1,-(sp)
	mulu	#$28,d4 	make d4 (long) pointer to channel table
	add.l	bv_chbas(a6),d4 
	cmp.l	bv_chp(a6),d4	is it within the table?
	bge.s	ut_chno 	... no
	move.l	0(a6,d4.l),a0	set channel id
	move.w	a0,d0		is it open?
	bpl.s	ut_chok 	... yes
ut_chno
	moveq	#err.no,d0	channel not open
	bra.s	ut_chexit
ut_chok
	moveq	#0,d0		no error
ut_chexit
	move.l	(sp)+,a1
	rts

* Find job information	V0.0    1985  Tony Tebby   QJUMP
ut_fjob
	moveq	#0,d1			start from job 0
utj_loop
	move.l	a1,-(sp)		save name pointer
	bsr.s	ut_jinf 		get Job information
	move.l	(sp)+,a1
	bne.s	ut_fjob 		... oops, try again
	cmp.w	#$4afb,(a0)+		check flag
	bne.s	utj_cknx
	bsr.s	ut_cnmar		compare names
	beq.s	utj_rts 		... name found
utj_cknx
	move.l	d5,d1			restore next job id
	bne.s	utj_loop		... and check name of next job
	moveq	#err.nj,d0		job not found
utj_rts
	rts
*
ut_jinf
	move.l	d1,d4			save Job ID
	moveq	#mt.jinf,d0		get Job information
	moveq	#0,d2			scan whole tree
	trap	#1
	move.l	d1,d5			save next Job ID
	addq.l	#6,a0			move to flag
	tst.l	d0			check errors
	rts

* Compare names  V0.1	 1985	Tony Tebby    QJUMP
*
*	d0   s
*	d1   s
*	d2   s
*	a0 c p	pointer to string
*	a1 c p	pointer to string 
*
* a0 absolute,	a1 relative
ut_cnmar
	movem.l a0/a1,-(sp)		save name pointers
	moveq	#0,d0
	addq.l	#1,a0			(byte length only)
	move.b	(a0)+,d0		get string lengths
	cmp.b	1(a6,a1.l),d0
	addq.l	#2,a1
	bra.s	cn_lend
cn_loop
	move.b	(a0)+,d1		get char
	bsr.s	cn_uc			convert to upper case
	move.b	d1,d2
	move.b	(a6,a1.l),d1		a1 is relative
	addq.l	#1,a1
	bsr.s	cn_uc			convert this then
	cmp.b	d1,d2			the same?
cn_lend
	dbne	d0,cn_loop		next character
cn_exit
	movem.l (sp)+,a0/a1
	rts
*
cn_uc
	cmp.b	#'a',d1 		is it 'a'
	blo.s	uc_rts
	cmp.b	#'z',d1 		... to 'z'
	bls.s	uc_set
	cmp.b	#$80,d1 		is it 'a umlaut'
	blo.s	uc_rts
	cmp.b	#$8b,d1 		... to 'oe'
	bhi.s	uc_rts
uc_set
	bchg	#5,d1			set upper case
uc_rts
	rts

	end
