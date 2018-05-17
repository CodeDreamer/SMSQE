; Find / possibly create the common heap space to hold llst for a job
; V1.00 (c) W. Lenerz 2015


	section jobs

	include dev8_keys_recent_thing
	include dev8_keys_sys
	include dev8_keys_qlv
	include dev8_keys_qdos_sms

	xdef	findheap
	xdef	findhsh

	xref	hash
	xref	hash2
	xref	rcfg_nbr


; d0	s THIS IS NOT AN ERROR CODE  !!!!!!!!
; d1 c	s job id
; d2	s
; d3	s
; d7 c	s if =0 create heap for this job (if it doesn't exist), else don't
; a1 c p  parameter stack, MUST point to job ID
; a2 c p  thing linkage block
; a3  r   pointer to heap (old one if found, else newly created one) or 0 if error
;
; on return, the Z flag is set if NO heap could be found/created for this job
;	     ============================

;----------------------------------------------------------------------
; the list maintained for eash job is a circular lifo buffer
; for each job a memory soace is allocated in the common heap. These are in a
; simple linked list
;-----------------------------------------------------------------------


fjreg	reg	d2-d4/a0/a1

; there is no heap yet
noheap	tst.l	d1		       ; any jobID other than job 0?
	bne.s	notfnd1 	       ; yes
	bra	idnoname	       ; if this is job 0, try to make heap now


; find with hash only, in this case:
; d1 c	s hash
; (rest remains the same)
findhsh movem.l fjreg,-(a7)
	moveq	#-1,d4
	sub.l	a0,a0			; may be pointer to job name
	bra.s	srchhsh

findheap
	movem.l fjreg,-(a7)
	sub.l	a0,a0			; may be pointer to job name
	move.l	(a1),d1
	move.l	d1,d4			; find job first with jobID
	cmp.l	#-2,d1			; is it special job ID, i.e. name pointer?
	bne.s	nospec
	move.l	4(a1),d0		; any name?
	beq	hp_err			; no, so I can't find anything
					; and I can't create a heap either
	move.l	d0,a3			; point to name
	bsr	hash			; make hash of name
	moveq	#-1,d4			; signal no valid job ID
	bra.s	srchhsh 		; now try to find the heap with the name

nospec	cmp.l	#-1,d1			; is it for myself?
	bne.s	valID			; no->...
	moveq	#sms.info,d0
	trap	#1			; get jobID into d1
	tst.l	d0
	bne	hp_err			; ooops
	move.l	d1,d4			; keep job ID
valID	move.l	rcnt_heap(a2),d0	; poiner to next heap space
	beq.s	noheap			; there is none, perhaps try to make hash
sloop1	move.l	d0,a3			; point to space
	cmp.l	rcnt_jbID(a3),d1	; same job ID?
	beq.s	foundID 		; yes
	move.l	rcnt_next(a3),d0
	bne.s	sloop1
; if we get here, nothing found in the job IDs.
; Now make hash and check that
notfnd1
	bsr.s	mkhash			; try to make a valid hash (into d1)
	beq.s	IDnoname		; couldn't (job has no name?)

; here we search with the hash
srchhsh move.l	rcnt_heap(a2),d0	; poiner to first heap space
	beq.s	notfound		; none, we have a hash though
sloop2	move.l	d0,a3			; point to this heap
	cmp.l	rcnt_hsh(a3),d1 	; same hash?
	beq.s	fndhash 		; yes
	move.l	rcnt_next(a3),d0	; no, try next
	bne.s	sloop2
	bra.s	notfound		; if we get here, no matching heap found
fndhash cmp.l	#-1,d4			; valid job ID?
	beq.s	fhout			; no->...
	move.l	d4,rcnt_jbID(a3)	; fill in job ID
fhout	move.l	a3,d0			; set NE if heap found
	movem.l (a7)+,fjreg
	rts				; done, A3 points to heap

; if we get here, found the heap for this job ID
foundID tst.l	rcnt_hsh(a3)		; does this job also have a hash?
	bne.s	fhout			; yes, done, A3 points to heap
	move.l	a3,d4
	bsr.s	mkhash			; no hash yet, make one
	move.l	d4,a3
	beq.s	fhout			; couldn't get a hash
sethsh	move.l	d1,rcnt_hsh(a3)
	move.l	a3,d0			; set NE if heap found
	movem.l (a7)+,fjreg
	rts				; done, A3 points to heap
; make a simple hash from the job name
; d1 cr    jobID / hash
; d2	s
; d3	s
; a1	s
; a3	s
; on return : condition code Z set : problem in making hash !
mkhash	tst.l	d1
	beq.s	noname			; job 0 has hash 0
	moveq	#sms.injb,d0		; get Job information
	clr.l	d2			; scan whole tree
	trap	#1			; find this job
	move.l	#rc_inihash,d1		; preset no hash could be made
	sub.l	a3,a3			; preset job could not be found
	tst.l	d0			; info on job OK?
	bne.s	noname			; no, ooops
	lea	6(a0),a3		; move to name flag
	cmp.w	#$4afb,(a3)+		; check flag
	bne.s	noname			; no name for job
	bsr	hash2			; make hash from name & return
noname	cmp.l	#rc_inihash,d1		; did hash fail?
	rts

; if we get here, the job is not in the heaps and no valid name hash
; could be made

idnoname
	clr.l	d1			; show no hash
; if we get here, the job is not in the heaps but valid name hash
; could be made
; a0 c	 pointer to job name
; d1 c	 hash
; d4 c	 jobID
notfound
	tst.l	d7			; do we create a heap?
	bne	hp_err			; no, done
	move.l	a0,d7			; keep pointer to name
	movem.l d1/a2-a3,-(a7)
	move.l	#hd_entryl,d1		; length of 1 entry in list (filename + string length +job iD + even up)
	lea	rcfg_nbr,a2
	clr.l	d0
	move.b	(a2),d0 		; nbr of entries in list wished
	addq.l	#2,d0			; + space for job name
	mulu	d0,d1			; * length of one entry
	add.l	#rcnt_hdr,d1		; + space for my header
	move.l	d1,-(a7)
	moveq	#sms.achp,d0		; get mem...
	clr.l	d2			; ...for job 0...
	trap	#1			; ...now
	move.l	(a7)+,d1		; amount of mem
	movem.l (a7)+,d2/a2-a3		; !!! hash not back into D1, but d2
	tst.l	d0
	bne.s	hp_err			; error
	move.l	a0,a3			; pointer to heap
	move.l	d2,rcnt_hsh(a3) 	; hash
	move.l	d4,rcnt_jbID(a3)	; set job ID
	lea	rcnt_hdr(a3),a0
	move.l	a0,rcnt_1st(a3) 	; where first entry in list goes
	sub.l	#rcnt_hdr+hd_entryl*2,d1;
	add.l	d1,a0
	move.l	a0,rcnt_hend(a3)	; end of space for list
	tst.l	d7			; is there a name?
	beq.s	addl			; no
	addq.l	#6,d7
	move.l	d7,a1			; ptr to name
	addq.l	#2,a0
	cmp.w	#$4afb,(a1)+		; check flag
	bne.s	addl			; no name for job
	move.w	(a1)+,d0
	beq.s	addl
	moveq	#hd_entryl*2-4,d1	; job name may be this long
	sub.w	d0,d1
	ble.s	addl			; but it's longer, leave
	move.w	d0,(a0)+
cplom	move.b	(a1)+,(a0)+
	dbf	d0,cplom
; now add this heap to the end of our list
addl	lea	rcnt_heap(a2),a0
loop	move.l	(a0),d0
	beq.s	lstend
	move.l	d0,a0
	bra.s	loop
lstend	move.l	a3,(a0)
chkerr	move.l	a3,d0
	movem.l (a7)+,fjreg
	rts
hp_err	sub.l	a3,a3
	bra.s	chkerr
	end
