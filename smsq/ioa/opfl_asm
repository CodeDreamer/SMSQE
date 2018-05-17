* Open file
* 2.02	 14.11.2015  amendments to use the Recent thing  (wl)
; nb: if things are put on the stack, amend "d1stk"
; 2.01	  1986  Tony Tebby  QJUMP
* QDOS compatible version
*
*
	section ioa
*
	xdef	ioa_opfl
*
	xref	ioa_ffsd
	xref	ioa_cknm
	xref	mem_llst
	xref	mem_rlst
	xref	mem_achp
	xref	mem_rchp
	xref	gu_thjmp
*
	include dev8_keys_err
	include dev8_keys_sys
	include dev8_keys_chn
	include dev8_keys_iod
	include dev8_keys_thg		; wl
	include dev8_keys_qdos_sms	; wl
	include dev8_keys_qdos_ioa   !!!!!!!!!!
*
*	d0	error return
*	d1 c  p
*	d2   s	scratch
*	d3 c s	open key (-1 is delete)
*	d4   s	scratch
*	d5   s	scratch
*	d6   s	scratch
*	d7    p
*	a0  r	pointer to channel block
*	a1 c s	pointer to name / pointer to physical definition
*	a2    p (pointer to physical definition)
*	a3   s	base of linkage block
*	a4   s	fsd open routine address
*	a5  r	iod linkage
*	a6 c  p base of system vars
*
reglist reg	d1/d7/a2
*
iof_ikey
	moveq	#err.ipar,d0		invalid parameter
	rts
*
ioa_opfl
	move.b	d3,d0			check range of open key
	addq.b	#1,d0
	subq.b	#ioa.maxo+1,d0
	bhi.s	iof_ikey		... invalid
*
	movem.l reglist,-(sp)		save non-shashables
*
	move.l	a1,-(sp)		; keep name pointer ****** 2.02 wl
*
*
	bsr.l	ioa_ffsd		find filing system device (a2,a3,a5)
	bne.l	iof_exit		... oops
*
	move.l	#chn_fend,d1		allocate file system channel block
	bsr.l	mem_achp		... in heap
	bne	iof_exit		... oops
*
	move.b	d3,chn_accs(a0) 	set access mode
	move.b	d6,chn_drid(a0) 	and drive id
*
	lea	chn_name(a0),a4 	copy name into channel block
	move.w	d7,(a4)+
	bra.s	iof_nlend
*
iof_nloop
	move.b	(a1)+,(a4)+		copy characters of name
iof_nlend
	dbra	d7,iof_nloop
*
* physical definition allocated, channel allocated, check for 'in use'
*
	move.l	a0,-(sp)		save pointer to strings
	lea	sys_fsch(a6),a1 	scan linked list of fsd channels
	lea	chn_link(a0),a0
	bsr.l	mem_llst
	move.l	a0,a1			... skip the first - it's me
*
iof_ckiu
	move.l	(a1),d0 		any more?
	beq.s	iof_open		... no
	move.l	d0,a1
	cmp.l	#chn_nchk,chn_nchk-chn_link(a1); no checking?
	beq.s	iof_ckiu		... no

	cmp.b	chn_drid-chn_link(a1),d6 same drive?
	bne.s	iof_ckiu		... no, carry on
	lea	chn_name-chn_link(a1),a4 name of file already open
	move.l	(sp),a0
	bsr.l	ioa_cknm		check file name
	bne.s	iof_ckiu		... no match
*
	moveq	#0,d0
	move.b	chn_accs-chn_link(a1),d0 look at access mode of existing
	lea	iof_iutab(pc),a4
	btst	d3,(a4,d0.w)		shareable?
	bne	iof_iu			... no
*
iof_open
	move.l	(sp)+,a0
	move.l	iod_open(a3),a4 	call open routine
	move.l	a2,a1			!!!! for qdos driver
	movem.l a2/a3/a5,-(sp)		save linkage
	jsr	(a4)			do call
	movem.l (sp)+,a2/a3/a5
	tst.l	d0
	bne	iof_rchp
	tst.b	chn_accs(a0)		delete?
	bmi.s	iof_rchp		... yes
*
	addq.b	#1,iod_nrfl(a2) 	one more file open
*

;------------------------------------------------------------
; recent list addition
rcntreg reg a0/a1/a3
d1stk	equ	24			; where jobID is on stack
; try to stop dirs from being added to the list
; here d0 is still 0
	tst.b	sys_rthg(a6)		; use recent thing?
	beq.s	iof_tst 		; no...
	cmp.b	#4,d3			; open dir?
	beq.s	iof_tst 		; yes, don't add to recent thing
	lea	chn_name(a0),a4 	; point to name
	move.w	(a4)+,d1		; length of name
	beq.s	iof_tst 		; 0 length, was (main) directory
	cmp.b	#'_',-1(a4,d1.w)	; if name ends with "_" it's a dir
	beq.s	iof_tst 		; so don't add it to recent thing
	move.l	(sp),d0 		; pointer to entire name
	movem.l rcntreg,-(a7)
	move.l	d0,a3			; keep ptr to name
	move.l	#'ADDF',d2		; extension to use
	lea	rcnt_name,a0		; point to name of thing
	moveq	#-1,d3			; wait forever
	moveq	#-1,d1			; I will use the thing
	moveq	#sms.uthg,d0		; use thing
	jsr	gu_thjmp		; on return A2= ptr thg header, a1 to thg

	tst.l	d0			; use thing ok?
	bne.s	ad_err			; no->...
	move.l	a1,a0			; pointer to thing
	subq.l	#8,sp			; get some space
	move.l	sp,a1			; and point to it
	move.l	d1stk(sp),(a1)		; insert ID of job
	move.l	a3,4(a1)		; pointer to name
	jsr	thh_code(a0)		; call extn thing
	addq.l	#8,sp			; reset stack
	lea	rcnt_name,a0		; now free thing
	moveq	#sms.fthg,d0
	moveq	#-1,d1
	jsr	gu_thjmp
ad_err	moveq	#0,d0			; pretend this always succeeded
	movem.l (sp)+,rcntreg

;----------------------------------------

iof_tst
	tst.l	d0
iof_exit
	addq.l	#4,sp			; ***** 2.02 get rid of saved a1
	movem.l (sp)+,reglist
	rts
*
iof_iu
	move.l	(sp)+,a0	restore channel base
	moveq	#err.fdiu,d0	in use
iof_rchp
	move.l	d0,d4		save error flag

	lea	sys_fsch(a6),a1 	unlink this channel
	lea	chn_link(a0),a0
	bsr.l	mem_rlst
	lea	-chn_link(a0),a0

	bsr.l	mem_rchp	return heap
	move.l	d4,d0		reset error
	bra.s	iof_exit
*
* table of exclusive use: 0,2,3,(7) exclusive, 1,4 shareable
*
iof_iutab
	dc.b	%11111111
	dc.b	%10001101
	dc.b	%11111111
	dc.b	%11111111
	dc.b	%10001101


rcnt_name dc.w	  6,'Recent'

	end
