; Default list handling for the home thing  (c) Marcel Kilgus,	W. Lenerz

; 2005-10-23  1.00  initial version
; 2005-11-14  1.01  fixed return of fndjbname if there's no defaults list (mk)
; 2005-11-20  1.02  fndjbname keeps D1 (wl)
;
;
;------------------------------------------------
; This defaults list is organized as a linked list, as follows :
; Each element in the list is 1000 bytes long and may have several job names
; entries.
; If the list element does not have further space for a new job name entry,
; then a new list element is created & linked into the list.
; The job name entries in a list element have the following format:
;
; word	: total length of job entry incl. the length words etc, made even **
; word	: length word of job name
; bytes : job name (even padding byte at end if necessary
; word	: length word of filename
; bytes : filename
;
** if the length word is 0, then end of list was reached and the next word
** is 0, too
** if the length word is negative, it is followed by the address for the
** next part of the list
;--------------------------------------------------------------


	section util

	include dev8_smsq_home_data
	include dev8_keys_err
	include dev8_keys_chp
	include dev8_keys_qdos_sms
	include dev8_keys_jcb

	xdef	setdef			; set default list entry for a job name
	xdef	fndjbname		; find job is defaults list with its job ID
	xdef	fndlist 		; find job name in defaults list

	xref	checkid
			  

;---------------------------------------------------------------------
; set default homedir
; regs:
; D0  r    error return
; D1   s
; A1 cr    pointer parameter (job name, file name)
; A2 cr    my linkage block
;
; error returns:
; 0		OK
; err.fex	job with this name already has a default set
;---------------------------------------------------------------------

setreg	reg	d2/a3/a4
setnf	movem.l (sp)+,setreg
seterr	moveq	#err.fex,d0		; this already exists
	rts

setdef	movem.l setreg,-(sp)
	lea	hmt_defs(a2),a0 	; pointer to defaults list
	move.l	(a0),d0 		; point to defaults list
	bne.s	defext			; it already exists, fine
	bsr	getdefl 		; it doesn't, so make one
	bra.s	defext2
defext
	move.l	d0,a0			; point to defaults list now
defext2
; first check that no default for this job already exists
	bsr	fndlist 		; check for existing default
	beq.s	setnf			; there is one already
; now calculate total length of space need for this job entry
	clr.l	d1			; none yet
	clr.l	d0			; clear "error"
	clr.l	d2
	move.l	hdf_fnm(a1),a3		; point to file name
	move.w	(a3),d1 		; length of name
	beq.s	setnf			; what?
	addq.w	#5,d1
	bclr	#0,d1			; make even by overshoot + length word x2
	move.l	hdf_jnm(a1),a3		; pointer to jobname
	move.w	(a3),d2 		; job name length
	beq.s	setnf			; what?
	add.w	d2,d1			; job name plus filename length
	addq.w	#3,d1			; total length needed for this job entry
	bclr	#0,d1			; even
; find end of list, add it there
	clr.l	d2			; cumulative length of entries in this list element
nxt_ntry
	add.l	d0,a0			; point to next job entry
	add.l	d0,d2			; cumulative legnth till now
gotoend move.w	(a0),d0 		; length of next job entry
	beq.s	fndend			; reached the end
	bgt.s	nxt_ntry		; not end yet
	move.l	4(a0),a0		; pointer to next elemnt in list
	clr.l	d2			; nothing used yet in this new list element
	bra.s	gotoend
; a0 now points to end of my list
fndend	move.l	#deflen-8,d0		; length of one element in my list minus 8 bytes for link to next part and 2 length words
	sub.w	d2,d0			; what we have used up till now
	sub.w	d1,d0			; does new job entry fit?
	bge.s	set_it			; yes, so now we set it in list
	move.l	#-1,(a0)+		; signal end of this heap
	bsr.s	getdefl 		; make new list element
set_it	move.w	d1,(a0)+		; total length
	move.w	(a3)+,d1		; job name length
	beq	setnf			; there is none!
	move.w	d1,(a0)+
	move.w	d1,d0
	subq.w	#1,d1
cpylp1	move.b	(a3)+,d2
	andi.b	#$df,d2 		; case
	move.b	d2,(a0)+
	dbf	d1,cpylp1		; copy name, adjusting case
	btst	#0,d0			; odd name length?
	beq.s	evened			; yes
	addq.l	#1,a0			; no, even out
evened	move.l	hdf_fnm(a1),a3		; point to filename
	move.w	(a3)+,d1		; length
	move.w	d1,(a0)+
	subq.w	#1,d1
cpylp2	move.b	(a3)+,(a0)+
	dbf	d1,cpylp2
	clr.l	d0
	movem.l (sp)+,setreg
	rts


; make a new list element
; a0 cr  pointer to (end of) old list / pointer to new list
hpareg	reg	d1-d3/a1-a4
staka0	equ	12

getdefl
	movem.l hpareg,-(sp)
	move.l	a0,a4			; keep pointer to old space
	move.l	#deflen,d1		; bytes to allocate
	clr.l	d2			;
	moveq	#sms.achp,d0		; allocate for job 0
	trap	#1
	tst.l	d0			; OK?
	bne.s	gdout			; ooops
	move.l	a0,(a4) 		; set pointer to new list
	moveq	#0,d0
gdout	movem.l (sp)+,hpareg
	rts

;---------------------------------------------------------------------
; find job in my default list, given its name
;
;
; A0 cr 	pointer to list / pointer to name entry
; A1 c	p	pointer to name parameter (Lword followed by ptr to name)
; a2 c	p	thing linkage block
;
; error returns in D0
; 0		found in list, A0 points to name entry
; err.itnf	not found in list
;
;---------------------------------------------------------------------

fndreg	reg	d1-d3/a2/a3
fndlist movem.l fndreg,-(sp)
	move.l	4(a1),a3
	move.w	(a3)+,d1		; length of parameter job name
	beq.s	no_name 		; there is none!
fndloop
	move.l	(a0),d0 		; .W = length of this job name
	beq.s	no_name 		; end of list, not found
	bgt.s	comp_nm 		; this is a valid name
	move.l	4(a0),a0		; point to next part of list
	bra.s	fndloop
comp_nm cmp.w	d0,d1			; same?
	beq.s	tryname 		; yes, this might be it
notfnd	swap	d0			; not found, jump over this job name
	add.w	d0,a0
	bra.s	fndloop
tryname move.w	d1,d2
	move.l	4(a1),a3		; point to parameter job name
	addq.l	#2,a3
	lea	4(a0),a2		; point to name in default list
	subq.w	#1,d2
tryloop
	move.b	(a3)+,d3		; part of name
	andi.b	#$df,d3 		; case
	cmp.b	(a2)+,d3		; same?
	bne.s	notfnd			; no
	dbf	d2,tryloop		;
; if we get here, this is it
	moveq	#0,d0			; no error
	movem.l (sp)+,fndreg
	rts
no_name movem.l (sp)+,fndreg
nt_fnd	moveq	#err.itnf,d0
	rts
;---------------------------------------------------------------------
; find job in my default list, given its job ID
; a0   r	pointer to name entry if found
; A1  c  p	pointer to parameter
; a2  c  p	pointer to thing header
; d1  c  p	job ID
;
; error returns in D0
; 0		found in list, A0 points to name entry
; err.itnf	not found in list
;---------------------------------------------------------------------

fndjbname
	movem.l d1/a1,-(sp)
	moveq	#-2,d1			; special flag, use current job ID
	bsr	checkid 		; try to see if job exists, get header into A0
	bne.s	nt_fnd2 		; a job with that ID doesn't exist
	moveq	#err.itnf,d0		; preset error not found
	lea	jcb_end+8(a0),a1	; point to presumed name of job
	cmp.w	#$4afb,-2(a1)		; correct job flag? (i.e. is there a name?)
	bne.s	nt_fnd2 		; no, not found, then
	move.l	hmt_defs(a2),d1 	; point to defaults list
	beq.s	nt_fnd2 		; there is none
	move.l	d1,a0
	subq.l	#8,sp			; make room
	move.l	a1,4(sp)		; pointer to name
	move.l	sp,a1
	bsr.s	fndlist 		; find name
	addq.l	#8,sp
nt_fnd2 movem.l (sp)+,d1/a1
	tst.l	d0			; return with error from find name
	rts

	end
