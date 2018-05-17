; Current dir handling for the HOME thing  (c) W. Lenerz + Marcel Kilgus 2005



; 2005-10-23  1.00  initial release version
; 2005-11-02  1.01  uses checkid
; 2005-12-04  1.02  check that directory is actually valid and resolve DEV refs
; 2006-03-31  1.03  correct length set if simple device name (wl)
;
;
;---------------------------------------------------------------------
;
; The current dir is the only thing in the HOME thing datastructure that is
; not permanent - it may change as the job sees fit.
;
;--------------------------------------------------------------------

	section util

	include dev8_keys_err
	include dev8_keys_chp
	include dev8_keys_qdos_sms
	include dev8_keys_qdos_ioa
	include dev8_keys_qdos_io
	include dev8_smsq_home_data
	include dev8_keys_jcb
	include dev8_keys_hdr

	xdef	setcur			; set current dir for a job with job ID
	xref	checkid 		; check job ID, set if -1
	xref	h_fndjb
  

hms.reg reg	d1/d2/d3/d7/a1/a3/a4

; go to current dir in a job entry
; a0 cr 	job entry start/current dir start
; d0 cr 	length up to now/adjusted name length
; d1   s

del_it2 bsr.s	del_it			; do it twice
	nop				; stupid 68K
del_it	moveq	#2,d1			; length word
	add.w	(a0),d1 		; + length
	addq.l	#1,d1
	bclr	#0,d1			; make even
	add.l	d1,a0			; point to next entry
	sub.l	d1,d0			; minus length of this name
	rts

;---------------------------------------------------------------------
; This sets the current directory to any kind of directory.
; First we check in list entry whether it will still fit in the current space:
; If yes, just replace old current dir.
; If not, set "length" of this dir to -1, then get new mem, set current dir
; in it and set pointer to it in list entry
;---------------------------------------------------------------------


; A1 c p points to parameter list (job ID, LW, dir name)
;
setcur
	movem.l hms.reg,-(sp)
	suba.w	#hdr.len+64,sp
	move.l	set_nam(a1),a0		; point to name
	moveq	#ioa.kshr,d3		; default to file for all directories
	cmp.w	#5,(a0) 		; Probably root dir like 'win1_'
	bhi.s	c_open
	moveq	#ioa.kdir,d3		; use open directory for root dir
c_open	moveq	#ioa.open,d0
	trap	#2			; now
	tst.l	d0			; open ok?
	bne	err_nf			; no, exit
	move.l	a1,a4
	move.l	sp,a1			; where we read file header to
	moveq	#hdr.len,d2		; we have that much space
	moveq	#-1,d3
	moveq	#iof.rhdr,d0
	trap	#3			; read file header of this file
	move.l	d0,d2			; keep error
	bne.s	c_close
	lea	hdr.len(sp),a1
	moveq	#0,d1
	moveq	#-1,d3
	moveq	#iof.xinf,d0		; get medium information
	trap	#3
	move.l	d0,d2			; keep error
c_close moveq	#ioa.clos,d0
	trap	#2			; close file again
	move.b	hdr_type(sp),d3 	; file type
	tst.l	d2
	bne	cs_out
	cmp.b	#hdrt.dir,d3		; was it a directory?
	bne	err_nf			; no!

	move.l	a4,a1
	move.l	(a1),d1 		; job ID into D1
	bsr	checkid 		; check job ID, convert if -1
	bne.s	cs_out			; not a real job, that
	bsr	h_fndjb 		; try to find this job in my list
	bne.s	cs_out			; not found, can't set curent dir, then!
	move.l	-chp.len(a0),d0 	; length of this common heap space
	sub.l	#chp.len,d0		; minus common heap space header
	sub.l	#hl_data,d0		; minus my list entry header
	lea	hl_data(a0),a0		; start of data
	bsr	del_it2 		; point past first two strings
	sub.l	a3,a3			; preset: curr dir is in my list entry
	move.w	(a0),d1 		; length of current dir
	bge.s	c_dir			; is normal, curr dir is in list entry
	move.l	a0,a3			; keep this pointer
	move.l	2(a0),a0		; in fact, A0 points to mem space only for curr dir
	move.l	-chp.len(a0),d0 	; length
	sub.l	#chp.len,d0		; total length for string
	bra.s	c_com

c_dir	sub.l	d1,d0			; total space for total string
c_com	subq.l	#2,d0			; total space minus length word
	moveq	#3,d1			; space for two '_' and drive number
	add.w	hdr.len+ioi_dnam(sp),d1 ; space for drive name
	add.w	hdr_name(sp),d1 	; space for directory name
;;;	   move.l  set_nam(a1),a1	   ; point to name
;;;	   move.w  (a1)+,d1		   ; length of name to set
	sub.l	d1,d0
	blt.s	new_spce		; new name doesn't fit in space available
cpy_nm
; a0 points to length word for new space
; d1 = length
; A1 = new name (after length word
	move.l	a0,a3			; keep pointer
	move.w	d1,(a0)+		; set length word
	lea	hdr.len+ioi_dnam(sp),a1 ; drive name
	move.w	(a1)+,d1
	bra.s	cpy_dn1
cpy_dn	move.b	(a1)+,(a0)+		; copy drive name
cpy_dn1 dbf	d1,cpy_dn

	moveq	#'0',d1
	add.b	hdr.len+ioi_dnum(sp),d1
	move.b	d1,(a0)+		; drive number
	move.b	#'_',(a0)+		; drive underscore

	lea	hdr_name(sp),a1 	; finally copy directory name
	move.w	(a1)+,d1
	bne.s	cpy_lp1
	subq.w	#1,(a3) 		; if only drive, make correct length **1.03**
	bra.s	cs_nous
cpy_lp	move.b	(a1)+,(a0)+
cpy_lp1 dbf	d1,cpy_lp
	move.b	#'_',(a0)+		; and final underscore
cs_nous
	moveq	#0,d0
cs_out
	adda.w	#hdr.len+64,sp
	movem.l (sp)+,hms.reg
	tst.l d0
	rts
err_nf
	moveq	#err.fdnf,d0
	bra.s	cs_out

; we need a new space for the current dir, the space we have is too small
new_spce
	move.l	a0,a4			; point to length word for current dir
	move.l	a1,-(sp)
	move.l	a3,d0			; were we still in list entry ?
	beq.s	no_remv 		; yes, no need to get rid of independent mem space
	move.l	a3,a4			; this is pointer in list entry
	moveq	#sms.rchp,d0
	trap	#1			; remove old heap space
no_remv
	clr.w	(a4)			; in case I can't get new mem: no more current dir
	moveq	#sms.achp,d0		; get new mem for current dir
	move.l	d1,d7			; keep
	moveq	#-1,d2			; for myself
	trap	#1			; get new mem now
	move.l	(sp)+,a1
	tst.l	d0			; OK?
	bne.s	cs_out			; ...  no
	move.w	#-1,(a4)+		; flag new heap space
	move.l	a0,(a4) 		; set pointer to it
	move.l	d7,d1
	bra.s	cpy_nm

	end
