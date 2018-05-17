* Process change window event  V1.01   1986  Tony Tebby   QJUMP
* 2003-05-27			v1.02 call upon new move routine (WL)
	section wman
*
	xdef	wm_chwin
*
	xref	sp_wmove
	xref	sp_wsize
	xref	wm_clrci
	xref	wm_wpos
	xref	changewin
*
	include dev8_keys_qdos_io
	include dev8_keys_qdos_pt
	include dev8_keys_sys
	include dev8_keys_chn
	include dev8_keys_k
	include dev8_keys_wwork
	include dev8_keys_wstatus
	include dev8_keys_con
*
*	d0  r	error return
*	d1  r	pointer move
*	d4  r	0 or pt..sizw
*	a0  r	channel ID of window
*	a1   sp window status area
*	a4 c  p pointer to working definition
*
*		all other registers preserved
*
reglist  reg	d1-d7/a1/a4
stk_d1	 equ	$0
stk_ev	 equ	$0c
stk_stat equ	$1c
*
*
wm_chwin
	moveq	#0,d4			preset return
	movem.l reglist,-(sp)		save registers
	move.l	ww_chid(a4),a0		get channel ID
	move.l	ww_wstat(a4),a1 	window status area
	bsr.l	wm_clrci		clear current item
	moveq	#0,d0
	move.l	ws_ptpos(a1),d5 	set old pointer position
*
	bclr	#pt..wmov,wsp_weve(a1)	is it?
	bne.s	wcw_move		... yes
	btst	#pt..wsiz,wsp_weve(a1)	try size
	beq.s	wcw_exit		... no
*
	moveq	#$ffffff00+pt.wsize,d2
	bsr.s	wcw_rptr		read new pointer position
	sub.w	d5,d1			and set difference
	swap	d5
	swap	d1
	sub.w	d5,d1
	swap	d1
	move.l	d1,stk_d1(sp)		return the answer
	move.b	#pt..wsiz,stk_ev+3(sp)	set wsiz event to be done
	tst.l	d0
	bra.s	wcw_exit
*
wcw_move
	jsr	changewin		try new move routines
	beq.s	wcw_exit		... were OK
	moveq	#$ffffff00+pt.wmove,d2	window move sprite
	bsr.s	wcw_rptr		read new position
	bne.s	wcw_exit
*
	move.l	ww_xorg(a4),d7		save old origin
	sub.l	ww_xorg(a4),d5		make old position relative to the window
	moveq	#1,d6			outline move
	bsr.l	wm_wpos 		position window
	beq.s	wcw_exit		... ok
	move.l	d7,ww_xorg(a4)		restore window origin
*
wcw_exit
	movem.l (sp)+,reglist		restore registers
	rts
	page
*
* read pointer move
*
secreg	reg	a0/a2
secfram equ	sw_psprt+16
wcw_rptr
	tst.b	ww_pulld(a4)		pulled-down window?
	beq.s	wcw_dord		no, just do read
*
	movem.l secreg,-(sp)
	sub.w	#secfram,sp		make space for a sub-window definition
	move.l	a1,-(sp)		keep workspace pointer
	lea	8+4(sp),a1		point to it
	clr.l	(a1)			no list
	clr.l	sw_xorg+4(a1)		origin at top left
	clr.l	sw_attr+4(a1)		  disable cursor
	clr.l	sw_attr+8(a1)
	lea	sp_wmove(pc),a2 	move sprite
	cmp.b	#pt.wmove,d2		is it move?
	beq.s	wcw_sspr		yes
	lea	sp_wsize(pc),a2 	no, set size sprite 
wcw_sspr
	move.l	a2,sw_psprt+4(a1)	set sprite
	lea	wcw_exsw(pc),a2 	set primary's sub-window definition
	moveq	#-1,d3
	moveq	#iow.xtop,d0
	trap	#3
	move.l	d1,a0			get primary's channel ID
	move.l	(sp)+,a1		restore workspace
	addq.l	#ws_point,a1		...smashed version
wcw_rrdp
	subq.l	#ws_point,a1		unsmash A1 from DORD
	moveq	#pt.kystk,d2		do an ordinary read
	bsr.s	wcw_dord
	move.l	d0,d2			keep any error
	bne.s	wcw_exms		there was one, exit whatever
	cmp.b	#k.maxch,pp_kstrk(a1)	cursor key?
	bhi.s	wcw_rrdp		yes, ignore it
wcw_exms
	lea	8(sp),a1		point to bits to restore
	lea	wcw_exrs(pc),a2
	moveq	#iow.xtop,d0
	trap	#3			reset primary's sub and secondary lists
	add.w	#secfram,a7
	movem.l (sp)+,secreg
	move.l	d2,d0			restore any error
	rts
*
wcw_dord
	moveq	#iop.rptr,d0		read pointer
	addq.l	#ws_point,a1		read pointer record
	moveq	#-1,d3
	trap	#3
	tst.l	d0
	rts
*
*	EXTOP to set primary's sub-window definition, and return its
*	channel ID.
*
*	Registers:
*		Entry				Exit
*	D1					primary's channel ID
*	A1	new SWDEF			old swdef
*
extreg	reg	a0/a2
wcw_exsw
	movem.l extreg,-(sp)
	move.l	sd_pprwn(a0),a0 	point to primary window
	move.w	chn_tag(a0),d1		get its tag
	swap	d1
	move.l	sys_chtb(a6),a2 	scan channel table
	move.w	#0,d1			starting with channel number 0
wcx_loop
	cmp.l	(a2),a0 		our primary?
	beq.s	wcx_cfnd		yes
	addq.w	#1,d1			no, next channel...
	addq.l	#4,a2			...is here
	bra.s	wcx_loop		try that
wcx_cfnd
	add.w	#sd.extnl,a0		point to normal cdb
	move.l	sd_wwdef(a0),-4(a1)	save old sub-window definition
	move.l	sd_sewll(a0),-8(a1)	and secondary list
	move.l	a1,sd_wwdef(a0) 	set new sub-window definition
	clr.l	sd_sewll(a0)		and pretend there's no secondaries
	move.l	sd_xsize(a0),sw_xsize+4(a1) set sub-window size
	movem.l (sp)+,extreg
	moveq	#0,d0
	rts
*
wcw_exrs
	move.l	-4(a1),sd_wwdef(a0)	restore old secondary
	move.l	-8(a1),sd_sewll(a0)	and secondary list
	moveq	#0,d0
	rts
*
	end
