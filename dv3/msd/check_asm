; DV3 MSDOS Check Format	      V3.01	      1993 Tony Tebby
;
; 2020-03-08  3.01  modified for correct name in ddf_mname (wl)

	section dv3

	xdef	msd_check

	xref	msd_tab4
	xref	msd_mname
	xref	msd_count

	xref	dv3_psector
	xref	dv3_change
	xref	dv3_setfd
	xref	dv3_redef

	include 'dev8_keys_dos'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_msd_keys'
	include 'dev8_mac_xword'
	include 'dev8_mac_assert'
;+++
; DV3 MSDOS Check Format
;
;	d7 c  p drive ID / number
;	a0 c  p pointer to channel block
;	a1 c  p pointer to root sector
;	a2    p
;	a3 c  p pointer to linkage
;	a4 c  p pointer to drive definition
;
;	this is a clean routine
;	status return standard or positive (not recognised)
;
;---
msd_check
mck.regc reg	a2/a5
mck.regx reg	d1-d7/a1
mck.reg reg	d1-d7/a1/a2/a5
stk_d7	equ	6*4

	movem.l mck.regc,-(sp)

	tst.b	ddf_mstat(a4)		 ; valid medium?
	beq.s	mck_changed		 ; ... no

	lea	mdf_map(a4),a5
	move.l	a1,a2
	moveq	#12,d0			 ; check first 52 bytes
mck_loop
	cmpm.l	(a5)+,(a2)+
	dbne	d0,mck_loop
	bne.s	mck_changed

	st	ddf_mstat(a4)
	moveq	#0,d0
mckc_exit
	movem.l (sp)+,mck.regc
	rts

mck_changed
	jsr	dv3_change		 ; change OK
	bne.s	mckc_exit		 ; ... no

	movem.l mck.regx,-(sp)
	tst.b	ddl_buff(a3)		 ; buffered?
	beq.l	mck_bad 		 ; ... no

	assert	ddf.msd3,0
	sf	ddf_stype(a4)		 ; clear subtype (3 nibble map)

	moveq	#%1111,d0		 ; check format type
	or.b	dos_mdid(a1),d0 	 ; %11111111
	addq.b	#1,d0			 ;
	bne.s	mck_badn

; now dig out the medium information, checking the validity
;
;	d2	sectors ber sector
;	d3	sectors per track
;	d4	number of heads
;	d5	number of cylinders
;	d6	sectors / allocation unit
;	d7	total sectors

	lea	dos_sctl(a1),a2 	 ; check starting with sector length
	tst.b	(a2)+			 ; >256
	bne.s	mck_badn		 ; ... no
	moveq	#0,d2
	move.b	(a2)+,d2
	lsr.w	#1,d2			 ; n*512?
	bcs.s	mck_badn
	moveq	#0,d6
	move.b	(a2),d6 		 ; cluster size
	ble.s	mck_badl		 ; ... zero
	cmp.b	#8,d6
	bgt.s	mck_badn		 ; ... too large
	moveq	#$f,d0
	and.b	dos_dire(a1),d0 	 ; directory entries must be x16
	bne.s	mck_badn

	moveq	#0,d7
	move.w	dos_sect+1(a1),d7
	move.b	dos_sect(a1),d7 	 ; total sectors
	move.w	d7,d0			 ; total sectors
mck_badl
	ble.l	mck_bad 		 ; ... none

	moveq	#0,d3			 ; preset no track format
	moveq	#0,d4
	moveq	#0,d5
	tst.b	ddl_cylhds(a3)		 ; cylinder / sector / side?
	beq.s	mck_sts 		 ; ... no

	move.w	dos_strk(a1),d3 	 ; sectors per track
	xword	d3
	blt.s	mck_badl		 ; ... none
	cmp.w	#80,d3			 ; no more than 80
	bgt.s	mck_badn

	move.w	dos_head(a1),d4 	 ; number of heads
	xword	d4
	blt.s	mck_badl		 ; ... none
	cmp.w	#32,d4			 ; no more than 32
	bgt.s	mck_badn


	move.w	d4,d1
	mulu	d3,d1			 ; sectors / cylinder
	divu	d1,d0			 ; cylinders
	swap	d0
	tst.w	d0			 ; there should be no remainder
mck_badn
	bne.l	mck_bad
mck_sts
	mulu	d2,d3			 ; adjust sectors / track
	mulu	d2,d6			 ;	  sectors / alloc
	mulu	d2,d7			 ;	  sectors / drive

	assert	ddf_strk,ddf_sintlv-4,ddf_sskew-6,ddf_heads-8
	lea	ddf_strk(a4),a2
	move.w	d3,(a2)+		 ; sectors per track
	addq.l	#2,a2
	clr.l	(a2)+			 ; interleave and skew
	move.w	d4,(a2)+		 ; number of heads

	assert ddf_heads+2,ddf_scyl
	assert ddf_scyl,ddf_asect-2,ddf_asize-4,ddf_atotal-6,ddf_agood-$a
	mulu	d3,d4
	move.w	d4,(a2)+		 ; sectors per cylinder
	move.w	d6,(a2)+		 ; cluster size (sectors)
	move.w	ddf_slen(a4),d0
	mulu	d6,d0
	move.w	d0,(a2)+		 ; allocation size (bytes)
	move.l	d7,d1			 ; sectors
	divu	d6,d1			 ; total allocation units
	move.w	d1,d0
	move.l	d0,(a2)+		 ; number of allocation units
	move.l	d0,(a2)+		 ; number good ... to be adjusted

; the basic medium information is set up, now for the information about the
; map (root+FAT) and the root directory
;
;	d1	map sectors (1+FAT)
;	d2	map + root directory sectors
;	d3	reserved sectors >=1
;	d4	sectors in each FAT on disk
;	d5	total sectors in FATS
;	d6	offset to first data sector
;	d7	number of entries in FAT

	moveq	#0,d3
	move.w	dos_ress(a1),d3 	 ; reserved sectors
	xword	d3
mck_badl2
	ble.s	mck_badl
	mulu	d2,d3

	move.w	dos_sfat(a1),d4 	 ; FAT sectors
	xword	d4
	ble.s	mck_badl
	mulu	d2,d4			 ; real number of FAT sectors

	moveq	#0,d5
	move.b	dos_fats(a1),d5 	 ; number of FATS
	ble.s	mck_badl2		 ; ... none
	cmp.b	#2,d5
	bgt.s	mck_badn		 ; ... too many

	mulu	d4,d5			 ; total FAT sectors

	moveq	#0,d0
	move.w	dos_dire+1(a1),d0
	move.b	dos_dire(a1),d0 	 ; number of directory entries
	lsl.w	#dos.dres,d0		 ; length of directory
	move.l	d0,d1
	subq.l	#1,d1
	divu	ddf_slen(a4),d1 	 ; round up to (mega) sector
	ext.l	d1
	divu	d2,d1
	addq.w	#1,d1			 ; number of directory (mega) sectors
	mulu	d1,d2			 ; number of dir sectors

	add.l	d0,d0			 ; internal is twice MSDOS
	move.l	d0,ddf_rdlen(a4)	 ; keep it
	move.l	#ddf.rdid,ddf_rdid(a4)	 ; set root directory ID


	move.l	d6,d0			 ; save sectors per alloc
	move.l	d3,d6			 ; reserved sectors
	add.w	d5,d6			 ; + map
	add.w	d2,d6			 ; + root directory
	sub.w	ddf_asect(a4),d6
	sub.w	ddf_asect(a4),d6	 ; DOS groups start at 2!!

	sub.l	d6,d7			 ; the number of sectors alloc by FAT
	divu	d0,d7			 ; ... the number of groups in the FAT

	move.w	d7,d0
	add.w	d7,d0
	add.w	d7,d0
	addq.w	#1,d0
	lsr.w	#1,d0			 ; the number of bytes in a 12 bit FAT

	cmp.w	#$ff3,d7		 ; 12 bit fat (4085 clusters official)
	bgt.s	mck_4nib		 ; ... certainly not
	cmp.b	#ddl.flp,ddl_mtype(a3)	 ; floppy diskette?
	beq.s	mck_redef		 ; ... yes, it is a 12 bit
	cmp.b	#dos.jmp,dos_jmp(a1)	 ; is it true DOS format?
	beq.s	mck_redef		 ; ... yes, it is a 12 bit

mck_4nib
	lea	msd_tab4,a2		 ; reset vector table
	jsr	dv3_setfd

	addq.b	#ddf.msd4,ddf_stype(a4)  ; 16 bit FAT

	move.w	d7,d0
	add.l	d0,d0			 ; size of FAT

mck_redef
	subq.l	#1,d0			 ; round up to a sector length
	divu	ddf_slen(a4),d0
	addq.w	#2,d0			 ; room for complete sectors + root

	move.w	d0,d1
	add.w	d2,d0			 ; + directory
	move.w	d0,d2

	mulu	ddf_slen(a4),d0
	add.l	#mdf_map,d0		 ; size required
	jsr	dv3_redef		 ; re-allocate drive definition
	bne.l	mck_exit		 ; ... oops

	move.w	d1,mdf_msect(a4)	 ; map (root+FAT) sectors
	move.w	d2,mdf_mdsect(a4)	 ; + directory

	assert	mdf_ress,mdf_sfat-2,mdf_sftx-4
	movem.w d3/d4/d5,mdf_ress(a4)	 ; set disk root sector constants
	add.l	ddf_psoff(a4),d6
	move.l	d6,ddf_lsoff(a4)	 ; set logical sector offset

	move.w	d7,mdf_fent(a4) 	 ; number of FAT entries

	move.l	stk_d7(sp),d7		 ; restore drive info

	move.l	a1,a2
	lea	mdf_map(a4),a1
	move.w	ddf_slen(a4),d0
	lsr.w	#2,d0
	subq.w	#1,d0
mck_cloop
	move.l	(a2)+,(a1)+		 ; copy root sector
	dbra	d0,mck_cloop


	move.l	a1,mdf_fat(a4)		 ; pointer to fat

	sub.w	d1,d2			 ; number of directory sectors
	move.w	d2,d6
	subq.w	#1,d1

	moveq	#0,d2
	move.b	ddl_msect(a3),d2	 ; multiple sector reads?
	bne.s	mck_mloop		 ; ... yes
	moveq	#1,d2			 ; read one sector at a time

mck_mloop
	cmp.w	d2,d1			 ; how many sectors to go?
	bhs.s	mck_lsec		 ; enough
	move.w	d1,d2			 ; just this many
mck_lsec
	move.l	d3,d0			 ; next sector
	jsr	dv3_psector
	jsr	ddl_rsect(a3)		 ; read it
	beq.s	mck_mnext
	cmp.w	d4,d5			 ; another FAT?
	beq	mck_exd0		 ; ... no
	move.l	d3,d0
	add.w	d4,d0			 ; ... yes, try it
	jsr	dv3_psector
	jsr	ddl_rsect(a3)		 ; read it
	bne.s	mck_exit

mck_mnext
	add.l	d2,d3			 ; next sector
	move.w	d2,d0
	mulu	ddf_slen(a4),d0
	add.l	d0,a1			 ; ... next address
	sub.w	d2,d1
	bhi.s	mck_mloop

mck_rdir
	move.w	mdf_ress(a4),d3 	 ; reserved sectors
	add.w	d5,d3			 ; first directory sector
	subq.w	#1,d6

	move.l	a1,mdf_rdir(a4)
	moveq	#1,d2			 ; directory one sector at a time
mck_dloop
	move.l	d3,d0			 ; next sector
	jsr	dv3_psector
	jsr	ddl_rsect(a3)		 ; read it
	bne.s	mck_exit
	addq.w	#1,d3
	add.w	ddf_slen(a4),a1
	dbra	d6,mck_dloop

mck_cfree
	st	ddf_mstat(a4)		 ; ... medium OK now

	jsr	msd_mname		 ; ... find medium name
	bne.s	mck_count

	lea	ddf_mname+2(a4),a2	 ; skip len word
	move.l	(a1)+,(a2)+
	move.l	(a1)+,(a2)+		 ; DOS name is 8 long

	moveq	#7,d0			 ; max nbrs of chars -1 (dbf)
	moveq	#' ',d6 		 ; string is space filled
cmp_lp	cmp.b	-(a2),d6
	bne.s	cont1
	dbf	d0,cmp_lp
cont1	addq.w	#1,d0
	move.w	d0,ddf_mname(a4)

mck_count
	jsr	msd_count

	sub.l	d2,ddf_agood(a4)	 ; adjust number good
	move.l	d1,ddf_afree(a4)	 ; set number free

	clr.l	ddf_fhlen(a4)		 ; no inbuilt header

	moveq	#ddf.change,d0
	bra.s	mck_exit

mck_ok
	moveq	#0,d0
mck_exit
	movem.l (sp)+,mck.reg
	rts

mck_bad
	moveq	#ddf.unrec,d0		 ; unrecognised format
mck_exd0
	tst.l	d0
	bra.s	mck_exit

	end
