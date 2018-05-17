* Error processing extras  V0.1    1985  Tony Tebby   QJUMP
*
	section exten
*
*	ERR_xx
*	ERNUM
*	ERLIN
*
	xdef	ernum
	xdef	erlin
*
	xref	ut_rtint
	xref	ut_rtfd1
	xref	ut_par0 		check for no parameters
*
	include dev8_sbsext_ext_keys

erlin
	bsr.l	ut_par0 		 no params
	move.w	bv_erlin(a6),d1
	jmp	ut_rtint		 return it


ernum
	bsr.l	ut_par0
	move.l	bv_ernum(a6),d1
	jmp	ut_rtfd1		 return error as FP


errx	macro	errn
	xdef	[errn]
[errn]
	bsr.s	errx_set
	endm
errx_base
	errx ERR$NC
	errx ERR$NJ
	errx ERR$OM
	errx ERR$OR
	errx ERR$BO
	errx ERR$NO
	errx ERR$NF
	errx ERR$EX
	errx ERR$IU
	errx ERR$EF
	errx ERR$DF
	errx ERR$BN
	errx ERR$TE
	errx ERR$FF
	errx ERR$BP
	errx ERR$FE
	errx ERR$XP
	errx ERR$OV
	errx ERR$NI
	errx ERR$RO
	errx ERR$BL

	nop
errx_set
	lea	errx_base,a1
	sub.l	(sp)+,a1
	move.l	a1,d1
	asr.l	#1,d1			error code to check

	bsr.l	ut_par0 		check for no parameters
	sub.l	bv_ernum(a6),d1
	seq	d1			equ=255
	and.l	#1,d1
	bra.l	ut_rtfd1		and return it

	end
