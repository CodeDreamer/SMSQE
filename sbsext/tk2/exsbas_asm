* EX, EW and ET V2.07	 1984 Sinclair Research and Tony Tebby  QJUMP
*			 1994 Tony Tebby SBASIC compatible version
*
; 2003-11-10	2.01	Added functions FET, FEX, FEW  pjwitte 2oo3
* 2004-05-15	2.02	Added ex_m (wl)
* 2005-01-25	2.03	Added call to Home thing (wl)
* 2005-11-17	2.04	If A1 absolute, use supervisor mode when calling home thing
* 2006-03-27	2.05	Use system specific execution delay (MK)
; 2014-06-08	2.06	Added function FEX_M (pjw)
* 2018-01-10	2.07	Removed probing for BAS as EX for QDOS cannot do it (MK)

	section exten
*
	xdef	etsb
	xdef	ewsb
	xdef	exsb
	xdef	emsb

	xdef	fetsb
	xdef	fewsb
	xdef	fexsb
	xdef	femsb
*
	xref	ut_ckcomma
	xref	ut_cksemi
	xref	ut_ckto
	xref	ut_openj
	xref	ut_opdefxj
	xref	ut_openp
	xref	ut_fopen
	xref	ut_fname
	xref	ut_gtnm1
	xref	ut_chanf
	xref	prior

	xref	ut_rtfd1

*
	include 'dev8_keys_sbasic'
	include 'dev8_keys_sys'
	include 'dev8_keys_err'
	include 'dev8_keys_jcb'
	include 'dev8_keys_chn'
	include 'dev8_keys_thg'
	include 'dev8_keys_qdos_io'
	include 'dev8_keys_qdos_ioa'
	include 'dev8_keys_qdos_sms'
	include 'dev8_sbsext_tk2_ex_defs'
	include 'dev8_sbsext_ut_opdefx_keys'

*
femsb			      ; Function: No waiting, owned by me
	pea	fex_exit	stack return address
	bra.s	emsb

fexsb
	pea	fex_exit	stack return address
	bra.s	exsb		Function EX
fetsb
	bsr.s	etsb		Function ET

fex_exit
	tst.l	d1		parameter error?
	bne.l	ut_rtfd1	 no, ret job ID

fex_rts
	rts			hard error

fewsb
	bsr.s	ewsb		Function EW

	tst.l	d1		parameter error?
	beq.s	fex_rts 	 yes, return hard error
	move.l	d0,d1		 no, return program's error code
	bra.l	ut_rtfd1
*

emsb	clr.w	-(sp)		no waiting
	moveq	#-1,d0		job owned by me
	bra.s	ex_ew_et
etsb
	move.w	#1,-(sp)	flag to prevent activation
	bra.s	ex_et
ewsb
	moveq	#-1,d0		jobs owned by me
	move.w	d0,-(sp)	wait until complete
	bra.s	ex_ew_et
exsb
	clr.w	-(sp)		no waiting
ex_et
	moveq	#0,d0		jobs independent
ex_ew_et
	move.l	a3,-(sp)	save the start pointer
	clr.l	-(sp)		clear pointer to options on ri stack
	clr.l	-(sp)		clear the final pipe id
	clr.w	-(sp)		clear out the pipe flags
	clr.l	-(sp)
	clr.l	-(sp)		the last job id
	move.l	d0,-(sp)	and the owner job id
*
	cmp.l	a5,a3		any parameters?
	beq.s	ex_bp.1 	... no!!
*
	tst.b	-7(a6,a5.l)	is last one to #?
	bge.s	prg_loop	... not #
	lea	-8(a5),a3	... is #, look at it
	bsr.l	ut_ckto 	is it preceded by to?
	bne.s	prg_loop	... not to
	cmp.l	parm_st(sp),a3	it must not be first
ex_bp.1
	beq.l	ex_bp		... but it is
****	tst.w	job_wait(sp)	must not be ew
****	bmi.l	ex_bp		... but it is
	bsr.l	ut_chanf	find channel table address
	bne.s	ex_err.4
	bsr.l	ut_openp	open a pipe
	bne.s	ex_err.4
	move.l	a0,(a6,d4.l)	one end for basic
	sub.l	sb_chanb(a6),d4
	move.l	d4,pipe_end(sp) keep pointer in case of error
	move.l	d3,pipe_out(sp) and save other end in the pipe_out flag
	subq.l	#8,a5		remove last parameter
*
prg_loop
	clr.l	opt_ptr(sp)	clear the option pointer
	moveq	#0,d5		no space is required
	lea	-8(a5),a3	look at previous parameter
	bsr.l	ut_cksemi	is it preceded by a semicolon?
	bne.s	prog_find	... no
	bsr.l	ut_gtnm1	... yes, get string
ex_err.4
	bne.s	ex_err.3
	move.l	a3,a5		remove last parameter
	move.w	(a6,a1.l),d5	keep the number of bytes
	addq.l	#1,d5		rounded up
	bclr	#0,d5
	move.l	a1,sb_arthp(a6)   ... save the pointer so we do not loose string
	sub.l	sb_arthb(a6),a1 and keep the pointer relative to base of ri
	move.l	a1,opt_ptr(sp)
*
prog_find
	move.l	a5,a3		now start looking for program name
	moveq	#-1,d7		count the number of bits skipped
prog_look
	subq.l	#8,a3		move back one
	addq.l	#1,d7
	cmp.l	parm_st(sp),a3	is it the first on the line?
	beq.s	prog_found	yes
	bsr.l	ut_ckcomma	is it a data file (preceded by ',')?
	beq.s	prog_look	... yes, try again
	bsr.l	ut_ckto 	is it preceded by to?
	bne.s	ex_err.3	... no, bad, bad, bad
	st	pipe_in(sp)	... yes, remember that we have a pipe in
prog_found
	moveq	#ioa.kshr,d3
	moveq	#uod.prgd,d2	; program default
	jsr	ut_fopen	open program file
ex_err.3
	bne.l	ex_error
	or.b	#$10,1(a6,a3.l) and reset the separator to comma

	moveq	#iof.rhdr,d0	not SBASIC, read the header
	moveq	#$e,d2		14 bytes
	moveq	#-1,d3
	move.l	sb_buffb(a6),a1 into the basic buffer
	trap	#4
	trap	#3
	tst.l	d0		done?
	bne.s	ex_close	... no, it errored
	sub.w	d2,a1		backspace to start
	moveq	#err.ipar,d0	... assume it will error
	subq.b	#1,5(a6,a1.l)	is it executable?
	bne.s	ex_close	... no

	move.l	a0,a4		save channel id
	move.l	job_id(sp),d1	owned by 0 or previous job
	move.l	(a6,a1.l),d2	length of file
	move.l	d7,d3		number of files
	addq.l	#3,d3		plus two pipes (and two counts)
	lsl.l	#2,d3		*4
	add.l	d5,d3		plus string length
	add.l	6(a6,a1.l),d3	plus data space required
	moveq	#sms.crjb,d0	create job
	sub.l	a1,a1		start at start
	trap	#1
	exg	a0,a4		restore channel id
	tst.l	d0		did it create ok
	bne.s	ex_close	... no

	move.l	a4,a1		set address to load
	move.l	a4,a2		... and keep it
	add.l	d2,a4		set up address of stack
	add.l	d3,a4
	move.l	d1,job_id(sp)	and save the job id
	tst.l	top_id(sp)	is it the first job?
	bne.s	ex_load
	move.l	d1,top_id(sp)	yes, save the top job id
ex_load
	moveq	#iof.load,d0	... no, load the program
	moveq	#-1,d3
	trap	#3
ex_close
	move.l	d0,d4		save error code
	moveq	#ioa.clos,d0	and close channel
	trap	#2
	move.l	d4,d0
	bne.l	ex_error

ex_parm_set
	clr.w	-(a4)		leave a zero option string on the job's stack
	move.l	opt_ptr(sp),d1	get the option pointer
	beq.s	ex_pip_in	... it's zero
	add.l	sb_arthb(a6),d1 plus bas of ri stack
	addq.l	#2,a4		remove zero from job's stack
	add.l	d5,d1		end of string
ex_opt_loop
	move.w	(a6,d1.l),-(a4) copy a word at a time
	subq.l	#2,d1
	sub.l	#2,d5
	bge.s	ex_opt_loop
*
ex_pip_in
	moveq	#0,d5		no channel ids put on stack so far
	move.l	a4,a1		and a1 points to command string
	tst.b	pipe_in(sp)	... is there an input pipe?
	beq.s	ex_pip_out	... no
	addq.w	#1,d7		... yes, so there is one more file
ex_pip_out
	move.l	pipe_out(sp),d4 is there an output pipe
	beq.s	ex_files	... no
	addq.w	#1,d7		... yes, one more file
	move.l	d4,-(a4)	id on stack
	addq.w	#1,d5		... yes, one more
*
* now transfer ownership of pipe to job (to get eof when job removes itself)
*
	moveq	#sms.info,d0
	trap	#1
	move.l	sys_chtb(a0),a0
	lsl.w	#2,d4
	move.l	(a0,d4.w),a0
	move.l	job_id(sp),chn_ownr(a0)
*
ex_files
	addq.l	#6,a2		look for flag
	cmp.w	#$4afb,(a2)+
	bne.s	ex_standard	... not there
	move.w	(a2)+,d1	get name length
	addq.w	#1,d1		skip name
	bclr	#0,d1
	add.w	d1,a2
	cmp.w	#$4afb,(a2)+	is there another flag?
	beq.l	ex_special	... yes, special file handling
*
ex_standard
	move.l	a5,a3		set start pointer
ex_floop
	subq.l	#8,a3		take next parameter
	bsr.l	ut_ckcomma	... filenames are preceded by comma
	bne.s	ex_set_in	... done
	bsr.l	ut_fname	get filename or id
	blt.s	ex_err.1	... oops
	addq.w	#1,d5		one more file
	tst.l	d0		is it a channel id?
	bgt.s	ex_set_ch	... yes
	moveq	#ioa.kovr,d3	assume overwrite
	tst.b	pipe_in(a7)	can it be primary input?
	bne.s	ex_fopen	... no
	cmp.w	d5,d7		is it primary input?
	bne.s	ex_fopen	... no
	moveq	#ioa.kshr,d3	... yes, assume share
	cmp.w	#1,d5		but is it also primary output?
	bne.s	ex_fopen	... no
	moveq	#ioa.kexc,d3	... yes, open for input and output
ex_fopen
	bsr.l	ut_openj	open file
ex_err.1
	bne.s	ex_err.0	oops
ex_set_ch
	move.l	a0,-(a4)	put channel id on stack
	bra.s	ex_floop	next
*
ex_set_in
	tst.b	pipe_in(sp)	was there an input pipe?
	beq.s	ex_fend 	... no
	bsr.l	ut_openp	... yes, open a pipe
	move.l	a0,-(a4)	put id on stack
	addq.w	#1,d5		(one more)
	move.l	d3,pipe_out(sp) set output from next
	sf	pipe_in(sp)	there may not be another input
ex_fend
	move.w	d5,-(a4)	put the number of channel ids on stack
	moveq	#sms.spjb,d0	get address of job header by devious means
	move.l	job_id(sp),d1
	moveq	#0,d2
	trap	#1
	move.l	a4,jcb_a7(a0)	and put the stack pointer in
*
ex_next
	move.l	a3,a5		set new end pointer
	subq.l	#8,a3
	cmp.l	parm_st(sp),a3	is there just one item left?
	blt.s	ex_active	... no, none
	bgt.l	prg_loop	... no, look at next group
	tst.b	1(a6,a3.l)	... yes, is it #?
	bpl.l	prg_loop	... no
****	tst.w	job_wait(sp)	... yes, it must not be ew
****	bmi.s	ex_bp		... oh! but it is
	bsr.l	ut_chanf	... find channel pointer
ex_err.0
	bne.s	ex_error
	move.l	pipe_out(sp),(a6,d4.l) ... and set pipe id in it
*
ex_active
	move.w	job_wait(sp),d7 is it et (or ex or ew)?
	bgt.s	ex_exit_ok	... et, do not activate
	move.l	job_id(sp),d5	start at most recent job
ex_act_loop
	move.l	d5,d1
	moveq	#sms.injb,d0	find owner
	move.l	d1,d4		but save this job's id
	moveq	#0,d2
	trap	#1
	move.l	d2,d5		save owner
	moveq	#sms.acjb,d0	activate
	move.l	d4,d1		this job
	move.w	prior(pc),d2	at given priority
	cmp.l	top_id(sp),d1	is this the top job?
	beq.s	ex_act_top
	moveq	#0,d3		do not wait
	trap	#1
	bra.s	ex_act_loop
ex_act_top
	move.w	d7,d3		top job waits if ew
	trap	#1
	tst.w	d7		ew?
	bne.s	ex_exit_ok	... yes, do not pause
*
	moveq	#sms.ssjb,d0	suspend
	moveq	#myself,d1	myself
	moveq	#25,d3		give job 0.5s to start up
	sub.l	a1,a1		no flag
	trap	#1
*
ex_exit_ok			; pjw
	move.l	job_id(sp),d1	return this job's ID
	add.w	#ex_frame,sp	remove local variables
	rts

ex_exit 			; pjw
	moveq	#0,d1		flag parameter error
	add.w	#ex_frame,sp	remove local variables
	rts

ex_bp
	moveq	#err.ipar,d0
ex_error
	move.l	d0,d4		save error code
	moveq	#sms.frjb,d0	and get rid of all jobs and their channels
	move.l	top_id(sp),d1	... by removing the top job
	trap	#1
	move.l	pipe_end(sp),d5 get pipe connected to basic
	beq.s	ex_err_out	... it does not exist
	add.l	sb_chanb(a6),d5
	move.l	(a6,d5.l),a0	get id
	moveq	#-1,d0
	move.l	d0,(a6,d5.l)	close in channel table
	moveq	#ioa.clos,d0	close it
	trap	#2
ex_err_out
	move.l	pipe_out(sp),d0 is there a dangling output pipe?
	beq.s	ex_err_d0	... no
	move.l	d0,a0		... yes, close it (it might be gone already)
	moveq	#ioa.clos,d0
	trap	#2
ex_err_d0
	move.l	d4,d0		restore the error code
	bra.s	ex_exit
*
ex_special
	moveq	#1,d4		get 1 in d4 if there is an input pipe
	and.b	pipe_in(sp),d4	  (already 1 in d5 if there is an output)
	move.l	job_id(sp),d6	job id in d6
	lea	ex_extra(pc),a0 routine to be called to get a filename
	move.l	a3,-(sp)	save current parameter pointer
	addq.l	#8,a3		it used to point to program
	jsr	(a2)		call program's own initialisation
	move.l	(sp)+,a3
	tst.l	d0
	bpl.l	ex_set_in
	bra.s	ex_error
*
* extra routines to support program initialisation
*
ex_extra
	bra.s	ex_ut_fname
*
	movem.l d6/a2,-(sp)
	move.l	d6,d1		set owner job
	moveq	#uod.datd,d2
	bsr.l	ut_opdefxj	and open data channel for extra
	movem.l (sp)+,d6/a2
	rts
*
ex_ut_fname
	movem.l d4/d6/a2,-(sp) get a name/id without smashing the registers
	bsr.l	ut_fname
	movem.l (sp)+,d4/d6/a2
	rts

	end
