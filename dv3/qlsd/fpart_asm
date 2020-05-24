; DV3 QLSD Find "Partition" (= container file)	V1.02	 2018 W.Lenerz
;
; 2018-06-25  1.01  Keep error codes from sector read calls (MK)
; 2020-01-16  1.02  Allow direct QLWA format (MK)

	section dv3

	xdef	hd_fpart
	xdef	hd_fpart_internal

	xref	hd_rscard

	include 'dev8_dv3_qlsd_keys'
	include 'dev8_keys_err'
	include 'dev8_keys_dos'
	include 'dev8_keys_atari'
	include 'dev8_mac_xword'

;+++
; DV3 Q68 find (qxl.win / qubide) container file
;
; ******* !!!!
; If this is a container file on a Q68 SDHC card, then the following applies:
; The card must be formatted with the FAT32 file system, with a sector size of
; 512 bytes and contain a partition table in sector 0. The QXL.WIN container
; files must be in the FIRST partition and within the first 16 entries of
; the directory of that partition.
; ******* !!!!
;
;	d1  r	as d3 but in this format if required
;	d2  r	size of partition, -1 no partition table
;	d3  r	sector nbr with partition root, = sector 0 of qxl.win file
;	d4  r	should be partition ID, is scratch
;	d7 c  p drive ID / number
;	a3 c  p pointer to linkage block
;	a4 c  p pointer to physical definition
;
;	error return 0 or error
;---
srchreg reg	d5-d7/a0/a2/a3

hd_fpart
	movem.l srchreg,-(a7)
	bsr.s	hd_fpart_internal
	bne.s	hdf_exit

	subq.l	#2,d1			; First data cluster is cluster 2

	move.l	d1,d0			; keep for multiply
	mulu	d3,d1			; lower 16 bit * sectors per cluster
	swap	d0
	mulu	d3,d0			; upper 16 bit * sectors per cluster
	swap	d0
	add.l	d0,d1			; combine
	add.l	d6,d1			; d1 = start sector of my qxl.win file

; D1 is the start position of my container, as a sector number (one sector =
; 512 bytes). The rest of the container must necessarily be contained in
; contiguous sectors from there on. (Perhaps in later versions it needn't be).
hdf_d1
	move.l	d1,sdf_partoffset(a4)
	moveq	#0,d1			; will go into ddf_psoff
	moveq	#-1,d2			; partition length unknown
	moveq	#0,d3			; no partition offset
	move.l	#art.qflg,d4		; for QDOS disk
	moveq	#0,d0			; all ok
hdf_exit
	movem.l (a7)+,srchreg
	rts

;+++
;	d1  r	cluster number of start of partition
;	d2  r	size of partition, -1 no partition table
;	d3  r	sectors per cluster
;	d4  r	start sector of fat32
;	d6  r	start sector of data area
;	d7 c  p drive ID / number
;	a1    s
;	a2    s
;	a3 c  p pointer to linkage block
;
;	error return 0 or error
;---
hd_fpart_internal
; chk_name: superficial name check of file on card this presumes that names have
; been normalized, which in turn means that illegal file names have been
; replaced by 0.
	clr.l	d1
	move.w	d7,d1			; drive nbr
	subq.w	#1,d1
	lsl.l	#4,d1			; relative offset in table
	lea	qlsd_names(a3),a2	; point to normalized name...
	add.l	d1,a2			; ... for this drive
	move.l	(a2),d1 		; first part of file name
	beq	hfp_fnf 		; file not found

	lea	hdl_buff(a3),a1 	; sector buffer
	move.b	#-1,dos_vmbr(a1)	; preset error
	moveq	#1,d2
	clr.l	d0			; I want to read sector 0
	jsr	hd_rscard		; read this sector
	bne	hfp_rts 		; ooops card doesn't exist at all or
					; isn't initialized

	cmp.l	#'QLWA',(a1)		; Special, is SD directly QLWA?
	bne.s	hfp_check_mbr
; SD is not formatted with FAT but directly with QLWA
hfp_qlwa
	moveq	#2,d1			; Because there is an subq.l #2 later
	moveq	#0,d6			; No offset
;	 moveq	 #0,d0			; Should still be 0 from hd_rscard
	rts

hfp_check_mbr
	move.b	dos_vmbr(a1),d0 	; check for valid MBR
	beq.s	hfp_mbr 		; seems to be ok so far
	cmp.b	#$80,d0 		; and this could be an ok value, too
	bne	hfp_fnf 		; ooops
hfp_mbr cmp.w	#dos.ftcs,dos_ftcs(a1)	; marker present?
	bne	hfp_fnf 		; no
	moveq	#1,d2			; always only read 1 sector

; I should now have the MBR at (a1)
; get nbr of partition 1 sector - is in Intel format, so switch around
	move.l	dos_prt1(a1),d0 	; partition start
	xlong	d0			; as block (=512 bytes sector) number
	move.l	d0,d4			; keep for later, start of partition
	jsr	hd_rscard		; read partition sector into (a1)
	bne	hfp_rts 		; ooops
	cmp.l	#'FAT3',dos_f32s(a1)	; check for FAT32 signature
	bne	hfp_fnf
	cmp.b	#'2',dos_f32s+4(a1)	; continue check for FAT32 signature
	bne	hfp_fnf
	cmp.b	#2,dos_sctl+1(a1)	; sector size =512 bytes?
	bne	hfp_fnf
	tst.b	dos_sctl(a1)		; still 512 bytes?
	bne.s	hfp_fnf
; I should now have what I can consider a valid fat32 partition
; now find the root dir
	clr.l	d3
	move.b	dos_clst(a1),d3 	; cluster size (sectors per cluster)
	beq.s	hfp_fnf 		; what??????
	clr.l	d5
	move.b	dos_fats(a1),d5 	; nbr of fats
	beq.s	hfp_fnf 		; what ????
	move.l	dos_bspf(a1),d0 	; '"big" sectors per fat
	xlong	d0			; in big endian format
	move.l	d0,d6			; keep : d6 = sectors per fat
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

; root dir cluster * sectors per cluster
	move.l	d1,d0			; keep root dir cluster
	mulu	d3,d1			; lower 16 bit * sectors per cluster
	swap	d0
	mulu	d3,d0			; upper 16 bit * sectors per cluster
	swap	d0
	add.l	d1,d0			; combine

	add.l	d6,d0			; root dir start = data start + root dir sect
	jsr	hd_rscard		; read root dir 1st sector
	bne.s	hfp_rts
; check the first 16 entries in the root dir for my file
	moveq	#15,d1			; check first 16 entries in root dir
	move.l	a2,d0			; normalized name we're looking for
	move.l	a3,d5			; keep linkage block
	move.l	a1,a3			; point to start of dir
	move.l	a1,a0			; also point to start of dir
srchlp1 move.l	d0,a2			; point to my name (again)
not_fnd cmp.l	(a2)+,(a3)+		; name matches?
	bne.s	nxtsrch 		; no
	cmp.l	(a2)+,(a3)+
	bne.s	nxtsrch
	move.l	(a3),d2
	clr.b	d2
	cmp.l	(a2),d2 		; last part of name matches?
	beq.s	found			; yes

nxtsrch add.w	#dos.drel,a0		; get next entry in the root dir
	move.l	a0,a3
fndlp	dbf	d1,srchlp1
	move.l	d5,a3			; get my pointer back
hfp_fnf moveq	#err.fdnf,d0		; card or file not found
hfp_rts rts

; here the dir entry for the image file is found at (a0), get to 1st sector of it
found
	move.l	d5,a3			; get my pointer back
	move.l	dos_flen(a0),d2 	; "partition" length
	xlong	d2
	move.w	$14(a0),d1		; high word of 1st cluster of file
	rol.w	#8,d1
	swap	d1
	move.w	$1a(a0),d1		; low word of 1st cluster of file
	rol.w	#8,d1			; d1 = 1st cluster of file
hfp_ok
	moveq	#0,d0			; all ok!
	rts

	end
