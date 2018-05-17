; CON2 16 bit  Send characters to CONSOLE  V2.01     1999 Tony Tebby
;						      2016 Marcel Kilgus
;
; 2016-04-16  2.01  Added alpha blending (MK)

	section con

	xdef	cn_sbyte
	xdef	cn_smulc

	xref	cn_cksize_p
	xref	cn_donl
	xref	bm_apixel

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
	move.w	sd_linel(a0),a2 	 ; will be modified for print mode
	sub.w	#12,a2			 ; standard print mode

	moveq	#$ff-1<<sd..gmod-1<<sd..flsh,d0 ; ignore graphics and flash bit
	and.b	sd_cattr(a0),d0
	cmp.b	#$ff,sd_alpha(a0)
	beq.s	cnsm_noalpha
	ori.b	#1<<sd..flsh,d0 	 ; we abuse the flash bit for alpha here
cnsm_noalpha
	add.w	d0,d0
	lea	cnsm_optab,a5		 ; operation table
	add.w	(a5,d0.w),a5		 ; ... operation

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
	beq.s	cnsm_scolour		 ; ... no
	add.w	d5,a1			 ; ... yes, skip these
cnsm_more
	tst.w	stk_count(sp)		 ; any more characters?
	bgt.l	cnsm_sloop
	bra.l	cnsm_finished

; set up colours

cnsm_scolour
	movem.l sd_smask(a0),d5/d7	 ; set colour contrast main
;
;	d4 c  p strip colour mask first pixel (second row, first row)
;	d5 c  p strip colour mask next pixel
;	d6 c  p ink colour mask first pixel
;	d7 c  p ink colour mask next pixel
;
	move.l	d5,d4			 ; assume strip colour is solid
	move.w	#%11111000,d0
	and.b	sd_scolr(a0),d0
	beq.s	cnsm_icolr

	lsr.w	#6,d0
	add.w	d0,d0
	move.w	cnsm_stable(pc,d0.w),d0
	jmp	cnsm_stable(pc,d0.w)
cnsm_stable
	dc.w	cnsms_1of4-cnsm_stable
	dc.w	cnsms_horiz-cnsm_stable
	dc.w	cnsms_vert-cnsm_stable
	dc.w	cnsms_check-cnsm_stable

cnsms_1of4
	swap	d5
	move.w	d5,d4			 ; first column is solid
	swap	d5
	bra.s	cnsm_icolr

cnsms_vert
	swap	d5			 ; odd column is contrast
	move.w	d4,d0
	move.w	d5,d4			 ; in both lines
	move.w	d0,d5
	bra.s	cnsm_icolr

cnsms_horiz
	swap	d4			 ; even column is different

cnsms_check
	swap	d5			 ; odd column is different

cnsm_icolr
	move.l	d7,d6			 ; assume ink colour is solid
	move.w	#%11111000,d0
	and.b	sd_icolr(a0),d0
	beq.s	cnsm_colrset

	lsr.w	#6,d0
	add.w	d0,d0
	move.w	cnsm_itable(pc,d0.w),d0
	jmp	cnsm_itable(pc,d0.w)
cnsm_itable
	dc.w	cnsmi_1of4-cnsm_itable
	dc.w	cnsmi_horiz-cnsm_itable
	dc.w	cnsmi_vert-cnsm_itable
	dc.w	cnsmi_check-cnsm_itable

cnsmi_1of4
	swap	d7
	move.w	d7,d6			 ; first column is solid
	swap	d7
	bra.s	cnsm_colrset

cnsmi_vert
	swap	d7			 ; odd column is contrast
	move.w	d6,d0
	move.w	d7,d6			 ; in both lines
	move.w	d0,d7
	bra.s	cnsm_colrset

cnsmi_horiz
	swap	d6			 ; even column is different

cnsmi_check
	swap	d7			 ; odd column is different

cnsm_colrset


; set row address

cnsm_srow
	move.w	sd_linel(a0),d2
	move.w	sd_ymin(a0),d1
	add.w	sd_ypos(a0),d1
	mulu	d1,d2			 ; row start in screen
	add.l	sd_scrb(a0),d2
	move.l	d2,stk_base(sp) 	 ; veritable row address

	move.w	sd_xmin(a0),d0		 ; area origin
	add.w	sd_xpos(a0),d0		 ; cursor origin

cnsm_colrpos
	btst	#0,d0
	beq.s	cnsm_colry
	exg	d4,d5
	exg	d6,d7			 ; odd pixel

cnsm_colry
	lsr.w	#1,d1
	bcc.s	cnsm_colrdone
	swap	d4
	swap	d5
	swap	d6			 ; odd row
	swap	d7

cnsm_colrdone

; now do a slice

	move.w	stk_slice(sp),d1
cnsm_cloop
	move.l	stk_base(sp),a4
	move.w	d1,-(sp)
	move.w	d0,-(sp)

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
	move.w	sd_xinc(a0),d2
	add.w	d2,d0			 ; new x pos

	lsr.w	#1,d2
	bcc.s	cnsm_ecloop
	exg	d4,d5
	exg	d6,d7			 ; odd/even pixel

cnsm_ecloop
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
	dc.w	cc_stdch-.,cc_stdul-.,cc_alpch-.,cc_alpul-.
	dc.w	cc_ovrch-.,cc_ovrul-.,cc_alpch-.,cc_alpul-.
	dc.w	cc_xorch-.,cc_xorul-.,cc_xorch-.,cc_xorul-.
	dc.w	cc_xorch-.,cc_xorul-.,cc_xorch-.,cc_xorul-.

	dc.w	ch_stdch-.,ch_stdul-.,ch_alpch-.,ch_alpul-.
	dc.w	ch_ovrch-.,ch_ovrul-.,ch_alpch-.,ch_alpul-.
	dc.w	ch_xorch-.,ch_xorul-.,ch_xorch-.,ch_xorul-.
	dc.w	ch_xorch-.,ch_xorul-.,ch_xorch-.,ch_xorul-.

	dc.w	cc_stdcx-.,cc_stdux-.,cc_alpcx-.,cc_alpux-.
	dc.w	cc_ovrcx-.,cc_ovrux-.,cc_alpcx-.,cc_alpux-.
	dc.w	cc_xorcx-.,cc_xorux-.,cc_xorcx-.,cc_xorux-.
	dc.w	cc_xorcx-.,cc_xorux-.,cc_xorcx-.,cc_xorux-.

	dc.w	ch_stdcx-.,ch_stdux-.,ch_alpcx-.,ch_alpux-.
	dc.w	ch_ovrcx-.,ch_ovrux-.,ch_alpcx-.,ch_alpux-.
	dc.w	ch_xorcx-.,ch_xorux-.,ch_xorcx-.,ch_xorux-.
	dc.w	ch_xorcx-.,ch_xorux-.,ch_xorcx-.,ch_xorux-.

	dc.w	cw_stdch-.,cw_stdul-.,cw_alpch-.,cw_alpul-.
	dc.w	cw_ovrch-.,cw_ovrul-.,cw_alpch-.,cw_alpul-.
	dc.w	cw_xorch-.,cw_xorul-.,cw_xorch-.,cw_xorul-.
	dc.w	cw_xorch-.,cw_xorul-.,cw_xorch-.,cw_xorul-.

	dc.w	cz_stdch-.,cz_stdul-.,cz_alpch-.,cz_alpul-.
	dc.w	cz_ovrch-.,cz_ovrul-.,cz_alpch-.,cz_alpul-.
	dc.w	cz_xorch-.,cz_xorul-.,cz_xorch-.,cz_xorul-.
	dc.w	cz_xorch-.,cz_xorul-.,cz_xorch-.,cz_xorul-.

	dc.w	cw_stdcx-.,cw_stdux-.,cw_alpcx-.,cw_alpux-.
	dc.w	cw_ovrcx-.,cw_ovrux-.,cw_alpcx-.,cw_alpux-.
	dc.w	cw_xorcx-.,cw_xorux-.,cw_xorcx-.,cw_xorux-.
	dc.w	cw_xorcx-.,cw_xorux-.,cw_xorcx-.,cw_xorux-.

	dc.w	cz_stdcx-.,cz_stdux-.,cz_alpcx-.,cz_alpux-.
	dc.w	cz_ovrcx-.,cz_ovrux-.,cz_alpcx-.,cz_alpux-.
	dc.w	cz_xorcx-.,cz_xorux-.,cz_xorcx-.,cz_xorux-.
	dc.w	cz_xorcx-.,cz_xorux-.,cz_xorcx-.,cz_xorux-.

	page
;+++
; General character drawing code
;
;	d4 c  p strip colour mask first pixel (second row, first row)
;	d5 c  p strip colour mask next pixel
;	d6 c  p ink colour mask first pixel
;	d7 c  p ink colour mask next pixel
;	a2 c  u line increment
;	a3 cs	pointer to fount patterns for this character
;	a4 cs	pointer to screen
;	a5 cr	pointer to character code
;---

;+++
; Standard size underline character drawing code
;---
cc_stdul
	move.l	#$ffff0006,d1		 ; one + seven + two rows
	bra.s	cc_stdc1

;+++
; Standard size / attribute character drawing code
;---
cc_stdch
	moveq	#8,d1			 ; nine rows

cc_stdc1
	move.w	d4,(a4)+		 ; the blank line
	move.w	d5,(a4)+
	move.w	d4,(a4)+		 ; the blank line
	move.w	d5,(a4)+
	move.w	d4,(a4)+		 ; the blank line
	move.w	d5,(a4)+

	swap	d4
	swap	d5
	swap	d6
	swap	d7

cc_stdll
	add.l	a2,a4
	move.b	(a3)+,d2

	add.b	d2,d2
	bcc.s	cc_stdpap0
	move.w	d6,(a4)+		 ; ink point
	bra.s	cc_stdpx1
cc_stdpap0
	move.w	d4,(a4)+		 ; paper point

cc_stdpx1
	add.b	d2,d2
	bcc.s	cc_stdpap1
	move.w	d7,(a4)+		 ; ink point
	bra.s	cc_stdpx2
cc_stdpap1
	move.w	d5,(a4)+		 ; paper point

cc_stdpx2
	add.b	d2,d2
	bcc.s	cc_stdpap2
	move.w	d6,(a4)+		 ; ink point
	bra.s	cc_stdpx3
cc_stdpap2
	move.w	d4,(a4)+		 ; paper point

cc_stdpx3
	add.b	d2,d2
	bcc.s	cc_stdpap3
	move.w	d7,(a4)+		 ; ink point
	bra.s	cc_stdpx4
cc_stdpap3
	move.w	d5,(a4)+		 ; paper point

cc_stdpx4
	add.b	d2,d2
	bcc.s	cc_stdpap4
	move.w	d6,(a4)+		 ; ink point
	bra.s	cc_stdpx5
cc_stdpap4
	move.w	d4,(a4)+		 ; paper point

cc_stdpx5
	add.b	d2,d2
	bcc.s	cc_stdpap5
	move.w	d7,(a4)+		 ; ink point
	bra.s	cc_stdls
cc_stdpap5
	move.w	d5,(a4)+		 ; paper point

cc_stdls
	swap	d4
	swap	d5
	swap	d6
	swap	d7

	dbra	d1,cc_stdll

	tst.l	d1			 ; underscore?
	bpl	cnsm_next		 ; ... no

	add.l	a2,a4
	move.w	d6,(a4)+		 ; the underscore
	move.w	d7,(a4)+
	move.w	d6,(a4)+		 ; the underscore
	move.w	d7,(a4)+
	move.w	d6,(a4)+		 ; the underscore
	move.w	d7,(a4)+
	moveq	#1,d1
	addq.l	#1,a3			 ; skip font line
	bra.s	cc_stdls

;+++
; Standard size underline extended character drawing code
;---
cc_stdux
	subq.l	#4,a2			 ; reduce increment
	addq.l	#4,a5			 ; and move entry
	move.l	#$ffff0006,d1		 ; one + seven + two rows
	bra.s	cc_stdx

;+++
; Standard size extended character drawing code
;---
cc_stdcx
	subq.l	#4,a2			 ; reduce increment
	addq.l	#4,a5			 ; and move entry

	moveq	#8,d1			 ; one + nine rows
cc_stdx
	moveq	#3,d0
cc_stdxbl
	move.w	d4,(a4)+		 ; the blank line
	move.w	d5,(a4)+
	dbra	d0,cc_stdxbl

	swap	d4
	swap	d5
	swap	d6
	swap	d7

cc_stdxll
	add.l	a2,a4
	move.b	(a3)+,d2

	moveq	#3,d0
cc_stdxpl
	add.b	d2,d2
	bcs.s	cc_stdxink0
	move.w	d4,(a4)+		 ; paper point
	add.b	d2,d2
	bcs.s	cc_stdxink1
	move.w	d5,(a4)+		 ; paper point
	dbra	d0,cc_stdxpl
	bra.s	cc_stdxls

cc_stdxink0
	move.w	d6,(a4)+		 ; ink point
	add.b	d2,d2
	bcs.s	cc_stdxink1
	move.w	d5,(a4)+		 ; paper point
	dbra	d0,cc_stdxpl
	bra.s	cc_stdxls

cc_stdxink1
	move.w	d7,(a4)+		 ; ink point
	dbra	d0,cc_stdxpl

cc_stdxls
	swap	d4
	swap	d5
	swap	d6
	swap	d7

	dbra	d1,cc_stdxll

	tst.l	d1			 ; underscore?
	bpl	cnsm_next		 ; ... no

	add.l	a2,a4
	moveq	#3,d0
cc_stdxull
	move.w	d6,(a4)+		 ; the underscore
	move.w	d7,(a4)+
	dbra	d0,cc_stdxull

	moveq	#1,d1
	addq.l	#1,a3			 ; skip font line
	bra.s	cc_stdxls


;+++
; Double width underline character drawing code
;---
cw_stdul
	sub.w	#12,a2			 ; reduce increment
	addq.l	#6,a5			 ; and move entry
	move.l	#$ffff0006,d1		 ; one + seven + two rows
	move.w	#5,d3
	bra.s	cw_std

;+++
; Double width character drawing code
;---
cw_stdch
	sub.w	#12,a2			 ; reduce increment
	addq.l	#6,a5			 ; and move entry
	moveq	#8,d1			 ; one + nine rows
	move.w	#5,d3
	bra.s	cw_std

;+++
; Double width underline extended character drawing code
;---
cw_stdux
	sub.w	#20,a2			 ; reduce increment
	addq.l	#6,a5			 ; and move entry
	move.l	#$ffff0006,d1		 ; one + seven + two rows
	move.w	#7,d3
	bra.s	cw_std

;+++
; Double width extended character drawing code
;---
cw_stdcx
	sub.w	#20,a2			 ; reduce increment
	addq.l	#6,a5			 ; and move entry
	moveq	#8,d1			 ; one + nine rows
	move.w	#7,d3

cw_std
	move.w	d3,d0
cw_stdbl
	move.w	d4,(a4)+		 ; the blank line
	move.w	d5,(a4)+
	dbra	d0,cw_stdbl

	swap	d4
	swap	d5
	swap	d6
	swap	d7

cw_stdll
	add.l	a2,a4
	move.b	(a3)+,d2

	move.w	d3,d0
cw_stdpl
	add.b	d2,d2
	bcc.s	cw_stdpap
	move.w	d6,(a4)+		 ; ink point
	move.w	d7,(a4)+		 ; ink point
	dbra	d0,cw_stdpl
	bra.s	cw_stdls

cw_stdpap
	move.w	d4,(a4)+		 ; paper point
	move.w	d5,(a4)+		 ; paper point
	dbra	d0,cw_stdpl

cw_stdls
	swap	d4
	swap	d5
	swap	d6
	swap	d7

	dbra	d1,cw_stdll

	tst.l	d1			 ; underscore?
	bpl	cnsm_next		 ; ... no

	add.l	a2,a4
	move.w	d3,d0
cw_stdull
	move.w	d6,(a4)+		 ; the underscore
	move.w	d7,(a4)+
	dbra	d0,cw_stdull

	moveq	#1,d1
	addq.l	#1,a3			 ; skip font line
	bra.s	cw_stdls

;+++
; Double height underline character drawing code
;---
ch_stdul
	move.l	#$fffd000d,d1		 ; two + fourteen + four rows
	move.w	#2,d3
	bra.s	ch_std

;+++
; Double height character drawing code
;---
ch_stdch
	moveq	#$11,d1 		 ; eighteen rows
	move.w	#2,d3
	bra.s	ch_std

;+++
; Double height underline extended character drawing code
;---
ch_stdux
	subq.l	#4,a2			 ; reduce increment
	addq.l	#4,a5			 ; and move entry

	move.l	#$fffd000d,d1		 ; two + fourteen + four rows
	move.w	#3,d3
	bra.s	ch_std

;+++
; Double height extended character drawing code
;---
ch_stdcx
	subq.l	#4,a2			 ; reduce increment
	addq.l	#4,a5			 ; and move entry

	moveq	#$11,d1 		 ; eighteen rows
	move.w	#3,d3

ch_std
	move.w	d3,d0
ch_stdbl
	move.w	d4,(a4)+		 ; the blank line
	move.w	d5,(a4)+
	dbra	d0,ch_stdbl

	swap	d4
	swap	d5
	swap	d6
	swap	d7

	add.l	a2,a4
	move.w	d3,d0
ch_stdbl1
	move.w	d4,(a4)+		 ; the second blank line
	move.w	d5,(a4)+
	dbra	d0,ch_stdbl1

	swap	d4
	swap	d5
	swap	d6
	swap	d7

ch_stdll
	move.b	(a3)+,d2
ch_stdllu
	add.l	a2,a4

	move.w	d3,d0
ch_stdpl
	add.b	d2,d2
	bcs.s	ch_stdink0
	move.w	d4,(a4)+		 ; paper point
	add.b	d2,d2
	bcs.s	ch_stdink1
	move.w	d5,(a4)+		 ; paper point
	dbra	d0,ch_stdpl
	bra.s	ch_stdls

ch_stdink0
	move.w	d6,(a4)+		 ; ink point
	add.b	d2,d2
	bcs.s	ch_stdink1
	move.w	d5,(a4)+		 ; paper point
	dbra	d0,ch_stdpl
	bra.s	ch_stdls

ch_stdink1
	move.w	d7,(a4)+		 ; ink point
	dbra	d0,ch_stdpl

ch_stdls
	swap	d4
	swap	d5
	swap	d6
	swap	d7

	btst	#0,d1			 ; first line of pair?
	beq.s	ch_stdle		 ; ... no
	subq.l	#1,a3			 ; yes backspace
ch_stdle
	dbra	d1,ch_stdll

	tst.l	d1			 ; underscore?
	bpl	cnsm_next		 ; ... no

	moveq	#-1,d2
	addq.l	#1,d1			 ; ... yes
	bne.s	ch_stdllu		 ; genuine
	addq.l	#1,a3			 ; skip font line
	moveq	#1,d1
	bra.s	ch_stdll


;+++
; Double size underline character drawing code
;---
cz_stdul
	sub.w	#12,a2			 ; reduce increment
	addq.l	#6,a5			 ; and move entry
	move.l	#$fffd000d,d1		 ; two + fourteen + four rows
	move.w	#5,d3
	bra.s	cz_std

;+++
; Double size character drawing code
;---
cz_stdch
	sub.w	#12,a2			 ; reduce increment
	addq.l	#6,a5			 ; and move entry
	moveq	#$11,d1 		 ; eighteen rows
	move.w	#5,d3
	bra.s	cz_std

;+++
; Double size underline extended character drawing code
;---
cz_stdux
	sub.w	#20,a2			 ; reduce increment
	addq.l	#6,a5			 ; and move entry
	move.l	#$fffd000d,d1		 ; two + fourteen + four rows
	move.w	#7,d3
	bra.s	cz_std

;+++
; Double size extended character drawing code
;---
cz_stdcx
	sub.w	#20,a2			 ; reduce increment
	addq.l	#6,a5			 ; and move entry
	moveq	#$11,d1 		 ; eighteen rows
	move.w	#7,d3

cz_std
	move.w	d3,d0
cz_stdbl
	move.w	d4,(a4)+		 ; the blank line
	move.w	d5,(a4)+
	dbra	d0,cz_stdbl

	swap	d4
	swap	d5
	swap	d6
	swap	d7

	add.l	a2,a4
	move.w	d3,d0
cz_stdbl1
	move.w	d4,(a4)+		 ; the blank line
	move.w	d5,(a4)+
	dbra	d0,cz_stdbl1

	swap	d4
	swap	d5
	swap	d6
	swap	d7


cz_stdll
	move.b	(a3)+,d2
cz_stdllu
	add.l	a2,a4

	move.w	d3,d0
cz_stdpl
	add.b	d2,d2
	bcc.s	cz_stdpap
	move.w	d6,(a4)+		 ; ink point
	move.w	d7,(a4)+		 ; ink point
	dbra	d0,cz_stdpl
	bra.s	cz_stdls

cz_stdpap
	move.w	d4,(a4)+		 ; paper point
	move.w	d5,(a4)+		 ; paper point
	dbra	d0,cz_stdpl

cz_stdls
	swap	d4
	swap	d5
	swap	d6
	swap	d7

	btst	#0,d1			 ; first line of pair?
	beq.s	cz_stdle		 ; ... no
	subq.l	#1,a3			 ; yes backspace
cz_stdle
	dbra	d1,cz_stdll

	tst.l	d1			 ; underscore?
	bpl	cnsm_next		 ; ... no

	moveq	#-1,d2
	addq.l	#1,d1			 ; ... yes
	bne.s	cz_stdllu		 ; ... genuine
	addq.l	#1,a3			 ; skip font line
	moveq	#1,d1
	bra.s	cz_stdll

;+++
; Standard size over underline character drawing code
;---
cc_ovrul
	move.l	#$ffff0006,d1		 ; one + seven + two rows
	bra.s	cc_ovrc1

;+++
; Standard size over character drawing code
;---
cc_ovrch
	moveq	#8,d1			 ; nine rows

cc_ovrc1
	swap	d6
	swap	d7
	add.w	#12,a4

cc_ovrll
	add.l	a2,a4
	move.b	(a3)+,d2
	bne.s	cc_ovrpx
	add.w	#12,a4			 ; blank line
	bra.s	cc_ovrls

cc_ovrpx
	add.b	d2,d2
	bcc.s	cc_ovrpap0
	move.w	d6,(a4) 		 ; ink point
cc_ovrpap0
	addq.l	#2,a4
cc_ovrpx1
	add.b	d2,d2
	bcc.s	cc_ovrpap1
	move.w	d7,(a4) 		 ; ink point
cc_ovrpap1
	addq.l	#2,a4

cc_ovrpx2
	add.b	d2,d2
	bcc.s	cc_ovrpap2
	move.w	d6,(a4) 		 ; ink point
cc_ovrpap2
	addq.l	#2,a4

cc_ovrpx3
	add.b	d2,d2
	bcc.s	cc_ovrpap3
	move.w	d7,(a4) 		 ; ink point
cc_ovrpap3
	addq.l	#2,a4

cc_ovrpx4
	add.b	d2,d2
	bcc.s	cc_ovrpap4
	move.w	d6,(a4) 		 ; ink point
cc_ovrpap4
	addq.l	#2,a4

cc_ovrpx5
	add.b	d2,d2
	bcc.s	cc_ovrpap5
	move.w	d7,(a4) 		 ; ink point
cc_ovrpap5
	addq.l	#2,a4

cc_ovrls
	swap	d6
	swap	d7

	dbra	d1,cc_ovrll

	tst.l	d1			 ; underscore?
	bpl	cnsm_next		 ; ... no

	add.l	a2,a4
	move.w	d6,(a4)+		 ; the underscore
	move.w	d7,(a4)+
	move.w	d6,(a4)+		 ; the underscore
	move.w	d7,(a4)+
	move.w	d6,(a4)+		 ; the underscore
	move.w	d7,(a4)+
	moveq	#1,d1
	addq.l	#1,a3			 ; skip font line
	bra.s	cc_ovrls

;+++
; Standard size over underline extended character drawing code
;---
cc_ovrux
	subq.l	#4,a2			 ; reduce increment
	addq.l	#4,a5			 ; and move entry
	move.l	#$ffff0006,d1		 ; one + seven + two rows
	bra.s	cc_ovrx

;+++
; Standard size over extended character drawing code
;---
cc_ovrcx
	subq.l	#4,a2			 ; reduce increment
	addq.l	#4,a5			 ; and move entry

	moveq	#8,d1			 ; one + nine rows
cc_ovrx
	swap	d6
	swap	d7
	add.w	#16,a4

cc_ovrxll
	add.l	a2,a4
	move.b	(a3)+,d2
	bne.s	cc_ovrxpx
	add.w	#16,a4			 ; blank line
	bra.s	cc_ovrxls

cc_ovrxpx
	moveq	#3,d0
cc_ovrxpl
	add.b	d2,d2
	bcc.s	cc_ovrxpap0
	move.w	d6,(a4) 		 ; ink point
cc_ovrxpap0
	addq.l	#2,a4
	add.b	d2,d2
	bcc.s	cc_ovrxpap1
	move.w	d7,(a4) 		 ; ink point
cc_ovrxpap1
	addq.l	#2,a4
	dbra	d0,cc_ovrxpl

cc_ovrxls
	swap	d6
	swap	d7

	dbra	d1,cc_ovrxll

	tst.l	d1			 ; underscore?
	bpl	cnsm_next		 ; ... no

	add.l	a2,a4
	moveq	#3,d0
cc_ovrxull
	move.w	d6,(a4)+		 ; the underscore
	move.w	d7,(a4)+
	dbra	d0,cc_ovrxull

	moveq	#1,d1
	addq.l	#1,a3			 ; skip font line
	bra.s	cc_ovrxls


;+++
; Double width over underline character drawing code
;---
cw_ovrul
	sub.w	#12,a2			 ; reduce increment
	addq.l	#6,a5			 ; and move entry
	move.l	#$ffff0006,d1		 ; one + seven + two rows
	move.w	#5,d3
	bra.s	cw_ovr

;+++
; Double width over character drawing code
;---
cw_ovrch
	sub.w	#12,a2			 ; reduce increment
	addq.l	#6,a5			 ; and move entry
	moveq	#8,d1			 ; one + nine rows
	move.w	#5,d3
	bra.s	cw_ovr

;+++
; Double width over underline extended character drawing code
;---
cw_ovrux
	sub.w	#20,a2			 ; reduce increment
	addq.l	#6,a5			 ; and move entry
	move.l	#$ffff0006,d1		 ; one + seven + two rows
	move.w	#7,d3
	bra.s	cw_ovr

;+++
; Double width over extended character drawing code
;---
cw_ovrcx
	sub.w	#20,a2			 ; reduce increment
	addq.l	#6,a5			 ; and move entry
	moveq	#8,d1			 ; one + nine rows
	move.w	#7,d3

cw_ovr
	swap	d6
	swap	d7
	move.w	d3,d0
	addq.w	#1,d0
	lsl.w	#2,d0
	add.w	d0,a4			 ; skip line

cw_ovrll
	add.l	a2,a4
	move.b	(a3)+,d2
	bne.s	cw_ovrpx
	move.w	d3,d0
	addq.w	#1,d0
	lsl.w	#2,d0
	add.w	d0,a4			 ; blank line
	bra.s	cw_ovrls

cw_ovrpx
	move.w	d3,d0
cw_ovrpl
	add.b	d2,d2
	bcc.s	cw_ovrpap
	move.w	d6,(a4)+		 ; ink point
	move.w	d7,(a4)+		 ; ink point
	dbra	d0,cw_ovrpl
	bra.s	cw_ovrls

cw_ovrpap
	addq.l	#4,a4
	dbra	d0,cw_ovrpl

cw_ovrls
	swap	d6
	swap	d7

	dbra	d1,cw_ovrll

	tst.l	d1			 ; underscore?
	bpl	cnsm_next		 ; ... no

	add.l	a2,a4
	move.w	d3,d0
cw_ovrull
	move.w	d6,(a4)+		 ; the underscore
	move.w	d7,(a4)+
	dbra	d0,cw_ovrull

	moveq	#1,d1
	addq.l	#1,a3			 ; skip font line
	bra.s	cw_ovrls

;+++
; Double height over underline character drawing code
;---
ch_ovrul
	move.l	#$fffd000d,d1		 ; two + fourteen + four rows
	move.w	#2,d3
	bra.s	ch_ovr

;+++
; Double height over character drawing code
;---
ch_ovrch
	moveq	#$11,d1 		 ; eighteen rows
	move.w	#2,d3
	bra.s	ch_ovr

;+++
; Double height over underline extended character drawing code
;---
ch_ovrux
	subq.l	#4,a2			 ; reduce increment
	addq.l	#4,a5			 ; and move entry

	move.l	#$fffd000d,d1		 ; two + fourteen + four rows
	move.w	#3,d3
	bra.s	ch_ovr

;+++
; Double height over extended character drawing code
;---
ch_ovrcx
	subq.l	#4,a2			 ; reduce increment
	addq.l	#4,a5			 ; and move entry

	moveq	#$11,d1 		 ; eighteen rows
	move.w	#3,d3

ch_ovr
	move.w	d3,d0			 ; skip two lines
	addq.w	#1,d0
	lsl.w	#3,d0
	add.w	d0,a4
	add.l	a2,a4

ch_ovrll
	move.b	(a3)+,d2
	bne.s	ch_ovrpx
	move.w	d3,d0			 ; two blank lines
	addq.w	#1,d0
	lsl.w	#3,d0
	add.w	d0,a4
	add.l	a2,a4
	add.l	a2,a4
	subq.w	#1,d1
	bra.s	ch_ovrle

ch_ovrpx
ch_ovrllu
	add.l	a2,a4

	move.w	d3,d0
ch_ovrpl
	add.b	d2,d2
	bcc.s	ch_ovrpap0
	move.w	d6,(a4) 		 ; ink point
ch_ovrpap0
	addq.l	#2,a4
	add.b	d2,d2
	bcc.s	ch_ovrpap1
	move.w	d7,(a4) 		 ; ink point
ch_ovrpap1
	addq.l	#2,a4
	dbra	d0,ch_ovrpl

ch_ovrls
	swap	d6
	swap	d7

	btst	#0,d1			 ; first line of pair?
	beq.s	ch_ovrle		 ; ... no
	subq.l	#1,a3			 ; yes backspace
ch_ovrle
	dbra	d1,ch_ovrll

	tst.l	d1			 ; underscore?
	bpl	cnsm_next		 ; ... no

	moveq	#-1,d2
	addq.l	#1,d1			 ; ... yes
	bne.s	ch_ovrllu		 ; genuine
	addq.l	#1,a3			 ; skip font line
	moveq	#1,d1
	bra.s	ch_ovrll


;+++
; Double size over underline character drawing code
;---
cz_ovrul
	sub.w	#12,a2			 ; reduce increment
	addq.l	#6,a5			 ; and move entry
	move.l	#$fffd000d,d1		 ; two + fourteen + four rows
	move.w	#5,d3
	bra.s	cz_ovr

;+++
; Double size over character drawing code
;---
cz_ovrch
	sub.w	#12,a2			 ; reduce increment
	addq.l	#6,a5			 ; and move entry
	moveq	#$11,d1 		 ; eighteen rows
	move.w	#5,d3
	bra.s	cz_ovr

;+++
; Double size over underline extended character drawing code
;---
cz_ovrux
	sub.w	#20,a2			 ; reduce increment
	addq.l	#6,a5			 ; and move entry
	move.l	#$fffd000d,d1		 ; two + fourteen + four rows
	move.w	#7,d3
	bra.s	cz_ovr

;+++
; Double size over extended character drawing code
;---
cz_ovrcx
	sub.w	#20,a2			 ; reduce increment
	addq.l	#6,a5			 ; and move entry
	moveq	#$11,d1 		 ; eighteen rows
	move.w	#7,d3

cz_ovr
	move.w	d3,d0
	addq.w	#1,d0
	lsl.w	#3,d0
	add.w	d0,a4
	add.l	a2,a4

cz_ovrll
	move.b	(a3)+,d2
	bne.s	cz_ovrpx
	move.w	d3,d0
	addq.w	#1,d0
	lsl.w	#3,d0
	add.w	d0,a4
	add.l	a2,a4
	add.l	a2,a4
	subq.w	#1,d1
	bra.s	cz_ovrle

cz_ovrpx
cz_ovrllu
	add.l	a2,a4

	move.w	d3,d0
cz_ovrpl
	add.b	d2,d2
	bcc.s	cz_ovrpap
	move.w	d6,(a4)+		 ; ink point
	move.w	d7,(a4)+		 ; ink point
	dbra	d0,cz_ovrpl
	bra.s	cz_ovrls

cz_ovrpap
	addq.l	#4,a4
	dbra	d0,cz_ovrpl

cz_ovrls
	swap	d6
	swap	d7

	btst	#0,d1			 ; first line of pair?
	beq.s	cz_ovrle		 ; ... no
	subq.l	#1,a3			 ; yes backspace
cz_ovrle
	dbra	d1,cz_ovrll

	tst.l	d1			 ; underscore?
	bpl	cnsm_next		 ; ... no

	moveq	#-1,d2
	addq.l	#1,d1			 ; ... yes
	bne.s	cz_ovrllu		 ; ... genuine
	addq.l	#1,a3			 ; skip font line
	moveq	#1,d1
	bra.s	cz_ovrll

;+++
; Standard size xor underline character drawing code
;---
cc_xorul
	move.l	#$ffff0006,d1		 ; one + seven + two rows
	bra.s	cc_xorc1

;+++
; Standard size xor character drawing code
;---
cc_xorch
	moveq	#8,d1			 ; nine rows

cc_xorc1
	swap	d6
	swap	d7
	add.w	#12,a4

cc_xorll
	add.l	a2,a4
	move.b	(a3)+,d2
	bne.s	cc_xorpx
	add.w	#12,a4			 ; blank line
	bra.s	cc_xorls

cc_xorpx
	add.b	d2,d2
	bcc.s	cc_xorpap0
	eor.w	d6,(a4) 		 ; ink point
cc_xorpap0
	addq.l	#2,a4
cc_xorpx1
	add.b	d2,d2
	bcc.s	cc_xorpap1
	eor.w	d7,(a4) 		 ; ink point
cc_xorpap1
	addq.l	#2,a4

cc_xorpx2
	add.b	d2,d2
	bcc.s	cc_xorpap2
	eor.w	d6,(a4) 		 ; ink point
cc_xorpap2
	addq.l	#2,a4

cc_xorpx3
	add.b	d2,d2
	bcc.s	cc_xorpap3
	eor.w	d7,(a4) 		 ; ink point
cc_xorpap3
	addq.l	#2,a4

cc_xorpx4
	add.b	d2,d2
	bcc.s	cc_xorpap4
	eor.w	d6,(a4) 		 ; ink point
cc_xorpap4
	addq.l	#2,a4

cc_xorpx5
	add.b	d2,d2
	bcc.s	cc_xorpap5
	eor.w	d7,(a4) 		 ; ink point
cc_xorpap5
	addq.l	#2,a4

cc_xorls
	swap	d6
	swap	d7

	dbra	d1,cc_xorll

	tst.l	d1			 ; underscore?
	bpl	cnsm_next		 ; ... no

	add.l	a2,a4
	eor.w	d6,(a4)+		 ; the underscore
	eor.w	d7,(a4)+
	eor.w	d6,(a4)+		 ; the underscore
	eor.w	d7,(a4)+
	eor.w	d6,(a4)+		 ; the underscore
	eor.w	d7,(a4)+
	moveq	#1,d1
	addq.l	#1,a3			 ; skip font line
	bra.s	cc_xorls

;+++
; Standard size xor underline extended character drawing code
;---
cc_xorux
	subq.l	#4,a2			 ; reduce increment
	addq.l	#4,a5			 ; and move entry
	move.l	#$ffff0006,d1		 ; one + seven + two rows
	bra.s	cc_xorx

;+++
; Standard size xor extended character drawing code
;---
cc_xorcx
	subq.l	#4,a2			 ; reduce increment
	addq.l	#4,a5			 ; and move entry

	moveq	#8,d1			 ; one + nine rows
cc_xorx
	swap	d6
	swap	d7
	add.w	#16,a4

cc_xorxll
	add.l	a2,a4
	move.b	(a3)+,d2
	bne.s	cc_xorxpx
	add.w	#16,a4			 ; blank line
	bra.s	cc_xorxls

cc_xorxpx
	moveq	#3,d0
cc_xorxpl
	add.b	d2,d2
	bcc.s	cc_xorxpap0
	eor.w	d6,(a4) 		 ; ink point
cc_xorxpap0
	addq.l	#2,a4
	add.b	d2,d2
	bcc.s	cc_xorxpap1
	eor.w	d7,(a4) 		 ; ink point
cc_xorxpap1
	addq.l	#2,a4
	dbra	d0,cc_xorxpl

cc_xorxls
	swap	d6
	swap	d7

	dbra	d1,cc_xorxll

	tst.l	d1			 ; underscore?
	bpl	cnsm_next		 ; ... no

	add.l	a2,a4
	moveq	#3,d0
cc_xorxull
	eor.w	d6,(a4)+		 ; the underscore
	eor.w	d7,(a4)+
	dbra	d0,cc_xorxull

	moveq	#1,d1
	addq.l	#1,a3			 ; skip font line
	bra.s	cc_xorxls


;+++
; Double width xor underline character drawing code
;---
cw_xorul
	sub.w	#12,a2			 ; reduce increment
	addq.l	#6,a5			 ; and move entry
	move.l	#$ffff0006,d1		 ; one + seven + two rows
	move.w	#5,d3
	bra.s	cw_xor

;+++
; Double width xor character drawing code
;---
cw_xorch
	sub.w	#12,a2			 ; reduce increment
	addq.l	#6,a5			 ; and move entry
	moveq	#8,d1			 ; one + nine rows
	move.w	#5,d3
	bra.s	cw_xor

;+++
; Double width xor underline extended character drawing code
;---
cw_xorux
	sub.w	#20,a2			 ; reduce increment
	addq.l	#6,a5			 ; and move entry
	move.l	#$ffff0006,d1		 ; one + seven + two rows
	move.w	#7,d3
	bra.s	cw_xor

;+++
; Double width xor extended character drawing code
;---
cw_xorcx
	sub.w	#20,a2			 ; reduce increment
	addq.l	#6,a5			 ; and move entry
	moveq	#8,d1			 ; one + nine rows
	move.w	#7,d3

cw_xor
	swap	d6
	swap	d7
	move.w	d3,d0
	addq.w	#1,d0
	lsl.w	#2,d0
	add.w	d0,a4			 ; skip line

cw_xorll
	add.l	a2,a4
	move.b	(a3)+,d2
	bne.s	cw_xorpx
	move.w	d3,d0
	addq.w	#1,d0
	lsl.w	#2,d0
	add.w	d0,a4			 ; blank line
	bra.s	cw_xorls

cw_xorpx
	move.w	d3,d0
cw_xorpl
	add.b	d2,d2
	bcc.s	cw_xorpap
	eor.w	d6,(a4)+		 ; ink point
	eor.w	d7,(a4)+		 ; ink point
	dbra	d0,cw_xorpl
	bra.s	cw_xorls

cw_xorpap
	addq.l	#4,a4
	dbra	d0,cw_xorpl

cw_xorls
	swap	d6
	swap	d7

	dbra	d1,cw_xorll

	tst.l	d1			 ; underscore?
	bpl	cnsm_next		 ; ... no

	add.l	a2,a4
	move.w	d3,d0
cw_xorull
	eor.w	d6,(a4)+		 ; the underscore
	eor.w	d7,(a4)+
	dbra	d0,cw_xorull

	moveq	#1,d1
	addq.l	#1,a3			 ; skip font line
	bra.s	cw_xorls

;+++
; Double height xor underline character drawing code
;---
ch_xorul
	move.l	#$fffd000d,d1		 ; two + fourteen + four rows
	move.w	#2,d3
	bra.s	ch_xor

;+++
; Double height xor character drawing code
;---
ch_xorch
	moveq	#$11,d1 		 ; eighteen rows
	move.w	#2,d3
	bra.s	ch_xor

;+++
; Double height xor underline extended character drawing code
;---
ch_xorux
	subq.l	#4,a2			 ; reduce increment
	addq.l	#4,a5			 ; and move entry

	move.l	#$fffd000d,d1		 ; two + fourteen + four rows
	move.w	#3,d3
	bra.s	ch_xor

;+++
; Double height xor extended character drawing code
;---
ch_xorcx
	subq.l	#4,a2			 ; reduce increment
	addq.l	#4,a5			 ; and move entry

	moveq	#$11,d1 		 ; eighteen rows
	move.w	#3,d3

ch_xor
	move.w	d3,d0			 ; skip two lines
	addq.w	#1,d0
	lsl.w	#3,d0
	add.w	d0,a4
	add.l	a2,a4

ch_xorll
	move.b	(a3)+,d2
	bne.s	ch_xorpx
	move.w	d3,d0			 ; two blank lines
	addq.w	#1,d0
	lsl.w	#3,d0
	add.w	d0,a4
	add.l	a2,a4
	add.l	a2,a4
	subq.w	#1,d1
	bra.s	ch_xorle

ch_xorpx
ch_xorllu
	add.l	a2,a4

	move.w	d3,d0
ch_xorpl
	add.b	d2,d2
	bcc.s	ch_xorpap0
	eor.w	d6,(a4) 		 ; ink point
ch_xorpap0
	addq.l	#2,a4
	add.b	d2,d2
	bcc.s	ch_xorpap1
	eor.w	d7,(a4) 		 ; ink point
ch_xorpap1
	addq.l	#2,a4
	dbra	d0,ch_xorpl

ch_xorls
	swap	d6
	swap	d7

	btst	#0,d1			 ; first line of pair?
	beq.s	ch_xorle		 ; ... no
	subq.l	#1,a3			 ; yes backspace
ch_xorle
	dbra	d1,ch_xorll

	tst.l	d1			 ; underscore?
	bpl	cnsm_next		 ; ... no

	moveq	#-1,d2
	addq.l	#1,d1			 ; ... yes
	bne.s	ch_xorllu		 ; genuine
	addq.l	#1,a3			 ; skip font line
	moveq	#1,d1
	bra.s	ch_xorll


;+++
; Double size xor underline character drawing code
;---
cz_xorul
	sub.w	#12,a2			 ; reduce increment
	addq.l	#6,a5			 ; and move entry
	move.l	#$fffd000d,d1		 ; two + fourteen + four rows
	move.w	#5,d3
	bra.s	cz_xor

;+++
; Double size xor character drawing code
;---
cz_xorch
	sub.w	#12,a2			 ; reduce increment
	addq.l	#6,a5			 ; and move entry
	moveq	#$11,d1 		 ; eighteen rows
	move.w	#5,d3
	bra.s	cz_xor

;+++
; Double size xor underline extended character drawing code
;---
cz_xorux
	sub.w	#20,a2			 ; reduce increment
	addq.l	#6,a5			 ; and move entry
	move.l	#$fffd000d,d1		 ; two + fourteen + four rows
	move.w	#7,d3
	bra.s	cz_xor

;+++
; Double size xor extended character drawing code
;---
cz_xorcx
	sub.w	#20,a2			 ; reduce increment
	addq.l	#6,a5			 ; and move entry
	moveq	#$11,d1 		 ; eighteen rows
	move.w	#7,d3

cz_xor
	move.w	d3,d0
	addq.w	#1,d0
	lsl.w	#3,d0
	add.w	d0,a4
	add.l	a2,a4

cz_xorll
	move.b	(a3)+,d2
	bne.s	cz_xorpx
	move.w	d3,d0
	addq.w	#1,d0
	lsl.w	#3,d0
	add.w	d0,a4
	add.l	a2,a4
	add.l	a2,a4
	subq.w	#1,d1
	bra.s	cz_xorle

cz_xorpx
cz_xorllu
	add.l	a2,a4

	move.w	d3,d0
cz_xorpl
	add.b	d2,d2
	bcc.s	cz_xorpap
	eor.w	d6,(a4)+		 ; ink point
	eor.w	d7,(a4)+		 ; ink point
	dbra	d0,cz_xorpl
	bra.s	cz_xorls

cz_xorpap
	addq.l	#4,a4
	dbra	d0,cz_xorpl

cz_xorls
	swap	d6
	swap	d7

	btst	#0,d1			 ; first line of pair?
	beq.s	cz_xorle		 ; ... no
	subq.l	#1,a3			 ; yes backspace
cz_xorle
	dbra	d1,cz_xorll

	tst.l	d1			 ; underscore?
	bpl	cnsm_next		 ; ... no

	moveq	#-1,d2
	addq.l	#1,d1			 ; ... yes
	bne.s	cz_xorllu		 ; ... genuine
	addq.l	#1,a3			 ; skip font line
	moveq	#1,d1
	bra.s	cz_xorll

pp.reg	reg	d0-d7/a1
pp.d4	equ	16
pp.d5	equ	20
pp.d6	equ	24
pp.d7	equ	28

; Put background colour d4
put_pixel_d4
	btst	#sd..over,sd_cattr(a0)
	bne.s	pp_rts			 ; OVER mode, don't draw background
	movem.l pp.reg,-(sp)
pp_all
	move.b	sd_alpha(a0),d5
	move.l	a4,a1
	jsr	bm_apixel
	movem.l (sp)+,pp.reg
pp_rts
	addq.w	#2,a4
	rts

; Put background colour d5
put_pixel_d5
	btst	#sd..over,sd_cattr(a0)
	bne.s	pp_rts			 ; OVER mode, don't draw background
	movem.l pp.reg,-(sp)
	move.w	d5,d4
	bra.s	pp_all

; Put ink colour d6
put_pixel_d6
	movem.l pp.reg,-(sp)
	move.w	d6,d4
	bra.s	pp_all

; Put ink colour d7
put_pixel_d7
	movem.l pp.reg,-(sp)
	move.w	d7,d4
	bra.s	pp_all

;+++
; Standard size (over) underline alpha character drawing code
;---
cc_alpul
	move.l	#$ffff0006,d1		 ; one + seven + two rows
	bra.s	cc_alpc1

;+++
; Standard size / attribute (over) alpha character drawing code
;---
cc_alpch
	moveq	#8,d1			 ; nine rows

cc_alpc1
	bsr.s	put_pixel_d4		 ; paper ink
	bsr.s	put_pixel_d5
	bsr.s	put_pixel_d4
	bsr.s	put_pixel_d5
	bsr.s	put_pixel_d4
	bsr.s	put_pixel_d5

	swap	d4
	swap	d5
	swap	d6
	swap	d7

cc_alpll
	add.l	a2,a4
	move.b	(a3)+,d2
	moveq	#2,d3
cc_alppxl
	add.b	d2,d2
	bcc.s	cc_alppap0
	bsr	put_pixel_d6		 ; ink point
	bra.s	cc_alppx1
cc_alppap0
	bsr	put_pixel_d4		 ; paper point
cc_alppx1
	add.b	d2,d2
	bcc.s	cc_alppap1
	bsr	put_pixel_d7		 ; ink point
	bra.s	cc_alppx2
cc_alppap1
	bsr	put_pixel_d5		 ; paper point
cc_alppx2
	dbf	d3,cc_alppxl

cc_alpls
	swap	d4
	swap	d5
	swap	d6
	swap	d7

	dbra	d1,cc_alpll

	tst.l	d1			 ; underscore?
	bpl	cnsm_next		 ; ... no

	add.l	a2,a4
	bsr.s	put_pixel_d6		 ; the underscore
	bsr.s	put_pixel_d7
	bsr.s	put_pixel_d6
	bsr.s	put_pixel_d7
	bsr.s	put_pixel_d6
	bsr.s	put_pixel_d7

	moveq	#1,d1
	addq.l	#1,a3			 ; skip font line
	bra.s	cc_alpls

;+++
; Standard size (over) underline extended alpha character drawing code
;---
cc_alpux
	subq.l	#4,a2			 ; reduce increment
	addq.l	#4,a5			 ; and move entry
	move.l	#$ffff0006,d1		 ; one + seven + two rows
	bra.s	cc_alpx

;+++
; Standard size (over) extended alpha character drawing code
;---
cc_alpcx
	subq.l	#4,a2			 ; reduce increment
	addq.l	#4,a5			 ; and move entry

	moveq	#8,d1			 ; one + nine rows
cc_alpx
	moveq	#3,d0
cc_alpxbl
	bsr	put_pixel_d4		 ; the blank line
	bsr	put_pixel_d5
	dbra	d0,cc_alpxbl

	swap	d4
	swap	d5
	swap	d6
	swap	d7

cc_alpxll
	add.l	a2,a4
	move.b	(a3)+,d2

	moveq	#3,d0
cc_alpxpl
	add.b	d2,d2
	bcs.s	cc_alpxink0
	bsr	put_pixel_d4		 ; paper point
	add.b	d2,d2
	bcs.s	cc_alpxink1
	bsr	put_pixel_d5		 ; paper point
	dbra	d0,cc_alpxpl
	bra.s	cc_alpxls

cc_alpxink0
	bsr	put_pixel_d6		 ; ink point
	add.b	d2,d2
	bcs.s	cc_alpxink1
	bsr	put_pixel_d5		 ; paper point
	dbra	d0,cc_alpxpl
	bra.s	cc_alpxls

cc_alpxink1
	bsr	put_pixel_d7		 ; ink point
	dbra	d0,cc_alpxpl

cc_alpxls
	swap	d4
	swap	d5
	swap	d6
	swap	d7

	dbra	d1,cc_alpxll

	tst.l	d1			 ; underscore?
	bpl	cnsm_next		 ; ... no

	add.l	a2,a4
	moveq	#3,d0
cc_alpxull
	bsr	put_pixel_d6		 ; the underscore
	bsr	put_pixel_d7
	dbra	d0,cc_alpxull

	moveq	#1,d1
	addq.l	#1,a3			 ; skip font line
	bra.s	cc_alpxls

;+++
; Double width (over) underline character drawing code
;---
cw_alpul
	sub.w	#12,a2			 ; reduce increment
	addq.l	#6,a5			 ; and move entry
	move.l	#$ffff0006,d1		 ; one + seven + two rows
	move.w	#5,d3
	bra.s	cw_alp

;+++
; Double width (over) character drawing code
;---
cw_alpch
	sub.w	#12,a2			 ; reduce increment
	addq.l	#6,a5			 ; and move entry
	moveq	#8,d1			 ; one + nine rows
	move.w	#5,d3
	bra.s	cw_alp

;+++
; Double width (over) underline extended character drawing code
;---
cw_alpux
	sub.w	#20,a2			 ; reduce increment
	addq.l	#6,a5			 ; and move entry
	move.l	#$ffff0006,d1		 ; one + seven + two rows
	move.w	#7,d3
	bra.s	cw_alp

;+++
; Double width (over) extended character drawing code
;---
cw_alpcx
	sub.w	#20,a2			 ; reduce increment
	addq.l	#6,a5			 ; and move entry
	moveq	#8,d1			 ; one + nine rows
	move.w	#7,d3

cw_alp
	move.w	d3,d0
cw_alpbl
	bsr	put_pixel_d4		 ; the blank line
	bsr	put_pixel_d5
	dbra	d0,cw_alpbl

	swap	d4
	swap	d5
	swap	d6
	swap	d7

cw_alpll
	add.l	a2,a4
	move.b	(a3)+,d2

	move.w	d3,d0
cw_alppl
	add.b	d2,d2
	bcc.s	cw_alppap
	bsr	put_pixel_d6		 ; ink point
	bsr	put_pixel_d7		 ; ink point
	dbra	d0,cw_alppl
	bra.s	cw_alpls

cw_alppap
	bsr	put_pixel_d4		 ; paper point
	bsr	put_pixel_d5		 ; paper point
	dbra	d0,cw_alppl

cw_alpls
	swap	d4
	swap	d5
	swap	d6
	swap	d7

	dbra	d1,cw_alpll

	tst.l	d1			 ; underscore?
	bpl	cnsm_next		 ; ... no

	add.l	a2,a4
	move.w	d3,d0
cw_alpull
	bsr	put_pixel_d6		 ; the underscore
	bsr	put_pixel_d7
	dbra	d0,cw_alpull

	moveq	#1,d1
	addq.l	#1,a3			 ; skip font line
	bra.s	cw_alpls

;+++
; Double height underline character drawing code
;---
ch_alpul
	move.l	#$fffd000d,d1		 ; two + fourteen + four rows
	move.w	#2,d3
	bra.s	ch_alp

;+++
; Double height (over) character drawing code
;---
ch_alpch
	moveq	#$11,d1 		 ; eighteen rows
	move.w	#2,d3
	bra.s	ch_alp

;+++
; Double height (over) underline extended character drawing code
;---
ch_alpux
	subq.l	#4,a2			 ; reduce increment
	addq.l	#4,a5			 ; and move entry

	move.l	#$fffd000d,d1		 ; two + fourteen + four rows
	move.w	#3,d3
	bra.s	ch_alp

;+++
; Double height (over) extended character drawing code
;---
ch_alpcx
	subq.l	#4,a2			 ; reduce increment
	addq.l	#4,a5			 ; and move entry

	moveq	#$11,d1 		 ; eighteen rows
	move.w	#3,d3

ch_alp
	move.w	d3,d0
ch_alpbl
	bsr	put_pixel_d4		 ; the blank line
	bsr	put_pixel_d5
	dbra	d0,ch_alpbl

	swap	d4
	swap	d5
	swap	d6
	swap	d7

	add.l	a2,a4
	move.w	d3,d0
ch_alpbl1
	bsr	put_pixel_d4		 ; the second blank line
	bsr	put_pixel_d5
	dbra	d0,ch_alpbl1

	swap	d4
	swap	d5
	swap	d6
	swap	d7

ch_alpll
	move.b	(a3)+,d2
ch_alpllu
	add.l	a2,a4

	move.w	d3,d0
ch_alppl
	add.b	d2,d2
	bcs.s	ch_alpink0
	bsr	put_pixel_d4		 ; paper point
	add.b	d2,d2
	bcs.s	ch_alpink1
	bsr	put_pixel_d5		 ; paper point
	dbra	d0,ch_alppl
	bra.s	ch_alpls

ch_alpink0
	bsr	put_pixel_d6		 ; ink point
	add.b	d2,d2
	bcs.s	ch_alpink1
	bsr	put_pixel_d5		 ; paper point
	dbra	d0,ch_alppl
	bra.s	ch_alpls

ch_alpink1
	bsr	put_pixel_d7		 ; ink point
	dbra	d0,ch_alppl

ch_alpls
	swap	d4
	swap	d5
	swap	d6
	swap	d7

	btst	#0,d1			 ; first line of pair?
	beq.s	ch_alple		 ; ... no
	subq.l	#1,a3			 ; yes backspace
ch_alple
	dbra	d1,ch_alpll

	tst.l	d1			 ; underscore?
	bpl	cnsm_next		 ; ... no

	moveq	#-1,d2
	addq.l	#1,d1			 ; ... yes
	bne.s	ch_alpllu		 ; genuine
	addq.l	#1,a3			 ; skip font line
	moveq	#1,d1
	bra.s	ch_alpll

;+++
; Double size (over) underline character drawing code
;---
cz_alpul
	sub.w	#12,a2			 ; reduce increment
	addq.l	#6,a5			 ; and move entry
	move.l	#$fffd000d,d1		 ; two + fourteen + four rows
	move.w	#5,d3
	bra.s	cz_alp

;+++
; Double size (over) character drawing code
;---
cz_alpch
	sub.w	#12,a2			 ; reduce increment
	addq.l	#6,a5			 ; and move entry
	moveq	#$11,d1 		 ; eighteen rows
	move.w	#5,d3
	bra.s	cz_alp

;+++
; Double size (over) underline extended character drawing code
;---
cz_alpux
	sub.w	#20,a2			 ; reduce increment
	addq.l	#6,a5			 ; and move entry
	move.l	#$fffd000d,d1		 ; two + fourteen + four rows
	move.w	#7,d3
	bra.s	cz_alp

;+++
; Double size (over) extended character drawing code
;---
cz_alpcx
	sub.w	#20,a2			 ; reduce increment
	addq.l	#6,a5			 ; and move entry
	moveq	#$11,d1 		 ; eighteen rows
	move.w	#7,d3

cz_alp
	move.w	d3,d0
cz_alpbl
	bsr	put_pixel_d4		 ; the blank line
	bsr	put_pixel_d5
	dbra	d0,cz_alpbl

	swap	d4
	swap	d5
	swap	d6
	swap	d7

	add.l	a2,a4
	move.w	d3,d0
cz_alpbl1
	bsr	put_pixel_d4		 ; the blank line
	bsr	put_pixel_d5
	dbra	d0,cz_alpbl1

	swap	d4
	swap	d5
	swap	d6
	swap	d7


cz_alpll
	move.b	(a3)+,d2
cz_alpllu
	add.l	a2,a4

	move.w	d3,d0
cz_alppl
	add.b	d2,d2
	bcc.s	cz_alppap
	bsr	put_pixel_d6		 ; ink point
	bsr	put_pixel_d7		 ; ink point
	dbra	d0,cz_alppl
	bra.s	cz_alpls

cz_alppap
	bsr	put_pixel_d4		 ; paper point
	bsr	put_pixel_d5		 ; paper point
	dbra	d0,cz_alppl

cz_alpls
	swap	d4
	swap	d5
	swap	d6
	swap	d7

	btst	#0,d1			 ; first line of pair?
	beq.s	cz_alple		 ; ... no
	subq.l	#1,a3			 ; yes backspace
cz_alple
	dbra	d1,cz_alpll

	tst.l	d1			 ; underscore?
	bpl	cnsm_next		 ; ... no

	moveq	#-1,d2
	addq.l	#1,d1			 ; ... yes
	bne.s	cz_alpllu		 ; ... genuine
	addq.l	#1,a3			 ; skip font line
	moveq	#1,d1
	bra.s	cz_alpll

	end
