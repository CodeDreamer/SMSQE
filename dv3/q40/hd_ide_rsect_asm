; Q40 DV3 IDE Read Sector		  V3.00     1998     Tony Tebby
; Provide for LBA FAT32 access & byteswap V3.01     W. Lenerz 2017 Nov 21
;
; If the drive is addressed with LLBA and not CHS, then it is presumed
; that the drive needs a byteswap ; each word read must have the two bytes
; composing it swapped. This is due to the assumption that such a drive
; is actual a qxl.win ype file on a memory card (or any other FAT32 device)
; and the MC68K and Intel processors get the byte order differently.
; The lba flag is not only used elsewhere to make sure that the drive is
; actually addressed via LBA, but also to inverse byte order.
;
; The assumption lba = qxlwin file on a fat32 drive is not ** necessarily **
; true but should be so within the Qx0 universe.

	section dv3

	xdef	id_rdirect		; read sector for direct sector IO
	xdef	id_rsint		; read sector internal
	xdef	id_rsecid		; read ID sector
	xdef	id_rsecs		; read sector (special, key in d1)

	xref	id_diradd		; direct sector IO addressing

	xref	id_cmdw

	xref.s	ideo.data
	xref.s	ideo.stat

	include 'dev8_keys_err'
	include 'dev8_keys_q40'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'
	include 'dev8_dv3_hd_ide_keys'
	include 'dev8_mac_assert'

drv_led   equ	hdl_freq+1
;+++
; This routine reads sectors from a hard disk for direct sector I/O
;
;	d0 cr	sector number / error code
;	d2 c  p number of sectors
;	d7 c  p drive ID / number
;	a1 c  p address to read into
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0, ERR.NC or ERR.MCHK
;
;---
id_rdirect
	assert	ddf.drct-1
	tst.b	ddf_ftype(a4)		 ; real direct access?
	bge.s	id_rsint		 ; ... no
	jsr	id_diradd		 ; scrumple address for direct sector
	bne.s	idr_rts

;+++
; This routine reads sectors from an IDE disk at an even address
;
;	d0 cr	sector number / error code
;	d2 c  p number of sectors
;	d7 c  p drive ID / number
;	a1 c  p address to read into
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0, ERR.NC or ERR.MCHK
;
;---
id_rsint
idr.reg reg	d1
	move.l	d1,-(sp)
	assert	hdl.hold,-1
	subq.b	#hdl.acss,hdl_acss(a3)	; hold poll
	move.l	d0,d1
	tst.b	ddf_lba(a4)		; check for "real sector?"
	beq.s	idr_rsect		; no
	move.l	d0,d1			; ... yes, set CC again
	bpl.s	idr_rsect		; real sector
	bsr.s	id_rsecid		; read ID sector
	bra.s	idr_exrp

idr_rsect
	bsr.s	idr_do			; read
	ble.s	idr_done
	bsr.s	idr_retry		; try again
	ble.s	idr_done
	bsr.s	idr_retry		; try again

idr_done
	beq.s	idr_exrp
idr_mchk
	moveq	#err.mchk,d0
idr_exrp
	addq.b	#hdl.acss,hdl_acss(a3)	; restore poll
	move.l	(sp)+,d1
	tst.l	d0
idr_rts
	rts


;******************

;+++
; This routine reads the ID sector from an IDE disk
;
;	d0  r	error code
;	d1  r	sector address = 0
;	d2  r	number of sectors = 1
;	d7 c  p drive ID / number
;	a1 c  p address to read into
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0, ERR.NC or ERR.MCHK
;
;---
id_rsecid
	moveq	#ide.idnt,d0
	moveq	#0,d1			 ; address 0
	moveq	#1,d2			 ; one sector
	bra.s	id_rsecs

idr_retry
idr_do
	moveq	#ide.read,d0

;+++
; This routine reads sectors from an IDE disk (special key)
;
;	d0 cr	command / error code
;	d1 c  p sector address
;	d2 c  p number of sectors
;	d7 c  p drive ID / number
;	a1 c  p address to read into
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0, ERR.NC or ERR.MCHK
;
;---
id_rsecs
idrs.reg reg	a1/d1/d2/d3/d4/a5
	movem.l idrs.reg,-(sp)
	tst.b	ddf_lba(a4)
	bne.s	idr_cmd
	add.l	ddf_dtop-4(a4),d1	; when in LBA mode, always add partition offset
idr_cmd jsr	id_cmdw 		; read sector(s)
	bne.s	idrs_exit

idrs_sector
	move.l	hdl_1sec(a3),d3
	lsl.l	#2,d3			; allow for run-up
idrs_wait
	btst	#ide..drq,ideo.stat(a5) ; wait for data request
	bne.s	idrs_copy
	subq.l	#1,d3
	bgt.s	idrs_wait
	moveq	#err.mchk,d0
	bra.s	idrs_check

idrs_copy
	move.w	#255,d3

	tst.b	ddf_lba(a4)		; is it LBA ?
	bne.s	idrs_cloop		; no, no need to switch bytes
idrs_lba				; LBA addressed drive, presume byte switsh
	move.w	ideo.data(a5),d4
	rol.w	#8,d4			; switch bytes
	move.w	d4,(a1)+		; copy data
	dbra	d3,idrs_lba
idrs_cpok
	subq.b	#1,d2
	bne.s	idrs_sector

idrs_check
	btst	#ide..err,ideo.stat(a5) ; check for error
	beq.s	idrs_exit		; ... none

	moveq	#1,d0

idrs_exit
	movem.l (sp)+,idrs.reg
	tst.l	d0
	rts
idrs_cloop
	move.w	ideo.data(a5),(a1)+	; copy data
	dbra	d3,idrs_cloop
	bra.s	idrs_cpok
	end
