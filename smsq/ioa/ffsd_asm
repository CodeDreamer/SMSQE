* Find filing system device   V2.03    1986   Tony Tebby  QJUMP
*
* 2004-09-21  2.02  check that drive number <= 8 (wl)
* 2005-11-17  2.03  try to release DDB if table is full (MK)
*
	section ioa
*
	xdef	ioa_ffsd
*
	xref	ioa_ckch
	xref	mem_achp
	xref	mem_rchp
	xref	cv_cttab
*
	include dev8_keys_err
	include dev8_keys_sys
	include dev8_keys_iod
	include dev8_keys_chn
	include dev8_keys_k
*
*	d0	error return
*	d1   s
*	d2   s
*	d5  r	drive number
*	d6  r	drive ID
*	d7  r	length of remains of name
*	a0 c  p
*	a1 cr	name / filename
*	a2  r	physical definition
*	a3  r	base of iod linkage block
*	a4  s
*	a5  r	iod linkage
*	a6 c  p base of system vars
*
*	all other registers preserved
*
ioa_ffsd
	move.l	a0,-(sp)
	move.w	(a1)+,d7		length of name
	ble.l	iff_nf			... none
*
	move.l	a1,a3			save start of characters
	move.w	d7,d6			count letters in device name
	lea	cv_cttab(pc),a2 	table of character types
	moveq	#0,d1
*
iff_cklt
	move.b	(a1)+,d1		next character
	cmp.b	#k.lclet,(a2,d1.w)	letter?
	beq.s	iff_ltf
	cmp.b	#k.uclet,(a2,d1.w)
	bne.s	iff_cknr		... no, check the number
iff_ltf
	subq.w	#1,d7			one fewer in file name
	bgt.s	iff_cklt		... ok
	bra.l	iff_nf
*
iff_cknr
	sub.w	d7,d6			d6 now device name char count
	beq.l	iff_nf			... none
	cmp.b	#k.dig09,(a2,d1.w)	digit?
	bne.l	iff_nf
	sub.b	#'0',d1 		0?
	beq.l	iff_nf
	move.w	d1,d5			drive number
**
	cmp.b	#8,d1			but not more than 8!!!!
	bhi	iff_nf
**
	cmp.b	#'_',(a1)+		then underscore?
	bne.l	iff_nf
	subq.w	#2,d7			two fewer characters
	blt.l	iff_nf			... not a file name
	cmp.w	#chn.nmln,d7		name too long?
	bhi.l	iff_bn			... bad name
	move.l	a1,a2			save name pointer
*
* check device name against all fsd drivers
*
	lea	sys_fsdl(a6),a5
iff_fdrv
	move.l	(a5),d0 		next driver
	beq.l	iff_nf
	move.l	d0,a5
	lea	iod_dnus-iod_iolk(a5),a4 drive name
	cmp.w	(a4)+,d6		the right length?
	bne.s	iff_fdrv		... no
	move.w	d6,d0			name length
	move.l	a3,a0			comparison name
	cmp.w	d0,d0			set Z
	bsr.l	ioa_ckch		check characters
	bne.s	iff_fdrv
*
* the right drive type has been found, check if physical definition set up
*
	moveq	#sys.nfsd,d6		number of file definitions
	lea	sys_fsdt(a6),a3 	scan from top down
	moveq	#-1,d1			set no hole
iff_fphy
	subq.w	#1,d6			drive 'ID'
	blt.s	iff_newp		... new definition required
	move.l	-(a3),d0		next drive
	bne.s	iff_cphy		... there is one
	move.w	d6,d1			keep lowest empty
	move.l	a3,d2			and address of it
	bra.s	iff_fphy
iff_cphy
	move.l	d0,a0			check
	cmp.l	iod_drlk(a0),a5 	... driver
	bne.s	iff_fphy
	cmp.b	iod_dnum(a0),d5 	... and number
	bne.s	iff_fphy
	bra.s	iff_done		all done
*
iff_newp
	move.w	d1,d6			new physical definition ID
	move.l	d2,a3			at this position
	bpl.s	iff_alnewp		... possition available
* MK: Table is full. We will try to release an old entry. The first scan will
* look for an entry without open files and no name. It's probably an
* abandoned drive block.
	moveq	#sys.nfsd,d6
	lea	sys_fsdt(a6),a3 	top down
iff_scanloop
	subq.w	#1,d6
	blt.s	iff_aggscan		none found, do aggressive scan
	move.l	-(a3),a0		next drive
	tst.b	iod_nrfl(a0)		open files?
	bne.s	iff_scanloop		yes, don't use that
	move.l	iod_mnam(a0),d0 	check for empty name
	or.l	iod_mnam+4(a0),d0
	or.w	iod_mnam+8(a0),d0
	bne.s	iff_scanloop		is not empty, scan further
	bra.s	iff_releaseold

* Second, more aggressive scan for any block with no open files
iff_aggscan
	moveq	#sys.nfsd,d6
	lea	sys_fsdt(a6),a3 	top down
iff_aggscanloop
	subq.w	#1,d6
	blt.s	iff_tf			okay, we really need to give up now
	move.l	-(a3),a0		next drive
	tst.b	iod_nrfl(a0)		open files?
	bne.s	iff_aggscanloop 	yes, don't use that

iff_releaseold
	bsr.l	mem_rchp		return old space
	clr.l	(a3)			space now available again
*
iff_alnewp
	move.l	iod_plen-iod_iolk(a5),d1 length to allocate
	bsr.l	mem_achp		allocate some space
	bne.s	iff_exit		... oops
	move.l	a0,(a3) 		set new id in table
*
	move.l	a5,iod_drlk(a0) 	set drive linkage
	move.b	d5,iod_dnum(a0) 	and number
	move.b	d6,iod_drid(a0) 	and ID
	moveq	#0,d0
*
iff_done
	move.l	a2,a1
	move.l	a0,a2			get phys def and name in right regs
	lea	-iod_iolk(a5),a3	set linkage base
iff_exit
	move.l	(sp)+,a0
	rts
*
iff_nf
	moveq	#err.fdnf,d0		set file not found
	bra.s	iff_exit
iff_bn
	moveq	#err.inam,d0		set invalid name
	bra.s	iff_exit
iff_tf
	moveq	#err.imem,d0		set insufficient memory
	bra.s	iff_exit

	end
