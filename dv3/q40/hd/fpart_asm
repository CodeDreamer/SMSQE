; DV3 Q40 Find Partition (Atari root)	     1999  Tony Tebby

; 2020-04-29  V3.05 fix for atari partition check (wl)
; 2020-03-24  V3.04 fix for direct QLWA formatted CF cards, can use other partitions on FAT32 cards (wl)
; 2018-02-06  V3.03 fix for *d2d access to fat16 CF cards (wl)
; 2017-11-21  V3.02 LBA access to QXL.WIN files on FAT32 formatted media (wl)
; 2003-01-17  V3.01 Fixed the ICD extension (8 additional partitions in table)
; Note : when using FAT32 formateted sdhc cards, this always sets ddf_psoff to 0
; and stores the "real" partition offset at ddf_dtop-4(a4)1

	section dv3

	xdef	hd_fpart

	xref	id_diradd

	include 'dev8_keys_err'
	include 'dev8_keys_qlwa'
	include 'dev8_keys_atari'
	include 'dev8_dv3_keys'
	include 'dev8_keys_dos'
	include 'dev8_mac_xword'
	include 'dev8_dv3_hd_keys'
;+++
; DV3 Atari find partition.
; It starts by loading and examining the root sector.
;
;	d1  r	as d3 but in ths format if required
;	d2  r	size of partition, -1 no partition table
;	d3  r	sector number with partition root
;	d4  r	partition ID
;	d7 c  p drive ID / number
;	a3 c  p pointer to linkage block
;	a4 c  p pointer to physical definition
;
;	error return 0 or error
;---
hd_fpart
hfp.reg reg	d5-d7/a0-a3/a5
	movem.l hfp.reg,-(sp)
	clr.b	ddf_lba(a4)		; preset LBA addressing
	moveq	#0,d4
	lea	hdl_part-1(a3),a1
	move.b	(a1,d7.w),d4		; partition required
	bmi.l	hfp_mchk
	moveq	#0,d3			; start at real root
	moveq	#0,d6			; offset starts at 0
	clr.l	ddf_dtop-4(a4)		;!!! no partition offset !!!

hfp_rootloop
	lea	hdl_buff(a3),a1 	; root sector buffer
	move.l	d3,d0			; physical root sector - MBR
	moveq	#1,d2

	jsr	id_diradd		; check
	bne	hfp_mchk
	jsr	hdl_rsint(a3)		; read sector
	bne	hfp_npart		; no partition
	move.l	d4,d0
	lsl.w	#4,d0			; 3.36 : don't change d4
	add.w	#dos_prt1,d0
	move.l	(a1,d0.w),d0		; partition start
	cmp.w	#dos.ftcs,dos_ftcs(a1)	; (unswapped) FAT32 marker present?
	beq	chk_fat32_normal	; ... yes
	cmp.w	#dos.ftsw,dos_ftcs(a1)	; (swapped) FAT32 marker present?
	beq	chk_fat32_switched	; ... yes
hfp_atri
	cmp.w	#$0260,(a1)		; swapped atari type root sector marker ?
	beq.s	hfp_lpart		; ... yes
	cmp.w	#art.id,(a1)		; unswapped  atari type root sector ?
	beq.s	hfp_lpart2		; ... yes
	tst.b	d4			; partition 0?
	beq	hfp_root		; ... yes, presume it's SMSQE QLWA
	bra	hfp_mchk		; ... no, then we have a problem

; here we hve a byte-swapped Atari type root partition
; swap the bytes back
swp_sct
	move.l	a1,a2
	moveq	#127,d2 		; 512 bytes in long words - DBF
swp_lp	move.l	(a2),d0
	rol.w	#8,d0
	swap	d0
	rol.w	#8,d0
	swap	d0
	move.l	d0,(a2)+
	dbf	d2,swp_lp
	rts

hfp_lpart
	bsr.s	swp_sct 		; swap bytes
hfp_lpart2
	st	ddf_lba(a4)		; this is NOT an LBA drive, but CHS
	lea	art_p0(a1),a1		; look from partition 0
	move.l	#art.pchk,d1
	move.l	#art.xflg,d5
	moveq	#4,d2			; check four in first section

hfp_partloop
	move.l	d1,d0
	and.l	art_pflg(a1),d0 	; partition flag
	cmp.l	d5,d0			; extended?
	beq.s	hfp_xlink
	subq.b	#1,d4			; partition found
	blt.s	hfp_found
	add.w	#art.plen,a1
	subq.w	#1,d2			; extended group yet?
	bne.s	hfp_partloop
	tst.l	d2			; all tried?
	bne.s	hfp_root		 ; was: beq.s	hfp_root
	moveq	#-8,d2
	neg.w	d2			; try eight more
	lea	art_pext-art_p0-4*art.plen(a1),a1 ; (JG: this should work now!)
	bra.s	hfp_partloop

hfp_xlink
	move.l	d6,d3
	add.l	art_pstt(a1),d3 	; new root
	tst.l	d6			; first partition yet?
	bne	hfp_rootloop
	move.l	d3,d6			; ... yes, save base of first
	bra	hfp_rootloop

hfp_found
	btst	#art.pval+24,d0 	; partition valid?
	beq.s	hfp_mchk

	move.l	art_psiz(a1),d2 	; size of partition
	add.l	art_pstt(a1),d3 	; root sector for partition
	move.l	d0,d4			; partition ID
hfp_ok
	move.l	d3,d0
	jsr	id_diradd
	move.l	d0,d1			; physical root sector
	moveq	#0,d0
	move.b	d0,hdl_npart(a3)	; partition found

hfp_exit
	movem.l (sp)+,hfp.reg
	rts
hfp_fnf
hfp_mchk
	moveq	#err.mchk,d0
hfp_npart
	st	hdl_npart(a3)		; set no partition found
	bra.s	hfp_exit

hfp_root
	st	ddf_lba(a4)		; this is NOT an LBA drive, but CHS
	cmp.l	#'LQAW',(a1)		; swapped?
	bne.s	noswp
	bsr	swp_sct 		; re-swap
noswp	moveq	#-1,d2			; size unknown
	move.l	#art.qflg,d4		; for QDOS disk
	bra.s	hfp_ok


; if we get here, we have a fat32 drive (possibly)
; the bytes in each word are (normally) still the wrong way round

chk_fat32_switched			; bytes were switched, swap only words
	swap	d0
	bra.s	common

chk_fat32_normal			; bytes not switched, swap bytes & words
	xlong	d0
common
	sf	ddf_lba(a4)		; use LBA & switch bytes
	move.l	d0,d4			; keep for later, start of partition
	jsr	hdl_rsint(a3)		; read partition root sector into (a1)
	bne	nofat32 		; ooops

; I  should now have the partition root sector at (a1)
	cmp.l	#'FAT3',dos_f32s(a1)	; check for FAT32 signature
	bne	nofat32 		; ... nope
	cmp.b	#'2',dos_f32s+4(a1)	; continue check for FAT32 signature
	bne	nofat32
	cmp.b	#2,dos_sctl+1(a1)	; sector size =512 bytes?
	bne.s	hfp_fnf 		; I can't handle anything else
	tst.b	dos_sctl(a1)		; still 512 bytes?
	bne.s	hfp_fnf 		; ... no

; Here I should have what I can consider a valid fat32 partition.
; Now find the root dir.
	clr.l	d3
	move.b	dos_clst(a1),d3 	; cluster size (sectors per cluster)
	beq	hfp_fnf 		; what??????
	moveq	#0,d5
	move.b	dos_fats(a1),d5 	; nbr of fats
	beq	hfp_fnf 		; what ????
	move.l	dos_bspf(a1),d0 	; "big" sectors per fat
	xlong	d0			; in big endian format
	move.l	d0,d6			; d6 = also sectors per fat
					; now use MK's multiply routine
	mulu	d5,d6			; lower 16 bit * number of fats
	swap	d0
	mulu	d5,d0			; upper 16 bit * number of fats
	swap	d0
	add.l	d0,d6			; combine
	add.l	d4,d6			; add start of partition

	clr.l	d0
	move.w	dos_ress(a1),d0
	rol.w	#8,d0			; res sector count
	add.l	d0,d6			; d6 + res sect count = data start
	add.l	d0,d4			; D4 = start of fat
	move.l	dos_rdir(a1),d1 	; root dir cluster
	xlong	d1
	subq.l	#2,d1			; always starts at 2 less!

; root dir cluster * sectors per cluster (MK)
	move.l	d1,d0			; keep root dir cluster
	mulu	d3,d1			; lower 16 bit * sectors per cluster
	swap	d0
	mulu	d3,d0			; upper 16 bit * sectors per cluster
	swap	d0
	add.l	d1,d0			; combine

	add.l	d6,d0			; root dir start = data start + root dir sect
	jsr	hdl_rsint(a3)		; read root dir 1st sector
	bne	hfp_fnf

; check the first 16 entries in the root dir for my file
	moveq	#15,d1			; check first 16 entries in root dir
	lea	hdl_end-12(a3),a2
	moveq	#0,d0
	move.w	d7,d0
	mulu	#12,d0
	add.l	a2,d0			; normalized name we're looking for
	move.l	a3,d5			; keep linkage block
	move.l	a1,a0			; also point to start of dir
srchlp1 move.l	d0,a2			; point to my name (again)
	move.l	a0,a3
	cmp.l	(a2)+,(a3)+		; name matches?
	bne.s	nxtsrch 		; no
	cmp.l	(a2)+,(a3)+
	bne.s	nxtsrch
	move.l	(a3),d2
	clr.b	d2
	cmp.l	(a2),d2 		; last part of name matches?
	beq.s	found			; yes

nxtsrch add.w	#dos.drel,a0		; get next entry in the root dir
fndlp	dbf	d1,srchlp1
	bra	hfp_fnf 		; if we get here, file wasn't found

; here the dir entry for the image file is found at (a0), get to 1st sector of it
found
	move.l	dos_flen(a0),d2 	; filesize = "partition length"
	xlong	d2
	move.w	$14(a0),d0		; high word of 1st cluster of file
	rol.w	#8,d0
	swap	d0
	move.w	$1a(a0),d1		; low word of 1st cluster of file
	rol.w	#8,d1			;
	move.w	d1,d0			; d0 = 1rst cluster of file

	subq.l	#2,d0			; First data cluster is cluster 2

	move.l	d0,d1			; keep for multiply
	mulu	d3,d1			; lower 16 bit * sectors per cluster
	swap	d0
	mulu	d3,d0			; upper 16 bit * sectors per cluster
	swap	d0
	add.l	d0,d1			; combine
	add.l	d6,d1			; d1 = start sector of my qxl.win file

	move.l	d5,a3			; get my pointer back

; D1 is the start position of my container, as a sector number ( one sector =
; 512 bytes). The rest of the container must necessarily be contained in
; contiguous sectors from there on. (Perhaps in later versions it needn't be).
;
; For LBA drives, the partition offset in ddf_psoff is set to 0 and the real
; offset is kept at ddt_dtop-4 -

	move.l	d1,ddf_dtop-4(a4)
	moveq	#0,d1			; will go into ddf_psoff
	moveq	#-1,d2			; partition length unknown
	move.l	d1,d3
	move.l	#art.qflg,d4		; for QDOS disk
	move.l	d1,d0			; all ok
	movem.l (sp)+,hfp.reg
	rts

; v. 3.03 fix
nofat32 st	ddf_lba(a4)		; this is NOT an LBA drive, but CHS
	move.l	d3,d0			; physical root sector - MBR
	moveq	#1,d2
	jsr	id_diradd		; check
	bne	hfp_mchk
	jsr	hdl_rsint(a3)		; read sector
	beq	hfp_atri
	bra	hfp_npart		; no partition

	end
