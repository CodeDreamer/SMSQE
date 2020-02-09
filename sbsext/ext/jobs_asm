* Job control procedures V0.6	    1984/1985	Tony Tebby  QJUMP
*
*	JOBS [#n]			lists the current jobs
*	RJOB nr,tag,error		removes a job
*	SPJOB nr,tag,priority		sets a job's priority
*	SUSJB nr,tag,timeout		suspends a job +ve ticks, -1 forever
*	AJOB nr,tag,priority		activates a job
*
* 2018-07-24  0.6  Added SUSJB (pjw)

	section exten
*
	xdef	rjob
	xdef	spjob
	xdef	susjb
	xdef	ajob
	xdef	jobs
	xdef	job_gid
*
	xref	ut_fjob 		find job using name
	xref	ut_jinf 		get job information
	xref	ut_gtnm1		get one name
	xref	ut_gtlin		get long integers
	xref	ut_gtli1		get one long int
	xref	ut_gxli1		get exactly 1 long integer
	xref	ut_winset		set up window
	xref	ut_winchk		check window
	xref	ut_messg		write message
	xref	ut_mesa1		write standard string (a1)
	xref	ut_wrtst		write string of bytes (a6,a1.l)
	xref	ut_wrtnl		write newline
	xref	err_bp
*
	include dev8_sbsext_ext_keys
*
rjob
	moveq	#mt.frjob,d7		force remove job
	bra.s	job_jb

susjb
	moveq	#mt.susjb,d7		suspend job
	bra.s	job_jb

spjob
	moveq	#mt.prior,d7		set priority
	bra.s	job_jb

ajob
	moveq	#mt.activ,d7		activate

job_jb
	bsr.s	job_proc		2/3 arguments
	bne.s	job_rts

	move.l	d7,d0			set trap

	cmp.b	#mt.activ,d7
	beq.s	job_trap		(d3 = zero)

	move.l	d2,d3			d3 = timeout (susjb) or error (rjob)
	suba.l	a1,a1			no flag byte (susjb)
job_trap
	trap	#1
job_rts
	rts
job_proc
	moveq	#8,d5			get 2 parameters (8 bytes)
*
job_gid
	cmp.l	a3,a5			must be some parameters
	beq.s	job_bp
	tst.b	(a6,a3.l)		is it unused name?
	beq.s	job_gtnm		... yes, get name
	moveq	#$f,d0
	and.b	1(a6,a3.l),d0		get variable type
	subq.b	#1,d0			is it string?
	beq.s	job_gtnm		... yes
	tst.w	2(a6,a3.l)		any name?
	bmi.s	job_gtln		... no, get long integers
	tst.w	4(a6,a3.l)		any value?
	bpl.s	job_gtln		... yes, get it
job_gtnm
	move.l	d5,d6			save number of parms
	bsr.l	ut_gtnm1		get name
	bne.s	job_rts 		... oops
	addq.l	#8,a3			move parameter pointer on
*
	bsr.l	ut_fjob 		find job
	bne.s	job_rts 		... oops
*
	subq.l	#8,a0			backspace a0 to base of job
	move.l	d4,d1			set Job ID
	moveq	#0,d2			no additional information
	cmp.l	a3,a5			any more params?
	beq.s	job_rts 		... no
	subq.l	#4,d6			was another param needed?
	beq.s	job_bp			... no, bad
	bsr.l	ut_gxli1		... yes, one long integer
	bne.s	job_rts
	addq.l	#4,bv_rip(a6)		restore RI stack
	move.l	d4,d1			reset job ID
	subq.l	#4,a1			move RI stack pointer to ...
	bra.s	job_parm		... set parameter
*
job_gtln
	bsr.l	ut_gtlin		get long integers
	bne.s	job_rts
	ext.l	d3
	lsl.w	#2,d3			make d3 number of bytes
	add.l	d3,bv_rip(a6)		and restore RI stack pointer
	sub.w	d5,d3			did we get the right number?
	blt.s	job_bp			too few
	beq.s	job_id1 		combined id
	subq.w	#4,d3			was it number/tag?
job_bp
	bne.l	err_bp			... no
	move.l	6(a6,a1.l),d1		get tag in msw of d1
	move.w	2(a6,a1.l),d1		and number in lsw
	addq.l	#4,a1			move up a bit
	bra.s	job_parm
job_id1
	move.l	(a6,a1.l),d1		get job id
job_parm
	move.l	4(a6,a1.l),d2		set parameter
	tst.l	d0
	rts
*
* write a list of jobs to selected or default channel
*
jobs
	bsr.l	ut_winset		get channel and window size
	bne.s	job_rts1		... ok?
	addq.w	#1,d6			allow for header
	move.l	a0,a4			put channel id out of harm's way
	moveq	#ms.jhead,d0		write job heading
	bsr.l	ut_messg
	moveq	#0,d1			start at job 0
job_loop
	bsr.l	ut_jinf 		get job information
	bne.s	job_rts1
	bsr.l	ut_winchk		check for room
	bsr.s	job_wrinf		write out information on job
	bne.s	job_rts1
	move.l	d5,d1			if next job is not zero ...
	bne.s	job_loop		... carry on
job_rts1
	rts
*
job_wrinf
	move.l	d2,d7			we are about to smash the registers
	move.l	a0,-(sp)		... and the job address
	move.l	d3,-(sp)
*
	move.l	bv_bfbas(a6),a1 	we will use the basic buffer
	addq.l	#2,a1			leave a little bit of room at the bottom
	move.l	a1,a0
	move.l	a0,a5			and set our field pointer
*
	move.w	d4,d1			first the job number
	addq.l	#4,a5			... in a field of 4 characters
	bsr.s	job_num
	move.l	d4,d1			now the job tag
	swap	d1			... which is in the most significant wrd
	addq.l	#7,a5			... in a field of 7
	bsr.s	job_num
	move.w	d7,d1			now the owner number
	addq.l	#6,a5			... in a field of 5+1
	bsr.s	job_num
	tst.w	(sp)+			now check if suspended
	bpl.s	job_w_pr
	move.b	#'s',-1(a6,a0.l)	... yes, put in s flag
job_w_pr
	move.w	(sp)+,d1		now the priority (which is only a byte)
	and.w	#$ff,d1
	addq.l	#4,a5			... in a field of 4
	bsr.s	job_num
*
* all the numbers are in
*
	moveq	#21,d2			send 21 characters
	move.l	a4,a0			get the channel ID back
	bsr.l	ut_wrtst		(pointed to by a1)
	move.l	(sp)+,a1		restore job flag address
	bne.s	job_rts1		... oops
*
* now add the name
*
	cmpi.w	#$4afb,(a1)+		check for flag
	bne.s	job_wr_done		... no flag
	bsr.l	ut_mesa1		... write string (message) at a1
	bne.s	job_rts1		... oops
*
job_wr_done
	bra.l	ut_wrtnl		write newline
*
* put an integer into a line and space along to end of field
*	(a6,a1.l) points to base of buffer, a1 is preserved
*	(a6,a0.l) points to buffer
*	(a6,a5.l) points to end of field
*
job_num
	subq.l	#2,a1			move pointer down
	move.w	d1,(a6,a1.l)		and put integer in
	jsr	cn..itod*3+qlv.off	convert integer to decimal
job_nr_loop
	move.b	#' ',(a6,a0.l)		move a space in
	addq.l	#1,a0			and move buffer pointer on
	cmpa.l	a0,a5			have we filled field yet?
	bhi.s	job_nr_loop		try again
	rts
	end
