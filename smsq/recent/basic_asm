; SBasic routines for Recent Thing  v1.00  copyright (c) W. Lenerz 2015

	section exten

	xdef	rcnt_addf
	xdef	rcnt_gffj$
	xdef	rcnt_gffa$
	xdef	rcnt_jobs
	xdef	rcnt_gall
	xdef	rcnt_info
	xdef	rcnt_garr
	xdef	rcnt_galj
	xdef	rcnt_garj
	xdef	rcnt_load
	xdef	rcnt_save
	xdef	rcnt_remv
	xdef	rcnt_sync

	xref	rcnt_name

	xref	ut_gxst1
	xref	ut_gtstr
	xref	ut_gtli1
	xref	ut_rtfd1
	xref	ut_rtint
	xref	ut_gtlin
	xref	ut_chkri
	xref	ut_gxli2
	xref	ut_gxli1
	xref	ut_retst
	xref	gu_thjmp
	xref	ut_gtnm1

	include 'dev8_keys_recent_thing'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_thg'
	include 'dev8_keys_err'
	include 'dev8_keys_qlv'
	include 'dev8_keys_sbasic'
	include 'dev8_keys_chn'

;++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
; length = RCNT_INFO ({job_id,}str_nbr%,str_len%,max_nbr%)
; get info on a list of files
;   where:
;	length = space needed for getting all strings, including length word
;		  OR : negative error code
;	job_id = id of job the info is about
;	    EITHER as a long int where
;		0  means get the general list,
;		-1 means get the list for myself (=default)
;	    OR as a string with the name of the job
;	str_len% = RETURN parameter, max length of string
;	str_nbr% = RETURN parameter, number of strings currently held
;	max_nbr% = RETURN parameter, max number of strings in lists
;++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
rcnt_info
	moveq	#3,d0
	bsr.s	getjbId
	bne.s	inferr
	move.l	#'INFO',d2		; extension to use
	lea	rcnt_name,a0		; point to name of thing
	moveq	#-1,d3			; wait forever
	moveq	#-1,d1			; I will use the thing
	moveq	#sms.uthg,d0		; use thing
	jsr	gu_thjmp		; on return A2= ptr thg header, a1 to thg
	move.l	d0,d1			; ok?
	bne.s	inf_out 		; no, return error!
	move.l	a1,a0
	movem.l d7/a4,-(sp)		; call parameters
	move.l	sp,a1
	jsr	thh_code(a0)		; call extn thing
	addq.l	#8,sp
	move.l	d0,d5			; keep error
	move.l	d1,d6
	move.l	d2,d7
	move.l	a2,a4			; keep data returned
	lea	rcnt_name,a0		; free thing
	moveq	#sms.fthg,d0
	moveq	#-1,d1
	jsr	gu_thjmp
	move.l	d5,d1			; any error from my routine?
	bne.s	inf_out 		; yes, return it!
	bsr.s	sb_let			; nbr of strings
	swap	d7
	bsr.s	sb_let			; max str length
	move.l	d6,d7
	bsr.s	sb_let			; max allowed nbr of strings
	move.l	a4,d1			; space required
inf_out jmp	ut_rtfd1

inferr	moveq	#err.ijob,d1
	jmp	ut_rtfd1

; change paramter to which a3 points
sb_let	move.l	sb_arthp(a6),a1 	; point to math stack
	subq.l	#2,a1
	move.l	a1,sb_arthp(a6) 	; point to math stack
	move.w	sb.putp,a2
	move.w	d7,(a6,a1.l)
	jsr	(a2)			; assign to a variable
	addq.l	#8,a3			; point to next variable
	rts

; on entry, D0 = nbr of long int  parameters OTHER than the optional job ID
; on exit:
; d0 = 0 or err.ipar (wrong nbr of parameters)
; D7 = possible job ID , or -2 (if a4<>0)
; A4 = string (name of job) or 0
getjbID move.l	a5,-(sp)
	lsl.l	#3,d0			; number of necessary params
	moveq	#-1,d7			; preset myself for job ID
	sub.l	a4,a4			; and no name
	add.l	a3,d0
	sub.l	a5,d0
	beq.s	got_jid
	addq.l	#8,d0
	bne.s	err_bp2
; check what kind of parameter we got
cid_chk lea	8(a3),a5
	tst.b	(a6,a3.l)		; is it unused name?
	beq.s	job_gtnm		; ... yes, get name
	moveq	#$f,d0
	and.b	1(a6,a3.l),d0		; get variable type
	subq.b	#1,d0			; is it string?
	beq.s	job_gtnm		; ... yes, get it
; here it must be a long int
	jsr	ut_gxli1		; get one long integer parameter only
	bne.s	inferr			; oops
	move.l	(a6,a1.l),d7		; job ID
	bra.s	gtpar
job_gtnm
	bsr.l	ut_gtnm1		; get name
	bne.s	inferr
	lea	(a6,a1.l),a4		; job name
	moveq	#-2,d7			; special
gtpar	move.l	a5,a3
got_jid moveq	#0,d0
	move.l	(sp)+,a5
	rts
err_bp2 moveq	#err.ipar,d0
	move.l	(sp)+,a5
	rts
;+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
;
; RCNT_ADDF (job_ID,) filename$
;   adds a file name to the list
;	filename is the name of the file to add
;	job_id is the optional jobID the job supposed to have opened the file
;	(defaults to -1, i.e. myself)
;
;+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
rcnt_addf
	moveq	#8,d0			; one fixed parameter
	bsr.s	long_jobid		; get (optional) job id
	bne.s	ad_exit 		; ooops?

	move.l	d1,d4			; keep job id to set
	jsr	ut_gxst1		; get single string
	bne.s	ad_exit 		; karang
	lea	(a6,a1.l),a4		; string to set
sh_common
	move.l	#'ADDF',d2		; extension to use
	lea	rcnt_name,a0		; point to name of thing
	moveq	#-1,d3			; wait forever
	moveq	#-1,d1			; I will use the thing
	moveq	#sms.uthg,d0		; use thing
	jsr	gu_thjmp		; on return A2= ptr thg header, a1 to thg
	tst.l	d0			; ok?
	bne.s	ad_exit 		; no!
	move.l	a1,a0			; pointer to thing (!!!)
	movem.l d4/a4,-(a7)		; make parameter stack
	move.l	sp,a1			; and point to it
sp8	jsr	thh_code(a0)		; call extn thing
	addq.l	#8,sp			; reset stack
leave	move.l	d0,d4			; remember error
	lea	rcnt_name,a0		; free thing
	moveq	#sms.fthg,d0
	moveq	#-1,d1
	jsr	gu_thjmp
	move.l	d4,d0			; restore error
ad_exit rts


; Get optional job ID, which may NOT be string
;
; In:  d0 = parameter count for function without specified job ID * 8
; Out: d1 = job ID
;      a3 = updated
; ---
long_jobid
	movem.l a1,-(sp)
	add.l	a3,d0
	cmp.l	d0,a5			; exactly the parameter count w/o jobID?
	beq.s	gji_default		; yes, just use default

	jsr	ut_gtli1		; get one long integer
	bne.s	gji_exit
	addq.l	#8,a3			; and move past it
	move.l	0(a6,a1.l),d1		; get job ID
	addq.l	#4,sb_arthp(a6) 	; reset ri stack pointer
	bra.s	gji_ok

gji_default
	moveq	#-1,d1			; default to current job
gji_ok
	moveq	#0,d0
gji_exit
	movem.l (sp)+,a1
	rts


   
;+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
;
; file$ = RCNT_GFFA$ ()
;
; gets the first (=most recent) file name in the list.
; If list is empty, this will be a null length string.
;
;+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

rcnt_gffa$
	moveq	#-1,d7			; simulate for myself
	move.l	#'GFFA',d6
	bra.s	got_id

;+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
;
; file$ = RCNT_GFFJ$ (job_ID)
;
; gets the first (=most recent) file for the given job
; if none found, this is an empty string.
;
;	job_id is optional, if not given, search for current job
;
;+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

rcnt_gffj$
	move.l	#'GFFJ',d6

get_1str
	moveq	#0,d0
	bsr	getjbID 		; job ID in D7, possible string in A4
	bne.s	exit

got_id	moveq	#rc_maxlen+3,d1 	; max string size + length word+even
	jsr	ut_chkri		; ensure enough space on RI stack
	move.l	d1,d5			; this much space in buffer now
	sub.l	d5,a1			; go to start of RI space
	move.l	a1,a3			; keep maths stack pointer
g_str
	adda.l	a6,a1			; A1 = absolute pointer
	move.l	a1,a5			; save buffer pointer
	move.l	d6,d2			; extension to use
	lea	rcnt_name,a0		; point to name of thing
	moveq	#-1,d3			; wait forever
	moveq	#-1,d1			; I will use the thing
	moveq	#sms.uthg,d0		; use thing
	jsr	gu_thjmp		; on return A2= ptr thg header, a1 to thg
	tst.l	d0			; ok?
	bne.s	exit			; no!
	move.l	a1,a0			; pointer to thing (!!!)
	sub.l	#14,sp			; get some space

; a4 = nm ptr
; d7 = job id
; a5 = ptr to buffer
	exg	a4,a5			; a5 =nm ptr
	exg	a4,d7			; a4 = job id, d7=buffer

	move.l	sp,a1			; and point to it
	move.w	d5,(a1) 		; buffer size
	movem.l d7/a4/a5,2(a1)		; buffer, ID, ptr to name
gt_cont jsr	thh_code(a0)		; call extn thing
	add.l	#14,sp			; reset stack
	move.l	d0,d5			; remember error
	lea	rcnt_name,a0		; free thing
	moveq	#sms.fthg,d0
	moveq	#-1,d1
	jsr	gu_thjmp
	move.l	d5,d0			; restore error
	bne.s	exit			; error now?
	move.l	a3,a1
	jmp	ut_retst		; return string
exit	rts
   

;+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
;
; result% = RCNT_JOBS (length,buffer)
;
;  Get a list of all jobs into a buffer:
;  format in buffer : jobID,string
;  strings (length word + chars) always at an even address
;
;	buffer = space for list
;	length = length of buffer
;
;	result% = 0 or +ive:  number of jobs in the list
;		  else negative error code (e.g. "buffer full" if the buffer was
;		  too small)
;
;+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
rcnt_jobs
	move.l	#'JOBS',d7
	bra.s	getbufcom
		    

;+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
;
; result% = RCNT_GALL (length,buffer)
;
;  Get ALL file names from the general list into a buffer.  The file name
;	will be typical SMSQ strings (length word + chars) always at an even
;	address
;
;	length = length of buffer  - this should be at least as much as
;		 returned by the RCNT_INFO keyword
;	buffer = space for list
;
;	result% = 0 if all went ok, else negative error code :
;		    err.bffl - buffer too small
;		    err.ipar - wrong number of parameters
;		    err.ijob - wrong job ID
;		    any error from the thing use routine
;	if the error is err.bffl, as much as possible is filled in the buffer
;
;+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
rcnt_gall
	move.l	#'GALL',d7

getbufcom
	jsr	ut_gxli2		; get 2 long ints
	move.l	d0,d1
	bne.s	gallout 		; but couldn't
	add.l	a6,a1
	movem.l (a1),d5/d6		; length, buffer
	btst	#0,d6
	bne	err_bp			; buffer was not at even address
	lea	rcnt_name,a0		; point to name of thing
	moveq	#-1,d3			; wait forever
	moveq	#-1,d1			; I will use the thing
	move.l	d7,d2			; extension to use
	moveq	#sms.uthg,d0		; use thing
	jsr	gu_thjmp		; on return A2= ptr thg header, a1 to thg
	move.l	d0,d1			; ok?
	bne.s	gallout 		; no!
	move.l	a1,a0			; pointer to thing (!!!)
	movem.l d5/d6,-(a7)
	move.l	sp,a1			; and point to it
	jsr	thh_code(a0)		; call extn thing
	addq.l	#8,sp			; reset stack
	bra.s	retint
gallout jmp	ut_rtint		; return error



;+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
;
; result% = RCNT_GALJ ({jobID,} length, buffer)
;
;  Get ALL file names for a job into a buffer.	The file names	will be typical
;  SMSQ strings (length word + chars) always at an even address
;
;	length = length of buffer  - this should be at least as much as
;		 returned by the RCNT_INFO keyword for this jobs
;	buffer = space for list
;	job_id = (optional) id of job:
;	    EITHER as a long int where
;		-1 means get the list for myself (=default)
;	    OR as a string with the name of the job
;
;	result% = 0 if all went ok, else negative error code :
;		    err.bffl - buffer too small
;		    err.ipar - wrong number of parameters
;		    err.ijob - wrog job ID
;		    any error from the thing use routine
;
;+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
rcnt_galj
	moveq	#-1,d7
	sub.l	a4,a4			; preset no job ID
	moveq	#16,d0
	add.l	a3,d0
	sub.l	a5,d0			; is there a job ID?
	beq.s	gettwo			; no, continue with defaults
	addq.l	#8,a3
	jsr	ut_gxli2		; get 2 long ints
	move.l	d0,d1
	bne.s	galjbp			; oops
	movem.l (a6,a1.l),d5/d6 	; length/buffer
	move.l	a3,a5
	subq.l	#8,a3			; a3 & a5 point to 1st param (jobID)
	moveq	#0,d0			; just the job id
	bsr	getjbID 		; get job ID (d7,a4)
	bne.s	galjnjb 		; wrong jobID
	bra.s	galjcom

gettwo	jsr	ut_gxli2		; get 2 long ints
	move.l	d0,d1
	bne.s	galjbp			; but couldn't
	movem.l (a6,a1.l),d5/d6 	; length/buffer
galjcom btst	#0,d6
	bne	err_bp			; buffer not at even address
	moveq	#-1,d3			; wait forever
	moveq	#-1,d1			; I will use the thing
	move.l	#'GALJ',d2		; extension to use
	lea	rcnt_name,a0		; point to name of thing
	moveq	#sms.uthg,d0		; use thing
	jsr	gu_thjmp		; on return A2= ptr thg header, a1 to thg
	move.l	d0,d1			; ok?
	bne.s	galjout 		; no!
	move.l	a1,a0			; pointer to thing (!!!)
	movem.l d5-d7/a4,-(a7)		; parameters : length, buffer, jbID, nm ptr
	move.l	sp,a1
	jsr	thh_code(a0)		; call extn thing
	add.l	#16,sp
retint	move.l	d0,d5			; remember error
	lea	rcnt_name,a0		; free thing
	moveq	#sms.fthg,d0
	moveq	#-1,d1
	jsr	gu_thjmp
	move.l	d5,d1			; restore possible error
galjout jmp	ut_rtint		; return 0 or error
galjbp	moveq	#err.ipar,d1
	jmp	ut_rtint		; return error
galjnjb moveq	#err.ijob,d1
	jmp	ut_rtint		; return error


;+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
;
; RCNT_GARR array$
;
; Get all filenames in an ARRay, from the general list
;
;	array$=a 2 dimensional string array.
;	ATTENTION: the array will be filled in starting at element 0.
;
;+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
rcnt_garr
	move.l	a3,d0
	addq.l	#8,d0
	sub.l	a5,d0			; exactly one param?
	bne.s	err_bp			; no->...
	move.w	(a6,a3.l),d1		; d1 = type of parameter
	move.w	d1,d4			; keep this for later
	andi.w	#$f0f,d1
	cmpi.w	#$301,d1		; is it a string array?
	bne.s	err_bp			; no, so error bad parameter
	move.l	4(a6,a3.l),d1		; d1=pointer to arr descriptor
	bmi.s	err_bp			; if neg(=undefined): error bad par
	add.l	$28(a6),d1		; point to the arr descriptor now
	movea.l d1,a4			; keep
	cmp.w	#2,4(a6,a4.l)		; must be 2 dims
	bne.s	err_bp			; else error->...
	move.w	6(a6,a4.l),d6		; number of elements in array
	addq.w	#1,d6			; SBasic starts at element 0
	move.w	8(a6,a4.l),d7		; "length" of each array elmt
	move.l	(a6,a4.l),a4
	add.l	$28(a6),a4		; point to value for array
	add.l	a6,a4			; not relative to a6

	lea	rcnt_name,a0		; point to name of thing
	moveq	#-1,d3			; wait forever
	moveq	#-1,d1			; I will use the thing
	move.l	#'GARR',d2		; extension to use
	moveq	#sms.uthg,d0		; use thing
	jsr	gu_thjmp		; on return A2= ptr thg header, a1 to thg
	tst.l	d0			; ok?
	bne.s	garrout 		; no!
	move.l	a1,a0			; pointer to thing (!!!)
	subq.l	#8,sp			; get some space
	move.l	sp,a1			; and point to it
	move.l	a4,(a1) 		; array
	move.w	d6,4(a1)		; nbr of elements
	move.w	d7,6(a1)		; length of string
	bra	sp8
 
err_bp	moveq	#err.ipar,d0
garrout rts
						  


;+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
;
; RCNT_GARJ {job_ID,}array$
;
; Get all filenames in an ARRay, for one Job
;	JOB_id the id of the job to be gotten
;	array$=a 2 dimensional string array.
;	ATTENTION: the array will be filled in starting at element 0.
;
;+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
rcnt_garj
	moveq	#1,d0
	bsr	getjbID
	bne.s	garjout 		; d7 job ID, a4 ptr to name
	move.w	(a6,a3.l),d1		; d1 = type of parameter
	andi.w	#$f0f,d1
	cmpi.w	#$301,d1		; is it a string array?
	bne	err_bp			; no, so error bad parameter
	move.l	4(a6,a3.l),d1		; d1=pointer to arr descriptor
	bmi	err_bp			; if neg(=undefined): error bad par
	add.l	$28(a6),d1		; point to the arr descriptor now
	movea.l d1,a3			; keep
	cmp.w	#2,4(a6,a3.l)		; must be 2 dims
	bne	err_bp			; else error->...
	move.w	6(a6,a3.l),d6		; number of elements in array
	addq.w	#1,d6			; SBasic starts at element 0
	swap	d6
	move.w	8(a6,a3.l),d6		; "length" of each array elmt
	move.l	(a6,a3.l),d5
	add.l	$28(a6),d5		; point to value for array
	add.l	a6,d5			; not relative to a6

	lea	rcnt_name,a0		; point to name of thing
	moveq	#-1,d3			; wait forever
	moveq	#-1,d1			; I will use the thing
	move.l	#'GARJ',d2		; extension to use
	moveq	#sms.uthg,d0		; use thing
	jsr	gu_thjmp		; on return A2= ptr thg header, a1 to thg
	tst.l	d0			; ok?
	bne.s	garjout 		; no!
	move.l	a1,a0			; pointer to thing (!!!)
	sub.l	#16,sp			; get some space
	move.l	sp,a1			; and point to it
	movem.l d5-d7/a4,(a1)		; save parameters
	jsr	thh_code(a0)		; call extn thing
	add.l	#16,sp			; reset stack
	bra	leave
garjout rts

	 

;+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
;
; RCNT_LOAD
;
;	LOAD lists from configured file  : no parameters
;
;++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
rcnt_sync
	move.l	#'SYNC',d7
	bra.s	savecom
		

;+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
;
; RCNT_LOAD
;
;	LOAD lists from configured file  : no parameters
;
;++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
rcnt_load
	move.l	#'LOAD',d7
	bra.s	savecom



;+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
;
; RCNT_SAVE
;
;	SAVE lists to configured file  : no parameters
;
;++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
rcnt_save
	move.l	#'SAVE',d7
savecom lea	rcnt_name,a0		; point to name of thing
	moveq	#-1,d3			; wait forever
	moveq	#-1,d1			; I will use the thing
	move.l	d7,d2			; extension to use
	moveq	#sms.uthg,d0		; use thing
	jsr	gu_thjmp		; on return A2= ptr thg header, a1 to thg
	move.l	d0,d1			; ok?
	bne.s	garjout 		; no!
	jsr	thh_code(a1)		; call extn thing
	bra	leave			; release thing, done



;+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
;
; RCNT_REMV job_ID
;
;    removes the list for the fiven job
;
;	job_id is optional, if not given, remove list for current job
;
;+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
rcnt_remv
	moveq	#0,d0
	bsr	getjbID 		; job ID in D7, possible string in A4
	bne.s	garjout

	lea	rcnt_name,a0		; point to name of thing
	moveq	#-1,d3			; wait forever
	moveq	#-1,d1			; I will use the thing
	moveq	#sms.uthg,d0		; use thing
	move.l	#'REMV',d2
	jsr	gu_thjmp		; on return A2= ptr thg header, a1 to thg
	tst.l	d0			; ok?
	bne.s	garjout 		; no!
	movem.l d7/a4,-(a7)		; ID, ptr to name
	move.l	a1,a0
	move.l	a7,a1
	jsr	thh_code(a0)		; call extn thing
	addq.l	#8,sp			; reset stack
	bra	leave
   
			


	end
