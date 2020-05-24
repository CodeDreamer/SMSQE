; Q68 check non-fragmentation of qxl.win file  1.00. Copyright (c) W. Lenerz 2020

	section exten

	xdef	win_chk
	xdef	get_dir
	xdef	get_dir2

	xref	hdt_doact		; cause following code to exec as device driver (in windrv_asm)
	xref	gu_achpp
	xref	gu_rchp
	xref	crd_dir
	xref	hd_byte


	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'
	include 'dev8_keys_q68'
	include 'dev8_keys_thg'
	include 'dev8_keys_err'
	include 'dev8_keys_dos'
	include 'dev8_mac_thg'
	include 'dev8_mac_xword'
	include 'dev8_mac_assert'


	include 'dev8_keys_qdos_ioa'


hfp_fnf moveq	#err.itnf,d0
hfp_out rts

hdt.reg reg	d1-d7/a0-a5 ; DON'T CHANGE THIS UNLESS IT IS ALSO CHANGED
;!!!!			    ; IN dev8_dv3_q40_hd_thing_asm  and carddir_asm


;************************** Extention thing routine
; WIN_CHECK drive
;
; check that a drive lies in contiguous sectors on the card, by reading the
; FAT of the card
;
;***************************
  
win_chk thg_extn {CHEK},crd_dir,hd_byte
	movem.l hdt.reg,-(a7)
	bsr	hdt_doact		; this calls the following code in super mode

; Code is called as device driver, in supervisor mode with a3 pointing to device
; definition block and a4 pointing to drive defn (if it exists)
; on entry, condition code = Z if a drive definition is found, else NE
;	d0 c	0 or error if no drive definition was found
;	d7 c  p drive ID / number  (from 1 to 8)
;	a1 c  p addres of thing
;	a3 c  p device driver linkage block
;	a4 c  p drive definition block (if it exists)

	lea	hdl_end-12(a3),a2
	moveq	#0,d0
	move.w	d7,d0
	mulu	#12,d0
	add.w	d0,a2			; normalized name we're looking for
	move.l	(a2),d1 		; first part of file name
	beq.s	hfp_fnf 		; file not found, there can be no drive
	bsr	get_dir
	bne.s	hfp_out

; Here I should have the parition root dir
; d2 = 1
; d3 = cluster size (sectors per cluster)
; d4 = start of fat
; d5 = sectors per fat
; d6 = start of data (not fat) sectors on disk
; d7 = drv nbr
; a0 = allcoated space	(currently holding 1st sector of root dir + data for A4)
; a1 = same as A0
; a3 = device driver linkage block
; a4 = pretend drive defn block, part of the space pointed to by A0

	move.l	d4,d3

; check the first 16 entries in the root dir for my file
	moveq	#15,d1			; check first 16 entries in root dir
	lea	hdl_end-12(a3),a2
	moveq	#0,d0
	move.w	d7,d0
	mulu	#12,d0
	add.l	a2,d0			; normalized name we're looking for

	move.l	a1,a5			; point to start of dir
srchlp1 move.l	d0,a2			; point to my name (again)
	cmp.l	(a2)+,(a5)+		; name matches?
	bne.s	nxtsrch 		; no
	cmp.l	(a2)+,(a5)+
	bne.s	nxtsrch
	move.l	(a5),d2
	clr.b	d2
	cmp.l	(a2),d2 		; last part of name matches?
	beq.s	found			; yes

nxtsrch add.w	#dos.drel,a1		; get next entry in the root dir
	move.l	a1,a5
fndlp	dbf	d1,srchlp1
	bra.s	not_fnd 		; if we get here, file wasn't found

; here the dir entry for the image file is found at (a1), get to 1st sector of it
found
	move.w	$14(a1),d0		; high word of 1st cluster of file
	rol.w	#8,d0
	swap	d0
	move.w	$1a(a1),d1		; low word of 1st cluster of file
	rol.w	#8,d1			;
	move.w	d1,d0			; d0 = 1st cluster of file
	move.l	d0,d1
; find place where this cluster is in FAT
	move.l	d0,d5			; keep cluster nbr
	lsr.l	#7,d0			; 1 sector in fat = 128 cluster entries
	move.l	d0,d2			; !!!!! must set back to 1 later
	lsl.l	#7,d2			; * 128 again
	sub.l	d2,d5			; remainder of division = where in sector
	add.l	d0,d3			; this is now the sector in the fat
					; (direct sector nbr)
	move.l	d3,d0			; this is the sector of the FAT to read
	moveq	#1,d2			; read one sector
	move.l	a0,a1
	jsr	hdl_rsint(a3)		;
	lea	512(a1),a2		; end of sector
	move.l	a2,d6			; keep
	move.l	d5,d0			; reminder of division
	lsl.w	#2,d0			; *4 there are 4 bytes per table entry
	move.l	a1,a2			; start of this fat sector
	add.l	d0,a2			; where to start checking in that sector
	move.l	#$ffffff07,d4		; anything larger(unsigned) = eof marker
	bra.s	check

; read every entry in the fat and check that it is the previous one +1, until
; we hit the end of file marker
; d4 = current sector of fat
; a1 = pointer to start of space
; a2 = pointer to data
; d1 = file cluster nbr; next entry in fat should be this + 1, if not the file...
;      is fragmented
cklp1	move.l	a1,a2
check	move.l	(a2)+,d0		; next fat entry
	beq.s	rel_mem 		; empty, so end of file, all ok
	cmp.l	d4,d0			; end of file marker?
	bhi.s	ok			; ...yes, done, all ok
	rol.w	#8,d0			; make nbr in motorola format
	swap	d0
	rol.w	#8,d0
	addq.l	#1,d1			; next entry if not fragmented
	cmp.l	d0,d1			; is it?
	bne.s	err_file		; ... no, so file is fragmented
	cmp.l	d6,a2			; end of this fat sector reached?
	blt.s	check			; ... no
	addq.l	#1,d3			; ... yes so read next fat sector
	move.l	d3,d0			; this is the sector to read
	jsr	hdl_rsint(a3)
	beq.s	cklp1			; and check it, if read ok
	bra.s	rel_mem 		; pb reading

ok	moveq	#0,d0
rel_mem jmp	gu_rchp 		; return with result in d0
			    
not_fnd moveq	#err.itnf,d0		; drive wasn't found
	bra.s	rel_mem

nimp	moveq	#err.nimp,d0
	bra.s	rel_mem
hfp_npart
	moveq	#err.mchk,d0
	bra.s	rel_mem

no_hold moveq	#err.fdiu,d0		; couldn't hold the interface
chk_out rts
	
err_file
	moveq	#err.mchk,d0		; drive is fragmented
	bra.s	rel_mem



; get root dir cluster
; D0   r  0 or error
; d2   r  1
; d3   r  cluster size (sectors per cluster)
; d4   r  start of fat
; d5   r  sectors per fat
; d6   r  start of data (not fat) sectors on disk
; d7 cp   drv nbr
; a0   r  memory block if no error, or spurious value if not
; a1   r  mem (currently  1st sector of root dir)
; a3 cp   device driver linkage block
; a4   r  pretend drive defn block (within mem block pointed by A0)
get_dir
	sub.l	a0,a0
	lea	hdl_part-1(a3),a1	;
	moveq	#0,d4
	move.b	(a1,d7.w),d4		; partition required
	blt	hfp_fnf 		; there is no drive
get_dir2
	move.l	#512+ddf_dtop,d0	; sector buffer + pretend drv def block
	jsr	gu_achpp
	bne	hfp_out 		; oops
	lea	512(a0),a4		; pretend there's a drive defintion block
	move.l	a0,a1			;
	moveq	#1,d2			; d0 is already 0 -read sector 0 of disk
	jsr	hdl_rsint(a3)		; read sector
	bne.s	hfp_npart		; no partition
	lsl.w	#4,d4
	add.w	#dos_prt1,d4
	move.l	(a1,d4.w),d0		; partition start
	beq.s	not_fnd 		; there is none, partition doesn't exist
	cmp.w	#dos.ftcs,dos_ftcs(a1)	; (unswapped) FAT32 marker present?
	beq.s	chk_fat32_normal	; ... yes
	cmp.w	#dos.ftsw,dos_ftcs(a1)	; (swapped) FAT32 marker present?
	bne	nimp			; no, so no fat32 partition, not implemented

chk_fat32_switched			; bytes were switched, swap only words
	swap	d0
	bra.s	common

chk_fat32_normal			; bytes not switched, swap bytes & words
	xlong	d0
common	moveq	#1,d2			; read 1 sector
	move.l	d0,d4			; keep for later, start of partition
	jsr	hdl_rsint(a3)		; read partition root sector into (a1)
	bne	rel_mem 		; ooops

; I  should now have the partition root sector at (a1)
	cmp.l	#'FAT3',dos_f32s(a1)	; check for FAT32 signature
	bne	nimp			; ... nope
	cmp.b	#'2',dos_f32s+4(a1)	; continue check for FAT32 signature
	bne	nimp
	cmp.b	#2,dos_sctl+1(a1)	; sector size =512 bytes?
	bne	nimp			; I can't handle anything else
	tst.b	dos_sctl(a1)		; still 512 bytes?
	bne	nimp			; ... no

; Here I should have what I can consider a valid fat32 partition.
; Now find the root dir.
; d2 = 1
; d4 = start of partition
; a0 = mem
; a1 = mem (currently parition root sector)
; a4 = pretend drive defn block

	moveq	#err.itnf,d0		; preset error
	moveq	#0,d3
	move.b	dos_clst(a1),d3 	; cluster size (sectors per cluster)
	beq	rel_mem 		; what??????
	moveq	#0,d1
	move.b	dos_fats(a1),d1 	; nbr of fats
	beq	rel_mem 		; what ????
	move.l	dos_bspf(a1),d0 	; "big" sectors per fat
	xlong	d0			; in big endian format
	move.l	d0,d6			; keep : d6 = sectors per fat
	move.l	d0,d5
					; now use MK's mutiply routine
	mulu	d1,d6			; lower 16 bit * number of fats
	swap	d0
	mulu	d1,d0			; upper 16 bit * number of fats
	swap	d0
	add.l	d0,d6			; combine

	add.l	d4,d6			; add start of partition
	moveq	#0,d0
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
	jsr	hdl_rsint(a3)		; read root dir 1st sector
	bne	rel_mem
	rts

	end
