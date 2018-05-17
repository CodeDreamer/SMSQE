; DEV_USE/DEV_USE$/DEV_NEXT    V2.00	 1989	Tony Tebby  QJUMP

	section exten

	xdef	dev_basic
dev_basic

	xdef	dev_xinit
	xdef	dev_use
	xdef	dev_usen
	xdef	dev_use$
	xdef	dev_next
	xdef	dev_list

	xref	dev_name
	xref	cv_cttab
	xref	cv_upcas
	xref	iou_flnk
	xref	iou_use
	xref	ut_gtnm1
	xref	ut_gtin1
	xref	ut_gxin1
	xref	ut_retst
	xref	ut_rtint
	xref	ut_chkri
	xref	gu_sstrg
	xref	gu_nl
	xref	gu_sbyte
	xref	ut_chan1

	include 'dev8_keys_err'
	include 'dev8_keys_k'
	include 'dev8_keys_iod'
	include 'dev8_dd_dev_data'
	include 'dev8_keys_sbasic'

;+++
; dummy init extensions
;---
dev_xinit
	moveq	#0,d0
	rts

;+++
; DEV_USE dev,name,next
;---
dev_use
	moveq	#8,d0
	add.l	a3,d0
	sub.l	a5,d0			 ; just one parameter?
	blt.s	dev_set
dev_usen
	move.l	dev_name,d7
	jmp	iou_use

dev_set
	bsr.l	dvu_dev 		 ; get table entry (a0)

	moveq	#0,d6			 ; no next

	move.l	sb_buffb(a6),a1
	clr.w	(a6,a1.l)		 ; no name

	cmp.l	a3,a5			 ; any string
	beq.s	dvu_check

	jsr	ut_gtnm1		 ; and one name
	addq.l	#8,a3
	move.l	a1,sb_arthp(a6) 	 ; keep it on stack!!

	cmp.l	a3,a5			 ; another param?
	beq.s	dvu_check

	bsr.l	dvu_next		 ; next is in d6

dvu_check
	moveq	#0,d0			 ; count directory name
	move.w	(a6,a1.l),d2		 ; and the rest
	beq.s	dvu_set

	lea	cv_cttab(pc),a3 	 ; table of character types
	moveq	#0,d1
	moveq	#0,d3

dvu_cklt
	move.b	2(a6,a1.l),d1		 ; next character
	addq.l	#1,a1
	jsr	cv_upcas
	cmp.b	#k.uclet,(a3,d1.w)	 ; letter?
	bne.s	dvu_cknr		 ; ... no, check the number

	move.b	d1,d3
	ror.l	#8,d3			 ; keep up to 4 letters
	addq.w	#1,d0			 ; one more
	cmp.w	#dvd.dnam,d0
	ble.s	dvu_cklt		 ; not too many yet
	bra.s	dvu_inam

dvu_cknr
	tst.w	d0			 ; any device?
	ble.s	dvu_inam		 ; ... no
	cmp.l	#'VED'<<8,d3		 ; DEV?
	beq.s	dvu_inam
	cmp.l	#'D'<<24,d3		 ; D?
	beq.s	dvu_inam

	sub.w	d0,d2
	subq.w	#2,d2			 ; d2 now file name char count
	blt.s	dvu_inam		 ; ... less than none
	cmp.w	#dvd.fnam,d2		 ; too many?
	bhi.s	dvu_inam

	sub.b	#'0',d1 		 ; 0?
	ble.s	dvu_inam		 ;
	cmp.b	#8,d1			 ; 8?
	bhi.s	dvu_inam
	cmp.b	#'_',2(a6,a1.l) 	 ; then underscore?
	bne.s	dvu_inam

dvu_set
	move.b	d1,dvd_dnum(a0) ; set number
	move.b	d6,dvd_next(a0) ; set next

	lea	dvd_dnam(a0),a2
	sub.w	d0,a1
	move.w	d0,(a2)+		 ; drive name
	bra.s	dvu_dnle
dvu_dnlp
	move.b	1(a6,a1.l),d1		 ; drive name character
	addq.l	#1,a1
	jsr	cv_upcas
	move.b	d1,(a2)+		 ; copied
dvu_dnle
	dbra	d0,dvu_dnlp

	lea	dvd_fnam(a0),a2

	move.w	d2,(a2)+		 ; file name
	bra.s	dvu_fnle
dvu_fnlp
	move.b	3(a6,a1.l),(a2)+	 ; copied
	addq.l	#1,a1
dvu_fnle
	dbra	d2,dvu_fnlp

dvu_ok
	moveq	#0,d0
	rts
dvu_inam
	moveq	#err.inam,d0
	rts

dvu_ipar
	moveq	#err.ipar,d0
	rts

;+++
; DEV_USE$(dev)
;---
dev_use$
	bsr.l	dvu_dev 		 ; get table entry (a0)
	cmp.l	a3,a5
	bne.s	dvu_ipar

	moveq	#5,d1
	move.w	dvd_dnam(a0),d2
	add.w	dvd_fnam(a0),d2
	beq.s	dvu_null
	add.w	d2,d1
	bclr	#0,d1
	jsr	ut_chkri		 ; check for room
	sub.w	d1,a1			 ; start of string

	move.l	a1,a3

	lea	dvd_dnam(a0),a2
	move.w	(a2)+,d0
	bra.s	dvu_rtde
dvu_rtdl
	move.b	(a2)+,2(a6,a3.l)	 ; device name
	addq.l	#1,a3
dvu_rtde
	dbra	d0,dvu_rtdl

	moveq	#'0',d0
	add.b	dvd_dnum(a0),d0

	move.b	d0,2(a6,a3.l)		  ; ... number
	move.b	#'_',3(a6,a3.l)
	addq.l	#2,a3

	lea	dvd_fnam(a0),a2
	move.w	(a2)+,d0
	bra.s	dvu_rtfe
dvu_rtfl
	move.b	(a2)+,2(a6,a3.l)	 ; file name
	addq.l	#1,a3
dvu_rtfe
	dbra	d0,dvu_rtfl

	sub.l	a1,a3			 ; length
	move.w	a3,(a6,a1.l)		 ; returned
dvu_rtst
	jmp	ut_retst

dvu_null
	subq.w	#2,a1
	clr.w	(a6,a1.l)		 ; null return
	bra.s	dvu_rtst


;+++
; DEV_NEXT(dev)
;---
dev_next
	bsr.l	dvu_dev 		 ; get table entry (a0)
	cmp.l	a3,a5
	bne.s	dvu_ipar

	moveq	#0,d1
	move.b	dvd_next(a0),d1
	jmp	ut_rtint

;+++
; DEV_LIST #chan
;---
dev_list
	bsr.l	dvu_link		 ; find link
	bne.s	dvl_rts

	lea	-iod_iolk(a0),a2

	jsr	ut_chan1		 ; get channel
	bne.s	dvl_rts

	moveq	#1,d7			 ; start at 1 defaults

dvl_loop
	tst.w	dvd_dnam+dvd.entl(a2)	 ; any drive?
	beq.s	dvl_eloop

	move.b	d7,d6
	bsr.s	dvl_devn

	moveq	#' ',d1
	bsr.l	gu_sbyte

	lea	dvd_dnam+dvd.entl(a2),a1
	bsr.l	gu_sstrg		 ; write drive name

	moveq	#'0',d1
	add.b	dvd_dnum+dvd.entl(a2),d1
	bsr.l	gu_sbyte		 ; bits
	moveq	#'_',d1
	bsr.l	gu_sbyte

	lea	dvd_fnam+dvd.entl(a2),a1
	bsr.l	gu_sstrg		 ; write filename

	move.b	dvd_next+dvd.entl(a2),d6 ; next
	beq.s	dvl_nl			 ; ... none

	lea	dvl_next,a1
	bsr.l	gu_sstrg		 ; next indicator

	bsr.s	dvl_devn		 ; next dev

dvl_nl
	bsr.l	gu_nl			 ; and newline

dvl_eloop
	add.l	#dvd.entl,a2
	addq.b	#1,d7
	cmp.b	#8,d7
	bls.s	dvl_loop

dvl_rts
	rts

dvl_devn
	lea	dvl_name,a1
	jsr	gu_sstrg
	moveq	#'0',d1
	add.b	d6,d1
	bsr.l	gu_sbyte		 ; bits
	moveq	#'_',d1
	bra.l	gu_sbyte


dvl_next dc.w	4,' -> '
dvl_name dc.w	3,'DEV0'

dvu_dev
	bsr.s	dvu_link		 ; find link
	bne.s	dvu_rts4

	jsr	ut_gtin1		 ; get an integer
	addq.l	#8,a3
	bne.s	dvu_rts4

	move.w	(a6,a1.l),d7		 ; dev n
	ble.s	dvu_ipa4
	cmp.w	#8,d7
	bhi.s	dvu_ipa4		 ; n is out of range
	lsl.w	#dvd.shft,d7		 ; index table
	lea	-iod_iolk(a0,d7.w),a0

dvu_remi
	addq.l	#2,a1
	move.l	a1,sb_arthp(a6) 	 ; clean up stack
	rts

dvu_ipa4
	moveq	#err.ipar,d0
dvu_rts4
	addq.l	#4,sp
	rts

dvu_next
	jsr	ut_gxin1		 ; get an integer
	bne.s	dvu_rts4

	move.w	(a6,a1.l),d6		 ; next
	blt.s	dvu_ipa4
	cmp.w	#8,d6
	bhi.s	dvu_ipa4		 ; next is out of range
	bra.s	dvu_remi


dvu_link
	move.l	dev_name,d7
	jmp	iou_flnk		 ; find link

	end
