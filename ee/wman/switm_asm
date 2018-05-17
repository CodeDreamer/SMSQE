; Window manager set window to item  V0.03    1986   Tony Tebby   QJUMP
;
; 2002-13-11  0.03  Adapted for high colour use (MK)

	section wman

	xdef	wm_swsec

	xdef	wm_swinf
	xdef	wm_swlit
	xdef	wm_swapp

	xref	wmc_trap3

	include dev8_keys_err
	include dev8_keys_qdos_io
	include dev8_keys_wwork
	include dev8_keys_wstatus
	include dev8_mac_assert

;	d0  r	error return (out of range, not open??)
;	d1 c  p x,y section numbers
;	d2 c  p ink colour (-ve long word if attributes not to be reset)
;	a0   r	channel ID
;	a3 c  p sub-window definition
;	a4 c  p working definitiion

reg_sec  reg	d1/d2/d3/d4/d5/d6/a1/a2
stk_ink  equ	$04

wm_swsec
	movem.l reg_sec,-(sp)
	move.l	wwa_part+wwa.clen(a3),d6 ; y part control block
	beq.s	wms_wsec		 ; none
	move.l	d6,a2
	move.w	(a2),d6
	beq.s	wms_wsec		 ; no sections
	move.w	wwa_iatt+wwa_curw(a3),d6
	add.w	d6,d6
	addq.w	#ww.scarr,d6		 ; scroll arrow height + border
wms_wsec
	add.w	d6,wwa_yorg(a3) 	 ; adjust size
	add.w	d6,d6
	sub.w	d6,wwa_ysiz(a3)
	move.l	wwa_watt+wwa_papr(a3),d5 ; paper
	move.w	d2,d5			 ; and ink
	lea	wwa_xsiz(a3),a1
	bsr.l	wms_setw		 ; set window
	add.w	d6,wwa_ysiz(a3)
	lsr.w	#1,d6
	sub.w	d6,wwa_yorg(a3)
	tst.l	d0
	movem.l (sp)+,reg_sec
	rts
	page

;	d0  r	error return (out of range, not open??)
;	d1 c  p item (or sub-window) number
;	d2 c  p ink colour / item status (-ve long word if attributes not reset)
;	a0  r	channel ID
;	a1  r	pointer to item/window
;	a4 c  p working definitiion

reglist  reg	d1/d2/d3/d4/d5/a3
stk_stat equ	$04
	assert	stk_ink,stk_stat

wm_swlit
	movem.l reglist,-(sp)
	cmp.w	ww_nlitm(a4),d1 	 ; item number ok?
	bhs.s	wms_orng		 ; ... no
	mulu	#wwl.elen,d1
	move.l	ww_plitm(a4),a3 	 ; loose item list
	add.l	d1,a3

	ext.w	d2
	beq.s	wms_sitat		 ; available
	bgt.s	wms_unav		 ; unavailable
	moveq	#wwa_selc-wwa_aval,d2	 ; selected
	bra.s	wms_sitat
wms_unav
	moveq	#wwa_unav-wwa_aval,d2
wms_sitat
	assert	wwa_back,wwa_ink-2
	move.l	ww_lattr+wwa_aval+wwa_back(a4,d2.w),d5	; set item attributes
	assert	wwi_xsiz,wwl_xsiz,wwa_xsiz,0
	lea	wwi_xsiz(a3),a1 	 ; window definition
	bsr.s	wms_setw
	moveq	#0,d1
	moveq	#0,d2
	move.b	wwl_xjst(a3),d1 	 ; set start position
	move.b	wwl_yjst(a3),d2
	moveq	#iow.spix,d0
	bsr.l	wms_io
	moveq	#0,d0			 ; ignore the error
	bra.s	wms_exit

wm_swinf
	movem.l reglist,-(sp)
	cmp.w	ww_ninfo(a4),d1 	 ; information window ok?
	bhs.s	wms_orng		 ; ... no
	mulu	#wwi.elen,d1
	move.l	ww_pinfo(a4),a3 	 ; point to info
	add.l	d1,a3
	bra.s	wms_attr		 ; set attributes

wm_swapp
	movem.l reglist,-(sp)
	cmp.w	ww_nappl(a4),d1 	 ; application window ok?
	bhs.s	wms_orng		 ; ... no
	lsl.w	#2,d1
	move.l	ww_pappl(a4),a3 	 ; point to application window list
	move.l	(a3,d1.w),a3		 ; indirect
wms_attr
	assert	wwi_watt,wwa_watt
	move.l	wwa_watt+wwa_papr(a3),d5 ; paper colour msw
	move.w	d2,d5			 ; ink colour

wms_wind
	assert	wwi_xsiz,wwl_xsiz,wwa_xsiz,0
	lea	wwi_xsiz(a3),a1 	 ; window definition
	bsr.s	wms_setw

wms_exit
	move.l	a3,a1			 ; return item pointer
	tst.l	d0
	movem.l (sp)+,reglist
	rts

wms_orng
	moveq	#err.orng,d0
	bra.s	wms_exit
	page

wms_setw
	move.l	ww_xorg(a4),d4
	add.l	d4,wwi_xorg(a3) 	 ; absolute origin

	move.l	ww_chid(a4),a0		 ; channel ID
	moveq	#iow.defw,d0		 ; define window
	moveq	#0,d2			 ; no border
	bsr.s	wms_io
	sub.l	d4,wwi_xorg(a3)
	tst.l	d0
	bne.s	wms_rts

	btst	#7,stk_ink+4(sp)	 ; set attributes?
	bne.s	wms_rts0		 ; no

	moveq	#iow.sink,d0		 ; set ink
	move.w	d5,d1
	bsr.s	wms_io

	moveq	#iow.sstr,d0		 ; set strip
	swap	d5
	move.w	d5,d1
	bsr.s	wms_io

	moveq	#iow.spap,d0		 ; set paper
	move.w	d5,d1
	bsr.s	wms_io

	moveq	#iow.sova,d0		 ; set overwrite
	moveq	#0,d1
	bsr.s	wms_io

	moveq	#iow.ssiz,d0		 ; set character size
	moveq	#0,d1
	moveq	#0,d2

wms_io
	moveq	#forever,d3		 ; wait forever
	jsr	wmc_trap3
wms_rts0
	tst.l	d0
wms_rts
	rts
	end
