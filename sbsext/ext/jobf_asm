* Job manipulation functions V0.6      1984/1985 Tony Tebby  QJUMP
*
*	PJOB (nr,tag)		priority of a job
*	OJOB (nr,tag)		owner of a job
*	JOB$ (nr,tag)		job name
*	NXJOB(nr,tag,owner,tag) next job in tree
*	JOBID [({nr, tag} | <name> )] named job id or my id (no parameters)
*
* 2006-01-10	0.07	JOBID function added (PJW + wl)

	section exten
*
	xdef	jobid
	xdef	pjob
	xdef	ojob
	xdef	job$
	xdef	nxjob
*
	xref	zero_w			; a zero word
	xref	job_gid 		; get job using ID
	xref	ut_rtfd1		; return floating point value of d1
	xref	ut_rtstr		; put string on and return it
	xref	err_bp
*
	include dev8_sbsext_ext_keys
	include dev8_keys_qdos_sms

jobid
	cmp.l	a3,a5			; any parameters at all?
	beq.s	job_me			; none means me

	bsr.s	job_inf_0		; get job information (top of tree 0)
	move.l	d7,d1			; and get real job ID back
	bra.s	job_rlin		; return d1 => job id

job_me
	moveq	#sms.info,d0		; get system information, which ...
	trap	#1			;  ... returns my job ID in D1
	tst.l	d0
	bne.s	jobf_rts
	bra.s	job_rlin		; return d1 => job id

pjob
	bsr.s	job_inf_0		; get job information (top of tree 0)
	moveq	#0,d1
	move.b	d3,d1			; priority byte in d3
	bra.s	job_rlin
ojob
	bsr.s	job_inf_0		; get job information (top of tree 0)
	move.l	d2,d1			; return d2 (owner job)
	bra.s	job_rlin
job$
	bsr.s	job_inf_0		; get job information (top of tree 0)
	lea	zero_w(pc),a4		; point to null string (word 0)
	addq.l	#6,a0			; check
	cmp.w	#$4afb,(a0)+		; ... flag
	bne.s	job_rstr
	move.l	a0,a4			; pointer to string
job_rstr
	bra.l	ut_rtstr		; return string
*
nxjob
	move.l	a3,a4			; save bottom pointer
	add.w	#$10,a3 		; and look at second set of parameters
	cmp.l	a3,a5			; were there only two?
	blt.l	err_bp			; ... not even two!!
	bgt.s	job_top_check		; ... no, more than two
	subq.l	#8,a3			; ... yes, each one must be a job id
job_top_check
	bsr.s	job_inf_0		; check top of tree
	move.l	a3,a5			; reset pointers to
	move.l	a4,a3			; ... look at first id
	bsr.s	job_inf 		; now get job information
job_rlin
	bra.l	ut_rtfd1		; float long integer, in d1, onto stack and return
*
job_inf_0
	moveq	#0,d7			; top of tree 0
job_inf
	moveq	#4,d5			; one parameter (four bytes)
	bsr.s	job_gid 		; get the id
	bne.s	jobf_quit		; ... oops
	moveq	#mt.jinf,d0		; get job information
	move.l	d7,d2			; top of tree
	move.l	d1,d7			; save job id
	trap	#1
	tst.l	d0
	beq.s	jobf_rts
	moveq	#0,d0			; clear error return
	moveq	#-1,d1			; set silly next job
	moveq	#0,d2			; owned by 0
	moveq	#0,d3			; priority 0
	lea	*,a0			; starting here (no 4afb flag here!!!)
jobf_rts
	rts
jobf_quit
	addq.l	#4,sp
	rts
	end
