; DV3 QUBIDE Directory Name	V3.00	        1992 Tony Tebby
; adapted for qubide		V3.01	        2017 W. Lenerz
	section dv3

	xdef	qw1_drname

	xref	qw1_dlen

	xref	qw1_dre2
	xref	qw1_drloc
	xref	dv3_drloc
	xref	dv3_drnew
	xref	dv3_drupd
	xref	dv3_fbloc
	xref	dv3_iqdt
	xref	cv_lctab

	include 'dev8_dv3_keys'
	include 'dev8_keys_hdr'
	include 'dev8_keys_err'
	include 'dev8_mac_assert'
	include 'dev8_keys_qlw1'
	include 'dev8_keys_dos'

;+++
; DV3 Look for name in directory
;
;	d2 c  p byte set if directory entry to be filled
;	d3 c  u type of search / bit 31 set if file is a directory
;	d6  r	file ID
;	d7 c  p drive ID / number
;	a0 c  p pointer to channel block
;	a1 c  p pointer to name
;	a2 c  p pointer to old channel block (rename only)
;	a3 c  p pointer to linkage block
;	a4 c  p pointer to drive definition block
;
;	status return standard
;
;---
qw1_drname
qdn.reg reg	d1/d2/d4/d5/a1/a2/a3/a5
stk_empty equ	$00
stk_save  equ	$04	; save ID / name length / position
stk_svid  equ	$04
stk_svlen equ	$06
stk_svpos equ	$08

stk_sav2  equ	$0c	; second level save
qdn.frame equ	$14
stk_sden  equ	qdn.frame+1*4+3
stk_name  equ	qdn.frame+4*4
stk_old   equ	qdn.frame+5*4
stk_link  equ	qdn.frame+6*4

	movem.l qdn.reg,-(sp)
	clr.l	-(sp)
	clr.l	-(sp)
	clr.l	-(sp)
	clr.l	-(sp)
	clr.l	-(sp)

	moveq	#hdr.len,d0
	add.l	d0,d3c_sdlen(a0)	 ; adjust root directory for header
	tst.w	(a1)			 ; any name?
	beq.l	qw1_ckdr		 ; ... no

	lea	cv_lctab,a5
qw1_dloop				 ; loop for each(sub-)directory
	moveq	#0,d6			 ; initial file pointer
	moveq	#0,d4			 ; in byte / sector form
	moveq	#0,d5			 ; sector of dir being examined
	move.l	d5,stk_empty(sp)

	jsr	dv3_drloc		 ; locate first sector
	bne.l	qw1_err 		 ; ... oops

qw1_eloop				 ; loop searching each entry in a dir
	moveq	#hdr.len,d0
	add.l	d0,d6			 ; new directory file pointer position
	add.w	d0,d4			 ; new position within directory sector

	cmp.l	d3c_sdlen(a0),d6	 ; end of file ?
	bhs.s	qw1_not_found		 ; ... yes, not found

	cmp.w	ddf_slen(a4),d4 	 ; end of sector ?
	blo.s	qw1_check		 ; ... no, continue search in this one

	moveq	#0,d4			 ; new sector start search from beginning
	addq.w	#1,d5			 ; next sector
	jsr	dv3_drloc		 ; locate next sector
	bne.l	qw1_err 		 ; ... oops

qw1_check
	tst.w	hdr_flid(a2,d4.w)	 ; vacant directory entry?
	beq.s	qw1_empty		 ; ... yes
	cmp.b	#hdrt.dir,hdr_type(a2,d4.w) ; directory type?
	bne.l	qw1_cknm		 ; ... no, check name

; this entry in the dir is a sub-directory
	bsr.l	qw1_ckdn		 ; check sub-directory name
	bne.s	qw1_eloop		 ; ... no match

; this is a sub-directory and name matches; pop into it and continue searching

; the root dir ID is always 0. This would clash with the "no dir above" ID
; so, if the dir to be saved is the root dir, set its saved ID to -1
	move.w	d3c_sdid+2(a0),d0	 ; ID of containing directory
	bne.s	qw1_nr1 		 ; ... it isn't root dir
	moveq	#-1,d0			 ; ... it's the root dir, so fudge it
qw1_nr1
	move.w	hdr_name(a2,d4.w),d2	 ; name length of sub-directory
	movem.w d0/d2/d4/d5,stk_save(sp) ; save directory ID and positions
	movem.l d0/d1,-(a7)
	move.w	hdr_flid(a2,d4.w),d1	 ; file ID of sub-dir
	move.w	d1,d3c_sdid+2(a0)	 ; down one directory level
	jsr	qw1_dlen		 ; get subdir length
	move.l	d0,d3c_sdlen(a0)	 ; set in channel defn
	movem.l (a7)+,d0/d1

qw1_edir
	addq.w	#1,d2			 ; length of dir, inc _
	cmp.w	(a1),d2 		 ; length of name required vs sdir
	bgt.s	qw1_ckdr		 ; ... shorter, it is a directory
	blt.s	qw1_dloop		 ; ... sdir (inc _) is shorter, carry on

	bset	#31,d3			 ; it is possibly open directory
	bra.s	qw1_dloop

qw1_empty				 ; vacant slot found
	tst.l	stk_empty(sp)		 ; have we already found an empty entry?
	bne.s	qw1_eloop		 ; ... yes
	move.l	d6,stk_empty(sp)	 ; ... no, save the pointer to this one
	bra.s	qw1_eloop		 ; and go around again with this one

; name not found - but if MSB of d3 set, it was a directory

qw1_not_found
	tst.l	d3			 ; was it a directory?
	bpl.s	qw1_nofile		 ; ... no

; a directory has been found if this is remove, it must be empty
; otherwise open type may be open old file or open directory

qw1_ckdr
	tst.b	d3			 ; remove
	blt.l	qw1_rmdr		 ; ... yes
	cmp.b	#ddf.new,d3		 ; what open type?
	blt.s	qw1_sdir		 ; ... open directory : is OK
	beq.l	qw1_fdex		 ; ... open new : error already exists
	bra.l	qw1_fdiu		 ; ... any other open type : in use

; no file has been found

qw1_nofile
	tst.b	d3			 ; is it open directory?
	bgt.l	qw1_new 		 ; ... no, check for new entry
	blt.l	qw1_nf			 ; ... really not found, remove nothing

; open a subdir
qw1_sdir
	movem.w stk_save(sp),d0/d2/d4/d5 ; get directory above
	tst.w	d2			 ; root dir (name)?
	beq.s	qw1_sdlen		 ; ... yes

	move.b	#'_',2(a1,d2.w) 	 ; no, add underscore at end
	addq.w	#1,d2			 ; one more char
qw1_sdlen
	move.w	d2,(a1) 		 ; new name length
	bset	#31,d3			 ; directory found

	moveq	#-hdr.len,d1
	add.l	d3c_sdlen(a0),d1	 ; file length without header
	move.l	d1,d3c_flen(a0) 	 ; set directory length
	st	d3c_type(a0)		 ; and type
	move.b	#dos.subd,d3c_dsattr(a0) ; it's a subdir
	st	d3c_denum(a0)

	mulu	ddf_slen(a4),d5 	 ; sector number in dir * sect length
	add.w	d4,d5			 ; +offset in sector = position for entry number

	move.l	d3c_sdid(a0),d6 	 ; file ID
	move.w	d0,d3c_sdid+2(a0)	 ; and (sub)directory ID

	moveq	#0,d0			 ; OK!!
	bra.s	qw1_sete		 ; and set entry

; check name
qw1_cknm
	bsr.l	qw1_ckfn		 ; characters match?
	bne.l	qw1_eloop		 ; no, it is not the same, check next dir entry
; here chars match
	tst.b	d3			 ; is it open directory?
	beq.s	qw1_sdir		 ; ... yes
	bmi.s	qw1_remv		 ; ... no, it's remove file
	cmp.b	#ddf.new,d3		 ; other open type, it is open new?
	beq.s	qw1_fdex		 ; ... yes, signal not found (which would be ok)
	cmp.b	#ddf.rename,d3		 ; ... no, is it rename?
	beq.s	qw1_fdex		 ; ... yes ????????????

; file was found and an open old file was requested
qw1_setn
	bclr	#31,d3			 ; ... yes, it's not a directory
	move.l	d6,d5			 ; so set entry number
	tst.b	stk_sden(sp)		 ; directory entry required?
	beq.s	qw1_seti
	add.w	d4,a2			 ; un-scrumple directory entry
	jsr	qw1_dre2
	sub.w	d4,a2
qw1_seti
	moveq	#0,d6
	move.w	hdr_flid(a2,d4.w),d6	 ; set file ID

qw1_sete				 ; d5 = position of entry in directory
	lsr.l	#hdr.sft,d5		 ; entry
	subq.l	#1,d5
	move.l	d5,d3c_sdent(a0)	 ; sub-directory entry number
qw1_ok
	moveq	#0,d0

qw1_exit
	add.w	#qdn.frame,sp
	movem.l (sp)+,qdn.reg
	rts

qw1_fdex
	moveq	#err.fex,d0
	bra.s	qw1_err
qw1_nf
	moveq	#err.fdnf,d0
	bra.s	qw1_err
qw1_fdiu
	moveq	#err.fdiu,d0
	bra.s	qw1_err
qw1_drfl
	moveq	#err.drfl,d0

qw1_err
	moveq	#0,d6
	tst.l	d0
	bra.s	qw1_exit

; remove (directory or) file from directory

qw1_rmdr
	movem.w stk_save(sp),d0/d2/d4/d5 ; restore directory above
	cmp.w	#-1,d0			 ; was it root?
	bne.s	nr2
	clr.w	d0
nr2
	move.l	d3c_sdid(a0),d6 	 ; to make this dir a file
	move.w	d0,d3c_sdid+2(a0)
	move.l	d3c_sdlen(a0),d0	 ; directory length (inc header)

	bsr.l	qw1_ckde		 ; check directory empty
	bne.s	qw1_err
	jsr	dv3_drloc		 ; find our entry again
	bne.s	qw1_err

	mulu	ddf_slen(a4),d5
	add.w	d4,d5			 ; position for entry number
	move.l	d5,d6			 ; shuffle it

qw1_remv
	add.w	d4,a2
	move.l	d6,d5
	moveq	#0,d6
	move.w	hdr_flid(a2),d6 	 ; set file ID
	bsr.l	qw1_clent		 ; clear everything!
	sub.w	d4,a2
	jsr	dv3_drupd		 ; sector updated
	bra.s	qw1_sete		 ; set return

qw1_rename
	move.l	d3c_flid(a2),d6
	move.l	d6,d3c_flid(a0) 	 ; copy old ID
	move.l	d3c_sdent(a2),d3c_sdent(a0) ; and entry number
	jsr	qw1_drloc		 ; get our entry
	bne.s	qw1_exit

	bsr.l	qw1_setnm		 ; set name

	jsr	dv3_drupd		 ; director entry updated
	bra	qw1_ok

; create new entry (only for new, either or rename)

qw1_new
	cmp.b	#ddf.new,d3		 ; is it new, either or rename?
	blt.s	qw1_nf			 ; ... no
	cmp.b	#ddf.rename,d3		 ; rename?
	blt.s	qw1_adde		 ; ... no, add entry
	move.l	stk_old(sp),a2		 ; old channel block
	move.l	d3c_sdid(a2),d0
	cmp.l	d3c_sdid(a0),d0 	 ; same sub-directory?
	beq.s	qw1_rename		 ; ... yes, it is just a new name

qw1_adde
	tst.l	ddf_afree(a4)		 ; any free sectors?
	beq	qw1_drfl		 ; ... no

	move.l	stk_empty(sp),d0	 ; empty hole somewhere in dir?
	bne.s	qw1_gbd0		 ; ... yes, get buffer etc

	cmp.w	ddf_slen(a4),d4 	 ; is there still room at end of dir?
	blo.s	qw1_xdir		 ; ... yes, there is room at end

	moveq	#0,d4
	addq.w	#1,d5			 ; next sector

	move.l	d5,d0
	divu	ddf_asect(a4),d0	 ; first in new group?
	swap	d0
	tst.w	d0
	bne.s	qw1_drnew		 ; ... no

	cmp.l	#2,ddf_afree(a4)	 ; are there at least two free clusters
					 ; (for dir + new file)
	blo	qw1_drfl		 ; ... no
qw1_drnew
	jsr	dv3_drnew		 ; allocate new cluster
	blt	qw1_err 		 ; cannot do

; Qubide (or at least QL-SD) apparently doesn't set the length of a subdir in
; the corresponding entry in its containing dir -this is always $40!. That is
; doubly annoying. First of all, I have to peruse the entire FAT to find out how
; many clusters are allocated to the dir and then calcualte how "long" it is
; (see qx1_dlen in qw1_check_asm) -  and then the length isn't the real length
; of the dir as SMSQ/E normally understands it, but simply the length of all
; sectors.
; Second, and more importantly here, when the dir needs to be extended, a
; slave block is allocated to the cluster newly reserved for that dir. This
; slave block may well be one that was previously allocated for some other file.
; This means that is is NOT EMPTY which in turn means that when I try to find a
; hole in the dir entries for a new file to be opened, I may get it wrong since
; I don't know where the dir ends and have to check all potential entries, which
; may be NOT EMPTY even though they should be.
; The only issue I see to this is that for a new directory cluster allocated, I
; must entirely clear the corresponding slave block.

; For directory entries, I must clear the slave block buffer!
; On return from dv3_drnew, A2 points to the slave block buffer, containing
; one slave block which is 512 bytes long (sbt.size).

zapdreg reg	d0/a2
qw1_zapd
	movem.l zapdreg,-(a7)
	moveq	#sbt.size/4-1,d0
qw1_zplp
	clr.l	(a2)+
	dbf	d0,qw1_zplp
	movem.l (a7)+,zapdreg
qw1_xdir
	tst.w	stk_save(sp)		 ; any directory above?
	bne.s	qw1_gbuf		 : no


; root dir handling
; From what I understand, the root dir length is, sort of, contained in the
; the root dir header itself. The "length" there is as
; if the directory was as just long enough to encompass the last file in it.

; so: When a file is deleted, the length isn't changed, unless it's the last
; file in the dir.
; when a file is added, the length is changed if the file was added to the end
; of the dir.

qw1_xroot
;	 move.l  d6,ddf_rdlen(a4)	  ; new root directory length
;	 bra.s	 qw1_gbuf		  ; d6 is posn in hdr, ddf_rdlen is len exc hdr

qw1_gbd0
	move.l	d0,d6			 ; new position
	move.w	ddf_smask+2(a4),d4
	and.w	d0,d4			 ; new sector/byte position
	divu	ddf_slen(a4),d0
	moveq	#0,d5
	move.w	d0,d5

qw1_gbuf
	jsr	dv3_drloc		 ; get appropriate sector of directory
	bne.l	qw1_err
	cmp.b	#ddf.rename,d3		 ; rename
	beq.s	qw1_move

; now a brief diversion to allocate the first group of the file

	move.l	d6,d5			 ; to set entry on exit
qw1_id0
	moveq	#0,d6			 ; ID 0
qw1_afsec
	moveq	#0,d2			 ; first sector
	jsr	ddf_salloc(a4)		 ; (resets d6)
	blt.l	qw1_err

; finally set the new directory entry

	add.w	d4,a2
	bsr.l	qw1_clent		 ; clear entry
	move.l	ddf_fhlen(a4),hdr_flen(a2); set length
	bsr.l	qw1_setnm		 ; set name
	move.l	d6,hdr_vers(a2) 	 ; version zero / file ID

	jsr	dv3_drupd		 ; sector updated

; check whether root dir length needs to be updated
	tst.w	stk_save(sp)		 ; any directory above?
	bne.s	qw1_nr3 		 ; ... yes, this is not root, done
	tst.l	stk_empty(sp)		 ; was it an empty hole somwhere in dir?
	bne.s	qw1_nr3 		 ; ... yes, root dir length needn't change
	moveq	#0,d5
	jsr	dv3_drloc		 ; get 1st sector of root dir
;	 moveq	 #hdr.len,d0
	add.l	#hdr.len,(a2)		 ; set new length
	jsr	dv3_drupd		 ; and update this
qw1_nr3
	moveq	#0,d0			 ; OK
	bra.l	qw1_sete		 ; set SD entry number

; renamed file needs to be moved from one directory to another

qw1_move
	lsr.l	#hdr.sft,d6		 ; entry
	subq.l	#1,d6
	move.l	d6,d3c_sdent(a0)	 ; sub-directory entry number
	move.l	a0,-(sp)		 ; save new channel block
	lea	(a2,d4.l),a1		 ; ... and entry

	move.l	stk_old+4(sp),a0	 ; old channel block
	move.l	d3c_flid(a0),d6
	jsr	qw1_drloc

	moveq	#hdr.len/4-1,d0
qw1_mhdr
	move.l	(a2),(a1)+		 ; copy old directory entry
	clr.l	(a2)+			 ; and clear it
	dbra	d0,qw1_mhdr

	jsr	dv3_drupd		 ; old entry updated

	lea	-hdr.len(a1),a2 	 ; new entry
	move.l	a0,a1			 ; old channel block
	move.l	(sp)+,a0		 ; new channel block

	bsr.s	qw1_setnm		 ; set new name in entry

qw1_mset
	jsr	dv3_drupd		 ; mark new dir entry updated
	move.l	d6,d3c_flid(a0) 	 ; set (new) ID in channel block
	bra	qw1_ok

qw1_flush
	jsr	ddl_dflush(a3)		 ; flush all buffers before change ID
	addq.l	#-err.nc,d0
	beq.s	qw1_flush
	rts


; routine to check name against directory name

qw1_ckdn
	move.w	hdr_name(a2,d4.w),d2	 ; length of name to check against
	cmp.w	(a1),d2
	bgt.s	qw1_rts 		 ; ... filename is too short
	beq.s	qw1_ckfd		 ; ... exact
	cmp.b	#'_',2(a1,d2.w) 	 ; ... longer, '_' in the right place?
	beq.s	qw1_ckfd		 ; ... ... yes
qw1_rts
	rts


; routine to check name against file name

qw1_ckfn
	move.w	hdr_name(a2,d4.w),d2
	cmp.w	(a1),d2
	bne.s	qw1_rts
qw1_ckfd
	lea	hdr_name+2(a2,d4.w),a3	 ; start of characters
	move.w	stk_svlen+4(sp),d0	 ; amount already checked
	sub.w	d0,d2			 ; amount to check
	lea	2(a1,d0.w),a1		 ; chars to check
	add.w	d0,a3
	move.w	d2,d0			 ; save length
	moveq	#0,d1
	bra.s	qw1_ckle
qw1_cklp
	move.b	(a3)+,d1		 ; real name character
	move.b	(a5,d1.w),d1
	cmp.b	(a1)+,d1		 ; matches?
qw1_ckle
	dbne	d2,qw1_cklp
	bne.s	qw1_ra1

	bra.s	qw1_cple		 ; match
qw1_cplp
	move.b	-(a3),-(a1)		 ; ... copy back real name characters
qw1_cple
	dbra	d0,qw1_cplp

	moveq	#0,d0

qw1_ra1
	move.l	stk_name+4(sp),a1	 ; restore a1
	move.l	stk_link+4(sp),a3	 ; restore a3
	rts

; routine to clear directory entry
;	d0   s
;	d1   s
;	a2 c  p pointer to entry
;
qw1_clent
	moveq	#hdr.len/4-1,d1
qw1_cled1
	moveq	#0,d0			 ; most efficient clear
	move.l	a2,-(sp)
qw1_clloop
	move.l	d0,(a2)+
	dbra	d1,qw1_clloop
	move.l	(sp)+,a2
	rts

; routine to set name in directory entry
;	d0   s
;	d1   s
;	a1 c  p pointer to channel block
;	a2 c  p pointer to entry
;	a5   s
;
qw1_setnm
	movem.l a1/a2,-(sp)
	lea	dv3_iqdt,a5		 ; internal to QL
	lea	d3c_name(a0),a1
	lea	hdr_name(a2),a2
	moveq	#0,d1
	move.w	(a1)+,d0		 ; length of name
	move.w	d0,(a2)+
	bra.s	qw1_snle
qw1_snloop
	move.b	(a1)+,d1
	move.b	(a5,d1.w),(a2)+ 	 ; translate
qw1_snle
	dbra	d0,qw1_snloop
	movem.l (sp)+,a1/a2
	rts


; check if directory empty  (ID d6, length d0)

qw1_ckde
qdce.reg reg	d1/d3/d4/d5/a2
	movem.l qdce.reg,-(sp)

	moveq	#hdr.len,d1
	move.l	d0,d3			 ; end of the file
	sub.l	d1,d3			 ; start of last entry
	ble.s	qdce_ok 		 ; nothing!!
	move.l	d3,d4
	divu	ddf_slen(a4),d4
	moveq	#0,d5
	move.w	d4,d5			 ; sector
	swap	d4			 ; byte
qdce_fbloc
	jsr	dv3_fbloc		 ; ... locate file buffer
	bne.s	qdce_exit

qdce_loop
	tst.w	hdr_flid(a2,d4.w)	 ; empty?
	bne.s	qdce_iu 		 ; ... no
	sub.l	d1,d3			 ; previous entry
	ble.s	qdce_ok 		 ; ... there is none
	sub.w	d1,d4			 ; previous entry
	bge.s	qdce_loop
	move.w	ddf_slen(a4),d4 	 ; go back a sector
	sub.w	d1,d4
	subq.w	#1,d5
	bra.s	qdce_fbloc

qdce_ok
	moveq	#0,d0
qdce_exit
	movem.l (sp)+,qdce.reg
	rts
qdce_iu
	moveq	#err.fdiu,d0
	bra.s	qdce_exit
	end
