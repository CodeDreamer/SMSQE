; Open DEV	    V2.00     1989   Tony Tebby   QJUMP

	section dev

	xdef	dev_open

	xref	iou_achb
	xref	iou_rchb
	xref	cv_streq

	include 'dev8_keys_err'
	include 'dev8_keys_sys'
	include 'dev8_keys_chn'
	include 'dev8_keys_iod'
	include 'dev8_dd_dev_data'

;+++
; This routine expands the name given, and then finds the real device driver
; and drive definition. It throws away the DEV drive definition patches, the
; addresses on the stack, checks for in use, and finally calls the real open
; routine.
;---
dev_open
	lea	dvd_save(a3),a4
dvd_opdo
dvo.reg  reg	 d4-d7/a0/a1/a3/a4
dvo.rego reg	 a0/a1/a3		 ; open call registers
dvo_rego equ	$10
stk_scrobble equ 24*4			 ; nr of bytes to scrobble up
stk_chan equ	$10
stk_dd	 equ	$14
stk_base equ	$18
stk_save equ	$1c
	movem.l dvo.reg,-(sp)		 ; save non-shashables

	clr.b	dvd_chck-dvd_save(a4)	 ; no DEVs checked

	moveq	#0,d6
	move.b	chn_drid(a0),d6 	 ; drive id

	lea	chn_name(a0),a0
	move.l	a4,a2
	moveq	#dvd.svnm/2,d0
dvo_svnm
	move.w	(a0)+,(a2)+
	dbra	d0,dvo_svnm		 ; save the name

	moveq	#0,d7
	move.b	iod_dnum(a1),d7 	 ; drive number

	lsl.w	#2,d6
	lea	sys_fsdd(a6),a0
	clr.l	(a0,d6.w)		 ; remove dd block from table

	move.l	a1,a0
	jsr	iou_rchb		 ; return dd block
	bra.s	dvo_try

dvo_retry
	move.w	dvd_svnx-dvd_save(a4),d7 ; next?
	beq.l	dvo_nf

dvo_try
	lea	-dvd_save(a4),a3	 ; get our linkage back
	bset	d7,dvd_chck(a3) 	 ; already checked?
	bne.l	dvo_nf			 ; ... yes

	lsl.w	#dvd.shft,d7		 ; usage name offset
	move.b	dvd_next(a3,d7.w),dvd_svnx+1(a3)  ; set pointer to next

	move.w	dvd_dnam(a3,d7.w),d1	 ; length of device name
	ble.s	dvo_retry		 ; ... none

	lea	dvd_fnam(a3,d7.w),a1	 ; directory name
	move.w	(a1)+,d0
	move.w	(a4),d2 		 ; length of file name
	add.w	d0,d2
	cmp.w	#chn.nmln,d2		 ; too long?
	bhi.s	dvo_retry

	move.l	stk_chan(sp),a0
	lea	chn_name(a0),a0

	move.w	d2,(a0)+

	bra.s	dvo_cpde
dvo_cpdn
	move.b	(a1)+,(a0)+
dvo_cpde
	dbra	d0,dvo_cpdn		 ; directory in front

	lea	(a4),a1 		 ; old name
	move.w	(a1)+,d0
	bra.s	dvo_cpfe
dvo_cpfn
	move.b	(a1)+,(a0)+
dvo_cpfe
	dbra	d0,dvo_cpfn		 ; old name afterwards

; check device name against all fsd drivers

	lea	sys_fsdl(a6),a5
dvo_fdrv
	move.l	(a5),d0 		 ; next driver
	beq.l	dvo_nf
	move.l	d0,a5
	lea	iod_dnus-iod_iolk(a5),a1 ; drive name
	cmp.w	(a1)+,d1		 ; the right length?
	bne.s	dvo_fdrv		 ; ... no
	lea	dvd_dnam+2(a3,d7.w),a0	 ; characters of name
	move.w	d1,d0			 ; name length
	subq.w	#1,d0
dvo_cnloop
	cmp.b	(a0)+,(a1)+		 ; check characters
	dbne	d0,dvo_cnloop
	bne.s	dvo_fdrv

	cmp.l	a3,a5			 ; in our linkage?
	blt.s	dvo_dnum		 ; ... no
	lea	dvd_end(a3),a1
	cmp.l	a1,a5
	blt.s	dvo_fdrv		 ; ... it is us!!

dvo_dnum
	move.b	dvd_dnum(a3,d7.w),d5	 ; drive number

; the right drive type has been found, check if physical definition set up

	moveq	#sys.nfsd,d6		 ; number of file definitions
	lea	sys_fsdt(a6),a2 	 ; scan from top down
	moveq	#-1,d1			 ; set no hole
dvo_fphy
	subq.w	#1,d6			 ; drive 'ID'
	blt.s	dvo_newp		 ; ... new definition required
	move.l	-(a2),d0		 ; next drive
	bne.s	dvo_cphy		 ; ... there is one
	move.w	d6,d1			 ; keep lowest empty
	move.l	a2,d2			 ; and address of it
	bra.s	dvo_fphy
dvo_cphy
	move.l	d0,a0			 ; check
	cmp.l	iod_drlk(a0),a5 	 ; ... driver
	bne.s	dvo_fphy
	cmp.b	iod_dnum(a0),d5 	 ; ... and number
	bne.s	dvo_fphy
	bra.s	dvo_scrobble		 ; all done

dvo_newp
	move.w	d1,d6			 ; new physical definition
	move.l	d2,a2			 ; at this position
	blt.l	dvo_tf			 ; ... table full !!!!

	move.l	iod_plen-iod_iolk(a5),d0 ; length to allocate
	bsr.l	iou_achb		 ; allocate some space
	bne.l	dvo_exit		 ; ... oops
	move.l	a0,(a2) 		 ; set new id in table

	move.l	a5,iod_drlk(a0) 	 ; set drive linkage
	move.b	d5,iod_dnum(a0) 	 ; and number

; We have now found the drive and set up the file name, we do not need
; the dev linkage any more, so we will scrobble up the stack changing changing
; to pointers to the linkage and dd blocks
;	d1=old dd
;	a1=new dd
;	d3=old linkage base
;	a3=new linkage base
;	d5=old dd link
;	a5=new dd link
;
dvo_scrobble
	moveq	#stk_scrobble/2-1,d2	 ; amount to scrobble

	move.l	stk_dd(sp),d1		 ; old
	move.l	a0,a1			 ; new dd

	move.l	stk_base(sp),d3 	 ; old
	lea	-iod_iolk(a5),a3	 ; new linkage base

	moveq	#iod_iolk,d5		 ; old
	add.l	d3,d5			 ; .... io linkage

	move.l	sp,a0
dvo_scloop
	move.l	(a0),d0
	cmp.l	d1,d0			 ; dd?
	beq.s	dvo_sdd
	cmp.l	d3,d0			 ; base?
	beq.s	dvo_sbase
	cmp.l	d5,d0			 ; link?
	bne.s	dvo_scend
	move.l	a5,(a0)
	bra.s	dvo_scend
dvo_sbase
	move.l	a3,(a0)
	bra.s	dvo_scend
dvo_sdd
	move.l	a1,(a0)

dvo_scend
	addq.w	#2,a0			 ; move on a word
	dbra	d2,dvo_scloop

dvo_open
	move.l	stk_chan(sp),a0 	 ; restore channel
	move.l	a5,a2			 ; this is true sometimes
	move.b	d6,chn_drid(a0)
	move.l	a2,chn_drvr(a0) 	 ; just in case this is already set
	move.b	chn_accs(a0),d3 	 ; recover this
	bge.s	dvo_ckfl
	st	dvd_chck-dvd_save(a4)	 ; delete - do not chain

; all set up, check for 'in use'

dvo_ckfl
	lea    chn_name(a0),a0		 ; point to name
	lea    sys_fsch(a6),a4		 ; scan linked list of fsd channels

dvo_ckiu
	move.l	(a4),d0 		 ; any more?
	beq.s	dvo_done		 ; ... no
	move.l	d0,a4
	cmp.l	#chn_nchk,chn_nchk-chn_link(a4); no checking?
	beq.s	dvo_ckiu		... no

	cmp.b	chn_drid-chn_link(a4),d6 ; same drive?
	bne.s	dvo_ckiu		 ; ... no, carry on
	lea	chn_name-chn_link(a4),a1 ; name of file already open
	cmp.l	a0,a1			 ; same channel block?
	beq.s	dvo_ckiu		 ; ... yes, skip it
	jsr	cv_streq		 ; check file name
	bne.s	dvo_ckiu		 ; ... no match

	moveq	#0,d0
	move.b	chn_accs-chn_link(a4),d0 ; look at access mode of existing
	lea	dvo_iutab(pc),a4
	btst	d3,(a4,d0.w)		 ; shareable?
	bne.s	dvo_iu			 ; ... no

dvo_done
	move.l	iod_open(a3),a4 	 ; call open routine
	movem.l dvo_rego(sp),dvo.rego
	jsr	(a4)			 ; do normal open call
	move.l	a0,stk_chan(sp) 	 ; channel might have changed!
	beq.s	dvo_exit		 ; ... ok
	cmp.l	#err.fdnf,d0		 ; not found?
	bne.s	dvo_exit		 ; ... no
	move.l	stk_save(sp),a4 	 ; ... yes, get back our save address
	bra	dvo_retry		 ; and retry

dvo_tf
	moveq	#err.imem,d0
	bra.s	dvo_exit

dvo_nf
	moveq	#err.fdnf,d0
	bra.s	dvo_exit

dvo_iu
	moveq	#err.fdiu,d0

dvo_exit
	movem.l (sp)+,dvo.reg
	tst.l	d0
	rts

*
* table of exclusive use: 0,2,3,(7) exclusive, 1,4 shareable
*
dvo_iutab
	dc.b	%11111111
	dc.b	%10001101
	dc.b	%11111111
	dc.b	%11111111
	dc.b	%10001101
	end
