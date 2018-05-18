; HOME thing  mem allocation/deallocation  V1.02 (c) W. Lenerz, M. Kilgus 2005

; This is the code that does the actual mem allocation for a job.
; It reserves enough mem & then copies the file & dirnames into it

; 2005-10-23  1.00  initial release version
; 2005-11-09  1.01  error returned if empty filename passed
; 2006-01-17  1.02  now inherits current directory of calling job (mk)
; 2017-02-22  1.03  set job ID on opening dirs


	section util

	include dev8_keys_chn
	include dev8_keys_chp
	include dev8_keys_iod
	include dev8_keys_jcb
	include dev8_keys_err
	include dev8_keys_qlv
	include dev8_keys_sys
	include dev8_keys_thg
	include dev8_keys_qdos_sms
	include dev8_keys_qdos_ioa
	include dev8_keys_qdos_io
	include DEV8_keys_hdr
	include dev8_smsq_home_data
		     
	xdef	h_setmem
	xdef	h_fr_mem

	xref	getcur
	xref	h_link
	xref	h_unlnk

*--------------------------------------------------------------------
* set memory - make an entry for a new job
*
*	Registers:
*		Entry				Exit
*	D0					0 or errors
*	D1	ID of Job to set homedir for	same
*	D2/D3					smashed
*	A0					pointer to entry for this job
*	A1	pointer to parameters		smashed
*	A2	pointer to th link. header	smashed
*	A3					smashed
*	A4					smashed
*
*  this code is called in USER mode
*
*	error returns
*	IJOB - job in D1 doesn't exist
*	IPAR - no filename passed (empty string!)
*	any error from mem alloc routine
*	any error from open file
*--------------------------------------------------------------------
hpareg	reg	d1-d3/a1-a3

noname
	moveq	#err.ipar,d0
	rts

h_setmem
	clr.l	d2
	move.l	set_nam(a1),a3		; point to filename
	move.w	(a3),d2 		; length of filename
	beq.s	noname
	add.w	d2,d2			; twice, (for homedir)
	moveq	#hd_maxlen+3,d0 	; space for current dir + len counter
	bclr	#0,d0			; round down
	add.w	d0,d2
	add.l	#hdr.len,d2		;  + space for file header (includes space for my header)
	movem.l hpareg,-(sp)
	exg	d2,d1			; this much space for this job
	moveq	#sms.achp,d0		; allocate
	trap	#1
	movem.l (sp)+,hpareg
	tst.l	d0			; alloc OK?
	bne	tha_exit		; ...oops
	lea	hmt_free+iod_iolk-iod_clos(a2),a3 ; point to "driver"
	move.l	a3,chp_drlk-chp.len(a0) ; fill it in in usage block, this will call the job remove routine to call the free routine
	bsr	h_link			; link into my list

; now set the directories etc...
; a0 points to mem
; first get the dir name : to obtain this, we gradually shorten the total file-
; name until we get to the first '_' (from the end). Then we open a file with
; that name & see whether it is a dir. If yes, we found the directory.
; All this is to get around the dev bug.
dirregs reg	d1/d4/d7/a1/a2
d1_stk	equ	0
	lea	hl_data(a0),a0		; point to usable space now
	move.l	a0,a3			; and keep
	movem.l dirregs,-(sp)
	move.l	set_nam(a1),a2		; point to filename
	clr.l	d7
	move.w	(a2),d7 		; full length of file name
	move.l	d7,d4			; keep
; strip until next  underline
namelp2 lea	1(a2),a1		; point to end of name
namelp	subq.w	#1,d4			; make new end of name
	beq.s	huh			; errrrr?
	cmp.b	#'_',(a1,d4.w)		; is it an underscore?
	bne.s	namelp			; no, so loop until we find one
; here we found a potential directory
fndnm	move.w	d4,(a2) 		; set new name length (!)
	move.l	a2,a0			; point to "name"
	moveq	#ioa.open,d0		; open ...
	moveq	#ioa.kshr,d3		; ...as old file ...
	move.l	d1_stk(a7),d1		; ...for this job ...  ***1.03
	trap	#2			; ...now
	tst.l	d0			; open ok?
	bne.s	namelp2 		; no, so try next
	move.l	a3,a1			; where we read file header to
	moveq	#hdr.len,d2		; we have that much space
	moveq	#-1,d3
	moveq	#iof.rhdr,d0
	trap	#3			; read file header of this file
	move.l	d0,d2
	moveq	#ioa.clos,d0
	trap	#2			; close file again
	tst.l	d2			; could we read file?
	bne.s	namelp2 		; no, we couldn't, go round again
	cmp.b	#-1,hdr_type(a3)	; is file a directory?
	bne.s	namelp2 		; no
	move.w	d4,d3			; new file length
	move.w	d7,(a2) 		; reset old file length
	moveq	#0,d0			; no error
	bra.s	rd_end
huh	moveq	#err.itnf,d2
rd_end	movem.l (sp)+,dirregs
	move.l	d2,d0
	bne.s	tha_exit		; ooops on header read
	move.l	a3,a0

; a0 = pointer to usable space (currently contains file header)
; a1 = pointer to parameters
; a2 = thing linkage
; a3 = a0
; a4 =

; now first copy the filename
	move.l	d4,-(sp)
	move.l	set_nam(a1),a4		; point to filename
	move.w	(a4)+,d0		; length of filename
	move.l	a4,a1			; keep
	move.w	d0,(a3)+		; lrngth of filename
	addq.w	#1,d0
	bclr	#0,d0			; make even
	subq.w	#1,d0
cpylp1	move.b	(a4)+,(a3)+		; copy filename
	dbf	d0,cpylp1
; now copy the dirname
	move.w	d3,d4			; length of dirname
	addq.w	#1,d3
	bclr	#0,d3			; make even
	subq.w	#1,d3			; make counter to copy dir name
	move.l	a1,a4			; point to filename
	move.w	d4,(a3)+		; length of dirname
	move.w	d3,d0			; how often we copy a byte from name
cpylp2	move.b	(a1)+,(a3)+		; copy dirname
	dbf	d0,cpylp2

; and insert a copy of current directory of the calling job
	sub.w	#12,sp			; simulate thing call
	move.l	sp,a1
	moveq	#-1,d0			; calling job
	move.l	d0,(a1) 		; job ID
	move.w	#hd_maxlen+2,6(a1)	; space in our buffer
	move.l	a3,8(a1)		; and the buffer itself
	bsr	getcur
	add.w	#12,sp
	tst.l	d0			; all right?
	bne.s	cpy3			; no, copy home directory as default
	tst.w	(a3)			; got any name?
	bne.s	tha_ok			; yes, it's all right
cpy3	move.w	d4,(a3)+		; no, copy home directory instead
cpylp3	move.b	(a4)+,(a3)+
	dbf	d3,cpylp3
tha_ok
	move.l	(sp)+,d4
	moveq	#0,d0			; no error
tha_exit
	rts				; leave


;--------------------------------------------------------------------
* Free the memory occupied by this job from my list.
* Note that this is called when the job remove routine is called
* (and only then).
* There is no facility to remove the job entry from my list
* other than here.
* This is called from SUPERVISOR mode, with A0 pointing
* to the (common heap link) header of the reserved space
;---------------------------------------------------------------------

h_fr_mem
	move.l	chp_drlk(a0),a2 	; point to "driver"...
	lea	iod_clos-iod_iolk-hmt_free(a2),a2 ; ...and thus my linkage block
	bsr	h_unlnk 		; unlink from my linked list
	move.w	mem.rchp,a2		; and return to common heap
	jmp	(a2)

	end
