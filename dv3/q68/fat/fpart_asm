; DV3 Q68 FAT16 Find Partition	V1.00	 2017 W. Lenerz
;
	section dv3

	xdef	hfp_fat

	include 'dev8_keys_err'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'
	include 'dev8_keys_dos'
	include 'dev8_mac_xword'


;+++
; DV3 FAT16 find partition.
;
; ******* !!!!
;
; It is presumed here that the FAT16 partition one is looking for
; - either is the first and only partition on a FAT16 formatted medium
; - or is one of the primary (!!!!) partitions on a FAT32 formatted medium.
; If not, the partition won't be found.
;
; For the Q68, I need to card on which the parition can be found, and the
; partition nbr. They can be found at hdl_unit(a3) and hdl_part(a3) respectively.
; The hdl_part is used here, the hdl_unit in the read/write routines.
; ******* !!!!
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

hfp.reg reg	a1

hfp_fat
	movem.l hfp.reg,-(a7)		;
	lea	hdl_part-1(a3),a1	; table stating which drive on what arition
	moveq	#0,d4
	move.b	(a1,d7.w),d4		; partition required
prt_chk ble.s	hfp_mchk		; neither 0 nor negatives!
	cmp.b	#4,d4
	bgt.s	hfp_mchk		; no more than 4 partitions, please
	clr.l	d3			; start by reading root sector
hfp_chk move.l	d3,d0			; root sect or start sect of partition
	lea	hdl_buff(a3),a1 	; sector buffer
	moveq	#1,d2
	jsr	hdl_rsint(a3)		; read this sector
	bne.s	hfp_npart		; oops, say no partition
	cmp.l	#'FAT1',dos_f16s(a1)	; (direct) fat 16 partition?
	bne.s	hfp_f32 		; no, could be fat32
	cmp.b	#'6',4+dos_f16s(a1)	; still fat16 ?
	bne.s	hfp_mchk		; no
hfp_got moveq	#0,d2
	move.w	dos_sect+1(a1),d2
	move.b	dos_sect(a1),d2 	; total sectors (small numbers)
	tst.w	d2			; will be 0 if big number
mck_badl
	blt.s	hfp_mchk		; no negative values!
	bgt.s	hfp_ok1 		; small numbers ok
	move.l	dos_bnos(a1),d2 	; get big sectors number
	xlong	d2
	move.l	d3,d1			; got a FAT16 partition
	move.l	#dos.fflg,d4		; for FAT disk
hfp_ok1 moveq	#0,d0
	move.b	d0,hdl_npart(a3)	; partition found
hfp_exit
	movem.l (sp)+,hfp.reg
	rts
hfp_mchk
	moveq	#err.mchk,d0
hfp_npart
	st	hdl_npart(a3)		 ; set no partition found
	bra.s	hfp_exit

; Check whether this is a FAT32 MBR, if yes, try to get the FAT16 partition.
; If d3<>0 then we alread checked the MBR, it was a FaT32 MBR and we tried to
; get the FAT16 partition from there - but this failed since we're back here.
; This means that we can't find the FAT16 partition.
hfp_f32 tst.l	d3			; will be 0 if root sector read
	bne.s	hfp_mchk		; we read a part. and it wasn't fat16
;	 cmp.l	 #'FAT3',dos_f32s(a1)	 ; fat32?
;	 bne.s	 hfp_mchk		 ; no
;	 cmp.b	 #'2',4+dos_f32s(a1)	 ; still fat32 ?
;	 bne.s	 hfp_mchk		 ; no
	cmp.w	#$55aa,$1fe(a1) 	; could this be a partition table?
	bne.s	hfp_mchk		: definitely not
	lea	dos_ptbl+dos_pstt(a1),a1; point to parition table start
	moveq	#dos.plen,d0		; length of one partition table entry
hfp_lp	subq.b	#1,d4			; this partition?
	beq.s	hfp_fnd 		; yes
	blt.s	hfp_mchk		; too far (should have been caught at trp_chk)
	add.l	d0,a1			; point next entry in table
	bra.s	hfp_lp			; try again
hfp_fnd move.l	(a1)+,d3		; start of partition sector nbr
	xlong	d3
	bra	hfp_chk 		; check for FAT16 partition

	end
