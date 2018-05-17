; DV3 Allocate / Locate New Buffer   V3.00    1992   Tony Tebby

	section dv3

	xdef	dv3_albf
	xdef	dv3_lcbf

	xref	dv3_sbloc
	xref	dv3_sbnew

	include 'dev8_dv3_keys'
	include 'dev8_keys_err'
	include 'dev8_mac_assert'

;+++
; DV3 allocate or locate a slave block for write operations.
; If necessary, it reads the appropriate sector from the disk or extends the
; file allocation.
;
;	d3  r	(word) bytes left in buffer
;	d4 c  p file position
;	d5  r	file sector
;	d6 c  p file ID
;	d7 c  p drive number
;	a0 c  p channel block
;	a2  r	pointer to buffer
;	a3 c  p linkage block
;	a4 c  p definition block
;	a5 c  p pointer to map
;
;	status return standard
;---
dv3_albf
	cmp.l	d3c_feof(a0),d4 	 ; end of file?
	blt.s	dv3_lcbf		 ; ... no, locate

	move.l	d3c_fsect(a0),d5
	move.l	ddf_smask(a4),d3	 ; byte within sector
	and.w	d4,d3
	bne.s	dlb_loc1		 ; not the start of a new sector

	addq.w	#1,d5			 ; next sector
	bcc.s	dab_do			 ; ... current version can only cope
					 ; with sector number up to 65535
	move.l	d4,d5			 ; start of file?
	beq.s	dab_do

	moveq	#0,d5
	subq.w	#1,d5			 ; highest sector number
	moveq	#err.eof,d0
	rts

dab_do
dab.reg reg	d1/d2/a1
	movem.l dab.reg,-(sp)

	move.l	d5,d3			 ; sector number
	divu	ddf_asect(a4),d3	 ; this can deal with group up to 65535
	moveq	#0,d2
	move.w	d3,d2
	assert	d3c_dgroup,d3c_fgroup-4
	movem.l d3c_dgroup(a0),d0/d1	 ; current drive and file group
	cmp.l	d3,d2			 ; first sector of group?
	bne.s	dab_loc 		 ; ... no, locate
	jsr	ddf_salloc(a4)		 ; allocate new group
	bge.s	dab_sgrp
	bra.s	dab_exit
dab_loc
	cmp.l	d1,d2			 ; current group?
	beq.s	dab_new 		 ; ... yes, just get new slave block
	jsr	ddf_slocate(a4) 	 ; locate this group
	blt.s	dab_exit
dab_sgrp
	assert	d3c_dgroup,d3c_fgroup-4
	movem.l d0/d2,d3c_dgroup(a0)	 ; new current drive and file group

dab_new
	move.w	d0,d3			 ; drive sector / group
	lea	d3c_csb(a0),a2
	jsr	dv3_sbnew		 ; find new slave block

	moveq	#0,d3
	move.w	ddf_slen(a4),d3 	 ; bytes left in buffer
	move.l	d5,d3c_fsect(a0)	 ; ***** after this something must be
					 ; put into the sector
	moveq	#0,d0
dab_exit
	movem.l (sp)+,dab.reg
	rts

;+++
; DV3 locate a slave block for read operations.
; If necessary, it reads the appropriate sector from the disk or extends the
; file allocation.
;
;	d3  r	(word) bytes left in buffer
;	d4 c  p file position
;	d5  r	file sector
;	d6 c  p file ID
;	d7 c  p drive number
;	a0 c  p channel block
;	a2  r	pointer to buffer
;	a3 c  p linkage block
;	a4 c  p definition block
;	a5 c  p pointer to map
;
;	status return standard
;---
dv3_lcbf
	move.l	d3c_fsect(a0),d5	 ; file sector
	move.l	ddf_smask(a4),d3	 ; byte within sector
	and.w	d4,d3
	bne.s	dlb_loc1		 ; start of a new sector
	addq.l	#1,d5			 ; next sector !!!!

dlb_loc1
	movem.l dab.reg,-(sp)

	lea	d3c_csb(a0),a2
	jsr	dv3_sbloc		 ; locate slave block
	beq.s	dlb_done
	blt.s	dlb_exit
	tst.b	d3c_nnslv(a0)		 ; create new slave block?
	bne.s	dlb_nc			 ; ... no

	move.l	d5,d3			 ; sector number
	divu	ddf_asect(a4),d3	 ; this can deal with group up to 65535
	moveq	#0,d2
	move.w	d3,d2
	assert	d3c_dgroup,d3c_fgroup-4
	lea	d3c_fgroup(a0),a1
	move.l	(a1),d1
	move.l	-(a1),d0		 ; current drive and file group
	beq.s	dlb_loc2		 ; none yet
	cmp.w	d1,d2			 ; current group?
	beq.s	dlb_new 		 ; ... yes, new slave block required
dlb_loc2
	jsr	ddf_slocate(a4)
	blt.s	dab_exit
	assert	d3c_dgroup,d3c_fgroup-4
	movem.l d0/d2,d3c_dgroup(a0)	; new current drive and file group

dlb_new
	move.w	d0,d3			 ; drive sector /  group
	lea	d3c_csb(a0),a2
	jsr	dv3_sbnew		 ; find new slave block

	move.l	d3c_feof(a0),d0
	sub.l	ddf_fhlen(a4),d0	 ; position at start of new file?
	beq.s	dlb_posn		 ; ... yes

	addq.b	#sbt.read-sbt.true,sbt_stat(a1) ; read requested

	jsr	ddl_slbfill(a3) 	 ; read sector
	bne.s	dlb_exit
dlb_posn
	move.l	ddf_smask(a4),d3	 ; restore byte within sector
	and.w	d4,d3

dlb_done
	move.l	d5,d3c_fsect(a0)	 ; ***** after this something must be
					 ; taken out of or put into the sector:
					 ; beware of iob.test which does not
					 ; move the pointer on

	add.w	d3,a2			 ; pointer position
	neg.w	d3
	add.w	ddf_slen(a4),d3 	 ; bytes left in buffer
	moveq	#0,d0
dlb_exit
	movem.l (sp)+,dab.reg
	rts

dlb_nc
	moveq	#err.nc,d0
	bra.s	dlb_exit

	end
