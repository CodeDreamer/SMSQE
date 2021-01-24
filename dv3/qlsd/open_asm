; DV3 open routines		  V3.04 	  1992 Tony Tebby
;
; QDOS compatible version
;
; 2018-06-24  3.02  Keep error code from ddl_check (MK)
; 2018-07-30  3.03  Fixed Minerva workaround in case of open through DEV (MK)
; 2020-04-22  3.04  Fixed Minerva heap corruption issue (MK)

	section dv3

	xdef	dv3_open
	xdef	dv3_opnb

	xref	dv3_density
	xref	dv3_slen
	xref	dv3_posi
	xref	dv3_sbtr
	xref	dv3_sbclr
	xref	dv3_qdit

	include 'dev8_dv3_qlsd_keys'
	include 'dev8_keys_err'
	include 'dev8_keys_sys'
	include 'dev8_keys_qlv'
	include 'dev8_keys_chn'
	include 'dev8_keys_qdos_ioa'
	include 'dev8_mac_assert'

;+++
; DV3 open routine (QDOS compatible entry)
;---
dv3_open
dv3_opnb
	move.l	a1,a4			 ; drive definition block
	moveq	#0,d7
	move.b	d3c_qdid(a0),d7
	move.b	d7,ddf_drid(a4) 	 ; drive id
	swap	d7
	move.b	ddf_dnum(a4),d7 	 ; drive number

	tst.b	ddf_ftype(a4)		 ; open for direct sector access?
	bpl.s	dop_do
	moveq	#err.fdiu,d0
dop_rterr
	rts

dop_do
	jsr	ddl_fslave(a3)		 ; find real linkage
	bne.s	dop_rterr
	move.l	a3,ddf_ptddl(a4)	 ; set driver linkages

	move.l	a0,a5			 ; save old channel block
	lea	sys_fsch(a6),a1
	lea	d3c_qdlink(a5),a0
	move.w	mem.rlst,a2
	jsr	(a2)			 ; unlink old block (safe)

	clr.l	(a0)			 ; and clear link

	moveq	#d3c_end/4,d1
	lsl.l	#2,d1
	moveq	#0,d2
	movem.l a3/a6,-(sp)		 ; for QDOS compatibility...
	move.w	mem.achp,a2
	jsr	(a2)			 ; allocate new channel block
	movem.l (sp)+,a3/a6
	beq.s	dop_copy		 ; ... ok, copy channel
	move.l	a5,a0			 ; ... oops
	rts

dop_copy
	lea	d3c_drvr(a0),a1
	lea	d3c_drvr(a5),a2
	moveq	#(d3c_name+d3c.pthl+4-d3c_drvr)/4-1,d0
dop_cchan
	move.l	(a2)+,(a1)+		 ; copy useful part of old channel
	dbra	d0,dop_cchan

	lea	sys_fsch(a6),a1
	lea	d3c_qdlink(a0),a2
	move.l	(a1),(a2)
	move.l	a2,(a1) 		 ; link back in to channel list

	move.l	sys_chtb(a6),a1 	 ; search channel table for old block
	move.w	sys_chtp(a6),d0
dop_xchn
	cmp.l	(a1)+,a5		 ; old channel?
	dbeq	d0,dop_xchn
	bne.s	dop_prec		 ; not found
	move.l	a0,-(a1)		 ; replace old channel

dop_prec
	move.l	a0,d3
	lea	d3c_name+2(a0),a1
	move.l	a1,d2			 ; start of string (destination)
	lea	d3c_qname(a0),a0
	move.w	(a0)+,d0		 ; source of string

	lea	dv3_qdit,a2		 ; QDOS to internal translate table
	moveq	#0,d1
	bra.s	dop_cine
dop_cint
	move.b	(a0)+,d1
	move.b	(a2,d1.w),(a1)+ 	 ; translate
dop_cine
	dbra	d0,dop_cint

	move.l	d3,a0			 ; restore channel block

	sf	(a1)			 ; zero at end
	exg	d2,a1
	sub.w	a1,d2
	move.w	d2,-(a1)		 ; string length

	moveq	#1,d0
	add.b	d3c_accs(a0),d0 	 ; access mode + 1
	move.b	dop_rdon(pc,d0.w),d3c_ro(a0) ; set read only flag

	moveq	#$ffffffdf,d1		 ; check for *D or *d
	and.l	d3c_name(a0),d1
	sub.l	#$00042a44,d1		 ; is it?
	bne.l	dop_check		 ; ... no, check medium
	move.b	d3c_name+4(a0),d1	 ; ... length
	sub.b	#'0',d1
	blt.s	dop_inam		 ; bad sector length
	cmp.b	#7,d1
	bgt.s	dop_inam		 ; bad sector length
	btst	d1,ddl_sectl(a3)	 ; valid length?
	beq.s	dop_inam		 ; ... no

	move.b	d3c_name+5(a0),d0	 ; density flag
	jsr	dv3_density
	beq.s	dop_direct

dop_inam
	moveq	#err.inam,d0
	bra.s	dop_ex1

dop_ro
	moveq	#err.rdo,d0
	bra.s	dop_ex1

dop_noshare
	move.l	d4,a0			 ; restore channel
	move.l	d3,a3			 ; and link
dop_fdiu
	moveq	#err.fdiu,d0
	bra.s	dop_ex1

dop_fdnf
	moveq	#err.fdnf,d0
dop_ex1
	bra.l	dop_exit

dop_rdon dc.b	d3c.ok	 ; delete
	 dc.b	d3c.ok	 ; exclusive
	 dc.b	d3c.ro	 ; share
	 dc.b	d3c.updt ; new
	 dc.b	d3c.updt ; overwrite
	 dc.b	d3c.ro	 ; directory

; All the messing about is done, time for a direct sector open

dop_direct
	tst.b	ddf_nropen(a4)		 ; files open?
	bne.s	dop_fdiu		 ; ... yes
	tst.b	d3c_accs(a0)		 ; delete?
	blt.s	dop_fdiu		 ; ... yes

dop_dflush
	jsr	ddl_dflush(a3)		 ; flush everything
	addq.l	#-err.nc,d0		 ; ... not complete?
	beq.s	dop_dflush

	jsr	dv3_sbclr		 ; and clear out all the slave blocks

	move.b	d1,ddf_slflag(a4)
	move.b	d2,ddf_density(a4)

	jsr	dv3_slen		 ; set sector lengths

	jsr	ddl_direct(a3)		 ; direct sector open
	bne.l	dop_exit		 ; ... oops

	move.l	ddf_flong(a4),ddf_fsave(a4) ; save format information
	not.b	ddf_ftype(a4)		 ; direct access

	addq.b	#d3c.asect,d3c_atype(a0) ; access
	assert	d3c.ro,1
	moveq	#d3c.ro,d0
	and.b	ddf_wprot(a4),d0
	move.b	d0,d3c_ro(a0)		 ; set read only flag

	bra.l	dop_set 		 ; set IDs and link in channel block

; All the messing about is done, time for a genuine open

dop_check
	jsr	ddl_check(a3)		 ; check the medium
;;;	   blt.s   dop_fdnf		    ; ... oops, bad medium
	blt.s	dop_ex1 		 ; keep error code from check

	assert	ddf.wprot,$ff
	tst.b	ddf_wprot(a4)		 ; write protected?
	bpl.s	dop_ofile		 ; ... no
	assert	d3c.ro,1
	tst.b	d3c_ro(a0)		 ; read only access?
	bgt.s	dop_ofile		 ; ... yes
	tst.b	d3c_accs(a0)		 ; OPEN no option?
	bne.s	dop_ro			 ; ... no
	addq.b	#ioa.kshr,d3c_accs(a0)	 ; ... yes, set to share
	addq.b	#d3c.ro,d3c_ro(a0)	 ; ... set to share

dop_ofile
	move.l	a3,d3
	move.l	a0,d4
	lea	d3c_name(a0),a1
	move.w	(a1)+,d0		 ; length of name
	beq.s	dop_root		 ; ... no name

	lea	d3c_qname(a5),a0
	move.w	d0,(a0)+

	move.l	ddf_itopck(a4),a3	 ; internal to check translate table
	moveq	#0,d1
dop_xlate
	move.b	(a1)+,d1
	move.b	(a3,d1.w),(a0)+ 	 ; translate
	subq.w	#1,d0
	bgt.s	dop_xlate

; Here we need to check if file already open

	lea	ddf_chnlst-d3c_link(a4),a0 ; linked list of chans

dop_lloop
	move.l	d3c_link(a0),d0 	 ; next channel
	beq.s	dop_open		 ; not already open - do real open
	move.l	d0,a0
	lea	d3c_name(a0),a1
	move.w	(a1)+,d0		 ; length of name
	lea	d3c_qname(a5),a2
	cmp.w	(a2)+,d0
	bra.s	dop_ccelp

dop_cchr
	move.b	(a1)+,d1
	move.b	(a3,d1.w),d1		 ; translate
	cmp.b	(a2)+,d1		 ; ... and compare

dop_ccelp
	dbne	d0,dop_cchr
	bne.s	dop_lloop		 ; no match

	move.l	d4,a1			 ; our real channel block
	tst.b	d3c_ro(a1)		 ; shared possible
	ble.l	dop_noshare		 ; ... no
	tst.b	d3c_ro(a0)
	ble.l	dop_noshare		 ; ... no

	move.b	d3c_atype(a0),d3c_atype(a1) ; old channel is directory?
	bmi.s	dop_open		 ; ... yes, do real directory open

	cmp.b	#ioa.kdir,d3c_accs(a1)	 ; is it open directory?
	beq.s	dop_open		 ; ... yes, do real directory open

	lea	d3c_feof(a0),a0 	  ; copy most of top end
	lea	d3c_feof(a1),a1
	moveq	#(d3c_end-d3c_feof)/4-1,d0
dop_cchn
	move.l	(a0)+,(a1)+
	dbra	d0,dop_cchn

	move.l	d4,a0			 ; restore channel
	move.l	d3,a3			 ; and link

	jsr	dv3_posi		 ; set position to start of file
	bra.l	dop_link		 ; and link

dop_root
	bsr.s	dop_adir		 ; access directory
dop_open
	move.l	d4,a0			 ; restore channel
	move.l	d3,a3			 ; and link

	assert	d3c_sdsb,d3c_sdid-4,d3c_sdlen-8,d3c_sdent-$c
	lea	d3c_sdsb(a0),a1
	move.l	ddf_rdsb(a4),(a1)+	 ; this is a good start for slave block

	move.l	ddf_rdid(a4),(a1)+	 ; preset directory info
	move.l	ddf_rdlen(a4),(a1)+
	moveq	#-1,d0
	move.l	d0,(a1)

	st	d3c_denum+1(a0) 	 ; invalid directory entry number
	st	d3c_fenum+1(a0) 	 ; and formatted entry

	lea	d3c_qname(a5),a1	  ; set the pointer to the name

; Now we do a different open for each key

	moveq	#1,d0
	add.b	d3c_accs(a0),d0 	 ; access type
	add.b	dop_tab(pc,d0.w),d0
	jmp	dop_tab(pc,d0.w)

dop_tab
	dc.b	dop_del-*	; delete
	dc.b	dop_exc-*	; open exclusive
	dc.b	dop_shr-*	; open shared file
	dc.b	dop_new-*	; open new file
	dc.b	dop_ovr-*	; open overwrite file
	dc.b	dop_dir-*	; open directory

dop_adir		; file turns out to be directory - check access
	move.b	d3c_accs(a0),d0 	 ; is it delete?
	blt.s	dop_iu4 		 ; ... yes
	subq.b	#ioa.kovr,d0
	beq.s	dop_iu4
	move.b	#d3c.ro,d3c_ro(a0)	 ; set read only
	move.b	#ioa.kdir,d3c_accs(a0)	 ; set access type to directory
	rts

dop_iu4
	addq.l	#4,sp			 ; skip return
	bra	dop_fdiu

dop_trunc
	moveq	#-1,d5
	jsr	dv3_sbtr		 ; truncate slave blocks
	moveq	#0,d0
	jmp	ddf_strunc(a4)		 ; ... and allocation

dop_del
	moveq	#0,d2
	moveq	#0,d3
	assert	ddf.remove,-1
	st	d3
	jsr	ddf_drname(a4)		 ; search for name
	bne.s	dop_nfok		 ; ... not found is OK
	moveq	#0,d2			 ; truncate to nothing
	bsr	dop_trunc
	move.l	d0,d4			 ; save error
	moveq	#0,d0			 ; non-urgent flush
	jsr	ddl_fflush(a3)		 ; and do flush
	move.l	d4,d0
	bra.l	dop_exit

dop_nfok
	cmp.w	#err.fdnf,d0		 ; not found
	bne.l	dop_exit
dop_ok
	moveq	#0,d0
	bra.l	dop_exit

dop_exc
	moveq	#ddf.old,d3
	bra.s	dop_nopen		 ; normal open

dop_shr
	moveq	#ddf.old,d3
dop_nopen
	moveq	#-1,d2
	jsr	ddf_drname(a4)		 ; search for name
	bne.l	dop_exit
	tst.l	d3			 ; is it a directory?
	bpl.s	dop_schan		 ; ... no, set channel block
	bsr	dop_adir		 ; ... yes check access to directory
	bra.s	dop_sdir		 ; set directory channel block

dop_dir
	moveq	#-1,d2
	moveq	#ddf.dir,d3
	jsr	ddf_drname(a4)		 ; search for existing name
	bne.l	dop_exit		 ; ... oops

dop_sdir
	st	d3c_atype(a0)		 ; set directory access mode
	clr.l	d3c_fpos(a0)		 ; start at beginning
	move.l	d3c_flen(a0),d3c_feof(a0) ; end of file
	bra.s	dop_sch1

dop_new
	moveq	#0,d2
	moveq	#ddf.new,d3
	jsr	ddf_drname(a4)		 ; search for name
	beq.s	dop_snew
	bra.l	dop_exit

dop_ovr
	moveq	#0,d2
	moveq	#ddf.either,d3
	jsr	ddf_drname(a4)		 ; search for name
	bne.s	dop_exit
	tst.l	d3			 ; ... directory?
	bmi	dop_fdiu		 ; ... yes, in use

	moveq	#1,d2
	and.b	ddf_zalloc(a4),d2	 ; 0 for all, 1 for keep first
	bsr	dop_trunc		 ; truncate
	not.l	d3c_setmask(a0) 	 ; set all information on close

dop_snew
	move.l	ddf_fhlen(a4),d3c_flen(a0) ; set zero file length


; File is opened, set up channel block

dop_schan
	move.l	d3c_flen(a0),d3c_feof(a0) ; end of file
	jsr	dv3_posi		 ; initial position

dop_sch1
	lea	d3c_qname(a5),a1
	move.w	(a1)+,d0		 ; length of name
	move.w	d0,d3c_qname(a0)	 ; set in new block
	lea	d3c_name(a0),a2
	move.w	d0,(a2)+
	beq.s	dop_set 		 ; ... no name

	move.l	ddf_ptoi(a4),d3 	 ; translate table
	beq.s	dop_rsnt		 ; ... no translate

	exg	a3,d3
	moveq	#0,d1
dop_rsname
	move.b	(a1)+,d1
	move.b	(a3,d1.w),(a2)+ 	 ; translate
	subq.w	#1,d0
	bgt.s	dop_rsname

	move.l	d3,a3			 ; restore linkage
	bra.s	dop_set

dop_rsnt
	move.b	(a1)+,(a2)+		 ; copy
	subq.w	#1,d0
	bgt.s	dop_rsnt

dop_set
	assert	d3c_flid,d3c_drid-4,d3c_drnr-6,d3c_ddef-8
	movem.l d6/d7/a4,d3c_flid(a0)	    ; file ID / drive number / def block
dop_link
	move.l	ddf_chnlst(a4),d3c_link(a0) ; link in
	move.l	a0,ddf_chnlst(a4)

; Minerva rounds up heap block size if the allocation would result in
; a free block with 16 bytes. Therefore we need to calculate the size of
; the second heap block or the heap chain will sometimes get corrupted!
	move.l	#d3c_qdend,d4		; base block length
	move.l	(a0),d0 		; size of allocated heap block
	sub.l	d4,d0			; minus the base block
	move.l	d4,(a0) 		; only now overwrite size of base block
	move.l	d0,d3c_qdend(a0)	; and set remaining block length
	move.l	#d3c.qdnchk,d3c_qdnchk(a0)  ; and no check flag
	moveq	#0,d0

dop_exit
	move.l	d0,d4
	beq.s	dop_rchn		 ; OK, return channel
	tst.b	ddf_nropen(a4)		 ; bad, any files open?
	bne.s	dop_rchn		 ; ... yes, return channel
	jsr	ddl_done(a3)		 ; ... no, we've finished with drive

dop_rchn
	exg	a0,a5
	movem.l a0/a3/a6,-(sp)		 ; for QDOS compatibility...
	move.w	mem.rchp,a2
	jsr	(a2)			 ; return old channel
	movem.l (sp)+,a0/a3/a6
	exg	a0,a5

	assert	qlsd.minerva,1
	tst.b	qlsd_os(a3)		; Minerva?
	ble.s	dop_set_err		; ... no
; Only Minerva doesn't let us change the address in a0 as it "helpfully"
; restores it from the stack. In this case we need to be naughty and mess
; with the caller stack. Problem is, we MIGHT have been called through the
; DEV or another redirection device, so we don't know the exact stack location!
;
; Fortunately Minerva saves a0 with an offset, so we can stop once we find
; this pointer and not be irritated by other copies in between (which in the
; case of DEV will be fixed by DEV itself)
	moveq	#chn_end,d0		; Minerva saves a0 with this offset
	add.l	d0,a5			; original value
	add.l	a0,d0			; new value

	move.l	a7,a2			; scan stack
	moveq	#$30/2-1,d2		; scan this deep (DEV adds about $20)
dop_stack_scan
	cmp.l	(a2),a5 		; original value here?
	bne.s	dop_stack_next		; ... no, try next word
	move.l	d0,(a2) 		; set new channel ID!
	bra.s	dop_set_err
dop_stack_next
	addq.l	#2,a2
	dbf	d2,dop_stack_scan
dop_set_err
	move.l	d4,d0			; set error
dop_rts
	rts
	end
