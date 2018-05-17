* ED - Read input keys		 1985	Tony Tebby   QJUMP
*
	section ed
*
	xdef	ed_keyin
	xdef	ed_up
	xdef	ed_down
	xdef	ed_right
*
	xref	ed_sttmd
	xref	ed_endmd
	xref	ed_abtmd
	xref	ed_getln
*
	xref	ed_setps
*
	xref	ed_insch
	xref	ed_delch
	xref	ed_enter
	xref	ed_dline
	xref	ed_wprev
	xref	ed_wnext
	xref	ed_scrup
	xref	ed_scrdn
	xref	ed_wrtop

	xref	cv_cttab
	xref	ut_stufc
*
	include dev8_sbsext_ed_keys
	include dev8_sbsext_ed_keydef
	include dev8_sbsext_ed_data
*
ed_keyin
	tas	ed_clear(a5)	is window 0 clear?
	bne.s	ed_key1 	... yes
	tst.w	ed_chno(a5)	editing in channel 0?
	beq.s	ed_key1 	... yes
	move.l	a0,a4
	move.l	bv_chbas(a6),a0
	move.l	(a6,a0.l),a0	... no, clear zero
	moveq	#sd.clear,d0
	trap	#3
	move.l	a4,a0
ed_key1
	bsr.l	ed_setps	set cursor position
	moveq	#sd.cure,d0	and enable it
	trap	#3
	moveq	#io.fbyte,d0	... then read a byte
	trap	#3
	move.l	d0,d4		save error code
	moveq	#sd.curs,d0	and disable cursor
	trap	#3
	move.l	d4,d0
	bne.s	ed_abort
*
	move.b	d1,ed_ccmd(a5)	save the keystroke
	cmp.b	#k.fchar,d1	check for printable character
	blo.s	key_special	... special
	cmp.b	#k.lchar,d1
	bhi.s	key_special
*
	move.b	d1,-(sp)
	bsr.s	edk_sttmd	start modify
	move.b	(sp)+,d1
	bsr.l	ed_insch	and insert character
	bra.s	ed_keyin
*
key_special
	lea	key_act-2(pc),a1 find action table
key_look
	addq.l	#2,a1
	move.w	(a1)+,d2	next action code
	beq.s	ed_keyin	... end of list
	cmp.b	d1,d2		is this action the one required?
	bne.s	key_look	... no, try next one
	add.w	(a1),a1 	... yes, add offset
	move.l	a1,-(sp)
	asr.w	#8,d2		check for modification action
	beq.s	key_call
	bgt.s	key_endmd
	lsl.b	#1,d2		get line?
	beq.s	key_getln
	bsr.s	edk_sttmd	start modification
	bra.s	key_call
key_endmd
	bsr.l	ed_endmd	end modifiction
	beq.s	key_call	... ok
key_oops
	addq.l	#4,sp		get rid of call
	bra.s	ed_key1 	and read without clearing window zero

key_getln
	jsr	ed_getln	get line (so we can move by word etc)

key_call
	move.l	(sp)+,a1
	jsr	(a1)		and do action
	bra	ed_keyin
*
* before starting any modifications, ensure cursor not in margin
*
edk_sttmd
	bsr.l	edk_marg	move out of margin
	bra.l	ed_sttmd	and start modification
*
*
ed_esc
	tst.b	ed_chang(a5)	is line changed?
	bne.s	ed_abort	... yes, abort changes
	addq.l	#4,sp		otherwise return directly
ed_abort
	bra.l	ed_abtmd	abort modifications
*
edk_up
	tst.w	edr_rno(a5,d5.w) is this first row of line?
	bne.s	edku_do 	... no, just do it
	bsr.l	ed_endmd	... yes, end mod
	bne.s	key_oops	oops
ed_up
edku_do
	tst.w	d6		at top?
	beq.s	edk_scru	... yes, scroll up
	subq.w	#8,d5		... no, move up a line
	subq.w	#1,d6
edku_rts
	rts
*
edk_scru
	tst.w	edr_lnr(a5)	blank line?
	beq.s	edku_rts	... yes
	moveq	#0,d2		... no, scroll up from top
	bsr.l	ed_scrup
	bra.l	ed_wrtop	and rewrite top line
*
edk_down
	tst.w	edr_rno+edr.len(a5,d5.w) is this last row of line?
	bne.s	edkd_do 	... no, just do it
	bsr.l	ed_endmd	... yes, end mod
	bne.s	key_oops	oops
ed_down
edkd_do
	cmp.w	ed_nrow1(a5),d6 is it at the bottom?
	beq.s	edk_scrdem	... yes, scroll down after end mod
	addq.w	#8,d5		... no, move cursor down
	addq.w	#1,d6
edkd_rts
	rts
*

edk_scrdem
	bsr.l	ed_endmd	for safety
	bne.s	ed_abort	... panic
*****	     bne.s   key_oops
edk_scrd
	moveq	#0,d2		scroll to top
	move.w	ed_nrow1(a5),d0 number of lines to scroll
	asl.w	#3,d0
	tst.w	edr_lnr(a5,d0.w) is last row blank ?
	bne.l	ed_scrdn	... no, scroll down
	rts
*
* move left and right
*
edk_toleft
	bsr.l	edk_strtl	find number of characters to start of line
	bra.s	edk_mleft
edk_wleft
	bsr.l	edk_lwleft	locate word to left
	bra.s	edk_mleft
edk_btab
	bsr.s	edk_cpos	find current position
	subq.w	#1,d1		and find how far to move
	and.w	#$07,d1
	addq.w	#1,d1
	bra.s	edk_mleft
*
edk_left
	bsr.s	edk_cpos	find current position
edk_lft1
	moveq	#1,d1		move one space
edk_mleft
	sub.w	d0,d7		move to lhs of window
	sub.w	d1,d7		move left
	bge.s	edkl_marg	... still on screen, add margin back
	tst.w	d0		is it on first row of line?
	beq.s	edkm_d0 	... yes, set zero
	add.w	ed_ncols(a5),d7 set to rhs
	bra.s	edku_do 	and move up
edkl_marg
	add.w	d0,d7		add margin back
	rts
*
* find current position on line
*
edk_cpos
	bsr.s	edk_marg	get cursor out of margin
edk_cps1
	move.w	edr_rno(a5,d5.w),d1 start position on line
	mulu	ed_ncolx(a5),d1
	add.w	d7,d1		current position
	rts

edk_marg
	moveq	#0,d0		reset margin
	tst.w	edr_rno(a5,d5.w) first line?
	beq.s	edkm_rts	yes, no margin
	moveq	#ed.xoff,d0	set margin
	cmp.w	d0,d7		left of margin?
	bge.s	edkm_rts	... no, done
edkm_d0
	move.w	d0,d7
edkm_rts
	rts
*
edk_toright
	bsr.l	edk_endl	find number of characters to end of line
	bra.s	edk_mright
edk_wright
	bsr.l	edk_lwright	locate word to left
	bra.s	edk_mright
edk_tab
	bsr.s	edk_cpos	find current position
	not.w	d1		find number of spaces to move
	and.w	#$07,d1
	addq.w	#1,d1
	bra.s	edk_mright
*
ed_right
edk_right
	moveq	#1,d1
edk_mright
	add.w	d1,d7		move cursor
	move.w	ed_ncol1(a5),d2
	cmp.w	d2,d7		is it at rhs?
	ble.s	edkr_rts	... no, done
	tst.w	edr_rno+edr.len(a5,d5.w) is it on last row of line?
	beq.s	edkr_srhs	... yes, set rhs
	sub.w	d2,d7
	addq.w	#ed.xoff-1,d7	set to lhm
	bra.l	edkd_do 	and move down
edkr_srhs
	move.w	d2,d7
edkr_rts
	rts
*
edk_dleft
	bsr.s	edk_cpos	get character position
	beq.s	edkr_rts	... back at the start
	bsr.s	edk_lft1	go left
edk_dright
	bra.l	ed_delch	delete this character
*
edk_dtoleft
	bsr.s	edk_strtl	find number of characters to start of line
	bra.s	edk_dmleft
edk_dwleft
	bsr.s	edk_lwleft	locate word to left
	bra.s	edk_dmleft
edk_dmlloop
	move.w	d1,-(sp)
	bsr.s	edk_cpos	 get character position
	bsr	edk_lft1	 move back
	jsr	ed_delch
	move.w	(sp)+,d1
edk_dmleft
	dbra	d1,edk_dmlloop
	rts

edk_dtoright
	bsr.s	edk_endl	find number of characters to end of line
	bra.s	edk_dmright
edk_dwright
	bsr.s	edk_lwright	locate word to left
	bra.s	edk_dmright
edk_dmrloop
	move.w	d1,-(sp)	save count
	jsr	ed_delch
	move.w	(sp)+,d1
edk_dmright
	dbra	d1,edk_dmrloop
	rts

edk_strtl
	bsr.s	edk_csrch	set registers for search
	move.l	bv_bfbas(a6),a2
	moveq	#0,d2
edk_stloop
	cmp.l	bv_bfp(a6),a2
	bge.s	edk_setl	set position for left
	move.b	(a6,a2.l),d2
	addq.l	#1,a2
	cmp.b	#k.digit,(a1,d2.w)    digit?
	beq.s	edk_stloop
	subq.l	#1,a2
	bra.s	edk_setl

edk_lwleft
	bsr.s	edk_csrch	set registers for search
	move.l	bv_bfbas(a6),a2
	add.w	d1,a2		starting here
edk_llld
	cmp.l	bv_bfbas(a6),a2
	ble.s	edk_setl	set position for left
	subq.l	#1,a2
	move.b	(a6,a2.l),d2	look back
	cmp.b	#k.other,(a1,d2.w) non letter digit?
	beq.s	edk_llld	... yes

edk_llnld
	cmp.l	bv_bfbas(a6),a2
	ble.s	edk_setl	set position for left
	subq.l	#1,a2
	move.b	(a6,a2.l),d2	look back
	cmp.b	#k.other,(a1,d2.w) non letter digit?
	bne.s	edk_llnld	 ... no

	addq.l	#1,a2

edk_setl
	sub.l	bv_bfbas(a6),a2
	sub.w	a2,d1		distance to move back
	bge.s	edk_slrts
	moveq	#0,d1		do not move
edk_slrts
	rts
edk_csrch
	bsr	edk_cpos	cursor position
	lea	cv_cttab,a1	character type table
	moveq	#0,d2
	rts

edk_endl
	bsr.s	edk_csrch	set registers for search
	move.l	bv_bfp(a6),a2	where to go
	bra.s	edk_setr

edk_lwright
	bsr.s	edk_csrch	set registers for search
	move.l	bv_bfbas(a6),a2
	add.w	d1,a2		starting here

edk_lrnld
	cmp.l	bv_bfp(a6),a2
	bge.s	edk_setr	set position for right
	move.b	(a6,a2.l),d2	look forward
	addq.l	#1,a2
	cmp.b	#k.other,(a1,d2.w) non letter digit?
	bne.s	edk_lrnld	 ... no

edk_lrld
	cmp.l	bv_bfp(a6),a2
	bge.s	edk_setr	set position for right
	move.b	(a6,a2.l),d2	look forward
	addq.l	#1,a2
	cmp.b	#k.other,(a1,d2.w) non letter digit?
	beq.s	edk_lrld	... yes

	subq.l	#1,a2

edk_setr
	sub.l	bv_bfbas(a6),a2
	sub.w	a2,d1		distance to move back
	neg.w	d1		... forward
	bge.s	edk_srrts
	moveq	#0,d0		do not move
edk_srrts
	rts


edk_mode
	not.b	ed_overw(a5)	change overwrite mode
edk_help
edk_rts
	rts

edk_stuff
	bsr	edk_strtl
	move.l	bv_bfp(a6),d2	  end of buffer
	add.l	bv_bfbas(a6),a2   start of chars
	cmp.b	#' ',(a6,a2.l)	  starts with space?
	bne.s	edks_set
	addq.l	#1,a2		  skip space
edks_set
	sub.l	a2,d2
	ble.s	edk_rts
	lea	(a6,a2.l),a1
	jmp	ut_stufc	  stuff



	page
*
* Keystroke actions
*
k.sttmd equ	$ff00		negative byte to start mod
k.getln equ	$8000		or get line
k.endmd equ	$0100		positive to end
*
key_act
	dc.w	k.enter+k.endmd,ed_enter-*-2
	dc.w	k.esc,ed_esc-*-2
	dc.w	k.left,edk_left-*-2
	dc.w	k.right,edk_right-*-2
	dc.w	k.wleft+k.getln,edk_wleft-*-2
	dc.w	k.wright+k.getln,edk_wright-*-2
	dc.w	k.dleft+k.sttmd,edk_dleft-*-2
	dc.w	k.dright+k.sttmd,edk_dright-*-2
	dc.w	k.dwleft+k.sttmd,edk_dwleft-*-2
	dc.w	k.dwright+k.sttmd,edk_dwright-*-2
	dc.w	k.tolft+k.getln,edk_toleft-*-2
	dc.w	k.torgt+k.getln,edk_toright-*-2
	dc.w	k.dtolft+k.sttmd,edk_dtoleft-*-2
	dc.w	k.dtorgt+k.sttmd,edk_dtoright-*-2
	dc.w	k.dline,ed_dline-*-2
	dc.w	k.up,edk_up-*-2
	dc.w	k.scrup+k.endmd,edk_scru-*-2
	dc.w	k.pup+k.endmd,ed_wprev-*-2
	dc.w	k.down,edk_down-*-2
	dc.w	k.scrdn+k.endmd,edk_scrd-*-2
	dc.w	k.pdown+k.endmd,ed_wnext-*-2
	dc.w	k.tab,edk_tab-*-2
	dc.w	k.btab,edk_btab-*-2
	dc.w	k.mode,edk_mode-*-2
	dc.w	k.stuff+k.getln,edk_stuff-*-2
	dc.w	0

	end
