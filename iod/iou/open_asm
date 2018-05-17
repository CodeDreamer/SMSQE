; Open Operations   V2.07     1989  Tony Tebby   QJUMP

	section iou

	xdef	iou_open
	xdef	iou_opfl

	xref	iou_smlw
	xref	iou_fde0
	xref	iou_sde0
	xref	iou_sden
	xref	iou_fnam
	xref	iou_ckde

	xref	iou_dirshr

	include 'dev8_keys_err'
	include 'dev8_keys_sys'
	include 'dev8_keys_chn'
	include 'dev8_keys_iod'
	include 'dev8_keys_hdr'
	include 'dev8_keys_qdos_ioa'

	include 'dev8_mac_assert'
;+++
; Internal OPEN call from first IO of QDOS opened shared file or from rename.
;
;	d1-d7 / a0-a3 / a6 all preserved
;
;	a0 c  p channel base address
;	a2 c  p (old channel iof.rnam only)
;	a3 c  p linkage block address
;	a4  r	pointer to physical definition
;	a5  r	pointer to map
;
;	status return standard
;---
iou_opfl
reg.opfl reg	d1-d7/a0/a1/a2
stk_chan equ	$1c+4 ; include return address
stk_ochn equ	$24+4
	movem.l reg.opfl,-(sp)
	moveq	#0,d6			 ; get drive id
	move.b	chn_drid(a0),d6
	lsl.w	#2,d6  
	lea	sys_fsdd(a6),a4 	 ; drive table
	move.l	(a4,d6.w),a4		 ; drive definition
	bsr.s	iou_opdo		 ; open
	movem.l (sp)+,reg.opfl
	rts

;+++
; General purpose filing system device OPEN routine
;
;	d1-d5 scratch
;	d6   s	drive number / file id
;	d7   s
;	a0 c  p channel base address
;	a1 c s	pointer to physical definition
;	a2   s	internal buffer address
;	a3 c  p linkage block address
;	a4  r	pointer to physical definition
;	a5  r	pointer to map
;---
iou_open
	move.l	a1,a4
	move.b	chn_drid(a0),iod_drid(a4) ; keep drive ID

iou_opdo
	tst.b	iod_ftyp(a4)		 ; direct sector?
	blt.l	iop_fiu 		 ; ... yes, in use

	moveq	#0,d6			 ; get drive number
	move.b	iod_dnum(a4),d6
	swap	d6			 ; in msw
	move.l	a4,chn_ddef(a0) 	 ; set definition pointer
	move.l	iod_map(a4),a5
	jsr	iod_ckop(a3)		 ; check drive for open
	move.l	a5,iod_map(a4)

	tst.l	d0
	beq.s	iop_open		 ; OK
	blt.l	iop_mchk		 ; ... bad

	tst.b	chn_accs(a0)		 ; ... direct sector, delete?
	blt.l	iop_rtok		 ; ... yes!!!

	tst.b	iod_nrfl(a4)		 ; files open?
	bne.l	iop_fiu 		 ; ... yes

	not.b	iod_ftyp(a4)		 ; set format type negative
	move.b	#ioa.kexc,chn_accs(a0)	 ; exclusive
	assert	chn_drnr,chn_flid-2
	move.l	d6,chn_drnr(a0) 	 ; drive / sector length
	move.w	#128,d0
	lsl.w	d6,d0			 ; actual sector length
	move.w	d0,iod_sctl(a4)
	bra.l	iop_rtok
	
iop_open
	tst.b	iod_wprt(a4)		 ; write protected?
	beq.s	iop_opdo		 ; ... no
	cmp.b	#ioa.kdir,chn_accs(a0)	 ; ... yes, directory?
	beq.s	iop_opdo
	cmp.b	#ioa.kshr,chn_accs(a0)	 ; ... yes, check it
	bhi.l	iop_rdon		 ; ... oops, read only
iop_opdo
	move.w	iod_rdid(a4),d6 	 ; get root directory
	assert	chn_drnr,chn_flid-2
	move.l	d6,chn_drnr(a0) 	 ; drive / file ID 
	move.l	iod_rdln(a4),chn_feof(a0) ; and length

	moveq	#0,d3			 ; no directory
	moveq	#0,d4

	tst.w	chn_name(a0)		 ; any name?
	beq.l	iop_ckdr		 ; ... no, check for directory

	jsr	iou_fnam
	move.l	d0,d2			 ; d2 <0 if new file
	beq.l	iop_exst		 ; ... it exists
	cmp.b	#err.fdnf,d0		 ; not found?
	bne.l	iop_d0

; file not found: was it delete, new or overwrite

	move.b	chn_accs(a0),d1
	blt.l	iop_rtok		 ; ... done
	cmp.b	#ioa.kovr,d1		 ; overwrite?
	beq.s	iop_create		 ; ... yes
	cmp.b	#ioa.knew,d1		 ; new?
	beq.s	iop_create
	cmp.b	#ioa.kdir,d1		 ; directory?
	beq.l	iop_dir 		 ; ... yes
	cmp.b	#ioa.krnm,d1		 ; rename?
	bne.l	iop_d0			 ; ... no, oops
iop_rnam
	move.l	stk_ochn(sp),a2 	 ; old channel
	lea	chn_name(a0),a1 	 ; new name
	lea	chn_name(a2),a2 	 ; old name
	move.w	(a1)+,d1
	move.w	d1,(a2)+
iop_cnew
	move.b	(a1)+,(a2)+		 ; set new name in old channel
	subq.w	#1,d1
	bgt.s	iop_cnew

	lea	chn_opwk(a0),a1 	 ; channel working area
	move.l	stk_ochn(sp),a0 	 ; old channel
	cmp.w	chn_sdid(a0),d6 	 ; same sub-directory?
	beq.s	iop_rnfl		 ; ... yes, just rename file

	moveq	#hdr.len,d7
	jsr	iou_fde0		 ; fetch the old directory entry from 0
	lea	hdr_name+chn_opwk+chn.nmln+2(a0),a1
	moveq	#chn.nmln+1,d1
iop_clnm
	clr.b	-(a1)			 ; clear out the old name
	dbra	d1,iop_clnm
	move.w	chn_flid(a0),d1 	 ; old file id
	jsr	iod_alfs(a3)
	move.w	d1,chn_flid(a0) 	 ; new file ID (in old channel)
	move.l	stk_chan(sp),a0 	 ; new channel
	moveq	#hdr.len,d7
	bra.s	iop_newnm

iop_rnfl
	moveq	#hdr_name,d4		 ; reset name in directory
	moveq	#hdr.nmln+2,d7
	lea	chn_name(a0),a1
	jmp	iou_sden

iop_create
	bsr.l	iop_clear		 ; clear spare bit
	moveq	#0,d1
	jsr	iod_alfs(a3)		 ; allocate first sector
	bne.l	iop_d0
	move.w	d1,chn_flid(a0) 	 ; keep id
	st	chn_updt(a0)		 ; and mark updated

iop_newent
	move.l	d7,hdr_flen+chn_opwk(a0) ; set length
iop_newnm
	move.w	d1,hdr_flid+chn_opwk(a0) ; set file id
	lea	chn_name(a0),a2 	 ; copy name into spare
	lea	chn_opwk+hdr_name(a0),a1
	move.w	(a2)+,d1
	move.w	d1,(a1)+
iop_copy
	move.b	(a2)+,(a1)+		 ; copy name
	subq.w	#1,d1
	bgt.s	iop_copy

	tst.l	d5			 ; new entry?
	sge	d2			 ; ... keep no
	bge.s	iop_idset		 ; ... no
	move.l	chn_feof(a0),d5 	 ; yes, put at end of file
iop_idset
	lea	chn_opwk(a0),a1
	jsr	iou_smlw		 ; set directory entry
	bne.s	iop_drerr
	tst.b	d2			 ; longer?
	bne.s	iop_bspc		 ; ... no
	move.l	d5,chn_feof(a0) 	 ; new length
	tst.w	d4			 ; root directory?
	beq.s	iop_srdl		 ; ... yes
	move.l	d3,chn_sdps(a0) 	 ; ... no, update directory above
	move.w	d4,chn_sdid(a0)
	lea	chn_spr(a0),a1
	move.l	d5,(a1)
	moveq	#4,d7			 ; a long word
	jsr	iou_sde0		 ; set one directory level up
	bne.s	iop_d0			 ; ... oops
	bra.s	iop_bspc

iop_srdl
	move.l	d5,iod_rdln(a4) 	 ; set root directory length

iop_bspc
	moveq	#hdr.len,d1
	sub.l	d1,d5			 ; backspace to start of entry
	moveq	#0,d1
	cmp.b	#ioa.krnm,chn_accs(a0)	 ; was it rename?
	bne.l	iop_seof		 ; ... no
	bsr.l	iop_clear		 ; ... yes, clear spare bit (in new!)
	move.l	stk_ochn(sp),a0
	jsr	iou_sde0
	bne.s	iop_d0
	bsr.l	iop_done		 ; and set new sub-directory pointers
	move.l	chn_feof(a0),d1 	 ; end of file
	move.l	stk_chan(sp),a0
	bra.l	iop_donf

; failure to set directory entry: if new file, scrub it

iop_drerr
	tst.b	d2			 ; was it trying to extend dir?
	bne.s	iop_d0			 ; ... no
	cmp.b	#ioa.krnm,chn_accs(a0)	 ; was it rename?
	beq.s	iop_d0
	move.l	d0,-(sp)
	moveq	#hdr.len,d1		 ; length of file
       bsr.l   iop_trall		; ... no, truncate the file
	move.l	(sp)+,d0
iop_d0
	tst.l	d0
	rts

; file exists, ok?

iop_exst
	bclr	#31,d4			 ; is it a directory?
	beq.s	iop_exck		 ; ... no
	move.b	chn_accs(a0),d1 	 ; delete directory?
	blt.l	iop_dldr		 ; ... yes

iop_ckdr
	move.b	chn_accs(a0),d1 	 ; access mode
	blt.l	iop_rtok		 ; ... delete, ignore root directory
	cmp.b	#ioa.kdir,d1		 ; open directory?
	beq.s	iop_dir 		 ; ... yes, OK
	assert	ioa.kexc,ioa.kshr-1,0
	subq.b	#ioa.kshr,d1		 ; open existing file?
	bgt.l	iop_fex 		 ; ... no, in use
	move.b	#ioa.kdir,chn_accs(a0)	 ; ... yes, set to directory
	bra.s	iop_dir

iop_exck
	cmp.b	#ioa.kdir,chn_accs(a0)	 ; open directory?
	beq.s	iop_dir
	move.w	hdr_flid+chn_opwk(a0),chn_flid(a0) ; set file ID
	tst.b	iod_ftyp(a4)		 ; floppy disk?
	bne.s	iop_sacc
	cmp.w	#iod.rdid,d6		 ; in root-directory?
	bne.s	iop_sacc		 ; ... no
	move.l	d5,d1			 ; OLD STYLE QDOS has implicit ID
	lsr.l	#6,d1			 ; based on position
	addq.w	#1,d1
	move.w	d1,chn_flid(a0)
iop_sacc
	move.b	chn_accs(a0),d1 	 ; access mode
	blt.l	iop_delete		 ; ... delete
	cmp.b	#ioa.kovr,d1		 ; overwrite
	beq.s	iop_over
	cmp.b	#ioa.knew,d1		 ; new?
	beq.s	iop_fex 		 ; ... yes, oops
	cmp.b	#ioa.krnm,d1		 ; rename?
	beq.s	iop_fex
	moveq	#-hdr.len,d1		 ; set real length $$$$ QDOS
	add.l	hdr_flen+chn_opwk(a0),d1

iop_seof
	add.l	iod_hdrl(a4),d1 	 ; find end of file
iop_donf
	move.l	chn_feof(a0),chn_sdef(a0) ; sub-directory end of file
	move.l	d1,chn_feof(a0) 	 ; set end of file
	bra.s	iop_done

iop_dir
	move.w	chn_qdid(a0),chn_name(a0) ; set directory name length
	move.w	iou_dirshr,d0		 ; directory shareable in this driver
	eor.w	d0,chn_name(a0) 	 ; blat name
	move.w	d6,chn_flid(a0) 	 ; id is directory
	move.l	d3,d5			 ; directory is one level up
	move.l	d4,d6
	clr.l	chn_sdef(a0)		 ; do not know the length

iop_done
	move.w	d6,chn_sdid(a0) 	 ; set sub-directory ID
	move.l	d5,chn_sdps(a0) 	 ; and file position
iop_rtok
	moveq	#0,d0
iop_rts
	rts
iop_fex
	moveq	#err.fex,d0
	rts

iop_mchk
	cmp.l	chn_feof(a0),d0 	 ; real bad medium?
	beq	iop_d0
	moveq	#err.fdnf,d0
	rts

iop_fiu
	moveq	#err.fdiu,d0
	rts
iop_rdon
	moveq	#err.rdo,d0
	rts

iop_over
	movem.l d3-d6,-(sp)
	move.l	iod_hdrl(a4),d5
	move.l	chn_feof(a0),-(sp)	 ; save sub directory length
	move.l	hdr_flen+chn_opwk(a0),chn_feof(a0) ; file length to truncate
	bsr.s	iop_trnc		 ; truncate file
	move.l	(sp)+,chn_feof(a0)
	movem.l (sp)+,d3-d6

	bsr.s	iop_clear		 ; clear spare area
	move.w	chn_flid(a0),d1 	 ; keep file ID
	bra.l	iop_newent		 ; and create new entry

iop_dldr
	jsr	iou_ckde		 ; check if directory empty
	bne.s	iop_rts 		 ; ... no, do not delete
	move.w	d4,d6			 ; reset pointers to directory
	move.l	d3,d5
	move.l	chn_feof(a0),-(sp)
	move.b	#$7f,chn_feof(a0)	 ; dummy end of file for set directory
	bra.s	iop_delent

iop_delete
	move.l	hdr_flen+chn_opwk(a0),-(sp); file length to truncate
iop_delent
	bsr.s	iop_clear		 ; clear spare area
	bsr.l	iou_smlw		 ; set directory entry
	move.l	(sp)+,chn_feof(a0)
iop_trall
	moveq	#-1,d5			 ; truncate all sectors
iop_trnc
	move.w	chn_flid(a0),d6
	jmp	iod_trnc(a3)
	page

; set header length and clear spare area

iop_clear
	moveq	#hdr.len,d7		 ; set header length
	lea	chn_opwk(a0),a1 	 ; clear spare area
	moveq	#hdr.len/4-1,d1
iop_cloop
	clr.l	(a1)+	  
	dbra	d1,iop_cloop
	sub.w	d7,a1
	rts
	end
