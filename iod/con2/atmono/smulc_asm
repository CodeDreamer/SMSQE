; Send characters to CONSOLE  V2.00     1990 Tony Tebby

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
	cmp.b	#k.nl,d0		 ; newline
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
	move.w	d2,-(sp)
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

;	 btst	 #sd..dwdt,sd_cattr(a0)    ; double width?
;	 bne.s	 cnsm_cdw		  ; ... yes
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
;	 bra.s	 cnsm_doslice

;cnsm_cdw
;	 movem.l sd_smask(a0),d6/d7	  ; set colour masks
;	 asr.w	 #1,d0			  ; ... odd line?
;	 bcs.s	 cnsm_doslice		  ; ... yes
;	 swap	 d6
;	 swap	 d7

; now do a slice

cnsm_doslice
	move.w	sd_xmin(a0),d0		 ; area origin
	add.w	sd_xpos(a0),d0		 ; cursor origin
	move.w	stk_slice(sp),d1
cnsm_cloop
	move.l	stk_base(sp),a4
	move.w	d1,-(sp)
	move.w	d0,-(sp)

	moveq	#$f,d2			 ; character shift   ;*****
	and.w	d0,d2
	lsr.w	#4,d0
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
	moveq	#9,d1			 ; ten rows
	lsl.w	#2,d2			 ; index character mask table
	lea	cc_mask6,a6
	move.l	(a6,d2.w),d3		 ; character mask
	lsl.w	#6,d2			 ; index character shift table
	lea	cc_shift,a6
	add.w	d2,a6
	tst.w	d3			 ; word or long operation required?
	bne.s	cc_swpl 		 ; ... long

	swap	d3
	move.w	d3,d4
	not.w	d4
	bra.s	cc_swpwb

cc_swpwl
	moveq	#0,d0
	move.b	(a3)+,d0		 ; next bit
	bne.s	cc_swpwc

cc_swpwb
	or.w	d3,(a4) 		 ; the strip at the top
	bra.s	cc_swpwe

cc_swpwc
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
cc_swpwe
	add.w	a2,a4			 ; next row
	dbra	d1,cc_swpwl
	bra.l	cnsm_next

cc_swpl
	move.l	d3,d4
	not.l	d4
	bra.s	cc_swplb

cc_swpll
	moveq	#0,d0
	move.b	(a3)+,d0		 ; next bit
	bne.s	cc_swplc

cc_swplb
	or.l	d3,(a4) 		 ; the strip at the top
	bra.s	cc_swple

cc_swplc
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
cc_swple
	add.l	a2,a4			 ; next row
	dbra	d1,cc_swpll
	bra.l	cnsm_next

;+++
; Standard size / attribute black ink drawing code
;---
cc_stdbi
	moveq	#9,d1			 ; ten rows
	lsl.w	#2,d2			 ; index character mask table
	lea	cc_mask6,a6
	move.l	(a6,d2.w),d3		 ; character mask
	lsl.w	#6,d2			 ; index character shift table
	lea	cc_shift,a6
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
	move.w	(a6,d0.w),d0		 ; character pattern
	not.w	d0
cc_sbiwb
	and.w	d3,d0			 ; the ink
	and.w	d4,d0			 ; ... strip
	move.w	d6,d2
	and.w	(a4),d2 		 ; the rest of the long word from screen
	or.w	d2,d0
	move.w	d0,(a4) 		 ; back in the screen
cc_sbiwe
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
cc_sbilc
	move.l	(a6,d0.w),d0		 ; character pattern
	not.l	d0
	and.l	d3,d0			 ; the ink / strip
cc_sbilb
	and.l	d4,d0
	move.l	d6,d2
	and.l	(a4),d2 		 ; the rest of the long word from screen
	or.l	d2,d0
	move.l	d0,(a4) 		 ; back in the screen
cc_sbile
	add.l	a2,a4			 ; next row
	exg	d4,d5
	dbra	d1,cc_sbill
	bra.l	cnsm_next

;+++
; Standard size / attribute black on white drawing code
;---
cc_stdbw
	moveq	#9,d1			 ; ten rows
	lsl.w	#2,d2			 ; index character mask table
	lea	cc_mask6,a6
	move.l	(a6,d2.w),d3		 ; character mask
	lsl.w	#6,d2			 ; index character shift table
	lea	cc_shift,a6
	add.w	d2,a6
	tst.w	d3			 ; word or long operation required?
	bne.s	cc_sbwl 		 ; ... long

	swap	d3
	move.w	d3,d4
	not.w	d4
	bra.s	cc_sbwwb

cc_sbwwl
	moveq	#0,d0
	move.b	(a3)+,d0		 ; next bit
	bne.s	cc_sbwwc

cc_sbwwb
	or.w	d3,(a4) 		 ; the strip at the top
	bra.s	cc_sbwwe

cc_sbwwc
	move.w	(a6,d0.w),d0		 ; character pattern
	not.w	d0
	and.w	d3,d0			 ; the ink
	move.w	d4,d2
	and.w	(a4),d2 		 ; the rest of the long word from screen
	or.w	d2,d0
	move.w	d0,(a4) 		 ; back in the screen
cc_sbwwe
	add.w	a2,a4			 ; next row
	dbra	d1,cc_sbwwl
	bra.l	cnsm_next

cc_sbwl
	move.l	d3,d4
	not.l	d4
	bra.s	cc_sbwlb

cc_sbwll
	moveq	#0,d0
	move.b	(a3)+,d0		 ; next bit
	bne.s	cc_sbwlc

cc_sbwlb
	or.l	d3,(a4) 		 ; the strip at the top
	bra.s	cc_sbwle

cc_sbwlc
	move.l	(a6,d0.w),d0		 ; character pattern
	not.l	d0
	and.l	d3,d0			 ; the ink
	move.l	d4,d2
	and.l	(a4),d2 		 ; the rest of the long word from screen
	or.l	d2,d0
	move.l	d0,(a4) 		 ; back in the screen
cc_sbwle
	add.l	a2,a4			 ; next row
	dbra	d1,cc_sbwll
	bra.l	cnsm_next

cc_mask6
	dc.l	$fc000000
	dc.l	$7e000000
	dc.l	$3f000000
	dc.l	$1f800000
	dc.l	$0fc00000
	dc.l	$07e00000
	dc.l	$03f00000
	dc.l	$01f80000
	dc.l	$00fc0000
	dc.l	$007e0000
	dc.l	$003f0000
	dc.l	$001f8000
	dc.l	$000fc000
	dc.l	$0007e000
	dc.l	$0003f000
	dc.l	$0001f800

;+++
; Standard size / attribute black paper drawing code
;---
cc_stdbp
	moveq	#9,d1			 ; ten rows
	lsl.w	#2,d2			 ; index character mask table
	move.l	cc_mask6(pc,d2.w),d3	 ; character mask
	lsl.w	#6,d2			 ; index character shift table
	lea	cc_shift,a6
	add.w	d2,a6
	tst.w	d3			 ; word or long operation required?
	bne.s	cc_sbpl 		 ; ... long

	swap	d3
	move.w	d3,d4
	not.w	d4
	bra.s	cc_sbpwb

cc_sbpwl
	moveq	#0,d0
	move.b	(a3)+,d0		 ; next bit
	bne.s	cc_sbpwc

cc_sbpwb
	and.w	d4,(a4) 		 ; the strip at the top
	bra.s	cc_sbpwe

cc_sbpwc
	move.w	(a6,d0.w),d0		 ; character pattern
	and.w	d6,d0			 ; the ink
	move.w	d4,d2
	and.w	(a4),d2 		 ; the rest of the long word from screen
	or.w	d2,d0
	move.w	d0,(a4) 		 ; back in the screen
cc_sbpwe
	add.w	a2,a4			 ; next row
	exg	d6,d7
	dbra	d1,cc_sbpwl
	bra.l	cnsm_next

cc_sbpl
	move.l	d3,d4
	not.l	d4
	bra.s	cc_sbplb

cc_sbpll
	moveq	#0,d0
	move.b	(a3)+,d0		 ; next bit
	bne.s	cc_sbplc


cc_sbplb
	and.l	d4,(a4) 		 ; the strip at the top
	bra.s	cc_sbple

cc_sbplc
	move.l	(a6,d0.w),d0		 ; character pattern
	and.l	d6,d0			 ; the ink
	move.l	d4,d2
	and.l	(a4),d2 		 ; the rest of the long word from screen
	or.l	d2,d0
	move.l	d0,(a4) 		 ; back in the screen
cc_sbple
	add.l	a2,a4			 ; next row
	dbra	d1,cc_sbpll
	bra.l	cnsm_next

cc_mask8
	dc.l	$ff000000
	dc.l	$7f800000
	dc.l	$3fc00000
	dc.l	$1fe00000
	dc.l	$0ff00000
	dc.l	$07f80000
	dc.l	$03fc0000
	dc.l	$01fe0000
	dc.l	$00ff0000
	dc.l	$007f8000
	dc.l	$003fc000
	dc.l	$001fe000
	dc.l	$000ff000
	dc.l	$0007f800
	dc.l	$0003fc00
	dc.l	$0001fe00

;+++
; Standard size, underline / extended character drawing code
;---
cc_stdux
	move.l	#$10007,d1		      ; eight+two rows
	bra.s	cc_stdx

;+++
; Standard size, extended character drawing code
;---
cc_stdcx
	moveq	#9,d1			 ; ten rows
cc_stdx
	lsl.w	#2,d2			 ; index character mask table
	move.l	cc_mask8(pc,d2.w),d3	 ; character mask
	bra.s	cc_stdc2
;+++
; Standard size, underline character drawing code
;---
cc_stdul
	move.l	#$10007,d1		      ; eight+two rows
	bra.s	cc_stdc1

;+++
; Standard size / attribute character drawing code
;---
cc_stdch
	move.b	sd_scolr(a0),d0 	 ; black strip?
	bne.s	cc_stdcw
	lea	cc_stdbp,a5
	jmp	(a5)			 ; ... yes

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
	moveq	#9,d1			 ; ten rows
cc_stdc1
	lsl.w	#2,d2			 ; index character mask table
	lea	cc_mask6,a6
	move.l	(a6,d2.w),d3		 ; character mask
cc_stdc2
	lsl.w	#6,d2			 ; index character shift table
	lea	cc_shift,a6
	add.w	d2,a6
	tst.w	d3			 ; word or long operation required?
	beq.s	cc_stdw 		 ; ... standard word
	bra.s	cc_stdlb

cc_stdll
	moveq	#0,d0
	move.b	(a3)+,d0		 ; next bit
	bne.s	cc_stdlc

cc_stdlb
	move.l	d4,d2
	and.l	d3,d2			 ; the strip at the top
	bra.s	cc_stdls

cc_stdlc
	move.l	(a6,d0.w),d0		 ; character pattern
	move.l	d0,d2
	not.l	d2			 ; inverse character
	and.l	d4,d2
	and.l	d3,d2			 ; the strip
	and.l	d6,d0			 ; the ink
	or.l	d0,d2			 ; the new character square
cc_stdls
	move.l	d3,d0
	not.l	d0
	and.l	(a4),d0 		 ; the rest of the long word from screen
	or.l	d2,d0
	move.l	d0,(a4) 		 ; back in the screen

	add.l	a2,a4			 ; next row
	exg	d6,d7
	exg	d4,d5
	dbra	d1,cc_stdll

	swap	d1
	tst.w	d1			 ; underscore?
	beq.l	cnsm_next		 ; ... no
	ext.l	d1
	move.l	d6,d2
	and.l	d3,d2			 ; the underscore at the bottom
	addq.l	#1,a3			 ; skip font line
	bra.s	cc_stdls

cc_stdw
	swap	d3			 ; all in a word
	bra.s	cc_stdwb

cc_stdwl
	moveq	#0,d0
	move.b	(a3)+,d0		 ; next bit
	bne.s	cc_stdwc

cc_stdwb
	move.w	d4,d2
	and.w	d3,d2			 ; the strip at the top
	bra.s	cc_stdws

cc_stdwc
	move.w	(a6,d0.w),d0		 ; character pattern
	move.w	d0,d2
	not.w	d2			 ; inverse character
	and.w	d4,d2
	and.w	d3,d2			 ; the strip
	and.w	d6,d0			 ; the ink
	or.w	d0,d2			 ; the new character square
cc_stdws
	move.w	d3,d0
	not.w	d0
	and.w	(a4),d0 		 ; the rest of the word from screen
	or.w	d2,d0
	move.w	d0,(a4) 		 ; back in the screen

	add.l	a2,a4			 ; next row
	exg	d6,d7
	exg	d4,d5
	dbra	d1,cc_stdwl

	swap	d1			 ; underscore?
	tst.w	d1
	beq.l	cnsm_next		 ; ... no
	ext.l	d1			 ; ... yes, but no more
	move.w	d6,d2
	and.w	d3,d2			 ; the underscore at the bottom
	addq.l	#1,a3			 ; skip font line
	bra.s	cc_stdws

;+++
; Standard size, underline / over / extended character drawing code
;---
cc_ovrux
	move.l	#$10007,d1		      ; eight+two rows
	bra.s	cc_ovrx

;+++
; Standard size, over / extended character drawing code
;---
cc_ovrcx
	moveq	#9,d1			 ; ten rows
cc_ovrx
	lea	cc_mask8,a6
	bra.s	cc_ovrc2

;+++
; Standard size, underline / over character drawing code
;---
cc_ovrul
	move.l	#$10007,d1		      ; eight+two rows
	bra.s	cc_ovrc1

;+++
; Standard size, over character drawing code
;---
cc_ovrch
	moveq	#9,d1			 ; ten rows
cc_ovrc1
	lea	cc_mask6,a6
cc_ovrc2
	lsl.w	#2,d2			 ; index character mask table
	move.l	(a6,d2.w),d3		 ; character mask (for underline!!)
	lsl.w	#6,d2			 ; index character shift table
	lea	cc_shift,a6
	add.w	d2,a6
	tst.w	d3			 ; word or long operation required?
	beq.s	cc_ovrw 		 ; ... word
	bra.s	cc_ovrln

cc_ovrll
	moveq	#0,d0
	move.b	(a3)+,d0		 ; next bit
	beq.s	cc_ovrln

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

	swap	d1
	tst.w	d1			 ; underscore?
	beq.l	cnsm_next		 ; ... no
	ext.l	d1
	move.l	d3,d0			 ; the underscore at the bottom
	addq.l	#1,a3			 ; skip font line
	bra.s	cc_ovrls

cc_ovrw
	swap	d3			 ; all in a word
	bra.s	cc_ovrwn

cc_ovrwl
	moveq	#0,d0
	move.b	(a3)+,d0		 ; next bit
	beq.s	cc_ovrwn

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

	swap	d1			 ; underscore?
	tst.w	d1
	beq.l	cnsm_next		 ; ... no
	ext.l	d1			 ; ... yes, but no more
	move.l	d3,d0			 ; the underscore at the bottom
	addq.l	#1,a3			 ; skip font line
	bra.s	cc_ovrws

;+++
; Standard size, underline / xor / extended character drawing code
;---
cc_xorux
	move.l	#$10007,d1		      ; eight+two rows
	bra.s	cc_xorx

;+++
; Standard size, xor character drawing code
;---
cc_xorcx
	moveq	#9,d1			 ; ten rows
cc_xorx
	lea	cc_mask8,a6
	bra.s	cc_xorc2

;+++
; Standard size, underline / xor character drawing code
;---
cc_xorul
	move.l	#$10007,d1		      ; eight+two rows
	bra.s	cc_xorc1

;+++
; Standard size, xor character drawing code
;---
cc_xorch
	moveq	#9,d1			 ; ten rows
cc_xorc1
	lea	cc_mask6,a6
cc_xorc2
	lsl.w	#2,d2			 ; index character mask table
	move.l	(a6,d2.w),d3		 ; character mask (for underline!!)
	lsl.w	#6,d2			 ; index character shift table
	lea	cc_shift,a6
	add.w	d2,a6
	tst.w	d3			 ; word or long operation required?
	beq.s	cc_xorw 		 ; ... word
	bra.s	cc_xorln

cc_xorll
	moveq	#0,d0
	move.b	(a3)+,d0		 ; next bit
	beq.s	cc_xorln

	move.l	(a6,d0.w),d0		 ; character pattern
cc_xorls
	and.l	d6,d0
	eor.l	d0,(a4) 		 ; xor into the screen

cc_xorln
	add.l	a2,a4			 ; next row
	exg	d6,d7
	dbra	d1,cc_xorll

	swap	d1
	tst.w	d1			 ; underscore?
	beq.l	cnsm_next		 ; ... no
	ext.l	d1
	move.l	d3,d0			 ; the underscore at the bottom
	addq.l	#1,a3			 ; skip font line
	bra.s	cc_xorls

cc_xorw
	swap	d3			 ; all in a word
	bra.s	cc_xorwn

cc_xorwl
	moveq	#0,d0
	move.b	(a3)+,d0		 ; next bit
	beq.s	cc_xorwn

	move.w	(a6,d0.w),d0		 ; character pattern
cc_xorws
	and.w	d6,d0
	eor.w	d0,(a4) 		 ; xor into the screen

cc_xorwn
	add.l	a2,a4			 ; next row
	exg	d6,d7
	dbra	d1,cc_xorwl

	swap	d1			 ; underscore?
	tst.w	d1
	beq.l	cnsm_next		 ; ... no
	ext.l	d1			 ; ... yes, but no more
	move.l	d3,d0			 ; the underscore at the bottom
	addq.l	#1,a3			 ; skip font line
	bra.s	cc_xorws

	page

;+++
; Double height / underline / extended character drawing code
;---
ch_stdux
	move.l	#$10007,d1		 ; eight+two rows
	bra.s	ch_stdx

;+++
; Double height / extended character drawing code
;---
ch_stdcx
	moveq	#9,d1			 ; ten rows
ch_stdx
	moveq	#0,d0			 ; standard
	bra.s	ch_allx
;+++
; Double height / underline / over / extended character drawing code
;---
ch_ovrux
	move.l	#$10007,d1		 ; eight+two
	bra.s	ch_ovrx
;+++
; Double height / over / extended character drawing code
;---
ch_ovrcx
	moveq	#9,d1			 ; ten rows
ch_ovrx
	moveq	#1,d0			 ; over
	bra.s	ch_allx

;+++
; Double height / underline / xor / extended character drawing code
;---
ch_xorux
	move.l	#$10007,d1		 ; eight+two rows
	bra.s	ch_xorx

;+++
; Double height / xor / extended character drawing code
;---
ch_xorcx
	moveq	#9,d1			 ; ten rows
ch_xorx
	moveq	#-1,d0			 ; xor mode

ch_allx
	lea	cc_mask8,a6
	bra.s	ch_alldo

;+++
; Double height / underline character drawing code
;---
ch_stdul
	move.l	#$10007,d1		 ; eight+two rows
	bra.s	ch_std

;+++
; Double height character drawing code
;---
ch_stdch
	moveq	#9,d1			 ; ten rows
ch_std
	moveq	#0,d0			 ; standard
	bra.s	ch_alls
;+++
; Double height / underline / over character drawing code
;---
ch_ovrul
	move.l	#$10007,d1		 ; eight+two
	bra.s	ch_ovr
;+++
; Double height / over character drawing code
;---
ch_ovrch
	moveq	#9,d1			 ; ten rows
ch_ovr
	moveq	#1,d0			 ; over
	bra.s	ch_alls

;+++
; Double height / underline / xor character drawing code
;---
ch_xorul
	move.l	#$10007,d1		  ; eight+two rows
	bra.s	ch_xor

;+++
; Double height / xor character drawing code
;---
ch_xorch
	moveq	#9,d1			 ; ten rows
ch_xor
	moveq	#-1,d0			 ; xor mode

ch_alls
	lea	cc_mask6,a6
ch_alldo
	movem.l a0/a1/a2,-(sp)		 ; we need extra regs
	move.b	d0,-(sp)
	lea	(a4,a2.l),a1		 ; alternate line
	add.l	a2,a2			 ; two at a time

	lsl.w	#2,d2			 ; index character mask table
	move.l	(a6,d2.w),d3		 ; character mask (for underline!!)
	lsl.w	#6,d2			 ; index character shift table
	lea	cc_shift,a6
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
	bne.l	ch_allle		 ; ... yes
	move.l	d4,d0
	and.l	d3,d0			 ; the strip at the top
	move.l	d3,d2
	not.l	d2
	and.l	(a4),d2
	or.l	d0,d2
	move.l	d2,(a4)
	move.l	d5,d0
	and.l	d3,d0
	move.l	d3,d2
	not.l	d2
	and.l	(a1),d2
	or.l	d0,d2
	move.l	d2,(a1)
	bra.s	ch_allle

ch_alllc
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

	move.l	a0,d0
	move.l	d0,d2
	not.l	d2			 ; inverse character
	and.l	(a1),d2 		 ; the background
	and.l	d7,d0
	or.l	d2,d0			 ; the new character square
	move.l	d0,(a1) 		 ; back in the screen

	bra.s	ch_allle

ch_alllx
	move.l	d0,a0
	and.l	d6,d0
	eor.l	d0,(a4) 		 ; xor into the screen
	move.l	a0,d0
	and.l	d7,d0
	eor.l	d0,(a1) 		 ; xor into the screen

	bra.s	ch_allle

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

	move.l	a0,d0
	move.l	d0,d2
	not.l	d2			 ; inverse character
	and.l	d5,d2
	and.l	d3,d2			 ; the strip
	and.l	d7,d0			 ; the ink
	or.l	d0,d2			 ; the new character square
	move.l	d3,d0
	not.l	d0
	and.l	(a1),d0 		 ; the rest of the long word from screen
	or.l	d2,d0
	move.l	d0,(a1) 		 ; back in the screen

ch_allle
	add.l	a2,a4			 ; next row
	add.l	a2,a1
	dbra	d1,ch_allll

	swap	d1
	tst.w	d1			 ; underscore?
	beq.l	ch_exit 		 ; ... no
	ext.l	d1
	move.l	d3,d0			 ; the underscore at the bottom
	addq.l	#1,a3
	bra.s	ch_allls

ch_allw
	swap	d3			 ; all in a word
	bra.s	ch_allwb

ch_allwl
	moveq	#0,d0
	move.b	(a3)+,d0		 ; next bit
	bne.s	ch_allwc

ch_allwb
	tst.b	(sp)			 ; blank line: over or xor?
	bne.l	ch_allwe		 ; ... yes
	move.w	d4,d0
	and.w	d3,d0			 ; the strip at the top
	move.w	d3,d2
	not.w	d2
	and.w	(a4),d2
	or.w	d0,d2
	move.w	d2,(a4)
	move.w	d5,d0
	and.w	d3,d0
	move.w	d3,d2
	not.w	d2
	and.w	(a1),d2
	or.w	d0,d2
	move.w	d2,(a1)
	bra.s	ch_allwe

ch_allwc
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

	move.w	a0,d0
	move.w	d0,d2
	not.w	d2			 ; inverse character
	and.w	(a1),d2 		 ; the background
	and.w	d7,d0
	or.w	d2,d0			 ; the new character square
	move.w	d0,(a1) 		 ; back in the screen

	bra.s	ch_allwe

ch_allwx
	move.w	d0,a0
	and.w	d6,d0
	eor.w	d0,(a4) 		 ; xor into the screen
	move.w	a0,d0
	and.w	d7,d0
	eor.w	d0,(a1) 		 ; xor into the screen

	bra.s	ch_allwe

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

	move.w	a0,d0
	move.w	d0,d2
	not.w	d2			 ; inverse character
	and.w	d5,d2
	and.w	d3,d2			 ; the strip
	and.w	d7,d0			 ; the ink
	or.w	d0,d2			 ; the new character square
	move.w	d3,d0
	not.w	d0
	and.w	(a1),d0 		 ; the rest of the long word from screen
	or.w	d2,d0
	move.w	d0,(a1) 		 ; back in the screen

ch_allwe
	add.l	a2,a4			 ; next row
	add.l	a2,a1
	dbra	d1,ch_allwl

	swap	d1
	tst.w	d1			 ; underscore?
	beq.s	ch_exit 		 ; ... no
	ext.l	d1
	move.l	d3,d0			 ; the underscore at the bottom
	addq.l	#1,a3
	bra.s	ch_allws

ch_exit
	addq.l	#2,sp			 ; clean up the stack
	movem.l (sp)+,a0/a1/a2
	bra.l	cnsm_next

	page

;+++
; Double size / underline / extended character drawing code
;---
cz_stdux
	move.l	#$10007,d1		 ; eight+two rows
	bra.s	cz_stdx

;+++
; Double size / extended character drawing code
;---
cz_stdcx
	moveq	#9,d1			 ; ten rows
cz_stdx
	move.w	#$00ff,d0		 ; double height
	bra.s	cz_setx
;+++
; Double size / underline / over / extended character drawing code
;---
cz_ovrux
	move.l	#$10007,d1		 ; eight+two
	bra.s	cz_ovrx
;+++
; Double size / over / extended character drawing code
;---
cz_ovrcx
	moveq	#9,d1			 ; ten rows
cz_ovrx
	move.w	#$01ff,d0		 ; over
	bra.s	cz_setx

;+++
; Double size / underline / xor / extended character drawing code
;---
cz_xorux
	move.l	#$10007,d1		 ; eight+two rows
	bra.s	cz_xorx

;+++
; Double size / xor / extended character drawing code
;---
cz_xorcx
	moveq	#9,d1			 ; ten rows
cz_xorx
	moveq	#-1,d0			 ; xor mode

cz_setx
	moveq	#cw_maskx-cw_mask,d3	 ; extended double width
	bra.l	cw_alldo

;+++
; Double size / underline character drawing code
;---
cz_stdul
	move.l	#$10007,d1		 ; eight+two rows
	bra.s	cz_std

;+++
; Double size character drawing code
;---
cz_stdch
	moveq	#9,d1			 ; ten rows
cz_std
	move.w	#$00ff,d0		 ; double height
	bra.s	cz_sets
;+++
; Double size / underline / over character drawing code
;---
cz_ovrul
	move.l	#$10007,d1		 ; eight+two
	bra.s	cz_ovr
;+++
; Double size / over character drawing code
;---
cz_ovrch
	moveq	#9,d1			 ; ten rows
cz_ovr
	move.w	#$01ff,d0		 ; over
	bra.s	cz_sets

;+++
; Double size / underline / xor character drawing code
;---
cz_xorul
	move.l	#$10007,d1		  ; eight+two rows
	bra.s	cz_xor

;+++
; Double size / xor character drawing code
;---
cz_xorch
	moveq	#9,d1			 ; ten rows
cz_xor
	moveq	#-1,d0			 ; xor mode / double size

cz_sets
	moveq	#0,d3			 ; double width
	bra.l	cw_alldo

;+++
; Double width / underline / extended character drawing code
;---
cw_stdux
	move.l	#$10007,d1		 ; eight+two rows
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
	move.l	#$10007,d1		 ; eight+two
	bra.s	cw_ovrx
;+++
; Double width / over / extended character drawing code
;---
cw_ovrcx
	moveq	#9,d1			 ; ten rows
cw_ovrx
	move.w	#$0100,d0		 ; over
	bra.s	cw_setx

;+++
; Double width / underline / xor / extended character drawing code
;---
cw_xorux
	move.l	#$10007,d1		 ; eight+two rows
	bra.s	cw_xorx

;+++
; Double width / xor / extended character drawing code
;---
cw_xorcx
	moveq	#9,d1			 ; ten rows
cw_xorx
	move.w	#$ff00,d0		 ; xor mode

cw_setx
	moveq	#cw_maskx-cw_mask,d3	 ; extended double width
	bra.l	cw_alldo

;+++
; Double width / underline character drawing code
;---
cw_stdul
	move.l	#$10007,d1		 ; eight+two rows
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
	move.l	#$10007,d1		 ; eight+two
	bra.s	cw_ovr
;+++
; Double width / over character drawing code
;---
cw_ovrch
	moveq	#9,d1			 ; ten rows
cw_ovr
	move.w	#$0100,d0		 ; over
	bra.s	cw_sets

;+++
; Double width / underline / xor character drawing code
;---
cw_xorul
	move.l	#$10007,d1		  ; eight+two rows
	bra.s	cw_xor

;+++
; Double width / xor character drawing code
;---
cw_xorch
	moveq	#9,d1			 ; ten rows
cw_xor
	move.w	#$ff00,d0		 ; xor mode

cw_sets
	moveq	#0,d3			 ; double width
	bra.l	cw_alldo


cw_maskx
	dc.l   $ffff0000
	dc.l   $7fff8000
	dc.l   $3fffc000
	dc.l   $1fffe000
	dc.l   $0ffff000
	dc.l   $07fff800
	dc.l   $03fffc00
	dc.l   $01fffe00
	dc.l   $00ffff00
	dc.l   $007fff80
	dc.l   $003fffc0
	dc.l   $001fffe0
	dc.l   $000ffff0
	dc.l   $0007fff8
	dc.l   $0003fffc
	dc.l   $0001fffe

cw_mask
	dc.l   $fff00000
	dc.l   $7ff80000
	dc.l   $3ffc0000
	dc.l   $1ffe0000
	dc.l   $0fff0000
	dc.l   $07ff8000
	dc.l   $03ffc000
	dc.l   $01ffe000
	dc.l   $00fff000
	dc.l   $007ff800
	dc.l   $003ffc00
	dc.l   $001ffe00
	dc.l   $000fff00
	dc.l   $0007ff80
	dc.l   $0003ffc0
	dc.l   $0001ffe0

;+++
; Double width
;---
cw_alldo
	movem.l a0/a1,-(sp)		 ; spare regs
	move.w	d0,-(sp)
	move.w	d2,a1			 ; shift in pixels
	lsl.w	#2,d2			 ; index mask table by fours
	add.w	d2,d3			 ; mask table
	move.l	cw_mask(pc,d3.w),d3	 ; mask in d3
	lea	cw_spread,a6

	tst.w	d3			 ; word or long?
	bne.l	cw_alllb		 ; long

	swap	d3
	bra.s	cw_allwb

cw_allwl
	moveq	#0,d0
	move.b	(a3)+,d0		 ; next bit
	beq.s	cw_allwb

	lsr.w	#1,d0
	move.w	(a6,d0.w),d0		 ; spread character pattern
	move.w	a1,d2
	lsr.w	d2,d0			 ; shift right
	bne.s	cw_allws

cw_allwb
	tst.b	(sp)			 ; blank line: over or xor?
	beq.s	cw_allwt		 ; ... no
	tst.b	1(sp)			 ; double height?
	bge.l	cw_allwi		 ; ... no, swap ink
	add.l	a2,a4			 ; ... down a row
	bra.l	cw_allwn

cw_allwt
	move.w	d4,d0
	and.w	d3,d0			 ; the strip at the top
	move.w	d3,d2
	not.w	d2
	and.w	(a4),d2
	or.w	d0,d2
	move.w	d2,(a4)
	tst.b	1(sp)			 ; double height?
	bge.l	cw_allwe		 ; ... no
	exg	d4,d5
	add.l	a2,a4
	move.w	d4,d0
	and.w	d3,d0
	move.w	d3,d2
	not.w	d2
	and.w	(a4),d2
	or.w	d0,d2
	move.w	d2,(a4)
	exg	d4,d5
	bra.l	cw_allwn

cw_allws
	tst.b	(sp)
	beq.s	cw_allww
	blt.s	cw_allwx

	move.w	d0,a0
	move.w	d0,d2
	not.w	d2			 ; inverse character
	and.w	(a4),d2 		 ; the background
	and.w	d6,d0
	or.w	d2,d0			 ; the new character square
	move.w	d0,(a4) 		 ; back in the screen

	tst.b	1(sp)			 ; double height?
	bge.s	cw_allwi		 ; ... no
	exg	d6,d7
	add.l	a2,a4
	move.w	a0,d0
	move.w	d0,d2
	not.w	d2			 ; inverse character
	and.w	(a4),d2 		 ; the background
	and.w	d6,d0
	or.w	d2,d0			 ; the new character square
	move.w	d0,(a4) 		 ; back in the screen

	bra.s	cw_allwi

cw_allwx
	move.w	d0,a0
	and.w	d6,d0
	eor.w	d0,(a4) 		 ; xor into the screen

	tst.b	1(sp)			 ; double height?
	bge.s	cw_allwi		 ; ... no
	exg	d6,d7
	add.l	a2,a4
	move.w	a0,d0
	and.w	d6,d0
	eor.w	d0,(a4) 		 ; xor into the screen

	bra.s	cw_allwi

cw_allww
	move.w	d0,a0			 ; keep this
	move.w	d0,d2
	not.w	d2			 ; inverse character
	and.w	d4,d2
	and.w	d3,d2			 ; the strip
	and.w	d6,d0			 ; the ink
	or.w	d0,d2			 ; the new character square
	move.w	d3,d0
	not.w	d0
	and.w	(a4),d0 		 ; the rest of the word from screen
	or.w	d2,d0
	move.w	d0,(a4) 		 ; back in the screen

	tst.b	1(sp)			 ; double height?
	bge.s	cw_allwe		 ; ... no
	exg	d4,d5
	exg	d6,d7
	add.l	a2,a4
	move.w	a0,d0
	move.w	d0,d2
	not.w	d2			 ; inverse character
	and.w	d4,d2
	and.w	d3,d2			 ; the strip
	and.w	d6,d0			 ; the ink
	or.w	d0,d2			 ; the new character square
	move.w	d3,d0
	not.w	d0
	and.w	(a4),d0 		 ; the rest of the word from screen
	or.w	d2,d0
	move.w	d0,(a4) 		 ; back in the screen

cw_allwe
	exg	d4,d5
cw_allwi
	exg	d6,d7
cw_allwn
	add.l	a2,a4			 ; next row
	dbra	d1,cw_allwl

	swap	d1
	tst.w	d1			 ; underscore?
	beq.l	cw_exit 		 ; ... no
	ext.l	d1
	move.l	d3,d0			 ; the underscore at the bottom
	addq.l	#1,a3
	bra.l	cw_allws


cw_allll
	moveq	#0,d0
	move.b	(a3)+,d0		 ; next bit
	beq.s	cw_alllb

	lsr.w	#1,d0
	move.w	(a6,d0.w),d0		 ; spread character pattern
	move.w	a1,d2
	swap	d0
	lsr.l	d2,d0			 ; shift right
	bne.s	cw_allls

cw_alllb
	tst.b	(sp)			 ; blank line: over or xor?
	beq.s	cw_alllt		 ; ... no
	tst.b	1(sp)			 ; double height?
	bge.l	cw_allli		 ; ... no, swap ink
	add.l	a2,a4			 ; ... down a row
	bra.l	cw_allln

cw_alllt
	move.l	d4,d0
	and.l	d3,d0			 ; the strip at the top
	move.l	d3,d2
	not.l	d2
	and.l	(a4),d2
	or.l	d0,d2
	move.l	d2,(a4)
	tst.b	1(sp)			 ; double height?
	bge.l	cw_allle		 ; ... no
	exg	d4,d5
	add.l	a2,a4
	move.l	d4,d0
	and.l	d3,d0
	move.l	d3,d2
	not.l	d2
	and.l	(a4),d2
	or.l	d0,d2
	move.l	d2,(a4)
	exg	d4,d5
	bra.l	cw_allln

cw_allls
	tst.b	(sp)
	beq.s	cw_alllw
	blt.s	cw_alllx

	move.l	d0,a0
	move.l	d0,d2
	not.l	d2			 ; inverse character
	and.l	(a4),d2 		 ; the background
	and.l	d6,d0
	or.l	d2,d0			 ; the new character square
	move.l	d0,(a4) 		 ; back in the screen

	tst.b	1(sp)			 ; double height?
	bge.s	cw_allli		 ; ... no
	exg	d6,d7
	add.l	a2,a4
	move.l	a0,d0
	move.l	d0,d2
	not.l	d2			 ; inverse character
	and.l	(a4),d2 		 ; the background
	and.l	d6,d0
	or.l	d2,d0			 ; the new character square
	move.l	d0,(a4) 		 ; back in the screen

	bra.s	cw_allli

cw_alllx
	move.l	d0,a0
	and.l	d6,d0
	eor.l	d0,(a4) 		 ; xor into the screen

	tst.b	1(sp)			 ; double height?
	bge.s	cw_allli		 ; ... no
	exg	d6,d7
	add.l	a2,a4
	move.l	a0,d0
	and.l	d6,d0
	eor.l	d0,(a4) 		 ; xor into the screen

	bra.s	cw_allli

cw_alllw
	move.l	d0,a0			 ; keep this
	move.l	d0,d2
	not.l	d2			 ; inverse character
	and.l	d4,d2
	and.l	d3,d2			 ; the strip
	and.l	d6,d0			 ; the ink
	or.l	d0,d2			 ; the new character square
	move.l	d3,d0
	not.l	d0
	and.l	(a4),d0 		 ; the rest of the word from screen
	or.l	d2,d0
	move.l	d0,(a4) 		 ; back in the screen

	tst.b	1(sp)			 ; double height?
	bge.s	cw_allle		 ; ... no
	exg	d4,d5
	exg	d6,d7
	add.l	a2,a4
	move.l	a0,d0
	move.l	d0,d2
	not.l	d2			 ; inverse character
	and.l	d4,d2
	and.l	d3,d2			 ; the strip
	and.l	d6,d0			 ; the ink
	or.l	d0,d2			 ; the new character square
	move.l	d3,d0
	not.l	d0
	and.l	(a4),d0 		 ; the rest of the word from screen
	or.l	d2,d0
	move.l	d0,(a4) 		 ; back in the screen

cw_allle
	exg	d4,d5
cw_allli
	exg	d6,d7
cw_allln
	add.l	a2,a4			 ; next row
	dbra	d1,cw_allll

	swap	d1
	tst.w	d1			 ; underscore?
	beq.s	cw_exit 		 ; ... no
	ext.l	d1
	move.l	d3,d0			 ; the underscore at the bottom
	addq.l	#1,a3
	bra.l	cw_allls

cw_exit
	addq.l	#2,sp			 ; clean up the stack
	movem.l (sp)+,a0/a1
	bra.l	cnsm_next

	section ctab

cw_spread
	dc.w	$0000,$0030,$00C0,$00F0,$0300,$0330,$03C0,$03F0
	dc.w	$0C00,$0C30,$0CC0,$0CF0,$0F00,$0F30,$0FC0,$0FF0
	dc.w	$3000,$3030,$30C0,$30F0,$3300,$3330,$33C0,$33F0
	dc.w	$3C00,$3C30,$3CC0,$3CF0,$3F00,$3F30,$3FC0,$3FF0
	dc.w	$C000,$C030,$C0C0,$C0F0,$C300,$C330,$C3C0,$C3F0
	dc.w	$CC00,$CC30,$CCC0,$CCF0,$CF00,$CF30,$CFC0,$CFF0
	dc.w	$F000,$F030,$F0C0,$F0F0,$F300,$F330,$F3C0,$F3F0
	dc.w	$FC00,$FC30,$FCC0,$FCF0,$FF00,$FF30,$FFC0,$FFF0


cc_shift
	dc.l	$00000000,$04000000,$08000000,$0C000000
	dc.l	$10000000,$14000000,$18000000,$1C000000
	dc.l	$20000000,$24000000,$28000000,$2C000000
	dc.l	$30000000,$34000000,$38000000,$3C000000
	dc.l	$40000000,$44000000,$48000000,$4C000000
	dc.l	$50000000,$54000000,$58000000,$5C000000
	dc.l	$60000000,$64000000,$68000000,$6C000000
	dc.l	$70000000,$74000000,$78000000,$7C000000
	dc.l	$80000000,$84000000,$88000000,$8C000000
	dc.l	$90000000,$94000000,$98000000,$9C000000
	dc.l	$A0000000,$A4000000,$A8000000,$AC000000
	dc.l	$B0000000,$B4000000,$B8000000,$BC000000
	dc.l	$C0000000,$C4000000,$C8000000,$CC000000
	dc.l	$D0000000,$D4000000,$D8000000,$DC000000
	dc.l	$E0000000,$E4000000,$E8000000,$EC000000
	dc.l	$F0000000,$F4000000,$F8000000,$FC000000

	dc.l	$00000000,$02000000,$04000000,$06000000
	dc.l	$08000000,$0A000000,$0C000000,$0E000000
	dc.l	$10000000,$12000000,$14000000,$16000000
	dc.l	$18000000,$1A000000,$1C000000,$1E000000
	dc.l	$20000000,$22000000,$24000000,$26000000
	dc.l	$28000000,$2A000000,$2C000000,$2E000000
	dc.l	$30000000,$32000000,$34000000,$36000000
	dc.l	$38000000,$3A000000,$3C000000,$3E000000
	dc.l	$40000000,$42000000,$44000000,$46000000
	dc.l	$48000000,$4A000000,$4C000000,$4E000000
	dc.l	$50000000,$52000000,$54000000,$56000000
	dc.l	$58000000,$5A000000,$5C000000,$5E000000
	dc.l	$60000000,$62000000,$64000000,$66000000
	dc.l	$68000000,$6A000000,$6C000000,$6E000000
	dc.l	$70000000,$72000000,$74000000,$76000000
	dc.l	$78000000,$7A000000,$7C000000,$7E000000

	dc.l	$00000000,$01000000,$02000000,$03000000
	dc.l	$04000000,$05000000,$06000000,$07000000
	dc.l	$08000000,$09000000,$0A000000,$0B000000
	dc.l	$0C000000,$0D000000,$0E000000,$0F000000
	dc.l	$10000000,$11000000,$12000000,$13000000
	dc.l	$14000000,$15000000,$16000000,$17000000
	dc.l	$18000000,$19000000,$1A000000,$1B000000
	dc.l	$1C000000,$1D000000,$1E000000,$1F000000
	dc.l	$20000000,$21000000,$22000000,$23000000
	dc.l	$24000000,$25000000,$26000000,$27000000
	dc.l	$28000000,$29000000,$2A000000,$2B000000
	dc.l	$2C000000,$2D000000,$2E000000,$2F000000
	dc.l	$30000000,$31000000,$32000000,$33000000
	dc.l	$34000000,$35000000,$36000000,$37000000
	dc.l	$38000000,$39000000,$3A000000,$3B000000
	dc.l	$3C000000,$3D000000,$3E000000,$3F000000

	dc.l	$00000000,$00800000,$01000000,$01800000
	dc.l	$02000000,$02800000,$03000000,$03800000
	dc.l	$04000000,$04800000,$05000000,$05800000
	dc.l	$06000000,$06800000,$07000000,$07800000
	dc.l	$08000000,$08800000,$09000000,$09800000
	dc.l	$0A000000,$0A800000,$0B000000,$0B800000
	dc.l	$0C000000,$0C800000,$0D000000,$0D800000
	dc.l	$0E000000,$0E800000,$0F000000,$0F800000
	dc.l	$10000000,$10800000,$11000000,$11800000
	dc.l	$12000000,$12800000,$13000000,$13800000
	dc.l	$14000000,$14800000,$15000000,$15800000
	dc.l	$16000000,$16800000,$17000000,$17800000
	dc.l	$18000000,$18800000,$19000000,$19800000
	dc.l	$1A000000,$1A800000,$1B000000,$1B800000
	dc.l	$1C000000,$1C800000,$1D000000,$1D800000
	dc.l	$1E000000,$1E800000,$1F000000,$1F800000

	dc.l	$00000000,$00400000,$00800000,$00C00000
	dc.l	$01000000,$01400000,$01800000,$01C00000
	dc.l	$02000000,$02400000,$02800000,$02C00000
	dc.l	$03000000,$03400000,$03800000,$03C00000
	dc.l	$04000000,$04400000,$04800000,$04C00000
	dc.l	$05000000,$05400000,$05800000,$05C00000
	dc.l	$06000000,$06400000,$06800000,$06C00000
	dc.l	$07000000,$07400000,$07800000,$07C00000
	dc.l	$08000000,$08400000,$08800000,$08C00000
	dc.l	$09000000,$09400000,$09800000,$09C00000
	dc.l	$0A000000,$0A400000,$0A800000,$0AC00000
	dc.l	$0B000000,$0B400000,$0B800000,$0BC00000
	dc.l	$0C000000,$0C400000,$0C800000,$0CC00000
	dc.l	$0D000000,$0D400000,$0D800000,$0DC00000
	dc.l	$0E000000,$0E400000,$0E800000,$0EC00000
	dc.l	$0F000000,$0F400000,$0F800000,$0FC00000

	dc.l	$00000000,$00200000,$00400000,$00600000
	dc.l	$00800000,$00A00000,$00C00000,$00E00000
	dc.l	$01000000,$01200000,$01400000,$01600000
	dc.l	$01800000,$01A00000,$01C00000,$01E00000
	dc.l	$02000000,$02200000,$02400000,$02600000
	dc.l	$02800000,$02A00000,$02C00000,$02E00000
	dc.l	$03000000,$03200000,$03400000,$03600000
	dc.l	$03800000,$03A00000,$03C00000,$03E00000
	dc.l	$04000000,$04200000,$04400000,$04600000
	dc.l	$04800000,$04A00000,$04C00000,$04E00000
	dc.l	$05000000,$05200000,$05400000,$05600000
	dc.l	$05800000,$05A00000,$05C00000,$05E00000
	dc.l	$06000000,$06200000,$06400000,$06600000
	dc.l	$06800000,$06A00000,$06C00000,$06E00000
	dc.l	$07000000,$07200000,$07400000,$07600000
	dc.l	$07800000,$07A00000,$07C00000,$07E00000

	dc.l	$00000000,$00100000,$00200000,$00300000
	dc.l	$00400000,$00500000,$00600000,$00700000
	dc.l	$00800000,$00900000,$00A00000,$00B00000
	dc.l	$00C00000,$00D00000,$00E00000,$00F00000
	dc.l	$01000000,$01100000,$01200000,$01300000
	dc.l	$01400000,$01500000,$01600000,$01700000
	dc.l	$01800000,$01900000,$01A00000,$01B00000
	dc.l	$01C00000,$01D00000,$01E00000,$01F00000
	dc.l	$02000000,$02100000,$02200000,$02300000
	dc.l	$02400000,$02500000,$02600000,$02700000
	dc.l	$02800000,$02900000,$02A00000,$02B00000
	dc.l	$02C00000,$02D00000,$02E00000,$02F00000
	dc.l	$03000000,$03100000,$03200000,$03300000
	dc.l	$03400000,$03500000,$03600000,$03700000
	dc.l	$03800000,$03900000,$03A00000,$03B00000
	dc.l	$03C00000,$03D00000,$03E00000,$03F00000

	dc.l	$00000000,$00080000,$00100000,$00180000
	dc.l	$00200000,$00280000,$00300000,$00380000
	dc.l	$00400000,$00480000,$00500000,$00580000
	dc.l	$00600000,$00680000,$00700000,$00780000
	dc.l	$00800000,$00880000,$00900000,$00980000
	dc.l	$00A00000,$00A80000,$00B00000,$00B80000
	dc.l	$00C00000,$00C80000,$00D00000,$00D80000
	dc.l	$00E00000,$00E80000,$00F00000,$00F80000
	dc.l	$01000000,$01080000,$01100000,$01180000
	dc.l	$01200000,$01280000,$01300000,$01380000
	dc.l	$01400000,$01480000,$01500000,$01580000
	dc.l	$01600000,$01680000,$01700000,$01780000
	dc.l	$01800000,$01880000,$01900000,$01980000
	dc.l	$01A00000,$01A80000,$01B00000,$01B80000
	dc.l	$01C00000,$01C80000,$01D00000,$01D80000
	dc.l	$01E00000,$01E80000,$01F00000,$01F80000

	dc.l	$00000000,$00040000,$00080000,$000C0000
	dc.l	$00100000,$00140000,$00180000,$001C0000
	dc.l	$00200000,$00240000,$00280000,$002C0000
	dc.l	$00300000,$00340000,$00380000,$003C0000
	dc.l	$00400000,$00440000,$00480000,$004C0000
	dc.l	$00500000,$00540000,$00580000,$005C0000
	dc.l	$00600000,$00640000,$00680000,$006C0000
	dc.l	$00700000,$00740000,$00780000,$007C0000
	dc.l	$00800000,$00840000,$00880000,$008C0000
	dc.l	$00900000,$00940000,$00980000,$009C0000
	dc.l	$00A00000,$00A40000,$00A80000,$00AC0000
	dc.l	$00B00000,$00B40000,$00B80000,$00BC0000
	dc.l	$00C00000,$00C40000,$00C80000,$00CC0000
	dc.l	$00D00000,$00D40000,$00D80000,$00DC0000
	dc.l	$00E00000,$00E40000,$00E80000,$00EC0000
	dc.l	$00F00000,$00F40000,$00F80000,$00FC0000

	dc.l	$00000000,$00020000,$00040000,$00060000
	dc.l	$00080000,$000A0000,$000C0000,$000E0000
	dc.l	$00100000,$00120000,$00140000,$00160000
	dc.l	$00180000,$001A0000,$001C0000,$001E0000
	dc.l	$00200000,$00220000,$00240000,$00260000
	dc.l	$00280000,$002A0000,$002C0000,$002E0000
	dc.l	$00300000,$00320000,$00340000,$00360000
	dc.l	$00380000,$003A0000,$003C0000,$003E0000
	dc.l	$00400000,$00420000,$00440000,$00460000
	dc.l	$00480000,$004A0000,$004C0000,$004E0000
	dc.l	$00500000,$00520000,$00540000,$00560000
	dc.l	$00580000,$005A0000,$005C0000,$005E0000
	dc.l	$00600000,$00620000,$00640000,$00660000
	dc.l	$00680000,$006A0000,$006C0000,$006E0000
	dc.l	$00700000,$00720000,$00740000,$00760000
	dc.l	$00780000,$007A0000,$007C0000,$007E0000

	dc.l	$00000000,$00010000,$00020000,$00030000
	dc.l	$00040000,$00050000,$00060000,$00070000
	dc.l	$00080000,$00090000,$000A0000,$000B0000
	dc.l	$000C0000,$000D0000,$000E0000,$000F0000
	dc.l	$00100000,$00110000,$00120000,$00130000
	dc.l	$00140000,$00150000,$00160000,$00170000
	dc.l	$00180000,$00190000,$001A0000,$001B0000
	dc.l	$001C0000,$001D0000,$001E0000,$001F0000
	dc.l	$00200000,$00210000,$00220000,$00230000
	dc.l	$00240000,$00250000,$00260000,$00270000
	dc.l	$00280000,$00290000,$002A0000,$002B0000
	dc.l	$002C0000,$002D0000,$002E0000,$002F0000
	dc.l	$00300000,$00310000,$00320000,$00330000
	dc.l	$00340000,$00350000,$00360000,$00370000
	dc.l	$00380000,$00390000,$003A0000,$003B0000
	dc.l	$003C0000,$003D0000,$003E0000,$003F0000

	dc.l	$00000000,$00008000,$00010000,$00018000
	dc.l	$00020000,$00028000,$00030000,$00038000
	dc.l	$00040000,$00048000,$00050000,$00058000
	dc.l	$00060000,$00068000,$00070000,$00078000
	dc.l	$00080000,$00088000,$00090000,$00098000
	dc.l	$000A0000,$000A8000,$000B0000,$000B8000
	dc.l	$000C0000,$000C8000,$000D0000,$000D8000
	dc.l	$000E0000,$000E8000,$000F0000,$000F8000
	dc.l	$00100000,$00108000,$00110000,$00118000
	dc.l	$00120000,$00128000,$00130000,$00138000
	dc.l	$00140000,$00148000,$00150000,$00158000
	dc.l	$00160000,$00168000,$00170000,$00178000
	dc.l	$00180000,$00188000,$00190000,$00198000
	dc.l	$001A0000,$001A8000,$001B0000,$001B8000
	dc.l	$001C0000,$001C8000,$001D0000,$001D8000
	dc.l	$001E0000,$001E8000,$001F0000,$001F8000

	dc.l	$00000000,$00004000,$00008000,$0000C000
	dc.l	$00010000,$00014000,$00018000,$0001C000
	dc.l	$00020000,$00024000,$00028000,$0002C000
	dc.l	$00030000,$00034000,$00038000,$0003C000
	dc.l	$00040000,$00044000,$00048000,$0004C000
	dc.l	$00050000,$00054000,$00058000,$0005C000
	dc.l	$00060000,$00064000,$00068000,$0006C000
	dc.l	$00070000,$00074000,$00078000,$0007C000
	dc.l	$00080000,$00084000,$00088000,$0008C000
	dc.l	$00090000,$00094000,$00098000,$0009C000
	dc.l	$000A0000,$000A4000,$000A8000,$000AC000
	dc.l	$000B0000,$000B4000,$000B8000,$000BC000
	dc.l	$000C0000,$000C4000,$000C8000,$000CC000
	dc.l	$000D0000,$000D4000,$000D8000,$000DC000
	dc.l	$000E0000,$000E4000,$000E8000,$000EC000
	dc.l	$000F0000,$000F4000,$000F8000,$000FC000

	dc.l	$00000000,$00002000,$00004000,$00006000
	dc.l	$00008000,$0000A000,$0000C000,$0000E000
	dc.l	$00010000,$00012000,$00014000,$00016000
	dc.l	$00018000,$0001A000,$0001C000,$0001E000
	dc.l	$00020000,$00022000,$00024000,$00026000
	dc.l	$00028000,$0002A000,$0002C000,$0002E000
	dc.l	$00030000,$00032000,$00034000,$00036000
	dc.l	$00038000,$0003A000,$0003C000,$0003E000
	dc.l	$00040000,$00042000,$00044000,$00046000
	dc.l	$00048000,$0004A000,$0004C000,$0004E000
	dc.l	$00050000,$00052000,$00054000,$00056000
	dc.l	$00058000,$0005A000,$0005C000,$0005E000
	dc.l	$00060000,$00062000,$00064000,$00066000
	dc.l	$00068000,$0006A000,$0006C000,$0006E000
	dc.l	$00070000,$00072000,$00074000,$00076000
	dc.l	$00078000,$0007A000,$0007C000,$0007E000

	dc.l	$00000000,$00001000,$00002000,$00003000
	dc.l	$00004000,$00005000,$00006000,$00007000
	dc.l	$00008000,$00009000,$0000A000,$0000B000
	dc.l	$0000C000,$0000D000,$0000E000,$0000F000
	dc.l	$00010000,$00011000,$00012000,$00013000
	dc.l	$00014000,$00015000,$00016000,$00017000
	dc.l	$00018000,$00019000,$0001A000,$0001B000
	dc.l	$0001C000,$0001D000,$0001E000,$0001F000
	dc.l	$00020000,$00021000,$00022000,$00023000
	dc.l	$00024000,$00025000,$00026000,$00027000
	dc.l	$00028000,$00029000,$0002A000,$0002B000
	dc.l	$0002C000,$0002D000,$0002E000,$0002F000
	dc.l	$00030000,$00031000,$00032000,$00033000
	dc.l	$00034000,$00035000,$00036000,$00037000
	dc.l	$00038000,$00039000,$0003A000,$0003B000
	dc.l	$0003C000,$0003D000,$0003E000,$0003F000

	dc.l	$00000000,$00000800,$00001000,$00001800
	dc.l	$00002000,$00002800,$00003000,$00003800
	dc.l	$00004000,$00004800,$00005000,$00005800
	dc.l	$00006000,$00006800,$00007000,$00007800
	dc.l	$00008000,$00008800,$00009000,$00009800
	dc.l	$0000A000,$0000A800,$0000B000,$0000B800
	dc.l	$0000C000,$0000C800,$0000D000,$0000D800
	dc.l	$0000E000,$0000E800,$0000F000,$0000F800
	dc.l	$00010000,$00010800,$00011000,$00011800
	dc.l	$00012000,$00012800,$00013000,$00013800
	dc.l	$00014000,$00014800,$00015000,$00015800
	dc.l	$00016000,$00016800,$00017000,$00017800
	dc.l	$00018000,$00018800,$00019000,$00019800
	dc.l	$0001A000,$0001A800,$0001B000,$0001B800
	dc.l	$0001C000,$0001C800,$0001D000,$0001D800
	dc.l	$0001E000,$0001E800,$0001F000,$0001F800

	end
