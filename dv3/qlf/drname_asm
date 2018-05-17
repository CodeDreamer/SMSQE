; DV3 QL Directory Name 		V3.00	        1992 Tony Tebby

	section dv3

	xdef	qlf_drname

	xref	qlf_dre2
	xref	qlf_drloc
	xref	qlf_idfr
	xref	qlf_idtr
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
qlf_drname
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
	beq.l	qdn_ckdr		 ; ... no

	lea	cv_lctab,a5
qdn_dloop
	moveq	#0,d6			 ; initial file pointer
	moveq	#0,d4			 ; in byte / sector form
	moveq	#0,d5
	move.l	d5,stk_empty(sp)

	jsr	dv3_drloc		 ; locate first sector
	bne.l	qdn_err 		 ; ... oops

qdn_eloop
	moveq	#hdr.len,d0
	add.l	d0,d6
	add.w	d0,d4

	cmp.l	d3c_sdlen(a0),d6	 ; end of file
	bhs.s	qdn_not_found		 ; ... yes, not found

	cmp.w	ddf_slen(a4),d4 	 ; end of sector
	blo.s	qdn_check

	moveq	#0,d4
	addq.w	#1,d5			 ; next sector
	jsr	dv3_drloc		 ; locate next sector
	bne.l	qdn_err 		 ; ... oops

qdn_check
	tst.w	hdr_name(a2,d4.w)	 ; vacant?
	beq.s	qdn_empty		 ; ... yes
	cmp.b	#hdrt.dir,hdr_type(a2,d4.w) ; directory type?
	bne.l	qdn_cknm		 ; ... no
;;;	   tst.l   d3			    ; are we just looking for no name?
;;;	   bmi.s   qdn_eloop		    ; ... yes, ignore directory

	bsr.l	qdn_ckdn		 ; check directory name
	bne.s	qdn_eloop		 ; ... no match

	move.w	d3c_sdid+2(a0),d0
	move.w	hdr_name(a2,d4.w),d2
	movem.w d0/d2/d4/d5,stk_save(sp) ; save directory ID and positions

	move.w	hdr_flid(a2,d4.w),d3c_sdid+2(a0) ; down one directory level
	move.l	hdr_flen(a2,d4.w),d3c_sdlen(a0)

	cmp.b	#ddf.ql5b,ddf_stype(a4)  ; implicit file ID in root directory?
	bgt.s	qdn_edir		 ; ... no
	subq.w	#ddf.rdid,d0		 ; in root?
	bne.s	qdn_edir		 ; ... no
	lsr.l	#hdr.sft,d6
	addq.w	#1,d6			 ; file ID is position+1
	move.w	d6,d3c_sdid+2(a0)	 ; set file ID

qdn_edir
	addq.w	#1,d2			 ; length of dir, inc _
	cmp.w	(a1),d2 		 ; length of name required vs sdir
	bgt.s	qdn_ckdr		 ; ... shorter, it is a directory
	blt.s	qdn_dloop		 ; ... sdir (inc _) is shorter, carry on

;;;	   tst.b   d3			    ; open directory?
;;;	   beq.s   qdn_sdir		    ; ... yes
	bset	#31,d3			 ; it is possibly open directory
	bra.l	qdn_dloop

qdn_empty
	tst.l	stk_empty(sp)		 ; have we already found an empty entry?
	bne.s	qdn_eloop		 ; ... yes
	move.l	d6,stk_empty(sp)	 ; ... no, save the pointer to this one
	bra.l	qdn_eloop

; name not found - but if MSB of d3 set, it was a directory

qdn_not_found
	tst.l	d3			 ; was it a directory?
	bpl.s	qdn_nofile		 ; ... no

; a directory has been found if this is remove, it must be empty
;			     otherwise it may be old or directory

qdn_ckdr
	tst.b	d3			 ; remove
	blt.l	qdn_rmdr		 ; ... yes
	cmp.b	#ddf.new,d3
	blt.s	qdn_sdir		 ; ... directory
	beq.l	qdn_fdex		 ; ... new
	bra.l	qdn_fdiu		 ; ... either

; no file has been found

qdn_nofile
	tst.b	d3			 ; is it open directory?
	bgt.l	qdn_new 		 ; ... no, check for new entry
	blt.l	qdn_nf			 ; ... remove nothing

qdn_sdir
	movem.w stk_save(sp),d0/d2/d4/d5 ; get directory above
	tst.w	d2			 ; root?
	beq.s	qdn_sdlen
	move.b	#'_',2(a1,d2.w) 	 ; underscore at end
	addq.w	#1,d2
qdn_sdlen
	move.w	d2,(a1)
	bset	#31,d3			 ; directory found

	moveq	#-hdr.len,d1
	add.l	d3c_sdlen(a0),d1
	move.l	d1,d3c_flen(a0) 	 ; set directory length
	st	d3c_type(a0)		 ; and type
	move.b	#dos.subd,d3c_dsattr(a0)
	st	d3c_denum(a0)

	mulu	ddf_slen(a4),d5
	add.w	d4,d5			 ; position for entry number

	move.l	d3c_sdid(a0),d6 	 ; file ID
	move.w	d0,d3c_sdid+2(a0)	 ; and (sub)directory ID

	moveq	#0,d0			 ; OK!!
	bra.s	qdn_sete		 ; and set entry

qdn_cknm
	bsr.l	qdn_ckfn		 ; characters match?
	bne.l	qdn_eloop		 ; ... no, it is not the same
	tst.b	d3			 ; ... yes, is it open directory?
	beq.s	qdn_sdir		 ; ... yes
	bmi.l	qdn_remv		 ; ... no, remove file
	cmp.b	#ddf.new,d3		 ; new?
	beq.s	qdn_fdex
	cmp.b	#ddf.rename,d3		 ; rename?
	beq.s	qdn_fdex

qdn_setn
	bclr	#31,d3			 ; ... yes, it's not a directory
	move.l	d6,d5			 ; so set entry number
	tst.b	stk_sden(sp)		 ; directory entry required?
	beq.s	qdn_seti
	add.w	d4,a2			 ; un-scrumple directory entry
	jsr	qlf_dre2
	sub.w	d4,a2

qdn_seti
	assert	0,ddf.ql5a,ddf.ql5b-1,ddf.qlwa-2
	cmp.b	#ddf.ql5b,ddf_stype(a4)  ; implicit file ID in root directory?
	bgt.s	qdn_id			 ; ... no
	cmp.w	#ddf.rdid,d3c_sdid+2(a0) ; in root?
	bne.s	qdn_id			 ; ... no
	lsr.l	#hdr.sft,d6
	addq.w	#1,d6			 ; file ID is position+1
	bra.s	qdn_sete
qdn_id
	moveq	#0,d6
	move.w	hdr_flid(a2,d4.w),d6	 ; set file ID

qdn_sete
	lsr.l	#hdr.sft,d5		 ; entry
	subq.l	#1,d5
	move.l	d5,d3c_sdent(a0)	 ; sub-directory entry number

qdn_ok
	moveq	#0,d0

qdn_exit
	add.w	#qdn.frame,sp
	movem.l (sp)+,qdn.reg
	rts

qdn_fdex
	moveq	#err.fex,d0
	bra.s	qdn_err
qdn_nf
	moveq	#err.fdnf,d0
	bra.s	qdn_err
qdn_fdiu
	moveq	#err.fdiu,d0
	bra.s	qdn_err
qdn_drfl
	moveq	#err.drfl,d0

qdn_err
	moveq	#0,d6
	tst.l	d0
	bra.s	qdn_exit

; remove (directory or) file from directory

qdn_rmdr
	movem.w stk_save(sp),d0/d2/d4/d5 ; restore directory above
	move.l	d3c_sdid(a0),d6 	 ; to make this dir a file
	move.w	d0,d3c_sdid+2(a0)
	move.l	d3c_sdlen(a0),d0	 ; directory length (inc header)

	bsr.l	qdn_ckde		 ; check directory empty
	bne.s	qdn_err
	jsr	dv3_drloc		 ; find our entry again
	bne.s	qdn_err

	mulu	ddf_slen(a4),d5
	add.w	d4,d5			 ; position for entry number
	move.l	d5,d6			 ; shuffle it

qdn_remv
	add.w	d4,a2
	moveq	#(hdr_name+2-1)/4,d1	 ; clear up to an including name len
	bsr.l	qdn_cled1
	sub.w	d4,a2
	jsr	dv3_drupd		 ; sector updated
	move.l	d6,d5			 ; entry number
	bra.s	qdn_seti		 ; set return

qdn_rename
	move.l	d3c_flid(a2),d6
	move.l	d6,d3c_flid(a0) 	 ; copy old ID
	move.l	d3c_sdent(a2),d3c_sdent(a0) ; and entry number
	jsr	qlf_drloc		 ; get our entry
	bne.s	qdn_exit

	bsr.l	qdn_setnm		 ; set name

	jsr	dv3_drupd		 ; director entry updated
	bra.s	qdn_ok

; create new entry (only for new, either or rename)

qdn_new
	cmp.b	#ddf.new,d3		 ; is it new, either or rename?
	blt.s	qdn_nf			 ; ... no
	cmp.b	#ddf.rename,d3		 ; rename?
	blt.s	qdn_adde		 ; ... no, add entry
	move.l	stk_old(sp),a2		 ; old channel block
	move.l	d3c_sdid(a2),d0
	cmp.l	d3c_sdid(a0),d0 	 ; same sub-directory?
	beq.s	qdn_rename		 ; ... yes, it is just a new name

qdn_adde
	tst.l	ddf_afree(a4)		 ; any free sectors?
	beq.s	qdn_drfl		 ; ... no

	move.l	stk_empty(sp),d0	 ; empty hole?
	bne.s	qdn_gbd0		 ; ... yes, get buffer

	cmp.w	ddf_slen(a4),d4
	blo.s	qdn_xdir		 ; ... there is room at end

	moveq	#0,d4
	addq.w	#1,d5			 ; next sector

	move.l	d5,d0
	divu	ddf_asect(a4),d0	 ; first in new group?
	swap	d0
	tst.w	d0
	bne.s	qdn_drnew		 ; ... no

	cmp.l	#2,ddf_afree(a4)	 ; at least two allocation units
	blo	qdn_drfl		 ; ... no
qdn_drnew
	jsr	dv3_drnew
	blt	qdn_err 		 ; cannot do

qdn_xdir
	tst.w	stk_save(sp)		 ; any directory above?
	beq.s	qdn_xroot		 ; extend root directory

	move.w	d3c_sdid+2(a0),d0
	movem.w d0/d2/d4/d5,stk_sav2(sp) ; save current directory
	movem.w stk_save(sp),d0/d2/d4/d5 ; get the one above
	move.w	d0,d3c_sdid+2(a0)
	jsr	dv3_drloc
	bne.l	qdn_err

	moveq	#hdr.len,d0
	add.l	d6,d0
	move.l	d0,hdr_flen(a2,d4.w)	 ; new file length
	jsr	dv3_drupd		 ; updated

	movem.w stk_sav2(sp),d0/d2/d4/d5 ; restore directory
	move.w	d0,d3c_sdid+2(a0)
	bra.s	qdn_gbuf

qdn_xroot
	move.l	d6,ddf_rdlen(a4)	 ; new root directory length
	bra.s	qdn_gbuf	 ; d6 is posn in hdr, ddf_rdlen is len exc hdr

qdn_gbd0
	move.l	d0,d6			 ; new position
	move.w	ddf_smask+2(a4),d4
	and.w	d0,d4			 ; new sector/byte position
	divu	ddf_slen(a4),d0
	moveq	#0,d5
	move.w	d0,d5

qdn_gbuf
	jsr	dv3_drloc		 ; get appropriate sector of directory
	bne.l	qdn_err
	cmp.b	#ddf.rename,d3		 ; rename
	beq.s	qdn_move

; now a brief diversion to allocate the first group of the file

	move.l	d6,d5			 ; to set entry on exit
	lsr.l	#hdr.sft,d6		 ; entry
	addq.w	#1,d6			 ; 0 / file ID
	assert	0,ddf.ql5a,ddf.ql5b-1,ddf.qlwa-2
	cmp.b	#ddf.ql5b,ddf_stype(a4)  ; implicit file ID in root directory?
	bgt.s	qdn_id0 		 ; ... no
	cmp.w	#ddf.rdid,d3c_sdid+2(a0) ; in root?
	beq.s	qdn_afsec		 ; ... no
qdn_id0
	moveq	#0,d6			 ; ID 0
qdn_afsec
	moveq	#0,d2			 ; first sector
	jsr	ddf_salloc(a4)		 ; (resets d6)
	blt.l	qdn_err

; finally set the new directory entry

	add.w	d4,a2
	bsr.l	qdn_clent		 ; clear entry
	move.l	ddf_fhlen(a4),hdr_flen(a2); set length
	bsr.l	qdn_setnm		 ; set name
	move.l	d6,hdr_vers(a2) 	 ; version zero / file ID

	jsr	dv3_drupd		 ; sector updated
	moveq	#0,d0			 ; OK
	bra.l	qdn_sete		 ; set SD entry number

; renamed file needs to be moved from one directory to another

qdn_move
	lsr.l	#hdr.sft,d6		 ; entry
	subq.l	#1,d6
	move.l	d6,d3c_sdent(a0)	 ; sub-directory entry number
	move.l	a0,-(sp)		 ; save new channel block
	lea	(a2,d4.l),a1		 ; ... and entry

	move.l	stk_old+4(sp),a0	 ; old channel block
	move.l	d3c_flid(a0),d6
	jsr	qlf_drloc

	moveq	#hdr.len/4-1,d0
qdn_mhdr
	move.l	(a2),(a1)+		 ; copy old directory entry
	clr.l	(a2)+			 ; and clear it
	dbra	d0,qdn_mhdr

	jsr	dv3_drupd		 ; old entry updated

	lea	-hdr.len(a1),a2 	 ; new entry
	move.l	a0,a1			 ; old channel block
	move.l	(sp)+,a0		 ; new channel block

	bsr.l	qdn_setnm		 ; set new name in entry

	assert	ddf.ql5a,0
	cmp.b	#ddf.ql5b,ddf_stype(a4)  ; old QDOS?
	bgt.s	qdn_mset		 ; ... no

	cmp.w	#ddf.rdid,d3c_sdid+2(a1) ; was old in root?
	beq.s	qdn_mfroot		 ; ... yes, move from root
	cmp.w	#ddf.rdid,d3c_sdid+2(a0) ; is new in root?
	bne.s	qdn_mset
	bsr.s	qdn_flush		 ; ... beware, flush sbs
	move.w	d3c_sdent+2(a0),d1
	addq.w	#2,d1			 ; new ID is real entry + 1
	jsr	qlf_idtr		 ; move to root
	bra.s	qdn_midset
qdn_mfroot
	jsr	qlf_idfr		 ; move from root
qdn_midset
	move.w	d6,hdr_flid(a2) 	 ; and set ID in new entry

qdn_mset
	jsr	dv3_drupd		 ; mark new dir entry updated
	move.l	d6,d3c_flid(a0) 	 ; set (new) ID in channel block
	bra	qdn_ok

qdn_flush
	jsr	ddl_dflush(a3)		 ; flush all buffers before change ID
	addq.l	#-err.nc,d0
	beq.s	qdn_flush
	rts


; routine to check name against directory name

qdn_ckdn
	move.w	hdr_name(a2,d4.w),d2	 ; length of name to check against
	cmp.w	(a1),d2
	bgt.s	qdn_rts 		 ; ... filename is too short
	beq.s	qdn_ckfd		 ; ... exact
	cmp.b	#'_',2(a1,d2.w) 	 ; ... longer, '_' in the right place?
	beq.s	qdn_ckfd		 ; ... ... yes
qdn_rts
	rts


; routine to check name against file name

qdn_ckfn
	move.w	hdr_name(a2,d4.w),d2
	cmp.w	(a1),d2
	bne.s	qdn_rts
qdn_ckfd
	lea	hdr_name+2(a2,d4.w),a3	 ; start of characters
	move.w	stk_svlen+4(sp),d0	 ; amount already checked
	sub.w	d0,d2			 ; amount to check
	lea	2(a1,d0.w),a1		 ; chars to check
	add.w	d0,a3
	move.w	d2,d0			 ; save length
	moveq	#0,d1
	bra.s	qdn_ckle
qdn_cklp
	move.b	(a3)+,d1		 ; real name character
	move.b	(a5,d1.w),d1
	cmp.b	(a1)+,d1		 ; matches?
qdn_ckle
	dbne	d2,qdn_cklp
	bne.s	qdn_ra1

	bra.s	qdn_cple		 ; match
qdn_cplp
	move.b	-(a3),-(a1)		 ; ... copy back real name characters
qdn_cple
	dbra	d0,qdn_cplp

	moveq	#0,d0

qdn_ra1
	move.l	stk_name+4(sp),a1	 ; restore a1
	move.l	stk_link+4(sp),a3	 ; restore a3
	rts

; routine to clear directory entry
;	d0   s
;	d1   s
;	a2 c  p pointer to entry
;
qdn_clent
	moveq	#hdr.len/4-1,d1
qdn_cled1
	moveq	#0,d0			 ; most efficient clear
	move.l	a2,-(sp)
qdn_clloop
	move.l	d0,(a2)+
	dbra	d1,qdn_clloop
	move.l	(sp)+,a2
	rts

; routine to set name in directory entry
;	d0   s
;	d1   s
;	a1 c  p pointer to channel block
;	a2 c  p pointer to entry
;	a5   s
;
qdn_setnm
	movem.l a1/a2,-(sp)
	lea	dv3_iqdt,a5		 ; internal to QL
	lea	d3c_name(a0),a1
	lea	hdr_name(a2),a2
	moveq	#0,d1
	move.w	(a1)+,d0		 ; length of name
	move.w	d0,(a2)+
	bra.s	qdn_snle
qdn_snloop
	move.b	(a1)+,d1
	move.b	(a5,d1.w),(a2)+ 	 ; translate
qdn_snle
	dbra	d0,qdn_snloop
	movem.l (sp)+,a1/a2
	rts


; check if directory empty  (ID d6, length d0)

qdn_ckde
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
	tst.w	hdr_name(a2,d4.w)	 ; empty?
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
