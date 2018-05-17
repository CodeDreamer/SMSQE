* Spooler  V1.01        1984/1985  Tony Tebby	 QJUMP
*
	section exten
*
	xdef	spl			spool file
	xdef	splf			... with <FF> at end
*
	xref	ut_crjob		create job
	xref	ut_ajob 		activate job
	xref	ut_jober		suicide (with error message)
	xref	ut_rjob 		remove job (ID in d5)
	xref	ut_fname		get filename or #
	xref	ut_opdefxj		open data file
	xref	ut_opdst		open destination
	xref	ut_copys		copy subroutine
	xref	ut_wrtch
*
	include dev8_mac_assert
	include dev8_sbsext_ext_keys
	include 'dev8_sbsext_ut_opdefx_keys'
*
spl_data dc.w	bv_brk+1+4+$18		data space required
	 dc.w	spl_job-*		entry address
	 dc.w	3,'SPL' 		job name
spl_a6	 equ	$10			end of job code
*
splf   
	moveq	#-1,d7			<FF> at end
	bra.s	spl_com
spl
	moveq	#0,d7			no <FF> at end
spl_com
	cmp.l	a5,a3			any parameters?
	beq.s	spl_bp			... no
*
	lea	spl_data(pc),a4 	set up job
	bsr.l	ut_crjob
	bne.s	spl_rts
*
	move.w	d7,(a4)+		set <FF> pointer
*
	bsr.l	ut_fname		get source
	blt.s	spl_rjob		... oops
	bgt.s	spl_seti		... #n
	move.l	a1,-(sp)		save pointer to name
	moveq	#io.share,d3		open input
	bsr.s	spl_open		... with data defaults
	move.l	(sp)+,a1		restore pointer to name
	bne.s	spl_rjob		... oops
spl_seti
	move.l	a0,(a4)+		set input channel id
	addq.l	#8,a3			move to next parameter
	cmp.l	a3,a5
	beq.s	spl_ddest		... only one
	bsr.l	ut_fname		get next parameter name
	blt.s	spl_rjob		... oops
	bgt.s	spl_active		... #n
	moveq	#io.overw,d3		open destination
	bsr.s	spl_open		... with data defaults
	beq.s	spl_active		... ok
	bra.s	spl_rjob		... oops

spl_open
	move.l	d5,d1			... open for job
	moveq	#uod.datd,d2
	jmp	ut_opdefxj		... with data defaults

spl_ddest
	moveq	#io.overw,d3		open destination
	move.l	d5,d1			for job
	bsr.l	ut_opdst		with destination defaults
	bge.s	spl_active		... ok
*
	moveq	#err.iu,d4
	cmp.l	d4,d0			is it in use?
	bne.s	spl_rjob		... oops
	move.l	d0,(a4)+		yes, put negative id into job
	move.w	(a6,a1.l),d0
	move.w	d0,(a4)+ 
spl_cdef
	move.b	2(a6,a1.l),(a4)+	and copy name
	addq.l	#1,a1
	subq.w	#1,d0
	bgt.s	spl_cdef
	bra.s	spl_act1
*
spl_active
	move.l	a0,(a4)+		set destination channel id
spl_act1
	moveq	#8,d2			priority 8
	bra.l	ut_ajob 		activate job
*
spl_bp
	moveq	#err.bp,d0
spl_rts
	rts
spl_rjob
	move.l	d5,d1			set job ID
	bra.l	ut_rjob
	page
spl_job
	lea	spl_a6(a6),a6		set a6 pointer for copy
	move.l	a6,a5
	move.w	(a5)+,d7		get <FF> flag
	move.l	(a5)+,a3		source channel
	move.l	(a5)+,a0		destination channel
	move.w	a0,d0			is output channel open?
	bge.s	spl_go
spl_try
	move.l	a5,a0			get pointer to name
	moveq	#io.open,d0		and try to open output
	moveq	#myself,d1
	moveq	#io.old,d3
	trap	#2
	tst.l	d0			ok?
	beq.s	spl_go			... yes
	moveq	#err.iu,d4
	cmp.w	d4,d0			was it in use?
	bne.s	suicide 		... no, give up
	moveq	#mt.susjb,d0		suspend 
	moveq	#50,d3			for 1 second
	trap	#1
	bra.s	spl_try
*
spl_go
	moveq	#4,d0			set up pseudo vars for copy
	assert	bv_bfbas,0
	move.l	d0,bv_bfbas(a6) 	pointer to buffer
	st	bv_brk(a6)		no break!!!
	moveq	#1,d5			set optional header
	bsr.l	ut_copys		and copy
	tst.w	d7			is <FF> required
	beq.s	suicide 		... no
	move.l	d0,d4			save the error
	moveq	#$c,d1
	bsr.l	ut_wrtch		and send <FF>
	move.l	d4,d0
suicide
	bra.l	ut_jober
	end
