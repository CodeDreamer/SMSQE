; HOME thing extensions  V1.02 (c) W. Lenerz, 2005

; This is the actual extension thing

; 2005-10-23	1.00	initial release version
; 2005-11-01	1.01	initialisation routine split off, OVER extn added
; 2005-11-11	1.02	returns buffer size in d2 if err.orng


	section exten

	xdef	home_thing		; name
	xdef	getcur

	xref	gu_thjmp		; set up thing
	xref	checkid 		; get job ID/check that job exists
	xref	gu_achpp		; get mem
	xref.l	hom_vers		; my version
	xref	hom_procs		; basic procedures
	xref	ut_procdef		; and routine to link'em in
	xref	h_fndjb
	xref	h_fr_mem
	xref	h_setmem
	xref	setdef			; set default name in default list
	xref	h_unlnk 		; and unlink one
	xref	fndjbname		; find job name in default list
	xref	setcur			; set current directory

	include dev8_keys_thg
	include dev8_keys_iod
	include dev8_keys_err
	include dev8_keys_k
	include dev8_keys_qdos_sms
	include dev8_smsq_home_data
	include dev8_mac_thg
	include dev8_mac_assert
	include dev8_keys_chp

; --------------------------------------------------------------------
; the expected parameters for the thing calls
;
p_seth	dc.w	thp.ulng,thp.str+thp.call,0		; parameters for SETH extension
p_geth	dc.w	thp.ulng,thp.ret+thp.str,0		; parameters hor getting names
p_setd	dc.w	thp.str+thp.call,thp.call+thp.str,0	; params to set defaults
;	id, word with max length of string, pointer to string
;---------------------------------------------------------------------


;---------------------------------------------------------------------
;
; This is the HOME Thing with its extensions
;
;---------------------------------------------------------------------

home_thing

;+++
;
; SETH extension : set the dir: parameters : job id + string, both compulsory
;		   the job ID may be passed as -1
;
;	d0  r	0 or error
;	a1 c  p parameter stack pointer
;	a2 c  p thing linkage block
;
;+++
; job_id (LW), $C1000000, ptr to string (LW)
;+++

hom_set thg_extn {SETH},hom_get,p_seth

hms.reg reg	d1/d2/d3/d7/a1/a3/a4
hms.d2	equ	4
sethome movem.l hms.reg,-(sp)
	bsr.s	fndjob			; find job in list (it SHOULDN'T be there)
	beq.s	hs_err			; already there, you can't set home dir twice!
	bsr	h_setmem		; add to my list & set dirs
hs_out	movem.l (sp)+,hms.reg
	tst.l	d0
	rts

hs_err	moveq	#err.fdiu,d0		; job home dir is already in use
	bra.s	hs_out

; This little subroutine gets the syswars into A4, checks the job ID passed
; as parameter (if it doesn't exists, this exits directly) and jumps to the routine
; that checks whether the job is in my list & returns through this to caller.
; Remember, the routines will interpret the result differently (SET: error
; if job is already in my list, GET error if job is NOT in list)

fndjob
	move.l	set_id(a1),d1		; get the job ID (or -1)
	bsr	checkid
	bne.s	fnd_err 		; ooops, doesn't exist
	bra	h_fndjb 		; try to find job in list & return to caller
fnd_err
	addq.l	#4,a7			; if error (job doesn't exist) jump over return address
	bra.s	hs_out			; restore regs & leave - allows to return a different error


;+++
;
; GETH extension : get the dir: parameters : job id + ret string, both compulsory
;
;	d2    r needed buffer size if err.orng
;	a1 c  p parameter stack pointer
;	a2 c  p thing linkage block
;
;	this returns
;	0 if OK
;	ITNF	if wrong job ID
;	ORNG	if space for return string is too short
;+++
; parameter list:
; job_id (LWord),
; #$A100 (word),
; Word with max length of buffer,
; ptr to buffer (LWord)
;+++

hom_get thg_extn {GETH},fil_get,p_geth
	movem.l hms.reg,-(sp)
	moveq	#2,d7			; flag: get 2nd name
get_common
	bsr.s	fndjob			; set up params, find job in list
	beq.s	jbfnd			; ok, got it
	bsr	fndjbname		; try to find name in default list
	bne.s	hs_out			; not found there, either, so there
; Here the job name was found in the default list only. Now set up a job entry
; for this job as if it was called from SETH (!) - changing data for this job
; must not impact the defaults that were set.
	movem.l a1/a2,-(sp)
	sub.l	#12,sp
	move.l	sp,a1			; simulate parameters for seth call
	move.l	d1,(a1) 		; job ID
	move.w	2(a0),d0		; job name length
	addq.l	#5,d0			; + 2x length word + make even
	bclr	#0,d0
	add.w	d0,a0			; point to filename
	move.l	a0,8(a1)
	bsr	sethome 		; make an entry for this job
	move.l	(a1),d1
	add.l	#12,sp
	movem.l (sp)+,a1/a2
	tst.l	d0			; huh?
	bne	hs_out
	bsr.s	fndjob			; and find it normally
	bne.s	hs_out			; what????

; a0 now points to list entry for this job
; d7 = index into string to be returned ( home dir name, home file name, current dir)
jbfnd
	lea	hl_data(a0),a2		; point to home name
	bra.s	test
get_lp	move.w	(a2)+,d0		; length of name
	addq.w	#1,d0
	bclr	#0,d0			; make even
	add.l	d0,a2			; a2 now points to a string
test	subq.w	#1,d7			; do we get this string?
	bne.s	get_lp			; no, the next one

	moveq	#0,d1
	move.w	(a2)+,d1		; length of my string
	bge.s	got_strg		; ok, got one
; here the string length is negative, this can only happen if the current
; dir was changed, in this case the next long word is the pointer to the mem
	move.l	(a2),a2 		; point to new mem
	move.w	(a2)+,d1		; and get length
got_strg
	move.w	get_len(a1),d0		; max length of return buffer
	subq.w	#2,d0			; 2 bytes for counter is minimum
	bcs.s	orng_strg		; not even that? -> out of range
	move.l	get_nam(a1),a0		; return string
	cmp.w	d0,d1			; is length ok ?
	ble.s	do_copy 		;   ...yes
orng_strg
	addq.l	#2,d1			; +2 bytes for counter
	move.l	d1,hms.d2(sp)		; return buffer length in D2
	moveq	#err.orng,d0		; and signal: string was too long
	bra	hs_out			; don't copy anything

do_copy moveq	#0,d0			; show that all was ok
	move.w	d1,(a0)+		; fill in length
	beq	hs_out			; 0 length string - finished
	subq.w	#1,d1
cpy_lp	move.b	(a2)+,(a0)+
	dbf	d1,cpy_lp		; copy my string to return string
	bra	hs_out			; reset regs & return


;+++
;
; GETF extension : get the FILENAME: parameters : job id + ret string, both compulsory
;
;	d2    r needed buffer size if err.orng
;	a1 c  p parameter stack pointer
;	a2 c  p thing linkage block
;
;+++
; job_id (LW), $A100, Word with max length, ptr to string (LW)
;+++

fil_get thg_extn {GETF},cur_get,p_geth
	movem.l hms.reg,-(sp)
	moveq	#1,d7			; flag: get 1rst name
	bra    get_common


;+++
;
; GETC extension : get the current dir : job id + ret string, both compulsory
;
;	d2    r needed buffer size if err.orng
;	a1 c  p parameter stack pointer
;	a2 c  p thing linkage block
;
;+++
; job_id (LW), $A100, Word with max length, ptr to string (LW)
;+++

cur_get thg_extn {GETC},cur_set,p_geth
getcur
	movem.l hms.reg,-(sp)
	moveq	#3,d7			; flag: get 3rd name
	bra	get_common


;+++
;
; SETC extension : Set the current dir : job id + string, both compulsory
;
;	a1 c  p parameter stack pointer
;	a2 c  p thing linkage block
;
;+++
; job_id (LW), $C1000000, ptr to string (LW)
;+++
      
cur_set thg_extn {SETC},def_set,p_seth
	bsr	setcur
	rts


;+++
;
; SETD extension : Set a job default : two strings
;
;	a1 c  p parameter stack pointer
;	a2 c  p thing linkage block
;
;+++
; job_id (LW), $A100, Word with max length, ptr to string (LW)
;+++

def_set thg_extn {SETD},over_set,p_setd
	bsr	setdef
def_out rts


;+++
;
; OVER extension : reset the dir: parameters : job id + string, both compulsory
;		   the job ID may be passed as -1
;
;	d0  r	0 or error
;	a1 c  p parameter stack pointer
;	a2 c  p thing linkage block
;
;+++
; job_id (LW), $C1000000, ptr to string (LW)
;+++

over_set thg_extn {OVER},,p_seth

	bsr	sethome 		; try a normal set first
	beq.s	def_out 		; done
	cmp.l	#err.fdiu,d0		; was thing "in use" by this job?
	bne.s	def_out 		; no, so other error, fail!
	bsr.s	rel_job 		; release this job entry in my linked list
	bne.s	def_out 		; ooops
	bra	sethome 		; and set home dirs

rel_job
	movem.l hms.reg,-(sp)
	bsr	fndjob			; find this job
	bne.s	rel_out 		; oops not found (what????)
	bsr	h_unlnk 		; unlink from list
	moveq	#sms.rchp,d0
	trap	#1			; release mem
rel_out
	movem.l (sp)+,hms.reg
	tst.l	d0
	rts

	end
