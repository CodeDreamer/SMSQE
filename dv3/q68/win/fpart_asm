; DV3 QLSD Find "Partition" (= container file)	V1.00	 2017 W. Lenerz
; 2020 Mar 29	1.01 better multiply (use MK's routine)  (wl)
	section dv3

	xdef	hfp_sdhc

	include 'dev8_keys_err'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'
	include 'dev8_keys_dos'
	include 'dev8_mac_xword'
	include 'dev8_keys_q68'

;+++
; DV3 Q68 find (qxl.win / qubide) container file
;
; ******* !!!!
; If this is a container file on a Q68 SDHC card, then the following applies:
; The card must be formatted with the FAT32 file system, with a sector size of
; 512 butes and contain a partition table in sector 0. The QXL.WIN container
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

hfp_fnf moveq	#err.fdnf,d0		; card or file not found
	movem.l (a7)+,srchreg
	rts

hfp_sdhc
	movem.l srchreg,-(a7)		;
; chk_name: superficial name check of file on card this presumes that names have
; been normalized, which in turn means that illegal file names have been
; replaced by 0.

	clr.l	d1
	move.w	d7,d1			; drive nbr
	subq.w	#1,d1
	lsl.l	#4,d1			; relative offset in table
	move.l	hdl_targ(a3),a2 	; point to narmalized name...
	add.l	d1,a2			; ... for this drive
	move.l	(a2),d1 		; first part of file name
	beq.s	hfp_fnf 		; file not found

; check card nbr for this drive
	clr.l	d1
	lea	hdl_unit-1(a3),a1	; log to phys table
	move.b	(a1,d7.w),d1		; drive is on this card (either-1, 0 or q68_coffst)
	blt.s	hfp_fnf
	lea	hdl_buff(a3),a1 	; sector buffer
	move.b	#-1,dos_vmbr(a1)	; preset error
	moveq	#1,d2
	clr.l	d0			; I want to read sector 0
	jsr	hdl_rsint(a3)		; read this sector
	bne.s	hfp_fnf 		; ooops card doesn't exist at all or
					; isn't initialized
	move.b	dos_vmbr(a1),d0 	; check for valid MBR
	beq.s	hfp_mbr 		; seems to be ok so far
	cmp.b	#$80,d0 		; and this could be an ok value, too
	bne.s	hfp_fnf 		; ooops
hfp_mbr cmp.w	#dos.ftcs,dos_ftcs(a1)	; marker present?
	bne.s	hfp_fnf 		; no
	moveq	#1,d2			; always only read 1 sector

; I should now have the MBR at (a1)
; get nbr of partition 1 sector - is in Intel format, so switch around
	move.l	dos_prt1(a1),d0 	; partition start
	xlong	d0			; as block (=512 bytes sector) number
	move.l	d0,d4			; keep for later, start of partition
	jsr	hdl_rsint(a3)		; read partition sector into (a1)
	bne.s	hfp_fnf 		; ooops
	cmp.l	#'FAT3',dos_f32s(a1)	; check for FAT32 signature
	bne.s	hfp_fnf
	cmp.b	#'2',dos_f32s+4(a1)	; continue check for FAT32 signature
	bne.s	hfp_fnf
	cmp.b	#2,dos_sctl+1(a1)	; sector size =512 bytes?
	bne	hfp_fnf
	tst.b	dos_sctl(a1)		; still 512 bytes?
	bne	hfp_fnf
; I should now have what I can consider a valid fat32 partition
; now find the root dir
	clr.l	d3
	move.b	dos_clst(a1),d3 	; cluster size (sectors per cluster)
	beq	hfp_fnf 		; what??????
	move.l	dos_bspf(a1),d0 	; '"big" sectors per fat
	xlong	d0			; in big endian format
	move.l	d0,d6			; keep : d6 = sectors per fat
	clr.l	d5
	move.b	dos_fats(a1),d5 	; nbr of fats
	beq	hfp_fnf 		; what ????
					; now use MK's mutiply routine
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
	bra	hfp_fnf 		; if we get here, file wasn't found

; here the dir entry for the image file is found at (a0), get to 1st sector of it
found
	move.l	dos_flen(a0),d2 	; "partition" length
	xlong	d2
	move.w	$14(a0),d0		; high word of 1st cluster of file
	rol.w	#8,d0
	swap	d0
	move.w	$1a(a0),d1		; low word of 1st cluster of file
	rol.w	#8,d1
	move.w	d1,d0			; d0 = 1rst cluster of file
	subq.l	#2,d0			; !!! -2 !!!
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
	move.l	d1,d3
	moveq	#0,d0			; set condition codes
	movem.l (a7)+,srchreg
	rts

	end
