; Most of the extension things for the Recent thing   V1.00 (c) W. Lenerz, 2015

;  2015-11-29	 1.00	 initial release version


;---------------------------------------------------------------------
; the list maintained is a circular lifo buffer
; for each job a memory space is allocated in the common heap. These are in a
; simple linked list

	section exten

	xdef	rcnt_thing		; name
	xdef	cklp2

	xref	rc_sync

	xref	gu_thjmp		; set up thing
	xref.l	rcnt_vers		; my version
	xref	sms_ckid
	xref	findheap		; locate /create heap for jobID
	xref	findhsh 		; locate heap for job with a hash
	xref	add2hp			; add file to heap
	xref	rcfg_nbr		; nbr of files per list
	xref	rcfg_file
	xref	hash

	include dev8_keys_thg
	include dev8_keys_err
	include dev8_keys_qdos_io
	include dev8_keys_qdos_ioa
	include dev8_keys_qdos_sms
	include dev8_keys_recent_thing
	include dev8_mac_thg
	include dev8_mac_assert


; --------------------------------------------------------------------
; the expected parameters for the thing calls
; note these aren't really used, but the macro needs something...
;
p_addf	dc.w	thp.ulng,thp.str,0		; parameters for ADDF extension
p_gffm	dc.w	thp.ulng,thp.swrd,thp.str,0	;
p_gtal	dc.w	thp.ulng,thp.ulng,0		; ***** !!!! 2 LWs !!!!  <<<<<<<<<
p_gtar	dc.w	thp.ulng,0
p_galj	dc.w	thp.ulng,thp.ulng,thp.ulng,0
;
;---------------------------------------------------------------------


rta.reg reg	d1/d3/d7/a1/a3/a4

rcnt_thing

;+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
;
; INFO extension : Get INFO on list : parameter : job ID, name pointer
;
;	If the job ID is -3, this gets info on the general list, else
;	info on the list for the job in question
;
;	If the job ID doesn't refer to a valid job (jobid = -2) then the
;	optional name is used to make a hash and look for the list of the job
;	with that name hash.
;
;	d1  r	maximum nbr of files per list
;	d2  r	2 words: High word max str length (NOT incl. length word) |
;		Low word nbr of strings
;		if no file is yet contained in the list, d2 will be 0 (this
;		should normally not happen)
;	d3	pointer to heap space IF one was looking for a job list, not the
;		general list
;	a1 c  p parameter stack pointer
;	a2 c  r thing linkage block  /	needed buffer size (long). The size is
;		the size necessary to store all strings + length words +
;		possible evening out of the individual string lengths.
;
;	error returns :
;	D0 = 0 - OK
;	   = err.ijob there is no list for this job
;
;++++
;
; parameter list:
; job ID	(LW) (-1=myself, -2 check with the name, -3 check general list)
; name pointer	(LW) pointer to job name if jobID=-2, or 0 if none
;
;+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
rcnt_info thg_extn {INFO},rcnt_jobs,p_gffm

inforeg reg	d4/d5/a3/a4
tinfo	movem.l inforeg,-(sp)
	move.l	(a1),d1 		; job ID
	cmp.l	#-3,d1			; is it for general list?
	beq.s	general 		; yes ->...
	move.l	d7,d5			; keep
	moveq	#-1,d7			; don't create heap
	bsr	findheap		; find heap for this job
	exg	d5,d7			; get d7 back but don't change cond. codes
	beq.s	jberr			; couldn't find the heap for this job
; here I found the job's heap
	moveq	#hd_entryl,d4		; length of one entry in list
	move.l	rcnt_hend(a3),a4	; end of space for list
	move.l	a3,d5			; keep this pointer (will be returned in D3)
	lea	rcnt_hdr(a3),a3 	; point to start of space for list
get_lst clr.l	d0			; will hold nbr of strings
	clr.l	d2			; max length of current string
	clr.l	d3			; temp var
	sub.l	a2,a2			; will be total space needed for list

inf_lp1 move.w	(a3),d3 		; point to name
	beq.s	next			; none, empty slot
	addq.l	#1,d0			; one more string
	cmp.w	d3,d2			; bigger than current max length?
	bge.s	nexchg			; no->...
	move.w	d3,d2			; yes, set current max length
nexchg	addq.w	#3,d3
	bclr	#0,d3			; length word + make even
	add.l	d3,a2			; add to total space needed
next	add.l	d4,a3			; point next name
	cmp.l	a3,a4			; done?
	bne.s	inf_lp1 		; no->...
	swap	d2
	move.w	d0,d2			; D2 = length | nbr
	clr.l	d1
	lea	rcfg_nbr,a3		; nbr of files per list
	move.b	(a3),d1
	move.l	d5,d3			; pointer to heap space
	clr.l	d0
	movem.l (sp)+,inforeg		; done
	rts
 
; here we check the general list
general clr.l	d5			; no pointer to heap
	lea	rcnt_end+4(a2),a3	; header end, start of space for list, AFTER first job ID
	move.l	rcnt_stop(a2),a4	; end of space for list
	addq.l	#4,a4			; after jobID
	moveq	#rc_entryl,d4		; spacing in list
	bra.s	get_lst

; error, no heap could be found for the job
jberr	clr.l	d1			;
	clr.l	d2			;
	clr.l	d3			; no info
	moveq	#err.ijob,d0		; nothing found for this job
	movem.l (sp)+,inforeg		; done
	rts



;+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
;
; JOBS extension : get a listing of all JOBS this thing holds a list for
;						 length of buffer, buffer.
;
;	d0  r	special return
;	a1 c s	parameter stack pointer
;	a2 c  p thing linkage block
;
;	this returns in D0
;	-> +ve number (or 0) : number of job names
;	-> err.bufl if the buffer was too short. In this case as much as
;		    possible is filled in
;
;	The buffer is filled in as follows: jobID, job name (standard string,
;	evened out). The list is termineted with a long word of -1.
;	If the string length is 0, then the job has no name.
;
;	!!!!! The length of the buffer is passed as a long word !!!!!
;+++
;
; parameter list:
;
; length of buffer  (LW) (!)
; pointer to buffer (LW)
;
;++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

rcnt_jobs thg_extn {JOBS},rcnt_addf,p_gffm

jobsreg reg	d1-d4/d7/a0/a3-a5

	movem.l jobsreg,-(a7)		;
	move.l	4(a1),a4		; start of buffer
	clr.l	d4			; will hold nbr of job names found
	move.l	a4,a5
	add.l	(a1),a5 		; end of buffer
	subq.w	#4,a5			; - 4 for safety
	move.l	rcnt_heap(a2),d1	; pointer to first heap
	beq	jobout			; no heap for any job,done
joblp
	move.l	d1,a3			; point to job space
	move.l	rcnt_jbID(a3),d1	; ID of this job
	cmp.l	#-1,d1			; hmm, could be a saved job not active
	beq	savjb			; get stored name
	move.l	d1,(a4)+		; store jobID in buffer
	cmp.l	a4,a5			; still enough buffer space?
	ble	jbbuferr		; no

jobinfo moveq	#sms.injb,d0		; get Job information
	moveq	#0,d2			; scan whole tree
	trap	#1			; find this job
	tst.l	d0
	bne	strdnm			; can't, may be deleted job
	lea	6(a0),a1		; move to flag
	cmp.w	#$4afb,(a1)+		; check flag
	bne	noname			; no name for job
gtname	clr.l	d1
	move.w	(a1)+,d1		; length of name
	beq	noname

; now remake the hashes for this job!!!!!!!
; this is necessary if jobs change their names (e.g. xchange)
	move.l	d1,a0			; keep length
	move.l	a3,d7			; ditto
	lea	-2(a1),a3		; ptr to name
	bsr	hash			; make hash
	move.l	d7,a3			; get heap back
	cmp.l	rcnt_hsh(a3),d1 	; same hash?
	beq.s	jbcont
	move.l	d1,rcnt_hsh(a3) 	; new hash
	movem.l a0/a1,-(a7)		; copy name to name store area
	move.l	a0,d1			; name length
	moveq	#hd_entryl-4,d0 	; max space or name
	sub.l	d1,d0
	ble.s	nocpy			; name wouldn't fit
	move.l	rcnt_hend(a3),a0
	addq.l	#2,a0			; point to space for name store
	move.w	d1,(a0)+
jbclp	move.b	(a1)+,(a0)+
	dbf	d1,jbclp
nocpy	movem.l (a7)+,a0/a1
jbcont	move.l	a1,d7			; keep
	move.l	a0,d1
	move.l	d1,d3
	addq.l	#7,d1			; +2 length +4 next job ID +1 even
	bclr	#0,d1
	move.l	a4,a1
	add.w	d1,a1
	cmp.l	a1,a5			; will all of this fit?
	blt.s	jbbuferr		; no, done->
	move.w	d3,(a4)+
	addq.w	#1,d3
	lsr.w	#1,d3
	subq.w	#1,d3			; but dbf
	move.l	d7,a1
jblp3	move.w	(a1)+,(a4)+
	dbf	d3,jblp3

jbdolp	addq.l	#1,d4			; one more job found
jblp2	move.l	rcnt_next(a3),d1	; another heap?
	bne	joblp			; yes
jobout	move.l	d4,d0
	move.l	#-1,(a4)		; signal end of list
	movem.l (sp)+,jobsreg		; d0 = nbr of jobs or -ive error
	rts
; saved job
savjb	move.l	#-2,(a4)+		; store "jobID"
	cmp.l	a4,a5			; still enough buffer space?
	ble.s	jbbuferr		; no

; we get here if job with that ID couldn't be found
strdnm
	move.l	rcnt_hend(a3),a1
	addq.l	#2,a1			; point to possible stored name
	tst.w	(a1)			; any name there?
	bne	gtname			; yes, use it
	lea	unknown,a1		; string "unknown job"
	bra	gtname

; we get here if job has a valid ID, but no name
noname	move.w	#0,(a4)+		; job has ID but no name
	move.l	a4,d1
	addq.l	#4,d1
	cmp.l	d1,a5			; enough space for another ID ?
	bgt.s	jbdolp			; yes -> try next job
	subq.w	#2,a4			; no, move back over length word

jbbuferr move.l #-1,-(a4)		; show end of list
	moveq	#err.bffl,d0		; error
	movem.l (sp)+,jobsreg		;
	rts

unknown dc.w	unkn-*-2
	dc.b	'Unknown (deleted) job'
unkn
	dc.w	0


; check for valid job ID
; d0   s
; d1 cr  job ID to check (may be -1) | true job id of job to check
; condition code Z if valid ID found, else NZ
cidreg	reg	d2-d3/d7/a0/a1
checkid tst.l	d1
	beq.s	chkout
	moveq	#err.ijob,d0
	movem.l cidreg,-(sp)		; keep
	cmp.l	#-1,d1			; myself?
	blt.s	chk_out 		; no and this is definitely not valid
	bne.s	realID			; not myself, seems to be real jobID
	moveq	#sms.info,d0
	trap	#1			; get current job ID into D1, sysvars into A0
	bra.s	ckout1			; done

; check whether the ID passed is a valid ID
realID	move.l	d1,d7			; keep ID
	moveq	#sms.injb,d0
	clr.l	d2
	trap	#1			; check that this is a valid job ID
	move.l	d7,d1			; get ID back
ckout1	tst.l	d0
chk_out movem.l (sp)+,cidreg		; get regs back and return
chkout	rts

;++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
;
; ADDF extension : add a file : parameters : job id, ptr to string
;		   the job ID may be passed as -1
;
;	d0  r	0 or error
;	d1    p
;	d2   s	scratch
;	d3    p
;	d4    p
;	d5
;	d6
;	d7    p
;	a1 c  p parameter stack pointer
;	a2 c  p thing linkage block
;
;++++
;
; parameter list:
; job_id	(LW)  must be ID of a currently executing job
; ptr to string (LW)
;
;++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
rcnt_addf thg_extn {ADDF},rcnt_gffa,p_addf
	movem.l rta.reg,-(sp)
	moveq	#0,d0
	lea	rcfg_file,a3		; are we trying to open the save file?
	cmp.l	a3,a0
	beq	add_out 		; yes, don't add to list!
	move.l	(a1),d1 		; job ID
	bsr.s	checkid 		; check it (and get it if -1)
	bne.s	add_out 		; ooops
addfgi	move.w	sr,d7
	trap	#0			; make atomic  (extn may be called from
					; jobs, not only trap#2 open call)
	move.l	a1,-(a7)
	move.l	rcnt_stop(a2),a4	; end of space for list
	lea	rcnt_end(a2),a3 	; point to first entry in list
	bsr.s	chk_ext 		; check whether the file exists in general list
	beq.s	adheap			; it does, check if it exists in job list
; here file diesn't exist in general list
	move.l	rcnt_first(a2),a3
	move.l	a3,d4			; keep
addlp1	tst.w	4(a3)			; next entry - if it's empty, nothing there
	beq.s	add_fil 		; so add file here
	add.l	#rc_entryl,a3		; point to next space
	cmp.l	a4,a3			; shoot over end of space for list?
	blt.s	no_over 		; no
	lea	rcnt_end(a2),a3 	; point to start of space for list
no_over cmp.l	a3,d4			; did we come round to first entry?
	bne.s	addlp1			; no, so continue
					; here we looped around: list is full
	add.l	#rc_entryl,a3		; point to next entry
	cmp.l	a4,a3			; would we overshoot end?
	blt.s	add_fil 		; no, fill in name
	lea	rcnt_end(a2),a3 	; yes, point to start of space for list

add_fil move.l	a3,d4
	move.l	d1,(a3)+		; store job id
	move.l	4(a1),a1		: name
	move.w	(a1)+,d0
	beq.s	rad_out 		; huh, 0 length file? change nothing, then
	cmp.w	#rc_maxlen,d0		; max allowed file name size
	ble.s	ad1
	move.w	#rc_maxlen,d0
ad1	move.w	d0,(a3)+		; file length
	subq.w	#1,d0
addlp2	move.b	(a1)+,(a3)+
	dbf	d0,addlp2
	move.l	d4,rcnt_first(a2)	; new first
adheap	move.l	(sp),a1 		; get parameter stack back
	move.l	d7,d4			; keep status reg
	moveq	#0,d7			; create heap if job not found
aa	bsr	findheap		;
	exg	d4,d7			; get sr into d7, don't change cond codes
	beq.s	ad_done 		; no heap found/created for this job (?????)
	bsr	add2hp			: add to this heap, too

ad_done moveq	#0,d0
rad_out addq.l	#4,a7			: get rid of d1
	move.w	d7,sr
add_out movem.l (sp)+,rta.reg		; done adding file
	tst.l	d0
	rts
	   
; this checks whether a file to be added is already in the main list.
; if it is, the file will not be added again
; (note : This is called in supervisor mode)
;	   crsp
;	d0   s
;	d1    p
;	d2   s
;	d7    p
;	a1 c  p parameter stack pointer
;	a2 c  p thing linkage block
;	a3 c  p first entry in list
;	a4 c  p end of space for list
;	a5    p
;	a6    p
;
; Conditon code is Z if file found, else NZ

chk.reg reg    d1/d3/d7/a0/a3-a5
				
chk_ext movem.l chk.reg,-(sp)
	lea	rcnt_ucfn(a2),a5	; will be uncased filename
	move.l	4(a1),a0		; file name
	move.w	(a0)+,d0
	beq.s	ck_out			; 0 length so file is already there
	move.w	d0,(a5)+		; file length
	move.w	d0,d2			; keep length
	move.l	a5,d7			; keep pointer
	lsr.w	#1,D0			; word sized move
cklp1	move.w	(a0)+,d1
	and.w	#$dfdf,d1
	move.w	d1,(a5)+
	dbf	d0,cklp1

	addq.l	#4,a3			; AFTER job ID
	addq.l	#4,a4			; same
	moveq	#rc_entryl-2,d3 	; offset to next name in list

; this portion here is also called from addheap
cklp2	move.w	(a3)+,d0		; next name in list
	beq.s	cknext			; slot is empty
	cmp.w	d2,d0			; same length?
	bne.s	cknext			; no, so not found here
	lsr.w	#1,d0			; word sized move
	beq.s	over			; nothing to do, except for 1 char at end
	subq.w	#1,d0
	move.l	a3,a0			; name in list
	move.l	d7,a5			; name of file opened
cklp3	move.w	(a0)+,d1		; next 2 chars of name in list
	andi.w	#$dfdf,d1
	cmp.w	(a5)+,d1		; are they the same?
	bne.s	cknext			; no->...
	dbf	d0,cklp3
; check last char if name has odd length
over	btst	#0,d2			; uneven length?
	beq.s	ck_out			; no, so match found
	move.b	(a0)+,d1		; next char of name in list
	andi.b	#$df,d1
	cmp.b	(a5)+,d1		; are they the same?
	beq.s	ck_out			; yes, names are equal

cknext	add.l	d3,a3
	cmp.l	a4,a3
	bne.s	cklp2
	moveq	#-1,d0
ck_out	movem.l (sp)+,chk.reg
	rts

;+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
;
; GFFA extension : Get First File for Any job (from general list):
;						buffer length, buffer
;
;	a1 c  p parameter stack pointer
;	a2 c  p thing linkage block
;
;	this returns
;	0 if OK
;	err.bffl if the buffer for the return string is too small - in this...
;	... case nothing is copied to the buffer.
;
;	if no file is found the string length returned is 0
;
;+++
;
; parameter list:
;
; (Signed!) word with max length of buffer
; ptr to buffer (LW)
;
;+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
rcnt_gffa thg_extn {GFFA},rcnt_gall,p_gffm
gffareg reg	d1/d3/a1/a3
	movem.l gffareg,-(sp)
	move.l	rcnt_first(a2),a3	; point to first file
	addq.l	#4,a3			; jump over job id
	move.w	(a3)+,d1		; length of string
	move.w	(a1),d3 		; length of buffer
	ble.s	gerr			; 0 or negative length - what????????
	subq.w	#2,d3			; nbr of bytes needed after length word
	ble.s	gerr			; not enough space!
	cmp.w	d1,d3			; is buffer big enough?
	blt.s	gerr			; no it isn't
	move.l	2(a1),a1		; point to return space
	move.w	d1,(a1)+		; fill in length (may be 0)
	beq.s	gout			; 0 length string - finished
	subq.w	#1,d1
cpy_lp	move.b	(a3)+,(a1)+
	dbf	d1,cpy_lp		; copy my string to return string
gout	clr.l	d0
	movem.l (sp)+,gffareg		; done adding file
	rts
gerr	moveq	#err.bffl,d0		; signal buffer full
	movem.l (sp)+,gffareg		;
	rts

  
;+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
;
; GALL extension : Get ALL files from general list: fill in buffer with all
;		       files from general list : length of buffer, ptr to buffer.
;
;	a1 c  p parameter stack pointer
;	a2 c  p thing linkage block
;
;	this returns
;	0 if OK
;	err.bufl if buffer was too short, in this case as much as is possible...
;	...is filled in
;
;	The buffer will contain a list of file names, standard strings. It is
;	terminated with a null word (0).
;
;+++
;
; parameter list:
;
; length of buffer  (LW) (!)
; pointer to buffer (LW)
;
;++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
rcnt_gall thg_extn {GALL},rcnt_garr,p_gtal

rta2.reg reg	 d1/d3-d7/a1/a3-a5
gallreg reg	d1/d3/d7/a1/a3/a4
	movem.l gallreg,-(sp)
					
	move.l	rcnt_first(a2),a3	; first element in list
	addq.l	#4,a3			; point after ID
	lea	rcnt_end(a2),a4 	; start of space for list
	addq.l	#4,a4
	move.l	(a1),d3 		; buffer size
	beq.s	gallerr 		; ah, c'me on!
	move.l	4(a1),a1		; pointer to buffer
	move.l	a3,d7			; keep pointer
	clr.l	d1

galllp1 move.w	(a3)+,d1		; bytes to copy for this string
	beq.s	cpyend1 		; nothing
	move.l	d1,d0
	addq.l	#3,d0
	bclr	#0,d0
	sub.l	d0,d3			; is there still enough space in the buffer?
	ble.s	gallerr 		; no, error, done (I also need space for null word at end)
	move.w	d1,(a1)+		; copy length
	lsr.w	#1,d0			; word sized copy
	subq.w	#2,d0			; - length word & dbf
galllp3 move.w	(a3)+,(a1)+
glcp1	dbf	d0,galllp3
		  
cpyend1 sub.l	#rc_entryl,d7		; point to previous entry
	move.l	d7,a3			;
cmp1	cmp.l	a4,a3			; would we overshoot start of space for list?
	bge.s	galllp1 		; no, copy again

; when we get here, we copied from the first element to the start of the space
; of the list - now we need to copy from the end of the space of the list to
; the first element

	move.l	rcnt_stop(a2),a3	; end of space for list
	sub.l	#rc_entryl-4,a3 	; point after job ID
	move.l	rcnt_first(a2),a4	; start of space for list
	addq.l	#4,a4
	move.l	a3,d7			; keep pointer
	bra.s	cmp2

galllp2 move.w	(a3)+,d1		; bytes to copy
	beq.s	cpyend2 		;
	move.l	d1,d0
	addq.l	#3,d0
	bclr	#0,d0
	sub.l	d0,d3			; is there still space in the buffer?
	ble.s	gallerr 		; no, error (I need 2 bytes for null word at end)
	move.w	d1,(a1)+		; copy length
	lsr.w	#1,d0
	subq.w	#2,d0
galllp4 move.w	(a3)+,(a1)+
glcp2	dbf	d0,galllp4
		  
cpyend2 sub.l	#rc_entryl,d7
	move.l	d7,a3
cmp2	cmp.l	a3,a4			; first element reached (we've come around)?
	bne.s	galllp2 		; no
	clr.l	d0			; yes, done, no error
gallout clr.w	(a1)
	movem.l (sp)+,rta2.reg
	tst.l	d0
	rts

gallerr clr.w	(a1)
	moveq	#err.bffl,d0
	bra.s	gallout



;+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
;
; GARR extension : Get ALL files into an ARRay: fill in array with all files.
;		 parameters : pointer, nbr of elmts, length of element in array
;
;	a1 c  p parameter stack pointer
;	a2 c  p thing linkage block
;
;	this returns :
;	0 if OK. If the nbr of elements in the array is smaller than the nbr
;		of elements in the thing, then only the first elements in
;		in the list are copied, to fill up the array. This DOES NOT
;		GIVE RISE TO AN ERROR!
;	err.ipar if the array had a null dimension or if the length of the elements
;		in the array is smaller than the max length of a string in the
;		list. NOTE: the max length of the strings in the list amay vary
;		if a files with a longer name is added, or if the longest file
;		falls out of the list.
;
;+++
;
; parameter list:
;
; pointer to  array, NOT relative to A6 		(LW)
; number of elements in array				(WORD)
; length of elements in array, INCLUDING length word	(WORD)
;
;++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

garrreg reg	d1-d3/d7/a1-a4
rcnt_garr thg_extn {GARR},rcnt_gffj,p_gtal
		
	movem.l garrreg,-(sp)
	move.l	a2,a3
	move.l	(a1),d7 		; keep ptr to array
	move.l	#-3,(a1)		; set "job id" to "get general list"
	jsr	tinfo			; d2 = max length of string (NOT incl.
	move.l	a3,a2			;  ....length word) | nbr of strings
	tst.w	d2
	beq	garrout 		; there is nothing to copy: no elements in list
	clr.l	d0
	move.w	4(a1),d0		; number of elements in array
	beq	garrerr 		; none, this is an error
	cmp.w	d0,d2			; do we have space for all elements in list?
	ble.s	gr_ok1			; yes->...
	move.w	d0,d2			; no, only this much
gr_ok1	swap	d2			; max length of element in list
	addq.w	#2,d2			; + length word
	move.w	6(a1),d0		; length of element in array
	cmp.w	d0,d2			; will biggest elements fit in array?
	bgt	garrerr 		; no, so this is a real error
	swap	d2			; d2 = nbr of elements to copy

; d0 = length of element in array
; d2 = nbr of elements to copy
; d7 = array
	move.l	rcnt_first(a2),a3	; first element in list
	addq.l	#4,a3			; point after ID
	lea	4+rcnt_end(a2),a4	; start of space for list, after job ID of 1st element
	move.l	d7,a1			; pointer to array
	move.l	a3,d7			; keep pointer
	clr.l	d1

garrlp1 move.w	(a3)+,d1		; bytes to copy
	beq.s	garcpy1 		; none
	move.w	d1,(a1)+		; copy length
	move.l	d0,d3			; length of one element in array
	sub.l	d1,d3			; number of bytes to set to 0 at end
	subq.l	#1,d1
garrlp2 move.b	(a3)+,(a1)+
	dbf	d1,garrlp2
	subq.l	#3,d3			; minus the length word & dbf
	blt.s	garcpy1
garz1	move.b	#0,(a1)+		; zero out space at end (for qliberator)
	dbf	d3,garz1
garcpy1 subq.w	#1,d2
	beq.s	garrout 		; done copying
	sub.l	#rc_entryl,d7		; point to previous entry
	move.l	d7,a3
	cmp.l	a4,a3			; would we overshoot start of space for list?
	bge.s	garrlp1 		; no, copy again

; when we get here, we copied from the first element to the start of the space
; of the list - now we need to copy from the end of the space for the list to
; the first element

	move.l	rcnt_stop(a2),a3	; end of space for list
	sub.l	#rc_entryl-4,a3 	; point after job ID
	move.l	4+rcnt_first(a2),a4	  ; start of space for list
	move.l	a3,d7			; keep pointer

garrlp3 move.w	(a3)+,d1		; bytes to copy
	beq.s	garcpy2 		;
	move.w	d1,(a1)+		; copy length
	move.l	d0,d3			; length of one element in array
	sub.l	d1,d3			; number of bytes to set to 0 at end
	subq.l	#1,d1
garrlp4 move.b	(a3)+,(a1)+
	dbf	d1,garrlp4
	subq.l	#3,d3			; minus the length word & dbf
	blt.s	garcpy2
garz2	move.b	#0,(a1)+		; zero out space at end (qliberator)
	dbf	d3,garz2
garcpy2 subq.w	#1,d2
	beq.s	garrout
	sub.l	#rc_entryl,d7		; point to previous entry
	move.l	d7,a3
	cmp.l	a4,a3			; would we overshoot start of space for list?
	bne.s	garrlp3 		; no, copy again
garrout clr.l	d0			; yes, done, no error
	movem.l (sp)+,garrreg
	rts
garrerr moveq	#err.ipar,d0
	movem.l (sp)+,garrreg
	rts

 
;+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
;
; GFFJ extension : Get First file For Job: buffer length, ptr to buffer,
;						job id, name pointer
;
;	a1 c  p parameter stack pointer
;	a2 c  p thing linkage block
;
;	If the job ID doesn't refer to a valid job (jobid = -2) then the
;	optional name is used to make a hash and look for the job with that
;	name.
;
;	this returns
;	0 if OK (if no file is found for job, the string length returned is 0)
;	err.bffl if the buffer for the return string is too small - in this...
;	... case nothing is copied to the buffer.
;
;+++
;
; parameter list:
; (signed!) word with max length of buffer
; ptr to buffer (LW)
; job_id	(LW) should be -2 if a name is supplied
; name pointer	(LW) pointer to job name if jobID = -2, or 0
;
;+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
gffjreg reg	d1/d3/d7/a1/a3
rcnt_gffj thg_extn {GFFJ},rcnt_galj,p_gffm
	movem.l gffjreg,-(sp)
	addq.l	#6,a1			; point to job ID
	moveq	#-1,d7
	bsr	findheap		; find heap for this job
	lea	-6(a1),a1		; point to param stack again
	beq.s	gj_nf			; no heap found (return 0 length string)
	move.l	rcnt_1st(a3),a3
	move.w	(a3)+,d1		; length of string
gjdocop move.w	(a1),d3 		; length of buffer
	ble.s	gj_err			; 0 or negative length - what????????
	subq.w	#2,d3			; nbr of bytes needed after length word
	ble.s	gj_err			; not enough space!
	cmp.w	d1,d3			; is buffer big enough?
	blt.s	gj_err			; no it isn't
	move.l	2(a1),a1		; point to return space
	move.w	d1,(a1)+		; fill in length (may be 0)
	beq.s	gj_out			; 0 length string - finished
	subq.w	#1,d1
gjcpylp move.b	(a3)+,(a1)+
	dbf	d1,gjcpylp		; copy my string to return string
	clr.l	d0
gj_out	movem.l (sp)+,gffjreg		; done
	rts
gj_err	moveq	#err.bffl,d0		; signal buffer full
	movem.l (sp)+,gffjreg		;
	rts

; found no list for this job
gj_nf	moveq	#0,d1			; nothing found, 0 length string is returned
	bra.s	gjdocop



   
;+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
;
; GALJ extension : Get ALl files for Job: fill in buffer with all files for job:
;			     length of buffer, buffer, jobID, name pointer
;
;	If the job ID doesn't refer to a valid job (jobid = -2) then the
;	optional name is used to make a hash and look for the job with that
;	name.
;
;	Attention, the length of the buffer is a long word
;
;	a1 c  p parameter stack pointer
;	a2 c  p thing linkage block
;
;	this returns
;	0 if OK
;	err.bufl if buffer was too short, in this case as much as is possible...
;	...is filled in
;	err.ijob  no list could be found for a job with that ID/name
;
;+++
;
; parameter list:
; length of buffer	(LW) (!)
; pointer to buffer	(LW)
; job_id		(LW) should be -2 if a name is supplied
; name pointer		(LW) pointer to job name if jobID = -2, or 0
;
;++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
galjreg reg	d1/d3/d6/d7/a1/a3/a4
rcnt_galj thg_extn {GALJ},rcnt_garj,p_galj
	movem.l galjreg,-(sp)
	addq.l	#8,a1
	move.l	(a1),d1 		; job id
	moveq	#-1,d7
	bsr	findheap		; find heap for this job
	subq.l	#8,a1			; (reset param stack ptr)
galjtst beq	galjnjb 		; no heap found
	move.l	a3,d6			;
	lea	rcnt_hdr(a3),a4 	; start of space for list
	move.l	rcnt_1st(a3),a3 	; first element in list
	movem.l (a1),d3/a1		; buffer size  & buffer
	move.l	a3,d7			; keep pointer
	clr.l	d1
galjlp1 move.w	(a3)+,d1		; bytes to copy for this string
	beq.s	cpyend3 		; nothing
	move.l	d1,d0
	addq.l	#5,d0			; lenth word + 0 word at end +make even
	bclr	#0,d0
	sub.l	d0,d3			; is there still enough space in the buffer?
	blt.s	galjerr 		; no, error
	move.w	d1,(a1)+		; copy length
	lsr.w	#1,d0			; word sized copy
	subq.w	#3,d0			; - length word, 0 word at end	& dbf
galjlp2 move.w	(a3)+,(a1)+
	dbf	d0,galjlp2
		  
cpyend3 sub.l	#hd_entryl,d7		; point to previous entry
	move.l	d7,a3			; point to previous element in list
	cmp.l	a4,a3			; would we overshoot start of space for list?
	bge.s	galjlp1 		; no, copy again

; when we get here, we copied from the first element to the start of the space
; of the list - now we need to copy from the end of the space of the list to
; the first element
	move.l	d6,a3
	move.l	rcnt_1st(a3),a4 	; first element in list
	move.l	rcnt_hend(a3),a3	; end of space for list
	sub.l	#hd_entryl,a3		; point o start of this entry
	move.l	a3,d7			; keep pointer

galjlp3 move.w	(a3)+,d1		; bytes to copy
	beq.s	cpyend4 		;
	move.l	d1,d0
	addq.l	#5,d0			; length word + 0 word at end +make even
	bclr	#0,d0
	sub.l	d0,d3			; is there still space in the buffer?
	blt.s	galjerr 		; no, error
	move.w	d1,(a1)+		; copy length
	lsr.w	#1,d0
	subq.w	#3,d0			; - length word, 0 word at end	& dbf
galjlp4 move.w	(a3)+,(a1)+
	dbf	d0,galjlp4
		  
cpyend4 sub.l	#hd_entryl,d7
	move.l	d7,a3
	cmp.l	a3,a4			; first element reached (we've come around)?
	bne.s	galjlp3 		; no
	clr.l	d0			; yes, done, no error
galjout move.w	#0,(a1) 		; terminating word
	movem.l (sp)+,galjreg
	tst.l	d0
	rts

galjerr moveq	#err.bffl,d0
	bra.s	galjout
      
galjnjb moveq	#err.ijob,d0
	bra.s	galjout




;+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
;
; GARJ extension : Get all files into an ARray for a Job: fill in array with
;		 all files.
;		 parameters : array, nbr of elmts, length of element, jobID, name ptr
;
;	a1 c  p parameter stack pointer
;	a2 c  p thing linkage block
;
;	this returns :
;	0 if OK. If the nbr of elements in the array is smaller than the nbr
;		of elements in the thing, then only the first elements in
;		in the list are copied, to fill up the array. This DOES NOT
;		GIVE RISE TO AN ERROR!
;	err.ipar if the array had a null dimension or if the length of the elements
;		in the array is smaller than the max length of a string in the
;		list. NOTE: the max length of the strings in the list may vary
;		if a files with longer names are added, or if the longest file
;		falls out of the list.
;	err.ijob if no list is kept for the job passed as parameter
;
;   NOTE don't forget that SMSQ/E standard strungs have a 2 byte length word in
;	front, the length of the element must take that into account (i.e add
;	a 2 to the length of the array).
;+++
;
; parameter list:
;
; pointer to array, NOT relative to A6			(LW)
; number of elements in array				(WORD)
; length of elements in array, INCLUDING length word	(WORD)
; job ID  should be -2 if a name is supplied		(LW)
; name pointer pointer to job name if jobID = -2, or 0	(LW)					     (LW)
;
;++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
garjreg reg	d3-d6/a1/a3/a4
rcnt_garj thg_extn {GARJ},rcnt_save,p_gtal
	movem.l garjreg,-(sp)
	move.l	a2,d6
	addq.l	#8,a1			; point to job ID
	jsr	tinfo			; get info on this job heap
	subq.l	#8,a1
	move.l	d6,a2
	bne	garjexit		; no heap for jobs exist
	tst.w	d2
	beq	garfout 		; there is nothing to copy: no elements in list
	tst.l	d3			; pointer to heap
	beq	garnjob 		; none!
	move.w	4(a1),d0		; number of elements in array
	beq	garferr 		; none, this is an error
	cmp.w	d0,d2			; do we have space for all elements in list?
	ble.s	garfok1 		; yes->...
	move.w	d0,d2			; no, only this much
garfok1 swap	d2			; max length of element in list
	addq.w	#2,d2			; + length word
	move.w	6(a1),d0		; length of element in array
	cmp.w	d0,d2			; will biggest elements fit in array?
	bgt	garferr 		; no, so this is a real error
	swap	d2			; d2 = nbr of elements to copy

; d0 = length of element in array
; d2 = nbr of elements to copy
	move.l	d3,a3
	lea	rcnt_hdr(a3),a4 	; start of space for list
	move.l	rcnt_1st(a3),a3 	; first element in list
	move.l	(a1),a1 		; pointer to array
	move.l	a3,d6			; keep pointer
	clr.l	d1

garflp1 move.w	(a3)+,d1		; bytes to copy
	beq.s	garfcpy1		; none
	move.w	d1,(a1)+		; copy length
	move.w	d0,d4			; length of one element in array
	sub.w	d1,d4			; number of bytes to set to 0 at end
	subq.w	#1,d1
garflp2 move.b	(a3)+,(a1)+
	dbf	d1,garflp2
	subq.w	#3,d4			; minus the length word & dbf
	blt.s	garfcpy1
garf1	move.b	#0,(a1)+		; zero out space at end (for qliberator)
	dbf	d4,garf1
garfcpy1
	subq.w	#1,d2
	beq.s	garfout 		; done copying
	sub.l	#hd_entryl,d6		; point to previous entry
	move.l	d6,a3
	cmp.l	a4,a3			; would we overshoot start of space for list?
	bge.s	garflp1 		; no, copy again

; when we get here, we copied from the first element to the start of the space
; of the list - now we need to copy from the end of the space for the list to
; the first element
	move.l	d3,a3
	move.l	rcnt_1st(a3),a4 	; start of space for list
	move.l	rcnt_hend(a3),a3	; end of space for list
	sub.l	#hd_entryl,a3		; point to string
	move.l	a3,d6			; keep pointer

garflp3 move.w	(a3)+,d1		; bytes to copy
	beq.s	garfcpy2		 ;
	move.w	d1,(a1)+		; copy length
	move.w	d0,d4
	sub.w	d1,d4			; number of bytes to set to 0 at end
	subq.w	#1,d1
garflp4 move.b	(a3)+,(a1)+
	dbf	d1,garflp4
	subq.w	#3,d4			; minus the length word & dbf
	blt.s	garfcpy2
garf2	move.b	#0,(a1)+		; zero out space at end (qliberator)
	dbf	d4,garf2
garfcpy2
	subq.w	#1,d2
	beq.s	garfout
	sub.l	#hd_entryl,d6		; point to previous entry
	move.l	d6,a3
	cmp.l	a4,a3			; would we overshoot start of space for list?
	bne.s	garflp3 		; no, copy again
garfout clr.l	d0			; yes, done, no error
garjexit
	movem.l (sp)+,garjreg
	rts
garferr moveq	#err.ipar,d0
	movem.l (sp)+,garjreg
	rts
garnjob moveq	#err.ijob,d0
	movem.l (sp)+,garjreg
	rts


;+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
;
; SAVE extension : SAVE lists to configured file  : no parameters
;
;	a2 c  p thing linkage block
;
;	this returns
;	0 if OK
;	any error from file operations
;	err.fdnf if no file is configured
;
;      if there is nothing to save, no error is returned
;+++
;
; parameter list:
; none
;
;++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
savereg reg	d1/d3-d7/a0-a5
rcnt_save thg_extn {SAVE},rcnt_load,p_gtal
	movem.l savereg,-(a7)
	move.l	rcnt_heap(a2),a0
	move.l	a0,d0
	beq.s	rcsv_out		; there is nothing to save
	move.l	d0,d5			; point to first heap
	lea	rcfg_file,a0		; save file
	tst.w	(a0)			; any name there?
	beq.s	saveerr 		; no, can't find save file
	moveq	#ioa.open,d0
	moveq	#-1,d1
	moveq	#ioa.kovr,d3
	trap	#2			; open file
	tst.l	d0			;
	bne.s	rcsv_out		; oops on open

	lea	rcfg_nbr,a1
	move.b	(a1),d0 		; nbr of files in list
	addq.l	#2,d0			; +space for name
	moveq	#hd_entryl,d4
	mulu	d0,d4			; space for each heap
	add.l	#rcnt_hdr,d4		; with header
	lea	marker,a1		; now save marker
	moveq	#12,d2			; it is 12 bytes long
	moveq	#-1,d3
	moveq	#iob.smul,d0
	trap	#3
	tst.l	d0
	bne.s	rcsvcls 		; ooops
	lea	rcnt_temp(a2),a1
	move.l	d4,(a1)
	moveq	#4,d2
	moveq	#-1,d3			; save space needed for 1 heap space
	moveq	#iob.smul,d0
	trap	#3
	tst.l	d0
	bne.s	rcsvcls
	move.w	sr,d6
	trap	#0			; I'll be mucking about in the heaps
svloop	move.l	d5,a1			; point to heap to save
	move.l	rcnt_1st(a1),d1 	; point to 1st file
	move.l	d1,d7			; keep pointer to insert it back later
	sub.l	a1,d1			; relative to start of heap
	move.l	d1,rcnt_1st(a1) 	; store thos location as 1st file
	move.l	d4,d2			; how much to save
	move.l	a1,d5			; keep this pointer
	moveq	#iob.smul,d0
	trap	#3
	move.l	d5,a1			; get ptr back
	move.l	d7,rcnt_1st(a1) 	; and reset the pointer to the 1st file
	tst.l	d0			; save ok?
	bne.s	rcsvcls2
	move.l	(a1),d5 		; point to next heap
	bne.s	svloop			; and do again unless we're done
rcsvcls2
	move.w	d6,sr
rcsvcls move.l	d0,d4
	moveq	#ioa.clos,d0
	trap	#2
	move.l	d4,d0
rcsv_out movem.l (a7)+,savereg
	rts

; there was nothing to save
nosave	moveq	#0,d0
	bra.s	rcsv_out
; no file was configured
saveerr moveq	#err.fdnf,d0
	bra.s	rcsv_out


marker
	dc.b	'RCNTTHGMARK1'

;+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
;
; LOAD extension : LOAD lists from configured file  : no parameters
;
;	a2 c  p thing linkage block
;
;	this returns
;	0 if OK
;	any error from file operations
;	err.inam if the file was not a valid recent thing save file
;
;+++
;
; parameter list:
; none
;
;++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
rcnt_load thg_extn {LOAD},rcnt_remv,p_gtal
	movem.l savereg,-(a7)
	lea	rcfg_file,a0
	tst.w	(a0)			; any name there?
	beq.s	saveerr 		; no, can't find save file
	moveq	#ioa.open,d0
	moveq	#-1,d1
	moveq	#ioa.kshr,d3
	trap	#2
	tst.l	d0
	bne.s	rcsv_out
	move.l	a0,d5
	move.w	sr,d6
	trap	#0			; I have to avoid other jobs changing things
	lea	rcnt_ucfn(a2),a1	; since I use this buffer, go super
	moveq	#16,d2
	moveq	#-1,d3
	moveq	#iob.fmul,d0
	trap	#3
	tst.l	d0
	bne	rcsvcls2
	lea	rcnt_ucfn(a2),a1
	cmp.l	#'RCNT',(a1)+
	bne.s	rcsvcls2		; check the marker
	cmp.l	#'THGM',(a1)+
	bne.s	rcsvcls2
	cmp.l	#'ARK1',(a1)+
	bne	rcsvcls2
	move.l	(a1),d7 		; d7 = mem needed for one heap space in file
; now release all previous heaps (except for the general one)
	move.l	d2,-(a7)
	lea	rcnt_heap(a2),a0
	move.l	(a0),d0 		; point to first heap space
	beq.s	rel_end 		; there is nothing to release
	move.l	a0,a4			; where pointer to next heap goes
	move.l	d0,d4			; pointer to heap
	clr.l	(a0)			; set that there is no more heap space
	bra.s	ld_lab1
ld_lp1	move.l	(a0),d4 		; keep ptr to next space
	moveq	#sms.rchp,d0
	trap	#1			; release this heap area
	tst.l	d0
	bne	ldsuerr 		; error
	tst.l	d4			; any further heap?
	beq.s	rel_end 		; no, done
ld_lab1 move.l	d4,a0			; point to it
	bra.s	ld_lp1			; and release it
rel_end move.l	(a7)+,d2
	tst.l	d0			; any error when releasing?
	bne	rcsvcls2		; yep, done
	move.l	d5,a0
	lea	rcfg_nbr,a1
	move.b	(a1),d0 		; nbr of files in list
	addq.l	#2,d0			; +space for name
	moveq	#hd_entryl,d4
	mulu	d0,d4			; space for each heap
	add.l	#rcnt_hdr,d4		; with header
	lea	rcnt_heap(a2),a5	; point to "previous" heap

; here we have
; a0 = channel ID
; a2 = ptr to thing
; a4 = next heap pointer in "previous heap"
; D4 = space needed for my list
; d5 = channel ID
; D6 = sr
; d7 = space needed for list in saved file


; D4 = space needed for my list, D7 = space needed for list in saved file
; if both are the same : great
; if saved file needs more space than my list : cut saved file
; if my list needs more space than saved file, expand it
	cmp.l	d7,d4
	bne.s	which

ld_lp2	moveq	#0,d0
	trap	#3			; is file EOF?
	tst.l	d0
	bne.s	loadend 		; yes, close file, we're done
	clr.l	d2
	clr.l	d3
	move.l	d7,d1			; nbr of bytes to read / to reserve
	move.l	a0,d5			; keep channel ID
	moveq	#sms.achp,d0
	trap	#1			; get space
	tst.l	d0
	bne	rcsvcls2		; oops
	move.l	a0,a1			; where to load to
	move.l	d7,d2			; nbr of bytes to get
	moveq	#-1,d3
	exg	d5,a0			; d5 = space, a0=channel ID
	moveq	#iob.fmul,d0
	trap	#3
	tst.l	d0
	bne.s	ldsuerr
	move.l	d5,a1

	tst.l	rcnt_jbID(a1)
	beq.s	noid3
	move.l	#-1,rcnt_jbID(a1)	; don't set ID to unknown if job 0

noid3	move.l	rcnt_1st(a1),d1 	; point to 1st file, relative to start of heap
	add.l	a1,d1			; absolute
	move.l	d1,rcnt_1st(a1) 	; store this location as 1st file
	move.l	a1,d1
	add.l	d7,d1			; point to end of heap space
	sub.l	#hd_entryl*2,d1 	; point to end of list
	move.l	d1,rcnt_hend(a1)	; this is it
	move.l	a1,(a4) 		; previous heap points to this one
	move.l	a1,a4			; this is now the previous heap
	bra.s	ld_lp2

loadend moveq	#0,d0
	bra	rcsvcls2		; leave w/o error

ldsuerr move.w	d6,sr			; this sets the status reg
	tst.l	d0			; so we have to test d0 again
	bra.s	loadout
ldsuerr2
	move.w	d6,sr
	moveq	#err.inam,d0
loadout movem.l (a7)+,savereg
	rts

; D4 = space needed for my list, D7 = space needed for list in saved file
; here the load size and the configured size are different
which	blt.s	more_spc		; if current list size < saved list size

	sub.l	d7,d4			; this is how much more bytes we have in the current list
ld_lp3	moveq	#0,d0
	trap	#3			; is file EOF?
	tst.l	d0
	bne.s	loadend 		; yes, close file, we're done
	clr.l	d2
	clr.l	d3
	move.l	d7,d1			; nbr of bytes to read / to reserve
	add.l	d4,d1			; our heap is larger than the stored one
	move.l	a0,d5			; keep channel ID
	moveq	#sms.achp,d0
	trap	#1			; get space
	tst.l	d0
	bne	rcsvcls2		; oops
	move.l	a0,a1			; where to load to
	move.l	d7,d2			; nbr of bytes to get
	moveq	#-1,d3
	exg	d5,a0			; d5 = space, a0=channel ID
	moveq	#iob.fmul,d0
	trap	#3
	tst.l	d0
	bne.s	ldsuerr
	move.l	d5,a1

	tst.l	rcnt_jbID(a1)
	beq.s	noid2
	move.l	#-1,rcnt_jbID(a1)	; don't set ID to unknown if job 0
noid2
	move.l	rcnt_1st(a1),d1 	; point to 1st file, relative to start of heap
	add.l	a1,d1			; absolute
	move.l	d1,rcnt_1st(a1) 	; store this location as 1st file
	move.l	a1,d1
	add.l	d7,d1			; point to end of LOADED heap space
	move.l	d1,d2			; keep this pointer
	add.l	d4,d1			; point to end of THIS heap space
	sub.l	#hd_entryl*2,d1 	; point to end of list, start of job name
	move.l	d1,rcnt_hend(a1)	; this is it
	move.l	a1,(a4) 		; previous heap points to this one
	move.l	a1,a4			; this is now the previous heap
	addq.l	#2,d1
	move.l	d1,a1			; point to job name

	sub.l	#hd_entryl*2-2,d2
	move.l	d2,a3
	moveq	#hd_entryl/4,d0 	; *** should be /2
	bra.s	dobglp
bglp1	move.l	(a3)+,(a1)+
dobglp	dbf	d0,bglp1
	bra.s	ld_lp3
	


more_spc
; here the current list size is smaller than that of the the saved lists.
	sub.l	d4,d7			; amount we jump over in the file
ld_lp4	moveq	#0,d0
	trap	#3			; is file EOF?
	tst.l	d0
	bne	loadend 		; yes, close file, we're done
	clr.l	d2
	clr.l	d3
	move.l	d4,d1			; nbr of bytes to read / to reserve
	move.l	a0,d5			; keep channel ID
	moveq	#sms.achp,d0
	trap	#1			; get space
	tst.l	d0
	bne	rcsvcls2		; oops
	move.l	a0,a1			; where to load to
	move.l	d4,d2			; nbr of bytes to get
	sub.l	#hd_entryl*2,d2 	; minus the job name
	moveq	#-1,d3
	exg	d5,a0			; d5 = space, a0=channel ID
	moveq	#iob.fmul,d0
	trap	#3			; get bytes
	tst.l	d0
	bne	ldsuerr
	move.l	d7,d1			; jump this much forward in the file
	moveq	#iof.posr,d0
	trap	#3			; the file now points to the job name
	tst.l	d0
	bne	ldsuerr

	move.l	d5,a1

	tst.l	rcnt_jbID(a1)
	beq.s	noid1
	move.l	#-1,rcnt_jbID(a1)	; don't set ID to unknown if job 0
noid1	
	move.l	a1,d2
	add.l	d4,d2			; point to end of RESERVED heap space
	sub.l	#hd_entryl*2,d2 	; now point to end of space for file list
	move.l	d2,rcnt_hend(a1)	; show it
	move.l	d2,d0
	sub.l	#hd_entryl,d2		; point to last possible name
	move.l	rcnt_1st(a1),d1 	; point to 1st file, relative to start of heap
	add.l	a1,d1			; absolute

	cmp.l	d1,d2
	bge.s	set_1st
	move.l	d2,d1
set_1st move.l	d1,rcnt_1st(a1) 	; store this location as 1st file

	move.l	a1,(a4) 		; previous heap points to this one
	move.l	a1,a4			; this is now the previous heap
	move.l	d0,a1			; point to jobname space
	move.l	#hd_entryl*2,d2 	; space reserved for name
	moveq	#iob.fmul,d0
	trap	#3			; get bytes
	tst.l	d0
	bne	ldsuerr
	bra    ld_lp4


;+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
;
; REMV extension : REMoVe a list for a job
;
;	If the job ID doesn't refer to a valid job (jobid = -2) then the
;	optional name is used to make a hash and look for the list of the job
;	with that name hash.
;
;	a1 c  p parameter stack pointer
;	a2 c  p pointer to thing
;
;	error returns :
;	D0 = 0 - OK
;	   = err.ijob there is no list for this job
;	   any error from sms.rchp
;++++
;
; parameter list:
; job ID	(LW) use -2 if you want to check with the name, -3 for general list
; name pointer	(LW) pointer to job name or 0 if none
;
;+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
remvreg reg	d1-d3/d7/a1-a3
rcnt_remv thg_extn {REMV},rc_sync,p_gtal
				  
	movem.l remvreg,-(a7)
	moveq	#-1,d7
	jsr	findheap		; find heap for this job
	beq.s	remverr 		; there is none
	move.l	a3,d1			; point to this heap
gotid	lea	rcnt_heap(a2),a3	; point to first job heap...
rm_lp1	move.l	(a3),d0 		; ... now
	cmp.l	d0,d1			; is it this heap?
	beq.s	fndheap 		; yes, now unlink & release it
	move.l	d0,a3
	bne.s	rm_lp1			; and go around again

; if we get here, nothing found in the job IDs or the hashes.
remverr moveq	#err.ijob,d0
	movem.l (a7)+,remvreg
	rts

; found the heap corresponding to the job
fndheap move.l	d1,a0			; point to my heap
	move.l	rcnt_next(a0),d0	; point to next heap
	move.l	d0,rcnt_next(a3)	; cut this heap out of the heap list
	moveq	#sms.rchp,d0
	trap	#1
	movem.l (a7)+,remvreg
	rts

	end
