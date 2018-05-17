; IO Operations   V2.05     1989  Tony Tebby	QJUMP

	section iou

	xdef	iou_io
	xdef	iou_flsh
	xdef	iou_load
	xdef	iou_save
	xdef	iou_ckro

	xref	iou_sect

	xref	iou_opfl
	xref	iou_fmul
	xref	iou_flin
	xref	iou_smul
	xref	iou_fden
	xref	iou_fde0
	xref	iou_sden
	xref	iou_fnam
	xref	iou_ckch
	xref	iou_mkdr
	xref	iou_ckde
	xref	iou_date

	xref	gu_achpp
	xref	gu_rchp

	include 'dev8_keys_revbin'
	include 'dev8_keys_err'
	include 'dev8_keys_sys'
	include 'dev8_keys_chn'
	include 'dev8_keys_iod'
	include 'dev8_keys_hdr'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_qdos_io'
	include 'dev8_keys_qdos_ioa'
	include 'dev8_mac_assert'
;+++
; General purpose IO routine
;
;	d0 c	operation
;	d1 cr	amount transferred / byte / position etc.
;	d2 c	buffer size
;	d5   s	file pointer
;	d6   s	drive number / file id
;	d7   s
;	a0 c	channel base address
;	a1 cr	buffer address
;	a2   s	internal buffer address
;	a3 c  p linkage block address
;	a4   s	pointer to physical definition
;	a5   s	pointer to map
;---
iou_io
	move.l	chn_ddef(a0),a4 	 ; definition block
	move.l	iod_map(a4),a5
	assert	chn..usd,7
	tas	chn_usef(a0)		 ; channel used?
	bne.s	iou_iodo		 ; ... yes
	move.l	a4,d7			 ; genuinely opened?
	bne.s	iou_iost		 ; ... yes
	move.l	d0,-(sp)
	jsr	iou_opfl		 ; ... no, open file
	move.l	(sp)+,d0

iou_iost
	move.l	iod_hdrl(a4),chn_fpos(a0); set to start of file

iou_iodo
	assert	chn_drnr,chn_flid-2
	move.l	chn_drnr(a0),d6 	 ; msw is drive / file ID

	tst.b	iod_ftyp(a4)		 ; format type?
	blt.l	iou_sect		 ; direct sector IO

	cmp.w	#iof.xinf,d0		 ; in range?
	bhi.s	io_ipar 		 ; ... no

iou_setj
	move.w	d0,d7
	cmp.w	#iob.smul,d7		 ; byte io?
	bls.s	io_jump 		 ; ... yes
	sub.w	#iof.chek,d7		 ; file io?
	blt.s	io_ipar 		 ; ... no
	addq.w	#iob.smul+1,d7

io_jump
	add.w	d7,d7
	add.w	io_tab(pc,d7.w),d7
	jmp	io_tab(pc,d7.w)

io_ipar
	moveq	#err.ipar,d0
	rts

io_tab
	dc.w	io_test-*
	dc.w	io_fbyt-*
	dc.w	io_flin-*
	dc.w	io_fmul-*
	dc.w	io_ipar-*
	dc.w	io_sbyt-*
	dc.w	io_smul-*
	dc.w	io_smul-*

	dc.w	io_chek-*
	dc.w	io_flsh-*
	dc.w	io_posa-*
	dc.w	io_posr-*
	dc.w	io_ipar-*
	dc.w	io_info-*
	dc.w	io_shdr-*
	dc.w	io_rhdr-*
	dc.w	io_load-*
	dc.w	io_save-*
	dc.w	io_rnam-*
	dc.w	io_trnc-*
	dc.w	io_date-*
	dc.w	io_mkdr-*
	dc.w	io_vers-*
	dc.w	io_xinf-*

; make directory

io_mkdr
	bsr.l	iou_ckro		 ; check read only
	jmp	iou_mkdr

; position pointer

io_posr
	move.l	iod_hdrl(a4),d2 	 ; beginning of file
	add.l	chn_fpos(a0),d1 	 ; absolute position
	bra.s	iop_ckef
io_posa
	move.l	iod_hdrl(a4),d2 	 ; beginning of file
	add.l	d2,d1			 ; absolute position
iop_ckef
	moveq	#0,d0			 ; assume ok
	cmp.l	d2,d1			 ; beginning of file?
	blt.s	iop_begf
	cmp.l	chn_feof(a0),d1 	 ; end of file?
;$$$$	ble.s	iop_spos		 ; ... no
	blt.s	iop_spos	; $$$$ prospero

	move.l	chn_feof(a0),d1 	 ; end of file
	bra.s	iop_seof

iop_begf
	move.l	d2,d1			 ; beginning of file
iop_seof
	moveq	#err.eof,d0

iop_spos
	move.l	d1,chn_fpos(a0) 	 ; set position
	sub.l	d2,d1			 ; ignoring header
	rts

; fetch medium information

io_info
	lea	iod_mnam(a4),a2 	 ; medium name
	move.l	(a2)+,(a1)+
	move.l	(a2)+,(a1)+
	move.w	(a2)+,(a1)+
	jmp	iod_occi(a3)		 ; get occupancy information

io_xinf
ioi.reg reg	d1/a0
	movem.l ioi.reg,-(sp)
	jsr	iod_occi(a3)		 ; set occupancy information
	lea	ioi.blkl(a1),a0 	 ; pre-fill information block
	moveq	#(ioi.blkl-ioi_remv)/4-1,d0
	moveq	#-1,d3
ioi_pre
	move.l	d3,-(a0)
	dbra	d0,ioi_pre

	assert	ioi.blkl-ioi_remv,$10
	clr.b	(a0)			 ; not removable

	assert	ioi_ftyp,ioi_styp-1,ioi_dens-2,ioi_mtyp-3
	move.l	#$01ffff00,-(a0)

	assert	iod_hdrl,iod_free+4,iod_totl+8,iod_allc+$a
	assert	ioi_hdrl,ioi_free+4,ioi_totl+8,ioi_allc+$a
	lea	iod_hdrl(a4),a2 	 ; set header, free, total and allc
	move.l	(a2),-(a0)
	move.l	-(a2),-(a0)
	move.l	-(a2),-(a0)
	move.w	-(a2),-(a0)

	assert	ioi_dnum,ioi_rdon-1,ioi_allc-2
	move.b	iod_wprt(a4),-(a0)	 ; set read only
	move.b	iod_dnum(a4),-(a0)	 ; set drive number

	clr.l	-(a0)			 ; null the drive name
	lea	iod_dnam(a3),a2
	move.w	(a2)+,-(a0)		 ; name length
	move.w	(a0)+,d3
ioi_cdnm
	move.b	(a2)+,(a0)+		 ; copy drive name
	subq.w	#1,d3
	bgt.s	ioi_cdnm

	lea	ioi_dnam(a1),a0 	 ; set medium name to zeros
	assert	ioi_dnam-(ioi_name+2),20 ; max number of characters
	clr.l	-(a0)
	clr.l	-(a0)
	clr.w	-(a0)			 ; clear last ten characters

	lea	iod_mnam+10(a4),a2
	moveq	#0,d1			 ; actual name length
	moveq	#10,d0			 ; max name length
ioi_cmnl
	move.b	-(a2),-(a0)
	cmp.b	#' ',(a0)		 ; space
	bne.s	ioi_smle		 ; ... no
	clr.b	(a0)
	subq.w	#1,d0
	bgt.s	ioi_cmnl
	bra.s	ioi_nmln

ioi_smnl
	move.b	-(a2),-(a0)		 ; copy characters
ioi_smle
	addq.w	#1,d1			 ; one moe char
	subq.w	#1,d0
	bgt.s	ioi_smnl

ioi_nmln
	move.w	d1,-(a0)		 ; name length
	movem.l (sp)+,ioi.reg		 ; restore regs
	rts

; set/read date

io_date
	moveq	#hdr_date,d4		 ; set / read date
	tst.b	d2			 ; update date?
	beq.s	iodt_sr 		 ; ... yes
	cmp.b	#2,d2			 ; backup date?
	bne.l	io_ipar 		 ; ... no
	moveq	#hdr_bkup,d4		 ; ... yes
iodt_sr
	moveq	#chn..dst,d3		 ; date set flag
	moveq	#4,d7			 ; set or read four bytes
	move.l	d1,chn_spr(a0)		 ; key or date
	bne.s	iodv_chk
	jsr	iou_date		 ; present date
	move.l	d1,chn_spr(a0)
	bra.s	iodv_chk

; set / read version

io_vers
	moveq	#-1,d2			 ; version flag
	moveq	#chn..vst,d3		 ; version set flag
	moveq	#hdr_vers,d4		 ; set / read version
	moveq	#2,d7			 ; two bytes
	move.w	d1,chn_spr(a0)		 ; key or version
	bne.s	iodv_chk		 ; set or read
	bset	d3,chn_usef(a0) 	 ; set usage flag
	moveq	#-1,d1			 ; and read it

iodv_chk
	cmp.b	#ioa.kdir,chn_accs(a0)	 ; is this directory?
	beq.l	io_ipar 		 ; ... no can do
	addq.l	#1,d1			 ; was it read?
	beq.s	iodv_read		 ; ... yes, carry on
	tst.b	d2			 ; was it backup date?
	bgt.s	iodv_set
	bsr.l	iou_ckrn		 ; check read only (do not set update)
iodv_set
	bset	d3,chn_usef(a0) 	 ; set usage
	move.l	a1,-(sp)
	lea	chn_spr(a0),a1
	jsr	iou_sden		 ; set date / version
	bra.s	iodv_ret
iodv_read
	move.l	a1,-(sp)
	lea	chn_spr(a0),a1
	jsr	iou_fden		 ; read date / version
iodv_ret
	tst.b	d2
	bge.s	iodv_rtd
	moveq	#0,d1
	move.w	-(a1),d1		 ; return version
	bra.s	iodv_exit
iodv_rtd
	move.l	-(a1),d1
iodv_exit
	move.l	(sp)+,a1
	rts

; do header

io_shdr
	cmp.b	#hdrt.dir,hdr_type(a1)	 ; directory?
	beq.l	io_ipar 		 ; ... yes, cannot set
	bsr.l	iou_ckro		 ; read only?
	moveq	#hdr.set,d7		 ; set header length
	moveq	#0,d4			 ; start
	jmp	iou_sden		 ; set directory entry

io_rhdr
	move.w	iod_rdid(a4),d0 	 ; root directory
	cmp.w	chn_flid(a0),d0 	 ; is it?
	beq.s	io_rdhdr		 ; read root directory header
	moveq	#hdr.len,d7
	cmp.l	d7,d2			 ; header read long enough?
	bhs.s	iorh_fden		 ; ... yes
	move.l	d2,d7			 ; ... no, read short
iorh_fden
	move.l	a1,-(sp)		 ; save address
	jsr	iou_fde0
	move.l	(sp)+,a2
	bne.s	iorh_rts		 ; ... oops
	move.l	iod_hdrl(a4),d2
	sub.l	d2,(a2) 		 ; adjust header length
iorh_rts
	rts

; read directory header

io_rdhdr
	moveq	#4,d1			 ; four bytes read
	move.l	chn_feof(a0),(a1)	 ; end of file
	move.l	iod_hdrl(a4),d0
	sub.l	d0,(a1)+		 ; adjusted to make real
iorh_loop
	cmp.w	d1,d2			 ; fill with zeros
	ble.l	io_rtok
	clr.b	(a1)+
	addq.b	#1,d1
	cmp.b	#hdr_type+1,d1		 ; ... except type
	bne.s	iorh_loop
	st	-1(a1)			 ; which is $FF
	bra.s	iorh_loop

; scatter load and save

io_load
	jmp	iod_load(a3)
io_save
	jmp	iod_save(a3)

; rename (atomic)

io_rnam
;***	    cmp.b   #ioa.kdir,chn_accs(a0)   ; we can rename directory
;***	    bne.s   iorn_ckro
;***	    tst.b   iod_rdon(a4)	     ; ... but only if not read_only
;***	    bne.s   iorn_ckro
;***	    jsr     iou_ckde		     ; check if directory empty
;***	    bne.s   iorn_rts		     ; ... no
;***	    bra.s   iorn_do		     ; ... yes, carry on

iorn_ckro
	bsr.l	iou_ckrn		 ; check read only
iorn_do
	movem.l a0/a4,-(sp)		 ; save name comparison regs
	move.w	(a1)+,d4		 ; name length
	lea	iod_dnus(a3),a4 	 ; drive name
	move.w	(a4)+,d0		 ; ... length
	sub.w	d0,d4			 ; name is shorter
	move.l	a1,a0
	cmp.w	d0,d0			 ; pretend lengths the same
	bsr.l	iou_ckch		 ; check the characters
	move.l	a0,a1
	movem.l (sp)+,a0/a4		 ; and restore name comparison regs
	bne.s	iorn_inam		 ; ... oops, wrong device
	moveq	#-'0',d0
	add.b	(a1)+,d0		 ; drive number
	swap	d6
	cmp.b	d6,d0			 ; correct?
	bne.s	iorn_inam		 ; ... no
	swap	d6
	cmp.b	#'_',(a1)+		 ; correct separator?
	bne.s	iorn_inam		 ; ... no
	subq.w	#2,d4			 ; length of remaining bit
	move.w	d4,d2
	cmp.w	#chn.nmln,d2		 ; too long?
	bhi.s	iorn_inam		 ; ... yes

	move.l	a0,a2
	move.l	#chn_fend,d0
	jsr	gu_achpp		 ; make a dummy channel block
	bne.s	iorn_rts

	bsr.s	iorn_cname		 ; set up name
	move.b	chn_drid(a2),chn_drid(a0); drive to search
	move.b	#ioa.krnm,chn_accs(a0)	 ; rename file
	jsr	iou_opfl		 ; open it
	jsr	gu_rchp 		 ; return heap
	move.l	a2,a0			 ; restore channel
iorn_rts
	rts
iorn_inam
	moveq	#err.inam,d0		 ; invalid name
	rts

iorn_cname
	move.l	a2,-(sp)
	lea	chn_name(a0),a2 	 ; copy new name into channel block
	move.w	d2,(a2)+		 ; ... length
	move.w	d2,d0
	bra.s	iorn_lend
iorn_loop
	move.b	(a1)+,(a2)+
iorn_lend
	dbra	d0,iorn_loop
	move.l	(sp)+,a2
	rts

; truncate

io_trnc
	bsr.l	iou_ckro		 ; check read only
	move.l	chn_fpos(a0),d5 	 ; truncation position
	jsr	iod_trnc(a3)		 ; truncate file
	move.l	chn_fpos(a0),chn_feof(a0) ; new end of file
	rts

; check pending operations

io_chek
	jmp	iod_chek(a3)		 ; check all pending operations
;+++
; Flush buffers and make safe
;     Sets length / update date in directory entry
;     Does device specific flush
;+++
iou_flsh
io_flsh
	tst.b	chn_updt(a0)		 ; file updated?
	beq.s	io_rtok 		 ; ... no
	tst.w	d3			 ; re-try?
	bne.s	iouf_do 		 ; ... yes
	moveq	#hdr_flen,d4		 ; update file length
	moveq	#4,d7			 ; (4 bytes)
	lea	chn_spr(a0),a1
	move.l	chn_feof(a0),(a1)
	bsr.l	iou_sden

	btst	#chn..dst,chn_usef(a0)	 ; date already set?
	bne.s	iouf_ver
	jsr	iou_date
	lea	chn_spr(a0),a1		 ; set time in spare
	move.l	d1,(a1)

	moveq	#hdr_date,d4		 ; set date
	moveq	#4,d7
	bsr.l	iou_sden

iouf_ver
	bset	#chn..vst,chn_usef(a0)	 ; version updated?
	bne.s	iouf_do0
	moveq	#hdr_vers,d4
	moveq	#2,d7			 ; get version
	lea	chn_spr(a0),a1
	bsr.l	iou_fden
	addq.w	#1,-(a1)		 ; update
	moveq	#hdr_vers,d4
	moveq	#2,d7
	bsr.l	iou_sden		 ; and set
iouf_do0
	moveq	#0,d3			 ; restore D3 to 0
iouf_do
	jsr	iod_flsh(a3)		 ; and do flush
	sne	chn_updt(a0)		 ; flush complete?
	rts

; OK return

io_rtok
	moveq	#0,d0
	rts

	page

; Test or fetch byte

;	d1	next byte
;	d5	position (byte)
;	d6	drive number / file id

io_test
io_fbyt
	move.l	chn_fpos(a0),d5 	 ; position
	cmp.l	chn_feof(a0),d5 	 ; length to end of file
	beq.s	iofb_eof		 ; ... end of file
	move.w	d0,d7			 ; save operation

; set internal buffer pointer in a2

	jsr	iod_lcbf(a3)		 ; locate buffer
	bne.s	iofb_exit		 ; ... oops

	move.b	(a2),d1 		 ; fetch byte

	addq.l	#1,d5			 ; move on to next byte
	subq.w	#iob.fbyt,d7		 ; was it iob.fbyt?
	beq.s	iofb_exit		 ; ... yes
	rts				 ; ... no, do not update pointer

; fetch multiple bytes

;	d1	amount read so far
;	d2	buffer size
;	d3	amount to buffer/unbuffer
;	d5	position (byte)
;	d6	drive number / file id
;	d7	max to read

io_flin
io_fmul
	ext.l	d2			 ; call buffer length is a word
;+++
; Load file using standard serial fetch
;
;	d1 cr	amount read so far
;	d2 c  p amount to load
;	d3   s	amount to buffer/unbuffer
;	d4   s	file block/byte
;	d5   s	start file position (byte) from channel block
;	d6 c  p drive number / file id
;	d7   s	max to read this time
;	a0 c  p channel block
;	a1 cr	pointer to memory to load into
;	a2   s	internal buffer address
;	a3 c  p pointer to linkage block
;	a4 c  p pointer to physical definition
;	a5 c  p pointer to map
;---
iou_load
io_sload
	move.l	d2,d7			 ; max to read
	sub.l	d1,d7			 ; max left to read
	move.l	chn_feof(a0),d3 	 ; end of file
	move.l	chn_fpos(a0),d5
	sub.l	d5,d3			 ; length to end of file
	beq.s	iofb_eof		 ; ... none
	cmp.l	d7,d3			 ; enough?
	bhs.s	iofm_dor		 ; ... enough
	move.l	d3,d7			 ; ... end of file
iofm_dor
	subq.w	#iob.flin,d0		 ; was it fetch line?
	beq.s	iofl_dor		 ; ... yes
	bsr.l	iou_fmul		 ; do fetch
	bne.s	iofb_exit		 ; ... oops
	cmp.l	d1,d2			 ; all read?
	beq.s	iofm_exit		 ; ... yes
iofb_eof
	moveq	#err.eof,d0		 ; ... no, must have been end of file

iofb_exit
iofm_exit
iofl_exit
	move.l	d5,chn_fpos(a0) 	 ; set current pointer
	rts

; do fetch line

iofl_dor
	bsr.l	iou_flin		 ; fetch line
	bra.s	iofl_exit
	page

; send a byte

;	d1	byte to send
;	d5	position (byte)
;	d6	drive number / file id

io_sbyt
	bsr.s	iou_ckro		 ; check read only
	move.l	chn_fpos(a0),d5 	 ; file position
	jsr	iod_albf(a3)		 ; locate / allocate buffer
	bne.s	iosb_exit		 ; ... oops
	move.b	d1,(a2)+		 ; copy byte
	addq.l	#1,d4			 ; next buffer position
	addq.l	#1,d5			 ; next byte
	jsr	iod_upbf(a3)		 ; buffer updated
	bra.s	iosb_exit
	page

; send multiple bytes

;	d1	amount sent so far
;	d2	buffer size
;	d3	amount to buffer/unbuffer
;	d5	position (byte)
;	d6	drive number / file id
;	d7	amount left to send

io_smul
	ext.l	d2			 ; call argument is word
;+++
; Save file using standard serial send
;
;	d1 cr	amount sent so far
;	d2 c  p amount to save
;	d3   s	amount to buffer/unbuffer
;	d4   s	file block/byte
;	d5   s	start file position (byte) from channel block
;	d6 c  p drive number / file id
;	d7   s	max to send this time
;	a0 c  p channel block
;	a1 cr	pointer to memory to save from
;	a2   s	internal buffer address
;	a3 c  p pointer to linkage block
;	a4 c  p pointer to physical definition
;	a5 c  p pointer to map
;---
iou_save
io_ssave
	bsr.s	iou_ckro		 ; check read only

	move.l	d2,d7			 ; amount to send
	sub.l	d1,d7			 ; amount left to send
	ble	io_rtok 		 ; ... none
	move.l	chn_fpos(a0),d5 	 ; file position
	bsr.l	iou_smul		 ; do send

iosb_exit
	move.l	d5,chn_fpos(a0) 	 ; set file position
	cmp.l	chn_feof(a0),d5 	 ; new end of file?
	bls.s	iosm_rts		 ; ... no
	move.l	d5,chn_feof(a0) 	 ; set end of file
iosm_rts
	tst.l	d0
	rts
;+++
; check read only status (and set chn_updt if OK)
;
;	smashes d0/a2
;	returns err.rdo at 4(sp) if d0 set to ERR.RDO
;
;---
iou_ckro
	st	chn_updt(a0)		 ; say modified
iou_ckrn
	move.b	chn_accs(a0),d0 	 ; check file access
	lea	io_share(pc),a2 	 ; GST asm bug
	btst	d0,(a2) 		 ; shareable?
	bne.s	io_rdo4 		 ; ... yes, read only
	rts
io_rdo4
	sf	chn_updt(a0)		 ; not really modified
	addq.l	#4,sp			 ; remove return
	moveq	#err.rdo,d0
	rts


io_share dc.b	.x..xxxx
	end
