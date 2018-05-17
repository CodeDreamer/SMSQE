; CON2 QL Send characters to CONSOLE  V2.01     1999 Tony Tebby

	section con

	xdef	cn_sbyte
	xdef	cn_smulc

	xref	cn_cksize_p
	xref	cn_donl

	include 'dev8_keys_con'
	include 'dev8_keys_k'
	include 'dev8_keys_err'
	include 'dev8_mac_assert'

;+++
; Send a byte
;
;	d1 c  p byte to send
;	a0 c  p CDB
;	a3 c  p linkage
;
;	status return standard
;---
cn_sbyte
cnsb.reg reg	d1/a1
	movem.l cnsb.reg,-(sp)
	cmp.b	#k.nl,d0		 ; newline?
	beq.s	cnsb_nl 		 ; ... yes

	lea	3(sp),a1		 ; ... no, cheat
	moveq	#0,d1
	moveq	#1,d2
	bsr.s	cn_smulc

	movem.l (sp)+,cnsb.reg
	rts
cnsb_nl
	bclr	#sd..gmod,sd_cattr(a0)	 ; ... newline, not graphic mode now
	tas	sd_nlsta(a0)		 ; set pending nl
	bpl.s	cnsb_exit		 ; there was not one before
	jsr	cn_donl 		 ; ... oh yes there was
	move.b	#$80,sd_nlsta(a0)	 ; and still is
cnsb_exit
	movem.l (sp)+,cnsb.reg
	rts

cnsm_rtok
	moveq	#0,d0
	rts

;+++
; Send multiple bytes
;
;	d1 c  u bytes sent so far
;	d2 c s	bytes to send
;	a0 c  p CDB
;	a1 c  u pointer to characters
;	a3 c  p linkage
;
;	status return standard
;---
cn_smulc
cnsm.reg reg	a3/a6
	sub.w	d1,d2			 ; count
	ble.s	cnsm_rtok		 ; nothing to print, so quit

	movem.l cnsm.reg,-(sp)
	sub.w	d1,a1
	move.l	a1,-(sp)		 ; start of buffer
	add.w	d1,a1
	move.w	d2,-(sp)		 ; number to get
	subq.l	#6,sp			 ; number to get this time + screen base

stk_base equ	$00
stk_slice equ	$04
stk_count equ	$06

	tst.b	sd_sflag(a0)		 ; check if we can fit in
	bpl.s	cnsm_do

	jsr	cn_cksize_p		 ; re-check the size
	blo.l	cnsm_orng		 ; ... oops

cnsm_do
	moveq	#$ff-1<<sd..gmod,d0	 ; print mode (ignoring graphics mode)
	and.b	sd_cattr(a0),d0
	add.w	d0,d0
	lea	cnsm_optab,a5		 ; operation table
	add.w	(a5,d0.w),a5		 ; ... operation

	move.w	sd_linel(a0),a2 	 ; common item

; loop slicing up string

cnsm_sloop
	cmp.b	#k.nl,(a1)		 ; new line?
	bne.s	cnsm_doch		 ; ... no

; newline character found

cnsm_fnl
	bclr	#sd..gmod,sd_cattr(a0)	 ; ... yes, not graphic mode now
	tas	sd_nlsta(a0)		 ; ... yes
	bpl.s	cnsm_enl		 ; there was not one before
	jsr	cn_donl 		 ; ... oh yes there was
	move.b	#$80,sd_nlsta(a0)	 ; and there still is
cnsm_enl
	addq.l	#1,a1
	subq.w	#1,stk_count(sp)	 ; one fewer to go
	bgt.s	cnsm_sloop
	bra.l	cnsm_finished

; process real characters in string

cnsm_doch
	tst.b	sd_nlsta(a0)		 ; pending newline?
	beq.s	cnsm_count		 ; ... no, count characters on this line
	blt.s	cnsm_donl		 ; ... yes genuine

	assert	sd..gmod,7
	tst.b	sd_cattr(a0)		 ; graphic mode?
	bpl.s	cnsm_donl		 ; ... no, do newline

cnsm_lnl
	cmp.b	#k.nl,(a1)+		 ; look for newline
	beq.s	cnsm_egraph
	subq.w	#1,stk_count(sp)
	bgt.s	cnsm_lnl
	bra.l	cnsm_finished		 ; no newline found

cnsm_egraph
	subq.l	#1,a1			 ; backspace to nl
	bra.s	cnsm_fnl		 ; and carry on

cnsm_donl
	jsr	cn_donl 		 ; do newline

; first count how many fit in

cnsm_count
	move.w	sd_xpos(a0),d0
	move.w	d0,d1
	move.w	sd_xinc(a0),d2
	move.w	sd_xsize(a0),d3
	move.w	stk_count(sp),d4
	move.w	d4,d5
	move.l	a1,a3

	moveq	#k.nl,d6
cnsm_cnt
	cmp.b	(a3)+,d6		 ; next is newline
	beq.s	cnsm_scnt
	add.w	d2,d1
	cmp.w	d3,d1			 ; off edge yet?
	bgt.s	cnsm_seol		 ; ... yes
	subq.w	#1,d4			 ; one more gone
	bge.s	cnsm_cnt		 ; ... not all gone yet
	moveq	#0,d4			 ; all gone
	bra.s	cnsm_scnt

cnsm_seol
	move.b	#1,sd_nlsta(a0) 	 ; newline required

cnsm_scnt
	move.w	d4,stk_count(sp)	 ; number left to go
	sub.w	d4,d5			 ; number to go
	beq.s	cnsm_more		 ; back to beginning of slicing loop

	move.w	d5,stk_slice(sp)	 ; number in this slice

	assert	sd.sfout,1
	tst.b	sd_sflag(a0)		 ; out of window?
	beq.s	cnsm_srow		 ; ... no
	add.w	d5,a1			 ; ... yes, skip these
cnsm_more
	tst.w	stk_count(sp)		 ; any more characters?
	bgt.l	cnsm_sloop
	bra.l	cnsm_finished

; set row address

cnsm_srow
	move.w	a2,d2
	move.w	sd_ymin(a0),d0
	add.w	sd_ypos(a0),d0
	mulu	d0,d2			 ; row start in screen
	add.l	sd_scrb(a0),d2
	move.l	d2,stk_base(sp) 	 ; veritable row address

; and colour masks depend on this

	btst	#sd..dwdt,sd_cattr(a0)	  ; double width?
	bne.s	cnsm_cdw		 ; ... yes
	movem.l sd_smask(a0),d1/d2	 ; set colour masks
	move.l	d1,d4			 ; strip even
	swap	d1
	move.w	d1,d4			 ; both halves
	move.l	d1,d5			 ; strip odd
	swap	d1
	move.w	d1,d5			 ; both halves

	move.l	d2,d6			 ; ink even
	swap	d2
	move.w	d2,d6			 ; both halves
	move.l	d2,d7			 ; ink odd
	swap	d2
	move.w	d2,d7			 ; both halves

	asr.w	#1,d0			 ; ... odd line?
	bcc.s	cnsm_doslice		 ; ... no
	exg	d4,d5
	exg	d6,d7
	bra.s	cnsm_doslice

cnsm_cdw
	movem.l sd_smask(a0),d6/d7	 ; set colour masks
	asr.w	#1,d0			 ; ... odd line?
	bcs.s	cnsm_doslice		 ; ... yes
	swap	d6
	swap	d7

; now do a slice

cnsm_doslice
	move.w	sd_xmin(a0),d0		 ; area origin
	add.w	sd_xpos(a0),d0		 ; cursor origin
	move.w	stk_slice(sp),d1
cnsm_cloop
	move.l	stk_base(sp),a4
	move.w	d1,-(sp)
	move.w	d0,-(sp)

	moveq	#7,d2			 ; character shift
	and.w	d0,d2
	lsr.w	#3,d0
	add.w	d0,d0
	add.w	d0,a4			 ; screen address

	moveq	#0,d1
	move.b	(a1)+,d1		 ; next character

	move.l	sd_font(a0),a3		 ; base fount
	sub.b	(a3)+,d1		 ; offset in fount table
	blo.s	cnsm_f2 		 ; ... off bottom
	cmp.b	(a3)+,d1		 ; top
	bls.s	cnsm_fset		 ; ... ok
	subq.l	#1,a3
cnsm_f2
	add.b	-(a3),d1
	move.l	sd_font+4(a0),a3	 ; second fount
	sub.b	(a3)+,d1		 ; offset in fount table
	blo.s	cnsm_f2z		 ; ... off bottom
	cmp.b	(a3),d1 		 ; top
	bls.s	cnsm_fs2		 ; ... ok
cnsm_f2z
	moveq	#0,d1			 ; ... no, use bottom
cnsm_fs2
	addq.l	#1,a3
cnsm_fset
	add.w	d1,a3
	lsl.w	#3,d1
	add.w	d1,a3			 ; at last, the fount pointer

	jmp	(a5)			 ; screen address, fount and colours set

cnsm_next
	move.w	(sp)+,d0
	move.w	(sp)+,d1
	add.w	sd_xinc(a0),d0		 ; new x pos
	subq.w	#1,d1			 ; one more gone
	bgt.s	cnsm_cloop

	sub.w	sd_xmin(a0),d0
	move.w	d0,sd_xpos(a0)

	tst.w	stk_count(sp)		 ; any more characters?
	bgt.l	cnsm_sloop

cnsm_finished
	moveq	#0,d0			 ; ... ok
cnsm_exit
	addq.l	#stk_count+2,sp 	 ; remove frame from stack
	move.l	a1,d1			 ; set character count
	sub.l	(sp)+,d1
	movem.l (sp)+,cnsm.reg
	tst.l	d0
	rts

cnsm_orng
	moveq	#err.orng,d0
	bra.s	cnsm_exit

	page

; table of character operations

cnsm_optab
.
	dc.w	cc_stdch-.,cc_stdul-.,cc_stdch-.,cc_stdul-.
	dc.w	cc_ovrch-.,cc_ovrul-.,cc_ovrch-.,cc_ovrul-.
	dc.w	cc_xorch-.,cc_xorul-.,cc_xorch-.,cc_xorul-.
	dc.w	cc_xorch-.,cc_xorul-.,cc_xorch-.,cc_xorul-.

	dc.w	ch_stdch-.,ch_stdul-.,ch_stdch-.,ch_stdul-.
	dc.w	ch_ovrch-.,ch_ovrul-.,ch_ovrch-.,ch_ovrul-.
	dc.w	ch_xorch-.,ch_xorul-.,ch_xorch-.,ch_xorul-.
	dc.w	ch_xorch-.,ch_xorul-.,ch_xorch-.,ch_xorul-.

	dc.w	cc_stdcx-.,cc_stdux-.,cc_stdcx-.,cc_stdux-.
	dc.w	cc_ovrcx-.,cc_ovrux-.,cc_ovrcx-.,cc_ovrux-.
	dc.w	cc_xorcx-.,cc_xorux-.,cc_xorcx-.,cc_xorux-.
	dc.w	cc_xorcx-.,cc_xorux-.,cc_xorcx-.,cc_xorux-.

	dc.w	ch_stdcx-.,ch_stdux-.,ch_stdcx-.,ch_stdux-.
	dc.w	ch_ovrcx-.,ch_ovrux-.,ch_ovrcx-.,ch_ovrux-.
	dc.w	ch_xorcx-.,ch_xorux-.,ch_xorcx-.,ch_xorux-.
	dc.w	ch_xorcx-.,ch_xorux-.,ch_xorcx-.,ch_xorux-.

	dc.w	cw_stdch-.,cw_stdul-.,cw_stdch-.,cw_stdul-.
	dc.w	cw_ovrch-.,cw_ovrul-.,cw_ovrch-.,cw_ovrul-.
	dc.w	cw_xorch-.,cw_xorul-.,cw_xorch-.,cw_xorul-.
	dc.w	cw_xorch-.,cw_xorul-.,cw_xorch-.,cw_xorul-.

	dc.w	cz_stdch-.,cz_stdul-.,cz_stdch-.,cz_stdul-.
	dc.w	cz_ovrch-.,cz_ovrul-.,cz_ovrch-.,cz_ovrul-.
	dc.w	cz_xorch-.,cz_xorul-.,cz_xorch-.,cz_xorul-.
	dc.w	cz_xorch-.,cz_xorul-.,cz_xorch-.,cz_xorul-.

	dc.w	cw_stdcx-.,cw_stdux-.,cw_stdcx-.,cw_stdux-.
	dc.w	cw_ovrcx-.,cw_ovrux-.,cw_ovrcx-.,cw_ovrux-.
	dc.w	cw_xorcx-.,cw_xorux-.,cw_xorcx-.,cw_xorux-.
	dc.w	cw_xorcx-.,cw_xorux-.,cw_xorcx-.,cw_xorux-.

	dc.w	cz_stdcx-.,cz_stdux-.,cz_stdcx-.,cz_stdux-.
	dc.w	cz_ovrcx-.,cz_ovrux-.,cz_ovrcx-.,cz_ovrux-.
	dc.w	cz_xorcx-.,cz_xorux-.,cz_xorcx-.,cz_xorux-.
	dc.w	cz_xorcx-.,cz_xorux-.,cz_xorcx-.,cz_xorux-.

	page
;+++
; General character drawing code
;
;	d2 cs	character shift
;	d4 c  p strip colour mask first row
;	d5 c  p strip colour mask next row
;	d6 c  p ink colour mask first row
;	d7 c  p ink colour mask next row
;	a2 c  p line increment
;	a3 cs	pointer to fount patterns for this character
;	a4 cs	pointer to screen
;	a5 cr	pointer to character code
;	a6  s	pointer to character split table
;---

;+++
; Standard size / attribute white paper drawing code
;---
cc_stdwp
	moveq	#8,d1			 ; blank + nine rows
	lsl.w	#2,d2			 ; index character mask table
	lea	cc_mask6,a6
	move.l	(a6,d2.w),d3		 ; character mask
	lsl.w	#8,d2			 ; index character split table
	lea	cc_split,a6
	add.w	d2,a6
	tst.w	d3			 ; word or long operation required?
	bne.s	cc_swpl 		 ; ... long

	swap	d3
	move.w	d3,d4
	not.w	d4
	or.w	d3,(a4) 		 ; the strip at the top
	add.w	a2,a4			 ; next row
	exg	d6,d7

cc_swpwl
	moveq	#0,d0
	move.b	(a3)+,d0		 ; next bit
	bne.s	cc_swpwc

	or.w	d3,(a4) 		 ; blank row
	add.w	a2,a4			 ; next row
	exg	d6,d7
	dbra	d1,cc_swpwl
	bra.l	cnsm_next

cc_swpwc
	lsl.w	#2,d0
	move.w	(a6,d0.w),d0		 ; character pattern
	move.w	d0,d2
	not.w	d2
	and.w	d3,d2			 ; the strip
	and.w	d6,d0			 ; the ink
	or.w	d2,d0
	move.w	d4,d2
	and.w	(a4),d2 		 ; the rest of the long word from screen
	or.w	d2,d0
	move.w	d0,(a4) 		 ; back in the screen

	add.w	a2,a4			 ; next row
	exg	d6,d7
	dbra	d1,cc_swpwl
	bra.l	cnsm_next

cc_swpl
	move.l	d3,d4
	not.l	d4
	or.l	d3,(a4) 		 ; the strip at the top
	add.l	a2,a4			 ; next row
	exg	d6,d7

cc_swpll
	moveq	#0,d0
	move.b	(a3)+,d0		 ; next bit
	bne.s	cc_swplc

	or.l	d3,(a4) 		 ; blank row
	add.l	a2,a4			 ; next row
	exg	d6,d7
	dbra	d1,cc_swpll
	bra.l	cnsm_next

cc_swplc
	lsl.w	#2,d0
	move.l	(a6,d0.w),d0		 ; character pattern
	move.l	d0,d2
	not.l	d2
	and.l	d3,d2			 ; the strip
	and.l	d6,d0			 ; the ink
	or.l	d2,d0
	move.l	d4,d2
	and.l	(a4),d2 		 ; the rest of the long word from screen
	or.l	d2,d0
	move.l	d0,(a4) 		 ; back in the screen

	add.l	a2,a4			 ; next row
	exg	d6,d7
	dbra	d1,cc_swpll
	bra.l	cnsm_next

;+++
; Standard size / attribute black ink drawing code
;---
cc_stdbi
	moveq	#9,d1			 ; one + nine rows
	lsl.w	#2,d2			 ; index character mask table
	lea	cc_mask6,a6
	move.l	(a6,d2.w),d3		 ; character mask
	lsl.w	#8,d2			 ; index character split table
	lea	cc_split,a6
	add.w	d2,a6
	tst.w	d3			 ; word or long operation required?
	bne.s	cc_sbil 		 ; ... long

	swap	d3
	move.w	d3,d6
	not.w	d6
	move.w	d3,d0
	bra.s	cc_sbiwb

cc_sbiwl
	moveq	#0,d0
	move.b	(a3)+,d0		 ; next bit
	lsl.w	#2,d0
	move.w	(a6,d0.w),d0		 ; character pattern
	not.w	d0
	and.w	d3,d0			 ; the ink
cc_sbiwb
	and.w	d4,d0			 ; ... strip
	move.w	d6,d2
	and.w	(a4),d2 		 ; the rest of the long from screen
	or.w	d2,d0
	move.w	d0,(a4) 		 ; back in the screen
	add.w	a2,a4			 ; next row
	exg	d4,d5
	dbra	d1,cc_sbiwl
	bra.l	cnsm_next

cc_sbil
	move.l	d3,d6
	not.l	d6
	move.l	d3,d0
	bra.s	cc_sbilb

cc_sbill
	moveq	#0,d0
	move.b	(a3)+,d0		 ; next bit
	lsl.w	#2,d0
	move.l	(a6,d0.w),d0		 ; character pattern
	not.l	d0
	and.l	d3,d0			 ; the ink / strip
cc_sbilb
	and.l	d4,d0
	move.l	d6,d2
	and.l	(a4),d2 		 ; the rest of the long word from screen
	or.l	d2,d0
	move.l	d0,(a4) 		 ; back in the screen

	add.l	a2,a4			 ; next row
	exg	d4,d5
	dbra	d1,cc_sbill
	bra.l	cnsm_next

;+++
; Standard size / attribute black on white drawing code
;---
cc_stdbw
	moveq	#8,d1			 ; one + nine rows
	lsl.w	#2,d2			 ; index character mask table
	lea	cc_mask6,a6
	move.l	(a6,d2.w),d3		 ; character mask
	lsl.w	#8,d2			 ; index character split table
	lea	cc_split,a6
	add.w	d2,a6
	tst.w	d3			 ; word or long operation required?
	bne.s	cc_sbwl 		 ; ... long

	swap	d3
	move.w	d3,d4
	not.w	d4
	or.w	d3,(a4) 		 ; the strip at the top
	add.w	a2,a4			 ; next row

cc_sbwwl
	moveq	#0,d0
	move.b	(a3)+,d0		 ; next bit
	bne.s	cc_sbwwc

	or.w	d3,(a4) 		 ; blank row
	add.w	a2,a4			 ; next row
	dbra	d1,cc_sbwwl
	bra.l	cnsm_next

cc_sbwwc
	lsl.w	#2,d0
	move.w	(a6,d0.w),d0		 ; character pattern
	not.w	d0
	and.w	d3,d0			 ; the ink
	move.w	d4,d2
	and.w	(a4),d2 		 ; the rest of the long word from screen
	or.w	d2,d0
	move.w	d0,(a4) 		 ; back in the screen

	add.w	a2,a4			 ; next row
	dbra	d1,cc_sbwwl
	bra.l	cnsm_next

cc_sbwl
	move.l	d3,d4
	not.l	d4
	or.l	d3,(a4) 		 ; the strip at the top
	add.l	a2,a4			 ; next row

cc_sbwll
	moveq	#0,d0
	move.b	(a3)+,d0		 ; next bit
	bne.s	cc_sbwlc

	or.l	d3,(a4) 		 ; the strip at the top
	add.l	a2,a4			 ; next row
	dbra	d1,cc_sbwll
	bra.l	cnsm_next

cc_sbwlc
	lsl.w	#2,d0
	move.l	(a6,d0.w),d0		 ; character pattern
	not.l	d0
	and.l	d3,d0			 ; the ink
	move.l	d4,d2
	and.l	(a4),d2 		 ; the rest of the long word from screen
	or.l	d2,d0
	move.l	d0,(a4) 		 ; back in the screen

	add.l	a2,a4			 ; next row
	dbra	d1,cc_sbwll
	bra.l	cnsm_next

cc_mask6
	dc.l	$fcfc0000
	dc.l	$7e7e0000
	dc.l	$3f3f0000
	dc.l	$1f1f8080
	dc.l	$0f0fc0c0
	dc.l	$0707e0e0
	dc.l	$0303f0f0
	dc.l	$0101f8f8

;+++
; Standard size / attribute black paper drawing code
;---
cc_stdbp
	moveq	#8,d1			 ; one + nine rows
	lsl.w	#2,d2			 ; index character mask table
	move.l	cc_mask6(pc,d2.w),d3	 ; character mask
	lsl.w	#8,d2			 ; index character split table
	lea	cc_split,a6
	add.w	d2,a6
	tst.w	d3			 ; word or long operation required?
	bne.s	cc_sbpl 		 ; ... long

	swap	d3
	move.w	d3,d4
	not.w	d4
	and.w	d4,(a4) 		 ; the strip at the top
	add.w	a2,a4			 ; next row
	exg	d6,d7

cc_sbpwl
	moveq	#0,d0
	move.b	(a3)+,d0		 ; next bit
	bne.s	cc_sbpwc

	and.w	d4,(a4) 		 ; blank row
	add.w	a2,a4			 ; next row
	exg	d6,d7
	dbra	d1,cc_sbpwl
	bra.l	cnsm_next

cc_sbpwc
	lsl.w	#2,d0
	move.w	(a6,d0.w),d0		 ; character pattern
	and.w	d6,d0			 ; the ink
	move.w	d4,d2
	and.w	(a4),d2 		 ; the rest of the long word from screen
	or.w	d2,d0
	move.w	d0,(a4) 		 ; back in the screen

	add.w	a2,a4			 ; next row
	exg	d6,d7
	dbra	d1,cc_sbpwl
	bra.l	cnsm_next

cc_sbpl
	move.l	d3,d4
	not.l	d4
	and.l	d4,(a4) 		 ; the strip at the top
	add.l	a2,a4			 ; next row
	exg	d6,d7

cc_sbpll
	moveq	#0,d0
	move.b	(a3)+,d0		 ; next bit
	bne.s	cc_sbplc

	and.l	d4,(a4) 		 ; the strip at the top
	add.l	a2,a4			 ; next row
	exg	d6,d7
	dbra	d1,cc_sbpll
	bra.l	cnsm_next

cc_sbplc
	lsl.w	#2,d0
	move.l	(a6,d0.w),d0		 ; character pattern
	and.l	d6,d0			 ; the ink
	move.l	d4,d2
	and.l	(a4),d2 		 ; the rest of the long word from screen
	or.l	d2,d0
	move.l	d0,(a4) 		 ; back in the screen

	add.l	a2,a4			 ; next row
	exg	d6,d7
	dbra	d1,cc_sbpll
	bra.l	cnsm_next

;+++
; Standard size / attribute white on black paper drawing code
;---
cc_stdwb
	moveq	#8,d1			 ; one + nine rows
	lsl.w	#2,d2			 ; index character mask table
	lea	cc_mask6,a6
	move.l	(a6,d2.w),d3		 ; character mask
	lsl.w	#8,d2			 ; index character split table
	lea	cc_split,a6
	add.w	d2,a6
	tst.w	d3			 ; word or long operation required?
	bne.s	cc_swbl 		 ; ... long

	swap	d3
	move.w	d3,d4
	not.w	d4

	and.w	d4,(a4) 		 ; the strip at the top
	add.w	a2,a4			 ; next row

cc_swbwl
	moveq	#0,d0
	move.b	(a3)+,d0		 ; next bit
	bne.s	cc_swbwc

	and.w	d4,(a4) 		 ; blank row
	add.w	a2,a4			 ; next row
	dbra	d1,cc_swbwl
	bra.l	cnsm_next

cc_swbwc
	move.w	d4,d2
	and.w	(a4),d2 		 ; the rest of the long word from screen
	lsl.w	#2,d0
	or.w	(a6,d0.w),d2		 ; character pattern
	move.w	d2,(a4) 		 ; back in the screen

	add.w	a2,a4			 ; next row
	dbra	d1,cc_swbwl
	bra.l	cnsm_next

cc_swbl
	move.l	d3,d4
	not.l	d4

	and.l	d4,(a4) 		 ; the strip at the top
	add.l	a2,a4			 ; next row

cc_swbll
	moveq	#0,d0
	move.b	(a3)+,d0		 ; next bit
	bne.s	cc_swblc

	and.l	d4,(a4) 		 ; the strip at the top
	add.l	a2,a4			 ; next row
	dbra	d1,cc_swbll
	bra.l	cnsm_next

cc_swblc
	move.l	d4,d2
	and.l	(a4),d2 		 ; the rest of the long word from screen
	lsl.w	#2,d0
	or.l	(a6,d0.w),d2		 ; character pattern
	move.l	d2,(a4) 		 ; back in the screen

	add.l	a2,a4			 ; next row
	dbra	d1,cc_swbll
	bra.l	cnsm_next

cc_mask8
	dc.l	$ffff0000
	dc.l	$7f7f8080
	dc.l	$3f3fc0c0
	dc.l	$1f1fe0e0
	dc.l	$0f0ff0f0
	dc.l	$0707f8f8
	dc.l	$0303fcfc
	dc.l	$0101fefe

;+++
; Standard size, underline / extended character drawing code
;---
cc_stdux
	move.l	#$ffff0006,d1		 ; one + seven + two rows
	bra.s	cc_stdx

;+++
; Standard size, extended character drawing code
;---
cc_stdcx
	moveq	#8,d1			 ; one + nine rows
cc_stdx
	lsl.w	#2,d2			 ; index character mask table
	move.l	cc_mask8(pc,d2.w),d3	 ; character mask
	bra.s	cc_stdc2
;+++
; Standard size, underline character drawing code
;---
cc_stdul
	move.l	#$ffff0006,d1		 ; one + seven + two rows
	bra.s	cc_stdc1

;+++
; Standard size / attribute character drawing code
;---
cc_stdch
	move.b	sd_scolr(a0),d0 	 ; black strip?
	bne.s	cc_stdcw
	cmp.b	#7,sd_icolr(a0) 	 ; white ink?
	beq.s	cc_sstwb

	lea	cc_stdbp,a5
	jmp	(a5)			 ; ... no
cc_sstwb
	lea	cc_stdwb,a5		 ; ... yes
	jmp	(a5)

cc_stdcw
	subq.b	#7,d0			 ; white strip?
	bne.s	cc_stdcb
	tst.b	sd_icolr(a0)		 ; black ink
	bne.s	cc_stdsw		 ; ... no
	lea	cc_stdbw,a5		 ; ... yes
	jmp	(a5)

cc_stdsw
	lea	cc_stdwp,a5		 ; white paper
	jmp	(a5)

cc_stdcb
	tst.b	sd_icolr(a0)		 ; black ink?
	bne.s	cc_stde1		 ; ... no
	lea	cc_stdbi,a5
	jmp	(a5)

cc_stde1
	lea	cc_stde2,a5		 ; skip test next time
cc_stde2
	moveq	#8,d1			 ; one + nine rows
cc_stdc1
	lsl.w	#2,d2			 ; index character mask table
	lea	cc_mask6,a6
	move.l	(a6,d2.w),d3		 ; character mask
cc_stdc2
	eor.l	d5,d7
	eor.l	d4,d6			 ; do eor code	*****

	lsl.w	#8,d2			 ; index character split table
	lea	cc_split,a6
	add.w	d2,a6
	tst.w	d3			 ; word or long operation required?
	beq.s	cc_stdw 		 ; ... standard word

	move.l	d4,d2
	and.l	d3,d2			 ; the strip at the top
	move.l	d3,d0
	not.l	d0
	and.l	(a4),d0 		 ; the rest of the long word from screen
	or.l	d2,d0
	move.l	d0,(a4) 		 ; back in the screen

	add.l	a2,a4			 ; next row
	exg	d6,d7
	exg	d4,d5

cc_stdll
	moveq	#0,d0
	move.b	(a3)+,d0		 ; next bit
	bne.s	cc_stdlc

	move.l	d4,d2
	and.l	d3,d2			 ; blank row
	move.l	d3,d0
	not.l	d0
	and.l	(a4),d0 		 ; the rest of the long word from screen
	or.l	d2,d0
	move.l	d0,(a4) 		 ; back in the screen

	add.l	a2,a4			 ; next row
	exg	d6,d7
	exg	d4,d5
	dbra	d1,cc_stdll

	tst.l	d1			 ; underscore?
	bpl.s	cnst_next		 ; ... no
	bra.s	cc_stdlu

cc_stdlc
	lsl.w	#2,d0
	move.l	(a6,d0.w),d2		 ; character pattern
	and.l	d6,d2			 ; the ink
cc_stdls
	eor.l	d4,d2			 ; the new character
	and.l	d3,d2			 ; in its square

	move.l	d3,d0
	not.l	d0
	and.l	(a4),d0 		 ; the rest of the long word from screen
	or.l	d2,d0
	move.l	d0,(a4) 		 ; back in the screen

	add.l	a2,a4			 ; next row
	exg	d6,d7
	exg	d4,d5
	dbra	d1,cc_stdll

	tst.l	d1			 ; underscore?
	bpl.s	cnst_next		 ; ... no

cc_stdlu
	move.l	d6,d2			 ; the underscore
	moveq	#1,d1
	addq.l	#1,a3			 ; skip font line

	bra.s	cc_stdls

cnst_next
	eor.l	d4,d6			 ; restore masks
	eor.l	d5,d7
	bra.l	cnsm_next

cc_stdw
	swap	d3			 ; all in a word

	move.w	d4,d2
	and.w	d3,d2			 ; the row at the top
	move.w	d3,d0
	not.w	d0
	and.w	(a4),d0 		 ; the rest of the word from screen
	or.w	d2,d0
	move.w	d0,(a4) 		 ; back in the screen

	add.l	a2,a4			 ; next row
	exg	d6,d7
	exg	d4,d5

cc_stdwl
	moveq	#0,d0
	move.b	(a3)+,d0		 ; next bit
	bne.s	cc_stdwc

	move.w	d4,d2
	and.w	d3,d2			 ; blank row
	move.w	d3,d0
	not.w	d0
	and.w	(a4),d0 		 ; the rest of the word from screen
	or.w	d2,d0
	move.w	d0,(a4) 		 ; back in the screen

	add.l	a2,a4			 ; next row
	exg	d6,d7
	exg	d4,d5
	dbra	d1,cc_stdwl

	tst.l	d1			 ; underscore?
	bpl.s	cnst_next		 ; ... no
	bra.s	cc_stdwu

cc_stdwc
	lsl.w	#2,d0
	move.w	(a6,d0.w),d2		 ; character pattern
	and.w	d6,d2			 ; the ink
cc_stdws
	eor.w	d4,d2			 ; the new character
	and.w	d3,d2			 ; in its square
	move.w	d3,d0
	not.w	d0
	and.w	(a4),d0 		 ; the rest of the word from screen
	or.w	d2,d0
	move.w	d0,(a4) 		 ; back in the screen

	add.l	a2,a4			 ; next row
	exg	d6,d7
	exg	d4,d5
	dbra	d1,cc_stdwl

	tst.l	d1			 ; underscore?
	bpl.s	cnst_next		 ; ... no

cc_stdwu
	moveq	#1,d1			 ; ... yes, but no more
	addq.l	#1,a3			 ; skip font line
	move.w	d6,d2			 ; the underscore at the bottom
	bra.s	cc_stdws

;+++
; Standard size, underline / over / extended character drawing code
;---
cc_ovrux
	move.l	#$ffff0006,d1		 ; one + seven + two rows
	bra.s	cc_ovrx

;+++
; Standard size, over / extended character drawing code
;---
cc_ovrcx
	moveq	#8,d1			 ; one + nine rows
cc_ovrx
	lea	cc_mask8,a6
	bra.s	cc_ovrc2

;+++
; Standard size, underline / over character drawing code
;---
cc_ovrul
	move.l	#$ffff0006,d1		 ; one + seven + two rows
	bra.s	cc_ovrc1

;+++
; Standard size, over character drawing code
;---
cc_ovrch
	moveq	#8,d1			 ; one + nine rows
cc_ovrc1
	lea	cc_mask6,a6
cc_ovrc2
	lsl.w	#2,d2			 ; index character mask table
	move.l	(a6,d2.w),d3		 ; character mask (for underline!!)
	lsl.w	#8,d2			 ; index character split table
	lea	cc_split,a6
	add.w	d2,a6
	tst.w	d3			 ; word or long operation required?
	beq.s	cc_ovrw 		 ; ... word

	add.l	a2,a4			 ; skip first row
	exg	d6,d7

cc_ovrll
	moveq	#0,d0
	move.b	(a3)+,d0		 ; next bit
	beq.s	cc_ovrln

	lsl.w	#2,d0
	move.l	(a6,d0.w),d0		 ; character pattern
cc_ovrls
	move.l	d0,d2
	not.l	d2			 ; inverse character
	and.l	(a4),d2 		 ; the background
	and.l	d6,d0
	or.l	d2,d0			 ; the new character square
	move.l	d0,(a4) 		 ; back in the screen

cc_ovrln
	add.l	a2,a4			 ; next row
	exg	d6,d7
	dbra	d1,cc_ovrll

	tst.l	d1			 ; underscore?
	bpl.l	cnsm_next		 ; ... no

	moveq	#1,d1
	move.l	d3,d0			 ; the underscore at the bottom
	addq.l	#1,a3			 ; skip font line
	bra.s	cc_ovrls

cc_ovrw
	swap	d3			 ; all in a word

	add.l	a2,a4			 ; skip first row
	exg	d6,d7

cc_ovrwl
	moveq	#0,d0
	move.b	(a3)+,d0		 ; next bit
	beq.s	cc_ovrwn

	lsl.w	#2,d0
	move.w	(a6,d0.w),d0		 ; character pattern
cc_ovrws
	move.w	d0,d2
	not.w	d2			 ; inverse character
	and.w	(a4),d2 		 ; the background
	and.w	d6,d0
	or.w	d2,d0			 ; the new character square
	move.w	d0,(a4) 		 ; back in the screen

cc_ovrwn
	add.l	a2,a4			 ; next row
	exg	d6,d7
	dbra	d1,cc_ovrwl

	tst.l	d1			 ; underscore?
	bpl.l	cnsm_next		 ; ... no
	moveq	#1,d1			 ; ... yes, but no more
	move.l	d3,d0			 ; the underscore at the bottom
	addq.l	#1,a3			 ; skip font line
	bra.s	cc_ovrws

;+++
; Standard size, underline / xor / extended character drawing code
;---
cc_xorux
	move.l	#$ffff0006,d1		 ; one + seven + two rows
	bra.s	cc_xorx

;+++
; Standard size, xor character drawing code
;---
cc_xorcx
	moveq	#8,d1			 ; one + nine rows
cc_xorx
	lea	cc_mask8,a6
	bra.s	cc_xorc2

;+++
; Standard size, underline / xor character drawing code
;---
cc_xorul
	move.l	#$ffff0006,d1		 ; one + seven + two rows
	bra.s	cc_xorc1

;+++
; Standard size, xor character drawing code
;---
cc_xorch
	moveq	#8,d1			 ; one + nine rows
cc_xorc1
	lea	cc_mask6,a6
cc_xorc2
	lsl.w	#2,d2			 ; index character mask table
	move.l	(a6,d2.w),d3		 ; character mask (for underline!!)
	lsl.w	#8,d2			 ; index character split table
	lea	cc_split,a6
	add.w	d2,a6
	tst.w	d3			 ; word or long operation required?
	beq.s	cc_xorw 		 ; ... word

	add.l	a2,a4			 ; next row
	exg	d6,d7

cc_xorll
	moveq	#0,d0
	move.b	(a3)+,d0		 ; next bit
	beq.s	cc_xorln

	lsl.w	#2,d0
	move.l	(a6,d0.w),d0		 ; character pattern
cc_xorls
	and.l	d6,d0
	eor.l	d0,(a4) 		 ; xor into the screen

cc_xorln
	add.l	a2,a4			 ; next row
	exg	d6,d7
	dbra	d1,cc_xorll

	tst.l	d1			 ; underscore?
	bpl.l	cnsm_next		 ; ... no

	moveq	#1,d1
	move.l	d3,d0			 ; the underscore at the bottom
	addq.l	#1,a3			 ; skip font line
	bra.s	cc_xorls

cc_xorw
	swap	d3			 ; all in a word
	add.l	a2,a4			 ; next row
	exg	d6,d7

cc_xorwl
	moveq	#0,d0
	move.b	(a3)+,d0		 ; next bit
	beq.s	cc_xorwn

	lsl.w	#2,d0
	move.w	(a6,d0.w),d0		 ; character pattern
cc_xorws
	and.w	d6,d0
	eor.w	d0,(a4) 		 ; xor into the screen

cc_xorwn
	add.l	a2,a4			 ; next row
	exg	d6,d7
	dbra	d1,cc_xorwl

	tst.l	d1			 ; underscore?
	bpl.l	cnsm_next		 ; ... no

	moveq	#1,d1			 ; ... yes, but no more
	move.l	d3,d0			 ; the underscore at the bottom
	addq.l	#1,a3			 ; skip font line
	bra.s	cc_xorws

	page

;+++
; Double height / underline / extended character drawing code
;---
ch_stdux
	move.l	#$ffff0007,d1		 ; one + seven + two rows
	bra.s	ch_stdx

;+++
; Double height / extended character drawing code
;---
ch_stdcx
	moveq	#9,d1			 ; one + nine rows
ch_stdx
	moveq	#0,d0			 ; standard
	bra.s	ch_allx
;+++
; Double height / underline / over / extended character drawing code
;---
ch_ovrux
	move.l	#$ffff0007,d1		 ; one + seven + two rows
	bra.s	ch_ovrx
;+++
; Double height / over / extended character drawing code
;---
ch_ovrcx
	moveq	#9,d1			 ; one + nine rows
ch_ovrx
	moveq	#1,d0			 ; over
	bra.s	ch_allx

;+++
; Double height / underline / xor / extended character drawing code
;---
ch_xorux
	move.l	#$ffff0007,d1		 ; one + seven + two rows
	bra.s	ch_xorx

;+++
; Double height / xor / extended character drawing code
;---
ch_xorcx
	moveq	#9,d1			 ; one + nine rows
ch_xorx
	moveq	#-1,d0			 ; xor mode

ch_allx
	lea	cc_mask8,a6
	bra.s	ch_alldo

;+++
; Double height / underline character drawing code
;---
ch_stdul
	move.l	#$ffff0007,d1		 ; one + seven + two rows
	bra.s	ch_std

;+++
; Double height character drawing code
;---
ch_stdch
	moveq	#9,d1			 ; one + nine rows
ch_std
	moveq	#0,d0			 ; standard
	bra.s	ch_alls
;+++
; Double height / underline / over character drawing code
;---
ch_ovrul
	move.l	#$ffff0007,d1		 ; one + seven + two rows
	bra.s	ch_ovr
;+++
; Double height / over character drawing code
;---
ch_ovrch
	moveq	#9,d1			 ; one + nine rows
ch_ovr
	moveq	#1,d0			 ; over
	bra.s	ch_alls

;+++
; Double height / underline / xor character drawing code
;---
ch_xorul
	move.l	#$ffff0007,d1		 ; one + seven + two rows
	bra.s	ch_xor

;+++
; Double height / xor character drawing code
;---
ch_xorch
	moveq	#9,d1			 ; one + nine rows
ch_xor
	moveq	#-1,d0			 ; xor mode

ch_alls
	lea	cc_mask6,a6
ch_alldo
	move.l	a0,-(sp)		 ; we need extra regs
	move.b	d0,-(sp)

	lsl.w	#2,d2			 ; index character mask table
	move.l	(a6,d2.w),d3		 ; character mask (for underline!!)
	lsl.w	#8,d2			 ; index character split table
	lea	cc_split,a6
	add.w	d2,a6
	tst.w	d3			 ; word or long operation required?
	beq.l	ch_allw 		 ; ... word
	bra.s	ch_alllb

ch_allll
	moveq	#0,d0
	move.b	(a3)+,d0		 ; next bit
	bne.s	ch_alllc

ch_alllb
	tst.b	(sp)			 ; blank line: over or xor?
	bne.s	ch_allle		 ; ... yes
	move.l	d4,d0
	and.l	d3,d0			 ; the strip at the top
	move.l	d3,d2
	not.l	d2
	and.l	(a4),d2
	or.l	d0,d2
	move.l	d2,(a4)
	add.l	a2,a4

	move.l	d5,d0
	and.l	d3,d0
	move.l	d3,d2
	not.l	d2
	and.l	(a4),d2
	or.l	d0,d2
	move.l	d2,(a4)
	add.l	a2,a4			 ; next row
	dbra	d1,ch_allll

	tst.l	d1			 ; underscore?
	bpl.l	ch_exit 		 ; ... no
	bra.l	ch_alllu

ch_allle
	add.l	a2,a4
	add.l	a2,a4			 ; next row
	dbra	d1,ch_allll

	tst.l	d1			 ; underscore?
	bpl.l	ch_exit 		 ; ... no
	bra.l	ch_alllu


ch_alllc
	lsl.w	#2,d0
	move.l	(a6,d0.w),d0		 ; character pattern
ch_allls
	tst.b	(sp)
	beq.s	ch_alllw
	blt.s	ch_alllx

	move.l	d0,a0
	move.l	d0,d2
	not.l	d2			 ; inverse character
	and.l	(a4),d2 		 ; the background
	and.l	d6,d0
	or.l	d2,d0			 ; the new character square
	move.l	d0,(a4) 		 ; back in the screen
	add.l	a2,a4

	move.l	a0,d0
	move.l	d0,d2
	not.l	d2			 ; inverse character
	and.l	(a4),d2 		 ; the background
	and.l	d7,d0
	or.l	d2,d0			 ; the new character square
	move.l	d0,(a4) 		 ; back in the screen

	add.l	a2,a4			 ; next row
	dbra	d1,ch_allll

	tst.l	d1			 ; underscore?
	bpl.l	ch_exit 		 ; ... no
	bra.s	ch_alllu

ch_alllx
	move.l	d0,a0
	and.l	d6,d0
	eor.l	d0,(a4) 		 ; xor into the screen
	add.l	a2,a4
	move.l	a0,d0
	and.l	d7,d0
	eor.l	d0,(a4) 		 ; xor into the screen

	add.l	a2,a4			 ; next row
	dbra	d1,ch_allll

	tst.l	d1			 ; underscore?
	bpl.l	ch_exit 		 ; ... no
	bra.s	ch_alllu


ch_alllw
	move.l	d0,a0			 ; keep this
	move.l	d0,d2
	not.l	d2			 ; inverse character
	and.l	d4,d2
	and.l	d3,d2			 ; the strip
	and.l	d6,d0			 ; the ink
	or.l	d0,d2			 ; the new character square
	move.l	d3,d0
	not.l	d0
	and.l	(a4),d0 		 ; the rest of the long word from screen
	or.l	d2,d0
	move.l	d0,(a4) 		 ; back in the screen
	add.l	a2,a4

	move.l	a0,d0
	move.l	d0,d2
	not.l	d2			 ; inverse character
	and.l	d5,d2
	and.l	d3,d2			 ; the strip
	and.l	d7,d0			 ; the ink
	or.l	d0,d2			 ; the new character square
	move.l	d3,d0
	not.l	d0
	and.l	(a4),d0 		 ; the rest of the long word from screen
	or.l	d2,d0
	move.l	d0,(a4) 		 ; back in the screen

	add.l	a2,a4			 ; next row
	dbra	d1,ch_allll

	tst.l	d1			 ; underscore?
	bpl.l	ch_exit 		 ; ... no
ch_alllu
	moveq	#1,d1
	move.l	d3,d0			 ; the underscore at the bottom
	addq.l	#1,a3
	bra.l	ch_allls

ch_allw
	swap	d3			 ; all in a word
	bra.s	ch_allwb

ch_allwl
	moveq	#0,d0
	move.b	(a3)+,d0		 ; next bit
	bne.s	ch_allwc

ch_allwb
	tst.b	(sp)			 ; blank line: over or xor?
	bne.s	ch_allwe		 ; ... yes
	move.w	d4,d0
	and.w	d3,d0			 ; the strip at the top
	move.w	d3,d2
	not.w	d2
	and.w	(a4),d2
	or.w	d0,d2
	move.w	d2,(a4)
	add.l	a2,a4

	move.w	d5,d0
	and.w	d3,d0
	move.w	d3,d2
	not.w	d2
	and.w	(a4),d2
	or.w	d0,d2
	move.w	d2,(a4)
	add.l	a2,a4			 ; next row
	dbra	d1,ch_allwl

	tst.l	d1			 ; underscore?
	bpl.l	ch_exit 		 ; ... no
	bra.l	ch_allwu

ch_allwe
	add.l	a2,a4
	add.l	a2,a4			 ; next row
	dbra	d1,ch_allwl

	tst.l	d1			 ; underscore?
	bpl.l	ch_exit 		 ; ... no
	bra.l	ch_allwu


ch_allwc
	lsl.w	#2,d0
	move.w	(a6,d0.w),d0		 ; character pattern
ch_allws
	tst.b	(sp)
	beq.s	ch_allww
	blt.s	ch_allwx

	move.w	d0,a0
	move.w	d0,d2
	not.w	d2			 ; inverse character
	and.w	(a4),d2 		 ; the background
	and.w	d6,d0
	or.w	d2,d0			 ; the new character square
	move.w	d0,(a4) 		 ; back in the screen
	add.l	a2,a4

	move.w	a0,d0
	move.w	d0,d2
	not.w	d2			 ; inverse character
	and.w	(a4),d2 		 ; the background
	and.w	d7,d0
	or.w	d2,d0			 ; the new character square
	move.w	d0,(a4) 		 ; back in the screen

	add.l	a2,a4			 ; next row
	dbra	d1,ch_allwl

	tst.l	d1			 ; underscore?
	bpl.s	ch_exit 		 ; ... no
	bra.s	ch_allwu

ch_allwx
	move.w	d0,a0
	and.w	d6,d0
	eor.w	d0,(a4) 		 ; xor into the screen
	add.w	a2,a4
	move.w	a0,d0
	and.w	d7,d0
	eor.w	d0,(a4) 		 ; xor into the screen

	add.l	a2,a4			 ; next row
	dbra	d1,ch_allwl

	tst.l	d1			 ; underscore?
	bpl.s	ch_exit 		 ; ... no
	bra.s	ch_allwu


ch_allww
	move.w	d0,a0			 ; keep this
	move.w	d0,d2
	not.w	d2			 ; inverse character
	and.w	d4,d2
	and.w	d3,d2			 ; the strip
	and.w	d6,d0			 ; the ink
	or.w	d0,d2			 ; the new character square
	move.w	d3,d0
	not.w	d0
	and.w	(a4),d0 		 ; the rest of the long word from screen
	or.w	d2,d0
	move.w	d0,(a4) 		 ; back in the screen
	add.l	a2,a4

	move.w	a0,d0
	move.w	d0,d2
	not.w	d2			 ; inverse character
	and.w	d5,d2
	and.w	d3,d2			 ; the strip
	and.w	d7,d0			 ; the ink
	or.w	d0,d2			 ; the new character square
	move.w	d3,d0
	not.w	d0
	and.w	(a4),d0 		 ; the rest of the long word from screen
	or.w	d2,d0
	move.w	d0,(a4) 		 ; back in the screen

	add.l	a2,a4			 ; next row
	dbra	d1,ch_allwl

	tst.l	d1			 ; underscore?
	bpl.s	ch_exit 		 ; ... no
ch_allwu
	moveq	#1,d1
	move.l	d3,d0			 ; the underscore at the bottom
	addq.l	#1,a3
	bra.l	ch_allws


ch_exit
	addq.l	#2,sp			 ; clean up the stack
	move.l	(sp)+,a0
	bra.l	cnsm_next

	page
;+++
; Double size / underline / extended character drawing code
;---
cz_stdux
	move.l	#$ffff0007,d1		 ; one + seven + two rows
	bra.s	cz_stdx

;+++
; Double size / extended character drawing code
;---
cz_stdcx
	moveq	#9,d1			 ; ten rows
cz_stdx
	moveq	#0,d0			 ; standard
	bra.s	cz_setx
;+++
; Double size / underline / over / extended character drawing code
;---
cz_ovrux
	move.l	#$ffff0007,d1		 ; one + seven + two rows
	bra.s	cz_ovrx
;+++
; Double size / over / extended character drawing code
;---
cz_ovrcx
	moveq	#9,d1			 ; ten rows
cz_ovrx
	moveq	#1,d0			 ; over
	bra.s	cz_setx

;+++
; Double size / underline / xor / extended character drawing code
;---
cz_xorux
	move.l	#$ffff0007,d1		 ; one + seven + two rows
	bra.s	cz_xorx

;+++
; Double size / xor / extended character drawing code
;---
cz_xorcx
	moveq	#9,d1			 ; ten rows
cz_xorx
	moveq	#-1,d0			 ; xor mode

cz_setx
	moveq	#cw_maskx-cw_mask,d4	 ; extended double width
	bra.s	cz_alldo

;+++
; Double size / underline character drawing code
;---
cz_stdul
	move.l	#$ffff0007,d1		 ; one + seven + two rows
	bra.s	cz_std

;+++
; Double size character drawing code
;---
cz_stdch
	moveq	#9,d1			 ; ten rows
cz_std
	moveq	#0,d0			 ; standard
	bra.s	cz_sets
;+++
; Double size / underline / over character drawing code
;---
cz_ovrul
	move.l	#$ffff0007,d1		 ; one + seven + two rows
	bra.s	cz_ovr
;+++
; Double size / over character drawing code
;---
cz_ovrch
	moveq	#9,d1			 ; ten rows
cz_ovr
	moveq	#1,d0			 ; over
	bra.s	cz_sets

;+++
; Double size / underline / xor character drawing code
;---
cz_xorul
	move.l	#$ffff0007,d1		 ; one + seven + two rows
	bra.s	cz_xor

;+++
; Double size / xor character drawing code
;---
cz_xorch
	moveq	#9,d1			 ; ten rows
cz_xor
	moveq	#-1,d0			 ; xor mode

cz_sets
	moveq	#0,d4			 ; double width
cz_alldo
	moveq	#-1,d5			 ; double height
	bra.l	cw_alldo

;+++
; Double width / underline / extended character drawing code
;---
cw_stdux
	move.l	#$ffff0007,d1		 ; one + seven + two rows
	bra.s	cw_stdx

;+++
; Double width / extended character drawing code
;---
cw_stdcx
	moveq	#9,d1			 ; ten rows
cw_stdx
	moveq	#0,d0			 ; standard
	bra.s	cw_setx
;+++
; Double width / underline / over / extended character drawing code
;---
cw_ovrux
	move.l	#$ffff0007,d1		 ; one + seven + two rows
	bra.s	cw_ovrx
;+++
; Double width / over / extended character drawing code
;---
cw_ovrcx
	moveq	#9,d1			 ; ten rows
cw_ovrx
	moveq	#1,d0			 ; over
	bra.s	cw_setx

;+++
; Double width / underline / xor / extended character drawing code
;---
cw_xorux
	move.l	#$ffff0007,d1		 ; one + seven + two rows
	bra.s	cw_xorx

;+++
; Double width / xor / extended character drawing code
;---
cw_xorcx
	moveq	#9,d1			 ; ten rows
cw_xorx
	moveq	#-1,d0			 ; xor mode

cw_setx
	moveq	#cw_maskx-cw_mask,d4	 ; extended double width
	bra.s	cw_setn

;+++
; Double width / underline character drawing code
;---
cw_stdul
	move.l	#$ffff0007,d1		 ; one + seven + two rows
	bra.s	cw_std

;+++
; Double width character drawing code
;---
cw_stdch
	moveq	#9,d1			 ; ten rows
cw_std
	moveq	#0,d0			 ; standard
	bra.s	cw_sets
;+++
; Double width / underline / over character drawing code
;---
cw_ovrul
	move.l	#$ffff0007,d1		 ; one + seven + two rows
	bra.s	cw_ovr
;+++
; Double width / over character drawing code
;---
cw_ovrch
	moveq	#9,d1			 ; ten rows
cw_ovr
	moveq	#1,d0			 ; over
	bra.s	cw_sets

;+++
; Double width / underline / xor character drawing code
;---
cw_xorul
	move.l	#$ffff0007,d1		 ; one + seven + two rows
	bra.s	cw_xor

;+++
; Double width / xor character drawing code
;---
cw_xorch
	moveq	#9,d1			 ; ten rows
cw_xor
	moveq	#-1,d0			 ; xor mode

cw_sets
	moveq	#0,d4			 ; double width
cw_setn
	moveq	#0,d5			 ; normal height
	bra.s	cw_alldo


cw_mask
	dc.l   $0000F0FF
	dc.l   $0000F87F
	dc.l   $0000FC3F
	dc.l   $0000FE1F
	dc.l   $0000FF0F
	dc.l   $0080FF07
	dc.l   $00C0FF03
	dc.l   $00E0FF01
cw_maskx
	dc.l   $0000FFFF
	dc.l   $0080FF7F
	dc.l   $00C0FF3F
	dc.l   $00E0FF1F
	dc.l   $00F0FF0F
	dc.l   $00F8FF07
	dc.l   $00FCFF03
	dc.l   $00FEFF01

;+++
; Double width
;---
cw_alldo
	movem.l a0/a1/a5,-(sp)		 ; spare reg
	move.b	d0,-(sp)
	move.l	d1,a5
	move.w	d2,d5			 ; shift in pixels
	lsl.w	#2,d2			 ; index mask table by fours
	add.w	d2,d4			 ; mask table
	move.l	cw_mask(pc,d4.w),d4	 ; mask in d4
	lea	cw_spread,a6
	move.l	a4,a1			 ; keep screen address

cw_arst
	move.b	d4,d3
	lsl.w	#8,d3
	move.b	d4,d3			 ; word mask in d3

	bra.s	cw_allb

cw_alll
	moveq	#0,d0
	move.b	(a3)+,d0		 ; next bit
	beq.s	cw_allb

	add.w	d0,d0
	move.w	(a6,d0.w),d0		 ; spread character pattern
	tst.w	d5			 ; left or right shift?
	blt.s	cw_sftl
	lsr.w	d5,d0			 ; left
	bra.s	cw_dupc
cw_sftl
	lsl.w	d5,d0			 ; right
cw_dupc
	move.w	d0,d2
	lsr.w	#8,d2
	move.b	d2,d0			 ; duplicated
	bne.s	cw_alls

cw_allb
	tst.b	(sp)			 ; blank line: over or xor?
	beq.s	cw_allbw		 ; ... no
	tst.l	d5			 ; double height?
	bge.l	cw_alli 		 ; ... no, swap ink
	add.l	a2,a4			 ; ... down a row
	bra.l	cw_alln

cw_allbw
	move.w	d6,d0
	and.w	d3,d0			 ; the strip at the top
	move.w	d3,d2
	not.w	d2
	and.w	(a4),d2
	or.w	d0,d2
	move.w	d2,(a4)
	tst.l	d5			 ; double height?
	bge.l	cw_alle 		 ; ... no
	swap	d6
	add.l	a2,a4
	move.w	d6,d0
	and.w	d3,d0
	move.w	d3,d2
	not.w	d2
	and.w	(a4),d2
	or.w	d0,d2
	move.w	d2,(a4)
	swap	d6
	bra.l	cw_alln

cw_alls
	tst.b	(sp)
	beq.s	cw_allw
	blt.s	cw_allx

	move.w	d0,a0
	move.w	d0,d2
	not.w	d2			 ; inverse character
	and.w	(a4),d2 		 ; the background
	and.w	d7,d0
	or.w	d2,d0			 ; the new character square
	move.w	d0,(a4) 		 ; back in the screen

	tst.l	d5			 ; double height?
	bge.s	cw_alli 		 ; ... no
	swap	d7
	add.l	a2,a4
	move.w	a0,d0
	move.w	d0,d2
	not.w	d2			 ; inverse character
	and.w	(a4),d2 		 ; the background
	and.w	d7,d0
	or.w	d2,d0			 ; the new character square
	move.w	d0,(a4) 		 ; back in the screen

	bra.s	cw_alli

cw_allx
	move.w	d0,a0
	and.w	d7,d0
	eor.w	d0,(a4) 		 ; xor into the screen

	tst.l	d5			 ; double height?
	bge.s	cw_alli 		 ; ... no
	swap	d7
	add.l	a2,a4
	move.w	a0,d0
	and.w	d7,d0
	eor.w	d0,(a4) 		 ; xor into the screen

	bra.s	cw_alli

cw_allw
	move.w	d0,a0			 ; keep this
	move.w	d0,d2
	not.w	d2			 ; inverse character
	and.w	d6,d2
	and.w	d3,d2			 ; the strip
	and.w	d7,d0			 ; the ink
	or.w	d0,d2			 ; the new character square
	move.w	d3,d0
	not.w	d0
	and.w	(a4),d0 		 ; the rest of the long word from screen
	or.w	d2,d0
	move.w	d0,(a4) 		 ; back in the screen

	tst.l	d5			 ; double height?
	bge.s	cw_alle 		 ; ... no
	swap	d6
	swap	d7
	add.l	a2,a4
	move.w	a0,d0
	move.w	d0,d2
	not.w	d2			 ; inverse character
	and.w	d6,d2
	and.w	d3,d2			 ; the strip
	and.w	d7,d0			 ; the ink
	or.w	d0,d2			 ; the new character square
	move.w	d3,d0
	not.w	d0
	and.w	(a4),d0 		 ; the rest of the long word from screen
	or.w	d2,d0
	move.w	d0,(a4) 		 ; back in the screen

cw_alle
	swap	d6
cw_alli
	swap	d7
cw_alln
	add.l	a2,a4			 ; next row
	dbra	d1,cw_alll

	tst.l	d1			 ; underscore?
	bpl.s	cw_ncol 		 ; ... no
	moveq	#1,d1
	move.l	d3,d0			 ; the underscore at the bottom
	addq.l	#1,a3
	bra.l	cw_alls

cw_ncol
	lsr.l	#8,d4			 ; mask
	beq.s	cw_exit 		 ; ... no more
	tst.w	d5			 ; next is shift left
	blt.s	cw_adds 		 ; is was already
	not.w	d5
	addq.b	#1,d5
cw_adds
	addq.b	#8,d5			 ; shift left more
	addq.l	#2,a1
	move.l	a1,a4			 ; updated screen address
	sub.w	#9,a3			 ; fount address
	move.l	a5,d1			 ; count
	bra.l	cw_arst

cw_exit
	addq.l	#2,sp			 ; clean up the stack
	movem.l (sp)+,a0/a1/a5
	bra.l	cnsm_next

	section ctab

cw_spread
	dc.w	$0000,$0003,$000C,$000F,$0030,$0033,$003C,$003F
	dc.w	$00C0,$00C3,$00CC,$00CF,$00F0,$00F3,$00FC,$00FF
	dc.w	$0300,$0303,$030C,$030F,$0330,$0333,$033C,$033F
	dc.w	$03C0,$03C3,$03CC,$03CF,$03F0,$03F3,$03FC,$03FF
	dc.w	$0C00,$0C03,$0C0C,$0C0F,$0C30,$0C33,$0C3C,$0C3F
	dc.w	$0CC0,$0CC3,$0CCC,$0CCF,$0CF0,$0CF3,$0CFC,$0CFF
	dc.w	$0F00,$0F03,$0F0C,$0F0F,$0F30,$0F33,$0F3C,$0F3F
	dc.w	$0FC0,$0FC3,$0FCC,$0FCF,$0FF0,$0FF3,$0FFC,$0FFF

	dc.w	$3000,$3003,$300C,$300F,$3030,$3033,$303C,$303F
	dc.w	$30C0,$30C3,$30CC,$30CF,$30F0,$30F3,$30FC,$30FF
	dc.w	$3300,$3303,$330C,$330F,$3330,$3333,$333C,$333F
	dc.w	$33C0,$33C3,$33CC,$33CF,$33F0,$33F3,$33FC,$33FF
	dc.w	$3C00,$3C03,$3C0C,$3C0F,$3C30,$3C33,$3C3C,$3C3F
	dc.w	$3CC0,$3CC3,$3CCC,$3CCF,$3CF0,$3CF3,$3CFC,$3CFF
	dc.w	$3F00,$3F03,$3F0C,$3F0F,$3F30,$3F33,$3F3C,$3F3F
	dc.w	$3FC0,$3FC3,$3FCC,$3FCF,$3FF0,$3FF3,$3FFC,$3FFF

	dc.w	$C000,$C003,$C00C,$C00F,$C030,$C033,$C03C,$C03F
	dc.w	$C0C0,$C0C3,$C0CC,$C0CF,$C0F0,$C0F3,$C0FC,$C0FF
	dc.w	$C300,$C303,$C30C,$C30F,$C330,$C333,$C33C,$C33F
	dc.w	$C3C0,$C3C3,$C3CC,$C3CF,$C3F0,$C3F3,$C3FC,$C3FF
	dc.w	$CC00,$CC03,$CC0C,$CC0F,$CC30,$CC33,$CC3C,$CC3F
	dc.w	$CCC0,$CCC3,$CCCC,$CCCF,$CCF0,$CCF3,$CCFC,$CCFF
	dc.w	$CF00,$CF03,$CF0C,$CF0F,$CF30,$CF33,$CF3C,$CF3F
	dc.w	$CFC0,$CFC3,$CFCC,$CFCF,$CFF0,$CFF3,$CFFC,$CFFF

	dc.w	$F000,$F003,$F00C,$F00F,$F030,$F033,$F03C,$F03F
	dc.w	$F0C0,$F0C3,$F0CC,$F0CF,$F0F0,$F0F3,$F0FC,$F0FF
	dc.w	$F300,$F303,$F30C,$F30F,$F330,$F333,$F33C,$F33F
	dc.w	$F3C0,$F3C3,$F3CC,$F3CF,$F3F0,$F3F3,$F3FC,$F3FF
	dc.w	$FC00,$FC03,$FC0C,$FC0F,$FC30,$FC33,$FC3C,$FC3F
	dc.w	$FCC0,$FCC3,$FCCC,$FCCF,$FCF0,$FCF3,$FCFC,$FCFF
	dc.w	$FF00,$FF03,$FF0C,$FF0F,$FF30,$FF33,$FF3C,$FF3F
	dc.w	$FFC0,$FFC3,$FFCC,$FFCF,$FFF0,$FFF3,$FFFC,$FFFF

cc_split
	dc.l	$00000000,$01010000,$02020000,$03030000
	dc.l	$04040000,$05050000,$06060000,$07070000
	dc.l	$08080000,$09090000,$0A0A0000,$0B0B0000
	dc.l	$0C0C0000,$0D0D0000,$0E0E0000,$0F0F0000
	dc.l	$10100000,$11110000,$12120000,$13130000
	dc.l	$14140000,$15150000,$16160000,$17170000
	dc.l	$18180000,$19190000,$1A1A0000,$1B1B0000
	dc.l	$1C1C0000,$1D1D0000,$1E1E0000,$1F1F0000
	dc.l	$20200000,$21210000,$22220000,$23230000
	dc.l	$24240000,$25250000,$26260000,$27270000
	dc.l	$28280000,$29290000,$2A2A0000,$2B2B0000
	dc.l	$2C2C0000,$2D2D0000,$2E2E0000,$2F2F0000
	dc.l	$30300000,$31310000,$32320000,$33330000
	dc.l	$34340000,$35350000,$36360000,$37370000
	dc.l	$38380000,$39390000,$3A3A0000,$3B3B0000
	dc.l	$3C3C0000,$3D3D0000,$3E3E0000,$3F3F0000
	dc.l	$40400000,$41410000,$42420000,$43430000
	dc.l	$44440000,$45450000,$46460000,$47470000
	dc.l	$48480000,$49490000,$4A4A0000,$4B4B0000
	dc.l	$4C4C0000,$4D4D0000,$4E4E0000,$4F4F0000
	dc.l	$50500000,$51510000,$52520000,$53530000
	dc.l	$54540000,$55550000,$56560000,$57570000
	dc.l	$58580000,$59590000,$5A5A0000,$5B5B0000
	dc.l	$5C5C0000,$5D5D0000,$5E5E0000,$5F5F0000
	dc.l	$60600000,$61610000,$62620000,$63630000
	dc.l	$64640000,$65650000,$66660000,$67670000
	dc.l	$68680000,$69690000,$6A6A0000,$6B6B0000
	dc.l	$6C6C0000,$6D6D0000,$6E6E0000,$6F6F0000
	dc.l	$70700000,$71710000,$72720000,$73730000
	dc.l	$74740000,$75750000,$76760000,$77770000
	dc.l	$78780000,$79790000,$7A7A0000,$7B7B0000
	dc.l	$7C7C0000,$7D7D0000,$7E7E0000,$7F7F0000
	dc.l	$80800000,$81810000,$82820000,$83830000
	dc.l	$84840000,$85850000,$86860000,$87870000
	dc.l	$88880000,$89890000,$8A8A0000,$8B8B0000
	dc.l	$8C8C0000,$8D8D0000,$8E8E0000,$8F8F0000
	dc.l	$90900000,$91910000,$92920000,$93930000
	dc.l	$94940000,$95950000,$96960000,$97970000
	dc.l	$98980000,$99990000,$9A9A0000,$9B9B0000
	dc.l	$9C9C0000,$9D9D0000,$9E9E0000,$9F9F0000
	dc.l	$A0A00000,$A1A10000,$A2A20000,$A3A30000
	dc.l	$A4A40000,$A5A50000,$A6A60000,$A7A70000
	dc.l	$A8A80000,$A9A90000,$AAAA0000,$ABAB0000
	dc.l	$ACAC0000,$ADAD0000,$AEAE0000,$AFAF0000
	dc.l	$B0B00000,$B1B10000,$B2B20000,$B3B30000
	dc.l	$B4B40000,$B5B50000,$B6B60000,$B7B70000
	dc.l	$B8B80000,$B9B90000,$BABA0000,$BBBB0000
	dc.l	$BCBC0000,$BDBD0000,$BEBE0000,$BFBF0000
	dc.l	$C0C00000,$C1C10000,$C2C20000,$C3C30000
	dc.l	$C4C40000,$C5C50000,$C6C60000,$C7C70000
	dc.l	$C8C80000,$C9C90000,$CACA0000,$CBCB0000
	dc.l	$CCCC0000,$CDCD0000,$CECE0000,$CFCF0000
	dc.l	$D0D00000,$D1D10000,$D2D20000,$D3D30000
	dc.l	$D4D40000,$D5D50000,$D6D60000,$D7D70000
	dc.l	$D8D80000,$D9D90000,$DADA0000,$DBDB0000
	dc.l	$DCDC0000,$DDDD0000,$DEDE0000,$DFDF0000
	dc.l	$E0E00000,$E1E10000,$E2E20000,$E3E30000
	dc.l	$E4E40000,$E5E50000,$E6E60000,$E7E70000
	dc.l	$E8E80000,$E9E90000,$EAEA0000,$EBEB0000
	dc.l	$ECEC0000,$EDED0000,$EEEE0000,$EFEF0000
	dc.l	$F0F00000,$F1F10000,$F2F20000,$F3F30000
	dc.l	$F4F40000,$F5F50000,$F6F60000,$F7F70000
	dc.l	$F8F80000,$F9F90000,$FAFA0000,$FBFB0000
	dc.l	$FCFC0000,$FDFD0000,$FEFE0000,$FFFF0000

	dc.l	$00000000,$00008080,$01010000,$01018080
	dc.l	$02020000,$02028080,$03030000,$03038080
	dc.l	$04040000,$04048080,$05050000,$05058080
	dc.l	$06060000,$06068080,$07070000,$07078080
	dc.l	$08080000,$08088080,$09090000,$09098080
	dc.l	$0A0A0000,$0A0A8080,$0B0B0000,$0B0B8080
	dc.l	$0C0C0000,$0C0C8080,$0D0D0000,$0D0D8080
	dc.l	$0E0E0000,$0E0E8080,$0F0F0000,$0F0F8080
	dc.l	$10100000,$10108080,$11110000,$11118080
	dc.l	$12120000,$12128080,$13130000,$13138080
	dc.l	$14140000,$14148080,$15150000,$15158080
	dc.l	$16160000,$16168080,$17170000,$17178080
	dc.l	$18180000,$18188080,$19190000,$19198080
	dc.l	$1A1A0000,$1A1A8080,$1B1B0000,$1B1B8080
	dc.l	$1C1C0000,$1C1C8080,$1D1D0000,$1D1D8080
	dc.l	$1E1E0000,$1E1E8080,$1F1F0000,$1F1F8080
	dc.l	$20200000,$20208080,$21210000,$21218080
	dc.l	$22220000,$22228080,$23230000,$23238080
	dc.l	$24240000,$24248080,$25250000,$25258080
	dc.l	$26260000,$26268080,$27270000,$27278080
	dc.l	$28280000,$28288080,$29290000,$29298080
	dc.l	$2A2A0000,$2A2A8080,$2B2B0000,$2B2B8080
	dc.l	$2C2C0000,$2C2C8080,$2D2D0000,$2D2D8080
	dc.l	$2E2E0000,$2E2E8080,$2F2F0000,$2F2F8080
	dc.l	$30300000,$30308080,$31310000,$31318080
	dc.l	$32320000,$32328080,$33330000,$33338080
	dc.l	$34340000,$34348080,$35350000,$35358080
	dc.l	$36360000,$36368080,$37370000,$37378080
	dc.l	$38380000,$38388080,$39390000,$39398080
	dc.l	$3A3A0000,$3A3A8080,$3B3B0000,$3B3B8080
	dc.l	$3C3C0000,$3C3C8080,$3D3D0000,$3D3D8080
	dc.l	$3E3E0000,$3E3E8080,$3F3F0000,$3F3F8080
	dc.l	$40400000,$40408080,$41410000,$41418080
	dc.l	$42420000,$42428080,$43430000,$43438080
	dc.l	$44440000,$44448080,$45450000,$45458080
	dc.l	$46460000,$46468080,$47470000,$47478080
	dc.l	$48480000,$48488080,$49490000,$49498080
	dc.l	$4A4A0000,$4A4A8080,$4B4B0000,$4B4B8080
	dc.l	$4C4C0000,$4C4C8080,$4D4D0000,$4D4D8080
	dc.l	$4E4E0000,$4E4E8080,$4F4F0000,$4F4F8080
	dc.l	$50500000,$50508080,$51510000,$51518080
	dc.l	$52520000,$52528080,$53530000,$53538080
	dc.l	$54540000,$54548080,$55550000,$55558080
	dc.l	$56560000,$56568080,$57570000,$57578080
	dc.l	$58580000,$58588080,$59590000,$59598080
	dc.l	$5A5A0000,$5A5A8080,$5B5B0000,$5B5B8080
	dc.l	$5C5C0000,$5C5C8080,$5D5D0000,$5D5D8080
	dc.l	$5E5E0000,$5E5E8080,$5F5F0000,$5F5F8080
	dc.l	$60600000,$60608080,$61610000,$61618080
	dc.l	$62620000,$62628080,$63630000,$63638080
	dc.l	$64640000,$64648080,$65650000,$65658080
	dc.l	$66660000,$66668080,$67670000,$67678080
	dc.l	$68680000,$68688080,$69690000,$69698080
	dc.l	$6A6A0000,$6A6A8080,$6B6B0000,$6B6B8080
	dc.l	$6C6C0000,$6C6C8080,$6D6D0000,$6D6D8080
	dc.l	$6E6E0000,$6E6E8080,$6F6F0000,$6F6F8080
	dc.l	$70700000,$70708080,$71710000,$71718080
	dc.l	$72720000,$72728080,$73730000,$73738080
	dc.l	$74740000,$74748080,$75750000,$75758080
	dc.l	$76760000,$76768080,$77770000,$77778080
	dc.l	$78780000,$78788080,$79790000,$79798080
	dc.l	$7A7A0000,$7A7A8080,$7B7B0000,$7B7B8080
	dc.l	$7C7C0000,$7C7C8080,$7D7D0000,$7D7D8080
	dc.l	$7E7E0000,$7E7E8080,$7F7F0000,$7F7F8080

	dc.l	$00000000,$00004040,$00008080,$0000C0C0
	dc.l	$01010000,$01014040,$01018080,$0101C0C0
	dc.l	$02020000,$02024040,$02028080,$0202C0C0
	dc.l	$03030000,$03034040,$03038080,$0303C0C0
	dc.l	$04040000,$04044040,$04048080,$0404C0C0
	dc.l	$05050000,$05054040,$05058080,$0505C0C0
	dc.l	$06060000,$06064040,$06068080,$0606C0C0
	dc.l	$07070000,$07074040,$07078080,$0707C0C0
	dc.l	$08080000,$08084040,$08088080,$0808C0C0
	dc.l	$09090000,$09094040,$09098080,$0909C0C0
	dc.l	$0A0A0000,$0A0A4040,$0A0A8080,$0A0AC0C0
	dc.l	$0B0B0000,$0B0B4040,$0B0B8080,$0B0BC0C0
	dc.l	$0C0C0000,$0C0C4040,$0C0C8080,$0C0CC0C0
	dc.l	$0D0D0000,$0D0D4040,$0D0D8080,$0D0DC0C0
	dc.l	$0E0E0000,$0E0E4040,$0E0E8080,$0E0EC0C0
	dc.l	$0F0F0000,$0F0F4040,$0F0F8080,$0F0FC0C0
	dc.l	$10100000,$10104040,$10108080,$1010C0C0
	dc.l	$11110000,$11114040,$11118080,$1111C0C0
	dc.l	$12120000,$12124040,$12128080,$1212C0C0
	dc.l	$13130000,$13134040,$13138080,$1313C0C0
	dc.l	$14140000,$14144040,$14148080,$1414C0C0
	dc.l	$15150000,$15154040,$15158080,$1515C0C0
	dc.l	$16160000,$16164040,$16168080,$1616C0C0
	dc.l	$17170000,$17174040,$17178080,$1717C0C0
	dc.l	$18180000,$18184040,$18188080,$1818C0C0
	dc.l	$19190000,$19194040,$19198080,$1919C0C0
	dc.l	$1A1A0000,$1A1A4040,$1A1A8080,$1A1AC0C0
	dc.l	$1B1B0000,$1B1B4040,$1B1B8080,$1B1BC0C0
	dc.l	$1C1C0000,$1C1C4040,$1C1C8080,$1C1CC0C0
	dc.l	$1D1D0000,$1D1D4040,$1D1D8080,$1D1DC0C0
	dc.l	$1E1E0000,$1E1E4040,$1E1E8080,$1E1EC0C0
	dc.l	$1F1F0000,$1F1F4040,$1F1F8080,$1F1FC0C0
	dc.l	$20200000,$20204040,$20208080,$2020C0C0
	dc.l	$21210000,$21214040,$21218080,$2121C0C0
	dc.l	$22220000,$22224040,$22228080,$2222C0C0
	dc.l	$23230000,$23234040,$23238080,$2323C0C0
	dc.l	$24240000,$24244040,$24248080,$2424C0C0
	dc.l	$25250000,$25254040,$25258080,$2525C0C0
	dc.l	$26260000,$26264040,$26268080,$2626C0C0
	dc.l	$27270000,$27274040,$27278080,$2727C0C0
	dc.l	$28280000,$28284040,$28288080,$2828C0C0
	dc.l	$29290000,$29294040,$29298080,$2929C0C0
	dc.l	$2A2A0000,$2A2A4040,$2A2A8080,$2A2AC0C0
	dc.l	$2B2B0000,$2B2B4040,$2B2B8080,$2B2BC0C0
	dc.l	$2C2C0000,$2C2C4040,$2C2C8080,$2C2CC0C0
	dc.l	$2D2D0000,$2D2D4040,$2D2D8080,$2D2DC0C0
	dc.l	$2E2E0000,$2E2E4040,$2E2E8080,$2E2EC0C0
	dc.l	$2F2F0000,$2F2F4040,$2F2F8080,$2F2FC0C0
	dc.l	$30300000,$30304040,$30308080,$3030C0C0
	dc.l	$31310000,$31314040,$31318080,$3131C0C0
	dc.l	$32320000,$32324040,$32328080,$3232C0C0
	dc.l	$33330000,$33334040,$33338080,$3333C0C0
	dc.l	$34340000,$34344040,$34348080,$3434C0C0
	dc.l	$35350000,$35354040,$35358080,$3535C0C0
	dc.l	$36360000,$36364040,$36368080,$3636C0C0
	dc.l	$37370000,$37374040,$37378080,$3737C0C0
	dc.l	$38380000,$38384040,$38388080,$3838C0C0
	dc.l	$39390000,$39394040,$39398080,$3939C0C0
	dc.l	$3A3A0000,$3A3A4040,$3A3A8080,$3A3AC0C0
	dc.l	$3B3B0000,$3B3B4040,$3B3B8080,$3B3BC0C0
	dc.l	$3C3C0000,$3C3C4040,$3C3C8080,$3C3CC0C0
	dc.l	$3D3D0000,$3D3D4040,$3D3D8080,$3D3DC0C0
	dc.l	$3E3E0000,$3E3E4040,$3E3E8080,$3E3EC0C0
	dc.l	$3F3F0000,$3F3F4040,$3F3F8080,$3F3FC0C0

	dc.l	$00000000,$00002020,$00004040,$00006060
	dc.l	$00008080,$0000A0A0,$0000C0C0,$0000E0E0
	dc.l	$01010000,$01012020,$01014040,$01016060
	dc.l	$01018080,$0101A0A0,$0101C0C0,$0101E0E0
	dc.l	$02020000,$02022020,$02024040,$02026060
	dc.l	$02028080,$0202A0A0,$0202C0C0,$0202E0E0
	dc.l	$03030000,$03032020,$03034040,$03036060
	dc.l	$03038080,$0303A0A0,$0303C0C0,$0303E0E0
	dc.l	$04040000,$04042020,$04044040,$04046060
	dc.l	$04048080,$0404A0A0,$0404C0C0,$0404E0E0
	dc.l	$05050000,$05052020,$05054040,$05056060
	dc.l	$05058080,$0505A0A0,$0505C0C0,$0505E0E0
	dc.l	$06060000,$06062020,$06064040,$06066060
	dc.l	$06068080,$0606A0A0,$0606C0C0,$0606E0E0
	dc.l	$07070000,$07072020,$07074040,$07076060
	dc.l	$07078080,$0707A0A0,$0707C0C0,$0707E0E0
	dc.l	$08080000,$08082020,$08084040,$08086060
	dc.l	$08088080,$0808A0A0,$0808C0C0,$0808E0E0
	dc.l	$09090000,$09092020,$09094040,$09096060
	dc.l	$09098080,$0909A0A0,$0909C0C0,$0909E0E0
	dc.l	$0A0A0000,$0A0A2020,$0A0A4040,$0A0A6060
	dc.l	$0A0A8080,$0A0AA0A0,$0A0AC0C0,$0A0AE0E0
	dc.l	$0B0B0000,$0B0B2020,$0B0B4040,$0B0B6060
	dc.l	$0B0B8080,$0B0BA0A0,$0B0BC0C0,$0B0BE0E0
	dc.l	$0C0C0000,$0C0C2020,$0C0C4040,$0C0C6060
	dc.l	$0C0C8080,$0C0CA0A0,$0C0CC0C0,$0C0CE0E0
	dc.l	$0D0D0000,$0D0D2020,$0D0D4040,$0D0D6060
	dc.l	$0D0D8080,$0D0DA0A0,$0D0DC0C0,$0D0DE0E0
	dc.l	$0E0E0000,$0E0E2020,$0E0E4040,$0E0E6060
	dc.l	$0E0E8080,$0E0EA0A0,$0E0EC0C0,$0E0EE0E0
	dc.l	$0F0F0000,$0F0F2020,$0F0F4040,$0F0F6060
	dc.l	$0F0F8080,$0F0FA0A0,$0F0FC0C0,$0F0FE0E0
	dc.l	$10100000,$10102020,$10104040,$10106060
	dc.l	$10108080,$1010A0A0,$1010C0C0,$1010E0E0
	dc.l	$11110000,$11112020,$11114040,$11116060
	dc.l	$11118080,$1111A0A0,$1111C0C0,$1111E0E0
	dc.l	$12120000,$12122020,$12124040,$12126060
	dc.l	$12128080,$1212A0A0,$1212C0C0,$1212E0E0
	dc.l	$13130000,$13132020,$13134040,$13136060
	dc.l	$13138080,$1313A0A0,$1313C0C0,$1313E0E0
	dc.l	$14140000,$14142020,$14144040,$14146060
	dc.l	$14148080,$1414A0A0,$1414C0C0,$1414E0E0
	dc.l	$15150000,$15152020,$15154040,$15156060
	dc.l	$15158080,$1515A0A0,$1515C0C0,$1515E0E0
	dc.l	$16160000,$16162020,$16164040,$16166060
	dc.l	$16168080,$1616A0A0,$1616C0C0,$1616E0E0
	dc.l	$17170000,$17172020,$17174040,$17176060
	dc.l	$17178080,$1717A0A0,$1717C0C0,$1717E0E0
	dc.l	$18180000,$18182020,$18184040,$18186060
	dc.l	$18188080,$1818A0A0,$1818C0C0,$1818E0E0
	dc.l	$19190000,$19192020,$19194040,$19196060
	dc.l	$19198080,$1919A0A0,$1919C0C0,$1919E0E0
	dc.l	$1A1A0000,$1A1A2020,$1A1A4040,$1A1A6060
	dc.l	$1A1A8080,$1A1AA0A0,$1A1AC0C0,$1A1AE0E0
	dc.l	$1B1B0000,$1B1B2020,$1B1B4040,$1B1B6060
	dc.l	$1B1B8080,$1B1BA0A0,$1B1BC0C0,$1B1BE0E0
	dc.l	$1C1C0000,$1C1C2020,$1C1C4040,$1C1C6060
	dc.l	$1C1C8080,$1C1CA0A0,$1C1CC0C0,$1C1CE0E0
	dc.l	$1D1D0000,$1D1D2020,$1D1D4040,$1D1D6060
	dc.l	$1D1D8080,$1D1DA0A0,$1D1DC0C0,$1D1DE0E0
	dc.l	$1E1E0000,$1E1E2020,$1E1E4040,$1E1E6060
	dc.l	$1E1E8080,$1E1EA0A0,$1E1EC0C0,$1E1EE0E0
	dc.l	$1F1F0000,$1F1F2020,$1F1F4040,$1F1F6060
	dc.l	$1F1F8080,$1F1FA0A0,$1F1FC0C0,$1F1FE0E0

	dc.l	$00000000,$00001010,$00002020,$00003030
	dc.l	$00004040,$00005050,$00006060,$00007070
	dc.l	$00008080,$00009090,$0000A0A0,$0000B0B0
	dc.l	$0000C0C0,$0000D0D0,$0000E0E0,$0000F0F0
	dc.l	$01010000,$01011010,$01012020,$01013030
	dc.l	$01014040,$01015050,$01016060,$01017070
	dc.l	$01018080,$01019090,$0101A0A0,$0101B0B0
	dc.l	$0101C0C0,$0101D0D0,$0101E0E0,$0101F0F0
	dc.l	$02020000,$02021010,$02022020,$02023030
	dc.l	$02024040,$02025050,$02026060,$02027070
	dc.l	$02028080,$02029090,$0202A0A0,$0202B0B0
	dc.l	$0202C0C0,$0202D0D0,$0202E0E0,$0202F0F0
	dc.l	$03030000,$03031010,$03032020,$03033030
	dc.l	$03034040,$03035050,$03036060,$03037070
	dc.l	$03038080,$03039090,$0303A0A0,$0303B0B0
	dc.l	$0303C0C0,$0303D0D0,$0303E0E0,$0303F0F0
	dc.l	$04040000,$04041010,$04042020,$04043030
	dc.l	$04044040,$04045050,$04046060,$04047070
	dc.l	$04048080,$04049090,$0404A0A0,$0404B0B0
	dc.l	$0404C0C0,$0404D0D0,$0404E0E0,$0404F0F0
	dc.l	$05050000,$05051010,$05052020,$05053030
	dc.l	$05054040,$05055050,$05056060,$05057070
	dc.l	$05058080,$05059090,$0505A0A0,$0505B0B0
	dc.l	$0505C0C0,$0505D0D0,$0505E0E0,$0505F0F0
	dc.l	$06060000,$06061010,$06062020,$06063030
	dc.l	$06064040,$06065050,$06066060,$06067070
	dc.l	$06068080,$06069090,$0606A0A0,$0606B0B0
	dc.l	$0606C0C0,$0606D0D0,$0606E0E0,$0606F0F0
	dc.l	$07070000,$07071010,$07072020,$07073030
	dc.l	$07074040,$07075050,$07076060,$07077070
	dc.l	$07078080,$07079090,$0707A0A0,$0707B0B0
	dc.l	$0707C0C0,$0707D0D0,$0707E0E0,$0707F0F0
	dc.l	$08080000,$08081010,$08082020,$08083030
	dc.l	$08084040,$08085050,$08086060,$08087070
	dc.l	$08088080,$08089090,$0808A0A0,$0808B0B0
	dc.l	$0808C0C0,$0808D0D0,$0808E0E0,$0808F0F0
	dc.l	$09090000,$09091010,$09092020,$09093030
	dc.l	$09094040,$09095050,$09096060,$09097070
	dc.l	$09098080,$09099090,$0909A0A0,$0909B0B0
	dc.l	$0909C0C0,$0909D0D0,$0909E0E0,$0909F0F0
	dc.l	$0A0A0000,$0A0A1010,$0A0A2020,$0A0A3030
	dc.l	$0A0A4040,$0A0A5050,$0A0A6060,$0A0A7070
	dc.l	$0A0A8080,$0A0A9090,$0A0AA0A0,$0A0AB0B0
	dc.l	$0A0AC0C0,$0A0AD0D0,$0A0AE0E0,$0A0AF0F0
	dc.l	$0B0B0000,$0B0B1010,$0B0B2020,$0B0B3030
	dc.l	$0B0B4040,$0B0B5050,$0B0B6060,$0B0B7070
	dc.l	$0B0B8080,$0B0B9090,$0B0BA0A0,$0B0BB0B0
	dc.l	$0B0BC0C0,$0B0BD0D0,$0B0BE0E0,$0B0BF0F0
	dc.l	$0C0C0000,$0C0C1010,$0C0C2020,$0C0C3030
	dc.l	$0C0C4040,$0C0C5050,$0C0C6060,$0C0C7070
	dc.l	$0C0C8080,$0C0C9090,$0C0CA0A0,$0C0CB0B0
	dc.l	$0C0CC0C0,$0C0CD0D0,$0C0CE0E0,$0C0CF0F0
	dc.l	$0D0D0000,$0D0D1010,$0D0D2020,$0D0D3030
	dc.l	$0D0D4040,$0D0D5050,$0D0D6060,$0D0D7070
	dc.l	$0D0D8080,$0D0D9090,$0D0DA0A0,$0D0DB0B0
	dc.l	$0D0DC0C0,$0D0DD0D0,$0D0DE0E0,$0D0DF0F0
	dc.l	$0E0E0000,$0E0E1010,$0E0E2020,$0E0E3030
	dc.l	$0E0E4040,$0E0E5050,$0E0E6060,$0E0E7070
	dc.l	$0E0E8080,$0E0E9090,$0E0EA0A0,$0E0EB0B0
	dc.l	$0E0EC0C0,$0E0ED0D0,$0E0EE0E0,$0E0EF0F0
	dc.l	$0F0F0000,$0F0F1010,$0F0F2020,$0F0F3030
	dc.l	$0F0F4040,$0F0F5050,$0F0F6060,$0F0F7070
	dc.l	$0F0F8080,$0F0F9090,$0F0FA0A0,$0F0FB0B0
	dc.l	$0F0FC0C0,$0F0FD0D0,$0F0FE0E0,$0F0FF0F0

	dc.l	$00000000,$00000808,$00001010,$00001818
	dc.l	$00002020,$00002828,$00003030,$00003838
	dc.l	$00004040,$00004848,$00005050,$00005858
	dc.l	$00006060,$00006868,$00007070,$00007878
	dc.l	$00008080,$00008888,$00009090,$00009898
	dc.l	$0000A0A0,$0000A8A8,$0000B0B0,$0000B8B8
	dc.l	$0000C0C0,$0000C8C8,$0000D0D0,$0000D8D8
	dc.l	$0000E0E0,$0000E8E8,$0000F0F0,$0000F8F8
	dc.l	$01010000,$01010808,$01011010,$01011818
	dc.l	$01012020,$01012828,$01013030,$01013838
	dc.l	$01014040,$01014848,$01015050,$01015858
	dc.l	$01016060,$01016868,$01017070,$01017878
	dc.l	$01018080,$01018888,$01019090,$01019898
	dc.l	$0101A0A0,$0101A8A8,$0101B0B0,$0101B8B8
	dc.l	$0101C0C0,$0101C8C8,$0101D0D0,$0101D8D8
	dc.l	$0101E0E0,$0101E8E8,$0101F0F0,$0101F8F8
	dc.l	$02020000,$02020808,$02021010,$02021818
	dc.l	$02022020,$02022828,$02023030,$02023838
	dc.l	$02024040,$02024848,$02025050,$02025858
	dc.l	$02026060,$02026868,$02027070,$02027878
	dc.l	$02028080,$02028888,$02029090,$02029898
	dc.l	$0202A0A0,$0202A8A8,$0202B0B0,$0202B8B8
	dc.l	$0202C0C0,$0202C8C8,$0202D0D0,$0202D8D8
	dc.l	$0202E0E0,$0202E8E8,$0202F0F0,$0202F8F8
	dc.l	$03030000,$03030808,$03031010,$03031818
	dc.l	$03032020,$03032828,$03033030,$03033838
	dc.l	$03034040,$03034848,$03035050,$03035858
	dc.l	$03036060,$03036868,$03037070,$03037878
	dc.l	$03038080,$03038888,$03039090,$03039898
	dc.l	$0303A0A0,$0303A8A8,$0303B0B0,$0303B8B8
	dc.l	$0303C0C0,$0303C8C8,$0303D0D0,$0303D8D8
	dc.l	$0303E0E0,$0303E8E8,$0303F0F0,$0303F8F8
	dc.l	$04040000,$04040808,$04041010,$04041818
	dc.l	$04042020,$04042828,$04043030,$04043838
	dc.l	$04044040,$04044848,$04045050,$04045858
	dc.l	$04046060,$04046868,$04047070,$04047878
	dc.l	$04048080,$04048888,$04049090,$04049898
	dc.l	$0404A0A0,$0404A8A8,$0404B0B0,$0404B8B8
	dc.l	$0404C0C0,$0404C8C8,$0404D0D0,$0404D8D8
	dc.l	$0404E0E0,$0404E8E8,$0404F0F0,$0404F8F8
	dc.l	$05050000,$05050808,$05051010,$05051818
	dc.l	$05052020,$05052828,$05053030,$05053838
	dc.l	$05054040,$05054848,$05055050,$05055858
	dc.l	$05056060,$05056868,$05057070,$05057878
	dc.l	$05058080,$05058888,$05059090,$05059898
	dc.l	$0505A0A0,$0505A8A8,$0505B0B0,$0505B8B8
	dc.l	$0505C0C0,$0505C8C8,$0505D0D0,$0505D8D8
	dc.l	$0505E0E0,$0505E8E8,$0505F0F0,$0505F8F8
	dc.l	$06060000,$06060808,$06061010,$06061818
	dc.l	$06062020,$06062828,$06063030,$06063838
	dc.l	$06064040,$06064848,$06065050,$06065858
	dc.l	$06066060,$06066868,$06067070,$06067878
	dc.l	$06068080,$06068888,$06069090,$06069898
	dc.l	$0606A0A0,$0606A8A8,$0606B0B0,$0606B8B8
	dc.l	$0606C0C0,$0606C8C8,$0606D0D0,$0606D8D8
	dc.l	$0606E0E0,$0606E8E8,$0606F0F0,$0606F8F8
	dc.l	$07070000,$07070808,$07071010,$07071818
	dc.l	$07072020,$07072828,$07073030,$07073838
	dc.l	$07074040,$07074848,$07075050,$07075858
	dc.l	$07076060,$07076868,$07077070,$07077878
	dc.l	$07078080,$07078888,$07079090,$07079898
	dc.l	$0707A0A0,$0707A8A8,$0707B0B0,$0707B8B8
	dc.l	$0707C0C0,$0707C8C8,$0707D0D0,$0707D8D8
	dc.l	$0707E0E0,$0707E8E8,$0707F0F0,$0707F8F8

	dc.l	$00000000,$00000404,$00000808,$00000C0C
	dc.l	$00001010,$00001414,$00001818,$00001C1C
	dc.l	$00002020,$00002424,$00002828,$00002C2C
	dc.l	$00003030,$00003434,$00003838,$00003C3C
	dc.l	$00004040,$00004444,$00004848,$00004C4C
	dc.l	$00005050,$00005454,$00005858,$00005C5C
	dc.l	$00006060,$00006464,$00006868,$00006C6C
	dc.l	$00007070,$00007474,$00007878,$00007C7C
	dc.l	$00008080,$00008484,$00008888,$00008C8C
	dc.l	$00009090,$00009494,$00009898,$00009C9C
	dc.l	$0000A0A0,$0000A4A4,$0000A8A8,$0000ACAC
	dc.l	$0000B0B0,$0000B4B4,$0000B8B8,$0000BCBC
	dc.l	$0000C0C0,$0000C4C4,$0000C8C8,$0000CCCC
	dc.l	$0000D0D0,$0000D4D4,$0000D8D8,$0000DCDC
	dc.l	$0000E0E0,$0000E4E4,$0000E8E8,$0000ECEC
	dc.l	$0000F0F0,$0000F4F4,$0000F8F8,$0000FCFC
	dc.l	$01010000,$01010404,$01010808,$01010C0C
	dc.l	$01011010,$01011414,$01011818,$01011C1C
	dc.l	$01012020,$01012424,$01012828,$01012C2C
	dc.l	$01013030,$01013434,$01013838,$01013C3C
	dc.l	$01014040,$01014444,$01014848,$01014C4C
	dc.l	$01015050,$01015454,$01015858,$01015C5C
	dc.l	$01016060,$01016464,$01016868,$01016C6C
	dc.l	$01017070,$01017474,$01017878,$01017C7C
	dc.l	$01018080,$01018484,$01018888,$01018C8C
	dc.l	$01019090,$01019494,$01019898,$01019C9C
	dc.l	$0101A0A0,$0101A4A4,$0101A8A8,$0101ACAC
	dc.l	$0101B0B0,$0101B4B4,$0101B8B8,$0101BCBC
	dc.l	$0101C0C0,$0101C4C4,$0101C8C8,$0101CCCC
	dc.l	$0101D0D0,$0101D4D4,$0101D8D8,$0101DCDC
	dc.l	$0101E0E0,$0101E4E4,$0101E8E8,$0101ECEC
	dc.l	$0101F0F0,$0101F4F4,$0101F8F8,$0101FCFC
	dc.l	$02020000,$02020404,$02020808,$02020C0C
	dc.l	$02021010,$02021414,$02021818,$02021C1C
	dc.l	$02022020,$02022424,$02022828,$02022C2C
	dc.l	$02023030,$02023434,$02023838,$02023C3C
	dc.l	$02024040,$02024444,$02024848,$02024C4C
	dc.l	$02025050,$02025454,$02025858,$02025C5C
	dc.l	$02026060,$02026464,$02026868,$02026C6C
	dc.l	$02027070,$02027474,$02027878,$02027C7C
	dc.l	$02028080,$02028484,$02028888,$02028C8C
	dc.l	$02029090,$02029494,$02029898,$02029C9C
	dc.l	$0202A0A0,$0202A4A4,$0202A8A8,$0202ACAC
	dc.l	$0202B0B0,$0202B4B4,$0202B8B8,$0202BCBC
	dc.l	$0202C0C0,$0202C4C4,$0202C8C8,$0202CCCC
	dc.l	$0202D0D0,$0202D4D4,$0202D8D8,$0202DCDC
	dc.l	$0202E0E0,$0202E4E4,$0202E8E8,$0202ECEC
	dc.l	$0202F0F0,$0202F4F4,$0202F8F8,$0202FCFC
	dc.l	$03030000,$03030404,$03030808,$03030C0C
	dc.l	$03031010,$03031414,$03031818,$03031C1C
	dc.l	$03032020,$03032424,$03032828,$03032C2C
	dc.l	$03033030,$03033434,$03033838,$03033C3C
	dc.l	$03034040,$03034444,$03034848,$03034C4C
	dc.l	$03035050,$03035454,$03035858,$03035C5C
	dc.l	$03036060,$03036464,$03036868,$03036C6C
	dc.l	$03037070,$03037474,$03037878,$03037C7C
	dc.l	$03038080,$03038484,$03038888,$03038C8C
	dc.l	$03039090,$03039494,$03039898,$03039C9C
	dc.l	$0303A0A0,$0303A4A4,$0303A8A8,$0303ACAC
	dc.l	$0303B0B0,$0303B4B4,$0303B8B8,$0303BCBC
	dc.l	$0303C0C0,$0303C4C4,$0303C8C8,$0303CCCC
	dc.l	$0303D0D0,$0303D4D4,$0303D8D8,$0303DCDC
	dc.l	$0303E0E0,$0303E4E4,$0303E8E8,$0303ECEC
	dc.l	$0303F0F0,$0303F4F4,$0303F8F8,$0303FCFC

	dc.l	$00000000,$00000202,$00000404,$00000606
	dc.l	$00000808,$00000A0A,$00000C0C,$00000E0E
	dc.l	$00001010,$00001212,$00001414,$00001616
	dc.l	$00001818,$00001A1A,$00001C1C,$00001E1E
	dc.l	$00002020,$00002222,$00002424,$00002626
	dc.l	$00002828,$00002A2A,$00002C2C,$00002E2E
	dc.l	$00003030,$00003232,$00003434,$00003636
	dc.l	$00003838,$00003A3A,$00003C3C,$00003E3E
	dc.l	$00004040,$00004242,$00004444,$00004646
	dc.l	$00004848,$00004A4A,$00004C4C,$00004E4E
	dc.l	$00005050,$00005252,$00005454,$00005656
	dc.l	$00005858,$00005A5A,$00005C5C,$00005E5E
	dc.l	$00006060,$00006262,$00006464,$00006666
	dc.l	$00006868,$00006A6A,$00006C6C,$00006E6E
	dc.l	$00007070,$00007272,$00007474,$00007676
	dc.l	$00007878,$00007A7A,$00007C7C,$00007E7E
	dc.l	$00008080,$00008282,$00008484,$00008686
	dc.l	$00008888,$00008A8A,$00008C8C,$00008E8E
	dc.l	$00009090,$00009292,$00009494,$00009696
	dc.l	$00009898,$00009A9A,$00009C9C,$00009E9E
	dc.l	$0000A0A0,$0000A2A2,$0000A4A4,$0000A6A6
	dc.l	$0000A8A8,$0000AAAA,$0000ACAC,$0000AEAE
	dc.l	$0000B0B0,$0000B2B2,$0000B4B4,$0000B6B6
	dc.l	$0000B8B8,$0000BABA,$0000BCBC,$0000BEBE
	dc.l	$0000C0C0,$0000C2C2,$0000C4C4,$0000C6C6
	dc.l	$0000C8C8,$0000CACA,$0000CCCC,$0000CECE
	dc.l	$0000D0D0,$0000D2D2,$0000D4D4,$0000D6D6
	dc.l	$0000D8D8,$0000DADA,$0000DCDC,$0000DEDE
	dc.l	$0000E0E0,$0000E2E2,$0000E4E4,$0000E6E6
	dc.l	$0000E8E8,$0000EAEA,$0000ECEC,$0000EEEE
	dc.l	$0000F0F0,$0000F2F2,$0000F4F4,$0000F6F6
	dc.l	$0000F8F8,$0000FAFA,$0000FCFC,$0000FEFE
	dc.l	$01010000,$01010202,$01010404,$01010606
	dc.l	$01010808,$01010A0A,$01010C0C,$01010E0E
	dc.l	$01011010,$01011212,$01011414,$01011616
	dc.l	$01011818,$01011A1A,$01011C1C,$01011E1E
	dc.l	$01012020,$01012222,$01012424,$01012626
	dc.l	$01012828,$01012A2A,$01012C2C,$01012E2E
	dc.l	$01013030,$01013232,$01013434,$01013636
	dc.l	$01013838,$01013A3A,$01013C3C,$01013E3E
	dc.l	$01014040,$01014242,$01014444,$01014646
	dc.l	$01014848,$01014A4A,$01014C4C,$01014E4E
	dc.l	$01015050,$01015252,$01015454,$01015656
	dc.l	$01015858,$01015A5A,$01015C5C,$01015E5E
	dc.l	$01016060,$01016262,$01016464,$01016666
	dc.l	$01016868,$01016A6A,$01016C6C,$01016E6E
	dc.l	$01017070,$01017272,$01017474,$01017676
	dc.l	$01017878,$01017A7A,$01017C7C,$01017E7E
	dc.l	$01018080,$01018282,$01018484,$01018686
	dc.l	$01018888,$01018A8A,$01018C8C,$01018E8E
	dc.l	$01019090,$01019292,$01019494,$01019696
	dc.l	$01019898,$01019A9A,$01019C9C,$01019E9E
	dc.l	$0101A0A0,$0101A2A2,$0101A4A4,$0101A6A6
	dc.l	$0101A8A8,$0101AAAA,$0101ACAC,$0101AEAE
	dc.l	$0101B0B0,$0101B2B2,$0101B4B4,$0101B6B6
	dc.l	$0101B8B8,$0101BABA,$0101BCBC,$0101BEBE
	dc.l	$0101C0C0,$0101C2C2,$0101C4C4,$0101C6C6
	dc.l	$0101C8C8,$0101CACA,$0101CCCC,$0101CECE
	dc.l	$0101D0D0,$0101D2D2,$0101D4D4,$0101D6D6
	dc.l	$0101D8D8,$0101DADA,$0101DCDC,$0101DEDE
	dc.l	$0101E0E0,$0101E2E2,$0101E4E4,$0101E6E6
	dc.l	$0101E8E8,$0101EAEA,$0101ECEC,$0101EEEE
	dc.l	$0101F0F0,$0101F2F2,$0101F4F4,$0101F6F6
	dc.l	$0101F8F8,$0101FAFA,$0101FCFC,$0101FEFE

	end
