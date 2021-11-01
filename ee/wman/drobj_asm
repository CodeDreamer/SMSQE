* Draw menu object or loose menu item  V1.06	 1986	Tony Tebby   QJUMP
* 2003-01-24	1.02	draws sprites according to item status (wl)
* 2003-05-06	1.03	uses pto..blk to check for sprite block (wl)
* 2003-05-20	1.04	calls pv_fsprt before checking on object data (mk)
* 2003-06-15	1.05	yet more bugfixes for statuts items (wl)
* 2020-08-13	1.06	index item drawing if item number -ve (AH)

	section wman
*
	xdef	wm_drmit
	xdef	wm_drmob
	xdef	wm_drlit
	xdef	wm_getrealobject
*
	xref	wm_trap3
*
	include dev8_keys_k
	include dev8_keys_qdos_io
	include dev8_keys_qdos_pt
	include dev8_keys_qdos_sms
	include dev8_keys_con
	include dev8_keys_sys
	include dev8_keys_wwork
	include dev8_keys_wstatus
*
* draw loose menu item
*
*	d0  r	error return
*	d3 c  p 0 byte draw, -byte check redraw
*	d4   sp hit area size
*	d5   sp hit area origin
*	a0  r	channel ID of window
*	a1 c  p pointer to window status area
*	a3 c  p pointer to loose item definition
*	a4 c  p pointer to window definition
*
*		all other registers preserved
*
reglist  reg	d1/d2/d3/d4/d5/d6/d7/a1/a2/a4
fill.blk equ	8			8 bytes for fill block
stk_d3b  equ	$13
stk_stat equ	$1c
*
wm_drlit
	movem.l reglist,-(sp)		save registers
	subq.l	#fill.blk,sp		make room for FILL block
*
	move.w	wwl_item(a3),d1 	get item number
	bclr	#0,ws_litem(a1,d1.w)	... redraw status
	bne.s	wdl_satt		... yes
	moveq	#0,d0			set ok
	tst.b	d3			draw all?
	blt.s	wdo_exit		... no
*
wdl_satt
	lea	wwa_attr+ww_lattr(a4),a4 attributes
;	 tst.b	 ws_litem(a1,d1.w)	 status? (old)
*
* now check whether this item is the current item
* and set d6 accordingly
	moveq	#0,d6			flag - not current item
	cmp.w	ws_citem(a1),D1 	is this the current item?
	bne.s	wdl_ncur		...no
	moveq	#-1,d6			yes, show it
wdl_ncur
	move.b	ws_litem(a1,d1.w),d6	status?
	bgt.s	wdl_ssiz		... unvailablle
	add.w	#wwa.alen,a4
	beq.s	wdl_ssiz		... available
	add.w	#wwa.alen,a4		... selected
*
wdl_ssiz
	move.l	wwl_xsiz(a3),d4 	set hit area size
	move.l	wwl_xorg(a3),d5 	... and origin
	lea	wwl_xjst(a3),a2 	set pointer to object definition
	bra.s	wdo_do			do it
	page
*
* draw menu item
*
*	d0  r	error return
*	d4   sp hit area size
*	d5   sp hit area origin
*	a0 c  p channel ID of window
*	a1 c  p window status area
*	a2 c  p pointer to object
*	a3 c  p pointer to menu sub-window
*
*		all other registers preserved
*
wm_drmit
	movem.l reglist,-(sp)		save registers
	subq.l	#fill.blk,sp		make room for fill block
*
	move.l	wsp_xorg(a1),d0 	origin of window
	move.l	ws_cihxs(a1),d4 	hit area size
	move.l	ws_cihxo(a1),d5 	hit area origin
	sub.l	d0,d5			relative to window
	moveq	#1,d3			draw this
*
	bra.s	wdo_menu
*
* draw menu object
*
*	d0  r	error return
*	d3 c  p +byte draw, -byte check status
*	d4 c  p hit area size
*	d5 c  p hit area origin
*	a0 c  p channel ID of window
*	a2 c  p pointer to object
*	a3 c  p pointer to menu sub-window
*
*		all other registers preserved
*
wm_drmob
	movem.l reglist,-(sp)		save registers
	subq.l	#fill.blk,sp		make room for fill block
*
wdo_menu
	move.w	wwm_item(a2),d1 	get item number
	bpl.s	wdo_mit 		-ve is index item (AH)
	lea	wwa_iiat(a3),a4 	index attributes - unav (AH)
	bra.s	wdo_do			draw index item instead (AH)
wdo_mit
	move.l	wwa_mstt(a3),a1 	status list
	move.b	wss_item(a1,d1.w),d2	status
	bclr	#0,d2			redraw?
	bne.s	wdo_satt		... yes
	moveq	#0,d0
	tst.b	d3			conditional?
	blt.s	wdo_exit		... no
wdo_satt
	lea	wwa_attr+wwa_iatt(a3),a4 attributes
	moveq	#0,d6			flag: not current item
	move.b	d2,d6			status?
	bgt.s	wdo_do			... unvailablle
	add.w	#wwa.alen,a4
	beq.s	wdo_do			... available
	add.w	#wwa.alen,a4		... selected
*
wdo_do
	bsr.s	wm_drobj		draw object
wdo_exit
	addq.l	#fill.blk,sp
	tst.l	d0
	movem.l (sp)+,reglist		restore registers
	rts
	page
*
* justify and draw menu object
*
*	d4	hit area size 
*	d5	hit area origin
*	a2	pointer to object definition (wwm_xjst)
*	a4	pointer to item attributes
*
wm_drobj
	moveq	#16,d0
	bclr	d0,d4			even pixel position
	bclr	d0,d5
*
	moveq	#1,d1
	moveq	#iow.sova,d0		set drawing mode
	bsr.l	wm_trap3

	moveq	#iow.spix,d0		move to origin (to prevent scroll up)
	moveq	#0,d1
	moveq	#0,d2
	bsr.l	wm_trap3
*
	move.w	(a4)+,d1		set block colour
	lea	fill.blk+4(sp),a1	and size
	move.l	d5,-(a1)
	move.l	d4,-(a1)
	moveq	#iow.blok,d0		fill block
	bsr.l	wm_trap3
*
	move.l	wwm_pobj(a2),d0 	any object?
	beq.l	wdo_rts
	move.b	wwm_type(a2),d7 	get type of object
	bgt.l	wdo_obj 		... not text
	not.b	d7			underscore position
	ext.w	d7
	moveq	#-1,d1			enquire text size
	moveq	#-1,d2
	moveq	#iow.ssiz,d0
	bsr.l	wm_trap3
*
	move.l	d1,d2			y spacing = height
	swap	d2			x spacing

	move.l	d4,d0			set max length
	clr.w	d0    
	swap	d0
	clr.w	d3
	move.b	(a2),d3 		justification rule
	bge.s	wdo_stmax
	neg.b	d3
wdo_stmax
	sub.w	d3,d0			actual space available
	ble.l	wdo_ok			... none
*
	divu	d2,d0			max chars
*
	move.l	wwm_pobj(a2),a1 	pointer to string length
	move.w	(a1)+,d3
	cmp.w	d0,d3			ok?
	bgt.s	wdo_fnl 		... no, too long
	move.w	d3,d0			... yes
wdo_fnl
	clr.w	d3			look for 'nl'
*
wdo_fnloop
	cmp.w	d0,d3			at end?
	beq.s	wdo_stln		... yes
	cmp.b	#k.enter,(a1)+		nl?
	beq.s	wdo_nlbs		... no
	addq.w	#1,d3			one more character
	bra.s	wdo_fnloop
*
wdo_nlbs
	subq.l	#1,a1			ignore nl
wdo_stln
	sub.w	d3,a1			backspace

	cmp.w	d7,d3			underscore within string?
	bls.s	wdo_nus 		... no
	cmp.b	#' ',(a1,d7.w)		any character?
	bls.s	wdo_nus 		... no
	mulu	d2,d7			underscore pixel position
	bra.s	wdo_slen
wdo_nus
	moveq	#-1,d7

wdo_slen
	mulu	d3,d2			set length in pixels
	swap	d2
	move.w	d1,d2			size of string all in one register
	moveq	#0,d1			origin of string
	bsr.l	wdo_just
	move.l	d1,d4			... keep it
	move.l	a1,a2			and string pointer

	move.w	(a4),d1 		set ink colour
	moveq	#iow.sink,d0
	bsr.l	wm_trap3

	tst.w	d7			underscore?
	bmi.s	wdo_sstrg		... no

	move.w	d4,d2			set cursor
	addq.w	#1,d2			down a bit
	move.l	d4,d1
	swap	d1
	add.w	d7,d1			underscore the right char
	moveq	#iow.spix,d0
	bsr.l	wm_trap3

	moveq	#'_',d1
	moveq	#iob.sbyt,d0		underscore
	bsr.l	wm_trap3

wdo_sstrg
	move.w	d4,d2			set cursor
	move.l	d4,d1
	swap	d1
	moveq	#iow.spix,d0
	bsr.l	wm_trap3

	move.w	d3,d2
	move.l	a2,a1			pointer to string
	moveq	#iob.smul,d0		write text
	bsr.l	wm_trap3
	bra	wdo_rts
*
* do sprite, blob or pattern
*
* (what I need here is an indication of the status of the item)
* use D6
; D6.b	= -ive: selected
;	= 0	available
;	= +ive	unavailable
; D6.L	= -ive this is a loose menu item and it is the current item

wdo_obj
	move.l	wwm_pobj(a2),a1 	set object pointer
	bsr	wm_getrealobject
	subq.b	#4,d7			is it sprite?
	beq.s	wdo_blob		... no, blob
; new wl
	subq.b	#2,d7			is it pattern?
	beq.s	wdo_patt		... yes
; check whether we have an extended sprite
; this must be a chained sprite (which has the same screen mode)
; and where the chainage is as follows:
; sprite to be used if available item (can point back to 1st sprite) 0
; sprite to be used if current item available			     4
; sprite to be used if selected item				     8
; sprite to be used if current item, selected			     12
; sprite to be used if unavailable item 			     16
; if any is missing, it is replaced by the first sprite
wdo_new
	move.b	pto_ctrl(a1),d0 	; get control bits
	btst	#pto..blk,d0		; is it an extended sprite?
	beq.s	wdo_old 		; ... no
	move.l	pto_blk(a1),d0		; pointer to sprite pointer block
	beq.s	wdo_old 		; ... but there is none!
	add.l	#pto_blk,d0		; absolute pointer
	add.l	d0,a1			; point to that block now
	move.l	(a1),d0 		; available item pointer
	tst.b	d6			; item status
	bgt.s	unav			; ... item is unavailable
	beq.s	avail			; ... item is available
sel	tst.l	8(a1)			; is there a selected pointer?
	beq.s	wdo_spt 		; ... no, use available
	move.l	8(a1),d0		; pointer to sprite, selected
	addq.l	#8,a1			; offset

avail	tst.l	d6			; current item?
	bge.s	wdo_spt 		; ... no, normal
	tst.l	4(a1)			; ... yes is there a ptr to curr item sprite
	beq.s	wdo_spt 		;    ... no , use normal
	addq.l	#4,a1			; point to it
	move.l	(a1),d0 		; offset
	bra.s	wdo_spt

unav	tst.l	16(a1)			; is there a pointer to unavailable?
	beq.s	wdo_spt 		; no
	move.l	16(a1),d0		; yes get it
	add.l	#16,d0			; and make more absolute
wdo_spt add.l	d0,a1			; point to correct sprite now
	bsr.s	wm_getrealobject

wdo_old
	bsr.s	wdo_ojst		justify arbitrary object
	moveq	#iop.wspt,d0		set sprite
	bra.s	wdo_drop		and drop object
*
wdo_blob
	move.l	wwa_patt-wwa_ink(a4),-(sp) set pointer to pattern
	bra.s	wdo_dblb
*
wdo_patt
	move.l	a1,-(sp)		set pointer to pattern
	move.l	wwa_blob-wwa_ink(a4),a1 and to blob
*
wdo_dblb
	bsr.s	wdo_ojst		justify arbitrary object
	move.l	(sp)+,a2		set pattern
	moveq	#iop.wblb,d0		write blob
wdo_drop
	bsr.l	wm_trap3
wdo_ok
	moveq	#0,d0			only get to here if no error!!
wdo_rts
	rts
	page
*
* justify x and y
*
* positive, object at hit origin + justification + object origin
* zero, object at hit origin + half (hit size - object size) + object origin
* negative, object at hit origin + hit size - object size + justification + ...
*
*	d1 cr	origin of object / object position
*	d2 c s	size of object
*	d4 c s	size of hit area
*	d5 c  p origin of hit area 
*	a1 c  p pointer to object
*	a2 c u	pointer to justification
*
wdo_ojst
	move.l	pto_org(a1),d1		set object origin
	move.l	pto_size(a1),d2 	set object size
*
wdo_just
	sub.l	d2,d4			hit size - object size
	add.l	d5,d1			hit origin + object origin
	bsr.s	wdo_jst1		justify one
	nop				and the other
wdo_jst1
	swap	d1			do other half
	swap	d4
	move.b	(a2)+,d0		justification rule
	ext.w	d0
	bgt.s	wdo_ajust		... just add the justificaion
	blt.s	wdo_asize		... add the size difference
	lsr.w	#1,d4			... add half the size difference
wdo_asize
	add.w	d4,d1			add size difference
wdo_ajust
	add.w	d0,d1			add justification
	rts

*
* Get actually used sprite/blob/pattern definition
*
*	d0   s
*	a1 cr	ptr to original / used sprite definition
*
wm_getrealobject
	movem.l d1-d2/a0/a3,-(sp)
	moveq	#sms.info,d0
	trap	#1
	move.l	sys_clnk(a0),a3 	CON linkage
	move.l	pt_vecs(a3),a0		CON vectors
	jsr	pv_fspr(a0)		get actually used sprite definition
	movem.l (sp)+,d1-d2/a0/a3
	rts

	end
