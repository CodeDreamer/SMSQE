; Make Directory    V2.02     1989  Tony Tebby   QJUMP

	section iou

	xdef	iou_mkdr

	xref	gu_achpp
	xref	gu_rchp
	xref	iou_fmlw
	xref	iou_smlw
	xref	iou_sde0
	xref	iou_cknm
	xref	iou_ckfn
	xref	iou_clsl

	include 'dev8_keys_sys'
	include 'dev8_keys_chn'
	include 'dev8_keys_iod'
	include 'dev8_keys_hdr'
	include 'dev8_keys_err'
	include 'dev8_keys_qdos_ioa'

	include 'dev8_mac_assert'
;+++
; Make Directory
;
;	d1-d7 / a0-a4 / a6 all preserved
;	d6 c  p drive / file ID
;	a0 c  p channel base address
;	a3 c  p linkage block address
;
;	status return standard
;---
iou_mkdr
reg.mkdr reg	d1-d7/a1
reg.chan reg	a0/a4
frame	 equ	4
stk_wrk  equ	$00
stk_chan equ	frame
stk_defb equ	frame+$04
	movem.l reg.mkdr,-(sp)
	movem.l reg.chan,-(sp)
	move.l	#chn_spr+hdr.len*2,d0	 ; allocate some working space
	add.w	d0,d0
	jsr	gu_achpp
	bne.l	imd_ex0
	move.l	a0,-(sp)		 ; keep address in frame
	lea	chn_spr(a0),a1	 ; put header in spare
	move.l	stk_chan(sp),a0 	 ; restore channel
	st	hdr_type(a1)		 ; set header type
	lea	hdr_name(a1),a1
	lea	chn_name(a0),a0
	move.w	(a0)+,d0
	cmp.b	#'_',-1(a0,d0.w)	 ; ends in underscore?
	bne.s	imd_snam
	subq.w	#1,d0			 ; ... yes, strip it
	beq.l	imd_inam
imd_snam
	move.w	d0,(a1)+
imd_name
	move.b	(a0)+,(a1)+		 ; copy name
	subq.w	#1,d0
	bgt.s	imd_name

	move.l	(sp),a1
	lea	chn_spr(a1),a1
	move.l	stk_chan(sp),a0
	move.w	chn_flid(a0),hdr_flid(a1) ; set ID
	moveq	#hdr.len,d7
	jsr	iou_sde0		 ; set directory entry

	or.b	#1<<chn..dst+1<<chn..vst,chn_usef(a0) ; set usage flags
	st	chn_updt(a0)		 ; updated

	move.b	#ioa.kdir,chn_accs(a0)

	move.l	stk_wrk(sp),a1		 ; get working block
	move.b	chn_drid(a0),chn_drid(a1) ; drive id
	move.l	chn_sdef(a0),chn_feof(a1) ; directory eof
	move.w	chn_drnr(a0),chn_drnr(a1) ; drive number
	move.w	chn_sdid(a0),chn_flid(a1) ; file ID
	move.l	iod_hdrl(a4),chn_fpos(a1) ; and position

imd_look
	move.l	stk_wrk(sp),a0
	move.l	chn_fpos(a0),d5 	 ; position
	cmp.l	chn_feof(a0),d5
	bhs.l	imd_ok
	move.w	chn_flid(a0),d6 	 ; file ID
	moveq	#hdr.len,d7		 ; fetch this much
	lea	chn_spr(a0),a1		 ; into buffer
	jsr	iou_fmlw
	bne.l	imd_exit
	move.l	d5,chn_fpos(a0) 	 ; next entry
	lea	hdr_name+chn_spr(a0),a4  ; compare the names
	move.l	stk_chan(sp),a0
	jsr	iou_ckfn
	movem.l stk_chan(sp),reg.chan
	bne.s	imd_look		 ; no match, look again

	moveq	#hdr.len,d7		 ; copy header
	sub.w	d7,a1			 ; from here
	move.w	hdr_flid(a1),d1 	 ;++ old file id
	tst.b	iod_ftyp(a4)		 ;** format type
	bne.s	imd_new 		 ;++ new file system V2
	cmp.w	#iod.rdid,chn_sdid(a0)	 ;** in root-directory?
	bne.s	imd_new 		 ;** no
	move.l	d5,d1			 ;++ old file system V1, position +1
	lsr.l	#6,d1			 ;++ ..is file id
imd_new
	move.l	chn_feof(a0),d5 	 ; put at end of file
	move.w	chn_flid(a0),d6 	 ; file ID
	jsr	iod_alfs(a3)		 ; re-allocate
	move.w	d1,hdr_flid(a1) 	 ; new file id
	jsr	iou_smlw		 ; set multible bytes and wait
	bne.s	imd_exit
	move.l	d5,chn_feof(a0) 	 ; new end of file

	moveq	#hdr.len,d7
	sub.l	d7,d5			 ; point to start of entry
	move.b	chn_drid(a0),d3 	 ; drive ID
	lea	sys_fsch(a6),a2 	 ; list of channel blocks
imd_chlp
	move.l	(a2),d0
	beq.s	imd_clhd		 ; ... done, clear header
	move.l	d0,a2
	lea	-chn_link(a2),a0	 ; file system channel block
	cmp.b	chn_drid(a0),d3 	 ; our drive?
	bne.s	imd_chlp		 ; ... no, try again
	lea	hdr_name-hdr.len(a1),a4  ; compare names
	jsr	iou_cknm
	bne.s	imd_chlp		 ; no match
	move.l	d5,chn_sdps-chn_link(a2) ; set sub-directory position
	move.w	d6,chn_sdid-chn_link(a2) ; set sub-directory ID
	move.w	hdr_flid-hdr.len(a1),chn_flid-chn_link(a2) ; and file id
	bra.s	imd_chlp

imd_clhd
	move.l	stk_wrk(sp),a0
	move.l	stk_defb(sp),a4
	move.l	chn_fpos(a0),d5 	 ; old directory position
	sub.l	d7,d5
	move.w	chn_flid(a0),d6
	jsr	iou_smlw		 ; send empty bit at end

	bra.l	imd_look		 ; and look for next one

imd_inam
	moveq	#err.inam,d0
	bra.s	imd_exit
imd_ok
	moveq	#0,d0
imd_exit
	move.l	(sp)+,a0		 ; throw away the work space
	jsr	gu_rchp
imd_ex0
	movem.l (sp)+,reg.chan
	movem.l (sp)+,reg.mkdr
	rts
	end
