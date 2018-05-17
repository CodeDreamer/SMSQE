; DV3 MSDOS Format Fetch Directory Entry    V3.00	    1993 Tony Tebby

	section dv3

	xdef	msd_drent
	xdef	msd_drefile
	xdef	msd_dre2

	xref	dv3_drloc
	xref	dv3_fbloc
	xref	msd_drlen

	include 'dev8_keys_dos'
	include 'dev8_keys_err'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_msd_keys'
	include 'dev8_mac_assert'
	include 'dev8_mac_xword'

;+++
; DV3 MSDOS Format Scrumple Directory Entry at A2
;
;	d6 c  p file ID
;	d7 c  p drive ID / number
;	a0 c  p pointer to channel block
;	a1 c  p pointer to buffer
;	a2    p pointer to directory entry
;	a3 c  p pointer to linkage
;	a4 c  p pointer to drive definition
;
;	status return positive
;
;---
msd_dre2
mde.reg reg	d1/d2/d4/d5/a1/a2/a5
	movem.l mde.reg,-(sp)
	moveq	#-1,d4			 ; my own header
	bra.s	mde_scrumple

;+++
; DV3 MSDOS Format Fetch Directory Entry For File
;
;	d0 cr	format type required / status return
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
msd_drefile
	movem.l mde.reg,-(sp)
	move.l	d3c_sdent(a0),d2	 ; our own entry
	moveq	#-1,d4			 ; our own header
	cmp.l	#ddf.rdid,d3c_sdid(a0)	 ; root directory?
	beq.s	mde_root

	addq.l	#2,d2			 ; skip first two entries
	lea	dv3_drloc,a2		 ; look in directory
	bra.s	mde_fent		 ; find entry


;+++
; DV3 MSDOS Format Fetch Directory Entry
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
msd_drent
	movem.l mde.reg,-(sp)
	move.l	d2,d4			 ; this entry

	cmp.l	#ddf.rdid,d3c_flid(a0)	 ; root directory?
	bne.s	mde_gbuf		 ; ... no, get buffer

mde_root
	cmp.b	#ddf.msdos,d0		 ; check for standard or MSDOS
	sne	-(sp)

	lsl.l	#dos.dres,d2		 ; root sector offset
	move.l	mdf_rdir(a4),a2
	add.l	d2,a2			 ; position in root
	bra.s	mde_test

mde_gbuf
	lea	dv3_fbloc,a2		 ; in file

mde_fent
	cmp.b	#ddf.msdos,d0		 ; check for standard or MSDOS
	sne	-(sp)

	addq.l	#2,d2			 ; sub-directory entries start at two
	move.l	d2,d5
	moveq	#7-dos.dres,d0		 ; header to sector
	add.b	ddf_slflag(a4),d0	 ; + sector length flag
	lsr.l	d0,d5			 ; header sector

	jsr	(a2)			 ; locate sector
	bne.l	mde_bad

	lsl.w	#dos.dres,d2
	and.w	ddf_smask+2(a4),d2	 ; byte within sector
	add.w	d2,a2			 ; start of data

mde_test
	tst.b	(sp)+			 ; scrumple or fetch?
	beq.l	mde_fetch

	move.b	(a2),d0 		 ; end of file?
	beq.l	mde_empty		 ; ... yes

	cmp.b	#dos.dele,d0		 ; deleted?
	beq.l	mde_empty

	btst	#dos..vol,dos_attr(a2)	 ; volume?
	bne.l	mde_empty		 ; ... yes

mde_scrumple
	clr.b	d3c_type(a0)		 ; normal file type

	move.l	a2,a5
	lea	d3c_fnam+2(a0),a1
	move.l	a1,d1
	move.l	(a5)+,(a1)+		 ; copy filename
	move.l	(a5)+,(a1)+

	moveq	#' ',d2
mde_lns
	cmp.b	-(a1),d2		 ; look for last non space char
	beq.s	mde_lns
	addq.l	#1,a1			 ; end of name

	cmp.w	#'EX',(a5)		 ; is it EX?
	bne.s	mde_sext
	moveq	#-'9',d0
	add.b	2(a5),d0		 ; followed by 0-9?
	bgt.s	mde_sext		 ; ... no
	add.b	#9,d0
	blt.s	mde_sext		 ; ... no

	move.l	#$200,d2		 ; EX0 is 512 bytes
	lsl.l	d0,d2
	move.l	d2,d3c_data(a0) 	 ; set data area
	move.b	#1,d3c_type(a0) 	 ; and executable
	bra.s	mde_es			 ; set name length

mde_sext
	move.b	(a5)+,d0
	cmp.b	d2,d0
	beq.s	mde_es			 ; no extension
	move.b	#d3c.pext,(a1)+ 	 ; extension
	move.b	d0,(a1)+

	move.b	(a5)+,d0
	cmp.b	d2,d0
	beq.s	mde_es			 ; no extension
	move.b	d0,(a1)+

	move.b	(a5)+,d0
	cmp.b	d2,d0
	beq.s	mde_es			 ; no extension
	move.b	d0,(a1)+

mde_es
	exg	a1,d1
	sub.w	a1,d1
	move.w	d1,-(a1)		 ; set name length

	move.b	dos_attr(a2),d0
	move.b	d0,d3c_dsattr(a0)	 ; set attributes
	btst	#dos..sub,d0
	beq.s	mde_updt
	st	d3c_type(a0)		 ; directory
mde_updt
	move.l	dos_updt(a2),d0
	xlong	d0
	move.l	d0,d3c_updd(a0) 	 ; copy date
	move.l	dos_flen(a2),d0
	xlong	d0
	bne.s	mde_flen
	tst.b	d3c_type(a0)		 ; no length, is it a sub-directory?
	bge.s	mde_flen		 ; ... no
	move.w	dos_clus(a2),d0
	xword	d0
	jsr	msd_drlen		 ; set max directory length
	assert	dos.drel,$20
	add.l	d0,d0			 ; in entries of $40
mde_flen
	move.l	d0,d3c_flen(a0) 	 ; file length

mde_sdone
	move.l	d4,d3c_denum(a0)	 ; new entry set
	moveq	#1,d0			 ; return scrumpled

mde_exit
	movem.l (sp)+,mde.reg
	rts

mde_empty
	clr.l	d3c_flen(a0)
	clr.l	d3c_arcd(a0)

	assert	d3c_dsattr,d3c_type-1
	clr.w	d3c_dsattr(a0)
	clr.w	d3c_fnam(a0)
	bra.s	mde_sdone

mde_fetch
	moveq	#dos.drel/4-1,d0
mde_cpl
	move.l	(a2)+,(a1)+		 ; copy entry
	dbra	d0,mde_cpl

	moveq	#0,d0			 ; return copied
	bra.s	mde_exit

mde_eof
	moveq	#err.eof,d0		 ; end of file
mde_bad
	addq.l	#2,sp			 ; skip return
	bra.s	mde_exit
	end
