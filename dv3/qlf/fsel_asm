; DV3 QL Format Select		  V3.00 	  1992 Tony Tebby

	section dv3

	xdef	qlf_fsel

	xref	dv3_slen
	xref	dv3_setfd
	xref	dv3_redef

	xref	qlf_tbwa

	include 'dev8_dv3_keys'
	include 'dev8_dv3_qlf_keys'
	include 'dev8_keys_ql5b'
	include 'dev8_keys_qlwa'
	include 'dev8_keys_err'
	include 'dev8_mac_assert'

;+++
; DV3 QL Format Select
;
;	d0 cr	format type / error code
;	d1 c  p format dependent flag
;	d7 c  p drive ID / number
;	a0 c  p pointer to physical format table
;	a3 c  p pointer to linkage
;	a4 c  p pointer to drive definition
;
;	status return standard
;
;---
qlf_fsel
qfs.reg reg	d1/d2/d3/d4/d5/d6/a1/a2
	movem.l qfs.reg,-(sp)
	move.b	ddl_mtype(a3),d2
	subq.b	#ddl.flp,d2		 ; floppy disk?
	bne.l	qfs_hdisk		 ; ... no

	tst.b	ddl_cylhds(a3)		 ; accessed by tracks?
	beq.l	qfs_nimp		 ; ... no

	move.w	dff_cyls(a0),d4 	 ; number of cylinders
	move.w	dff_heads(a0),d3	 ; number of heads

	cmp.l	#10,dff_size+dff.size*ddf.2048(a0) ; at least 10 2k byte sectors?
	bge.s	qfs_fed 		 ; ... yes, QL ED format

	moveq	#3,d5			 ; allocate by 3s
	moveq	#ddf.512,d1		 ; 512 byte sectors
	move.l	dff_size+dff.size*ddf.512(a0),d2 ; how many 512 sectors?
	cmp.b	#18,d2			 ; 18?
	bge.s	qfs_fhd
	cmp.b	#9,d2			 ; as many as 9?
	blt.l	qfs_nimp

	moveq	#9,d2			 ; nine sectors per track
	assert	ddf.ql5a,0
	clr.b	ddf_stype(a4)		 ; old format
	bra.s	qfs_fset

qfs_fhd
	moveq	#18,d2			 ; 18 sectors per track
	bra.s	qfs_f5b

qfs_fed
	moveq	#1,d5			 ; allocate by ones
	moveq	#ddf.2048,d1		 ; 2048 byte sectors
	moveq	#10,d2
qfs_f5b
	move.b	#ddf.ql5b,ddf_stype(a4)

qfs_fset
	move.w	#ddf.rdid,ddf_rdid+2(a4) ; set root director ID
	move.b	d1,ddf_slflag(a4)
	jsr	dv3_slen		 ; set lengths

	move.w	ddf_slen(a4),d1 	 ; sector length

	move.w	d4,d6			 ; tracks
	mulu	d2,d6
	mulu	d3,d6			 ; total sectors
	divu	d5,d6			 ; total groups (no remainder)

	moveq	#q5a_gmap-1,d0
	add.w	d6,d0
	add.w	d6,d0			 ; map space required
	add.w	d6,d0			 ; 3 bytes / group  (+header-1)
	move.w	#qdf_map+1,a1
	add.l	d0,a1			 ; top of map (rel a4)
	divu	d1,d0
	addq.w	#1,d0			 ; room for complete sectors
	exg	d0,d1			 ; keep sectors required
	mulu	d1,d0
	add.l	#qdf_map,d0
	move.l	d0,a2
	jsr	dv3_redef		 ; re-allocate drive definition
	bne.l	qfs_exit		 ; ... oops

	add.l	a4,a1
	move.l	a1,qdf_mtop(a4) 	 ; set top of map
	move.w	d1,qdf_msect(a4)	 ; and number of sectors

	add.l	a4,a2
	lea	qdf_map(a4),a1
	moveq	#-1,d0
qsf_fclr
	move.l	d0,-(a2)		 ; preset to all $FF
	cmp.l	a1,a2
	bgt.s	qsf_fclr

	assert	ddf_slen,ddf_strk-2,ddf_sintlv-6,ddf_sskew-8
	lea	ddf_slen(a4),a2
	move.w	(a2)+,d1		 ; sector length
	move.w	d2,(a2)+		 ; sectors per track
	addq.l	#2,a2
	clr.l	(a2)+			 ; interleave and skew

	assert	ddf_sskew,ddf_heads-2,ddf_scyl-4
	move.w	d3,(a2)+		 ; heads
	mulu	d3,d2
	move.w	d2,(a2)+		 ; sectors per cylinder

	assert	ddf_scyl,ddf_asect-2,ddf_asize-4,ddf_atotal-6
	move.w	d5,(a2)+		 ; allocation sectors
	mulu	d5,d1
	move.w	d1,(a2)+		 ; allocation bytes
	move.l	d6,(a2)+		 ; total allocation
	bra.l	qfs_ok


qfs_hdisk
	subq.b	#ddl.hd-ddl.flp,d2	 ; hard disk?
	bne.l	qfs_nimp		 ; ... no

	moveq	#0,d2			 ; sectors per track
	moveq	#0,d3			 ; number of tracks
	moveq	#0,d4			 ; number of cylinders
	move.l	dff_size+dff.size*ddf.512(a0),d6 ; 512 byte sectors

	tst.b	ddl_cylhds(a3)		 ; accessed by tracks?
	beq.s	qfs_hdlog		 ; ... no

	move.w	d6,d2			 ; sectors per track
	move.w	dff_heads(a0),d3	 ; number of heads
	move.w	dff_cyls(a0),d4 	 ; number of cylinders
	mulu	d3,d6
	mulu	d4,d6			 ; total sectors

qfs_hdlog
	lea	qlf_tbwa,a2		 ; set QLWA
	jsr	dv3_setfd

	move.l	d6,d5
	clr.w	d5
	swap	d5			 ; sectors /65536
	addq.w	#1,d5			 ; rounded up +, gives allocation size
	cmp.w	#$80,d5 		 ; $80 * $200 = $10000 is too large
	bls.s	qfs_small		 ; ... ok
	move.w	#$7f,d5 		 ; allocs cannot be 65536 bytes
	move.l	#$7f*$ffff,d6
qfs_small
	cmp.w	#4,d5			 ; at least 4?
	bge.s	qfs_hset		 ; ... yes
	moveq	#4,d5
qfs_hset
	move.b	#ddf.qlwa,ddf_stype(a4)

	move.b	#ddf.512,ddf_slflag(a4)
	jsr	dv3_slen		 ; set lengths
	move.w	ddf_slen(a4),d1 	 ; sector length

	move.l	d6,d0
	divu	d5,d0			 ; total allocation units
	moveq	#0,d6
	move.w	d0,d6

	moveq	#qwa_gmap-1,d0
	add.l	d6,d0			 ; map space required
	add.l	d6,d0			 ; 2 bytes / group  (+header-1)
	divu	d1,d0
	addq.w	#1,d0			 ; room for complete sectors
	swap	d3
	move.w	d0,d3			 ; number of swap sectors
	mulu	d1,d0
	add.l	#qdf_map,d0
	move.l	d0,a1

	jsr	dv3_redef		 ; re-allocate drive definition
	bne.s	qfs_exit		 ; ... oops

	add.l	a4,a1			 ; top of map
	move.l	a1,qdf_mtop(a4) 	 ; set top of map
	move.w	d3,qdf_msect(a4)	 ; map sectors
	swap	d3

	assert	ddf_strk,ddf_sintlv-4,ddf_sskew-6
	lea	ddf_strk(a4),a2
	move.w	d2,(a2)+		 ; sectors per track
	addq.l	#2,a2
	clr.l	(a2)+			 ; interleave and skew

	assert	ddf_sskew,ddf_heads-2,ddf_scyl-4
	move.w	d3,(a2)+		 ; heads and sectors per cylinder
	move.w	d2,d0
	mulu	d3,d0
	move.w	d0,(a2)+

	assert	ddf_scyl,ddf_asect-2,ddf_asize-4,ddf_atotal-6
	move.w	d5,(a2)+		 ; allocation sectors
	mulu	d5,d1
	move.w	d1,(a2)+		 ; allocation bytes
	move.l	d6,(a2)+		 ; total allocation

qfs_ok
	move.l	ddf_psoff(a4),ddf_lsoff(a4) ; set sector offset
	moveq	#0,d0
qfs_exit
	movem.l (sp)+,qfs.reg
	rts

qfs_nimp
	moveq	#err.nimp,d0
	bra.s	qfs_exit

	end
