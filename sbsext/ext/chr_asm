; CHR$ / CODE	    V2.00    	1990  Tony Tebby   QJUMP
;
;	CHR$ (code)
;	FILL$ (str$,len)
;	CODE (chr$)
;	LEN (str$)
;
	section exten

	xdef	chr$
	xdef	fill$
	xdef	code
	xdef	len

	xref	ut_gxin1		 ; get one integer
	xref	ut_gxst1
	xref	ut_gtst1
	xref	ut_chkri
	xref	ut_remst
	xref	ut_retst
	xref	ut_retin

	include 'dev8_sbsext_ext_keys'

fill$
	jsr	ut_gtst1		 ; get one string
	bne.s	chr_rts
	addq.l	#8,a3
	jsr	ut_gxin1		 ; and length
	bne.s	chr_rts
	move.w	(a6,a1.l),d3
	blt.s	chr_bp			 ; oops
	addq.l	#2,a1
	move.l	a1,bv_rip(a6)		 ; remove int
	move.w	(a6,a1.l),d2		 ; length of string
	beq.s	chr_fill		 ; fill this
	move.w	2(a6,a1.l),d2		 ; two char
	cmp.w	#1,(a6,a1.l)		 ; ... one?
	bgt.s	chr_fill
	move.b	2(a6,a1.l),d2		 ; duplicate char
chr_fill
	jsr	ut_remst		 ; remove string

	moveq	#3,d1
	add.w	d3,d1			 ; space for string
	bvs.s	chr_bp			 ; ... too long
	jsr	ut_chkri

	move.w	d3,d0
	addq.w	#1,d0
	lsr.w	#1,d0
chr_floop
	subq.w	#2,a1
	move.w	d2,(a6,a1.l)		 ; fill up (one to many)
	dbra	d0,chr_floop

	move.w	d3,(a6,a1.l)		 ; and set length
	bra.s	chr_rtst


chr$
	jsr	ut_gxin1		 ; get param
	bne.s	chr_rts

	move.w	(a6,a1.l),d3		 ; parameter
	move.b	d3,(a6,a1.l)		 ; in msw
	subq.l	#2,a1			 ; adjust stack
	move.w	#1,(a6,a1.l)		 ; single byte string
chr_rtst
	jmp	ut_retst		 ; return it

chr_bp
	moveq	#err.bp,d0
chr_rts
	rts

len
	moveq	#8,d0
	add.l	a3,d0
	cmp.l	a5,d0			 ; one param?
	bne.s	chr_bp
	cmp.w	#0201,(a6,a3.l) 	 ; string?
	bne.s	len_gen
	move.l	4(a6,a3.l),a0
	move.w	(a0),d3 		 ; ... length
	move.l	bv_rip(a6),a1
	subq.w	#2,a1
	bra.s	chr_rtd3

len_gen
	jsr	ut_gxst1		 ; get param
	bne.s	chr_rts
	move.w	(a6,a1.l),d3		 ; length
	bra.s	chr_remst

code
	jsr	ut_gxst1		 ; get param
	bne.s	chr_rts
	tst.w	(a6,a1.l)		 ; any string?
	beq.s	chr_rtin		 ; ... no

	moveq	#0,d3
	move.b	2(a6,a1.l),d3		 ; parameter

chr_remst
	moveq	#1,d0
	add.w	(a6,a1.l),d0
	bclr	#0,d0
	add.l	d0,a1

chr_rtd3
	move.w	d3,(a6,a1.l)

chr_rtin
	jmp	ut_retin		 ; return it

	end
