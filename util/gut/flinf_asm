; Set File Information			V1.01   1989  Tony Tebby  QJUMP
;
; 2007-07-30  1.01  Show big file sizes in MB (MK)

	section gen_util

	xdef	gu_flinf
	xdef	gu_flinm

	xref	gu_prlis
	xref	cv_d1asc

	include 'dev8_keys_qlv'
	include 'dev8_keys_fll'
	include 'dev8_mac_assert'

;---
; Sets the minumum file information for the files in a file list
;
;	Registers:
;		Entry				Exit
;	D0					error code
;	A1	pointer to list 		preserved
;---
gu_flinm
	move.l	a0,-(sp)
	lea	gfi_setm,a0		 ; how to fill in an entry
	bra.s	gfi_do
;---
; Sets the file information for the files in a file list
;
;	Registers:
;		Entry				Exit
;	D0					error code
;	D2	maximum file length		preserved
;	A1	pointer to list 		preserved
;---
gu_flinf
	move.l	a0,-(sp)
	lea	gfi_seti,a0		 ; how to fill in an entry
gfi_do
	jsr	gu_prlis
	move.l	(sp)+,a0
	rts
;+++
; How to make a directory list entry readable: the date, type and length
; are converted to ASCII in the space provided.
;
;	Registers:
;		Entry				Exit
;	D0					0
;	D2	maximum file length		preserved
;	A1	entry to fix			preserved
;---
gfi_seti
gfi.reg  reg	d1/a0-a3
gfi.fram equ	$24
	movem.l gfi.reg,-(sp)
	move.l	a1,a3
	move.l	sp,a1
	sub.w	#gfi.fram,sp

	move.l	fll_date(a3),d1 	 ; get date
	beq.s	gfi_nodate

	sub.l	a6,a1
	move.w	cv.ildat,a2
	jsr	(a2)
	add.l	a6,a1
	bra.s	gfi_setdate

gfi_nodate
	moveq	#gfi.fram/2-1,d0	 ; set date to spaces
gfi_ndloop
	move.w	#'  ',-(a1)
	dbra	d0,gfi_ndloop

gfi_setdate
	lea	gfi_date,a2		 ; date format
	lea	fll_fnam+2+9(a3,d2.w),a0 ; where to put it
	moveq	#0,d1			 ; xfer reg
	moveq	#gfi.date-1,d0
gfi_dtloop
	move.b	(a2)+,d1
	move.b	(a1,d1.w),(a0)+ 	 ; ... here
	dbra	d0,gfi_dtloop


	lea	fll_fnam+2+1(a3,d2.w),a0 ; fill in the file length
	move.l	a0,a2			 ; keep it
	move.l	fll_flen(a3),d1
	cmp.l	#99999999,d1		 ; silly length?
	ble.s	gfi_cvln		 ; OK

;;;	   moveq   #7,d1	      ; XXX *** dragon
;;;gfi_blatloop
;;;	   move.b  #'*',(a0)+	      ; the length seems to be limited to 8 chars
;;;	   dbra    d1,gfi_blatloop    ; in the menu structure
	addi.l	#$0FFFFF,d1		; MK round up
	moveq	#20,d0			; MK
	lsr.l	d0,d1			; MK convert to MB
	jsr	cv_d1asc		; MK
	move.b	#' ',(a0)+		; MK
	move.b	#'M',(a0)+		; MK
	move.b	#'B',(a0)+		; MK
	bra.s	gfi_cpnm

gfi_cvln
	jsr	cv_d1asc

gfi_cpnm
	lea	8(a2),a1		 ; number should go up to here
gfi_cpnl
	move.b	-(a0),-(a1)
	cmp.l	a2,a0			 ; all of number gone?
	bgt.s	gfi_cpnl		 ; ... no

	assert	fll_name+4,fll_fnam,fll_flch
	lea	fll_name(a3),a0
	moveq	#fll.extr,d0
	add.w	d2,d0			 ; standard length name
	move.w	d0,(a0)+		 ; ... set it

	cmp.w	#2,fll_dtyp(a3) 	 ; version 2?
	bne.s	gfi_type		 ; ... no
	tst.b	fll_type(a3)		 ; directory?
	blt.s	gfi_type		 ; ... yes

	move.l	a0,a2			 ; start of complete name
	add.w	d0,a0			 ; end of name
	moveq	#0,d1
	move.b	#' ',(a0)+
	move.b	#'V',(a0)+
	move.w	fll_vers(a3),d1 	 ; fill in version
	jsr	cv_d1asc
	exg	a0,a2
	sub.l	a0,a2			 ; new length of name
	move.w	a2,-2(a0)

gfi_type
	move.w	#'  ',(a0)+
	move.w	(a0),d1 		 ; length of file name
	moveq	#0,d0
	move.b	fll_type(a3),d0 	 ; file type
	addq.b	#1,d0
	cmp.w	#3,d0			 ; recognisable?
	bls.s	gfi_sflg
	moveq	#1,d0			 ; no, use 0
gfi_sflg
	move.b	gfi_flag(pc,d0.w),(a0)+  ; set flag character
	moveq	#' ',d0
	move.b	d0,(a0)+

	add.w	d1,a0			 ; end of file name
	bra.s	gfi_spce

gfi_spcl
	move.b	d0,-(a1)		 ; fill gap with spaces
gfi_spce
	cmp.l	a0,a1
	bgt.s	gfi_spcl

	add.w	#gfi.fram,sp
	movem.l (sp)+,gfi.reg
	moveq	#0,d0
	rts

gfi_date dc.b	6,6,14,15,16,17,18,10,11,12,6,7,8,9,6,4,5
gfi.date equ	17

gfi_flag dc.w	'> Er'


gfi_setm
	assert	fll_name+4,fll_fnam,fll_flch
	move.w	fll_fnam(a1),d0
	addq.w	#4,d0
	move.w	d0,fll_name(a1) 	 ; longer name
	move.l	#'    ',fll_name+2(a1)	 ; prefilled with spaces
	move.b	fll_type(a1),d0 	 ; file type
	addq.b	#1,d0
	cmp.w	#3,d0			 ; recognisable?
	bls.s	gfm_sflg
	moveq	#1,d0			 ; no, use 0
gfm_sflg
	move.b	gfi_flag(pc,d0.w),fll_name+4(a1)  ; set flag character
	moveq	#0,d0
	rts
	end
