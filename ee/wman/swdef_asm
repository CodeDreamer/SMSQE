* Set sub-window definition  V1.04     1986  Tony Tebby   QJUMP
*
* 2002-13-11  1.02  Adapted for high colour use (MK)
* 2013-12-21  1.03  sw_swdfn sets character csizes according to working defn (wl)
* 2014-02-03  1.04  Fixed pending new-line bug in wm_csize (MK)
*
	section wman
*
	xdef	wm_swdef
	xdef	wm_swdfn
	xdef	wm_csize
*
	xref	wmc_trap3
*
	include dev8_keys_qdos_io
	include dev8_keys_wwork
*
*	d0  r	error return
*	a0 c  p channel ID of window
*	a3 c  p pointer to sub-window definition
*	a4 c  p pointer to working definition
*
*		all other registers preserved
*
reglist  reg	d1-d3/a1
*
wm_swdef
	movem.l reglist,-(sp)
*
	move.l	wwa_xorg(a3),d0 	reset window
	add.l	ww_xorg(a4),d0		absolute origin
	move.l	d0,-(sp)
	move.l	wwa_xsiz(a3),-(sp)	set size
	move.l	sp,a1
	moveq	#iow.defw,d0		define window
	moveq	#0,d2			no border
	moveq	#-1,d3
	trap	#3			do trap
	tst.l	d0
	addq.l	#8,sp
	bne.s	wsw_exit
*
	moveq	#iow.spap,d0		set paper
	move.w	wwa_watt+wwa_papr(a3),d1
	jsr	wmc_trap3

set_csz moveq	#iow.ssiz,d0		set size
	moveq	#0,d1
	moveq	#0,d2
	trap	#3
	tst.l	d0
*
wsw_exit
	movem.l (sp)+,reglist		restore registers
	rts

* reset csize to 0,0 for wdw
wm_csize
	movem.l reglist,-(sp)

* iow.ssiz will execute a pending new line, make sure none is pending
	moveq	#iow.spix,d0
	moveq	#0,d1
	moveq	#0,d2
	moveq	#-1,d3
	trap	#3
	bra.s	set_csz
		  
*



* same as wm_swdef, but get and set the character csizes in appsub wdw
* this presumes that a3 points to a (menu) appsub defn
*
*	d0  r	error return
*	a0 c  p channel ID of window
*	a3 c  p pointer to (menu) app sub-window definition
*	a4 c  p pointer to working definition
*
*		all other registers preserved
*
wm_swdfn
	movem.l reglist,-(sp)
*
	move.l	wwa_xorg(a3),d0 	reset window
	add.l	ww_xorg(a4),d0		absolute origin
	move.l	d0,-(sp)
	move.l	wwa_xsiz(a3),-(sp)	set size
	move.l	sp,a1
	moveq	#iow.defw,d0		define window
	moveq	#0,d2			no border
	moveq	#-1,d3
	trap	#3			do trap
	tst.l	d0
	addq.l	#8,sp
	bne.s	wsw_ext2
*
	moveq	#iow.spap,d0		set paper
	move.w	wwa_watt+wwa_papr(a3),d1
	jsr	wmc_trap3

	moveq	#iow.ssiz,d0		set size
	moveq	#0,d1
	moveq	#0,d2
	move.b	wwa_xcsz(a3),d1 	set x csize
	move.b	wwa_ycsz(a3),d2 	set y csize
	trap	#3
	tst.l	d0
*
wsw_ext2
	movem.l (sp)+,reglist		restore registers
	rts
	

	end
