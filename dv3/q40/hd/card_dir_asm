; Q68 get first 16 dir entries of FAT32 root dir  1.00. Copyright (c) W. Lenerz 2020


	section exten

	xdef	crd_dir

	xref	get_dir2
	xref	gu_rchp
	xref	gu_achp0
	xref	hdt_doact
	xref	hd_byte

	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'
	include 'dev8_keys_q68'
	include 'dev8_keys_thg'
	include 'dev8_keys_err'
	include 'dev8_keys_dos'
	include 'dev8_mac_thg'
	include 'dev8_mac_xword'
	include 'dev8_mac_assert'


mdir	dc.w	thp.ubyt
	dc.w	thp.ubyt+thp.opt
	dc.w	thp.ret+thp.str
	dc.w	0



hdt.reg reg	d1-d7/a0-a5 ; DON'T CHANGE THIS UNLESS IT IS ALSO CHANGED
;!!!!			    ; IN dev8_dv3_q40_hd_thing_asm  and chkwin_asm


;************************** Extension thing routine ****************
;
* ret$ = CARD_DIR$ (card[,partition])
;
; function to get the 1st 16 dir entries of the partition on the card (Fat32)
;
;*******************************************************************


err_bp	moveq	#err.ipar,d0
err_out rts

crd_dir thg_extn {CDIR},crd_crush,mdir
	movem.l hdt.reg,-(a7)
	bsr	hdt_doact		; this calls the following code in super mode

; Code is called as device driver, in supervisor mode with a3 pointing to device
; definition block and a4 pointing to drive defn (if it exists)
; on entry, condition code = Z if a drive definition is found, else NE
;	d0 c	0 or error if no drive definition was found
;	d7 c  p drive ID / number  (from 1 to 8)
;	a1 c  p address of thing
;	a3 c  p device driver linkage block
;	a4 c  p drive defn block (if it exists)

	subq.w	#1,d7
	blt.s	err_bp
	cmp.w	#3,d7
	bgt.s	err_bp			; only 1...4 allowed, made into 0...3
	moveq	#0,d0
	move.l	d0,d4
	move.w	6(a1),d0		; buffer size
	move.l	d0,d1
	sub.w	#14*16,d1		; must be at least this big
	blt	err_bp			; not enough
	move.b	3(a1),d4		; partition (defaults to 0)
	blt.s	err_bp
	cmp.b	#3,d4
	bgt.s	err_bp			; only 0...3 allowed

; the read etc routines expect D7 to be a drive, not a card, they then get
; the card (=target) number from the device defn block (=A3).
; So set the card info in the unit block and us 0 as "drive"

	move.b	d7,hdl_targ-1(a3)	; card to get, will be "target"
	moveq	#0,d7			; fake drive number
	move.l	8(a1),a5		; space for result
	bsr	get_dir2		; get 1st sector of root dir
	bne.s	err_out

; I should now have the root dir at (a1) - read the first 16 entries in the root dir
	move.w	#16*14,(a5)+
	moveq	#15,d1			; read first 16 entries in root dir
	move.w	#$200a,d3		; space+lf
	move.l	#$ff000000,d2
loop1	move.l	a1,a2			; point start of dir entry
	move.l	(a2)+,d0		; first part of name
	beq.s	dir_eof 		; we're done with the dir entirely
	rol.l	#8,d0
	cmp.b	#$e5,d0 		; empty entry in dir?
	beq.s	empty			;  ... yes
	tst.b	d0			; empty entry and also end of dir?
	beq.s	dir_eof 		;  ... yes
	ror.l	#8,d0
	move.l	d0,(a5)+
	move.l	(a2)+,(a5)+
	move.l	(a2),d0
	cmp.b	#$0f,d0 		; is this a long name?
	beq.s	longname		; ... yes, show it
	move.b	#'.',d0 		; insert "." before the extension
	ror.l	#8,d0
	move.l	d0,(a5)+
lp1	move.w	d3,(a5)+		; extra space + LF
	add.w	#dos.drel,a1		; get next entry in the root dir
	dbf	d1,loop1
exit	moveq	#0,d0
	jmp	gu_rchp

; set "empty" string
empty	lea	mty_str,a2
cpy	move.l	(a2)+,(a5)+
	move.l	(a2)+,(a5)+
	move.l	(a2)+,(a5)+
	bra.s	lp1

; set "long name"
longname
	subq.l	#8,a5
	lea	lname,a2
	bra.s	cpy

; the end of the dir was reached, set all remaining to empty
dir_eof lea	mty_str,a2
cpy2	move.l	(a2),(a5)+
	move.l	4(a2),(a5)+
	move.l	8(a2),(a5)+
	move.w	d3,(a5)+
	dbf	d1,cpy2
	bra.s	exit

mty_str dc.l	'-- E','mpty',' -- '	     ;"-- Empty -- "
lname	dc.l	'-Lon','g na','me- '	     ;"-Long name- "


;************************** Extension thing routine ****************
;
; CARD_CRUSH card
;
; deletes sector 0 of the card, so that it may be reformetted as
; direct QL disk. This will, of course destroy all info on the card
;
; card	= 1...4
;
;*******************************************************************
crd_crush
	thg_extn {CRSH},,hd_byte
	movem.l hdt.reg,-(a7)
	bsr	hdt_doact		; this calls the following code in super mode

	subq.w	#1,d7			; from 1...4 to 0...3
	blt	err_bp
	cmp.w	#3,d7
	bgt	err_bp
	move.b	d7,hdl_targ-1(a3)
	moveq	#0,d7

	move.l	#512+ddf_dtop,d0	; sector buffer + pretend drv def block
	jsr	gu_achp0
	bne	err_out 		; oops
	lea	512(a0),a4		; pretend there's a drive defintion block
	move.l	a0,a1			;
	moveq	#1,d2			; d0 is already 0 -read sector 0 of disk
	jsr	hdl_rsint(a3)		; read sector, to make sure there's a card there
	bne.s	cc_ret			; no card exists there
	moveq	#127,d1 		; 128 * long words
cc_clr	move.l	d0,(a1)+
	dbf	d1,cc_clr
	move.l	a0,a1
	jsr	hdl_wsint(a3)		; overwrite sector with 0s!!!!!!!
cc_ret	jmp	gu_rchp 		; release mem & return


	 end


;************************** Extension thing routine ****************
;
; READ_SECT card (1-4),address,sector
;  reads a sector
;  address must point to an empty (filled with 0!!!) space of at lest 1024 bytes
;
;*******************************************************************

thp_xx	dc.w	thp.ulng	  ; 3x compulsory unsigned long
	dc.w	thp.ulng
	dc.w	thp.ulng
	dc.w	0

rsct	thg_extn {RSCT},wsct,thp_xx
	movem.l hdt.reg,-(a7)
	bsr	hdt_doact		; this calls the following code in super mode
	subq.w	#1,d7			; from 1...4 to 0...3
	blt	err_bp
	cmp.w	#3,d7
	bgt	err_bp
	move.b	d7,hdl_targ-1(a3)
	moveq	#0,d7
	move.l	4(a1),d0
	move.l	a1,a5		       ; keep param pointer
	move.l	(a1),a1
	lea	512(a1),a4
	moveq	#1,d2			; d0 is already 0 -read sector 0 of disk
	jmp	hdl_rsint(a3)		; read sector, to make sure there's a card there


;************************** Extension thing routine ****************
;
; CHANGE_FORMAT  long_int
;  changes long int from intel format into motorola format
;
;*******************************************************************
	include 'dev8_keys_qlv'
	xdef	change_format
	xref	ut_rtfd1
change_format
	move.w	sb.gtlin,a2
	jsr	(a2)
	bne.s	float2
	lsl.w	#2,d3
	add.l	d3,$58(a6)
	move.l	(a6,a1.l),d1
	ror.w	#8,d1
	swap	d1
	ror.w	#8,d1
	jmp	ut_rtfd1


;************************** Extension thing routine ****************
;
; WRITE_SECT card,address,sector
;  write a sector to the card.
;  address point to space with 512 bytes for the secctor and 512 empty bytes
;
;*******************************************************************

wsct	thg_extn {WSCT},,thp_xx
	movem.l hdt.reg,-(a7)
	bsr	hdt_doact		; this calls the following code in super mode
	subq.w	#1,d7			; from 1...4 to 0...3
	blt	err_bp
	cmp.w	#3,d7
	bgt	err_bp
	move.b	d7,hdl_targ-1(a3)
	moveq	#0,d7
	move.l	4(a1),d0
	move.l	a1,a5		       ; keep param pointer
	move.l	(a1),a1
	lea	512(a1),a4

	moveq	#1,d2			; d0 is already 0 -read sector 0 of disk
	jmp	hdl_wsint(a3)

	end
