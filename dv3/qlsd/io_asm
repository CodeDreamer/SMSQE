; DV3 IO routines		V3.03	        1992 Tony Tebby
;
; 2020-03-07  3.01  Modifications for correct medium name (xinf call) (MK+wl)
; 2020-04-26  3.02  Removed support for DOS timestamps to save space (MK)
; 2020-05-07  3.03  Fixed io.minf to fill the name with spaces (MK)

	section dv3

	xdef	dv3_io
	xdef	dv3_ionb

	xdef	dv3_rhdr
	xdef	dv3_flsh
	xdef	dv3_flhdr

	xdef	dv3_ckro

	xdef	dv3_minf
	xdef	dv3_xinf

	xref	dv3_dir
	xref	dv3_dsect

	xref	dv3_posa
	xref	dv3_posr

	xref	dv3_tbyt
	xref	dv3_fbyt
	xref	dv3_sbyt
	xref	dv3_fmul
	xref	dv3_flin
	xref	dv3_smul
	xref	dv3_mkdr
	xref	dv3_rename
	xref	dv3_date
	xref	dv3_albf
	xref	dv3_lcbf
	xref	dv3_sbtr
	xref	dv3_slbupd

	xref	dv3_iqdt

;;;	   xref    cv_rtdos
;;;	   xref    cv_dosrt

	include 'dev8_dv3_keys'
	include 'dev8_keys_err'
	include 'dev8_keys_hdr'
	include 'dev8_keys_qdos_io'
	include 'dev8_mac_assert'
;+++
; General purpose IO routine
;
;	d0 c	operation
;	d1 cr	amount transferred / byte / position etc.
;	d2 c	buffer size
;	d4   s	file pointer
;	d5   s	file sector
;	d6   s	file id
;	d7   s	drive number
;	a0 c	channel base address
;	a1 cr	buffer address
;	a2   s	internal buffer address
;	a3 c  p linkage block address
;	a4   s	pointer to physical definition
;---
dv3_io
dv3_ionb
	assert	d3c_flid,d3c_drid-4,d3c_drnr-6,d3c_ddef-8
	movem.l d3c_flid(a0),d6/d7/a4	 ; file ID / drive number / def block
	move.l	ddf_ptddl(a4),a3	 ; set linkage

	tst.b	d3c_atype(a0)		 ; access type?
	beq.s	io_file 		 ; normal file IO
	bmi.l	dv3_dir 		 ; directory IO
	bra.l	dv3_dsect		 ; direct sector IO

io_file
	cmp.w	#iof.xinf,d0		 ; in range?
	bhi.s	io_ipar 		 ; ... no

dv3_setj
	subq.w	#iob.smul+1,d0		 ; byte io?
	blo.s	io_jump 		 ; ... yes
	sub.w	#iof.chek-(iob.smul+1),d0 ; file io?
	blo.s	io_ipar 		 ; ... no

io_jump
	add.w	d0,d0
	add.w	io_tab(pc,d0.w),d0
	jmp	io_tab(pc,d0.w)

io_ipar
	moveq	#err.ipar,d0
	rts

	dc.w	dv3_tbyt-*
	dc.w	dv3_fbyt-*
	dc.w	io_flin-*
	dc.w	io_fmul-*
	dc.w	io_ipar-*
	dc.w	dv3_sbyt-*
	dc.w	io_smul-*
	dc.w	io_smul-*
io_tab
	dc.w	io_chek-*
	dc.w	io_flsh-*
	dc.w	dv3_posa-*
	dc.w	dv3_posr-*
	dc.w	io_ipar-*
	dc.w	io_minf-*
	dc.w	io_shdr-*
	dc.w	io_rhdr-*
	dc.w	io_load-*
	dc.w	io_save-*
	dc.w	dv3_rename-*
	dc.w	io_trnc-*
	dc.w	io_date-*
	dc.w	dv3_mkdr-*
	dc.w	io_vers-*
	dc.w	io_xinf-*

; fetch medium information: this is old style QDOS: the figures are a bit odd

dv3_minf
io_minf
	jsr	ddf_rsinfo(a4)		 ; update information
	lea	ddf_mname(a4),a2	 ; medium name
	move.w	(a2)+,d2		 ; name size
	move.l	(a2)+,(a1)+		 ; copy first 10 chars of name
	move.l	(a2)+,(a1)+
	move.w	(a2)+,(a1)+
	move.l	a1,a2			 ; keep a1 at end of buffer
	moveq	#9,d1
	sub.w	d2,d1			 ; characters remaining - 1
	bcs.s	ioi_ftype		 ; no space remaining
ioi_space
	move.b	#' ',-(a2)		 ; overwrite non-name part with space
	dbf	d1,ioi_space

ioi_ftype
; We only support QLWA here
;;;	   assert  ddf_ftype,ddf_stype-1
;;;	   cmp.w   #ddf.qdos<<8+ddf.ql5b,ddf_ftype(a4)	; old QL format?
;;;	   bhi.s   ioi_sallc		    ; use allocation units

;;;	   move.w  ddf_asect(a4),d2	    ; allocation units / sector
;;;	   move.b  ddf_slflag(a4),d1	    ; sector length flag
;;;	   subq.b  #2,d1		    ; treat 128 and 256 as one sector
;;;	   ble.s   ioi_ssect
;;;	   lsl.w   d1,d2		    ; but others as multiple sectors
;;;ioi_ssect
;;;	   move.w  ddf_afree+2(a4),d1	    ; free sectors
;;;	   mulu    d2,d1
;;;	   swap    d1
;;;	   mulu    ddf_agood+2(a4),d2	    ; good sectors
;;;	   move.w  d2,d1
;;;	   rts

;;;ioi_sallc
	move.l	ddf_afree+2(a4),d1	 ; free allocation units
	move.w	ddf_agood+2(a4),d1	 ; good allocation units
	rts

dv3_xinf
io_xinf
ioi.reg reg	d1/a0/d3/a5
	movem.l ioi.reg,-(sp)
	jsr	ddf_rsinfo(a4)		 ; set occupancy information
	lea	ioi.blkl(a1),a0 	 ; pre-fill information block
	moveq	#(ioi.blkl-ioi_remv)/4-1,d1
	moveq	#-1,d3
ioi_pre
	move.l	d3,-(a0)
	dbra	d1,ioi_pre

	assert	ioi.blkl-ioi_remv,$10
	move.b	ddf_remv(a4),(a0)

	assert	ddf_density,ddf_stype+1,ddf_ftype+2
	assert	ioi_remv,ioi_dens+2,ioi_styp+3,ioi_ftyp+4
	move.l	ddf_ftype(a4),-(a0)	 ; set flags
	move.b	ddl_mtype(a3),ioi_mtyp-ioi_ftyp(a0)

	assert	ddf_fhlen,ddf_afree+4,ddf_agood+$8
	assert	ioi_hdrl,ioi_free+4,ioi_totl+8,ioi_allc+$a
	lea	ddf_fhlen(a4),a2	 ; set header, free, total and allc
	move.l	(a2),-(a0)
	move.l	-(a2),-(a0)
	move.l	-(a2),-(a0)
	move.w	ddf_asize-ddf_agood(a2),-(a0)

	assert	ioi_dnum,ioi_rdon-1,ioi_allc-2
	move.b	ddf_wprot(a4),-(a0)	 ; set read only
	move.b	ddf_dnum(a4),-(a0)	 ; set drive number

	assert	ioi_dnam,ioi_dnum-6
	clr.l	-(a0)			 ; null the drive name
	lea	ddl_dname(a3),a2
	move.w	(a2)+,-(a0)		 ; name length
	move.w	(a0)+,d3
ioi_cdnm
	move.b	(a2)+,(a0)+		 ; copy drive name
	subq.w	#1,d3
	bgt.s	ioi_cdnm

	lea	ioi_dnam(a1),a0 	 ; set medium name to zeros
	assert	ioi_dnam-(ioi_name+2),20 ; max number of characters
	moveq	#ddf.mnlen/2,d3 	 ; clear name + len word
ioi_clr clr.w	-(a0)
	dbf	d3,ioi_clr		; a2 = ioi_dnam afterwards

	lea	ddf_mname(a4),a2	; medium (not device) name
	move.w	(a2)+,d1		; length
	move.w	d1,(a0)+
	bra.s	ioi_lp
ioi_cpy move.b	(a2)+,(a0)+		; copy name accross
ioi_lp	dbf	d1,ioi_cpy

	movem.l (sp)+,ioi.reg		; restore regs
	rts


; set/read date

io_date
	tst.b	d2			 ; update date?
	beq.s	iodt_upd		 ; ... yes
	cmp.b	#2,d2			 ; backup date?
	bne.l	io_ipar 		 ; ... no

	moveq	#d3c_arcd-$7f,d4	 ; archive date
	moveq	#d3c..bst,d3		 ; backup date set flag
	moveq	#ddf..arcd,d2
	bra.s	iodt_sr

iodt_upd
	moveq	#d3c_updd-$7f,d4	 ; update date
	moveq	#d3c..ust,d3		 ; date set flag
	moveq	#ddf..updd,d2
iodt_sr
	tst.l	d1			 ; current time?
	bne.s	iodt_sr1		 ; ... no, read or set
	jsr	dv3_date		 ; present date
iodt_sr1
	moveq	#1,d0
	add.l	d1,d0			 ; read?
	bne.s	iodt_set		 ; ... no
	move.l	$7f(a0,d4.w),d1 	 ; read date
;;;	   tst.b   ddf_dosd(a4) 	    ; DOS date?
;;;	   beq.s   iodt_rts
;;;	   jsr	   cv_dosrt		    ; convert from DOS
iodt_rts
	rts				 ; d0 already zero

iodt_set
	assert	d3c..bst,0
	tst.b	d3			 ; backup date?
	beq.s	iodt_sbd
	tst.b	d3c_ro(a0)		 ; read only?
	bgt.s	iodv_ro 		 ; ... yes
	bra.s	iodt_sd 		 ;
iodt_sbd
	tst.b	ddf_wprot(a4)		 ; write protected medium?
	bne.s	iodv_ro
iodt_sd
	move.l	d1,-(sp)
;;;	   tst.b   ddf_dosd(a4) 	    ; dos date?
;;;	   beq.s   iodt_sd1		    ; ... no
;;;	   jsr	   cv_rtdos		    ; convert to DOS
iodt_sd1
	move.l	d1,$7f(a0,d4.w) 	 ; set date
	move.l	(sp)+,d1

iodv_set
	bset	d2,d3c_setmask+3(a0)	 ; add to mask of info to set
	bset	d3,d3c_usef(a0) 	 ; add to date set
iodv_done
	moveq	#0,d0
	rts

iodv_ro
	moveq	#err.rdo,d0
	rts

; set / read version

io_vers
	moveq	#ddf..vers,d2		 ; version flags
	moveq	#d3c..vst,d3
	tst.l	d1			 ; read?
	blt.s	iov_read		 ; ... yes
	beq.s	iov_pres		 ; ... no, preserve

	tst.b	d3c_ro(a3)		 ; read only?
	bgt.s	iodv_ro 		 ; ... yes
	move.w	d1,d3c_vers(a0) 	 ; set version
	bra.s	iodv_set		 ; and say set

iov_pres
	tst.b	d3c_ro(a3)		 ; read only?
	bpl.s	iov_read		 ; ... yes, version will not be updated
	bset	d3,d3c_usef(a0) 	 ; ... do not update version on close
iov_read
	moveq	#0,d1
	move.w	d3c_vers(a0),d1
	bra.s	iodv_done

; do header

io_shdr
	moveq	#hdrt.dir+$ffffff00,d0
	cmp.b	hdr_type(a1),d0 	 ; set directory?
	beq.l	io_ipar 		 ; ... yes, cannot set
	cmp.b	d3c_type(a0),d0 	 ; already directory?
	beq.l	io_ipar 		 ; ... yes, cannot set
	bsr.l	dv3_ckro		 ; read only?

	addq.l	#hdr_accs,a1		 ; skip length
	move.w	(a1)+,d3c_dsattr(a0)	 ; set attributes
	move.l	(a1)+,d3c_data(a0)
	move.l	(a1)+,d3c_xtra(a0)
	or.b	#1<<ddf..data+1<<ddf..xtra+1<<ddf..type+1<<ddf..dsattr,d3c_setmask+3(a0)
	moveq	#14,d1			 ; we have set this much
	bra.s	iodv_done

io_rhdr
	move.l	d3c_feof(a0),d0
	sub.l	ddf_fhlen(a4),d0	 ; file length

dv3_rhdr
	cmp.w	#hdr.len,d2		 ; whole header?
	bhs.s	iorh_all		 ; ... yes

	move.l	a1,-(sp)		 ; no, put header in formatted entry
	st	d3c_fenum(a0)
	lea	d3c_fentry(a0),a1
	bsr.s	iorh_read		 ; all of it
	move.l	(sp)+,a1		 ; and copy what we need
	lea	d3c_fentry(a0),a2
	bra.s	iorh_cend
iorh_cloop
	move.b	(a2)+,(a1)+		 ; copy byte
iorh_cend
	dbra	d2,iorh_cloop
	moveq	#0,d0
	rts				 ; D0 and D1 set by iorh_read

iorh_all
	moveq	#hdr.len,d2
iorh_read
	move.l	d0,(a1)+		 ; set length

	assert	hdr_accs,hdr_type-1
	assert	d3c_dsattr,d3c_type-1
	move.w	d3c_dsattr(a0),(a1)+	 ; access (attributes) and type

	assert	hdr_accs,hdr_data-2
	move.l	d3c_data(a0),(a1)+	 ; data and extra
	move.l	d3c_xtra(a0),(a1)+

	lea	d3c_name(a0),a2
	moveq	#d3c.qnml,d0		 ; expected max name length
	move.w	(a2)+,d3		 ; name length
	cmp.w	d0,d3
	ble.s	iorh_nlen
	move.l	d0,d3			 ; truncate
iorh_nlen
	move.w	d3,(a1)+		 ; name length
	sub.w	d3,d0			 ; ... and blank
	moveq	#0,d1
	lea	dv3_iqdt,a5
	bra.s	iorh_nme
iorh_name
	move.b	(a2)+,d1
	move.b	(a5,d1.w),(a1)+ 	 ; copy name
iorh_nme
	dbra	d3,iorh_name

	bra.s	iorh_cle
iorh_clr
	sf	(a1)+			 ; clear the end
iorh_cle
	dbra	d0,iorh_clr

;;;	   tst.b   ddf_dosd(a4) 	    ; DOS dates?
;;;	   bne.s   iorh_dos
	move.l	d3c_updd(a0),(a1)+	 ; update date

	move.w	d3c_vers(a0),(a1)+	 ; version
	move.w	d3c_flid+2(a0),(a1)+	 ; file ID (lsw only)
	move.l	d3c_arcd(a0),(a1)+	 ; backup date
;;;	   bra.s   iorh_done

;;;iorh_dos
;;;	   move.l  d3c_updd(a0),d1
;;;	   jsr	   cv_dosrt
;;;	   move.l  d1,(a1)+		    ; update date
;;;	   move.w  d3c_vers(a0),(a1)+	    ; version
;;;	   move.w  d3c_flid+2(a0),(a1)+     ; file ID (lsw only)
;;;	   move.l  d3c_arcd(a0),d1
;;;	   jsr	   cv_dosrt
;;;	   move.l  d1,(a1)+		    ; backup date

;;;iorh_done
;;;	   move.w  d2,d1
	moveq	#0,d0
	rts

; scatter load and save

io_load
	jmp	ddf_sload(a4)
io_save
	jmp	ddf_ssave(a4)

; truncate

io_trnc
	bsr.l	dv3_ckro		 ; check read only
	move.l	d3c_fpos(a0),d3c_feof(a0)
	move.l	d3c_fsect(a0),d5	 ; truncation position
	jsr	dv3_sbtr		 ; slave block truncate
	assert	d3c_dgroup,d3c_fgroup-4
	movem.l d3c_dgroup(a0),d0/d1	 ; current drive and file group
	moveq	#0,d2
	divu	ddf_asect(a4),d5
	move.w	d5,d2			 ; truncate from here
	addq.l	#1,d2			 ; keeping this group
	bgt.s	iot_do			 ; ... not to zero
	tst.b	ddf_zalloc(a4)		 ; zero allocation?
	beq.s	iot_do			 ; ... no
	addq.l	#1,d2			 ; ... yes, do not scrub first
iot_do
	jmp	ddf_strunc(a4)		 ; truncate sectors on medium

; check pending operations

io_chek
	jmp	ddl_status(a3)		 ; check all pending operations
;+++
; Flush buffers and make safe
;     If updated sets length (if changed) / update date (d3=0) / version (once)
;+++
io_flsh
	tst.b	d3c_updt(a0)		 ; file updated?
	bge.s	io_rtok 		 ; ... no
;+++
; Flush buffers and make safe
;     Sets length (if changed) / update date (d3=0) / version (once)
;+++
dv3_flsh
	move.l	d3c_feof(a0),d0 	 ; current file length
	cmp.l	d3c_flen(a0),d0 	 ; is is changed?
	beq.s	iofl_date
	move.l	d0,d3c_flen(a0)
	bset	#ddf..flen,d3c_setmask(a0) ; it is now
iofl_date
	tst.w	d3			 ; set date only on first entry
	bne.s	iofl_ver
	btst	#d3c..ust,d3c_usef(a0)	 ; update date fixed?
	bne.s	iofl_ver
	jsr	dv3_date
;;;	   tst.b   ddf_dosd(a4) 	    ; DOS date?
;;;	   beq.s   iofl_sd		    ; ... no
;;;	   jsr	   cv_rtdos		    ; ... yes, convert it
;;;iofl_sd
	move.l	d1,d3c_updd(a0) 	 ; set update
	bset	#ddf..updd,d3c_setmask(a0)
iofl_ver
	bset	#d3c..vst,d3c_usef(a0)	 ; version updated?
	bne.s	iofl_do
	addq.w	#1,d3c_vers(a0)
	bset	#ddf..vers,d3c_setmask(a0)

iofl_do
	bsr.s	dv3_flhdr		 ; flush header information
	bne.s	iofl_rts

	move.w	d3,d0			 ; set urgent flush flag
	jsr	ddl_fflush(a3)		 ; and do flush
	sne	d3c_updt(a0)		 ; flush complete?
iofl_rts
	rts

dv3_flhdr
	move.l	d3c_setmask(a0),d1	 ; mask of bits to set
	beq.s	iofl_rts
	lea	d3c_dren(a0),a1
	jsr	ddf_drsfile(a4) 	 ; update directory entry
	bne.s	iofl_rts
	clr.l	d3c_setmask(a0)
	rts

; OK return

io_rtok
	moveq	#0,d0
	rts

	page

; fetch multiple bytes

io_fmul
	ext.l	d2			 ; call buffer length is a word
	jmp	dv3_fmul

; do fetch line

io_flin
	ext.l	d2			 ; call buffer length is a word
	jmp	dv3_flin

; send multiple bytes

io_smul
	ext.l	d2			 ; call argument is word
	jmp	dv3_smul		 ; do send

;+++
; check read only status (and set d3c_updt if OK)
;
;	smashes d0/a2
;	returns err.rdo at 4(sp) if d0 set to ERR.RDO
;
;---
dv3_ckro
	assert	d3c.ro,1
	assert	d3c.updt,-1
	tst.b	d3c_ro(a0)		 ; read only?
	bgt.s	dv3_rdo4
	st	d3c_updt(a0)		 ; set file updated
	rts

dv3_rdo4
	addq.l	#4,sp			 ; remove return
	moveq	#err.rdo,d0
	rts

	end
