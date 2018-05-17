; Standard menu hit routine   V1.05     1986  Tony Tebby   QJUMP

	section wman

	xdef	wm_mhit

	xref	wm_entry
	xref	wm_drbdr
	xref	wm_drins
	xref	wm_clrci
	xref	wm_mdraw
	xref	wm_scrow
	xref	wm_sccol
	xref	wm_msect
	xref	wm_chitd

	include dev8_mac_assert
	include dev8_keys_k
	include dev8_keys_qdos_io
	include dev8_keys_qdos_pt
	include dev8_keys_wwork
	include dev8_keys_wstatus

;	d0  r	error return
;	d1 c  p pointer position
;	d2 c  p number to skip (LSW/MSB), upper cased keystroke (LSB)
;	d4 c sp window event number (0 or DO) / hit area size
;	d3  rs	window section / returned -1
;	d5   sp hit area origin (relative to sub-window)
;	d7   sp sub window origin
;	a0 c  p channel ID of window
;	a1   sp pointer to window status area
;	a3 c  p pointer to sub-window definition
;	a4 c  p pointer to window working definition

;		all other registers preserved

found	 equ	-1

reglist  reg	d1/d2/d4-d7/a1/a2/a3
stk_pos  equ	$0
stk_kstr equ	$4
stk_ev	 equ	$8

wm_mhit
	movem.l reglist,-(sp)		 ; save registers

	move.l	ww_wstat(a4),a1 	 ; set pointer to window status area
	move.l	wsp_xorg(a1),d7 	 ; sub-window origin

	jsr	wm_msect		 ; check menu section
	beq.s	wmh_key 		 ; not pan/scroll item
	tst.b	d4			 ; event?
	beq.l	wmh_nohit		 ; ... no
	move.w	d0,d2			 ; item number
	moveq	#-1,d3			 ; (not on bar)
	move.l	wwa_ctrl(a3),d0
	bsr.l	wmh_call		 ; call control routine
	bra.l	wmh_exit		 ; done

;	   check for matching keystroke

wmh_key
	move.w	d2,d0			 ; key
	beq.l	wmh_nkey		 ; ... none
	addq.w	#1,d0			 ; window select?
	beq.l	wmh_nkey		 ; ... yes
	subq.b	#k.cancel+1,d0		 ; hit, do or cancel?
	beq.l	wmh_nkey		 ; ... ignore cancel
	bls.l	wmh_mhit		 ; ... look for hit or do

	moveq	#k.curtp,d0
	and.b	d2,d0
	cmp.b	#k.curs,d0
	beq.l	wmh_nkey		 ; it's a cursor key
wmh_keys
	move.w	d2,d1			 ; selection key to check
	movem.l d2/a1,-(sp)
	lea	wmk_dorw(pc),a1
	bsr.l	wm_scrow		 ; do all rows
	movem.l (sp)+,d2/a1
	addq.l	#-found,d0		 ; found?
	beq.s	wmh_mkhit
	sub.w	d2,d1			 ; any found at all?
	beq.l	wmh_nohit		 ; ... none found
wmh_rloop
	add.w	d1,d2			 ; assume we've gone round again
	bcs.s	wmh_rloop		 ; ... not run out yet
	sub.w	d1,d2			 ; ... overrun, go back
	bra.s	wmh_keys

wmh_mkhit
	move.b	#k.hit,wsp_kprs(a1)	... found, fake a hit on it
	add.l	d7,d5			make position absolute
	move.l	d5,d1			now set pointer
	add.l	d4,d1
	sub.l	#ww.ptoff,d1		just in
	move.l	d1,ws_ptpos(a1) 	set ours
	moveq	#iop.sptr,d0		... and it
	moveq	#iops.abs,d2		absolute
	move.l	d3,d6
	moveq	#-1,d3			wait forever
	trap	#3
	bsr.l	wm_clrci		remove current item
	move.l	d6,d3			restore virtual row/column
	bra.l	wmh_hit

;	      do a row

wmk_dorw
	tst.w	d6			genuine row?
	bne.s	wmk_ok			... no
	lea	wmk_doob(pc),a1 	set new action
	bsr.l	wm_sccol		do all columns
	lea	wmk_dorw(pc),a1 	reset our action
	rts
wmk_ok
	moveq	#0,d0
	rts

;	   do an object

wmk_doob
	tst.w	d6			genuine column?
	bne.s	wmk_ok			... no
	movem.l d3/a1/a2,-(sp)		save regs
	move.l	wwa_rowl(a3),a1 	get row list pointer
	clr.w	d3
	swap	d3
	lsl.l	#3,d3			index rowlist
	add.l	d3,a1
	move.l	(a1)+,a2		pointer to row
	move.l	(sp),d3
	mulu	#wwm.olen,d3		pointer to object
	add.l	d3,a2
	cmp.l	(a1),a2 		off end?
	bge.s	wmk_noob		... yes
	cmp.b	wwm_skey(a2),d1 	selection keystroke
	bne.s	wmk_noob		... no
	sub.w	#$100,d1		skip this one?
	bcc.s	wmk_noob		... yes
	moveq	#found,d0		... no
	bra.s	wmk_exob
wmk_noob
	moveq	#0,d0
wmk_exob
	movem.l (sp)+,d3/a1/a2	  restore regs
	rts
	page

;	    hit or do

wmh_mhit
	tst.w	ws_citem(a1)		 ; current item?
	blt.s	wmh_lhit		 ; ... no look for one
	cmp.b	#k.hit,wsp_kprs(a1)	 ; ... yes, use stroke if DO
	bls.s	wmh_lhit
	move.b	wsp_kstr(a1),wsp_kprs(a1)
	bra.s	wmh_lhit

;	    no keystroke, check if still in new item hit area

wmh_nkey
	tst.w	ws_citem(a1)		 ; current item?
	bge.l	wmh_nohit		 ; yes, do nothing
	moveq	#pt.keyup+pt.move,d0	 ; move and up bits
	and.b	wsp_peve(a1),d0
	eor.b	#pt.move,d0
	beq.s	wmh_lhit		 ; ok
	clr.b	wsp_kprs(a1)		 ; either key up or no move, do nought

wmh_lhit
	sub.l	d7,d1			 ; pointer relative to window
	lea	wmh_dorw(pc),a1
	bsr.l	wm_scrow		 ; do all rows
	move.l	ww_wstat(a4),a1
	addq.l	#-found,d0		 ; found?
	bne.l	wmh_nohit		 ; ... none hit
	add.l	d7,d5			 ; ... a hit, make position absolute
	bra.s	wmh_hit

;	   do a row

wmh_dorw
	tst.w	d6			 ; genuine row?
	bne.s	wmh_ok			 ; ... no
	lea	wmh_doob(pc),a1 	 ; set new action
	bsr.l	wm_sccol		 ; do all columns
	lea	wmh_dorw(pc),a1 	 ; reset our action
	rts
wmh_ok
	moveq	#0,d0
	rts

;	   do an object

wmh_doob
	tst.w	d6			 ; genuine column?
	bne.s	wmh_ok			 ; ... no
	movem.l d3/d4/d5/a1/a2,-(sp)	 ; save regs
	move.l	wwa_rowl(a3),a1 	 ; get row list pointer
	swap	d4			 ; get x,y
	swap	d5
	clr.w	d3
	swap	d3
	lsl.l	#3,d3			 ; index rowlist
	add.l	d3,a1
	move.l	(a1)+,a2		pointer to row
	move.l	(sp),d3
	mulu	#wwm.olen,d3		pointer to object
	add.l	d3,a2
	cmp.l	(a1),a2 		off end?
	bge.s	wmh_noob		... yes
	bsr.l	wm_chitd		check hit area
	bne.s	wmh_noob
	moveq	#found,d0
	bra.s	wmh_exob
wmh_noob
	moveq	#0,d0
wmh_exob
	movem.l (sp)+,d3/d4/d5/a1/a2	restore regs
	rts
	page

;	   The menu item has been found, the hit area set, now write the border

wmh_hit
	move.l	d3,d6			save row, column
	movem.l d4/d5,ws_cihit(a1)	... reset the area
	and.l	#$0000ffff,d3
	lsl.l	#3,d3			index row list
	move.l	wwa_rowl(a3),a2
	move.l	(a2,d3.l),a2		index row
	move.l	d6,d3
	swap	d3
	mulu	#wwm.olen,d3		find object
	add.l	d3,a2

	move.l	wwa_curw+wwa_iatt(a3),ws_cibrw(a1) set border width and colour
	bsr.l	wm_drbdr
	move.w	wwa_papr+wwa_watt(a3),ws_cipap(a1) ; and set old colour
	move.w	wwm_item(a2),d2 	 ; item number
	move.w	d2,ws_citem(a1) 	 ; ... keep it
	move.b	wsp_kprs(a1),d1 	 ; get keypress
	move.l	stk_ev(sp),d4		 ; and retrieve event for action later
	move.l	wwa_mstt(a3),a1 	 ; pointer to status block
	move.b	(a1,d2.w),d0		 ; available?
	bgt.s	wmh_nohit		 ; ... no
	subq.b	#k.hit,d1		 ; hit pressed?
	bhi.s	wmh_ssel		 ; ... DO, set select
	bne.s	wmh_nohit		 ; ... no
	tst.b	d0			 ; available?
	beq.s	wmh_ssel		 ; ... yes, set select
	move.b	#wsi.mkav,(a1,d2.w)	 ; ... no, make available
	bra.s	wmh_drmit
wmh_ssel
	move.b	#wsi.mksl,(a1,d2.w)	 ; make selected

wmh_drmit
	moveq	#-1,d3			 ; partial redraw
	bsr.l	wm_mdraw		 ; draw menu items
	bclr	#wsi..chg,(a1,d2.w)	 ; unset draw flag

	move.l	d6,d1			 ; set row, column
	move.l	wwm_pact(a2),d0 	 ; action routine
	bsr.s	wmh_call		 ; do action
	bne.s	wmh_exit		 ; ... oops

	tst.b	d4			 ; check window event
	beq.s	wmh_exit
	move.l	ww_wstat(a4),a1 	 ; set window status area
	bset	d4,wsp_weve(a1) 	 ; set window event

wmh_nohit
	moveq	#0,d0
wmh_exit
	moveq	#-1,d3			 ; set timeout
	tst.l	d0			 ; test error
	movem.l (sp)+,reglist
	rts

;	  call not using registers

wmh_call
	beq.s	wmh_rts
	clr.l	stk_ev+4(sp)		 ; event has been handled
	lea	wm_entry(pc),a2 	 ; set entry vector
	move.l	d0,-(sp)		!!!!!!!!!!!!!!!!!!!!
wmh_rts
	rts				!!!!!!!!!!!!!!!!!!!!
	end
