; DV3 QL Make Directory 	    V3.00	    1992 Tony Tebby
; 2014.06.01  3.01  check for open files before making dir (wl)
	section dv3

	xdef	qlf_drmake

	xref	dv3_drloc
	xref	dv3_drupd
	xref	dv3_fbloc
	xref	dv3_fbnew
	xref	dv3_fbupd
	xref	qlf_drloc
	xref	qlf_idfr
	xref	cv_lctab

	include 'dev8_keys_hdr'
	include 'dev8_dv3_keys'
	include 'dev8_keys_err'
	include 'dev8_mac_assert'

;+++
; DV3 QL Format Make Directory
;
;	d6 c  p file ID
;	d7 c  p drive ID / number
;	a0 c  p pointer to channel block
;	a3 c  p pointer to linkage
;	a4 c  p pointer to drive definition
;
;	status return standard
;
;---
qlf_drmake
qdm.reg reg	d1/d2/d3/d4/d5/a1/a2/a5
	movem.l qdm.reg,-(sp)
	jsr	qlf_drloc		; get my directory entry (into A2)
	tst.b	d3c_type(a0)		; any file type?
	bne.l	qdm_iu			; ... yes
**  3.01
	bsr.l	chk_ext 		; check for an open file that would...
	bne.l	qdm_iu			; ... be moved: if there is, no can do
**
	st	d3c_type(a0)		; no filetype, set directory
	st	hdr_type(a2)

	move.w	d3c_name(a0),d2 	; length of name
;qdm_trnam
;	 tst.b	 d3c_name+1(a0,d2.w)	; ends in US?
;	 bne.s	 qdm_setd		; ... no
;	 subq.w  #1,d2			; ... yes, truncate a bit
;	 bne.s	 qdm_trnam
;	 bra.l	 qdm_iu 		; ROOT is in use!!

qdm_setd
	move.w	d2,hdr_name(a2) 	; set file name length
	addq.w	#1,d2
	move.w	d2,d3c_name(a0) 	; and directory length

	jsr	dv3_drupd		; updated entry

qdm_flush
	jsr	ddl_dflush(a3)		; flush entire drive
	addq.l	#-err.nc,d0		; because we may be fiddling with
	beq.s	qdm_flush		; the file IDs

; Now comes the interesting bit
; we need to transfer existing files to the new directory.
; So, we have an outer loop looking at the files in the same directory

	lea	cv_lctab,a5		; translation table
	lea	hdr_name+2(a2),a1	; directory name
	subq.w	#2,d2			; allow for dbra on length comp

	moveq	#0,d0
	moveq	#0,d1
	moveq	#0,d3
	moveq	#0,d4
	moveq	#0,d5
	jsr	dv3_drloc		; locate directory buffer
	bra.l	qdm_nxfile

qdm_floop
	cmp.w	hdr_name(a2,d4.w),d2	; long enough to be in directory?
	bgt.l	qdm_nxfile		; ... no

qdm.creg reg	d2/a0/a1
	movem.l qdm.creg,-(sp)

	lea	hdr_name+2(a2,d4.w),a0	; file name characters
qdm_chknm
	move.b	(a1)+,d1		; dir name character
	move.b	(a5,d1.w),d1
	move.b	(a0)+,d0
	sub.b	(a5,d0.w),d1		; matches filename?
	dbne	d2,qdm_chknm
	bne.s	qdm_chkdone
	cmp.b	#'_',(a0)		; must be followed by underscore

qdm_chkdone
	movem.l (sp)+,qdm.creg
	bne.l	qdm_nxfile

; Now we have a problem: we have to move this file to the directory.
; This seems to be a "move" without changing the name.

;	a0	is our channel block
;	a2,d4	is the old directory entry

qdm.mreg reg	d4/d5/a1/a2
	movem.l qdm.mreg,-(sp)		; save old entry and position
	lea	(a2,d4.w),a1		; this is pointer to entry itself

	move.l	d3c_feof(a0),d4 	; new directory entry
	moveq	#hdr.len,d0
	add.l	d4,d0
	move.l	d0,d3c_feof(a0)
	divu	ddf_slen(a4),d4 	;
	moveq	#0,d5
	move.w	d4,d5			; sector number
	swap	d4			;
	tst.w	d4			; start of entry in sector
	bne.s	qdm_fbloc		; locate (directory) file buffer

	jsr	dv3_fbnew		; a new sector is required
	beq.s	qdm_move		; ok, move it
	bne.s	qdm_mdone		; ... oops

qdm_fbloc
	jsr	dv3_fbloc		; locate old buffer
	bne.s	qdm_mdone

qdm_move
	add.w	d4,a2			; start of new dir entry

	moveq	#hdr.len/4-1,d0
qdm_copy
	move.l	(a1)+,(a2)+		; copy entry
	dbra	d0,qdm_copy

	assert	ddf.ql5a,0
	cmp.b	#ddf.ql5b,ddf_stype(a4) ; old format?
	bgt.s	qdm_fbupdt		; ... no
	cmp.w	#ddf.rdid,d3c_sdid+2(a0); is file in root?
	bne.s	qdm_fbupdt

	move.w	d6,-(sp)		; save our file ID
	move.l	d3,d6
	lsr.l	#hdr.sft,d6
	addq.w	#1,d6			; file ID is position+1
	move.w	d6,-(sp)		; save old file ID
	jsr	qlf_idfr		; change ID from root
	subq.l	#hdr.len-hdr_flid,a2	; file ID
	move.w	d6,(a2) 		; new file ID

; Oh No!!! The file ID has changed, we had better change all the channels to it

	move.w	(sp)+,d1		 ; this is the old ID

	move.l	a0,-(sp)		 ; save channel block

	move.l	ddf_chnlst(a4),d0	 ; linked list of channels for drive

qdm_chloop
	move.l	d0,a0
	cmp.w	d3c_flid+2(a0),d1	 ; old ID?
	bne.s	qdm_cheloop
	move.w	d6,d3c_flid+2(a0)	 ; ... yes, change it
qdm_cheloop
	move.l	d3c_link(a0),d0
	bne.s	qdm_chloop

	move.l	(sp)+,a0
	move.w	(sp)+,d6

qdm_fbupdt
	jsr	dv3_fbupd		 ; update file block
	moveq	#0,d0
qdm_mdone
	movem.l (sp)+,qdm.mreg
	bne.s	qdm_exit		 ; failure to move file into directory

	moveq	#hdr.len,d0
	add.w	d4,a2			 ; blat old entry
	add.l	d0,a2			 ; starting at end
qdm_blat
	clr.l	-(a2)
	subq.w	#4,d0			 ; going back to the beginning
	bgt.s	qdm_blat
	sub.w	d4,a2

	jsr	dv3_drupd		 ; directory sector updated

qdm_nxfile
	moveq	#hdr.len,d0
	add.l	d0,d3			 ; next file
	cmp.l	d3c_sdlen(a0),d3
	bge.s	qdm_done		 ; ... there is none

	add.w	d0,d4
	cmp.w	ddf_slen(a4),d4 	 ; end of sector?
	blt.l	qdm_floop
	moveq	#0,d4
	addq.w	#1,d5
	jsr	dv3_drloc
	bra.l	qdm_floop

qdm_done
	clr.l	d3c_updd(a0)		 ; update date is zero
	bset	#d3c..ust,d3c_usef(a0)	 ; say date set
	bset	#ddf..updd,d3c_setmask(a0) ; end ensure that it will be
	st	d3c_updt(a0)		 ; directory updated
	moveq	#0,d0
qdm_exit
	movem.l (sp)+,qdm.reg
	rts
qdm_iu
	moveq	#err.fdiu,d0
	bra.s	qdm_exit


;------------------------------- 3.01
; this checks whether any file which should be moved ino the newly created
; directory has channels open to it. If yes,  the direcory will not be created.

chk_reg reg	d1-d4/d6/a0-a2	   ; d2/d6/a1/a2/a5
chk_ext
	movem.l chk_reg,-(sp)
	lea	cv_lctab,a5		; translation table
	lea	hdr_name+2(a2),a1	; directory name
	move.w	d3c_name(a0),d2 	; length of name
	subq.w	#1,d2			; allow for dbra on length comp

	moveq	#0,d0
	moveq	#0,d1
	moveq	#0,d3
	moveq	#0,d4
	moveq	#0,d5
	jsr	dv3_drloc		; locate directory buffer
	bra.s	chk_nxfile

chk_floop
	cmp.w	hdr_name(a2,d4.w),d2	; long enough to be in directory?
	bgt.s	chk_nxfile		; ... no

	movem.l qdm.creg,-(sp)

	lea	hdr_name+2(a2,d4.w),a0	; file name characters

chk_chknm
	move.b	(a1)+,d1		; dir name character
	move.b	(a5,d1.w),d1
	move.b	(a0)+,d0
	sub.b	(a5,d0.w),d1		; matches filename?
	dbne	d2,chk_chknm
	bne.s	chk_chkdone
	cmp.b	#'_',(a0)		; must be followed by underscore

chk_chkdone
	movem.l (sp)+,qdm.creg
	bne.s	chk_nxfile		; no match

; we will have to move this file to the directory.
; check whether it has any channels open to it
; if yes, abort the directory creation

; a0	  is our channel block
; a2,d4   is the old directory entry

	clr.l	d6
	move.w	hdr_flid(a2,d4.w),d6	; fileID
	move.l	a0,-(a7)
	move.l	ddf_chnlst(a4),d0	; linked list of channels for drive
chk_chloop
	move.l	d0,a0
	cmp.w	d3c_flid+2(a0),d6	; old ID?
	beq.s	refuse			; yes, refuse to make dir
	move.l	d3c_link(a0),d0 	; point next channel on drive
	bne.s	chk_chloop		; there is one
	move.l	(a7)+,a0

chk_nxfile				; check the next file
	moveq	#hdr.len,d0
	add.l	d0,d3			; next file
	cmp.l	d3c_sdlen(a0),d3
	bge.s	chk_done		; ... there is none

	add.w	d0,d4
	cmp.w	ddf_slen(a4),d4 	; end of sector?
	blt.s	chk_floop		; no
	moveq	#0,d4
	addq.w	#1,d5
	jsr	dv3_drloc
	bra.s	chk_floop

chk_done
	moveq	#0,d0
chk_out movem.l (sp)+,chk_reg
	rts

refuse
	move.l	(sp)+,a0
	moveq	#err.fdiu,d0
	bra.s	chk_out

	end
