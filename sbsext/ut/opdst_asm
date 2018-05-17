; Open destination  V1.0    1984   Tony Tebby	 QJUMP

	section utils

	xdef	ut_opdst
	xdef	ut_opmds

	xref	ut_fdef
	xref	ut_opdefxj

	include 'dev8_keys_sys'
	include 'dev8_sbsext_ut_opdefx_keys'

;	return status is LT=error, EQ=OK, GT=OK but destination is device

ut_opmds
	moveq	#-1,d1
ut_opdst
	move.l	a4,a0			 ; save a4
	moveq	#sys_dstd-sys.defo,d2	 ; get the default
	bsr.l	ut_fdef
	exg	a4,a0

	moveq	#uod.dstd+uod.prmt,d2	 ; open with destination default
	move.w	(a0),d0
	cmp.b	#'_',1(a0,d0.w) 	 ; does it end with '_'
	beq.l	ut_opdefxj		 ; ... yes, open
	sub.l	a1,a1			 ; ... no, open default only
	bsr.l	ut_opdefxj
	blt.s	utod_rts		 ; ... oops
	moveq	#1,d2			 ; set status positive
utod_rts
	rts
	end
