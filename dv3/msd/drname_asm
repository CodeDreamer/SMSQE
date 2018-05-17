; DV3 MSDOS Directory Name		   V3.00	   1993 Tony Tebby

	section dv3

	xdef	msd_drname

	xref	msd_dre2
	xref	msd_drlen
	xref	msd_setmu

	xref	dv3_drloc
	xref	dv3_drnew
	xref	dv3_drupd
	xref	dv3_fbloc
	xref	msd_deloc
	xref	msd_drupd

	xref.s	msd.hdrs

	include 'dev8_dv3_keys'
	include 'dev8_dv3_msd_keys'
	include 'dev8_keys_dos'
	include 'dev8_keys_err'
	include 'dev8_mac_assert'
	include 'dev8_mac_xword'

;+++
; DV3 Look for name in directory
;
;	d2 c  p byte set if directory entry to be filled
;	d3 c  u type of search / bit 31 set if file is a directory
;	d6  r	file ID
;	d7 c  p drive ID / number
;	a0 c  p pointer to channel block
;	a1 c  p pointer to name
;	a2    p
;	a3 c  p pointer to linkage block
;	a4 c  p pointer to drive definition block
;
;	status return standard
;
;---
msd_drname
mdn.reg reg	d1/d2/d4/d5/a1/a2/a5
stk_empty equ	$00
stk_save  equ	$04	; save ID / name length / position (three words)
stk_svid  equ	$04	; beware: this is 0 for root until directory found
stk_svlen equ	$06
stk_svpos equ	$08

stk_nadj  equ	$0e		; name adjusted
stk_npos  equ	$10		; next position in pathname
stk_opos  equ	$12		; old position in filename

stk_fnam  equ	$14		; eight character filename
stk_extn  equ	$1c		; three character extension

mdn.frame equ	$20
stk_sden  equ	mdn.frame+1*4+3
stk_name  equ	mdn.frame+4*4
stk_old   equ	mdn.frame+5*4
stk_link  equ	mdn.frame+6*4

	movem.l mdn.reg,-(sp)
	sub.w	#mdn.frame-stk_fnam,sp
	clr.l	-(sp)		; clear position in pathname and saved length
	clr.l	-(sp)
	clr.l	-(sp)
	clr.l	-(sp)
	clr.l	-(sp)

	move.l	mdf_rdir(a4),a2 	 ; root directory
	sub.w	ddf_slen(a4),a2
	lsr.w	d3c_sdlen+2(a0) 	 ; real root dir length is half standard
	moveq	#0,d4			 ; initial file pointer
	neg.w	stk_svpos(sp)		 ; formal values for ROOT

	add.w	(a1)+,a1
	move.b	#'\',(a1)		 ; put \ at end

mdn_dloop
	sf	stk_nadj(sp)		 ; name not adjusted
	move.w	stk_npos(sp),d0 	 ; move through name
	move.w	d0,stk_opos(sp) 	 ; save it
	move.l	stk_name(sp),a1
	cmp.w	(a1)+,d0
	bge.l	mdn_dirf		 ; name finished, and its a directory
mdn_drtry
	move.l	a1,d6			 ; start of name
	add.w	d0,a1

	move.l	#'    ',d2
	lea	stk_extn(sp),a5
	move.l	d2,(a5)
	move.l	d2,-(a5)
	move.l	d2,-(a5)		 ; preset names to blank

	moveq	#8,d1			 ; maximum name length
mdn_cpnl
	move.b	(a1)+,d2
	cmp.b	#'\',d2 		 ; name separator?
	beq.s	mdn_look		 ; ... yes, look for name
	cmp.b	#'.',d2 		 ; extension separator character?
	beq.s	mdn_cpxt		 ; ... yes, check extension
	move.b	d2,(a5)+		 ; name character
	subq.w	#1,d1
	bge.s	mdn_cpnl
	bra.s	mdn_enam		 ; bad name

mdn_cpxt
	lea	stk_extn(sp),a5
	moveq	#3,d1			 ; maximum extension length
mdn_cpxl
	move.b	(a1)+,d2
	cmp.b	#'\',d2 		 ; separator character?
	beq.s	mdn_look		 ; ... yes, look for name
	move.b	d2,(a5)+		 ; extension character
	subq.w	#1,d1
	bge.s	mdn_cpxl

mdn_enam
	tst.b	stk_nadj(sp)		 ; adjusted name?
	bne.l	mdn_nf			 ; ... yes, not found
	moveq	#err.inam,d0
	bra.l	mdn_exit

mdn_look
	sub.l	d6,a1
	move.w	a1,stk_npos(sp) 	 ; save running pointer to name

	moveq	#0,d6			 ; initial entry
	moveq	#0,d5			 ; initial sector
	st	stk_empty(sp)
	cmp.l	d3c_sdlen(a0),d6	 ; end of file?
	bhs.l	mdn_not_found		 ; ... yes, not found
	bra.s	mdn_dref

mdn_eloop
	moveq	#dos.drel,d0
	add.l	d0,d6
	add.w	d0,d4

	cmp.l	d3c_sdlen(a0),d6	 ; end of file?
	bhs.l	mdn_not_found		 ; ... yes, not found

	cmp.w	ddf_slen(a4),d4 	 ; end of sector
	blo.s	mdn_check

	moveq	#0,d4
	addq.w	#1,d5			 ; next sector

mdn_dref
	tst.w	stk_svid(sp)		 ; ... searching root?
	beq.s	mdn_nroot		 ; ... yes

	jsr	dv3_drloc		 ; locate next sector
	beq.s	mdn_check
	bra.l	mdn_err 		 ; ... oops

mdn_nroot
	add.w	ddf_slen(a4),a2 	 ; next root sector

mdn_check
	move.b	dos_fnam(a2,d4.w),d0	 ; vacant?
	beq.l	mdn_endd		 ; ... yes, end of directory
	cmp.b	#dos.dele,d0		 ; deleted?
	beq.s	mdn_empty		 ; ... yes

	lea	dos_fnam(a2,d4.w),a5
	lea	stk_fnam(sp),a1

	cmpm.l	(a5)+,(a1)+		 ; check name
	bne.s	mdn_eloop		 ; ... no match
	cmpm.l	(a5)+,(a1)+		 ; check name
	bne.s	mdn_eloop		 ; ... no match

	cmpm.w	(a5)+,(a1)+		 ; check three bytes of extension
	bne.s	mdn_tryex		 ; ... no match, try EX extension
	cmpm.b	(a5)+,(a1)+
	beq.s	mdn_match		 ; found it
	bra.s	mdn_eloop		 ; ... no match

mdn_tryex
	cmp.w	#'  ',-(a1)		 ; looking for no extension?
	bne.s	mdn_eloop		 ; ... no
	move.b	(a5),d0 		 ; third character of extension
	cmp.w	#'EX',-(a5)		 ; EXn?
	bne.s	mdn_eloop		 ; ... no
	cmp.b	#'0',d0 		 ; ... 0-9?
	blt.s	mdn_eloop		 ; ... no match
	cmp.b	#'9',d0
	bgt.s	mdn_eloop
	moveq	#dos.subd+dos.vol,d0	 ; is it a directory or volume lable?
	and.b	dos_attr(a2,d4.w),d0
	beq.l	mdn_file		 ; it is a file !!!
	bra.s	mdn_eloop		 ; forget it

mdn_match
	moveq	#dos.subd+dos.vol,d0	 ; is it a directory or volume lable?
	and.b	dos_attr(a2,d4.w),d0
	beq.l	mdn_file		 ; ... no, it is a file
	subq.b	#dos.vol,d0
	beq.l	mdn_eloop		 ; ignore volume lable

	move.w	stk_npos(sp),d2 	 ; directory name length

	move.w	d3c_sdid+2(a0),d0
	movem.w d0/d2/d4/d5/d6,stk_save(sp) ; save directory ID and positions

	move.w	dos_clus(a2,d4.w),d0
	xword	d0
	move.w	d0,d3c_sdid+2(a0)	 ; down one directory level
	jsr	msd_drlen		 ; find file length from FAT
	move.l	d0,d3c_sdlen(a0)
	moveq	#2*dos.drel,d4		 ; initial pointer in sub-directory
	bra.l	mdn_dloop

mdn_empty
	tst.b	stk_empty(sp)		 ; have we already found an empty entry?
	bge.l	mdn_eloop		 ; ... yes
	move.l	d6,stk_empty(sp)	 ; ... no, save the pointer to this one
	bra.l	mdn_eloop

; a directory has been found if this is remove, it must be empty
;			     otherwise it may be old or directory

mdn_dirf
	tst.b	d3			 ; remove
	blt.l	mdn_rmdr		 ; ... yes
	cmp.b	#ddf.new,d3
	blt.s	mdn_sdir		 ; ... directory
	beq.l	mdn_fdex		 ; ... new
	bra.l	mdn_fdiu		 ; ... either

; end of directory

mdn_endd
	tst.b	stk_empty(sp)		 ; have we already found an empty entry?
	bge.s	mdn_not_found		 ; ... yes
	move.l	d6,stk_empty(sp)	 ; ... no, save the pointer to this one

; The name has not been found, this is OK if open directory

mdn_not_found
	move.l	stk_name(sp),a1
	move.w	stk_npos(sp),d0
	cmp.w	(a1)+,d0		 ; end of name?
	bgt.s	mdn_rnf 		 ; ... yes, really not found
	move.b	#msd.hdrs,-1(a1,d0.w)	 ; put in host separator
	move.w	stk_opos(sp),d0 	 ; old position
	st	stk_nadj(sp)		 ; name adjusted
	moveq	#2*dos.drel,d4		 ; assume sub-dir
	tst.w	stk_save(sp)		 ; any directory above?
	bne	mdn_drtry		 ; ... yes
	move.l	mdf_rdir(a4),a2 	 ; root directory
	sub.w	ddf_slen(a4),a2
	moveq	#0,d4			 ; initial file pointer
	bra	mdn_drtry

mdn_rnf
	tst.b	d3			 ; is it open directory?
	beq.s	mdn_sdir		 ; ... yes
	blt.l	mdn_nf			 ; ... delete nothing

	move.w	stk_npos(sp),d0 	 ; we've processed this much name
	move.l	stk_name(sp),a1
	cmp.w	(a1)+,d0
	bgt.l	mdn_new 		 ; ... all of it, check for new entry
	bra.l	mdn_nf			 ; ... directory does not exist

mdn_sdir
	move.l	stk_name(sp),a1
	movem.w stk_save(sp),d0/d2/d4/d5/d6 ; restore directory ID and positions
	move.w	d2,(a1) 		 ; set true length of directory name

	bset	#31,d3			 ; directory found

	move.l	d3c_sdlen(a0),d1
	add.l	d1,d1
	move.l	d1,d3c_flen(a0) 	 ; set directory length
	st	d3c_type(a0)		 ; and type
	move.b	#dos.subd,d3c_dsattr(a0)
	st	d3c_denum(a0)

	move.l	d6,d5			 ; entry
	move.l	d3c_sdid(a0),d6 	 ; file ID
	move.w	d0,d3c_sdid+2(a0)	 ; and (sub)directory ID
	bra.s	mdn_sete		 ; set entry

; File is found, if operation is open directory, we need to go back up a level,
; otherwise the name must be complete.

mdn_file
	tst.b	d3			 ; is it open directory?
	beq.s	mdn_sdir		 ; ... yes

	move.w	stk_npos(sp),d0 	 ; we've processed this much name
	move.l	stk_name(sp),a1
	cmp.w	(a1)+,d0
	ble.s	mdn_nf			 ; name is not found


	tst.b	d3			 ; is it remove file?
	bmi.s	mdn_remv		 ; ... yes
	cmp.b	#ddf.new,d3		 ; new?
	beq.s	mdn_fdex		 ; ... yes
	cmp.b	#ddf.rename,d3		 ; rename?
	beq.s	mdn_fdex

mdn_setn
	move.l	d6,d5			 ; set entry number
	tst.b	stk_sden(sp)		 ; directory entry required?
	beq.s	mdn_seti
	add.w	d4,a2			 ; un-scrumple directory entry
	jsr	msd_dre2
	sub.w	d4,a2

mdn_seti
	moveq	#0,d6
	move.w	dos_clus(a2,d4.w),d6	 ; DOS cluster is file ID
	xword	d6			 ; ... but not in 80x86 format

mdn_sete
	asr.l	#dos.dres,d5		 ; entry
	move.l	d5,d3c_sdent(a0)	 ; sub-directory entry number

mdn_ok
	moveq	#0,d0

mdn_exit
	add.w	#mdn.frame,sp
	tst.b	ddf_mupd(a4)		 ; is map updated?
	beq.s	mdn_exd0		 ; ... no
	sub.l	a1,a1
	jsr	ddl_slbupd(a3)		 ; flush map

mdn_exd0
	tst.l	d0
	movem.l (sp)+,mdn.reg
	rts

mdn_fdex
	moveq	#err.fex,d0
	bra.s	mdn_err
mdn_nf
	moveq	#err.fdnf,d0
	bra.s	mdn_err
mdn_fdiu
	moveq	#err.fdiu,d0
	bra.s	mdn_err
mdn_rdfl
	; root dir full
mdn_drfl
	moveq	#err.drfl,d0

mdn_err
	moveq	#0,d6
	bra.s	mdn_exit

; remove file from directory

mdn_rmdr
	move.w	stk_svpos(sp),d4
	bsr.l	mdn_ckde		 ; check directory empty
	bne.s	mdn_fdiu
	movem.w stk_save(sp),d0/d2/d4/d5/d6 ; restore directory ID and positions
	move.w	d0,d3c_sdid+2(a0)
	bsr.l	mdn_drloc		 ; recover sector

mdn_remv
	move.b	#dos.dele,dos_fnam(a2,d4.w)
	jsr	msd_drupd		 ; sector updated
	move.l	d6,d5			 ; entry number
	bra.s	mdn_seti		 ; set return

mdn_rename
	move.l	d3c_flid(a2),d6
	move.l	d6,d3c_flid(a0) 	 ; copy old ID
	move.l	d3c_sdent(a2),d3c_sdent(a0) ; and entry number
	jsr	msd_deloc		 ; get our entry
	bne.s	mdn_exit

	assert	dos_fnam,0
	lea	stk_fnam(sp),a1
	move.l	(a1)+,(a2)+		 ; copy name
	move.l	(a1)+,(a2)+
	move.w	(a1)+,(a2)+
	move.b	(a1)+,(a2)+		 ; and extension

	jsr	msd_drupd		 ; director entry updated
	bra.s	mdn_ok

; create new entry (only for new, either or rename)

mdn_new
	cmp.b	#ddf.new,d3		 ; is it new, either or rename?
	blt.s	mdn_nf			 ; ... no
	cmp.b	#ddf.rename,d3		 ; rename?
	blt.s	mdn_adde		 ; ... no, add entry
	move.l	stk_old(sp),a2		 ; old channel block
	move.l	d3c_sdid(a2),d0
	cmp.l	d3c_sdid(a0),d0 	 ; same sub-directory?
	beq.s	mdn_rename		 ; ... yes, it is just a new name

mdn_adde
	tst.l	ddf_afree(a4)		 ; any free sectors?
	beq.s	mdn_drfl		 ; ... no

	move.l	stk_empty(sp),d0	 ; empty hole?
	bpl.s	mdn_gbd0		 ; ... yes, get buffer

	tst.w	stk_save(sp)		 ; any directory above?
	beq	mdn_rdfl		 ; root directory is full

	cmp.l	#2,ddf_afree(a4)	 ; at least two allocation units
	blo.l	mdn_drfl		 ; ... no

	move.w	ddf_asect(a4),d4	 ; number of new sectors

mdn_drnew
	addq.w	#1,d5			 ; next sector
	jsr	dv3_drnew
	blt	mdn_err 		 ; cannot do

	move.w	ddf_slen(a4),d0
	move.l	a2,a1
	moveq	#0,d1
mdn_drz
	move.l	d1,(a1)+		 ; clear sector
	subq.l	#4,d0
	bgt.s	mdn_drz

	jsr	dv3_drupd		 ; updated

	subq.w	#1,d4			 ; another sector in group
	bgt.s	mdn_drnew		 ; ... yes

	sub.w	ddf_asect(a4),d5
	addq.w	#1,d5			 ; next sector

	bra.s	mdn_gbuf

mdn_gbd0
	move.l	d0,d6
	tst.w	stk_save(sp)		 ; root directory?
	bne.s	mdn_gbuf		 ; ... no, get buffer

	move.l	mdf_rdir(a4),a2
	move.l	d6,d4			 ; position in root
	bra.s	mdn_setd

mdn_gbuf
	moveq	#2*dos.drel,d5
	add.l	d6,d5			 ; offset in subdir

	divu	ddf_slen(a4),d5 	 ; sector / byte
	swap	d5
	move.w	d5,d4
	clr.w	d5
	swap	d5

	jsr	dv3_drloc		 ; get appropriate sector of directory
	bne.l	mdn_err

mdn_setd
	add.w	d4,a2
	lea	stk_fnam(sp),a1 	 ; file name
	move.l	(a1)+,(a2)+
	move.l	(a1)+,(a2)+
	move.w	(a1)+,(a2)+
	move.b	(a1)+,(a2)+		 ; and extension

	cmp.b	#ddf.rename,d3		 ; rename?
	beq.s	mdn_move		 ; ... yes, get the next bits from old

	sf	(a2)+			 ; no attributes

	moveq	#(dos.drel-$c)/4-1,d0
	moveq	#0,d1
mdn_clre
	move.l	d1,(a2)+		 ; unused bits
	dbra	d0,mdn_clre

	sub.w	#dos.drel,a2		 ; beginning of entry

	jsr	msd_drupd		 ; sector updated

	move.l	d6,d5			 ; entry number
	moveq	#0,d6			 ; no file ID
	bra.l	mdn_sete		 ; set SD entry number

; renamed file needs to be moved

mdn_move
	asr.l	#dos.dres,d6		 ; entry
	move.l	d6,d3c_sdent(a0)	 ; sub-directory entry number

	move.l	a0,-(sp)		 ; save new channel block
	move.l	stk_old+4(sp),a0	 ; old channel block
	move.l	d3c_flid(a0),d6
	jsr	msd_deloc

	move.b	#dos.dele,(a2)		 ; old file is deleted
	jsr	msd_drupd		 ; old entry updated

	move.l	(sp)+,a0		 ; new channel block

	lea	dos.drel(a2),a2 	 ; end of entry

	moveq	#(dos.drel-dos_attr-1)/4-1,d0 ; copy the end to stack
mdn_mcopyts
	move.l	-(a2),-(sp)
	dbra	d0,mdn_mcopyts

	move.b	-(a2),-(sp)		 ; copy attributes

	jsr	msd_deloc		 ; now find new entry

	lea	dos_attr(a2),a1
	move.b	(sp)+,(a1)+

	moveq	#(dos.drel-dos_attr-1)/4-1,d0 ; copy the end from stack
mdn_mcopyfs
	move.l	(sp)+,(a1)+
	dbra	d0,mdn_mcopyfs

	jsr	msd_drupd		 ; updated

	move.l	d6,d3c_flid(a0) 	 ; set (new) ID in channel block
	bra	mdn_ok

; find directory sector for d3c_sdsb(a0)
; sets d3c_sdsb(a0) to 0 for root directory
;
;	d5 c  p directory sector
;	d7 c  p drive number
;	a0 c  p channel block
;	a2  r	pointer to buffer
;	a3 c  p linkage block
;	a4 c  p definition block
;	a5 c  p pointer to map
;
;	status return standard
;---
mdn_drloc
	cmp.l	#ddf.rdid,d3c_sdid(a0)	 ; root?
	bne.l	dv3_drloc		 ; ... no, standard

	move.w	d5,d0
	mulu	ddf_slen(a4),d0 	 ; "sector" in root
	move.l	mdf_rdir(a4),a2 	 ; root directory
	add.l	d0,a2
	moveq	#0,d0
	move.l	d0,d3c_sdsb(a0)
	rts


; here we must check for empty directory

mdn_ckde
mdce.reg reg	d1/d3
	movem.l mdce.reg,-(sp)
	move.w	dos_clus(a2,d4.w),d6	 ; DOS cluster is file ID
	xword	d6			 ; ... but not in 80x86 format
	moveq	#dos.drel,d1
	move.l	d3c_sdlen(a0),d2
	beq.s	mdce_ok
	moveq	#0,d3
	moveq	#2*dos.drel,d4		 ; initial pointer in sub-directory
	moveq	#0,d5

mdce_fbloc
	jsr	dv3_fbloc		 ; locate buffer
mdce_loop
	move.b	dos_fnam(a2,d4.w),d0	 ; end?
	beq.s	mdce_ok
	cmp.b	#dos.dele,d0		 ; empty?
	bne.s	mdce_iu 		 ; ... no

	add.w	d1,d3			 ; next entry
	cmp.w	d2,d3
	bge.s	mdce_ok 		 ; ... no more

	add.w	d1,d4			 ; next entry
	cmp.w	ddf_slen(a4),d4
	blt.s	mdce_loop		 ; ok, same sector
	addq.w	#1,d5			 ; go on one sector
	moveq	#0,d4
	bra.s	mdce_fbloc

mdce_ok
	moveq	#0,d0
mdce_exit
	movem.l (sp)+,mdce.reg
	rts
mdce_iu
	moveq	#err.fdiu,d0
	bra.s	mdce_exit


	end
