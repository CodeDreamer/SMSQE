* File maintenance   V1.5     1985   Tony Tebby   QJUMP
*
* 2002-05-09  1.3  STAT now calculates big drives correctly (MK)
* 2018-07-24  1.4  Added FDEL (pjw)
* 2020-03-07  1.5  correct FDEL

	section exten
*
*	STAT [#channel,] [name] 		print drive statistics
*	DELETE name				delete file
*	FDEL name				delete file (error return)
*	COPY name [TO name]			copy (optional header)
*	COPY_O name [TO name]			as copy but overwrites
*	COPY_N name [TO name]			copy no header
*	COPY_H name [TO name]			copy with header
*	RENAME name TO name			rename file
*
	xdef	stat
	xdef	stat_do
	xdef	stat_dos
	xdef	delete
	xdef	fdel
	xdef	copy
	xdef	copy_o
	xdef	copy_n
	xdef	copy_h
	xdef	rename
*
	xref	dec_cstrg
	xref	ut_winset		get channel default #1
	xref	ut_par2 		check for two parameters
	xref	ut_par1 		check for one parameter
	xref	ut_fopnp		open, prompt
	xref	ut_gtnm1		get one name
	xref	ut_gxnm1		get exactly one name
	xref	ut_opdefx		open with default
	xref	ut_opmds		open destination
	xref	ut_fclos		close a file
	xref	ut_copy 		copy a file
	xref	ut_fdire		read (directory entry) into buffer
	xref	ut_msexq		exists and query
	xref	ut_messg		write message
	xref	ut_wrtst		write string of bytes
	xref	ut_trp3r		trap #3 relative
	xref	ut_rtfd1
*
	include dev8_sbsext_ext_keys
	include dev8_sbsext_ut_opdefx_keys
	include dev8_keys_qdos_io
	include dev8_dv3_keys
*
stat
	bsr.l	ut_winset		get output channel
	bne.s	mnt_rt.1		... oops
	move.l	a0,a4			and save it
	moveq	#io.dir,d3		set up directory open
	cmp.l	a3,a5			check if there are any parameters
	beq.s	stat_null		... yes
	bsr.l	ut_par1 		must be one parameter
	bsr.l	ut_fopnp		open file
	bra.s	stat_opn
stat_null
	sub.l	a1,a1			no file name
	moveq	#uod.datd,d2
	jsr	ut_opdefx		a data directory
stat_opn
mnt_rt.1
	bne.l	mnt_rts
	move.l	a0,a3			save channel ID
	bsr.s	stat_do 		write out statistics
	bra.l	mnt_cla3

st_format dc.b	  2
null	dc.b	null-*,QDOS-*,MSDOS-*
qdos	dc.b	4,'QDOS'
msdos	dc.b	5,'MSDOS'
st_density dc.w   4
	dc.w	'SDDDHDED'
*
stat_do
	move.l	bv_bfbas(a6),d4 	base address kept
	moveq	#iof.xinf,d0		extended info
	moveq	#0,d1
	bsr.l	ut_fdire		... into basic buffer
	bne.s	stat_old
	move.l	ioi_free(a6,a1.l),d1	     allocation
	move.l	ioi_totl(a6,a1.l),d2
	move.b	ioi_allc(a6,a1.l),d0	     units
	lsr.w	#1,d0			was in bytes, now in sectors
;	 mulu	 d0,d1			16b * 16b is not enough
;	 mulu	 d0,d2			exchanged with 32b * 16b
	move.l	d3,-(sp)
	move.w	d1,d3			d0 * d1
	swap	d1
	mulu	d0,d1
	swap	d1
	mulu	d0,d3
	add.l	d3,d1

	move.w	d2,d3			d0 * d2
	swap	d2
	mulu	d0,d2
	swap	d2
	mulu	d0,d3
	add.l	d3,d2
	move.l	(sp)+,d3

	move.l	ioi_ftyp(a6,a1.l),d0	     flags

	add.l	a6,a1
	add.w	(a1)+,a1
	addq.l	#2,d4			start of name

	subq.b	#ddl.flp,d0
	beq.s	sts_ftype
	not.w	d0			not floppy, blat type
sts_ftype
	and.l	#$ff00ff00,d0		only format and density are of interest
	rol.l	#8,d0			format type
	lea	st_format,a0
	cmp.b	(a0)+,d0
	bhi.s	stat_set
	move.b	(a0,d0.w),d0
	add.w	d0,a0
	move.b	(a0)+,d0
	move.b	#' ',(a1)+
	bra.s	stat_fsle
stat_fsloop
	move.b	(a0)+,(a1)+		copy format name
stat_fsle
	dbra	d0,stat_fsloop

	swap	d0
	lea	st_density,a0
	cmp.w	(a0)+,d0		in range?
	bhs.s	stat_set
	add.w	d0,d0
	add.w	d0,a0
	move.b	#' ',(a1)+
	move.b	(a0)+,(a1)+		set density
	move.b	(a0)+,(a1)+
	bra.s	stat_set

stat_old
	moveq	#fs.mdinf,d0		get medium information
	bsr.l	ut_fdire		... into basic buffer
	bne.s	mnt_rts 		oops
	moveq	#0,d2
	move.w	d1,d2
	clr.w	d1
	swap	d1
	lea	10(a6,a1.l),a1		set buffer pointer to end
stat_set
	move.b	#$0a,(a1)+		put in newline
	sub.l	a6,a1
stat_dos
	move.l	d2,-(sp)
	bsr.s	stat_cn 		and convert
	move.l	(sp)+,d1
	move.b	#'/',(a6,a1.l)		set solidus
	addq.l	#1,a1
	bsr.s	stat_cn 		and convert other bit
	move.l	a1,d2			find out how many bytes
	move.l	d4,a1
	sub.l	a1,d2
	move.l	a4,a0			set screen channel
	bsr.l	ut_wrtst		and send the string of bytes
*
	moveq	#ms.sctrs,d0
	bra.l	ut_messg		send the sectors message
*
stat_cn
	movem.l d4-d7,-(sp)
	moveq	#0,d4			positive no sign
	moveq	#0,d5			create a string of file length
	moveq	#10,d6			field of 10
	moveq	#$7f,d7 		no commas
	bsr.l	dec_cstrg
	add.l	a1,d6
	sub.l	a2,d6			length of converted string
	bra.s	stat_cne
stat_cnl
	move.b	(a6,a2.l),(a6,a1.l)
	addq.l	#1,a1
	addq.l	#1,a2
stat_cne
	dbra	d6,stat_cnl
	movem.l (sp)+,d4-d7
	rts

; this is put here instead of further down to keep the bra in line 266 (just above rename) short
fdel	bsr.s	do_delete
	move.l	d0,d1			(d1 gets changed by ut_fopnp ->ut_opdefx)
	bra.l	ut_rtfd1		... return error code

*
mnt_cla3
	move.l	a3,a0			close source
mnt_clos
	bra.l	ut_fclos		close file
*
delete
	bsr.s	do_delete
	moveq	#err.nf,d4		is it error not found?
	cmp.l	d0,d4
	bne.s	mnt_rts 		... no
mnt_ok
	moveq	#0,d0
mnt_rts
	rts
	      
do_delete
	bsr.l	ut_par1 		check just one parameter
	moveq	#0,d3			set delete flag
	not.w	d3
	bra.l	ut_fopnp		and open data default
*
* various COPYs
*
copy_o
	moveq	#1,d5			set optional header
	moveq	#io.overw,d7		and overwrite
	bra.s	copy_opn
copy_n
	moveq	#0,d5			set no header
	st	d5
	bra.s	copy_new
copy_h
	moveq	#0,d5			set header
	bra.s	copy_new
copy
	moveq	#1,d5			set optional header
copy_new
	moveq	#io.new,d7		set new output
copy_opn
	bsr.l	ut_gtnm1		get one name
	bne.s	mnt_rts
	move.l	a1,bv_rip(a6)		... and save it
	moveq	#io.share,d3
	moveq	#uod.datd,d2
	jsr	ut_opdefx		and open data default
	bne.s	mnt_rts
	addq.l	#8,a3			move to next parameter
	cmp.l	a3,a5			last one?
	bne.s	copy_2			... no
	move.l	a0,a3			save source ID
	move.l	bv_rip(a6),a1		restore pointer
	move.l	d7,d3
	jsr	ut_opmds(pc)		open destination
	bra.s	copy_op2
copy_2
	move.l	a0,-(sp)
	bsr.l	ut_gxnm1		get exactly one name
	move.l	(sp)+,a3
	bne.s	mnt_rts 		... oops
	move.l	d7,d3
	moveq	#uod.datd+uod.prmt,d2
	jsr	ut_opdefx		open destination

copy_op2
	bge.l	ut_copy 		OK
	moveq	#err.ex,d3
	cmp.l	d3,d0			... exists?
	bne.s	mnt_cla3		... no
	moveq	#0,d0			no error, is this a goot idea?
	bra.s	mnt_cla3		bad
*
*
rename
	bsr.l	ut_par2 		must be two params
	moveq	#io.old,d3
	bsr.l	ut_fopnp		open source file
	bne.s	mnt_rts
	addq.l	#8,a3
	move.l	a0,d6			save ID
	moveq	#io.dir,d3		get full name
	bsr.l	ut_fopnp		of second bit
	bne.s	ren_do
	move.l	a1,-(sp)		save file name pointer
	moveq	#io.close,d0		and close file
	trap	#2
	move.l	(sp)+,a1
ren_do
	moveq	#fs.renam,d0		rename
	move.l	d6,a0
	bsr.l	ut_trp3r
	bra.l	ut_fclos		and close file
	end
