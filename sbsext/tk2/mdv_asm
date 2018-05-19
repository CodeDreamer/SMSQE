; SBSEXT_TK2_MDV - Extended MDV driver. Reverse engineered by Marcel Kilgus

	xdef	mdv_init
	xdef	mdv_io

	include 'dev8_keys_iod'
	include 'dev8_keys_qdos_io'
	include 'dev8_keys_qdos_ioa'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_hdr'
	include 'dev8_keys_err'
	include 'dev8_keys_sbt'
	include 'dev8_keys_sys'
	include 'dev8_keys_chn'

	section mdv

; Driver linkage block extensions
iod_oio equ	$42		; Original I/O
iod_oop equ	$46		; Original open
iod_ocl equ	$4a		; Original close
iod_end equ	$4e

; Channel definition block extensions
chn_spare equ	$58		; Some spare bytes to put data into

; Physical definition block
md_fail  equ	$24		; Byte Failure count
md_spare equ	$25		; 3 Bytes
md_map	 equ	$28		; $FF*2 bytes Microdrive sector map
md_lsect equ	$226		; Number of last sector allocated
md_pendg equ	$228		; Word Map of pending operations
md_end	 equ	$428


mdv_init
	trap	#0
	moveq	#sms.info,d0
	trap	#1
;	 cmpi.l  #'2.00',d2		 ; Um, what QDOS version 2.00? SMSQ/E?
	andi.l	#$FF00FFFF,d2
	cmpi.l	#$31003930,d2		; Minerva doesn't need our help either
	bge.s	mi_rts			; ... don't enable then
	lea	sys_fsdl(a0),a4 	; Filing system driver list
	movea.l a0,a5
mi_loop
	movea.l a4,a0
	movea.l (a4),a4
	tst.l	(a4)			; End of list?
	bne.s	mi_loop 		; No
	cmpa.l	#$0000C000,a4		; Driver in ROM?
	bge.s	mi_rts			; No, abort
	move.l	a0,-(sp)
	moveq	#sms.achp,d0
	moveq	#iod_end-iod_iolk,d1	; Size of new driver linkage block
	moveq	#0,d2
	trap	#1

	lea	sys_fsdd(a5),a1 	; Drive definitions
	moveq	#sys.nfsd-1,d1
mis_loop
	movea.l (a1)+,a2		; Next definition
	cmpa.l	iod_drlk(a2),a4 	; Does this belong to the driver?
	bne.s	mis_no			; No
	move.l	a0,iod_drlk(a2) 	; Yes, replace with new block
mis_no
	dbf	d1,mis_loop

	movea.l a0,a3			; New driver linkage block
	moveq	#iod_dnam-iod_iolk,d1
mi_copy
	move.l	(a4)+,(a3)+		; Copy everything except the linkages
	subq.w	#4,d1
	bgt.s	mi_copy

	lea	iod_ioad-iod_iolk(a0),a3
	lea	iod_oio-iod_iolk(a0),a4
	move.l	(a3),(a4)+		; Copy of original I/O routine
	lea	mdv_io(pc),a1
	move.l	a1,(a3)+		; Our I/O routine
	move.l	(a3),(a4)+		; Copy of original open
	lea	mdv_open(pc),a1
	move.l	a1,(a3)+		; Our open
	move.l	(a3),(a4)+		; Copy of original close
	lea	mdv_close(pc),a1
	move.l	a1,(a3)+		; Our close
	movea.l (sp)+,a1		; Entry in filing system driver list
	move.l	a0,(a1) 		; Overwrite
mi_rts
	move.w	#0,sr
	rts

; New MDV driver I/O
mdv_io	cmpi.b	#iof.rnam,d0
	beq	mdv_rnam
	cmpi.b	#iof.trnc,d0
	beq	mdv_trnc
	cmpi.b	#iof.flsh,d0
	bne.s	mdv_oio

; Update file header on flush
mdv_flsh
	tst.w	d3
	bne.s	mdv_oio
	move.l	chn_feof(a0),d0 	; File size
	lsl.w	#7,d0
	lsr.l	#7,d0			; In bytes
	lea	chn_spare(a0),a4	; There are some spare bytes
	move.l	d0,(a4) 		; Write file size in bytes to it
	moveq	#4,d2
	moveq	#hdr_flen,d4
	bsr.s	mdv_whead		; Update file length in both headers
	moveq	#iof.flsh,d0
mdv_oio
	movea.l iod_oio(a3),a4		; Original I/O routine
	jmp	(a4)

; Read complete file header into buffer A4
mdv_rhead
	moveq	#hdr_end,d2
	moveq	#iob.fmul,d0
mdv_trap				; Simulate trap call of original driver
	movea.l a4,a1			; Buffer
	movem.l d0/d2/d4/a2-a5,-(a7)
mtrap_loop
	movem.l (a7),d0/d2/d4/a2-a3
	moveq	#0,d1
	moveq	#0,d3
	bsr.s	mdv_oio
	addq.l	#1,d0			; err.nc?
	beq.s	mtrap_loop		; Then try again
	subq.l	#1,d0			; Restore error code
	movem.l (a7)+,d1-d2/d4/a2-a5
	rts

; Write header data
;
; d4 = position in header
; d2 = length of data
mdv_whead
	lea	chn_csb(a0),a2		; Save current file position
	move.l	-(a2),-(sp)		; chn_feof
	move.l	-(a2),-(sp)		; chn_fpos
	move.w	-(a2),-(sp)		; chn_qdid
	clr.w	(a2)			; Write to root directory
	move.w	(sp),d0 		; File ID
	lsl.l	#6,d0
	add.l	d4,d0			; Add wanted position
	lsl.l	#7,d0			; Decompose again into block/byte
	lsr.w	#7,d0
	move.l	d0,chn_fpos(a0) 	; Fake current position
	st	$0025(a0)		; Don't bother with end, max it (?)
	moveq	#iob.smul,d0
	bsr.s	mdv_trap		; Write bytes
	bne.s	mdv_posrestore
	move.w	(sp),(a2)		; File ID
	move.l	d4,chn_fpos(a0) 	; Use index as index into file
	moveq	#iob.smul,d0		; Also update header in file data
	bsr.s	mdv_trap
mdv_posrestore
	lea	chn_qdid(a0),a2 	; Restore file position
	move.w	(sp)+,(a2)+		; chn_qdid
	move.l	(sp)+,(a2)+		; chn_fpos
	move.l	(sp)+,(a2)+		; chn_feof
	tst.l	d0
	rts

; New driver supports RENAME
mdv_rnam
	move.w	(a1)+,d4		; New name length
	subq.w	#5,d4			; Minus MDVx_
	bls.s	mdv_inam		; Must be longer!
	cmpi.w	#hdr.nmln,d4
	bhi.s	mdv_inam		; Name too long
	bsr	mdv_getddb
	move.l	#$DFDFDFFF,d0
	and.l	(a1)+,d0		; Mask casing
	sub.b	iod_dnum(a2),d0 	; Remove drive number
	cmpi.l	#'MDV0',d0
	bne.s	mdv_inam		; Not for MDV? Cannot do
	cmpi.b	#'_',(a1)+
	bne.s	mdv_inam
	bsr.s	mr_do
	bne.s	mr_rts
	moveq	#hdr.nmln+2,d2
	moveq	#hdr_name,d4
	bra.s	mdv_whead		; Write new filename to header
mdv_inam
	moveq	#err.inam,d0
mr_rts
	rts

; Check if new filename already exists in directory. If not, rename in CDB
;
; d4 = size of filename (without device)
; a1 = filename (without device)
mr_do
	lea	chn_spare(a0),a4	; Spare space for header
	lea	chn_csb(a0),a2		; Remember current file position
	move.l	-(a2),-(sp)		; chn_feof
	move.l	-(a2),-(sp)		; chn_fpos
	move.w	-(a2),-(sp)		; chn_qdid
	move.l	a1,-(sp)		; Remember new filename
	clr.w	(a2)+			; Read root directory
	clr.l	(a2)+
	bsr	mdv_rhead		; Read header of directory
	bne.s	mr_exit
	move.l	(a4),d0 		; Length of directory in bytes
	lsl.l	#7,d0
	lsr.w	#7,d0			; Now blocks
	move.l	d0,chn_feof(a0) 	; Set EOF
	lea	hdr_name+2(a4),a5	; Pointer to name in directory entry
mr_searchloop
	bsr	mdv_rhead		; Read next directory entry
	bne.s	mr_rename_cdb		; No more entries
	movea.l (sp),a1 		; New filename
	cmp.w	-2(a5),d4		; Size matches?
	bne.s	mr_searchloop		; No
	moveq	#0,d0			; Start with first character
mr_checkname
	bsr.s	mr_getchar		; From new filename
	move.b	d1,d2
	bsr.s	mr_getchar		; And old filename
	cmp.b	d1,d2
	bne.s	mr_searchloop		; Don't match, continue search
	addq.w	#1,d0
	cmp.w	d4,d0			; End of filename?
	blt.s	mr_checkname		; Nope
	moveq	#err.fex,d0		; Filename does already exist, error
	bra.s	mr_exit

mr_rename_cdb
	movea.l (sp),a1 		; New filename
	lea	chn_name(a0),a2
	movea.l a2,a4
	move.w	d4,(a2)+		; Put new name into CDB
mr_setloop
	move.b	(a1)+,(a2)+
	subq.w	#1,d4
	bgt.s	mr_setloop
	moveq	#0,d0
mr_exit
	addq.l	#4,sp			; Skip new filename
	bra	mdv_posrestore		; And restore file position

; Get character by index, always upper-case. Except for umlauts...
mr_getchar
	exg	a1,a5
	move.b	(a5,d0.w),d1
	cmpi.b	#'a',d1
	blt.s	mrgc_rts
	cmpi.b	#'z',d1
	bgt.s	mrgc_rts
	subi.b	#'a'-'A',d1
mrgc_rts
	rts

; Get driver linkage block in A2 plus slave block drive ID in D1
mdv_getddb
	moveq	#0,d1
	move.b	chn_drid(a0),d1
	lsl.w	#2,d1
	lea	sys_fsdd(a6),a2
	movea.l (a2,d1.w),a2
	lsl.b	#2,d1			; Slave has drive ID in upper nibble
	addq.b	#1,d1			; Slave drive ID
	rts

; New driver supports TRUNCATE
mdv_trnc
	cmpi.b	#ioa.kshr,chn_accs(a0)
	bne.s	mt_do
	moveq	#err.rdo,d0
	rts

mt_do
	move.l	chn_fpos(a0),d4
	move.l	d4,chn_feof(a0) 	; Set FPOS as EOF
	subq.l	#1,d4
	swap	d4			; Last block within file
	move.w	chn_qdid(a0),d5 	; File ID
	bsr.s	mdv_getddb		; Get driver linkage in a2
	lea	md_map(a2),a5		; Sector map
	lea	$FF*2-2(a5),a4		; 2 bytes per sector, start with last

; Mark all sectors after the cut off point as empty
mt_sect_loop
	cmp.b	(a4),d5 		; For our file?
	bne.s	mt_sect_next
	cmp.b	1(a4),d4		; And a block beyond the new end?
	bcc.s	mt_sect_next		; No
	move.w	#$FD00,(a4)		; Yes, mark as empty
	clr.w	md_pendg-md_map(a4)	; And clear pending operations
mt_sect_next
	subq.l	#2,a4			; Work towards start of sector map
	cmpa.l	a5,a4
	bgt.s	mt_sect_loop

; Remove any slave block entries after cut off point
	movea.l sys_sbtb(a6),a4 	; Slave block base
mt_sb_loop
	moveq	#sbt.driv,d0		; Mask of pointer to drive
	and.b	(a4),d0
	cmp.b	d0,d1			; Is it ours?
	bne.s	mt_sb_next		; No
	cmp.w	sbt_file(a4),d5 	; File matches?
	bne.s	mt_sb_next		; No
	cmp.w	sbt_blok(a4),d4 	; Block is beyond the new ned?
	bcc.s	mt_sb_next		; No
	move.b	#sbt.mpty,(a4)		; Yes, forget slave block
mt_sb_next
	addq.l	#sbt.len,a4
	cmpa.l	sys_sbtt(a6),a4
	blt.s	mt_sb_loop
	move.w	#-1,md_pendg(a2)	; Write map
	st	chn_updt(a0)		; File was updated
	moveq	#0,d0
mdv_rts2
	rts

; New OPEN. Support overwrite and directory access
mdv_open
	move.b	chn_accs(a0),d3
	cmpi.b	#ioa.kovr,d3
	beq.s	mdv_open_over
	cmpi.b	#ioa.kdir,d3
	beq.s	mdv_open_dir
mdv_oopen
	movea.l iod_oop(a3),a4
	jmp	(a4)

mdv_open_over
	move.b	#ioa.kexc,chn_accs(a0)
	movem.l a1/a3,-(a7)
	bsr.s	mdv_oopen		; Try to open file to read
	movem.l (a7)+,a1/a3
	move.b	#ioa.kovr,chn_accs(a0)
	cmpi.w	#err.fdnf,d0
	beq.s	mdv_open_nf		; Not found, just open it normally
	tst.l	d0
	bne.s	mdv_rts2
	moveq	#hdr.len,d4
	lea	chn_spare+hdr_name(a0),a4
	clr.l	-(a4)			; Clear hdr_xtra
	clr.l	-(a4)			; Clear hdr_data
	clr.w	-(a4)			; Clear hdr_accs/hdr_type
	move.l	d4,-(a4)		; Empty file (just header)
	moveq	#iob.smul,d0
	moveq	#hdr_name,d2		; Everything up to the name
	clr.l	chn_fpos(a0)
	bsr	mdv_trap		; Overwrite header data
	move.l	d4,chn_fpos(a0) 	; Set position to start of file
	bra	mdv_trnc		; Truncate file

; File not found, just open it normally
mdv_open_nf
	clr.l	chn_fpos(a0)
	bra.s	mdv_oopen

mdv_open_dir
	clr.w	chn_name(a0)
	bra.s	mdv_oopen

; New CLOSE, update file date before closing
mdv_close
	tst.b	chn_updt(a0)		; File updated
	beq.s	mc_do			; No, no need to update date then
	moveq	#hdr_date,d0
	move.l	d0,chn_fpos(a0) 	; No need to save this, we're closing
	move.l	a0,-(sp)
	moveq	#sms.rrtc,d0		; Read current date
	trap	#1
	movea.l (sp)+,a0
	lea	chn_spare(a0),a4
	move.l	d1,(a4) 		; Put into spare buffer
	moveq	#iob.smul,d0
	moveq	#4,d2
	bsr	mdv_trap		; Update update date in header
mc_do	movea.l iod_ocl(a3),a4
	jmp	(a4)			; Call original close

	end
