* Routines to get parameters  V0.01    1985  Tony Tebby   QJUMP
*
	section utils
*
	xdef	ut_gtfp 	get floating points
	xdef	ut_gtstr	get strings
	xdef	ut_gtint	get integers
	xdef	ut_gtlin	get long integers
	xdef	ut_gtst1	get one string (hovever many param)
	xdef	ut_gtfp1	get one floating point (however many params)
	xdef	ut_gtin1	get one integer (however many params)
	xdef	ut_gtli1	get one long integer (however many params)
	xdef	ut_gnst1	get one string (no check)
	xdef	ut_gxst1	check exactly one param and get string
	xdef	ut_gxin1	check exactly one param and get integer
	xdef	ut_gxin2	check exactly two params and get integers
	xdef	ut_gxlin	check exactly n params and get long ints
	xdef	ut_gxli1	check exactly one param and get long int
	xdef	ut_gxli2	check exactly two params and get long ints
	xdef	ut_gxfp1	check exactly one param and get fp
*
	xref	ut_parn
	xref	ut_par1
	xref	ut_par2
	xref	err_bp
*
	include dev8_sbsext_ext_keys
*
*	d0 cr	8*nr of parameters / error return
*	d1   s
*	d2   s
*	d3  r	nr of parameters fetched
*	d4-d7 p 
*	a0    p
*	a1  r	pointer to first param on stack
*	a2    p
*	a3 c  p pointer to first param in name table
*	a4    p
*	a5 c  p pointer beyond last param in name table
*
ut_gxli2
	moveq	#$10,d0 		check for two params
	bra.s	ut_gxlin		and get long integers
ut_gxli1
	moveq	#$08,d0 		check for one param
ut_gxlin
	bsr.s	ut_parn 		check for n params
ut_gtlin
	moveq	#ca..gtlin-ca..gtint,d3 get long integers
	bra.s	utg_do
*
ut_gxin2
	bsr.s	ut_par2 		check for two params
	bra.s	ut_gtint		and get integers
ut_gxin1
	bsr.s	ut_par1 		check for one param
ut_gtint
	moveq	#ca..gtint-ca..gtint,d3 get integers
	bra.s	utg_do
*
ut_gxst1
	bsr.s	ut_par1 		check for one param
ut_gtstr
	moveq	#ca..gtstr-ca..gtint,d3 get strings
	bra.s	utg_do
*
ut_gnst1
	moveq	#ca..gtstr-ca..gtint,d3 get string
	bra.s	ut_getn1		... no check
ut_gtst1
	moveq	#ca..gtstr-ca..gtint,d3 get string
	bra.s	ut_get1
ut_gxfp1
	bsr.s	ut_par1 		check one param
ut_gtfp
	moveq	#ca..gtfp-ca..gtint,d3	get floating point
	bra.s	utg_do
ut_gtfp1
	moveq	#ca..gtfp-ca..gtint,d3	get floating point
	bra.s	ut_get1
ut_gtin1
	moveq	#ca..gtint-ca..gtint,d3 get integer
	bra.s	ut_get1
ut_gtli1
	moveq	#ca..gtlin-ca..gtint,d3 get long integer
ut_get1
	cmp.l	a3,a5			is there at least one parameter?
	ble.l	err_bp			... no, oops
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
	end
