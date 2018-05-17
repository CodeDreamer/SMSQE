* Entry point for thing manipulation	v0.00   Feb 1988  J.R.Oakley  QJUMP
*
	section thing
*
	include 'dev8_keys_err'
	include 'dev8_keys_68000'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_qdos_ioa'
*
	xref	th_lthg
	xref	th_rthg
	xref	th_uthg
	xref	th_fthg
	xref	th_zthg
	xref	th_nthg
	xref	th_nthu
*
	xdef	th_entry
	xdef	th_enfix
	xdef	th_exit
	xdef	th_exitd3
utimeout equ	8
*
th_enfix
	bset	#15,d0			 ; set fixed flag
*+++
* This entry point is for calling from user mode: in SMS2 it would be
* replaced by a TRAP #1, and the entry vectors added in with the rest.
* The parameters are exactly the same as for the SMS2 version, though.
* Under QDOS, all calls to SMS.ZTHG must be made in user mode, as
* must calls to FTHG on behalf of another Job.
*
*	Registers:
*		Entry				Exit
*	D0	key				error code
*	D1	Job ID or -1 (UTHG, FTHG)	preserved
*	D2	additional parameter		additional return (U/FTHG)
*						job ID		  (NTHU)
*	D3	additional parameter		preserved	  (FTHG)
*		timeout 			version ID	  (UTHG)
*	A0	name of thing (all but LTHG)	preserved
*	A1	pointer to thing linkage (LTHG) 
*						pointer to thing  (UTHG)
*		pointer to usage block		next usage block  (NTHU)
*	A2	additional parameter		additional return (U/FTHG)
*
*	D0 call and return values may be:
*	SMS.LTHG	link in thing	FEX  thing already exists
*	SMS.RTHG	remove thing	FDIU thing in use 
*					ITNF thing not found
*	SMS.UTHG	use thing	IMEM insufficient memory
*					ITNF thing not found
*					any other returns from Thing itself
*	SMS.FTHG	free thing	ITNF thing not found
*					any other returns from Thing itself
*	SMS.ZTHG	zap thing	ITNF thing not found
*	SMS.NTHG	next thing	ITNF thing not found
*	SMS.NTHU	next user	ITNF thing not found
*					IJOB user not found
*---
th_entry
*
extreg	reg	d5/d7
entreg	reg	d1/d3/d4/d6/a0/a3-a5
stk_d1	equ	$00
stk_d3	equ	$04
*
	movem.l extreg,-(sp)
	movem.l entreg,-(sp)		; registers go to user stack
	move.w	d0,d7
	swap	d7			; keep action... 
	move	sr,d7			; ...and status safe

the_retry
	trap	#0			; and use supervisor mode
	move.l	a6,a5			; now it's safe to keep old A6
*
infreg	reg	d1-d3/a0/a1
	movem.l infreg,-(sp)
	moveq	#sms.info,d0		 ; find system variables
	trap	#do.sms2
	move.l	a0,a6			 ; keep them safe
	move.l	d1,d6			 ; and current Job
	moveq	#0,d2			 ; now with system at top of tree
	moveq	#sms.injb,d0		 ; get info on current Job
	trap	#do.sms2
	tst.l	d0
	beq.s	the_relt
	moveq	#0,d0
	bra.s	the_not
the_relt
	bclr	#7,$16-$68(a0)		 ; is A0 relative to A6?...   $$$$ MAGIC
the_not
	movem.l (sp)+,infreg
*
	beq.s	setz			 ; ...no
	add.l	a5,a0			 ; yes, make it absolute
setz
	move.l	d7,d0			 ; get operation key
	swap	d0
	ext.w	d0
	sub.w	#sms.lthg,d0		 ; key must be at least this
	blt.s	the_exbp		 ; wrong
	cmp.w	#(th_etend-th_etab)/2,d0 ; and less than this
	bge.s	the_exbp		 ; also wrong
	moveq	#0,d5			 ; assume there's no timeout
	cmp.w	#sms.uthg-sms.lthg,d0	 ; only time out on UTHG
	bne.s	the_jtab		 ; which it isn't
	move.w	d3,d5			 ; it is, set the timeout
	ext.l	d5			 ; -ve is more or less indefinite
	bclr	#31,d5			 ; make it positive, though
the_jtab
	add.w	d0,d0			 ; index into table
	move.w	th_etab(pc,d0.w),d0	 ; find offset of code 
	jmp	th_etab(pc,d0.w)	 ; and call it
*+++
* To avoid using supervisor stack, the various thing routines are jumped
* to directly, and jump here when they complete.  The condition codes
* should be set and the supervisor stack empty when this routine is
* called.
*---
th_exitd3
	move.l	a5,a6			; restore old A6
	move	d7,sr			; back to user mode
	move.l	d3,stk_d3(sp)		; set d3 return
	bra.s	th_exchk

th_exit
	move.l	a5,a6			; restore old A6
	move	d7,sr			; back to user mode
th_exchk
	tst.l	d0
	beq.s	the_exit		; OK, done
	subq.l	#utimeout,d5		; are we timing out?
	ble.s	the_exit		; no, at least not any more
	cmp.l	#err.fdiu,d0		; yes, was it just "in use"?
	bne.s	the_exit		; no, quit
	moveq	#utimeout,d3		; wait N ticks
	moveq	#myself,d1		; that's me
	moveq	#sms.ssjb,d0
	trap	#do.sms2
	movem.l (sp),entreg		 ; restore environment
	move.w	d5,d3			 ; but with a different timeout
	bra	the_retry		 ; and try again
the_exit
	movem.l (sp)+,entreg		 ; restore registers from user stack
	movem.l (sp)+,extreg
	tst.l	d0
	rts				 ; and exit
*
th_etab
	dc.w	th_lthg-th_etab
	dc.w	th_rthg-th_etab
	dc.w	th_uthg-th_etab
	dc.w	th_fthg-th_etab
	dc.w	th_zthg-th_etab
	dc.w	th_nthg-th_etab
	dc.w	th_nthu-th_etab
th_etend
*
the_exbp
	moveq	#err.ipar,d0
	bra.s	th_exit
*
	end
