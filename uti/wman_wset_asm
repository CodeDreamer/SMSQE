* Setup a window for the window manager  V1.03	 1986	Tony Tebby   QJUMP
*
	section wman
*
	xdef	ut_wm_prpos
	xdef	ut_wm_pulld
	xdef	ut_wm_unset
	xdef	ut_wm_wrset
	xdef	ut_wm_wpos
*
	include dev8_keys_qdos_ioa
	include dev8_keys_qdos_io
	include dev8_keys_qdos_pt
	include dev8_keys_wwork
	include dev8_keys_wstatus
*
*	d0  r	error return
*	d1 c  p initial pointer position (or -1)
*	a0  r	channel ID of window
*	a1   sp pointer to window status area
*	a4 c  p pointer to working definition
*
*		all other registers preserved
*
reglist  reg	d1-d6/a1/a4
stk_d1	 equ	$0
stk_stat equ	$14 
*
*
ut_wm_unset
	movem.l reglist,-(sp)		save registers
	move.l	ww_chid(a4),a0		get channel ID
	sub.l	a1,a1			no sub-window definition
	moveq	#iop.swdf,d0
	moveq	#-1,d3
	trap	#3
*
*	Restore if pull-down
*
	tst.b	ww_pulld(a4)
	beq	wws_exit		; it's not a pull-down, done
*
	moveq	#0,d2			; throw away save area
	sub.l	a1,a1			; use your own save
	moveq	#-1,d3
	moveq	#iop.wrst,d0		; to restore from
	trap	#3
*
	moveq	#iop.sptr,d0		; reset the pointer position
	move.l	ww_psave(a4),d1
	moveq	#iops.abs,d2		; screen coordinates
	trap	#3
	tst.l	d0
*
	bra.l	wws_exit
*
*
ut_wm_wrset
	movem.l reglist,-(sp)		save registers
	move.l	ww_chid(a4),a0		get channel ID
	lea	ww_xsize(a4),a1 	window definition
	moveq	#iow.defw,d0
	moveq	#0,d2			no border
	moveq	#-1,d3
	trap	#3
	bra.s	wws_set 		set window
*
*
ut_wm_pulld
	movem.l reglist,-(sp)		save registers
*
	moveq	#ioa.open,d0		open
	moveq	#myself,d1		for me
	moveq	#ioa.kexc,d3		a device
	lea	console(pc),a0		named con
	trap	#2			do it
*
	tst.l	d0			check error
	bne.s	wws_exit
	move.l	a0,ww_chid(a4)		set channel ID
	st	ww_pulld(a4)		set 'window pulled down'
	bra.s	wws_cpnt
*
ut_wm_prpos
	movem.l reglist,-(sp)		save registers
	move.l	ww_chid(a4),a0		get channel ID
*
wws_cpnt
	move.l	ww_wstat(a4),a1 	set pointer to status area
	tas	ws_citem(a1)		... no current item
*
	addq.l	#ws_point,a1		set pointer to pointer record
	moveq	#iop.rptr,d0		read pointer
	moveq	#pt.outwn+pt.inwin,d2	return in any event
	moveq	#-1,d3			when complete
	trap	#3
	subq.l	#ws_point,a1		reset pointer to pointer record
	tst.l	d0			OK?
	bne.s	wws_exit		... no
	move.l	d1,ww_psave(a4) 	set saved pointer address
*
	move.l	stk_d1(sp),d0		is new pointer position given?
	bmi.s	wws_spos		... no
	move.l	d0,d1
*
wws_spos
	move.l	ww_xorg(a4),d5		get offset
*****	bne.s	wws_adjst
*****
*****			here find current item position
*****
wws_adjst
	moveq	#0,d6			outline key
	bsr.s	wm_wpos 		position window
	bne.s	wws_exit
*
	moveq	#iow.defb,d0		 set border
	move.w	ww_wattr+wwa_borc(a4),d1 ... colour
	move.w	ww_wattr+wwa_borw(a4),d2 ... and width
	trap	#3
*
wws_set
	moveq	#iow.spap,d0		 ; set paper colour
	move.w	ww_wattr+wwa_papr(a4),d1
	trap	#3
	tst.b	ww_wattr+wwa_clfg(a4)	 ; clear window?
	bmi.s	wws_sprite 

	moveq	#iow.clra,d0
	trap	#3

wws_sprite
	lea	ww_splst(a4),a1 	sprite list
	moveq	#iop.swdf,d0		set as window definition
	trap	#3
*
wws_exit
	tst.l	d0
	movem.l (sp)+,reglist		restore registers
	rts

	page

*
* set window position
*
ut_wm_wpos
wm_wpos
	moveq	#0,d0			find x shadow size
	move.b	wwa_shdd+ww_wattr(a4),d0 should be zero for pulldown!! 
	moveq	#ww.shadx,d2		.... but we're much more clever now
	mulu	d0,d2
	swap	d2
	mulu	#ww.shady,d0		and y shadow
	move.w	d0,d2
*
	moveq	#0,d0			find border width
	move.w	wwa_borw+ww_wattr(a4),d0
	move.w	d0,d4
	add.w	d4,d4			doubled
	swap	d4
	move.w	d0,d4			border width: x,y
*
	lea	ww_xsize(a4),a1 	set pointer to window definition block
	move.l	(a1),d0 		get window size
	add.l	d4,d0			adjust size to include border
	add.l	d4,d0
	move.l	d0,(a1)
	add.l	d4,d5			adjust offset to include border
*
* d0 size, d1 pointer pos, d2 shadow, d4 border, d5 offset
*
	move.l	d4,-(sp)
flimreg reg	d0/d2/a1
	movem.l flimreg,-(sp)
*
	subq.l	#8,a7
	move.l	a7,a1			space for limits
	moveq	#0,d2			unused flag, for now
	moveq	#-1,d3
	moveq	#iop.flim,d0		; find permissible limits
	trap	#3
	movem.l (sp)+,d3/d4		get limits to registers
	add.l	d4,d3			make into max/min
	add.l	#$00030001,d4		; round up minimum
	and.l	#$fffcfffe,d4
	tst.l	d0			; implemented?
	beq.s	wwp_flmset

	move.l	#$02000100,d3		; screen max
	moveq	#0,d4			; screen min

wwp_flmset
	movem.l (sp)+,flimreg
*
	bsr.s	wws_forg		find actual x origin of window
	bsr.s	wws_forg		... and y origin of window
	move.l	(sp)+,d4
*
	and.l	#$fffcfffe,d1		avoid problems with stipples
	move.l	d1,ww_xorg-ww_xsize(a1) set window position
*
	moveq	#iop.outl,d0		set window outline
	move.l	d2,d1			set shadow
	move.l	d6,d2			set d2 parameter
	moveq	#-1,d3
	trap	#3
	tst.l	d0
	bne.s	wwp_exit
*
	moveq	#iop.sptr,d0		set pointer position
	move.l	d5,d1
	moveq	#1,d2			... within hit area
	moveq	#-1,d3
	trap	#3
	moveq	#0,d0			ignore sptr error
*
wwp_exit
	lea	ww_xorg(a4),a1		reset pointer to window definition block
	add.l	d4,(a1) 		adjust for border
	add.l	d4,d4
	sub.l	d4,-(a1)
	tst.l	d0 

	rts


	page

*
* find origin
* call:    d0 size, d1 pointer pos, d2 shadow, d3 max, d4 min, d5 offset
* return:  d1 window origin
*
wws_forg
	swap	d0
	swap	d1			find ideal window origin
	swap	d2
	swap	d3
	swap	d4
	swap	d5
	sub.w	d5,d1			by taking away window offset
	cmp.w	d4,d1			less than minimum?
	ble.s	wwf_orgz		yes, set to minimum

	add.w	d1,d0			find opposite edge, add dimension
	add.w	d2,d0			... and shadow
	sub.w	d3,d0			take away maximum
	ble.s	wwf_done		... done

	sub.w	d0,d1			... outside, move left/up

wwf_done
	rts

wwf_orgz
	move.w	d4,d1			origin at left/top
	rts

*
console dc.w	15,'con_512x256a0x0'
	end
