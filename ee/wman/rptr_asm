* Read pointer	 V1.10				 1986	Tony Tebby   QJUMP
* 2003-01-24	1.07	calls sprite drawing routine also when current item
*			status change (wl)
* 2003-05-15	1.08	don't redraw sprites with no sprite block (wl)
* 2003-05-20	1.09	Uses pv_fspr before trying to check sprite data (mk)
* 2004-01-26	1.10	Even better check to distinguish between loose menu & appsub items (wl)
* 2004-10-10	1.11	Removed some redundant code at wc_ldraw (mk)
*
	section wman
*
	xdef	wm_rptr
	xdef	wm_rptrt
*
	xref	wm_entry
	xref	wm_upcas
	xref	wm_chit
	xref	wm_drbdr
	xref	wm_clrci
	xref	wm_ldraw
	xref	wm_htbar
	xref	wm_drlit
	xref	wm_getrealobject
*
	include dev8_keys_k
	include dev8_keys_err
	include dev8_keys_qdos_sms
	include dev8_keys_qdos_io
	include dev8_keys_qdos_pt
	include dev8_keys_sys
	include dev8_keys_sbasic
	include dev8_keys_wwork
	include dev8_keys_wstatus
*
* read pointer
*
*	d0  r	error return
*	d2 c  p events on which to return (only for rd_ptrt, events in LSByte)
*	d3 c  p timeout (in LSW, for rd_ptrt)
*	d4   sp event number
*	d5   sp next return condition
*	a0  r	channel ID of window
*	a1   sp pointer to window status area
*	a4 c  p pointer to working definition
*
*		all other registers preserved
*
bv_brk	equ	$8f
*
reglist  reg	d1-d7/a1/a2/a3/a4
stk_d7 equ	6*4
*
rptr_do
	moveq	#iop.rptr,d0		read pointer
	move.l	ws_ptpos(a1),d1 	get old pointer position
	addq.l	#ws_point,a1		set pointer record pointer
	trap	#3
	subq.l	#ws_point,a1		restore record pointer
	move.l	d1,ws_ptpos(a1) 	save pointer position
	rts
*
wrp_outwn
	bsr    wm_clcit 		 ; clear current area
	move.l #1<<31+pt.inwin,d5	 ; ignore key on back in window
	clr.w  d6
	bra.s  read_pointer
*
wm_rptrt
	movem.l reglist,-(sp)		 ; save registers
	moveq	#0,d7			 ; no event checks
	move.b	d2,d7
	ror.l	#8,d7			 ; job events requested
	move.w	d3,d7			 ; user requested wait
	moveq	#-1,d6			 ; new version flag
	clr.w	d6			 ; retry flag
	bra.s	wrp_rptr

wm_rptr
	movem.l reglist,-(sp)		 ; save registers
	moveq	#0,d7			 ; no event checks
	not.w	d7			 ; wait forever
	moveq	#0,d6			 ; preset retry / rptr flag

wrp_rptr
	move.l	ww_wstat(a4),a1 	 ; set window status area pointer
	clr.b	wsp_jeve(a1)		 ; in case there is something left
	move.l	ww_chid(a4),a0		 ; set channel id
	moveq	#pt.outwn+pt.inwin,d5	 ; get in / out window status
*
* main loop reading pointer
*
read_pointer
	moveq	#-1,d3
	move.w	d7,d3			 ; call wait
	bra.s	wrp_srev
wrp_rpw
	moveq	#-1,d3
	move.w	d7,d3			 ; call wait
wrp_rpd3
	tst.w	ws_citem(a1)		 ; current item set?
	blt.s	wrp_retry		 ; ... no
	clr.w	d6			 ; ... say it is set
	bra.s	wrp_srev

wrp_retry
	tas	d6			 ; already retried?
	bne.s	wrp_srev		 ; ... yes

wrp_srin
	moveq	#pt.inwin,d5		 ; return when in window

wrp_srev
	tst.b	wsp_jeve(a1)		 ; any job events
	bne.l	wrp_ok			 ; ... yes

	move.l	d7,d2			 ; job events required
	move.w	d5,d2			 ; set our return events
	bsr.s	rptr_do 		 ; READ POINTER

	cmp.l	#err.nc,d0
	blt.l	wrp_exit		 ; ... oops
	bgt.s	wrp_act 		 ; ... good
					 ; ok, here pointer call returned err.nc
	tst.l	d6			 ; new version?
	bpl.s	wrp_basic

	tst.l	d3			 ; external timer?
	bmi.l	wrp_exit		 ; ... yes

wrp_basic
	movem.l d0/d1/a0,-(sp)
	moveq	#sms.info,d0		 ; ... not complete, check if basic
	trap	#do.sms2
	tst.l	d1			 ; job 0?
	beq.s	wrp_brk
	cmp.l	sys_tpab(a0),a6 	 ; a6 in tpa?
	blt.s	wrp_nbas		 ; ... no
	cmp.l	sys_rpab(a0),a6
	bgt.s	wrp_nbas
	move.l	a6,d1
	lsr.l	#1,d1
	bcs.s	wrp_nbas		 ; odd address
	cmp.l	#sb.flag,sb_flag(a6)	 ; SBASIC?
	bne.s	wrp_nbas
wrp_brk
	tst.b	bv_brk(a6)		 ; ... break?
	smi	d1
	tst.b	d1			 ; Z=break
wrp_nbas
	movem.l (sp)+,d0/d1/a0
	beq.l	wrp_exit		 ; give up

wrp_act
	btst	#pt..inwn,wsp_peve(a1)	 ; in window?
	beq	wrp_outwn		 ; ... no

	cmp.b	#k.wake,wsp_kstr(a1)	 ; wake?
	beq.s	wrp_norm		 ; ... yes, normal handling
	tst.l	d5			 ; ignore key?
	blt.s	wrp_retry		 ; ... yes
wrp_norm
	moveq	#pt.kystk+pt.kyprs+pt.move+pt.outwn,d5 ; set normal return
*
	tst.w	d6			 ; was it sub-window select?
	blt.l	wrp_swsel		 ; ... yes
	move.b	wsp_kstr(a1),d2 	 ; keystroke
	bsr.l	wm_upcas		 ; uppercase keystroke
	cmp.b	#pt..do,d4		 ; event?
	blo.s	wrp_citem		 ; ... no
	beq.s	wrp_ekey
*
* process event keystroke
*
	bsr	wm_clcit		 ; clear current item
wrp_ekey
	move.l	ww_plitm(a4),d0 	 ; pointer to loose item list
	beq.l	wrp_ewin		 ; ... none
	move.l	d0,a3
wrp_leve
	tst.w	(a3)			 ; any more items?
	blt.s	wrp_nodo		 ; ... no
	move.w	wwl_item(a3),d3 	 ; set item number
	cmp.b	wwl_skey(a3),d2 	 ; matching keystroke?
	beq.s	wrp_ledo		 ; ... yes, check if do
	add.w	#wwl.elen,a3		 ; ... no
	bra.s	wrp_leve		 ; try next

wrp_nodo
	cmp.b	#pt..do,d4		 ; no event item was it DO?
	beq.s	wrp_citem		 ; ... yes
	bra.l	wrp_ewin		 ; ... no

wrp_ledo
	cmp.b	#pt..do,d4		 ; DO?
	bhi.l	wrp_lstat		 ; ... no, toggle stat, do action
	tst.w	ws_citem(a1)		 ; is there a current item?
	bge.s	wrp_chit		 ; yes, do that instead
	tst.w	wsp_swnr(a1)		 ; in application sw?
	blt.l	wrp_lstat		 ; ... no, toggle stat, do action
*
wrp_citem
	tst.w	ws_citem(a1)		 ; is there a current item?
	blt.s	wrp_ckey		 ; ... no, check key
wrp_chit
	lea	ws_cihit(a1),a2 	 ; check if in current item hit area
	bsr.l	wm_chit 		 ; check hit area
	beq.s	wrp_ckin		 ; ... same hit area, check key
wrp_clear
	bsr	wm_clcit		 ; clear current item
	bra.s	wrp_ckey		 ; ... ok, check key
*
* check for loose menu item keystroke
*
wrp_ckin
	tst.b	d2			 ; any key?
	beq.l	wrp_swind		 ; no, same area, do sub_window
wrp_ckey
	cmp.b	#k.do,d2		; was it no key, hit or do?
	bls.l	wrp_newci		; ... yes, check for new hit area
	move.l	ww_plitm(a4),d0 	; pointer to loose item list
	beq.s	wrp_ssel		; ... none, check sub-windows for key
	move.l	d0,a3
wrp_lkey
	tst.w	(a3)			; any more items?
	blt.s	wrp_ssel		; ... no
	move.w	wwl_item(a3),d3 	; item number
	cmp.b	wwl_skey(a3),d2 	; matching keystroke?
	beq.s	wrp_lkset		; ... yes
	add.w	#wwl.elen,a3		; ... no
	bra.s	wrp_lkey		; try next
*
wrp_lkset
	moveq	#pt.inwin,d5		; force recheck
	bsr	wm_clcit		; clear current item
	bra.l	wrp_lstat		; ... and do loose menu item status
*
* check for sub-window selection keystroke
*
wrp_ssel
	move.l	ww_pappl(a4),d0 	;  pointer to application sub-window list
	beq.l	read_pointer		; ... none, try again
	move.l	d0,a2
	moveq	#-1,d3			; count sub-windows
wrp_skey
	move.l	(a2)+,d0		; any more sub-windows?
	ble.l	wrp_swind		; ... no
	addq.w	#1,d3			; next
	move.l	d0,a3			; pointer to sub-window
	cmp.b	wwa_skey(a3),d2 	; matching keystroke?
	bne.s	wrp_skey		; ... no
*
* sub-window has been selected
*
	move.w	#-1,d6			 ; set selected flag
	cmp.w	wsp_swnr(a1),d3 	 ; current sub-window?
	bne.s	wrp_sswind		 ; ... no

	move.l	ws_ptpos(a1),d1
	sub.l	ww_xorg(a4),d1		 ; window relative pointer position

	tst.l	wwa_psbc(a3)		 ; pan bar?
	beq.s	wrp_ckss		 ; ... no, check scroll
	move.l	wwa_part(a3),d0 	 ; controlled?
	beq.s	wrp_ckss		 ; ... no
	move.l	d0,a2
	tst.w	(a2)			 ; any sections?
	beq.s	wrp_ckss		 ; ... no
	move.w	wwa_yorg(a3),d1 	 ; ... yes, set y origin
	add.w	wwa_ysiz(a3),d1
	addq.w	#ww.pnbar/2+2,d1	 ; into pan bar
	clr.w	d6			 ; ... not select window!
wrp_ckss
	tst.l	wwa_psbc+wwa.clen(a3)	 ; scroll bar?
	beq.s	wrp_sptr		 ; ... no, set position
	move.l	wwa_part+wwa.clen(a3),d0 ; controlled?
	beq.s	wrp_sptr		 ; ... no
	move.l	d0,a2
	tst.w	(a2)			 ; any sections?
	beq.s	wrp_sptr		 ; ... no
	swap	d1
	move.w	wwa_xorg(a3),d1 	 ; ... yes, set x origin
	add.w	wwa_xsiz(a3),d1
	addq.w	#ww.scbar/2+3,d1	 ; into scroll bar
	swap	d1
	clr.w	d6			 ; not select window
	bra.s	wrp_sptr

wrp_sswind
	move.l	wwa_xsiz(a3),d1
	lsr.l	#1,d1
	bclr	#15,d1			 ; x cannot be odd, can it?
	add.l	wwa_xorg(a3),d1 	 ; to centre
	add.l	#$00040002,d1		 ; right a bit
wrp_sptr
	moveq	#iop.sptr,d0		 ; set pointer
	add.l	ww_xorg(a4),d1
	moveq	#iops.abs,d2		 ; ... absolute
	moveq	#-1,d3
	trap	#3

	bra.l	wrp_srin		 ; carry on on in window

; just selected a sub-window

wrp_swsel
	moveq	#-1,d2			 ; sub-window select
	moveq	#0,d4			 ; no event
	clr.w	d6			 ; not select now
	bsr	wm_clcit		 ; clear current item

* check if in sub_window
*
wrp_swind
	move.w	wsp_swnr(a1),d0 	is pointer in sub-window?
	blt.l	read_pointer		... no
	bra.l	wrp_subw		... yes, call sub-window hit
*
* check for new current item
*
wrp_newci
	move.w	wsp_swnr(a1),d0 	is pointer in sub-window?
	bge.l	wrp_subw		... yes current item in sub-window
*
* pointer out of sub-windows, check loose menu items and pan/scroll bars
*
	move.l	wsp_xpos(a1),d1 	 ; get relative pointer position
	tst.b	wsp_kprs(a1)		 ; any hit/do pressed?
	beq.s	wrp_ckli		 ; ... no, check loose item area
	jsr	wm_htbar		 ; check for hit on bar
	beq.s	wrp_ckli
	moveq	#pt.kystk+pt.move+pt.outwn,d5 ; return on move or keystroke
	move.l	wwa_ctrl(a3),d0 	 ; control routine
	bra.l	wrp_clck

wrp_ckli
	move.l	ww_plitm(a4),d0 	 ; pointer to loose item list
	beq.s	wrp_doev		 ; ... none, do event?
	move.l	d0,a3
wrp_litem
	tst.w	(a3)			 ; any more items?
	bmi.s	wrp_doev		 ; ... no
	move.l	a3,a2
	bsr.l	wm_chit 		 ; hit on loose item
	beq.s	wrp_limark		 ; ... yes
	add.w	#wwl.elen,a3		 ; ... no
	bra.s	wrp_litem		 ; try next

wrp_doev
	cmp.b	#pt..do,d4		 ; do event?
	beq.s	wrp_eset		 ; ... yes
	bra.l	read_pointer		 ; ... no, carry on

wrp_limark
	move.w	wwl_item(a3),d3 	 ; item number
	cmp.b	#wsi.unav,ws_litem(a1,d3.w)
	beq.l	read_pointer		 ; unavailable, try again
*
; here we have:
; A0 = channel ID
; A1 = status area
; A3 = pointer to (loose) item
; A4 = working defn
	move.w	d3,ws_citem(a1) 	; set current item
*
	bsr	wc_ldraw		; redraw this item now
*
	move.l	ww_lattr(a4),ws_cibrw(a1) ; set border attributes
	move.l	wwl_xsiz(a3),ws_cihxs(a1) ; and hit area
	move.l	wwl_xorg(a3),d0
	add.l	wsp_xorg(a1),d0 	; ... absolute
	move.l	d0,ws_cihxo(a1)
	bsr.l	wm_drbdr
	move.w	ww_wattr+wwa_papr(a4),ws_cipap(a1) ; set paper colour
*
* check for HIT in loose menu item
*
	tst.b	d2			keystroke?
	beq.l	read_pointer		... no
wrp_lstat
	lea	ws_litem(a1,d3.w),a2	pointer to loose item status
	tst.b	(a2)			status?
	bgt.l	read_pointer		... unavailable
	beq.s	wrp_lsel		... select it
	cmp.b	#k.do,d2		is it do?
	beq.s	wrp_lact		... yes, just do action
	move.b	#wsi.mkav,(a2)		... set make available
	bra.s	wrp_drlit
wrp_lsel
	move.b	#wsi.mksl,(a2)		... set make selected
wrp_drlit
	moveq	#-1,d3			redraw selectivly
	bsr.l	wm_ldraw
	bclr	#wsi..chg,(a2)		and cancel redraw
wrp_lact
	move.l	wwl_pact(a3),d0 	loose menu item action routine
wrp_clck
	bsr.s	wrp_call		call it
	bne.s	wrp_exit
wrp_echk
	tst.b	d4			event?
	beq.l	wrp_rpw 		... no, done
wrp_eset
	bset	d4,wsp_weve(a1) 	set event
	bra.s	wrp_ok			... done

wrp_ewin    ; event in sub-window?
	move.w	wsp_swnr(a1),d0 	 ; which application sw are we in?
	bmi.s	wrp_eset		 ; none, set event
	cmp.b	#pt..can,d4		 ; cancel?
	bne.s	wrp_eset		 ; no, set event

wrp_subw
	lsl.w	#2,d0			pointer to sub-window in list
	move.l	ww_pappl(a4),a3 	... pointer to list
	move.l	(a3,d0.w),a3		pointer to sub-window
wrp_swht
	moveq	#-1,d3
	move.w	d7,d3			call timeout
	move.l	wwa_hit(a3),d0		pointer to hit routine
	bsr.s	wrp_call
	bne.s	wrp_exit		; oops
	tst.l	d6			; new version?
	bpl.s	wrp_ckev
	tst.l	d3			; d3 deliberately reset?
	bpl.s	wrp_ckev		; ... yes
	move.w	d7,d3			; ... no, restore call timeout
*
* check if job / window event set
*
wrp_ckev
	tst.w	wsp_jeve(a1)		is window / job level event set?
	beq.l	wrp_rpd3		... no, do window event with new timeout
wrp_ok
	moveq	#0,d0			return OK
*
wrp_exit
	tst.l	d0			test condition
	movem.l (sp)+,reglist		restore registers
wrp_rts
	rts
*
*
* call action / hit routine avoiding address registers
*
callreg reg	d5/d7  ; a1 saved separately
callframe equ	2*4+4			(include return address in frame!)
wrp_call
	beq.s	wrp_rts 		... no call
	movem.l callreg,-(sp)
	move.l	stk_d7+callframe(sp),d7
	move.l	a1,-(sp)
	move.b	wsp_jeve(a1),-(sp)
	pea	wrp_cret
	move.l	ws_ptpos(a1),d1 	set pointer position
	lea	wm_entry(pc),a2 	set menu vector
	move.l	d0,-(sp)		pointer to action / hit routine
	rts				do it
wrp_cret
	move	sr,d7			action routines can return ccr NZ but d0=0
	move.b	(sp)+,d5
	move.l	(sp)+,a1
	or.b	d5,wsp_jeve(a1) 	accumulate old and new job events
	move	d7,ccr
	movem.l (sp)+,callreg
	rts

*******
*  redraw "current item" when it no longer is the current item
*  (only for sprites)
*  +
*  clear current item (all item types)
*
*	d0  r	error return from wm_clrci
*	a0 c  p channel ID of window
*	a1 c  p pointer to window status area
*	a4 c  p pointer to working definition
*		all other registers preserved
*
********

clcreg	reg	d1/d3/a2/a3
wm_clcit
	move.w	ws_citem(a1),d0 	; is there a current loose item?
	bmi.s	wci_rts2		; ... no
; now we have to find the item that corresponds to the current item
; by searching through the item list until we find the item whose item
; nbr is that of the current item - and then check that current item is
; a loose item
	movem.l clcreg,-(sp)
	move.l	ww_plitm(a4),a3 	; point to item list
wci_tst cmp.w	#-1,(a3)		; end of list reached?
	beq.s	wci_rts 		; ... yes !!!!
	cmp.w	wwl_item(a3),d0 	; is it THIS loose item?
	beq.s	wci_fnd 		; ... yes
	add.l	#wwl.elen,a3		; ... no, point next loose item in list
	bra.s	wci_tst
wci_fnd 				; a3 now points item
	move.l	ws_ciact(a1),d0 	; current item action routine
	beq.s	wci_tst2		; there is none, do another check
	cmp.l	wwl_pact(a3),d0 	; is it this routine?
	bne.s	wci_rts 		; no, so it is an appsub menu item!
wci_tst2
	move.l	wwl_xsiz(a3),d0 	; loose item hit area
	cmp.l	ws_cihxs(a1),d0 	; same as current item ?
	bne.s	wci_rts 		; no, so this is no loose item

wci_do	move.w	ws_citem(a1),-(sp)
	st	ws_citem(a1)		; fake no current item
	bsr.s	wc_ldraw		; draw it
	move.w	(sp)+,ws_citem(a1)	; get current item back
wci_rts
	movem.l (sp)+,clcreg
wci_rts2
	moveq	#0,d0
	jmp	wm_clrci		; now clear current item

*******
*   force redraw an item  - if it is a sprite
*   and if the sprite contains a sprite control block
*   for the other sprites, there is no need to redraw them
*	d0  r	error return
*	a0 c  p channel ID of window
*	a1   su pointer to status area
*	a3 c  p pointer to loose item definition
*	a4 c  p pointer to working definition
*
*******
regist	reg    d1-d3/a2
stk_act  equ	8
*
wc_ldraw

	cmp.b	#2,wwl_type(a3) 	; is it a sprite?
	bne.s	wc_out			; no, don't redraw
	move.l	wwl_pobj(a3),d0 	; pointer to object
	beq.s	wc_out			; ...none, don't need to redraw
	move.l	a1,-(sp)
	move.l	d0,a1			; point object
	bsr	wm_getrealobject	; handle system sprites et al
	move.b	pto_ctrl(a1),d0 	; get sprite control byte
	move.l	(sp)+,a1
	btst	#pto..blk,d0		; is it a sprite with a control block?
	bne.s	wc_do			; yes
wc_out	moveq	#0,d0			; ... no, do nothing
	rts
wc_do	movem.l regist,-(sp)		; save registers
	lea	ww_xsize(a4),a1 	; pointer to window definition
	moveq	#iow.defw,d0		; ... redefine it
	moveq	#0,d2			; ... no border
	moveq	#-1,d3
	trap	#3			; do it now
*
	move.l	ww_wstat(a4),a1 	; pointer to window status area
	moveq	#0,d3			; force redraw
wc_item
	bsr.l	wm_drlit		draw this loose item only
wc_exit
	movem.l (sp)+,regist		restore registers
	rts

	end
