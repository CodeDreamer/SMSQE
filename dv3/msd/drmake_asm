; DV3 MSDOS Make Directory	       V3.00	       1993 Tony Tebby

	section dv3

	xdef	msd_drmake

	xref	dv3_fbnew
	xref	dv3_fbupd

	include 'dev8_keys_dos'
	include 'dev8_dv3_keys'
	include 'dev8_keys_err'
	include 'dev8_mac_xword'
	include 'dev8_mac_assert'

;+++
; DV3 MSDOS Format Make Directory
;
;	d6 cr	file ID
;	d7 c  p drive ID / number
;	a0 c  p pointer to channel block
;	a3 c  p pointer to linkage
;	a4 c  p pointer to drive definition
;
;	status return standard
;
;---
msd_drmake
mdm.reg reg	d4/d5/a1/a2
	movem.l mdm.reg,-(sp)

	moveq	#dos.subd+dos.vol,d0
	and.b	d3c_dsattr(a0),d0	 ; volume or subdir already?
	bne.s	mdm_iu			 ; ... yes
	tst.w	d6			 ; any sector allocated?
	bne.s	mdm_iu

	moveq	#0,d5			 ; first sector
	jsr	dv3_fbnew
	blt.s	mdm_exit		 ; cannot do

	move.l	#'    ',d0

	move.w	#'. ',d4		 ; file name
	move.w	d6,d5			 ; my id
	bsr.s	mdm_sete

	move.w	#'..',d4		 ; file name
	move.w	d3c_sdid+2(a0),d5
	cmp.w	#ddf.rdid,d5		 ; root?
	bne.s	mdm_seto
	moveq	#0,d5			 ; ... yes, formally zero
mdm_seto
	bsr.s	mdm_sete

	moveq	#0,d5			 ; first sector
	move.w	ddf_asect(a4),d4	 ; number of new sectors
	moveq	#-2*dos.drel,d0
	bra.s	mdm_drclr

mdm_drnew
	addq.l	#1,d5			 ; next sector
	jsr	dv3_fbnew
	blt.s	mdm_exit		 ; cannot do

	moveq	#0,d0
mdm_drclr
	add.w	ddf_slen(a4),d0
	move.l	a2,a1
	moveq	#0,d1
mdm_drz
	move.l	d1,(a1)+		 ; clear sector
	subq.w	#4,d0
	bgt.s	mdm_drz

	jsr	dv3_fbupd		 ; updated

	subq.w	#1,d4			 ; another sector
	bgt.s	mdm_drnew		 ; ... yes

	or.b	#dos.subd,d3c_dsattr(a0) ; set directory
	moveq	#0,d0
mdm_exit
	movem.l (sp)+,mdm.reg
	rts
mdm_iu
	moveq	#err.fdiu,d0
	bra.s	mdm_iu

mdm_sete
	move.w	d4,(a2)+		 ; '.' or '..' file
	move.l	d0,(a2)+
	move.l	d0,(a2)+
	move.b	d0,(a2)+
	move.b	#dos.subd,(a2)+
	clr.l	(a2)+			 : $10
	clr.l	(a2)+			 ; $10
	clr.l	(a2)+			 ; $14
	clr.w	(a2)+			 ; $18
	assert	dos_clus,$1a
	xword	d5
	move.w	d5,(a2)+
	clr.l	(a2)+
	rts

	end
