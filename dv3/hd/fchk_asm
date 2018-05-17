; Hard Disk Format Check    V2.00     1990   Tony Tebby QJUMP

	section dv3

	xdef	hd_fchk

	xref	cv_upcas

	include 'dev8_keys_err'
	include 'dev8_keys_msg8'
	include 'dev8_keys_sys'
	include 'dev8_keys_k'
	include 'dev8_keys_qdos_io'
	include 'dev8_keys_qdos_sms'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'
	
;+++
; This checks the whether it is sensible to format the hard disk
;
;	d7 c  p msw drive number
;	a3 c  p linkahge block
;	status return 0 or err.accd
;---
hd_fchk
hfc.reg  reg	d1/d3/d4/a0/a1
frame	 equ	4
	movem.l hfc.reg,-(sp)
	subq.l	#frame,sp

	lea	-1(a3,d7.w),a0
	tst.b	hdl_wprt(a0)		 ; positive for formattable
	ble.s	hfc_accd		   ; not formattable

	sub.l	a0,a0			 ; use channel 0
	move.w	#0,d3			 ; no wait
	moveq	#0,d1			 ; d1
	moveq	#0,d2
	moveq	#iop.pick,d0
	trap	#do.io

	move.w	#msg8.fmtq,a1		 ; format query message
	moveq	#sms.mptr,d0
	trap	#do.sms2

	bsr.s	hfc_wstrg

	move.w	sys_rand(a6),d0 	 ; get two character key
	divu	#26,d0
	move.l	d0,(sp)
	ext.l	d0    
	divu	#26,d0
	swap	d0
	move.b	d0,(sp)
	add.w	#'AA',(sp)
	move.w	#'> ',2(sp)
	move.l	sp,a1			 ; write it
	moveq	#4,d2			 ; four characters
	bsr.s	hfc_wmul

	bsr.s	hfc_rchar		 ; get first key
	bsr.s	hfc_rchar		 ; and second
	bsr.s	hfc_nl			 ; newline

	cmp.w	(sp),d2
	beq.s	hfc_ok

hfc_accd
	moveq	#err.accd,d0
	bra.s	hfc_exit
hfc_fdnf
	moveq	#err.fdnf,d0
	bra.s	hfc_exit
hfc_ok
	moveq	#0,d0
hfc_exit
	addq.l	#frame,sp
	movem.l (sp)+,hfc.reg
	rts

hfc_rnum
	moveq	#0,d2
	bsr.s	hfc_rchar		 ; read character
	sub.w	#'0',d2 		 ; make it a digit
	blt.s	hfc_rts
	cmp.w	#9,d2			 ; ... 0-9
	bgt.s	hfc_rts

hfc_nl
	moveq	#k.nl,d1
hfc_wchar
	moveq	#iob.sbyt,d0
	bra.s	hfc_io

hfc_rchar
	moveq	#iow.ecur,d0		 ; enable cursor
	bsr.s	hfc_io
	moveq	#iob.fbyt,d0
	bsr.s	hfc_io
	jsr	cv_upcas		 ; upper case
	lsl.w	#8,d2
	move.b	d1,d2			 ; save in d2
	bsr.s	hfc_wchar
	moveq	#iow.dcur,d0
	bra.s	hfc_io

hfc_wstrg
	move.w	(a1)+,d2
hfc_wmul
	moveq	#iob.smul,d0
	bra.s	hfc_io

hfc_retry
	move.l	(sp)+,d0
hfc_io
	move.l	d0,-(sp)
	trap	#do.io
	addq.l	#-err.nc,d0
	beq.s	hfc_retry
	subq.l	#-err.nc,d0
	addq.l	#4,sp
hfc_rts
	rts
	end
