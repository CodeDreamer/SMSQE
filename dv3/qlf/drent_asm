; DV3 QL Format Fetch Directory Entry	 V3.00		 1992 Tony Tebby

	section dv3

	xdef	qlf_drent
	xdef	qlf_drefile
	xdef	qlf_dre2

	xref	dv3_drloc
	xref	dv3_fbloc

	xref	dv3_iqdt
	xref	dv3_qdit

	include 'dev8_keys_hdr'
	include 'dev8_keys_dos'
	include 'dev8_dv3_keys'

;+++
; DV3 QL Format Scrumple Directory Entry at A2
;
;	d7 c  p drive ID / number
;	a0 c  p pointer to channel block
;	a2    p pointer to directory entry
;	a3 c  p pointer to linkage
;	a4 c  p pointer to drive definition
;
;	status return positive
;
;---
qlf_dre2
qde.reg reg	d1/d2/d4/d5/a1/a2/a5
	movem.l qde.reg,-(sp)
	moveq	#-1,d4			 ; my own header
	bra.s	qde_scrumple

;+++
; DV3 QL Format Fetch Directory Entry For File
;
;	d0 cr	format type required / status return
;	d7 c  p drive ID / number
;	a0 c  p pointer to channel block
;	a1 c  p pointer to buffer
;	a2    p
;	a3 c  p pointer to linkage
;	a4 c  p pointer to drive definition
;
;	status return standard or positive (not recognised)
;
;---
qlf_drefile
	movem.l qde.reg,-(sp)
	moveq	#-1,d4			 ; my own header
	move.l	d3c_sdent(a0),d2	 ; our own entry
	lea	dv3_drloc,a2		 ; look in directory
	bra.s	qde_fent

;+++
; DV3 QL Format Fetch Directory Entry
;
;	d0 cr	format type required / status return
;	d2 c  p directory entry required
;	d6 c  p file ID
;	d7 c  p drive ID / number
;	a0 c  p pointer to channel block
;	a1 c  p pointer to buffer
;	a2    p
;	a3 c  p pointer to linkage
;	a4 c  p pointer to drive definition
;
;	status return standard or positive (not recognised)
;
;---
qlf_drent
	movem.l qde.reg,-(sp)
	move.l	d2,d4			 ; this entry
	lea	dv3_fbloc,a2		 ; ... in file

qde_fent
	cmp.b	#ddf.qdos,d0		 ; check for standard or QDOS
	sne	-(sp)
	moveq	#1,d5			 ; entry 0 is at position 1
	add.l	d2,d5
	moveq	#7-hdr.sft,d0		 ; header to sector
	add.b	ddf_slflag(a4),d0	 ; + sector length flag
	lsr.l	d0,d5			 ; header sector

	jsr	(a2)			 ; locate sector
	bne.l	qde_bad

	addq.l	#1,d2
	lsl.w	#hdr.sft,d2
	and.w	ddf_smask+2(a4),d2	 ; byte within sector
	add.w	d2,a2			 ; start of data

	tst.b	(sp)+			 ; scrumple or copy?
	beq.s	qde_copy

qde_scrumple
	move.l	(a2)+,d3c_flen(a0)	 ; length
	move.w	(a2)+,d0
	cmp.b	#hdrt.dir,d0		 ; directory?
	bne.s	qde_sattr
	or.w	#dos.subd<<8,d0 	 ; set dos sub-directory attribute
qde_sattr
	move.w	d0,d3c_dsattr(a0)	 ; attributes
	move.l	(a2)+,d3c_data(a0)	 ; data
	move.l	(a2)+,d3c_xtra(a0)	 ; extra

	lea	d3c_fnam(a0),a1
	move.w	(a2)+,d0		 ; length on name in directory
	beq.s	qde_none

	moveq	#hdr.nmln,d2
	sub.w	d0,d2			 ; spare at end
	move.w	d3c_name(a0),d1
	add.w	d1,a2			 ; and where to start copying from

	lea	dv3_qdit,a5		 ; translate to internal
	sub.w	d1,d0			 ; filename length
	move.w	d0,(a1)+
	bge.s	qde_scne		 ; OK
	add.w	d0,a2			 ; -ve, this must be directory itself!
	bra.s	qde_scename
qde_scnl
	move.b	(a2)+,d1
	move.b	(a5,d1.w),(a1)+ 	 ; filename characters
qde_scne
	dbra	d0,qde_scnl

qde_scename
	add.w	d2,a2			 ; skip bit at end of name

	move.l	(a2)+,d3c_updd(a0)	 ; copy dates and version numbers
	move.w	(a2)+,d3c_vers(a0)
	move.l	2(a2),d3c_arcd(a0)
qde_sdone
	move.l	d4,d3c_denum(a0)	 ; new entry number
	moveq	#1,d0			 ; return scrumpled
	bra.s	qde_exit

qde_none
	clr.w	(a1)			 ; no name
	bra.s	qde_sdone


qde_copy
	move.l	(a2)+,(a1)+		 ; length
	move.l	(a2)+,(a1)+		 ; attributes, data xtras and name len
	move.l	(a2)+,(a1)+
	move.l	(a2)+,(a1)+

	moveq	#hdr.nmln,d0		 ; length of name to copy
	lea	d3c_name(a0),a5
	move.w	(a5)+,d4		 ; length of directory / file name
	beq.s	qde_cpne		 ; ... none
	sub.w	d4,d0
	add.w	d4,a2
	move.l	a2,d2
	lea	dv3_iqdt,a2		 ; translate to internal
	moveq	#0,d1
	bra.s	qde_cpfe
qde_cpfl
	move.b	(a5)+,d1
	move.b	(a2,d1.w),(a1)+ 	 ; directory / filename characters
qde_cpfe
	dbra	d4,qde_cpfl

	move.l	d2,a2

	bra.s	qde_cpne
qde_cpnl
	move.b	(a2)+,(a1)+		 ; the rest of the characters
qde_cpne
	dbra	d0,qde_cpnl

	move.l	(a2)+,(a1)+		 ; copy dates and version numbers
	move.l	(a2)+,(a1)+
	move.l	(a2)+,(a1)+

	moveq	#0,d0			 ; return copied

qde_exit
	movem.l (sp)+,qde.reg
	rts

qde_bad
	addq.l	#2,sp			 ; skip return
	bra	qde_exit
	end
