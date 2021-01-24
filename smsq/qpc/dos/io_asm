; DOS IO routines     V1.04     2000	Marcel Kilgus
;
; 2016-04-08  1.01  Added rename support (MK)
; 2018-11-28  1.02  Some software expect upper word of D1=0 in FMUL/FLIN (MK)
; 2019-03-28  1.03  Fixed iob.suml (MK)
; 2019-04-07  1.04  Adapted for new QPC2v5 DOS driver

	section dos

	xdef	dos_io

	xref	iou_achb
	xref	iou_rchb
	xref	iou_ckch

	include 'dev8_keys_err'
	include 'dev8_keys_sys'
	include 'dev8_keys_chn'
	include 'dev8_keys_iod'
	include 'dev8_keys_hdr'
	include 'dev8_smsq_qpc_dos_data'
	include 'dev8_smsq_qpc_keys'

dos_io
	cmpi.b	#$40,d0
	bcs	dos_bio
	cmpi.b	#$4f,d0
	bhi.s	dos_ipar
	add.w	d0,d0
	move.w	atable-$80(pc,d0.w),d0
	jmp	atable(pc,d0.w)

atable	dc.w	dos_chek-atable
	dc.w	dos_flsh-atable
	dc.w	dos_posa-atable
	dc.w	dos_posr-atable
	dc.w	dos_ipar-atable
	dc.w	dos_minf-atable
	dc.w	dos_shdr-atable
	dc.w	dos_rhdr-atable
	dc.w	dos_load-atable
	dc.w	dos_save-atable
	dc.w	dos_rnam-atable
	dc.w	dos_trnc-atable
	dc.w	dos_date-atable
	dc.w	dos_mkdr-atable
	dc.w	dos_vers-atable
	dc.w	dos_xinf-atable

dos_ipar
	moveq	#err.ipar,d0
	rts

;+++
; Check and flush
;---
dos_chek
dos_flsh
	moveq	#0,d0
	rts

;+++
; Alter file position
;---
dos_posa
	moveq	#0,d0
	bra.s	dos_pos
dos_posr
	moveq	#0,d0
	move.l	chn_fpos(a0),d2
dos_convert
	subi.l	#hdr.len,d2	; minus header
	add.l	d2,d1		; plus relative position
	bvs.s	dos_eof
dos_pos
	move.l	d1,d2		; position
	bmi.s	dos_start
	addi.l	#hdr.len,d2	; plus header
	bvs.s	dos_eof
	cmp.l	chn_feof(a0),d2 ; File EOF
	ble.s	dos_setpos
dos_eof
	moveq	#0,d1		; Later used as rel pos
	move.l	chn_feof(a0),d2 ; Take end of file
	moveq	#err.eof,d0
	bra.s	dos_convert
dos_start
	moveq	#hdr.len,d2	; Internal file position
	moveq	#0,d1		; "Official" file position
dos_setpos
	move.l	d2,chn_fpos(a0)
	tst.l	d0
	rts

;+++
; Media information
;---
dos_minf
	move.w	chn_drnr(a0),d1
	dc.w	qpc.dminf
	rts

;+++
; Save header
;---
dos_shdr
	movem.l a2,-(sp)
	move.l	chn_hand(a0),d1 ; file handle
	move.w	chn_drnr(a0),d2 ; drive number
	lea	chn_name(a0),a2 ; file name
	dc.w	qpc.dshdr+2	; set file header
	movem.l (sp)+,a2
	bmi.s	dos_rts
	add.w	d1,a1		; update header pointer
dos_rts
	rts

;+++
; Read header
;---
dos_rhdr
	movem.l d2/a2,-(sp)
	move.w	d2,d0		; buffer length
	move.l	chn_hand(a0),d1 ; file handle
	move.w	chn_drnr(a0),d2 ; drive number
	lea	chn_name(a0),a2 ; file name
	dc.w	qpc.drhdr+2	; get file header
	movem.l (sp)+,d2/a2
	bmi.s	dos_rts
	add.w	d1,a1		; update header pointer
	rts

;+++
; Load file
;---
dos_load
	move.l	chn_hand(a0),d1
	move.l	chn_fpos(a0),d0
	subi.l	#hdr.len,d0
	dc.w	qpc.dmove
	move.l	d2,d0
	dc.w	qpc.dread+1
	bmi.s	dos_lend
	add.l	d0,chn_fpos(a0)
	add.l	d0,a1
	moveq	#0,d0
dos_lend
	rts

;+++
; Save file
;---
dos_save
	move.l	chn_hand(a0),d1
	move.l	chn_fpos(a0),d0
	subi.l	#hdr.len,d0
	dc.w	qpc.dmove
	move.l	d2,d0
	dc.w	qpc.dwrte+1
	bmi.s	dos_send
	add.l	d0,chn_fpos(a0)
	add.l	d0,a1
	dc.w	qpc.dsize
	add.l	#hdr.len,d0
	move.l	d0,chn_feof(a0)
	moveq	#0,d0
dos_send
	rts

;+++
; Rename file
;---
dos_rnam
	movem.l a0/a4,-(sp)		; save name comparison regs
	move.w	(a1)+,d4		; name length
	lea	iod_dnus(a3),a4 	; drive name
	move.w	(a4)+,d0		; ... length
	sub.w	d0,d4			; name is shorter
	move.l	a1,a0
	bsr.l	iou_ckch		; check the characters
	move.l	a0,a1
	movem.l (sp)+,a0/a4		; and restore name comparison regs
	bne.s	dos_inam		; ... oops, wrong device
	moveq	#-'0',d0
	add.b	(a1)+,d0		; drive number
	move.w	chn_drnr(a0),d6 	; drive number
	cmp.b	d6,d0			; correct?
	bne.s	dos_inam		; ... no
	cmp.b	#'_',(a1)+		; correct separator?
	bne.s	dos_inam		; ... no
	subq.w	#2,d4			; length of remaining bit
	move.w	d4,d2
	cmp.w	#chn.nmln,d2		; too long?
	bhi.s	dos_inam		; ... yes

	move.l	chn_hand(a0),d1
	move.w	-(a1),-(sp)		; save 2 bytes before filename
	move.w	d4,(a1) 		; overwrite with length information
	move.w	chn_drnr(a0),d2 	; drive number
	dc.w	qpc.drnam+1		; try renaming file
	move.w	(sp)+,(a1)+		; restore 2 bytes before filename
	tst.l	d0
	bne.s	dos_rend

	move.l	a2,-(sp)
	lea	chn_name(a0),a2 	; copy new name into channel block
	move.w	d4,(a2)+		; ... length
	bra.s	dos_rnlend
dos_rnloop
	move.b	(a1)+,(a2)+
dos_rnlend
	dbra	d4,dos_rnloop
	move.l	(sp)+,a2
	moveq	#0,d0
dos_rend
	rts
dos_inam
	moveq	#err.inam,d0
	rts

;+++
; Truncate file
;---
dos_trnc
	move.l	chn_hand(a0),d1
	move.l	chn_fpos(a0),d0
	subi.l	#hdr.len,d0
	dc.w	qpc.dmove
	dc.w	qpc.dtrnc	; truncate file
	move.l	chn_fpos(a0),chn_feof(a0)
	tst.l	d0
	rts

;+++
; Make directory
;---
dos_mkdr
	movem.l d1-d2/a1,-(a7)
	cmp.b	#4,chn_accs(a0)
	beq.s	dos_ichn

	move.l	chn_hand(a0),d1
	dc.w	qpc.dsize+2
	tst.l	d2
	bne.s	dos_ichn

	dc.w	qpc.dclse	; close opened file
	move.w	chn_drnr(a0),d2 ; drive number
	lea	chn_name(a0),a1
	dc.w	qpc.ddele+1	; delete this file

	dc.w	qpc.dmdir+1	; create directory
	bne.s	dos_mdexit

	lea	chn_name(a0),a1
	dc.w	qpc.dodir+1	; now open it as a directory
	bne.s	dos_mdexit
	move.l	d1,chn_hand(a0)

	move.l	#hdr.len,chn_fpos(a0)
	move.l	#hdr.len,chn_feof(a0)
	move.l	#4,chn_accs(a0)
	moveq	#0,d0
dos_mdexit
	movem.l (a7)+,d1-d2/a1
	rts

dos_ichn
	movem.l (a7)+,d1-d2/a1
	moveq	#err.ichn,d0
	rts

;+++
; Get/set date
;---
dos_date
	move.l	d2,-(a7)
	cmp.b	#2,d2		; backup date not supported
	beq.s	dos_dateok

	cmp.l	#-1,d1
	beq.s	dos_getdate	; get date

	tst.l	d1
	bne.s	dos_setdate	; set date to specific value
	move.l	qpc_rtc,d1	; current date/time
dos_setdate
	cmp.b	#0,d2
	bne.s	dos_dateok	; can only set update date
	move.l	d1,d2
	move.l	chn_hand(a0),d1
	dc.w	qpc.sdate+2	; set date
	move.l	d2,d1		; return date set
	move.l	(a7)+,d2
	tst.l	d0
	rts

dos_getdate			; also return update date as backup date
	move.l	chn_hand(a0),d1
	dc.w	qpc.ddate+1	; get file date
dos_dateok
	move.l	(a7)+,d2
	moveq	#0,d0
	rts

;+++
; Set date
;---

;+++
; Set or read file version
;---
dos_vers
	moveq	#0,d0
	rts

;+++
; Extended information
;---
dos_xinf
;	 movem.l d1-d3/a4,-(a7)
;	 moveq	 #0,d2
;	 move.b  chn_drid(a0),d2
;	 lsl.w	 #2,d2
;	 lea	 sys_fsdd(a6),a4
;	 move.l  (a4,d2.w),a4
	move.w	chn_drnr(a0),d1

	dc.w	qpc.dxinf		; let PC side do this
;	 movem.l (a7)+,d1-d3/a4
	rts

;+++
; Byte I/O
;---
dos_bio
	cmpi.b	#7,d0
	bhi	dos_ipar
	add.w	d0,d0
	move.w	btable(pc,d0.w),d0
	jmp	btable(pc,d0.w)

btable	dc.w	dos_test-btable
	dc.w	dos_fbyt-btable
	dc.w	dos_flin-btable
	dc.w	dos_fmul-btable
	dc.w	dos_ipar-btable
	dc.w	dos_sbyt-btable
	dc.w	dos_smul-btable
	dc.w	dos_smul-btable

;+++
; Test I/O
;---
dos_test
	move.l	chn_hand(a0),d1 ; DOS handle
	dc.w	qpc.dsize	; Get file size in D0
	addi.l	#hdr.len,d0
	cmp.l	chn_fpos(a0),d0
	bls.s	dos_testeof	; At end of file
	moveq	#0,d0
	rts
dos_testeof
	moveq	#err.eof,d0
	rts

;+++
; Fetch byte
;---
dos_fbyt
	move.l	d1,-(a7)
	move.l	chn_hand(a0),d1 ; DOS handle
	move.l	chn_fpos(a0),d0
	subi.l	#hdr.len,d0
	dc.w	qpc.dmove
	lea	chn_buff(a0),a1 ; Some memory space
	moveq	#1,d0
	dc.w	qpc.dread+1
	bmi.s	dfb_exit
	bcs.s	dfb_feof
	add.l	d0,chn_fpos(a0)
	moveq	#0,d0
dfb_exit
	move.l	(a7)+,d1
	move.b	(a1),d1
	tst.l	d0
	rts

dfb_feof
	move.l	(a7)+,d1
	moveq	#err.eof,d0
	rts

;+++
; Fetch line
;---
dos_flin
	movem.l d3-d4/a2,-(a7)
	move.l	chn_hand(a0),d1
	move.l	chn_fpos(a0),d0
	subi.l	#hdr.len,d0
	dc.w	qpc.dmove
	moveq	#0,d0		; Bytes remaining in internal buffer
	moveq	#0,d3		; Bytes read

dfl_loop
	cmp.w	d2,d3
	beq.s	dfl_bffl

	tst.l	d0
	bne.s	dfl_getfrombuffer

	lea	chn_buff(a0),a2 ; Some memory space
	moveq	#chn.buff,d0	; Buffer length
	dc.w	qpc.dread+2
	bmi.s	dfl_exit
	tst.l	d0
	beq.s	dfl_eof

dfl_getfrombuffer
	subq.l	#1,d0
	addq.l	#1,d3		; One byte more "read"
	move.b	(a2)+,d4	; Next byte

	move.b	d4,(a1)+
	cmp.b	#$a,d4
	bne.s	dfl_loop
dfl_ok
	moveq	#0,d0
dfl_exit
	add.l	d3,chn_fpos(a0)
	moveq	#0,d1		; make sure upper word is clear
	move.w	d3,d1
	movem.l (a7)+,d3-d4/a2
	tst.l	d0
	rts

dfl_bffl
	moveq	#err.bffl,d0
	bra.s	dfl_exit
dfl_eof
	moveq	#err.eof,d0
	bra.s	dfl_exit

;+++
; Fetch multiple bytes
;---
dos_fmul
	move.l	chn_hand(a0),d1
	move.l	chn_fpos(a0),d0
	subi.l	#hdr.len,d0
	dc.w	qpc.dmove
	moveq	#0,d0
	move.w	d2,d0
	dc.w	qpc.dread+1
	bmi.s	dos_fend
	bcs.s	dos_feof
	add.l	d0,chn_fpos(a0)
	moveq	#0,d1		; make sure upper word is clear
	move.w	d0,d1
	add.w	d0,a1
	moveq	#0,d0
dos_fend
	rts
dos_feof
	add.l	d0,chn_fpos(a0)
	moveq	#0,d1		; make sure upper word is clear
	move.w	d0,d1
	add.w	d0,a1
	moveq	#err.eof,d0
	rts

;+++
; Save byte
;---
dos_sbyt
	lea	chn_buff(a0),a1 ; Some memory space
	move.b	d1,(a1) 	; Insert byte
	move.l	chn_hand(a0),d1 ; DOS handle
	move.l	chn_fpos(a0),d0
	subi.l	#hdr.len,d0
	dc.w	qpc.dmove
	moveq	#1,d0		; Ony byte
	dc.w	qpc.dwrte+1	; Write it
	bmi.s	dsb_exit
	add.l	d0,chn_fpos(a0)
	dc.w	qpc.dsize
	add.l	#hdr.len,d0
	move.l	d0,chn_feof(a0)
	moveq	#0,d0
dsb_exit
	rts

;+++
; Save multiple bytes
;---
dos_smul
	move.l	chn_hand(a0),d1
	move.l	chn_fpos(a0),d0
	subi.l	#hdr.len,d0
	dc.w	qpc.dmove
	moveq	#0,d0
	move.w	d2,d0
	dc.w	qpc.dwrte+1
	bmi.s	dos_smend
	add.l	d0,chn_fpos(a0)
	add.w	d0,a1
	move.l	d0,-(a7)
	dc.w	qpc.dsize
	add.l	#hdr.len,d0
	move.l	d0,chn_feof(a0)
	move.l	(a7)+,d1
	andi.l	#$ffff,d1
	moveq	#0,d0
dos_smend
	rts

	end
