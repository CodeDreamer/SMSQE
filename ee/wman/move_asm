; New move routines V1.21 (c) 1991-2013  T. Tebby, M. Kilgus, W. Lenerz

;---------------------------------------------------------------------------
; replacement window move routine, moves wdw or outline about the screen
; this uses the gu_achp and gu_rchp calls, include them;
; this only works when using a mouse!!!
; copyright (c) w. lenerz 1991 - 2013 and many routines adapted from TT!
; 2003-05-27	v. 1.12        - totally revamped version
; 2003-05-28	v. 1.13 	prov version for QPC + better ptr handling (mk)
; 2003-06-03	v. 1.14 	common version for all machines
;				added check for QPC + reset ptr pos
;				moving secondary wdws ok
; 2003-06-18	v. 1.15 	added mode 4 kludge
; 2003-10-29	v. 1.16 	pointer sprite is saved
; 2005-01-21	v. 1.17 	outline move: spurious outline :fixed (wl)
; 2012-04-21	v. 1.18 	check for smsqmulator made when testing for ptr move (wl)
; 2013-04-28	v. 1.19 	added wdw move with alpha blending (wl)
; 2013-10-07	v. 1.20 	bugfix for alpha move of pulled down wdw (wl)
; 2014-06-29	v. 1.21 	better handling of moving up to right screen edge (wl)
;
;---------------------------------------------------------------------------
	section wman

	include dev8_keys_wstatus
	include dev8_keys_wwork
	include dev8_keys_wman
	include dev8_keys_qdos_io
	include dev8_keys_qdos_pt
	include dev8_keys_con

	include dev8_keys_err
	include dev8_keys_sys
	include dev8_keys_chn
	include dev8_keys_iod
	include dev8_keys_qdos_sms
	include dev8_keys_wman_data
*
	xdef	changewin
	xref	gu_achp0
	xref	gu_rchp
	xref	wm_wpos

;(the regs on entry comply with the typical loose menu item action routine)
;----------------------------------------------------------------------
; on entry				on exit
;
; d0					error code or 0
; d1 = x,y pointer position		d1 = 0
;
; a0 = channel id			unchanged
; a2 = window manager vector		unchanged
; a4 = working defn of wdw		unchanged
;
;				all other regs unchanged,
;				cond. codes set on exit
;				however, the routine always
;				"succeeds" unless the wdw can't be
;				redrawn correctly
;-----------------------------------------------------------------------


xyposmask equ	$fffcfffe		; x pos of wdw always on multiple of 2
xyposadd  equ	$00020000		; add this first for rounding
left	equ	192			; keystroke for cursor left

; memory requirements
limits	equ	0			; max window limits (size, orig)
ptr_rec equ	limits+8		; my pointer record
psp	equ	ptr_rec+24		; ptr to (old) pointer sprite
kpoint	equ	psp+4			; space for pointer orig rel to wdw
dsize	equ	kpoint+4		; destination size
dsize2	equ	dsize+4 		; my wdw save area row incrment
mptr	equ	dsize2+4		; save area for my window only
xyoff	equ	mptr+4			; xyoffset if secondary wdw
spec4	equ	xyoff+4 		; special add if mode 4 & pulled down!
chblk	equ	spec4+4 		; channel defn block
xysz	equ	chblk+4 		; x,y size of wdw (hitsize +4 borders) in pixels
winorg	equ	xysz+4			; x,y origin of wdw
winofs	equ	winorg+4		; x,y difference mouse ptr <-> winorg
bord	equ	winofs+4		; x,y border size in pixels
winoutl equ	bord+4			; window outline size
bytel	equ	winoutl+4		; screen byte width (long)
butn	equ	bytel+4 		; button pressed (byte)
mtyp	equ	butn+1			; type of move (byte)
alphav	equ	mtyp+1			; alpha value (byte)
spare	equ	alphav+1		; spare to make even
spxlw	equ	spare+1
rpxlw	equ	spxlw+2 		; constants for pixel calculations
sarea	equ	rpxlw+2 		; save area for entire screen (if wdw is primary) or for primary
					; = source area 2
savorig equ	sarea+4 		; orig of this area
my_space equ	savorig+4		; some slack space
tot_mem equ	my_space+16		; (8 bytes needed)

; "some" regs to save
outreg reg    d1-d7/a0-a6
			    
; error returns
errexit3
	move.l	mptr(a5),a0
	jsr	gu_rchp 		; release my small wdw save aera
errexit2
	move.l	a5,a0			; pointer to work space
	jsr	gu_rchp 		; release it
cw_exit
	move.w	(sp)+,sr		; leave supervisor mode
	movem.l (sp)+,outreg
	tst.l	d0			; all ok?
	bne.s	cw_out			; ... no
	clr.l	d1			; ... yes, clear return!
cw_out
	rts
errexit
	moveq	#-15,d0
	bra.s	cw_exit

; on entry
; D1 = ptr position			stet (error) or 0 (succesful)
; A4 = working defn			stet


changewin
	movem.l outreg,-(sp)
	move.w	sr,d6
	trap	#0			; go super now
	move.w	d6,-(a7)
	moveq	#sms.info,d0
	trap	#1
	move.l	a0,a6			; keep pointer (ok for superbasic)
	move.l	sys_clnk(a0),a0
	move.l	pt_wdata(a0),a0 	; pointer to my wman config data
	move.b	wd_alpha(a0),d6 	; alpha value
	move.b	wd_movemd(a0),d7	; type of move wished
	beq.s	errexit 		; old  move : sprite only - leave
; first some basic checks on the channel ID
	move.l	ww_chid(a4),d0		; channel ID
	cmp.w	sys_chtp(a6),d0 	; off top of table?
	bhi.s	errexit 		;  ... yes
	lsl.w	#2,d0			; index table
	move.l	sys_chtb(a6),a3 	; base of table
	add.w	d0,a3
	tst.b	(a3)			; open?
	bmi.s	errexit 		; ... no
	move.l	(a3),a5 		; address of channel
	swap	d0			; check tag
	cmp.w	chn_tag(a5),d0
	bne.s	errexit 		; not same tag?????
	add.l	#$30,a5 		; point chan defn block "base"
; as of here,we have a valid channel ID
	moveq	#tot_mem,d0		;
	jsr	gu_achp0		; get mem for myself
	bne.s	cw_exit 		; ooops->...
	exg	a0,a5			; a5 = my memory, a0 = ch defn block
	move.l	a0,chblk(a5)		; keep channel defn block
	move.l	sys_clnk(a6),a3 	; driver linkage
	move.w	pt_bytel(a3),bytel+2(a5); keep screen increment
	move.b	d7,mtyp(a5)		; keep type of move
	move.b	d6,alphav(a5)		; keep alpha blending value
	move.l	ww_psprt(a4),psp(a5)	; save pointer sprite
	move.b	pt_bpoll(a3),butn(a5)	; mouse button used to action this item
* !!
	beq	errexit 		; !! none, just leave again!!!
* !!					; this works ONLY with the mouse!
	clr.l	pt_inc(a3)		; no mouse increase yet
	move.l	pt_vecs(a3),a1
	jsr	pv_size(a1)		; get some pixel constants
	move.l	d0,spxlw(a5)		; and keep them
	move.l	ww_wstat(a4),a2 	; a2 = status area
	move.l	ws_ptpos(a2),d5 	; absolute pointer position
	move.l	ww_xorg(a4),d7		; window position in d7
	sub.l	d7,d5			; d5 = pointer position rel to window
	move.l	d5,kpoint(a5)		; store it (to restore it later)

; now find window limits
	move.l	ww_chid(a4),a0		; a0 = channel ID
	lea	limits(a5),a1		; where to store result
	moveq	#0,d2
	moveq	#-1,d3
	moveq	#iop.flim,d0		;
	trap	#3			; find out largest window size possible
	tst.l	d0
	bne	errexit2		; ooops ->....
	move.l	4(a1),xyoff(a5) 	; xy origin (should 0,0 for primary wdws)
	move.b	ww_pulld(a4),d0 	; wdw pulled down?
	beq.s	mv_save 		; no, so it's a primary wdw
	move.b	pt_dmode(a3),d0 	; display mode
	bne.s	mv_save 		; not mode 4!
	move.w	4(a1),d0		; now mode 4  kludge if not on byte boundary
	divs	#8,d0			; x orig - on byte boundary?
	swap	d0			; this is 4 or 0
	move.w	d0,spec4(a5)		; special mode 4 kludge
; save window contents by saving hit area + borders (!)
mv_save move.l	chblk(a5),a1		; point to channel defn block
	move.l	sd_xouts(a1),winoutl(a5); keep current outline size
	move.l	sd_xhito(a1),winorg(a5) ; keep current hit area origin
	clr.l	winofs(a5)
	move.b	sys_mtyp(a6),d0
	andi.b	#sys.mtyp,d0
	sub.b	#sys.java,d0		; smsqmulator?
	beq.s	qpc			; yes
	sub.b	#sys.mqpc-sys.java,d0	; qpc?
	bne.s	no_qpc			; ... no
qpc	move.l	pt_npos(a3),d1
	sub.l	winorg(a5),d1		; offset mouse position <-> win origin
	move.l	d1,winofs(a5)
no_qpc
	move.w	ww_wattr+wwa_borw(a4),d1; window border width
	move.w	d1,d2
	add.w	d1,d2			; x border is double size
	swap	d2
	move.w	d1,d2			; x,y size of one border
	move.l	d2,bord(a5)		; keep the size of the border
	move.l	sd_xhits(a1),d0 	; wdw hit area x,y size
	move.l	d0,xysz(a5)		; keep it safe
	move.l	d0,d2			; size
	clr.w	d2
	swap	d2			; x size
	clr.l	d1
	move.w	spxlw(a5),d1
	add.w	rpxlw(a5),d2		; round up to...
	lsr.w	d1,d2			; ...width in long words
	addq.w	#1,d2			; one spare
	lsl.w	#2,d2
	move.l	d2,dsize2(a5)		; is save area increment
	bsr	calcsz			; calculate bytes needed for save area of this size
	jsr	gu_achp0		; get save area for myself
	tst.l	d0
	bne	errexit2		; oops!
	move.l	a0,mptr(a5)		; keep this safe
		  
	bsr	tot_save		; now save current "wdw" into save area
    
	lea	limits(a5),a1
	move.l	4(a1),d7		; x,y origins of max wdw
	add.l	(a1),d7 		; coords of end of wdw
	sub.l	winoutl(a5),d7		; max possible x,y coords of wdw outline!
;
	addi.l	#xyposadd,d7		; v. 1.21 make sure right/lower bounds...
	andi.l	#xyposmask,d7		; ...are on correct position for stipples
; 1.21
	move.l	limits+4(a5),d6 	; d6 = min x,y positions box can take
	addi.l	#xyposadd,d6
	andi.l	#xyposmask,d6
;

; interlude:
; now find out whether we have enough memory left to save the entire screen
; once (which will happen later)
; if not, we can give up gracefully before having destroyed the window content
	move.l	limits(a5),d0		; max wdw sizes
	bsr	calcsz			; how many bytes that takes
	move.l	d0,d4
	moveq	#sms.frtp,d0
	trap	#1			; get max space
	moveq	#-15,d0 		; preset error
	sub.l	d4,d1			; is it enough?
	ble	errexit3		; no!
	
; set wdw outline to max possible size, don't keep content
	lea	limits(a5),a1		; max wdw size
	move.l	ww_chid(a4),a0		; channel ID
	moveq	#0,d1			; no shadow
	moveq	#0,d2			; don't keep contents
	moveq	#iop.outl,d0		; set wdw outline to max size (screen/primary)
	trap	#3			;
	tst.l	d0			; OK?
	bne	errexit3		; no!!!!
	move.l	chblk(a5),a2		; get pointer to chan defn block
	move.l	sd_xhito(a2),d0
	neg.l	d0
	move.l	d0,savorig(a5)		; (negative) wdw orig in screen
	clr.l	d2
	move.w	sd_xouts(a2),d2 	; set x size
	clr.l	d0
	move.w	spxlw(a5),d0
	add.w	rpxlw(a5),d2		; round up to...
	lsr.w	d0,d2			; ...width in long words
	addq.w	#1,d2			; one spare
	lsl.w	#2,d2
	move.l	d2,dsize(a5)		; keep screen increment
* as of here, the wdw content is destroyed
	moveq	#0,d1
	moveq	#iop.wsav,d0		; save entire wdw into internal save area
	trap	#3			; - leave this, it isn't always done by itself!
	move.l	chblk(a5),a1
	move.l	sd_wsave(a1),sarea(a5)	; pointer to save area

	move.b	mtyp(a5),d0		; type of move
	subq.b	#1,d0			; move "outline"?
	beq	wc_mvoutl		; ...yes ->
; move entire wdw:
; show wdw content again
	move.l	winorg(a5),d1		; wdw origin
	addi.l	#xyposadd,d1		; round up
	andi.l	#xyposmask,d1		;
	move.l	d1,winorg(a5)		; store current pointer position
	subq.b	#1,d0
	move.b	d0,mtyp(a5)		; this is now 0  if move, 1 if alpha move
	bsr	tot_which		; show wdw content again at this position

; main program loop
ploop1
	bsr	test_move		; did ptr move? (if we get back - it did)
	pea	ploop1			; redraw wdw & restart at loop !

; wdw redraw
; 1 -	restore old screen content at newly discovered edges after wdw move,
;	this is done by taking the data out of the normal window save area
;	(window covers all of screen/primary)
; 2 -	restore wdw content at new coords
;	d1 c s	new coords
;	d2   s
;	d4 c s	old coords
;	a1   s
;	a5 c p	my memory

do_trace1
	lea	my_space(a5),a1
	move.l	d1,winorg(a5)		; keep new coords
	sub.w	d4,d1			; did window move up or down?
	beq.s	horiz			; ... no, check right/left ->...
	blt.s	low			; ... yes : window moved up ->

; window moved down - restore part of old window in y direction
	move.w	xysz(a5),(a1)		; x size of wdw block (wdw x size)
	move.w	d1,2(a1)		; y size of wdw block (nbr of pixels moved)
	move.l	d4,d1			; where to get it from (orig in block)
	bra.s	vert
; window moved up
low	move.w	xysz(a5),(a1)		; x size of wdw block (wdw x size)
	move.l	d4,d0			; where to put it
	add.w	xysz+2(a5),d0
	add.w	d1,d0
	neg.w	d1
	move.w	d1,2(a1)		; y size of block
	move.l	d0,d1
; 4(a1) = where to put it in the wdw, in absolute screen coordinates
; d1 = where to get it from the block, in absolute screen coordinates
vert	move.l	xyoff(a5),d0		; offsets from start of screen if pulled down
	sub.l	d0,d1			; make it relative to origin of wdw
	move.l	d1,4(a1)
	add.l	spec4(a5),d1		; possible mode 4 kludge
	bsr.s	pt_prest		; restore this part of the screen

; check if wdw moved right or left
horiz	lea	my_space(a5),a1 	;
	move.l	d4,d2
	swap	d4			; d4.w = x coord
	move.w	winorg(a5),d1		; new x coord
	sub.w	d4,d1			; any horizontal move?
	beq.s	tot_which		; ... none, restore wdw & return
	blt.s	mleft			; ... yes, wdw moved left ->...
; window moved to the right
	move.w	xysz+2(a5),2(a1)	; y size of block to restore
	move.w	d1,(a1) 		; x size
	move.l	d2,d1			; where to put it
	move.l	d1,4(a1)		; also in wdw
	bra.s	horiz2
; window moved left
mleft	move.w	xysz+2(a5),2(a1)	; y size
	move.w	d2,6(a1)		; y orig
	add.w	xysz(a5),d4		; x orign + xsize of wdw
	add.w	d1,d4			; - displacement
	move.w	d4,4(a1)		; into x orig
	neg.w	d1
	move.w	d1,(a1) 		; x size
	move.l	4(a1),d1		; orig in area
horiz2
	move.l	xyoff(a5),d0		; make relative to primary if necesary
	sub.l	d0,4(a1)
	sub.l	d0,d1
	add.l	spec4(a5),d1		; possible mode 4 kludge
	bsr.s	pt_prest		; restore part of background
	bra.s	tot_which		 ; and restore entire wdw to new position
					; (and return from there)

*------------------
* some subroutines
*------------------
rstregs reg	d3-d7/a0-a5

; PARTIAL restore (background)
;
;	Restore parts of the contents of a window save area into the screen memory,
;	in the location given.
;
;	Calculate parameters for saving a window
;
;	Registers:
;		Entry				before calling pt_mblock
;	D0
;	D1	origin in save area		size of window
;	D2					origin of window in save area
;	D3					origin of window
;	A0
;	A1	wdw block
;	A2					row increment in save area
;	A3	^ linkage block 		row increment in screen
;	A4					base of save area
;	A5	my data block			base of screen


pt_prest
	movem.l rstregs,-(sp)
	move.l	chblk(a5),a0		; get pointer to chan defn block
	move.l	dsize(a5),a2		; and increment
	move.l	d1,d2			; orig in save area (source area 1)
	movem.l (a1),d1/d3		; size & orig of wdw
	add.l	sd_xmin(a0),d3		; true origin in screen
	move.l	sd_wsave(a0),a4 	; save area base
	move.l	bytel(a5),a3		; screen increment
	move.l	sd_scrb(a0),a5		; screen base
*
cpy_jmp
	move.l	sys_clnk(a6),a0
	move.l	pt_vecs(a0),a0
	jsr	pv_mblk(a0)		; move from save area to screen
ptr_ok	movem.l (sp)+,rstregs
	rts
;
;	Calculate parameters for saving the "window" from screen to save area
;	and save the "wdw"
;
;	Registers:
;
;	Entry					Exit
;	D0					smashed
;	D1					size of window
;	D2					origin of window in save area
;	D3					origin of window
;	D5
;	A0
;	A2					row increment in save area
;	A3					row increment in screen
;	A4					base of save area
;	A5	my memory space 		base of screen
;

tot_which				; do we do a normal or an alpha move?
	tst.b	mtyp(a5)
	beq.s	tot_rest		; nomal move
	bra.s	tot_alpha
		
tot_save				; save entire wdw into save area
	movem.l rstregs,-(sp)		; keep regs
	bsr.s	cprm
	exg	a4,a5
	exg	a2,a3
	exg	d2,d3
	bra.s	cpy_jmp
		      
; same as above, but restore entire wdw from save area to screen
tot_rest
	movem.l rstregs,-(sp)		; keep regs
	bsr.s	cprm
	bra.s	cpy_jmp

; same as above, but alpha in the entire wdw from background save area to screen
; source area 2 is the background, i.e. the entire screen, or the primaty if wdw is pulled down
tot_alpha
	movem.l rstregs,-(sp)		; keep regs
	move.l	sarea(a5),a1		; point to save area for background (source area2)
	clr.l	d6
	move.b	alphav(a5),d6		; alpha value
	move.l	dsize(a5),d7		; source area 2 row increase
	move.l	savorig(a5),d4		; (negative) wdw orig
	bsr.s	cprm			; (destroys a5)
	tst.b	d6			; would wdw be totally transparent?
	beq.s	cpy_jmp 		; yes, this doesn't make sense, use totally opaque instead
	cmp.b	#255,d6
	beq.s	cpy_jmp 		; use faster routine if totally opaque
	add.l	d3,d4			; orig in source area 2
	move.l	sys_clnk(a6),a0
	move.l	pt_vecs(a0),a0
	jsr	pv_cmbbk(a0)		; combine window save area & backgound save area
	movem.l (sp)+,rstregs
	rts
	    
; calculate parameters (adapted from TT's code!)
cprm	move.l	xysz(a5),d1		; set size
	clr.l	d2			; origin in save area always top left, source area 1
	move.l	winorg(a5),d3		; set destination area origin
	move.l	chblk(a5),a0		; channel defn block
	move.l	dsize2(a5),a2		; row increment source area1
	move.l	bytel(a5),a3		; screen (destination area) increment
	move.l	mptr(a5),a4		; source area1 base address
	move.l	sd_scrb(a0),a5		; screen (destination) base
	rts
      
; calculate the amount of memory needed for a save area of a given size
;	d0 cr	x,y size of area / space needed in bytes
;	d1   s
;	d2   s
calcsz	move.l	d0,d1
	swap	d1			; outline area width (pixels)
	clr.l	d2
	move.w	spxlw(a5),d2
	add.w	rpxlw(a5),d1		; round up to...
	lsr.w	d2,d1			; ...width in long words
	addq.w	#1,d1			; one spare
	mulu	d1,d0			; space required in long words
	asl.l	#2,d0			; space in bytes
cs_rts
	rts

; test move: test whether pointer has moved from old position and
; whether user stopped move operation. In this latter case, this routine
; does not return to the caller, but leaves the move operation entirely.
; Else it only returns to caller if ptr did move

test_move
	moveq	#0,d5
	move.l	sys_clnk(a6),a1 	; pointer to linkage block
tm_lp
	move.b	pt_bpoll(a1),d0 	; button currently pressed
	cmp.b	butn(a5),d0		; still the same as before?
	bne	leave			; no, so we leave the move operation
tm_testxy
	move.b	sys_mtyp(a6),d0
	andi.b	#sys.mtyp,d0
	sub.b	#sys.java,d0		; smsqmulator?
	beq.s	tl_testxy_qpc		; yes
	sub.b	#sys.mqpc-sys.java,d0	; qpc?
	bne.s	tm_tstxy_o		; ... no
tl_testxy_qpc
	move.l	winorg(a5),d4		; ...yes, old window coordinates
	move.l	pt_npos(a1),d1		; current position
	sub.l	winofs(a5),d1
	bra.s	tm_tst_com

tm_tstxy_o
	move.l	winorg(a5),d1
	move.l	d1,d4			; d4 = old pointer coordinates
	add.w	pt_yinc(a1),d1		; + y move
	swap	d1
	add.w	pt_xinc(a1),d1		; + xmove
	swap	d1			; new ptr position
	clr.l	pt_inc(a1)		: no more increase yet
tm_tst_com
	addi.l	#xyposadd,d1		; round up
	andi.l	#xyposmask,d1		; window orig is always set to multiple of 4/2 (for stipples)
	cmp.l	d1,d4			; any change?
	beq.s	tm_lp			; no - go around again

; make sure that ptr doesn't stray beyond limits
test_bounds
	cmp.w	d1,d4			; did pointer move up/down ?
	beq.s	x_change		; ... no, so no need to check y bounds
y_change
	cmp.w	d1,d6			; test against upper boundary
	ble.s	min_ok			; ... ok, position doesn't exceed this boundary
	move.w	d6,d1			; ... ahem, set pointer to upper boundary
	bset	#0,d5			; flag pointer reset
	bra.s	x_change

min_ok	cmp.w	d1,d7
	bge.s	x_change		; ... no, so this is ok
	move.w	d7,d1			; max y position
	bset	#0,d5			; flag pointer reset
x_change
	swap	d1			; now treat x position
	swap	d4
	swap	d6
	swap	d7
	cmp.w	d1,d4			; did pointer move left/right?
	beq.s	tm_test 		; no - redraw directly
	cmp.w	d1,d6			; would we exceed left boundary?
	ble.s	min_ok2 		;  ... no
	move.w	d6,d1			;  ...yes, set to left boundary
	bset	#0,d5			; flag pointer reset
	bra.s	tm_test 		;

min_ok2 cmp.w	d1,d7
	bge.s	tm_test 		; ...no
	move.w	d7,d1			; ... yes, so this is where we stay
	bset	#0,d5			; flag pointer reset
tm_test
	swap	d6
	swap	d7
	swap	d1
	swap	d4			;
	bclr	#0,d5			; pointer needs resetting ?
	beq.s	no_ptr			; ...no ->...
	moveq	#0,d2			; ...yes, do it
	moveq	#iop.sptr,d0

	move.l	d1,-(sp)
	add.l	winofs(a5),d1
	trap	#3			; set pointer position in absolute coords
	move.l	(sp)+,d1
no_ptr
	cmp.l	d1,d4			; did ptr move after bounds correction?
	beq	tm_lp			; ... no
	rts				; ... yes, we need to redraw wdw

; -------------------------

; if we get here, then the  user released the button to end the move op
; now set window origin & redraw window in new position
leave
	addq.l	#4,sp			; !!! do not return to caller
	move.b	mtyp(a5),d0		; type of move
	subq.b	#1,d0			; outline only?
	bne.s	lv_nobox		; ...no
	move.l	d4,d1			; ... yes, get correct coords back (1.17)
	bsr.s	do_trace		;     and untrace box
	moveq	#0,d1
	moveq	#iow.sova,d0
	trap	#3			; unset xor
lv_nobox
	move.l	psp(a5),ww_psprt(a4)	; reset old pointer for the window
	move.l	ww_chid(a4),a0		; channel ID
	moveq	#0,d5
	move.l	winorg(a5),d1
	add.l	bord(a5),d1
	jsr	wm_wpos 		; reposition primary
	move.l	chblk(a5),a0
	move.l	sd_xouto(a0),winorg(a5)
	bsr	tot_rest		; copy content back to wdw
	move.l	ww_chid(a4),a0		; channel ID
	move.l	kpoint(a5),d1		; relative ptr position
	add.l	bord(a5),d1		; now
	moveq	#1,d2			; relative to hit area
	moveq	#iop.sptr,d0
	trap	#3			; set ptr position now
	moveq	#0,d0			; no error!
	bra	errexit3		; leave the move wdw routine
; -------------------------
; "outline" move
wc_mvoutl
	moveq	#-1,d1
	moveq	#iow.sova,d0
	trap	#3			; set xor
	move.l	winorg(a5),d1
	bsr.s	do_trace		; trace box now
	move.l	winorg(a5),d1
mvout_lp
	bsr	test_move
	exg	d4,d1			; yes : d1 = coords of old box
	bsr.s	do_trace		; undo box at old coords
	move.l	d4,d1			; new box position
	move.l	d1,winorg(a5)		; keep for undrawing
	pea	mvout_lp

; box drawing subroutine - this is achieved by drawing 4 blocks
; first the 2 horizontal blocks (1 pixel high)
; then the two vertical blocks (1 pixel wide)

do_trace
	sub.l	xyoff(a5),d1
	lea	my_space(a5),a1
	move.w	xysz(a5),(a1)		; length of box
	move.w	#1,2(a1)		; height "   "
	move.l	d1,4(a1)		; x,y orig
	bsr.s	do_blk			; upper x line of box
	move.w	xysz+2(a5),d0
	add.w	d0,6(a1)		; set y orig to end of y
	bsr.s	do_blk			; lower x line of box
	move.w	xysz+2(a5),d0
	move.w	#1,(a1) 		; new width of box
	move.w	d0,2(a1)		; new height of box
	sub.w	d0,6(a1)		; reset to orig
	bsr.s	do_blk			; draw left line
	move.w	xysz(a5),d0
	add.w	d0,4(a1)		; new x coord - draw right line
do_blk	moveq	#7,d1
	moveq	#iow.blok,d0		;
	trap	#3
	lea	my_space(a5),a1
	rts
 
	end
