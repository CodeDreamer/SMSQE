; Q68 check non-fragmentation of qxl.win file  1.00. Copyright (c) W. Lenerz 2016

	section exten

	xdef	win_chk

	xref	hdt_doact		; cause follwoing code to exec as device driver (in windrv_asm)
	xref	hd_byte 		; (windrv_asm), thing parameter
	xref	gu_achpp
	xref	gu_rchp
	xref	hfp_sdhc
	xref	crd_renm
	xref	get_rut2

	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'
	include 'dev8_keys_q68'
	include 'dev8_keys_thg'
	include 'dev8_keys_err'
	include 'dev8_keys_dos'
	include 'dev8_mac_thg'
	include 'dev8_mac_xword'
	include 'dev8_mac_assert'

;!!!
;!!!!
hdt.reg reg	d1-d7/a0-a5 ; DON'T CHANGE THIS UNLESS IT IS ALSO CHANGED
;!!!!				   ; IN dev8_dv3_q68_qlsd_windrv_asm
;!!!

hfp_fnf moveq	#err.itnf,d0
	rts

;************************** Extention thing routine
; WIN_CHECK drive
;
; check that a drive lies in contiguous sectors on the card, by reading the
; FAT of the card
;
;***************************
  
win_chk thg_extn {CHEK},crd_renm,hd_byte
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

	clr.l	d1
	move.w	d7,d1			; drive nbr
	subq.w	#1,d1
	lsl.l	#4,d1			; relative offset in table
	move.l	hdl_targ(a3),a4 	; point to normalized name...
	add.l	d1,a4			; ... for this drive
	move.l	(a4),d1 		; first part of file name
	beq.s	hfp_fnf 		; file not found, there can be no drive

; check card nbr for this drive
	clr.l	d1
	lea	hdl_unit-1(a3),a1	; log to phys table
	move.b	(a1,d7.w),d7		; drive is on this card (either-1, 0 or q68.coff)
	blt.s	hfp_fnf 		; there is no drive
	move.l	#512,d0
	jsr	get_rut2		; find partition & info
	bne	not_fnd
					; this call returned :
;	d0  r	0 or error
;	d1    s
;	d2  r	nbr of sectors per fat
;	d3  r	cluster size (sectors per cluster)
;	d4  r	start of FAT (sector nbr)
;	d5    s
;	d6  r	sector nbr of start of dir
;	d7 cr	cardnbr (0 or q68.coff)
;	a1 c	pointer to memspace, contains DIR
;	a2    s
;	a3 c  p linkage block
;	a6 c  p sysvars
;
;	error return 0 or error
				 
	move.l	d4,d3			; keep fat start number
; check the first 16 entries in the root dir for my file
	moveq	#15,d1			; check first 16 entries in root dir
	movem.l (a4),d4-d6		; my filename in FAT32 format
	move.l	a1,a0			; point to start of dir
srchlp1 move.l	a0,a2			; point to name in dir
	cmp.l	(a2)+,d4		; name matches?
	bne.s	nxtsrch 		; ... no
	cmp.l	(a2)+,d5		; next part of name
	bne.s	nxtsrch
	move.l	(a2),d0 		; last part
	clr.b	d0
	cmp.l	d6,d0			; last part of name matches?
	beq.s	found			; ... yes, got the file

nxtsrch add.w	#dos.drel,a0		; get next entry in the root dir
fndlp	dbf	d1,srchlp1
	bra	hfp_fnf 		; if we get here, file wasn't found
		
found
; here the dir entry for the image file is found at -8(a2)
; get the 1st cluster of file
	move.w	$14-8(a2),d0		; high word of 1st cluster of file
	rol.w	#8,d0
	swap	d0
	move.w	$1a-8(a2),d0		; low word of 1st cluster of file
	rol.w	#8,d0			; d0 = 1rst cluster of file
	move.l	d0,d1			; keep
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
rel_mem move.l	a1,a0
	jmp	gu_rchp 		; return with result in d0

no_hold moveq	#err.fdiu,d0		; couldn't hold the interface
chk_out rts

not_fnd moveq	#err.itnf,d0		; drive wasn't found
	bra.s	rel_mem
err_file
	moveq	#err.mchk,d0		; drive is fragmented
	bra.s	rel_mem

	end
