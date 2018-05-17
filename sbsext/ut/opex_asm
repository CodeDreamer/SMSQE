; Open routines for EX	V2.0    1994  Tony Tebby

	section utils

	xdef	ut_openp 
	xdef	ut_openj

	xref	pipe_len
	xref	pipe_nol
	xref	ut_opdefxj

	include 'dev8_keys_qdos_ioa'
	include 'dev8_sbsext_ut_opdefx_keys'
	include 'dev8_sbsext_ext_ex_defs'

ut_openp
	move.l	job_id+4(sp),d1 	 ; owner job
	moveq	#0,d3			 ; open device
	lea	pipe_len(pc),a0 	 ; as pipe of defined length
	bsr.s	uoj_do
	bne.s	uoj_rts
	move.l	a0,d3			 ; now open other end
	lea	pipe_nol(pc),a0
uoj_do
	moveq	#ioa.open,d0
	trap	#do.ioa
	tst.l	d0
uoj_rts
	rts


ut_openj
	moveq	#uod.datd,d2		 ; data default
	move.l	job_id+4(sp),d1 	 ; owner job
	bra.s	ut_opdefxj		 ; open
	end
