; DV3 QLWA Sector Allocate, Locate and Truncate  V3.00		 1992 Tony Tebby

	section dv3

	xdef	qlf_sawa
	xdef	qlf_slwa
	xdef	qlf_stwa

	include 'dev8_keys_qlwa'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_qlf_keys'
	include 'dev8_keys_err'

;+++
; DV3 QLWA allocate new group of sectors
;
;	d0 cr	known logical drive group / logical drive group
;	d1 c  p file group of known drive group
;	d2 c  p next file group
;	d6 c  p file ID (updated if first)
;	d7 c  p drive ID / number
;	a0 c  p pointer to channel block
;	a3 c  p pointer to linkage
;	a4 c  p pointer to drive definition
;
;	status return positive (OK) or standard error
;
;---
qlf_sawa
qwsa.reg reg	d2/d3/d4/a2
	movem.l qwsa.reg,-(sp)
	lea	qdf_map(a4),a2		 ; map
	moveq	#0,d3			 ; known group
	move.w	qwa_free(a2),d3 	 ; next free group
	beq.s	qwsa_drfl
	move.l	d3,d4
	add.l	d4,d4
	move.w	qwa_gmap(a2,d4.l),qwa_free(a2) ; next free
	clr.w	qwa_gmap(a2,d4.l)	 ; new end of file
	subq.w	#1,qwa_fgrp(a2)
	subq.w	#1,ddf_afree+2(a4)	 ; fewer sectors now

	bsr.s	qws_smd4		 ; (d4) updated

	subq.w	#1,d2
	bcs.s	qwsa_new		 ; new file

	bsr.s	qwsl_find
	bmi.s	qwsa_exit

	move.l	d0,d4
	add.l	d4,d4
	move.w	d3,qwa_gmap(a2,d4.l)	 ; link in new group
	bsr.s	qws_smd4		 ; map updated

	move.l	d3,d0

qwsa_exit
	movem.l (sp)+,qwsa.reg
	rts

qwsa_new
	move.l	d3,d0			 ; next free sector
	move.l	d0,d6			 ; file ID
	bra.s	qwsa_exit

qwsa_drfl
	moveq	#err.drfl,d0
	bra.s	qwsa_exit		 ; drive full


;+++
; DV3 QLWA locate group of sectors
;
;	d0 cr	known logical drive group / logical drive group
;	d1 c  p file group of known drive group
;	d2 c  p file group required
;	d6 c  p file ID
;	d7 c  p drive ID / number
;	a0 c  p pointer to channel block
;	a3 c  p pointer to linkage
;	a4 c  p pointer to drive definition
;
;	status return positive (OK) or standard error
;---
qlf_slwa
qwsl.reg reg	d2/d4/a2
	movem.l qwsl.reg,-(sp)
	lea	qdf_map(a4),a2		 ; map
	bsr.s	qwsl_find		 ; find sector
	movem.l (sp)+,qwsl.reg
	rts


qwsl_find
	tst.w	d0			 ; any start?
	beq.s	qwsl_start		 ; ... no
	sub.w	d1,d2			 ; number of groups to skip
	beq.s	qwsl_done
	bhi.s	qwsl_m1 		 ; count up
	add.w	d1,d2

qwsl_start
	move.l	d6,d0
qwsl_m1
	subq.w	#1,d2			 ; one pass if d2=1
	blt.s	qwsl_done		 ; first sector!!

qwsl_loop
	move.l	d0,d4
	add.l	d4,d4
	move.w	qwa_gmap(a2,d4.l),d0	 ; next group
	dbeq	d2,qwsl_loop
	beq.s	qwsl_mchk
qwsl_done
	tst.l	d0
	rts

qwsl_mchk
	moveq	#err.mchk,d0		 ; oops, not found
	rts

qws_smd4
	add.l	#qwa_gmap,d4		 ; index map directly
	divu	ddf_slen(a4),d4
	add.w	#qdf_mupd,d4
	st	(a4,d4.w)		 ; and this one
	st	qdf_mupd(a4)		 ; sector zero updated
	st	ddf_mupd(a4)		 ; map updated
	rts


;+++
; DV3 QLWA truncate sector allocation
;
;	d0 cr	known logical drive group / error status
;	d1 c  p file group of known drive group
;	d2 c  p first file group to free
;	d6 c  p file ID
;	d7 c  p drive ID / number
;	a0 c  p pointer to channel block
;	a3 c  p pointer to linkage
;	a4 c  p pointer to drive definition
;
;	status return standard
;---
qlf_stwa
qwst.reg reg	d2/d3/d4/d5/d6/d7/a1/a2
	movem.l qwst.reg,-(sp)
	lea	qdf_map(a4),a2		 ; map
	moveq	#7,d3
	add.b	ddf_slflag(a4),d3	 ; shift for blatting map
	lea	qdf_mupd(a4),a1 	 ; map to blat
	moveq	#qwa_gmap,d7		 ; offset in map

	subq.w	#1,d2
	bcs.s	qwst_start
	bsr.s	qwsl_find
	blt.l	qwst_exit
	move.l	d0,d4
	add.l	d4,d4

	move.w	qwa_gmap(a2,d4.l),d6	 ; first group to blat out
	beq.s	qwst_exok		 ; no trunc - could be done earlier
	clr.w	qwa_gmap(a2,d4.l)	 ; new end of file
	add.l	d7,d4
	lsr.l	d3,d4
	st	(a1,d4.w)		 ; part of map updated
qwst_start
	move.w	qwa_free(a2),d5 	 ; start linking in here
qwst_loop
	moveq	#0,d0
	move.w	d6,d0			 ; next group
	add.l	d0,d0			 ; address
	beq.s	qwst_exit

	addq.w	#1,qwa_fgrp(a2) 	 ; one more free
	addq.w	#1,ddf_afree+2(a4)

	move.w	qwa_free(a2),d4 	 ; first free group
	beq.s	qwst_newfr		 ; ... new, new free
	cmp.w	d4,d6			 ; new group before first free?
	bhi.s	qwst_lfr		 ; ... no, link into free
qwst_newfr
	move.w	d6,qwa_free(a2) 	 ; ... yes, new first free
	move.w	qwa_gmap(a2,d0.l),d6	 ; next from file
	move.w	d4,qwa_gmap(a2,d0.l)	 ; next in free list
	add.l	d7,d0
	lsr.l	d3,d0
	st	(a1,d0.w)		 ; update map
	bra.s	qwst_start		 ; and start again to get d5 set

qwst_lfr
	cmp.w	d5,d6			 ; before last linked?
	bhi.s	qwst_lfl		 ; ... no
	move.w	d4,d5			 ; start at first free
qwst_lfl
	moveq	#0,d2
	move.w	d5,d2
	add.l	d2,d2
	move.w	qwa_gmap(a2,d2.l),d5	 ; next free
	beq.s	qwst_lkin		 ; ... none
	cmp.w	d5,d6			 ; new group before next
	bhi.s	qwst_lfl		 ; ... no, next
qwst_lkin
	move.w	d6,qwa_gmap(a2,d2.l)	 ; point to this one
	add.l	d7,d2
	lsr.l	d3,d2
	st	(a1,d2.w)		 ; update map
	move.w	qwa_gmap(a2,d0.l),d6	 ; next in file list
	move.w	d5,qwa_gmap(a2,d0.l)	 ; new next in free list
	move.l	d0,d5
	lsr.l	#1,d5			 ; previous link
	add.l	d7,d0
	lsr.l	d3,d0
	st	(a1,d0.w)		 ; update map
	bra.s	qwst_loop

qwst_exok
	moveq	#0,d0
qwst_exit
	st	qdf_mupd(a4)		 ; sector zero of map updated
	st	ddf_mupd(a4)
	movem.l (sp)+,qwst.reg
	rts

	end
