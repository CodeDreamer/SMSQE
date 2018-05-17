* Set up the window working area  V1.04     1986  Tony Tebby	QJUMP
* 2013-12-21  1.05 check for and possibly set appsub wdw csizes (wl)
*
	section wman
*
	xdef	wm_fsize
	xdef	wm_setup
	xdef	wm_smenu
*
	xref	wm_entry
*
	include dev8_keys_qdos_sms
	include dev8_keys_sys
	include dev8_keys_wdef
	include dev8_keys_wwork
	include dev8_keys_wstatus
*
* Go through window definition block looking for a definition large enough
* for the requested size
*
*	Registers:
*		Entry				Exit
*	D0					smashed
*	D1	size required or 0		actual size
*	D2.w					layout table entry number
*	A3	window definition		preserved
*
fsreg	reg	d5/a3
wm_fsize
	movem.l fsreg,-(sp)
	tst.l	d1			; is default size required?
	bne.s	wsf_sbas		; no
	move.l	wd_xsize(a3),d1 	; yes, extract it
wsf_sbas
	add.w	#wd_rbase,a3		; skip to repeated part 
	move.l	a3,d5			; keep it safe
	bsr.s	ws_fsiz 		; find size to be used
	swap	d1
	move.w	d2,d1			; return in one register
	sub.l	d5,a3			; offset of start
	move.l	a3,d2
	divu	#wd.elen,d2		; thus entry number
	movem.l (sp)+,fsreg
	rts
*
* find correct size for window
*
*	Registers:
*		Entry				Exit
*	D0					smashed
*	D1	requested size x,y		actual X size
*	D2					actual Y size
*	A3	repeated part of defn		pointer to selected entry
*
ws_fsiz
	move.w	d1,d2			size in two registers
	swap	d1
ws_cksiz
	move.w	wd_xmin(a3),d0		x size
	bclr	#wd..vsiz,d0		(remove variable size flag)
	cmp.w	d0,d1			fit in x?
	blt.s	ws_smsiz		... no, try smaller size
	move.w	wd_ymin(a3),d0		y size
	bclr	#wd..vsiz,d0		(remove variable size flag)
	cmp.w	d0,d2			fit in y?
	bge.s	ws_ssize		... yes, set actual sizes
*
ws_smsiz
	add.w	#wd.elen,a3		next entry
	tst.w	(a3)			exists?
	bgt.s	ws_cksiz		... yes, check the sizes
	sub.w	#wd.elen,a3		... no, back space

ws_ssize
	move.w	wd_xmin(a3),d0		 ; fixed x size?
	bclr	#wd..vsiz,d0
	beq.s	ws_sfixx		 ; ... yes
	cmp.w	d1,d0			 ; xmin greater?
	ble.s	ws_ssizy		 ; ... no
ws_sfixx
	move.w	d0,d1			 ; fixed window size

ws_ssizy
	move.w	wd_ymin(a3),d0		 ; fixed y size?
	bclr	#wd..vsiz,d0
	beq.s	ws_sfixy		 ; ... yes
	cmp.w	d2,d0			 ; ymin greater?
	ble.s	ws_sscal		 ; ... no
ws_sfixy
	move.w	d0,d2			 ; fixed window size

ws_sscal
	rts
*
*	d1 c r	window size (x,y, zero or -1)
*	a0 c p	window channel ID  
*	a1 c p	pointer to window status area
*	a3 c p	pointer to window defintion 
*	a4 c p	pointer to working definition
*
*	all other registers preserved
*
*	standard error return (no error)
*
rgsetup  reg	d2/d3/d4/a0-a4
stk_stat equ	$10			pointer to window status area on stack
*
wm_setup
	movem.l rgsetup,-(sp)		 ; save registers

	move.l	d1,d3			 ; size required
	bgt.s	ws_flay 		 ; ... size given, find layout
	beq.s	ws_dfsiz
	move.l	ww_xsize(a4),d1 	 ; ... do not change size
	bra.s	ws_flay
ws_dfsiz
	move.l	wd_xsize(a3),d1 	 ; ... use default in definition
ws_flay
	move.l	a3,a2			 ; save definition pointer
	lea	wd_rbase(a3),a3
	bsr	ws_fsiz 		 ; find size...
	movem.w d1/d2,ww_xsize(a4)	 ; ...and set it in working definition
	sub.w	(a3)+,d1		 ; and find scaling factor for x
	and.w	#$0fff,d1
	sub.w	(a3)+,d2		 ; and for y
	and.w	#$0fff,d2
	exg	a2,a3

	movem.l d1/d2/a0,-(sp)		 ; save regs
	moveq	#sms.info,d0		 ; get system info
	trap	#do.sms2
	move.w	#$0108,d0		 ; QL mode 8
	and.b	sys_qlmr(a0),d0
	lsr.b	#3,d0			 ; in the right place
	move.w	d0,ws_wmode(a1)
	movem.l (sp)+,d1/d2/a0

	move.l	a4,(a1) 		 ; set pointer to working definition
	move.l	a1,(a4)+		 ; set pointer to status area
	addq.l	#4,a1
	move.l	a3,(a1)+		 ; set pointer to window definition
	move.l	a3,(a4)+		 ; save pointer to window definition
	move.l	a0,(a4)+		 ; set channel ID
	move.l	a1,(a4)+		 ; set pointer to pointer record
*
	clr.l	(a4)+			 ; clear old pointer position
	clr.l	(a4)+			 ; one spare
	clr.l	(a4)+			 ; another
	clr.l	(a4)+			 ; the sub-window sprite list
*
	cmp.l	(a3)+,(a4)+		 ; skip size (already set)
	tst.l	d3			 ; size / origin unchanged?
	bge.s	ws_sdorg		 ; ... no, set origin
	cmp.l	(a3)+,(a4)+		 ; ... yes, do not copy window origin
	bra.s	ws_sattr
ws_sdorg
	bsr.l	ws_2coord		 ; window origin (initial meaning)

ws_sattr
	move.l	(a3)+,(a4)+		 ; set window attributes
	move.l	(a3)+,(a4)+

	bsr.l	ws_cptr 		 ; set sprite pointer
	bsr.l	ws_ciatt		 ; set item attributes
	bsr.l	ws_cptr 		 ; set help pointer
*
* now go through window definition block
*
	move.l	a2,a3
*
* first do all the information sub-windows
*
	lea	ww_lists-ww_ninfo(a4),a2 set pointer to lists
	clr.l	(a4)+			clear count of information sub-windows
	bsr.l	ws_cptr 		set pointer to information sub-windows
	beq.s	ws_lmenu		... none, set loose menu object list
*
	move.l	a3,a0			save pointer to definition
	move.l	a1,a3			use pointer to information sub-windows
	move.l	a2,-(a4)		reset pointer to information sub-windows
	clr.l	d3			preset count of information sub-windows
	exg	a2,a4			exchange pointers
	bra.s	ws_infend
*
* first pass through information sub-windows
*
ws_info1
	addq.w	#1,d3			one more information sub-window
	bsr.l	ws_4coord		copy and scale four coordinates
	move.l	(a3)+,(a4)+		copy attributes
	move.l	(a3)+,(a4)+
	bsr.l	ws_cptr 		copy pointer to object list
ws_infend
	tst.w	(a3)			end of information sub-window list?
	bge.s	ws_info1		... no
*
	move.w	d3,ww_ninfo-ww_pinfo(a2) set number of information sub-windows
	move.w	#-1,(a4)+		put in terminator
*
* second pass through information sub-windows
*
	moveq	#0,d3			preset count of information objects
	move.l	a2,d4			save pointer to definition
	move.l	(a2),a2 		pointer to information sub-window list
ws_info2
	move.l	wwi_pobl(a2),a3 	pointer to information object definition
	move.l	a3,d0			exists?
	beq.s	ws_i2nxt		... no
	move.l	a4,wwi_pobl(a2) 	set actual pointer
	bra.s	ws_i2obend
*
ws_i2obl
	addq.w	#1,d3			one more object
	bsr.l	ws_4coord		copy and scale 4 coordinates
	tst.b	(a3)			text object?
	bgt.s	ws_infob		... no
	move.w	(a3)+,(a4)+		set text object type
	move.l	(a3)+,(a4)+		set text object ink and size
	bra.s	ws_pinfo
ws_infob
	move.w	(a3)+,(a4)+		copy type of object
	bsr.l	ws_cptr 		and pointer to blob or pattern
	addq.l	#2,a3			skip empty hole
ws_pinfo
	bsr.l	ws_cptr 		copy pointer to object
ws_i2obend
	tst.w	(a3)			end of list?
	bge.s	ws_i2obl		... no, next
*
	move.w	#-1,(a4)+		put in terminator
*
ws_i2nxt
	add.w	#wwi.elen,a2		next information sub-window
	tst.w	(a2)			last?
	bge.s	ws_info2
*
* end of information lists
*
	move.l	a4,a2			retore fill pointer
	move.l	d4,a4			restore working area pointer
	move.l	a0,a3			and definition pointer
	move.w	d3,ww_ninob-ww_pinfo(a4) set number of information objects
	addq.l	#ww_nlitm-ww_pinfo,a4	and move on
*	 
* now do all loose menu items
*
ws_lmenu
	clr.w	(a4)+			preset no loose menu items
	bsr.l	ws_cptr 		copy pointer
	beq.s	ws_appl 		... none, do application window
	move.l	a3,a0			save pointer to definition
	move.l	a1,a3			use pointer to loose menu items
	move.l	a2,-(a4)		reset pointer to loose menu items
	clr.l	d3			preset count of loose menu items
	exg	a2,a4			exchange pointers
	bra.s	ws_lmend
*
* go through loose menu items
*
ws_lmenl
	addq.w	#1,d3			one more loose menu item
	bsr.l	ws_4coord		copy and scale four coordinates
	move.l	(a3)+,(a4)+		copy justification rules and type
	bsr.l	ws_cptr 		copy pointer to object
	move.w	(a3)+,(a4)+		and item number
	bsr.l	ws_cptr 		copy pointer to action routine
ws_lmend
	tst.w	(a3)			end of loose menu items?
	bge.s	ws_lmenl		... no
*
* end of loose menu item list
*
	exg	a4,a2			restore working area pointers
	move.l	a0,a3			and definition pointer
	move.w	d3,ww_nlitm-ww_plitm(a4) set number of loose menu items
	addq.l	#ww_nappl-ww_plitm,a4	and move on
*	 
*
* now do all application sub-windows
*
ws_appl
	move.w	#-1,(a2)+		terminate loose menu item list
	clr.w	(a4)+			preset no application sub-windows
	bsr.l	ws_cptr 		copy pointer
	beq.s	ws_done 		... none, done
	move.l	a1,a3			use pointer to application sub-windows
	move.l	a2,-(a4)		reset pointer to application sub-windows
	move.l	a2,ww_splst-ww_pappl(a4) set default sprite list pointer
	moveq	#-1,d3			preset count of application sub-windows
	exg	a2,a4			exchange pointers
*
* count application sub-windows
*
ws_applc
	addq.w	#1,d3			one more application sub-window
	bsr.l	ws_cptr 		done?
	bne.s	ws_applc		... no
	move.w	d3,ww_nappl-ww_pappl(a2) set number of application sub-windows
	move.l	(a2),a2 		set pointer to sub-window list
*
* setup all application sub-windows
*
ws_appll
	move.l	(a2),a3 		next definition
	move.l	a3,d0			... is there one?
	beq.s	ws_done 		... no
	move.l	a4,(a2)+		set genuine pointer
*
	bsr.l	ws_4coord		copy and scale four coordinates
	move.l	(a3)+,(a4)+		copy window attributes
	move.l	(a3)+,(a4)+
	bsr.l	ws_cptr 		copy pointer to sprite
	bsr.l	ws_cptr 		get pointer to setup routine
	move.l	-(a4),a0		and keep it safe
	bsr.l	ws_cptr 		copy pointer to draw routine
	bsr.l	ws_cptr 		copy pointer to hit routine
	bsr.l	ws_cptr 		copy pointer to control routine
	move.l	stk_stat(sp),a1 	pointer to status block
	move.l	(a3)+,d3		set numbers of control sections
	move.l	d3,(a4)+
	move.w	(a3)+,(a4)+		copy selection key and spare
	clr.w	(a4)+			and extra spare
* v 1.05
	cmp.l	#wda_xtnd,-wda.blen-4(a3) is it extended defn?
	bne.s	ws_ctl			no just continue
	move.w	-wda.blen+wda_csiz(a3),(a4)  set csizes

* end 1.05
ws_ctl
	bsr.s	ws_sctrl		set x control
	bsr.s	ws_sctrl		and y control
*
	move.l	a0,d0			was there a setup routine?
	beq.s	ws_appll		... no, look at next
	move.l	a2,-(sp)		... yes, save list pointer
	lea	wm_entry(pc),a2 	set window manager vector
	jsr	(a0)			do it
	move.l	(sp)+,a2		restore list pointer
	bra.s	ws_appll		next application sub-window
*
ws_done
	movem.l (sp)+,rgsetup		restore registers
	move.l	ww_xsize(a4),d1 	reset size
	moveq	#0,d0
	rts
*
*
* setup control block
*
ws_sctrl
	swap	d3
	tst.w	d3			any control block for this one?
	beq.s	ws_appld		no control routine - put in dummy
	bsr.l	ws_cpsta		copy pointer to control block
	bsr.l	ws_2coord		copy and scale index spacing
	move.l	(a3)+,(a4)+		copy current item border
	move.l	a1,-(sp)		(save status area pointer)
	bsr.l	ws_ciatr		and normal attribute record
	move.l	(sp)+,a1
	move.w	(a3)+,(a4)+		pan scroll arrow
	move.l	(a3)+,(a4)+		pan scroll bar
	bra.s	wssc_rts		done
ws_appld
	moveq	#(wwa.clen)/2-1,d0	set dummy control block
ws_ctrlz
	clr.w	(a4)+			... to zero
	dbra	d0,ws_ctrlz
*
wssc_rts
	rts
	page
*
*	d1 c p	x scaling
*	d2 c p	y scaling
*	a1 c p	pointer to window status area
*	a3 c u	pointer to window definition
*	a4 c u	pointer to working definition
*
rgsmenu  reg	d3/d4/d5/a0/a1/a2
*
wm_smenu
	movem.l rgsmenu,-(sp)		save registers
	lea	wwa.mlen(a4),a2 	start of menu lists
	bsr.l	ws_cpsta		set pointer to status area
	bsr.l	ws_ciatt		set item attributes
	move.l	(a3)+,d3		get number of columns / rows
	move.l	d3,(a4)+		set number of columns / rows
	move.l	(a3)+,(a4)+		copy x,y offsets
	bsr.s	ws_setsp		set x spacing list
	bsr.s	ws_setsp		set y spacing list
*
	bsr.l	ws_setin		set x index list
	bsr.l	ws_setin		set y index list
*
	bsr.l	ws_cptr 		get pointer to row list
	beq.s	ws_mexit		... none, done
	subq.l	#4,a4			and reset it
	move.l	a2,(a4)+
	move.l	a3,-(sp)		save running pointer to structure
	move.l	a2,a4			now fill row list
	move.l	a1,a3
*
	move.w	d3,d0			get number of rows
	move.w	d3,d4			(saved)
	mulu	#wwm.rlen,d0		length of row list
	lea	(a4,d0.l),a2		pointer to object lists
	bra.s	ws_rowend
*
ws_rowloop
	bsr.l	ws_cptr 		get row list start
	move.l	a1,d3			save it
	bsr.l	ws_cptr 		get row list end
	beq.s	ws_rowend		... none
	subq.l	#8,a4			backspace fill pointer
	move.l	a2,(a4)+		and put in start pointer
	exg	a1,d3			get back the start
	sub.l	a1,d3			set count
	divu	#wdm.olen,d3		... actual number
	bsr.s	ws_setob
	move.l	a2,(a4)+		set end pointer
ws_rowend
	dbra	d4,ws_rowloop		... next row
*
	move.l	(sp)+,a3		restore running pointer to definition
	move.l	a2,a4			correct pointer to workspace
*
ws_mexit
	movem.l (sp)+,rgsmenu		restore registers
	moveq	#0,d0
	rts
*
* set spacing lists
*
ws_setsp
	swap	d3			take other dimension
	exg	d1,d2
	bsr.s	ws_cptr 		get pointer
	beq.s	ws_ssrts		... none
	subq.l	#4,a4			it will need to be reset
	tst.w	(a1)			is first spacing negative?
	bge.s	ws_slist		... no, it is a list

	bsr.s	ws_sneg
	nop
ws_sneg
	moveq	#0,d0
	sub.w	(a1)+,d0		make spacing positive
	bsr.l	ws_scld0		scale and copy d0
	neg.w	-2(a4)			reset to negative
	rts

ws_slist
	move.l	a2,(a4)+		point to copied list
	exg	a1,a3			set copy pointers
	exg	a2,a4
	move.w	d3,d5			set number of spacings
	bra.s	ws_ssend		copy spacings
*
ws_ssloop
	bsr.s	ws_coord		copy and scale spacing characteristics
	bsr.s	ws_coord
ws_ssend
	dbra	d5,ws_ssloop		set next
	exg	a2,a4			restore pointers
	move.l	a1,a3
*
ws_ssrts
	rts
*
* set index lists
*
ws_setin
	swap	d3			take other dimension
	bsr.s	ws_cptr 		get pointer
	beq.s	ws_sorts		... none
	subq.l	#4,a4			and reset it
	move.l	a2,(a4)+
*
ws_setob
	exg	a2,a4			fill end of structure
	move.l	a3,a0			save pointer to definition
	move.l	a1,a3			and use pointer to list
	move.w	d3,d5			set number of things to copy
	bra.s	ws_solend
ws_soloop
	move.l	(a3)+,(a4)+		copy justification rules, object type
	bsr.s	ws_cptr 		copy pointer to object
	move.w	(a3)+,(a4)+		copy item number
	bsr.s	ws_cptr 		copy pointer to action routine
ws_solend
	dbra	d5,ws_soloop
*
	exg	a4,a2			restore fill pointers
	move.l	a0,a3			and pointer to definition
ws_sorts
	rts
	page
*
* utility routines for wm_setup, wm_smenu
*
* copy item attributes
*
ws_ciatt
	move.l	(a3)+,(a4)+		copy current item border
	bsr.s	ws_ciatr		unavailable record
	bsr.s	ws_ciatr		available record
	nop				selected record
*
* copy item attribute record
*
ws_ciatr
	move.l	(a3)+,(a4)+		copy background and ink
	bsr.s	ws_cptr 		set blob
	nop				and pattern
*
* copy pointer
*
ws_cptr
	move.l	a3,a1			set relative address base
	move.w	(a3)+,d0		... offset
	beq.s	ws_longz		... none, long word zero
	bclr	#0,d0			make even address
	add.w	d0,a1			and offset
	beq.s	ws_longa		... ok, long word address
	add.l	(a1),a1 		... lsb set, indirect relative
ws_longa
	move.l	a1,(a4)+		set long word address
	rts
ws_longz
	clr.l	(a4)+			set long word zero
	rts
*
* copy pointer to status area
*
ws_cpsta
	move.w	(a3)+,d0		get relative address
	beq.s	ws_longz		... none
	ext.l	d0			sign extend
	move.l	a1,(a4) 		set base address of status area
	add.l	d0,(a4)+		+ offset
	rts
*
* copy and scale coordinates
*
ws_4coord
	bsr.s	ws_2coord		copy 2 coordinates
	nop				(twice)
ws_2coord
	exg	d1,d2			do x first
	bsr.s	ws_coord
	exg	d2,d1			... now y
ws_coord
	moveq	#0,d0
	move.w	(a3)+,d0		get coordinate
ws_scld0
	lsl.l	#4,d0
	lsr.w	#4,d0			basic size
	move.w	d0,(a4) 		... saved
	swap	d0
	mulu	d2,d0			scaling * 4
	lsr.w	#2,d0
	add.w	d0,(a4)+		added to coordinate
	rts
	end
