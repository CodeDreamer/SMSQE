; DV3 MSDOS Format Set Directory Entry	  V3.00 	  1993 Tony Tebby

	section dv3

	xdef	msd_drsfile
	xdef	msd_deloc
	xdef	msd_drupd

	xref	msd_setmu
	xref	dv3_drloc
	xref	dv3_drupd

	include 'dev8_keys_dos'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_msd_keys'
	include 'dev8_mac_xword'

;+++
; DV3 MSDOS Format Set Directory Entry For File
;
;	d6 c  p file ID
;	d7 c  p drive ID / number
;	a0 c  p pointer to channel block
;	a2    p
;	a3 c  p pointer to linkage
;	a4 c  p pointer to drive definition
;
;	status return standard (not recognised)
;
;---
msd_drsfile
msf.reg reg	d2/d5/a1/a2
	movem.l msf.reg,-(sp)
	bsr.s	msd_deloc
	bne.s	msf_exit

	move.b	d3c_dsattr(a0),dos_attr(a2) ; copy attributes
	move.l	d3c_updd(a0),d0
	xlong	d0
	move.l	d0,dos_updt(a2) 	 ; copy date
	move.l	d3c_feof(a0),d0
	xlong	d0
	move.l	d0,dos_flen(a2) 	 ; copy length
	move.w	d6,d0
	ror.w	#8,d0
	move.w	d0,dos_clus(a2) 	 ; and ID

	cmp.b	#1,d3c_type(a0) 	 ; executable?
	bne.s	msf_drupd		 ; ... no

	move.l	d3c_data(a0),d2
	beq.s	msf_drupd		 ; no data

	subq.l	#1,d2
	lsr.l	#3,d2			 ; 8 to 512k in a word	 (512 -> 3f)
	swap	d2
	tst.w	d2			 ; mare than 512k?
	bne.s	msf_drsetd
	swap	d2
	lsr.w	#5,d2			 ; 512 -> 3f -> 1

	moveq	#'0'-1,d0

msf_drsetd
	addq.b	#1,d0
	lsr.w	#1,d2			 ; set up the length
	bne.s	msf_drsetd

	move.w	#'EX',dos_extn(a2)	 ; set special extension
	move.b	d0,dos_extn+2(a2)	 ; with data size

msf_drupd
	bsr.s	msd_drupd
	moveq	#0,d0
msf_exit
	movem.l (sp)+,msf.reg
	rts

;+++
; DV3 MSDOS Format Locate Directory Entry For File
; If it is in the root, d3c_sdsb(a0) is cleared
; Otherwise, d3c_sdsb(a0) is set to the current slave block
;
;	d2    s
;	d5    s
;	d7 c  p drive ID / number
;	a0 c  p pointer to channel block
;	a2  r	pointer to directory entry
;	a3 c  p pointer to linkage
;	a4 c  p pointer to drive definition
;
;	status return standard (not recognised)
;
;---
msd_deloc
	move.l	d3c_sdent(a0),d2	 ; our own entry

	cmp.l	#ddf.rdid,d3c_sdid(a0)	 ; root directory?
	bne.s	mdl_gbuf		 ; ... no, get buffer

	lsl.l	#dos.dres,d2
	move.l	mdf_rdir(a4),a2
	add.l	d2,a2			 ; position in root
	moveq	#0,d0
	move.l	d0,d3c_sdsb(a0)
	rts

mdl_gbuf
	addq.l	#2,d2			 ; skip dummy entries
	move.l	d2,d5
	moveq	#7-dos.dres,d0		 ; header to sector
	add.b	ddf_slflag(a4),d0	 ; + sector length flag
	lsr.l	d0,d5			 ; header sector
	lsl.w	#dos.dres,d2
	and.w	ddf_smask+2(a4),d2	 ; byte within sector

	jsr	dv3_drloc		 ; locate sector
	add.w	d2,a2			 ; start of data
	rts

;+++
; DV3 MSDOS Format Mark Directory Entry For File Updated
; If d3c_sdsb(a0) is clear, the root directory sector is located using a2
; Otherwise, d3c_sdsb(a0) is taken to be the current directory slave block
;
;	d0	preserved
;	d7 c  p drive ID / number
;	a0 c  p pointer to channel block
;	a2 c  p pointer within root directory entry
;	a3 c  p pointer to linkage
;	a4 c  p pointer to drive definition
;
;	status return standard (not recognised)
;
;---
msd_drupd
	tst.l	d3c_sdsb(a0)
	bne.l	dv3_drupd		 ; directory sector updated

	move.l	a1,-(sp)
	jsr	msd_setmu		 ; set map updated
	sub.l	a1,a1
	jsr	ddl_slbupd(a3)		 ; and tell the device dependent routines
	move.l	(sp)+,a1
	rts
	end
