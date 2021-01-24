; Q68 the CARD_xxxx keywords, except card_init	1.01. Copyright (c) W. Lenerz 2016-2020

; 2020-11-27   1.03  card_speedup added, removed test code (wl)
; 2020-06-06   1.02  card_create bugfix (thanks Marcel) (wl)
; 2020-04-12   1.01  1st byte of filename in dir =0 means empty slot+end of dir (wl)

	section exten

	xdef	crd_renm
	xdef	get_rut2

	xref	hdt_doact		; cause following code to exec as device driver (in windrv_asm)
	xref	hd_byte 		; (windrv_asm), thing parameter
	xref	gu_achpp
	xref	gu_rchp
	xref	hfp_sdhc		; get to dos dir
	xref	norm_nm 		; normalise smsqe name into FAT 8.3 name
	xref	cv_rtdos		; convert my time to dos time


	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'
	include 'dev8_keys_q68'
	include 'dev8_keys_thg'
	include 'dev8_keys_err'
	include 'dev8_mac_thg'
	include 'dev8_mac_assert'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_dos'
	include 'dev8_mac_xword'


;!!!
;!!!!
hdt.reg reg	d1-d7/a0-a5 ; DON'T CHANGE THIS UNLESS IT IS ALSO CHANGED
;!!!!				   ; IN dev8_dv3_q68_win_windrv_asm
;!!!


crdren	 dc.w	thp.ubyt	 ; card nbr
	 dc.w	thp.call+thp.str ; old name string
	 dc.w	thp.call+thp.str ; new name string
	 dc.w	0

mdir	 dc.w	thp.ubyt	 ; drive
	 dc.w	thp.call+thp.str ; string
	 dc.w	0
creat	 dc.w	thp.ubyt	 ; drive
	 dc.w	thp.uwrd	 ; size (in MiB)
	 dc.w	thp.call+thp.str ; string
	 dc.w	0
  
jth_rwrd dc.w	thp.ret+thp.uwrd
	 dc.w	0


;************************** Extention thing routine
;
; CARD_RENF card,"oldname","newname"
;
; Rename file on card from old name to new name, within the first
; 16 entries in the FAT32 directory
;
;***************************
  
crd_renm thg_extn {CREN},crd_dir,crdren

	movem.l hdt.reg,-(a7)
	bsr	hdt_doact		; this calls the following code in super mode

; Code is called as device driver, in supervisor mode with a3 pointing to device
; definition block and a4 pointing to drive defn (if it exists)
; on entry, condition code = Z if a drive definition is found, else NE
;	d0 c	0 or error if no drive definition was found
;	d7 c  p drive ID / number  (from 1 to 8)
;	a1 c  p address of thing
;	a3 c  p device driver linkage block

	move.l	#512+24,d0		; res. mem read buffer + 2x name into A1
	bsr.s	get_rut 		; get root dir sect into (a1)
	bne.s	exit			; (keeps old a1 in a5)
	move.l	a1,a2			; keep mem pointer

; normalise old name
	move.l	4(a5),a0		; pointer to smsqe name (old)
	lea	512(a2),a1		; space for normalised name  (old)
	move.l	a1,a4			; keep this
	bsr	norm_nm 		; normalise name into (a1)
	move.l	a2,a1			; in case of error
	bne.s	rel_a1			; ooops

; normalise new name
	move.l	12(a5),a0		; new smsqe name
	lea	12(a4),a1		; space for normalized new name
	move.l	a1,a5			; keep ptr to normalized new name
	bsr	norm_nm 		; normalise name into (a1)
	move.l	a2,a1
	bne.s	rel_a1

; a1	  pointer to memspace with 1st sector of root dir
; a3	  linkage block
; a4	  old name (normalised -> no leading length word!)
; a5	  new name (normalised -> no leading length word!)
; a6	  sysvars

; I should now have the root dir at (a1)
; check the first 16 entries in the root dir for my old file
	move.l	d6,d0			; keep sector nbr
	movem.l (a4),d5-d7		; get name to search for
	clr.b	d7			; LSB of extn should be 0
	moveq	#15,d1			; check first 16 entries in root dir
srchlp1 move.l	a2,a4			; point start of dir entry
not_fnd cmp.l	(a4)+,d5		; name matches?
	bne.s	nxtsrch 		; no
	cmp.l	(a4)+,d6		; 2nd part of name matches?
	bne.s	nxtsrch 		; no
	move.l	(a4),d2
	clr.b	d2
	cmp.l	d7,d2			; last part of name matches?
	beq.s	found			; ... yes, replace
nxtsrch add.w	#dos.drel,a2		; ... no, get next entry in the root dir
fndlp	dbf	d1,srchlp1

; if we get here, old name wasn't found
	bra.s	 errnf

; I found the old name, now replace by new name
; d0 = sector number
found	move.l	(a5)+,(a2)+
	move.l	(a5)+,(a2)+
	move.w	(a5)+,(a2)+
	move.b	(a5),(a2)
	clr.l	d7
	moveq	#1,d2
	jsr	hdl_wsint(a3)		; write dir sector back
	bra.s	rel_a1			; done

mchkf	moveq	#err.mchk,d0
	bra.s	rel_a1

errnf	moveq	#err.itnf,d0		; old name wasn't found

rel_a1	move.l	a1,a0
	bra	gu_rchp

errbp	moveq	#err.ipar,d0
exit	rts
   

;--------------------------------------------
; DV3 Q68 find 1st sector of main FAT32 dir
;
;	d0 cr	amount of mem to reserve  /  0 or error
;	d1    s
;	d2  r	nbr of sectors per fat
;	d3  r	cluster size (sectors per cluster)
;	d4  r	start of FAT1 (sector nbr)
;	d5    s
;	d6  r	sector nbr of start of dir
;	d7 cr	get_rut : cardnbr (1 or 2)| 0
;	   cr	get_rut2 : 0 or q68.coff for card1 or 2  | 0
;	a0    s
;	a1  r	 pointer to memspace, contains DIR
;	a2    s
;	a3 c  p linkage block
;	a5  r	parameter pointer (old a1)
;	a6 c  p sysvars
;
;	error return 0 or error
;	if there is any error, this routine releases the memory it has reserved
;-------------------------------------------


get_rut subq.l	#1,d7
	lsl.w	#q68_dshft,d7
get_rut2
	move.w	d7,d2
	beq.s	ok
	sub.w	#q68.coff,d2
	bne	errbp
ok	move.l	a1,a5
	jsr	gu_achpp
	bne.s	exit
memok	move.l	a0,a1
	lea	hdl_unit-1(a3),a2
	move.b	d7,(a2) 		; pretend we're reading drive 0
	move.l	d7,a2			; restitute into d7 at end
	moveq	#1,d2			; read one sector
	clr.l	d0			; I want to read sector 0
	clr.l	d7			; for this "drive"
	jsr	hdl_rsint(a3)		; read this sector
	beq.s	got_sec 		; got it
	jsr	hdl_ststp(a3)		; try to INIT the card
	bne	rel_a1
	clr.l	d0
	jsr	hdl_rsint(a3)		; re-read this sector
	bne	rel_a1			; ooops card doesn't exist at all or
					; isn't initialized
got_sec move.b	dos_vmbr(a1),d0 	; check for valid MBR
	beq.s	hfp_mbr 		; seems to be ok so far
	cmp.b	#$80,d0 		; and this could be an ok value, too
	bne	mchkf			; ooops
hfp_mbr cmp.w	#dos.ftcs,dos_ftcs(a1)	; marker present?
	bne	mchkf			; no

; I should now have the MBR at (a1)
; get nbr of partition 1 sector - is in Intel format, so switch around
	move.l	dos_prt1(a1),d0 	; partition start
	xlong	d0			; as block (=512 bytes sector) number
	move.l	d0,d4			; keep for later, start of partition
	jsr	hdl_rsint(a3)		; read partition sector into (a1)
	bne	out			; ooops
	cmp.l	#'FAT3',dos_f32s(a1)	; check for FAT32 signature
	bne	mchkf
	cmp.b	#'2',dos_f32s+4(a1)	; continue check for FAT32 signature
	bne	mchkf
	cmp.b	#2,dos_sctl+1(a1)	; sector size =512 bytes?
	bne	mchkf
	tst.b	dos_sctl(a1)		; still 512 bytes?
	bne	mchkf
; I should now have what I can consider a valid fat32 partition
; now find the root dir
	clr.l	d3
	move.b	dos_clst(a1),d3 	; cluster size (sectors per cluster)
	beq	mchkf			; what??????
	move.l	dos_bspf(a1),d0 	; '"big" sectors per fat
	xlong	d0			; in big endian format
	move.l	d0,d6			; keep : d6 = sectors per fat
	move.l	d6,d2
	clr.l	d5
	move.b	dos_fats(a1),d5 	; nbr of fats
	beq	mchkf			  ; what ????
	subq.w	#2,d5			; dbf for primitive multiply
	bgt	mchkf
	blt.s	mul1ok			; we're done for this calculation
mul1lp	add.l	d0,d6			; calculate nbr of fats * sectors per fat
	dbf	d5,mul1lp		; at the end d6=old d6*d5
mul1ok	add.l	d4,d6			; add start of partition
	clr.l	d0
	move.w	dos_ress(a1),d0
	rol.w	#8,d0			; reserved sector count
	add.l	d0,d6			; d6 + res sect count = data start
	add.l	d0,d4			; D4 = start of fat

	move.l	dos_rdir(a1),d1 	; root dir cluster
	xlong	d1
	subq.l	#2,d1			; always starts at 2 less!
	move.l	d1,d0			; keep root dir cluster

; another primitive multiply		; root dir cluster * sectors per cluster
	move.w	d3,d5			; sectors per cluster, can't be  0
	subq.w	#2,d5			;
	blt.s	mul2ok			; done with multiplying
mul2lp	add.l	d1,d0
	dbf	d5,mul2lp		; at the end d0 = root dir sector
mul2ok
	add.l	d6,d0			; root dir start = data start + root dir sect
	move.l	d0,d6
	move.l	d2,d5
	moveq	#1,d2
	jsr	hdl_rsint(a3)		; read root dir 1st sector
	move.l	d5,d2
	tst.l	d0
out	rts


	      
;************************** Extension thing routine
;
; ret$ = CARD_DIR$ (card )
;
; function to get the 1st 16 dir entries of the card (Fat32)
;
;***************************
			
crd_dir thg_extn {CDIR},crd_crea,mdir
	movem.l hdt.reg,-(a7)
	bsr	hdt_doact		; this calls the following code in super mode

; Code is called as device driver, in supervisor mode with a3 pointing to device
; definition block and a4 pointing to drive defn (if it exists)
; on entry, condition code = Z if a drive definition is found, else NE
;	d0 c	0 or error if no drive definition was found
;	d7 c  p drive ID / number  (from 1 to 8)
;	a1 c  p address of thing
;	a3 c  p device driver linkage block

	move.w	2(a1),d0		; buffer size
	move.l	d0,d1
	sub.w	#14*12,d1
	blt	errbp			; not enough
	move.l	#512,d0
	bsr	get_rut
	bne.s	out			; oops

	move.l	4(a5),a4		; point to buffer
	move.l	a4,a5
	move.w	#16*14,(a4)+
; I should now have the root dir at (a1)
; read the first 16 entries in the root dir
	moveq	#15,d1			; read first 16 entries in root dir
	move.w	#$200a,d3		; space+lf
	move.l	#$ff000000,d2
	move.l	a1,a2			; point to start of dir
loop1	move.l	a2,a3			; point start of dir entry
	move.l	(a3)+,d0
	rol.l	#8,d0
	beq.s	dir_eof
	cmp.b	#$e5,d0
	beq.s	empty
	tst.b	d0
	beq.s	dir_eof
	ror.l	#8,d0
	move.l	d0,(a4)+
	move.l	(a3)+,(a4)+
	move.l	(a3),d0
	cmp.b	#$0f,d0
	beq.s	longname
	move.b	#'.',d0 		; insert "." before the extension
	ror.l	#8,d0
	move.l	d0,(a4)+
lp1	move.w	d3,(a4)+
	add.w	#dos.drel,a2		; get next entry in the root dir
	dbf	d1,loop1
cd_out	clr.l	d0
	bra	rel_a1

empty	lea	mty_str,a3
cpy	move.l	(a3)+,(a4)+
	move.l	(a3)+,(a4)+
	move.l	(a3)+,(a4)+
	bra.s	lp1

longname
	subq.l	#8,a4
	lea	lname,a3
	bra.s	cpy

; the end of the dir was reached, set all remaining to empty
dir_eof lea	mty_str,a2
cpy2	move.l	(a2),(a4)+
	move.l	4(a2),(a4)+
	move.l	8(a2),(a4)+
	move.w	d3,(a4)+
	dbf	d1,cpy2
	bra.s	cd_out

mty_str dc.l	'-- E','mpty',' -- '	     ;"-- Empty -- "
lname	dc.l	'-Lon','g na','me- '


;************************** Extension thing routine
;
; CARD_CREATE card,size_in_MiB,filename$
;
; create a file on the card
;
; error returns :
;
;  err.drfl equ    -11	 DRive FuLl  : no space for a file of this size
;  err.fex  equ    -8	 a File with this name already EXists
;  err.inam equ    -12	 Invalid file name (not an 8.3 name)
;  err.ipar equ    -15	 wrong card number ( not 1 or 2)
;  err.mchk equ    -16	 Medium CHecK failed because card wasn't readable
;			 (perhaps absent / not initialized?) or isn't a valid
;			 FAT32 partition
;  err.accd equ    -23	 Access denied because drive is fragmented
;  err.imem equ    -3	 Insufficient MEMory for operation
;  err.bffl equ    -5	 BuFfer FuLl : there is no space in the first 16 dir entries for a new file
;  err.orng equ    -4	 projected size of the file to big, or 0 or negative
;
;***************************
		  

fsize	equ	 4	; file size in mb
fatsz	equ	 8	; nbr of sectors per fat
clustsz equ	12	; cluster size (sectors per cluster)
fatstrt equ	16	; start of FAT (sector nbr)
dirsect equ	20	; sector nbr of start of dir
dirslot equ	24	; empty slot in dir
fclust	equ	28	; nbr of clusters needed for the file
nmoffset equ	fclust+4


errorng moveq	#err.orng,d0
	rts

errfex	moveq	#err.fex,d0
	bra.s	rel_mem

errbffl moveq	#err.bffl,d0
	bra.s	rel_mem

errdrfl moveq	#err.drfl,d0
rel_mem bra	gu_rchp

errinam moveq	#err.inam,d0
crea_er rts

crd_crea thg_extn {CREA},crd_spd,creat

	movem.l hdt.reg,-(a7)
	bsr	hdt_doact		; this calls the following code in super mode

; Code is called as device driver, in supervisor mode with a3 pointing to device
; definition block and a4 pointing to drive defn (if it exists)
; on entry, condition code = Z if a drive definition is found, else NE
;	d0 c	0 or error if no drive definition was found
;	d7 c  p card nbr (1 or 2)
;	a1 c  p address of thing
;	a3 c  p device driver linkage block
	move.l	#600,d0 		; reserve mem for a sector+ workspace
	bsr	get_rut 		; a5 becomes param ptr , a1 mem ptr
	bne.s	crea_er 		; d7 becomes 0

; normalise name
	move.l	a1,a4			; keep mem ptr
	move.l	(a5),d0 		; size needed
	ble.s	errorng 		; wrong size
	move.l	8(a5),a0		; pointer to smsqe name
	lea	512(a1),a5
	lea	nmoffset(a5),a1 	; space for normalised name
	movem.l d0/d2/d3/d4/d6,fsize(a5); keep :
					;   d0	 size needed
					;   d2	 nbr of sectors per fat
					;   d3	 cluster size (sectors per cluster)
					;   d4	 start of FAT (sector nbr)
					;   d6	 sector nbr of start of dir
	bsr	norm_nm 		; normalise name into (a1)
	move.l	a4,a1
	bne	rel_a1			; couldn't
					
; a1 mem space , contains 1st sector of dir
; a3 device linkage
; a5 poiner to work space, including normalized name

; check the first 16 entries in the root dir for an empty slot
; and that file name doesn't already exist
	moveq	#15,d1			; read first 16 entries in root dir
	move.l	a1,a2			; point to start of dir
	lea	nmoffset(a5),a0
	movem.l (a0),d3-d5		; my normalized name
	moveq	#-1,d2
	move.l	d2,a0			; a0 will be ptr to empty slot
crealp1 move.l	a2,a4			; point start of dir entry
	move.l	(a4)+,d0
	rol.l	#8,d0
	beq.s	mpty			; this is an empty slot
	cmp.b	#$e5,d0
	bne.s	nt_mpty 		; this is not an empty slot
mpty	cmp.l	d2,a0			; previous empty slot exists?
	bne.s	cr_dolp 		; yes, check next dir entry
	move.l	a2,a0			; point to empty slot
	bra.s	cr_dolp
nt_mpty ror.l	#8,d0
	cmp.l	d0,d3			; match for name?
	bne.s	cr_dolp 		; no, fine
	cmp.l	(a4)+,d4		; match?
	bne.s	cr_dolp 		; no, fine
	move.l	(a4),d0
	clr.b	d0
	cmp.l	d0,d5			; name match even here ?
	beq	errfex			; yes, error file already exists
cr_dolp add.w	#dos.drel,a2		; get next entry in the root dir
	dbf	d1,crealp1
	cmp.l	a0,d2			; did we find an empty slot?
	beq	errbffl 		; no, error
	move.l	a0,dirslot(a5)		; keep ptr to dir entry

; when we get here : name is valid and can be set in main dir
; now check FAT for contiguous nbr of clusters free clusters
; first get nbr of sectors the file needs
	move.l	fsize(a5),d6		; file size in MiB
	move.l	d6,d5
	moveq	#11,d2			; shift nbr to go from MiB to sectors
	lsl.l	d2,d6			; nbr of sectors needed for file

; now find out how many clusters I need. To avoid a long division, find out how
; often I need to shift nbr of sects (nbr of clusters is always a power of 2)
	move.l	clustsz(a5),d0		; nbr of sectors per cluster
	moveq	#7,d2			; 7 would be 128 sectors per cluster
cr_lp2	btst	d2,d0			; this many sects per cluster?
	bne.s	cr_fnd1 		; (nb could also shift down)
	subq.w	#1,d2
	beq	mchkf			; I haven't found nbr of sects/clust
	bra.s	cr_lp2			; try next factor of 2
cr_fnd1 move.l	d6,d0			; d0 = nbr of sectors for file
	lsr.l	d2,d0			; nbr of clusters neded
	move.l	d0,d3			; in here
	lsl.l	d2,d0			; shifting may leave a rest
	sub.l	d6,d0			; if this is 0, no rest
	bge.s	no_add
	addq.l	#1,d3			; final nbr of clusters needed
	
no_add	move.l	d3,d0
	lsr.l	#7,d0			; 128 cluster entries in a sector
	swap	d0
	tst.w	d0			; not more than sectors are allowed
	beq.s	goodnbr 		; and we're good
	moveq	#err.orng,d0
	bra	rel_mem 		; release mem & come back with error
goodnbr
	move.l	d3,fclust(a5)		; keep
	move.l	fatsz(a5),d1		; nbr of sectors per fat
	subq.w	#1,d1			; prepare for dbf in subroutine
	move.l	fatstrt(a5),d5		; sector nbr of start of fat
	move.l	d3,d6

; d1 = nbr of sectors per fat, adjusted for dbf
; d3 = nbr of clusters needed for the file
; d5 = sector nbr of start of fat
; d3 =
	moveq	#127,d4 		; check all entris in sector
	move.l	a1,a2			; start at beginning of sector
	bsr	gt_mpty 		; get first free (= empty) cluster
fnd_0	bne	crea_er 		; couldn't

; I now have the first entry in the fat which shows an empty cluster.
; on return:
; d1  nbr of sects remaining to be read in fat
; d3  nbr of clusters needed for file
; d5  sector nbr where empty slot was found
; d6  =d3
; a2  pointer to empty slot +4

; would there be enough space for my clusters if all the remaining were empty?
chk_ful move.l	d1,d0			; sectors remaining in the FAT
	lsl.l	#7,d0			; custer entries remaining in the fat
	move.l	a2,d2			; where we are in current sector
	sub.l	a1,d2
	lsr.l	#2,d2			; clusters reaining as of here in currrent sector
	add.l	d2,d0			; total clusters remaining
	sub.l	d3,d0			; are there enough (potentially) free
	blt	errdrfl 		; no, leave with error drive full ->

	movem.l d4/d5/a2,-(a7)

; now check that there are enough contiguous free clusters
; on return :
; d3  0 if all clusters needed were free
; search for the first non free cluster
	bsr	gt_full 		; find enough contiguous clusters
	beq.s	no_err			; no read error per se
	lea	12(a7),a7		; forget the saved regs
	bra	rel_a1
no_err
	tst.l	d3			; did we find enough clusters?
	beq.s	enough			; yes
; we didn't find enough clusters, try to restart search from here
	lea	12(a7),a7		; forget saved regs
	moveq	#1,d2
	move.l	d6,d3
	bsr	gtm_int 		; find empty cluster
	bne	crea_er 		; oops
	bra.s	chk_ful

enough	movem.l (a7)+,d4/d5/a2		; get these regs back

; now we need to fill the fat with data for my clusters
; calculate the cluster number
; d5 = sector nbr where free cluster was found, absolute
;
	move.l	d5,d6			; keep this
	sub.l	fatstrt(a5),d6		; d6 is now nth sector of fat
	lsl.l	#7,d6			; * 128, nbr of clusters per sector
	subq.l	#4,a2			; entry in the sector
	move.l	a2,d0			; keep
	sub.l	a1,d0			; relative to start
	lsr.l	#2,d0			; one cluster = 4 bytes
	add.l	d0,d6			; d6 = nbr of cluster (to be put into fat)
; now fill the fat
	moveq	#1,d2			; nbr of sectors to read/write
	move.l	d5,d0
	jsr	hdl_rsint(a3)		; read Fat sector back with 1st free slot
	bne	mchkf
	move.l	d6,d4			; d4 will be cluster nbr counter
	move.l	fclust(a5),d3		; nbr of clusters needed
	subq.l	#1,d3			; minus first cluster minus dbf
	lea	512(a1),a4		; end of each sector

flloop	addq.l	#1,d4			; next cluster for file
	move.l	d4,d0
	xlong	d0
	move.l	d0,(a2)+		; ptr to next cluster
	cmp.l	a2,a4			; end of sector?
	beq.s	nxtsect 		; yes
lpdbf	subq.l	#1,d3
	bne.s	flloop
	bra.s	filldir
nxtsect move.l	d5,d0
	jsr	hdl_wsint(a3)		; write this sector back
	bne	mchkf			; ooops
	move.l	d5,d0
	add.l	fatsz(a5),d0		; point to second FAT
	jsr	hdl_wsint(a3)		; write this sector back there, too
	bne	mchkf			; ooops
	addq.l	#1,d5
	move.l	d5,d0
	jsr	hdl_rsint(a3)		; read next sector
	bne	mchkf			; ooops
	move.l	a1,a2
	bra.s	lpdbf			; continue with dbf

; now fill in the dir entry
filldir move.l	d0,d4			; last used cluster

	move.l	#d32.eof,d0
	move.l	d0,(a2) 		; set EOF marker
	move.l	d5,d0
	jsr	hdl_wsint(a3)		; write this sector back
	move.l	d5,d0
	add.l	fatsz(a5),d0		; point to second FAT
	jsr	hdl_wsint(a3)		; write this sector back there, too
	move.l	dirsect(a5),d5
	move.l	d5,d0
	jsr	hdl_rsint(a3)		; read back dir sector
	move.l	dirslot(a5),a4		; slot in dir for this file
	lea	nmoffset(a5),a0 	; point to dos name
	move.l	(a0)+,(a4)+
	move.l	(a0)+,(a4)+
	move.l	(a0),(a4)		; fill in name + extension
	move.l	fsize(a5),d0		; size in MiB
	moveq	#20,d3
	lsl.l	d3,d0			; MiB into bytes
	xlong	d0
	move.l	d0,dos_flen-8(a4)	; filesize
	xword	d6
	move.w	d6,dos_clus-8(a4)	; lower word of cluster
	swap	d6
	xword	d6
	move.w	d6,dos_ucls-8(a4)	; upper word of cluster nbr
	moveq	#sms.rrtc,d0
	trap	#1			; get current time
	jsr	cv_rtdos		; make into dos time
	xlong	d1
	move.l	d1,dos_updt-8(a4)	; update time/date
	move.l	d1,dos_crtt-8(a4)	; creation time/date
	move.l	d5,d0
	jsr	hdl_wsint(a3)		; write sector back
; adjust the nbr of free clusters in file system info sector
	clr.l	d0			; I want to read sector 0
	jsr	hdl_rsint(a3)		; read this sector
	bne	rel_a1			 ; ooops
; I should now have the MBR at (a1)
; get nbr of partition 1 sector - is in Intel format, so switch around
	move.l	dos_prt1(a1),d0 	; partition start
	xlong	d0			; as block (=512 bytes sector) number
	addq.l	#1,d0			; I want the sector after that
	move.l	d0,d5
	jsr	hdl_rsint(a3)		; read partition sector into (a1)
	bne	rel_a1
	cmp.l	#$52526141,(a1) 	; FS sgnature
	bne	mchkf			; not there???
	move.l	$1e8(a1),d0		; nbr of free clusters
	xlong	d0
	sub.l	fclust(a5),d0
	xlong	d0
	move.l	d0,$1e8(a1)
	move.l	d4,$1ec(a1)
	move.l	d5,d0
	jsr	hdl_wsint(a3)
	bra	rel_a1			; release mem & leave


; read each sector of the fat in turn until I find the first one with an empty
; cluster (empty cluster = longword 0)
; d0   r    s
; d1 c	s   nbr of sects to read, adjusted for dbf / nbr of sects remaining to be read
; d2  r s   1
; d4 cr     nbr of clusters in sect to search / nbr remaining to be serached
; d5 c	 r  sector nbr of start in fat / sector nbr where empty slot was found
; a1 c	p   buffer
; a2 cr     where to start search in sector / pointer to empty slot +4
; a3 c	p   device linkage block
;
; on return status reg Z if ok, NZ if not

gt_mpty moveq	#1,d2			; always read one sector
gtm_lp1 move.l	d5,d0
	jsr	hdl_rsint(a3)		; read sector of fat
	bne.s	srchret 		; couldn't
	move.l	a1,a2
gtm_lp2 tst.l	(a2)+			; empty cluster?
	beq.s	srchret 		; yes, done
gtm_int dbf	d4,gtm_lp2
	addq.l	#1,d5			; try next sector of fat
	moveq	#127,d4 		; there are 128 longwords in a sector
	dbf	d1,gtm_lp1
	moveq	#err.drfl,d0		; no free clusters at all!
srchret rts


; read each sector of the fat in turn until I find the first not empty one
; for as long as I need
; d0	 r  =0 if enough free sectors found
; d1 c	s   nbr of sects in fat to read, adjusted for dbf / nbr of sects remaining to be read
; d2	s
; d3 c	s   number of clusters needed before not empty cluster found / nbr of clust remaining
; d4 c	 r  nbr of clusters in sect to search / where in sector first empty cluster is found
; d5 c	 r  sector nbr of start in fat / sector nbr where empty slot was found
; a1 c	 p  buffer
; a2	 r  pointer to full slot +4
; a3 c	 p  device linkage block
gt_full moveq	#1,d2			; always read one sector
	bra.s	lpctr
gtf_lp1 move.l	d5,d0
	jsr	hdl_rsint(a3)		; read sector of fat
	bne	mchkf			; couldn't
	move.l	a1,a2
gtf_lp2 tst.l	(a2)+			; empty cluster?
	bne.s	srchret 		; no, end search here
	subq.l	#1,d3			; are we done?
	beq.s	srchret 		; yes!
lpctr	dbf	d4,gtf_lp2
	addq.l	#1,d5			; try next sector of fat
	moveq	#127,d4 		; there are 128 longwords in a sector
	dbf	d1,gtf_lp1
	bra	errdrfl 		; no free clusters at all!


	      
;************************** Extension thing routine
;
; ret = CARD_SPEEDUP
;
; returns 1 if hardware is 40Mhz card transfer speed capable, 0 if not
;
;***************************
crd_spd thg_extn {SPED},,jth_rwrd
	move.b	q68_v2,d1
	andi.l	#1,d1
	move.l	d1,4(a1)
	rts
	end
