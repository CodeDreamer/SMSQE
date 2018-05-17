; DEV Thing  V2.01     1989  Tony Tebby  QJUMP

	section exten

	xdef	dev_xinit
	xdef	dev_tnam

	xref	dev_xprocs

	xref	cv_cttab
	xref	cv_upcas

	xref.l	dev_vers
	xref	gu_tpadd
	xref	gu_thjmp
	xref	gu_sstrg
	xref	gu_nl
	xref	gu_sbyte

	xref	thp_ostr

	include 'dev8_keys_thg'
	include 'dev8_keys_iod'
	include 'dev8_keys_err'
	include 'dev8_keys_k'
	include 'dev8_keys_qdos_sms'
	include 'dev8_dd_dev_data'
	include 'dev8_mac_thg'
	include 'dev8_mac_assert'

dev_tnam dc.b	0,3,'DEV',$a

;+++
; dev_xinit initialises the thing
;
;	a3 c  p pointer to linkage block
;	status return standard
;---
dev_xinit
	lea	dev_xprocs,a1		 ; add procedures
	jsr	gu_tpadd

	lea	dev_thing,a1		 ; our Thing
	move.l	a1,th_thing+dvd_thgl(a3) ; ... set pointer
	lea	th_verid+dvd_thgl(a3),a0
	move.l	#dev_vers,(a0)+ 	 ; ... set version
	lea	dev_tnam,a1		 ; thing name
	move.w	(a1)+,(a0)+
	move.l	(a1)+,(a0)+

	lea	dvd_thgl(a3),a1 	 ; link in thing
	moveq	#sms.lthg,d0
	jmp	gu_thjmp


thp_bstb dc.w	thp.ubyt,thp.call+thp.str+thp.opt,thp.ubyt+thp.opt,0
dvu_dev  equ	$00
dvu_name equ	$08
dvu_next equ	$0c

thp_brst dc.w	thp.ubyt,thp.ret+thp.str,0

thp_brb  dc.w	thp.ubyt,thp.ret+thp.ubyt,0
dvu_ret  equ	$08

thp_chn  dc.w	thp.chid,0

;+++
; This is the Thing with the DEV extensions
;
;	a1 c  p devameter stack pointer
;	a2 c  p thing linkage block
;
;---
dev_thing

dev_use thg_extn {USE },dev_usen,thp_bstb

dvu.reg reg	d1/d2/d3/d6/d7/a0/a1/a3
	movem.l dvu.reg,-(sp)

	move.l	dvu_dev(a1),d7		 ; dev n
	ble.l	dvu_ipar
	cmp.w	#8,d7
	bhi.l	dvu_ipar		 ; n is out of range

	move.l	dvu_next(a1),d6 	 ; next dev
	blt.l	dvu_ipar
	cmp.w	#8,d6
	bhi.l	dvu_ipar		 ; next is out of range
	cmp.b	d7,d6
	beq.l	dvu_ipar		 ; next is itself

	lsl.w	#dvd.shft,d7		 ; index table

	moveq	#0,d0			 ; count directory name
	moveq	#0,d1			 ; no number

	move.l	dvu_name(a1),a1 	 ; pointer to name
	move.l	a1,d2
	beq.s	dvu_setn		 ; ... no name
	move.w	(a1)+,d2		 ; and the rest
	beq.s	dvu_setn

	move.l	a1,a0			 ; save start of characters
	lea	cv_cttab(pc),a3 	 ; table of character types
	moveq	#0,d1
	moveq	#0,d3

dvu_cklt
	cmp.w	d2,d0			 ; all taken?
	bgt.s	dvu_inam1		 ; ... yes

	move.b	(a0)+,d1		 ; next character
	jsr	cv_upcas
	cmp.b	#k.uclet,(a3,d1.w)	 ; letter?
	bne.s	dvu_cknr		 ; ... no, check the number

	move.b	d1,d3
	ror.l	#8,d3			 ; keep up to 4 letters
	addq.w	#1,d0			 ; one more
	cmp.w	#dvd.dnam,d0
	ble.s	dvu_cklt		 ; not too many yet
dvu_inam1
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
	cmp.b	#'_',(a0)+		 ; then underscore?
	bne.s	dvu_inam

dvu_setn
	move.b	d1,dvd_dnum-dvd_thgl(a2,d7.w) ; set number
	move.b	d6,dvd_next-dvd_thgl(a2,d7.w) ; set next

	lea	dvd_dnam-dvd_thgl(a2,d7.w),a0

	move.w	d0,(a0)+		 ; drive name
	bra.s	dvu_dnle
dvu_dnlp
	move.b	(a1)+,d1
	jsr	cv_upcas
	move.b	d1,(a0)+		 ; copied
dvu_dnle
	dbra	d0,dvu_dnlp

	addq.l	#2,a1			 ; skip n_

	lea	dvd_fnam-dvd_thgl(a2,d7.w),a0

	move.w	d2,(a0)+		 ; file name
	bra.s	dvu_fnle
dvu_fnlp
	move.b	(a1)+,(a0)+		 ; copied
dvu_fnle
	dbra	d2,dvu_fnlp

dvu_ok
	moveq	#0,d0
dvu_exit
	movem.l (sp)+,dvu.reg
	rts
dvu_inam
	moveq	#err.inam,d0
	bra.s	dvu_exit

dvu_ipar
	moveq	#err.ipar,d0
	bra.s	dvu_exit

;---

dev_usen thg_extn {USEN},dev_use$,thp_ostr

	move.l	a0,-(sp)
	lea	iod_dnam+2-dvd_thgl(a2),a0 ; standard name
	move.l	4(a1),d0		 ; any parameter?
	beq.s	dus_upnam		 ; ... no
	move.l	d0,a0			 ; ... yes, this is it
	cmp.w	#3,(a0)+		 ; 3 characters long?
	bne.s	dus_ipar		 ; ... oops
dus_upnam
	move.l	(a0),d0 		 ; get new name
	and.l	#$5f5f5f00,d0		 ; in upper case
	add.b	#'0',d0 		 ; ending with '0'

	move.l	d0,iod_dnus+2-dvd_thgl(a2) ; set new name (but not length)
	moveq	#0,d0

dus_exit
	move.l	(sp)+,a0
	rts
dus_ipar
	moveq	#err.ipar,d0
	bra.s	dus_exit



;---

dev_use$ thg_extn {USE$},dev_next,thp_brst

	movem.l dvu.reg,-(sp)

	move.l	dvu_dev(a1),d7		 ; dev n
	ble.s	dvu_ipar
	cmp.w	#8,d7
	bhi.s	dvu_ipar		 ; n is out of range
	lsl.w	#dvd.shft,d7		 ; index table

	move.l	dvu_name(a1),a1 	 ; return name here
	clr.w	(a1)+			 ; no length
	move.l	a1,a3

	lea	dvd_dnam-dvd_thgl(a2,d7.w),a0
	move.w	(a0)+,d0
	beq.s	dvu_ok1
dvu_rtdl
	move.b	(a0)+,(a1)+		 ; device name
	subq.w	#1,d0
	bgt.s	dvu_rtdl

	moveq	#'0',d0
	add.b	dvd_dnum-dvd_thgl(a2,d7.w),d0

	move.b	d0,(a1)+		 ; ... number
	move.b	#'_',(a1)+

	lea	dvd_fnam-dvd_thgl(a2,d7.w),a0
	move.w	(a0)+,d0
	bra.s	dvu_rtfe
dvu_rtfl
	move.b	(a0)+,(a1)+		 ; file name
dvu_rtfe
	dbra	d0,dvu_rtfl

	sub.l	a3,a1			 ; length
	move.w	a1,-(a3)		 ; returned.
dvu_ok1
	bra.l	dvu_ok

;---

dev_next thg_extn {NEXT},dev_list,thp_brb

	movem.l dvu.reg,-(sp)

	move.l	dvu_dev(a1),d7		 ; dev n
	ble.l	dvu_ipar
	cmp.w	#8,d7
	bhi.l	dvu_ipar		 ; n is out of range
	lsl.w	#dvd.shft,d7		 ; index table

	move.l	dvu_ret(a1),a1		 ; return value here
	move.b	dvd_next-dvd_thgl(a2,d7.w),(a1)

	bra.s	dvu_ok1

;---

dev_list thg_extn {LIST},,thp_chn

	movem.l dvu.reg,-(sp)

	move.l	(a1),a0 		 ; channel

	moveq	#1,d7			 ; start at 1 defaults
	move.l	a2,a3
	moveq	#0,d0			 ; default return

dvl_loop
	tst.w	dvd_dnam+dvd.entl-dvd_thgl(a3)	  ; any drive?
	beq.s	dvl_eloop

	move.b	d7,d6
	bsr.s	dvl_devn

	moveq	#' ',d1
	bsr.l	gu_sbyte

	lea	dvd_dnam+dvd.entl-dvd_thgl(a3),a1
	bsr.l	gu_sstrg		 ; write drive name

	moveq	#'0',d1
	add.b	dvd_dnum+dvd.entl-dvd_thgl(a3),d1
	bsr.l	gu_sbyte		 ; bits
	moveq	#'_',d1
	bsr.l	gu_sbyte

	lea	dvd_fnam+dvd.entl-dvd_thgl(a3),a1
	bsr.l	gu_sstrg		 ; write filename

	move.b	dvd_next+dvd.entl-dvd_thgl(a3),d6 ; next
	beq.s	dvl_nl			 ; ... none

	lea	dvl_next,a1
	bsr.l	gu_sstrg		 ; next indicator

	bsr.s	dvl_devn		 ; next dev

dvl_nl
	bsr.l	gu_nl			 ; and newline

dvl_eloop
	add.l	#dvd.entl,a3
	addq.b	#1,d7
	cmp.b	#8,d7
	bls.s	dvl_loop

	bra.l	dvu_exit

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

	end
